package com.example.notebook.Util;

import java.io.IOException;
import java.io.Serializable;

public class GlobalVar extends AbstractContextUtil{

    private static final String GLOBAL_VALUE_DEFAULT="m_global_default_value";

    public static boolean set(String varName, Serializable object){
        try {
            SharedPreferenceUtil.saveSerializableObject(context,GLOBAL_VALUE_DEFAULT,varName,object);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Object get(String varName){
        try {
            return SharedPreferenceUtil.getSerializableObject(context,GLOBAL_VALUE_DEFAULT,varName);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
