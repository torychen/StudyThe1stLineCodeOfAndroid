package com.tory.parcelabletest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class SubActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        Intent intent = getIntent();

        MyPerson person = intent.getParcelableExtra("person_data");

        Toast.makeText(this, "The person age is " + person.getAge() + " name is " + person.getName(), Toast.LENGTH_SHORT).show();
    }
}
