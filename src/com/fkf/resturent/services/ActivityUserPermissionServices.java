package com.fkf.resturent.services;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * holding the service use by the templates
 * Created by kavi on 7/2/13.
 *
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class ActivityUserPermissionServices {

    /**
     * check the internet connection in the device for running application
     * @return boolean
     */
    public boolean isOnline(Activity activity) {

        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            return true;
        }

        return false;
    }
}
