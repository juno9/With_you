package com.example.with_you;

public class Event {
    String 날짜;
    String 내용;


    Event(String 날짜,String 내용){
         this.날짜=날짜;
       this.내용=내용;

    }

    public void set날짜(String 날짜) {
        this.날짜 = 날짜;
    }

    public void set내용(String 내용) {
        this.내용 = 내용;
    }

    public String get날짜() {
        return 날짜;
    }

    public String get내용() {
        return 내용;
    }
}
