package com.example.notebook.Util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SharedPreferenceUtil extends AbstractContextUtil{


    public static String getPathDir(String s) {
        Pattern pattern = Pattern.compile("^.*(?=\\/.*?$)");
        Matcher matcher = pattern.matcher(s);
        matcher.find();
        return matcher.group();
    }


    public static void save(String path, Object x) {
        ObjectOutputStream out = null;
        File f = new File(path);
        File dir = new File(getPathDir(path));
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            out = new ObjectOutputStream(new FileOutputStream(f));
            out.writeObject(x);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object read(String path) throws IOException, ClassNotFoundException {
        ObjectInputStream in = null;
        Object y = null;

        in = new ObjectInputStream(new FileInputStream(new File(path)));
        y = in.readObject();
        in.close();

        return y;
    }


    public static void openFile(Context context, String file) {
        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(file)), MapTable.getMIMEType(file));
            context.startActivity(intent);
            Intent.createChooser(intent, "请选择对应的软件打开该附件！");
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "sorry附件不能打开，请下载相关软件！", Toast.LENGTH_SHORT).show();
        }
    }

    /*
     * 保存序列化对象到本地
     *
     * @param context
     * @param fileName SP本地存储路径
     * @param key
     * @param object
     */
    public static void saveSerializableObject(Context context, String fileName, String key, Object object) throws IOException {
        SharedPreferences.Editor spEdit = context.getSharedPreferences(fileName, Context.MODE_PRIVATE).edit();
        //先将序列化结果写到byte缓存中，其实就分配一个内存空间
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);
        os.writeObject(object);//将对象序列化写入byte缓存
        //将序列化的数据转为16进制保存
        String bytesToHexString = bytesToHexString(bos.toByteArray());
        //保存该16进制数组
        spEdit.putString(key, bytesToHexString);
        spEdit.commit();
    }

    public static void saveSerializableObject(String fileName, String key, Object object){
        try {
            saveSerializableObject(context,fileName,key,object);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        /**
         * desc:将数组转为16进制
         *
         * @param bArray
         * @return
         */
    public static String bytesToHexString(byte[] bArray) {
        if (bArray == null) {
            return null;
        }
        if (bArray.length == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }


    /**
     * 用sp从本地反序列化获取对象
     *
     * @param context
     * @param key
     * @return
     */
    public static Object getSerializableObject(Context context, String fileName, String key) throws IOException, ClassNotFoundException {
        SharedPreferences sp = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        if (sp.contains(key)) {
            String string = sp.getString(key, "");
            if (TextUtils.isEmpty(string)) {
                return null;
            } else {
                //将16进制的数据转为数组，准备反序列化
                byte[] stringToBytes = StringToBytes(string);
                ByteArrayInputStream bis = new ByteArrayInputStream(stringToBytes);
                ObjectInputStream is = new ObjectInputStream(bis);
                //返回反序列化得到的对象
                Object readObject = is.readObject();
                return readObject;
            }
        }
        return null;
    }

    public static Object getSerializableObject( String fileName, String key){
        try {
            return getSerializableObject(context,fileName,key);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * desc:将16进制的数据转为数组
     *
     * @param data
     * @return
     */
    public static byte[] StringToBytes(String data) {
        String hexString = data.toUpperCase().trim();
        if (hexString.length() % 2 != 0) {
            return null;
        }
        byte[] retData = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i++) {
            int int_ch;  // 两位16进制数转化后的10进制数
            char hex_char1 = hexString.charAt(i); //两位16进制数中的第一位(高位*16)
            int int_ch1;
            if (hex_char1 >= '0' && hex_char1 <= '9')
                int_ch1 = (hex_char1 - 48) * 16;   // 0 的Ascll - 48
            else if (hex_char1 >= 'A' && hex_char1 <= 'F')
                int_ch1 = (hex_char1 - 55) * 16; // A 的Ascll - 65
            else
                return null;
            i++;
            char hex_char2 = hexString.charAt(i); //两位16进制数中的第二位(低位)
            int int_ch2;
            if (hex_char2 >= '0' && hex_char2 <= '9')
                int_ch2 = (hex_char2 - 48); // 0 的Ascll - 48
            else if (hex_char2 >= 'A' && hex_char2 <= 'F')
                int_ch2 = hex_char2 - 55; // A 的Ascll - 65
            else
                return null;
            int_ch = int_ch1 + int_ch2;
            retData[i / 2] = (byte) int_ch;//将转化后的数放入Byte里
        }
        return retData;
    }
}
