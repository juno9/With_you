package com.example.myapplication;

import android.net.Uri;

public class MyData {

    String imageString;
    String message;


    MyData(String imageString,String message){
        this.imageString=imageString;
        this.message=message;
    }

    public String getMessage() {
        return message;
    }

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
