package hirondelle.date4j;

import java.lang.reflect.Array;

final class ModelUtil {
    static final int HASH_SEED = 23;
    private static final int fODD_PRIME_NUMBER = 37;

    enum NullsGo {
        FIRST,
        LAST
    }

    private ModelUtil() {
    }

    static boolean areEqual(char c, char c2) {
        return c == c2;
    }

    static boolean areEqual(double d, double d2) {
        return Double.doubleToLongBits(d) == Double.doubleToLongBits(d2);
    }

    static boolean areEqual(float f, float f2) {
        return Float.floatToIntBits(f) == Float.floatToIntBits(f2);
    }

    static boolean areEqual(long j, long j2) {
        return j == j2;
    }

    static boolean areEqual(Object obj, Object obj2) {
        if (!isArray(obj) && !isArray(obj2)) {
            return obj == null ? obj2 == null : obj.equals(obj2);
        } else {
            throw new IllegalArgumentException("This method does not currently support arrays.");
        }
    }

    static boolean areEqual(boolean z, boolean z2) {
        return z == z2;
    }

    static <T extends Comparable<T>> int comparePossiblyNull(T t, T t2, NullsGo nullsGo) {
        int i = 0;
        if (t != null && t2 != null) {
            return t.compareTo(t2);
        }
        if (!(t == null && t2 == null)) {
            if (t == null && t2 != null) {
                i = -1;
            } else if (t != null && t2 == null) {
                i = 1;
            }
        }
        return NullsGo.LAST == nullsGo ? i * -1 : i;
    }

    static boolean equalsFor(Object[] objArr, Object[] objArr2) {
        if (objArr.length != objArr2.length) {
            throw new IllegalArgumentException("Array lengths do not match. 'This' length is " + objArr.length + ", while 'That' length is " + objArr2.length + ".");
        }
        for (int i = 0; i < objArr.length; i++) {
            if (!areEqual(objArr[i], objArr2[i])) {
                return false;
            }
        }
        return true;
    }

    private static int firstTerm(int i) {
        return i * fODD_PRIME_NUMBER;
    }

    static int hash(int i, char c) {
        return firstTerm(i) + c;
    }

    static int hash(int i, double d) {
        return hash(i, Double.doubleToLongBits(d));
    }

    static int hash(int i, float f) {
        return hash(i, Float.floatToIntBits(f));
    }

    static int hash(int i, int i2) {
        return firstTerm(i) + i2;
    }

    static int hash(int i, long j) {
        return firstTerm(i) + ((int) ((j >>> 32) ^ j));
    }

    static int hash(int i, Object obj) {
        if (obj == null) {
            return hash(i, 0);
        }
        if (!isArray(obj)) {
            return hash(i, obj.hashCode());
        }
        int length = Array.getLength(obj);
        int i2 = 0;
        int i3 = i;
        while (i2 < length) {
            i = hash(i3, Array.get(obj, i2));
            i2++;
            i3 = i;
        }
        return i3;
    }

    static int hash(int i, boolean z) {
        return (z ? 1 : 0) + firstTerm(i);
    }

    static final int hashCodeFor(Object... objArr) {
        int i = HASH_SEED;
        for (Object hash : objArr) {
            i = hash(i, hash);
        }
        return i;
    }

    private static boolean isArray(Object obj) {
        return obj != null && obj.getClass().isArray();
    }

    static Boolean quickEquals(Object obj, Object obj2) {
        return obj == obj2 ? Boolean.TRUE : !obj.getClass().isInstance(obj2) ? Boolean.FALSE : null;
    }

    static String toStringAvoidCyclicRefs(Object obj, Class cls, String str) {
        return ToStringUtil.getTextAvoidCyclicRefs(obj, cls, str);
    }

    static String toStringFor(Object obj) {
        return ToStringUtil.getText(obj);
    }
}
