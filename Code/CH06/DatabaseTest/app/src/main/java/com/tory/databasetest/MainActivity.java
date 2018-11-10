package com.tory.databasetest;

import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //final private static boolean forFile = true;
    //final private static boolean forSharedPreferrence = false;
    //final private static boolean forDatabase = false;

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

        /* test map
        Map<String, String> map = new HashMap<>();
        map.put("a", "aaa");
        map.put("b", "bbb");
        map.put("c", "ccc");
        map.put("d", "ddd");
        map.put("e", "eee");

        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            Object key = iterator.next();
            Log.d(TAG, "onCreate: map key is" + key.toString() + " map value is " + map.get(key));
        }
        */

        /*
        * "create table Book(" +
                    "id integer primary key autoincrement, " +
                    "author text, " +
                    "price real, " +
                    "page integer, " +
                    "name text)";*/

        Button btnCreate = findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDBHelper dbHelper = new MyDBHelper(MainActivity.this, "Book.db", null, 1);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                //db.execSQL("insert into Book(author, price, page, name)", new String []{"Aaa", "10.5", "100", "Aaa's book"});
            }
        });

    }

    /*
    //File stream operation ok 2018.11.9
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
    */

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLoad:
                SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);

                String s1 = "";
                s1 = sharedPreferences.getString("string", s1);
                Toast.makeText(this, "string from sp is " + s1, Toast.LENGTH_SHORT).show();



                break;
            case R.id.btnSave:
                try {

                    SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
                    editor.putBoolean("boolean", true);
                    editor.putFloat("float", 5.2f);
                    editor.putString("string", "hello shared preference");

                    editor.apply();

                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }


                break;
        }

    }
}
