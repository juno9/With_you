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
    String 내ID;
    String jsnstr;
    Random random=new Random();
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

        안내문구 = (TextView) findViewById(R.id.안내텍스트뷰);
        내ID표시 = (TextView) findViewById(R.id.나의ID);
        내ID = intent.getStringExtra("나의ID");
        내ID표시.setText("나의 ID: " + 내ID);//로그인 할 때 입력한 나의 아이디
        ID입력 = (EditText) findViewById(R.id.상대방ID입력);


        확인버튼 = (Button) findViewById(R.id.연결버튼);
        확인버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                I= (random.nextInt(9)+1)*1000+ (random.nextInt(10))*100+ (random.nextInt(10))*10+random.nextInt(10);
                String 상대방ID = ID입력.getText().toString();
                jsnstr = 쉐어드.getString(상대방ID, null);
                String jsnstr2 = 쉐어드.getString(내ID, null);
                if (쉐어드.getString(상대방ID, null) != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(jsnstr);//상대방 ID로 만든 제이슨 객체
                        JSONObject jsonObject2 = new JSONObject(jsnstr2);//연결 시도하는 본인의 정보가 들어있는 제이슨 객체
                        Toast.makeText(getApplicationContext(), jsonObject.get("이름").toString(), Toast.LENGTH_SHORT).show();
                        jsonObject.put("앨범코드", I);
                        jsonObject.put("연결상대",내ID);//상대방의 연결 상대를 나로 설정
                        jsonObject2.put("앨범코드", I);
                        jsonObject2.put("연결상대",상대방ID);//나의 연결상대를 입력한 ID로 설정
                        jsonObject2.put("연결여부","true");
                        String yourString =  jsonObject.toString();
                        String yourString2 =  jsonObject2.toString();
                        쉐어드에디터.putString(상대방ID,yourString);
                        쉐어드에디터.putString(내ID,yourString2);
                        쉐어드에디터.apply();
                        intent2 = new Intent(getApplicationContext(), Home.class);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent2);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "없다", Toast.LENGTH_SHORT).show();
                }

            }


        });

    }
}
