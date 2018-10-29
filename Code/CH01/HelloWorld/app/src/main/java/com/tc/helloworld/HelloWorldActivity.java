package com.tc.helloworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class HelloWorldActivity extends AppCompatActivity {
    //Type logt then tab will help create a tag.
    private static final String TAG = "HelloWorldActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_world);
        //Use logd, logi then tab will help create log.
        Log.d("HelloWorldActivity", "onCreate execute");
        Log.i(TAG, "onCreate: this is log.i().");
        Log.d("data", "onCreate: by tag data");


    }
}
