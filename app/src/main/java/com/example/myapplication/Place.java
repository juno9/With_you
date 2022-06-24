package com.example.myapplication;

public class Place {
    String 이름;
    String 위경도;

    String 주소;


    Place(String 이름,String 주소,String 위경도){
        this.이름=이름;
        this.주소=주소;
        this.위경도=위경도;

    }//생성자

    public void set이름(String 이름) {
        this.이름 = 이름;
    }

    public void set주소(String 주소) {
        this.주소 = 주소;
    }

    public void set위경도(String 위경도) {
        this.위경도 = 위경도;
    }

    public String get이름() {
        return 이름;
    }

    public String get주소() {
        return 주소;
    }

    public String get위경도() {
        return 위경도;
    }
}
