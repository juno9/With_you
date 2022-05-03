package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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


    Random random = new Random();
    int I;
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
                String 상대방ID = ID입력.getText().toString();//에딧텍스트에서 연결할 대상의 ID를 받음
                String 상대방제이슨스트링 = 쉐어드.getString(상대방ID, null);//상대방 ID를 키값으로 가진 쉐어드 내 스트링값 받아옴
                String 나의제이슨스트링 = 쉐어드.getString(내ID, null);//내 ID를 키값으로 가진 쉐어드 내 스트링값 받아옴

                if (쉐어드.getString(상대방ID, null) != null) {//상대가 등록된 사용자인지 먼저 판단
                    try {
                        JSONObject 상대방제이슨객체 = new JSONObject(상대방제이슨스트링);//상대방 ID로 만든 제이슨 객체
                        if (상대방제이슨객체.get("연결여부").toString().equals("false")) {//상대방의 연결여부가 false라면
                            JSONObject 나의제이슨객체 = new JSONObject(나의제이슨스트링);//연결 시도하는 본인의 정보가 들어있는 제이슨 객체

                            상대방제이슨객체.put("연결요청", "O");//상대방의 연결 상대를 나로 설정
                            나의제이슨객체.put("연결요청", "O");//나의 연결상대를 입력한 ID로 설정
                            상대방제이슨객체.put("연결요청상대", 내ID);//상대방의 연결 상대를 나로 설정
                            나의제이슨객체.put("연결요청상대", 상대방ID);//나의 연결상대를 입력한 ID로 설정

                            String yourString = 상대방제이슨객체.toString();
                            String yourString2 = 나의제이슨객체.toString();

                            쉐어드에디터.putString(상대방ID, yourString);//회원 쉐어드에 제이슨 스트링(밸류)을 넣음, 키값은 상대방 ID
                            쉐어드에디터.putString(내ID, yourString2);
                            쉐어드에디터.apply();
                            Toast.makeText(getApplicationContext(), 상대방ID + "님에게 연결 요청을 보냈습니다.", Toast.LENGTH_SHORT).show();
                            finish();
//                            JSONObject albumjsonObject = new JSONObject();//상대방 ID로 만든 제이슨 객체
//                           JSONObject albumjsonObject2 = new JSONObject();//내 ID로 만든 제이슨 객체
//                           albumjsonObject.put("첫번째사진", "X");
//                           albumjsonObject2.put("첫번째사진", "X");
//                           albumjsonObject.put("연결요청", "O");
//                           albumjsonObject2.put("연결요청", "O");
//                           String albumjsnstr = albumjsonObject.toString();
//                           String albumjsnstr2 = albumjsonObject2.toString();
//                           앨범쉐어드에디터.putString(상대방ID, albumjsnstr);
//                           앨범쉐어드에디터.putString(내ID, albumjsnstr2);
//                           앨범쉐어드에디터.apply();
//
//                           intent2 = new Intent(getApplicationContext(), Home.class);
//                           startActivity(intent2);

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
