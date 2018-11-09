package com.tory.databasetest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    final private static boolean forFile = true;
    final private static boolean forSharedPreferrence = false;
    final private static boolean forDatabase = false;

    private static final String TAG = "MainActivity";

    EditText metInput;
    TextView mtvReadBack;
    final static private String mFileName = "mydata";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        metInput = findViewById(R.id.etInput);
        mtvReadBack = findViewById(R.id.tvReadBack);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnLoad = findViewById(R.id.btnLoad);

        btnLoad.setOnClickListener(this);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLoad:
                StringBuilder stringBuilder = new StringBuilder();
                FileInputStream fileInputStream = null;
                try {
                    fileInputStream = openFileInput(mFileName);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }

                    mtvReadBack.setText(stringBuilder.toString());

                } catch (Exception e) {
                  e.printStackTrace();
                }


                break;
            case R.id.btnSave:
                String input = metInput.getText().toString();
                FileOutputStream fileOutputStream = null;
                BufferedWriter writer = null;
                if (!TextUtils.isEmpty(input)) {
                    try {
                        fileOutputStream = openFileOutput(mFileName, MODE_PRIVATE);
                        writer = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
                        writer.write(input);
                        writer.close();
                    } catch (Exception e){
                        Log.e(TAG, "onClick: fail to open file", e);
                        e.printStackTrace();
                        return;
                    }

                }
                break;
        }

    }
}
