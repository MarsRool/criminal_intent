package com.mars.rool.criminalintent;

import java.util.Date;
import java.util.UUID;

public class Crime {

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private boolean mRequiredPolice;

    public Crime(UUID id, String title, Date date, boolean solved, boolean requiredPolice) {
        mId = id;
        mTitle = title;
        mDate = date;
        mSolved = solved;
        mRequiredPolice = requiredPolice;
    }
    public Crime(UUID id) {
        this(id, "", new Date(), false, false);
    }
    public Crime() {
        this(UUID.randomUUID());
    }

    public UUID getId() { return mId; }

    public String getTitle() {
        return mTitle;
    }
    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }
    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }
    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public boolean isRequiredPolice() {
        return mRequiredPolice;
    }
    public void setRequiredPolice(boolean requiredPolice) {
        mRequiredPolice = requiredPolice;
    }
}
