package org.techtown.projectmain;

public class ProjectHomeList {
    String projectName;
    String person;
    String key;

    public ProjectHomeList(String projectName, String person){
        this.projectName = projectName;
        this.person = person;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getKey(){return key;}

    public void setKey(String key){this.key = key; }
}
