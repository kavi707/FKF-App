package com.fkf.resturent.templates;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import com.fkf.resturent.R;

/**
 * Created by kavi on 6/18/13.
 * Hold the new user register view functionalities.
 * register_layout.xml Activity class
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class RegisterActivity extends Activity {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText userEmailEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;

    private CheckBox newsAlertCheckBox;
    private Button userRegisterButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        setUpView();
    }

    private void setUpView() {

        firstNameEditText = (EditText) findViewById(R.id.firstNameEditText);
        lastNameEditText = (EditText) findViewById(R.id.lastNameEditText);
        userEmailEditText = (EditText) findViewById(R.id.userEmailEditText);
        usernameEditText = (EditText) findViewById(R.id.registerUserNameEditText);
        passwordEditText = (EditText) findViewById(R.id.registerPasswordEditText);

        newsAlertCheckBox = (CheckBox) findViewById(R.id.newsletterCheckBox);
        userRegisterButton = (Button) findViewById(R.id.newRegisterButton);
    }
}
