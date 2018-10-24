package com.zlt_tech.theapp;

import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;

import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity implements Observer {
    private static final String TAG = "MainActivity";

    private UsbCameraManager mUsbCameraManager;
    private AICoreManager mAICoreManager;

    private TextureView mTextureView;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;

    private Handler mHandler;

    private  Paint mPaint;

    @Override
    public void update(Observable observable, Object o) {

        AIResult aiResult = (AIResult) o;
        Rect[] rects = aiResult.getmRects();
        Bitmap bitmapOrg = aiResult.getmBitmap();
        Bitmap bitmap = bitmapOrg.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(bitmap);
        for (Rect rect:rects) {
            canvas.drawRect(rect, mPaint);
        }

        canvas = mSurfaceHolder.lockCanvas();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        canvas.drawBitmap(bitmap, 0, 0, mPaint);
        mSurfaceHolder.unlockCanvasAndPost(canvas);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextureView = findViewById(R.id.tvMain);

        mSurfaceView = findViewById(R.id.svMain);
        mSurfaceView.setZOrderMediaOverlay(true);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);

        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(10);
        mPaint.setStyle(Paint.Style.STROKE);

        ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA},1);

        mHandler = new Handler(this.getMainLooper());

        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);


        mUsbCameraManager = new UsbCameraManager(
                this,
                mHandler,
                cameraManager,
                mTextureView);

        mAICoreManager = new AICoreManager();
        if (!mAICoreManager.init(this)) {
            Log.e(TAG, "onCreate: Fatal error, ai core init fail.");
            finish();
        }

        if (!mUsbCameraManager.init(mAICoreManager)) {
            Log.e(TAG, "onCreate: Fatal error, usb camera init fail.");
            finish();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mPaint = null;
        //How to clean up the following resource?
        /*
        mHandler ;
        mTextureView;
        mSurfaceView;
        mSurfaceHolder
        */

        mAICoreManager.quit();
        mAICoreManager = null;

        mUsbCameraManager.quit();
        mUsbCameraManager = null;

    }
}
