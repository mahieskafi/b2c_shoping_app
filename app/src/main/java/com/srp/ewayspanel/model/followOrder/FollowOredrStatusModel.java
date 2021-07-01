package com.srp.ewayspanel.model.followOrder;

public class FollowOredrStatusModel {

    String mText;
    int mStatusCode;
    Boolean mIsClick;

    public FollowOredrStatusModel(String mText, int mStatusCode, Boolean mIsClick) {
        this.mText = mText;
        this.mStatusCode = mStatusCode;
        this.mIsClick = mIsClick;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public void setmStatusCode(int mStatusCode) {
        this.mStatusCode = mStatusCode;
    }

    public void setmIsClick(Boolean mIsClick) {
        this.mIsClick = mIsClick;
    }

    public String getmText() {
        return mText;
    }

    public int getmStatusCode() {
        return mStatusCode;
    }

    public Boolean getmIsClick() {
        return mIsClick;
    }
}
