package net.sourceforge.pinyin4j;

import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.xbill.DNS.WKSRecord.Service;
import org.xmlpull.v1.XmlPullParser;

class PinyinFormatter {
    PinyinFormatter() {
    }

    private static String convertToneNumber2ToneMark(String str) {
        String toLowerCase = str.toLowerCase();
        if (!toLowerCase.matches("[a-z]*[1-5]?")) {
            return toLowerCase;
        }
        if (!toLowerCase.matches("[a-z]*[1-5]")) {
            return toLowerCase.replaceAll("v", "\u00fc");
        }
        int numericValue = Character.getNumericValue(toLowerCase.charAt(toLowerCase.length() - 1));
        int indexOf = toLowerCase.indexOf(97);
        int indexOf2 = toLowerCase.indexOf(Service.HOSTNAME);
        int indexOf3 = toLowerCase.indexOf("ou");
        if (-1 != indexOf) {
            indexOf2 = 97;
        } else if (-1 != indexOf2) {
            indexOf = indexOf2;
            indexOf2 = Service.HOSTNAME;
        } else if (-1 != indexOf3) {
            char charAt = "ou".charAt(0);
            indexOf = indexOf3;
        } else {
            indexOf = toLowerCase.length() - 1;
            while (indexOf >= 0) {
                if (String.valueOf(toLowerCase.charAt(indexOf)).matches("[aeiouv]")) {
                    indexOf2 = toLowerCase.charAt(indexOf);
                    break;
                }
                indexOf--;
            }
            indexOf = -1;
            indexOf2 = 36;
        }
        if (36 == indexOf2 || -1 == indexOf) {
            return toLowerCase;
        }
        charAt = "\u0101\u00e1\u0103\u00e0a\u0113\u00e9\u0115\u00e8e\u012b\u00ed\u012d\u00eci\u014d\u00f3\u014f\u00f2o\u016b\u00fa\u016d\u00f9u\u01d6\u01d8\u01da\u01dc\u00fc".charAt(("aeiouv".indexOf(indexOf2) * 5) + (numericValue - 1));
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(toLowerCase.substring(0, indexOf).replaceAll("v", "\u00fc"));
        stringBuffer.append(charAt);
        stringBuffer.append(toLowerCase.substring(indexOf + 1, toLowerCase.length() - 1).replaceAll("v", "\u00fc"));
        return stringBuffer.toString();
    }

    static String formatHanyuPinyin(String str, HanyuPinyinOutputFormat hanyuPinyinOutputFormat) throws BadHanyuPinyinOutputFormatCombination {
        if (HanyuPinyinToneType.WITH_TONE_MARK == hanyuPinyinOutputFormat.getToneType() && (HanyuPinyinVCharType.WITH_V == hanyuPinyinOutputFormat.getVCharType() || HanyuPinyinVCharType.WITH_U_AND_COLON == hanyuPinyinOutputFormat.getVCharType())) {
            throw new BadHanyuPinyinOutputFormatCombination("tone marks cannot be added to v or u:");
        }
        if (HanyuPinyinToneType.WITHOUT_TONE == hanyuPinyinOutputFormat.getToneType()) {
            str = str.replaceAll("[1-5]", XmlPullParser.NO_NAMESPACE);
        } else if (HanyuPinyinToneType.WITH_TONE_MARK == hanyuPinyinOutputFormat.getToneType()) {
            str = convertToneNumber2ToneMark(str.replaceAll("u:", "v"));
        }
        if (HanyuPinyinVCharType.WITH_V == hanyuPinyinOutputFormat.getVCharType()) {
            str = str.replaceAll("u:", "v");
        } else if (HanyuPinyinVCharType.WITH_U_UNICODE == hanyuPinyinOutputFormat.getVCharType()) {
            str = str.replaceAll("u:", "\u00fc");
        }
        return HanyuPinyinCaseType.UPPERCASE == hanyuPinyinOutputFormat.getCaseType() ? str.toUpperCase() : str;
    }
}
