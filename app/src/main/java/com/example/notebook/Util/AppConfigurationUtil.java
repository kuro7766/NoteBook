package com.example.notebook.Util;

import java.io.Serializable;

public class AppConfigurationUtil {
    private Serializable serializableConfiguration;
    public String defaultKey="wy_appconfigurationutil_default_key";
    public void OnResume(){
        if(serializableConfiguration!=null){
            save();
        }
    }
    private void save(){
        SharedPreferenceUtil.saveSerializableObject(defaultKey,defaultKey,serializableConfiguration);
    }
    public <T extends Serializable> T read(OnFirstRead onFirstRead){
        serializableConfiguration= (Serializable) SharedPreferenceUtil.getSerializableObject(defaultKey,defaultKey);
        if(serializableConfiguration==null){
            serializableConfiguration=onFirstRead.onFirstCreate();
        }
        return (T) serializableConfiguration;
    }
    public interface OnFirstRead{
        Serializable onFirstCreate();
    }
}
