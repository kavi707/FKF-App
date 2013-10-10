package com.fkf.resturent.services.connections;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
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
                String modificationTimeStamp = jsonData.getString("last_updated");
                localDatabaseSQLiteOpenHelper.setLastModificationTimeStamp(modificationTimeStamp);

                JSONArray jsonRecipeArray = (JSONArray) jsonData.get("data");

                for(int i = 0; i < jsonRecipeArray.length(); i++) {
                    jsonData = jsonRecipeArray.getJSONObject(i);

                    int legacyEvent = jsonData.getInt("legacy");
                    Recipe getRecipe = new Recipe();
                    getRecipe.setProductId(jsonData.getString("id"));
                    getRecipe.setName(jsonData.getString("title"));
                    getRecipe.setImageUrl(jsonData.getString("image_s"));
                    getRecipe.setImageUrl_xs(jsonData.getString("image_xs"));
                    getRecipe.setImageUrl_s(jsonData.getString("image_s"));
                    getRecipe.setImageUrl_m(jsonData.getString("image_m"));
                    getRecipe.setImageUrl_l(jsonData.getString("image_l"));
                    String description = jsonData.getString("desc").replace("[\"", "").replace("\"]", "");
                    getRecipe.setDescription(description);
                    getRecipe.setRatings(jsonData.getInt("rating"));
                    getRecipe.setCategoryId(jsonData.getInt("category"));
                    getRecipe.setAddedDate(jsonData.getString("created"));
                    getRecipe.setLegacy(legacyEvent);

                    if (legacyEvent == 0) {
                        String instructions = jsonData.getString("instructions").replace("[\"", "").replace("\"]", "").replace("\",\"", ",");
                        getRecipe.setInstructions(instructions);

                        String ingredients = jsonData.getString("ingredients");
                        getRecipe.setIngredients(ingredients);
                    } else if(legacyEvent == 1) {
                        JSONArray jsonRecipeDataArray = (JSONArray) jsonData.get("body");
                        String bodyString = "";
                        for (int j = 0; j < jsonRecipeDataArray.length(); j++) {
                            bodyString = bodyString + "#" + jsonRecipeDataArray.getString(j);
                        }
                        getRecipe.setBody(bodyString);
                    }

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
