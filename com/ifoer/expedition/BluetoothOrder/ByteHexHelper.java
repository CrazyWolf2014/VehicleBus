package com.ifoer.expedition.BluetoothOrder;

import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.expeditionphone.WelcomeActivity;
import com.ifoer.mine.Contact;
import com.ifoer.util.AndroidToLan;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.Locale;
import java.util.regex.Pattern;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xmlpull.v1.XmlPullParser;

public class ByteHexHelper {
    private static boolean f1286D;

    static {
        f1286D = false;
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder(XmlPullParser.NO_NAMESPACE);
        if (src == null || src.length <= 0) {
            return XmlPullParser.NO_NAMESPACE;
        }
        for (byte b : src) {
            String hv = Integer.toHexString(b & KEYRecord.PROTOCOL_ANY);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static String byteToHexString(byte src) {
        StringBuilder stringBuilder = new StringBuilder(XmlPullParser.NO_NAMESPACE);
        String hv = Integer.toHexString(src & KEYRecord.PROTOCOL_ANY);
        if (hv.length() < 2) {
            stringBuilder.append(0);
        }
        stringBuilder.append(hv);
        return stringBuilder.toString();
    }

    public static int byteToInt(byte src) {
        return src & KEYRecord.PROTOCOL_ANY;
    }

    public static byte[] intToHexBytes(int id) {
        String hexString = Integer.toHexString(id);
        int len = hexString.length();
        while (len < 2) {
            hexString = new StringBuilder(Contact.RELATION_ASK).append(hexString).toString();
            len = hexString.length();
        }
        return hexStringToBytes(hexString);
    }

    public static byte[] intToTwoHexBytes(int id) {
        String hexString = Integer.toHexString(id);
        int len = hexString.length();
        while (len < 4) {
            hexString = new StringBuilder(Contact.RELATION_ASK).append(hexString).toString();
            len = hexString.length();
        }
        return hexStringToBytes(hexString);
    }

    public static byte[] intToFourHexBytes(int id) {
        String hexString = Integer.toHexString(id);
        int len = hexString.length();
        while (len < 8) {
            hexString = new StringBuilder(Contact.RELATION_ASK).append(hexString).toString();
            len = hexString.length();
        }
        return hexStringToBytes(hexString);
    }

    public static byte[] intToFourHexBytesTwo(int id) {
        String hexString = Integer.toHexString(id);
        int len = hexString.length();
        if (len < 2) {
            hexString = new StringBuilder(Contact.RELATION_ASK).append(hexString).toString();
            len = hexString.length();
        }
        if (len % 2 == 1) {
            hexString = new StringBuilder(Contact.RELATION_ASK).append(hexString).toString();
            len = hexString.length();
        }
        int validLen = len / 2;
        while (len < 8) {
            hexString = new StringBuilder(String.valueOf(hexString)).append(Contact.RELATION_ASK).toString();
            len = hexString.length();
        }
        byte[] d = hexStringToBytes(hexString);
        int j = 0;
        for (int i = validLen - 1; j < i; i--) {
            byte temp = d[j];
            d[j] = d[i];
            d[i] = temp;
            j++;
        }
        return d;
    }

    public static byte intToHexByte(int id) {
        String hexString = Integer.toHexString(id);
        int len = hexString.length();
        while (len < 2) {
            hexString = new StringBuilder(Contact.RELATION_ASK).append(hexString).toString();
            len = hexString.length();
        }
        return hexStringToByte(hexString);
    }

    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals(XmlPullParser.NO_NAMESPACE)) {
            return new byte[0];
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) ((charToByte(hexChars[pos]) << 4) | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    public static byte hexStringToByte(String hexString) {
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) ((charToByte(hexChars[pos]) << 4) | charToByte(hexChars[pos + 1]));
        }
        return d[0];
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static String XOR(String hex) {
        byte bytes = (byte) 0;
        if (hex.length() > 0) {
            for (int i = 0; i < hex.length() / 2; i++) {
                bytes = (byte) (hexStringToByte(hex.substring(i * 2, (i * 2) + 2)) ^ bytes);
            }
        }
        return bytesToHexString(new byte[]{bytes});
    }

    public static String currentData() {
        StringBuffer stringBuffer = new StringBuffer();
        DecimalFormat decimalFormat = new DecimalFormat("00");
        Calendar calendar = Calendar.getInstance();
        String year = decimalFormat.format((long) calendar.get(1));
        String month = decimalFormat.format((long) (calendar.get(2) + 1));
        String day = decimalFormat.format((long) calendar.get(5));
        String hour = decimalFormat.format((long) calendar.get(11));
        String minute = decimalFormat.format((long) calendar.get(12));
        String second = decimalFormat.format((long) calendar.get(13));
        stringBuffer.append(year.substring(2, year.length())).append(month).append(day).append(hour).append(minute).append(second).append(decimalFormat.format((long) (calendar.get(7) - 1)));
        return stringBuffer.toString();
    }

    public static String RandomMethod() {
        String hexString = Integer.toHexString((int) (Math.random() * 100.0d));
        int len = hexString.length();
        while (len < 2) {
            hexString = new StringBuilder(Contact.RELATION_ASK).append(hexString).toString();
            len = hexString.length();
        }
        return hexString;
    }

    public static String packLength(String str) {
        String hexLength = Integer.toHexString(str.length() / 2);
        int len = hexLength.length();
        while (len < 4) {
            hexLength = new StringBuilder(Contact.RELATION_ASK).append(hexLength).toString();
            len = hexLength.length();
        }
        return hexLength;
    }

    public static String checkedSite(int site) {
        String hexLength = Integer.toHexString(site);
        int len = hexLength.length();
        while (len < 2) {
            hexLength = new StringBuilder(Contact.RELATION_ASK).append(hexLength).toString();
            len = hexLength.length();
        }
        return hexLength;
    }

    public static String packLength(int dataLen) {
        String hexLength = Integer.toHexString(dataLen);
        int len = hexLength.length();
        while (len < 4) {
            hexLength = new StringBuilder(Contact.RELATION_ASK).append(hexLength).toString();
            len = hexLength.length();
        }
        return hexLength;
    }

    public static int intPackLength(String str) {
        return Integer.valueOf(str, 16).intValue();
    }

    public static int intPackLength(byte[] str) {
        return Integer.valueOf(bytesToHexString(str), 16).intValue();
    }

    public static String packVerify(String target, String source, String packLengths, String counter, String commandWord, String dataArea) {
        return XOR(new StringBuilder(String.valueOf(target)).append(source).append(packLengths).append(counter).append(commandWord).append(dataArea).toString());
    }

    public static String dpuString(String str) {
        String buffer = XmlPullParser.NO_NAMESPACE;
        if (str == null || str.length() <= 0) {
            return buffer;
        }
        String result = bytesToHexString(new StringBuilder(String.valueOf(str)).append("\u0000").toString().getBytes());
        return packLength(result) + result;
    }

    public static String byteToWord(byte[] data) {
        String word = XmlPullParser.NO_NAMESPACE;
        if (data == null) {
            return word;
        }
        try {
            if (MainActivity.country == null || MainActivity.country.length() <= 0) {
                MainActivity.country = Locale.getDefault().getCountry();
            }
            switch (AndroidToLan.languages(MainActivity.country)) {
                case KEYRecord.OWNER_USER /*0*/:
                    return new String(data, "GB2312");
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    return new String(data, "GB2312");
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    return new String(data, "EUC-JP");
                case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                    return new String(data, "ISO-8859-1");
                case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                    return new String(data, "BIG5");
                case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                    return new String(data, "ISO-8859-1");
                case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                    return new String(data, "ISO-8859-1");
                case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                    return new String(data, "ISO-8859-5");
                case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                    return new String(data, "ISO-8859-1");
                case FileOptions.OPTIMIZE_FOR_FIELD_NUMBER /*9*/:
                    return new String(data, "ISO-8859-1");
                case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
                    return new String(data, "ISO-8859-2");
                case TrafficIncident.LOCATION_FIELD_NUMBER /*11*/:
                    return new String(data, "ISO-8859-9");
                case TrafficIncident.VERTEXOFFSET_FIELD_NUMBER /*12*/:
                    return new String(data, "ISO-8859-1");
                case TrafficIncident.INCIDENTVERTEX_FIELD_NUMBER /*13*/:
                    return new String(data, "ISO-8859-7");
                case TrafficIncident.STARTTIME_FIELD_NUMBER /*14*/:
                    return new String(data, "ISO-8859-2");
                case TrafficIncident.ENDTIME_FIELD_NUMBER /*15*/:
                    return new String(data, "ISO-8859-6");
                case FileOptions.CC_GENERIC_SERVICES_FIELD_NUMBER /*16*/:
                    return new String(data, "ISO-8859-5");
                case FileOptions.JAVA_GENERIC_SERVICES_FIELD_NUMBER /*17*/:
                    return new String(data, "ISO-8859-2");
                case FileOptions.PY_GENERIC_SERVICES_FIELD_NUMBER /*18*/:
                    return new String(data, "ISO-8859-1");
                case WelcomeActivity.GPIO_IOCQDATAOUT /*19*/:
                    return new String(data, "windows-1256");
                case FileOptions.JAVA_GENERATE_EQUALS_AND_HASH_FIELD_NUMBER /*20*/:
                    return new String(data, "euc-kr");
                case WelcomeActivity.GPIO_IOCSDATAHIGH /*21*/:
                    return new String(data, "ISO-8859-1");
                case Protocol.XNS_IDP /*22*/:
                    return new String(data, "ISO-8859-1");
                case Service.TELNET /*23*/:
                    return new String(data, "ISO-8859-2");
                default:
                    return new String(data, "GB2312");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return word;
        }
    }

    public static String hexStringToWord(String hexString) {
        return byteToWord(hexStringToBytes(hexString));
    }

    public static String binaryString2hexString(String bString) {
        if (bString == null || bString.equals(XmlPullParser.NO_NAMESPACE)) {
            return XmlPullParser.NO_NAMESPACE;
        }
        int i;
        if (bString.length() % 8 != 0) {
            int addLen = 8 - (bString.length() % 8);
            for (i = 0; i < addLen; i++) {
                bString = new StringBuilder(String.valueOf(bString)).append(Contact.RELATION_ASK).toString();
            }
        }
        StringBuffer tmp = new StringBuffer();
        i = 0;
        while (i < bString.length()) {
            try {
                int iTmp = 0;
                for (int j = 0; j < 4; j++) {
                    iTmp += Integer.parseInt(bString.substring(i + j, (i + j) + 1)) << ((4 - j) - 1);
                }
                tmp.append(Integer.toHexString(iTmp));
                i += 4;
            } catch (NumberFormatException e) {
                return XmlPullParser.NO_NAMESPACE;
            }
        }
        return tmp.toString();
    }

    public static String hexString2binaryString(String hexString) {
        if (hexString == null || hexString.length() % 2 != 0) {
            return null;
        }
        String bString = XmlPullParser.NO_NAMESPACE;
        for (int i = 0; i < hexString.length(); i++) {
            String tmp = "0000" + Integer.toBinaryString(Integer.parseInt(hexString.substring(i, i + 1), 16));
            bString = new StringBuilder(String.valueOf(bString)).append(tmp.substring(tmp.length() - 4)).toString();
        }
        return bString;
    }

    public static String replaceBlank(String str) {
        String dest = XmlPullParser.NO_NAMESPACE;
        if (str != null) {
            dest = Pattern.compile("\t|\r|\n").matcher(str).replaceAll(XmlPullParser.NO_NAMESPACE);
        }
        return dest.trim();
    }

    public static String replaceBlank1(String str) {
        String dest = XmlPullParser.NO_NAMESPACE;
        if (str != null) {
            dest = Pattern.compile("\t|\r|\n").matcher(str).replaceAll(XmlPullParser.NO_NAMESPACE);
        }
        if (dest.length() > 6) {
            String destfenge = dest.substring(0, 2);
            String destfenge1 = dest.substring(2, 4);
            String destfenge2 = dest.substring(4, 6);
            dest = new StringBuilder(String.valueOf(destfenge)).append(",").append(destfenge1).append(",").append(destfenge2).append(",").append(dest.substring(6, dest.length())).toString();
        }
        return dest.trim();
    }

    public static ArrayList<String> toStringArray(byte[] data) {
        if (data != null) {
            int total_bytes = data.length;
            if (total_bytes >= 3) {
                int walkthrough = 0;
                ArrayList<String> result_strings = new ArrayList();
                while (walkthrough < total_bytes - 1) {
                    int temp_len = (data[walkthrough] << 8) | data[walkthrough + 1];
                    byte[] str_bytes = new byte[(temp_len - 1)];
                    System.arraycopy(data, walkthrough + 2, str_bytes, 0, temp_len - 1);
                    result_strings.add(new String(str_bytes));
                    walkthrough += temp_len + 2;
                }
                return result_strings;
            }
        }
        return null;
    }

    public static byte[] appendByteArray(byte[] src, byte[] data) {
        if (src.length <= 0 || data.length <= 0) {
            throw new IllegalArgumentException("\u5b57\u8282\u6570\u7ec4\u53c2\u6570\u9519\u8bef");
        }
        byte[] ret = new byte[(src.length + data.length)];
        System.arraycopy(src, 0, ret, 0, src.length);
        System.arraycopy(data, 0, ret, src.length, data.length);
        return ret;
    }

    public static String calculateSingleFileMD5sum(File file) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        FileInputStream fis = new FileInputStream(file);
        byte[] buff = new byte[KEYRecord.OWNER_ZONE];
        while (true) {
            int readLen = fis.read(buff);
            if (readLen == -1) {
                break;
            }
            md5.update(buff, 0, readLen);
        }
        fis.close();
        StringBuilder sb = new StringBuilder();
        for (byte b : md5.digest()) {
            sb.append(new Formatter().format("%02x", new Object[]{Byte.valueOf(b)}));
        }
        return sb.toString();
    }

    private static String toHexUtil(int n) {
        String rt = XmlPullParser.NO_NAMESPACE;
        switch (n) {
            case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
                return new StringBuilder(String.valueOf(rt)).append("A").toString();
            case TrafficIncident.LOCATION_FIELD_NUMBER /*11*/:
                return new StringBuilder(String.valueOf(rt)).append("B").toString();
            case TrafficIncident.VERTEXOFFSET_FIELD_NUMBER /*12*/:
                return new StringBuilder(String.valueOf(rt)).append("C").toString();
            case TrafficIncident.INCIDENTVERTEX_FIELD_NUMBER /*13*/:
                return new StringBuilder(String.valueOf(rt)).append("D").toString();
            case TrafficIncident.STARTTIME_FIELD_NUMBER /*14*/:
                return new StringBuilder(String.valueOf(rt)).append("E").toString();
            case TrafficIncident.ENDTIME_FIELD_NUMBER /*15*/:
                return new StringBuilder(String.valueOf(rt)).append("F").toString();
            default:
                return new StringBuilder(String.valueOf(rt)).append(n).toString();
        }
    }

    public static String toHex(int n) {
        StringBuilder sb = new StringBuilder();
        if (n / 16 == 0) {
            return toHexUtil(n);
        }
        sb.append(toHex(n / 16)).append(toHexUtil(n % 16));
        return sb.toString();
    }

    public static String parseAscii(String str) {
        StringBuilder sb = new StringBuilder();
        byte[] bs = str.getBytes();
        for (byte toHex : bs) {
            sb.append(toHex(toHex));
        }
        return sb.toString();
    }
}
