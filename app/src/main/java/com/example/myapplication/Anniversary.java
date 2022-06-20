package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Anniversary extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager myLayoutManager;
    Dialog 다이얼로그;
    Dialog 수정다이얼로그;

    Anniversaryadapter myAdapter;
    String 나의이메일;
    String 상대이메일;
    SharedPreferences 이벤트쉐어드;
    SharedPreferences.Editor 이벤트쉐어드에디터;
    ArrayList<Event> 넣어줄이벤트묶음 = new ArrayList<>();
    String 이벤트제이슨스트링;
    String 상대이벤트제이슨스트링;
    String 날짜;
    String 내용;
    String 처음만난날;
    JSONObject 내꺼제이슨;
    JSONObject 상대꺼제이슨;
    TextView 날짜입력텍스트뷰;
    TextView 이벤트날짜텍스트뷰;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);


        ActionBar ac = getSupportActionBar();
        ac.setTitle("기념일");
        Intent intent = getIntent();
        나의이메일 = intent.getStringExtra("나의이메일");
        상대이메일 = intent.getStringExtra("상대이메일");
        넣어줄이벤트묶음 = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        DatePickerDialog 날짜선택다이얼로그 = new DatePickerDialog(this, mDateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
        DatePickerDialog 날짜선택다이얼로그2 = new DatePickerDialog(this, mDateSetListener2, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));

        recyclerView = findViewById(R.id.알림리사이클러뷰);//리사이클러뷰 객체와 뷰를 연결
        myLayoutManager = new LinearLayoutManager(this); // 레이아웃 매니져를 LinearLayoutManager로 생성
        myAdapter = new Anniversaryadapter(넣어줄이벤트묶음, getApplicationContext());//어댑터 생성
        recyclerView.setLayoutManager(myLayoutManager);//리사이클러뷰의 레이아웃매니저 설정
        recyclerView.setAdapter(myAdapter);//리사이클러뷰의 어댑터 설정
        이벤트쉐어드 = getSharedPreferences("이벤트쉐어드프리퍼런스", MODE_PRIVATE);//데이터 가져올 쉐어드 선언
        이벤트쉐어드에디터 = 이벤트쉐어드.edit();

        
        try {

            JSONObject 이벤트제이슨 = new JSONObject(이벤트제이슨스트링);
            int 이벤트수 = 이벤트제이슨.getInt("이벤트수");
            for (int i = 1; i < 이벤트수 + 1; i++) {
                String 내용 = 이벤트제이슨.get("내용" + i).toString();
                String 날짜 = 이벤트제이슨.get("날짜" + i).toString();
                Event 이벤트 = new Event(날짜, 내용);
                넣어줄이벤트묶음.add(이벤트);
            }
            myAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }//이벤트 내용들 뿌려주는 코드들

        수정다이얼로그 = new Dialog(this);
        수정다이얼로그.requestWindowFeature(Window.FEATURE_NO_TITLE);
        수정다이얼로그.setContentView(R.layout.eventitemchange_dialog);
        myAdapter.setOnItemClickListener(new Anniversaryadapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                수정다이얼로그.show();
                Button 이벤트삭제버튼 = (Button) 수정다이얼로그.findViewById(R.id.이벤트삭제버튼);
                Button 내용수정버튼 = (Button) 수정다이얼로그.findViewById(R.id.내용수정버튼);
                EditText 내용표시에딧텍스트 = (EditText) 수정다이얼로그.findViewById(R.id.내용표시에딧텍스트);
                내용수정버튼.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String 수정할내용 = String.valueOf(내용표시에딧텍스트.getText());//이걸 누른 포지션에 넣어야 한다.
                        try {
                            내꺼제이슨 = new JSONObject(이벤트제이슨스트링);
                            상대꺼제이슨 = new JSONObject(상대이벤트제이슨스트링);
                            내꺼제이슨.put("내용" + (pos + 1), 수정할내용);
                            상대꺼제이슨.put("내용" + (pos + 1), 수정할내용);
                            String 나의제이슨객체스트링값 = 내꺼제이슨.toString();//내 제이슨을 스트링으로 변환
                            String 상대방제이슨객체스트링값 = 상대꺼제이슨.toString();
                            이벤트쉐어드에디터.putString(나의이메일, 나의제이슨객체스트링값);
                            이벤트쉐어드에디터.putString(상대이메일, 상대방제이슨객체스트링값);
                            이벤트쉐어드에디터.apply();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        myAdapter.notifyItemChanged(pos);
                        수정다이얼로그.dismiss();
                        finish();//인텐트 종료
                        overridePendingTransition(0, 0);//인텐트 효과 없애기
                        Intent intent = getIntent(); //인텐트
                        startActivity(intent); //액티비티 열기
                        overridePendingTransition(0, 0);//인텐트 효과 없애기
                    }
                });
                이벤트날짜텍스트뷰 = (TextView) 수정다이얼로그.findViewById(R.id.이벤트날짜텍스트뷰);
                이벤트날짜텍스트뷰.setText(넣어줄이벤트묶음.get(pos).날짜);
                내용표시에딧텍스트.setText(넣어줄이벤트묶음.get(pos).내용);
                Button 날짜수정버튼 = (Button) 수정다이얼로그.findViewById(R.id.이벤트날짜수정버튼);
                날짜수정버튼.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        날짜선택다이얼로그2.show();//캘린더 띄우는거 까지는 완료.
                    }
                });
                이벤트삭제버튼.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        넣어줄이벤트묶음.remove(pos);
                        try {
                            이벤트쉐어드에디터.clear();
                            내꺼제이슨 = new JSONObject();
                            상대꺼제이슨 = new JSONObject();
                            내꺼제이슨.put("이벤트수", 넣어줄이벤트묶음.size());
                            상대꺼제이슨.put("이벤트수", 넣어줄이벤트묶음.size());
                            for (int i = 0; i < 넣어줄이벤트묶음.size(); i++) {
                                String 내용 = 넣어줄이벤트묶음.get(i).내용;//0번부터 사진uri를 뽑음
                                String 날짜 = 넣어줄이벤트묶음.get(i).날짜;
                                내꺼제이슨.put("내용" + (i + 1), 내용);//뽑은 uri를 제이슨에 다시 넣음, 순서반영-순서가 바뀐 사진이 있으면 반영될것
                                내꺼제이슨.put("날짜" + (i + 1), 날짜);
                                상대꺼제이슨.put("내용" + (i + 1), 내용);//뽑은 uri를 제이슨에 다시 넣음, 순서반영-순서가 바뀐 사진이 있으면 반영될것
                                상대꺼제이슨.put("날짜" + (i + 1), 날짜);
                            }
                            String 나의제이슨객체스트링값 = 내꺼제이슨.toString();//내 제이슨을 스트링으로 변환
                            String 상대방제이슨객체스트링값 = 상대꺼제이슨.toString();
                            이벤트쉐어드에디터.putString(나의이메일, 나의제이슨객체스트링값);
                            이벤트쉐어드에디터.putString(상대이메일, 상대방제이슨객체스트링값);
                            이벤트쉐어드에디터.apply();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        myAdapter.notifyItemRemoved(pos);
                        myAdapter.notifyItemRangeChanged(pos, 넣어줄이벤트묶음.size());
                        수정다이얼로그.dismiss();
                    }
                });//이벤트삭제
                Button 닫기버튼 = (Button) 수정다이얼로그.findViewById(R.id.이벤트수정닫기);
                닫기버튼.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String 수정할날짜 = String.valueOf(이벤트날짜텍스트뷰.getText());//이걸 누른 포지션에 넣어야 한다.

                        try {
                            내꺼제이슨 = new JSONObject(이벤트제이슨스트링);
                            상대꺼제이슨 = new JSONObject(상대이벤트제이슨스트링);//제이슨 객체 만들기
                            내꺼제이슨.put("날짜" + (pos + 1), 수정할날짜);//수정할 날짜 넣어주기
                            상대꺼제이슨.put("날짜" + (pos + 1), 수정할날짜);//수정할 날짜 넣어주기기
                            String 나의제이슨객체스트링값 = 내꺼제이슨.toString();//내 제이슨을 스트링으로 변환
                            String 상대방제이슨객체스트링값 = 상대꺼제이슨.toString();
                            이벤트쉐어드에디터.putString(나의이메일, 나의제이슨객체스트링값);
                            이벤트쉐어드에디터.putString(상대이메일, 상대방제이슨객체스트링값);
                            이벤트쉐어드에디터.apply();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        myAdapter.notifyItemChanged(pos);
                        수정다이얼로그.dismiss();
                        finish();//인텐트 종료
                        overridePendingTransition(0, 0);//인텐트 효과 없애기
                        Intent intent = getIntent(); //인텐트
                        startActivity(intent); //액티비티 열기
                        overridePendingTransition(0, 0);//인텐트 효과 없애기
                    }
                });
            }//수정 다이얼로그 닫으면서 날짜 저장내용 바꿈
        });


        다이얼로그 = new Dialog(this);
        다이얼로그.setContentView(R.layout.activity_eventadddialog);
        EditText 내용입력에딧;

        Button 추가버튼, 취소버튼;
        날짜입력텍스트뷰 = (TextView) 다이얼로그.findViewById(R.id.날짜표시텍스트뷰);
        날짜입력텍스트뷰.setText(cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DATE));
        날짜입력텍스트뷰.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                날짜선택다이얼로그.show();
            }
        });


        내용입력에딧 = (EditText) 다이얼로그.findViewById(R.id.내용입력에딧텍스트);
        추가버튼 = (Button) 다이얼로그.findViewById(R.id.추가버튼);
        추가버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                날짜 = 날짜입력텍스트뷰.getText().toString();
                내용 = 내용입력에딧.getText().toString();
                Event 새이벤트 = new Event(날짜, 내용);
                넣어줄이벤트묶음.add(새이벤트);//리사이클러뷰에서 표시할 스트링 어레이에 이벤트 추가


                if (날짜 == null || 내용.equals("")) {
                    Toast.makeText(getApplicationContext(), "입력된 내용이 없습니다", Toast.LENGTH_SHORT).show();
                } else {
                    if (이벤트제이슨스트링.equals("_")) {
                        try {
                            JSONObject 이벤트제이슨 = new JSONObject();
                            JSONObject 상대이벤트제이슨 = new JSONObject();
                            이벤트제이슨.put("내용" + 넣어줄이벤트묶음.size(), 내용);//1개가 있고 거기서 1개 더 추가 한다면 사로 들어가는거는 2가 됨
                            //(사이즈가 2니까) , 2를 업애려면 포지션값(1)에서 1을 더한 2를 없애야 한다
                            이벤트제이슨.put("날짜" + 넣어줄이벤트묶음.size(), 날짜);
                            이벤트제이슨.put("이벤트수", 넣어줄이벤트묶음.size());
                            상대이벤트제이슨.put("내용" + 넣어줄이벤트묶음.size(), 내용);
                            상대이벤트제이슨.put("날짜" + 넣어줄이벤트묶음.size(), 날짜);
                            상대이벤트제이슨.put("이벤트수", 넣어줄이벤트묶음.size());
                            String 이벤트제이슨스트링2 = 이벤트제이슨.toString();//제이슨 데이터를 다시 스트링으로 바꿔줌ㅇ
                            String 이벤트제이슨스트링3 = 상대이벤트제이슨.toString();
                            이벤트쉐어드에디터.putString(나의이메일, 이벤트제이슨스트링2);
                            이벤트쉐어드에디터.putString(상대이메일, 이벤트제이슨스트링3);
                            이벤트쉐어드에디터.apply();
                            다이얼로그.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }//제이슨에 추가
                    } else {
                        try {
                            JSONObject 이벤트제이슨 = new JSONObject(이벤트제이슨스트링);
                            JSONObject 상대이벤트제이슨 = new JSONObject(상대이벤트제이슨스트링);
                            이벤트제이슨.put("내용" + 넣어줄이벤트묶음.size(), 내용);
                            이벤트제이슨.put("날짜" + 넣어줄이벤트묶음.size(), 날짜);
                            이벤트제이슨.put("이벤트수", 넣어줄이벤트묶음.size());
                            상대이벤트제이슨.put("내용" + 넣어줄이벤트묶음.size(), 내용);
                            상대이벤트제이슨.put("날짜" + 넣어줄이벤트묶음.size(), 날짜);
                            상대이벤트제이슨.put("이벤트수", 넣어줄이벤트묶음.size());
                            String 이벤트제이슨스트링2 = 이벤트제이슨.toString();//제이슨 데이터를 다시 스트링으로 바꿔줌ㅇ
                            String 이벤트제이슨스트링3 = 상대이벤트제이슨.toString();
                            이벤트쉐어드에디터.putString(나의이메일, 이벤트제이슨스트링2);
                            이벤트쉐어드에디터.putString(상대이메일, 이벤트제이슨스트링3);
                            이벤트쉐어드에디터.apply();
                            다이얼로그.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }//제이슨에 추가
                        myAdapter.notifyDataSetChanged();
                        다이얼로그.dismiss();
                    }
                }
            }
        });//일정추가


        취소버튼 = (Button) 다이얼로그.findViewById(R.id.취소버튼);
        취소버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                다이얼로그.dismiss();
            }
        });//일정추가 취소


        FloatingActionButton 이벤트추가버튼 = findViewById(R.id.이벤트추가버튼);//앨범에 사진을 추가하는 버튼을 만들고 연결
        이벤트추가버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                다이얼로그.show();
            }
        });//내용과 날짜를 추가하는 버튼
        myAdapter.notifyDataSetChanged();

        //Button 정렬버튼=(Button) findViewById(R.id.정렬버튼);


    }//온크리에이트

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }


    DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                @SuppressLint("DefaultLocale")
                @Override
                public void onDateSet(DatePicker datePicker, int yy, int mm, int dd) {
                    날짜입력텍스트뷰.setText(String.format("%d-%d-%d", yy, mm + 1, dd));
                }
            };

    DatePickerDialog.OnDateSetListener mDateSetListener2 =
            new DatePickerDialog.OnDateSetListener() {
                @SuppressLint("DefaultLocale")
                @Override
                public void onDateSet(DatePicker datePicker, int yy, int mm, int dd) {
                    이벤트날짜텍스트뷰.setText(String.format("%d-%d-%d", yy, mm + 1, dd));
                }
            };


    public void mOnClick_DatePick(View view) {

        // DATE Picker가 처음 떴을 때, 오늘 날짜가 보이도록 설정.

        Calendar cal = Calendar.getInstance();

        new DatePickerDialog(this, mDateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)).show();

    }

    public static String AddDate(String strDate, int year, int month, int day) throws Exception {

        SimpleDateFormat dtFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar cal = Calendar.getInstance();

        Date dt = dtFormat.parse(strDate);

        cal.setTime(dt);

        cal.add(Calendar.YEAR, year);
        cal.add(Calendar.MONTH, month);
        cal.add(Calendar.DATE, day);

        return dtFormat.format(cal.getTime());
    }


}


