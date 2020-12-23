package com.mars.rool.criminalintent.network;

import android.util.Log;

import com.mars.rool.criminalintent.Crime;
import com.mars.rool.criminalintent.CrimeListActivity;
import com.mars.rool.criminalintent.database.CrimeDbSchema;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class JsonHelper {

    public static boolean jsonGetResult(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            return jsonObject.getBoolean("result");
        } catch (JSONException ex) {
            Log.e(CrimeListActivity.DEBUG_TAG, "Failed to parse JSON", ex);
        }
        return false;
    }

    public static List<Crime> jsonToCrimeList(String jsonString) {
        try {
            List<Crime> crimes = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonCrime = jsonArray.getJSONObject(i);
                Crime crime = new Crime(
                        UUID.fromString(jsonCrime.getString(CrimeDbSchema.CrimeTable.Columns.UUID)),
                        jsonCrime.getString(CrimeDbSchema.CrimeTable.Columns.TITLE),
                        new Date(jsonCrime.getLong(CrimeDbSchema.CrimeTable.Columns.DATE)),
                        jsonCrime.getBoolean(CrimeDbSchema.CrimeTable.Columns.SOLVED),
                        jsonCrime.getBoolean(CrimeDbSchema.CrimeTable.Columns.REQUIRE_POLICE)
                );
                crimes.add(crime);
            }
            return crimes;
        } catch (JSONException ex) {
            Log.e(CrimeListActivity.DEBUG_TAG, "Failed to parse JSON", ex);
        }
        return new ArrayList<>();
    }

    public static String crimeListToJson(List<Crime> crimes) {
        try {
            JSONArray jsonArray = new JSONArray();
            for (Crime crime : crimes) {
                JSONObject crimeJsonObj = new JSONObject();
                crimeJsonObj.put(CrimeDbSchema.CrimeTable.Columns.UUID, crime.getId().toString());
                crimeJsonObj.put(CrimeDbSchema.CrimeTable.Columns.TITLE, crime.getTitle());
                crimeJsonObj.put(CrimeDbSchema.CrimeTable.Columns.DATE, crime.getDate().getTime());
                crimeJsonObj.put(CrimeDbSchema.CrimeTable.Columns.SOLVED, crime.isSolved());
                crimeJsonObj.put(CrimeDbSchema.CrimeTable.Columns.REQUIRE_POLICE, crime.isRequiredPolice());
                jsonArray.put(crimeJsonObj);
            }
            return jsonArray.toString();
        } catch (JSONException ex) {
            Log.e(CrimeListActivity.DEBUG_TAG, "Failed to create JSON", ex);
        }
        return "[]";
    }
}
