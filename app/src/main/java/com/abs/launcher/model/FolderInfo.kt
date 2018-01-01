package com.abs.launcher.model

/**
 * Created by zy on 17-12-17.
 */
class FolderInfo: ItemInfo(ITEM_TYPE_FOLDER) {

    val contents: MutableList<FolderItemInfo> = ArrayList()

    override fun getActionList(): List<ItemAction> {
        return ArrayList()
    }


}

interface FolderItemInfo {
    fun getInfo(): ItemInfo
}