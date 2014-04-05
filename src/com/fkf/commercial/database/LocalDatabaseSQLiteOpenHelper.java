package com.fkf.commercial.database;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import com.fkf.commercial.database.dbprovider.DbContentProvider;

import java.io.*;
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

    public static final String DB_PATH = "/data/data/com.fkf.commercial/databases/";
    private final Context dbContext;

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
    public static final String IMAGE_URL_T = "image_url_t";
    public static final String IMAGE_URL_XS = "image_url_xs";
    public static final String IMAGE_URL_S = "image_url_s";
    public static final String IMAGE_URL_M = "image_url_m";
    public static final String IMAGE_URL_L = "image_url_l";
    public static final String IMAGE_URL_XL = "image_url_xl";
    public static final String LINKED_IMAGES = "linked_images";
    public static final String LINKED_RECIPE_IDS = "linked_recipes";
    public static final String LEGACY = "legacy";
    public static final String BODY = "body";

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
    public static final String LOGIN_USER_PIC_URL = "user_pic_url";
    public static final String LOGIN_STATUS = "login_status";
    public static final String LAST_LOGIN_USERNAME = "last_login_username";
    public static final String LAST_LOGIN_PASSWORD = "last_login_password";
    public static final String LAST_LOGIN_NAME = "user_f_name";

    //logged user favorite recipes table columns
    public static final String USER_FAVORITE_RECIPES_TABLE_NAME = "user_favorite_recipes";
    public static final String ID = "id";
    // other columns are get from the recipes table column names

    //Database updated date table columns
    public static final String UPDATED_DATE_TABLE_NAME = "updated_date";
    public static final String UPDATED_ID = "updated_id";
    public static final String UPDATED_YEAR = "updated_year";
    public static final String UPDATED_MONTH = "updated_month";
    public static final String UPDATED_DAY = "updated_day";

    public LocalDatabaseSQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
        this.dbContext = context;
    }

    /***********************************************************************************************/
    /************************ Database creation from provided .sqlite file *************************/
    /***********************************************************************************************/
    /**
     * create database from provided database file
     * @throws IOException
     */
    public void createDatabase() throws IOException {
        boolean dbExist = checkDataBase();

        if(!dbExist){
            this.getReadableDatabase();

            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    /**
     * check the database availability
     * @return
     */
    public boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteCantOpenDatabaseException ex) {
            Log.d("Database Status : ", "database doesn't exist yet.");
        } catch(SQLiteException e){
            //database does't exist yet.
            Log.d("Database Status : ", "database doesn't exist yet.");
        }

        if(checkDB != null){
            checkDB.close();
        }

        return checkDB != null ? true : false;
    }

    /**
     * copy the database from assets
     * @throws IOException
     */
    private void copyDataBase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput = dbContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    /**
     * open created database
     * @throws SQLiteException
     */
    public void openDataBase() throws SQLiteException{
        //Open the database
        String myPath = DB_PATH + DB_NAME;
        localFKFDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    /**
     * backup the database (only for application dev usage)
     * @throws IOException
     */
    public void backupDatabase() throws IOException {
        String inFileName = DB_PATH + DB_NAME;
        File dbFile = new File(inFileName);
        FileInputStream fis = new FileInputStream(dbFile);

        String outFileName = Environment.getExternalStorageDirectory()+"/MYDB";

        OutputStream output = new FileOutputStream(outFileName);
        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fis.read(buffer))>0){
            output.write(buffer, 0, length);
        }
        //Close the streams
        output.flush();
        output.close();
        fis.close();
    }

    @Override
    public synchronized void close() {

        if(localFKFDatabase != null)
            localFKFDatabase.close();

        super.close();

    }
    /***********************************************************************************************/

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        /*createRecipesTable(sqLiteDatabase);
        createCategoriesTable(sqLiteDatabase);
        createLastDatabaseModificationDetailsTable(sqLiteDatabase);
        createLastLoginDetailsTable(sqLiteDatabase);
        createPopularYummysTable(sqLiteDatabase);
        createLatestYummysTable(sqLiteDatabase);
        createUserFavoriteRecipesTable(sqLiteDatabase);
        createUpdatedDateTable(sqLiteDatabase)*/
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
                IMAGE_URL + " text, " +
                IMAGE_URL_XS + " text, " +
                IMAGE_URL_S + " text, " +
                IMAGE_URL_M + " text, " +
                IMAGE_URL_L + " text, " +
                LINKED_IMAGES + " text, " +
                LINKED_RECIPE_IDS + " text, " +
                LEGACY + " int, " +
                BODY + " text, " +
                IMAGE_URL_T + " text, " +
                IMAGE_URL_XL + " text " +
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
                IMAGE_URL_XS + " text, " +
                IMAGE_URL_S + " text, " +
                IMAGE_URL_M + " text, " +
                IMAGE_URL_L + " text, " +
                IMAGE_URL_T + " text, " +
                IMAGE_URL_XL + " text " +
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
                IMAGE_URL_XS + " text, " +
                IMAGE_URL_S + " text, " +
                IMAGE_URL_M + " text, " +
                IMAGE_URL_L + " text, " +
                IMAGE_URL_T + " text, " +
                IMAGE_URL_XL + " text " +
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
                LAST_LOGIN_PASSWORD + " text, " +
                LAST_LOGIN_NAME + " text, " +
                LOGIN_USER_PIC_URL + " text " +
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

    private void createUpdatedDateTable(SQLiteDatabase sqLiteDatabase) {
        String createUpdatedDateTableQry = "create table " + UPDATED_DATE_TABLE_NAME + " ( " +
                UPDATED_ID + " int not null, " +
                UPDATED_YEAR + " int, " +
                UPDATED_MONTH + " int, " +
                UPDATED_DAY + " int " +
                ");";
        sqLiteDatabase.execSQL(createUpdatedDateTableQry);
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
        values.put(LAST_LOGIN_NAME, data.get("fName"));
        values.put(LOGIN_USER_PIC_URL, data.get("picUrl"));

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
                    detailsMap.put("fName", loginDetailsCursor.getString(5));
                    detailsMap.put("picUrl", loginDetailsCursor.getString(6));
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
    public void addNewCategory(int categoryProductId, String categoryName, Context context) {

//        localFKFDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CATEGORY_PRODUCT_ID, categoryProductId);
        values.put(CATEGORY_NAME, categoryName);

        //using content provider database access
        Uri contextUri = Uri.withAppendedPath(DbContentProvider.CONTENT_URI, CATEGORY_TABLE_NAME);
        if(contextUri != null) {
            Uri resultUri = context.getContentResolver().insert(contextUri, values);
        }

        //using direct database access
        /*try {
            localFKFDatabase.insert(CATEGORY_TABLE_NAME, null, values);
        } catch (SQLiteException ex) {
            throw ex;
        }*/
    }

    /**
     * get all recipe categories for the item menu
     * @return
     */
    public ArrayList<RecipeCategory> getAllCategories(){
        ArrayList<RecipeCategory> recipeCategoryList = new ArrayList<RecipeCategory>();

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
                    recipeCategory.setCategoryName("  " + categoryName);

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
    public void deleteAllCategories(Context context) {

        //using content provider database access
        Uri contextUri = Uri.withAppendedPath(DbContentProvider.CONTENT_URI, CATEGORY_TABLE_NAME);
        if(contextUri != null) {
            context.getContentResolver().delete(contextUri, null, null);
        }

        /*localFKFDatabase = this.getWritableDatabase();
        try {
            localFKFDatabase.delete(CATEGORY_TABLE_NAME, null, null);
        } catch (SQLiteException ex) {
            throw ex;
        }*/
    }





    /********************************************/
    /********* Recipes table methods ************/
    /********************************************/
    /**
     * save the given recipe from caller function
     * @param recipe
     */
    public void saveRecipe(Recipe recipe, Context context) {

//        localFKFDatabase = this.getWritableDatabase();

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
        values.put(IMAGE_URL_XS, recipe.getImageUrl_xs());
        values.put(IMAGE_URL_S, recipe.getImageUrl_s());
        values.put(IMAGE_URL_M, recipe.getImageUrl_m());
        values.put(IMAGE_URL_L, recipe.getImageUrl_l());
        values.put(LINKED_IMAGES, recipe.getLinkImages());
        values.put(LINKED_RECIPE_IDS, recipe.getLinkRecipeIds());
        values.put(LEGACY, recipe.getLegacy());
        values.put(BODY, recipe.getBody());

        //using content provider database access
        Uri contextUri = Uri.withAppendedPath(DbContentProvider.CONTENT_URI, RECIPES_TABLE_NAME);
        if(contextUri != null) {
            context.getContentResolver().insert(contextUri, values);
        }

        //using direct database access
        /*try {
            localFKFDatabase.insert(RECIPES_TABLE_NAME, null, values);
        } catch (SQLiteException ex) {
            throw ex;
        }*/
    }

    /**
     * get recipes from given category id
     * @param categoryId
     * @return
     */
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
                    String recipeImageUrl_xs = recipeCursor.getString(10);
                    String recipeImageUrl_s = recipeCursor.getString(11);
                    String recipeImageUrl_m = recipeCursor.getString(12);
                    String recipeImageUrl_l = recipeCursor.getString(13);
                    String linkedImages = recipeCursor.getString(14);
                    String linkedRecipes = recipeCursor.getString(15);
                    int legacy = recipeCursor.getInt(16);
                    String recipeBody = recipeCursor.getString(17);
                    String recipeImageUrl_t = recipeCursor.getString(18);
                    String recipeImageUrl_xl = recipeCursor.getString(19);

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
                    recipe.setImageUrl_xs(recipeImageUrl_xs);
                    recipe.setImageUrl_s(recipeImageUrl_s);
                    recipe.setImageUrl_m(recipeImageUrl_m);
                    recipe.setImageUrl_l(recipeImageUrl_l);
                    recipe.setLinkImages(linkedImages);
                    recipe.setLinkRecipeIds(linkedRecipes);
                    recipe.setLegacy(legacy);
                    recipe.setBody(recipeBody);
                    recipe.setImageUrlT(recipeImageUrl_t);
                    recipe.setImageUrl_xl(recipeImageUrl_xl);

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

    /**
     * get recipes from recipe id
     * @param selectedRecipeId
     * @return
     */
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
                    String recipeImageUrl_xs = recipeCursor.getString(10);
                    String recipeImageUrl_s = recipeCursor.getString(11);
                    String recipeImageUrl_m = recipeCursor.getString(12);
                    String recipeImageUrl_l = recipeCursor.getString(13);

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
                    recipe.setImageUrl_xs(recipeImageUrl_xs);
                    recipe.setImageUrl_s(recipeImageUrl_s);
                    recipe.setImageUrl_m(recipeImageUrl_m);
                    recipe.setImageUrl_l(recipeImageUrl_l);

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
     * get recipes from given recipe product id
     * @param selectedRecipeId
     * @return
     */
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
                    String recipeImageUrl_xs = recipeCursor.getString(10);
                    String recipeImageUrl_s = recipeCursor.getString(11);
                    String recipeImageUrl_m = recipeCursor.getString(12);
                    String recipeImageUrl_l = recipeCursor.getString(13);
                    String linkedImages = recipeCursor.getString(14);
                    String linkedRecipes = recipeCursor.getString(15);
                    int legacy = recipeCursor.getInt(16);
                    String body = recipeCursor.getString(17);
                    String recipeImageUrl_t = recipeCursor.getString(18);
                    String recipeImageUrl_xl = recipeCursor.getString(19);

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
                    recipe.setImageUrl_xs(recipeImageUrl_xs);
                    recipe.setImageUrl_s(recipeImageUrl_s);
                    recipe.setImageUrl_m(recipeImageUrl_m);
                    recipe.setImageUrl_l(recipeImageUrl_l);
                    recipe.setLinkImages(linkedImages);
                    recipe.setLinkRecipeIds(linkedRecipes);
                    recipe.setLegacy(legacy);
                    recipe.setBody(body);
                    recipe.setImageUrlT(recipeImageUrl_t);
                    recipe.setImageUrl_xl(recipeImageUrl_xl);

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
     * search recipe from given part of the recipe name
     * @param key
     * @return
     */
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
                    String recipeImageUrl_xs = recipeCursor.getString(10);
                    String recipeImageUrl_s = recipeCursor.getString(11);
                    String recipeImageUrl_m = recipeCursor.getString(12);
                    String recipeImageUrl_l = recipeCursor.getString(13);
                    String linkedImages = recipeCursor.getString(14);
                    String linkedRecipes = recipeCursor.getString(15);
                    int legacy = recipeCursor.getInt(16);
                    String body = recipeCursor.getString(17);
                    String recipeImageUrl_t = recipeCursor.getString(18);
                    String recipeImageUrl_xl = recipeCursor.getString(19);

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
                    recipe.setImageUrl_xs(recipeImageUrl_xs);
                    recipe.setImageUrl_s(recipeImageUrl_s);
                    recipe.setImageUrl_m(recipeImageUrl_m);
                    recipe.setImageUrl_l(recipeImageUrl_l);
                    recipe.setLinkImages(linkedImages);
                    recipe.setLinkRecipeIds(linkedRecipes);
                    recipe.setLegacy(legacy);
                    recipe.setBody(body);
                    recipe.setImageUrlT(recipeImageUrl_t);
                    recipe.setImageUrl_xl(recipeImageUrl_xl);

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
     * @param latestRecipes
     */
    public void saveLatestYummyRecipe(List<PopularOrLatestRecipe> latestRecipes, Activity activity) {

        Context context = activity.getApplicationContext();
//        localFKFDatabase = this.getWritableDatabase();
        ContentValues values;

        int latestRecipeCount = 1;
        for (PopularOrLatestRecipe latestRecipe : latestRecipes) {
            values = new ContentValues();
            values.put(LATEST_INDEX, latestRecipeCount);
            values.put(PRODUCT_ID, latestRecipe.getProductId());
            values.put(RECIPE_NAME, latestRecipe.getRecipeName());
            values.put(IMAGE_URL_XS, latestRecipe.getImageUrlXS());
            values.put(IMAGE_URL_S, latestRecipe.getImageUrlS());
            values.put(IMAGE_URL_M, latestRecipe.getImageUrlM());
            values.put(IMAGE_URL_L, latestRecipe.getImageUrlL());

            //using content provider database access
            Uri contextUri = Uri.withAppendedPath(DbContentProvider.CONTENT_URI, LATEST_YUMMY_TABLE_NAME);
            if(contextUri != null) {
                context.getContentResolver().insert(contextUri, values);
            }

            //using direct database access
            /*try {
                localFKFDatabase.insert(LATEST_YUMMY_TABLE_NAME, null, values);
            } catch (SQLiteException ex) {
                throw ex;
            }*/

            latestRecipeCount++;
        }
    }

    /**
     * get latest recipeId from given latest index
     * @param index
     * @return
     */
    public String getLatestRecipeFromIndex(int index) {

        String recipeProductId = "";
        List<String> recipeProductIdList = new ArrayList<String>();
        localFKFDatabase = this.getWritableDatabase();

        try {
            String getLatestRecipeQry = "SELECT * FROM " + LATEST_YUMMY_TABLE_NAME + " WHERE " + LATEST_INDEX + " = " + index;
            Cursor latestRecipeCursor = localFKFDatabase.rawQuery(getLatestRecipeQry, null);

            latestRecipeCursor.moveToFirst();
            if(!latestRecipeCursor.isAfterLast()) {
                do {
                    recipeProductIdList.add(latestRecipeCursor.getString(2));
                } while (latestRecipeCursor.moveToNext());
            }

            if(!recipeProductIdList.isEmpty()) {
                recipeProductId = recipeProductIdList.get(0);
            }
            latestRecipeCursor.close();
        } catch (SQLiteException ex) {
            throw ex;
        }
        return recipeProductId;
    }

    /**
     * get all latest recipes list
     * @return
     */
    public List<PopularOrLatestRecipe> getAllLatestRecipes() {

        List<PopularOrLatestRecipe> latestRecipesList = new ArrayList<PopularOrLatestRecipe>();
        localFKFDatabase = this.getWritableDatabase();

        try {
            String getLatestRecipeQry = "SELECT * FROM " + LATEST_YUMMY_TABLE_NAME;
            Cursor latestRecipeCursor = localFKFDatabase.rawQuery(getLatestRecipeQry, null);

            latestRecipeCursor.moveToFirst();
            if(!latestRecipeCursor.isAfterLast()) {
                do {
                    int index = latestRecipeCursor.getInt(1);
                    String productId = latestRecipeCursor.getString(2);
                    String recipeName = latestRecipeCursor.getString(3);
                    String imageUrlXS = latestRecipeCursor.getString(4);
                    String imageUrlS = latestRecipeCursor.getString(5);
                    String imageUrlM = latestRecipeCursor.getString(6);
                    String imageUrlL = latestRecipeCursor.getString(7);
                    String imageUrlXL = latestRecipeCursor.getString(8);

                    PopularOrLatestRecipe popularOrLatestRecipe = new PopularOrLatestRecipe();
                    popularOrLatestRecipe.setIndex(index);
                    popularOrLatestRecipe.setProductId(productId);
                    popularOrLatestRecipe.setRecipeName(recipeName);
                    popularOrLatestRecipe.setImageUrlXS(imageUrlXS);
                    popularOrLatestRecipe.setImageUrlS(imageUrlS);
                    popularOrLatestRecipe.setImageUrlM(imageUrlM);
                    popularOrLatestRecipe.setImageUrlL(imageUrlL);
                    popularOrLatestRecipe.setImageUrlXL(imageUrlXL);

                    latestRecipesList.add(popularOrLatestRecipe);
                } while (latestRecipeCursor.moveToNext());
            }

            latestRecipeCursor.close();
        } catch (SQLiteException ex) {
            throw ex;
        }

        return latestRecipesList;
    }


    /**
     * delete all latest recipes
     */
    public void deleteAllLatestRecipes() {
        localFKFDatabase = this.getWritableDatabase();
        try {
            localFKFDatabase.delete(LATEST_YUMMY_TABLE_NAME, null, null);
        } catch (SQLiteException ex) {
            throw ex;
        }
    }


    /***************************************************/
    /********* Popular Recipes table methods ************/
    /***************************************************/
    /**
     * save the given popular recipe from the caller function
     * @param popularRecipes
     */
    public void savePopularYummyRecipe(List<PopularOrLatestRecipe> popularRecipes, Activity activity) {

        Context context = activity.getApplicationContext();
//        localFKFDatabase = this.getWritableDatabase();
        ContentValues values;

        int popularRecipeCount = 1;
        for (PopularOrLatestRecipe popularRecipe : popularRecipes) {
            values = new ContentValues();
            values.put(POPULAR_INDEX, popularRecipeCount);
            values.put(PRODUCT_ID, popularRecipe.getProductId());
            values.put(RECIPE_NAME, popularRecipe.getRecipeName());
            values.put(IMAGE_URL_XS, popularRecipe.getImageUrlXS());
            values.put(IMAGE_URL_S, popularRecipe.getImageUrlS());
            values.put(IMAGE_URL_M, popularRecipe.getImageUrlM());
            values.put(IMAGE_URL_L, popularRecipe.getImageUrlL());

            //using content provider database access
            Uri contextUri = Uri.withAppendedPath(DbContentProvider.CONTENT_URI, POPULAR_YUMMY_TABLE_NAME);
            if(contextUri != null) {
                context.getContentResolver().insert(contextUri, values);
            }

            //using direct access to database
            /*try {
                localFKFDatabase.insert(POPULAR_YUMMY_TABLE_NAME, null, values);
            } catch (SQLiteException ex) {
                throw ex;
            }*/

            popularRecipeCount++;
        }

    }

    /**
     * get popular recipe product id from given index
     * @param index
     * @return
     */
    public String getPopularRecipeFromIndex(int index) {

        String recipeProductId = "";
        List<String> recipeProductIdList = new ArrayList<String>();
        localFKFDatabase = this.getWritableDatabase();

        try {
            String getPopularRecipeQry = "SELECT * FROM " + POPULAR_YUMMY_TABLE_NAME + " WHERE " + POPULAR_INDEX + " = " + index;
            Cursor popularRecipeCursor = localFKFDatabase.rawQuery(getPopularRecipeQry, null);

            popularRecipeCursor.moveToFirst();
            if(!popularRecipeCursor.isAfterLast()) {
                do {
                    recipeProductIdList.add(popularRecipeCursor.getString(2));
                } while (popularRecipeCursor.moveToNext());
            }

            if(!recipeProductIdList.isEmpty()) {
                recipeProductId = recipeProductIdList.get(0);
            }
            popularRecipeCursor.close();
        } catch (SQLiteException ex) {
            throw ex;
        }
        return recipeProductId;
    }

    /**
     * get all popular recipes
     * @return
     */
    public List<PopularOrLatestRecipe> getAllPopularRecipes() {

        List<PopularOrLatestRecipe> popularRecipesList = new ArrayList<PopularOrLatestRecipe>();
        localFKFDatabase = this.getWritableDatabase();

        try {
            String getAllPopularRecipeQry = "SELECT * FROM " + POPULAR_YUMMY_TABLE_NAME;
            Cursor popularRecipesCursor = localFKFDatabase.rawQuery(getAllPopularRecipeQry, null);

            popularRecipesCursor.moveToFirst();
            if(!popularRecipesCursor.isAfterLast()) {
                do {
                    int index = popularRecipesCursor.getInt(1);
                    String productId = popularRecipesCursor.getString(2);
                    String recipeName = popularRecipesCursor.getString(3);
                    String imageUrlXS = popularRecipesCursor.getString(4);
                    String imageUrlS = popularRecipesCursor.getString(5);
                    String imageUrlM = popularRecipesCursor.getString(6);
                    String imageUrlL = popularRecipesCursor.getString(7);
                    String imageUrlXL = popularRecipesCursor.getString(8);

                    PopularOrLatestRecipe popularOrLatestRecipe = new PopularOrLatestRecipe();
                    popularOrLatestRecipe.setIndex(index);
                    popularOrLatestRecipe.setProductId(productId);
                    popularOrLatestRecipe.setRecipeName(recipeName);
                    popularOrLatestRecipe.setImageUrlXS(imageUrlXS);
                    popularOrLatestRecipe.setImageUrlS(imageUrlS);
                    popularOrLatestRecipe.setImageUrlM(imageUrlM);
                    popularOrLatestRecipe.setImageUrlL(imageUrlL);
                    popularOrLatestRecipe.setImageUrlXL(imageUrlXL);

                    popularRecipesList.add(popularOrLatestRecipe);
                } while (popularRecipesCursor.moveToNext());
            }
            popularRecipesCursor.close();
        } catch (SQLiteException ex) {
            throw ex;
        }

        return popularRecipesList;
    }

    /**
     * delete all popular recipes
     */
    public void deleteAllPopularRecipes() {
        localFKFDatabase = this.getWritableDatabase();
        try {
            localFKFDatabase.delete(POPULAR_YUMMY_TABLE_NAME, null, null);
        } catch (SQLiteException ex) {
            throw ex;
        }
    }


    /**********************************************************/
    /********* User favorite Recipes table methods ************/
    /**********************************************************/
    /**
     * save recipe in my favorites
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

    /**
     * remove given recipe from user favorites
     * @param recipeProductId
     */
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
