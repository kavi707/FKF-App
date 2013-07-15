package com.fkf.resturent.services;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.fkf.resturent.database.LocalDatabaseSQLiteOpenHelper;
import com.fkf.resturent.database.Recipe;

import java.util.ArrayList;

/**
 * holding the service use by the templates
 * Created by kavi on 7/2/13.
 *
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class ActivityUserPermissionServices {

    private ApiConnector connector = new ApiConnector();

    /**
     * check the internet connection in the device for running application
     * @param activity
     * @return boolean
     */
    public boolean isOnline(Activity activity) {

        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            return true;
        }

        return false;
    }

    /**
     * update the local database from latest server data recipes
     * @param activity
     * @return
     */
    public boolean updateLocalRecipesFromServerRecipes(Activity activity) {

        LocalDatabaseSQLiteOpenHelper localDatabaseSQLiteOpenHelper = new LocalDatabaseSQLiteOpenHelper(activity);
//        ArrayList<Recipe> newRecipes = connector.getRecipesFromServer();
        ArrayList<Recipe> newRecipes = new ArrayList<Recipe>();

        //temp data
        Recipe recipe = new Recipe();
        recipe.setName("Recipe Name 1");
        recipe.setDescription("Recipe Description 1");
        recipe.setCategoryId(1);
        recipe.setRatings(4);

        Recipe recipe1 = new Recipe();
        recipe1.setName("Recipe Name 2");
        recipe1.setDescription("Recipe Description 2");
        recipe1.setCategoryId(2);
        recipe1.setRatings(3);

        newRecipes.add(recipe);
        newRecipes.add(recipe1);
        //temp data

        if(newRecipes.size() != 0) {
            localDatabaseSQLiteOpenHelper.deleteAllRecipes();
            for (Recipe newRecipe : newRecipes) {
                localDatabaseSQLiteOpenHelper.saveRecipe(newRecipe);
            }
        }

        return true;
    }
}
