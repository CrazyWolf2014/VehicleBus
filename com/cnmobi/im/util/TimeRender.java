package com.cnmobi.im.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeRender {
    private static SimpleDateFormat formatBuilder;

    static {
        formatBuilder = new SimpleDateFormat("MM-dd  HH:mm:ss");
    }

    public static String getDate() {
        return formatBuilder.format(new Date());
    }

    public static String getDate(Date date) {
        return formatBuilder.format(date);
    }
}
