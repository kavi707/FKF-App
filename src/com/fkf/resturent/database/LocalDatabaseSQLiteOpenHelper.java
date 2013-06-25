package com.fkf.resturent.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by kavi on 6/23/13.
 */
public class LocalDatabaseSQLiteOpenHelper extends SQLiteOpenHelper {

    private SQLiteDatabase localFKFDatabase;

    public static final String DB_NAME = "local_fkf_db.sqlite";
    public static final int VERSION = 1;

    //recipes table and columns
    public static final String RECIPES_TABLE_NAME = "recipes";
    public static final String RECIPE_ID = "recipe_id";
    public static final String RECIPE_NAME = "recipe_name";
    //category id must be on column of this recipe table
    public static final String ADDED_DATE = "added_date";
    public static final String RATINGS = "ratings";

    //category table and columns
    public static final String CATEGORY_TABLE_NAME = "categories";
    public static final String CATEGORY_ID = "category_id";
    public static final String CATEGORY_NAME = "category_name";

    public LocalDatabaseSQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createRecipesTable(sqLiteDatabase);
        createCategoriesTable(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

    /**
     * create recipes table store recipes after sync with the central db
     * @param sqLiteDatabase
     */
    private void createRecipesTable(SQLiteDatabase sqLiteDatabase) {
        String createRecipesTableQuery = "create table " + RECIPES_TABLE_NAME + " ( " +
                RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT not null, " +
                RECIPE_NAME + " text, " +
                CATEGORY_ID + " int, " +
                ADDED_DATE + " text, " +
                RATINGS + " int " +
                ");";
        sqLiteDatabase.execSQL(createRecipesTableQuery);
    }

    /**
     * create recipe categories table store recipes after sync with the central db
     * @param sqLiteDatabase
     */
    private void createCategoriesTable(SQLiteDatabase sqLiteDatabase) {
        String createCategoriesTableQuery = "create table " + CATEGORY_TABLE_NAME + " ( " +
                CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT not null, " +
                CATEGORY_NAME + " text " +
                ");";
        sqLiteDatabase.execSQL(createCategoriesTableQuery);

        //temp testing purpose value adding
        for (int i = 0; i < 3; i++) {
            ContentValues values = new ContentValues();
            values.put(CATEGORY_NAME, "item - " + i);

            try {
                sqLiteDatabase.insert(CATEGORY_TABLE_NAME, null, values);
            } catch (SQLiteException ex) {
                throw ex;
            }
        }
        // temp block
    }


    public ArrayList<String> getAllCategories(){
        ArrayList<String> categoryList = new ArrayList<String>();
        categoryList.add("Latest"); //default item for view the latest yummys and other stuff
        localFKFDatabase = this.getWritableDatabase();

        try {
            String grepCategoriesQry = "select * from " + CATEGORY_TABLE_NAME;
            Cursor categoryCursor = localFKFDatabase.rawQuery(grepCategoriesQry, null);

            categoryCursor.moveToFirst();

            if(!categoryCursor.isAfterLast()) {
                do {
                    String categoryName = categoryCursor.getString(1);
                    categoryList.add(categoryName);
                } while (categoryCursor.moveToNext());
            }
            categoryCursor.close();
        } catch (SQLiteException ex) {
            throw ex;
        }

        return categoryList;
    }
}
