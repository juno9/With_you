package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

public class Loading extends AppCompatActivity {

    int 이벤트수;
    JSONObject 이벤트제이슨;
    Thread thread;
    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);//레이아웃과 연결

        Intent intent = getIntent();
        String 나의이메일 = intent.getStringExtra("나의이메일");//쉐어드에 저장된 내 ID
        String 상대이메일 = intent.getStringExtra("상대이메일");//연결된 상대의 ID
        String 처음만난날 = intent.getStringExtra("처음만난날");
        TextView 설명텍스트, 갯수텍스트;
        설명텍스트 = (TextView) findViewById(R.id.안내텍스트뷰);
        갯수텍스트 = (TextView) findViewById(R.id.갯수텍스트뷰);

        설명텍스트.setText("저장된 일정 수");
        SharedPreferences 이벤트쉐어드프리퍼런스 = getSharedPreferences("이벤트쉐어드프리퍼런스", MODE_PRIVATE);
        SharedPreferences.Editor 이벤트쉐어드에디터 = 이벤트쉐어드프리퍼런스.edit();
        String eventjsnstr = 이벤트쉐어드프리퍼런스.getString(나의이메일, "_");


        try {
            if (eventjsnstr.equals("_")) {//저장된 이벤트가 하나도 없으면
                이벤트제이슨 = new JSONObject();
                이벤트제이슨.put("이벤트수", "0");
                String 이벤트제이슨스트링 = 이벤트제이슨.toString();
                이벤트쉐어드에디터.putString(나의이메일, 이벤트제이슨스트링);
                이벤트쉐어드에디터.putString(상대이메일, 이벤트제이슨스트링);
                이벤트쉐어드에디터.apply();
                이벤트수 = 0;
                Intent intent1 = new Intent(getApplicationContext(), Anniversary.class);
                intent1.putExtra("나의이메일", 나의이메일);
                intent1.putExtra("상대이메일", 상대이메일);
                intent1.putExtra("처음만난날", 처음만난날);
                startActivity(intent1);
                finish();
            } else {
                이벤트제이슨 = new JSONObject(eventjsnstr);
                String 이벤트갯수스트링 = 이벤트제이슨.get("이벤트수").toString();
                이벤트수 = Integer.parseInt(이벤트갯수스트링);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                갯수텍스트.setText(Integer.toString(msg.what));

                thread.interrupt();
                Intent intent1 = new Intent(getApplicationContext(), Anniversary.class);
                intent1.putExtra("나의이메일", 나의이메일);
                intent1.putExtra("상대이메일", 상대이메일);
                intent1.putExtra("처음만난날", 처음만난날);
                startActivity(intent1);
                finish();

            }
        };//핸들러는 스레드에서 받은 메시지에 따라 뷰를 바꿔줌

        thread = new Thread() {//여기서는 백그라운드에서 돌아갈 작업을 정의한다.
            public void run() {
                try {
                    for (int i = 0; i < 이벤트수 + 1; i++) {
                        Message msg = handler.obtainMessage();//핸들러로 보낼 메시지 객체 생성
                        msg.what = i;//메시지의 what을 1로 설정
                        handler.sendMessage(msg);//핸들러에게 메시지를 보낸다
                        sleep(300);//0.3초
                    }
                }//백그라운드 스레드(앱과 별개로 따로 돌아가고 있다.)
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();//스레드스타트
    }


}
