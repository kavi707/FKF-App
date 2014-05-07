package com.fkf.commercial;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fkf.commercial.database.LocalDatabaseSQLiteOpenHelper;
import com.fkf.commercial.database.dbprovider.ContentProviderAccessor;
import com.fkf.commercial.services.ActivityUserPermissionServices;
import com.fkf.commercial.templates.LoginActivity;
import com.fkf.commercial.templates.RecipesActivity;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
    protected static final int TIMER_RUNTIME = 40000;
    protected boolean mbActive;
    private String appFilePath;
    private boolean isActivityActivated = false;
//    private int onContinueCount = 0;

    private ProgressBar appLoadingProgressBar;
    private TextView appLoadingProgressTitleTextView;

    final Context context = this;
    private Handler mHandler;

    private AlertDialog messageBalloonAlertDialog;

    private ActivityUserPermissionServices userPermissionServices = new ActivityUserPermissionServices();
    private LocalDatabaseSQLiteOpenHelper localDatabaseSQLiteOpenHelper = new LocalDatabaseSQLiteOpenHelper(this);
    private ContentProviderAccessor contentProviderAccessor = new ContentProviderAccessor();
    public static Map<String, Integer> widthAndHeight = new HashMap<String, Integer>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //following content is for server direct connectivity purposes
        StrictMode.ThreadPolicy policy = new StrictMode.
                ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //get device height and width (resolution)
        widthAndHeight = userPermissionServices.getDeviceWidthAndHeight(WelcomeActivity.this);

        //following content is for get the internal application files path
        appFilePath = getFilesDir().getAbsolutePath();

        if (localDatabaseSQLiteOpenHelper.checkDataBase()) {
            if (isApplicationUpdatedForToday()) {
                onContinue();
            } else {
                setUpViews();
            }
        } else {
            setUpViews();
        }

    }

    /**
     * Called after {@link #onCreate} &mdash; or after {@link #onRestart} when
     * the activity had been stopped, but is now again being displayed to the
     * user.  It will be followed by {@link #onResume}.
     * <p/>
     * <p><em>Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.</em></p>
     *
     * @see #onCreate
     * @see #onStop
     * @see #onResume
     */
    @Override
    protected void onStart() {
        super.onStart();
        isActivityActivated = true;
    }

    /**
     * Called when you are no longer visible to the user.  You will next
     * receive either {@link #onRestart}, {@link #onDestroy}, or nothing,
     * depending on later user activity.
     * <p/>
     * <p>Note that this method may never be called, in low memory situations
     * where the system does not have enough memory to keep your activity's
     * process running after its {@link #onPause} method is called.
     * <p/>
     * <p><em>Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.</em></p>
     *
     * @see #onRestart
     * @see #onResume
     * @see #onSaveInstanceState
     * @see #onDestroy
     */
    @Override
    protected void onStop() {
        super.onStop();
        isActivityActivated = false;
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
                                    //Do nothing @ progress in 20
                                    break;
                                case 20:
                                    /*appLoadingProgressTitleTextView.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            appLoadingProgressTitleTextView.setText("Check the Internet connection ...");
                                        }
                                    });*/
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {

                                            //create database from given database file in assets
                                            try {
                                                if (!localDatabaseSQLiteOpenHelper.checkDataBase()) {
                                                    localDatabaseSQLiteOpenHelper.createDatabase();
                                                    localDatabaseSQLiteOpenHelper.openDataBase();
                                                }
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

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
                                                if (isActivityActivated) {
                                                    messageBalloonAlertDialog.show();
                                                }
                                            } else {

                                                //create internal app dir if not exists
                                                userPermissionServices.createInternalAppDirectories(appFilePath);

                                                //update the database if server database is modified
                                                userPermissionServices.updateLocalRecipesFromServerRecipes(WelcomeActivity.this);

                                                //update the recipe categories from the server data
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
                                    //Do nothing @ progress in 20
                                    break;
                                case 80:
                                    //Do nothing @ progress in 20
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

        //TODO need to fix this dual calling on this method from above switch
        /*if (onContinueCount == 0) {*/
        Map<String, String> lastLoginDetails = contentProviderAccessor.getLoginDetails(WelcomeActivity.this);
        if (!lastLoginDetails.isEmpty()) {
            LoginActivity.LOGGED_STATUS = 1;
            LoginActivity.LOGGED_USER_ID = lastLoginDetails.get("userId");
            LoginActivity.LOGGED_USER = lastLoginDetails.get("username");
            LoginActivity.LOGGED_USER_NAME = lastLoginDetails.get("fName");
            LoginActivity.LOGGED_USER_PASSWORD = lastLoginDetails.get("password");
            LoginActivity.LOGGED_USER_PIC_URL = lastLoginDetails.get("picUrl");

            Intent recipeIntent = new Intent(WelcomeActivity.this, RecipesActivity.class);
            startActivity(recipeIntent);
            finish();
        } else {
            Intent loginIntent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }

//            onContinueCount++;
        /*} else {
            Log.d("Tag", "This happens one time this is " + onContinueCount + " calling");
        }*/
    }

    private boolean isApplicationUpdatedForToday() {
        boolean status = false;

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String[] dateStringArray = dateFormat.format(date).split("-");

        int year = Integer.parseInt(dateStringArray[0]);
        int month = Integer.parseInt(dateStringArray[1]);
        int day = Integer.parseInt(dateStringArray[2]);

        Map<String, Integer> updatedDate = contentProviderAccessor.getUpdatedDate(context);


        if (year == updatedDate.get("updatedYear") &&
                month == updatedDate.get("updatedMonth") && day == updatedDate.get("updatedDay")) {
            status = true;
        } else {
            status = false;
        }

        return status;
    }
}
