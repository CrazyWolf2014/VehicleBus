package org.jivesoftware.smack.util;

import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import com.cnmobi.im.util.XmppConnection;
import com.cnmobi.im.view.RecordButton;
import com.ifoer.mine.Contact;
import com.tencent.mm.sdk.platformtools.FilePathGenerator;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Pattern;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.kxml2.wap.WbxmlParser;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;
import org.xmlpull.v1.XmlPullParser;

public class StringUtils {
    private static final char[] AMP_ENCODE;
    private static final char[] APOS_ENCODE;
    private static final char[] GT_ENCODE;
    private static final char[] LT_ENCODE;
    private static final char[] QUOTE_ENCODE;
    public static final DateFormat XEP_0082_UTC_FORMAT;
    private static final List<PatternCouplings> couplings;
    private static final DateFormat dateFormatter;
    private static final Pattern datePattern;
    private static final DateFormat dateTimeFormatter;
    private static final DateFormat dateTimeNoMillisFormatter;
    private static final Pattern dateTimeNoMillisPattern;
    private static final Pattern dateTimePattern;
    private static MessageDigest digest;
    private static char[] numbersAndLetters;
    private static Random randGen;
    private static final DateFormat timeFormatter;
    private static final DateFormat timeNoMillisFormatter;
    private static final DateFormat timeNoMillisNoZoneFormatter;
    private static final Pattern timeNoMillisNoZonePattern;
    private static final Pattern timeNoMillisPattern;
    private static final DateFormat timeNoZoneFormatter;
    private static final Pattern timeNoZonePattern;
    private static final Pattern timePattern;
    private static final DateFormat xep0091Date6DigitFormatter;
    private static final DateFormat xep0091Date7Digit1MonthFormatter;
    private static final DateFormat xep0091Date7Digit2MonthFormatter;
    private static final DateFormat xep0091Formatter;
    private static final Pattern xep0091Pattern;

    /* renamed from: org.jivesoftware.smack.util.StringUtils.1 */
    static class C09661 implements Comparator<Calendar> {
        final /* synthetic */ Calendar val$now;

        C09661(Calendar calendar) {
            this.val$now = calendar;
        }

        public int compare(Calendar calendar, Calendar calendar2) {
            return new Long(this.val$now.getTimeInMillis() - calendar.getTimeInMillis()).compareTo(new Long(this.val$now.getTimeInMillis() - calendar2.getTimeInMillis()));
        }
    }

    private static class PatternCouplings {
        DateFormat formatter;
        boolean needToConvertTimeZone;
        Pattern pattern;

        public PatternCouplings(Pattern pattern, DateFormat dateFormat) {
            this.needToConvertTimeZone = false;
            this.pattern = pattern;
            this.formatter = dateFormat;
        }

        public PatternCouplings(Pattern pattern, DateFormat dateFormat, boolean z) {
            this.needToConvertTimeZone = false;
            this.pattern = pattern;
            this.formatter = dateFormat;
            this.needToConvertTimeZone = z;
        }

        public String convertTime(String str) {
            if (str.charAt(str.length() - 1) == 'Z') {
                return str.replace("Z", "+0000");
            }
            return str.replaceAll("([\\+\\-]\\d\\d):(\\d\\d)", "$1$2");
        }
    }

    static {
        XEP_0082_UTC_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormatter = DateFormatType.XEP_0082_DATE_PROFILE.createFormatter();
        datePattern = Pattern.compile("^\\d+-\\d+-\\d+$");
        timeFormatter = DateFormatType.XEP_0082_TIME_MILLIS_ZONE_PROFILE.createFormatter();
        timePattern = Pattern.compile("^(\\d+:){2}\\d+.\\d+(Z|([+-](\\d+:\\d+)))$");
        timeNoZoneFormatter = DateFormatType.XEP_0082_TIME_MILLIS_PROFILE.createFormatter();
        timeNoZonePattern = Pattern.compile("^(\\d+:){2}\\d+.\\d+$");
        timeNoMillisFormatter = DateFormatType.XEP_0082_TIME_ZONE_PROFILE.createFormatter();
        timeNoMillisPattern = Pattern.compile("^(\\d+:){2}\\d+(Z|([+-](\\d+:\\d+)))$");
        timeNoMillisNoZoneFormatter = DateFormatType.XEP_0082_TIME_PROFILE.createFormatter();
        timeNoMillisNoZonePattern = Pattern.compile("^(\\d+:){2}\\d+$");
        dateTimeFormatter = DateFormatType.XEP_0082_DATETIME_MILLIS_PROFILE.createFormatter();
        dateTimePattern = Pattern.compile("^\\d+(-\\d+){2}+T(\\d+:){2}\\d+.\\d+(Z|([+-](\\d+:\\d+)))?$");
        dateTimeNoMillisFormatter = DateFormatType.XEP_0082_DATETIME_PROFILE.createFormatter();
        dateTimeNoMillisPattern = Pattern.compile("^\\d+(-\\d+){2}+T(\\d+:){2}\\d+(Z|([+-](\\d+:\\d+)))?$");
        xep0091Formatter = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");
        xep0091Date6DigitFormatter = new SimpleDateFormat("yyyyMd'T'HH:mm:ss");
        xep0091Date7Digit1MonthFormatter = new SimpleDateFormat("yyyyMdd'T'HH:mm:ss");
        xep0091Date7Digit2MonthFormatter = new SimpleDateFormat("yyyyMMd'T'HH:mm:ss");
        xep0091Pattern = Pattern.compile("^\\d+T\\d+:\\d+:\\d+$");
        couplings = new ArrayList();
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        XEP_0082_UTC_FORMAT.setTimeZone(timeZone);
        dateFormatter.setTimeZone(timeZone);
        timeFormatter.setTimeZone(timeZone);
        timeNoZoneFormatter.setTimeZone(timeZone);
        timeNoMillisFormatter.setTimeZone(timeZone);
        timeNoMillisNoZoneFormatter.setTimeZone(timeZone);
        dateTimeFormatter.setTimeZone(timeZone);
        dateTimeNoMillisFormatter.setTimeZone(timeZone);
        xep0091Formatter.setTimeZone(timeZone);
        xep0091Date6DigitFormatter.setTimeZone(timeZone);
        xep0091Date7Digit1MonthFormatter.setTimeZone(timeZone);
        xep0091Date7Digit1MonthFormatter.setLenient(false);
        xep0091Date7Digit2MonthFormatter.setTimeZone(timeZone);
        xep0091Date7Digit2MonthFormatter.setLenient(false);
        couplings.add(new PatternCouplings(datePattern, dateFormatter));
        couplings.add(new PatternCouplings(dateTimePattern, dateTimeFormatter, true));
        couplings.add(new PatternCouplings(dateTimeNoMillisPattern, dateTimeNoMillisFormatter, true));
        couplings.add(new PatternCouplings(timePattern, timeFormatter, true));
        couplings.add(new PatternCouplings(timeNoZonePattern, timeNoZoneFormatter));
        couplings.add(new PatternCouplings(timeNoMillisPattern, timeNoMillisFormatter, true));
        couplings.add(new PatternCouplings(timeNoMillisNoZonePattern, timeNoMillisNoZoneFormatter));
        QUOTE_ENCODE = "&quot;".toCharArray();
        APOS_ENCODE = "&apos;".toCharArray();
        AMP_ENCODE = "&amp;".toCharArray();
        LT_ENCODE = "&lt;".toCharArray();
        GT_ENCODE = "&gt;".toCharArray();
        digest = null;
        randGen = new Random();
        numbersAndLetters = "0123456789abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    }

    public static Date parseXEP0082Date(String str) throws ParseException {
        return parseDate(str);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Date parseDate(java.lang.String r3) throws java.text.ParseException {
        /*
        r0 = xep0091Pattern;
        r0 = r0.matcher(r3);
        r0 = r0.matches();
        if (r0 == 0) goto L_0x0032;
    L_0x000c:
        r0 = "T";
        r0 = r3.split(r0);
        r1 = 0;
        r0 = r0[r1];
        r0 = r0.length();
        r1 = 8;
        if (r0 >= r1) goto L_0x0024;
    L_0x001d:
        r0 = handleDateWithMissingLeadingZeros(r3, r0);
        if (r0 == 0) goto L_0x0066;
    L_0x0023:
        return r0;
    L_0x0024:
        r1 = xep0091Formatter;
        monitor-enter(r1);
        r0 = xep0091Formatter;	 Catch:{ all -> 0x002f }
        r0 = r0.parse(r3);	 Catch:{ all -> 0x002f }
        monitor-exit(r1);	 Catch:{ all -> 0x002f }
        goto L_0x0023;
    L_0x002f:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x002f }
        throw r0;
    L_0x0032:
        r0 = couplings;
        r1 = r0.iterator();
    L_0x0038:
        r0 = r1.hasNext();
        if (r0 == 0) goto L_0x0066;
    L_0x003e:
        r0 = r1.next();
        r0 = (org.jivesoftware.smack.util.StringUtils.PatternCouplings) r0;
        r2 = r0.pattern;
        r2 = r2.matcher(r3);
        r2 = r2.matches();
        if (r2 == 0) goto L_0x0038;
    L_0x0050:
        r1 = r0.needToConvertTimeZone;
        if (r1 == 0) goto L_0x0058;
    L_0x0054:
        r3 = r0.convertTime(r3);
    L_0x0058:
        r1 = r0.formatter;
        monitor-enter(r1);
        r0 = r0.formatter;	 Catch:{ all -> 0x0063 }
        r0 = r0.parse(r3);	 Catch:{ all -> 0x0063 }
        monitor-exit(r1);	 Catch:{ all -> 0x0063 }
        goto L_0x0023;
    L_0x0063:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0063 }
        throw r0;
    L_0x0066:
        r1 = dateTimeNoMillisFormatter;
        monitor-enter(r1);
        r0 = dateTimeNoMillisFormatter;	 Catch:{ all -> 0x0071 }
        r0 = r0.parse(r3);	 Catch:{ all -> 0x0071 }
        monitor-exit(r1);	 Catch:{ all -> 0x0071 }
        goto L_0x0023;
    L_0x0071:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0071 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jivesoftware.smack.util.StringUtils.parseDate(java.lang.String):java.util.Date");
    }

    private static Date handleDateWithMissingLeadingZeros(String str, int i) throws ParseException {
        if (i == 6) {
            Date parse;
            synchronized (xep0091Date6DigitFormatter) {
                parse = xep0091Date6DigitFormatter.parse(str);
            }
            return parse;
        }
        Calendar instance = Calendar.getInstance();
        Calendar parseXEP91Date = parseXEP91Date(str, xep0091Date7Digit1MonthFormatter);
        Calendar parseXEP91Date2 = parseXEP91Date(str, xep0091Date7Digit2MonthFormatter);
        List filterDatesBefore = filterDatesBefore(instance, parseXEP91Date, parseXEP91Date2);
        if (filterDatesBefore.isEmpty()) {
            return null;
        }
        return determineNearestDate(instance, filterDatesBefore).getTime();
    }

    private static Calendar parseXEP91Date(String str, DateFormat dateFormat) {
        try {
            Calendar calendar;
            synchronized (dateFormat) {
                dateFormat.parse(str);
                calendar = dateFormat.getCalendar();
            }
            return calendar;
        } catch (ParseException e) {
            return null;
        }
    }

    private static List<Calendar> filterDatesBefore(Calendar calendar, Calendar... calendarArr) {
        List<Calendar> arrayList = new ArrayList();
        for (Calendar calendar2 : calendarArr) {
            if (calendar2 != null && calendar2.before(calendar)) {
                arrayList.add(calendar2);
            }
        }
        return arrayList;
    }

    private static Calendar determineNearestDate(Calendar calendar, List<Calendar> list) {
        Collections.sort(list, new C09661(calendar));
        return (Calendar) list.get(0);
    }

    public static String formatXEP0082Date(Date date) {
        String format;
        synchronized (dateTimeFormatter) {
            format = dateTimeFormatter.format(date);
        }
        return format;
    }

    public static String formatDate(Date date, DateFormatType dateFormatType) {
        return null;
    }

    public static String parseName(String str) {
        if (str == null) {
            return null;
        }
        int lastIndexOf = str.lastIndexOf(XmppConnection.JID_SEPARATOR);
        if (lastIndexOf <= 0) {
            return XmlPullParser.NO_NAMESPACE;
        }
        return str.substring(0, lastIndexOf);
    }

    public static String parseServer(String str) {
        if (str == null) {
            return null;
        }
        int lastIndexOf = str.lastIndexOf(XmppConnection.JID_SEPARATOR);
        if (lastIndexOf + 1 > str.length()) {
            return XmlPullParser.NO_NAMESPACE;
        }
        int indexOf = str.indexOf(FilePathGenerator.ANDROID_DIR_SEP);
        if (indexOf <= 0 || indexOf <= lastIndexOf) {
            return str.substring(lastIndexOf + 1);
        }
        return str.substring(lastIndexOf + 1, indexOf);
    }

    public static String parseResource(String str) {
        if (str == null) {
            return null;
        }
        int indexOf = str.indexOf(FilePathGenerator.ANDROID_DIR_SEP);
        if (indexOf + 1 > str.length() || indexOf < 0) {
            return XmlPullParser.NO_NAMESPACE;
        }
        return str.substring(indexOf + 1);
    }

    public static String parseBareAddress(String str) {
        if (str == null) {
            return null;
        }
        int indexOf = str.indexOf(FilePathGenerator.ANDROID_DIR_SEP);
        if (indexOf < 0) {
            return str;
        }
        if (indexOf == 0) {
            return XmlPullParser.NO_NAMESPACE;
        }
        return str.substring(0, indexOf);
    }

    public static String escapeNode(String str) {
        if (str == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder(str.length() + 8);
        int length = str.length();
        for (int i = 0; i < length; i++) {
            char charAt = str.charAt(i);
            switch (charAt) {
                case Type.ATMA /*34*/:
                    stringBuilder.append("\\22");
                    break;
                case Type.A6 /*38*/:
                    stringBuilder.append("\\26");
                    break;
                case Service.RLP /*39*/:
                    stringBuilder.append("\\27");
                    break;
                case Service.NI_FTP /*47*/:
                    stringBuilder.append("\\2f");
                    break;
                case Opcodes.ASTORE /*58*/:
                    stringBuilder.append("\\3a");
                    break;
                case RecordButton.MAX_TIME /*60*/:
                    stringBuilder.append("\\3c");
                    break;
                case Protocol.CFTP /*62*/:
                    stringBuilder.append("\\3e");
                    break;
                case WbxmlParser.WAP_EXTENSION /*64*/:
                    stringBuilder.append("\\40");
                    break;
                case Opcodes.DUP2 /*92*/:
                    stringBuilder.append("\\5c");
                    break;
                default:
                    if (!Character.isWhitespace(charAt)) {
                        stringBuilder.append(charAt);
                        break;
                    }
                    stringBuilder.append("\\20");
                    break;
            }
        }
        return stringBuilder.toString();
    }

    public static String unescapeNode(String str) {
        if (str == null) {
            return null;
        }
        char[] toCharArray = str.toCharArray();
        StringBuilder stringBuilder = new StringBuilder(toCharArray.length);
        int i = 0;
        int length = toCharArray.length;
        while (i < length) {
            char charAt = str.charAt(i);
            if (charAt == '\\' && i + 2 < length) {
                char c = toCharArray[i + 1];
                char c2 = toCharArray[i + 2];
                if (c == '2') {
                    switch (c2) {
                        case Type.DNSKEY /*48*/:
                            stringBuilder.append(' ');
                            i += 2;
                            continue;
                        case Type.NSEC3 /*50*/:
                            stringBuilder.append('\"');
                            i += 2;
                            continue;
                        case Opcodes.ISTORE /*54*/:
                            stringBuilder.append('&');
                            i += 2;
                            continue;
                        case Service.ISI_GL /*55*/:
                            stringBuilder.append('\'');
                            i += 2;
                            continue;
                        case Service.ISO_TSAP /*102*/:
                            stringBuilder.append('/');
                            i += 2;
                            continue;
                    }
                } else if (c == '3') {
                    switch (c2) {
                        case Service.SWIFT_RVF /*97*/:
                            stringBuilder.append(':');
                            i += 2;
                            continue;
                        case Service.METAGRAM /*99*/:
                            stringBuilder.append('<');
                            i += 2;
                            continue;
                        case Service.HOSTNAME /*101*/:
                            stringBuilder.append('>');
                            i += 2;
                            continue;
                        default:
                            break;
                    }
                } else if (c == '4') {
                    if (c2 == '0') {
                        stringBuilder.append(XmppConnection.JID_SEPARATOR);
                        i += 2;
                        i++;
                    }
                } else if (c == '5' && c2 == 'c') {
                    stringBuilder.append("\\");
                    i += 2;
                    i++;
                }
            }
            stringBuilder.append(charAt);
            i++;
        }
        return stringBuilder.toString();
    }

    public static String escapeForXML(String str) {
        int i = 0;
        if (str == null) {
            return null;
        }
        char[] toCharArray = str.toCharArray();
        int length = toCharArray.length;
        StringBuilder stringBuilder = new StringBuilder((int) (((double) length) * 1.3d));
        int i2 = 0;
        while (i2 < length) {
            char c = toCharArray[i2];
            if (c <= '>') {
                if (c == '<') {
                    if (i2 > i) {
                        stringBuilder.append(toCharArray, i, i2 - i);
                    }
                    i = i2 + 1;
                    stringBuilder.append(LT_ENCODE);
                } else if (c == '>') {
                    if (i2 > i) {
                        stringBuilder.append(toCharArray, i, i2 - i);
                    }
                    i = i2 + 1;
                    stringBuilder.append(GT_ENCODE);
                } else if (c == '&') {
                    if (i2 > i) {
                        stringBuilder.append(toCharArray, i, i2 - i);
                    }
                    if (length <= i2 + 5 || toCharArray[i2 + 1] != '#' || !Character.isDigit(toCharArray[i2 + 2]) || !Character.isDigit(toCharArray[i2 + 3]) || !Character.isDigit(toCharArray[i2 + 4]) || toCharArray[i2 + 5] != ';') {
                        i = i2 + 1;
                        stringBuilder.append(AMP_ENCODE);
                    }
                } else if (c == '\"') {
                    if (i2 > i) {
                        stringBuilder.append(toCharArray, i, i2 - i);
                    }
                    i = i2 + 1;
                    stringBuilder.append(QUOTE_ENCODE);
                } else if (c == '\'') {
                    if (i2 > i) {
                        stringBuilder.append(toCharArray, i, i2 - i);
                    }
                    i = i2 + 1;
                    stringBuilder.append(APOS_ENCODE);
                }
            }
            i2++;
        }
        if (i == 0) {
            return str;
        }
        if (i2 > i) {
            stringBuilder.append(toCharArray, i, i2 - i);
        }
        return stringBuilder.toString();
    }

    public static synchronized String hash(String str) {
        String encodeHex;
        synchronized (StringUtils.class) {
            if (digest == null) {
                try {
                    digest = MessageDigest.getInstance("SHA-1");
                } catch (NoSuchAlgorithmException e) {
                    System.err.println("Failed to load the SHA-1 MessageDigest. Jive will be unable to function normally.");
                }
            }
            try {
                digest.update(str.getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET));
            } catch (UnsupportedEncodingException e2) {
                System.err.println(e2);
            }
            encodeHex = encodeHex(digest.digest());
        }
        return encodeHex;
    }

    public static String encodeHex(byte[] bArr) {
        StringBuilder stringBuilder = new StringBuilder(bArr.length * 2);
        for (byte b : bArr) {
            if ((b & KEYRecord.PROTOCOL_ANY) < 16) {
                stringBuilder.append(Contact.RELATION_ASK);
            }
            stringBuilder.append(Integer.toString(b & KEYRecord.PROTOCOL_ANY, 16));
        }
        return stringBuilder.toString();
    }

    public static String encodeBase64(String str) {
        byte[] bArr = null;
        try {
            bArr = str.getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeBase64(bArr);
    }

    public static String encodeBase64(byte[] bArr) {
        return encodeBase64(bArr, false);
    }

    public static String encodeBase64(byte[] bArr, boolean z) {
        return encodeBase64(bArr, 0, bArr.length, z);
    }

    public static String encodeBase64(byte[] bArr, int i, int i2, boolean z) {
        return Base64.encodeBytes(bArr, i, i2, z ? 0 : 8);
    }

    public static byte[] decodeBase64(String str) {
        byte[] bytes;
        try {
            bytes = str.getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            bytes = str.getBytes();
        }
        return Base64.decode(bytes, 0, bytes.length, 0);
    }

    public static String randomString(int i) {
        if (i < 1) {
            return null;
        }
        char[] cArr = new char[i];
        for (int i2 = 0; i2 < cArr.length; i2++) {
            cArr[i2] = numbersAndLetters[randGen.nextInt(71)];
        }
        return new String(cArr);
    }

    private StringUtils() {
    }
}
