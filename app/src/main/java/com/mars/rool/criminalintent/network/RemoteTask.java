package com.mars.rool.criminalintent.network;

import android.os.AsyncTask;

public abstract class RemoteTask<Result> extends AsyncTask<String, Void, Result> {
    Callback<Result> mCallback;
    public RemoteTask(Callback<Result> callback) {
        mCallback = callback;
    }

    @Override
    protected void onPostExecute(Result result) {
        if (mCallback != null)
            mCallback.callback(result);
    }

    public interface Callback<Res> {
        void callback(Res result);
    }
}
