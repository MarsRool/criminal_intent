package com.mars.rool.criminalintent.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mars.rool.criminalintent.Crime;
import com.mars.rool.criminalintent.CrimeListActivity;
import com.mars.rool.criminalintent.database.CrimeDbSchema.CrimeTable;

public class CrimeDbContext {
    private final Context mContext;
    private final SQLiteDatabase mDatabase;

    public CrimeDbContext(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new CrimeDatabaseHelper(mContext).getWritableDatabase();
    }

    public void addCrime(Crime c) {
        ContentValues values = getContentValues(c);
        long result = mDatabase.insert(CrimeTable.NAME, null, values);
        Log.d(CrimeListActivity.DEBUG_TAG, "addCrime: result == " + result);
    }

    public boolean deleteCrime(Crime c) {
        long result = mDatabase.delete(CrimeTable.NAME,
                CrimeTable.Columns.UUID
                + " = ?" + c.getId().toString() + "\'",
                new String[]{ c.getId().toString() });
        Log.d(CrimeListActivity.DEBUG_TAG, "deleteCrime: result == " + result);
        return result > 0;
    }

    public void updateCrime(Crime c) {
        String uuidString = c.getId().toString();
        ContentValues values = getContentValues(c);
        mDatabase.update(CrimeTable.NAME, values,
                CrimeTable.Columns.UUID + " = ?",
                new String[] { uuidString });
    }

    private static ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Columns.UUID, crime.getId().toString());
        values.put(CrimeTable.Columns.TITLE, crime.getTitle());
        values.put(CrimeTable.Columns.DATE, crime.getDate().getTime());
        values.put(CrimeTable.Columns.SOLVED, crime.isSolved() ? 1 : 0);
        values.put(CrimeTable.Columns.REQUIRE_POLICE, crime.isRequiredPolice() ? 1 : 0);
        return values;
    }
}
