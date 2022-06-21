package com.example.myapplication;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Home extends AppCompatActivity {

    private Dialog dialog01;//좌상단 버튼 누를 때
    private Dialog dialog02;//우상단 버튼 누를 때
    private Button 전화버튼;
    private Button 프로필편집버튼;
    private ImageButton 앨범버튼;
    private ImageButton 기념일버튼;
    private ImageButton 옵션버튼;
    private ImageButton 상대프로필;
    private ImageButton 나의프로필;
    private TextView 만난날짜텍스트뷰;
    private ImageView 광고이미지뷰;
    private Intent 전화번호입력인텐트;
    private SharedPreferences 쉐어드프리퍼런스;
    private SharedPreferences.Editor 쉐어드에디터;
    private SharedPreferences 이벤트쉐어드프리퍼런스;
    private SharedPreferences.Editor 이벤트쉐어드에디터;
    private String 전화번호;
    private String 나의이메일;
    private String 상대이메일;
    JSONObject jsonObject;
    JSONObject partnerjsonObject;
    private JSONObject eventjsonObject;
    Event 이벤트;
    ImageButton 나의프로필내사진;
    ImageButton 상대프로필내사진;
    Thread thread2;
    String 처음만난날;

    // Channel에 대한 id 생성
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    // Channel을 생성 및 전달해 줄 수 있는 Manager 생성
    private NotificationManager mNotificationManager;
    // Notification에 대한 ID 생성
    private static final int NOTIFICATION_ID = 0;


//만약 전화번호 바꾸는 인텐트를 받으면 그 다이얼로그를 띄우는거까지 해줘


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {//최초 빌드때 실행
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ActionBar ac = getSupportActionBar();
        ac.setTitle("HOME");

        Intent intent = getIntent();
        나의이메일 = intent.getStringExtra("나의이메일");//쉐어드에 저장된 내 ID

        쉐어드프리퍼런스 = getSharedPreferences("회원정보쉐어드프리퍼런스", MODE_PRIVATE);
        쉐어드에디터 = 쉐어드프리퍼런스.edit();
        이벤트쉐어드프리퍼런스 = getSharedPreferences("이벤트쉐어드프리퍼런스", MODE_PRIVATE);
        이벤트쉐어드에디터 = 이벤트쉐어드프리퍼런스.edit();

        String userjsnstr = 쉐어드프리퍼런스.getString(나의이메일, "_");//회원정보 쉐어드 내에 ID를 키값으로 가진 데이터를 스트링으로 불러옴

        try {
            jsonObject = new JSONObject(userjsnstr);//스트링으로 저장되어 있는 제이슨 데이터를 참조하여 제이슨객체 생성
            상대이메일=jsonObject.get("연결상대").toString();
            String partnerjsnstr=쉐어드프리퍼런스.getString(상대이메일,"_");
            partnerjsonObject=new JSONObject(partnerjsnstr);
            처음만난날=jsonObject.get("처음만난날").toString();
           String 이벤트제이슨스트링= 이벤트쉐어드프리퍼런스.getString(나의이메일,"");
            JSONObject 이벤트제이슨객체=new JSONObject(이벤트제이슨스트링);
            String 날짜=이벤트제이슨객체.get("날짜1").toString();
            String 내용=이벤트제이슨객체.get("내용1").toString();
            이벤트=new Event(날짜,내용);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    Glide.with(getApplicationContext()).load(R.drawable.couple2).fitCenter().into(광고이미지뷰);
                } else if (msg.what == 2) {
                    Glide.with(getApplicationContext()).load(R.drawable.couple).fitCenter().into(광고이미지뷰);
                } else if (msg.what == 3) {
                    Glide.with(getApplicationContext()).load(R.drawable.couple_4).fitCenter().into(광고이미지뷰);
                } else if (msg.what == 4) {
                    Glide.with(getApplicationContext()).load(R.drawable.couple3).fitCenter().into(광고이미지뷰);
                }
            }
        };//핸들러는 스레드에서 받은 메시지에 따라 뷰에 이미지를 그려줌

        Thread thread = new Thread() {//여기서는 백그라운드에서 돌아갈 작업을 정의한다.
            public void run() {
                try {
                    for (int i = 0; i < 4; i++) {//여기 트루 대신 변수 하나 넣고 다른 액티비티 실행하면 멈추게 만들면 될거같다.
                        Message msg = handler.obtainMessage();//핸들러로 보낼 메시지 객체 생성
                        msg.what = i + 1;//메시지의 what을 1로 설정
                        handler.sendMessage(msg);//핸들러에게 메시지를 보낸다
                        sleep(4000);//3초간 스레드 멈춤
                        if (i == 3) {//사진갯수 끝까지 가면 다시 처음부터
                            i = 0;
                        }
                    }
                }//백그라운드 스레드(앱과 별개로 따로 돌아가고 있다.)
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();//스레드스타트

        Handler handler2 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    sendNotification();
                }
            }
        };//핸들러는 스레드에서 받은 메시지에 따라 뷰에 이미지를 그려줌

        createNotificationChannel();


        thread2 = new Thread() {//여기서는 백그라운드에서 돌아갈 작업을 정의한다.
            public void run() {
                try {
                    SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date 현재시간 = new Date();//현재시간 객체화
                    String 현재시간스트링 = transFormat.format(현재시간);
                    String 이벤트날짜 = 이벤트.날짜;//이벤트 시간 객체화
                    Date from = transFormat.parse(이벤트날짜);//이벤트 시간 단순화,데이트화
                    Date to = transFormat.parse(현재시간스트링);//현재시간 단순화,데이트화
                    assert from != null;
                    if (from.after(to)) {//이벤트 날짜가 오늘 이후면
                        sleep(4000);
                        Message msg2 = handler.obtainMessage();
                        msg2.what = 1;
                        handler2.sendMessage(msg2);
                    }
                }//백그라운드 스레드(앱과 별개로 따로 돌아가고 있다.)
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread2.start();//스레드스타트
        만난날짜텍스트뷰 = findViewById(R.id.날짜텍스트뷰);
        만난날짜텍스트뷰.setText("처음만난날\n" + "  " + 처음만난날);
        광고이미지뷰 = (ImageView) findViewById(R.id.배너이미지뷰);
        상대프로필 = (ImageButton) findViewById(R.id.imageButton);
        Glide.with(getApplicationContext()).load(R.drawable.smileicon).fitCenter().into(상대프로필);
        dialog01 = new Dialog(this);//다이얼로그1에는 상대의 정보가 있어야 한다.
        dialog01.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog01.setContentView(R.layout.activity_dialog);
        findViewById(R.id.imageButton).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDialog01(); // 아래 showDialog01() 함수 호출
                    }
                });

        상대프로필내사진 = (ImageButton) dialog01.findViewById(R.id.상대프로필이미지);
        try {
            if (partnerjsonObject.get("프로필이미지") != "") {
                Glide.with(getApplicationContext()).load(Uri.parse(partnerjsonObject.get("프로필이미지").toString())).fitCenter().into(상대프로필);
                Glide.with(getApplicationContext()).load(Uri.parse(partnerjsonObject.get("프로필이미지").toString())).fitCenter().into(상대프로필내사진);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        TextView 프로필텍스트뷰 = (TextView) dialog01.findViewById(R.id.textView);//다이얼로그 레이아웃의 텍스트뷰 연결
        try {
            프로필텍스트뷰.setText(partnerjsonObject.getString("이름") + "\n" + partnerjsonObject.getString("이메일"));//프로필 있는 텍스트박스1
        } catch (
                JSONException e) {
            e.printStackTrace();
        }

        //전화번호 있는 버튼
        전화버튼 = (Button) dialog01.findViewById(R.id.callbtn); //다이얼로그 1의 전화버튼 생성, 연결

        try {
            전화버튼.setText(partnerjsonObject.getString("전화번호"));//밸류값은 상대방의 전화번호
            전화버튼.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + 전화버튼.getText())));
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Button 번호변경버튼 = (Button) dialog01.findViewById(R.id.numberchangebtn);//다이얼로그1의 번호변경 버튼 객체를 생성하고 다이얼로그의 버튼뷰를 찾은 다음 이 둘을 연결
        번호변경버튼.setOnClickListener(new View.OnClickListener() {//연결한 버튼의 동작을 정의
            @Override
            public void onClick(View view) {
                전화번호입력인텐트 = new Intent(getApplicationContext(), Numberchange.class);
                전화번호입력인텐트.putExtra("번호변경인텐트", "10");//인텐트를 생성
                startActivityForResult(전화번호입력인텐트, 1);
            }
        });//전화번호 바꾸는 액티비티 불러오는 버튼

        dialog02 = new Dialog(this);

        dialog02.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog02.setContentView(R.layout.activity_dialog2);
        findViewById(R.id.imageButton2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog02(); // 아래 showDialog02() 함수 호출
            }
        });
        나의프로필 = (ImageButton) findViewById(R.id.imageButton2);
        나의프로필내사진 = (ImageButton) dialog02.findViewById(R.id.사용자프로필);
        try {
            Glide.with(getApplicationContext()).load(Uri.parse(jsonObject.get("프로필이미지").toString())).fitCenter().into(나의프로필);
            Glide.with(getApplicationContext()).load(Uri.parse(jsonObject.get("프로필이미지").toString())).fitCenter().into(나의프로필내사진);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        try {
            TextView 프로필텍스트뷰2 = (TextView) dialog02.findViewById(R.id.textView2);//다이얼로그 레이아웃의 텍스트뷰 연결
            프로필텍스트뷰2.setText(jsonObject.getString("이름") + "\n" + jsonObject.getString("이메일"));//프로필 있는 텍스트박스1
        } catch (
                JSONException e) {
            e.printStackTrace();
        }
        프로필편집버튼 = (Button) dialog02.findViewById(R.id.프로필사진편집버튼);
        프로필편집버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 2);
            }
        });

        앨범버튼 = (ImageButton) findViewById(R.id.imageButton3);
        Glide.with(getApplicationContext()).load(R.drawable.album2).fitCenter().into(앨범버튼);
        앨범버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Album.class);
                intent.putExtra("나의이메일", 나의이메일);
                intent.putExtra("상대이메일", 상대이메일);
                startActivity(intent);

            }
        });//앨범 액티비티 실행하기

        기념일버튼 = (ImageButton) findViewById(R.id.기념일버튼);
        Glide.with(getApplicationContext()).load(R.drawable.calender5).fitCenter().into(기념일버튼);
        기념일버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Loading.class);
                intent.putExtra("나의이메일", 나의이메일);
                intent.putExtra("상대이메일", 상대이메일);
                intent.putExtra("처음만난날", 처음만난날);
                startActivity(intent);

            }
        });//알림 액티비티 실행하기

        옵션버튼 = (ImageButton)
                findViewById(R.id.imageButton5);
        Glide.with(getApplicationContext()).load(R.drawable.setting4).centerCrop().into(옵션버튼);
        옵션버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Options.class);
                startActivity(intent);
            }
        });//옵션 액티비티 실행하기

    }//온크리에이트


    public void showDialog01() {
        dialog01.show(); // 다이얼로그 띄우는 메소드 호출
    }

    public void showDialog02() {
        dialog02.show(); // 다이얼로그 띄우는 메소드 호출
    }


    public void createNotificationChannel() {
        //notification manager 생성
        mNotificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        // 기기(device)의 SDK 버전 확인 ( SDK 26 버전 이상인지 - VERSION_CODES.O = 26)
        if (android.os.Build.VERSION.SDK_INT
                >= android.os.Build.VERSION_CODES.O) {
            //Channel 정의 생성자( construct 이용 )
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID
                    , "Test Notification", mNotificationManager.IMPORTANCE_HIGH);
            //Channel에 대한 기본 설정
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from Mascot");
            // Manager을 이용하여 Channel 생성
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }

    // Notification Builder를 만드는 메소드
    private NotificationCompat.Builder getNotificationBuilder() {
        Intent notificationIntent = new Intent(this, Anniversary.class);
        notificationIntent.putExtra("나의이메일", 나의이메일);
        notificationIntent.putExtra("상대이메일", 상대이메일);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                .setContentTitle("일정 알림")
                .setContentText(이벤트.날짜 + "에 예정된 " + 이벤트.내용 + "일정이 있습니다.")
                .setSmallIcon(R.drawable.ic_android)
                .setContentIntent(notificationPendingIntent)
                .setAutoCancel(true);
        return notifyBuilder;
    }

    // Notification을 보내는 메소드
    public void sendNotification() {
        // Builder 생성
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        // Manager를 통해 notification 디바이스로 전달
        mNotificationManager.notify(NOTIFICATION_ID, notifyBuilder.build());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent 받은인텐트) {
        super.onActivityResult(requestCode, resultCode, 받은인텐트);
        if (requestCode == 1 && resultCode == 1) {
            String 결과 = 받은인텐트.getStringExtra("보낼번호");//인텐트에 담았던 값은 putextra 할 때 정한 이름(키값)을 넣어야 그 내용물(밸류)을 얻을 수 있다.
            전화버튼.setText(결과);
            전화번호 = 결과;
            try {
                jsonObject.put("전화번호", 결과);//72열에서 불러온 제이슨 객체의 전화번호키값에 새로운 밸류값을 넣어줌
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String jsnstr2 = jsonObject.toString();//새로운 전화번호를 넣은 제이슨 데이터를 스트링으로 변환.
            쉐어드에디터.putString(나의이메일, jsnstr2);//쉐어드 내에 ID를 키값으로가지고 225열의 스트링을 밸류값으로 저장
            쉐어드에디터.apply();//에디터에 변경사항 적용
            전화버튼.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + 결과)));
                }
            });
        } else if (requestCode == 2) {
            Uri uri = Uri.parse(받은인텐트.getData().toString());
            나의프로필 = (ImageButton) findViewById(R.id.imageButton2);
            나의프로필내사진 = (ImageButton) dialog02.findViewById(R.id.사용자프로필);
            Glide.with(getApplicationContext()).load(uri).fitCenter().into(나의프로필);
            Glide.with(getApplicationContext()).load(uri).fitCenter().into(나의프로필내사진);

            try {
                jsonObject.put("프로필이미지", uri);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String 저장할제이슨스트링 = jsonObject.toString();
            쉐어드에디터.putString(나의이메일, 저장할제이슨스트링);
            쉐어드에디터.apply();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        thread2.interrupt();
    }
}









