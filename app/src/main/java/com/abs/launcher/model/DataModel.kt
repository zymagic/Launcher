package com.abs.launcher.model

import android.content.ComponentName
import android.content.Intent

/**
 * Created by zy on 17-12-17.
 */
class DataModel {
    val allApplications: MutableList<AppInfo> = ArrayList()

    val allShortcuts: MutableList<HomeAppInfo> = ArrayList()
    val allAppWidgets: MutableList<AppWidgetInfo> = ArrayList()
    val allWidgetViews: MutableList<WidgetViewInfo> = ArrayList()
    val folderMap: MutableMap<Long, FolderInfo> = HashMap()

    fun clear() {
        allApplications.clear()
        allShortcuts.clear()
        allAppWidgets.clear()
        allWidgetViews.clear()
        folderMap.clear()
    }

    fun getApp(intent: Intent) : AppInfo? {
        return allApplications.find { it.intent.component == intent.component }
    }

    fun getApp(component: ComponentName) : AppInfo? {
        return allApplications.find { it.intent.component == component }
    }

    fun getHomeAppInfo(info: AppInfo) : HomeAppInfo? {
        return allShortcuts.find { it.appInfo == info }
    }
}