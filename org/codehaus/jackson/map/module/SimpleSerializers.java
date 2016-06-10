package org.codehaus.jackson.map.module;

import java.util.HashMap;
import org.codehaus.jackson.map.BeanDescription;
import org.codehaus.jackson.map.BeanProperty;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.Serializers;
import org.codehaus.jackson.map.type.ClassKey;
import org.codehaus.jackson.type.JavaType;

public class SimpleSerializers implements Serializers {
    protected HashMap<ClassKey, JsonSerializer<?>> _classMappings;
    protected HashMap<ClassKey, JsonSerializer<?>> _interfaceMappings;

    public SimpleSerializers() {
        this._classMappings = null;
        this._interfaceMappings = null;
    }

    public void addSerializer(JsonSerializer<?> ser) {
        Class<?> cls = ser.handledType();
        if (cls == null || cls == Object.class) {
            throw new IllegalArgumentException("JsonSerializer of type " + ser.getClass().getName() + " does not define valid handledType() (use alternative registration method?)");
        }
        _addSerializer(cls, ser);
    }

    public <T> void addSerializer(Class<? extends T> type, JsonSerializer<T> ser) {
        _addSerializer(type, ser);
    }

    private void _addSerializer(Class<?> cls, JsonSerializer<?> ser) {
        ClassKey key = new ClassKey(cls);
        if (cls.isInterface()) {
            if (this._interfaceMappings == null) {
                this._interfaceMappings = new HashMap();
            }
            this._interfaceMappings.put(key, ser);
            return;
        }
        if (this._classMappings == null) {
            this._classMappings = new HashMap();
        }
        this._classMappings.put(key, ser);
    }

    public JsonSerializer<?> findSerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc, BeanProperty property) {
        JsonSerializer<?> ser;
        Class<?> cls = type.getRawClass();
        ClassKey key = new ClassKey(cls);
        if (cls.isInterface()) {
            if (this._interfaceMappings != null) {
                ser = (JsonSerializer) this._interfaceMappings.get(key);
                if (ser != null) {
                    return ser;
                }
            }
        } else if (this._classMappings != null) {
            ser = (JsonSerializer) this._classMappings.get(key);
            if (ser != null) {
                return ser;
            }
            for (Class<?> curr = cls; curr != null; curr = curr.getSuperclass()) {
                key.reset(curr);
                ser = (JsonSerializer) this._classMappings.get(key);
                if (ser != null) {
                    return ser;
                }
            }
        }
        if (this._interfaceMappings != null) {
            ser = _findInterfaceMapping(cls, key);
            if (ser != null) {
                return ser;
            }
            if (!cls.isInterface()) {
                do {
                    cls = cls.getSuperclass();
                    if (cls != null) {
                        ser = _findInterfaceMapping(cls, key);
                    }
                } while (ser == null);
                return ser;
            }
        }
        return null;
    }

    protected JsonSerializer<?> _findInterfaceMapping(Class<?> cls, ClassKey key) {
        for (Class<?> iface : cls.getInterfaces()) {
            key.reset(iface);
            JsonSerializer<?> ser = (JsonSerializer) this._interfaceMappings.get(key);
            if (ser != null) {
                return ser;
            }
            ser = _findInterfaceMapping(iface, key);
            if (ser != null) {
                return ser;
            }
        }
        return null;
    }
}
