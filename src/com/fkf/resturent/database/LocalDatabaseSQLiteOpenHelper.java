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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    //last login details table columns
    public static final String LOGIN_DETAIL_TABLE_NAME = "last_login_details";
    public static final String LOGIN_ID = "id";
    public static final String LOGIN_USER_ID = "user_id";
    public static final String LOGIN_STATUS = "login_status";
    public static final String LAST_LOGIN_USERNAME = "last_login_username";
    public static final String LAST_LOGIN_PASSWORD = "last_login_password";

    //logged user favorite recipes table columns
    public static final String USER_FAVORITE_RECIPES_TABLE_NAME = "user_favorite_recipes";
    public static final String ID = "id";
    // other columns are get from the recipes table column names

    public LocalDatabaseSQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        createRecipesTable(sqLiteDatabase);
        createCategoriesTable(sqLiteDatabase);
        createLastDatabaseModificationDetailsTable(sqLiteDatabase);
        createLastLoginDetailsTable(sqLiteDatabase);
        createPopularYummysTable(sqLiteDatabase);
        createLatestYummysTable(sqLiteDatabase);
        createUserFavoriteRecipesTable(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }





                            /******************************************************/
                            /********* Database table creation methods ************/
                            /******************************************************/

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
                PRODUCT_ID + " text " +
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
                PRODUCT_ID + " text " +
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
     * create last login details table
     * @param sqLiteDatabase
     */
    private void createLastLoginDetailsTable(SQLiteDatabase sqLiteDatabase) {
        String createLastLoginTableQuery = "create table " + LOGIN_DETAIL_TABLE_NAME + " ( " +
                LOGIN_ID + " int not null, " +
                LOGIN_STATUS + " int, " +
                LOGIN_USER_ID + " text, " +
                LAST_LOGIN_USERNAME + " text, " +
                LAST_LOGIN_PASSWORD + " text " +
                ");";
        sqLiteDatabase.execSQL(createLastLoginTableQuery);
    }

    /**
     * create logged user favorite recipes table
     * @param sqLiteDatabase
     */
    private void createUserFavoriteRecipesTable(SQLiteDatabase sqLiteDatabase) {
        String createUserFavoriteRecipesTableQuery = "create table " + USER_FAVORITE_RECIPES_TABLE_NAME + " ( " +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT not null, " +
                PRODUCT_ID + " text, " +
                LOGIN_USER_ID + " text " +
                ");";
        sqLiteDatabase.execSQL(createUserFavoriteRecipesTableQuery);
    }





                            /***************************************************/
                            /********* Database functioning methods ************/
                            /***************************************************/



    /**********************************************************/
    /********* Last Modified details table methods ************/
    /**********************************************************/
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




    /**************************************************/
    /********* Login details table methods ************/
    /**************************************************/
    /**
     * add last login detail record to system
     * @param data
     * @return
     */
    public boolean insertLoginDetails(Map<String, String> data) {

        boolean result = false;
        localFKFDatabase = this.getWritableDatabase();
        assert(data != null);
        ContentValues values = new ContentValues();
        values.put(LOGIN_ID, 1);
        values.put(LOGIN_STATUS, data.get("loginStatus"));
        values.put(LOGIN_USER_ID, data.get("userId"));
        values.put(LAST_LOGIN_USERNAME, data.get("username"));
        values.put(LAST_LOGIN_PASSWORD, data.get("password"));

        try {
            localFKFDatabase.insert(LOGIN_DETAIL_TABLE_NAME, null, values);
            result = true;
        } catch (SQLiteException ex) {
            result = false;
            throw ex;
        }

        return result;
    }

    /**
     * get last login detail record
     * @return
     */
    public Map<String, String> getLoginDetails(){

        Map<String, String> detailsMap = new HashMap<String, String>();
        localFKFDatabase = this.getWritableDatabase();
        try {
            String getLoginDetailsQry = "select * from " + LOGIN_DETAIL_TABLE_NAME + " where " + LOGIN_ID + " = 1";
            Cursor loginDetailsCursor = localFKFDatabase.rawQuery(getLoginDetailsQry, null);
            loginDetailsCursor.moveToFirst();

            if(!loginDetailsCursor.isAfterLast()){
                do {
                    detailsMap.put("loginStatus", loginDetailsCursor.getString(1));
                    detailsMap.put("userId", loginDetailsCursor.getString(2));
                    detailsMap.put("username", loginDetailsCursor.getString(3));
                    detailsMap.put("password", loginDetailsCursor.getString(4));
                } while (loginDetailsCursor.moveToNext());
            }
            loginDetailsCursor.close();
        } catch (SQLiteException ex) {
            detailsMap = null;
            throw ex;
        }

        return detailsMap;
    }

    /**
     * delete login details from the database
     */
    public void deleteLoginDetails(){
        localFKFDatabase = this.getWritableDatabase();
        try {
            localFKFDatabase.delete(LOGIN_DETAIL_TABLE_NAME, LOGIN_ID + "=1",null);
        } catch (SQLiteException ex) {
            throw ex;
        }
    }



    /****************************************************/
    /********* Recipe category table methods ************/
    /****************************************************/
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
     * get all recipe categories for the item menu
     * @return
     */
    public ArrayList<RecipeCategory> getAllCategories(){
        ArrayList<RecipeCategory> recipeCategoryList = new ArrayList<RecipeCategory>();

        RecipeCategory latestYummy = new RecipeCategory();
        latestYummy.setCategoryName("Latest Yummys");
        latestYummy.setCategoryId(-1);
        recipeCategoryList.add(latestYummy);

        //If user logged in to application
        if(LoginActivity.LOGGED_STATUS == 1){
            RecipeCategory myFavorite = new RecipeCategory();
            myFavorite.setCategoryName("My Favorites");
            myFavorite.setCategoryId(-2);
            recipeCategoryList.add(myFavorite);
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

                    RecipeCategory recipeCategory = new RecipeCategory();
                    recipeCategory.setCategoryId(categoryId);
                    recipeCategory.setCategoryName(categoryName);

                    recipeCategoryList.add(recipeCategory);

                } while (categoryCursor.moveToNext());
            }
            categoryCursor.close();
        } catch (SQLiteException ex) {
            throw ex;
        }

        return recipeCategoryList;
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





    /********************************************/
    /********* Recipes table methods ************/
    /********************************************/
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

    public ArrayList<Recipe> getRecipeFromRecipeProductId(String selectedRecipeId) {
        ArrayList<Recipe> selectedRecipeList = new ArrayList<Recipe>();
        localFKFDatabase = this.getWritableDatabase();

        try {
            String grepRecipeQry = "select * from " + RECIPES_TABLE_NAME + " where " + PRODUCT_ID + " = " + selectedRecipeId;
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
            recipeCursor.close();
        } catch (SQLiteException ex) {

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



    /***************************************************/
    /********* Latest Recipes table methods ************/
    /***************************************************/
    /**
     * save the given latest recipe from the caller function
     * @param latestRecipesProductIds
     */
    public void saveLatestYummyRecipe(List<String> latestRecipesProductIds) {

        localFKFDatabase = this.getWritableDatabase();
        ContentValues values;

        int latestRecipeCount = 1;
        for (String latestRecipesProductId : latestRecipesProductIds) {
            values = new ContentValues();
            values.put(LATEST_INDEX, latestRecipeCount);
            values.put(PRODUCT_ID, latestRecipesProductId);

            try {
                localFKFDatabase.insert(LATEST_YUMMY_TABLE_NAME, null, values);
            } catch (SQLiteException ex) {
                throw ex;
            }

            latestRecipeCount++;
        }

    }





    /***************************************************/
    /********* Popular Recipes table methods ************/
    /***************************************************/
    /**
     * save the given popular recipe from the caller function
     * @param popularRecipesProductIds
     */
    public void savePopularYummyRecipe(List<String> popularRecipesProductIds) {

        localFKFDatabase = this.getWritableDatabase();
        ContentValues values;

        int popularRecipeCount = 1;
        for (String popularRecipesProductId : popularRecipesProductIds) {
            values = new ContentValues();
            values.put(POPULAR_INDEX, popularRecipeCount);
            values.put(PRODUCT_ID, popularRecipesProductId);

            try {
                localFKFDatabase.insert(POPULAR_YUMMY_TABLE_NAME, null, values);
            } catch (SQLiteException ex) {
                throw ex;
            }

            popularRecipeCount++;
        }

    }




    /**********************************************************/
    /********* User favorite Recipes table methods ************/
    /**********************************************************/
    /**
     *
     * @param recipeProductId
     * @param userId
     */
    public void saveUserFavoriteRecipes(String recipeProductId, String userId) {

        localFKFDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(PRODUCT_ID, recipeProductId);
        values.put(LOGIN_USER_ID, userId);

        try {
            localFKFDatabase.insert(USER_FAVORITE_RECIPES_TABLE_NAME, null, values);
        } catch (SQLiteException ex) {
            throw ex;
        }
    }

    /**
     * query favorite recipe ids from local database and return them as string list
     * @return
     */
    public List<String> getLoggedUserFavoriteRecipeIds() {
        localFKFDatabase = this.getWritableDatabase();
        List<String> favoriteRecipeIds = new ArrayList<String>();

        try {
            String getUserFavoriteRecipesQuery = "SELECT * FROM " + USER_FAVORITE_RECIPES_TABLE_NAME;
            Cursor favoriteRecipesCursor = localFKFDatabase.rawQuery(getUserFavoriteRecipesQuery, null);

            favoriteRecipesCursor.moveToFirst();

            if(!favoriteRecipesCursor.isAfterLast()) {

                do{
                    String favoriteRecipeProductId = favoriteRecipesCursor.getString(1);
                    favoriteRecipeIds.add(favoriteRecipeProductId);
                } while (favoriteRecipesCursor.moveToNext());
            }
            favoriteRecipesCursor.close();
        } catch (SQLiteException ex) {
            throw ex;
        }

        return favoriteRecipeIds;
    }

    /**
     * check given recipe is favorite recipe of the logged user
     * @param recipeProductId
     * @param userId
     * @return
     */
    public boolean isUserFavoriteRecipe(String recipeProductId, String userId) {
        boolean isFavorite = false;
        localFKFDatabase = this.getWritableDatabase();

        try {
            String isFavoriteCheckQuery = "SELECT * FROM " + USER_FAVORITE_RECIPES_TABLE_NAME + " WHERE " + PRODUCT_ID + "=" + recipeProductId
                    + " AND " + LOGIN_USER_ID + "=" + userId;

            Cursor favoriteRecipeCursor = localFKFDatabase.rawQuery(isFavoriteCheckQuery, null);

            if(!favoriteRecipeCursor.isAfterLast()) {
                isFavorite = true;
            } else {
                isFavorite = false;
            }
            favoriteRecipeCursor.close();
        } catch (SQLiteException ex) {
            throw ex;
        }

        return isFavorite;
    }

    public void removeFromUserFavorite(String recipeProductId) {
        localFKFDatabase = this.getWritableDatabase();
        try {
            String where = PRODUCT_ID + " = '" + recipeProductId + "'";
            localFKFDatabase.delete(USER_FAVORITE_RECIPES_TABLE_NAME, where, null);
        } catch (SQLiteException ex) {
            throw ex;
        }
    }

    /**
     * delete user favorite recipe table items from the local database
     */
    public void deleteAllUserFavoriteRecipes() {
        localFKFDatabase = this.getWritableDatabase();
        try {
            localFKFDatabase.delete(USER_FAVORITE_RECIPES_TABLE_NAME, null, null);
        } catch (SQLiteException ex) {
            throw ex;
        }
    }
}
