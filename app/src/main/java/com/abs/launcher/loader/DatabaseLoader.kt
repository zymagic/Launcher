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
import com.abs.launcher.util.runOnUiThread
import com.abs.launcher.util.safe

/**
 * Created by zy on 17-12-18.
 */
class DatabaseLoader(private val context: Context, private val model: LauncherModel, var loadDesktop: Boolean = true) {
    var applications: MutableList<AppInfo> = ArrayList()
    var shortcuts: MutableList<HomeAppInfo> = ArrayList()
    var widgetViews: MutableList<WidgetViewInfo> = ArrayList()
    var appWidgets: MutableList<AppWidgetInfo> = ArrayList()
    var folders: MutableMap<Long, FolderInfo> = HashMap()

    fun load() {
        loadApplications()
        loadDockbar()
        loadDesktop()
        loadFolderItems()
        var data = model.data
        data.clear()
        data.allApplications.addAll(applications)
        data.allShortcuts.addAll(shortcuts)
        data.allWidgetViews.addAll(widgetViews)
        data.allAppWidgets.addAll(appWidgets)
        data.folderMap.putAll(folders)
    }

    private fun loadDesktop() {
        var defaultScreen = getDefaultHomeScreen()
        var screenCount = getHomeScreenCount()
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
        var selection = when (container) {
            CONTAINER_DESKTOP -> "$SCREEN = $screen and $CONTAINER = $container"
            CONTAINER_DOCKBAR -> "$CONTAINER = $container"
            else -> "$CONTAINER >= 0"
        }
        var items = ArrayList<HomeItemInfo>()
        var cursor = context.contentResolver.query(Favorites.getUri(), null, selection, null, null)
        var index = FavoriteIndex(cursor)
        loadItem@ while (cursor.moveToNext()) {
            safe {
                var intent = Intent.parseUri(cursor.getString(index.intentIndex), 0)
                var itemType = cursor.getInt(index.itemTypeIndex)
                when (itemType) {
                    ITEM_TYPE_APPLICATION, ITEM_TYPE_SHORTCUT -> {
                        var app = applications.find { it.intent == intent }
                        if (app != null) {
                            var homeApp = HomeAppInfo(app).apply { commonLoadFromCursor(cursor, index) }
                            shortcuts.add(homeApp)
                            items.add(homeApp)
                            if (homeApp.position.container >= 0) {
                                getFolder(homeApp.position.container).add(homeApp)
                            }
                        }
                    }
                    ITEM_TYPE_APP_WIDGET -> {
                        var widgetId = cursor.getInt(index.widgetIdIndex)
                        var widget = AppWidgetInfo(widgetId).apply { commonLoadFromCursor(cursor, index) }
                        appWidgets.add(widget)
                        items.add(widget)
                    }
                    ITEM_TYPE_FOLDER -> {
                        var folder = FolderInfo().apply { commonLoadFromCursor(cursor, index) }
                        folders.put(folder.id, folder)
                        items.add(folder)
                    }
                }
            }
        }
        if (!items.isEmpty()) {
            var cbk = model.callbackRef?.get()
            runOnUiThread {
                cbk?.bindItemLoaded(items)
            }
        }
    }

    private fun loadApplications() {
        var cursor = context.contentResolver.query(Applications.getUri(), null, null, null, null)
        var index = ApplicationIndex(cursor)
        while (cursor.moveToNext()) {
            safe {
                var id = cursor.getLong(index.idIndex)
                var title = cursor.getString(index.titleIndex)
                var intent = Intent.parseUri(cursor.getString(index.intentIndex), 0)
                var icon = cursor.getString(index.iconIndex)

                var app = AppInfo(title, IconCache.defaultIcon, intent)
                app.id = id
                app.iconResource = icon
                app.category = cursor.getInt(index.categoryIndex)
                app.storage = cursor.getInt(index.storageIndex)
                app.isSystem = cursor.getInt(index.storageIndex) == 1

                applications.add(app)
            }
        }
        var cbk = model.callbackRef?.get()
        runOnUiThread {
            cbk?.bindAppLoaded(applications)
        }
    }

    fun HomeItemInfo.commonLoadFromCursor(cursor: Cursor, index: FavoriteIndex) {
        id = cursor.getLong(index.idIndex)
        var cellX = cursor.getInt(index.cellXIndex)
        var cellY = cursor.getInt(index.cellYIndex)
        var spanX = cursor.getInt(index.spanXIndex)
        var spanY = cursor.getInt(index.spanYIndex)
        var container = cursor.getLong(index.containerIndex)
        var screen = cursor.getInt(index.screenIndex)
        position = Position(container, screen, cellX, cellY, spanX, spanY)
        category = cursor.getInt(index.categoryIndex)
    }

    fun getFolder(id: Long) : FolderInfo {
        return folders[id] ?: FolderInfo().apply { this.id = id; folders[id] = this }
    }
}