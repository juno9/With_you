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
    String 상대방이메일;
    TextView 안내문구;
    TextView 나의인증번호표시;
    EditText 인증번호입력;
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
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(1);

        안내문구 = (TextView) findViewById(R.id.안내텍스트뷰);
        나의인증번호표시 = (TextView) findViewById(R.id.나의인증번호);//나의 인증번호 보여줌
        String 나의이메일 = intent.getStringExtra("나의이메일");
        String 제이슨스트링 = 쉐어드.getString(나의이메일, "");
        try {
            JSONObject 나의제이슨객체 = new JSONObject(제이슨스트링);
            String 인증번호 = 나의제이슨객체.get("인증번호").toString();
            나의인증번호표시.setText("나의 인증번호: " + 인증번호);//로그인 할 때 입력한 나의 아이디
        } catch (JSONException e) {
            e.printStackTrace();
        }

        인증번호입력 = (EditText) findViewById(R.id.상대방인증번호입력);


        확인버튼 = (Button) findViewById(R.id.연결버튼);
        확인버튼.setOnClickListener(new View.OnClickListener() {
            @Override//상대방이 누군지도 알아야 하는데.
            public void onClick(View view) {
                String 상대방인증번호 = 인증번호입력.getText().toString();//상대방 인증번호를 입력한다.
                String 인증번호제이슨스트링 = 쉐어드.getString(상대방인증번호, "");//받은 코드를 키값으로 가진 쉐어드 데이터를 스트링으로 받아옴
                try {
                    JSONObject 상대방연결용제이슨 = new JSONObject(인증번호제이슨스트링);//제이슨 객체를 만들고
                    상대방이메일 = 상대방연결용제이슨.get("이메일").toString();//만들어진 제이슨 객체에서 이메일을 가져옴
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String 상대방제이슨스트링 = 쉐어드.getString(상대방이메일, null);//상대방 이메일를 키값으로 가진 쉐어드 내 스트링값 받아옴
                String 나의제이슨스트링 = 쉐어드.getString(나의이메일, null);//내 이메일를 키값으로 가진 쉐어드 내 스트링값 받아옴
                if (쉐어드.getString(상대방이메일, null) != null) {//상대가 등록된 사용자라면
                    if (상대방이메일.equals(나의이메일)) {//자기 아이디 연결하려 하면
                        Toast.makeText(getApplicationContext(), "자기 자신과는 연결할 수 없습니다", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            JSONObject 나의제이슨객체 = new JSONObject(나의제이슨스트링);
                            JSONObject 상대방제이슨객체 = new JSONObject(상대방제이슨스트링);//상대방 이메일로 만든 제이슨 객체
                            String 나의인증번호 = 나의제이슨객체.get("인증번호").toString();
                            if (상대방제이슨객체.get("연결여부").toString().equals("false")) {//상대방의 연결여부가 false라면
                                상대방제이슨객체.put("연결상대", 나의이메일);//요청 받는 입장에서는 받은 연결요청을 true로 바꿔줌
                                나의제이슨객체.put("연결상대", 상대방이메일);//요청하는 입장에서는 연결대기를 true로 바꿔줌
                                String 나의제이슨스트링2 = 나의제이슨객체.toString();
                                String 상대방제이슨스트링2 = 상대방제이슨객체.toString();//상대방 제이슨 다시 스트링으로 바꿈
                                쉐어드에디터.putString(나의이메일, 나의제이슨스트링2);//스트링으로 바꾼 제이슨을 키값은 상대이메일, 밸류는 제이슨으로 넣음
                                쉐어드에디터.putString(상대방이메일, 상대방제이슨스트링2);//스트링으로 바꾼 제이슨을 키값은 상대이메일, 밸류는 제이슨으로 넣음
                                쉐어드에디터.apply();
                                Toast.makeText(getApplicationContext(), 상대방이메일 + "님과 연결되었습니다", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), ConnectRegister.class);
                                intent.putExtra("나의이메일",나의이메일);
                                intent.putExtra("연결상대",상대방이메일);
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
                    Toast.makeText(getApplicationContext(), "연결 상대를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

}
