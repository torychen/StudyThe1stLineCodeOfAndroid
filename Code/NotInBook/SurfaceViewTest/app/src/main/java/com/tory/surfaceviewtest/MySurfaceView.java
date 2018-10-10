package com.tory.surfaceviewtest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable {
    private SurfaceHolder m_Holder;
    private Canvas m_Canvas;
    private boolean m_IsDrawing;
    private Paint m_Paint;

    private void init() {
        m_Paint = new Paint();
        m_Paint.setColor(Color.WHITE);
        m_Paint.setStrokeWidth(20);
        m_Paint.setStyle(Paint.Style.FILL);

        m_Holder = getHolder();
        m_Holder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);
    }

    public MySurfaceView(Context context) {
        super(context);
        init();
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        m_IsDrawing = true;
        new Thread(this).start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        m_IsDrawing = false;
    }

    @Override
    public void run() {
        while (m_IsDrawing) {
            draw();
        }

    }

    private static final String TAG = "MySurfaceView";
    private void draw() {
        try {
            m_Canvas = m_Holder.lockCanvas();
            m_Canvas.drawColor(Color.BLACK);
            m_Canvas.drawPoint(200,300,m_Paint);
        } catch (Exception e) {
            Log.e(TAG, "draw: ", e);

        } finally {
            if (m_Canvas != null) {
                m_Holder.unlockCanvasAndPost(m_Canvas);

            }
        }

    }
}
