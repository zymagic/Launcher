package com.abs.launcher.loader

import android.content.Context
import com.abs.launcher.IconCache
import com.abs.launcher.LauncherModel
import com.abs.launcher.getDefaultHomeScreen
import com.abs.launcher.getHomeScreenCount
import com.abs.launcher.model.*
import com.abs.launcher.model.db.*
import com.zy.kotlinutils.core.db.manage
import com.zy.kotlinutils.core.db.map
import com.zy.kotlinutils.core.db.parseObject
import com.zy.kotlinutils.core.safe
import com.zy.kotlinutils.core.uiThread

/**
 * Created by zy on 17-12-18.
 */
class DatabaseLoader(private val context: Context, private val model: LauncherModel, var loadDesktop: Boolean = true) {
    private val applications: MutableList<AppInfo> = ArrayList()
    private val shortcuts: MutableList<HomeAppInfo> = ArrayList()
    private val widgetViews: MutableList<WidgetViewInfo> = ArrayList()
    private val appWidgets: MutableList<AppWidgetInfo> = ArrayList()
    private val folders: MutableMap<Long, FolderInfo> = HashMap()

    fun load() {
        loadApplications()
        if (loadDesktop) {
            loadDockbar()
            loadDesktop()
            loadFolderItems()
        }
        val data = model.data
        data.clear()
        data.allApplications.addAll(applications)
        data.allShortcuts.addAll(shortcuts)
        data.allWidgetViews.addAll(widgetViews)
        data.allAppWidgets.addAll(appWidgets)
        data.folderMap.putAll(folders)
    }

    private fun loadDesktop() {
        val defaultScreen = getDefaultHomeScreen()
        val screenCount = getHomeScreenCount()
        var leftIndex = defaultScreen - 1
        var rightIndex = defaultScreen + 1
        loadItem(defaultScreen, CONTAINER_DESKTOP)
        while (leftIndex >= 0 || rightIndex < screenCount) {
            if (leftIndex >= 0) {
                loadItem(leftIndex, CONTAINER_DESKTOP)
                leftIndex--
            }
            if (rightIndex < screenCount) {
                loadItem(leftIndex, CONTAINER_DESKTOP)
                rightIndex++
            }
        }
    }

    private fun loadDockbar() {
        loadItem(-1, CONTAINER_DOCKBAR)
    }

    private fun loadFolderItems() {
        loadItem(0, 0)
    }

    private fun loadItem(screen: Int, container: Int) {
        val selection = when (container) {
            CONTAINER_DESKTOP -> "$SCREEN = $screen and $CONTAINER = $container"
            CONTAINER_DOCKBAR -> "$CONTAINER = $container"
            else -> "$CONTAINER >= 0"
        }
        val items = ArrayList<HomeItemInfo>()
        context.contentResolver.query(uri<Favorites>(), null, selection, null, null)
            .manage {
                map {
                    parseObject<Favorites>()
                }.forEach {
                    safe {
                        val intent = it.intent
                        val itemType = it.itemType
                        when (itemType) {
                            ITEM_TYPE_APPLICATION, ITEM_TYPE_SHORTCUT -> {
                                val app = applications.find { it.intent == intent }
                                if (app != null) {
                                    val homeApp = HomeAppInfo(app).apply { commonLoadFromCursor(it) }
                                    shortcuts.add(homeApp)
                                    items.add(homeApp)
                                    if (homeApp.position.container >= 0) {
                                        getFolder(homeApp.position.container).add(homeApp)
                                    }
                                }
                            }
                            ITEM_TYPE_APP_WIDGET -> {
                                val widgetId = it.widgetId
                                val widget = AppWidgetInfo(widgetId).apply { commonLoadFromCursor(it) }
                                appWidgets.add(widget)
                                items.add(widget)
                            }
                            ITEM_TYPE_FOLDER -> {
                                val folder = FolderInfo().apply { commonLoadFromCursor(it) }
                                folders[folder.id] = folder
                                items.add(folder)
                            }
                        }
                    }
                }
            }

        if (!items.isEmpty()) {
            val cbk = model.callbackRef?.get()
            uiThread {
                val actualCallback = model.callbackRef?.get()
                if (actualCallback == cbk) {
                    cbk?.bindItemLoaded(items)
                }
            }
        }
    }

    private fun loadApplications() {
        context.contentResolver.query(uri<Applications>(), null, null, null, null)
            .manage {
                map {
                    parseObject<Applications>()
                }.forEach {
                    safe {
                        val id = it.id
                        val title = it.title!!
                        val intent = it.intent!!
                        val icon = it.icon!!

                        val app = AppInfo(title, IconCache.defaultIcon, intent)
                        app.id = id
                        app.iconResource = IconResource(icon)
                        app.category = it.category
                        app.storage = it.storage
                        app.isSystem = it.system

                        applications.add(app)
                    }
                }
            }

        val cbk = model.callbackRef?.get()
        uiThread {
            val actualCbk = model.callbackRef?.get()
            if (cbk == actualCbk) {
                cbk?.bindAppLoaded(applications)
            }
        }
    }

    private fun HomeItemInfo.commonLoadFromCursor(favorites: Favorites) {
        id = favorites.id
        val cellX = favorites.cellX
        val cellY = favorites.cellY
        val spanX = favorites.spanX
        val spanY = favorites.spanY
        val container = favorites.container
        val screen = favorites.screen
        position = Position(container, screen, cellX, cellY, spanX, spanY)
        category = favorites.category
    }

    private fun getFolder(id: Long) : FolderInfo {
        return folders[id] ?: FolderInfo().apply { this.id = id; folders[id] = this }
    }
}