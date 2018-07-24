package com.abs.launcher

import android.app.Activity
import android.content.Context
import com.abs.launcher.loader.LoaderTask
import com.abs.launcher.model.AppInfo
import com.abs.launcher.model.DataModel
import com.abs.launcher.model.HomeItemInfo
import java.lang.ref.WeakReference

/**
 * Created by zy on 17-12-18.
 */
class LauncherModel(private val context: Context) {
    var callbackRef: WeakReference<Callback>? = null
    val data: DataModel = DataModel()

    fun setCallback(callback: Callback) {
        var old = callbackRef?.get()
        if (old is Activity) {
            old.finish()
        }
        callbackRef = WeakReference(callback)
    }

    fun buildLoaderTask() : LoaderTask.Builder = LoaderTask.Builder(context, this)

    interface Callback {
        fun onStartBindingInHome()
        fun onFinishBindingInHome()

        fun bindItemLoaded(items: List<HomeItemInfo>)
        fun bindItemAdded(items: List<HomeItemInfo>)
        fun bindItemRemoved(items: List<HomeItemInfo>)
        fun bindItemUpdated(items: List<HomeItemInfo>)

        fun bindAppLoaded(apps: List<AppInfo>)
        fun bindAppAdded(apps: List<AppInfo>)
        fun bindAppRemoved(apps: List<AppInfo>)
        fun bindAppUpdated(apps: List<AppInfo>)
    }
}