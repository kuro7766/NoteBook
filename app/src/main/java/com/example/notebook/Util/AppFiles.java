package com.example.notebook.Util;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppFiles extends AbstractContextUtil {
    public static String getPathDir(String s) {
        Pattern pattern = Pattern.compile("^.*(?=\\/.*?$)");
        Matcher matcher = pattern.matcher(s);
        matcher.find();
        return matcher.group();
    }

    public static void saveToFile(String content, String dir) {
        String convertPath = context.getCacheDir().getAbsolutePath() + "/" + dir;
        new File(convertPath).getParentFile().mkdirs();
        new FileOperator(convertPath).write(content);
    }

    public static String readFromFile(String dir) {
        String convertPath = context.getCacheDir().getAbsolutePath() + "/" + dir;
        if (new File(convertPath).exists())
            return new FileOperator(convertPath).read();
        else
            return null;
    }
}
