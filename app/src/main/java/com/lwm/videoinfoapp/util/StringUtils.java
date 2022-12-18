package com.lwm.videoinfoapp.util;

public class StringUtils {

    public static boolean isEmpty(String str) {
        if (str == null || str.length() <= 0) {
            return true;  // str为空
        } else {
            return false; // str不为空
        }
    }
}
