package com.tory.databasetest;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MyDbTest {
    static Context appContext;
    static MyOpDbHelper opDbHelper;

    SQLiteDatabase database;

    private static final String TAG = "MyDbTest --->";

    @BeforeClass
    public  static void beforeClass(){
        Log.d(TAG, "beforeClass:  ");
        System.out.println("=========== by system out beforeClass:  ");

        appContext = InstrumentationRegistry.getTargetContext();

        opDbHelper = new MyOpDbHelper(appContext, "mydb", null, 1);
    }

    @AfterClass
    public static void afterClass(){
        Log.d(TAG, "afterClass: ");

        opDbHelper = null;
        appContext = null;
    }

    @Before
    public void before() {
        // Context of the app under test.
        Log.d(TAG, "before: ");

        database = opDbHelper.getReadableDatabase();
    }

    //test 1
    @Test
    public void test1() {
        Log.d(TAG, "test1: ");
        assertEquals("com.tory.databasetest", appContext.getPackageName());
    }

    //test 2
    @Test
    public void test2() {
        Log.d(TAG, "test2: ");
        assertTrue("just true for demo", true);
    }


    @After
    public  void after(){
        Log.d(TAG, "after: ");
        if (database != null) {
            database.close();
        }
    }

}
