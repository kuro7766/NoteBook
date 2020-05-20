package com.example.notebook.Util;

import java.io.IOException;

public class CheckFirstOperationUtil extends AbstractContextUtil{

    public static final String checkFirstOperationUtilFileKey="wy_checkfirst_operation_file_name";

    public static void resetIsFirst(String key) {
        try {
            SharedPreferenceUtil.saveSerializableObject(context,checkFirstOperationUtilFileKey,key,false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void firstSave(String key) {
        try {
            SharedPreferenceUtil.saveSerializableObject(context,checkFirstOperationUtilFileKey,key,true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isFirst(String key) {
        Object o = null;
        try {
            o = SharedPreferenceUtil.getSerializableObject(context,checkFirstOperationUtilFileKey,key);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (o == null || o.equals(false)) {
            firstSave(key);
            return true;
        } else {
            return false;
        }
    }
}
