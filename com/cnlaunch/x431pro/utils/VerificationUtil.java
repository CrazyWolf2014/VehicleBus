package com.cnlaunch.x431pro.utils;

import java.util.regex.Pattern;

public class VerificationUtil {
    public static boolean checkEmail(String email) {
        try {
            return Pattern.compile("^([a-zA-Z0-9]+[_|_|.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|_|.]?)*[a-zA-Z0-9]+.[a-zA-Z]{2,4}$").matcher(email).matches();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isMobileNO(String mobiles) {
        try {
            return mobiles.length() <= 16;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isMobileNO2Contact(String mobiles) {
        return Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$").matcher(mobiles).matches();
    }
}
