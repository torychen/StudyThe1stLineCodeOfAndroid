package com.zlt_tech.theapp;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class AIResult {
    private Bitmap mBitmap;
    private Rect[] mRects;

    AIResult(Bitmap bitmap, Rect[] rects) {
        mBitmap = bitmap;
        mRects = rects;
    }

    public Bitmap getmBitmap() {
        return mBitmap;
    }

    public Rect[] getmRects() {
        return mRects;
    }
}
