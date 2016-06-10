package com.google.zxing;

public class ResultPoint {
    private final float f921x;
    private final float f922y;

    public ResultPoint(float f, float f2) {
        this.f921x = f;
        this.f922y = f2;
    }

    private static float crossProductZ(ResultPoint resultPoint, ResultPoint resultPoint2, ResultPoint resultPoint3) {
        float f = resultPoint2.f921x;
        float f2 = resultPoint2.f922y;
        return ((resultPoint3.f921x - f) * (resultPoint.f922y - f2)) - ((resultPoint.f921x - f) * (resultPoint3.f922y - f2));
    }

    public static float distance(ResultPoint resultPoint, ResultPoint resultPoint2) {
        float x = resultPoint.getX() - resultPoint2.getX();
        float y = resultPoint.getY() - resultPoint2.getY();
        return (float) Math.sqrt((double) ((x * x) + (y * y)));
    }

    public static void orderBestPatterns(ResultPoint[] resultPointArr) {
        ResultPoint resultPoint;
        ResultPoint resultPoint2;
        ResultPoint resultPoint3;
        float distance = distance(resultPointArr[0], resultPointArr[1]);
        float distance2 = distance(resultPointArr[1], resultPointArr[2]);
        float distance3 = distance(resultPointArr[0], resultPointArr[2]);
        if (distance2 >= distance && distance2 >= distance3) {
            resultPoint = resultPointArr[0];
            resultPoint2 = resultPointArr[1];
            resultPoint3 = resultPointArr[2];
        } else if (distance3 < distance2 || distance3 < distance) {
            resultPoint = resultPointArr[2];
            resultPoint2 = resultPointArr[0];
            resultPoint3 = resultPointArr[1];
        } else {
            resultPoint = resultPointArr[1];
            resultPoint2 = resultPointArr[0];
            resultPoint3 = resultPointArr[2];
        }
        if (crossProductZ(resultPoint2, resultPoint, resultPoint3) >= 0.0f) {
            ResultPoint resultPoint4 = resultPoint3;
            resultPoint3 = resultPoint2;
            resultPoint2 = resultPoint4;
        }
        resultPointArr[0] = resultPoint3;
        resultPointArr[1] = resultPoint;
        resultPointArr[2] = resultPoint2;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ResultPoint)) {
            return false;
        }
        ResultPoint resultPoint = (ResultPoint) obj;
        return this.f921x == resultPoint.f921x && this.f922y == resultPoint.f922y;
    }

    public final float getX() {
        return this.f921x;
    }

    public final float getY() {
        return this.f922y;
    }

    public int hashCode() {
        return (Float.floatToIntBits(this.f921x) * 31) + Float.floatToIntBits(this.f922y);
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer(25);
        stringBuffer.append('(');
        stringBuffer.append(this.f921x);
        stringBuffer.append(',');
        stringBuffer.append(this.f922y);
        stringBuffer.append(')');
        return stringBuffer.toString();
    }
}
