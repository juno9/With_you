package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

public class Place implements Parcelable {
    String 이름;
    String 위도;
    String 경도;
    String 주소;


    Place(String 이름, String 주소, String 위도,String 경도) {
        this.이름 = 이름;
        this.주소 = 주소;
        this.위도 = 위도;
        this.경도 = 경도;
    }//생성자

    public void set이름(String 이름) {
        this.이름 = 이름;
    }

    public void set주소(String 주소) {
        this.주소 = 주소;
    }

    public void set위경도(String 위경도) {
        this.위도 = 위경도;
    }

    public void set경도(String 경도) {
        this.경도 = 경도;
    }

    public String get이름() {
        return 이름;
    }

    public String get주소() {
        return 주소;
    }

    public String get위경도() {
        return 위도;
    }

    public String get경도() {
        return 경도;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
