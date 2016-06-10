package org.codehaus.jackson.node;

import java.io.IOException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.SerializerProvider;
import org.xmlpull.v1.XmlPullParser;

public final class MissingNode extends BaseJsonNode {
    private static final MissingNode instance;

    static {
        instance = new MissingNode();
    }

    private MissingNode() {
    }

    public static MissingNode getInstance() {
        return instance;
    }

    public JsonToken asToken() {
        return JsonToken.NOT_AVAILABLE;
    }

    public boolean isMissingNode() {
        return true;
    }

    public String getValueAsText() {
        return null;
    }

    public int getValueAsInt(int defaultValue) {
        return 0;
    }

    public long getValueAsLong(long defaultValue) {
        return 0;
    }

    public double getValueAsDouble(double defaultValue) {
        return 0.0d;
    }

    public JsonNode path(String fieldName) {
        return this;
    }

    public JsonNode path(int index) {
        return this;
    }

    public final void serialize(JsonGenerator jg, SerializerProvider provider) throws IOException, JsonProcessingException {
    }

    public boolean equals(Object o) {
        return o == this;
    }

    public String toString() {
        return XmlPullParser.NO_NAMESPACE;
    }
}
