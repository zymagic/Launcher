package com.abs.launcher

import android.net.Uri
import android.provider.BaseColumns

/**
 * Created by zy on 17-12-18.
 */
val AUTHORITY = "com.abs.launcher"
val TABLE_FAVORITES = "favorites"
val TABLE_APPS = "apps"
val TABLE_HIDDEN = "hidden"
val TABLE_USAGE = "usg"

object Columns {
    val CELL_X get() = "cellX"
    val CELL_Y get() = "cellY"
    val SPAN_X get() = "spanX"
    val SPAN_Y get() = "spanY"
    val CONTAINER get() = "container"
    val SCREEN get() = "screen"

    val ITEM_TYPE get() = "itemType"
    val CATEGORY get() = "category"

    val table: String = ""

    fun getUri(notify: Boolean = false) = Uri.parse("content://$AUTHORITY/$table?notify=$notify")
    fun getUri(id: Long, notify: Boolean = false) = Uri.parse("content://$AUTHORITY/$table/$id?notify=$notify")
}

object FavoriteColumns {
    val INTENT get() = "intent"

    val ICON get() = "icon"
    val ICON_RESOURCE get() = "icon_resource"
    val ICON_PACKAGE get() = "icon_package"

    val TITLE get() = "title"
    val TITLE_RESOURCE get() = "title_resource"
    val TITLE_PACKAGE get() = "title_package"

    val EXTRA get() = "extra"

    val table: String
        get() = TABLE_FAVORITES
}

interface ApplicationColumns: BaseColumns {
    fun getUri(notify: Boolean = false) = Uri.parse("content://$AUTHORITY/$TABLE_APPS?notify=$notify")
    fun getUri(id: Long, notify: Boolean = false) = Uri.parse("content://$AUTHORITY/$TABLE_APPS/$id?notify=$notify")

    companion object {
        const val INTENT = "intent"

        const val ICON = "icon"
        const val ICON_RESOURCE = "icon_resource"
        const val ICON_PACKAGE = "icon_package"

        const val TITLE = "title"
        const val TITLE_RESOURCE = "title_resource"
        const val TITLE_PACKAGE = "title_package"

        const val SYSTEM = "system"
        const val STORAGE = "storage"
    }
}

class ColumnIndex {

}


fun ttt() {
    var sss: String = FavoriteColumns.obj.CELL_X
}



