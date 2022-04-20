package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class Album extends AppCompatActivity {


    private static final String TAG = "MultiImageActivity";
    ArrayList<MyData> mData = new ArrayList<>();     // 이미지의 uri를 담을 ArrayList 객체
    RecyclerView recyclerView;  // 이미지를 보여줄 리사이클러뷰
    MultiImageAdapter adapter;  // 리사이클러뷰에 적용시킬 어댑터
    private final int GET_GALLERY_IMAGE = 200;
    int 포지션값 = 0;
    Dialog 다이얼로그;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        recyclerView = findViewById(R.id.recyclerView);//리사이클러뷰 레이아웃과 클래스의 리사이클러뷰를 연결

        FloatingActionButton btn_getImage = findViewById(R.id.floatAingActionButton);//앨범에 사진을 추가하는 버튼을 만들고 연결
        btn_getImage.setOnClickListener(new View.OnClickListener() {//이 버튼을 누르면 어떤 행동을 하게 될지 정의

            @SuppressLint("IntentReset")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);//인텐트를 생성-행동을 정의
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);//인텐트의 타입을 정의
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);//인텐트에 값을 집어넣음
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//인텐트의 데이터를 설정
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
                EditText 날짜입력=(EditText) 다이얼로그.findViewById(R.id.날짜입력에딧텍스트);
                Button 삭제버튼 = (Button) 다이얼로그.findViewById(R.id.삭제버튼);
                삭제버튼.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mData.remove(pos);
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
                Button 날짜수정버튼=(Button) 다이얼로그.findViewById(R.id.날짜수정버튼);
                날짜수정버튼.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String 입력날짜=날짜입력.getText().toString();
                        MyData 날짜넣은데이터=new MyData(mData.get(pos).imageuri,입력날짜);
                        mData.set(pos,날짜넣은데이터);
                        adapter.notifyItemChanged(pos);
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
            Log.e(TAG, "onActivityResult:사진불러오기 " + data);
            Toast.makeText(getApplicationContext(), "2222", Toast.LENGTH_SHORT).show();
            Uri imageUri = data.getData();
            MyData mydata=new MyData(imageUri,null);
            mData.add(mydata);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        } else if (requestCode == 3333) {
            Uri uri = data.getData();
            MyData mydata=new MyData(uri,null);
            mData.set(포지션값, mydata);
            adapter.notifyItemRemoved(포지션값);
            adapter.notifyItemRangeChanged(포지션값, mData.size());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }


    }
}

