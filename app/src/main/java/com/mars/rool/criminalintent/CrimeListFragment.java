package com.mars.rool.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class CrimeListFragment extends Fragment {

    private static final int REQUEST_CRIME = 1;

    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mCrimeAdapter;

    private int mCurrentCrimeIndex;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_crime_list, container, false);

        mCurrentCrimeIndex = -1;

        mCrimeRecyclerView = v.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());

        if (mCrimeAdapter == null) {
            mCrimeAdapter = new CrimeAdapter(crimeLab);
            mCrimeRecyclerView.setAdapter(mCrimeAdapter);
        } else {
            mCrimeAdapter.updateUI(mCurrentCrimeIndex);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CRIME && resultCode == FragmentActivity.RESULT_OK) {
            //обработка результата
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

        private CrimeLab mCrimeLab;

        CrimeAdapter (CrimeLab crimeLab) {
            mCrimeLab = crimeLab;
        }

        @NonNull
        @Override
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new CrimeHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder holder, int position) {
            holder.bind(mCrimeLab.getCrime(position), position);
        }

        @Override
        public int getItemCount() {
            return mCrimeLab.getSize();
        }

        @Override
        public int getItemViewType(int position) {
            if (mCrimeLab.getCrime(position).isRequiredPolice())
                return 1;
            else
                return 0;
        }

        private void updateUI(int position) {
            if (position == -1)
                return;
            notifyItemChanged(position);
        }
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mCallPoliceImageView;
        private ImageView mSoldedImageView;

        private Crime mCrime;
        private int mPosition;

        CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_crime, parent, false));

            mTitleTextView = this.itemView.findViewById(R.id.list_crime_title);
            mDateTextView = this.itemView.findViewById(R.id.list_crime_date);
            mCallPoliceImageView = this.itemView.findViewById(R.id.list_crime_special);
            mSoldedImageView = this.itemView.findViewById(R.id.list_crime_solved);

            itemView.setOnClickListener(this);
        }

        void bind(Crime crime, int position) {
            mCrime = crime;
            mPosition = position;
            mTitleTextView.setText(mCrime.getTitle());
            DateFormat df = new SimpleDateFormat("E, dd MMMM, YYYY ");
            mDateTextView.setText(df.format(mCrime.getDate()));
            mCallPoliceImageView.setVisibility(crime.isRequiredPolice() ? View.VISIBLE : View.INVISIBLE);
            mSoldedImageView.setVisibility(crime.isSolved() ? View.VISIBLE : View.INVISIBLE);
        }

        @Override
        public void onClick(View view) {
            Log.d(CrimeListActivity.DEBUG_TAG, "CrimeHolder onClick");
            mCurrentCrimeIndex = mPosition;
            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
            startActivityForResult(intent, CrimeListFragment.REQUEST_CRIME);
        }
    }

}
