package com.cnlaunch.x431pro.utils.date;

import android.annotation.SuppressLint;
import com.cnlaunch.framework.utils.NLog;
import com.tencent.mm.sdk.platformtools.Util;
import hirondelle.date4j.DateTime;
import hirondelle.date4j.DateTime.DayOverflow;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import org.xmlpull.v1.XmlPullParser;

public class DateUtils {
    public static final String UPDATETIME = "updatetime";
    private static final String tag;

    static {
        tag = DateUtils.class.getSimpleName();
    }

    public static String currentDateTime() {
        return currentDateTime(DateStyle.YYYY_MM_DD);
    }

    public static String currentDateTime(String dateStyle) {
        String result = DateTime.now(TimeZone.getDefault()).format(dateStyle);
        NLog.m917e(tag, "currentDateTime: " + result);
        return result;
    }

    private void daysTillChristmas() {
        DateTime today = DateTime.today(TimeZone.getDefault());
        DateTime christmas = DateTime.forDateOnly(today.getYear(), Integer.valueOf(12), Integer.valueOf(25));
        if (!today.isSameDayAs(christmas)) {
            int result;
            if (today.lt(christmas)) {
                result = today.numDaysFrom(christmas);
            } else if (today.gt(christmas)) {
                result = today.numDaysFrom(DateTime.forDateOnly(Integer.valueOf(today.getYear().intValue() + 1), Integer.valueOf(12), Integer.valueOf(25)));
            }
        }
    }

    public static String plusDayFromToday(int plusDay) {
        return DateTime.today(TimeZone.getDefault()).plusDays(Integer.valueOf(plusDay)).format(DateStyle.YYYY_MM_DD);
    }

    public static String plusMonthDayFromToday(int plusMonth, int plusDay) {
        return DateTime.today(TimeZone.getDefault()).plus(Integer.valueOf(0), Integer.valueOf(plusMonth), Integer.valueOf(plusDay), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), DayOverflow.FirstDay).format("YYYY-MM-DD");
    }

    private void hoursDifferenceBetweenParisAndPerth() {
        int result = DateTime.now(TimeZone.getTimeZone("Australia/Perth")).getHour().intValue() - DateTime.now(TimeZone.getTimeZone("Europe/Paris")).getHour().intValue();
        if (result < 0) {
            result += 24;
        }
    }

    private void weeksSinceStart() {
        int result = DateTime.today(TimeZone.getDefault()).getWeekIndex().intValue() - DateTime.forDateOnly(Integer.valueOf(2010), Integer.valueOf(9), Integer.valueOf(6)).getWeekIndex().intValue();
    }

    private void timeTillMidnight() {
        DateTime now = DateTime.now(TimeZone.getDefault());
        long result = now.numSecondsFrom(now.plusDays(Integer.valueOf(1)).getStartOfDay());
    }

    private void imitateISOFormat() {
        DateTime now = DateTime.now(TimeZone.getDefault());
    }

    private void firstDayOfThisWeek() {
        DateTime today = DateTime.today(TimeZone.getDefault());
        DateTime firstDayThisWeek = today;
        int todaysWeekday = today.getWeekDay().intValue();
        if (todaysWeekday > 1) {
            firstDayThisWeek = today.minusDays(Integer.valueOf(todaysWeekday - 1));
        }
    }

    private void jdkDatesSuctorial() {
        int result = DateTime.today(TimeZone.getDefault()).getYear().intValue() - DateTime.forDateOnly(Integer.valueOf(1996), Integer.valueOf(1), Integer.valueOf(23)).getYear().intValue();
    }

    @SuppressLint({"SimpleDateFormat"})
    public static String getTimeState(String timestamp, String format) {
        if (timestamp == null || XmlPullParser.NO_NAMESPACE.equals(timestamp)) {
            return XmlPullParser.NO_NAMESPACE;
        }
        try {
            long _timestamp = Long.parseLong(formatTimestamp(timestamp));
            if (System.currentTimeMillis() - _timestamp < Util.MILLSECONDS_OF_MINUTE) {
                return "\u521a\u521a";
            }
            if (System.currentTimeMillis() - _timestamp < 1800000) {
                return new StringBuilder(String.valueOf(((System.currentTimeMillis() - _timestamp) / 1000) / 60)).append("\u5206\u949f\u524d").toString();
            }
            Calendar now = Calendar.getInstance();
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(_timestamp);
            if (c.get(1) == now.get(1) && c.get(2) == now.get(2) && c.get(5) == now.get(5)) {
                return new SimpleDateFormat("\u4eca\u5929 HH:mm").format(c.getTime());
            }
            if (c.get(1) == now.get(1) && c.get(2) == now.get(2) && c.get(5) == now.get(5) - 1) {
                return new SimpleDateFormat("\u6628\u5929 HH:mm").format(c.getTime());
            }
            SimpleDateFormat sdf;
            if (c.get(1) == now.get(1)) {
                if (format == null || format.equalsIgnoreCase(XmlPullParser.NO_NAMESPACE)) {
                    sdf = new SimpleDateFormat("M\u6708d\u65e5 HH:mm:ss");
                } else {
                    sdf = new SimpleDateFormat(format);
                }
                return sdf.format(c.getTime());
            }
            if (format == null || format.equalsIgnoreCase(XmlPullParser.NO_NAMESPACE)) {
                sdf = new SimpleDateFormat("yyyy\u5e74M\u6708d\u65e5 HH:mm:ss");
            } else {
                sdf = new SimpleDateFormat(format);
            }
            return sdf.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return XmlPullParser.NO_NAMESPACE;
        }
    }

    public static String formatTimestamp(String timestamp) {
        if (timestamp == null || XmlPullParser.NO_NAMESPACE.equals(timestamp)) {
            return XmlPullParser.NO_NAMESPACE;
        }
        return new StringBuffer(new StringBuilder(String.valueOf(timestamp)).append("00000000000000").toString()).substring(0, 13);
    }
}
