package com.mars.rool.criminalintent;

import android.content.Context;
import android.util.Log;

import com.mars.rool.criminalintent.database.CrimeDbContext;
import com.mars.rool.criminalintent.model.Crime;
import com.mars.rool.criminalintent.model.User;
import com.mars.rool.criminalintent.network.RemoteRepo;
import com.mars.rool.criminalintent.network.RemoteTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class CrimeLab {
    private static final String PASSWORD_ACCESSOR = "js2o3$4d-i46=fh@hks№ad[an@ao_d45u:ig#qw25kdhj32vas";

    private static CrimeLab sCrimeLab;

    private final Map<UUID, Crime> mCrimeMap;
    private final List<Crime> mCrimeList;
    private final CrimeDbContext mCrimeDbContext;
    private final RemoteRepo mRemoteRepo;
    private RemoteTask.Callback<Void> mUpdateUICallback;

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null)
            sCrimeLab = new CrimeLab(context);
        return sCrimeLab;
    }

    private CrimeLab(Context context) {
        mCrimeDbContext = new CrimeDbContext(context);
        mRemoteRepo = new RemoteRepo();
        mCrimeMap = new TreeMap<>();
        mCrimeList = new ArrayList<>();
        initialize();
    }

    public void setUpdateUICallback(RemoteTask.Callback<Void> updateUICallback) {
        mUpdateUICallback = updateUICallback;
    }

    public int getSize() { return mCrimeList.size(); }

    public void addCrime(Crime c) {
        if (c != null) {
            mCrimeMap.put(c.getId(), c);
            mCrimeList.add(c);
            mCrimeDbContext.addCrime(c);
        }
    }

    public void deleteCrime(Crime c) {
        if (c != null && mCrimeMap.containsKey(c.getId())) {
            mCrimeMap.remove(c.getId());
            mCrimeList.remove(c);
            mCrimeDbContext.deleteCrime(c);
        }
    }

    public void updateCrime(Crime c) {
        if (c != null) {
            mCrimeDbContext.updateCrime(c);
        }
    }

    public void tryLogin(String email, String password, RemoteTask.Callback<Boolean> callback) {
        mRemoteRepo.requestLogin(email, password, callback);
    }
    public void tryLogin(RemoteTask.Callback<Boolean> callback) {
        User user = mCrimeDbContext.getAuthorizedUser();
        if (user == null)
            callback.callback(false);
        else
            tryLogin(user.getEmail(), user.getPassword(PASSWORD_ACCESSOR), callback);
    }
    public void saveAuthorizedUser(String email, String password) {
        mCrimeDbContext.saveAuthorizedUser(email, password);
    }
    public void clearAuthorizedUser() {
        User user = mCrimeDbContext.getAuthorizedUser();
        if (user != null)
        {
            mCrimeDbContext.deleteAuthorizedUser();
        }
    }

    public void tryRegister(String email, String password, RemoteTask.Callback<Boolean> callback) {
        mRemoteRepo.requestRegister(email, password, callback);
    }

    public Crime getCrime(UUID id) {
        return mCrimeMap.get(id);
    }
    public Crime getCrime(int position) {
        return mCrimeList.get(position);
    }
    public int getCrimePosition(UUID id) {
        for (int i = 0; i < getSize(); i++) {
            if (mCrimeList.get(i).getId().equals(id))
                return i;
        }
        return -1;
    }

    public void downloadFromRemote() {
        User user = mCrimeDbContext.getAuthorizedUser();
        mRemoteRepo.requestCrimes(user.getEmail(), user.getPassword(PASSWORD_ACCESSOR), new GetCrimesCallback());
    }

    public void uploadToRemote() {
        User user = mCrimeDbContext.getAuthorizedUser();
        mRemoteRepo.requestSetCrimes(user.getEmail(), user.getPassword(PASSWORD_ACCESSOR), mCrimeList, new SetCrimesCallback());
    }

    private void initialize() {
        List<Crime> crimes = mCrimeDbContext.getCrimes();
        for (Crime crime : crimes) {
            mCrimeMap.put(crime.getId(), crime);
            mCrimeList.add(crime);
        }
    }

    private class GetCrimesCallback implements RemoteTask.Callback<List<Crime>> {
        @Override
        public void callback(List<Crime> crimes) {
            Log.d(CrimeListActivity.DEBUG_TAG, "CrimeLab GetCrimesCallback: crimesRemote.size == " + crimes.size());
            mCrimeDbContext.deleteCrimes();
            for (Crime crime : crimes) {
                Log.d(CrimeListActivity.DEBUG_TAG,
                        "CrimeLab initialize:" + crime.getId()
                + " " + crime.getTitle()
                + " " + crime.getDate()
                + (crime.isSolved() ? " solved" : " not solved")
                + (crime.isRequiredPolice() ? " required" : " not required"));
                mCrimeDbContext.addCrime(crime);
            }
            if (mUpdateUICallback != null)
                mUpdateUICallback.callback(null);
        }
    }

    private class SetCrimesCallback implements RemoteTask.Callback<Boolean> {
        @Override
        public void callback(Boolean result) {
            Log.d(CrimeListActivity.DEBUG_TAG, "CrimeLab SetCrimesCallback: result == " + (result ? "true" : "false"));
        }
    }
}
