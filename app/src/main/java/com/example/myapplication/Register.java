package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {


    EditText PW입력;
    EditText PW확인입력;
    EditText 이름입력;
    EditText 이메일입력;
    EditText 전화번호입력;
    Button 회원가입버튼;
    Button 취소버튼;
    String PW, 이름, 이메일, 전화번호, PW확인;
    SharedPreferences 회원가입쉐어드프리퍼런스;
    SharedPreferences.Editor 회원가입쉐어드에디터;
    int 연결코드;


    @Override
    protected void onCreate(Bundle savedInstanceState) {//최초 빌드때 실행

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Intent intent = getIntent();
        이메일입력 = findViewById(R.id.가입이메일에딧텍스트);
        PW입력 = findViewById(R.id.가입PW에딧텍스트);
        PW확인입력 = findViewById(R.id.가입PW확인에딧텍스트);
        이름입력 = findViewById(R.id.가입이름에딧텍스트);
        전화번호입력 = findViewById(R.id.가입전화번호에딧텍스트);


        if (intent.getStringExtra("로그인방식") != null) {

            이메일입력.setText(intent.getStringExtra("이메일"));
            이름입력.setText(intent.getStringExtra("이름"));
            전화번호입력.setText(intent.getStringExtra("전화번호"));
        }


        회원가입버튼 = findViewById(R.id.회원가입완료버튼);
        회원가입버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                이메일 = 이메일입력.getText().toString();
                PW = PW입력.getText().toString();
                PW확인 = PW확인입력.getText().toString();
                이름 = 이름입력.getText().toString();
                전화번호 = 전화번호입력.getText().toString();
                String 인증번호=numberGen(6,2);
                String email = 이메일;
                Pattern pattern = android.util.Patterns.EMAIL_ADDRESS;
//                if(pattern.matcher(email).matches()){//입력한 이메일이 형식에 맞는지 확인
                    if(PW.equals(PW확인)) {
                        JSONObject jsonObject = new JSONObject();
                        JSONObject 연결용제이슨객체=new JSONObject();
                        try {
                            jsonObject.put("이메일", 이메일);
                            jsonObject.put("PW", PW);
                            jsonObject.put("이름", 이름);
                            jsonObject.put("전화번호", 전화번호);
                            jsonObject.put("연결여부", "false");
                            jsonObject.put("연결상대", "x");//필요함
                            jsonObject.put("연결대기", "false");
                            jsonObject.put("받은연결요청", "false");
                            jsonObject.put("프로필이미지", "");
                            jsonObject.put("인증번호", 인증번호);
                            jsonObject.put("처음만난날","");
                            연결용제이슨객체.put("이메일", 이메일);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //제이슨 객체를 만들어주고 거기에 값을 넣어야
                        String 회원정보제이슨스트링 = jsonObject.toString();//위의 값들을 가진 제이슨 데이터를 스트링으로 변환
                        String 연결용객체스트링 = 연결용제이슨객체.toString();

                        회원가입쉐어드프리퍼런스 = getSharedPreferences("회원정보쉐어드프리퍼런스", Activity.MODE_PRIVATE);//ID를 키값으로 가지는 쉐어드를 만든다.
                        회원가입쉐어드에디터 = 회원가입쉐어드프리퍼런스.edit();
                        회원가입쉐어드에디터.putString(이메일, 회원정보제이슨스트링);//인트로 생성한 난수를 스트링으로 변환하여 쉐어드에 저장된 회원정보의 키값으로 활용
                        회원가입쉐어드에디터.putString(인증번호, 연결용객체스트링);//인증할때만 쓸거
                        회원가입쉐어드에디터.apply();//에디터에 적용
                        finish();
                        Toast.makeText(getApplicationContext(), "가입완료", Toast.LENGTH_SHORT).show();
                    } else{
                        Toast.makeText(Register.this, "입력하신 비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                    }


//                } else {
//                    Toast.makeText(Register.this, "이메일 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
//                }




            }
        });//각 에딧텍스트의 값을 제이슨 형식으로 묶어줌
        취소버튼 = findViewById(R.id.취소버튼);
        취소버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public static String numberGen(int len, int dupCd ) {

        Random rand = new Random();
        String numStr = ""; //난수가 저장될 변수

        for(int i=0;i<len;i++) {

            //0~9 까지 난수 생성
            String ran = Integer.toString(rand.nextInt(10));

            if(dupCd==1) {
                //중복 허용시 numStr에 append
                numStr += ran;
            }else if(dupCd==2) {
                //중복을 허용하지 않을시 중복된 값이 있는지 검사한다
                if(!numStr.contains(ran)) {
                    //중복된 값이 없으면 numStr에 append
                    numStr += ran;
                }else {
                    //생성된 난수가 중복되면 루틴을 다시 실행한다
                    i-=1;
                }
            }
        }
        return numStr;
    }//난수생성 메소드



}
