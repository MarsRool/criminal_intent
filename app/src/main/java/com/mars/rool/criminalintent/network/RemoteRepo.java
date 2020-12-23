package com.mars.rool.criminalintent.network;

import android.util.Log;

import com.mars.rool.criminalintent.Crime;
import com.mars.rool.criminalintent.CrimeListActivity;

import java.util.List;

public class RemoteRepo {
    private static final String REMOVE_ADDRESS = "https://crimes-rest-api.herokuapp.com/api";
    private final RemoteHttpContext mContext;
    public RemoteRepo()
    {
        mContext = new RemoteHttpContext();
    }

    public void requestCrimes(String email, String password, RemoteTask.Callback<List<Crime>> callback) {
        try {
            RemoteTask<List<Crime>> task = new RemoteTask<List<Crime>>(callback) {
                @Override
                protected List<Crime> doInBackground(String... params) {
                    return getCrimes(params[0], params[1]);
                }
            };
            task.execute(email, password);
        } catch (Exception ex) {
            Log.e(CrimeListActivity.DEBUG_TAG, "Failed to request crimes", ex);
        }
    }

    public void requestRegister(String email, String password, RemoteTask.Callback<Boolean> callback) {
        try {
            RemoteTask<Boolean> task = new RemoteTask<Boolean>(callback) {
                @Override
                protected Boolean doInBackground(String... params) {
                    return registerUser(params[0], params[1]);
                }
            };
            task.execute(email, password);
        } catch (Exception ex) {
            Log.e(CrimeListActivity.DEBUG_TAG, "Failed to request crimes", ex);
        }
    }

    private List<Crime> getCrimes(String email, String password) {
        String url = REMOVE_ADDRESS + "/crimes?email=" + email + "&password=" + password;
        Log.d(CrimeListActivity.DEBUG_TAG, "getCrimes url: " + url);
        String jsonString = mContext.getRemoteJsonResponce(url);
        Log.d(CrimeListActivity.DEBUG_TAG, "getCrimes JSON: " + jsonString);
        return mContext.jsonToCrimeList(jsonString);
    }

    private Boolean registerUser(String email, String password) {
        String url = REMOVE_ADDRESS + "/register?email=" + email + "&password=" + password;
        Log.d(CrimeListActivity.DEBUG_TAG, "registerUser url: " + url);
        String jsonString = mContext.getRemoteJsonResponce(url);
        Log.d(CrimeListActivity.DEBUG_TAG, "registerUser JSON: " + jsonString);
        return mContext.jsonGetResult(jsonString);
    }
}
