package com.abs.launcher.model

/**
 * Created by zy on 17-12-17.
 */
open class HomeItemInfo(type: Int) : ItemInfo(type) {
    override open fun getActionList(): List<ItemAction> {
        return ArrayList()
    }
}