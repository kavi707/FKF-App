package com.fkf.commercial.templates;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
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
    private TextView spaceTextView;

    private Context context = this;
    private Handler handler;

    private EditText usernameEditText;
    private EditText passwordEditText;

    private LinearLayout usernameLinearLayout;
    private LinearLayout passwordLinearLayout;
    private ImageView logoImageView;
    private LinearLayout.LayoutParams logoViewParams, usernameParams, passwordParams, loginButtonParams, spaceTextViewParams;
    private LinearLayout.LayoutParams usernameEditTextParams, passwordEditTextParams;

    private ProgressDialog progress;

    private AlertDialog messageBalloonAlertDialog;

    public static String LOGGED_USER_ID;
    public static String LOGGED_USER;
    public static String LOGGED_USER_PASSWORD;
    public static String LOGGED_USER_NAME;
    public static String LOGGED_USER_PIC_URL;
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

    /**
     * Called after {@link #onRestoreInstanceState}, {@link #onRestart}, or
     * {@link #onPause}, for your activity to start interacting with the user.
     * This is a good place to begin animations, open exclusive-access devices
     * (such as the camera), etc.
     * <p/>
     * <p>Keep in mind that onResume is not the best indicator that your activity
     * is visible to the user; a system window such as the keyguard may be in
     * front.  Use {@link #onWindowFocusChanged} to know for certain that your
     * activity is visible to the user (for example, to resume a game).
     * <p/>
     * <p><em>Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.</em></p>
     *
     * @see #onRestoreInstanceState
     * @see #onRestart
     * @see #onPostResume
     * @see #onPause
     */
    @Override
    protected void onResume() {
        super.onResume();
        registerButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.register_btn_background));
    }

    private void setUpView() {
        registerButton = (Button) findViewById(R.id.registerButton);
        loginButton = (Button) findViewById(R.id.loginButton);
        browsRecipesTextView = (TextView) findViewById(R.id.browsRecipesTextView);

        spaceTextView = (TextView) findViewById(R.id.spaceTextView);

        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);

        usernameLinearLayout = (LinearLayout) findViewById(R.id.usernameLinearLayout);
        passwordLinearLayout = (LinearLayout) findViewById(R.id.passwordLinearLayout);
        logoImageView = (ImageView) findViewById(R.id.logoImageView);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.register_btn_pressed_background));
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
                finish();
            }
        });

        Map<String, Integer> deviceWidthAndHeight = userPermissionServices.getDeviceWidthAndHeight(LoginActivity.this);
        int width = deviceWidthAndHeight.get("width");

        if (width <= 480) {
            spaceTextViewParams = (LinearLayout.LayoutParams) spaceTextView.getLayoutParams();
            spaceTextViewParams.height = 80;
            spaceTextView.setLayoutParams(spaceTextViewParams);

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
                    loginButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_btn_pressed_background));
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
                                LOGGED_USER_PIC_URL = loginResult.get("picUrl");

                                LOGGED_STATUS = 1;

                                localDatabaseSQLiteOpenHelper.insertLoginDetails(loginResult);
                                //update the logged user's favorite recipes
                                userPermissionServices.updateUserFavoriteRecipesFromServer(LoginActivity.this);

//                                Intent recipesIntent = new Intent(LoginActivity.this, RecipesActivity.class);
//                                startActivity(recipesIntent);
                                Intent recipesMenuIntent = new Intent(LoginActivity.this, RecipesMenuActivity.class);
                                startActivity(recipesMenuIntent);
                                LoginActivity.this.finish();

                                errorStatus = false;

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progress.dismiss();
                                    }
                                });

                            } else if (loginResult.get("loginStatus").equals("2")) {
                                loginButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_btn_background));
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
//                        Intent recipesIntent = new Intent(LoginActivity.this, RecipesActivity.class);
//                        startActivity(recipesIntent);
                        Intent recipesMenuIntent = new Intent(LoginActivity.this, RecipesMenuActivity.class);
                        startActivity(recipesMenuIntent);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            messageBalloonAlertDialog = new AlertDialog.Builder(context)
                    .setTitle(R.string.warning)
                    .setMessage("Do you want to close Fauzia's Kitchen Fun ?")
                    .setPositiveButton(R.string.yes, new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            LoginActivity.this.finish();
                        }
                    })
                    .setNegativeButton(R.string.no, new AlertDialog.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            messageBalloonAlertDialog.cancel();
                        }
                    }).create();
            messageBalloonAlertDialog.show();
        }
        return super.onKeyDown(keyCode, event);
    }
}