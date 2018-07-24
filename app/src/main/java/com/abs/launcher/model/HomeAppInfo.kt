package com.abs.launcher.model

import android.graphics.drawable.Drawable

/**
 * Created by zy on 17-12-17.
 */
class HomeAppInfo(var appInfo: AppInfo? = null): HomeItemInfo(if (appInfo != null && appInfo.isShortcut) ITEM_TYPE_SHORTCUT else ITEM_TYPE_APPLICATION), FolderItemInfo {

    val icon: Drawable?
        get() = appInfo?.icon

    val title: String?
        get() = appInfo?.title

    override fun getInfo(): HomeItemInfo {
        return this
    }

}