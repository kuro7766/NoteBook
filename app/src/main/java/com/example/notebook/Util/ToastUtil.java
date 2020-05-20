package com.example.notebook.Util;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;


public class ToastUtil extends AbstractContextUtil{
    public static void showShort(Context context, String info) {
        Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
    }
    public static void showShort(final String info){
        new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(context,info,Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();
    }
    public static void showLong(final String info){
        new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(context,info,Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
    }
}
