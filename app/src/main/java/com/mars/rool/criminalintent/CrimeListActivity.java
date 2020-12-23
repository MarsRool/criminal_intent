package com.mars.rool.criminalintent;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

public class CrimeListActivity extends SingleFragmentActivity {

    public static final String DEBUG_TAG = "DEBUG_TAG";

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, CrimeListActivity.class);
    }
}
