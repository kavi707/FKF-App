package com.fkf.resturent.templates;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.fkf.resturent.R;
import com.fkf.resturent.database.LocalDatabaseSQLiteOpenHelper;
import com.fkf.resturent.services.ActivityUserPermissionServices;
import com.fkf.resturent.services.connections.ApiConnector;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by kavi on 6/16/13.
 * Hold the login_layout view functionalities.
 * login_layout.xml Activity class
 *
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class LoginActivity extends Activity {

    private Button registerButton;
    private Button loginButton;
    private TextView browsRecipesTextView;
    private TextView upperTextView;

    private Context context = this;
    private Handler handler;

    private EditText usernameEditText;
    private EditText passwordEditText;

    private LinearLayout usernameLinearLayout;
    private LinearLayout passwordLinearLayout;
    private LinearLayout scrollLinearLayout;
    private ImageView logoImageView;
    private LinearLayout.LayoutParams logoViewParams, usernameParams, passwordParams, loginButtonParams;
    private LinearLayout.LayoutParams usernameEditTextParams, passwordEditTextParams;
    private LinearLayout.LayoutParams upperTextViewParams;

    private ProgressDialog progress;

    public static String LOGGED_USER_ID;
    public static String LOGGED_USER;
    public static String LOGGED_USER_PASSWORD;
    public static String LOGGED_USER_NAME;
    public static int LOGGED_STATUS = 0;

    private Map<String, String> loginResult = new HashMap<String, String>();
    private boolean errorStatus = false;
    private String errorMsg = "";
    private final int[] scrollLayoutHeightArray = new int[1];

    private ApiConnector connector = new ApiConnector();
    private ActivityUserPermissionServices userPermissionServices = new ActivityUserPermissionServices();
    private LocalDatabaseSQLiteOpenHelper localDatabaseSQLiteOpenHelper = new LocalDatabaseSQLiteOpenHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        setUpView();
    }

    private void setUpView() {
        registerButton = (Button) findViewById(R.id.registerButton);
        loginButton = (Button) findViewById(R.id.loginButton);
        browsRecipesTextView = (TextView) findViewById(R.id.browsRecipesTextView);

        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);

        usernameLinearLayout = (LinearLayout) findViewById(R.id.usernameLinearLayout);
        passwordLinearLayout = (LinearLayout) findViewById(R.id.passwordLinearLayout);
        logoImageView = (ImageView) findViewById(R.id.logoImageView);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //database backup called - this is for tempory usage
                /*try {
                    localDatabaseSQLiteOpenHelper.backupDatabase();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
                finish();
            }
        });

        Map<String, Integer> deviceWidthAndHeight = userPermissionServices.getDeviceWidthAndHeight(LoginActivity.this);
        int width = deviceWidthAndHeight.get("width");

        if (width <= 480) {
            logoViewParams = (LinearLayout.LayoutParams)logoImageView.getLayoutParams();
            logoViewParams.width = 350;
            logoImageView.setLayoutParams(logoViewParams);

            usernameParams = (LinearLayout.LayoutParams)usernameLinearLayout.getLayoutParams();
            usernameParams.width = 350;
            usernameLinearLayout.setLayoutParams(usernameParams);

            usernameEditTextParams = (LinearLayout.LayoutParams)usernameEditText.getLayoutParams();
            usernameEditTextParams.width = 285;
            usernameEditText.setLayoutParams(usernameEditTextParams);

            passwordParams = (LinearLayout.LayoutParams)passwordLinearLayout.getLayoutParams();
            passwordParams.width = 350;
            passwordLinearLayout.setLayoutParams(passwordParams);

            passwordEditTextParams = (LinearLayout.LayoutParams)passwordEditText.getLayoutParams();
            passwordEditTextParams.width = 285;
            passwordEditText.setLayoutParams(passwordEditTextParams);

            loginButtonParams = (LinearLayout.LayoutParams)loginButton.getLayoutParams();
            loginButtonParams.width = 350;
            loginButton.setLayoutParams(loginButtonParams);
            loginButton.setTextSize(20);
        }

        int height = deviceWidthAndHeight.get("height");
        upperTextView = (TextView) findViewById(R.id.upperTextView);
        upperTextViewParams = (LinearLayout.LayoutParams)upperTextView.getLayoutParams();

        scrollLinearLayout = (LinearLayout) findViewById(R.id.scrollLinearLayout);

        if(height <= 800) {
            int newHeight = 110;
            upperTextViewParams.height = newHeight;
            upperTextView.setLayoutParams(upperTextViewParams);
        } else if(height > 800) {
            upperTextView.setLayoutParams(upperTextViewParams);
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userPermissionServices.isOnline(LoginActivity.this)) {
                    progress = ProgressDialog.show(LoginActivity.this, "Login", "Check user and login to the systems. Please wait ...");

                    handler = new Handler(context.getMainLooper());

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String getUsername = usernameEditText.getText().toString();
                            String getPassword = passwordEditText.getText().toString();
                            loginResult = connector.userLogin(getUsername, getPassword);

                            if (loginResult.get("loginStatus").equals("1")) {

                                LOGGED_USER_ID = loginResult.get("userId");
                                LOGGED_USER = loginResult.get("username");
                                LOGGED_USER_PASSWORD = loginResult.get("password");
                                LOGGED_USER_NAME = loginResult.get("fName");

                                Log.d("user's name : >>>>>>>>>>>>>> 2 ", loginResult.get("fName"));

                                LOGGED_STATUS = 1;

                                localDatabaseSQLiteOpenHelper.insertLoginDetails(loginResult);
                                //update the logged user's favorite recipes
                                userPermissionServices.updateUserFavoriteRecipesFromServer(LoginActivity.this);

                                Intent recipesIntent = new Intent(LoginActivity.this, RecipesActivity.class);
                                startActivity(recipesIntent);
                                finish();

                                errorStatus = false;

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progress.dismiss();
                                    }
                                });

                            } else if (loginResult.get("loginStatus").equals("2")) {
                                progress.dismiss();
                                errorStatus = true;
                                errorMsg = loginResult.get("msg");
                                Toast.makeText(getApplicationContext(),
                                        "Login failed. Because of " + errorMsg, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Application is on offline mode. If need to login, please put your device to online", Toast.LENGTH_LONG).show();
                }
            }
        });

        browsRecipesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress = ProgressDialog.show(LoginActivity.this, "Loading", "Loading the current recipes. Please wait ...");
                handler = new Handler(context.getMainLooper());

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        /*try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/

                        LOGGED_STATUS = 0;
                        Intent recipesIntent = new Intent(LoginActivity.this, RecipesActivity.class);
                        startActivity(recipesIntent);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress.dismiss();
                            }
                        });
                    }
                });

                /*AsyncTask<Void, Void, Void> updateTask = new AsyncTask<Void, Void, Void>(){
                    ProgressDialog dialog = new ProgressDialog(LoginActivity.this);
                    @Override
                    protected void onPreExecute() {
                        // what to do before background task
                        dialog.setTitle("Loading...");
                        dialog.setMessage("Please wait.");
                        dialog.setIndeterminate(true);
                        dialog.setCancelable(false);
                        dialog.show();
                    }

                    @Override
                    protected Void doInBackground(Void... params) {
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        // what to do when background task is completed
                        long delayInMillis = 10000;
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        }, delayInMillis);

                        LOGGED_STATUS = 0;
                        Intent recipesIntent = new Intent(LoginActivity.this, RecipesActivity.class);
                        startActivity(recipesIntent);
                        dialog.dismiss();
                    };

                    @Override
                    protected void onCancelled() {
                        dialog.dismiss();
                        super.onCancelled();
                    }
                };
                updateTask.execute((Void[])null);*/
            }
        });
    }
}
