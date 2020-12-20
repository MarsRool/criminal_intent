package com.mars.rool.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class CrimeLab {

    private static CrimeLab sCrimeLab;

    private Map<UUID, Crime> mCrimeMap;
    private List<Crime> mCrimeList;

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null)
            sCrimeLab = new CrimeLab(context);
        return sCrimeLab;
    }

    private CrimeLab(Context context) {
        mCrimeMap = new TreeMap<>();
        mCrimeList = new ArrayList<>();
    }

    public int getSize() { return mCrimeList.size(); }

    public void addCrime(Crime c) {
        mCrimeMap.put(c.getId(), c);
        mCrimeList.add(c);
    }

    public void deleteCrime(Crime c) {
        if (c != null && mCrimeMap.containsKey(c.getId())) {
            mCrimeMap.remove(c.getId());
            mCrimeList.remove(c);
        }
    }

    public Crime getCrime(UUID id) {
        return mCrimeMap.get(id);
    }
    public Crime getCrime(int position) {return mCrimeList.get(position); }
    public int getCrimePosition(UUID id) {
        for (int i = 0; i < getSize(); i++) {
            if (mCrimeList.get(i).getId().equals(id))
                return i;
        }
        return -1;
    }
}
