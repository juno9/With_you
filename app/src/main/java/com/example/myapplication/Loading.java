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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);//레이아웃과 연결

        Intent intent = getIntent();
        String ID = intent.getStringExtra("ID");//쉐어드에 저장된 내 ID
        String 상대ID = intent.getStringExtra("연결상대");//연결된 상대의 ID
        TextView 설명텍스트, 갯수텍스트;
        설명텍스트 = (TextView) findViewById(R.id.안내텍스트뷰);
        갯수텍스트 = (TextView) findViewById(R.id.갯수텍스트뷰);

        설명텍스트.setText("저장된 일정 수");
        SharedPreferences 이벤트쉐어드프리퍼런스 = getSharedPreferences("이벤트쉐어드프리퍼런스", MODE_PRIVATE);
        SharedPreferences.Editor 이벤트쉐어드에디터 = 이벤트쉐어드프리퍼런스.edit();
        String eventjsnstr = 이벤트쉐어드프리퍼런스.getString(ID, "_");
        try {
            JSONObject eventjsonObject = new JSONObject(eventjsnstr);
            이벤트수 = (int) eventjsonObject.get("이벤트수");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                    갯수텍스트.setText(Integer.toString(msg.what));
                    if(msg.what==이벤트수){
                        Intent intent1 = new Intent(getApplicationContext(), Notification.class);
                        intent1.putExtra("ID", ID);
                        intent1.putExtra("상대ID", 상대ID);
                        startActivity(intent1);
                        finish();
                    }

            }
        };//핸들러는 스레드에서 받은 메시지에 따라 뷰를 바꿔줌


        Thread thread = new Thread() {//여기서는 백그라운드에서 돌아갈 작업을 정의한다.
            public void run() {
                try {
                    for (int i = 0; i < 이벤트수; i++) {
                        Message msg = handler.obtainMessage();//핸들러로 보낼 메시지 객체 생성
                        msg.what = i + 1;//메시지의 what을 1로 설정
                        handler.sendMessage(msg);//핸들러에게 메시지를 보낸다
                        sleep(300);//0.7초
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