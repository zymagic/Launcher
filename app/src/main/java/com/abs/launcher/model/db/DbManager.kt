package com.abs.launcher.model.db

import android.content.ContentUris
import android.content.ContentValues
import com.abs.launcher.LauncherApp
import com.abs.launcher.model.ItemInfo

private fun getResolver() = LauncherApp.instance.contentResolver

fun ItemInfo.insertDb() {
    val cv = ContentValues()
    fillDbValue(cv)
    id = ContentUris.parseId(getResolver().insert(Favorites.getUri(), cv))
}

fun ItemInfo.updateDb() {
    val cv = ContentValues()
    fillDbValue(cv)
    getResolver().update(Favorites.getUri(id, false), cv, null, null)
}

fun ItemInfo.deleteDb() {
    getResolver().delete(Favorites.getUri(id, false), null, null)
}