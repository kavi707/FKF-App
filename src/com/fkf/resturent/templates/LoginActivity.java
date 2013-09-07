package com.fkf.resturent.templates;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fkf.resturent.R;
import com.fkf.resturent.database.LocalDatabaseSQLiteOpenHelper;

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

    public static String LOGGED_USER;
    public static String LOGGED_USER_PASSWORD;
    public static int LOGGED_STATUS = 0;


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
                //TODO these following values are for tempory usage. Need to get values from entered username password
                LOGGED_USER = "kavimal"; //tempory initialization
                LOGGED_USER_PASSWORD = "kavi123";
                LOGGED_STATUS = 1;

                Map<String, String> loginData = new HashMap<String, String>();
                loginData.put("loginStatus", "1");
                loginData.put("username",LOGGED_USER);
                loginData.put("password",LOGGED_USER_PASSWORD);

                localDatabaseSQLiteOpenHelper.insertLoginDetails(loginData);

                Intent recipesIntent = new Intent(LoginActivity.this, RecipesActivity.class);
                startActivity(recipesIntent);
                finish();
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
