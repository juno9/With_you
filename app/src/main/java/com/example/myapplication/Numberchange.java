package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class Numberchange extends AppCompatActivity {

    EditText 번호입력;
    Button 확인버튼;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numberchange);//넘버체인지 액티비티가 열림
        번호입력 = (EditText) findViewById(R.id.editTextNumber);//에디트텍스트 만들어둔거 연결
        확인버튼 = (Button) findViewById(R.id.button8);//옆에 확정버튼 연결
        //버튼1의 값을 받아왔다
        //에딧텍스트에 번호 입력하고 버튼 누르면 전화번호나오는 버튼의 텍스트가 바뀌어야된다.
        확인버튼.setOnClickListener(new View.OnClickListener() {//컨펌버튼이 어떤 역할을 할지
            @Override
            public void onClick(View view) {
//리퀘스트코드에 따라 구분해야하는데 어케하지
                if (getIntent().getStringExtra("번호변경인텐트").equals("10")) {
                    //만일 다이얼로그2에서 왔다면 어떻게 하라 이런식으로
                    Intent intent = new Intent();
                    String 받은번호 = 번호입력.getText().toString();
                    intent.putExtra("보낼번호", 받은번호);//인텐트에 값을 넣고 키값을 정함
                    setResult(1, intent);//인텐트랑 결과코드 같이 전달
                    finish();
                } else if(getIntent().getStringExtra("번호변경인텐트").equals("20")){
                    Intent intent = new Intent();
                    String 받은번호 = 번호입력.getText().toString();
                    intent.putExtra("보낼번호", 받은번호);//인텐트에 값을 넣고 키값을 정함
                    setResult(2, intent);//인텐트랑 결과코드 같이 전달
                    finish();
                }
            }

        });//컨펌버튼을 누르면 받은 텍스트 값이 보인다.
        //액티비티에서 입력값을 받아오는거는 하고 있으니 이걸 전화버튼에 넘기기만 하면 된다.그걸 저장해보자
    }//인텐트의 시작점이 여기라고
}
