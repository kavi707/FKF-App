package com.fkf.commercial.services;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.view.Display;

import com.fkf.commercial.WelcomeActivity;
import com.fkf.commercial.database.PopularOrLatestRecipe;
import com.fkf.commercial.database.dbprovider.ContentProviderAccessor;
import com.fkf.commercial.services.image.downloader.DownloadFileTask;
import com.fkf.commercial.services.connections.ApiConnector;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * holding the service use by the templates
 * Created by kavi on 7/2/13.
 *
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class ActivityUserPermissionServices {

    private ApiConnector connector = new ApiConnector();
    private ContentProviderAccessor contentProviderAccessor = new ContentProviderAccessor();

    private String salt = "ae26dde136fc01876b1ec2ba3adc47b7";

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

    public void createInternalAppDirectories(String appFilePath) {
        File appDir = new File(appFilePath+"/fauzias");
        if(!appDir.isDirectory()) {
            File appDirLatestYummy = new File(appFilePath+"/fauzias/latest_yummys");
            File appDirPopularYummy = new File(appFilePath+"/fauzias/popular_yummys");
            File appDirUser = new File(appFilePath+"/fauzias/user");
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
    public int getDeviceScreenSize(Context activity) {

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
    public boolean populateLatestYummyDetails(Activity activity, String appFilePath) {

        boolean imageDownloadStatus = false;

        List<PopularOrLatestRecipe> latestYummyList = connector.getLatestYummysFromServer();

        /*LocalDatabaseSQLiteOpenHelper localDatabaseSQLiteOpenHelper = new LocalDatabaseSQLiteOpenHelper(activity);
        localDatabaseSQLiteOpenHelper.deleteAllLatestRecipes();
        localDatabaseSQLiteOpenHelper.saveLatestYummyRecipe(latestYummyList, activity);*/

        ContentProviderAccessor contentProviderAccessor = new ContentProviderAccessor();
        contentProviderAccessor.deleteAllLatestRecipes(activity);
        contentProviderAccessor.saveLatestRecipes(latestYummyList, activity);

        DownloadFileTask downloadFile = new DownloadFileTask();
//        String path = Environment.getExternalStorageDirectory() + "/fauzias/latest_yummys/";
        String path = appFilePath + "/fauzias/latest_yummys/";

        List<Map<String, String>> downloadDetailsList = new ArrayList<Map<String, String>>();

        int recipeCount = 1;
        if (latestYummyList != null) {
            for (PopularOrLatestRecipe latestRecipe : latestYummyList) {

                String url;
                if (WelcomeActivity.widthAndHeight.get("width") == 480) {
                    url = latestRecipe.getImageUrlM();
                } else if (WelcomeActivity.widthAndHeight.get("width") == 720){
                    url = latestRecipe.getImageUrlL();
                } else {
                    url = latestRecipe.getImageUrlL();
                }
                String newName = "icon_" + recipeCount;

                Map<String, String> downloadingDetails = new HashMap<String, String>();
                downloadingDetails.put("url", url);
                downloadingDetails.put("path", path);
                downloadingDetails.put("name", newName);

                downloadDetailsList.add(downloadingDetails);

                recipeCount++;
            }
        }

        try {
            String returnStatus = downloadFile.execute(downloadDetailsList).get();

            if (returnStatus.equals("true")) {
                imageDownloadStatus = true;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return imageDownloadStatus;
    }

    /**
     * download popular yummy icon images from urls and save them to SD card location
     */
    public void populatePopularYummyDetails(Activity activity, String appFilePath) {

        List<PopularOrLatestRecipe> popularRecipesList = connector.getPopularYummysFromServer();

        /*LocalDatabaseSQLiteOpenHelper localDatabaseSQLiteOpenHelper = new LocalDatabaseSQLiteOpenHelper(activity);
        localDatabaseSQLiteOpenHelper.deleteAllPopularRecipes();
        localDatabaseSQLiteOpenHelper.savePopularYummyRecipe(popularRecipesList, activity);*/

        ContentProviderAccessor contentProviderAccessor = new ContentProviderAccessor();
        contentProviderAccessor.deleteAllPopularRecipes(activity);
        contentProviderAccessor.savePopularRecipes(popularRecipesList, activity);

        DownloadFileTask downloadFile = new DownloadFileTask();
//        String path = Environment.getExternalStorageDirectory() + "/fauzias/popular_yummys/";
        String path = appFilePath + "/fauzias/popular_yummys/";

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
     * check is there any server database update is available or not
     * @param activity
     * @return
     */
    public boolean isDbUpdate(Activity activity) {
        boolean status = false;
        String lastModifiedTimeStamp = contentProviderAccessor.getLastModificationTimeStamp(activity.getApplicationContext());
        status = connector.isServerDbUpdated(lastModifiedTimeStamp);
        return status;
    }

    /**
     * update the local database from latest server data recipes
     * @param activity
     * @return
     */
    public boolean updateLocalRecipesFromServerRecipes(Activity activity) {

        String lastModifiedTimeStamp = contentProviderAccessor.getLastModificationTimeStamp(activity.getApplicationContext());
        String concatenateString = "";
        String key = "";
        //TODO this initialization is for temp
        try {
            if (lastModifiedTimeStamp == null) {
//            lastModifiedTimeStamp = "1376430210";
//            lastModifiedTimeStamp = "0000000000";
                lastModifiedTimeStamp = "111";
                key = "b2eb532e2cc34302106acfddd5fe29c6";
            } else {
                concatenateString = lastModifiedTimeStamp + salt;
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(concatenateString.getBytes());
                byte[] digest = md.digest();
                StringBuffer sb = new StringBuffer();
                        /*for (byte b : digest) {
                            sb.append(Integer.toHexString((b & 0xff)));
                        }*/
                for (int i = 0; i < digest.length; i++) {
                    String h = Integer.toHexString(0xFF & digest[i]);
                    while (h.length() < 2)
                        h = "0" + h;
                    sb.append(h);
                }

                key = sb.toString();
            }

            connector.getRecipesFromServer(lastModifiedTimeStamp, key, activity);

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String[] dateStringArray = dateFormat.format(date).split("-");

            int year = Integer.parseInt(dateStringArray[0]);
            int month = Integer.parseInt(dateStringArray[1]);
            int day = Integer.parseInt(dateStringArray[2]);

            contentProviderAccessor.deleteUpdatedDate(activity);
            contentProviderAccessor.saveUpdateDate(day, month, year, activity);

            return true;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return true;
        }
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
