package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class Album extends AppCompatActivity {


    private static final String TAG = "MultiImageActivity";
    ArrayList<MyData> mData = new ArrayList<>();     // 이미지의 uri를 담을 ArrayList 객체
    RecyclerView recyclerView;  // 이미지를 보여줄 리사이클러뷰
    MultiImageAdapter adapter;  // 리사이클러뷰에 적용시킬 어댑터
    private final int GET_GALLERY_IMAGE = 200;
    int 포지션값 = 0;
    Dialog 다이얼로그;
    SharedPreferences 앨범쉐어드프리퍼런스;
    SharedPreferences.Editor 앨범쉐어드에디터;
    String ID;
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        Intent intent = getIntent();
        ID = intent.getStringExtra("ID");
        앨범쉐어드프리퍼런스 = getSharedPreferences("회원가입쉐어드프리퍼런스", Activity.MODE_PRIVATE);//데이터들을 꺼내올 쉐어드파일을 받아옴
        앨범쉐어드에디터 = 앨범쉐어드프리퍼런스.edit();//에디터가 관리할 쉐어드프리퍼런스를 설정
        String jsnstr = 앨범쉐어드프리퍼런스.getString(ID, null);

        try {
            jsonObject = new JSONObject(jsnstr);//제이슨 객체 불러옴->포장지 깜
            int 배열사이즈 = (int) jsonObject.get("mData.size");
            //반복문으로 내용물 다 까서 m데이터에 넣어줘야한다.
            for (int i = 0; i<배열사이즈; i++) {
                String 이미지스트링 = (String) jsonObject.get("MyData배열"+(i+1));
                MyData data= new MyData(이미지스트링, null);
                mData.add(i,data);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


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
                EditText 날짜입력 = (EditText) 다이얼로그.findViewById(R.id.날짜입력에딧텍스트);
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
            MyData mydata = new MyData(uri.toString(), null);//MyData객체 생성하면서 134열에서 가져온 URI값을 스트링으로 변환하여 넣어줌.
            mData.add(mydata);//생성한 데이터를 배열에 추가
            try {
                jsonObject.put("MyData배열" + mData.size(), uri.toString());
                jsonObject.put("mData.size", mData.size());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String jsnstr2 = jsonObject.toString();//이미지 넣은 제이슨오브젝트를 다시 스트링으로
            앨범쉐어드에디터.putString(ID, jsnstr2);//스트링으로 바꾼 제이슨 오브젝트를 ID키값에 밸류로 넣어줌
            앨범쉐어드에디터.apply();
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        } else if (requestCode == 3333) {
            Uri uri = data.getData();
            MyData mydata = new MyData(uri.toString(), null);
            mData.set(포지션값, mydata);
            try {
                jsonObject.put("MyData배열" + (포지션값 + 1), uri.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String jsnstr2 = jsonObject.toString();//이미지 넣은 제이슨오브젝트를 다시 스트링으로
            앨범쉐어드에디터.putString(ID, jsnstr2);//스트링으로 바꾼 제이슨 오브젝트를 ID키값에 밸류로 넣어줌
            앨범쉐어드에디터.apply();
            adapter.notifyItemRemoved(포지션값);
            adapter.notifyItemRangeChanged(포지션값, mData.size());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        }


    }
}

