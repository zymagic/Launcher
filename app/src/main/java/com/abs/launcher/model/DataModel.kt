package com.abs.launcher.model

/**
 * Created by zy on 17-12-17.
 */
class DataModel {
    var allApplications: MutableList<AppInfo> = ArrayList()
    var allHomeItems: MutableList<out HomeItemInfo> = ArrayList()

    var allAppWidgets: MutableList<AppWidgetInfo> = ArrayList()
    var allWidgetViews: MutableList<WidgetViewInfo> = ArrayList()
    var allFolders: MutableList<FolderInfo> = ArrayList()
    var folderMap: MutableMap<Long, FolderInfo> = HashMap()
}