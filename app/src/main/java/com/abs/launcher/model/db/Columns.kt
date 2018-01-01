package com.abs.launcher.model.db

import android.provider.BaseColumns
import kotlin.reflect.KProperty

/**
 * Created by zy on 17-12-29.
 */
const val AUTHORITY = "com.abs.launcher"
const val TABLE_FAVORITES = "favorites"
const val TABLE_APPS = "apps"
const val TABLE_HIDDEN = "hidden"
const val TABLE_USAGE = "usg"

@Type(ColumnType.TEXT)
const val CELL_X = "cellX"
@Type(ColumnType.TEXT)
const val CELL_Y = "cellY"
@Type(ColumnType.TEXT)
const val SPAN_X = "spanX"
@Type(ColumnType.TEXT)
const val SPAN_Y = "spanY"
@Type(ColumnType.TEXT)
const val CONTAINER = "container"
@Type(ColumnType.TEXT)
const val SCREEN = "screen"

@Type(ColumnType.TEXT)
const val ITEM_TYPE = "itemType"
@Type(ColumnType.TEXT)
const val CATEGORY = "category"

@Type(ColumnType.TEXT)
const val INTENT = "intent"

@Type(ColumnType.BLOB)
const val ICON = "icon"
@Type(ColumnType.TEXT)
const val ICON_RESOURCE = "icon_resource"
@Type(ColumnType.TEXT)
const val ICON_PACKAGE = "icon_package"

@Type(ColumnType.TEXT)
const val TITLE = "title"
@Type(ColumnType.TEXT)
const val TITLE_RESOURCE = "title_resource"
@Type(ColumnType.TEXT)
const val TITLE_PACKAGE = "title_package"

@Type(ColumnType.TEXT)
const val EXTRA = "extra"

@Type(ColumnType.INTEGER)
const val SYSTEM = "system"
@Type(ColumnType.INTEGER)
const val STORAGE = "storage"

open class Columns(vararg val cs: KProperty<*>) : BaseColumns

object Favorites : Columns(
        ::CELL_X,
        ::CELL_Y,
        ::SPAN_X,
        ::SPAN_Y,
        ::CONTAINER,
        ::SCREEN,
        ::ITEM_TYPE,
        ::CATEGORY
)

object Applications : Columns(

)