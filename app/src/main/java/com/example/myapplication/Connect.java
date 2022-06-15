package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
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
    TextView 나의이메일표시;
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
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(1);

        안내문구 = (TextView) findViewById(R.id.안내텍스트뷰);
        나의이메일표시 = (TextView) findViewById(R.id.나의ID);
        String 나의이메일 = intent.getStringExtra("나의이메일");
        나의이메일표시.setText("나의 이메일: " + 나의이메일);//로그인 할 때 입력한 나의 아이디
        ID입력 = (EditText) findViewById(R.id.상대방ID입력);


        확인버튼 = (Button) findViewById(R.id.연결버튼);
        확인버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String 상대방이메일 = ID입력.getText().toString();//에딧텍스트에서 연결할 대상의 이메일를 입력받음
                String 상대방제이슨스트링 = 쉐어드.getString(상대방이메일, null);//상대방 이메일를 키값으로 가진 쉐어드 내 스트링값 받아옴
                String 나의제이슨스트링 = 쉐어드.getString(나의이메일, null);//내 이메일를 키값으로 가진 쉐어드 내 스트링값 받아옴

                if (쉐어드.getString(상대방이메일, null) != null) {//상대가 등록된 사용자라면
                    if (상대방이메일.equals(나의이메일)) {//자기 아이디 연결하려 하면
                        Toast.makeText(getApplicationContext(), "자기 자신과는 연결할 수 없습니다", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            JSONObject 나의제이슨객체 = new JSONObject(나의제이슨스트링);
                            JSONObject 상대방제이슨객체 = new JSONObject(상대방제이슨스트링);//상대방 이메일로 만든 제이슨 객체
                            if (상대방제이슨객체.get("연결여부").toString().equals("false")) {//상대방의 연결여부가 false라면
                                상대방제이슨객체.put("받은연결요청", "true");//요청 받는 입장에서는 받은 연결요청을 true로 바꿔줌
                                나의제이슨객체.put("연결대기", "true");//요청하는 입장에서는 연결대기를 true로 바꿔줌
                                나의제이슨객체.put("연결대기상대", 상대방이메일);
                                상대방제이슨객체.put("연결요청상대", 나의이메일);
                                String 나의제이슨스트링2=나의제이슨객체.toString();
                                String 상대방제이슨스트링2 = 상대방제이슨객체.toString();//상대방 제이슨 다시 스트링으로 바꿈
                                쉐어드에디터.putString(나의이메일,나의제이슨스트링2);//스트링으로 바꾼 제이슨을 키값은 상대이메일, 밸류는 제이슨으로 넣음
                                쉐어드에디터.putString(상대방이메일, 상대방제이슨스트링2);//스트링으로 바꾼 제이슨을 키값은 상대이메일, 밸류는 제이슨으로 넣음
                                쉐어드에디터.apply();
                                Toast.makeText(getApplicationContext(), 상대방이메일 + "님께 연결요청을 보냈습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Login.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "연결할 수 없는 사용자입니다", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "없다", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}
