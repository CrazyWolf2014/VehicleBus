package com.thoughtworks.xstream.core.util;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.DataHolder;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.ObjectOutputStream.PutField;
import java.util.Map;

public class CustomObjectOutputStream extends ObjectOutputStream {
    private static final String DATA_HOLDER_KEY;
    private FastStack callbacks;
    private FastStack customFields;

    private class CustomPutField extends PutField {
        private final Map fields;

        private CustomPutField() {
            this.fields = new OrderRetainingMap();
        }

        public Map asMap() {
            return this.fields;
        }

        public void write(ObjectOutput out) throws IOException {
            CustomObjectOutputStream.this.peekCallback().writeToStream(asMap());
        }

        public void put(String name, Object val) {
            this.fields.put(name, val);
        }

        public void put(String name, byte val) {
            put(name, new Byte(val));
        }

        public void put(String name, char val) {
            put(name, new Character(val));
        }

        public void put(String name, double val) {
            put(name, new Double(val));
        }

        public void put(String name, float val) {
            put(name, new Float(val));
        }

        public void put(String name, int val) {
            put(name, new Integer(val));
        }

        public void put(String name, long val) {
            put(name, new Long(val));
        }

        public void put(String name, short val) {
            put(name, new Short(val));
        }

        public void put(String name, boolean val) {
            put(name, val ? Boolean.TRUE : Boolean.FALSE);
        }
    }

    public interface StreamCallback {
        void close() throws IOException;

        void defaultWriteObject() throws IOException;

        void flush() throws IOException;

        void writeFieldsToStream(Map map) throws IOException;

        void writeToStream(Object obj) throws IOException;
    }

    static {
        DATA_HOLDER_KEY = CustomObjectOutputStream.class.getName();
    }

    public static synchronized CustomObjectOutputStream getInstance(DataHolder whereFrom, StreamCallback callback) {
        CustomObjectOutputStream result;
        synchronized (CustomObjectOutputStream.class) {
            try {
                result = (CustomObjectOutputStream) whereFrom.get(DATA_HOLDER_KEY);
                if (result == null) {
                    result = new CustomObjectOutputStream(callback);
                    whereFrom.put(DATA_HOLDER_KEY, result);
                } else {
                    result.pushCallback(callback);
                }
            } catch (IOException e) {
                throw new ConversionException("Cannot create CustomObjectStream", e);
            }
        }
        return result;
    }

    public CustomObjectOutputStream(StreamCallback callback) throws IOException, SecurityException {
        this.callbacks = new FastStack(1);
        this.customFields = new FastStack(1);
        this.callbacks.push(callback);
    }

    public void pushCallback(StreamCallback callback) {
        this.callbacks.push(callback);
    }

    public StreamCallback popCallback() {
        return (StreamCallback) this.callbacks.pop();
    }

    public StreamCallback peekCallback() {
        return (StreamCallback) this.callbacks.peek();
    }

    public void defaultWriteObject() throws IOException {
        peekCallback().defaultWriteObject();
    }

    protected void writeObjectOverride(Object obj) throws IOException {
        peekCallback().writeToStream(obj);
    }

    public void writeBoolean(boolean val) throws IOException {
        peekCallback().writeToStream(val ? Boolean.TRUE : Boolean.FALSE);
    }

    public void writeByte(int val) throws IOException {
        peekCallback().writeToStream(new Byte((byte) val));
    }

    public void writeInt(int val) throws IOException {
        peekCallback().writeToStream(new Integer(val));
    }

    public void writeChar(int val) throws IOException {
        peekCallback().writeToStream(new Character((char) val));
    }

    public void writeDouble(double val) throws IOException {
        peekCallback().writeToStream(new Double(val));
    }

    public void writeFloat(float val) throws IOException {
        peekCallback().writeToStream(new Float(val));
    }

    public void writeLong(long val) throws IOException {
        peekCallback().writeToStream(new Long(val));
    }

    public void writeShort(int val) throws IOException {
        peekCallback().writeToStream(new Short((short) val));
    }

    public void write(byte[] buf) throws IOException {
        peekCallback().writeToStream(buf);
    }

    public void writeChars(String str) throws IOException {
        peekCallback().writeToStream(str.toCharArray());
    }

    public void writeUTF(String str) throws IOException {
        peekCallback().writeToStream(str);
    }

    public void write(int val) throws IOException {
        peekCallback().writeToStream(new Byte((byte) val));
    }

    public void write(byte[] buf, int off, int len) throws IOException {
        byte[] b = new byte[len];
        System.arraycopy(buf, off, b, 0, len);
        peekCallback().writeToStream(b);
    }

    public void flush() throws IOException {
        peekCallback().flush();
    }

    public void close() throws IOException {
        peekCallback().close();
    }

    public PutField putFields() {
        CustomPutField result = new CustomPutField();
        this.customFields.push(result);
        return result;
    }

    public void writeFields() throws IOException {
        peekCallback().writeFieldsToStream(((CustomPutField) this.customFields.pop()).asMap());
    }

    public void reset() {
        throw new UnsupportedOperationException();
    }

    public void useProtocolVersion(int version) {
        throw new UnsupportedOperationException();
    }

    public void writeBytes(String str) {
        throw new UnsupportedOperationException();
    }

    public void writeUnshared(Object obj) {
        throw new UnsupportedOperationException();
    }
}
