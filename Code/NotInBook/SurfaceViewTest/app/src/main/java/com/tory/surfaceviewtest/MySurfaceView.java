package com.tory.surfaceviewtest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable {
    private SurfaceHolder m_Holder;
    private Canvas m_Canvas;
    private boolean m_IsDrawing;

    private void init() {
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

    private void draw() {
        try {
            m_Canvas = m_Holder.lockCanvas();
            m_Canvas.drawColor(Color.GREEN);
            m_Canvas.drawLine(0, 0, 50, 50, null);
        } catch (Exception e) {

        } finally {
            if (m_Canvas != null) {
                m_Holder.unlockCanvasAndPost(m_Canvas);

            }
        }

    }
}
