package com.mars.rool.criminalintent.network;

import android.util.Log;

import com.mars.rool.criminalintent.CrimeListActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RemoteHttpContext {

    public String getRemoteJsonResponce(String urlSpec) {
        try {
            return new String(getUrlBytes(urlSpec));
        } catch (IOException ex) {
            Log.e(CrimeListActivity.DEBUG_TAG, "Failed to get JSON responce", ex);
        }
        return "";
    }

    private byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage()
                        + ": with " + urlSpec);
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }
}
