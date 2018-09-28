package com.tc.activitytest;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_layout);
        Button btn1 = findViewById(R.id.btn_for_toast);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FirstActivity.this, "You clicked Button 1 to jump to the 2nd activity", Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
                Intent intent = new Intent("com.tc.activitytest.ACTION_START");
                intent.addCategory("com.tc.activitytest.MY_CATEGORY");
                intent.putExtra("extra_data", "Hello the 2nd activity");
                startActivity(intent);
            }
        });

        Button btn2 = findViewById(R.id.btn_free);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FirstActivity.this, "Free activity", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        Button btn3 = findViewById(R.id.btn_open_browser);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.baidu.com"));
                startActivity(intent);
            }
        });

        Button btn4 = findViewById(R.id.btn_dial);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:10086"));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_add:
                Toast.makeText(this, "You click Add", Toast.LENGTH_SHORT).show();
                break;
            case R.id.item_remove:
                Toast.makeText(this, "You click Remove", Toast.LENGTH_SHORT).show();
                break;

            default:
        }
        return true;
    }
}
