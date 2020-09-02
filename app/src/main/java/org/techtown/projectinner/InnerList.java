package org.techtown.projectinner;

public class InnerList {
    String personName;
    String message;
    String key;
    String myName;
    String myMessage;

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public String getMyMessage() {
        return myMessage;
    }

    public void setMyMessage(String myMessage) {
        this.myMessage = myMessage;
    }


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
