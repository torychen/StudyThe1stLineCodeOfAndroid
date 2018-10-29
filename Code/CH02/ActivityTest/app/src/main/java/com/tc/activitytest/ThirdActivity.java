package com.tc.activitytest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ThirdActivity extends AppCompatActivity {

    private void returnData ()
    {
        Intent intent = new Intent();
        intent.putExtra("data_return", "Hello FirstActivity");
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        Button btnGobackTo1stActivity = findViewById(R.id.btn_in_3rd_activity);
        btnGobackTo1stActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnData();
            }
        });
    }

    @Override
    public void onBackPressed() {
        returnData();
    }
}
