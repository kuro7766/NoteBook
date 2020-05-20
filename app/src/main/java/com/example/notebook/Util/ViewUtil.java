package com.example.notebook.Util;

import android.view.ViewGroup;

public class ViewUtil {
    public static int getWrapContentHeight(ViewGroup v){
        v.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return v.getMeasuredHeight();
    }
    public static int getWrapContentWidth(ViewGroup v){
        v.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        return v.getMeasuredWidth();
    }
}
