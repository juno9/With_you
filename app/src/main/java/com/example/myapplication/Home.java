package com.example.myapplication;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
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
import com.bumptech.glide.Glide;
import org.json.JSONException;
import org.json.JSONObject;


public class Home extends AppCompatActivity {

    private Dialog dialog01;//좌상단 버튼 누를 때
    private Dialog dialog02;//우상단 버튼 누를 때
    private Button 전화버튼;
    private ImageButton 앨범버튼;
    private ImageButton 알림버튼;
    private ImageButton 옵션버튼;
    private TextView 만난날짜텍스트뷰;
    private ImageView 광고이미지뷰;
    private Intent 전화번호입력인텐트;
    private SharedPreferences 쉐어드프리퍼런스;
    private SharedPreferences.Editor 쉐어드에디터;
    private SharedPreferences 이벤트쉐어드프리퍼런스;
    private SharedPreferences.Editor 이벤트쉐어드에디터;
    private String 전화번호;
    private String ID;
    private String 상대ID;
    JSONObject jsonObject;
    JSONObject partnerjsonObject;
    private JSONObject eventjsonObject;
    Event 이벤트;

    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    // Channel을 생성 및 전달해 줄 수 있는 Manager 생성
    private NotificationManager mNotificationManager;

    // Notification에 대한 ID 생성
    private static final int NOTIFICATION_ID = 0;

    // Notification을 호출할 button 변수
    private Button button_notify;


//만약 전화번호 바꾸는 인텐트를 받으면 그 다이얼로그를 띄우는거까지 해줘


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {//최초 빌드때 실행
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ActionBar ac = getSupportActionBar();
        ac.setTitle("홈");

        Intent intent = getIntent();
        ID = intent.getStringExtra("ID");//쉐어드에 저장된 내 ID
        상대ID = intent.getStringExtra("연결상대");//연결된 상대의 ID
        쉐어드프리퍼런스 = getSharedPreferences("회원정보쉐어드프리퍼런스", MODE_PRIVATE);
        쉐어드에디터 = 쉐어드프리퍼런스.edit();
        이벤트쉐어드프리퍼런스 = getSharedPreferences("이벤트쉐어드프리퍼런스", MODE_PRIVATE);
        이벤트쉐어드에디터 = 쉐어드프리퍼런스.edit();
        createNotificationChannel();

        String userjsnstr = 쉐어드프리퍼런스.getString(ID, "_");//회원정보 쉐어드 내에 ID를 키값으로 가진 데이터를 스트링으로 불러옴
        String partnerjsnstr = 쉐어드프리퍼런스.getString(상대ID, "_");//회원정보 쉐어드 내에 상대방ID를 키값으로 가진 데이터를 스트링으로 불러옴
        String eventjsnstr = 이벤트쉐어드프리퍼런스.getString(ID, "_");

        try {
            jsonObject = new JSONObject(userjsnstr);//스트링으로 저장되어 있는 제이슨 데이터를 참조하여 제이슨객체 생성
            partnerjsonObject = new JSONObject(partnerjsnstr);//스트링으로 저장되어 있는 제이슨 데이터를 참조하여 제이슨객체 생성
            eventjsonObject = new JSONObject(eventjsnstr);
            String 날짜=eventjsonObject.getString("날짜1");
            String 내용=eventjsonObject.getString("내용1");
            이벤트=new Event(날짜,내용);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    Glide.with(getApplicationContext()).load(R.drawable.image1).centerCrop().into(광고이미지뷰);
                } else if (msg.what == 2) {
                    Glide.with(getApplicationContext()).load(R.drawable.image2).centerCrop().into(광고이미지뷰);
                } else if (msg.what == 3) {
                    Glide.with(getApplicationContext()).load(R.drawable.image3).centerCrop().into(광고이미지뷰);
                } else if (msg.what == 4) {
                    Glide.with(getApplicationContext()).load(R.drawable.image4).centerCrop().into(광고이미지뷰);
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


        Handler alarmhandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                   sendNotification();
                    }
//여기서 알림 띄우는 코드를 짜면 되고, 넣어 줄 내용은 쉐어드에서 가져오면 된다.
//알림을 띄우는 역할이 메인
            }
        };//액티비티에 보여줄 행동


        Thread alarmthread = new Thread() {//여기서는 백그라운드에서 돌아갈 작업을 정의한다.
            public void run() {
                while (true) {
                    try {
                        sleep(8000);
                        Message msg = alarmhandler.obtainMessage();
                        msg.what = 1;
                        alarmhandler.sendMessage(msg);

//일정 시간이 지나면 쉐어드의 어떤 이벤트를 알림으로 띄울 수 있게 전달함
//타이머 역할이 메인

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };//백그라운드 스레드(앱과 별개로 따로 돌아가고 있다.)
        alarmthread.start();

        광고이미지뷰 = (ImageView) findViewById(R.id.배너이미지뷰);


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

        만난날짜텍스트뷰 = (TextView) findViewById(R.id.textView);
        만난날짜텍스트뷰.setText(intent.getStringExtra("처음사귄날"));


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
        } catch (
                JSONException e) {
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

        findViewById(R.id.imageButton2).
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDialog02(); // 아래 showDialog02() 함수 호출
                    }
                });

        TextView 프로필텍스트뷰2 = (TextView) dialog02.findViewById(R.id.textView2);//다이얼로그 레이아웃의 텍스트뷰 연결
        try {
            프로필텍스트뷰2.setText(jsonObject.getString("이름") + "\n" + jsonObject.getString("이메일"));//프로필 있는 텍스트박스1
        } catch (
                JSONException e) {
            e.printStackTrace();
        }


        앨범버튼 = (ImageButton) findViewById(R.id.imageButton3);
        앨범버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Album.class);
                intent.putExtra("ID", ID);
                intent.putExtra("상대ID", 상대ID);
                startActivity(intent);
            }
        });//앨범 액티비티 실행하기

        알림버튼 = (ImageButton) findViewById(R.id.알림버튼);
        알림버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Notification.class);
                intent.putExtra("ID", ID);
                intent.putExtra("상대ID", 상대ID);
                startActivity(intent);
            }
        });//알림 액티비티 실행하기

        옵션버튼 = (ImageButton)
                findViewById(R.id.imageButton5);
        옵션버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Options.class);
                startActivity(intent);
            }
        });//옵션 액티비티 실행하기

    }


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

    private NotificationCompat.Builder getNotificationBuilder() {
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                .setContentTitle("이벤트 알림")
                .setContentText(이벤트.get날짜()+"에 예정된 "+이벤트.get내용()+"일정이 있습니다" )
                .setSmallIcon(R.drawable.ic_android);
        return notifyBuilder;
    }

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
            쉐어드에디터.putString(ID, jsnstr2);//쉐어드 내에 ID를 키값으로가지고 225열의 스트링을 밸류값으로 저장
            쉐어드에디터.apply();//에디터에 변경사항 적용
            전화버튼.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + 결과)));
                }
            });
        }
    }
}









