package com.mars.rool.criminalintent;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

public class RegisterActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new RegisterFragment();
    }
    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, RegisterActivity.class);
    }
}
