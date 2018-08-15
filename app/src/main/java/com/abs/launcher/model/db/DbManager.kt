package com.abs.launcher.model.db

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import com.abs.launcher.LauncherApp
import com.abs.launcher.model.ItemInfo

private val resolver: ContentResolver by lazy { LauncherApp.instance.contentResolver }

fun ItemInfo.insertDb() {
    val cv = ContentValues()
    fillDbValue(cv)
    id = ContentUris.parseId(resolver.insert(uri<Favorites>(), cv))
}

fun ItemInfo.updateDb() {
    val cv = ContentValues()
    fillDbValue(cv)
    resolver.update(uri<Favorites>(id), cv, null, null)
}

fun ItemInfo.deleteDb() {
    resolver.delete(uri<Favorites>(id), null, null)
}