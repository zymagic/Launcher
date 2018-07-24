package com.abs.launcher

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import com.abs.launcher.model.db.Applications
import com.abs.launcher.model.db.Favorites
import com.abs.launcher.model.db.generateSQL

/**
 * Created by zy on 18-1-7.
 */
class LauncherProvider : ContentProvider() {

    private lateinit var database: DatabaseHelper
    private val BASE_URI = Uri.parse("content://$AUTHORITY")

    override fun onCreate(): Boolean {
        database = DatabaseHelper(context)
        return true
    }

    override fun insert(uri: Uri, values: ContentValues): Uri {
        var sql = SqlArgument(uri)
        var db = database.writableDatabase
        var id = db.insert(sql.table, null, values)
        if (id as Int == -1) {
            return BASE_URI
        }
        return uri.buildUpon().appendPath(id.toString()).build()
    }

    override fun query(uri: Uri, projection: Array<out String>?, selection: String?, selectionArgs: Array<out String>?, sortOrder: String?): Cursor {
        var sql = SqlArgument(uri)
        var db = database.readableDatabase
        var cursor = db.query(sql.table, projection, selection, selectionArgs, null, null, sortOrder)
        return cursor
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        var sql = SqlArgument(uri)
        var db = database.writableDatabase
        var count = db.update(sql.table, values, selection, selectionArgs)
        return count
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        var sql = SqlArgument(uri)
        var db = database.writableDatabase
        return db.delete(sql.table, selection, selectionArgs)
    }

    override fun getType(uri: Uri): String {
        var sql = SqlArgument(uri)
        return if (sql.id == null) {
            "vnd.android.cursor.dir/vnd.launcher." + sql.table
        } else {
            "vnd.android.cursor.item/vnd.launcher." + sql.table
        }
    }

    class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, "launcher.db", null, DATABASE_VERSION) {

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(generateSQL(Favorites))
            db.execSQL(generateSQL(Applications))
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS ${Favorites.table}")
            db.execSQL("DROP TABLE IF EXISTS ${Applications.table}")
        }

    }

    class SqlArgument(uri: Uri) {
        var table: String? = uri.pathSegments.elementAtOrNull(0)
        var id: String? = uri.pathSegments.elementAtOrNull(1)
        var notify = uri.getQueryParameter("notify")?.toBoolean() ?: false
    }

}