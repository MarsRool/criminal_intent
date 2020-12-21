package com.mars.rool.criminalintent.network;

import android.os.AsyncTask;

import com.mars.rool.criminalintent.Crime;

import java.util.List;

public class RemoteTask extends AsyncTask<String, Void, List<Crime>> {
    Callback mCallback;
    public RemoteTask(Callback callback) {
        mCallback = callback;
    }
    @Override
    protected List<Crime> doInBackground(String... params) {
        if (params.length != 2)
            throw new RuntimeException("RemoteTask invalid params provided");
        return new RemoteHttpContext().getCrimes(params[0], params[1]);
    }

    @Override
    protected void onPostExecute(List<Crime> crimes) {
        if (mCallback != null)
            mCallback.callback(crimes);
    }

    public interface Callback {
        void callback(List<Crime> crimes);
    }
}
