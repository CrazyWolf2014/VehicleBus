package com.ifoer.expedition.BluetoothChat;

import org.xmlpull.v1.XmlPullParser;

public class DataStreamUtils {
    public static boolean isDigit(String streamStr) {
        if (XmlPullParser.NO_NAMESPACE.equals(streamStr)) {
            return false;
        }
        char[] temC = streamStr.toCharArray();
        int count = 0;
        for (int i = 0; i < streamStr.length(); i++) {
            int tempC = temC[i];
            if ((tempC < 48 || tempC > 57) && tempC != 46 && tempC != 45) {
                return false;
            }
            if (tempC == 45) {
                count++;
            }
        }
        if (count <= 1) {
            return true;
        }
        return false;
    }

    public static boolean isNum(String str) {
        return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
