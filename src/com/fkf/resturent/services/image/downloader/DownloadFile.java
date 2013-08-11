package com.fkf.resturent.services.image.downloader;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Map;

/**
 * Download file from given url
 * Created by kavi on 8/9/13.
 *
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */
public class DownloadFile extends AsyncTask<Map<String, String>, Integer, String> {
    @Override
    protected String doInBackground(Map<String, String>... downloadFilesDetails) {

        Map<String, String> downloadFile = downloadFilesDetails[0];

        try {
            URL url = new URL(downloadFile.get("url"));
            URLConnection connection = url.openConnection();
            connection.connect();

            int fileLength = connection.getContentLength();

            InputStream inputStream = new BufferedInputStream(url.openStream());
            OutputStream outputStream = new FileOutputStream(downloadFile.get("path")+downloadFile.get("name")+".jpg");

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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
