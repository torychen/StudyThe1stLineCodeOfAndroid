package com.tory.baidumap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;

    //What the following for?
    public LocationClient mLocationClient;//why public
    private MyLocationListener myListener;
    private MyLocationData locData;

    private Button mBtnReset;
    private Button mBtnSatellite;
    private Button mBtnNormal;

    private LatLng mLatLng;

    private boolean mIsFirstLoc;


    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //MyApplication application = (MyApplication) getApplication();
        //Toast.makeText(MainActivity.this, application.getAppName(), Toast.LENGTH_SHORT ).show();

        mMapView = findViewById(R.id.bmapView);


        mIsFirstLoc = true;

        myListener = new MyLocationListener();

        initView();

        initMap();

    }

    private void initMap() {
        mBaiduMap = mMapView.getMap();

        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);

        mLocationClient = new LocationClient(getApplicationContext());

        initLocation();
        mLocationClient.registerLocationListener(myListener);


        mLocationClient.start();

        mLocationClient.requestLocation();
    }

    //配置定位SDK参数
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation // .getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);
        option.setOpenGps(true); // 打开gps //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    private void  MyToast(String something) {
        Toast.makeText(MainActivity.this, something, Toast.LENGTH_SHORT).show();
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            // map view 销毁后不在处理新接收的位置
            if (bdLocation == null || mMapView == null) {
                return;
            }

            mLatLng = new LatLng(bdLocation.getLatitude(),
                    bdLocation.getLongitude());

            locData = new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100)
                    .latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude())
                    .build();

            mBaiduMap.setMyLocationData(locData);

            if (mIsFirstLoc) {
                mIsFirstLoc = false;
                LatLng ll = new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();

                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

                if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
                    MyToast(bdLocation.getAddrStr());
                } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
                    MyToast(bdLocation.getAddrStr());
                } else if (bdLocation.getLocType() == BDLocation.TypeOffLineLocation) {
                    MyToast(bdLocation.getAddrStr());
                }else if (bdLocation.getLocType() == BDLocation.TypeServerError) {
                    MyToast("服务器错误");
                }else if (bdLocation.getLocType() == BDLocation.TypeNetWorkException) {
                    MyToast("网络错误");
                }else if (bdLocation.getLocType() == BDLocation.TypeCriteriaException) {
                    MyToast("手机模式错误");
                }else {
                    MyToast("Should NOT enter here.");
                }

            }
        }
    }

    private void initView () {
        mBtnReset = findViewById(R.id.btnReset);
        mBtnReset.setOnClickListener(this);

        mBtnSatellite = findViewById(R.id.btnSatellite);
        mBtnSatellite.setOnClickListener(this);

        mBtnNormal = findViewById(R.id.btnNormal);
        mBtnNormal.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnReset:
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(mLatLng);
                mBaiduMap.animateMapStatus(mapStatusUpdate);
                break;
            case R.id.btnSatellite:
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                break;

            case R.id.btnNormal:
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                break;
        }
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
