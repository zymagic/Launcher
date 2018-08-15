package com.abs.launcher.model.db

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns
import com.abs.launcher.AUTHORITY
import com.zy.kotlinutils.core.db.Column
import com.zy.kotlinutils.core.db.Table
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * Created by zy on 17-12-29.
 */
const val TABLE_FAVORITES = "favorites"
const val TABLE_APPS = "apps"
const val TABLE_HIDDEN = "hidden"
const val TABLE_USAGE = "usg"

const val NO_ID : Long = -1

@Table(TABLE_FAVORITES)
class Favorites {
    @Column(name = "_id", primary = true)
    var id: Long = NO_ID
    var cellX = -1
    var cellY = -1
    var spanX = 1
    var spanY = 1
    var container: Long = -1
    var screen = -1
    var itemType = -1
    var category = -1
    var intent: Intent? = null
    var widgetId = -1
    var extra: String? = null
}

@Table(TABLE_APPS)
class Applications {
    @Column(name = "_id", primary = true)
    var id: Long = NO_ID
    var title: String? = null
    var intent: Intent? = null
    var icon: String? = null
    var storage: Int = 0
    var system = false
    var category = -1
}

const val CELL_X = "cellX"
const val CELL_Y = "cellY"
const val SPAN_X = "spanX"
const val SPAN_Y = "spanY"
const val CONTAINER = "container"
const val SCREEN = "screen"

const val ITEM_TYPE = "itemType"
const val CATEGORY = "category"

const val INTENT = "intent"

const val ICON = "icon"
const val ICON_RESOURCE = "icon_resource"
const val ICON_PACKAGE = "icon_package"

const val TITLE = "title"
const val TITLE_RESOURCE = "title_resource"
const val TITLE_PACKAGE = "title_package"

const val WIDGET_ID = "widget_id"

const val EXTRA = "extra"

const val SYSTEM = "system"
const val STORAGE = "storage"

open class Columns(val table: String, vararg val cs: KProperty<String>) : BaseColumns {
    fun getUri(notify: Boolean = false): Uri {
        return Uri.parse("content://$AUTHORITY/$table${if (notify) "?notify=true" else ""}")
    }

    fun getUri(id: Long, notify: Boolean = false): Uri {
        return Uri.parse("content://$AUTHORITY/$table/$id${if(notify) "?notify=true" else ""}")
    }

}

fun KClass<*>.uri(id: Long = -1, notify: Boolean = false): Uri {
    val table = this.java.annotations.find { it is Table }!! as Table
    return Uri.parse("content://$AUTHORITY/${table.name}/${ if (id.toInt() == -1) "" else id.toString()}${ if (notify) "?notify=true" else ""}")
}

inline fun <reified T: Any> uri(id: Long = -1, notify: Boolean = false): Uri = T::class.uri(id, notify)