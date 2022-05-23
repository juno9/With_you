package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Options extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

         SharedPreferences 쉐어드프리퍼런스 = getSharedPreferences("회원정보쉐어드프리퍼런스", MODE_PRIVATE);
        SharedPreferences.Editor 쉐어드에디터 = 쉐어드프리퍼런스.edit();

        Button 로그아웃버튼=(Button) findViewById(R.id.로그아웃버튼);
        로그아웃버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Login.class);
                finish();
                startActivity(intent);
            }
        });
    }
}
