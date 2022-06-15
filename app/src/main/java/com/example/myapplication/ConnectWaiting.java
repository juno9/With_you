package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class ConnectWaiting extends AppCompatActivity {

    TextView 안내텍스트뷰;
    Button 돌아가기버튼;
    JSONObject 나의제이슨객체;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connectwaitng);

//여기는 요청한 사람이 요청확인을 받기 전에 대기하는 곳
        //누구한테 요청했는지 만 띄워주면 된다.

        안내텍스트뷰 = findViewById(R.id.대기안내텍스트뷰);
        돌아가기버튼 = findViewById(R.id.돌아가기버튼);

        SharedPreferences 쉐어드 = getSharedPreferences("회원정보쉐어드프리퍼런스", MODE_PRIVATE);
        SharedPreferences.Editor 쉐어드에디터 = 쉐어드.edit();


        Intent intent = getIntent();
        String 내ID = intent.getStringExtra("나의ID");

        try {
            나의제이슨객체=new JSONObject(쉐어드.getString(내ID, ""));


        } catch (JSONException e) {
            e.printStackTrace();
        }
        String 상대방ID= null;
        try {
            상대방ID = 나의제이슨객체.get("연결대기상대").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        안내텍스트뷰.setText(상대방ID+"님의 수락을 기다리고 있습니다.");


    }
}
