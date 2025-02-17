package com.mars.rool.criminalintent.network;

import android.util.Log;

import com.mars.rool.criminalintent.model.Crime;
import com.mars.rool.criminalintent.CrimeListActivity;

import java.util.List;

public class RemoteRepo {
    private static final String REMOTE_ADDRESS = "https://crimes-rest-api.herokuapp.com/api";
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

    public void requestLogin(String email, String password, RemoteTask.Callback<Boolean> callback) {
        try {
            RemoteTask<Boolean> task = new RemoteTask<Boolean>(callback) {
                @Override
                protected Boolean doInBackground(String... params) {
                    return loginUser(params[0], params[1]);
                }
            };
            task.execute(email, password);
        } catch (Exception ex) {
            Log.e(CrimeListActivity.DEBUG_TAG, "Failed to request login", ex);
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
            Log.e(CrimeListActivity.DEBUG_TAG, "Failed to request register", ex);
        }
    }

    public void requestSetCrimes(String email, String password, List<Crime> crimes, RemoteTask.Callback<Boolean> callback) {
        try {
            String crimesJsonString = JsonHelper.crimeListToJson(crimes);
            Log.d(CrimeListActivity.DEBUG_TAG, "setCrimes crimesJsonString: " + crimesJsonString);
            RemoteTask<Boolean> task = new RemoteTask<Boolean>(callback) {
                @Override
                protected Boolean doInBackground(String... params) {
                    return setCrimes(params[0], params[1], params[2]);
                }
            };
            task.execute(email, password, crimesJsonString);
        } catch (Exception ex) {
            Log.e(CrimeListActivity.DEBUG_TAG, "Failed to request set crimes", ex);
        }
    }

    private List<Crime> getCrimes(String email, String password) {
        String url = REMOTE_ADDRESS + "/crimes?email=" + email + "&password=" + password;
        Log.d(CrimeListActivity.DEBUG_TAG, "getCrimes url: " + url);
        String jsonString = mContext.getRemoteJsonResponce(url);
        Log.d(CrimeListActivity.DEBUG_TAG, "getCrimes JSON: " + jsonString);
        return JsonHelper.jsonToCrimeList(jsonString);
    }

    private Boolean loginUser(String email, String password) {
        String url = REMOTE_ADDRESS + "/login?email=" + email + "&password=" + password;
        Log.d(CrimeListActivity.DEBUG_TAG, "loginUser url: " + url);
        String jsonString = mContext.getRemoteJsonResponce(url);
        Log.d(CrimeListActivity.DEBUG_TAG, "loginUser JSON: " + jsonString);
        return JsonHelper.jsonGetResult(jsonString);
    }

    private Boolean registerUser(String email, String password) {
        String url = REMOTE_ADDRESS + "/register?email=" + email + "&password=" + password;
        Log.d(CrimeListActivity.DEBUG_TAG, "registerUser url: " + url);
        String jsonString = mContext.getRemoteJsonResponce(url);
        Log.d(CrimeListActivity.DEBUG_TAG, "registerUser JSON: " + jsonString);
        return JsonHelper.jsonGetResult(jsonString);
    }

    private Boolean setCrimes(String email, String password, String crimesJsonString) {
        String url = REMOTE_ADDRESS + "/set_crimes?email=" + email + "&password=" + password + "&crimes=" + crimesJsonString;
        Log.d(CrimeListActivity.DEBUG_TAG, "setCrimes url: " + url);
        String jsonString = mContext.getRemoteJsonResponce(url);
        Log.d(CrimeListActivity.DEBUG_TAG, "setCrimes JSON: " + jsonString);
        return JsonHelper.jsonGetResult(jsonString);
    }
}
