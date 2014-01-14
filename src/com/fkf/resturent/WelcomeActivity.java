package com.fkf.resturent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.fkf.resturent.database.LocalDatabaseSQLiteOpenHelper;
import com.fkf.resturent.database.Recipe;
import com.fkf.resturent.database.dbprovider.ContentProviderAccessor;
import com.fkf.resturent.services.ActivityUserPermissionServices;
import com.fkf.resturent.templates.LoginActivity;
import com.fkf.resturent.templates.RecipesActivity;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Holding the loading ui
 * main.xml Activity class
 *
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class WelcomeActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    protected static final int TIMER_RUNTIME = 15000;
    protected boolean mbActive;
    private String appFilePath;

    private ProgressBar appLoadingProgressBar;
    private TextView appLoadingProgressTitleTextView;

    final Context context = this;
    private Handler mHandler;

    private AlertDialog messageBalloonAlertDialog;

    private ActivityUserPermissionServices userPermissionServices = new ActivityUserPermissionServices();
    private LocalDatabaseSQLiteOpenHelper localDatabaseSQLiteOpenHelper = new LocalDatabaseSQLiteOpenHelper(this);
    private ContentProviderAccessor contentProviderAccessor = new ContentProviderAccessor();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //following content is for server direct connectivity purposes
        StrictMode.ThreadPolicy policy = new StrictMode.
                ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //following content is for get the internal application files path
        appFilePath = getFilesDir().getAbsolutePath();

        setUpViews();
    }

    private void setUpViews() {

        appLoadingProgressBar = (ProgressBar) findViewById(R.id.loadingProgressBar);
        appLoadingProgressTitleTextView = (TextView) findViewById(R.id.progressBarTitleTextView);
        mHandler = new Handler();

        final Thread timerThread = new Thread() {
            @Override
            public void run() {
                mbActive = true;
                int progress = 0;

                try {
                    int waited = 0;
                    while (mbActive && (waited < TIMER_RUNTIME)) {
                        sleep(200);
                        if (mbActive) {
                            waited += 200;
                            progress = updateProgress(waited);

                            switch (progress) {
                                case 0:
                                    appLoadingProgressTitleTextView.post(new Runnable() {
                                        @Override
                                        public void run() {
//                                            appLoadingProgressTitleTextView.setText("Loading configurations ...");
                                        }
                                    });
                                    break;
                                case 20:
                                    appLoadingProgressTitleTextView.post(new Runnable() {
                                        @Override
                                        public void run() {
//                                            appLoadingProgressTitleTextView.setText("Check the Internet connection ...");
                                        }
                                    });
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {

                                            //create database from given database file in assets
                                            /*try {
                                                if(!localDatabaseSQLiteOpenHelper.checkDataBase()) {
                                                    localDatabaseSQLiteOpenHelper.createDatabase();
                                                    localDatabaseSQLiteOpenHelper.openDataBase();
                                                }
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }*/

                                            if (!userPermissionServices.isOnline(WelcomeActivity.this)) {
                                                mbActive = false;
                                                messageBalloonAlertDialog = new AlertDialog.Builder(context)
                                                        .setTitle(R.string.warning)
                                                        .setMessage("Internet connection is not available. Do you need to continue offline?")
                                                        .setPositiveButton(R.string.yes, new AlertDialog.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                appLoadingProgressBar.setProgress(100);
                                                                onContinue();
                                                            }
                                                        })
                                                        .setNeutralButton("settings", new AlertDialog.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                                                                finish();
                                                            }
                                                        })
                                                        .setNegativeButton(R.string.no, new AlertDialog.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                finish();
                                                            }
                                                        }).create();
                                                messageBalloonAlertDialog.show();
                                            } else {

                                                //create app dir if not exists
//                                                userPermissionServices.createAppDirectories();
                                                //create internal app dir if not exists
                                                userPermissionServices.createInternalAppDirectories(appFilePath);

                                                //update the database if server database is modified
                                                userPermissionServices.updateLocalRecipesFromServerRecipes(WelcomeActivity.this);
                                                //update the recipe categories from the server data

                                                //TODO following cases must happen if recipes are updated only. This case must be handle
                                                userPermissionServices.updateLocalRecipeCategoriesFromServer(WelcomeActivity.this);

                                                //populate latest yummy details and download images
                                                userPermissionServices.populateLatestYummyDetails(WelcomeActivity.this, appFilePath);
                                                //populate popular yummy details and download images
                                                userPermissionServices.populatePopularYummyDetails(WelcomeActivity.this, appFilePath);
                                            }
                                        }
                                    });
                                    break;
                                case 50:
                                    appLoadingProgressTitleTextView.post(new Runnable() {
                                        @Override
                                        public void run() {
//                                            appLoadingProgressTitleTextView.setText("Populating recipes from Fauzia's Kitchen ...");
                                        }
                                    });
                                    break;
                                case 80:
                                    appLoadingProgressTitleTextView.post(new Runnable() {
                                        @Override
                                        public void run() {
//                                            appLoadingProgressTitleTextView.setText("Latest Yummys and Popular Yummys ...");
                                        }
                                    });
                                    break;
                                case 100:
                                    onContinue();
                                    break;
                            }
                            appLoadingProgressBar.setProgress(progress);
                        }
                    }
                } catch (InterruptedException e) {
                    // do nothing
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private int updateProgress(final int timePassed) {
        if (null != appLoadingProgressBar) {
            // Ignore rounding error here
            final int progress = appLoadingProgressBar.getMax() * timePassed / TIMER_RUNTIME;
            return progress;
        }
        return 0;
    }

    private void onContinue() {

//        Map<String, String> lastLoginDetails = localDatabaseSQLiteOpenHelper.getLoginDetails();
        Map<String, String> lastLoginDetails = contentProviderAccessor.getLoginDetails(WelcomeActivity.this);
        if(!lastLoginDetails.isEmpty()) {
            LoginActivity.LOGGED_STATUS = 1;
            LoginActivity.LOGGED_USER_ID = lastLoginDetails.get("userId");
            LoginActivity.LOGGED_USER = lastLoginDetails.get("username");
            LoginActivity.LOGGED_USER_NAME = lastLoginDetails.get("fName");
            LoginActivity.LOGGED_USER_PASSWORD = lastLoginDetails.get("password");

            Intent recipeIntent = new Intent(WelcomeActivity.this, RecipesActivity.class);
            startActivity(recipeIntent);
            finish();
        } else {
            Intent loginIntent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
    }
}
