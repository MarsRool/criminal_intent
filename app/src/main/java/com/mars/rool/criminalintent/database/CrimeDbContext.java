package com.mars.rool.criminalintent.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mars.rool.criminalintent.database.CrimeDbSchema.UserTable;
import com.mars.rool.criminalintent.model.Crime;
import com.mars.rool.criminalintent.CrimeListActivity;
import com.mars.rool.criminalintent.database.CrimeDbSchema.CrimeTable;
import com.mars.rool.criminalintent.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeDbContext {
    private final Context mContext;
    private final SQLiteDatabase mDatabase;

    public CrimeDbContext(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new CrimeDatabaseHelper(mContext).getWritableDatabase();
    }

    public void addCrime(Crime c) {
        ContentValues values = getCrimeContentValues(c);
        long result = mDatabase.insert(CrimeTable.NAME, null, values);
        Log.d(CrimeListActivity.DEBUG_TAG, "addCrime: result == " + result);
    }

    public boolean deleteCrime(Crime c) {
        long result = mDatabase.delete(CrimeTable.NAME,
                CrimeTable.Columns.UUID + " = ?",
                new String[]{ c.getId().toString() });
        Log.d(CrimeListActivity.DEBUG_TAG, "deleteCrime: result == " + result);
        return result > 0;
    }

    public void updateCrime(Crime c) {
        String uuidString = c.getId().toString();
        ContentValues values = getCrimeContentValues(c);
        long result = mDatabase.update(CrimeTable.NAME, values,
                CrimeTable.Columns.UUID + " = ?",
                new String[] { uuidString });
        Log.d(CrimeListActivity.DEBUG_TAG, "updateCrime: result == " + result);
    }

    public List<Crime> getCrimes() {
        List<Crime> crimes = new ArrayList<>();
        CrimeCursorWrapper cursorWrapper = queryCrimes(null, null);

        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()) {
                crimes.add(cursorWrapper.getCrime());
                cursorWrapper.moveToNext();
            }
        } finally {
            cursorWrapper.close();
        }
        return crimes;
    }

    public Crime getCrime(UUID id) {
        CrimeCursorWrapper cursorWrapper = queryCrimes(
                CrimeTable.Columns.UUID + " = ?",
                new String[] { id.toString() }
        );

        try {
            if (cursorWrapper.getCount() == 0) {
                return null;
            }

            cursorWrapper.moveToFirst();
            return cursorWrapper.getCrime();
        } finally {
            cursorWrapper.close();
        }
    }

    public User getAuthorizedUser() {
        UserCursorWrapper cursorWrapper = queryUser();
        try {
            if (cursorWrapper.getCount() == 0) {
                return null;
            }
            cursorWrapper.moveToFirst();
            return cursorWrapper.getUser();
        } finally {
            cursorWrapper.close();
        }
    }
    public void saveAuthorizedUser(String email, String password) {
        ContentValues values = getUserContentValues(email, password);
        long result = mDatabase.insert(UserTable.NAME, null, values);
        Log.d(CrimeListActivity.DEBUG_TAG, "saveAuthorizedUser: result == " + result);
    }
    public boolean deleteAuthorizedUser() {
        long result = mDatabase.delete(UserTable.NAME, null, null);
        Log.d(CrimeListActivity.DEBUG_TAG, "deleteAuthorizedUser: result == " + result);
        return result > 0;
    }

    private static ContentValues getCrimeContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(CrimeTable.Columns.UUID, crime.getId().toString());
        values.put(CrimeTable.Columns.TITLE, crime.getTitle());
        values.put(CrimeTable.Columns.DATE, crime.getDate().getTime());
        values.put(CrimeTable.Columns.SOLVED, crime.isSolved() ? 1 : 0);
        values.put(CrimeTable.Columns.REQUIRE_POLICE, crime.isRequiredPolice() ? 1 : 0);
        return values;
    }

    private static ContentValues getUserContentValues(String email, String password) {
        ContentValues values = new ContentValues();
        values.put(UserTable.Columns.EMAIL, email);
        values.put(UserTable.Columns.PASSWORD, password);
        return values;
    }

    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CrimeTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null,
                null
        );
        return new CrimeCursorWrapper(cursor);
    }

    private UserCursorWrapper queryUser() {
        return new UserCursorWrapper(mDatabase.query(UserTable.NAME, null, null, null, null, null, null));
    }
}
