package com.example.with_you;

import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.poi_item.TMapPOIItem;

import java.util.ArrayList;

public class Placeselect extends AppCompatActivity {
    EditText 장소입력에딧텍스트;
    Button 검색버튼;
    ArrayList<Place> 장소목록 = new ArrayList();
    RecyclerView 리사이클러뷰;
    PlaceAdapter 장소어댑터;
    Handler handler;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.placeselect);

        Intent intent = getIntent();
        String 나의이메일 = intent.getStringExtra("나의이메일");
        String 상대이메일 = intent.getStringExtra("상대이메일");
        String uri=intent.getStringExtra("uri").toString();
        리사이클러뷰 = findViewById(R.id.장소목록리사이클러뷰);//리사이클러뷰 연결

        장소입력에딧텍스트 = (EditText) findViewById(R.id.장소입력에딧텍스트);
        검색버튼 = (Button) findViewById(R.id.검색버튼);
        검색버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                장소목록.clear();
                additem(장소입력에딧텍스트.getText().toString());//목록에 찾은 주소들 넣어줌
                handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.what == 1) {
                            장소어댑터 = new PlaceAdapter(장소목록, getApplicationContext());//어댑터 생성
                            장소어댑터.setOnItemClickListener(new PlaceAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int pos) {//각 아이템 누르면 그 아이템의 포지션값을 받아온다
                                    Intent intent = new Intent(getApplicationContext(), Photoinput.class);
                                    String 장소명 = 장소목록.get(pos).get이름();
                                    String 주소 = 장소목록.get(pos).get주소();
                                    String 위도 = 장소목록.get(pos).get위도();
                                    String 경도 = 장소목록.get(pos).get경도();
                                    intent.putExtra("장소명", 장소명);
                                    intent.putExtra("주소", 주소);
                                    intent.putExtra("위도", 위도);
                                    intent.putExtra("경도", 경도);
                                    intent.putExtra("나의이메일", 나의이메일);
                                    intent.putExtra("상대이메일", 상대이메일);
                                    setResult(1, intent);
                                    finish();
                                }
                            });
                            리사이클러뷰.setAdapter(장소어댑터);//리사이클러뷰 어댑터 설정
                            리사이클러뷰.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                        }
                    }
                };//핸들러는 스레드에서 받은 메시지에 따라 뷰에 이미지를 그려줌

            }
        });

    }

    public void additem(String 입력값) {
        TMapData tmapdata = new TMapData();
        tmapdata.findAllPOI(입력값, new TMapData.FindAllPOIListenerCallback() {
            @Override
            public void onFindAllPOI(ArrayList<TMapPOIItem> poiItem) {// 검색어가 들어간 주소들을 어레이리스트로 받아옴
                for (int i = 0; i < poiItem.size(); i++) {
                    TMapPOIItem item = poiItem.get(i);
                    item.getPOIPoint();
                    Log.d("주소로찾기", "POI Name: " + item.getPOIName().toString() + ", " +
                            "Address: " + item.getPOIAddress().replace("null", "") + ", " +
                            "Point: " + item.getPOIPoint().toString());

                    Place place = new Place(item.getPOIName(), item.getPOIAddress().replace("null", ""), item.frontLat, item.frontLon);
                    장소목록.add(place);

                }
                Message msg = handler.obtainMessage();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        });
    }


}
