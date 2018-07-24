package com.abs.launcher.model

/**
 * Created by zy on 17-12-17.
 */
class FolderInfo : HomeItemInfo(ITEM_TYPE_FOLDER) {

    var title: String? = null
        get() {
            if (field == null) {
                //TODO get default folder name from resource
            }
            return field
        }

    private val contents: MutableList<FolderItemInfo> = ArrayList()

    override fun getActionList(): List<ItemAction> {
        return ArrayList()
    }

    fun add(info: FolderItemInfo) {
        info.getInfo().position.container = id
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

    fun isEmpty(): Boolean {
        return contents.isEmpty()
    }
}

interface FolderItemInfo {
    fun getInfo(): HomeItemInfo
}