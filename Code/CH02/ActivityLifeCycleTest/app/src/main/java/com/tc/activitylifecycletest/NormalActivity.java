package com.tc.activitylifecycletest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class NormalActivity extends Activity {
    private static final String TAG = "NormalActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        String strTmp = bd.getString("data");
        Log.d(TAG, "onCreate: string from main activity " + strTmp);

        if (savedInstanceState != null) {
            String savedStr = savedInstanceState.getString("data_key");

            Log.d(TAG, "onCreate: string saved before destroyed." + savedStr);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        EditText editText = findViewById(R.id.etMain);
        String strTmp = editText.getText().toString();

        outState.putString("data_key", strTmp);
        Log.d(TAG, "onSaveInstanceState: ");
    }
}
