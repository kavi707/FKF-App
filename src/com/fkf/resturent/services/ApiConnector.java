package com.fkf.resturent.services;

import android.util.Log;
import com.fkf.resturent.database.Recipe;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * setup and communicate with the server api
 * Created by kavi on 7/9/13.
 *
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class ApiConnector {

    public ArrayList<Recipe> getRecipesFromServer() {
        //initialize json object according to request
        JSONObject reqJSONObject = new JSONObject();

        sendHTTPPost(reqJSONObject);
        return null;
    }

    private void sendHTTPPost(JSONObject req){

        HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000);
        HttpResponse response;

        JSONObject jsonObject = new JSONObject();
        List<String> addresses = new ArrayList<String>();

        try {
            HttpPost post = new HttpPost("http://10.0.2.2:7000/sms/send");
//            HttpPost post = new HttpPost("http://10.0.2.2/courses/quiz-api/add");
            //initialized json object
            addresses.add("tel:94776351232");
            jsonObject.put("applicationId","APP_000001");
            jsonObject.put("password", "pass");
            jsonObject.put("message","Hi android");
            jsonObject.put("destinationAddresses",addresses);
            jsonObject.put("deliveryStatusRequest",0);

            /*jsonObject.put("quizName", "test quiz");
            jsonObject.put("quizDescription", "test quiz description");
            jsonObject.put("quizTime", "60");*/

            StringEntity se = new StringEntity(jsonObject.toString());
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
                    Log.d("Tag","Success Response : " + result);
                } else {
                    Log.d("error status code", String.valueOf(statusCode));
                }
            } else {
                Log.d("Error", "null response after sending http req");
            }

        } catch (Exception ex) {
            Log.d("Exception", ex.toString());
        }
    }
}
