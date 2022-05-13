package com.example.myapplication;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;

import android.app.Dialog;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


public class Home extends AppCompatActivity {

    Dialog dialog01;//좌상단 버튼 누를 때
    Dialog dialog02;//우상단 버튼 누를 때
    Button 전화버튼;
    ImageButton 앨범버튼;
    ImageButton 알림버튼;
    ImageButton 옵션버튼;
    TextView 만난날짜텍스트뷰;
    ImageView 광고이미지뷰;
    Intent 전화번호입력인텐트;
    SharedPreferences 쉐어드프리퍼런스;
    SharedPreferences.Editor 쉐어드에디터;
    String 전화번호;
    String ID;
    String 상대ID;
    JSONObject jsonObject;
    JSONObject partnerjsonObject;


//만약 전화번호 바꾸는 인텐트를 받으면 그 다이얼로그를 띄우는거까지 해줘


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {//최초 빌드때 실행
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ActionBar ac = getSupportActionBar();
        ac.setTitle("홈");

        Intent intent = getIntent();
        ID = intent.getStringExtra("ID");//쉐어드에 저장된 내 ID
        상대ID = intent.getStringExtra("연결상대");//연결된 상대의 ID
        쉐어드프리퍼런스 = getSharedPreferences("회원정보쉐어드프리퍼런스", MODE_PRIVATE);
        쉐어드에디터 = 쉐어드프리퍼런스.edit();


        String userjsnstr = 쉐어드프리퍼런스.getString(ID, "_");//회원정보 쉐어드 내에 ID를 키값으로 가진 데이터를 스트링으로 불러옴
        String partnerjsnstr = 쉐어드프리퍼런스.getString(상대ID, "_");//회원정보 쉐어드 내에 상대방ID를 키값으로 가진 데이터를 스트링으로 불러옴


        try {
            jsonObject = new JSONObject(userjsnstr);//스트링으로 저장되어 있는 제이슨 데이터를 참조하여 제이슨객체 생성
            partnerjsonObject = new JSONObject(partnerjsnstr);//스트링으로 저장되어 있는 제이슨 데이터를 참조하여 제이슨객체 생성

        } catch (JSONException e) {
            e.printStackTrace();
        }


        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    Glide.with(getApplicationContext()).load(R.drawable.image1).centerCrop().into(광고이미지뷰);
                } else if (msg.what == 2) {
                    Glide.with(getApplicationContext()).load(R.drawable.image2).centerCrop().into(광고이미지뷰);
                } else if (msg.what == 3) {
                    Glide.with(getApplicationContext()).load(R.drawable.image3).centerCrop().into(광고이미지뷰);
                } else if (msg.what == 4) {
                    Glide.with(getApplicationContext()).load(R.drawable.image4).centerCrop().into(광고이미지뷰);
                }
            }
        };//핸들러는 스레드에서 받은 메시지에 따라 뷰에 이미지를 그려줌

        Thread thread = new Thread() {//여기서는 백그라운드에서 돌아갈 작업을 정의한다.
            public void run() {
                try {
                    for (int i = 0; i < 4; i++) {//여기 트루 대신 변수 하나 넣고 다른 액티비티 실행하면 멈추게 만들면 될거같다.
                        Message msg = handler.obtainMessage();//핸들러로 보낼 메시지 객체 생성
                        msg.what = i + 1;//메시지의 what을 1로 설정
                        handler.sendMessage(msg);//핸들러에게 메시지를 보낸다
                        sleep(4000);//3초간 스레드 멈춤
                        if (i == 3) {
                            i = 0;
                        }
                    }
                }//백그라운드 스레드(앱과 별개로 따로 돌아가고 있다.)
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();//스레드스타트





        Handler alarmhandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

            }
        };

        //home액티비티 oncreate시에 스레드 돌기 시작
        Thread alarmthread = new Thread() {//여기서는 백그라운드에서 돌아갈 작업을 정의한다.
            public void run() {
                try {


                }//백그라운드 스레드(앱과 별개로 따로 돌아가고 있다.)
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };


        광고이미지뷰 = (ImageView) findViewById(R.id.배너이미지뷰);


        dialog01 = new Dialog(this);//다이얼로그1에는 상대의 정보가 있어야 한다.
        dialog01.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog01.setContentView(R.layout.activity_dialog);
        findViewById(R.id.imageButton).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDialog01(); // 아래 showDialog01() 함수 호출
                    }
                });

        만난날짜텍스트뷰 = (TextView) findViewById(R.id.textView);
        만난날짜텍스트뷰.setText(intent.getStringExtra("처음사귄날"));


        TextView 프로필텍스트뷰 = (TextView) dialog01.findViewById(R.id.textView);//다이얼로그 레이아웃의 텍스트뷰 연결
        try {
            프로필텍스트뷰.setText(partnerjsonObject.getString("이름") + "\n" + partnerjsonObject.getString("이메일"));//프로필 있는 텍스트박스1
        } catch (
                JSONException e) {
            e.printStackTrace();
        }

        //전화번호 있는 버튼
        전화버튼 = (Button) dialog01.findViewById(R.id.callbtn); //다이얼로그 1의 전화버튼 생성, 연결
        try {
            전화버튼.setText(partnerjsonObject.getString("전화번호"));//밸류값은 상대방의 전화번호
        } catch (
                JSONException e) {
            e.printStackTrace();
        }

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

        findViewById(R.id.imageButton2).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDialog02(); // 아래 showDialog02() 함수 호출
                    }
                });

        TextView 프로필텍스트뷰2 = (TextView) dialog02.findViewById(R.id.textView2);//다이얼로그 레이아웃의 텍스트뷰 연결
        try {
            프로필텍스트뷰2.setText(jsonObject.getString("이름") + "\n" + jsonObject.getString("이메일"));//프로필 있는 텍스트박스1
        } catch (
                JSONException e) {
            e.printStackTrace();
        }


        앨범버튼 = (ImageButton) findViewById(R.id.imageButton3);
        앨범버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Album.class);
                intent.putExtra("ID", ID);
                intent.putExtra("상대ID", 상대ID);
                startActivity(intent);
            }
        });//앨범 액티비티 실행하기

        알림버튼 = (ImageButton) findViewById(R.id.알림버튼);
        알림버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Notification.class);
                intent.putExtra("ID",ID);
                startActivity(intent);
            }
        });//알림 액티비티 실행하기

        옵션버튼 = (ImageButton)
                findViewById(R.id.imageButton5);
        옵션버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Options.class);
                startActivity(intent);
            }
        });//옵션 액티비티 실행하기
    }

    public void showDialog01() {
        dialog01.show(); // 다이얼로그 띄우는 메소드 호출
    }

    public void showDialog02() {
        dialog02.show(); // 다이얼로그 띄우는 메소드 호출
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent 받은인텐트) {
        super.onActivityResult(requestCode, resultCode, 받은인텐트);
        if (requestCode == 1 && resultCode == 1) {
            String 결과 = 받은인텐트.getStringExtra("보낼번호");//인텐트에 담았던 값은 putextra 할 때 정한 이름(키값)을 넣어야 그 내용물(밸류)을 얻을 수 있다.
            전화버튼.setText(결과);
            전화번호 = 결과;
            try {
                jsonObject.put("전화번호", 결과);//72열에서 불러온 제이슨 객체의 전화번호키값에 새로운 밸류값을 넣어줌
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String jsnstr2 = jsonObject.toString();//새로운 전화번호를 넣은 제이슨 데이터를 스트링으로 변환.
            쉐어드에디터.putString(ID, jsnstr2);//쉐어드 내에 ID를 키값으로가지고 225열의 스트링을 밸류값으로 저장
            쉐어드에디터.apply();//에디터에 변경사항 적용
            전화버튼.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + 결과)));
                }
            });
        }
    }
}









