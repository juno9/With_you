package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class Photomap extends AppCompatActivity implements OnMapReadyCallback, TMapGpsManager.onLocationChangedCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,

    };
    private static final int PICK_IMAGE_FILE = 2;
    private FusedLocationSource mLocationSource;
    private MapView mapView;
    private NaverMap naverMap;
    private Button 스크롤로보기버튼;
    private Dialog 사진날짜다이얼로그;
    private TMapPOIItem tMapPOIItem = new TMapPOIItem();
    private TMapData tmapdata = new TMapData();
    String 나의이메일;
    String 상대이메일;
    SharedPreferences 앨범쉐어드;
    SharedPreferences.Editor 앨범쉐어드에디터;
    ArrayList<Bitmap> mPhotoBitmapList = new ArrayList<>();
    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        나의이메일 = intent.getStringExtra("나의이메일");
        상대이메일 = intent.getStringExtra("상대이메일");
        앨범쉐어드 = getSharedPreferences("앨범쉐어드프리퍼런스", MODE_PRIVATE);//데이터 가져올 쉐어드 선언
        앨범쉐어드에디터 = 앨범쉐어드.edit();//선언한 쉐어드의 에디터 선언
        setContentView(R.layout.activity_photomap);//레이아웃이랑 클래스랑 연결


        //티맵 지도 미사용시 앱키설정
        TMapTapi tmaptapi = new TMapTapi(this);
        tmaptapi.setSKTMapAuthentication("l7xxad1064632fe7465c9e0f080c695df971");
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
                naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
            }
        }
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {


        String 나의쉐어드데이터 = 앨범쉐어드.getString(나의이메일, "");
        String 상대쉐어드데이터 = 앨범쉐어드.getString(상대이메일, "");

        try {
            JSONObject 나의제이슨 = new JSONObject(나의쉐어드데이터);
            int 사진갯수 = (int) 나의제이슨.get("사진갯수");
            for (int i = 1; i < 사진갯수 + 1; i++) {
                String 이미지 = 나의제이슨.get(i + "번째사진").toString();
                Uri uri = Uri.parse(이미지);
                double 위도 = Double.parseDouble(나의제이슨.get(i + "번째사진위도").toString());
                double 경도 = Double.parseDouble(나의제이슨.get(i + "번째사진경도").toString());
                this.naverMap = naverMap;
                Marker marker = new Marker();//마커 만들기//
                marker.setPosition(new LatLng(위도, 경도));//위치 생성//위치에 맞게 들어가는거 확인
                marker.setWidth(150);//마커 넓이 설정
                marker.setHeight(150);//높이 설정
                String path = getRealPathFromURI(uri);
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



    public String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;    // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {        // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) { // 주 저장소이면.
                    return Environment.getExternalStorageDirectory() + "/" + split[1]; // 내부저장소 ex) /storage/0/
                } else {
                    return "/storage/" + type + "/" + split[1]; //외부저장소 ex)/storage/1/, /storage/sdcard/
                }
                // TODO handle non-primary volumes
            }        // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }        // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }    // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }    // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for * MediaStore Uris, and other file-based ContentProviders. * * @param context       The context. * @param uri           The Uri to query. * @param selection     (Optional) Filter used in the query. * @param selectionArgs (Optional) Selection arguments used in the query. * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check. * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check. * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check. * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());


    }

    public String getRealPathFromURI(Uri contentUri) {

        String[] proj = { MediaStore.Images.Media.DATA };

        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        cursor.moveToNext();
        @SuppressLint("Range") String path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
        Uri uri = Uri.fromFile(new File(path));

        cursor.close();
        return path;
    }

    private void openFile(Uri pickerInitialUri) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");

        // Optionally, specify a URI for the file that should appear in the
        // system file picker when it loads.
        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);

        startActivityForResult(intent, PICK_IMAGE_FILE);
    }




}