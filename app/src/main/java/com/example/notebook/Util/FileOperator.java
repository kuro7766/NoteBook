package com.example.notebook.Util;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileOperator extends AbstractContextUtil {
    public static String ROOT = "/sdcard/";
    private String filePath;

    public FileOperator(String filePath) {
        this.filePath = filePath;
    }

    public String read() {
        StringBuilder sb = new StringBuilder();
        boolean appendN=false;
        try {
            FileReader in = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(in);
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
                appendN=true;
            }
            in.close();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (sb.length() > 1&&appendN)
            sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static String read(InputStream in) {
        StringBuilder sb = new StringBuilder();
        boolean appendN=false;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        String line = null;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
                appendN=true;
            }
            bufferedReader.close();
        } catch (IOException e) {
            return null;
        }
        if (sb.length() > 1&&appendN)
            sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public void write(String s) {
        //默认覆盖
        write(s, false);
    }

    public void write(String s, boolean append) {
        try {
            FileOutputStream o = new FileOutputStream(filePath, append);
            o.write(s.getBytes());
            o.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String fromAsset(String fileName) {
        BufferedReader reader = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            reader = new BufferedReader(
                    new InputStreamReader(context.getAssets().open(fileName)));

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                //process line
                stringBuilder.append(mLine);
                stringBuilder.append("\n");
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        return stringBuilder.toString();
    }

    public static void openFileChooser(FragmentActivity activity, ActResultRequest.Callback callback) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        new ActResultRequest(activity)
                .startActivityForResult(intent, callback);
    }


    public static <T> void openTxt(final FragmentActivity activity, final TCallBack<T> callBack) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*");
        new ActResultRequest(activity)
                .startActivityForResult(intent, new ActResultRequest.Callback() {
                    @Override
                    public void onActivityResult(int resultCode, Intent data) {
                        Uri uri = data.getData();
                        try {
                            InputStream in = activity.getContentResolver().openInputStream(uri);
                            callBack.call((T) FileOperator.read(in));
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
