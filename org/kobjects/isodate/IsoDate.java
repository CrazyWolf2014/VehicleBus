package org.kobjects.isodate;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.codehaus.jackson.org.objectweb.asm.signature.SignatureVisitor;

public class IsoDate {
    public static final int DATE = 1;
    public static final int DATE_TIME = 3;
    public static final int TIME = 2;

    static void dd(StringBuffer buf, int i) {
        buf.append((char) ((i / 10) + 48));
        buf.append((char) ((i % 10) + 48));
    }

    public static String dateToString(Date date, int type) {
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT"));
        c.setTime(date);
        StringBuffer buf = new StringBuffer();
        if ((type & DATE) != 0) {
            int year = c.get(DATE);
            dd(buf, year / 100);
            dd(buf, year % 100);
            buf.append(SignatureVisitor.SUPER);
            dd(buf, (c.get(TIME) + 0) + DATE);
            buf.append(SignatureVisitor.SUPER);
            dd(buf, c.get(5));
            if (type == DATE_TIME) {
                buf.append(NDEFRecord.TEXT_WELL_KNOWN_TYPE);
            }
        }
        if ((type & TIME) != 0) {
            dd(buf, c.get(11));
            buf.append(':');
            dd(buf, c.get(12));
            buf.append(':');
            dd(buf, c.get(13));
            buf.append('.');
            int ms = c.get(14);
            buf.append((char) ((ms / 100) + 48));
            dd(buf, ms % 100);
            buf.append('Z');
        }
        return buf.toString();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Date stringToDate(java.lang.String r13, int r14) {
        /*
        r12 = 2;
        r11 = 14;
        r10 = 5;
        r9 = 11;
        r8 = 0;
        r0 = java.util.Calendar.getInstance();
        r5 = r14 & 1;
        if (r5 == 0) goto L_0x00e0;
    L_0x000f:
        r5 = 1;
        r6 = 4;
        r6 = r13.substring(r8, r6);
        r6 = java.lang.Integer.parseInt(r6);
        r0.set(r5, r6);
        r5 = 7;
        r5 = r13.substring(r10, r5);
        r5 = java.lang.Integer.parseInt(r5);
        r5 = r5 + -1;
        r5 = r5 + 0;
        r0.set(r12, r5);
        r5 = 8;
        r6 = 10;
        r5 = r13.substring(r5, r6);
        r5 = java.lang.Integer.parseInt(r5);
        r0.set(r10, r5);
        r5 = 3;
        if (r14 != r5) goto L_0x0044;
    L_0x003e:
        r5 = r13.length();
        if (r5 >= r9) goto L_0x0059;
    L_0x0044:
        r0.set(r9, r8);
        r5 = 12;
        r0.set(r5, r8);
        r5 = 13;
        r0.set(r5, r8);
        r0.set(r11, r8);
        r5 = r0.getTime();
    L_0x0058:
        return r5;
    L_0x0059:
        r13 = r13.substring(r9);
    L_0x005d:
        r5 = r13.substring(r8, r12);
        r5 = java.lang.Integer.parseInt(r5);
        r0.set(r9, r5);
        r5 = 12;
        r6 = 3;
        r6 = r13.substring(r6, r10);
        r6 = java.lang.Integer.parseInt(r6);
        r0.set(r5, r6);
        r5 = 13;
        r6 = 6;
        r7 = 8;
        r6 = r13.substring(r6, r7);
        r6 = java.lang.Integer.parseInt(r6);
        r0.set(r5, r6);
        r4 = 8;
        r5 = r13.length();
        if (r4 >= r5) goto L_0x00f3;
    L_0x008e:
        r5 = r13.charAt(r4);
        r6 = 46;
        if (r5 != r6) goto L_0x00f3;
    L_0x0096:
        r3 = 0;
        r2 = 100;
    L_0x0099:
        r4 = r4 + 1;
        r1 = r13.charAt(r4);
        r5 = 48;
        if (r1 < r5) goto L_0x00a7;
    L_0x00a3:
        r5 = 57;
        if (r1 <= r5) goto L_0x00ec;
    L_0x00a7:
        r0.set(r11, r3);
    L_0x00aa:
        r5 = r13.length();
        if (r4 >= r5) goto L_0x00da;
    L_0x00b0:
        r5 = r13.charAt(r4);
        r6 = 43;
        if (r5 == r6) goto L_0x00c0;
    L_0x00b8:
        r5 = r13.charAt(r4);
        r6 = 45;
        if (r5 != r6) goto L_0x00f7;
    L_0x00c0:
        r5 = new java.lang.StringBuilder;
        r6 = "GMT";
        r5.<init>(r6);
        r6 = r13.substring(r4);
        r5 = r5.append(r6);
        r5 = r5.toString();
        r5 = java.util.TimeZone.getTimeZone(r5);
        r0.setTimeZone(r5);
    L_0x00da:
        r5 = r0.getTime();
        goto L_0x0058;
    L_0x00e0:
        r5 = new java.util.Date;
        r6 = 0;
        r5.<init>(r6);
        r0.setTime(r5);
        goto L_0x005d;
    L_0x00ec:
        r5 = r1 + -48;
        r5 = r5 * r2;
        r3 = r3 + r5;
        r2 = r2 / 10;
        goto L_0x0099;
    L_0x00f3:
        r0.set(r11, r8);
        goto L_0x00aa;
    L_0x00f7:
        r5 = r13.charAt(r4);
        r6 = 90;
        if (r5 != r6) goto L_0x0109;
    L_0x00ff:
        r5 = "GMT";
        r5 = java.util.TimeZone.getTimeZone(r5);
        r0.setTimeZone(r5);
        goto L_0x00da;
    L_0x0109:
        r5 = new java.lang.RuntimeException;
        r6 = "illegal time format!";
        r5.<init>(r6);
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.kobjects.isodate.IsoDate.stringToDate(java.lang.String, int):java.util.Date");
    }
}
