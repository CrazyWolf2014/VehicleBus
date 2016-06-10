package org.codehaus.jackson.map.module;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.Module;
import org.codehaus.jackson.map.Module.SetupContext;

public class SimpleModule extends Module {
    protected SimpleDeserializers _deserializers;
    protected final String _name;
    protected SimpleSerializers _serializers;
    protected final Version _version;

    public SimpleModule(String name, Version version) {
        this._serializers = null;
        this._deserializers = null;
        this._name = name;
        this._version = version;
    }

    public SimpleModule addSerializer(JsonSerializer<?> ser) {
        if (this._serializers == null) {
            this._serializers = new SimpleSerializers();
        }
        this._serializers.addSerializer(ser);
        return this;
    }

    public <T> SimpleModule addSerializer(Class<? extends T> type, JsonSerializer<T> ser) {
        if (this._serializers == null) {
            this._serializers = new SimpleSerializers();
        }
        this._serializers.addSerializer(type, ser);
        return this;
    }

    public <T> SimpleModule addDeserializer(Class<T> type, JsonDeserializer<? extends T> deser) {
        if (this._deserializers == null) {
            this._deserializers = new SimpleDeserializers();
        }
        this._deserializers.addDeserializer(type, deser);
        return this;
    }

    public String getModuleName() {
        return this._name;
    }

    public void setupModule(SetupContext context) {
        if (this._serializers != null) {
            context.addSerializers(this._serializers);
        }
        if (this._deserializers != null) {
            context.addDeserializers(this._deserializers);
        }
    }

    public Version version() {
        return this._version;
    }
}
