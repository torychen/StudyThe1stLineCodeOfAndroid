package com.example.jarry.playvideo_texuture;

import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.opengl.Matrix;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;

import java.io.IOException;
import java.util.List;

import com.example.jarry.R;

public class TextureViewMediaActivity0 extends Activity implements TextureView.SurfaceTextureListener,
        MediaPlayer.OnPreparedListener,  SurfaceHolder.Callback, SensorEventListener{
    private static final String TAG = "GLViewMediaActivity";

    private SensorManager mSensorManager;
    private Sensor mRotation;
    public static final String videoPath = Environment.getExternalStorageDirectory()+"/live.mp4";
    private TextureView textureView;
    private MediaPlayer mediaPlayer;
    private SurfaceView surfaceView;
    
    //private TextureSurfaceRenderer videoRenderer;
    private BallSurfaceRenderer videoRenderer;
    private int surfaceWidth;
    private int surfaceHeight;

    private float[] matrix=new float[16];
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_0);

        textureView = (TextureView) findViewById(R.id.id_textureview);
        textureView.setSurfaceTextureListener(this);
        
        mSensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensors=mSensorManager.getSensorList(Sensor.TYPE_ALL);
        //todo 鍒ゆ柇鏄惁瀛樺湪rotation vector sensor
        mRotation=mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        
        //surfaceView = (SurfaceView) findViewById(R.id.id_surfaceview);
        //surfaceView.getHolder().addCallback(this);
    }

    private void playVideo(SurfaceTexture surfaceTexture) {
        //videoRenderer = new VideoTextureSurfaceRenderer(this, surfaceTexture, surfaceWidth, surfaceHeight);
    	videoRenderer = new BallSurfaceRenderer(this, surfaceTexture, surfaceWidth, surfaceHeight); //SurfaceRenderer
    	Matrix.setRotateM(matrix, 0, 0, 1, 0, 0);
    	videoRenderer.setMatrix(matrix);
        
        initMediaPlayer();
    }

    private void initMediaPlayer() {
        try {
            this.mediaPlayer = new MediaPlayer();

            while (videoRenderer.getVideoTexture() == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            Surface surface = new Surface(videoRenderer.getVideoTexture());
            mediaPlayer.setDataSource(videoPath);
            mediaPlayer.setSurface(surface);
            //mediaPlayer.setSurface(surfaceView.getHolder().getSurface());
            
            surface.release();

            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setLooping(true);
        } catch (IllegalArgumentException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (SecurityException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IllegalStateException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }
    @Override
    public void onPrepared(MediaPlayer mp) {
        try {
            if (mp != null) {
                mp.start();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this,mRotation,SensorManager.SENSOR_DELAY_GAME); 
        Log.v(TAG, "GLViewMediaActivity::onResume()");
    }

    @Override protected void onStart()
    {
        Log.v(TAG, "GLViewMediaActivity::onStart()");
        super.onStart();
    }

    @Override
    protected void onPause() {
        Log.v(TAG, "GLViewMediaActivity::onPause()");
        super.onPause();
        mSensorManager.unregisterListener(this);
        
        if (videoRenderer != null) {
            videoRenderer.onPause();
            videoRenderer = null;
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer =null;
        }
    }

    @Override protected void onStop()
    {
        Log.v(TAG, "GLViewMediaActivity::onStop()");
        super.onStop();
    }

    @Override protected void onDestroy()
    {
        Log.v(TAG, "GLViewMediaActivity::onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        SensorManager.getRotationMatrixFromVector(matrix,event.values);
        videoRenderer.setMatrix(matrix);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Log.v( TAG, "GLViewMediaActivity::onSurfaceTextureAvailable()"+ " tName:" + Thread.currentThread().getName() + "  tid:");

        surfaceWidth = width;
        surfaceHeight = height;
        playVideo(surface);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    /****************************************************************************************/

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.v( TAG, "GLViewMediaActivity::surfaceCreated()" );
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.v( TAG, "GLViewMediaActivity::surfaceChanged()" );
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.v( TAG, "GLViewMediaActivity::surfaceDestroyed()" );
    }
}
