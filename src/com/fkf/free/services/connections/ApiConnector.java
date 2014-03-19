package com.fkf.free.services.connections;

import android.app.Activity;
import android.util.Log;
import com.fkf.free.database.LocalDatabaseSQLiteOpenHelper;
import com.fkf.free.database.PopularOrLatestRecipe;
import com.fkf.free.templates.LoginActivity;
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

    /**
     * register a new user from given params
     * @param userRegParams
     * @return
     */
    public Map<String, String> userCreate(Map<String, String> userRegParams) {
        Map<String, String> statusMap = new HashMap<String, String>();
        String userRegUrl = "http://www.fauziaskitchenfun.com/api/user_add";
        String result = null;

        JSONObject reqParams = new JSONObject();
        try {
//            reqParams.put("name", userRegParams.get("uName"));
            reqParams.put("name", "iwa");
//            reqParams.put("mail", userRegParams.get("email"));
            reqParams.put("mail", "iwa@gmail.com");
//            reqParams.put("pass", userRegParams.get("pass"));
            reqParams.put("pass", "kavi");
//            reqParams.put("field_fname[und][0][value]", userRegParams.get("fName"));
//            reqParams.put("field_fname[und][0][value]", "kavi");
            /*if(userRegParams.get("newsAlert").equals("1")) {
                reqParams.put("newsletters[2]", 2);
            }*/
//            reqParams.put("hash", userRegParams.get("hash"));
            reqParams.put("hash", "8d9dceaeffcf7dc14118b695fc7f70e8");

            result = this.sendHTTPPost(reqParams, userRegUrl);

            Log.d("User Reg >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>.", result);
            JSONObject jsonData = new JSONObject(result);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return statusMap;
    }

    /**
     * this is for user login post method. Its returns logged user's user id
     * @param username
     * @param password
     * @return statusMap
     */
    public Map<String, String> userLogin(String username, String password) {
        Map<String, String> statusMap = new HashMap<String, String>();
        String userLoginUrl = "http://www.fauziaskitchenfun.com/api/user_login";
        String result = null;

        JSONObject reqParams = new JSONObject();
        try {
            reqParams.put("username", username);
            reqParams.put("password", password);

            result = this.sendHTTPPost(reqParams, userLoginUrl);
            JSONObject jsonData = new JSONObject(result);
            String loginStatus = jsonData.getString("status");
            if(loginStatus.equals("true")) {
                JSONObject jsonUserData = (JSONObject) jsonData.get("data");

                if(jsonUserData != null) {
                    String userId = jsonUserData.getString("uid");
                    String fName = jsonUserData.getString("fname");

                    Log.d("user's name : >>>>>>>>>>>>>> 1 ", fName);

                    statusMap.put("loginStatus", "1");
                    statusMap.put("userId", userId);
                    statusMap.put("username", username);
                    statusMap.put("password", password);
                    statusMap.put("fName", fName);
                }
            } else if(loginStatus.equals("false")) {
                statusMap.put("loginStatus", "2");
                statusMap.put("msg", jsonData.getString("msg"));
            }
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
    public boolean addToMyFavorite(String recipeId, Activity activity) {

        String addMyFavoriteUrl = "http://www.fauziaskitchenfun.com/api/favourites/create";
        String result = null;
        boolean status = false;

        JSONObject reqParams = new JSONObject();
        try {
            LocalDatabaseSQLiteOpenHelper localDatabaseSQLiteOpenHelper = new LocalDatabaseSQLiteOpenHelper(activity);
            Map<String, String> loggedUserDetails = localDatabaseSQLiteOpenHelper.getLoginDetails();

            reqParams.put("username", loggedUserDetails.get("username"));
            reqParams.put("password", loggedUserDetails.get("username"));
            reqParams.put("id", recipeId);
            reqParams.put("action", "add");

            result = this.sendHTTPPost(reqParams, addMyFavoriteUrl);
            JSONObject jsonData = new JSONObject(result);

            String state = jsonData.getString("status");
            if(state.equals("true"))
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
    public boolean removeFromMyFavorite(String recipeId, Activity activity) {
        String addMyFavoriteUrl = "http://www.fauziaskitchenfun.com/api/favourites/create";
        String result = null;
        boolean status = false;

        JSONObject reqParams = new JSONObject();
        try {
            LocalDatabaseSQLiteOpenHelper localDatabaseSQLiteOpenHelper = new LocalDatabaseSQLiteOpenHelper(activity);
            Map<String, String> loggedUserDetails = localDatabaseSQLiteOpenHelper.getLoginDetails();

            reqParams.put("username", loggedUserDetails.get("username"));
            reqParams.put("password", loggedUserDetails.get("username"));
            reqParams.put("id", recipeId);
            reqParams.put("action", "remove");

            result = this.sendHTTPPost(reqParams, addMyFavoriteUrl);
            JSONObject jsonData = new JSONObject(result);

            String state = jsonData.getString("status");
            if(state.equals("true"))
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
    public boolean isMyFavorite(String recipeId, Activity activity) {
        String addMyFavoriteUrl = "http://www.fauziaskitchenfun.com/api/favourites/create";
        String result = null;
        boolean status = false;

        JSONObject reqParams = new JSONObject();
        try {
            LocalDatabaseSQLiteOpenHelper localDatabaseSQLiteOpenHelper = new LocalDatabaseSQLiteOpenHelper(activity);
            Map<String, String> loggedUserDetails = localDatabaseSQLiteOpenHelper.getLoginDetails();

            reqParams.put("username", loggedUserDetails.get("username"));
            reqParams.put("password", loggedUserDetails.get("username"));
            reqParams.put("id", recipeId);
            reqParams.put("action", "check");

            result = this.sendHTTPPost(reqParams, addMyFavoriteUrl);
            JSONObject jsonData = new JSONObject(result);

            String state = jsonData.getString("status");
            if(state.equals("true"))
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
     * @param key
     * @param activity
     */
    public void getRecipesFromServer(String timeStamp, String key, Activity activity) {

        //this initialization is for testing
        RecipeDataSyncTask dataSyncTask = new RecipeDataSyncTask(activity);
        dataSyncTask.execute("http://www.fauziaskitchenfun.com/api/recipe/retrieve?timestamp=" + timeStamp + "&key=" + key);
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
    public List<PopularOrLatestRecipe> getLatestYummysFromServer() {
        String jsonResult = callWebService("http://www.fauziaskitchenfun.com/api/latest_recipe");
        List<PopularOrLatestRecipe> latestRecipeList = new ArrayList<PopularOrLatestRecipe>();
        try {
            if (jsonResult != null) {
                JSONArray jsonArray = new JSONArray(jsonResult);
                JSONObject jsonData = null;
                for(int i = 0; i < jsonArray.length(); i++) {
                    jsonData = jsonArray.getJSONObject(i);

                    PopularOrLatestRecipe popularOrLatestRecipe = new PopularOrLatestRecipe();
                    popularOrLatestRecipe.setProductId(jsonData.getString("id"));
                    popularOrLatestRecipe.setRecipeName(jsonData.getString("title"));
                    popularOrLatestRecipe.setImageUrlXS(jsonData.getString("image_xs"));
                    popularOrLatestRecipe.setImageUrlS(jsonData.getString("image_s"));
                    popularOrLatestRecipe.setImageUrlM(jsonData.getString("image_m"));
                    popularOrLatestRecipe.setImageUrlL(jsonData.getString("image_l"));
                    popularOrLatestRecipe.setImageUrlT(jsonData.getString("image_t"));
                    popularOrLatestRecipe.setImageUrlXL(jsonData.getString("image_xl"));

                    latestRecipeList.add(popularOrLatestRecipe);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            throw e;
        }

        return latestRecipeList;
    }

    public List<PopularOrLatestRecipe> getPopularYummysFromServer() {
        String jsonResult = callWebService("http://www.fauziaskitchenfun.com/api/top_recipe");
        List<String> recipeList = new ArrayList<String>();
        List<PopularOrLatestRecipe> popularRecipeList = new ArrayList<PopularOrLatestRecipe>();
        try {
            if(jsonResult != null) {
                JSONArray jsonArray = new JSONArray(jsonResult);
                JSONObject jsonData = null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    jsonData = jsonArray.getJSONObject(i);
                    recipeList.add(jsonData.getString("id"));

                    PopularOrLatestRecipe popularOrLatestRecipe = new PopularOrLatestRecipe();
                    popularOrLatestRecipe.setProductId(jsonData.getString("id"));
                    popularOrLatestRecipe.setRecipeName(jsonData.getString("title"));
                    popularOrLatestRecipe.setImageUrlXS(jsonData.getString("image_xs"));
                    popularOrLatestRecipe.setImageUrlS(jsonData.getString("image_s"));
                    popularOrLatestRecipe.setImageUrlM(jsonData.getString("image_m"));
                    popularOrLatestRecipe.setImageUrlL(jsonData.getString("image_l"));
                    popularOrLatestRecipe.setImageUrlT(jsonData.getString("image_t"));
                    popularOrLatestRecipe.setImageUrlXL(jsonData.getString("image_xl"));

                    popularRecipeList.add(popularOrLatestRecipe);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            throw e;
        }

        return popularRecipeList;
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
