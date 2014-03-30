package com.fkf.commercial.services.connections;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Background task for check the db is updated or not
 *
 * Created by kavi on 3/30/14.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class CheckDbUpdateTask extends AsyncTask<String, Void, Boolean> {

    private ApiConnector connector = new ApiConnector();

    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p/>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param strings The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected Boolean doInBackground(String... strings) {

        String jsonResult = connector.callWebService(strings[0]);
        boolean isUpdateAvailable = false;

        try {
            if (jsonResult != null) {
                JSONObject jsonData = new JSONObject(jsonResult);
                isUpdateAvailable = jsonData.getBoolean("updates_available");
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return isUpdateAvailable;
    }
}
