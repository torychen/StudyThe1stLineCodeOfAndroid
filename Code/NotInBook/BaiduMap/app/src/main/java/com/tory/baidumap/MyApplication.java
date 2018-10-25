package com.tory.baidumap;

import android.app.Application;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        SDKInitializer.initialize(this);
        SDKInitializer.setCoordType(CoordType.BD09LL);
    }

    static public String getAppName() {
        return "This is my app to use baidu map";
    }
}
