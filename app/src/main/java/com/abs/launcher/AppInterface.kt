package com.abs.launcher

import android.app.Application

/**
 * Created by zy on 18-1-3.
 */
class AppInterface {

    var model: LauncherModel? = null

    var application: Application? = null

    companion object {
        var instance: AppInterface? = null
    }
}