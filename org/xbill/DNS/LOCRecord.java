package org.xbill.DNS;

import com.tencent.mm.sdk.platformtools.Util;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xbill.DNS.Tokenizer.Token;

public class LOCRecord extends Record {
    private static final long serialVersionUID = 9058224788126750409L;
    private static NumberFormat w2;
    private static NumberFormat w3;
    private long altitude;
    private long hPrecision;
    private long latitude;
    private long longitude;
    private long size;
    private long vPrecision;

    static {
        w2 = new DecimalFormat();
        w2.setMinimumIntegerDigits(2);
        w3 = new DecimalFormat();
        w3.setMinimumIntegerDigits(3);
    }

    LOCRecord() {
    }

    Record getObject() {
        return new LOCRecord();
    }

    public LOCRecord(Name name, int i, long j, double d, double d2, double d3, double d4, double d5, double d6) {
        super(name, 29, i, j);
        this.latitude = (long) (((3600.0d * d) * 1000.0d) + 2.147483648E9d);
        this.longitude = (long) (((3600.0d * d2) * 1000.0d) + 2.147483648E9d);
        this.altitude = (long) ((100000.0d + d3) * 100.0d);
        this.size = (long) (100.0d * d4);
        this.hPrecision = (long) (100.0d * d5);
        this.vPrecision = (long) (100.0d * d6);
    }

    void rrFromWire(DNSInput dNSInput) throws IOException {
        if (dNSInput.readU8() != 0) {
            throw new WireParseException("Invalid LOC version");
        }
        this.size = parseLOCformat(dNSInput.readU8());
        this.hPrecision = parseLOCformat(dNSInput.readU8());
        this.vPrecision = parseLOCformat(dNSInput.readU8());
        this.latitude = dNSInput.readU32();
        this.longitude = dNSInput.readU32();
        this.altitude = dNSInput.readU32();
    }

    private double parseFixedPoint(String str) {
        if (str.matches("^-?\\d+$")) {
            return (double) Integer.parseInt(str);
        }
        if (str.matches("^-?\\d+\\.\\d*$")) {
            String[] split = str.split("\\.");
            double parseInt = (double) Integer.parseInt(split[0]);
            double parseInt2 = (double) Integer.parseInt(split[1]);
            if (parseInt < 0.0d) {
                parseInt2 *= -1.0d;
            }
            return (parseInt2 / Math.pow(10.0d, (double) split[1].length())) + parseInt;
        }
        throw new NumberFormatException();
    }

    private long parsePosition(Tokenizer tokenizer, String str) throws IOException {
        boolean equals = str.equals("latitude");
        int i = 0;
        double d = 0.0d;
        int uInt16 = tokenizer.getUInt16();
        if (uInt16 > Opcodes.GETFIELD || (uInt16 > 90 && equals)) {
            throw tokenizer.exception("Invalid LOC " + str + " degrees");
        }
        String string = tokenizer.getString();
        try {
            i = Integer.parseInt(string);
            if (i < 0 || i > 59) {
                throw tokenizer.exception("Invalid LOC " + str + " minutes");
            }
            d = parseFixedPoint(tokenizer.getString());
            if (d < 0.0d || d >= 60.0d) {
                throw tokenizer.exception("Invalid LOC " + str + " seconds");
            }
            string = tokenizer.getString();
            if (string.length() != 1) {
                throw tokenizer.exception("Invalid LOC " + str);
            }
            long j;
            long j2 = (long) ((d + ((double) (60 * (((long) i) + (60 * ((long) uInt16)))))) * 1000.0d);
            char toUpperCase = Character.toUpperCase(string.charAt(0));
            if ((equals && toUpperCase == 'S') || (!equals && toUpperCase == 'W')) {
                j = -j2;
            } else if ((!equals || toUpperCase == 'N') && (equals || toUpperCase == 'E')) {
                j = j2;
            } else {
                throw tokenizer.exception("Invalid LOC " + str);
            }
            return j + 2147483648L;
        } catch (NumberFormatException e) {
        }
    }

    private long parseDouble(Tokenizer tokenizer, String str, boolean z, long j, long j2, long j3) throws IOException {
        Token token = tokenizer.get();
        if (!token.isEOL()) {
            String str2 = token.value;
            if (str2.length() > 1 && str2.charAt(str2.length() - 1) == 'm') {
                str2 = str2.substring(0, str2.length() - 1);
            }
            try {
                j3 = (long) (100.0d * parseFixedPoint(str2));
                if (j3 < j || j3 > j2) {
                    throw tokenizer.exception("Invalid LOC " + str);
                }
            } catch (NumberFormatException e) {
                throw tokenizer.exception("Invalid LOC " + str);
            }
        } else if (z) {
            throw tokenizer.exception("Invalid LOC " + str);
        } else {
            tokenizer.unget();
        }
        return j3;
    }

    void rdataFromString(Tokenizer tokenizer, Name name) throws IOException {
        this.latitude = parsePosition(tokenizer, "latitude");
        this.longitude = parsePosition(tokenizer, "longitude");
        this.altitude = parseDouble(tokenizer, "altitude", true, -10000000, 4284967295L, 0) + 10000000;
        this.size = parseDouble(tokenizer, "size", false, 0, 9000000000L, 100);
        this.hPrecision = parseDouble(tokenizer, "horizontal precision", false, 0, 9000000000L, 1000000);
        this.vPrecision = parseDouble(tokenizer, "vertical precision", false, 0, 9000000000L, 1000);
    }

    private void renderFixedPoint(StringBuffer stringBuffer, NumberFormat numberFormat, long j, long j2) {
        stringBuffer.append(j / j2);
        long j3 = j % j2;
        if (j3 != 0) {
            stringBuffer.append(".");
            stringBuffer.append(numberFormat.format(j3));
        }
    }

    private String positionToString(long j, char c, char c2) {
        StringBuffer stringBuffer = new StringBuffer();
        long j2 = j - 2147483648L;
        if (j2 < 0) {
            j2 = -j2;
        } else {
            c2 = c;
        }
        stringBuffer.append(j2 / Util.MILLSECONDS_OF_HOUR);
        j2 %= Util.MILLSECONDS_OF_HOUR;
        stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuffer.append(j2 / Util.MILLSECONDS_OF_MINUTE);
        long j3 = j2 % Util.MILLSECONDS_OF_MINUTE;
        stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        renderFixedPoint(stringBuffer, w3, j3, 1000);
        stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuffer.append(c2);
        return stringBuffer.toString();
    }

    String rrToString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(positionToString(this.latitude, 'N', 'S'));
        stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuffer.append(positionToString(this.longitude, 'E', 'W'));
        stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        renderFixedPoint(stringBuffer, w2, this.altitude - 10000000, 100);
        stringBuffer.append("m ");
        renderFixedPoint(stringBuffer, w2, this.size, 100);
        stringBuffer.append("m ");
        renderFixedPoint(stringBuffer, w2, this.hPrecision, 100);
        stringBuffer.append("m ");
        renderFixedPoint(stringBuffer, w2, this.vPrecision, 100);
        stringBuffer.append("m");
        return stringBuffer.toString();
    }

    public double getLatitude() {
        return ((double) (this.latitude - 2147483648L)) / 3600000.0d;
    }

    public double getLongitude() {
        return ((double) (this.longitude - 2147483648L)) / 3600000.0d;
    }

    public double getAltitude() {
        return ((double) (this.altitude - 10000000)) / 100.0d;
    }

    public double getSize() {
        return ((double) this.size) / 100.0d;
    }

    public double getHPrecision() {
        return ((double) this.hPrecision) / 100.0d;
    }

    public double getVPrecision() {
        return ((double) this.vPrecision) / 100.0d;
    }

    void rrToWire(DNSOutput dNSOutput, Compression compression, boolean z) {
        dNSOutput.writeU8(0);
        dNSOutput.writeU8(toLOCformat(this.size));
        dNSOutput.writeU8(toLOCformat(this.hPrecision));
        dNSOutput.writeU8(toLOCformat(this.vPrecision));
        dNSOutput.writeU32(this.latitude);
        dNSOutput.writeU32(this.longitude);
        dNSOutput.writeU32(this.altitude);
    }

    private static long parseLOCformat(int i) throws WireParseException {
        long j = (long) (i >> 4);
        int i2 = i & 15;
        if (j > 9 || i2 > 9) {
            throw new WireParseException("Invalid LOC Encoding");
        }
        long j2 = j;
        while (true) {
            int i3 = i2 - 1;
            if (i2 <= 0) {
                return j2;
            }
            j2 *= 10;
            i2 = i3;
        }
    }

    private int toLOCformat(long j) {
        int i = 0;
        while (j > 9) {
            i = (byte) (i + 1);
            j /= 10;
        }
        return (int) ((j << 4) + ((long) i));
    }
}
