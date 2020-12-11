package org.techtown.projectmain;

import android.widget.TextView;

//내 활동 데이타 ProjectBottomMenu
public class MyActivitiesData {

    private String myAcId;
    private String myAcKind;
    private String myAcContents;
    private String myAcDate;
    private String myAcInfo;

    public MyActivitiesData(String myAcId, String myAcKind, String myAcContents, String myAcDate, String myAcInfo) {
        this.myAcId = myAcId;
        this.myAcKind = myAcKind;
        this.myAcContents = myAcContents;
        this.myAcDate = myAcDate;
        this.myAcInfo = myAcInfo;
    }

    public String getMyAcId() {
        return myAcId;
    }

    public void setMyAcId(String myAcId) {
        this.myAcId = myAcId;
    }

    public String getMyAcKind() {
        return myAcKind;
    }

    public void setMyAcKind(String myAcKind) {
        this.myAcKind = myAcKind;
    }

    public String getMyAcContents() {
        return myAcContents;
    }

    public void setMyAcContents(String myAcContents) {
        this.myAcContents = myAcContents;
    }

    public String getMyAcDate() {
        return myAcDate;
    }

    public void setMyAcDate(String myAcDate) {
        this.myAcDate = myAcDate;
    }

    public String getMyAcInfo() {
        return myAcInfo;
    }

    public void setMyAcInfo(String myAcInfo) {
        this.myAcInfo = myAcInfo;
    }
}
