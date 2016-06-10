package org.codehaus.jackson.node;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser.NumberType;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.io.NumberOutput;
import org.codehaus.jackson.map.SerializerProvider;

public final class IntNode extends NumericNode {
    private static final IntNode[] CANONICALS;
    static final int MAX_CANONICAL = 10;
    static final int MIN_CANONICAL = -1;
    final int _value;

    static {
        CANONICALS = new IntNode[12];
        for (int i = 0; i < 12; i++) {
            CANONICALS[i] = new IntNode(i + MIN_CANONICAL);
        }
    }

    public IntNode(int v) {
        this._value = v;
    }

    public static IntNode valueOf(int i) {
        if (i > MAX_CANONICAL || i < MIN_CANONICAL) {
            return new IntNode(i);
        }
        return CANONICALS[i + 1];
    }

    public JsonToken asToken() {
        return JsonToken.VALUE_NUMBER_INT;
    }

    public NumberType getNumberType() {
        return NumberType.INT;
    }

    public boolean isIntegralNumber() {
        return true;
    }

    public boolean isInt() {
        return true;
    }

    public Number getNumberValue() {
        return Integer.valueOf(this._value);
    }

    public int getIntValue() {
        return this._value;
    }

    public long getLongValue() {
        return (long) this._value;
    }

    public double getDoubleValue() {
        return (double) this._value;
    }

    public BigDecimal getDecimalValue() {
        return BigDecimal.valueOf((long) this._value);
    }

    public BigInteger getBigIntegerValue() {
        return BigInteger.valueOf((long) this._value);
    }

    public String getValueAsText() {
        return NumberOutput.toString(this._value);
    }

    public boolean getValueAsBoolean(boolean defaultValue) {
        return this._value != 0;
    }

    public final void serialize(JsonGenerator jg, SerializerProvider provider) throws IOException, JsonProcessingException {
        jg.writeNumber(this._value);
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o.getClass() != getClass()) {
            return false;
        }
        if (((IntNode) o)._value != this._value) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this._value;
    }
}
