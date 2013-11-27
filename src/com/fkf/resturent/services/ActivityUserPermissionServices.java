package com.fkf.resturent.services;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import com.fkf.resturent.database.LocalDatabaseSQLiteOpenHelper;
import com.fkf.resturent.database.PopularOrLatestRecipe;
import com.fkf.resturent.database.Recipe;
import com.fkf.resturent.database.dbprovider.ContentProviderAccessor;
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
    private ContentProviderAccessor contentProviderAccessor = new ContentProviderAccessor();

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
     * return the application running device screen size
     * @param activity
     * @return
     */
    public int getDeviceScreenSize(Activity activity) {

        int screenSizeInt = 0;

        int screenSize = activity.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                screenSizeInt = Configuration.SCREENLAYOUT_SIZE_XLARGE;
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                screenSizeInt = Configuration.SCREENLAYOUT_SIZE_LARGE;
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                screenSizeInt = Configuration.SCREENLAYOUT_SIZE_NORMAL;
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                screenSizeInt = Configuration.SCREENLAYOUT_SIZE_SMALL;
                break;
        }

        return screenSizeInt;
    }

    /**
     * return the device screen width and height
     * @param activity
     * @return
     */
    public Map<String, Integer> getDeviceWidthAndHeight(Activity activity) {
        Map<String, Integer> deviceWidthHeight = new HashMap<String, Integer>();

        Display mDisplay = activity.getWindowManager().getDefaultDisplay();
        int width  = mDisplay.getWidth();
        int height = mDisplay.getHeight();

        deviceWidthHeight.put("width", width);
        deviceWidthHeight.put("height", height);

        Log.d("Device width : ", String.valueOf(width));
        Log.d("Device height : ", String.valueOf(height));

        return deviceWidthHeight;
    }

    /**
     * download latest yummy icon images from urls and save them to SD card location
     */
    public void populateLatestYummyDetails(Activity activity) {

        List<PopularOrLatestRecipe> latestYummyList = connector.getLatestYummysFromServer();

        /*LocalDatabaseSQLiteOpenHelper localDatabaseSQLiteOpenHelper = new LocalDatabaseSQLiteOpenHelper(activity);
        localDatabaseSQLiteOpenHelper.deleteAllLatestRecipes();
        localDatabaseSQLiteOpenHelper.saveLatestYummyRecipe(latestYummyList, activity);*/

        ContentProviderAccessor contentProviderAccessor = new ContentProviderAccessor();
        contentProviderAccessor.deleteAllLatestRecipes(activity);
        contentProviderAccessor.saveLatestRecipes(latestYummyList, activity);

        DownloadFileTask downloadFile = new DownloadFileTask();
        String path = Environment.getExternalStorageDirectory() + "/fauzias/latest_yummys/";

        List<Map<String, String>> downloadDetailsList = new ArrayList<Map<String, String>>();

        int recipeCount = 1;
        if (latestYummyList != null) {
            for (PopularOrLatestRecipe latestRecipe : latestYummyList) {

                String url = latestRecipe.getImageUrlL();
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

        List<PopularOrLatestRecipe> popularRecipesList = connector.getPopularYummysFromServer();

        /*LocalDatabaseSQLiteOpenHelper localDatabaseSQLiteOpenHelper = new LocalDatabaseSQLiteOpenHelper(activity);
        localDatabaseSQLiteOpenHelper.deleteAllPopularRecipes();
        localDatabaseSQLiteOpenHelper.savePopularYummyRecipe(popularRecipesList, activity);*/

        ContentProviderAccessor contentProviderAccessor = new ContentProviderAccessor();
        contentProviderAccessor.deleteAllPopularRecipes(activity);
        contentProviderAccessor.savePopularRecipes(popularRecipesList, activity);

        DownloadFileTask downloadFile = new DownloadFileTask();
        String path = Environment.getExternalStorageDirectory() + "/fauzias/popular_yummys/";

        List<Map<String, String>> downloadDetailsList = new ArrayList<Map<String, String>>();

        int recipeCount = 1;
        if (popularRecipesList != null) {
            for (PopularOrLatestRecipe popularRecipe : popularRecipesList) {

                String url = popularRecipe.getImageUrlT();
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

        String lastModifiedTimeStamp = contentProviderAccessor.getLastModificationTimeStamp(activity.getApplicationContext());

        //TODO this initialization is for temp
        if(lastModifiedTimeStamp == null)
            lastModifiedTimeStamp = "1376430210";
            //lastModifiedTimeStamp = "0000000000";
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
