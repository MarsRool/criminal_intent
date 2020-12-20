package com.mars.rool.criminalintent.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.mars.rool.criminalintent.Crime;
import com.mars.rool.criminalintent.database.CrimeDbSchema.CrimeTable;

import java.util.Date;
import java.util.UUID;

public class CrimeCursorWrapper extends CursorWrapper {
    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime() {
        String uuidString = getString(getColumnIndex(CrimeTable.Columns.UUID));
        String title = getString(getColumnIndex(CrimeTable.Columns.TITLE));
        long date = getLong(getColumnIndex(CrimeTable.Columns.DATE));
        int isSolved = getInt(getColumnIndex(CrimeTable.Columns.SOLVED));
        int requiredPolice = getInt(getColumnIndex(CrimeTable.Columns.REQUIRE_POLICE));

        return new Crime(
                UUID.fromString(uuidString),
                title,
                new Date(date),
                isSolved != 0,
                requiredPolice != 0);
    }
}
