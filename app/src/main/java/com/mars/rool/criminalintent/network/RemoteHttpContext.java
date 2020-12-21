package com.mars.rool.criminalintent.network;

import android.net.Uri;
import android.util.Log;

import com.mars.rool.criminalintent.Crime;
import com.mars.rool.criminalintent.CrimeListActivity;
import com.mars.rool.criminalintent.database.CrimeDbSchema;
import com.mars.rool.criminalintent.database.CrimeDbSchema.CrimeTable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class RemoteHttpContext {

    public List<Crime> getCrimes(String email, String password) {
        try {
            String url = "https://crimes-rest-api.herokuapp.com/api/crimes?email=" + email + "&password=" + password;
            Log.d(CrimeListActivity.DEBUG_TAG,"RemoteHttpContext getCrimes: " + url);
            String jsonString = getUrlString(url);
            Log.i(CrimeListActivity.DEBUG_TAG, "Received JSON: " + jsonString);
            return parseJson(jsonString);
        } catch (IOException ex) {
            Log.e(CrimeListActivity.DEBUG_TAG, "Failed to get json responce", ex);
        } catch (JSONException ex) {
            Log.e(CrimeListActivity.DEBUG_TAG, "Failed to parse JSON responce", ex);
        }
        return new ArrayList<>();
    }

    private List<Crime> parseJson(String jsonString) throws JSONException {
        List<Crime> crimes = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(jsonString);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonCrime = jsonArray.getJSONObject(i);
            Crime crime = new Crime(
                    UUID.fromString(jsonCrime.getString(CrimeTable.Columns.UUID)),
                    jsonCrime.getString(CrimeTable.Columns.TITLE),
                    new Date(jsonCrime.getLong(CrimeTable.Columns.DATE)),
                    jsonCrime.getBoolean(CrimeTable.Columns.SOLVED),
                    jsonCrime.getBoolean(CrimeTable.Columns.REQUIRE_POLICE)
            );
            crimes.add(crime);
        }
        return crimes;
    }

    private String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
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
