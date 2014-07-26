package com.fkf.free.database.dbprovider;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.fkf.free.database.LocalDatabaseSQLiteOpenHelper;
import com.fkf.free.database.PopularOrLatestRecipe;
import com.fkf.free.database.Recipe;
import android.util.Log;
import java.util.ArrayList;
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

    /**
     * return the last modification time stamp
     * @param context
     * @return
     */
    public String getLastModificationTimeStamp(Context context) {
        String lastTimeStamp = "";
        Uri contextUri = Uri.withAppendedPath(DbContentProvider.CONTENT_URI, LocalDatabaseSQLiteOpenHelper.LAST_MODIFIED_DETAILS_TABLE_NAME);
        if(contextUri != null) {
            String[] selections = {
                    LocalDatabaseSQLiteOpenHelper.MODIFICATION_ID,
                    LocalDatabaseSQLiteOpenHelper.MODIFIED_TIME_STAMP
            };

            Cursor lastModificationDetailsCursor = context.getContentResolver().query(contextUri,selections, null, null, null);
            if(lastModificationDetailsCursor != null) {
                lastModificationDetailsCursor.moveToFirst();
                if(!lastModificationDetailsCursor.isAfterLast()) {
                    do {
                        lastTimeStamp = lastModificationDetailsCursor.getString(1);
                    } while (lastModificationDetailsCursor.moveToNext());
                }
            }
        }

        return lastTimeStamp;
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
                    LocalDatabaseSQLiteOpenHelper.LAST_LOGIN_PASSWORD,
                    LocalDatabaseSQLiteOpenHelper.LAST_LOGIN_NAME
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
                        loginDetails.put("fName", loginDetailsCursor.getString(5));
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
        values.put(LocalDatabaseSQLiteOpenHelper.LINKED_IMAGES, recipe.getLinkImages());
        values.put(LocalDatabaseSQLiteOpenHelper.LINKED_RECIPE_IDS, recipe.getLinkRecipeIds());
        values.put(LocalDatabaseSQLiteOpenHelper.LEGACY, recipe.getLegacy());
        values.put(LocalDatabaseSQLiteOpenHelper.BODY, recipe.getBody());
        values.put(LocalDatabaseSQLiteOpenHelper.IMAGE_URL_T, recipe.getImageUrlT());
        values.put(LocalDatabaseSQLiteOpenHelper.IMAGE_URL_XL, recipe.getImageUrl_xl());
        values.put(LocalDatabaseSQLiteOpenHelper.SERVINGS, recipe.getServings());

        //using content provider database access
        Uri contextUri = Uri.withAppendedPath(DbContentProvider.CONTENT_URI, LocalDatabaseSQLiteOpenHelper.RECIPES_TABLE_NAME);
        if(contextUri != null) {
            context.getContentResolver().insert(contextUri, values);
        }
    }

    /**
     * Get recipes from given category id
     * @param categoryId
     * @param context
     * @return
     */
    public ArrayList<Recipe> getRecipesFromCategoryId(int categoryId, Context context) {
        ArrayList<Recipe> selectedRecipeList = new ArrayList<Recipe>();
        Uri contextUri = Uri.withAppendedPath(DbContentProvider.CONTENT_URI, LocalDatabaseSQLiteOpenHelper.RECIPES_TABLE_NAME);
        if (contextUri != null) {
            String where = LocalDatabaseSQLiteOpenHelper.CATEGORY_ID + " = " + categoryId;

            Cursor recipeCursor = context.getContentResolver().query(contextUri,null, where, null, null);
            if(recipeCursor != null) {
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
                        String servings = recipeCursor.getString(20);

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
                        recipe.setServings(servings);

                        selectedRecipeList.add(recipe);
                    } while (recipeCursor.moveToNext() && selectedRecipeList.size() < 11);
                }
                recipeCursor.close();
            }
        }
        return selectedRecipeList;
    }

    public boolean isRecipeExist(String recipeProductId, Context context) {
        boolean status = false;

        Uri contextUri = Uri.withAppendedPath(DbContentProvider.CONTENT_URI, LocalDatabaseSQLiteOpenHelper.RECIPES_TABLE_NAME);
        if(contextUri != null) {
            String[] selections = {
                    LocalDatabaseSQLiteOpenHelper.RECIPE_ID,
                    LocalDatabaseSQLiteOpenHelper.PRODUCT_ID,
            };
            String where = LocalDatabaseSQLiteOpenHelper.PRODUCT_ID + " = " + recipeProductId;

            Cursor recipeDetailsCursor = context.getContentResolver().query(contextUri,selections, where, null, null);
            if(recipeDetailsCursor != null) {
                int existCount = 0;
                recipeDetailsCursor.moveToFirst();
                if(!recipeDetailsCursor.isAfterLast()) {
                    do {
                        String productId = recipeDetailsCursor.getString(1);
                        if(productId.equals(recipeProductId)) {
                            existCount++;
                        }
                    } while (recipeDetailsCursor.moveToNext());
                }

                if(existCount != 0) {
                    status = true;
                } else {
                    status = false;
                }
            } else {
                status = false;
            }
        }

        return status;
    }

    public void deleteRecipeFromProductId(String recipeProductId, Context context) {
        String where = LocalDatabaseSQLiteOpenHelper.PRODUCT_ID + " = " + recipeProductId;
        Uri contextUri = Uri.withAppendedPath(DbContentProvider.CONTENT_URI, LocalDatabaseSQLiteOpenHelper.RECIPES_TABLE_NAME);
        if(contextUri != null) {
            context.getContentResolver().delete(contextUri, where, null);
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
            values.put(LocalDatabaseSQLiteOpenHelper.IMAGE_URL_XL, latestRecipe.getImageUrlXL());

            //using content provider database access
            Uri contextUri = Uri.withAppendedPath(DbContentProvider.CONTENT_URI, LocalDatabaseSQLiteOpenHelper.LATEST_YUMMY_TABLE_NAME);
            if(contextUri != null) {
                context.getContentResolver().insert(contextUri, values);
            }

            latestRecipeCount++;
        }
    }

    /**
     * Return all latest recipes
     * @param context
     * @return
     */
    public List<PopularOrLatestRecipe> getAllLatestRecipes(Context context) {

        List<PopularOrLatestRecipe> latestRecipesList = new ArrayList<PopularOrLatestRecipe>();
        Uri contextUri = Uri.withAppendedPath(DbContentProvider.CONTENT_URI, LocalDatabaseSQLiteOpenHelper.LATEST_YUMMY_TABLE_NAME);
        if(contextUri != null) {
            String[] selections = {
                    LocalDatabaseSQLiteOpenHelper.LATEST_INDEX,
                    LocalDatabaseSQLiteOpenHelper.PRODUCT_ID,
                    LocalDatabaseSQLiteOpenHelper.RECIPE_NAME,
                    LocalDatabaseSQLiteOpenHelper.IMAGE_URL_XS,
                    LocalDatabaseSQLiteOpenHelper.IMAGE_URL_S,
                    LocalDatabaseSQLiteOpenHelper.IMAGE_URL_M,
                    LocalDatabaseSQLiteOpenHelper.IMAGE_URL_L,
                    LocalDatabaseSQLiteOpenHelper.IMAGE_URL_T,
                    LocalDatabaseSQLiteOpenHelper.IMAGE_URL_XL,
            };

            Cursor latestRecipesCursor = context.getContentResolver().query(contextUri,selections, null, null, null);
            if(latestRecipesCursor != null) {
                latestRecipesCursor.moveToFirst();
                if(!latestRecipesCursor.isAfterLast()) {
                    do {
                        int index = latestRecipesCursor.getInt(0);
                        String productId = latestRecipesCursor.getString(1);
                        String recipeName = latestRecipesCursor.getString(2);
                        String imageUrlXS = latestRecipesCursor.getString(3);
                        String imageUrlS = latestRecipesCursor.getString(4);
                        String imageUrlM = latestRecipesCursor.getString(5);
                        String imageUrlL = latestRecipesCursor.getString(6);
                        String imageUrlT = latestRecipesCursor.getString(7);
                        String imageUrlXL = latestRecipesCursor.getString(8);

                        PopularOrLatestRecipe popularOrLatestRecipe = new PopularOrLatestRecipe();
                        popularOrLatestRecipe.setIndex(index);
                        popularOrLatestRecipe.setProductId(productId);
                        popularOrLatestRecipe.setRecipeName(recipeName);
                        popularOrLatestRecipe.setImageUrlXS(imageUrlXS);
                        popularOrLatestRecipe.setImageUrlS(imageUrlS);
                        popularOrLatestRecipe.setImageUrlM(imageUrlM);
                        popularOrLatestRecipe.setImageUrlL(imageUrlL);
                        popularOrLatestRecipe.setImageUrlT(imageUrlT);
                        popularOrLatestRecipe.setImageUrlXL(imageUrlXL);

                        latestRecipesList.add(popularOrLatestRecipe);

                    } while (latestRecipesCursor.moveToNext());
                }
            }
        }
        return latestRecipesList;
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
            values.put(LocalDatabaseSQLiteOpenHelper.IMAGE_URL_XL, popularRecipe.getImageUrlXL());

            //using content provider database access
            Uri contextUri = Uri.withAppendedPath(DbContentProvider.CONTENT_URI, LocalDatabaseSQLiteOpenHelper.POPULAR_YUMMY_TABLE_NAME);
            if(contextUri != null) {
                context.getContentResolver().insert(contextUri, values);
            }

            popularRecipeCount++;
        }
    }

    public List<PopularOrLatestRecipe> getAllPopularRecipes(Context context) {
        List<PopularOrLatestRecipe> popularRecipesList = new ArrayList<PopularOrLatestRecipe>();
        Uri contextUri = Uri.withAppendedPath(DbContentProvider.CONTENT_URI, LocalDatabaseSQLiteOpenHelper.POPULAR_YUMMY_TABLE_NAME);
        if (contextUri != null) {
            Cursor popularRecipesCursor = context.getContentResolver().query(contextUri, null, null, null, null);
            if(popularRecipesCursor != null) {
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
            }
        }
        return popularRecipesList;
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


    /**********************************************************/
    /********* User favorite Recipes table methods ************/
    /**********************************************************/

    /**
     * check given recipe is favorite recipe of the logged user
     * @param context
     * @param recipeProductId
     * @param userId
     * @return
     */
    public boolean isUserFavoriteRecipe(Context context, String recipeProductId, String userId) {
        boolean isFavorite = false;
        Uri contextUri = Uri.withAppendedPath(DbContentProvider.CONTENT_URI, LocalDatabaseSQLiteOpenHelper.USER_FAVORITE_RECIPES_TABLE_NAME);
        if(contextUri != null) {
            String[] selections = {
                    LocalDatabaseSQLiteOpenHelper.ID,
                    LocalDatabaseSQLiteOpenHelper.PRODUCT_ID,
                    LocalDatabaseSQLiteOpenHelper.LOGIN_USER_ID
            };

            String where =
                    LocalDatabaseSQLiteOpenHelper.PRODUCT_ID + " = " + recipeProductId;

            Cursor isFavoriteDetailCursor = context.getContentResolver().query(contextUri,selections, where, null, null);
            if(isFavoriteDetailCursor != null) {
                isFavoriteDetailCursor.moveToFirst();
                if(!isFavoriteDetailCursor.isAfterLast()) {
                    do {
                        String getUserId = isFavoriteDetailCursor.getString(2);
                        if (getUserId.equals(userId)) {
                            isFavorite = true;
                        }
                    } while (isFavoriteDetailCursor.moveToNext());
                }
            }
        }

        return  isFavorite;
    }

    /*************************************************/
    /********* Updated date table methods ************/
    /*************************************************/
    /**
     * Save new updated date
     * @param day
     * @param month
     * @param year
     * @param activity
     */
    public void saveUpdateDate(int day, int month, int year, Activity activity) {

        Context context = activity.getApplicationContext();
        ContentValues values;

        values = new ContentValues();
        values.put(LocalDatabaseSQLiteOpenHelper.UPDATED_ID, 1);
        values.put(LocalDatabaseSQLiteOpenHelper.UPDATED_YEAR, year);
        values.put(LocalDatabaseSQLiteOpenHelper.UPDATED_MONTH, month);
        values.put(LocalDatabaseSQLiteOpenHelper.UPDATED_DAY, day);

        //using content provider database access
        Uri contextUri = Uri.withAppendedPath(DbContentProvider.CONTENT_URI, LocalDatabaseSQLiteOpenHelper.UPDATED_DATE_TABLE_NAME);
        if(contextUri != null) {
            context.getContentResolver().insert(contextUri, values);
        }
    }

    /**
     * Get updated date in the database
     * @param context
     * @return
     */
    public Map<String, Integer> getUpdatedDate(Context context) {
        Map<String, Integer> updatedDate = new HashMap<String, Integer>();

        Uri contextUri = Uri.withAppendedPath(DbContentProvider.CONTENT_URI, LocalDatabaseSQLiteOpenHelper.UPDATED_DATE_TABLE_NAME);
        if (contextUri != null) {
            String[] selections = {
                    LocalDatabaseSQLiteOpenHelper.UPDATED_ID,
                    LocalDatabaseSQLiteOpenHelper.UPDATED_YEAR,
                    LocalDatabaseSQLiteOpenHelper.UPDATED_MONTH,
                    LocalDatabaseSQLiteOpenHelper.UPDATED_DAY
            };

            String where = LocalDatabaseSQLiteOpenHelper.UPDATED_ID + " = 1";

            Cursor updatedDateCursor = context.getContentResolver().query(contextUri,selections, where, null, null);
            if (updatedDateCursor != null) {
                updatedDateCursor.moveToFirst();
                if(!updatedDateCursor.isAfterLast()) {
                    do {
                        updatedDate.put("updatedId", updatedDateCursor.getInt(0));
                        updatedDate.put("updatedYear", updatedDateCursor.getInt(1));
                        updatedDate.put("updatedMonth", updatedDateCursor.getInt(2));
                        updatedDate.put("updatedDay", updatedDateCursor.getInt(3));
                    } while (updatedDateCursor.moveToNext());
                }
                updatedDateCursor.close();
            }
        }

        return updatedDate;
    }

    /**
     * Delete updated date
     * @param activity
     */
    public void deleteUpdatedDate(Activity activity) {
        Context context = activity.getApplicationContext();
        Uri contextUri = Uri.withAppendedPath(DbContentProvider.CONTENT_URI, LocalDatabaseSQLiteOpenHelper.UPDATED_DATE_TABLE_NAME);
        if(contextUri != null) {
            context.getContentResolver().delete(contextUri, null, null);
        }
    }
}
