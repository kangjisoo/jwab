package org.techtown.projectmain;

import android.widget.CheckBox;

public class ProjectPerson {
    private String members;
    private String searchId;
    CheckBox membercheckBox;

    public ProjectPerson(String members, String searchId) {
        this.members = members;
        this.searchId = searchId;

    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) { this.searchId = searchId; }

//    public void getMembercheckBox() {
//        membercheckBox.setChecked(false);
//    }
//    public void setMembercheckBox(){
//        membercheckBox.setChecked(true);
//    }


}
