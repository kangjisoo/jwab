package org.techtown.projectmain;

public class ProjectPerson {
    private String members;
    private String searchId;

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
}
