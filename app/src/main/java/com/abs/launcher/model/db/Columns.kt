package com.abs.launcher.model.db

import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns
import com.abs.launcher.AUTHORITY
import kotlin.reflect.KProperty

/**
 * Created by zy on 17-12-29.
 */
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

@Type(ColumnType.INTEGER)
const val WIDGET_ID = "widget_id"

@Type(ColumnType.TEXT)
const val EXTRA = "extra"

@Type(ColumnType.INTEGER)
const val SYSTEM = "system"
@Type(ColumnType.INTEGER)
const val STORAGE = "storage"

open class Columns(val table: String, vararg val cs: KProperty<String>) : BaseColumns {
    fun getUri(notify: Boolean = false): Uri {
        return Uri.parse("content://$AUTHORITY/$table${if (notify) "?notify=true" else ""}")
    }

    fun getUri(id: Int, notify: Boolean = false) {
        Uri.parse("content://$AUTHORITY/$table/$id${if(notify) "?notify=true" else ""}")
    }

}

open class CursorIndex(cursor: Cursor) {
    val idIndex = cursor.getColumnIndexOrThrow(BaseColumns._ID)
}

object Favorites : Columns(
        TABLE_FAVORITES,
        ::CELL_X,
        ::CELL_Y,
        ::SPAN_X,
        ::SPAN_Y,
        ::CONTAINER,
        ::SCREEN,
        ::ITEM_TYPE,
        ::CATEGORY,
        ::INTENT,
        ::WIDGET_ID,
        ::EXTRA
)

class FavoriteIndex(cursor: Cursor) : CursorIndex(cursor) {
    val cellXIndex = cursor.getColumnIndexOrThrow(CELL_X)
    val cellYIndex = cursor.getColumnIndexOrThrow(CELL_Y)
    val spanXIndex = cursor.getColumnIndexOrThrow(SPAN_X)
    val spanYIndex = cursor.getColumnIndexOrThrow(SPAN_Y)
    val containerIndex = cursor.getColumnIndexOrThrow(CONTAINER)
    val screenIndex = cursor.getColumnIndexOrThrow(SCREEN)
    val itemTypeIndex = cursor.getColumnIndexOrThrow(ITEM_TYPE)
    val categoryIndex = cursor.getColumnIndexOrThrow(CATEGORY)
    val intentIndex = cursor.getColumnIndexOrThrow(INTENT)
    val widgetIdIndex = cursor.getColumnIndexOrThrow(WIDGET_ID)
    val extraIndex = cursor.getColumnIndexOrThrow(EXTRA)
}

object Applications : Columns(
        TABLE_APPS,
        ::TITLE,
        ::INTENT,
        ::ICON,
        ::STORAGE,
        ::SYSTEM,
        ::CATEGORY
)

class ApplicationIndex(cursor: Cursor) : CursorIndex(cursor) {
    val titleIndex = cursor.getColumnIndexOrThrow(TITLE)
    val intentIndex = cursor.getColumnIndexOrThrow(INTENT)
    val iconIndex = cursor.getColumnIndexOrThrow(ITEM_TYPE)
    val storageIndex = cursor.getColumnIndexOrThrow(STORAGE)
    val systemIndex = cursor.getColumnIndexOrThrow(SYSTEM)
    val categoryIndex = cursor.getColumnIndexOrThrow(CATEGORY)
}

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
    var sql = "CREATE TABLE IF NOT EXISTS ${p.table} ("
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