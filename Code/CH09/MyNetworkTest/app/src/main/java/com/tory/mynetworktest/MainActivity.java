package com.tory.mynetworktest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.tvResponse);

        mTextView.setText(getRandomLengthName("Before send request"));

        Button btnSendRequest = findViewById(R.id.btnSendRequest);
        btnSendRequest.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSendRequest:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        showResponse(getResponse("http://10.0.2.2/"));
                    }
                }).start();


                break;
        }
    }

    private String getResponse(final String addr) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(addr)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String data = response.body().string();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "no response from " + addr;

    }

    private void showResponse (final String response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextView.setText(response);

            }
        });
    }

    private String getRandomLengthName(String name) {
        Random random = new Random();
        int length = random.nextInt(500) + 1;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(name);
        }
        return builder.toString();
    }
}
