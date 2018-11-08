package com.tory.parcelabletest;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    static  Handler mHandler;
    private static final String TAG = "MainThread";
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnGoto2ndActivity:
                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                MyPerson person = new MyPerson();
                person.setAge(10);
                person.setName("Tory");

                intent.putExtra("person_data", person);
                startActivity(intent);

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
                Log.d(TAG, "handleMessage: msg from sub thread is "+ str);

            }
        };

        Button btnSendMsg2Main = findViewById(R.id.btnSendMsg2MainThread);
        btnSendMsg2Main.setOnClickListener(this);
    }
}
