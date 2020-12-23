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

public class LoginFragment extends Fragment {

    private TextView mWrongDataTextView;
    private EditText mEmailField;
    private EditText mPasswordField;
    private Button mSubmitButton;
    private Button mRegisterButton;

    private CrimeLab mCrimeLab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCrimeLab = CrimeLab.get(getActivity());
        mCrimeLab.tryLogin(new LoginCallback());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCrimeLab.uploadToRemote();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        mEmailField = v.findViewById(R.id.login_email);
        mPasswordField = v.findViewById(R.id.login_password);
        mSubmitButton = v.findViewById(R.id.login_submit);
        mWrongDataTextView = v.findViewById(R.id.wrong_data_label);
        mWrongDataTextView.setVisibility(View.GONE);
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCrimeLab.tryLogin(
                        mEmailField.getText().toString(),
                        mPasswordField.getText().toString(),
                        new LoginCallback());
            }
        });
        mRegisterButton = v.findViewById(R.id.register_redirect);
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        return v;
    }

    private void login() {
        mCrimeLab.downloadFromRemote();
        Intent intent = CrimeListActivity.newIntent(getActivity());
        startActivity(intent);
    }

    private void register() {
        Intent intent = RegisterActivity.newIntent(getActivity());
        startActivity(intent);
    }

    private class LoginCallback implements RemoteTask.Callback<Boolean> {
        @Override
        public void callback(Boolean result) {
            Log.d(CrimeListActivity.DEBUG_TAG, "CrimeLab LoginCallback: result == " + (result ? "true" : "false"));
            if (result)
            {
                mWrongDataTextView.setVisibility(View.GONE);
                mCrimeLab.saveAuthorizedUser(
                        mEmailField.getText().toString(),
                        mPasswordField.getText().toString()
                );
                login();
            }
            else
            {
                if (mWrongDataTextView != null) {
                    mWrongDataTextView.setVisibility(View.VISIBLE);
                }
                mCrimeLab.clearAuthorizedUser();
            }
        }
    }
}
