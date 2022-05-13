package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;

public class Notification extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager myLayoutManager;
    private DatePickerDialog.OnDateSetListener callbackMethod;
    Dialog dialog;
    TextView 날짜텍스트뷰;
    Notificationadapter myAdapter;
    String ID;
    SharedPreferences 이벤트쉐어드;
    SharedPreferences.Editor 이벤트쉐어드에디터;
    ArrayList<Event> 넣어줄이벤트묶음;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        ActionBar ac = getSupportActionBar();
        ac.setTitle("기념일");
        Intent intent = getIntent();
        ID = intent.getStringExtra("ID");

        넣어줄이벤트묶음 = new ArrayList<>();

        이벤트쉐어드 = getSharedPreferences("이벤트쉐어드프리퍼런스", MODE_PRIVATE);//데이터 가져올 쉐어드 선언
        이벤트쉐어드에디터 = 이벤트쉐어드.edit();
        recyclerView = findViewById(R.id.알림리사이클러뷰);//리사이클러뷰 객체와 뷰를 연결
        myLayoutManager = new LinearLayoutManager(this); // 레이아웃 매니져를 LinearLayoutManager로 생성
        myAdapter = new Notificationadapter(넣어줄이벤트묶음);//어댑터 생성
        recyclerView.setLayoutManager(myLayoutManager);//리사이클러뷰의 레이아웃매니저 설정
        recyclerView.setAdapter(myAdapter);//리사이클러뷰의 어댑터 설정
        myAdapter.setOnItemClickListener(new Notificationadapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                TextView 이벤트삭제버튼=(TextView) findViewById(R.id.이벤트삭제버튼);
                이벤트삭제버튼.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        넣어줄이벤트묶음.remove(pos);
                        myAdapter.notifyItemRemoved(pos);
                        myAdapter.notifyItemRangeChanged(pos, 넣어줄이벤트묶음.size());
                        Toast.makeText(getApplicationContext(),(pos+1)+"번째 사진 삭제",Toast.LENGTH_SHORT).show();
                        String 제이슨스트링값 = 이벤트쉐어드.getString(ID, "");
                        try {
                            JSONObject 내꺼제이슨 = new JSONObject(제이슨스트링값);
                            내꺼제이슨.remove("내용"+(pos+1));
                            내꺼제이슨.remove("날짜"+(pos+1));
                            내꺼제이슨.put("이벤트수",넣어줄이벤트묶음.size());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });


        String 이벤트제이슨스트링 = 이벤트쉐어드.getString(ID, " ");

        if (이벤트제이슨스트링 != null) {
            try {
                JSONObject 이벤트제이슨 = new JSONObject(이벤트제이슨스트링);
                int 이벤트수 = 이벤트제이슨.getInt("이벤트수");
                for (int i = 0; i < 이벤트수; i++) {
                    String 내용 = 이벤트제이슨.get("내용" + (i + 1)).toString();
                    String 날짜 = 이벤트제이슨.get("날짜" + (i + 1)).toString();
                    Event 이벤트 = new Event(내용, 날짜);
                    넣어줄이벤트묶음.add(이벤트);
                }
                myAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(),"저장된이벤트 없음",Toast.LENGTH_SHORT).show();
        }


        dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_eventadddialog);
        EditText 날짜입력에딧, 내용입력에딧;
        Button 추가버튼, 취소버튼;
        날짜입력에딧 = (EditText) dialog.findViewById(R.id.날짜표시텍스트뷰);
        내용입력에딧 = (EditText) dialog.findViewById(R.id.내용표시텍스트뷰);
        추가버튼 = (Button) dialog.findViewById(R.id.추가버튼);
        추가버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String 날짜 = 날짜입력에딧.getText().toString();
                String 내용 = 내용입력에딧.getText().toString();
                Event 새이벤트 = new Event(날짜, 내용);
                넣어줄이벤트묶음.add(새이벤트);

                try {
                    JSONObject 이벤트제이슨 = new JSONObject(이벤트제이슨스트링);
                    이벤트제이슨.put("내용" + 넣어줄이벤트묶음.size(), 내용);
                    이벤트제이슨.put("날짜" + 넣어줄이벤트묶음.size(), 날짜);
                    이벤트제이슨.put("이벤트수", 넣어줄이벤트묶음.size());
                    String 이벤트제이슨스트링 = 이벤트제이슨.toString();
                    이벤트쉐어드에디터.putString(ID, 이벤트제이슨스트링);
                    이벤트쉐어드에디터.apply();
                } catch (JSONException e) {
                    e.printStackTrace();
                }//제이슨에 추가


                myAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        취소버튼 = (Button) dialog.findViewById(R.id.취소버튼);
        취소버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        FloatingActionButton 이벤트추가버튼 = findViewById(R.id.이벤트추가버튼);//앨범에 사진을 추가하는 버튼을 만들고 연결
        이벤트추가버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });//내용과 날짜를


        myAdapter.notifyDataSetChanged();
    }

}


