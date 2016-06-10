package org.codehaus.jackson.map.ser.impl;

import java.io.IOException;
import java.util.TimeZone;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.ScalarSerializerBase;

public class TimeZoneSerializer extends ScalarSerializerBase<TimeZone> {
    public static final TimeZoneSerializer instance;

    static {
        instance = new TimeZoneSerializer();
    }

    public TimeZoneSerializer() {
        super(TimeZone.class);
    }

    public void serialize(TimeZone value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        jgen.writeString(value.getID());
    }
}
