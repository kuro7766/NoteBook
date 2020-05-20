package com.example.notebook.Entity;

import android.content.Context;

import com.example.notebook.Util.AbstractContextUtil;

public class BaseContextWrapper {
    protected static Context context(){
        if(AbstractContextUtil.context==null){
            throw new RuntimeException("context has not been initialized!");
        }
        return AbstractContextUtil.context;
    }
}
