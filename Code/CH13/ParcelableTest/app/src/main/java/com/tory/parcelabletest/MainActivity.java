package com.tory.parcelabletest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        startActivity(intent, .SubActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnGoto2ndActivity = findViewById(R.id.btnGoto2ndActivity);
        btnGoto2ndActivity.setOnClickListener(this);
    }
}
