package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;



public class Login extends AppCompatActivity {

    EditText ID입력;
    EditText PW입력;
    Button 로그인버튼;
    Button 회원가입버튼;
    SharedPreferences 로그인쉐어드프리퍼런스;
    SharedPreferences.Editor 로그인쉐어드에디터;

    @Override
    protected void onCreate(Bundle savedInstanceState) {//최초 빌드때 실행

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        ID입력 = (EditText) findViewById(R.id.ID에딧텍스트);
        PW입력 = (EditText) findViewById(R.id.PW에딧텍스트);
        로그인버튼 = (Button) findViewById(R.id.로그인버튼);//누르면 기존 정보들을 가지고 홈액티비티로 가야한다.
        로그인버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //가입한 ID가 키값, 제이슨데이터를 변환한 스트링이 밸류값.
                String InputID = ID입력.getText().toString();//입력받은 ID값 스트링으로 변환
                String InputPW = PW입력.getText().toString();
                로그인쉐어드프리퍼런스 = getSharedPreferences("회원가입쉐어드프리퍼런스", Activity.MODE_PRIVATE);
                로그인쉐어드에디터=로그인쉐어드프리퍼런스.edit();
                로그인쉐어드에디터.clear();
                로그인쉐어드에디터.apply();
                String strJson = 로그인쉐어드프리퍼런스.getString(InputID, null);

                if (strJson != null) {
                    try {

                        JSONObject response = new JSONObject(strJson);
                        String 저장된ID = response.get("ID").toString();
                        String 저장된PW = response.get("PW").toString();
                        String 저장된이름 = response.get("이름").toString();
                        String 저장된전화번호 = response.get("전화번호").toString();
                        String 저장된이메일 = response.get("이메일").toString();
                        String 저장된처음사귄날 = response.get("처음사귄날").toString();

                        Intent intent = new Intent(getApplicationContext(), Home.class);
                        intent.putExtra("이름",저장된이름);
                        intent.putExtra("전화번호",저장된전화번호);
                        intent.putExtra("이메일",저장된이메일);

                        startActivity(intent);
                        finish();
                        Toast.makeText(getApplicationContext(),"정보 존재",Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {

                    }

                } else{
                    Toast.makeText(getApplicationContext(),"정보없음",Toast.LENGTH_SHORT).show();
                }

            }

        });
        회원가입버튼 = (Button) findViewById(R.id.회원가입버튼);//누르면 회원가입 액티비티를 띄워야 한다.
        회원가입버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });

    }


}