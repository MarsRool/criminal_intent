package com.mars.rool.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.mars.rool.criminalintent.model.Crime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static android.widget.CompoundButton.OnCheckedChangeListener;

public class CrimeFragment extends Fragment {

    private static final String ARGS_CRIME_ID = "crime_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_TIME = "DialogTime";

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;

    private EditText mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private CheckBox mSolvedCheckBox;
    private CheckBox mRequiredPoliceCheckBox;

    private Crime mCrime;

    static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARGS_CRIME_ID, crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        Log.d(CrimeListActivity.DEBUG_TAG, "CrimeFragment onCreate, args="+(args == null?"null":"not null"));
        if (args != null) {
            UUID crimeId = (UUID) args.getSerializable(ARGS_CRIME_ID);
            assert crimeId != null;
            Log.d(CrimeListActivity.DEBUG_TAG, "CrimeFragment onCreate, id="+crimeId);
            mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
            Log.d(CrimeListActivity.DEBUG_TAG, "CrimeFragment onCreate, mCrime="+(mCrime == null?"null":"not null"));
        } else {
            mCrime = new Crime();
            Log.d(CrimeListActivity.DEBUG_TAG, "CrimeFragment onCreate, id=?, create new Crime()");
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        mTitleField = v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCrime.setTitle(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mDateButton = v.findViewById(R.id.crime_date);
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(fragmentManager, DIALOG_DATE);
            }
        });

        mTimeButton = v.findViewById(R.id.crime_time);
        updateTime();
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                TimePickerFragment dialog = TimePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                dialog.show(fragmentManager, DIALOG_TIME);
            }
        });

        mSolvedCheckBox = v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });

        mRequiredPoliceCheckBox = v.findViewById(R.id.crime_required_police);
        mRequiredPoliceCheckBox.setChecked(mCrime.isRequiredPolice());
        mRequiredPoliceCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                mCrime.setRequiredPolice(isChecked);
            }
        });
        return v;
    }

    @Override
    public void onPause() {
        super.onPause();

        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode != Activity.RESULT_OK)
            return;
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) intent.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        } else if (requestCode == REQUEST_TIME) {
            Date date = (Date) intent.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mCrime.setDate(date);
            updateTime();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_crime:
                CrimeLab.get(getActivity()).deleteCrime(mCrime);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateDate() {
        DateFormat df = new SimpleDateFormat("E, dd MMMM, YYYY ");
        mDateButton.setText(df.format(mCrime.getDate()));
    }

    private void updateTime() {
        DateFormat df = new SimpleDateFormat("HH:mm");
        mTimeButton.setText(df.format(mCrime.getDate()));
    }

    public void returnResult() {
        getActivity().setResult(Activity.RESULT_OK, null);
    }
}
