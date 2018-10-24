package com.zlt_tech.theapp;

import android.content.Context;
import android.widget.Toast;

public class MyUtility {
    static public void Toast(final Context context, final String text)
    {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
