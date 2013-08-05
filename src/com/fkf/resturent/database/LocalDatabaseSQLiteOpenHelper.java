package com.fkf.resturent.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.fkf.resturent.templates.LoginActivity;

import java.util.ArrayList;

/**
 * Created by kavi on 6/23/13.
 * local sqlite database using for application offline functioning
 * create tables,
 * data insert/delete/update cases
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class LocalDatabaseSQLiteOpenHelper extends SQLiteOpenHelper {

    private SQLiteDatabase localFKFDatabase;

    public static final String DB_NAME = "local_fkf_db.sqlite";
    public static final int VERSION = 1;

    //recipes table and columns
    public static final String RECIPES_TABLE_NAME = "recipes";
    public static final String RECIPE_ID = "recipe_id";
    public static final String RECIPE_NAME = "recipe_name";
    public static final String RECIPE_DESCRIPTION = "recipe_description";
    //category id must be on column of this recipe table
    public static final String ADDED_DATE = "added_date";
    public static final String RATINGS = "ratings";
    public static final String IMAGE_URL = "image_url";

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
                RECIPE_DESCRIPTION + " text, " +
                CATEGORY_ID + " int, " +
                ADDED_DATE + " text, " +
                RATINGS + " int, " +
                IMAGE_URL + " text " +
                ");";
        sqLiteDatabase.execSQL(createRecipesTableQuery);

        //temp testing purpose value adding
        for (int i = 0; i < 3; i++) {
            ContentValues values = new ContentValues();
            values.put(RECIPE_NAME, "recipe name - " + i);
            values.put(RECIPE_DESCRIPTION, "recipe des - " + i);
            values.put(CATEGORY_ID, 1);
            values.put(ADDED_DATE, "26-06-2013");
            values.put(RATINGS, i);
            values.put(IMAGE_URL, "http://news.cnet.com/i/bto/20080112/small_car.jpg");

            try {
                sqLiteDatabase.insert(RECIPES_TABLE_NAME, null, values);
            } catch (SQLiteException ex) {
                throw ex;
            }
        }
        // temp block
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

    /**
     * get all recipe categories for the item menu
     * @return
     */
    public ArrayList<String> getAllCategories(){
        ArrayList<String> categoryList = new ArrayList<String>();
        categoryList.add(":Latest Yummys"); //default item for view the latest yummys and other stuff
        //If user logged in to application
        if(LoginActivity.LOGGED_STATUS == 1){
            categoryList.add(":My Favorites");
        }
        localFKFDatabase = this.getWritableDatabase();

        try {
            String grepCategoriesQry = "select * from " + CATEGORY_TABLE_NAME;
            Cursor categoryCursor = localFKFDatabase.rawQuery(grepCategoriesQry, null);

            categoryCursor.moveToFirst();

            if(!categoryCursor.isAfterLast()) {
                do {
                    int categoryId = categoryCursor.getInt(0);
                    String categoryName = categoryCursor.getString(1);
                    categoryList.add(categoryId+ ":" + categoryName);
                } while (categoryCursor.moveToNext());
            }
            categoryCursor.close();
        } catch (SQLiteException ex) {
            throw ex;
        }

        return categoryList;
    }

    public ArrayList<Recipe> getRecipesFromCategoryId(int categoryId) {
        ArrayList<Recipe> selectedRecipeList = new ArrayList<Recipe>();
        localFKFDatabase = this.getWritableDatabase();

        try {
            String grepRecipeListQry = "select * from " + RECIPES_TABLE_NAME + " where " + CATEGORY_ID + " = " + categoryId;
            Cursor recipeCursor = localFKFDatabase.rawQuery(grepRecipeListQry, null);

            recipeCursor.moveToFirst();
            Recipe recipe;

            if(!recipeCursor.isAfterLast()) {
                do {
                    int recipeId = recipeCursor.getInt(0);
                    String recipeName = recipeCursor.getString(1);
                    String recipeDescription = recipeCursor.getString(2);
                    int recipeCategoryId = recipeCursor.getInt(3);
                    String recipeAddedDate = recipeCursor.getString(4);
                    int recipeRatings = recipeCursor.getInt(5);
                    String imageUrl = recipeCursor.getString(6);

                    recipe = new Recipe();

                    recipe.setId(recipeId);
                    recipe.setName(recipeName);
                    recipe.setDescription(recipeDescription);
                    recipe.setCategoryId(recipeCategoryId);
                    recipe.setAddedDate(recipeAddedDate);
                    recipe.setRatings(recipeRatings);
                    recipe.setImageUrl(imageUrl);

                    selectedRecipeList.add(recipe);
                } while (recipeCursor.moveToNext());
            }
            recipeCursor.close();
        } catch (SQLiteException ex) {
            throw ex;
        }

        Log.d("selected list count ", String.valueOf(selectedRecipeList.size()));

        return selectedRecipeList;
    }

    public ArrayList<Recipe> getRecipeFromRecipeId(int selectedRecipeId) {
        ArrayList<Recipe> selectedRecipeList = new ArrayList<Recipe>();
        localFKFDatabase = this.getWritableDatabase();

        try {
            String grepRecipeQry = "select * from " + RECIPES_TABLE_NAME + " where " + RECIPE_ID + " = " + selectedRecipeId;
            Cursor recipeCursor = localFKFDatabase.rawQuery(grepRecipeQry, null);

            recipeCursor.moveToFirst();
            Recipe recipe;

            if(!recipeCursor.isAfterLast()) {
                do {
                    int recipeId = recipeCursor.getInt(0);
                    String recipeName = recipeCursor.getString(1);
                    String recipeDescription = recipeCursor.getString(2);
                    int recipeCategoryId = recipeCursor.getInt(3);
                    String recipeAddedDate = recipeCursor.getString(4);
                    int recipeRatings = recipeCursor.getInt(5);
                    String recipeImageUrl = recipeCursor.getString(6);

                    recipe = new Recipe();

                    recipe.setId(recipeId);
                    recipe.setName(recipeName);
                    recipe.setDescription(recipeDescription);
                    recipe.setCategoryId(recipeCategoryId);
                    recipe.setAddedDate(recipeAddedDate);
                    recipe.setRatings(recipeRatings);
                    recipe.setImageUrl(recipeImageUrl);

                    selectedRecipeList.add(recipe);
                } while (recipeCursor.moveToNext());
            }
            recipeCursor.close();
        } catch (SQLiteException ex) {
            throw ex;
        }

        return selectedRecipeList;
    }

    /**
     * save the given recipe from caller function
     * @param recipe
     */
    public void saveRecipe(Recipe recipe) {

        localFKFDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(RECIPE_NAME, recipe.getName());
        values.put(RECIPE_DESCRIPTION, recipe.getDescription());
        values.put(CATEGORY_ID, recipe.getCategoryId());
        values.put(ADDED_DATE, "26-06-2013");
        values.put(RATINGS, recipe.getRatings());
        values.put(IMAGE_URL, recipe.getImageUrl());

        try {
            localFKFDatabase.insert(RECIPES_TABLE_NAME, null, values);
        } catch (SQLiteException ex) {
            throw ex;
        }
    }

    /**
     * delete all recipes in local database
     */
    public void deleteAllRecipes() {
        localFKFDatabase = this.getWritableDatabase();
        try {
            localFKFDatabase.delete(RECIPES_TABLE_NAME, null, null);
        } catch (SQLiteException ex) {
            throw ex;
        }
    }

    /**
     * this method is for testing purposes. check the recipes in local database.
     */
    public void getAllRecipes() {
        localFKFDatabase = this.getWritableDatabase();
        try {
            String grepRecipeQry = "select * from " + RECIPES_TABLE_NAME;
            Cursor recipeCursor = localFKFDatabase.rawQuery(grepRecipeQry, null);

            recipeCursor.moveToFirst();

            if(!recipeCursor.isAfterLast()) {
                do {
                    int recipeId = recipeCursor.getInt(0);
                    Log.d("id", String.valueOf(recipeId));
                    String recipeName = recipeCursor.getString(1);
                    Log.d("recipeName", recipeName);
                    String recipeDescription = recipeCursor.getString(2);
                    Log.d("recipeDes", recipeDescription);
                    int recipeCategoryId = recipeCursor.getInt(3);
                    Log.d("recipeCategory", String.valueOf(recipeCategoryId));
                    String recipeAddedDate = recipeCursor.getString(4);
                    Log.d("recipeAddDate", recipeAddedDate);
                    int recipeRatings = recipeCursor.getInt(5);
                    Log.d("recipeRatings", String.valueOf(recipeRatings));
                    String recipeImageUrl = recipeCursor.getString(6);
                    Log.d("url", recipeImageUrl);
                } while (recipeCursor.moveToNext());
            }
        } catch (SQLiteException ex) {

        }
    }
}
