package com.example.notebook.Util;

import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;

public class AssetUtil extends AbstractContextUtil {
    public static InputStream getInputStream(String name){
        AssetManager am = context.getAssets();
        InputStream is=null;
        try {
            is = am.open(name);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return is;
    }
}
