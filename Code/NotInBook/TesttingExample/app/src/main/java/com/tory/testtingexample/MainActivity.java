package com.tory.testtingexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.btnMain);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int res = TestTarget.add(1, 2);
                Toast.makeText(MainActivity.this, "add(1,2) return " + res,Toast.LENGTH_SHORT).show();
            }
        });

    }
}
