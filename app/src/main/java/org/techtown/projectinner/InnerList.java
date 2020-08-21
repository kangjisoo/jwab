package org.techtown.projectinner;

public class InnerList {
    String personName;
    String message;
    String key;

    public InnerList(String personName, String message){
        this.personName = personName;
        this.message = message;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getKey(){return key;}

    public void setKey(String key){this.key = key; }
}
