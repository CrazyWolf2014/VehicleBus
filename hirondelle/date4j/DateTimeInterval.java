package hirondelle.date4j;

import hirondelle.date4j.DateTime.DayOverflow;

final class DateTimeInterval {
    private static final int MAX = 9999;
    private static final int MAX_NANOS = 999999999;
    private static final int MIN = 0;
    private static final boolean MINUS = false;
    private static final int MIN_NANOS = 0;
    private static final boolean PLUS = true;
    private Integer fDay;
    private int fDayIncr;
    private DayOverflow fDayOverflow;
    private final DateTime fFrom;
    private Integer fHour;
    private int fHourIncr;
    private boolean fIsPlus;
    private Integer fMinute;
    private int fMinuteIncr;
    private Integer fMonth;
    private int fMonthIncr;
    private Integer fNanosecond;
    private int fNanosecondIncr;
    private Integer fSecond;
    private int fSecondIncr;
    private Integer fYear;
    private int fYearIncr;

    DateTimeInterval(DateTime dateTime, DayOverflow dayOverflow) {
        int i = 1;
        int i2 = MIN_NANOS;
        this.fFrom = dateTime;
        checkUnits();
        this.fYear = Integer.valueOf(this.fFrom.getYear() == null ? 1 : this.fFrom.getYear().intValue());
        this.fMonth = Integer.valueOf(this.fFrom.getMonth() == null ? 1 : this.fFrom.getMonth().intValue());
        if (this.fFrom.getDay() != null) {
            i = this.fFrom.getDay().intValue();
        }
        this.fDay = Integer.valueOf(i);
        this.fHour = Integer.valueOf(this.fFrom.getHour() == null ? MIN_NANOS : this.fFrom.getHour().intValue());
        this.fMinute = Integer.valueOf(this.fFrom.getMinute() == null ? MIN_NANOS : this.fFrom.getMinute().intValue());
        this.fSecond = Integer.valueOf(this.fFrom.getSecond() == null ? MIN_NANOS : this.fFrom.getSecond().intValue());
        if (this.fFrom.getNanoseconds() != null) {
            i2 = this.fFrom.getNanoseconds().intValue();
        }
        this.fNanosecond = Integer.valueOf(i2);
        this.fDayOverflow = dayOverflow;
    }

    private void changeDay() {
        for (int i = MIN_NANOS; i < this.fDayIncr; i++) {
            stepDay();
        }
    }

    private void changeHour() {
        for (int i = MIN_NANOS; i < this.fHourIncr; i++) {
            stepHour();
        }
    }

    private void changeMinute() {
        for (int i = MIN_NANOS; i < this.fMinuteIncr; i++) {
            stepMinute();
        }
    }

    private void changeMonth() {
        for (int i = MIN_NANOS; i < this.fMonthIncr; i++) {
            stepMonth();
        }
    }

    private void changeNanosecond() {
        if (this.fIsPlus) {
            this.fNanosecond = Integer.valueOf(this.fNanosecond.intValue() + this.fNanosecondIncr);
        } else {
            this.fNanosecond = Integer.valueOf(this.fNanosecond.intValue() - this.fNanosecondIncr);
        }
        if (this.fNanosecond.intValue() > MAX_NANOS) {
            stepSecond();
            this.fNanosecond = Integer.valueOf((this.fNanosecond.intValue() - MAX_NANOS) - 1);
        } else if (this.fNanosecond.intValue() < 0) {
            stepSecond();
            this.fNanosecond = Integer.valueOf((this.fNanosecond.intValue() + MAX_NANOS) + 1);
        }
    }

    private void changeSecond() {
        for (int i = MIN_NANOS; i < this.fSecondIncr; i++) {
            stepSecond();
        }
    }

    private void changeYear() {
        if (this.fIsPlus) {
            this.fYear = Integer.valueOf(this.fYear.intValue() + this.fYearIncr);
        } else {
            this.fYear = Integer.valueOf(this.fFrom.getYear().intValue() - this.fYearIncr);
        }
    }

    private void checkRange(Integer num, String str) {
        if (num.intValue() < 0 || num.intValue() > MAX) {
            throw new IllegalArgumentException(str + " is not in the range " + MIN_NANOS + ".." + MAX);
        }
    }

    private void checkRangeNanos(Integer num) {
        if (num.intValue() < 0 || num.intValue() > MAX_NANOS) {
            throw new IllegalArgumentException("Nanosecond interval is not in the range 0..999999999");
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void checkUnits() {
        /*
        r8 = this;
        r7 = 3;
        r6 = 2;
        r1 = 0;
        r0 = 1;
        r2 = r8.fFrom;
        r3 = 6;
        r3 = new hirondelle.date4j.DateTime.Unit[r3];
        r4 = hirondelle.date4j.DateTime.Unit.YEAR;
        r3[r1] = r4;
        r4 = hirondelle.date4j.DateTime.Unit.MONTH;
        r3[r0] = r4;
        r4 = hirondelle.date4j.DateTime.Unit.DAY;
        r3[r6] = r4;
        r4 = hirondelle.date4j.DateTime.Unit.HOUR;
        r3[r7] = r4;
        r4 = 4;
        r5 = hirondelle.date4j.DateTime.Unit.MINUTE;
        r3[r4] = r5;
        r4 = 5;
        r5 = hirondelle.date4j.DateTime.Unit.SECOND;
        r3[r4] = r5;
        r2 = r2.unitsAllPresent(r3);
        if (r2 == 0) goto L_0x0033;
    L_0x0029:
        if (r0 != 0) goto L_0x008d;
    L_0x002b:
        r0 = new java.lang.IllegalArgumentException;
        r1 = "For interval calculations, DateTime must have year-month-day, or hour-minute-second, or both.";
        r0.<init>(r1);
        throw r0;
    L_0x0033:
        r2 = r8.fFrom;
        r3 = new hirondelle.date4j.DateTime.Unit[r7];
        r4 = hirondelle.date4j.DateTime.Unit.YEAR;
        r3[r1] = r4;
        r4 = hirondelle.date4j.DateTime.Unit.MONTH;
        r3[r0] = r4;
        r4 = hirondelle.date4j.DateTime.Unit.DAY;
        r3[r6] = r4;
        r2 = r2.unitsAllPresent(r3);
        if (r2 == 0) goto L_0x005f;
    L_0x0049:
        r2 = r8.fFrom;
        r3 = new hirondelle.date4j.DateTime.Unit[r7];
        r4 = hirondelle.date4j.DateTime.Unit.HOUR;
        r3[r1] = r4;
        r4 = hirondelle.date4j.DateTime.Unit.MINUTE;
        r3[r0] = r4;
        r4 = hirondelle.date4j.DateTime.Unit.SECOND;
        r3[r6] = r4;
        r2 = r2.unitsAllAbsent(r3);
        if (r2 != 0) goto L_0x0029;
    L_0x005f:
        r2 = r8.fFrom;
        r3 = new hirondelle.date4j.DateTime.Unit[r7];
        r4 = hirondelle.date4j.DateTime.Unit.YEAR;
        r3[r1] = r4;
        r4 = hirondelle.date4j.DateTime.Unit.MONTH;
        r3[r0] = r4;
        r4 = hirondelle.date4j.DateTime.Unit.DAY;
        r3[r6] = r4;
        r2 = r2.unitsAllAbsent(r3);
        if (r2 == 0) goto L_0x008b;
    L_0x0075:
        r2 = r8.fFrom;
        r3 = new hirondelle.date4j.DateTime.Unit[r7];
        r4 = hirondelle.date4j.DateTime.Unit.HOUR;
        r3[r1] = r4;
        r4 = hirondelle.date4j.DateTime.Unit.MINUTE;
        r3[r0] = r4;
        r4 = hirondelle.date4j.DateTime.Unit.SECOND;
        r3[r6] = r4;
        r2 = r2.unitsAllPresent(r3);
        if (r2 != 0) goto L_0x0029;
    L_0x008b:
        r0 = r1;
        goto L_0x0029;
    L_0x008d:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: hirondelle.date4j.DateTimeInterval.checkUnits():void");
    }

    private void handleMonthOverflow() {
        int numDaysInMonth = numDaysInMonth();
        if (this.fDay.intValue() <= numDaysInMonth) {
            return;
        }
        if (DayOverflow.Abort == this.fDayOverflow) {
            throw new RuntimeException("Day Overflow: Year:" + this.fYear + " Month:" + this.fMonth + " has " + numDaysInMonth + " days, but day has value:" + this.fDay + " To avoid these exceptions, please specify a different DayOverflow policy.");
        } else if (DayOverflow.FirstDay == this.fDayOverflow) {
            this.fDay = Integer.valueOf(1);
            stepMonth();
        } else if (DayOverflow.LastDay == this.fDayOverflow) {
            this.fDay = Integer.valueOf(numDaysInMonth);
        } else if (DayOverflow.Spillover == this.fDayOverflow) {
            this.fDay = Integer.valueOf(this.fDay.intValue() - numDaysInMonth);
            stepMonth();
        }
    }

    private int numDaysInMonth() {
        return DateTime.getNumDaysInMonth(this.fYear, this.fMonth).intValue();
    }

    private int numDaysInPreviousMonth() {
        return this.fMonth.intValue() > 1 ? DateTime.getNumDaysInMonth(this.fYear, Integer.valueOf(this.fMonth.intValue() - 1)).intValue() : DateTime.getNumDaysInMonth(Integer.valueOf(this.fYear.intValue() - 1), Integer.valueOf(12)).intValue();
    }

    private DateTime plusOrMinus(boolean z, Integer num, Integer num2, Integer num3, Integer num4, Integer num5, Integer num6, Integer num7) {
        this.fIsPlus = z;
        this.fYearIncr = num.intValue();
        this.fMonthIncr = num2.intValue();
        this.fDayIncr = num3.intValue();
        this.fHourIncr = num4.intValue();
        this.fMinuteIncr = num5.intValue();
        this.fSecondIncr = num6.intValue();
        this.fNanosecondIncr = num7.intValue();
        checkRange(Integer.valueOf(this.fYearIncr), "Year");
        checkRange(Integer.valueOf(this.fMonthIncr), "Month");
        checkRange(Integer.valueOf(this.fDayIncr), "Day");
        checkRange(Integer.valueOf(this.fHourIncr), "Hour");
        checkRange(Integer.valueOf(this.fMinuteIncr), "Minute");
        checkRange(Integer.valueOf(this.fSecondIncr), "Second");
        checkRangeNanos(Integer.valueOf(this.fNanosecondIncr));
        changeYear();
        changeMonth();
        handleMonthOverflow();
        changeDay();
        changeHour();
        changeMinute();
        changeSecond();
        changeNanosecond();
        return new DateTime(this.fYear, this.fMonth, this.fDay, this.fHour, this.fMinute, this.fSecond, this.fNanosecond);
    }

    private void stepDay() {
        if (this.fIsPlus) {
            this.fDay = Integer.valueOf(this.fDay.intValue() + 1);
        } else {
            this.fDay = Integer.valueOf(this.fDay.intValue() - 1);
        }
        if (this.fDay.intValue() > numDaysInMonth()) {
            this.fDay = Integer.valueOf(1);
            stepMonth();
        } else if (this.fDay.intValue() < 1) {
            this.fDay = Integer.valueOf(numDaysInPreviousMonth());
            stepMonth();
        }
    }

    private void stepHour() {
        if (this.fIsPlus) {
            this.fHour = Integer.valueOf(this.fHour.intValue() + 1);
        } else {
            this.fHour = Integer.valueOf(this.fHour.intValue() - 1);
        }
        if (this.fHour.intValue() > 23) {
            this.fHour = Integer.valueOf(MIN_NANOS);
            stepDay();
        } else if (this.fHour.intValue() < 0) {
            this.fHour = Integer.valueOf(23);
            stepDay();
        }
    }

    private void stepMinute() {
        if (this.fIsPlus) {
            this.fMinute = Integer.valueOf(this.fMinute.intValue() + 1);
        } else {
            this.fMinute = Integer.valueOf(this.fMinute.intValue() - 1);
        }
        if (this.fMinute.intValue() > 59) {
            this.fMinute = Integer.valueOf(MIN_NANOS);
            stepHour();
        } else if (this.fMinute.intValue() < 0) {
            this.fMinute = Integer.valueOf(59);
            stepHour();
        }
    }

    private void stepMonth() {
        if (this.fIsPlus) {
            this.fMonth = Integer.valueOf(this.fMonth.intValue() + 1);
        } else {
            this.fMonth = Integer.valueOf(this.fMonth.intValue() - 1);
        }
        if (this.fMonth.intValue() > 12) {
            this.fMonth = Integer.valueOf(1);
            stepYear();
        } else if (this.fMonth.intValue() < 1) {
            this.fMonth = Integer.valueOf(12);
            stepYear();
        }
    }

    private void stepSecond() {
        if (this.fIsPlus) {
            this.fSecond = Integer.valueOf(this.fSecond.intValue() + 1);
        } else {
            this.fSecond = Integer.valueOf(this.fSecond.intValue() - 1);
        }
        if (this.fSecond.intValue() > 59) {
            this.fSecond = Integer.valueOf(MIN_NANOS);
            stepMinute();
        } else if (this.fSecond.intValue() < 0) {
            this.fSecond = Integer.valueOf(59);
            stepMinute();
        }
    }

    private void stepYear() {
        if (this.fIsPlus) {
            this.fYear = Integer.valueOf(this.fYear.intValue() + 1);
        } else {
            this.fYear = Integer.valueOf(this.fYear.intValue() - 1);
        }
    }

    DateTime minus(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        return plusOrMinus(MINUS, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4), Integer.valueOf(i5), Integer.valueOf(i6), Integer.valueOf(i7));
    }

    DateTime plus(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        return plusOrMinus(PLUS, Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4), Integer.valueOf(i5), Integer.valueOf(i6), Integer.valueOf(i7));
    }
}
