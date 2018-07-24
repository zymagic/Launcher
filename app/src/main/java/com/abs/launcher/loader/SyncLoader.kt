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

    private val added: MutableList<AppInfo> = ArrayList()
    private val updated: MutableList<AppInfo> = ArrayList()
    private val removed: MutableList<AppInfo> = ArrayList()

    fun sync() {
        syncApps()
        syncFolders()
    }

    private fun syncApps() {
        val pm = context.packageManager
        val loadedApps = ArrayList(model.data.allApplications)
        val resolveIntent = Intent(Intent.ACTION_MAIN).apply { this.addCategory(Intent.CATEGORY_LAUNCHER) }
        val resolveInfos = pm.queryIntentActivities(resolveIntent, 0)
        for (ri in resolveInfos) {
            val cn = ComponentName(ri.activityInfo.packageName, ri.activityInfo.name)
            val loadedApp = model.data.getApp(cn)
            if (loadedApp != null) {
                loadedApps.remove(loadedApp)
                var update = false
                val title = ri.loadLabel(pm).toString()
                if (title != loadedApp.title) {
                    loadedApp.title = title
                    update = true
                }
                val system = (ri.activityInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
                if (system != loadedApp.isSystem) {
                    loadedApp.isSystem = system
                    update = true
                }
                //TODO check storage update
                if (update) {
                    updated.add(loadedApp)
                }
            } else {
                val intent = Intent().apply { component = cn }
                val title = ri.loadLabel(pm)
                val icon = ri.loadIcon(pm)
                val appInfo = AppInfo(title.toString(), icon, intent)
                added.add(appInfo)
            }
        }
        removed.addAll(loadedApps)

        val cbk = model.callbackRef?.get()
        if (cbk != null) {
            runOnUiThread {
                cbk.bindAppAdded(added)
            }
            runOnUiThread {
                cbk.bindAppUpdated(updated)
            }
            val updatedHomeApps = updated.mapNotNull { model.data.getHomeAppInfo(it) }
            runOnUiThread {
                cbk.bindItemUpdated(updatedHomeApps)
            }
            runOnUiThread {
                cbk.bindAppRemoved(removed)
            }
        }
    }

    private fun syncFolders() {
        val folderToRemove = ArrayList(model.data.folderMap.filterValues { it.isEmpty() }.values)
        if (folderToRemove.isEmpty()) {
            return
        }
        for (folder in folderToRemove) {
            model.data.folderMap.remove(folder.id)
        }
        val cbk = model.callbackRef?.get()
        if (cbk != null) {
            runOnUiThread {
                cbk.bindItemRemoved(folderToRemove)
            }
        }
    }
}