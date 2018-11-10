package com.tory.databasetest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {
    final  static  String CREATE_TABLE_BOOK =
            "create table Book(" +
                    "id integer primary key autoincrement, " +
                    "author text, " +
                    "price real, " +
                    "page integer, " +
                    "name text)";

    private static Context mContext;
    private String mName;
    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

        mContext = context;
        mName = name;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_BOOK);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
