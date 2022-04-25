package com.example.myapplication;

import java.util.ArrayList;

public class User {

    String 이름;
    String ID;
    String PW;
    String 전화번호;
    String 이메일;
    String 시작날짜;
    ArrayList<MyData> 이미지배열;


    User(String 이름,String ID,String PW, String 전화번호,String 시작날짜,String 이메일,ArrayList<MyData> 이미지배열){
        this.ID=ID;
        this.PW=PW;
        this.이름=이름;
        this.전화번호=전화번호;
        this.시작날짜=시작날짜;
        this.이미지배열=이미지배열;
        this.이메일=이메일;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setPW(String PW) {
        this.PW = PW;
    }

    public void set이름(String 이름) {
        this.이름 = 이름;
    }

    public void set시작날짜(String 시작날짜) {
        this.시작날짜 = 시작날짜;
    }

    public void set이메일(String 이메일) {
        this.이메일 = 이메일;
    }

    public void set이미지배열(ArrayList<MyData> 이미지배열) {
        this.이미지배열 = 이미지배열;
    }

    public void set전화번호(String 전화번호) {
        this.전화번호 = 전화번호;
    }

    public ArrayList<MyData> get이미지배열() {
        return 이미지배열;
    }

    public String getID() {
        return ID;
    }

    public String getPW() {
        return PW;
    }

    public String get시작날짜() {
        return 시작날짜;
    }

    public String get이름() {
        return 이름;
    }

    public String get이메일() {
        return 이메일;
    }

    public String get전화번호() {
        return 전화번호;
    }
}

