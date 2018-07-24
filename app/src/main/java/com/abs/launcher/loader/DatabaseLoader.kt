package com.abs.launcher.loader

import android.content.Context
import android.content.Intent
import android.database.Cursor
import com.abs.launcher.IconCache
import com.abs.launcher.LauncherModel
import com.abs.launcher.getDefaultHomeScreen
import com.abs.launcher.getHomeScreenCount
import com.abs.launcher.model.*
import com.abs.launcher.model.db.*
import com.abs.launcher.util.manage
import com.abs.launcher.util.runOnUiThread
import com.abs.launcher.util.safe

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
        context.contentResolver.query(Favorites.getUri(), null, selection, null, null)
            .manage {
                val index = FavoriteIndex(this)
                loadItem@ while (moveToNext()) {
                    safe {
                        val intent = Intent.parseUri(getString(index.intentIndex), 0)
                        val itemType = getInt(index.itemTypeIndex)
                        when (itemType) {
                            ITEM_TYPE_APPLICATION, ITEM_TYPE_SHORTCUT -> {
                                val app = applications.find { it.intent == intent }
                                if (app != null) {
                                    val homeApp = HomeAppInfo(app).apply { commonLoadFromCursor(this@manage, index) }
                                    shortcuts.add(homeApp)
                                    items.add(homeApp)
                                    if (homeApp.position.container >= 0) {
                                        getFolder(homeApp.position.container).add(homeApp)
                                    }
                                }
                            }
                            ITEM_TYPE_APP_WIDGET -> {
                                val widgetId = getInt(index.widgetIdIndex)
                                val widget = AppWidgetInfo(widgetId).apply { commonLoadFromCursor(this@manage, index) }
                                appWidgets.add(widget)
                                items.add(widget)
                            }
                            ITEM_TYPE_FOLDER -> {
                                val folder = FolderInfo().apply { commonLoadFromCursor(this@manage, index) }
                                folders[folder.id] = folder
                                items.add(folder)
                            }
                        }
                    }
                }
            }

        if (!items.isEmpty()) {
            val cbk = model.callbackRef?.get()
            runOnUiThread {
                val actualCallback = model.callbackRef?.get()
                if (actualCallback == cbk) {
                    cbk?.bindItemLoaded(items)
                }
            }
        }
    }

    private fun loadApplications() {
        context.contentResolver.query(Applications.getUri(), null, null, null, null)
            .manage {
                val index = ApplicationIndex(this)
                while (moveToNext()) {
                    safe {
                        val id = getLong(index.idIndex)
                        val title = getString(index.titleIndex)
                        val intent = Intent.parseUri(getString(index.intentIndex), 0)
                        val icon = getString(index.iconIndex)

                        val app = AppInfo(title, IconCache.defaultIcon, intent)
                        app.id = id
                        app.iconResource = IconResource(icon)
                        app.category = getInt(index.categoryIndex)
                        app.storage = getInt(index.storageIndex)
                        app.isSystem = getInt(index.storageIndex) == 1

                        applications.add(app)
                    }
                }
            }

        val cbk = model.callbackRef?.get()
        runOnUiThread {
            val actualCbk = model.callbackRef?.get()
            if (cbk == actualCbk) {
                cbk?.bindAppLoaded(applications)
            }
        }
    }

    private fun HomeItemInfo.commonLoadFromCursor(cursor: Cursor, index: FavoriteIndex) {
        id = cursor.getLong(index.idIndex)
        val cellX = cursor.getInt(index.cellXIndex)
        val cellY = cursor.getInt(index.cellYIndex)
        val spanX = cursor.getInt(index.spanXIndex)
        val spanY = cursor.getInt(index.spanYIndex)
        val container = cursor.getLong(index.containerIndex)
        val screen = cursor.getInt(index.screenIndex)
        position = Position(container, screen, cellX, cellY, spanX, spanY)
        category = cursor.getInt(index.categoryIndex)
    }

    private fun getFolder(id: Long) : FolderInfo {
        return folders[id] ?: FolderInfo().apply { this.id = id; folders[id] = this }
    }
}