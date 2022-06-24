package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.overlay.Marker;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapTapi;
import com.skt.Tmap.poi_item.TMapPOIItem;

import java.util.ArrayList;

public class Photoinput extends AppCompatActivity {
    private TMapPOIItem tMapPOIItem = new TMapPOIItem();
    private TextView 장소텍스트뷰;
    private ImageView 사진이미지뷰;
    private TMapData tmapdata;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photoinput);

        Intent intent = getIntent();
        String 나의이메일 = intent.getStringExtra("나의이메일");
        String 상대이메일 = intent.getStringExtra("상대이메일");
        String uristr = intent.getStringExtra("uri");

        TMapTapi tmaptapi = new TMapTapi(this);
        tmaptapi.setSKTMapAuthentication("l7xxad1064632fe7465c9e0f080c695df971");


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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String 장소명=data.getExtras().getString("장소명");
        장소텍스트뷰.setText(장소명);
    }
}