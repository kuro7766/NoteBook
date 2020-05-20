package com.example.notebook.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
    public static List<String> findAll(String s, String regex) {
        List<String> rt = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        while (matcher.find()) {
            String http = matcher.group();
            rt.add(http);
        }
        return rt;
    }

    public static String first(String s, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        String rt = null;
        if (matcher.find()) {
            String http = matcher.group();
            rt = http;
        } else {
            return null;
        }
        return rt;
    }
}
