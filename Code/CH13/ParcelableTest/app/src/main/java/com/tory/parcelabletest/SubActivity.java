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

        MyPerson person = intent.getParcelableExtra("pperson");
        if (person != null) {

            Toast.makeText(this, "The person age is " + person.getAge() + " name is " + person.getName(), Toast.LENGTH_SHORT).show();
        }

        MyPersonSerializable personSerializable = (MyPersonSerializable) intent.getSerializableExtra("sperson");
        if (personSerializable != null) {

            Toast.makeText(this, "The person age is " + personSerializable.getAge() + " name is " + personSerializable.getName(), Toast.LENGTH_SHORT).show();
        }

    }
}
