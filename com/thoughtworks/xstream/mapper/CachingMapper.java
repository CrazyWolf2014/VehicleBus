package com.thoughtworks.xstream.mapper;

import com.thoughtworks.xstream.core.Caching;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.xbill.DNS.KEYRecord.Flags;

public class CachingMapper extends MapperWrapper implements Caching {
    private transient Map realClassCache;

    public CachingMapper(Mapper wrapped) {
        super(wrapped);
        readResolve();
    }

    public Class realClass(String elementName) {
        Object cached = this.realClassCache.get(elementName);
        if (cached == null) {
            try {
                Class result = super.realClass(elementName);
                this.realClassCache.put(elementName, result);
                return result;
            } catch (CannotResolveClassException e) {
                this.realClassCache.put(elementName, e);
                throw e;
            }
        } else if (cached instanceof Class) {
            return (Class) cached;
        } else {
            throw ((CannotResolveClassException) cached);
        }
    }

    public void flushCache() {
        this.realClassCache.clear();
    }

    private Object readResolve() {
        this.realClassCache = Collections.synchronizedMap(new HashMap(Flags.FLAG8));
        return this;
    }
}
