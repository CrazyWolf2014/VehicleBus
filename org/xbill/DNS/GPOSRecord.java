package org.xbill.DNS;

import com.mapabc.minimap.map.vmap.VMapProjection;
import java.io.IOException;
import org.codehaus.jackson.util.MinimalPrettyPrinter;

public class GPOSRecord extends Record {
    private static final long serialVersionUID = -6349714958085750705L;
    private byte[] altitude;
    private byte[] latitude;
    private byte[] longitude;

    GPOSRecord() {
    }

    Record getObject() {
        return new GPOSRecord();
    }

    private void validate(double d, double d2) throws IllegalArgumentException {
        if (d < -90.0d || d > 90.0d) {
            throw new IllegalArgumentException("illegal longitude " + d);
        } else if (d2 < VMapProjection.MinLongitude || d2 > VMapProjection.MaxLongitude) {
            throw new IllegalArgumentException("illegal latitude " + d2);
        }
    }

    public GPOSRecord(Name name, int i, long j, double d, double d2, double d3) {
        super(name, 27, i, j);
        validate(d, d2);
        this.longitude = Double.toString(d).getBytes();
        this.latitude = Double.toString(d2).getBytes();
        this.altitude = Double.toString(d3).getBytes();
    }

    public GPOSRecord(Name name, int i, long j, String str, String str2, String str3) {
        super(name, 27, i, j);
        try {
            this.longitude = Record.byteArrayFromString(str);
            this.latitude = Record.byteArrayFromString(str2);
            validate(getLongitude(), getLatitude());
            this.altitude = Record.byteArrayFromString(str3);
        } catch (TextParseException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    void rrFromWire(DNSInput dNSInput) throws IOException {
        this.longitude = dNSInput.readCountedString();
        this.latitude = dNSInput.readCountedString();
        this.altitude = dNSInput.readCountedString();
        try {
            validate(getLongitude(), getLatitude());
        } catch (IllegalArgumentException e) {
            throw new WireParseException(e.getMessage());
        }
    }

    void rdataFromString(Tokenizer tokenizer, Name name) throws IOException {
        try {
            this.longitude = Record.byteArrayFromString(tokenizer.getString());
            this.latitude = Record.byteArrayFromString(tokenizer.getString());
            this.altitude = Record.byteArrayFromString(tokenizer.getString());
            try {
                validate(getLongitude(), getLatitude());
            } catch (IllegalArgumentException e) {
                throw new WireParseException(e.getMessage());
            }
        } catch (TextParseException e2) {
            throw tokenizer.exception(e2.getMessage());
        }
    }

    String rrToString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(Record.byteArrayToString(this.longitude, true));
        stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuffer.append(Record.byteArrayToString(this.latitude, true));
        stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuffer.append(Record.byteArrayToString(this.altitude, true));
        return stringBuffer.toString();
    }

    public String getLongitudeString() {
        return Record.byteArrayToString(this.longitude, false);
    }

    public double getLongitude() {
        return Double.parseDouble(getLongitudeString());
    }

    public String getLatitudeString() {
        return Record.byteArrayToString(this.latitude, false);
    }

    public double getLatitude() {
        return Double.parseDouble(getLatitudeString());
    }

    public String getAltitudeString() {
        return Record.byteArrayToString(this.altitude, false);
    }

    public double getAltitude() {
        return Double.parseDouble(getAltitudeString());
    }

    void rrToWire(DNSOutput dNSOutput, Compression compression, boolean z) {
        dNSOutput.writeCountedString(this.longitude);
        dNSOutput.writeCountedString(this.latitude);
        dNSOutput.writeCountedString(this.altitude);
    }
}
