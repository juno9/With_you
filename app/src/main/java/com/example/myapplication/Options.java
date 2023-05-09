package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

public class Options extends AppCompatActivity {
    String 나의이메일;
    JSONObject jsonObject;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        Intent intent=getIntent();
        나의이메일 = intent.getStringExtra("나의이메일");
        SharedPreferences 쉐어드프리퍼런스 = getSharedPreferences("회원정보쉐어드프리퍼런스", MODE_PRIVATE);
        SharedPreferences.Editor 쉐어드에디터 = 쉐어드프리퍼런스.edit();
        ImageButton 프로필이미지 = (ImageButton) findViewById(R.id.imageButton7);
        String userjsnstr = 쉐어드프리퍼런스.getString(나의이메일, "_");

        try{
            jsonObject = new JSONObject(userjsnstr);
            Glide.with(getApplicationContext()).load(Uri.parse(jsonObject.get("프로필이미지").toString())).fitCenter().into(프로필이미지);
        }catch (Exception e){

        }




        Button 로그아웃버튼 = (Button) findViewById(R.id.로그아웃버튼);
        로그아웃버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                finish();
                startActivity(intent);
            }
        });
    }
}
