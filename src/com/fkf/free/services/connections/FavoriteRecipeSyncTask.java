package com.fkf.free.services.connections;

import android.content.Context;
import android.os.AsyncTask;
import com.fkf.free.database.LocalDatabaseSQLiteOpenHelper;
import com.fkf.free.templates.LoginActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Background task for sync user favorite recipes data with the local data
 * Created by kavi on 9/14/13.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class FavoriteRecipeSyncTask extends AsyncTask<String, Void, String> {

    private LocalDatabaseSQLiteOpenHelper localDatabaseSQLiteOpenHelper;
    private ApiConnector connector = new ApiConnector();

    public FavoriteRecipeSyncTask(Context context) {
        this.localDatabaseSQLiteOpenHelper = new LocalDatabaseSQLiteOpenHelper(context);
    }

    @Override
    protected String doInBackground(String... strings) {

        String jsonResult = connector.callWebService(strings[0]);

        try {
            if(jsonResult != null) {
                this.localDatabaseSQLiteOpenHelper.deleteAllUserFavoriteRecipes();
                JSONArray jsonArray = new JSONArray(jsonResult);
                JSONObject jsonData = null;
                for(int i=0; i < jsonArray.length(); i++) {
                    jsonData = jsonArray.getJSONObject(i);

                    this.localDatabaseSQLiteOpenHelper.saveUserFavoriteRecipes(jsonData.getString("id"), LoginActivity.LOGGED_USER_ID);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
