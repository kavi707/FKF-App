package com.fkf.resturent.services.connections;

import android.app.Activity;
import android.util.Log;
import com.fkf.resturent.database.Recipe;
import com.fkf.resturent.templates.LoginActivity;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * setup and communicate with the server connections
 * Created by kavi on 7/9/13.
 *
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class ApiConnector {

    //TODO this is not complete
    public Map<String, Object> userLogin(Map<String, String> loginData) {
        Map<String, Object> statusMap = new HashMap<String, Object>();
        String userLoginUrl = "http://www.fauziaskitchenfun.com/api/user/login";
        String result = null;

        JSONObject reqParams = new JSONObject();
        try {
            reqParams.put("username", loginData.get("username"));
            reqParams.put("password", loginData.get("password"));

            result = this.sendHTTPPost(reqParams, userLoginUrl);
            JSONObject jsonData = new JSONObject(result);
            JSONObject jsonUserData = new JSONObject((String) jsonData.get("user"));

            String userId = jsonUserData.getString("uid");
            statusMap.put("status", true);
            statusMap.put("userId", userId);
            statusMap.put("username", loginData.get("username"));
            statusMap.put("password", loginData.get("password"));
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return  statusMap;
    }

    /**
     * add recipe to user favorite list
     * @param recipeId
     * @return
     */
    public boolean addToMyFavorite(String recipeId) {

        String addMyFavoriteUrl = "http://www.fauziaskitchenfun.com/api/favourites/create";
        String result = null;
        boolean status = false;

        JSONObject reqParams = new JSONObject();
        try {
            reqParams.put("username", LoginActivity.LOGGED_USER);
            reqParams.put("password", LoginActivity.LOGGED_USER_PASSWORD);
            reqParams.put("id", recipeId);
            reqParams.put("action", "add");

            result = this.sendHTTPPost(reqParams, addMyFavoriteUrl);
            JSONObject jsonData = new JSONObject(result);

            String state = jsonData.getString("status");
            if(state.equals(true))
                status = true;
            else
                status = false;
        } catch (JSONException e) {
            status = false;
            e.printStackTrace();
        }

        return status;
    }

    /**
     * remove recipe from user favorite
     * @param recipeId
     * @return
     */
    public boolean removeFromMyFavorite(String recipeId) {
        String addMyFavoriteUrl = "http://www.fauziaskitchenfun.com/api/favourites/create";
        String result = null;
        boolean status = false;

        JSONObject reqParams = new JSONObject();
        try {
            reqParams.put("username", LoginActivity.LOGGED_USER);
            reqParams.put("password", LoginActivity.LOGGED_USER_PASSWORD);
            reqParams.put("id", recipeId);
            reqParams.put("action", "remove");

            result = this.sendHTTPPost(reqParams, addMyFavoriteUrl);
            JSONObject jsonData = new JSONObject(result);

            String state = jsonData.getString("status");
            if(state.equals(true))
                status = true;
            else
                status = false;
        } catch (JSONException e) {
            status = false;
            e.printStackTrace();
        }

        return status;
    }

    /**
     * check the given recipe is user favorite or not
     * @param recipeId
     * @return
     */
    public boolean isMyFavorite(String recipeId) {
        String addMyFavoriteUrl = "http://www.fauziaskitchenfun.com/api/favourites/create";
        String result = null;
        boolean status = false;

        JSONObject reqParams = new JSONObject();
        try {
            reqParams.put("username", LoginActivity.LOGGED_USER);
            reqParams.put("password", LoginActivity.LOGGED_USER_PASSWORD);
            reqParams.put("id", recipeId);
            reqParams.put("action", "check");

            result = this.sendHTTPPost(reqParams, addMyFavoriteUrl);
            JSONObject jsonData = new JSONObject(result);

            String state = jsonData.getString("status");
            if(state.equals(true))
                status = true;
            else
                status = false;
        } catch (JSONException e) {
            status = false;
            e.printStackTrace();
        }

        return status;
    }

    /**
     * update the local database from logged user favorite recipes
     * @param activity
     */
    public void getUserFavoriteRecipeIdsFromServer(Activity activity) {
        FavoriteRecipeSyncTask favoriteRecipeSyncTask = new FavoriteRecipeSyncTask(activity);
        favoriteRecipeSyncTask.execute("http://www.fauziaskitchenfun.com/api/favourites/retrieve?userid=" + LoginActivity.LOGGED_USER_ID);
    }

    /**
     * get the updated recipes from the server using given time stamp
     * @param timeStamp
     * @param activity
     */
    public void getRecipesFromServer(String timeStamp, Activity activity) {

        //TODO need to remove the following hardcoded timeStamp     value
        //this initialization is for testing
        timeStamp = "1376430210";
        RecipeDataSyncTask dataSyncTask = new RecipeDataSyncTask(activity);
        dataSyncTask.execute("http://www.fauziaskitchenfun.com/api/recipe/retrieve?timestamp=" + timeStamp);
    }

    /**
     * get all categories from the server
     * @param activity
     */
    public void getRecipeCategoriesFromServer(Activity activity) {
        RecipeCategoryDataSyncTask dataSyncTask = new RecipeCategoryDataSyncTask(activity);
        dataSyncTask.execute("http://www.fauziaskitchenfun.com/api/category/retrieve");
    }

    /**
     * get latest yummys from the server
     * @return
     */
    public List<Recipe> getLatestYummysFromServer() {
        String jsonResult = callWebService("http://www.fauziaskitchenfun.com/api/latest");
        List<Recipe> recipeList = new ArrayList<Recipe>();
        try {
            if (jsonResult != null) {
                JSONArray jsonArray = new JSONArray(jsonResult);
                JSONObject jsonData = null;
                for(int i = 0; i < jsonArray.length(); i++) {
                    jsonData = jsonArray.getJSONObject(i);

                    Recipe getRecipe = new Recipe();
                    getRecipe.setProductId(jsonData.getString("nid"));
                    getRecipe.setName(jsonData.getString("title"));

                    recipeList.add(getRecipe);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            throw e;
        }

        return recipeList;
    }

    /**
     * function to call to rest connections
     * @param requestUrl
     */
    protected String callWebService(String requestUrl){

        String restCallResult = null;

        HttpClient httpclient = new DefaultHttpClient();
        HttpGet request = new HttpGet(requestUrl);
        ResponseHandler<String> handler = new BasicResponseHandler();
        try {
            restCallResult = httpclient.execute(request, handler);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        httpclient.getConnectionManager().shutdown();

        return restCallResult;
    }

    /**
     * function to send http posts with json object
     * @param req
     */
    private String sendHTTPPost(JSONObject req, String url){

        HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);
        HttpResponse response;

        String responseResult = null;

        try {
//            HttpPost post = new HttpPost("http://10.0.2.2:7000/sms/send");
            HttpPost post = new HttpPost(url);

            StringEntity se = new StringEntity(req.toString());
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            post.setEntity(se);
            response = client.execute(post);

            if(response != null) {
                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();

                if(statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream in = entity.getContent();
                    BufferedReader bfr = new BufferedReader(new InputStreamReader(in));
                    String line;
                    StringBuilder builder = new StringBuilder("");
                    while ((line = bfr.readLine()) != null){
                        builder.append(line + "\n");
                    }
                    in.close();
                    String result = builder.toString();
                    responseResult = result;
                    Log.d("status","Success Response : " + result);
                } else {
                    Log.d("error status code", String.valueOf(statusCode));
                    responseResult = "error";
                }
            } else {
                Log.d("Error", "null response after sending http req");
                responseResult = "error";
            }

        } catch (Exception ex) {
            Log.d("Exception", ex.toString());
        }

        return responseResult;
    }
}
