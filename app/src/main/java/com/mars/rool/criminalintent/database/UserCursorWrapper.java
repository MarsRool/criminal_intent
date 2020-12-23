package com.mars.rool.criminalintent.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.mars.rool.criminalintent.database.CrimeDbSchema.UserTable;
import com.mars.rool.criminalintent.model.User;

public class UserCursorWrapper extends CursorWrapper {
    public UserCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public User getUser() {
        String email = getString(getColumnIndex(UserTable.Columns.EMAIL));
        String password = getString(getColumnIndex(UserTable.Columns.PASSWORD));

        return new User(
                email,
                password);
    }
}
