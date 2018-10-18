package com.example.jarry.playvideo_texuture;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import java.util.List;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.example.jarry.R;

/**
 * Created by aiya on 2017/5/19.
 */

public class VrContextActivity extends Activity implements GLSurfaceView.Renderer,SensorEventListener {

    private GLSurfaceView mGLView;
    private SensorManager mSensorManager;
    private Sensor mRotation;
    private SkySphere mSkySphere;
    private final float TOUCH_SCALE_FACTOR = 360.0f/1280;//180.0f/320;//角度缩放比例
    private float mPreviousY;//上次的触控位置Y坐标
    private float mPreviousX;//上次的触控位置X坐标
    float yAngle;//绕Y轴旋转的角度
	float xAngle; //绕X轴旋转的角度
	
    private float[] matrix=new float[16];
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.glview0);

        mSensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensors=mSensorManager.getSensorList(Sensor.TYPE_ALL);
        //todo 鍒ゆ柇鏄惁瀛樺湪rotation vector sensor
        mRotation=mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        mGLView=(GLSurfaceView) findViewById(R.id.mGLView);
        mGLView.setEGLContextClientVersion(2);
        mGLView.setRenderer(this);
        mGLView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        
        /*
        mGLView.setOnTouchListener(new OnTouchListener() {  
            
            public boolean onTouch(View v, MotionEvent e) {  
                // TODO Auto-generated method stub 
            	float y = e.getY();
                float x = e.getX();
                switch (e.getAction()) {  
                case MotionEvent.ACTION_MOVE://检测到点击事件时 
                	float dy = y - mPreviousY;//计算触控笔Y位移
                    float dx = x - mPreviousX;//计算触控笔X位移
                    
                    yAngle += dx;// * TOUCH_SCALE_FACTOR;//设置绕y轴旋转角度
                    xAngle += dy;// * TOUCH_SCALE_FACTOR;//设置绕x轴旋转角度
                    
                    //MatrixState.rotate(20f, 0, 1, 0);//绕y轴旋转
                    // if(xAngle> yAngle) {
                       Matrix.rotateM(matrix, 0, xAngle, 1, 0, 0);
                    //}else {
                       Matrix.rotateM(matrix, 0, yAngle, 0, 1, 0);
                    //}
                    mSkySphere.setMatrix(matrix);
                    break;
                case MotionEvent.ACTION_UP:
                	yAngle = 0;
                	yAngle = 0;
                	break;              	
                }  
                mPreviousY = y;//记录触控笔位置
                mPreviousX = x;//记录触控笔位置
                return true;  
            }  
        });  
        */

        mSkySphere=new SkySphere(this.getApplicationContext(),"vr/mypic.png"); //vr/360sp.jpg
        
        Matrix.setRotateM(matrix, 0, 0, 1, 0, 0);
        mSkySphere.setMatrix(matrix);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this,mRotation,SensorManager.SENSOR_DELAY_GAME); //SENSOR_DELAY_GAME SENSOR_DELAY_FASTEST
        mGLView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
        mGLView.onPause();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mSkySphere.create();
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
        GLES20.glCullFace(GLES20.GL_FRONT);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mSkySphere.setSize(width, height);
        GLES20.glViewport(0,0,width,height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT|GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glClearColor(1,1,1,1);
        mSkySphere.draw();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        SensorManager.getRotationMatrixFromVector(matrix,event.values);
        mSkySphere.setMatrix(matrix);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
