package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class ConnectRegister extends AppCompatActivity {
    TextView 날짜표시텍스트뷰;
    DatePickerDialog 날짜입력다이얼로그;
    Button 연결완료버튼;
    int mYear, mMonth, mDay = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connectregister);


        Intent intent = getIntent();
        String 나의이메일 = intent.getStringExtra("나의이메일");
        String 상대이메일 = intent.getStringExtra("연결상대");





        SharedPreferences 쉐어드 = getSharedPreferences("회원정보쉐어드프리퍼런스", MODE_PRIVATE);
        SharedPreferences.Editor 쉐어드에디터 = 쉐어드.edit();




//        if (intent.getStringExtra("로그인방식").equals("네이버로그인")) {
//            String 이름=intent.getStringExtra("이름");
//            String 전화번호=intent.getStringExtra("전화번호");
//            String 이메일=intent.getStringExtra("이메일");
//            이름입력.setText(이름);
//            전화번호입력.setText(전화번호);
//        }



        날짜표시텍스트뷰 = (TextView) findViewById(R.id.처음만난날텍스트뷰);//날짜표시할 텍스트뷰 연결
        Calendar cal = Calendar.getInstance();
        날짜입력다이얼로그 = new DatePickerDialog(this, mDateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));

        날짜표시텍스트뷰.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                날짜입력다이얼로그.show();
            }
        });


        String 제이슨바꿀스트링 = 쉐어드.getString(나의이메일, "");
        try {
            JSONObject 내제이슨 = new JSONObject(제이슨바꿀스트링);
            if (내제이슨.get("처음만난날").toString() != null) {
                String 처음만난날 = 내제이슨.get("처음만난날").toString();
                날짜표시텍스트뷰.setText(처음만난날);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        연결완료버튼 = findViewById(R.id.연결완료버튼);
        연결완료버튼.setOnClickListener(new View.OnClickListener() {
            String 제이슨바꿀스트링 = 쉐어드.getString(나의이메일, "");


            @Override
            public void onClick(View v) {


                String 처음만난날 = 날짜표시텍스트뷰.getText().toString();
                if (날짜표시텍스트뷰 == null) {
                    Toast.makeText(getApplicationContext(), "처음 만난날을 입력해 주세요", Toast.LENGTH_SHORT).show();
                }  else {
                    try {
                        JSONObject 내제이슨 = new JSONObject(제이슨바꿀스트링);
                        String 상대이메일 = 내제이슨.get("연결요청상대").toString();
                        String 제이슨바꿀상대스트링 = 쉐어드.getString(상대이메일, "");
                        JSONObject 상대제이슨 = new JSONObject(제이슨바꿀상대스트링);
                        내제이슨.put("처음만난날", 처음만난날);
                        내제이슨.put("받은연결요청", "false");
                        내제이슨.remove("연결대기");
                        내제이슨.remove("받은연결요청");
                        내제이슨.put("연결상대", 상대이메일);
                        상대제이슨.put("처음만난날", 처음만난날);
                        상대제이슨.put("연결요청상대", 나의이메일);
                        상대제이슨.put("연결대기", "false");
                        상대제이슨.put("받은연결요청", "true");
                        내제이슨.put("연결여부", "true");
                        String 내제이슨스트링 = 내제이슨.toString();
                        String 상대제이슨스트링 = 상대제이슨.toString();
                        쉐어드에디터.putString(나의이메일, 내제이슨스트링);
                        쉐어드에디터.putString(상대이메일, 상대제이슨스트링);
                        쉐어드에디터.apply();
                        Intent intent1 = new Intent(getApplicationContext(), Home.class);
                        intent1.putExtra("이메일", 나의이메일);
                        intent1.putExtra("연결상대", 상대이메일);
                        startActivity(intent1);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }//이름, 전번, 날짜 다 입력되면
            }
        });
    }


    DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                @SuppressLint("DefaultLocale")
                @Override
                public void onDateSet(DatePicker datePicker, int yy, int mm, int dd) {
                    날짜표시텍스트뷰.setText(String.format("%d-%d-%d", yy, mm + 1, dd));
                }
            };


}
