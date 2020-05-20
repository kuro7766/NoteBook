package com.example.notebook.Util;

import android.text.Html;
import android.text.Spanned;

public class HighLightStringForTextView {
    public static Spanned highLightString(String regex, String fullString) {
        String newString = fullString.replaceAll("("+regex+")", "<font color='red'>$1</font>");
        return Html.fromHtml(newString);
    }
}
