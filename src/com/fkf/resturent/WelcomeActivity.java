package com.fkf.resturent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.fkf.resturent.templates.LoginActivity;

/**
 * Holding the loading ui
 * main.xml Activity class
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        setUpViews();
    }

    private void setUpViews() {

        appLoadingProgressBar = (ProgressBar) findViewById(R.id.loadingProgressBar);

        final Thread timerThread = new Thread() {
            @Override
            public void run() {
                mbActive = true;

                try {
                    int waited = 0;
                    while(mbActive && (waited < TIMER_RUNTIME)) {
                        sleep(200);
                        if(mbActive) {
                            waited += 200;
                            updateProgress(waited);
                        }
                    }
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    onContinue();
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void updateProgress(final int timePassed) {
        if(null != appLoadingProgressBar) {
            // Ignore rounding error here
            final int progress = appLoadingProgressBar.getMax() * timePassed / TIMER_RUNTIME;
            appLoadingProgressBar.setProgress(progress);
        }
    }

    public void onContinue() {
        Intent loginIntent = new Intent(WelcomeActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }
}
