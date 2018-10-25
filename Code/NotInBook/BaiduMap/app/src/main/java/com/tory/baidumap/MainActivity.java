package com.tory.baidumap;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.MapView;

public class MainActivity extends AppCompatActivity {

    private MapView mMapView = null;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyApplication application = (MyApplication) getApplication();
        Toast.makeText(MainActivity.this, application.getAppName(), Toast.LENGTH_SHORT ).show();

        mMapView = (MapView) findViewById(R.id.bmapView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }



    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
