package org.techtown.projectmain;

public class ProjectHomeList {
    String projectName;
    String person;
    String key;
    int position;



    public ProjectHomeList(String projectName, String person){
        this.projectName = projectName;
        this.person = person;
        this.position = -5;
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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
