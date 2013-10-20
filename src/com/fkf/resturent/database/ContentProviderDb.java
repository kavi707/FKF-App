package com.fkf.resturent.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.fkf.resturent.database.LocalDatabaseSQLiteOpenHelper;

/**
 * Created by kavi on 10/17/13.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class ContentProviderDb extends ContentProvider {

    private LocalDatabaseSQLiteOpenHelper localDatabaseSQLiteOpenHelper;
    public static final String AUTHORITY = "com.fkf.resturent.database.ContentProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    @Override
    public boolean onCreate() {
        localDatabaseSQLiteOpenHelper = new LocalDatabaseSQLiteOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] columns, String selection, String[] selectionArgs, String sortOrder) {
        String tableName = getTableName(uri);
        SQLiteDatabase mobileDatabase = localDatabaseSQLiteOpenHelper.getWritableDatabase();
        Cursor qryCursor = mobileDatabase.query(tableName, columns, selection, selectionArgs, null, null, sortOrder);
        return qryCursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        String tableName = getTableName(uri);
        SQLiteDatabase mobileDatabase = localDatabaseSQLiteOpenHelper.getWritableDatabase();
        long insertResult = mobileDatabase.insert(tableName, null, contentValues);
        return Uri.withAppendedPath(CONTENT_URI, String.valueOf(insertResult));
    }

    @Override
    public int delete(Uri uri, String where, String[] args) {
        String tableName = getTableName(uri);
        SQLiteDatabase mobileDatabase = localDatabaseSQLiteOpenHelper.getWritableDatabase();
        return mobileDatabase.delete(tableName, where, args);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    public static String getTableName(Uri uri){
        String value = uri.getPath();
        value = value.replace("/", "");//we need to remove '/'
        return value;
    }
}
