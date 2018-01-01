package com.abs.launcher

import android.app.Application

/**
 * Created by zy on 17-12-18.
 */
class LauncherApp: Application() {

    companion object {
        var instance: LauncherApp? = null
    }

    var model: LauncherModel? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}