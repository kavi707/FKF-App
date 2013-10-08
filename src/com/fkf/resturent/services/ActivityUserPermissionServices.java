package com.fkf.resturent.services;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import com.fkf.resturent.database.LocalDatabaseSQLiteOpenHelper;
import com.fkf.resturent.database.PopularOrLatestRecipe;
import com.fkf.resturent.database.Recipe;
import com.fkf.resturent.services.image.downloader.DownloadFileTask;
import com.fkf.resturent.services.connections.ApiConnector;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

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
    public void populateLatestYummyDetails(Activity activity) {

        LocalDatabaseSQLiteOpenHelper localDatabaseSQLiteOpenHelper = new LocalDatabaseSQLiteOpenHelper(activity);
        List<PopularOrLatestRecipe> latestYummyList = connector.getLatestYummysFromServer();

        localDatabaseSQLiteOpenHelper.deleteAllLatestRecipes();
        localDatabaseSQLiteOpenHelper.saveLatestYummyRecipe(latestYummyList);

        DownloadFileTask downloadFile = new DownloadFileTask();
        String path = Environment.getExternalStorageDirectory() + "/fauzias/latest_yummys/";

        List<Map<String, String>> downloadDetailsList = new ArrayList<Map<String, String>>();

        int recipeCount = 1;
        if (latestYummyList != null) {
            for (PopularOrLatestRecipe latestRecipe : latestYummyList) {

                String url = latestRecipe.getImageUrlS();
                String newName = "icon_" + recipeCount;

                Map<String, String> downloadingDetails = new HashMap<String, String>();
                downloadingDetails.put("url", url);
                downloadingDetails.put("path", path);
                downloadingDetails.put("name", newName);

                downloadDetailsList.add(downloadingDetails);

                recipeCount++;
            }
        }

        downloadFile.execute(downloadDetailsList);

    }

    /**
     * download popular yummy icon images from urls and save them to SD card location
     */
    public void populatePopularYummyDetails(Activity activity) {

        LocalDatabaseSQLiteOpenHelper localDatabaseSQLiteOpenHelper = new LocalDatabaseSQLiteOpenHelper(activity);
        List<PopularOrLatestRecipe> popularRecipesList = connector.getPopularYummysFromServer();

        localDatabaseSQLiteOpenHelper.savePopularYummyRecipe(popularRecipesList);

        DownloadFileTask downloadFile = new DownloadFileTask();
        String path = Environment.getExternalStorageDirectory() + "/fauzias/popular_yummys/";

        List<Map<String, String>> downloadDetailsList = new ArrayList<Map<String, String>>();

        int recipeCount = 1;
        if (popularRecipesList != null) {
            for (PopularOrLatestRecipe popularRecipe : popularRecipesList) {

                String url = popularRecipe.getImageUrlS();
                String newName = "icon_" + recipeCount;

                Map<String, String> downloadingDetails = new HashMap<String, String>();
                downloadingDetails.put("url", url);
                downloadingDetails.put("path", path);
                downloadingDetails.put("name", newName);

                downloadDetailsList.add(downloadingDetails);

                recipeCount++;

            }
        }

        downloadFile.execute(downloadDetailsList);
    }

    /**
     * update the local database from latest server data recipes
     * @param activity
     * @return
     */
    public boolean updateLocalRecipesFromServerRecipes(Activity activity) {

        LocalDatabaseSQLiteOpenHelper localDatabaseSQLiteOpenHelper = new LocalDatabaseSQLiteOpenHelper(activity);
        String lastModifiedTimeStamp = localDatabaseSQLiteOpenHelper.getLastModificationTimeStamp();
        localDatabaseSQLiteOpenHelper.deleteLastModifiedTimeStamp();

        //TODO this initialization is for temp
        if(lastModifiedTimeStamp == null)
            lastModifiedTimeStamp = "1376430210";
        connector.getRecipesFromServer(lastModifiedTimeStamp, activity);

        return true;
    }

    /**
     * update recipe categories from server database
     * @param activity
     * @return
     */
    public boolean updateLocalRecipeCategoriesFromServer(Activity activity) {

        connector.getRecipeCategoriesFromServer(activity);
        return true;
    }

    /**
     * update logged user favorite recipes from server database
     * @param activity
     * @return
     */
    public boolean updateUserFavoriteRecipesFromServer(Activity activity) {

        connector.getUserFavoriteRecipeIdsFromServer(activity);
        return true;
    }
}
