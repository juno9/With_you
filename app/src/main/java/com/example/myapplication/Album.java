package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.naver.maps.map.NaverMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;


public class Album extends AppCompatActivity {



    ArrayList<MyData> mData = new ArrayList<>();     // 이미지의 uri를 담을 ArrayList 객체
    RecyclerView recyclerView;  // 이미지를 보여줄 리사이클러뷰
    MultiImageAdapter adapter;  // 리사이클러뷰에 적용시킬 어댑터
    int 포지션값 = 0;
    Dialog 다이얼로그;
    SharedPreferences 앨범쉐어드;
    SharedPreferences.Editor 앨범쉐어드에디터;
    String 나의이메일;
    String 상대이메일;
    JSONObject 나의제이슨객체;
    JSONObject 상대제이슨객체;
    JSONObject 내꺼제이슨;
    JSONObject 상대꺼제이슨;
    Button 지도로보기버튼;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        ActionBar ac = getSupportActionBar();
        ac.setTitle("앨범");
        Intent intent = getIntent();
        나의이메일 = intent.getStringExtra("나의이메일");
        상대이메일 = intent.getStringExtra("상대이메일");//인텐트에 담아보낸 이메일들을 먼저 받음

        앨범쉐어드 = getSharedPreferences("앨범쉐어드프리퍼런스", MODE_PRIVATE);//데이터 가져올 쉐어드 선언
        앨범쉐어드에디터 = 앨범쉐어드.edit();//선언한 쉐어드의 에디터 선언
//        앨범쉐어드에디터.clear();
//        앨범쉐어드에디터.apply();

        Calendar cal = Calendar.getInstance();
        String 나의제이슨스트링 = 앨범쉐어드.getString(나의이메일, "_");//앨범쉐어드 내에 나의이메일를 키값으로 가지는 데이터를 스트링으로 불러옴
        String 상대제이슨스트링 = 앨범쉐어드.getString(상대이메일, "_");


        if (나의제이슨스트링.equals("_")) {
            Toast.makeText(getApplicationContext(), "저장된 사진이 없습니다.", Toast.LENGTH_SHORT).show();
            내꺼제이슨 = new JSONObject();
            상대꺼제이슨 = new JSONObject();
            try {
                내꺼제이슨.put("사진갯수", "0");
                상대꺼제이슨.put("사진갯수", "0");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String 생성할제이슨스트링 = 내꺼제이슨.toString();
            String 생성할상대제이슨스트링 = 내꺼제이슨.toString();

            앨범쉐어드에디터.putString(나의이메일, 생성할제이슨스트링);
            앨범쉐어드에디터.putString(상대이메일, 생성할상대제이슨스트링);
            앨범쉐어드에디터.apply();

        } else {//쉐어드에 저장된 제이슨이 있다면
            try {
                나의제이슨객체 = new JSONObject(나의제이슨스트링);//불러온 스트링형태의 제이슨 데이터를 제이슨으로 다시 변환
                상대제이슨객체 = new JSONObject(상대제이슨스트링);

                int 사진갯수 = 나의제이슨객체.getInt("사진갯수");
                if (사진갯수 == 0) {
                    Toast.makeText(getApplicationContext(), "저장된 사진이 없습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < 사진갯수; i++) {
                        String uristring = (String) 나의제이슨객체.getString(i + 1 + "번째사진");
                        MyData mydata = new MyData(uristring, null);
                        mData.add(mydata);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        //저장되어 있는 사진 뿌려줘야한다./사진을 어떻게 저장할지 정하고 저장되는지까지 보자


        recyclerView = findViewById(R.id.앨범리사이클러뷰);//리사이클러뷰 레이아웃과 클래스의 리사이클러뷰를 연결
        FloatingActionButton btn_getImage = findViewById(R.id.floatAingActionButton);//앨범에 사진을 추가하는 버튼을 만들고 연결
        btn_getImage.setOnClickListener(new View.OnClickListener() {//이 버튼을 누르면 어떤 행동을 하게 될지 정의
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.putExtra("나의이메일", 나의이메일);
                intent.putExtra("상대이메일", 상대이메일);
                intent.setType("image/*");
                startActivityForResult(intent, 2222);
            }
        });
        지도로보기버튼 = (Button) findViewById(R.id.지도로보기버튼);
        지도로보기버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Photomap.class);
                intent.putExtra("나의이메일", 나의이메일);
                intent.putExtra("상대이메일", 상대이메일);
                startActivity(intent);
                finish();
            }
        });
        adapter = new MultiImageAdapter(mData, getApplicationContext());//어댑터 생성
        다이얼로그 = new Dialog(this);
        다이얼로그.requestWindowFeature(Window.FEATURE_NO_TITLE);
        다이얼로그.setContentView(R.layout.itemchange_dialog);
        adapter.setOnItemClickListener(new MultiImageAdapter.OnItemClickListener() {
            @SuppressLint("IntentReset")
            @Override
            public void onItemClick(int pos) {
                다이얼로그.show();
                Button 사진삭제버튼 = (Button) 다이얼로그.findViewById(R.id.삭제버튼);
                사진삭제버튼.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mData.remove(pos);
                        try {
                            앨범쉐어드에디터.clear();
                            내꺼제이슨 = new JSONObject();
                            상대꺼제이슨 = new JSONObject();
                            내꺼제이슨.put("사진갯수", mData.size());//변경된 갯수를 반영하여 사진갯수도 재정의
                            상대꺼제이슨.put("사진갯수", mData.size());//변경된 갯수를 반영하여 사진갯수도 재정의
                            for (int i = 0; i < mData.size(); i++) {
                                String 사진uri = mData.get(i).imageString;//0번부터 사진uri를 뽑음
                                내꺼제이슨.put(i + 1 + "번째사진", 사진uri);//뽑은 uri를 제이슨에 다시 넣음, 순서반영-순서가 바뀐 사진이 있으면 반영될것
                                상대꺼제이슨.put(i + 1 + "번째사진", 사진uri);
                            }
                            String 나의제이슨객체스트링값 = 내꺼제이슨.toString();//내 제이슨을 스트링으로 변환
                            String 상대방제이슨객체스트링값 = 상대꺼제이슨.toString();
                            앨범쉐어드에디터.putString(나의이메일, 나의제이슨객체스트링값);
                            앨범쉐어드에디터.putString(상대이메일, 상대방제이슨객체스트링값);
                            앨범쉐어드에디터.apply();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }//
                        adapter.notifyItemRemoved(pos);
                        adapter.notifyItemRangeChanged(pos, mData.size());
                        다이얼로그.dismiss();
                    }
                });
                Button 사진가져오기버튼 = (Button) 다이얼로그.findViewById(R.id.사진수정버튼);
                사진가져오기버튼.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        포지션값 = pos;
                        Toast.makeText(getApplicationContext(), (pos + 1) + "번째 아이템 선택", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.putExtra("나의이메일", 나의이메일);
                        intent.putExtra("상대이메일", 상대이메일);
                        intent.setType("image/*");
                        startActivityForResult(intent, 3333);
                        다이얼로그.dismiss();
                    }
                });


            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    // 갤러리앱에서 사진 선택하면 돌아오는 메소드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2222) {
            if (data == null) {
                Toast.makeText(getApplicationContext(), "사진을 선택하지 않았습니다", Toast.LENGTH_SHORT).show();
            } else {
                Uri uri = data.getData();//인텐트에 실려온 uri값을 꺼내줌
                String uristr=uri.toString();
                Intent intent = new Intent(getApplicationContext(), Photoinput.class);
                intent.putExtra("uri", uristr);
                intent.putExtra("나의이메일", 나의이메일);
                intent.putExtra("상대이메일", 상대이메일);
                intent.putExtra("띄울화면","앨범");
                startActivity(intent);
                finish();

            }
        } else if (requestCode == 3333) {
            if (data == null) {
                Toast.makeText(getApplicationContext(), "사진을 선택하지 않았습니다", Toast.LENGTH_SHORT).show();
            } else {
                Uri uri = data.getData();//인텐트에 실려온 uri값을 꺼내줌
                String uristr=uri.toString();
                Intent intent = new Intent(getApplicationContext(), Photoinput.class);
                intent.putExtra("uri",uristr);
                intent.putExtra("나의이메일", 나의이메일);
                intent.putExtra("상대이메일", 상대이메일);
                intent.putExtra("띄울화면","앨범");

                startActivity(intent);
                finish();
            }
        }
    }










}

