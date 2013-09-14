package com.fkf.resturent.services.connections;

import android.content.Context;
import android.os.AsyncTask;
import com.fkf.resturent.database.LocalDatabaseSQLiteOpenHelper;
import com.fkf.resturent.database.Recipe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Background task for sync server recipes data with the local data
 * Created by kavi on 9/2/13.
 *
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class RecipeDataSyncTask extends AsyncTask<String, Void, String> {

    private LocalDatabaseSQLiteOpenHelper localDatabaseSQLiteOpenHelper;
    private ApiConnector connector = new ApiConnector();

    public RecipeDataSyncTask(Context context){
        this.localDatabaseSQLiteOpenHelper = new LocalDatabaseSQLiteOpenHelper(context);
    }

    @Override
    protected String doInBackground(String... strings) {

        String jsonResult = connector.callWebService(strings[0]);

        try {
            if (jsonResult != null) {
                JSONObject jsonData = new JSONObject(jsonResult);

                JSONArray jsonRecipeArray = (JSONArray) jsonData.get("data");

                for(int i = 0; i < jsonRecipeArray.length(); i++) {
                    jsonData = jsonRecipeArray.getJSONObject(i);

                    Recipe getRecipe = new Recipe();
                    getRecipe.setProductId(jsonData.getString("id"));
                    getRecipe.setName(jsonData.getString("title"));
                    getRecipe.setImageUrl(jsonData.getString("image_s"));

                    String description = jsonData.getString("desc").replace("[\"", "").replace("\"]", "");
                    getRecipe.setDescription(description);

                    String instructions = jsonData.getString("instructions").replace("[\"", "").replace("\"]", "");
                    getRecipe.setInstructions(instructions);

                    getRecipe.setRatings(jsonData.getInt("rating"));
                    getRecipe.setCategoryId(jsonData.getInt("category"));
                    getRecipe.setAddedDate(jsonData.getString("created"));

                    localDatabaseSQLiteOpenHelper.saveRecipe(getRecipe);
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
