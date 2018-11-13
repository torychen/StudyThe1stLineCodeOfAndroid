package com.tory.mynetworktest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

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

    private int counter = 0;
    private final static int RESPONSE_RAW = 0;
    private final static int RESPONSE_JSON = 1;
    private final static int RESPONSE_XML = 2;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSendRequest:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        counter++;
                        if (counter % 3 == RESPONSE_RAW) {

                            showResponse(getResponse("http://10.0.2.2/"), RESPONSE_RAW);
                        } else if (counter % 3 == RESPONSE_JSON) {
                            showResponse(getResponse("http://10.0.2.2/get_data.json"), RESPONSE_JSON);
                        } else {
                            showResponse(getResponse("http://10.0.2.2/get_data.xml"), RESPONSE_XML);
                        }
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

    private void showResponse (final String response, final int flag) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                StringBuilder builder = new StringBuilder();

                switch (flag) {
                    case RESPONSE_JSON:
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                builder.append("id is " + jsonObject.getString("id"));
                                builder.append("name is " + jsonObject.getString("name"));
                                builder.append("version is " + jsonObject.getString("version"));
                            }
                        } catch (Exception e) {
                            builder.append("parse json file fail. The original string is ");
                        }

                        break;
                    case RESPONSE_RAW:
                        break;
                }

                builder.append(response);
                mTextView.setText(builder.toString());

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
