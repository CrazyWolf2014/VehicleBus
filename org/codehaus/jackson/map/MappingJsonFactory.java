package org.codehaus.jackson.map;

import org.codehaus.jackson.JsonFactory;

public class MappingJsonFactory extends JsonFactory {
    public MappingJsonFactory() {
        this(null);
    }

    public MappingJsonFactory(ObjectMapper mapper) {
        super(mapper);
        if (mapper == null) {
            setCodec(new ObjectMapper((JsonFactory) this));
        }
    }

    public final ObjectMapper getCodec() {
        return (ObjectMapper) this._objectCodec;
    }
}
