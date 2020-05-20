package com.example.notebook.Util;

import android.content.Context;
import android.content.Intent;

public class ActivityManager extends AbstractContextUtil {
    public static void startActivity(Context context,Class c){
        context.startActivity(new Intent(context,c));
    }
    public static void startActivity(Class c){
        Intent intent=new Intent(context,c);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        context.startActivity(intent);
    }
}
