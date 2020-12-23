package com.mars.rool.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.mars.rool.criminalintent.network.RemoteTask;

public class RegisterFragment extends Fragment {

    private TextView mPasswordsNotEqualTextView;
    private TextView mUserExistsTextView;
    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mPasswordConfirmField;
    private Button mSubmitButton;

    private CrimeLab mCrimeLab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCrimeLab = CrimeLab.get(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);

        mEmailField = v.findViewById(R.id.register_email);
        mPasswordField = v.findViewById(R.id.register_password);
        mPasswordConfirmField = v.findViewById(R.id.register_password_confirm);
        mPasswordsNotEqualTextView = v.findViewById(R.id.passwords_not_equal_label);
        mPasswordsNotEqualTextView.setVisibility(View.GONE);
        mUserExistsTextView = v.findViewById(R.id.user_already_exists_label);
        mUserExistsTextView.setVisibility(View.GONE);
        mSubmitButton = v.findViewById(R.id.register_submit);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPasswordField.getText().toString().compareTo(mPasswordConfirmField.getText().toString()) != 0)
                {
                    mPasswordsNotEqualTextView.setVisibility(View.VISIBLE);
                    return;
                } else {
                    mPasswordsNotEqualTextView.setVisibility(View.GONE);
                }
                mCrimeLab.tryRegister(
                        mEmailField.getText().toString(),
                        mPasswordField.getText().toString(),
                        new RegisterCallback());
            }
        });

        return v;
    }

    private void register() {
        getActivity().finish();
    }

    private class RegisterCallback implements RemoteTask.Callback<Boolean> {
        @Override
        public void callback(Boolean result) {
            Log.d(CrimeListActivity.DEBUG_TAG, "CrimeLab RegisterCallback: result == " + (result ? "true" : "false"));
            if (result)
            {
                mUserExistsTextView.setVisibility(View.GONE);
                register();
            }
            else
            {
                if (mUserExistsTextView != null) {
                    mUserExistsTextView.setVisibility(View.VISIBLE);
                }
                mCrimeLab.clearAuthorizedUser();
            }
        }
    }
}
