package com.abs.launcher.model

/**
 * Created by zy on 17-12-17.
 */
abstract class ItemInfo(var type: Int) {
    var id: Long = NO_ID
    var category: Int = -1
    var position: Position = NO_POSITION

    abstract fun getActionList(): List<ItemAction>
}

data class Position(var container: Int, var screen: Int, var cellX: Int, var cellY: Int, var spanX: Int = 1, var spanY: Int = 1)

const val NO_ID: Long = -1
val NO_POSITION = Position(-1, -1, -1, -1, -1)

fun Position.isEmpty() = this.screen < 0 || this.cellX < 0 || this.cellY < 0

interface ItemAction

const val ITEM_TYPE_APPLICATION = 0
const val ITEM_TYPE_SHORTCUT = 1
const val ITEM_TYPE_APP_WIDGET = 2
const val ITEM_TYPE_WIDGET_VIEW = 3;
const val ITEM_TYPE_FOLDER = 4;