package hirondelle.date4j;

import com.ifoer.mine.Contact;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.codehaus.jackson.util.MinimalPrettyPrinter;

final class DateTimeParser {
    private static final String CL = "\\:";
    private static final String COLON = ":";
    private static final Pattern DATE;
    private static final Integer NUM_DIGITS;
    private static final String NUM_DIGITS_FOR_FRACTIONAL_SECONDS = "9";
    private static final int THIRD_POSITION = 2;
    private static final Pattern TIME;
    private static final String TT = "(\\d\\d)";
    private Integer fDay;
    private Integer fHour;
    private Integer fMinute;
    private Integer fMonth;
    private Integer fNanosecond;
    private Integer fSecond;
    private Integer fYear;

    private class Parts {
        String datePart;
        String timePart;

        private Parts() {
        }

        boolean hasDateOnly() {
            return this.timePart == null;
        }

        boolean hasTimeOnly() {
            return this.datePart == null;
        }

        boolean hasTwoParts() {
            return (this.datePart == null || this.timePart == null) ? false : true;
        }
    }

    static final class UnknownDateTimeFormat extends RuntimeException {
        private static final long serialVersionUID = -7179421566055773208L;

        UnknownDateTimeFormat(String str) {
            super(str);
        }

        UnknownDateTimeFormat(String str, Throwable th) {
            super(str, th);
        }
    }

    static {
        DATE = Pattern.compile("(\\d{1,4})-(\\d\\d)-(\\d\\d)|(\\d{1,4})-(\\d\\d)|(\\d{1,4})");
        NUM_DIGITS = Integer.valueOf(NUM_DIGITS_FOR_FRACTIONAL_SECONDS);
        TIME = Pattern.compile("(\\d\\d)\\:(\\d\\d)\\:(\\d\\d)\\.(\\d{1,9})|(\\d\\d)\\:(\\d\\d)\\:(\\d\\d)|(\\d\\d)\\:(\\d\\d)|(\\d\\d)");
    }

    DateTimeParser() {
    }

    private String convertToNanoseconds(String str) {
        StringBuilder stringBuilder = new StringBuilder(str);
        while (stringBuilder.length() < NUM_DIGITS.intValue()) {
            stringBuilder.append(Contact.RELATION_ASK);
        }
        return stringBuilder.toString();
    }

    private String getGroup(Matcher matcher, int... iArr) {
        String str = null;
        for (int group : iArr) {
            str = matcher.group(group);
            if (str != null) {
                break;
            }
        }
        return str;
    }

    private boolean hasColonInThirdPlace(String str) {
        return str.length() >= THIRD_POSITION ? COLON.equals(str.substring(THIRD_POSITION, 3)) : false;
    }

    private void parseDate(String str) {
        Matcher matcher = DATE.matcher(str);
        if (matcher.matches()) {
            String group = getGroup(matcher, 1, 4, 6);
            if (group != null) {
                this.fYear = Integer.valueOf(group);
            }
            group = getGroup(matcher, THIRD_POSITION, 5);
            if (group != null) {
                this.fMonth = Integer.valueOf(group);
            }
            String group2 = getGroup(matcher, 3);
            if (group2 != null) {
                this.fDay = Integer.valueOf(group2);
                return;
            }
            return;
        }
        throw new UnknownDateTimeFormat("Unexpected format for date:" + str);
    }

    private void parseTime(String str) {
        Matcher matcher = TIME.matcher(str);
        if (matcher.matches()) {
            String group = getGroup(matcher, 1, 5, 8, 10);
            if (group != null) {
                this.fHour = Integer.valueOf(group);
            }
            group = getGroup(matcher, THIRD_POSITION, 6, 9);
            if (group != null) {
                this.fMinute = Integer.valueOf(group);
            }
            group = getGroup(matcher, 3, 7);
            if (group != null) {
                this.fSecond = Integer.valueOf(group);
            }
            String group2 = getGroup(matcher, 4);
            if (group2 != null) {
                this.fNanosecond = Integer.valueOf(convertToNanoseconds(group2));
                return;
            }
            return;
        }
        throw new UnknownDateTimeFormat("Unexpected format for time:" + str);
    }

    private Parts splitIntoDateAndTime(String str) {
        Parts parts = new Parts();
        int dateTimeSeparator = getDateTimeSeparator(str);
        int i = (dateTimeSeparator <= 0 || dateTimeSeparator >= str.length()) ? 0 : 1;
        if (i != 0) {
            parts.datePart = str.substring(0, dateTimeSeparator);
            parts.timePart = str.substring(dateTimeSeparator + 1);
        } else if (hasColonInThirdPlace(str)) {
            parts.timePart = str;
        } else {
            parts.datePart = str;
        }
        return parts;
    }

    int getDateTimeSeparator(String str) {
        int indexOf = str.indexOf(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        return indexOf == -1 ? str.indexOf(NDEFRecord.TEXT_WELL_KNOWN_TYPE) : indexOf;
    }

    DateTime parse(String str) {
        if (str == null) {
            throw new NullPointerException("DateTime string is null");
        }
        Parts splitIntoDateAndTime = splitIntoDateAndTime(str.trim());
        if (splitIntoDateAndTime.hasTwoParts()) {
            parseDate(splitIntoDateAndTime.datePart);
            parseTime(splitIntoDateAndTime.timePart);
        } else if (splitIntoDateAndTime.hasDateOnly()) {
            parseDate(splitIntoDateAndTime.datePart);
        } else if (splitIntoDateAndTime.hasTimeOnly()) {
            parseTime(splitIntoDateAndTime.timePart);
        }
        return new DateTime(this.fYear, this.fMonth, this.fDay, this.fHour, this.fMinute, this.fSecond, this.fNanosecond);
    }
}
