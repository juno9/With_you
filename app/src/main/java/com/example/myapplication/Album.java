package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Album extends AppCompatActivity {


    private static final String TAG = "MultiImageActivity";
    ArrayList<MyData> mData = new ArrayList<>();     // 이미지의 uri를 담을 ArrayList 객체
    RecyclerView recyclerView;  // 이미지를 보여줄 리사이클러뷰
    MultiImageAdapter adapter;  // 리사이클러뷰에 적용시킬 어댑터
    int 포지션값 = 0;
    Dialog 다이얼로그;
    SharedPreferences 앨범쉐어드;
    SharedPreferences.Editor 앨범쉐어드에디터;
    SharedPreferences 광고쉐어드;
    SharedPreferences.Editor 광고쉐어드에디터;
    String 내ID;
    String 상대ID;
    JSONObject 나의제이슨객체;
    JSONObject 내꺼제이슨;
    JSONObject 상대꺼제이슨;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        ActionBar ac = getSupportActionBar();
        ac.setTitle("앨범");
        Intent intent = getIntent();
        내ID = intent.getStringExtra("ID");
        상대ID = intent.getStringExtra("상대ID");//인텐트에 담아보낸 ID들을 먼저 받음

        앨범쉐어드 = getSharedPreferences("앨범쉐어드프리퍼런스", MODE_PRIVATE);//데이터 가져올 쉐어드 선언
        앨범쉐어드에디터 = 앨범쉐어드.edit();//선언한 쉐어드의 에디터 선언
        광고쉐어드 = getSharedPreferences("광고쉐어드프리퍼런스", MODE_PRIVATE);//데이터 가져올 쉐어드 선언
        광고쉐어드에디터 = 광고쉐어드.edit();//선언한 쉐어드의 에디터 선언
//        앨범쉐어드에디터.clear();
//        앨범쉐어드에디터.apply();

        String 유저제이슨스트링 = 앨범쉐어드.getString(내ID, "");//앨범쉐어드 내에 내ID를 키값으로 가지는 데이터를 스트링으로 불러옴
       try {
            나의제이슨객체 = new JSONObject(유저제이슨스트링);//불러온 스트링형태의 제이슨 데이터를 제이슨으로 다시 변환
            JSONObject 광고제이슨객체 = new JSONObject();
            int 사진갯수 = 나의제이슨객체.getInt("사진갯수");

            for (int i = 1; i < 사진갯수 + 1; i++) {
                String uristring = (String) 나의제이슨객체.getString(i + "번째사진");
                MyData mydata = new MyData(uristring, null);
                mData.add(mydata);
                광고제이슨객체.put(i + "번째사진", uristring);
            }
            String 광고제이슨스트링 = 광고제이슨객체.toString();
            광고쉐어드에디터.putString(내ID, 광고제이슨스트링);
            광고쉐어드에디터.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        광고쉐어드에디터.apply();

        //저장되어 있는 사진 뿌려줘야한다./사진을 어떻게 저장할지 정하고 저장되는지까지 보자


        recyclerView = findViewById(R.id.앨범리사이클러뷰);//리사이클러뷰 레이아웃과 클래스의 리사이클러뷰를 연결
        FloatingActionButton btn_getImage = findViewById(R.id.floatAingActionButton);//앨범에 사진을 추가하는 버튼을 만들고 연결
        btn_getImage.setOnClickListener(new View.OnClickListener() {//이 버튼을 누르면 어떤 행동을 하게 될지 정의

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("내ID", 내ID);
                intent.putExtra("상대ID", 상대ID);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 2222);

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
                EditText 날짜입력 = (EditText) 다이얼로그.findViewById(R.id.날짜입력에딧텍스트);
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
                            앨범쉐어드에디터.putString(내ID, 나의제이슨객체스트링값);
                            앨범쉐어드에디터.putString(상대ID, 상대방제이슨객체스트링값);
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
                        Intent intent = new Intent(Intent.ACTION_PICK);//인텐트를 생성-행동을 정의
                        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);//인텐트의 타입을 정의
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);//인텐트에 값을 집어넣음
                        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//인텐트의 데이터를 설정
                        startActivityForResult(intent, 3333);
                        다이얼로그.dismiss();
                    }
                });
                Button 날짜수정버튼 = (Button) 다이얼로그.findViewById(R.id.날짜수정버튼);
                날짜수정버튼.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String 입력날짜 = 날짜입력.getText().toString();
                        MyData 날짜넣은데이터 = new MyData(mData.get(pos).imageString, 입력날짜);
                        mData.set(pos, 날짜넣은데이터);
                        adapter.notifyItemChanged(pos);//아이템 내부 값 바뀐거 알려주기
                        다이얼로그.dismiss();
                    }
                });
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    // 앨범에서 액티비티로 돌아온 후 실행되는 메서드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);//
        if (requestCode == 2222) {
            Uri uri = data.getData();//인텐트에 실려온 uri값을 꺼내줌
            String uritostring = uri.toString();
            String 제이슨스트링값 = 앨범쉐어드.getString(내ID, "");
            String 상대제이슨스트링값 = 앨범쉐어드.getString(상대ID, "");
            MyData mydata = new MyData(uri.toString(), " ");
            mData.add(mydata);

            try {
                내꺼제이슨 = new JSONObject(제이슨스트링값);
                상대꺼제이슨 = new JSONObject(상대제이슨스트링값);
                //불러온 스트링형태의 제이슨 데이터를 제이슨으로 다시 변환
                내꺼제이슨.put(mData.size() + "번째사진", uritostring);
                내꺼제이슨.put("사진갯수", mData.size());
                상대꺼제이슨.put(mData.size() + "번째사진", uritostring);
                상대꺼제이슨.put("사진갯수", mData.size());
                String 나의제이슨객체스트링값 = 내꺼제이슨.toString();//내 제이슨을 스트링으로 변환
                String 상대방제이슨객체스트링값 = 상대꺼제이슨.toString();
                앨범쉐어드에디터.putString(내ID, 나의제이슨객체스트링값);
                앨범쉐어드에디터.putString(상대ID, 상대방제이슨객체스트링값);
                앨범쉐어드에디터.apply();
            } catch (JSONException e) {
                e.printStackTrace();
            }//
            //상대방 제이슨을 스트링으로 변환


            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        } else if (requestCode == 3333) {
            Uri uri = data.getData();
            String uritostring = uri.toString();
            MyData mydata = new MyData(uri.toString(), null);
            mData.set(포지션값, mydata);
            String 내꺼ID = 앨범쉐어드.getString(내ID, "_");
            String 상대꺼ID = 앨범쉐어드.getString(상대ID, "_");
            try {
                JSONObject 내꺼 = new JSONObject(내꺼ID);
                JSONObject 상대꺼 = new JSONObject(상대꺼ID);//불러온 스트링형태의 제이슨 데이터를 제이슨으로 다시 변환
                내꺼.put(포지션값 + 1 + "번째사진", uritostring);
                상대꺼.put(포지션값 + 1 + "번째사진", uritostring);
                String 나의제이슨객체스트링값 = 내꺼.toString();//내 제이슨을 스트링으로 변환
                String 상대방제이슨객체스트링값 = 상대꺼.toString();
                앨범쉐어드에디터.putString(내ID, 나의제이슨객체스트링값);
                앨범쉐어드에디터.putString(상대ID, 상대방제이슨객체스트링값);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            adapter.notifyItemRemoved(포지션값);
            adapter.notifyItemRangeChanged(포지션값, mData.size());
            앨범쉐어드에디터.apply();
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }


    }
}

