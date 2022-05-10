package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class Connect extends AppCompatActivity {

    Intent intent;

    TextView 안내문구;
    TextView 내ID표시;
    EditText ID입력;
    Button 확인버튼;
    SharedPreferences 쉐어드;
    SharedPreferences.Editor 쉐어드에디터;
    SharedPreferences 앨범쉐어드;
    SharedPreferences.Editor 앨범쉐어드에디터;



    Intent intent2;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        intent = getIntent();

        쉐어드 = getSharedPreferences("회원정보쉐어드프리퍼런스", MODE_PRIVATE);
        쉐어드에디터 = 쉐어드.edit();
        앨범쉐어드 = getSharedPreferences("앨범쉐어드프리퍼런스", MODE_PRIVATE);
        앨범쉐어드에디터 = 앨범쉐어드.edit();


        안내문구 = (TextView) findViewById(R.id.안내텍스트뷰);
        내ID표시 = (TextView) findViewById(R.id.나의ID);
        String 내ID = intent.getStringExtra("나의ID");
        내ID표시.setText("나의 ID: " + 내ID);//로그인 할 때 입력한 나의 아이디
        ID입력 = (EditText) findViewById(R.id.상대방ID입력);


        확인버튼 = (Button) findViewById(R.id.연결버튼);
        확인버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String 상대방ID = ID입력.getText().toString();//에딧텍스트에서 연결할 대상의 ID를 입력받음
                String 상대방제이슨스트링 = 쉐어드.getString(상대방ID, null);//상대방 ID를 키값으로 가진 쉐어드 내 스트링값 받아옴
                String 나의제이슨스트링 = 쉐어드.getString(내ID, null);//내 ID를 키값으로 가진 쉐어드 내 스트링값 받아옴

                if (쉐어드.getString(상대방ID, null) != null) {//상대가 등록된 사용자인지 먼저 판단
                    try {
                        JSONObject 상대방제이슨객체 = new JSONObject(상대방제이슨스트링);//상대방 ID로 만든 제이슨 객체
                        if (상대방제이슨객체.get("연결여부").toString().equals("false")) {//상대방의 연결여부가 false라면
                            JSONObject 나의제이슨객체 = new JSONObject(나의제이슨스트링);//연결 시도하는 본인의 정보가 들어있는 제이슨 객체
                            JSONObject 나의앨범제이슨객체 = new JSONObject();//상대방 ID를 키값으로 만들 제이슨 객체
                            JSONObject 상대방앨범제이슨객체 = new JSONObject();//내 ID를 키값으로 만들 제이슨 객체

                            상대방제이슨객체.put("연결상대", 내ID);//상대방의 연결 상대를 나로 설정
                            나의제이슨객체.put("연결상대", 상대방ID);//나의 연결상대를 입력한 ID로 설정
                            상대방제이슨객체.put("연결여부", "true");//상대방의 연결 상대를 나로 설정
                            나의제이슨객체.put("연결여부", "true");//나의 연결상대를 입력한 ID로 설정
                            String 저장된ID = 나의제이슨객체.get("ID").toString();
                            String 저장된PW = 나의제이슨객체.get("PW").toString();
                            String 저장된이름 = 나의제이슨객체.get("이름").toString();
                            String 저장된전화번호 = 나의제이슨객체.get("전화번호").toString();
                            String 저장된이메일 = 나의제이슨객체.get("이메일").toString();
                            String 저장된처음사귄날 = 나의제이슨객체.get("처음사귄날").toString();
                            String 저장된연결상대 = 나의제이슨객체.get("연결상대").toString();

                            String yourString = 상대방제이슨객체.toString();
                            String yourString2 = 나의제이슨객체.toString();


                            나의앨범제이슨객체.put("첫번째사진", "X");
                            상대방앨범제이슨객체.put("첫번째사진", "X");

                            String albumjsnstr = 나의앨범제이슨객체.toString();
                            String albumjsnstr2 = 상대방앨범제이슨객체.toString();

                            쉐어드에디터.putString(상대방ID, yourString);//회원 쉐어드에 제이슨 스트링(밸류)을 넣음, 키값은 상대방 ID
                            쉐어드에디터.putString(내ID, yourString2);

                            앨범쉐어드에디터.putString(상대방ID, albumjsnstr);
                            앨범쉐어드에디터.putString(내ID, albumjsnstr2);
                            앨범쉐어드에디터.apply();
                            쉐어드에디터.apply();

                            intent2 = new Intent(getApplicationContext(), Home.class);
                            intent2.putExtra("ID", 저장된ID);
                            intent2.putExtra("이름", 저장된이름);
                            intent2.putExtra("전화번호", 저장된전화번호);
                            intent2.putExtra("이메일", 저장된이메일);
                            intent2.putExtra("처음사귄날", 저장된처음사귄날);
                            intent2.putExtra("연결상대", 저장된연결상대);

                            startActivity(intent2);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "연결할 수 없는 사용자입니다", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "없다", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}
