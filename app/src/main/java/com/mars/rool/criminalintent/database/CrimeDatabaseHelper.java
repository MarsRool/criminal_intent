package com.mars.rool.criminalintent.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mars.rool.criminalintent.database.CrimeDbSchema.CrimeTable;
import com.mars.rool.criminalintent.database.CrimeDbSchema.UserTable;

public class CrimeDatabaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "crimeDatabase.db";

    public CrimeDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + CrimeTable.NAME + "("
            + "id INTEGER PRIMARY KEY NOT NULL, "
            + CrimeTable.Columns.UUID + " TEXT NOT NULL, "
            + CrimeTable.Columns.TITLE + " TEXT, "
            + CrimeTable.Columns.DATE + " INTEGER NOT NULL, "
            + CrimeTable.Columns.SOLVED + " BOOLEAN NOT NULL, "
            + CrimeTable.Columns.REQUIRE_POLICE + " BOOLEAN NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + UserTable.NAME + "("
            + UserTable.Columns.EMAIL + " TEXT NOT NULL, "
            + UserTable.Columns.PASSWORD + " TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
