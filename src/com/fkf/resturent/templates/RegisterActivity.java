package com.fkf.resturent.templates;

import android.app.Activity;
import android.os.Bundle;
import com.fkf.resturent.R;

/**
 * Created by kavi on 6/18/13.
 * Hold the new user register view functionalities.
 * register_layout.xml Activity class
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class RegisterActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        setUpView();
    }

    private void setUpView() {

    }
}
