package com.abs.launcher.model

/**
 * Created by zy on 17-12-17.
 */
class HomeAppInfo(var appInfo: AppInfo? = null): HomeItemInfo(if (appInfo != null && appInfo.isShortcut) ITEM_TYPE_SHORTCUT else ITEM_TYPE_APPLICATION), FolderItemInfo {
    override fun getInfo(): ItemInfo {
        return this
    }

}