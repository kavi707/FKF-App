package com.fkf.resturent.services;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import com.fkf.resturent.database.LocalDatabaseSQLiteOpenHelper;
import com.fkf.resturent.database.Recipe;
import com.fkf.resturent.services.image.downloader.DownloadFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
     * create application directories in SD card for hold images
     */
    public void createAppDirectories() {

        File appDir = new File(Environment.getExternalStorageDirectory()+"/fauzias");
        if(!appDir.isDirectory()) {
            File appDirLatestYummy = new File(Environment.getExternalStorageDirectory()+"/fauzias/latest_yummys");
            File appDirPopularYummy = new File(Environment.getExternalStorageDirectory()+"/fauzias/popular_yummys");
            File appDirUser = new File(Environment.getExternalStorageDirectory()+"/fauzias/user");
            try {
                appDir.mkdir();
                appDirLatestYummy.mkdir();
                appDirPopularYummy.mkdir();
                appDirUser.mkdir();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * download latest yummy icon images from urls and save them to SD card location
     */
    public void populateLatestYummyDetails() {

        //TODO need to get the latest yummys from server
        //TODO populate local database from latest yummys
        //TODO download images for selected latest yummys

        DownloadFile downloadFile = new DownloadFile();
        String url = "http://mathworld.wolfram.com/images/gifs/smstdo-o.jpg";
        String newName = "icon_1";
        String path = Environment.getExternalStorageDirectory() + "/fauzias/latest_yummys/";
        Map<String, String> downloadingDetails = new HashMap<String, String>();
        downloadingDetails.put("url", url);
        downloadingDetails.put("path", path);
        downloadingDetails.put("name", newName);

        downloadFile.execute(downloadingDetails);
    }

    /**
     * download popular yummy icon images from urls and save them to SD card location
     */
    public void populatePopularYummyDetails() {

        //TODO need to get the popular yummys from server
        //TODO populate local database from popular yummys
        //TODO download images for selected popular yummys

        DownloadFile downloadFile = new DownloadFile();
        String url = "http://mathworld.wolfram.com/images/gifs/smstdo-o.jpg";
        String newName = "icon_1";
        String path = Environment.getExternalStorageDirectory() + "/fauzias/popular_yummys/";
        Map<String, String> downloadingDetails = new HashMap<String, String>();
        downloadingDetails.put("url", url);
        downloadingDetails.put("path", path);
        downloadingDetails.put("name", newName);

        downloadFile.execute(downloadingDetails);
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

        /*newRecipes.add(recipe);
        newRecipes.add(recipe1);*/
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
