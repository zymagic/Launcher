package com.abs.launcher;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.abs.launcher.util.Utils;

import java.util.List;

/**
 * Created by zy on 17-5-18.
 */

public class LauncherProvider extends ContentProvider {

    public static final String AUTHORITY = "com.abs.launcher.Provider";

    private static final String DATABASE_NAME = "launcher.db";
    private static final int DATABASE_VERSION = 1;

    private SQLiteOpenHelper mDbHelper;

    private static final int MATCH_CODE_ITEM = 1;
    private static final int MATCH_CODE_DIR = 2;
    private static UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        URI_MATCHER.addURI(AUTHORITY, "*/#", MATCH_CODE_ITEM);
        URI_MATCHER.addURI(AUTHORITY, "*", MATCH_CODE_DIR);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new DataBaseHelper(this.getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SqlArguments sql = new SqlArguments(uri);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        return db.query(sql.table, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public String getType(Uri uri) {
        SqlArguments sql = new SqlArguments(uri);
        switch (URI_MATCHER.match(uri)) {
            case MATCH_CODE_ITEM:
                return "vnd.android.cursor.item/vnd.launcher." + sql.table;
            case MATCH_CODE_DIR:
                return "vnd.android.cursor.dir/vnd.launcher." + sql.table;
        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SqlArguments sql = new SqlArguments(uri);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long id = db.insert(sql.table, null, values);
        return Uri.parse("content://" + AUTHORITY + "/" + sql.table + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SqlArguments sql = new SqlArguments(uri);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        return db.delete(sql.table, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SqlArguments sql = new SqlArguments(uri);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        return db.update(sql.table, values, selection, selectionArgs);
    }

    private class DataBaseHelper extends SQLiteOpenHelper {

        public DataBaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + DbManager.TABLE_FAVORITE + "(" +
                    DbManager.generateSqlCreation(DbManager.Favorites.class) +
                    ")");
            db.execSQL("CREATE TABLE IF NOT EXISTS " + DbManager.TABLE_ALL_APPS + "(" +
                    DbManager.generateSqlCreation(DbManager.Applications.class) +
                    ")");
            db.execSQL("CREATE TABLE IF NOT EXISTS " + DbManager.TABLE_HIDDEN_APPS + "(" +
                    DbManager.generateSqlCreation(DbManager.HiddenApplications.class) +
                    ")");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    private static class SqlArguments {
        String table;
        String id;
        boolean notify;

        SqlArguments(Uri uri) {
            notify = Boolean.parseBoolean(uri.getQueryParameter("notify"));
            List<String> paths = uri.getPathSegments();
            if (!Utils.isEmpty(paths)) {
                switch (paths.size()) {
                    case 2:
                        id = paths.get(1);
                    case 1:
                        table = paths.get(0);
                        return;
                }
            }
            throw new IllegalArgumentException("invalid uri");
        }
    }
}
