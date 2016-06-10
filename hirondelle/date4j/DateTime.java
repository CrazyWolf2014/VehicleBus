package hirondelle.date4j;

import com.ifoer.util.MyHttpException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import org.codehaus.jackson.util.BufferRecycler;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xmlpull.v1.XmlPullParser;

public final class DateTime implements Comparable<DateTime>, Serializable {
    private static int EPOCH_MODIFIED_JD = 0;
    private static final int EQUAL = 0;
    private static final int MILLION = 1000000;
    private static final long serialVersionUID = -1300068157085493891L;
    private String fDateTime;
    private Integer fDay;
    private int fHashCode;
    private Integer fHour;
    private boolean fIsAlreadyParsed;
    private Integer fMinute;
    private Integer fMonth;
    private Integer fNanosecond;
    private Integer fSecond;
    private Integer fYear;

    public enum DayOverflow {
        LastDay,
        FirstDay,
        Spillover,
        Abort
    }

    static final class ItemOutOfRange extends RuntimeException {
        private static final long serialVersionUID = 4760138291907517660L;

        ItemOutOfRange(String str) {
            super(str);
        }
    }

    static final class MissingItem extends RuntimeException {
        private static final long serialVersionUID = -7359967338896127755L;

        MissingItem(String str) {
            super(str);
        }
    }

    public enum Unit {
        YEAR,
        MONTH,
        DAY,
        HOUR,
        MINUTE,
        SECOND,
        NANOSECONDS
    }

    static {
        EPOCH_MODIFIED_JD = 2400000;
    }

    public DateTime(Integer num, Integer num2, Integer num3, Integer num4, Integer num5, Integer num6, Integer num7) {
        this.fIsAlreadyParsed = true;
        this.fYear = num;
        this.fMonth = num2;
        this.fDay = num3;
        this.fHour = num4;
        this.fMinute = num5;
        this.fSecond = num6;
        this.fNanosecond = num7;
        validateState();
    }

    public DateTime(String str) {
        this.fIsAlreadyParsed = false;
        if (str == null) {
            throw new IllegalArgumentException("String passed to DateTime constructor is null. You can use an empty string, but not a null reference.");
        }
        this.fDateTime = str;
    }

    private void addToString(String str, Object obj, StringBuilder stringBuilder) {
        stringBuilder.append(str + ":" + String.valueOf(obj) + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
    }

    private String calcToStringFormat() {
        if (unitsAllPresent(Unit.YEAR)) {
            if (unitsAllAbsent(Unit.MONTH, Unit.DAY, Unit.HOUR, Unit.MINUTE, Unit.SECOND, Unit.NANOSECONDS)) {
                return "YYYY";
            }
        }
        if (unitsAllPresent(Unit.YEAR, Unit.MONTH)) {
            if (unitsAllAbsent(Unit.DAY, Unit.HOUR, Unit.MINUTE, Unit.SECOND, Unit.NANOSECONDS)) {
                return "YYYY-MM";
            }
        }
        if (unitsAllPresent(Unit.YEAR, Unit.MONTH, Unit.DAY)) {
            if (unitsAllAbsent(Unit.HOUR, Unit.MINUTE, Unit.SECOND, Unit.NANOSECONDS)) {
                return "YYYY-MM-DD";
            }
        }
        if (unitsAllPresent(Unit.YEAR, Unit.MONTH, Unit.DAY, Unit.HOUR)) {
            if (unitsAllAbsent(Unit.MINUTE, Unit.SECOND, Unit.NANOSECONDS)) {
                return "YYYY-MM-DD hh";
            }
        }
        if (unitsAllPresent(Unit.YEAR, Unit.MONTH, Unit.DAY, Unit.HOUR, Unit.MINUTE)) {
            if (unitsAllAbsent(Unit.SECOND, Unit.NANOSECONDS)) {
                return "YYYY-MM-DD hh:mm";
            }
        }
        if (unitsAllPresent(Unit.YEAR, Unit.MONTH, Unit.DAY, Unit.HOUR, Unit.MINUTE, Unit.SECOND)) {
            if (unitsAllAbsent(Unit.NANOSECONDS)) {
                return "YYYY-MM-DD hh:mm:ss";
            }
        }
        if (unitsAllPresent(Unit.YEAR, Unit.MONTH, Unit.DAY, Unit.HOUR, Unit.MINUTE, Unit.SECOND, Unit.NANOSECONDS)) {
            return "YYYY-MM-DD hh:mm:ss.fffffffff";
        }
        if (unitsAllAbsent(Unit.YEAR, Unit.MONTH, Unit.DAY)) {
            if (unitsAllPresent(Unit.HOUR, Unit.MINUTE, Unit.SECOND, Unit.NANOSECONDS)) {
                return "hh:mm:ss.fffffffff";
            }
        }
        if (unitsAllAbsent(Unit.YEAR, Unit.MONTH, Unit.DAY, Unit.NANOSECONDS)) {
            if (unitsAllPresent(Unit.HOUR, Unit.MINUTE, Unit.SECOND)) {
                return "hh:mm:ss";
            }
        }
        if (!unitsAllAbsent(Unit.YEAR, Unit.MONTH, Unit.DAY, Unit.SECOND, Unit.NANOSECONDS)) {
            return null;
        }
        return unitsAllPresent(Unit.HOUR, Unit.MINUTE) ? "hh:mm" : null;
    }

    private int calculateJulianDayNumberAtNoon() {
        int intValue = this.fYear.intValue();
        int intValue2 = this.fMonth.intValue();
        return (((((((intValue + 4800) + ((intValue2 - 14) / 12)) * 1461) / 4) + ((((intValue2 - 2) - (((intValue2 - 14) / 12) * 12)) * 367) / 12)) - (((((intValue + 4900) + ((intValue2 - 14) / 12)) / 100) * 3) / 4)) + this.fDay.intValue()) - 32075;
    }

    private void checkNumDaysInMonth(Integer num, Integer num2, Integer num3) {
        if (hasYearMonthDay(num, num2, num3) && num3.intValue() > getNumDaysInMonth(num, num2).intValue()) {
            throw new ItemOutOfRange("The day-of-the-month value '" + num3 + "' exceeds the number of days in the month: " + getNumDaysInMonth(num, num2));
        }
    }

    private void checkRange(Integer num, int i, int i2, String str) {
        if (num == null) {
            return;
        }
        if (num.intValue() < i || num.intValue() > i2) {
            throw new ItemOutOfRange(str + " is not in the range " + i + ".." + i2 + ". Value is:" + num);
        }
    }

    private void ensureHasYearMonthDay() {
        ensureParsed();
        if (!hasYearMonthDay()) {
            throw new MissingItem("DateTime does not include year/month/day.");
        }
    }

    public static DateTime forDateOnly(Integer num, Integer num2, Integer num3) {
        return new DateTime(num, num2, num3, null, null, null, null);
    }

    public static DateTime forInstant(long j, TimeZone timeZone) {
        Calendar gregorianCalendar = new GregorianCalendar(timeZone);
        gregorianCalendar.setTimeInMillis(j);
        return new DateTime(Integer.valueOf(gregorianCalendar.get(1)), Integer.valueOf(gregorianCalendar.get(2) + 1), Integer.valueOf(gregorianCalendar.get(5)), Integer.valueOf(gregorianCalendar.get(11)), Integer.valueOf(gregorianCalendar.get(12)), Integer.valueOf(gregorianCalendar.get(13)), Integer.valueOf((gregorianCalendar.get(14) * 1000) * 1000));
    }

    public static DateTime forInstantNanos(long j, TimeZone timeZone) {
        long j2;
        long j3 = j / 1000000;
        long j4 = j % 1000000;
        if (j < 0) {
            j2 = j4 + 1000000;
            j4 = j3 - 1;
        } else {
            j2 = j4;
            j4 = j3;
        }
        Calendar gregorianCalendar = new GregorianCalendar(timeZone);
        gregorianCalendar.setTimeInMillis(j4);
        return new DateTime(Integer.valueOf(gregorianCalendar.get(1)), Integer.valueOf(gregorianCalendar.get(2) + 1), Integer.valueOf(gregorianCalendar.get(5)), Integer.valueOf(gregorianCalendar.get(11)), Integer.valueOf(gregorianCalendar.get(12)), Integer.valueOf(gregorianCalendar.get(13)), Integer.valueOf(MILLION * gregorianCalendar.get(14))).plus(Integer.valueOf(EQUAL), Integer.valueOf(EQUAL), Integer.valueOf(EQUAL), Integer.valueOf(EQUAL), Integer.valueOf(EQUAL), Integer.valueOf(EQUAL), Integer.valueOf((int) j2), DayOverflow.Spillover);
    }

    public static DateTime forTimeOnly(Integer num, Integer num2, Integer num3, Integer num4) {
        return new DateTime(null, null, null, num, num2, num3, num4);
    }

    static DateTime fromJulianDayNumberAtNoon(int i) {
        int i2 = 68569 + i;
        int i3 = (i2 * 4) / 146097;
        i2 -= ((146097 * i3) + 3) / 4;
        int i4 = ((i2 + 1) * 4000) / 1461001;
        i2 = (i2 - ((i4 * 1461) / 4)) + 31;
        int i5 = (i2 * 80) / 2447;
        i2 -= (i5 * 2447) / 80;
        int i6 = i5 / 11;
        return forDateOnly(Integer.valueOf((((i3 - 49) * 100) + i4) + i6), Integer.valueOf((i5 + 2) - (i6 * 12)), Integer.valueOf(i2));
    }

    static Integer getNumDaysInMonth(Integer num, Integer num2) {
        if (num == null || num2 == null) {
            return null;
        }
        if (num2.intValue() == 1) {
            return Integer.valueOf(31);
        }
        if (num2.intValue() == 2) {
            return Integer.valueOf(isLeapYear(num) ? 29 : 28);
        } else if (num2.intValue() == 3) {
            return Integer.valueOf(31);
        } else {
            if (num2.intValue() == 4) {
                return Integer.valueOf(30);
            }
            if (num2.intValue() == 5) {
                return Integer.valueOf(31);
            }
            if (num2.intValue() == 6) {
                return Integer.valueOf(30);
            }
            if (num2.intValue() == 7) {
                return Integer.valueOf(31);
            }
            if (num2.intValue() == 8) {
                return Integer.valueOf(31);
            }
            if (num2.intValue() == 9) {
                return Integer.valueOf(30);
            }
            if (num2.intValue() == 10) {
                return Integer.valueOf(31);
            }
            if (num2.intValue() == 11) {
                return Integer.valueOf(30);
            }
            if (num2.intValue() == 12) {
                return Integer.valueOf(31);
            }
            throw new AssertionError("Month is out of range 1..12:" + num2);
        }
    }

    private Object[] getSignificantFields() {
        return new Object[]{this.fYear, this.fMonth, this.fDay, this.fHour, this.fMinute, this.fSecond, this.fNanosecond};
    }

    private DateTime getStartEndDateTime(Integer num, Integer num2, Integer num3, Integer num4, Integer num5) {
        ensureHasYearMonthDay();
        return new DateTime(this.fYear, this.fMonth, num, num2, num3, num4, num5);
    }

    private boolean hasYearMonthDay(Integer num, Integer num2, Integer num3) {
        return isPresent(num, num2, num3);
    }

    private static boolean isLeapYear(Integer num) {
        if (num.intValue() % 100 == 0) {
            if (num.intValue() % MyHttpException.ERROR_PARAMETER == 0) {
                return true;
            }
        } else if (num.intValue() % 4 == 0) {
            return true;
        }
        return false;
    }

    public static boolean isParseable(String str) {
        try {
            new DateTime(str).ensureParsed();
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    private boolean isPresent(Object... objArr) {
        int length = objArr.length;
        for (int i = EQUAL; i < length; i++) {
            if (objArr[i] == null) {
                return false;
            }
        }
        return true;
    }

    public static DateTime now(TimeZone timeZone) {
        return forInstant(System.currentTimeMillis(), timeZone);
    }

    private int numSecondsInTimePortion() {
        int i = EQUAL;
        if (this.fSecond != null) {
            i = EQUAL + this.fSecond.intValue();
        }
        if (this.fMinute != null) {
            i += this.fMinute.intValue() * 60;
        }
        return this.fHour != null ? i + (this.fHour.intValue() * 3600) : i;
    }

    private void parseDateTimeText() {
        DateTime parse = new DateTimeParser().parse(this.fDateTime);
        this.fYear = parse.fYear;
        this.fMonth = parse.fMonth;
        this.fDay = parse.fDay;
        this.fHour = parse.fHour;
        this.fMinute = parse.fMinute;
        this.fSecond = parse.fSecond;
        this.fNanosecond = parse.fNanosecond;
        validateState();
    }

    private void readObject(ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException {
        objectInputStream.defaultReadObject();
        validateState();
    }

    public static DateTime today(TimeZone timeZone) {
        return now(timeZone).truncate(Unit.DAY);
    }

    private void validateState() {
        checkRange(this.fYear, 1, 9999, "Year");
        checkRange(this.fMonth, 1, 12, "Month");
        checkRange(this.fDay, 1, 31, "Day");
        checkRange(this.fHour, EQUAL, 23, "Hour");
        checkRange(this.fMinute, EQUAL, 59, "Minute");
        checkRange(this.fSecond, EQUAL, 59, "Second");
        checkRange(this.fNanosecond, EQUAL, 999999999, "Nanosecond");
        checkNumDaysInMonth(this.fYear, this.fMonth, this.fDay);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
    }

    public DateTime changeTimeZone(TimeZone timeZone, TimeZone timeZone2) {
        ensureHasYearMonthDay();
        if (unitsAllAbsent(Unit.HOUR)) {
            throw new IllegalArgumentException("DateTime does not include the hour. Cannot change the time zone if no hour is present.");
        }
        Calendar gregorianCalendar = new GregorianCalendar(timeZone);
        gregorianCalendar.set(1, getYear().intValue());
        gregorianCalendar.set(2, getMonth().intValue() - 1);
        gregorianCalendar.set(5, getDay().intValue());
        gregorianCalendar.set(11, getHour().intValue());
        if (getMinute() != null) {
            gregorianCalendar.set(12, getMinute().intValue());
        } else {
            gregorianCalendar.set(12, EQUAL);
        }
        gregorianCalendar.set(13, EQUAL);
        gregorianCalendar.set(14, EQUAL);
        Calendar gregorianCalendar2 = new GregorianCalendar(timeZone2);
        gregorianCalendar2.setTimeInMillis(gregorianCalendar.getTimeInMillis());
        return new DateTime(Integer.valueOf(gregorianCalendar2.get(1)), Integer.valueOf(gregorianCalendar2.get(2) + 1), Integer.valueOf(gregorianCalendar2.get(5)), Integer.valueOf(gregorianCalendar2.get(11)), getMinute() != null ? Integer.valueOf(gregorianCalendar2.get(12)) : null, getSecond(), getNanoseconds());
    }

    public int compareTo(DateTime dateTime) {
        if (this == dateTime) {
            return EQUAL;
        }
        ensureParsed();
        dateTime.ensureParsed();
        NullsGo nullsGo = NullsGo.FIRST;
        int comparePossiblyNull = ModelUtil.comparePossiblyNull(this.fYear, dateTime.fYear, nullsGo);
        if (comparePossiblyNull != 0) {
            return comparePossiblyNull;
        }
        comparePossiblyNull = ModelUtil.comparePossiblyNull(this.fMonth, dateTime.fMonth, nullsGo);
        if (comparePossiblyNull != 0) {
            return comparePossiblyNull;
        }
        comparePossiblyNull = ModelUtil.comparePossiblyNull(this.fDay, dateTime.fDay, nullsGo);
        if (comparePossiblyNull != 0) {
            return comparePossiblyNull;
        }
        comparePossiblyNull = ModelUtil.comparePossiblyNull(this.fHour, dateTime.fHour, nullsGo);
        if (comparePossiblyNull != 0) {
            return comparePossiblyNull;
        }
        comparePossiblyNull = ModelUtil.comparePossiblyNull(this.fMinute, dateTime.fMinute, nullsGo);
        if (comparePossiblyNull != 0) {
            return comparePossiblyNull;
        }
        comparePossiblyNull = ModelUtil.comparePossiblyNull(this.fSecond, dateTime.fSecond, nullsGo);
        if (comparePossiblyNull != 0) {
            return comparePossiblyNull;
        }
        comparePossiblyNull = ModelUtil.comparePossiblyNull(this.fNanosecond, dateTime.fNanosecond, nullsGo);
        return comparePossiblyNull != 0 ? comparePossiblyNull : EQUAL;
    }

    void ensureParsed() {
        if (!this.fIsAlreadyParsed) {
            parseDateTimeText();
        }
    }

    public boolean equals(Object obj) {
        ensureParsed();
        Boolean quickEquals = ModelUtil.quickEquals(this, obj);
        if (quickEquals == null) {
            DateTime dateTime = (DateTime) obj;
            dateTime.ensureParsed();
            quickEquals = Boolean.valueOf(ModelUtil.equalsFor(getSignificantFields(), dateTime.getSignificantFields()));
        }
        return quickEquals.booleanValue();
    }

    public String format(String str) {
        return new DateTimeFormatter(str).format(this);
    }

    public String format(String str, List<String> list, List<String> list2, List<String> list3) {
        return new DateTimeFormatter(str, list, list2, list3).format(this);
    }

    public String format(String str, Locale locale) {
        return new DateTimeFormatter(str, locale).format(this);
    }

    public Integer getDay() {
        ensureParsed();
        return this.fDay;
    }

    public Integer getDayOfYear() {
        ensureHasYearMonthDay();
        return Integer.valueOf(((((this.fMonth.intValue() * 275) / 9) - ((isLeapYear().booleanValue() ? 1 : 2) * ((this.fMonth.intValue() + 9) / 12))) + this.fDay.intValue()) - 30);
    }

    public DateTime getEndOfDay() {
        ensureHasYearMonthDay();
        return getStartEndDateTime(this.fDay, Integer.valueOf(23), Integer.valueOf(59), Integer.valueOf(59), Integer.valueOf(999999999));
    }

    public DateTime getEndOfMonth() {
        ensureHasYearMonthDay();
        return getStartEndDateTime(Integer.valueOf(getNumDaysInMonth()), Integer.valueOf(23), Integer.valueOf(59), Integer.valueOf(59), Integer.valueOf(999999999));
    }

    public Integer getHour() {
        ensureParsed();
        return this.fHour;
    }

    public long getMilliseconds(TimeZone timeZone) {
        int i = EQUAL;
        Integer year = getYear();
        Integer month = getMonth();
        Integer day = getDay();
        Integer valueOf = Integer.valueOf(getHour() == null ? EQUAL : getHour().intValue());
        Integer valueOf2 = Integer.valueOf(getMinute() == null ? EQUAL : getMinute().intValue());
        Integer valueOf3 = Integer.valueOf(getSecond() == null ? EQUAL : getSecond().intValue());
        if (getNanoseconds() != null) {
            i = getNanoseconds().intValue();
        }
        Integer valueOf4 = Integer.valueOf(i);
        Calendar gregorianCalendar = new GregorianCalendar(timeZone);
        gregorianCalendar.set(1, year.intValue());
        gregorianCalendar.set(2, month.intValue() - 1);
        gregorianCalendar.set(5, day.intValue());
        gregorianCalendar.set(11, valueOf.intValue());
        gregorianCalendar.set(12, valueOf2.intValue());
        gregorianCalendar.set(13, valueOf3.intValue());
        gregorianCalendar.set(14, valueOf4.intValue() / MILLION);
        return gregorianCalendar.getTimeInMillis();
    }

    public Integer getMinute() {
        ensureParsed();
        return this.fMinute;
    }

    public Integer getModifiedJulianDayNumber() {
        ensureHasYearMonthDay();
        return Integer.valueOf((calculateJulianDayNumberAtNoon() - 1) - EPOCH_MODIFIED_JD);
    }

    public Integer getMonth() {
        ensureParsed();
        return this.fMonth;
    }

    public Integer getNanoseconds() {
        ensureParsed();
        return this.fNanosecond;
    }

    public long getNanosecondsInstant(TimeZone timeZone) {
        int i = EQUAL;
        Integer year = getYear();
        Integer month = getMonth();
        Integer day = getDay();
        Integer valueOf = Integer.valueOf(getHour() == null ? EQUAL : getHour().intValue());
        Integer valueOf2 = Integer.valueOf(getMinute() == null ? EQUAL : getMinute().intValue());
        Integer valueOf3 = Integer.valueOf(getSecond() == null ? EQUAL : getSecond().intValue());
        if (getNanoseconds() != null) {
            i = getNanoseconds().intValue();
        }
        Integer valueOf4 = Integer.valueOf(i);
        int intValue = valueOf4.intValue() / MILLION;
        i = valueOf4.intValue() % MILLION;
        Calendar gregorianCalendar = new GregorianCalendar(timeZone);
        gregorianCalendar.set(1, year.intValue());
        gregorianCalendar.set(2, month.intValue() - 1);
        gregorianCalendar.set(5, day.intValue());
        gregorianCalendar.set(11, valueOf.intValue());
        gregorianCalendar.set(12, valueOf2.intValue());
        gregorianCalendar.set(13, valueOf3.intValue());
        gregorianCalendar.set(14, intValue);
        return ((long) i) + (gregorianCalendar.getTimeInMillis() * 1000000);
    }

    public int getNumDaysInMonth() {
        ensureHasYearMonthDay();
        return getNumDaysInMonth(this.fYear, this.fMonth).intValue();
    }

    public Unit getPrecision() {
        ensureParsed();
        if (isPresent(this.fNanosecond)) {
            return Unit.NANOSECONDS;
        }
        if (isPresent(this.fSecond)) {
            return Unit.SECOND;
        }
        if (isPresent(this.fMinute)) {
            return Unit.MINUTE;
        }
        if (isPresent(this.fHour)) {
            return Unit.HOUR;
        }
        if (isPresent(this.fDay)) {
            return Unit.DAY;
        }
        if (isPresent(this.fMonth)) {
            return Unit.MONTH;
        }
        return isPresent(this.fYear) ? Unit.YEAR : null;
    }

    public String getRawDateString() {
        return this.fDateTime;
    }

    public Integer getSecond() {
        ensureParsed();
        return this.fSecond;
    }

    public DateTime getStartOfDay() {
        ensureHasYearMonthDay();
        return getStartEndDateTime(this.fDay, Integer.valueOf(EQUAL), Integer.valueOf(EQUAL), Integer.valueOf(EQUAL), Integer.valueOf(EQUAL));
    }

    public DateTime getStartOfMonth() {
        ensureHasYearMonthDay();
        return getStartEndDateTime(Integer.valueOf(1), Integer.valueOf(EQUAL), Integer.valueOf(EQUAL), Integer.valueOf(EQUAL), Integer.valueOf(EQUAL));
    }

    public Integer getWeekDay() {
        ensureHasYearMonthDay();
        return Integer.valueOf(((calculateJulianDayNumberAtNoon() + 1) % 7) + 1);
    }

    public Integer getWeekIndex() {
        return getWeekIndex(forDateOnly(Integer.valueOf(BufferRecycler.DEFAULT_WRITE_CONCAT_BUFFER_LEN), Integer.valueOf(1), Integer.valueOf(2)));
    }

    public Integer getWeekIndex(DateTime dateTime) {
        ensureHasYearMonthDay();
        dateTime.ensureHasYearMonthDay();
        return Integer.valueOf(((getModifiedJulianDayNumber().intValue() - dateTime.getModifiedJulianDayNumber().intValue()) / 7) + 1);
    }

    public Integer getYear() {
        ensureParsed();
        return this.fYear;
    }

    public boolean gt(DateTime dateTime) {
        return compareTo(dateTime) > 0;
    }

    public boolean gteq(DateTime dateTime) {
        return compareTo(dateTime) > 0 || equals(dateTime);
    }

    public boolean hasHourMinuteSecond() {
        return unitsAllPresent(Unit.HOUR, Unit.MINUTE, Unit.SECOND);
    }

    public boolean hasYearMonthDay() {
        return unitsAllPresent(Unit.YEAR, Unit.MONTH, Unit.DAY);
    }

    public int hashCode() {
        if (this.fHashCode == 0) {
            ensureParsed();
            this.fHashCode = ModelUtil.hashCodeFor(getSignificantFields());
        }
        return this.fHashCode;
    }

    public boolean isInTheFuture(TimeZone timeZone) {
        return now(timeZone).lt(this);
    }

    public boolean isInThePast(TimeZone timeZone) {
        return now(timeZone).gt(this);
    }

    public Boolean isLeapYear() {
        ensureParsed();
        if (isPresent(this.fYear)) {
            return Boolean.valueOf(isLeapYear(this.fYear));
        }
        throw new MissingItem("Year is absent. Cannot determine if leap year.");
    }

    public boolean isSameDayAs(DateTime dateTime) {
        ensureHasYearMonthDay();
        dateTime.ensureHasYearMonthDay();
        return this.fYear.equals(dateTime.fYear) && this.fMonth.equals(dateTime.fMonth) && this.fDay.equals(dateTime.fDay);
    }

    public boolean lt(DateTime dateTime) {
        return compareTo(dateTime) < 0;
    }

    public boolean lteq(DateTime dateTime) {
        return compareTo(dateTime) < 0 || equals(dateTime);
    }

    public DateTime minus(Integer num, Integer num2, Integer num3, Integer num4, Integer num5, Integer num6, Integer num7, DayOverflow dayOverflow) {
        return new DateTimeInterval(this, dayOverflow).minus(num.intValue(), num2.intValue(), num3.intValue(), num4.intValue(), num5.intValue(), num6.intValue(), num7.intValue());
    }

    public DateTime minusDays(Integer num) {
        return plusDays(Integer.valueOf(num.intValue() * -1));
    }

    public int numDaysFrom(DateTime dateTime) {
        return dateTime.getModifiedJulianDayNumber().intValue() - getModifiedJulianDayNumber().intValue();
    }

    public long numSecondsFrom(DateTime dateTime) {
        long j = 0;
        if (hasYearMonthDay() && dateTime.hasYearMonthDay()) {
            j = (long) (numDaysFrom(dateTime) * 86400);
        }
        return (j - ((long) numSecondsInTimePortion())) + ((long) dateTime.numSecondsInTimePortion());
    }

    public DateTime plus(Integer num, Integer num2, Integer num3, Integer num4, Integer num5, Integer num6, Integer num7, DayOverflow dayOverflow) {
        return new DateTimeInterval(this, dayOverflow).plus(num.intValue(), num2.intValue(), num3.intValue(), num4.intValue(), num5.intValue(), num6.intValue(), num7.intValue());
    }

    public DateTime plusDays(Integer num) {
        ensureHasYearMonthDay();
        DateTime fromJulianDayNumberAtNoon = fromJulianDayNumberAtNoon(((getModifiedJulianDayNumber().intValue() + 1) + EPOCH_MODIFIED_JD) + num.intValue());
        return new DateTime(fromJulianDayNumberAtNoon.getYear(), fromJulianDayNumberAtNoon.getMonth(), fromJulianDayNumberAtNoon.getDay(), this.fHour, this.fMinute, this.fSecond, this.fNanosecond);
    }

    public String toString() {
        String str = XmlPullParser.NO_NAMESPACE;
        if (Util.textHasContent(this.fDateTime)) {
            return this.fDateTime;
        }
        if (calcToStringFormat() != null) {
            return format(calcToStringFormat());
        }
        StringBuilder stringBuilder = new StringBuilder();
        addToString("Y", this.fYear, stringBuilder);
        addToString("M", this.fMonth, stringBuilder);
        addToString("D", this.fDay, stringBuilder);
        addToString("h", this.fHour, stringBuilder);
        addToString("m", this.fMinute, stringBuilder);
        addToString("s", this.fSecond, stringBuilder);
        addToString("f", this.fNanosecond, stringBuilder);
        return stringBuilder.toString().trim();
    }

    public DateTime truncate(Unit unit) {
        ensureParsed();
        if (Unit.NANOSECONDS != unit) {
            return Unit.SECOND == unit ? new DateTime(this.fYear, this.fMonth, this.fDay, this.fHour, this.fMinute, this.fSecond, null) : Unit.MINUTE == unit ? new DateTime(this.fYear, this.fMonth, this.fDay, this.fHour, this.fMinute, null, null) : Unit.HOUR == unit ? new DateTime(this.fYear, this.fMonth, this.fDay, this.fHour, null, null, null) : Unit.DAY == unit ? new DateTime(this.fYear, this.fMonth, this.fDay, null, null, null, null) : Unit.MONTH == unit ? new DateTime(this.fYear, this.fMonth, null, null, null, null, null) : Unit.YEAR == unit ? new DateTime(this.fYear, null, null, null, null, null, null) : null;
        } else {
            throw new IllegalArgumentException("It makes no sense to truncate to nanosecond precision, since that's the highest precision available.");
        }
    }

    public boolean unitsAllAbsent(Unit... unitArr) {
        ensureParsed();
        int length = unitArr.length;
        boolean z = true;
        for (int i = EQUAL; i < length; i++) {
            Unit unit = unitArr[i];
            if (Unit.NANOSECONDS == unit) {
                z = z && this.fNanosecond == null;
            } else if (Unit.SECOND == unit) {
                z = z && this.fSecond == null;
            } else if (Unit.MINUTE == unit) {
                z = z && this.fMinute == null;
            } else if (Unit.HOUR == unit) {
                z = z && this.fHour == null;
            } else if (Unit.DAY == unit) {
                z = z && this.fDay == null;
            } else if (Unit.MONTH == unit) {
                z = z && this.fMonth == null;
            } else if (Unit.YEAR == unit) {
                z = z && this.fYear == null;
            }
        }
        return z;
    }

    public boolean unitsAllPresent(Unit... unitArr) {
        ensureParsed();
        int length = unitArr.length;
        boolean z = true;
        for (int i = EQUAL; i < length; i++) {
            Unit unit = unitArr[i];
            if (Unit.NANOSECONDS == unit) {
                z = z && this.fNanosecond != null;
            } else if (Unit.SECOND == unit) {
                z = z && this.fSecond != null;
            } else if (Unit.MINUTE == unit) {
                z = z && this.fMinute != null;
            } else if (Unit.HOUR == unit) {
                z = z && this.fHour != null;
            } else if (Unit.DAY == unit) {
                z = z && this.fDay != null;
            } else if (Unit.MONTH == unit) {
                z = z && this.fMonth != null;
            } else if (Unit.YEAR == unit) {
                z = z && this.fYear != null;
            }
        }
        return z;
    }
}
