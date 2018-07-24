package com.abs.launcher

import android.app.Application

/**
 * Created by zy on 17-12-18.
 */
class LauncherApp: Application() {

    companion object {
        lateinit var instance: LauncherApp
    }

    val model: LauncherModel by lazy { LauncherModel(this) }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}