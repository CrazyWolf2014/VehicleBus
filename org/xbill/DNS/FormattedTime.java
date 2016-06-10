package org.xbill.DNS;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

final class FormattedTime {
    private static NumberFormat w2;
    private static NumberFormat w4;

    static {
        w2 = new DecimalFormat();
        w2.setMinimumIntegerDigits(2);
        w4 = new DecimalFormat();
        w4.setMinimumIntegerDigits(4);
        w4.setGroupingUsed(false);
    }

    private FormattedTime() {
    }

    public static String format(Date date) {
        Calendar gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        StringBuffer stringBuffer = new StringBuffer();
        gregorianCalendar.setTime(date);
        stringBuffer.append(w4.format((long) gregorianCalendar.get(1)));
        stringBuffer.append(w2.format((long) (gregorianCalendar.get(2) + 1)));
        stringBuffer.append(w2.format((long) gregorianCalendar.get(5)));
        stringBuffer.append(w2.format((long) gregorianCalendar.get(11)));
        stringBuffer.append(w2.format((long) gregorianCalendar.get(12)));
        stringBuffer.append(w2.format((long) gregorianCalendar.get(13)));
        return stringBuffer.toString();
    }

    public static Date parse(String str) throws TextParseException {
        if (str.length() != 14) {
            throw new TextParseException("Invalid time encoding: " + str);
        }
        Calendar gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        gregorianCalendar.clear();
        try {
            gregorianCalendar.set(Integer.parseInt(str.substring(0, 4)), Integer.parseInt(str.substring(4, 6)) - 1, Integer.parseInt(str.substring(6, 8)), Integer.parseInt(str.substring(8, 10)), Integer.parseInt(str.substring(10, 12)), Integer.parseInt(str.substring(12, 14)));
            return gregorianCalendar.getTime();
        } catch (NumberFormatException e) {
            throw new TextParseException("Invalid time encoding: " + str);
        }
    }
}
