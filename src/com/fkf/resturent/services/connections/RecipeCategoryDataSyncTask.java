package com.fkf.resturent.services.connections;

import android.content.Context;
import android.os.AsyncTask;
import com.fkf.resturent.database.LocalDatabaseSQLiteOpenHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Background task for sync server recipes category data with the local data
 * Created by kavi on 9/3/13.
 *
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class RecipeCategoryDataSyncTask extends AsyncTask<String, Void, String> {

    private LocalDatabaseSQLiteOpenHelper localDatabaseSQLiteOpenHelper;
    private ApiConnector connector = new ApiConnector();

    public RecipeCategoryDataSyncTask(Context context) {
        this.localDatabaseSQLiteOpenHelper = new LocalDatabaseSQLiteOpenHelper(context);
    }

    @Override
    protected String doInBackground(String... strings) {

        String jsonResult = connector.callWebService(strings[0]);

        try {
            if(jsonResult != null) {
                this.localDatabaseSQLiteOpenHelper.deleteAllCategories();
                JSONArray jsonArray = new JSONArray(jsonResult);
                JSONObject jsonData = null;
                for(int i=0; i < jsonArray.length(); i++) {
                    jsonData = jsonArray.getJSONObject(i);

                    this.localDatabaseSQLiteOpenHelper.addNewCategory(jsonData.getInt("id"), jsonData.getString("name"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            throw e;
        }

        return null;
    }
}
