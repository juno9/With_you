package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class Register extends AppCompatActivity {


    EditText ID입력;
    EditText PW입력;
    EditText 이름입력;
    EditText 이메일입력;
    EditText 전화번호입력;
    EditText 처음사귄날입력;
    Button 회원가입버튼;
    Button 취소버튼;
    String ID, PW, 이름, 이메일, 전화번호, 처음사귄날;
    User 유저;
    ArrayList<User> 유저배열 = new ArrayList<User>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {//최초 빌드때 실행

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        File file = new File(getApplication().getFilesDir(), 이름 + ".json");


        ID입력 = findViewById(R.id.가입ID에딧텍스트);
        PW입력 = findViewById(R.id.가입PW에딧텍스트);
        이름입력 = findViewById(R.id.가입이름에딧텍스트);
        이메일입력 = findViewById(R.id.가입이메일에딧텍스트);
        전화번호입력 = findViewById(R.id.가입전화번호에딧텍스트);
        처음사귄날입력 = findViewById(R.id.가입처음사귄날에딧텍스트);
        //가입할 때 입력란에 정보를 입력하면

        회원가입버튼 = findViewById(R.id.회원가입완료버튼);
        회원가입버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ID = ID입력.getText().toString();
                PW = PW입력.getText().toString();
                이름 = 이름입력.getText().toString();
                전화번호 = 전화번호입력.getText().toString();
                이메일 = 이메일입력.getText().toString();
                처음사귄날 = 처음사귄날입력.getText().toString();
                User 유저 = new User(이름, ID, PW, 전화번호, 처음사귄날, null);
                유저배열.add(유저);
                try {
                    JSONArray jArray = new JSONArray();//제이슨 배열 생성
                    for (int j = 0; j < 유저배열.size(); j++) {
                        JSONObject sObject = new JSONObject();//배열 내에 들어갈 json오브젝트
                        sObject.put("ID", 유저배열.get(j).getID());
                        sObject.put("PW", 유저배열.get(j).getPW());
                        sObject.put("이름", 유저배열.get(j).get이름());
                        sObject.put("전화번호", 유저배열.get(j).get전화번호());
                        sObject.put("이메일", 유저배열.get(j).get이메일());
                        sObject.put("처음사귄날", 유저배열.get(j).get시작날짜());
                        jArray.put(sObject);
                        if (j >= 유저배열.size() - 1) {


                        }


                    }

                    Toast.makeText(getApplicationContext(), "가입완료", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });//각 에딧텍스트의 값을 제이슨 형식으로 묶어주고
        취소버튼 = findViewById(R.id.취소버튼);
        취소버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}
