package com.tc.activitylifecycletest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class NormalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        String strTmp = bd.getString("data");
        Toast.makeText(NormalActivity.this, strTmp, Toast.LENGTH_SHORT).show();
    }
}
