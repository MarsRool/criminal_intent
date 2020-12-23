package com.mars.rool.criminalintent;

import android.content.Context;
import android.util.Log;

import com.mars.rool.criminalintent.database.CrimeDbContext;
import com.mars.rool.criminalintent.model.Crime;
import com.mars.rool.criminalintent.network.RemoteRepo;
import com.mars.rool.criminalintent.network.RemoteTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class CrimeLab {

    private static CrimeLab sCrimeLab;

    private final Map<UUID, Crime> mCrimeMap;
    private final List<Crime> mCrimeList;
    private final CrimeDbContext mCrimeDbContext;
    private final RemoteRepo mRemoteRepo;

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null)
            sCrimeLab = new CrimeLab(context);
        return sCrimeLab;
    }

    private CrimeLab(Context context) {
        mCrimeDbContext = new CrimeDbContext(context);
        mCrimeMap = new TreeMap<>();
        mCrimeList = new ArrayList<>();
        mRemoteRepo = new RemoteRepo();
        initialize();
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

    private void initialize() {
        List<Crime> crimes = mCrimeDbContext.getCrimes();
        for (Crime crime : crimes) {
            mCrimeMap.put(crime.getId(), crime);
            mCrimeList.add(crime);
        }

        mRemoteRepo.requestCrimes("1","1", new GetCrimesCallback());
        mRemoteRepo.requestRegister("1","1", new RegisterCallback());
    }

    private void setRemoteCrimes(List<Crime> crimes) {
        mRemoteRepo.requestSetCrimes("1","1", crimes, new SetCrimesCallback());
    }

    private class GetCrimesCallback implements RemoteTask.Callback<List<Crime>> {
        @Override
        public void callback(List<Crime> crimes) {
            Log.d(CrimeListActivity.DEBUG_TAG, "CrimeLab GetCrimesCallback: crimesRemote.size == " + crimes.size());
            for (Crime crime : crimes) {
                Log.d(CrimeListActivity.DEBUG_TAG,
                        "CrimeLab initialize:" + crime.getId()
                + " " + crime.getTitle()
                + " " + crime.getDate()
                + (crime.isSolved() ? " solved" : " not solved")
                + (crime.isRequiredPolice() ? " required" : " not required"));
            }
            setRemoteCrimes(crimes);
        }
    }

    private class RegisterCallback implements RemoteTask.Callback<Boolean> {
        @Override
        public void callback(Boolean result) {
            Log.d(CrimeListActivity.DEBUG_TAG, "CrimeLab RegisterCallback: result == " + (result ? "true" : "false"));
        }
    }

    private class SetCrimesCallback implements RemoteTask.Callback<Boolean> {
        @Override
        public void callback(Boolean result) {
            Log.d(CrimeListActivity.DEBUG_TAG, "CrimeLab SetCrimesCallback: result == " + (result ? "true" : "false"));
        }
    }
}
