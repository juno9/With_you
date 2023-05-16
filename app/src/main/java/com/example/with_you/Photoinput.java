package com.example.with_you;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapTapi;
import com.skt.Tmap.poi_item.TMapPOIItem;

import org.json.JSONException;
import org.json.JSONObject;

public class Photoinput extends AppCompatActivity {
    private TMapPOIItem tMapPOIItem = new TMapPOIItem();
    private TextView 장소텍스트뷰;
    private ImageView 사진이미지뷰;
    private TMapData tmapdata;
    private Button 사진저장버튼;
    private Button 사진저장취소버튼;
    SharedPreferences 앨범쉐어드;
    SharedPreferences.Editor 앨범쉐어드에디터;
    String 장소명, 주소, 위도, 경도;
    String 띄울화면;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photoinput);

        Intent intent = getIntent();
        String 나의이메일 = intent.getStringExtra("나의이메일");
        String 상대이메일 = intent.getStringExtra("상대이메일");
        String uristr = intent.getStringExtra("uri");
        띄울화면 = intent.getStringExtra("띄울화면");

        TMapTapi tmaptapi = new TMapTapi(this);
        tmaptapi.setSKTMapAuthentication("l7xxad1064632fe7465c9e0f080c695df971");
        앨범쉐어드 = getSharedPreferences("앨범쉐어드프리퍼런스", MODE_PRIVATE);//데이터 가져올 쉐어드 선언
        앨범쉐어드에디터 = 앨범쉐어드.edit();//선언한 쉐어드의 에디터 선언


        사진이미지뷰 = findViewById(R.id.사진이미지뷰);
        Glide.with(getApplicationContext()).load(uristr).fitCenter().into(사진이미지뷰);

        장소텍스트뷰 = findViewById(R.id.장소텍스트뷰);
        장소텍스트뷰.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//터치하여 위치 검색 클릭하면
                Intent intent = new Intent(getApplicationContext(), Placeselect.class);//장소선택 액티비티로 이동
                intent.putExtra("uri", uristr);
                intent.putExtra("나의이메일", 나의이메일);
                intent.putExtra("상대이메일", 상대이메일);
                startActivityForResult(intent, 1);
            }
        });
        사진저장버튼 = (Button) findViewById(R.id.사진저장버튼);
        사진저장버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (장소텍스트뷰.getText().toString().equals("")) {//장소를 선택하지 않았다면
                    Toast.makeText(Photoinput.this, "장소를 선택하지 않았습니다.", Toast.LENGTH_SHORT).show();
                } else {//장소를 선택 했다면

                    String 나의쉐어드스트링 = 앨범쉐어드.getString(나의이메일, "");
                    String 상대쉐어드스트링 = 앨범쉐어드.getString(상대이메일, "");
                    if (띄울화면.equals("앨범")) {//앨범에서 사진을 새로 업로드 하거나 수정 했다면
                        String 신규수정여부 = intent.getStringExtra("신규/수정여부");
                        if (신규수정여부.equals("신규")) {
                            try {
                                JSONObject 나의제이슨객체 = new JSONObject(나의쉐어드스트링);
                                JSONObject 상대제이슨객체 = new JSONObject(상대쉐어드스트링);
                                int 사진갯수 = Integer.parseInt(나의제이슨객체.get("사진갯수").toString());
                                나의제이슨객체.put(사진갯수 + 1 + "번째사진", uristr);
                                나의제이슨객체.put(사진갯수 + 1 + "번째사진주소", 주소);
                                나의제이슨객체.put(사진갯수 + 1 + "번째사진장소명", 장소명);
                                나의제이슨객체.put(사진갯수 + 1 + "번째사진위도", 위도);
                                나의제이슨객체.put(사진갯수 + 1 + "번째사진경도", 경도);
                                나의제이슨객체.put("사진갯수", 사진갯수 + 1);
                                상대제이슨객체.put(사진갯수 + 1 + "번째사진", uristr);
                                상대제이슨객체.put(사진갯수 + 1 + "번째사진주소", 주소);
                                상대제이슨객체.put(사진갯수 + 1 + "번째사진장소명", 장소명);
                                상대제이슨객체.put(사진갯수 + 1 + "번째사진위도", 위도);
                                상대제이슨객체.put(사진갯수 + 1 + "번째사진경도", 경도);
                                상대제이슨객체.put("사진갯수", 사진갯수 + 1);//몇번째인지 정해야되네
                                String 나의제이슨스트링 = 나의제이슨객체.toString();
                                String 상대제이슨스트링 = 상대제이슨객체.toString();
                                앨범쉐어드에디터.putString(나의이메일, 나의제이슨스트링);
                                앨범쉐어드에디터.putString(상대이메일, 상대제이슨스트링);
                                앨범쉐어드에디터.apply();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            int 포지션값 = intent.getIntExtra("포지션값", 0);
                            Log.i("포지션값", String.valueOf(포지션값));
                            try {
                                JSONObject 나의제이슨객체 = new JSONObject(나의쉐어드스트링);
                                JSONObject 상대제이슨객체 = new JSONObject(상대쉐어드스트링);

                                나의제이슨객체.put(포지션값 + 1 + "번째사진", uristr);
                                나의제이슨객체.put(포지션값 + 1 + "번째사진주소", 주소);
                                나의제이슨객체.put(포지션값 + 1 + "번째사진장소명", 장소명);
                                나의제이슨객체.put(포지션값 + 1 + "번째사진위도", 위도);
                                나의제이슨객체.put(포지션값 + 1 + "번째사진경도", 경도);

                                상대제이슨객체.put(포지션값 + 1 + "번째사진", uristr);
                                상대제이슨객체.put(포지션값 + 1 + "번째사진주소", 주소);
                                상대제이슨객체.put(포지션값 + 1 + "번째사진장소명", 장소명);
                                상대제이슨객체.put(포지션값 + 1 + "번째사진위도", 위도);
                                상대제이슨객체.put(포지션값 + 1 + "번째사진경도", 경도);

                                String 나의제이슨스트링 = 나의제이슨객체.toString();
                                String 상대제이슨스트링 = 상대제이슨객체.toString();
                                앨범쉐어드에디터.putString(나의이메일, 나의제이슨스트링);
                                앨범쉐어드에디터.putString(상대이메일, 상대제이슨스트링);
                                앨범쉐어드에디터.apply();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                        Intent intent = new Intent(getApplicationContext(), Album.class);
                        intent.putExtra("나의이메일", 나의이메일);
                        intent.putExtra("상대이메일", 상대이메일);
                        startActivity(intent);
                        finish();
                    } else {//앨범의 지도형식으로 보기에서 사진을 올렸다면
                        Intent intent = new Intent(getApplicationContext(), Photomap.class);
                        intent.putExtra("나의이면일", 나의이메일);
                        intent.putExtra("상대이메일", 상대이메일);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

        사진저장취소버튼 = (Button) findViewById(R.id.사진저장취소버튼);
        사진저장취소버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override//장소 선택하면 주소, 장소명, 위경도를 받아옴.
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            Toast.makeText(this, "장소를 선택하지 않았습니다.", Toast.LENGTH_SHORT).show();
        } else {
            장소명 = data.getStringExtra("장소명");
            주소 = data.getStringExtra("주소");
            위도 = data.getStringExtra("위도");
            경도 = data.getStringExtra("경도");
            장소텍스트뷰.setText(장소명 + "\n" + 주소);
        }
    }
}