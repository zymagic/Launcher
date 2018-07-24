package com.abs.launcher.loader

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import com.abs.launcher.LauncherModel
import com.abs.launcher.model.AppInfo
import com.abs.launcher.util.runOnUiThread

/**
 * Created by zy on 18-1-7.
 */
class SyncLoader(private val context: Context, val model: LauncherModel) {

    var added: MutableList<AppInfo> = ArrayList()
    var updated: MutableList<AppInfo> = ArrayList()
    var removed: MutableList<AppInfo> = ArrayList()

    fun sync() {
        syncApps()
        syncFolders()
    }

    private fun syncApps() {
        var pm = context.packageManager
        var loadedApps = ArrayList(model.data.allApplications)
        var intent = Intent(Intent.ACTION_MAIN).apply { this.addCategory(Intent.CATEGORY_LAUNCHER) }
        var resolveInfos = pm.queryIntentActivities(intent, 0)
        for (ri in resolveInfos) {
            var cn = ComponentName(ri.activityInfo.packageName, ri.activityInfo.name)
            var loadedApp = model.data.getApp(cn)
            if (loadedApp != null) {
                loadedApps.remove(loadedApp)
                var update = false
                var title = ri.loadLabel(pm).toString()
                if (title != loadedApp.title) {
                    loadedApp.title = title
                    update = true
                }
                var system = (ri.activityInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
                if (system != loadedApp.isSystem) {
                    loadedApp.isSystem = system
                    update = true
                }
                //TODO check storage update
                if (update) {
                    updated.add(loadedApp)
                }
            } else {
                var intent = Intent().apply { component = cn }
                var title = ri.loadLabel(pm)
                var icon = ri.loadIcon(pm)
                var appInfo = AppInfo(title.toString(), icon, intent)
                added.add(appInfo)
            }
        }
        removed.addAll(loadedApps)

        var cbk = model.callbackRef?.get()
        if (cbk != null) {
            runOnUiThread {
                cbk.bindAppAdded(added)
            }
            runOnUiThread {
                cbk.bindAppUpdated(updated)
            }
            var updatedHomeApps = updated.mapNotNull { model.data.getHomeAppInfo(it) }
            runOnUiThread {
                cbk.bindItemUpdated(updatedHomeApps)
            }
            runOnUiThread {
                cbk.bindAppRemoved(removed)
            }
        }
    }

    private fun syncFolders() {
        var folderToRemove = ArrayList(model.data.folderMap.filterValues { !it.isEmpty() }.values)
        if (folderToRemove.isEmpty()) {
            return
        }
        for (folder in folderToRemove) {
            model.data.folderMap.remove(folder.id)
        }
        var cbk = model.callbackRef?.get()
        if (cbk != null) {
            runOnUiThread {
                cbk.bindItemRemoved(folderToRemove)
            }
        }
    }
}