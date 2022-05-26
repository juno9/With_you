package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
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

public class Register extends AppCompatActivity {


    EditText ID입력;
    EditText PW입력;
    EditText 이름입력;
    EditText 이메일입력;
    EditText 전화번호입력;
    TextView 처음사귄날텍스트뷰;
    Button 회원가입버튼;
    Button 취소버튼;
    String ID, PW, 이름, 이메일, 전화번호, 처음사귄날;
    SharedPreferences 회원가입쉐어드프리퍼런스;
    SharedPreferences.Editor 회원가입쉐어드에디터;




    @Override
    protected void onCreate(Bundle savedInstanceState) {//최초 빌드때 실행

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        ID입력 = findViewById(R.id.가입ID에딧텍스트);
        PW입력 = findViewById(R.id.가입PW에딧텍스트);
        이름입력 = findViewById(R.id.가입이름에딧텍스트);
        이메일입력 = findViewById(R.id.가입이메일에딧텍스트);
        전화번호입력 = findViewById(R.id.가입전화번호에딧텍스트);

       // 처음사귄날텍스트뷰 = findViewById(R.id.가입처음사귄날텍스트뷰);
        //가입할 때 입력란에 정보를 입력하면
//        Calendar cal = Calendar.getInstance();
//        DatePickerDialog 날짜선택다이얼로그 = new DatePickerDialog(this, mDateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
//        처음사귄날텍스트뷰.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                날짜선택다이얼로그.show();
//            }
//        });



        회원가입버튼 = findViewById(R.id.회원가입완료버튼);
        회원가입버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ID = ID입력.getText().toString();
                PW = PW입력.getText().toString();
                이름 = 이름입력.getText().toString();
                전화번호 = 전화번호입력.getText().toString();
                이메일 = 이메일입력.getText().toString();


                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.put("ID",ID);
                    jsonObject.put("PW",PW);
                    jsonObject.put("이름",이름);
                    jsonObject.put("전화번호",전화번호);
                    jsonObject.put("이메일",이메일);
                    jsonObject.put("처음사귄날",처음사귄날);
                    jsonObject.put("연결여부",false);
                    jsonObject.put("연결상대","x");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //제이슨 객체를 만들어주고 거기에 값을 넣어야
                String yourString =  jsonObject.toString();//위의 값들을 가진 제이슨 데이터를 스트링으로 변환


                회원가입쉐어드프리퍼런스 = getSharedPreferences("회원정보쉐어드프리퍼런스", Activity.MODE_PRIVATE);//ID를 키값으로 가지는 쉐어드를 만든다.
                회원가입쉐어드에디터 = 회원가입쉐어드프리퍼런스.edit();
                회원가입쉐어드에디터.putString(ID,yourString);//인트로 생성한 난수를 스트링으로 변환하여 쉐어드에 저장된 회원정보의 키값으로 활용
                회원가입쉐어드에디터.apply();//에디터에 적용
                finish();
                Toast.makeText(getApplicationContext(),"가입완료",Toast.LENGTH_SHORT).show();
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

//    DatePickerDialog.OnDateSetListener mDateSetListener =
//            new DatePickerDialog.OnDateSetListener() {
//                @SuppressLint("DefaultLocale")
//                @Override
//                public void onDateSet(DatePicker datePicker, int yy, int mm, int dd) {
//                   처음사귄날텍스트뷰.setText(String.format("%d-%d-%d", yy,mm+1,dd));
//                }
//            };


}
