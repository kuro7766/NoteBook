package com.example.notebook.Util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class FilePermissionUtil {
    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public interface PermissionCallBack{
        void callBack(String[] permissions, int[] grantResults);
    }
    public void requestPermission(Activity activity, final PermissionCallBack callBack) {
        if (Build.VERSION.SDK_INT > 23) {
            if (ContextCompat.checkSelfPermission(activity,
                    permissions[0])
                    == PackageManager.PERMISSION_GRANTED) {
                //授予权限
                Log.i("requestPermission:", "用户之前已经授予了权限！");
                if(callBack!=null){
                    callBack.callBack(permissions,null);
                }
            } else {
                //未获得权限
                Log.i("requestPermission:", "未获得权限，现在申请！");
//                activity.requestPermissions(permissions
//                        , REQUEST_PERMISSION_CODE);
                new ActResultRequest((FragmentActivity) activity)
                        .startPermissionRequestForResult(permissions, new ActResultRequest.ActResultRequestCallBackAdapter() {
                            @Override
                            public void onRequestPermissionResult(String[] permissions, int[] grantResults) {
                                super.onRequestPermissionResult(permissions, grantResults);
                                if(callBack!=null){
                                    callBack.callBack(permissions,grantResults);
                                }
                            }
                        });
            }
        }
    }
}
