package com.example.with_you;

public class MyData {

    String imageString;
    String location;


    MyData(String imageString,String location){
        this.imageString=imageString;

        this.location=location;
    }



    public String getImageString() {
        return imageString;
    }

    public String getLocation() {
        return location;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
