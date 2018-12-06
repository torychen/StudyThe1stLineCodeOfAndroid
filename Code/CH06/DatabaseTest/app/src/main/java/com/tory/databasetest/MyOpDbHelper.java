package com.tory.databasetest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpDbHelper extends SQLiteOpenHelper {
    final  static  int VERSION = 1;
    final  static  String CREATE_TABLE = "CREATE TABLE `question` ("
          +"`id` integer NOT NULL primary key autoincrement, "
          +"`title` varchar(20) ,"
          +"`body` text NOT NULL ,"
          +"`answer` text ,"
          +"`submitter` varchar(20) DEFAULT '小明',"
          +"`modifier` varchar(20) DEFAULT '我不知道',"
          +"`lastmodify` datetime DEFAULT NULL,"
          +"`language` varchar(10) DEFAULT 'common',"
          +"`category` varchar(10) DEFAULT NULL ,"
          +"`company` varchar(20) DEFAULT NULL ,"
          +"`rate` int(1) DEFAULT '1',"
          +"`imgpath` varchar(256) DEFAULT NULL ,"
          +"`heat` int(1) DEFAULT '1' ,"
          +"`syncflag` int(1) DEFAULT '0' ,"
          +"`blame` int(1) DEFAULT '0' ,"
          +"`duplicate` int(1) DEFAULT '0');"
          ;

    public MyOpDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
