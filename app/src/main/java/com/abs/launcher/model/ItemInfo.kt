package com.abs.launcher.model

import android.content.ContentValues
import android.content.Intent
import android.graphics.drawable.Drawable
import android.provider.BaseColumns
import com.abs.launcher.model.db.*

/**
 * Created by zy on 17-12-17.
 */
abstract class ItemInfo(var type: Int) {
    var id: Long = NO_ID
    var category: Int = -1
    var position: Position = NO_POSITION

    abstract fun getActionList(): List<ItemAction>

    open fun fillDbValue(cv: ContentValues) {
        with (cv) {
            put(BaseColumns._ID, id)

            put(CATEGORY, category)

            put(CONTAINER, position.container)
            put(SCREEN, position.screen)
            put(CELL_X, position.cellX)
            put(CELL_Y, position.cellY)
            put(CELL_X, position.spanX)
            put(CELL_Y, position.spanY)
        }
    }
}

data class Position(var container: Long, var screen: Int, var cellX: Int, var cellY: Int, var spanX: Int = 1, var spanY: Int = 1)

const val NO_ID: Long = -1
val NO_POSITION = Position(-1, -1, -1, -1, -1)

fun Position.isEmpty() = this.screen < 0 || this.cellX < 0 || this.cellY < 0

data class ItemAction(val title: String, val icon: Drawable, val intent: Intent)

const val CONTAINER_DESKTOP = -100
const val CONTAINER_DOCKBAR = -101

const val ITEM_TYPE_APPLICATION = 0
const val ITEM_TYPE_SHORTCUT = 1
const val ITEM_TYPE_APP_WIDGET = 2
const val ITEM_TYPE_WIDGET_VIEW = 3
const val ITEM_TYPE_FOLDER = 4