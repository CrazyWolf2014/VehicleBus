package com.thoughtworks.xstream.core.util;

import com.thoughtworks.xstream.core.util.Pool.Factory;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ThreadSafeSimpleDateFormat {
    private final String formatString;
    private final Pool pool;
    private final TimeZone timeZone;

    /* renamed from: com.thoughtworks.xstream.core.util.ThreadSafeSimpleDateFormat.1 */
    class C11381 implements Factory {
        final /* synthetic */ boolean val$lenient;
        final /* synthetic */ Locale val$locale;

        C11381(Locale locale, boolean z) {
            this.val$locale = locale;
            this.val$lenient = z;
        }

        public Object newInstance() {
            SimpleDateFormat dateFormat = new SimpleDateFormat(ThreadSafeSimpleDateFormat.this.formatString, this.val$locale);
            dateFormat.setLenient(this.val$lenient);
            return dateFormat;
        }
    }

    public ThreadSafeSimpleDateFormat(String format, TimeZone timeZone, int initialPoolSize, int maxPoolSize, boolean lenient) {
        this(format, timeZone, Locale.ENGLISH, initialPoolSize, maxPoolSize, lenient);
    }

    public ThreadSafeSimpleDateFormat(String format, TimeZone timeZone, Locale locale, int initialPoolSize, int maxPoolSize, boolean lenient) {
        this.formatString = format;
        this.timeZone = timeZone;
        this.pool = new Pool(initialPoolSize, maxPoolSize, new C11381(locale, lenient));
    }

    public String format(Date date) {
        DateFormat format = fetchFromPool();
        try {
            String format2 = format.format(date);
            return format2;
        } finally {
            this.pool.putInPool(format);
        }
    }

    public Date parse(String date) throws ParseException {
        DateFormat format = fetchFromPool();
        try {
            Date parse = format.parse(date);
            return parse;
        } finally {
            this.pool.putInPool(format);
        }
    }

    private DateFormat fetchFromPool() {
        DateFormat format = (DateFormat) this.pool.fetchFromPool();
        TimeZone tz = this.timeZone != null ? this.timeZone : TimeZone.getDefault();
        if (!tz.equals(format.getTimeZone())) {
            format.setTimeZone(tz);
        }
        return format;
    }

    public String toString() {
        return this.formatString;
    }
}
