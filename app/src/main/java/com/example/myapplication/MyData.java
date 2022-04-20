package com.example.myapplication;

import android.net.Uri;

public class MyData {

    Uri imageuri;
    String message;


    MyData(Uri imageuri,String message){
        this.imageuri=imageuri;
        this.message=message;
    }

    public String getMessage() {
        return message;
    }

    public Uri getImageuri() {
        return imageuri;
    }

    public void setImageuri(Uri imageuri) {
        this.imageuri = imageuri;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
