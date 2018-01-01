package com.abs.launcher

import com.abs.launcher.loader.LoaderTask
import com.abs.launcher.model.DataModel
import java.lang.ref.WeakReference

/**
 * Created by zy on 17-12-18.
 */
class LauncherModel {
    var callbackRef: WeakReference<Callback>? = null
    val data: DataModel = DataModel()

    fun buildLoaderTask() : LoaderTask.Builder = LoaderTask.Builder()

    interface Callback {
        fun onStartBindingInHome()
        fun onFinishBindingInHome()
        fun bindItemAdded()
        fun bindItemRemoved()
        fun bindItemUpdated()
    }
}