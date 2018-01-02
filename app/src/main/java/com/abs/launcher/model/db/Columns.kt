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

open class Columns(val table: String, vararg val cs: KProperty<String>) : BaseColumns

object Favorites : Columns(
        TABLE_FAVORITES,
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
        TABLE_APPS,
        ::TITLE,
        ::INTENT,
        ::ITEM_TYPE,
        ::STORAGE,
        ::SYSTEM
)

private fun generateSQL(p: KProperty<String>): String {
    var value: String = p.name.toLowerCase()
    var an: Type? = p.annotations.find { it is Type }?.let { it as Type }
    var typeStr = an?.type?.name ?: ""
    var isPrimary = p.annotations.any { it is  PrimaryKey}
    var isNotNull = p.annotations.any { it is NotNull }
    var def = p.annotations.find { it is Def }.let { if (it == null) "" else (" DEFAULT " + (it as Def).value) }
    return "$value $typeStr${if (isPrimary) " PRIMARY KEY" else ""}${if (isNotNull) " NOT NULL" else ""}$def"
}

fun generateSQL(p: Columns): String {
    var sql = "CREATE TABLE ${p.table} ("
    var first = true
    for (pro in p.cs) {
        if (first) {
            first = false
        } else {
            sql += ", "
        }
        sql += generateSQL(pro)
    }
    sql += ")"
    return sql
}