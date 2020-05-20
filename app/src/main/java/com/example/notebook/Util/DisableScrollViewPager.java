package com.example.notebook.Util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class DisableScrollViewPager extends ViewPager {
    private boolean scrollable=true;


    public DisableScrollViewPager(@NonNull Context context) {
        super(context);
    }

    public DisableScrollViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(scrollable){
            return super.onInterceptTouchEvent(ev);
        }
        return this.scrollable && super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(scrollable){
            return super.onTouchEvent(ev);
        }
        return this.scrollable && super.onTouchEvent(ev);
    }



    public void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
    }
}
