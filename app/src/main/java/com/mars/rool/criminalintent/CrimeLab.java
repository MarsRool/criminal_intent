package com.mars.rool.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.mars.rool.criminalintent.database.CrimeDatabaseHelper;
import com.mars.rool.criminalintent.database.CrimeDbContext;
import com.mars.rool.criminalintent.database.CrimeDbSchema;
import com.mars.rool.criminalintent.database.CrimeDbSchema.CrimeTable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class CrimeLab {

    private static CrimeLab sCrimeLab;

    private final Map<UUID, Crime> mCrimeMap;
    private final List<Crime> mCrimeList;
    private final CrimeDbContext mCrimeDbContext;

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null)
            sCrimeLab = new CrimeLab(context);
        return sCrimeLab;
    }

    private CrimeLab(Context context) {
        mCrimeDbContext = new CrimeDbContext(context);
        mCrimeMap = new TreeMap<>();
        mCrimeList = new ArrayList<>();
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
    }
}
