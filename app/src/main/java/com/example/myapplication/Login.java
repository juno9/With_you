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
                로그인쉐어드에디터 = 로그인쉐어드프리퍼런스.edit();


                String strJson = 로그인쉐어드프리퍼런스.getString(InputID, null);
                   //스트링으로 변환하여 쉐어드에 저장한 제이슨 데이터를 다시 제이슨형태로 바꾸기 위해 스트링 형태로 재호출,
                   // 가입할 때의 ID가 키값으로 쓰이도록 설정해 뒀으니 스트링 데이터를 쉐어드에서 가져옴
                if (strJson != null) {
                    try {

                        JSONObject response = new JSONObject(strJson);
                        String 저장된ID = response.get("ID").toString();
                        String 저장된PW = response.get("PW").toString();
                        String 저장된이름 = response.get("이름").toString();
                        String 저장된전화번호 = response.get("전화번호").toString();
                        String 저장된이메일 = response.get("이메일").toString();
                        String 저장된처음사귄날 = response.get("처음사귄날").toString();
                        if (InputID.equals(저장된ID) && InputPW.equals(저장된PW)) {
                            Intent intent = new Intent(getApplicationContext(), Home.class);
                            intent.putExtra("ID", 저장된ID);
                            intent.putExtra("이름", 저장된이름);
                            intent.putExtra("전화번호", 저장된전화번호);
                            intent.putExtra("이메일", 저장된이메일);
                            intent.putExtra("처음사귄날", 저장된처음사귄날);

                            startActivity(intent);
                            finish();
                            Toast.makeText(getApplicationContext(), "정보 존재", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "로그인 정보가 다릅니다.", Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {

                    }

                } else {
                    Toast.makeText(getApplicationContext(), "정보없음", Toast.LENGTH_SHORT).show();
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