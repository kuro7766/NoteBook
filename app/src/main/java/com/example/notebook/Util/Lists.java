package com.example.notebook.Util;

import java.util.ArrayList;
import java.util.List;

public class Lists {
    public static <T> List<T> copy(List<T> list) {
        List<T> copied = new ArrayList<>();
        for (T t : list) {
            copied.add(t);
        }
        return copied;
    }
}
