package com.tory.parcelabletest;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;


public class MyLooperThread extends Thread {
    static Handler mHandler;

    private static final String TAG = "SubThread";

    @Override
    public void run() {
        super.run();
        Looper.prepare();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                Log.d(TAG, "handleMessage: msg from other is " + (String) msg.obj);
                
            }
        };

        Looper.loop();
    }
}
