package com.kenai.jbosh;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

final class StaticBody extends AbstractBody {
    private static final int BUFFER_SIZE = 1024;
    private static final BodyParser PARSER;
    private final Map<BodyQName, String> attrs;
    private final String raw;

    static {
        PARSER = new BodyParserXmlPull();
    }

    private StaticBody(Map<BodyQName, String> map, String str) {
        this.attrs = map;
        this.raw = str;
    }

    public static StaticBody fromStream(InputStream inputStream) throws BOSHException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byte[] bArr = new byte[BUFFER_SIZE];
            int read;
            do {
                read = inputStream.read(bArr);
                if (read > 0) {
                    byteArrayOutputStream.write(bArr, 0, read);
                    continue;
                }
            } while (read >= 0);
            return fromString(byteArrayOutputStream.toString());
        } catch (Throwable e) {
            throw new BOSHException("Could not read body data", e);
        }
    }

    public static StaticBody fromString(String str) throws BOSHException {
        return new StaticBody(PARSER.parse(str).getAttributes(), str);
    }

    public Map<BodyQName, String> getAttributes() {
        return Collections.unmodifiableMap(this.attrs);
    }

    public String toXML() {
        return this.raw;
    }
}
