package org.techtown.projectmain;

import android.widget.TextView;

//내 활동 데이타 ProjectBottomMenu
public class MyActivitiesData {

    private TextView myAcId;
    private TextView myAcKind;
    private TextView myAcContents;
    private TextView myAcDate;
    private TextView myAcInfo;

    public MyActivitiesData(TextView myAcId, TextView myAcKind, TextView myAcContents, TextView myAcDate, TextView myAcInfo) {
        this.myAcId = myAcId;
        this.myAcKind = myAcKind;
        this.myAcContents = myAcContents;
        this.myAcDate = myAcDate;
        this.myAcInfo = myAcInfo;
    }

    public TextView getMyAcId() {
        return myAcId;
    }

    public void setMyAcId(TextView myAcId) {
        this.myAcId = myAcId;
    }

    public TextView getMyAcKind() {
        return myAcKind;
    }

    public void setMyAcKind(TextView myAcKind) {
        this.myAcKind = myAcKind;
    }

    public TextView getMyAcContents() {
        return myAcContents;
    }

    public void setMyAcContents(TextView myAcContents) {
        this.myAcContents = myAcContents;
    }

    public TextView getMyAcDate() {
        return myAcDate;
    }

    public void setMyAcDate(TextView myAcDate) {
        this.myAcDate = myAcDate;
    }

    public TextView getMyAcInfo() {
        return myAcInfo;
    }

    public void setMyAcInfo(TextView myAcInfo) {
        this.myAcInfo = myAcInfo;
    }
}
