package com.abs.launcher.loader

import android.content.Context
import com.abs.launcher.LauncherModel
import com.abs.launcher.exitDaemon
import com.abs.launcher.runInDaemon
import com.zy.kotlinutils.core.forward
import com.zy.kotlinutils.core.safe
import com.zy.kotlinutils.core.uiThread

/**
 * Created by zy on 17-12-18.
 */
class LoaderTask: Runnable {

    companion object {
        private var previousTask: LoaderTask? = null
    }

    private var params: Builder
    private var stoped: Boolean = false

    private constructor(param: Builder) {
        this.params = param
    }

    override fun run() {
        safe { load() }.forward {
            if (previousTask == this) {
                previousTask = null
            }
        }
    }

    private fun load() {
        params.model.callbackRef?.get()?.let {
            uiThread {
                it.onStartBindingInHome()
            }
        }
        DatabaseLoader(params.context, params.model).load()
        params.model.callbackRef?.get()?.let {
            uiThread {
                it.onFinishBindingInHome()
            }
        }
        SyncLoader(params.context, params.model).sync()
    }

    fun start() {
        previousTask?.exitDaemon()
        previousTask = this
        runInDaemon()
    }

    fun stop() {
        stoped = true
        if (previousTask == this) {
            previousTask = null
        }
        this.exitDaemon()
    }

    data class Builder(
            var context: Context,
            var model: LauncherModel,
            var isFirst: Boolean = false,
            var isLaunching: Boolean = false,
            var reloadWorkspace: Boolean = false
    ) {
        fun build(): LoaderTask = LoaderTask(this)
    }
}