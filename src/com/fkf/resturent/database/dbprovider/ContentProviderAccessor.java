package com.fkf.resturent.database.dbprovider;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import com.fkf.resturent.database.LocalDatabaseSQLiteOpenHelper;
import com.fkf.resturent.database.PopularOrLatestRecipe;
import com.fkf.resturent.database.Recipe;

import java.util.List;

/**
 * Created by kavi on 10/31/13.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class ContentProviderAccessor {

    public void saveLastModificationTimeStamp(String newModifiedTimeStamp, Context context) {
        ContentValues values = new ContentValues();
        values.put(LocalDatabaseSQLiteOpenHelper.MODIFIED_TIME_STAMP, newModifiedTimeStamp);

        Uri contextUri = Uri.withAppendedPath(DbContentProvider.CONTENT_URI, LocalDatabaseSQLiteOpenHelper.LAST_MODIFIED_DETAILS_TABLE_NAME);
        if(contextUri != null) {
            Uri resultUri = context.getContentResolver().insert(contextUri, values);
        }
    }

    public void deleteLastModificationTimeStamp(Context context) {
        Uri contextUri = Uri.withAppendedPath(DbContentProvider.CONTENT_URI, LocalDatabaseSQLiteOpenHelper.LAST_MODIFIED_DETAILS_TABLE_NAME);
        if(contextUri != null) {
            context.getContentResolver().delete(contextUri, null, null);
        }
    }

    public void saveNewCategory(int categoryProductId, String categoryName, Context context) {

        ContentValues values = new ContentValues();
        values.put(LocalDatabaseSQLiteOpenHelper.CATEGORY_PRODUCT_ID, categoryProductId);
        values.put(LocalDatabaseSQLiteOpenHelper.CATEGORY_NAME, categoryName);

        Uri contextUri = Uri.withAppendedPath(DbContentProvider.CONTENT_URI, LocalDatabaseSQLiteOpenHelper.CATEGORY_TABLE_NAME);
        if(contextUri != null) {
            Uri resultUri = context.getContentResolver().insert(contextUri, values);
        }
    }

    public void deleteAllCategories(Context context) {
        Uri contextUri = Uri.withAppendedPath(DbContentProvider.CONTENT_URI, LocalDatabaseSQLiteOpenHelper.CATEGORY_TABLE_NAME);
        if(contextUri != null) {
            context.getContentResolver().delete(contextUri, null, null);
        }
    }

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

    public void deleteAllLatestRecipes(Activity activity) {
        Context context = activity.getApplicationContext();
        Uri contextUri = Uri.withAppendedPath(DbContentProvider.CONTENT_URI, LocalDatabaseSQLiteOpenHelper.LATEST_YUMMY_TABLE_NAME);
        if(contextUri != null) {
            context.getContentResolver().delete(contextUri, null, null);
        }
    }

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

    public void deleteAllPopularRecipes(Activity activity) {
        Context context = activity.getApplicationContext();
        Uri contextUri = Uri.withAppendedPath(DbContentProvider.CONTENT_URI, LocalDatabaseSQLiteOpenHelper.POPULAR_YUMMY_TABLE_NAME);
        if(contextUri != null) {
            context.getContentResolver().delete(contextUri, null, null);
        }
    }

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
}
