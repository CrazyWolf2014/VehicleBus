package org.xbill.DNS;

public final class Serial {
    private static final long MAX32 = 4294967295L;

    private Serial() {
    }

    public static int compare(long j, long j2) {
        if (j < 0 || j > MAX32) {
            throw new IllegalArgumentException(j + " out of range");
        } else if (j2 < 0 || j2 > MAX32) {
            throw new IllegalArgumentException(j2 + " out of range");
        } else {
            long j3 = j - j2;
            if (j3 >= MAX32) {
                j3 -= 4294967296L;
            } else if (j3 < -4294967295L) {
                j3 += 4294967296L;
            }
            return (int) j3;
        }
    }

    public static long increment(long j) {
        if (j < 0 || j > MAX32) {
            throw new IllegalArgumentException(j + " out of range");
        } else if (j == MAX32) {
            return 0;
        } else {
            return 1 + j;
        }
    }
}
