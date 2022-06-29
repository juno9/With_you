package com.example.myapplication;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapTapi;
import com.skt.Tmap.poi_item.TMapPOIItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;


public class Photomap extends AppCompatActivity implements OnMapReadyCallback, TMapGpsManager.onLocationChangedCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private FusedLocationSource mLocationSource;
    private MapView mapView;
    private NaverMap naverMap;
    private Button 스크롤로보기버튼;
    private Dialog 사진날짜다이얼로그;
    String 나의이메일;
    String 상대이메일;
    SharedPreferences 앨범쉐어드;
    SharedPreferences.Editor 앨범쉐어드에디터;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        나의이메일 = intent.getStringExtra("나의이메일");
        상대이메일 = intent.getStringExtra("상대이메일");
        앨범쉐어드 = getSharedPreferences("앨범쉐어드프리퍼런스", MODE_PRIVATE);//데이터 가져올 쉐어드 선언
        앨범쉐어드에디터 = 앨범쉐어드.edit();//선언한 쉐어드의 에디터 선언
        setContentView(R.layout.activity_photomap);//레이아웃이랑 클래스랑 연결

        FloatingActionButton btn_getImage = findViewById(R.id.floatAingActionButton2);
        btn_getImage.setOnClickListener(new View.OnClickListener() {//이 버튼을 누르면 어떤 행동을 하게 될지 정의
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.putExtra("나의이메일", 나의이메일);
                intent.putExtra("상대이메일", 상대이메일);
                intent.setType("image/*");
                startActivityForResult(intent, 6666);
            }
        });
        //티맵 지도 미사용시 앱키설정
        TMapTapi tmaptapi = new TMapTapi(this);
        tmaptapi.setSKTMapAuthentication("l7xxad1064632fe7465c9e0f080c695df971");
        //티맵 지도 미사용시 앱키설정

        imageView = findViewById(R.id.짬통);
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
        사진날짜다이얼로그 = new Dialog(this);
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
                naverMap.setLocationTrackingMode(LocationTrackingMode.NoFollow);
            }
        }
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {

        String 나의쉐어드데이터 = 앨범쉐어드.getString(나의이메일, "");
        ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE);
        try {
            JSONObject 나의제이슨 = new JSONObject(나의쉐어드데이터);
            int 사진갯수 = (int) 나의제이슨.get("사진갯수");
            for (int i = 1; i < 사진갯수 + 1; i++) {
                String 이미지 = 나의제이슨.get(i + "번째사진").toString();
                Uri uri = Uri.parse(이미지);
                String path=getRealPathFromURI(uri);
                double 위도 = Double.parseDouble(나의제이슨.get(i + "번째사진위도").toString());
                double 경도 = Double.parseDouble(나의제이슨.get(i + "번째사진경도").toString());
                this.naverMap = naverMap;
                Marker marker = new Marker();//마커 만들기//
                marker.setPosition(new LatLng(위도, 경도));//위치 생성//위치에 맞게 들어가는거 확인
                marker.setWidth(100);//마커 넓이 설정
                marker.setHeight(100);//높이 설정
                marker.setIcon(OverlayImage.fromPath(path));
                marker.setMap(naverMap);
                ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onLocationChange(Location location) {

    }


    private String getRealPathFromURI(Uri contentUri) {
        if (contentUri.getPath().startsWith("/storage")) {
            return contentUri.getPath();
        }
        String id = DocumentsContract.getDocumentId(contentUri).split(":")[1];
        String[] columns = {MediaStore.Files.FileColumns.DATA};
        String selection = MediaStore.Files.FileColumns._ID + " = " + id;
        Cursor cursor = getContentResolver().query(MediaStore.Files.getContentUri("external"), columns, selection, null, null);
        try {
            int columnIndex = cursor.getColumnIndex(columns[0]);
            if (cursor.moveToFirst()) {
                return cursor.getString(columnIndex);
            }
        } finally {
            cursor.close();
        }
        return null;
    }
//uri에서 절대경로얻어오는 메소드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 6666) {
            if (data == null) {
                Toast.makeText(getApplicationContext(), "사진을 선택하지 않았습니다", Toast.LENGTH_SHORT).show();
            } else {
                Uri uri = data.getData();//인텐트에 실려온 uri값을 꺼내줌
                String uristr=uri.toString();
                Intent intent = new Intent(getApplicationContext(), Photoinput.class);
                intent.putExtra("uri", uristr);
                intent.putExtra("나의이메일", 나의이메일);
                intent.putExtra("상대이메일", 상대이메일);
                intent.putExtra("띄울화면","지도");
                startActivityForResult(intent,7777);
                finish();
            }
        } else if(requestCode == 7777){
            onMapReady(naverMap);
        }
    }


}