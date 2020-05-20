package com.example.notebook.Util;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;

import com.example.notebook.MainActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Photos extends AbstractContextUtil{
    private static Uri imageUri;
    public static void take(final TCallBack<Bitmap> callBack, FragmentActivity activity){

        File outputImage=new File(context.getExternalCacheDir(),"output_image.jpg");
        if(outputImage.exists()){
            outputImage.delete();
        }
        try {
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(Build.VERSION.SDK_INT>=24){
            imageUri=FileProvider.getUriForFile(context,"com.example.notebook.fileprovider",outputImage);
        }else {
            imageUri=Uri.fromFile(outputImage);
        }
        Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        new ActResultRequest(activity).startActivityForResult(intent, new ActResultRequest.Callback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(imageUri));
                    callBack.call(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRequestPermissionResult(String[] permissions, int[] grantResults) {

            }
        });
    }
}
