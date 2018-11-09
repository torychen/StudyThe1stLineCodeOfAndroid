package com.tory.parcelabletest;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    static  Handler mHandler;
    private static final String TAG = "MainThread";
    //private enum TYPE{SERAILIZABLE, PERCELABLE};

    private static class MyHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;
        public MyHandler(MainActivity activity){
            mActivity = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            //do something
        }
    }

    @Override
    public void onClick(View view) {
        Intent [] intent = new Intent[1];
        intent[0] = new Intent(MainActivity.this, SubActivity.class);
        PendingIntent pi = PendingIntent.getActivities(MainActivity.this, 0, intent, 0);

        switch (view.getId()) {
            case R.id.btnAlarm:
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                long atTime = SystemClock.elapsedRealtime() + 10*1000;//System.currentTimeMillis() 1970.1.1

                alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, atTime,pi);


                break;
            case R.id.btnNotification:
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                MyPersonSerializable person = new MyPersonSerializable();
                person.setAge(20);
                person.setName("notification");
                intent[0].putExtra("sperson", person);

                Notification notification = new NotificationCompat.Builder(this)
                                            .setContentTitle("This notificaton")
                                            .setSmallIcon(R.mipmap.ic_launcher)
                        .setAutoCancel(true)
                        .setContentIntent(pi)
                        .build();
                manager.notify(1, notification);


                break;
            case R.id.btnGoto2ndActivity:
                Intent intent2 = new Intent(MainActivity.this, SubActivity.class);
                MyPerson person2 = new MyPerson();
                person2.setAge(10);
                person2.setName("Tory");

                intent2.putExtra("pperson", person2);
                startActivity(intent2);

                break;

            case R.id.btnSendMsg2MainThread:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = Message.obtain();
                        String string = "This is sub thread.";
                        msg.obj = string;
                        mHandler.sendMessage(msg);
                    }
                }).start();
                break;

                default:
                    break;
        }





    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnGoto2ndActivity = findViewById(R.id.btnGoto2ndActivity);
        btnGoto2ndActivity.setOnClickListener(this);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String str = (String) msg.obj;
                Log.d(TAG, "main: msg from sub thread tid "+ msg.arg1 + " string " + str);

            }
        };

        Button btnSendMsg2Main = findViewById(R.id.btnSendMsg2MainThread);
        btnSendMsg2Main.setOnClickListener(this);

        HandlerThread handlerThread = new HandlerThread("A handlerThread");
        handlerThread.start();
        Looper looper = handlerThread.getLooper();
        Handler subHandler = new Handler(looper);

        subHandler.post(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                msg.arg1 = android.os.Process.myTid();
                msg.obj = "Sub thread 2.";
                mHandler.sendMessage(msg);
            }
        });

        handlerThread.quitSafely();


        Button btnAlarm = findViewById(R.id.btnAlarm);
        btnAlarm.setOnClickListener(this);

        Button btnNotification = findViewById(R.id.btnNotification);
        btnNotification.setOnClickListener(this);

    }
}
