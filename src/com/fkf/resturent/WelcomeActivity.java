package com.fkf.resturent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.fkf.resturent.services.ActivityUserPermissionServices;
import com.fkf.resturent.templates.LoginActivity;

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
    protected static final int TIMER_RUNTIME = 10000;
    protected boolean mbActive;

    private ProgressBar appLoadingProgressBar;
    private TextView appLoadingProgressTitleTextView;

    final Context context = this;
    private Handler mHandler;

    private AlertDialog messageBalloonAlertDialog;

    private ActivityUserPermissionServices userPermissionServices = new ActivityUserPermissionServices();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

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
                                            appLoadingProgressTitleTextView.setText("Loading configurations ...");
                                        }
                                    });
                                    break;
                                case 20:
                                    appLoadingProgressTitleTextView.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            appLoadingProgressTitleTextView.setText("Loading the application templates");
                                        }
                                    });
                                    break;
                                case 50:
                                    appLoadingProgressTitleTextView.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            appLoadingProgressTitleTextView.setText("Check the local database connections ...");
                                        }
                                    });
                                    break;
                                case 80:
                                    appLoadingProgressTitleTextView.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            appLoadingProgressTitleTextView.setText("Check the Internet connection ...");
                                        }
                                    });
                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            //check the internet connection for the device
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
                                                        .setNegativeButton(R.string.no, new AlertDialog.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                finish();
                                                            }
                                                        }).create();
                                                messageBalloonAlertDialog.show();
                                            }
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
        Intent loginIntent = new Intent(WelcomeActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }
}
