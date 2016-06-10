package hirondelle.date4j;

import com.cnmobi.im.util.XmppConnection;
import com.ifoer.mine.Contact;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.codehaus.jackson.util.BufferRecycler;

final class DateTimeFormatter {
    private static final int AM = 0;
    private static final String f1662D = "D";
    private static final String DD = "DD";
    private static final String EMPTY_STRING = "";
    private static final Pattern ESCAPED_RANGE;
    private static final String ESCAPE_CHAR = "|";
    private static final Pattern FRACTIONALS;
    private static final String f1663M = "M";
    private static final String MM = "MM";
    private static final String MMM = "MMM";
    private static final String MMMM = "MMMM";
    private static final int PM = 1;
    private static final List<String> TOKENS;
    private static final String WWW = "WWW";
    private static final String WWWW = "WWWW";
    private static final String YY = "YY";
    private static final String YYYY = "YYYY";
    private static final String f1664a = "a";
    private static final String f1665h = "h";
    private static final String h12 = "h12";
    private static final String hh = "hh";
    private static final String hh12 = "hh12";
    private static final String f1666m = "m";
    private static final String mm = "mm";
    private static final String f1667s = "s";
    private static final String ss = "ss";
    private final Map<Locale, List<String>> fAmPm;
    private final CustomLocalization fCustomLocalization;
    private Collection<EscapedRange> fEscapedRanges;
    private final String fFormat;
    private Collection<InterpretedRange> fInterpretedRanges;
    private final Locale fLocale;
    private final Map<Locale, List<String>> fMonths;
    private final Map<Locale, List<String>> fWeekdays;

    private final class CustomLocalization {
        List<String> AmPmIndicators;
        List<String> Months;
        List<String> Weekdays;

        CustomLocalization(List<String> list, List<String> list2, List<String> list3) {
            if (list.size() != 12) {
                throw new IllegalArgumentException("Your List of custom months must have size 12, but its size is " + list.size());
            } else if (list2.size() != 7) {
                throw new IllegalArgumentException("Your List of custom weekdays must have size 7, but its size is " + list2.size());
            } else if (list3.size() != 2) {
                throw new IllegalArgumentException("Your List of custom a.m./p.m. indicators must have size 2, but its size is " + list3.size());
            } else {
                this.Months = list;
                this.Weekdays = list2;
                this.AmPmIndicators = list3;
            }
        }
    }

    private static final class EscapedRange {
        int End;
        int Start;

        private EscapedRange() {
        }
    }

    private static final class InterpretedRange {
        int End;
        int Start;
        String Text;

        private InterpretedRange() {
        }

        public String toString() {
            return "Start:" + this.Start + " End:" + this.End + " '" + this.Text + "'";
        }
    }

    static {
        ESCAPED_RANGE = Pattern.compile("\\|[^\\|]*\\|");
        FRACTIONALS = Pattern.compile("f{1,9}");
        TOKENS = new ArrayList();
        TOKENS.add(YYYY);
        TOKENS.add(YY);
        TOKENS.add(MMMM);
        TOKENS.add(MMM);
        TOKENS.add(MM);
        TOKENS.add(f1663M);
        TOKENS.add(DD);
        TOKENS.add(f1662D);
        TOKENS.add(WWWW);
        TOKENS.add(WWW);
        TOKENS.add(hh12);
        TOKENS.add(h12);
        TOKENS.add(hh);
        TOKENS.add(f1665h);
        TOKENS.add(mm);
        TOKENS.add(f1666m);
        TOKENS.add(ss);
        TOKENS.add(f1667s);
        TOKENS.add(f1664a);
        TOKENS.add("fffffffff");
        TOKENS.add("ffffffff");
        TOKENS.add("fffffff");
        TOKENS.add("ffffff");
        TOKENS.add("fffff");
        TOKENS.add("ffff");
        TOKENS.add("fff");
        TOKENS.add("ff");
        TOKENS.add("f");
    }

    DateTimeFormatter(String str) {
        this.fMonths = new LinkedHashMap();
        this.fWeekdays = new LinkedHashMap();
        this.fAmPm = new LinkedHashMap();
        this.fFormat = str;
        this.fLocale = null;
        this.fCustomLocalization = null;
        validateState();
    }

    DateTimeFormatter(String str, List<String> list, List<String> list2, List<String> list3) {
        this.fMonths = new LinkedHashMap();
        this.fWeekdays = new LinkedHashMap();
        this.fAmPm = new LinkedHashMap();
        this.fFormat = str;
        this.fLocale = null;
        this.fCustomLocalization = new CustomLocalization(list, list2, list3);
        validateState();
    }

    DateTimeFormatter(String str, Locale locale) {
        this.fMonths = new LinkedHashMap();
        this.fWeekdays = new LinkedHashMap();
        this.fAmPm = new LinkedHashMap();
        this.fFormat = str;
        this.fLocale = locale;
        this.fCustomLocalization = null;
        validateState();
    }

    private String addLeadingZero(String str) {
        return (Util.textHasContent(str) && str.length() == PM) ? Contact.RELATION_ASK + str : str;
    }

    private String amPmIndicator(Integer num) {
        String str = EMPTY_STRING;
        if (num == null) {
            return str;
        }
        if (this.fCustomLocalization != null) {
            return lookupCustomAmPmFor(num);
        }
        if (this.fLocale != null) {
            return lookupAmPmFor(num);
        }
        throw new IllegalArgumentException("Your date pattern requires either a Locale, or your own custom localizations for text:" + Util.quote(this.fFormat));
    }

    private void findEscapedRanges() {
        Matcher matcher = ESCAPED_RANGE.matcher(this.fFormat);
        while (matcher.find()) {
            EscapedRange escapedRange = new EscapedRange();
            escapedRange.Start = matcher.start();
            escapedRange.End = matcher.end() - 1;
            this.fEscapedRanges.add(escapedRange);
        }
    }

    private String firstNChars(String str, int i) {
        return (!Util.textHasContent(str) || str.length() < i) ? str : str.substring(AM, i);
    }

    private String firstThreeChars(String str) {
        return (!Util.textHasContent(str) || str.length() < 3) ? str : str.substring(AM, 3);
    }

    private String fullMonth(Integer num) {
        String str = EMPTY_STRING;
        if (num == null) {
            return str;
        }
        if (this.fCustomLocalization != null) {
            return lookupCustomMonthFor(num);
        }
        if (this.fLocale != null) {
            return lookupMonthFor(num);
        }
        throw new IllegalArgumentException("Your date pattern requires either a Locale, or your own custom localizations for text:" + Util.quote(this.fFormat));
    }

    private String fullWeekday(Integer num) {
        String str = EMPTY_STRING;
        if (num == null) {
            return str;
        }
        if (this.fCustomLocalization != null) {
            return lookupCustomWeekdayFor(num);
        }
        if (this.fLocale != null) {
            return lookupWeekdayFor(num);
        }
        throw new IllegalArgumentException("Your date pattern requires either a Locale, or your own custom localizations for text:" + Util.quote(this.fFormat));
    }

    private String getAmPmTextFor(Integer num) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(f1664a, this.fLocale);
        Calendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.set(PM, BufferRecycler.DEFAULT_WRITE_CONCAT_BUFFER_LEN);
        gregorianCalendar.set(2, 6);
        gregorianCalendar.set(5, 15);
        gregorianCalendar.set(11, num.intValue());
        return simpleDateFormat.format(gregorianCalendar.getTime());
    }

    private InterpretedRange getInterpretation(int i) {
        InterpretedRange interpretedRange = null;
        for (InterpretedRange interpretedRange2 : this.fInterpretedRanges) {
            InterpretedRange interpretedRange22;
            if (interpretedRange22.Start != i) {
                interpretedRange22 = interpretedRange;
            }
            interpretedRange = interpretedRange22;
        }
        return interpretedRange;
    }

    private void interpretInput(DateTime dateTime) {
        String str = this.fFormat;
        Object obj = str;
        for (String str2 : TOKENS) {
            Matcher matcher = Pattern.compile(str2).matcher(obj);
            while (matcher.find()) {
                InterpretedRange interpretedRange = new InterpretedRange();
                interpretedRange.Start = matcher.start();
                interpretedRange.End = matcher.end() - 1;
                if (!isInEscapedRange(interpretedRange)) {
                    interpretedRange.Text = interpretThe(matcher.group(), dateTime);
                    this.fInterpretedRanges.add(interpretedRange);
                }
            }
            String replace = obj.replace(str2, withCharDenotingAlreadyInterpreted(str2));
        }
    }

    private String interpretThe(String str, DateTime dateTime) {
        String str2 = EMPTY_STRING;
        if (YYYY.equals(str)) {
            return valueStr(dateTime.getYear());
        }
        if (YY.equals(str)) {
            return noCentury(valueStr(dateTime.getYear()));
        }
        if (MMMM.equals(str)) {
            return fullMonth(Integer.valueOf(dateTime.getMonth().intValue()));
        }
        if (MMM.equals(str)) {
            return firstThreeChars(fullMonth(Integer.valueOf(dateTime.getMonth().intValue())));
        }
        if (MM.equals(str)) {
            return addLeadingZero(valueStr(dateTime.getMonth()));
        }
        if (f1663M.equals(str)) {
            return valueStr(dateTime.getMonth());
        }
        if (DD.equals(str)) {
            return addLeadingZero(valueStr(dateTime.getDay()));
        }
        if (f1662D.equals(str)) {
            return valueStr(dateTime.getDay());
        }
        if (WWWW.equals(str)) {
            return fullWeekday(Integer.valueOf(dateTime.getWeekDay().intValue()));
        }
        if (WWW.equals(str)) {
            return firstThreeChars(fullWeekday(Integer.valueOf(dateTime.getWeekDay().intValue())));
        }
        if (hh.equals(str)) {
            return addLeadingZero(valueStr(dateTime.getHour()));
        }
        if (f1665h.equals(str)) {
            return valueStr(dateTime.getHour());
        }
        if (h12.equals(str)) {
            return valueStr(twelveHourStyle(dateTime.getHour()));
        }
        if (hh12.equals(str)) {
            return addLeadingZero(valueStr(twelveHourStyle(dateTime.getHour())));
        }
        if (f1664a.equals(str)) {
            return amPmIndicator(Integer.valueOf(dateTime.getHour().intValue()));
        }
        if (mm.equals(str)) {
            return addLeadingZero(valueStr(dateTime.getMinute()));
        }
        if (f1666m.equals(str)) {
            return valueStr(dateTime.getMinute());
        }
        if (ss.equals(str)) {
            return addLeadingZero(valueStr(dateTime.getSecond()));
        }
        if (f1667s.equals(str)) {
            return valueStr(dateTime.getSecond());
        }
        if (!str.startsWith("f")) {
            throw new IllegalArgumentException("Unknown token in date formatting pattern: " + str);
        } else if (FRACTIONALS.matcher(str).matches()) {
            return firstNChars(nanosWithLeadingZeroes(dateTime.getNanoseconds()), str.length());
        } else {
            throw new IllegalArgumentException("Unknown token in date formatting pattern: " + str);
        }
    }

    private boolean isInEscapedRange(InterpretedRange interpretedRange) {
        for (EscapedRange escapedRange : this.fEscapedRanges) {
            if (escapedRange.Start <= interpretedRange.Start && interpretedRange.Start <= escapedRange.End) {
                return true;
            }
        }
        return false;
    }

    private String lookupAmPmFor(Integer num) {
        String str = EMPTY_STRING;
        if (!this.fAmPm.containsKey(this.fLocale)) {
            List arrayList = new ArrayList();
            arrayList.add(getAmPmTextFor(Integer.valueOf(6)));
            arrayList.add(getAmPmTextFor(Integer.valueOf(18)));
            this.fAmPm.put(this.fLocale, arrayList);
        }
        return num.intValue() < 12 ? (String) ((List) this.fAmPm.get(this.fLocale)).get(AM) : (String) ((List) this.fAmPm.get(this.fLocale)).get(PM);
    }

    private String lookupCustomAmPmFor(Integer num) {
        String str = EMPTY_STRING;
        return num.intValue() < 12 ? (String) this.fCustomLocalization.AmPmIndicators.get(AM) : (String) this.fCustomLocalization.AmPmIndicators.get(PM);
    }

    private String lookupCustomMonthFor(Integer num) {
        return (String) this.fCustomLocalization.Months.get(num.intValue() - 1);
    }

    private String lookupCustomWeekdayFor(Integer num) {
        return (String) this.fCustomLocalization.Weekdays.get(num.intValue() - 1);
    }

    private String lookupMonthFor(Integer num) {
        String str = EMPTY_STRING;
        if (!this.fMonths.containsKey(this.fLocale)) {
            List arrayList = new ArrayList();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(MMMM, this.fLocale);
            for (int i = AM; i <= 11; i += PM) {
                Calendar gregorianCalendar = new GregorianCalendar();
                gregorianCalendar.set(PM, BufferRecycler.DEFAULT_WRITE_CONCAT_BUFFER_LEN);
                gregorianCalendar.set(2, i);
                gregorianCalendar.set(5, 15);
                arrayList.add(simpleDateFormat.format(gregorianCalendar.getTime()));
            }
            this.fMonths.put(this.fLocale, arrayList);
        }
        return (String) ((List) this.fMonths.get(this.fLocale)).get(num.intValue() - 1);
    }

    private String lookupWeekdayFor(Integer num) {
        String str = EMPTY_STRING;
        if (!this.fWeekdays.containsKey(this.fLocale)) {
            List arrayList = new ArrayList();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE", this.fLocale);
            for (int i = 8; i <= 14; i += PM) {
                Calendar gregorianCalendar = new GregorianCalendar();
                gregorianCalendar.set(PM, 2009);
                gregorianCalendar.set(2, PM);
                gregorianCalendar.set(5, i);
                arrayList.add(simpleDateFormat.format(gregorianCalendar.getTime()));
            }
            this.fWeekdays.put(this.fLocale, arrayList);
        }
        return (String) ((List) this.fWeekdays.get(this.fLocale)).get(num.intValue() - 1);
    }

    private String nanosWithLeadingZeroes(Integer num) {
        String valueStr = valueStr(num);
        while (valueStr.length() < 9) {
            valueStr = Contact.RELATION_ASK + valueStr;
        }
        return valueStr;
    }

    private String nextLetter(int i) {
        return this.fFormat.substring(i, i + PM);
    }

    private String noCentury(String str) {
        return Util.textHasContent(str) ? str.substring(2) : EMPTY_STRING;
    }

    private String produceFinalOutput() {
        StringBuilder stringBuilder = new StringBuilder();
        int i = AM;
        while (i < this.fFormat.length()) {
            String nextLetter = nextLetter(i);
            InterpretedRange interpretation = getInterpretation(i);
            if (interpretation != null) {
                stringBuilder.append(interpretation.Text);
                i = interpretation.End;
            } else if (!ESCAPE_CHAR.equals(nextLetter)) {
                stringBuilder.append(nextLetter);
            }
            i += PM;
        }
        return stringBuilder.toString();
    }

    private Integer twelveHourStyle(Integer num) {
        return num != null ? num.intValue() == 0 ? Integer.valueOf(12) : num.intValue() > 12 ? Integer.valueOf(num.intValue() - 12) : num : num;
    }

    private void validateState() {
        if (!Util.textHasContent(this.fFormat)) {
            throw new IllegalArgumentException("DateTime format has no content.");
        }
    }

    private String valueStr(Object obj) {
        return obj != null ? String.valueOf(obj) : EMPTY_STRING;
    }

    private String withCharDenotingAlreadyInterpreted(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = PM; i <= str.length(); i += PM) {
            stringBuilder.append(XmppConnection.JID_SEPARATOR);
        }
        return stringBuilder.toString();
    }

    String format(DateTime dateTime) {
        this.fEscapedRanges = new ArrayList();
        this.fInterpretedRanges = new ArrayList();
        findEscapedRanges();
        interpretInput(dateTime);
        return produceFinalOutput();
    }
}
