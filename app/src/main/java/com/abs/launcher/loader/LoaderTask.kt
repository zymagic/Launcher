package com.abs.launcher.loader

import com.abs.launcher.exitDaemon
import com.abs.launcher.runInDaemon
import com.abs.launcher.util.safe

/**
 * Created by zy on 17-12-18.
 */
class LoaderTask: Runnable {

    companion object {
        var previousTask: LoaderTask? = null
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
        var databaseBinder = DatabaseLoader()
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
        var isFirst: Boolean = false,
        var isLaunching: Boolean = false,
        var reloadWorkspace: Boolean = false
    ) {
        fun build(): LoaderTask = LoaderTask(this)
    }
}