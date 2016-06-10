package org.codehaus.jackson.node;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.codehaus.jackson.JsonParser.NumberType;

public abstract class NumericNode extends ValueNode {
    public abstract BigInteger getBigIntegerValue();

    public abstract BigDecimal getDecimalValue();

    public abstract double getDoubleValue();

    public abstract int getIntValue();

    public abstract long getLongValue();

    public abstract NumberType getNumberType();

    public abstract Number getNumberValue();

    public abstract String getValueAsText();

    protected NumericNode() {
    }

    public final boolean isNumber() {
        return true;
    }

    public int getValueAsInt() {
        return getIntValue();
    }

    public int getValueAsInt(int defaultValue) {
        return getIntValue();
    }

    public long getValueAsLong() {
        return getLongValue();
    }

    public long getValueAsLong(long defaultValue) {
        return getLongValue();
    }

    public double getValueAsDouble() {
        return getDoubleValue();
    }

    public double getValueAsDouble(double defaultValue) {
        return getDoubleValue();
    }
}
