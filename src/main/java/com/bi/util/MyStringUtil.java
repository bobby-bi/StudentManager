package com.bi.util;

import java.util.Date;

public class MyStringUtil {
    public static String generateSn(String prefix,String suffix){
        return prefix + new Date().getTime()+suffix;
    }
}
