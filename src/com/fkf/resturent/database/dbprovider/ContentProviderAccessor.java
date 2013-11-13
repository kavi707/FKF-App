package com.fkf.resturent.database.dbprovider;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import com.fkf.resturent.database.LocalDatabaseSQLiteOpenHelper;
import com.fkf.resturent.database.PopularOrLatestRecipe;
import com.fkf.resturent.database.Recipe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Methods handler for content provider cases
 * Created by kavi on 10/31/13.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class ContentProviderAccessor {

    /********************************************************/
    /********* Modified time stamp table methods ************/
    /********************************************************/
    /**
     * save new updated time stamp in database
     * @param newModifiedTimeStamp
     * @param context
     */
    public void saveLastModificationTimeStamp(String newModifiedTimeStamp, Context context) {
        ContentValues values = new ContentValues();
        values.put(LocalDatabaseSQLiteOpenHelper.MODIFIED_TIME_STAMP, newModifiedTimeStamp);

        Uri contextUri = Uri.withAppendedPath(DbContentProvider.CONTENT_URI, LocalDatabaseSQLiteOpenHelper.LAST_MODIFIED_DETAILS_TABLE_NAME);
        if(contextUri != null) {
            Uri resultUri = context.getContentResolver().insert(contextUri, values);
        }
    }

    /**
     * delete existing last time stamp from database
     * @param context
     */
    public void deleteLastModificationTimeStamp(Context context) {
        Uri contextUri = Uri.withAppendedPath(DbContentProvider.CONTENT_URI, LocalDatabaseSQLiteOpenHelper.LAST_MODIFIED_DETAILS_TABLE_NAME);
        if(contextUri != null) {
            context.getContentResolver().delete(contextUri, null, null);
        }
    }

    /**************************************************/
    /********* Login details table methods ************/
    /**************************************************/
    /**
     * retrieve saved login details in application
     * @param context
     * @return
     */
    public Map<String, String> getLoginDetails(Context context) {
        Map<String, String> loginDetails = new HashMap<String, String>();

        Uri contextUri = Uri.withAppendedPath(DbContentProvider.CONTENT_URI, LocalDatabaseSQLiteOpenHelper.LOGIN_DETAIL_TABLE_NAME);
        if(contextUri != null) {
            String[] selections = {
                    LocalDatabaseSQLiteOpenHelper.LOGIN_ID,
                    LocalDatabaseSQLiteOpenHelper.LOGIN_USER_ID,
                    LocalDatabaseSQLiteOpenHelper.LOGIN_STATUS,
                    LocalDatabaseSQLiteOpenHelper.LAST_LOGIN_USERNAME,
                    LocalDatabaseSQLiteOpenHelper.LAST_LOGIN_PASSWORD
            };

            String where = LocalDatabaseSQLiteOpenHelper.LOGIN_ID + " = 1";

            Cursor loginDetailsCursor = context.getContentResolver().query(contextUri,selections, where, null, null);
            if (loginDetailsCursor != null) {
                loginDetailsCursor.moveToFirst();
                if(!loginDetailsCursor.isAfterLast()) {
                    do {
                        loginDetails.put("loginStatus", loginDetailsCursor.getString(1));
                        loginDetails.put("userId", loginDetailsCursor.getString(2));
                        loginDetails.put("username", loginDetailsCursor.getString(3));
                        loginDetails.put("password", loginDetailsCursor.getString(4));
                    } while (loginDetailsCursor.moveToNext());
                }
                loginDetailsCursor.close();
            }

        }

        return loginDetails;
    }


    /****************************************************/
    /********* Recipe Category table methods ************/
    /****************************************************/
    /**
     * save new category
     * @param categoryProductId
     * @param categoryName
     * @param context
     */
    public void saveNewCategory(int categoryProductId, String categoryName, Context context) {

        ContentValues values = new ContentValues();
        values.put(LocalDatabaseSQLiteOpenHelper.CATEGORY_PRODUCT_ID, categoryProductId);
        values.put(LocalDatabaseSQLiteOpenHelper.CATEGORY_NAME, categoryName);

        Uri contextUri = Uri.withAppendedPath(DbContentProvider.CONTENT_URI, LocalDatabaseSQLiteOpenHelper.CATEGORY_TABLE_NAME);
        if(contextUri != null) {
            Uri resultUri = context.getContentResolver().insert(contextUri, values);
        }
    }

    /**
     * delete existing all categories from database
     * @param context
     */
    public void deleteAllCategories(Context context) {
        Uri contextUri = Uri.withAppendedPath(DbContentProvider.CONTENT_URI, LocalDatabaseSQLiteOpenHelper.CATEGORY_TABLE_NAME);
        if(contextUri != null) {
            context.getContentResolver().delete(contextUri, null, null);
        }
    }

    /********************************************/
    /********* Recipes table methods ************/
    /********************************************/
    /**
     * save new recipe
     * @param recipe
     * @param context
     */
    public void saveNewRecipe(Recipe recipe, Context context) {

        ContentValues values = new ContentValues();
        values.put(LocalDatabaseSQLiteOpenHelper.PRODUCT_ID, recipe.getProductId());
        values.put(LocalDatabaseSQLiteOpenHelper.RECIPE_NAME, recipe.getName());
        values.put(LocalDatabaseSQLiteOpenHelper.RECIPE_DESCRIPTION, recipe.getDescription());
        values.put(LocalDatabaseSQLiteOpenHelper.INGREDIENTS, recipe.getIngredients());
        values.put(LocalDatabaseSQLiteOpenHelper.INSTRUCTIONS, recipe.getInstructions());
        values.put(LocalDatabaseSQLiteOpenHelper.CATEGORY_ID, recipe.getCategoryId());
        values.put(LocalDatabaseSQLiteOpenHelper.ADDED_DATE, "26-06-2013");
        values.put(LocalDatabaseSQLiteOpenHelper.RATINGS, recipe.getRatings());
        values.put(LocalDatabaseSQLiteOpenHelper.IMAGE_URL, recipe.getImageUrl());
        values.put(LocalDatabaseSQLiteOpenHelper.IMAGE_URL_XS, recipe.getImageUrl_xs());
        values.put(LocalDatabaseSQLiteOpenHelper.IMAGE_URL_S, recipe.getImageUrl_s());
        values.put(LocalDatabaseSQLiteOpenHelper.IMAGE_URL_M, recipe.getImageUrl_m());
        values.put(LocalDatabaseSQLiteOpenHelper.IMAGE_URL_L, recipe.getImageUrl_l());
        values.put(LocalDatabaseSQLiteOpenHelper.LEGACY, recipe.getLegacy());
        values.put(LocalDatabaseSQLiteOpenHelper.BODY, recipe.getBody());
        values.put(LocalDatabaseSQLiteOpenHelper.IMAGE_URL_T, recipe.getImageUrlT());

        //using content provider database access
        Uri contextUri = Uri.withAppendedPath(DbContentProvider.CONTENT_URI, LocalDatabaseSQLiteOpenHelper.RECIPES_TABLE_NAME);
        if(contextUri != null) {
            context.getContentResolver().insert(contextUri, values);
        }
    }

    /***************************************************/
    /********* Latest Recipes table methods ************/
    /***************************************************/
    /**
     * save latest recipes
     * @param latestRecipes
     * @param activity
     */
    public void saveLatestRecipes(List<PopularOrLatestRecipe> latestRecipes, Activity activity) {

        ContentValues values;
        Context context = activity.getApplicationContext();

        int latestRecipeCount = 1;
        for (PopularOrLatestRecipe latestRecipe : latestRecipes) {
            values = new ContentValues();
            values.put(LocalDatabaseSQLiteOpenHelper.LATEST_INDEX, latestRecipeCount);
            values.put(LocalDatabaseSQLiteOpenHelper.PRODUCT_ID, latestRecipe.getProductId());
            values.put(LocalDatabaseSQLiteOpenHelper.RECIPE_NAME, latestRecipe.getRecipeName());
            values.put(LocalDatabaseSQLiteOpenHelper.IMAGE_URL_XS, latestRecipe.getImageUrlXS());
            values.put(LocalDatabaseSQLiteOpenHelper.IMAGE_URL_S, latestRecipe.getImageUrlS());
            values.put(LocalDatabaseSQLiteOpenHelper.IMAGE_URL_M, latestRecipe.getImageUrlM());
            values.put(LocalDatabaseSQLiteOpenHelper.IMAGE_URL_L, latestRecipe.getImageUrlL());
            values.put(LocalDatabaseSQLiteOpenHelper.IMAGE_URL_T, latestRecipe.getImageUrlT());

            //using content provider database access
            Uri contextUri = Uri.withAppendedPath(DbContentProvider.CONTENT_URI, LocalDatabaseSQLiteOpenHelper.LATEST_YUMMY_TABLE_NAME);
            if(contextUri != null) {
                context.getContentResolver().insert(contextUri, values);
            }

            latestRecipeCount++;
        }
    }

    /**
     * delete all existing latest recipes
     * @param activity
     */
    public void deleteAllLatestRecipes(Activity activity) {
        Context context = activity.getApplicationContext();
        Uri contextUri = Uri.withAppendedPath(DbContentProvider.CONTENT_URI, LocalDatabaseSQLiteOpenHelper.LATEST_YUMMY_TABLE_NAME);
        if(contextUri != null) {
            context.getContentResolver().delete(contextUri, null, null);
        }
    }

    /****************************************************/
    /********* Popular Recipes table methods ************/
    /****************************************************/
    /**
     * save popular recipes
     * @param popularRecipes
     * @param activity
     */
    public void savePopularRecipes(List<PopularOrLatestRecipe> popularRecipes, Activity activity) {

        Context context = activity.getApplicationContext();
        ContentValues values;

        int popularRecipeCount = 1;
        for (PopularOrLatestRecipe popularRecipe : popularRecipes) {
            values = new ContentValues();
            values.put(LocalDatabaseSQLiteOpenHelper.POPULAR_INDEX, popularRecipeCount);
            values.put(LocalDatabaseSQLiteOpenHelper.PRODUCT_ID, popularRecipe.getProductId());
            values.put(LocalDatabaseSQLiteOpenHelper.RECIPE_NAME, popularRecipe.getRecipeName());
            values.put(LocalDatabaseSQLiteOpenHelper.IMAGE_URL_XS, popularRecipe.getImageUrlXS());
            values.put(LocalDatabaseSQLiteOpenHelper.IMAGE_URL_S, popularRecipe.getImageUrlS());
            values.put(LocalDatabaseSQLiteOpenHelper.IMAGE_URL_M, popularRecipe.getImageUrlM());
            values.put(LocalDatabaseSQLiteOpenHelper.IMAGE_URL_L, popularRecipe.getImageUrlL());
            values.put(LocalDatabaseSQLiteOpenHelper.IMAGE_URL_T, popularRecipe.getImageUrlT());

            //using content provider database access
            Uri contextUri = Uri.withAppendedPath(DbContentProvider.CONTENT_URI, LocalDatabaseSQLiteOpenHelper.POPULAR_YUMMY_TABLE_NAME);
            if(contextUri != null) {
                context.getContentResolver().insert(contextUri, values);
            }

            popularRecipeCount++;
        }
    }

    /**
     * delete all existing popular recipes
     * @param activity
     */
    public void deleteAllPopularRecipes(Activity activity) {
        Context context = activity.getApplicationContext();
        Uri contextUri = Uri.withAppendedPath(DbContentProvider.CONTENT_URI, LocalDatabaseSQLiteOpenHelper.POPULAR_YUMMY_TABLE_NAME);
        if(contextUri != null) {
            context.getContentResolver().delete(contextUri, null, null);
        }
    }
}
