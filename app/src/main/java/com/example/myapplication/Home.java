package com.example.myapplication;


import androidx.appcompat.app.AppCompatActivity;


import android.app.Activity;
import android.app.Dialog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


public class Home extends AppCompatActivity {

    Dialog dialog01;//좌상단 버튼 누를 때
    Dialog dialog02;//우상단 버튼 누를 때
    Button 전화버튼;
    Button 전화버튼2;
    ImageButton 앨범버튼;
    ImageButton 알림버튼;
    ImageButton 옵션버튼;
    Intent 전화번호입력인텐트;
    SharedPreferences 쉐어드프리퍼런스;
    SharedPreferences.Editor 쉐어드에디터;
    String 전화번호;

//만약 전화번호 바꾸는 인텐트를 받으면 그 다이얼로그를 띄우는거까지 해줘


    @Override
    protected void onCreate(Bundle savedInstanceState) {//최초 빌드때 실행

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        쉐어드프리퍼런스 = getSharedPreferences("쉐어드프리퍼런스", Activity.MODE_PRIVATE);
        쉐어드에디터 = 쉐어드프리퍼런스.edit();
        전화번호 = 쉐어드프리퍼런스.getString("MyStr", "_");

        dialog01 = new Dialog(this);
        dialog01.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog01.setContentView(R.layout.activity_dialog);

        findViewById(R.id.imageButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog01(); // 아래 showDialog01() 함수 호출
            }
        });

        TextView 프로필텍스트뷰 = (TextView) dialog01.findViewById(R.id.textView);//다이얼로그 레이아웃의 텍스트뷰 연결
        프로필텍스트뷰.setText("배준호\n1993년 7월 9일\nppow1123@naver.com");//프로필 있는 텍스트박스1

        //전화번호 있는 버튼
        전화버튼 = (Button) dialog01.findViewById(R.id.callbtn); //다이얼로그 1의 전화버튼 생성, 연결
        전화버튼.setText(String.valueOf(전화번호));

        Button 번호변경버튼 = (Button) dialog01.findViewById(R.id.numberchangebtn);//다이얼로그1의 번호변경 버튼 객체를 생성하고 다이얼로그의 버튼뷰를 찾은 다음 이 둘을 연결
        번호변경버튼.setOnClickListener(new View.OnClickListener() {//연결한 버튼의 동작을 정의
            @Override
            public void onClick(View view) {
                전화번호입력인텐트 = new Intent(getApplicationContext(), Numberchange.class);
                전화번호입력인텐트.putExtra("번호변경인텐트", "10");//인텐트를 생성
                startActivityForResult(전화번호입력인텐트, 1);
            }
        });//전화번호 바꾸는 액티비티 불러오는 버튼

        dialog02 = new Dialog(this);
        dialog02.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog02.setContentView(R.layout.activity_dialog2);
        findViewById(R.id.imageButton2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog02(); // 아래 showDialog02() 함수 호출
            }
        });


        TextView 프로필텍스트뷰2 = (TextView) dialog02.findViewById(R.id.textView2);//다이얼로그 레이아웃의 텍스트뷰 연결
        프로필텍스트뷰2.setText("배요한\n1994년 2월 9일\naa4444x@naver.com");//프로필 있는 텍스트박스2

        전화버튼2 = (Button) dialog02.findViewById(R.id.callbtn2);

        Button 번호변경버튼2 = (Button) dialog02.findViewById(R.id.numberchangebtn2);
        번호변경버튼2.setOnClickListener(new View.OnClickListener() {//연결한 버튼의 동작을 정의
            @Override
            public void onClick(View view) {
                전화번호입력인텐트 = new Intent(getApplicationContext(), Numberchange.class);
                전화번호입력인텐트.putExtra("번호변경인텐트", "20");//인텐트를 생성
                startActivityForResult(전화번호입력인텐트, 2);
            }
        });//전화번호 바꾸는 액티비티 불러오는 버튼

        앨범버튼 = (ImageButton) findViewById(R.id.imageButton3);
        앨범버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Album.class);
                startActivity(intent);
            }
        });//앨범 액티비티 실행하기

        알림버튼 = (ImageButton) findViewById(R.id.imageButton4);
        알림버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Notification.class);
                startActivity(intent);
            }
        });//알림 액티비티 실행하기

        옵션버튼 = (ImageButton) findViewById(R.id.imageButton5);
        옵션버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Options.class);
                startActivity(intent);
            }
        });//옵션 액티비티 실행하기

        String json = "";
        try {
            InputStream is = getAssets().open("jsons/test.json"); // json파일 이름
            int fileSize = is.available();

            byte[] buffer = new byte[fileSize];
            is.read(buffer);
            is.close();

            //json파일명을 가져와서 String 변수에 담음
            json = new String(buffer, "UTF-8");
            Log.d("--  json = ", json);


            JSONObject jsonObject = new JSONObject(json);

            //배열로된 자료를 가져올때
            JSONArray Array = jsonObject.getJSONArray("food");//배열의 이름
            for (int i = 0; i < Array.length(); i++) {
                JSONObject Object = Array.getJSONObject(i);
                Log.d("--  name is ", Object.getString("name"));
                Log.d("--  materials are", Object.getString("Material"));
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    // dialog01을 디자인하는 함수
    public void showDialog01() {
        dialog01.show(); // 다이얼로그 띄우는 메소드 호출
    }

    public void showDialog02() {
        dialog02.show(); // 다이얼로그 띄우는 메소드 호출
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Toast.makeText(this, "온크리에이트", Toast.LENGTH_SHORT).show();//상태 확인용 토스트

    }

    @Override
    protected void onStop() {
        super.onStop();

//        Toast.makeText(this, "온스탑", Toast.LENGTH_SHORT).show();


    }

    @Override
    protected void onPause() {
        super.onPause();
//        Toast.makeText(this, "온퍼즈", Toast.LENGTH_SHORT).show();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Toast.makeText(this, "온디스트로이", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onRestart() {
        super.onRestart();


//        Toast.makeText(this, "온리스타트", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent 받은인텐트) {
        super.onActivityResult(requestCode, resultCode, 받은인텐트);
        if (requestCode == 1 && resultCode == 1) {
            String 결과 = 받은인텐트.getStringExtra("보낼번호");//인텐트에 담았던 값은 putextra 할 때 정한 이름(키값)을 넣어야 그 내용물(밸류)을 얻을 수 있다.
            전화버튼.setText(결과);
            전화번호=결과;
            쉐어드에디터.putString("MyStr",결과);
            쉐어드에디터.apply();
            전화버튼.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + 결과)));
                }
            });
        } else if (requestCode == 2 && resultCode == 2) {

            String 결과 = 받은인텐트.getStringExtra("보낼번호");//putextra 할 때 정한 이름(키값)을 넣어야 내용물(밸류)을 얻을 수 있다.
            전화버튼2.setText(결과);

            전화버튼2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + 결과)));
                }
            });

        }
    }//w
}









