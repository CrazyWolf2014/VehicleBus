package com.thoughtworks.xstream.converters.extended;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class ISO8601GregorianCalendarConverter extends AbstractSingleValueConverter {
    private static final DateTimeFormatter[] formattersNoUTC;
    private static final DateTimeFormatter[] formattersUTC;

    static {
        formattersUTC = new DateTimeFormatter[]{ISODateTimeFormat.dateTime(), ISODateTimeFormat.dateTimeNoMillis(), ISODateTimeFormat.basicDateTime(), ISODateTimeFormat.basicOrdinalDateTime(), ISODateTimeFormat.basicOrdinalDateTimeNoMillis(), ISODateTimeFormat.basicTime(), ISODateTimeFormat.basicTimeNoMillis(), ISODateTimeFormat.basicTTime(), ISODateTimeFormat.basicTTimeNoMillis(), ISODateTimeFormat.basicWeekDateTime(), ISODateTimeFormat.basicWeekDateTimeNoMillis(), ISODateTimeFormat.ordinalDateTime(), ISODateTimeFormat.ordinalDateTimeNoMillis(), ISODateTimeFormat.time(), ISODateTimeFormat.timeNoMillis(), ISODateTimeFormat.tTime(), ISODateTimeFormat.tTimeNoMillis(), ISODateTimeFormat.weekDateTime(), ISODateTimeFormat.weekDateTimeNoMillis()};
        formattersNoUTC = new DateTimeFormatter[]{ISODateTimeFormat.basicDate(), ISODateTimeFormat.basicOrdinalDate(), ISODateTimeFormat.basicWeekDate(), ISODateTimeFormat.date(), ISODateTimeFormat.dateHour(), ISODateTimeFormat.dateHourMinute(), ISODateTimeFormat.dateHourMinuteSecond(), ISODateTimeFormat.dateHourMinuteSecondFraction(), ISODateTimeFormat.dateHourMinuteSecondMillis(), ISODateTimeFormat.hour(), ISODateTimeFormat.hourMinute(), ISODateTimeFormat.hourMinuteSecond(), ISODateTimeFormat.hourMinuteSecondFraction(), ISODateTimeFormat.hourMinuteSecondMillis(), ISODateTimeFormat.ordinalDate(), ISODateTimeFormat.weekDate(), ISODateTimeFormat.year(), ISODateTimeFormat.yearMonth(), ISODateTimeFormat.yearMonthDay(), ISODateTimeFormat.weekyear(), ISODateTimeFormat.weekyearWeek(), ISODateTimeFormat.weekyearWeekDay()};
    }

    public boolean canConvert(Class type) {
        return type.equals(GregorianCalendar.class);
    }

    public Object fromString(String str) {
        int i = 0;
        while (i < formattersUTC.length) {
            try {
                Calendar calendar = formattersUTC[i].parseDateTime(str).toGregorianCalendar();
                calendar.setTimeZone(TimeZone.getDefault());
                break;
            } catch (IllegalArgumentException e) {
                i++;
            }
        }
        String timeZoneID = TimeZone.getDefault().getID();
        i = 0;
        while (i < formattersNoUTC.length) {
            try {
                calendar = formattersNoUTC[i].withZone(DateTimeZone.forID(timeZoneID)).parseDateTime(str).toGregorianCalendar();
                calendar.setTimeZone(TimeZone.getDefault());
                return calendar;
            } catch (IllegalArgumentException e2) {
                i++;
            }
        }
        throw new ConversionException("Cannot parse date " + str);
    }

    public String toString(Object obj) {
        return new DateTime(obj).toString(formattersUTC[0]);
    }
}
