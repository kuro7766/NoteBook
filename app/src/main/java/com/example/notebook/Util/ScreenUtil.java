package com.example.notebook.Util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class ScreenUtil extends AbstractContextUtil {
    private static DisplayMetrics dm;

    public static int convert(String floatValue) {
        DisplayMetrics dm = getDm();
        if (floatValue.matches("[Hh].*")) {
            return (int) (dm.heightPixels * Float.parseFloat(floatValue.replaceAll("[Hh]", "")));
        } else if (floatValue.matches("[Ww].*")) {
            return (int) (dm.widthPixels * Float.parseFloat(floatValue.replaceAll("[Ww]", "")));
        }
        return 0;
    }

    public static int centerHorizontal() {
        DisplayMetrics dm = getDm();
        return dm.widthPixels / 2;
    }

    public static int centerVerticle() {
        if (dm == null) {
            dm = context.getResources().getDisplayMetrics();
        }
        return dm.heightPixels / 2;
    }

    private static DisplayMetrics getDm() {
        if (dm == null) {
            dm = context.getResources().getDisplayMetrics();
        }
        return dm;
    }

    public static int width(){
        return getDm().widthPixels;
    }
    public static int height(){
        return getDm().heightPixels;
    }

    public static Position calPosition4(float x,float y){
        return calPosition4((int)x,(int)y);
    }
    public static Position calPosition4(int x,int y){
        if(x<centerHorizontal()&&y>centerVerticle()){
            return Position.leftDown;
        }
        if(x<centerHorizontal()&&y<centerVerticle()){
            return Position.leftUp;
        }
        if(x>centerHorizontal()&&y>centerVerticle()){
            return Position.rightDown;
        }
        if(x>centerHorizontal()&&y<centerVerticle()){
            return Position.rightUp;
        }
        return Position.defaultPosition;
    }
    public enum Position{
        leftUp,rightUp,leftDown,rightDown,defaultPosition
    }
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }
    public static float px2dp(Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }
    public static float px2sp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }
}


