package com.example.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapTapi;
import com.skt.Tmap.poi_item.TMapPOIItem;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;


public class Photomap extends AppCompatActivity implements OnMapReadyCallback, TMapGpsManager.onLocationChangedCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private FusedLocationSource mLocationSource;
    private MapView mapView;
    private NaverMap naverMap;
    private Button 스크롤로보기버튼;
    private Dialog 사진날짜다이얼로그;
    private TMapPOIItem tMapPOIItem=new TMapPOIItem();
    private TMapData tmapdata = new TMapData();




    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String 나의이메일 = intent.getStringExtra("나의이메일");
        String 상대이메일 = intent.getStringExtra("상대이메일");
        setContentView(R.layout.activity_photomap);//레이아웃이랑 클래스랑 연결


        //티맵 지도 미사용시 앱키설정
        TMapTapi tmaptapi = new TMapTapi(this);
        tmaptapi.setSKTMapAuthentication ("l7xxad1064632fe7465c9e0f080c695df971");
        //티맵 지도 미사용시 앱키설정



        스크롤로보기버튼 = (Button) findViewById(R.id.스크롤로보기버튼);
        스크롤로보기버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Album.class);
                intent.putExtra("나의이메일", 나의이메일);
                intent.putExtra("상대이메일", 상대이메일);
                startActivity(intent);
                finish();
            }
        });
        mapView = findViewById(R.id.map_view);//지도를 띄워주는 자체 뷰
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        mLocationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
        사진날짜다이얼로그=new Dialog(this);
        사진날짜다이얼로그.setContentView(R.layout.activity_photodatedialog);//보여줄다이얼로그 만들고 연결까지
    }
    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
            }
        }
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        Marker marker = new Marker();
        marker.setPosition(new LatLng(37.5670135, 126.9783740));
        marker.setWidth(100);
        marker.setHeight(100);
        marker.setIcon(OverlayImage.fromResource(R.drawable.couple2));
        marker.setMap(naverMap);
        naverMap.setLocationSource(mLocationSource);
        ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onLocationChange(Location location) {

    }


}