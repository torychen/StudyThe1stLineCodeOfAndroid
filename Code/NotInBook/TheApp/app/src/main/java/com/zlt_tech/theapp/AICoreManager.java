package com.zlt_tech.theapp;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;

import java.util.Observable;
import java.util.Observer;

public class AICoreManager extends Observable implements Observer {
    private static final String TAG = "AICoreManager";
    private Bitmap mBitmap;

    AICoreManager() {
        Log.d(TAG, "AICoreManager: ctor.");
    }

    public boolean init(Observer observer) {
        Log.d(TAG, "init: ");

        mBitmap = null;

        this.addObserver(observer);
        return true;
    }

    @Override
    public void update(Observable observable, Object o) {
        mBitmap = (Bitmap) o;

        informResult();
    }

    //For IT purpose.
    private boolean IsForIT = true;
    private int start = 200;
    private int stride = 300;
    private int right = start + stride;
    private int bottom = start + stride;
    private void informResult() {
        //Log.d(TAG, "informResult: ");

        //For IT.
        Rect[] rects;
        if (IsForIT) {
            rects = new Rect[1];
            rects[0] = new Rect(start,start,right,bottom);
            right+=20;
            bottom+=20;

            if (right - (start + stride) > stride) {
                right = start + stride;
                bottom = start + stride;
            }
        } else {
            //TODO the real algorithm.
            return;
        }

        if (mBitmap != null && rects != null) {

            AIResult aiResult = new AIResult(mBitmap, rects);

            setChanged();
            notifyObservers(aiResult);
        }
    }

    public void quit() {
        Log.d(TAG, "quit: ");
    }
}
