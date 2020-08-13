package org.techtown.projectmain;

import android.widget.CheckBox;

public class ProjectPerson {
    private String members;
    private String searchId;
    private boolean isChecked;


    public ProjectPerson(String members, String searchId) {
        this.members = members;
        this.searchId = searchId;

    }

    public ProjectPerson(String members, String searchId, boolean isChecked){
        this.members = members;
        this.searchId = searchId;
        this.isChecked = isChecked;
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

    public void setChecked(boolean isChecked) {this.isChecked = isChecked;}

    public boolean isChecked() { return isChecked; }

}
