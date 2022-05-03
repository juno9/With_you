package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;


public class Login extends AppCompatActivity {

    EditText ID입력;
    EditText PW입력;
    Button 로그인버튼;
    Button 회원가입버튼;
    SharedPreferences 로그인쉐어드프리퍼런스;
    SharedPreferences.Editor 로그인쉐어드에디터;
    Dialog 연결확인다이얼로그;
    String 상대방ID;
    TextView 안내문구텍스트뷰;
    Button 수락버튼;
    Button 거절버튼;

    @Override
    protected void onCreate(Bundle savedInstanceState) {//최초 빌드때 실행

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        로그인쉐어드프리퍼런스 = getSharedPreferences("회원정보쉐어드프리퍼런스", Activity.MODE_PRIVATE);
        로그인쉐어드에디터 = 로그인쉐어드프리퍼런스.edit();
        SharedPreferences 앨범쉐어드프리퍼런스 = getSharedPreferences("앨범쉐어드프리퍼런스", Activity.MODE_PRIVATE);
        SharedPreferences.Editor 앨범쉐어드에디터 = 앨범쉐어드프리퍼런스.edit();
        ID입력 = (EditText) findViewById(R.id.ID에딧텍스트);
        PW입력 = (EditText) findViewById(R.id.PW에딧텍스트);
        연결확인다이얼로그 = new Dialog(this);//연결 확인 다이얼로그 생성
        연결확인다이얼로그.requestWindowFeature(Window.FEATURE_NO_TITLE);//다이얼로그 특징 지정
        연결확인다이얼로그.setContentView(R.layout.activity_connectconfirm);//레이아웃 연결
//        앨범쉐어드에디터.clear();
//                로그인쉐어드에디터.clear();
//                앨범쉐어드에디터.apply();
//                로그인쉐어드에디터.apply();


        안내문구텍스트뷰 = (TextView) 연결확인다이얼로그.findViewById(R.id.안내텍스트뷰);
        수락버튼 = (Button) 연결확인다이얼로그.findViewById(R.id.승낙버튼);
        수락버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String InputID = ID입력.getText().toString();//입력받은 ID값 스트링으로 변환
                    String 내ID쉐어드데이터 = 로그인쉐어드프리퍼런스.getString(InputID, "");
                    JSONObject 내ID제이슨객체 = new JSONObject(내ID쉐어드데이터);//나의 제이슨 객체 생성
                    String 상대방ID = (String) 내ID제이슨객체.get("연결요청상대");
                    String 상대방ID쉐어드데이터 = 로그인쉐어드프리퍼런스.getString(상대방ID, "");
                    JSONObject 상대방ID제이슨객체 = new JSONObject(상대방ID쉐어드데이터);//상대방 제이슨 객체 생성
                    JSONObject albumjsonObject = new JSONObject();//상대방 ID로 만든 제이슨 객체생성
                    JSONObject albumjsonObject2 = new JSONObject();//내 ID로 만든 제이슨 객체생성

                    내ID제이슨객체.put("연결상대", 상대방ID);
                    상대방ID제이슨객체.put("연결상대", 상대방ID);
                    내ID제이슨객체.put("연결여부", "true");
                    상대방ID제이슨객체.put("연결여부", "true");
                    내ID제이슨객체.put("연결요청", "X");
                    상대방ID제이슨객체.put("연결요청", "X");

                    albumjsonObject.put("첫번째사진", "X");
                    albumjsonObject2.put("첫번째사진", "X");
                    String albumjsnstr = albumjsonObject.toString();
                    String albumjsnstr2 = albumjsonObject2.toString();
                    앨범쉐어드에디터.putString(상대방ID, albumjsnstr);
                    앨범쉐어드에디터.putString(InputID, albumjsnstr2);
                    앨범쉐어드에디터.apply();
                    Toast.makeText(getApplicationContext(), 상대방ID + "님과 연결되었습니다.", Toast.LENGTH_SHORT).show();
                    연결확인다이얼로그.dismiss();
                } catch (JSONException e) {

                }


            }
        });

        거절버튼 = (Button) 연결확인다이얼로그.findViewById(R.id.거절버튼);
        거절버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "요청을 거절하였습니다.", Toast.LENGTH_SHORT).show();
                연결확인다이얼로그.dismiss();
            }
        });
        로그인버튼 = (Button) findViewById(R.id.로그인버튼);//누르면 기존 정보들을 가지고 홈액티비티로 가야한다.
        로그인버튼.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                //가입한 ID가 키값, 제이슨데이터를 변환한 스트링이 밸류값.
                String InputID = ID입력.getText().toString();//입력받은 ID값 스트링으로 변환
                String InputPW = PW입력.getText().toString();

//
                String strJson = 로그인쉐어드프리퍼런스.getString(InputID, null);
                //스트링으로 변환하여 쉐어드에 저장한 제이슨 데이터를 다시 제이슨형태로 바꾸기 위해 스트링 형태로 재호출,
                // 가입할 때의 ID가 키값으로 쓰이도록 설정해 뒀으니 스트링 데이터를 쉐어드에서 가져옴
                if (strJson != null) {
                    try {
                        JSONObject response = new JSONObject(strJson);
//                        상대방ID = (String) response.get("연결요청상대");
//                        안내문구텍스트뷰.setText(상대방ID + "님의 연결요청이 있습니다");
//                        if (response.get("연결요청").toString().equals("O")) {
//                            연결확인다이얼로그.show();
//                        }else {
                        String 저장된ID = response.get("ID").toString();
                        String 저장된PW = response.get("PW").toString();
                        String 저장된이름 = response.get("이름").toString();
                        String 저장된전화번호 = response.get("전화번호").toString();
                        String 저장된이메일 = response.get("이메일").toString();
                        String 저장된처음사귄날 = response.get("처음사귄날").toString();
                        String 연결여부 = response.get("연결여부").toString();
                        String 저장된연결상대 = response.get("연결상대").toString();
                        String 연결요청여부 = response.get("연결요청").toString();
                        //로그인한 ID에서 뽑아낸 정보들

                        if (InputID.equals(저장된ID) && InputPW.equals(저장된PW)) {
                            if (연결여부.equals("false")) {
                                if (연결요청여부.equals("O")) {
                                    연결확인다이얼로그.show();
                                } else {
                                    Intent intent = new Intent(getApplicationContext(), Connect.class);
                                    intent.putExtra("나의ID", 저장된ID);
                                    startActivity(intent);
                                    finish();
                                }
                            } else if (연결여부.equals("true")) {
                                Intent intent = new Intent(getApplicationContext(), Home.class);
                                intent.putExtra("ID", 저장된ID);
                                intent.putExtra("이름", 저장된이름);
                                intent.putExtra("전화번호", 저장된전화번호);
                                intent.putExtra("이메일", 저장된이메일);
                                intent.putExtra("처음사귄날", 저장된처음사귄날);
                                intent.putExtra("연결상대", 저장된연결상대);
                                startActivity(intent);
                                finish();
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "로그인 정보가 다릅니다.", Toast.LENGTH_SHORT).show();

                        }
//                        }
                    } catch (JSONException e) {

                    }

                } else {
                    Toast.makeText(getApplicationContext(), "정보없음", Toast.LENGTH_SHORT).show();
                }
            }

        });
        회원가입버튼 = (Button) findViewById(R.id.회원가입버튼);//누르면 회원가입 액티비티를 띄워야 한다.
        회원가입버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });


    }


}