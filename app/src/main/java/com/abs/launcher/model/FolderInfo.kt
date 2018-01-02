package com.abs.launcher.model

/**
 * Created by zy on 17-12-17.
 */
class FolderInfo: ItemInfo(ITEM_TYPE_FOLDER) {

    private val contents: MutableList<FolderItemInfo> = ArrayList()

    override fun getActionList(): List<ItemAction> {
        return ArrayList()
    }

    fun add(info: FolderItemInfo) {
        info.getInfo().position.container = id as Int
        for ((index, item) in contents.withIndex()) {
            if (item.getInfo().position.cellX >= info.getInfo().position.cellX) {
                contents.add(index, info)
                return
            }
        }
        contents.add(info)
    }

    fun getContents(): List<FolderItemInfo> {
        return ArrayList(contents)
    }
}

interface FolderItemInfo {
    fun getInfo(): ItemInfo
}