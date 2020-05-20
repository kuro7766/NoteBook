package com.example.notebook.Util;

import android.app.Application;
import android.content.Context;

public class AbstractContextUtil extends Application {
    public static Context context;
    public static void onApplicationInit(Context context){
        AbstractContextUtil.context=context;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        if(context==null)
            context=getApplicationContext();
    }
}
