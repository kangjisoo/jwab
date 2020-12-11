package org.techtown.projectinner;

import android.graphics.drawable.Drawable;

public class InnerList {
    String personName;
    String message;
    String key;
    String img;
    String myName;
    String myMessage;


    public InnerList(String personName, String message){
        this.personName = personName;
        this.message = message;

    }

    public InnerList(String personName, String message, String img){
        this.personName = personName;
        this.message = message;
        this.img = img;

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

    public String getImg(){return img;}

    public void setImg(String img) {
        this.img = img;
    }

}
