package com.fkf.commercial.templates;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.fkf.commercial.R;
import com.fkf.commercial.database.LocalDatabaseSQLiteOpenHelper;
import com.fkf.commercial.services.ActivityUserPermissionServices;
import com.fkf.commercial.services.connections.ApiConnector;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kavi on 6/18/13.
 * Hold the new user register view functionalities.
 * register_layout.xml Activity class
 *
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class RegisterActivity extends Activity {

    private EditText firstNameEditText;
    private EditText userEmailEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText verifyPasswordEditText;

    private CheckBox newsAlertCheckBox;
    private Button userRegisterButton;

    private ProgressDialog progress;
    private Handler handler;

    final Context context = this;
    private AlertDialog messageBalloonAlertDialog;

    private String saltString = "ae26dde136fc01876b1ec2ba3adc47b7";

    private ApiConnector connector = new ApiConnector();
    private LocalDatabaseSQLiteOpenHelper localDatabaseSQLiteOpenHelper = new LocalDatabaseSQLiteOpenHelper(this);
    private ActivityUserPermissionServices userPermissionServices = new ActivityUserPermissionServices();
    private Map<String, String> loginResult = new HashMap<String, String>();
    private String finalUserName, finalPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        setUpView();
    }

    private void setUpView() {

        firstNameEditText = (EditText) findViewById(R.id.firstNameEditText);
        userEmailEditText = (EditText) findViewById(R.id.userEmailEditText);
        usernameEditText = (EditText) findViewById(R.id.registerUserNameEditText);
        passwordEditText = (EditText) findViewById(R.id.registerPasswordEditText);
        verifyPasswordEditText = (EditText) findViewById(R.id.verifyPasswordEditText);

        newsAlertCheckBox = (CheckBox) findViewById(R.id.newsletterCheckBox);
        newsAlertCheckBox.setChecked(true);
        userRegisterButton = (Button) findViewById(R.id.newRegisterButton);

        userRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userPermissionServices.isOnline(RegisterActivity.this)) {

                    userRegisterButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_btn_pressed_background));

                    String firstName = firstNameEditText.getText().toString();
                    String email = userEmailEditText.getText().toString();
                    String username = usernameEditText.getText().toString();
                    String password = passwordEditText.getText().toString();
                    String verifyPassword = verifyPasswordEditText.getText().toString();
                    String hashCode = "";

                    Map<String, String> userRegParams = new HashMap<String, String>();

                    if (firstName == null || firstName.equals("")) {
                        showMessageBalloon("First name is required");
                    } else if (email == null || email.equals("")) {
                        showMessageBalloon("Email is required");
                    } else if (username == null || username.equals("")) {
                        showMessageBalloon("username is required");
                    } else if (password == null || password.equals("")) {
                        showMessageBalloon("password is required");
                    } else if (verifyPassword == null || verifyPassword.equals("")) {
                        showMessageBalloon("password verification is required");
                    } else if (!password.equals(verifyPassword)) {
                        showMessageBalloon("password verification is failed");
                    } else {

                        boolean isChecked = newsAlertCheckBox.isChecked();

                        String md5String = email + username + saltString;
                        try {
                            MessageDigest md = MessageDigest.getInstance("MD5");
                            md.update(md5String.getBytes());
                            byte[] digest = md.digest();
                            StringBuffer sb = new StringBuffer();

                            for (int i = 0; i < digest.length; i++) {
                                String h = Integer.toHexString(0xFF & digest[i]);
                                while (h.length() < 2)
                                    h = "0" + h;
                                sb.append(h);
                            }

                            hashCode = sb.toString();

                            userRegParams.put("fName", firstName);
                            userRegParams.put("email", email);
                            userRegParams.put("uName", username);
                            userRegParams.put("pass", password);
                            userRegParams.put("hash", hashCode);
                            if (isChecked) {
                                userRegParams.put("newsAlert", "1");
                            } else {
                                userRegParams.put("newsAlert", "0");
                            }

                            Map<String, String> resultMap = connector.userCreate(userRegParams);

                            if (resultMap != null) {
                                if (resultMap.get("status").equals("true")) {

                                    finalUserName = username;
                                    finalPassword = password;

                                    progress = ProgressDialog.show(RegisterActivity.this, "Sign In", "Signing in with the new account");
                                    handler = new Handler(context.getMainLooper());

                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            firstNameEditText.setText(null);
                                            userEmailEditText.setText(null);
                                            usernameEditText.setText(null);
                                            passwordEditText.setText(null);
                                            verifyPasswordEditText.setText(null);

                                            loginResult = connector.userLogin(finalUserName, finalPassword);

                                            if (loginResult.get("loginStatus").equals("1")) {
                                                LoginActivity.LOGGED_USER_ID = loginResult.get("userId");
                                                LoginActivity.LOGGED_USER = loginResult.get("username");
                                                LoginActivity.LOGGED_USER_PASSWORD = loginResult.get("password");
                                                LoginActivity.LOGGED_USER_NAME = loginResult.get("fName");

                                                LoginActivity.LOGGED_STATUS = 1;

                                                localDatabaseSQLiteOpenHelper.insertLoginDetails(loginResult);
                                                //update the logged user's favorite recipes
                                                userPermissionServices.updateUserFavoriteRecipesFromServer(RegisterActivity.this);

                                                Intent recipesIntent = new Intent(RegisterActivity.this, RecipesActivity.class);
                                                startActivity(recipesIntent);
                                                finish();

                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        progress.dismiss();
                                                    }
                                                });
                                            } else if (loginResult.get("loginStatus").equals("2")) {
                                                progress.dismiss();
                                                Toast.makeText(getApplicationContext(),
                                                        "Login failed. Because of " + loginResult.get("msg"), Toast.LENGTH_LONG).show();
                                                openLoginActivity();
                                            }
                                        }
                                    });

                                } else if (resultMap.get("status").equals("false")) {
                                    Toast.makeText(getApplicationContext(),
                                            resultMap.get("msg"), Toast.LENGTH_LONG).show();
                                    firstNameEditText.setText(null);
                                    userEmailEditText.setText(null);
                                    usernameEditText.setText(null);
                                    passwordEditText.setText(null);
                                    verifyPasswordEditText.setText(null);

                                    userRegisterButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_btn_background));
                                }
                            }

                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Application is in offline mode. Please on your mobile data", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void showMessageBalloon(String msg) {
        messageBalloonAlertDialog = new AlertDialog.Builder(context)
                .setTitle(R.string.warning)
                .setMessage(msg)
                .setNegativeButton(R.string.ok, new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        messageBalloonAlertDialog.cancel();
                        userRegisterButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_btn_background));
                    }
                }).create();
        messageBalloonAlertDialog.show();
    }

    private void openLoginActivity() {
        userRegisterButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.login_btn_background));
        Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            this.openLoginActivity();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}