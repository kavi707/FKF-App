package com.fkf.resturent.templates;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fkf.resturent.R;
import com.fkf.resturent.database.LocalDatabaseSQLiteOpenHelper;
import com.fkf.resturent.services.ActivityUserPermissionServices;
import com.fkf.resturent.services.connections.ApiConnector;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kavi on 6/16/13.
 * Hold the login_layout view functionalities.
 * login_layout.xml Activity class
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class LoginActivity extends Activity {

    private Button registerButton;
    private Button loginButton;
    private TextView browsRecipesTextView;

    private EditText usernameEditText;
    private EditText passwordEditText;

    public static String LOGGED_USER_ID;
    public static String LOGGED_USER;
    public static String LOGGED_USER_PASSWORD;
    public static int LOGGED_STATUS = 0;

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

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getUsername = usernameEditText.getText().toString();
                String getPassword = passwordEditText.getText().toString();

                if (userPermissionServices.isOnline(LoginActivity.this)) {
                    Map<String, String> loginResult = connector.userLogin(getUsername, getPassword);

                    if (loginResult.get("loginStatus").equals("1")) {

                        LOGGED_USER_ID = loginResult.get("userId");
                        LOGGED_USER = loginResult.get("username");
                        LOGGED_USER_PASSWORD = loginResult.get("password");
                        LOGGED_STATUS = 1;

                        localDatabaseSQLiteOpenHelper.insertLoginDetails(loginResult);
                        //update the logged user's favorite recipes
                        userPermissionServices.updateUserFavoriteRecipesFromServer(LoginActivity.this);

                        Intent recipesIntent = new Intent(LoginActivity.this, RecipesActivity.class);
                        startActivity(recipesIntent);
                        finish();
                    } else if (loginResult.get("loginStatus").equals("2")) {
                        //TODO need to handle username password error case
                    }
                }
            }
        });

        browsRecipesTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LOGGED_STATUS = 0;
                Intent recipesIntent = new Intent(LoginActivity.this, RecipesActivity.class);
                startActivity(recipesIntent);
            }
        });
    }
}
