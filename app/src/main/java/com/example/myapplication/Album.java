package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;


public class Album extends AppCompatActivity {


    private static final String TAG = "MultiImageActivity";
    ArrayList<Uri> uriList = new ArrayList<>();     // 이미지의 uri를 담을 ArrayList 객체
    RecyclerView recyclerView;  // 이미지를 보여줄 리사이클러뷰
    MultiImageAdapter adapter;  // 리사이클러뷰에 적용시킬 어댑터
    private final int GET_GALLERY_IMAGE = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);


//        FloatingActionButton 사진추가버튼 = (FloatingActionButton) findViewById(R.id.floatAingActionButton);
//        사진추가버튼.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent 사진추가인텐트 = new Intent(Intent.ACTION_PICK);
//                사진추가인텐트.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");//인텐트의 데이터타입을 정의
//                startActivityForResult(사진추가인텐트, GET_GALLERY_IMAGE);
//            }
//        });



        FloatingActionButton btn_getImage = findViewById(R.id.floatAingActionButton);//앨범에 사진을 추가하는 버튼을 만들고 연결
        btn_getImage.setOnClickListener(new View.OnClickListener() {//이 버튼을 누르면 어떤 행동을 하게 될지 정의
            @SuppressLint("IntentReset")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);//인텐트를 생성-행동을 정의
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);//인텐트의 타입을 정의
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);//인텐트에 값을 집어넣음
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//인텐트의 데이터를 설정
                startActivityForResult(intent, 2222);
            }
        });

        recyclerView = findViewById(R.id.recyclerView);//리사이클러뷰 레이아웃과 클래스의 리사이클러뷰를 연결
    }

    // 앨범에서 액티비티로 돌아온 후 실행되는 메서드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);//

        if (data == null) {   // 어떤 이미지도 선택하지 않은 경우
            Toast.makeText(getApplicationContext(), "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
        } else {   // 이미지를 하나라도 선택한 경우
            if (data.getClipData() == null) {     // 이미지를 하나만 선택한 경우
                Log.e("single choice: ", String.valueOf(data.getData()));
                Uri imageUri = data.getData();
                uriList.add(imageUri);

                adapter = new MultiImageAdapter(uriList, getApplicationContext());//어댑터 생성
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

            } else {      // 이미지를 여러장 선택한 경우
                ClipData clipData = data.getClipData();
                Log.e("clipData", String.valueOf(clipData.getItemCount()));

                if (clipData.getItemCount() > 10) {   // 선택한 이미지가 11장 이상인 경우
                    Toast.makeText(getApplicationContext(), "사진은 10장까지 선택 가능합니다.", Toast.LENGTH_LONG).show();
                } else {   // 선택한 이미지가 1장 이상 10장 이하인 경우
                    Log.e(TAG, "multiple choice");

                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        Uri imageUri = clipData.getItemAt(i).getUri();  // 선택한 이미지들의 uri를 가져온다.
                        try {
                            uriList.add(imageUri);  //uri를 list에 담는다.
                        } catch (Exception e) {
                            Log.e(TAG, "File select error", e);
                        }
                    }
                    adapter = new MultiImageAdapter(uriList, getApplicationContext());//어댑터 생성하고 앨범 클래스에 있는 uri리스트 만들고 거기에 값을 집어넣음.
                    recyclerView.setAdapter(adapter);   // 리사이클러뷰에 어댑터 세팅
                    recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                }
            }
        }
    }


}