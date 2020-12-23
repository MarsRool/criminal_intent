package com.mars.rool.criminalintent;

import androidx.fragment.app.Fragment;

public class CrimeListActivity extends SingleFragmentActivity {

    public static final String DEBUG_TAG = "DEBUG_TAG";

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
