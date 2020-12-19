package com.mars.rool.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.fragment.app.Fragment;

import java.util.Date;

public class DateActivity extends SingleFragmentActivity {

    private static final String EXTRA_DATE_ID =
            "com.mars.rool.criminalintent.date_id";

    @Override
    protected Fragment createFragment() {
        Date date = (Date) getIntent()
                .getSerializableExtra(EXTRA_DATE_ID);
        Log.d(CrimeListActivity.DEBUG_TAG, "DateActivity create DatePickerFragment, date="+date);
        return DatePickerFragment.newInstance(date);
    }

    public static Intent newIntent(Context packageContext, Date date) {
        Intent intent = new Intent(packageContext, DateActivity.class);
        intent.putExtra(EXTRA_DATE_ID, date);
        Log.d(CrimeListActivity.DEBUG_TAG, "DateActivity newIntent, date="+date);
        return intent;
    }
}
