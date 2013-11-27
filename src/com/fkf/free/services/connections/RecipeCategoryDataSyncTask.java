package com.fkf.free.services.connections;

import android.content.Context;
import android.os.AsyncTask;
import com.fkf.free.database.LocalDatabaseSQLiteOpenHelper;
import com.fkf.free.database.dbprovider.ContentProviderAccessor;
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
    private ContentProviderAccessor contentProviderAccessor;
    private ApiConnector connector = new ApiConnector();
    private Context context;

    public RecipeCategoryDataSyncTask(Context context) {
        this.context = context;
        this.localDatabaseSQLiteOpenHelper = new LocalDatabaseSQLiteOpenHelper(context);
        this.contentProviderAccessor = new ContentProviderAccessor();
    }

    @Override
    protected String doInBackground(String... strings) {

        String jsonResult = connector.callWebService(strings[0]);

        try {
            if(jsonResult != null) {
//                this.localDatabaseSQLiteOpenHelper.deleteAllCategories(context);
                this.contentProviderAccessor.deleteAllCategories(context);
                JSONArray jsonArray = new JSONArray(jsonResult);
                JSONObject jsonData = null;
                for(int i=0; i < jsonArray.length(); i++) {
                    jsonData = jsonArray.getJSONObject(i);

//                    this.localDatabaseSQLiteOpenHelper.addNewCategory(jsonData.getInt("id"), jsonData.getString("name"), context);
                    this.contentProviderAccessor.saveNewCategory(jsonData.getInt("id"), jsonData.getString("name"), context);
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
