package com.fkf.commercial.templates;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.fkf.commercial.R;
import com.fkf.commercial.database.LocalDatabaseSQLiteOpenHelper;
import com.fkf.commercial.services.ActivityUserPermissionServices;
import com.fkf.commercial.services.connections.ApiConnector;
import java.util.HashMap;
import java.util.Map;

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

    private Context context = this;
    private Handler handler;

    private EditText usernameEditText;
    private EditText passwordEditText;

    private LinearLayout usernameLinearLayout;
    private LinearLayout passwordLinearLayout;
    private ImageView logoImageView;
    private LinearLayout.LayoutParams logoViewParams, usernameParams, passwordParams, loginButtonParams;
    private LinearLayout.LayoutParams usernameEditTextParams, passwordEditTextParams;

    private ProgressDialog progress;

    public static String LOGGED_USER_ID;
    public static String LOGGED_USER;
    public static String LOGGED_USER_PASSWORD;
    public static String LOGGED_USER_NAME;
    public static int LOGGED_STATUS = 0;

    private Map<String, String> loginResult = new HashMap<String, String>();
    private boolean errorStatus = false;
    private String errorMsg = "";

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
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
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

                        LOGGED_STATUS = 0;
                        Intent recipesIntent = new Intent(LoginActivity.this, RecipesActivity.class);
                        startActivity(recipesIntent);
                        finish();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress.dismiss();
                            }
                        });
                    }
                });
            }
        });
    }
}
