package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
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
    Dialog 연결확인다이얼로그;
    CheckBox 자동로그인체크박스;

    @Override
    protected void onCreate(Bundle savedInstanceState) {//최초 빌드때 실행

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        로그인쉐어드프리퍼런스 = getSharedPreferences("회원정보쉐어드프리퍼런스", Activity.MODE_PRIVATE);
        로그인쉐어드에디터 = 로그인쉐어드프리퍼런스.edit();
        SharedPreferences 앨범쉐어드프리퍼런스 = getSharedPreferences("앨범쉐어드프리퍼런스", Activity.MODE_PRIVATE);
        SharedPreferences.Editor 앨범쉐어드에디터 = 앨범쉐어드프리퍼런스.edit();
        SharedPreferences 자동로그인쉐어드프리퍼런스 = getSharedPreferences("자동로그인쉐어드프리퍼런스", Activity.MODE_PRIVATE);
        SharedPreferences.Editor 자동로그인쉐어드에디터 = 앨범쉐어드프리퍼런스.edit();


        ID입력 = (EditText) findViewById(R.id.ID에딧텍스트);
        PW입력 = (EditText) findViewById(R.id.PW에딧텍스트);

//        앨범쉐어드에디터.clear();
//        로그인쉐어드에디터.clear();
//        앨범쉐어드에디터.apply();
//        로그인쉐어드에디터.apply();
//만일 자동로그인이 활성화되어 있다면? 입력이 따로 필요없이 바로 로그인 할 수 있어야 한다.

        자동로그인체크박스 = (CheckBox) findViewById(R.id.자동로그인체크박스);


        로그인버튼 = (Button) findViewById(R.id.로그인버튼);//누르면 기존 정보들을 가지고 홈액티비티로 가야한다.
        로그인버튼.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                //가입한 ID가 키값, 제이슨데이터를 변환한 스트링이 밸류값.
                String InputID = ID입력.getText().toString();//입력받은 ID값 스트링으로 변환
                String InputPW = PW입력.getText().toString();

//
                String strJson = 로그인쉐어드프리퍼런스.getString(InputID, null);
                //스트링으로 변환하여 쉐어드에 저장한 제이슨 데이터를 다시 제이슨형태로 바꾸기 위해 스트링 형태로 재호출,
                // 가입할 때의 ID가 키값으로 쓰이도록 설정해 뒀으니 스트링 데이터를 쉐어드에서 가져옴
                if (strJson != null) {//회원정보 있으면
                    try {
                        JSONObject response = new JSONObject(strJson);
                        String 저장된ID = response.get("ID").toString();
                        String 저장된PW = response.get("PW").toString();
                        String 저장된이름 = response.get("이름").toString();
                        String 저장된전화번호 = response.get("전화번호").toString();
                        String 저장된이메일 = response.get("이메일").toString();
                        String 저장된처음사귄날 = response.get("처음사귄날").toString();
                        String 연결여부 = response.get("연결여부").toString();
                        String 저장된연결상대 = response.get("연결상대").toString();
                        if (InputID.equals(저장된ID) && InputPW.equals(저장된PW)) {//id,비밀번호를 똑바로 넣었으면
                            if(자동로그인체크박스.isChecked()) {
                                if (연결여부.equals("false")) {//연결되어있다면
                                    Intent intent = new Intent(getApplicationContext(), Connect.class);
                                    intent.putExtra("나의ID", 저장된ID);
                                    startActivity(intent);
                                    finish();
                                }//연결되어있다면
                                else if (연결여부.equals("true")) {//연결이 안되어있으면
                                    Intent intent = new Intent(getApplicationContext(), Home.class);
                                    intent.putExtra("ID", 저장된ID);
                                    intent.putExtra("이름", 저장된이름);
                                    intent.putExtra("전화번호", 저장된전화번호);
                                    intent.putExtra("이메일", 저장된이메일);
                                    intent.putExtra("처음사귄날", 저장된처음사귄날);
                                    intent.putExtra("연결상대", 저장된연결상대);
                                    startActivity(intent);
                                    finish();
                                }//연결이 안되어있으면
                            }
                            if (연결여부.equals("false")) {//연결되어있다면
                                Intent intent = new Intent(getApplicationContext(), Connect.class);
                                intent.putExtra("나의ID", 저장된ID);
                                startActivity(intent);
                                finish();
                            }//연결되어있다면
                            else if (연결여부.equals("true")) {//연결이 안되어있으면
                                Intent intent = new Intent(getApplicationContext(), Home.class);
                                intent.putExtra("ID", 저장된ID);
                                intent.putExtra("이름", 저장된이름);
                                intent.putExtra("전화번호", 저장된전화번호);
                                intent.putExtra("이메일", 저장된이메일);
                                intent.putExtra("처음사귄날", 저장된처음사귄날);
                                intent.putExtra("연결상대", 저장된연결상대);
                                startActivity(intent);
                                finish();
                            }//연결이 안되어있으면


                        } //id,비밀번호를 똑바로 넣었으면
                        else {//id,비밀번호 잘못 넣으면
                            Toast.makeText(getApplicationContext(), "로그인 정보가 다릅니다.", Toast.LENGTH_SHORT).show();
                        }//id,비밀번호 잘못 넣으면
                    } catch (JSONException e) {

                    }
                }//회원정보 있으면
                else {//회원정보 없으면
                    Toast.makeText(getApplicationContext(), "정보없음", Toast.LENGTH_SHORT).show();
                }//회원정보 없으면
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