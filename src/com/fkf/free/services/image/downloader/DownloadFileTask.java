package com.fkf.free.services.image.downloader;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * Download file from given url
 * Created by kavi on 8/9/13.
 *
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class DownloadFileTask extends AsyncTask<List<Map<String, String>>, Integer, String> {
    @Override
    protected String doInBackground(List<Map<String, String>>... downloadFilesDetails) {

        List<Map<String, String>> downloadFile = downloadFilesDetails[0];
        String returnText = "false";

        for (Map<String, String> stringStringMap : downloadFile) {

            try {

                URL url = new URL(stringStringMap.get("url"));
                URLConnection connection = url.openConnection();
                connection.connect();

                int fileLength = connection.getContentLength();

                InputStream inputStream = new BufferedInputStream(url.openStream());
                OutputStream outputStream = new FileOutputStream(stringStringMap.get("path")+stringStringMap.get("name")/*+".jpg"*/);

                byte data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = inputStream.read(data)) != -1) {
                    total += count;
                    publishProgress((int)(total * 100 / fileLength));
                    outputStream.write(data, 0, count);
                }

                outputStream.flush();
                outputStream.close();
                inputStream.close();

                returnText = "true";
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return returnText;
    }
}
