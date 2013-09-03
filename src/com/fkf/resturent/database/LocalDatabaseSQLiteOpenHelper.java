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
import java.util.List;

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
    public static final String PRODUCT_ID = "product_id";
    public static final String RECIPE_NAME = "recipe_name";
    public static final String RECIPE_DESCRIPTION = "recipe_description";
    //category id must be on column of this recipe table
    public static final String INGREDIENTS = "ingredients";
    public static final String INSTRUCTIONS = "instructions";
    public static final String ADDED_DATE = "added_date";
    public static final String RATINGS = "ratings";
    public static final String IMAGE_URL = "image_url";

    //popular yummy table
    public static final String POPULAR_YUMMY_TABLE_NAME = "popular_yummys";
    public static final String POPULAR_INDEX = "popular_index";

    //latest yummy table
    public static final String LATEST_YUMMY_TABLE_NAME = "latest_yummys";
    public static final String LATEST_INDEX = "latest_index";

    //category table and columns
    public static final String CATEGORY_TABLE_NAME = "categories";
    public static final String CATEGORY_ID = "category_id";
    public static final String CATEGORY_PRODUCT_ID = "category_product_id";
    public static final String CATEGORY_NAME = "category_name";

    //last database modified date & time details table columns
    public static final String LAST_MODIFIED_DETAILS_TABLE_NAME = "last_modified_details";
    public static final String MODIFICATION_ID = "modification_id";
    public static final String MODIFIED_TIME_STAMP = "modified_time_stamp";

    public LocalDatabaseSQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createRecipesTable(sqLiteDatabase);
        createCategoriesTable(sqLiteDatabase);
        createLastDatabaseModificationDetailsTable(sqLiteDatabase);
        createPopularYummysTable(sqLiteDatabase);
        createLatestYummysTable(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

    /**
     * create recipes table
     * @param sqLiteDatabase
     */
    private void createRecipesTable(SQLiteDatabase sqLiteDatabase) {
        String createRecipesTableQuery = "create table " + RECIPES_TABLE_NAME + " ( " +
                RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT not null, " +
                PRODUCT_ID + " text, " +
                RECIPE_NAME + " text, " +
                RECIPE_DESCRIPTION + " text, " +
                INGREDIENTS + " text, " +
                INSTRUCTIONS + " text, " +
                CATEGORY_ID + " int, " +
                ADDED_DATE + " text, " +
                RATINGS + " int, " +
                IMAGE_URL + " text " +
                ");";
        sqLiteDatabase.execSQL(createRecipesTableQuery);
    }

    /**
     * create popular yummys table
     * @param sqLiteDatabase
     */
    private void createPopularYummysTable(SQLiteDatabase sqLiteDatabase) {
        String createRecipesTableQuery = "create table " + POPULAR_YUMMY_TABLE_NAME + " ( " +
                RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT not null, " +
                POPULAR_INDEX + " int, " +
                PRODUCT_ID + " text, " +
                RECIPE_NAME + " text, " +
                RECIPE_DESCRIPTION + " text, " +
                INGREDIENTS + " text, " +
                INSTRUCTIONS + " text, " +
                CATEGORY_ID + " int, " +
                ADDED_DATE + " text, " +
                RATINGS + " int, " +
                IMAGE_URL + " text " +
                ");";
        sqLiteDatabase.execSQL(createRecipesTableQuery);
    }

    /**
     * create latest yummys table
     * @param sqLiteDatabase
     */
    private void createLatestYummysTable(SQLiteDatabase sqLiteDatabase) {
        String createRecipesTableQuery = "create table " + LATEST_YUMMY_TABLE_NAME + " ( " +
                RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT not null, " +
                LATEST_INDEX + " int, " +
                PRODUCT_ID + " text, " +
                RECIPE_NAME + " text, " +
                RECIPE_DESCRIPTION + " text, " +
                INGREDIENTS + " text, " +
                INSTRUCTIONS + " text, " +
                CATEGORY_ID + " int, " +
                ADDED_DATE + " text, " +
                RATINGS + " int, " +
                IMAGE_URL + " text " +
                ");";
        sqLiteDatabase.execSQL(createRecipesTableQuery);
    }

    /**
     * create recipe categories table
     * @param sqLiteDatabase
     */
    private void createCategoriesTable(SQLiteDatabase sqLiteDatabase) {
        String createCategoriesTableQuery = "create table " + CATEGORY_TABLE_NAME + " ( " +
                CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT not null, " +
                CATEGORY_PRODUCT_ID + " int, " +
                CATEGORY_NAME + " text " +
                ");";
        sqLiteDatabase.execSQL(createCategoriesTableQuery);
    }

    /**
     * create last database modification details table
     * @param sqLiteDatabase
     */
    private void createLastDatabaseModificationDetailsTable(SQLiteDatabase sqLiteDatabase) {
        String createModificationDetailTableQuery = "create table " + LAST_MODIFIED_DETAILS_TABLE_NAME + " ( " +
                MODIFICATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT not null, " +
                MODIFIED_TIME_STAMP + " text " +
                ");";
        sqLiteDatabase.execSQL(createModificationDetailTableQuery);
    }

    /**
     * return last modified time stamp saved in local database
     * @return
     */
    public String getLastModificationTimeStamp() {
        localFKFDatabase = this.getWritableDatabase();
        String lastModificationTimeStamp = null;

        try {
            String grepLastModificationDetailsQuery = "select * from " + LAST_MODIFIED_DETAILS_TABLE_NAME;
            Cursor lastModificationDetailCursor = localFKFDatabase.rawQuery(grepLastModificationDetailsQuery, null);
            lastModificationDetailCursor.moveToFirst();

            if(!lastModificationDetailCursor.isAfterLast()) {
                do {
                    int modificationId = lastModificationDetailCursor.getInt(0);
                    String timeStamp = lastModificationDetailCursor.getString(1);

                    lastModificationTimeStamp = timeStamp;
                } while (lastModificationDetailCursor.moveToNext());
            }
            lastModificationDetailCursor.close();

        } catch (SQLiteException ex) {
            throw ex;
        }

        return lastModificationTimeStamp;
    }

    /**
     * set new modified time stamp to local database
     * @param newModifiedTimeStamp
     */
    public void setLastModificationTimeStamp(String newModifiedTimeStamp) {

        localFKFDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(MODIFIED_TIME_STAMP, newModifiedTimeStamp);

        try {
            localFKFDatabase.insert(LAST_MODIFIED_DETAILS_TABLE_NAME, null, values);
        } catch (SQLiteException ex) {
            throw ex;
        }
    }

    /**
     * delete last modified time stamp from the local database
     */
    public void deleteLastModifiedTimeStamp() {
        localFKFDatabase = this.getWritableDatabase();
        try {
            localFKFDatabase.delete(LAST_MODIFIED_DETAILS_TABLE_NAME, null, null);
        } catch (SQLiteException ex) {
            throw ex;
        }
    }

    /**
     * get all recipe categories for the item menu
     * @return
     */
    public ArrayList<String> getAllCategories(){
        ArrayList<String> categoryList = new ArrayList<String>();
        categoryList.add("Latest Yummys"); //default item for view the latest yummys and other stuff
        //If user logged in to application
        if(LoginActivity.LOGGED_STATUS == 1){
            categoryList.add("My Favorites");
        }
        localFKFDatabase = this.getWritableDatabase();

        try {
            String grepCategoriesQry = "select * from " + CATEGORY_TABLE_NAME;
            Cursor categoryCursor = localFKFDatabase.rawQuery(grepCategoriesQry, null);

            categoryCursor.moveToFirst();

            if(!categoryCursor.isAfterLast()) {
                do {
                    int categoryId = categoryCursor.getInt(1);
                    String categoryName = categoryCursor.getString(2);
                    categoryList.add(categoryId+ ":" + categoryName);
                } while (categoryCursor.moveToNext());
            }
            categoryCursor.close();
        } catch (SQLiteException ex) {
            throw ex;
        }

        return categoryList;
    }

    /**
     * Add new category from the given category product id and category name
     * @param categoryProductId
     * @param categoryName
     */
    public void addNewCategory(int categoryProductId, String categoryName) {

        localFKFDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CATEGORY_PRODUCT_ID, categoryProductId);
        values.put(CATEGORY_NAME, categoryName);

        try {
            localFKFDatabase.insert(CATEGORY_TABLE_NAME, null, values);
        } catch (SQLiteException ex) {
            throw ex;
        }
    }

    /**
     * delete current categories from the local database
     */
    public void deleteAllCategories() {
        localFKFDatabase = this.getWritableDatabase();
        try {
            localFKFDatabase.delete(CATEGORY_TABLE_NAME, null, null);
        } catch (SQLiteException ex) {
            throw ex;
        }
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
                    String productId = recipeCursor.getString(1);
                    String recipeName = recipeCursor.getString(2);
                    String recipeDescription = recipeCursor.getString(3);
                    String recipeIngredients = recipeCursor.getString(4);
                    String recipeInstructions = recipeCursor.getString(5);
                    int recipeCategoryId = recipeCursor.getInt(6);
                    String recipeAddedDate = recipeCursor.getString(7);
                    int recipeRatings = recipeCursor.getInt(8);
                    String recipeImageUrl = recipeCursor.getString(9);

                    recipe = new Recipe();

                    recipe.setId(recipeId);
                    recipe.setProductId(productId);
                    recipe.setName(recipeName);
                    recipe.setDescription(recipeDescription);
                    recipe.setIngredients(recipeIngredients);
                    recipe.setInstructions(recipeInstructions);
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
                    String productId = recipeCursor.getString(1);
                    String recipeName = recipeCursor.getString(2);
                    String recipeDescription = recipeCursor.getString(3);
                    String recipeIngredients = recipeCursor.getString(4);
                    String recipeInstructions = recipeCursor.getString(5);
                    int recipeCategoryId = recipeCursor.getInt(6);
                    String recipeAddedDate = recipeCursor.getString(7);
                    int recipeRatings = recipeCursor.getInt(8);
                    String recipeImageUrl = recipeCursor.getString(9);

                    recipe = new Recipe();

                    recipe.setId(recipeId);
                    recipe.setProductId(productId);
                    recipe.setName(recipeName);
                    recipe.setDescription(recipeDescription);
                    recipe.setIngredients(recipeIngredients);
                    recipe.setInstructions(recipeInstructions);
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

    public ArrayList<Recipe> searchRecipesFromGivenRecipeName(String key) {
        ArrayList<Recipe> selectedRecipeList = new ArrayList<Recipe>();
        localFKFDatabase = this.getWritableDatabase();

        try {
            String qry = "SELECT * FROM " + RECIPES_TABLE_NAME + " WHERE " + RECIPE_NAME + " LIKE '%" + key + "%'";
            Cursor recipeCursor = localFKFDatabase.rawQuery(qry, null);

            recipeCursor.moveToFirst();
            Recipe recipe;

            if(!recipeCursor.isAfterLast()) {
                do {
                    int recipeId = recipeCursor.getInt(0);
                    String productId = recipeCursor.getString(1);
                    String recipeName = recipeCursor.getString(2);
                    String recipeDescription = recipeCursor.getString(3);
                    String recipeIngredients = recipeCursor.getString(4);
                    String recipeInstructions = recipeCursor.getString(5);
                    int recipeCategoryId = recipeCursor.getInt(6);
                    String recipeAddedDate = recipeCursor.getString(7);
                    int recipeRatings = recipeCursor.getInt(8);
                    String recipeImageUrl = recipeCursor.getString(9);

                    recipe = new Recipe();

                    recipe.setId(recipeId);
                    recipe.setProductId(productId);
                    recipe.setName(recipeName);
                    recipe.setDescription(recipeDescription);
                    recipe.setIngredients(recipeIngredients);
                    recipe.setInstructions(recipeInstructions);
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
        values.put(PRODUCT_ID, recipe.getProductId());
        values.put(RECIPE_NAME, recipe.getName());
        values.put(RECIPE_DESCRIPTION, recipe.getDescription());
        values.put(INGREDIENTS, recipe.getIngredients());
        values.put(INSTRUCTIONS, recipe.getInstructions());
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
     * save the given latest recipe from the caller function
     * @param latestRecipes
     */
    public void saveLatestYummyRecipe(List<Recipe> latestRecipes) {

        localFKFDatabase = this.getWritableDatabase();
        ContentValues values;

        int latestRecipeCount = 1;
        for (Recipe latestRecipe : latestRecipes) {
            values = new ContentValues();
            values.put(LATEST_INDEX, latestRecipeCount);
            values.put(PRODUCT_ID, latestRecipe.getProductId());
            values.put(RECIPE_NAME, latestRecipe.getName());
            values.put(RECIPE_DESCRIPTION, latestRecipe.getDescription());
            values.put(INGREDIENTS, latestRecipe.getIngredients());
            values.put(INSTRUCTIONS, latestRecipe.getInstructions());
            values.put(CATEGORY_ID, latestRecipe.getCategoryId());
            values.put(ADDED_DATE, "26-06-2013");
            values.put(RATINGS, latestRecipe.getRatings());
            values.put(IMAGE_URL, latestRecipe.getImageUrl());

            try {
                localFKFDatabase.insert(LATEST_YUMMY_TABLE_NAME, null, values);
            } catch (SQLiteException ex) {
                throw ex;
            }

            latestRecipeCount++;
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
