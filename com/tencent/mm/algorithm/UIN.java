package com.tencent.mm.algorithm;

import com.tencent.mm.sdk.platformtools.Util;

public class UIN extends Number {
    private int f1616h;

    public UIN(int i) {
        this.f1616h = 0;
        this.f1616h = i;
    }

    public UIN(long j) {
        this.f1616h = 0;
        this.f1616h = (int) (-1 & j);
    }

    public static int valueOf(String str) {
        try {
            return new UIN(Long.valueOf(str).longValue()).intValue();
        } catch (Exception e) {
            return 0;
        }
    }

    public double doubleValue() {
        return ((double) (((long) this.f1616h) | 0)) + 0.0d;
    }

    public float floatValue() {
        return (float) (((double) (((long) this.f1616h) | 0)) + 0.0d);
    }

    public int intValue() {
        return this.f1616h;
    }

    public long longValue() {
        return ((long) this.f1616h) & Util.MAX_32BIT_VALUE;
    }

    public String toString() {
        return String.valueOf(((long) this.f1616h) & Util.MAX_32BIT_VALUE);
    }

    public int value() {
        return this.f1616h;
    }
}
