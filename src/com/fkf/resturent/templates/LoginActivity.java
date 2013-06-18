package com.fkf.resturent.templates;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.fkf.resturent.R;

/**
 * Created by kavi on 6/16/13.
 * Hold the login_layout view functionalities.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class LoginActivity extends Activity {

    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        setUpView();
    }

    private void setUpView() {
        registerButton = (Button) findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });
    }
}
