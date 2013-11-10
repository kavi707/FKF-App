package com.fkf.resturent.database.dbprovider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import com.fkf.resturent.database.LocalDatabaseSQLiteOpenHelper;

/**
 * Created by kavi on 10/27/13.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class DbContentProvider extends ContentProvider {

    private LocalDatabaseSQLiteOpenHelper localDatabaseSQLiteOpenHelper;
    SQLiteDatabase recipeDatabase;

    private static final String AUTHORITY = "com.fkf.resturent.database.dbprovider.DbContentProvider";

    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static String getTableName(Uri uri){
        String value = uri.getPath();
        value = value.replace("/", "");//we need to remove '/'
        return value;
    }

    @Override
    public boolean onCreate() {
        localDatabaseSQLiteOpenHelper = new LocalDatabaseSQLiteOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        String tableName = getTableName(uri);
        if(recipeDatabase == null) {
            recipeDatabase = localDatabaseSQLiteOpenHelper.getWritableDatabase();
        }

        Cursor qryCursor = null;
        try {
            qryCursor = recipeDatabase.query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return qryCursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        String tableName = getTableName(uri);
        if(recipeDatabase == null) {
            recipeDatabase = localDatabaseSQLiteOpenHelper.getWritableDatabase();
        }
        long value = recipeDatabase.insert(tableName, null, contentValues);
        return Uri.withAppendedPath(CONTENT_URI, String.valueOf(value));
    }

    @Override
    public int delete(Uri uri, String where, String[] args) {
        String tableName = getTableName(uri);
        if(recipeDatabase == null) {
            recipeDatabase = localDatabaseSQLiteOpenHelper.getWritableDatabase();
        }
        return recipeDatabase.delete(tableName, where, args);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
