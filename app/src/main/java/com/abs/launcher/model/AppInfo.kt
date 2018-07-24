package com.abs.launcher.model

import android.content.Intent
import android.graphics.drawable.Drawable

/**
 * Created by zy on 17-12-17.
 */
data class AppInfo(var title: String, var icon: Drawable?, val intent: Intent) {
    var id = NO_ID
    var isSystem: Boolean = false
    var storage: Int = 0
    var category: Int = -1

    var iconResource: String? = null

    var isShortcut = intent.component == null

    var lastUpdateTime: Long = 0
    var lastCalledTime: Long = 0
    var calledNum: Int = 0
}