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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.navercorp.nid.NaverIdLoginSDK;
import com.navercorp.nid.oauth.NidOAuthBehavior;
import com.navercorp.nid.oauth.NidOAuthLogin;
import com.navercorp.nid.oauth.NidOAuthLoginState;
import com.navercorp.nid.oauth.OAuthLoginCallback;
import com.navercorp.nid.oauth.view.NidOAuthLoginButton;
import com.navercorp.nid.profile.NidProfileCallback;
import com.navercorp.nid.profile.data.NidProfile;
import com.navercorp.nid.profile.data.NidProfileResponse;

import org.json.JSONException;
import org.json.JSONObject;


public class Login extends AppCompatActivity {
    NidOAuthLogin 로그인 = new NidOAuthLogin();
    NidOAuthLoginButton 네이버로그인버튼;
    EditText 이메일입력;
    EditText PW입력;
    Button 로그인버튼;
    Button 회원가입버튼;
    SharedPreferences 로그인쉐어드프리퍼런스;
    SharedPreferences.Editor 로그인쉐어드에디터;
    Dialog 연결확인다이얼로그;
    Dialog 연결대기다이얼로그;

    Button 일반회원가입버튼;
    Button 네이버회원가입버튼;
    TextView 안내텍스트뷰;

    @Override
    protected void onCreate(Bundle savedInstanceState) {//최초 빌드때 실행

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar ac = getSupportActionBar();
        ac.setTitle("With you");
        로그인쉐어드프리퍼런스 = getSharedPreferences("회원정보쉐어드프리퍼런스", Activity.MODE_PRIVATE);
        로그인쉐어드에디터 = 로그인쉐어드프리퍼런스.edit();
        SharedPreferences 앨범쉐어드프리퍼런스 = getSharedPreferences("앨범쉐어드프리퍼런스", Activity.MODE_PRIVATE);
        SharedPreferences.Editor 앨범쉐어드에디터 = 앨범쉐어드프리퍼런스.edit();


        이메일입력 = (EditText) findViewById(R.id.이메일에딧텍스트);
        PW입력 = (EditText) findViewById(R.id.PW에딧텍스트);

//        앨범쉐어드에디터.clear();
//        로그인쉐어드에디터.clear();
//        앨범쉐어드에디터.apply();
//        로그인쉐어드에디터.apply();


        연결확인다이얼로그 = new Dialog(this);
        연결확인다이얼로그.setContentView(R.layout.activity_connectconfirm);
        연결대기다이얼로그 = new Dialog(this);
        연결대기다이얼로그.setContentView(R.layout.activity_connectwaitng);
        안내텍스트뷰 = 연결대기다이얼로그.findViewById(R.id.대기안내텍스트뷰);
        Button 닫기버튼 = 연결대기다이얼로그.findViewById(R.id.돌아가기버튼);
        닫기버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                연결대기다이얼로그.dismiss();
            }
        });

        로그인버튼 = (Button) findViewById(R.id.로그인버튼);//누르면 기존 정보들을 가지고 홈액티비티로 가야한다.
        로그인버튼.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                //가입한 ID가 키값, 제이슨데이터를 변환한 스트링이 밸류값.
                String 이메일입력값 = 이메일입력.getText().toString();//입력받은 ID값 스트링으로 변환
                String PW입력값 = PW입력.getText().toString();
                String strJson = 로그인쉐어드프리퍼런스.getString(이메일입력값, null);
                //스트링으로 변환하여 쉐어드에 저장한 제이슨 데이터를 다시 제이슨형태로 바꾸기 위해 스트링 형태로 재호출,
                // 가입할 때의 ID가 키값으로 쓰이도록 설정해 뒀으니 스트링 데이터를 쉐어드에서 가져옴
                if (strJson != null) {//회원정보 있으면
                    try {
                        JSONObject response = new JSONObject(strJson);//입력한 ID와 동일한 키를 가진 제이슨 객체를 받음
                        String 저장된이메일 = response.get("이메일").toString();//제이슨 객체에서 유저데이터들을 받음
                        String 저장된PW = response.get("PW").toString();
                        String 연결여부 = response.get("연결여부").toString();
                        String 저장된연결상대 = response.get("연결상대").toString();//제이슨 객체에서 유저데이터들을 받음
                        if (이메일입력값.equals(저장된이메일) && PW입력값.equals(저장된PW)) {//id,비밀번호를 똑바로 넣었으면
                            if (연결여부.equals("false")) {//연결되어있지 않다면
                                String 연결대기 = response.get("연결대기").toString();
                                String 연결요청 = response.get("받은연결요청").toString();
                                if (연결대기.equals("true")) {//상대방의 응답을 기다리고 있다면
                                    String 상대이메일 = 저장된연결상대;
                                    안내텍스트뷰.setText(상대이메일 + "님의 응답을 기다리고 있습니다");
                                    연결대기다이얼로그.show();
                                } else if (연결대기.equals("false")) {//상대방의 응답을 기다리고 있다면
                                    if (연결요청.equals("true")) {//받은 연결 요청이 있다면
                                        String 연결요청상대 = response.get("연결요청상대").toString();
                                        연결확인다이얼로그.show();
                                        Button 수락버튼, 거절버튼;
                                        TextView 안내텍스트뷰 = 연결확인다이얼로그.findViewById(R.id.안내텍스트뷰);
                                        안내텍스트뷰.setText(연결요청상대);
                                        수락버튼 = 연결확인다이얼로그.findViewById(R.id.수락버튼);
                                        수락버튼.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(Login.this, ConnectRegister.class);
                                                intent.putExtra("나의이메일", 저장된이메일);
                                                intent.putExtra("연결상대", 연결요청상대);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                        거절버튼 = 연결확인다이얼로그.findViewById(R.id.거절버튼);
                                    } else {
                                        Intent intent = new Intent(getApplicationContext(), Connect.class);//연결액티비티로 넘어감
                                        intent.putExtra("나의이메일", 저장된이메일);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            }//연결되어있다면
                            else if (연결여부.equals("true")) {//연결이 안되어있으면
                                Intent intent = new Intent(getApplicationContext(), Home.class);
                                intent.putExtra("나의이메일", 저장된이메일);
                                intent.putExtra("연결상대", 저장된연결상대);
                                startActivity(intent);
                                finish();
                            }//연결이 안되어있으면
                        } //받은 유저데이터를 기반으로 id,비밀번호를 똑바로 넣었으면
                        else {//id,비밀번호 잘못 넣으면
                            Toast.makeText(getApplicationContext(), "로그인 정보가 다릅니다.", Toast.LENGTH_SHORT).show();
                        }//받은 유저데이터를 기반으로 id,비밀번호 잘못 넣으면
                    } catch (JSONException e) {

                    }
                }//회원정보 있으면
                else {//회원정보 없으면
                    Toast.makeText(getApplicationContext(), "정보없음", Toast.LENGTH_SHORT).show();
                }//회원정보 없으면
            }

        });
        네이버로그인버튼 = (NidOAuthLoginButton) findViewById(R.id.네이버로그인버튼);
        네이버로그인버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NaverIdLoginSDK naverIdLoginSDK = NaverIdLoginSDK.INSTANCE;
                naverIdLoginSDK.initialize(Login.this, getString(R.string.naver_client_id), getString(R.string.naver_client_secret), getString(R.string.app_name));//초기화
                NidOAuthBehavior nidOAuthBehavior = NidOAuthBehavior.DEFAULT;
                naverIdLoginSDK.setBehavior(nidOAuthBehavior);
                naverIdLoginSDK.authenticate(Login.this, new OAuthLoginCallback() {
                    @Override
                    public void onSuccess() {
                        String accessToken = naverIdLoginSDK.getAccessToken();
                        String refreshToken = naverIdLoginSDK.getRefreshToken();
                        long expires = naverIdLoginSDK.getExpiresAt();
                        String tyoe = naverIdLoginSDK.getTokenType();
                        NidOAuthLoginState state = naverIdLoginSDK.getState();
                        Toast.makeText(getApplicationContext(), "네이버 로그인 성공", Toast.LENGTH_SHORT).show();
                        로그인.callProfileApi(new NidProfileCallback<NidProfileResponse>() {
                            @Override
                            public void onSuccess(NidProfileResponse response) {
                                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT).show();
                                NidProfile 니드프로필 = response.getProfile();
                                String 이름 = 니드프로필.getName();
                                String 전화번호 = 니드프로필.getMobile();
                                String 이메일=니드프로필.getEmail();
                                String 쉐어드스트링=로그인쉐어드프리퍼런스.getString(이메일,"");

                                try {
                                    if(쉐어드스트링.equals("")){//가입이 안되어 있으면
                                        Intent intent=new Intent(getApplicationContext(),Register.class);
                                        intent.putExtra("이름",이름);
                                        intent.putExtra("전화번호",전화번호);
                                        intent.putExtra("이메일",이메일);
                                        intent.putExtra("로그인방식","네이버로그인");
                                        startActivity(intent);
                                    } else{//가입되어 있다면
                                        JSONObject 제이슨객체=new JSONObject(쉐어드스트링);
                                        String 연결여부=제이슨객체.get("연결여부").toString();
                                        String 저장된연결상대 = 제이슨객체.get("연결상대").toString();
                                        if (연결여부.equals("false")) {//연결되어있지 않다면
                                            String 연결대기 = 제이슨객체.get("연결대기").toString();
                                            String 연결요청 = 제이슨객체.get("받은연결요청").toString();
                                            if (연결대기.equals("true")) {//상대방의 응답을 기다리고 있다면
                                                String 상대이메일 = 저장된연결상대;
                                                안내텍스트뷰.setText(상대이메일 + "님의 응답을 기다리고 있습니다");
                                                연결대기다이얼로그.show();
                                            } else if (연결대기.equals("false")) {//상대방의 응답을 기다리고 있다면
                                                if (연결요청.equals("true")) {//받은 연결 요청이 있다면
                                                    String 연결요청상대 = 제이슨객체.get("연결요청상대").toString();
                                                    연결확인다이얼로그.show();
                                                    Button 수락버튼, 거절버튼;
                                                    TextView 안내텍스트뷰 = 연결확인다이얼로그.findViewById(R.id.안내텍스트뷰);
                                                    안내텍스트뷰.setText(연결요청상대);
                                                    수락버튼 = 연결확인다이얼로그.findViewById(R.id.수락버튼);
                                                    수락버튼.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            Intent intent = new Intent(Login.this, ConnectRegister.class);
                                                            intent.putExtra("나의이메일", 이메일);
                                                            intent.putExtra("연결상대", 연결요청상대);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    });
                                                    거절버튼 = 연결확인다이얼로그.findViewById(R.id.거절버튼);
                                                } else {
                                                    Intent intent = new Intent(getApplicationContext(), Connect.class);//연결액티비티로 넘어감
                                                    intent.putExtra("나의이메일", 이메일);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        }//연결되어있다면
                                        else if (연결여부.equals("true")) {//연결이 안되어있으면
                                            Intent intent = new Intent(getApplicationContext(), Home.class);
                                            intent.putExtra("나의이메일", 이메일);
                                            intent.putExtra("연결상대", 저장된연결상대);
                                            startActivity(intent);
                                            finish();
                                        }//연결이 안되어있으면
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int i, @NonNull String s) {
                                Toast.makeText(getApplicationContext(), "콜백실패", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onError(int i, @NonNull String s) {
                                Toast.makeText(getApplicationContext(), "콜백오류", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Toast.makeText(getApplicationContext(), "네이버 로그인 실패", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(getApplicationContext(), "네이버 로그인 오류", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        회원가입버튼 = (Button) findViewById(R.id.회원가입버튼);//누르면 회원가입 액티비티를 띄워야 한다.
        회원가입버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),Register.class);
                startActivity(intent);
            }
        });


    }


}