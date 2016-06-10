package com.cnlaunch.x431pro.utils;

import android.text.TextUtils;
import com.cnlaunch.framework.utils.NLog;
import java.util.List;
import java.util.regex.Pattern;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.smile.SmileConstants;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xmlpull.v1.XmlPullParser;

public class StringUtils {
    private static final String tag;

    static {
        tag = StringUtils.class.getSimpleName();
    }

    public static boolean isEmpty(String str) {
        if (TextUtils.isEmpty(str) || XmlPullParser.NO_NAMESPACE.equals(str)) {
            return true;
        }
        return false;
    }

    public static <T> boolean isEmpty(List<T> list) {
        if (list == null || list.isEmpty()) {
            return true;
        }
        return false;
    }

    public static int getRandomIndex(int size) {
        return (int) (Math.random() * ((double) size));
    }

    public static int getStrlength(String value) {
        int valueLength = 0;
        String chinese = "[\u0391-\uffe5]";
        for (int i = 0; i < value.length(); i++) {
            if (value.substring(i, i + 1).matches(chinese)) {
                valueLength += 2;
            } else {
                valueLength++;
            }
        }
        NLog.m917e(tag, "getStrlength : " + valueLength);
        return valueLength;
    }

    public static boolean isNumber(String str) {
        return Pattern.compile("[0-9]*").matcher(str).matches();
    }

    public static boolean isEmail(String email) {
        return Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$").matcher(email).matches();
    }

    public static boolean isEmailCharacter(String email) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_\\-\\.]+$");
        if (email.indexOf(64) == -1) {
            return pattern.matcher(email).matches();
        }
        if (email.length() <= 1) {
            return false;
        }
        if (email.indexOf(64) == email.length() - 1) {
            return pattern.matcher(email.substring(0, email.length() - 1)).matches();
        }
        if (email.indexOf(64) == 0) {
            return false;
        }
        if (email.lastIndexOf(46) == email.length() - 1) {
            return isEmail(new StringBuilder(String.valueOf(email)).append("cn").toString());
        }
        return isEmail(new StringBuilder(String.valueOf(email)).append(".cn").toString());
    }

    public static boolean isPasswordValid(String str) {
        return str.matches("^[a-zA-Z0-9_@]{6,20}$");
    }

    public static boolean isPasswordInvalidChar(String str) {
        return str.matches("^[a-zA-Z0-9_@]+$");
    }

    public static boolean isAcountPassword(String str) {
        if (str.length() <= 5 || str.length() >= 21 || !Pattern.compile("^[a-zA-Z0-9_@]+$").matcher(str).matches()) {
            return false;
        }
        return true;
    }

    public static boolean isPassword(String str) {
        if (Pattern.compile("^[a-zA-Z0-9_@]+$").matcher(str).matches()) {
            return true;
        }
        return false;
    }

    public static boolean isIdentifyCode(String str) {
        if (str.length() == 4 && isNumber(str)) {
            return true;
        }
        return false;
    }

    public static boolean isMobile(String mobile) {
        return Pattern.compile("^\\d{6,11}$").matcher(mobile).matches();
    }

    public static boolean isChinese(String str) {
        return Pattern.compile("[\u0391-\uffe5]+$").matcher(str).matches();
    }

    public static Boolean isAcount(String str) {
        Boolean bl = Boolean.valueOf(false);
        if (Pattern.compile("^[a-zA-Z][0-9a-zA-Z_]{4,20}$").matcher(str).matches()) {
            return Boolean.valueOf(true);
        }
        return bl;
    }

    public static Boolean isAcountCharacter(String str) {
        Boolean bl = Boolean.valueOf(false);
        if (Pattern.compile("(^[a-zA-Z][0-9a-zA-Z_]+$)|(^[a-zA-Z]$)").matcher(str).matches()) {
            return Boolean.valueOf(true);
        }
        return bl;
    }

    public static boolean isAmericaZipCode(String str) {
        return str.matches("^(\\d{5})$");
    }

    public static boolean isCanadaZipCode(String str) {
        Boolean isCanadaZipcode = Boolean.valueOf(false);
        return Boolean.valueOf(str.matches("^([ABCEGHJKLMNPRSTVXYabceghjklmnprstvxy]\\d[A-Za-z][ -]\\d[A-Za-z]\\d)$")).booleanValue();
    }

    public static boolean compareVersion(String versionNo, String maxOldVersion) {
        if (TextUtils.isEmpty(versionNo)) {
            return false;
        }
        if (TextUtils.isEmpty(maxOldVersion)) {
            return true;
        }
        if (versionNo.compareToIgnoreCase(maxOldVersion) > 0) {
            return true;
        }
        return false;
    }

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

    public static int toNaturalNumber(String s) {
        int n;
        try {
            n = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            n = 0;
        }
        return n > 0 ? n : 0;
    }

    public static String decodeUnicode(String theString) {
        if (TextUtils.isEmpty(theString)) {
            return null;
        }
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        int x = 0;
        while (x < len) {
            int x2 = x + 1;
            char aChar = theString.charAt(x);
            if (aChar == '\\') {
                x = x2 + 1;
                aChar = theString.charAt(x2);
                if (aChar == 'u') {
                    int value = 0;
                    int i = 0;
                    while (i < 4) {
                        x2 = x + 1;
                        aChar = theString.charAt(x);
                        switch (aChar) {
                            case Type.DNSKEY /*48*/:
                            case Service.LOGIN /*49*/:
                            case Type.NSEC3 /*50*/:
                            case Service.LA_MAINT /*51*/:
                            case Type.TLSA /*52*/:
                            case SimpleResolver.DEFAULT_PORT /*53*/:
                            case Opcodes.ISTORE /*54*/:
                            case Service.ISI_GL /*55*/:
                            case SmileConstants.MAX_SHORT_NAME_UNICODE_BYTES /*56*/:
                            case Opcodes.DSTORE /*57*/:
                                value = ((value << 4) + aChar) - 48;
                                break;
                            case Service.TACACS_DS /*65*/:
                            case Protocol.RVD /*66*/:
                            case Service.BOOTPS /*67*/:
                            case Service.BOOTPC /*68*/:
                            case Service.TFTP /*69*/:
                            case 'F':
                                value = (((value << 4) + 10) + aChar) - 65;
                                break;
                            case Service.SWIFT_RVF /*97*/:
                            case Service.TACNEWS /*98*/:
                            case Service.METAGRAM /*99*/:
                            case ParseCharStream.HISTORY_LENGTH /*100*/:
                            case Service.HOSTNAME /*101*/:
                            case Service.ISO_TSAP /*102*/:
                                value = (((value << 4) + 10) + aChar) - 97;
                                break;
                            default:
                                NLog.m917e(tag, "Malformed encoding.aChar=" + aChar);
                                break;
                        }
                        i++;
                        x = x2;
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't') {
                        aChar = '\t';
                    } else if (aChar == 'r') {
                        aChar = '\r';
                    } else if (aChar == 'n') {
                        aChar = '\n';
                    } else if (aChar == 'f') {
                        aChar = '\f';
                    }
                    outBuffer.append(aChar);
                }
            } else {
                outBuffer.append(aChar);
                x = x2;
            }
        }
        return outBuffer.toString();
    }
}
