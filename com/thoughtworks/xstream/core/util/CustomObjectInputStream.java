package com.thoughtworks.xstream.core.util;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.DataHolder;
import com.thoughtworks.xstream.core.ClassLoaderReference;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.NotActiveException;
import java.io.ObjectInputStream;
import java.io.ObjectInputStream.GetField;
import java.io.ObjectInputValidation;
import java.io.ObjectStreamClass;
import java.io.StreamCorruptedException;
import java.util.Map;
import org.xbill.DNS.WKSRecord.Service;

public class CustomObjectInputStream extends ObjectInputStream {
    private static final String DATA_HOLDER_KEY;
    private FastStack callbacks;
    private final ClassLoaderReference classLoaderReference;

    private class CustomGetField extends GetField {
        private Map fields;

        public CustomGetField(Map fields) {
            this.fields = fields;
        }

        public ObjectStreamClass getObjectStreamClass() {
            throw new UnsupportedOperationException();
        }

        private Object get(String name) {
            return this.fields.get(name);
        }

        public boolean defaulted(String name) {
            return !this.fields.containsKey(name);
        }

        public byte get(String name, byte val) {
            return defaulted(name) ? val : ((Byte) get(name)).byteValue();
        }

        public char get(String name, char val) {
            return defaulted(name) ? val : ((Character) get(name)).charValue();
        }

        public double get(String name, double val) {
            return defaulted(name) ? val : ((Double) get(name)).doubleValue();
        }

        public float get(String name, float val) {
            return defaulted(name) ? val : ((Float) get(name)).floatValue();
        }

        public int get(String name, int val) {
            return defaulted(name) ? val : ((Integer) get(name)).intValue();
        }

        public long get(String name, long val) {
            return defaulted(name) ? val : ((Long) get(name)).longValue();
        }

        public short get(String name, short val) {
            return defaulted(name) ? val : ((Short) get(name)).shortValue();
        }

        public boolean get(String name, boolean val) {
            return defaulted(name) ? val : ((Boolean) get(name)).booleanValue();
        }

        public Object get(String name, Object val) {
            return defaulted(name) ? val : get(name);
        }
    }

    public interface StreamCallback {
        void close() throws IOException;

        void defaultReadObject() throws IOException;

        Map readFieldsFromStream() throws IOException;

        Object readFromStream() throws IOException;

        void registerValidation(ObjectInputValidation objectInputValidation, int i) throws NotActiveException, InvalidObjectException;
    }

    static {
        DATA_HOLDER_KEY = CustomObjectInputStream.class.getName();
    }

    public static CustomObjectInputStream getInstance(DataHolder whereFrom, StreamCallback callback) {
        return getInstance(whereFrom, callback, (ClassLoader) null);
    }

    public static synchronized CustomObjectInputStream getInstance(DataHolder whereFrom, StreamCallback callback, ClassLoader classLoader) {
        CustomObjectInputStream instance;
        synchronized (CustomObjectInputStream.class) {
            instance = getInstance(whereFrom, callback, new ClassLoaderReference(classLoader));
        }
        return instance;
    }

    public static synchronized CustomObjectInputStream getInstance(DataHolder whereFrom, StreamCallback callback, ClassLoaderReference classLoaderReference) {
        CustomObjectInputStream result;
        synchronized (CustomObjectInputStream.class) {
            try {
                result = (CustomObjectInputStream) whereFrom.get(DATA_HOLDER_KEY);
                if (result == null) {
                    result = new CustomObjectInputStream(callback, classLoaderReference);
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

    public CustomObjectInputStream(StreamCallback callback, ClassLoaderReference classLoaderReference) throws IOException, SecurityException {
        this.callbacks = new FastStack(1);
        this.callbacks.push(callback);
        this.classLoaderReference = classLoaderReference;
    }

    public CustomObjectInputStream(StreamCallback callback, ClassLoader classLoader) throws IOException, SecurityException {
        this(callback, new ClassLoaderReference(classLoader));
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

    protected Class resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        ClassLoader classLoader = this.classLoaderReference.getReference();
        if (classLoader == null) {
            return super.resolveClass(desc);
        }
        return Class.forName(desc.getName(), false, classLoader);
    }

    public void defaultReadObject() throws IOException {
        peekCallback().defaultReadObject();
    }

    protected Object readObjectOverride() throws IOException {
        return peekCallback().readFromStream();
    }

    public Object readUnshared() throws IOException, ClassNotFoundException {
        return readObject();
    }

    public boolean readBoolean() throws IOException {
        return ((Boolean) peekCallback().readFromStream()).booleanValue();
    }

    public byte readByte() throws IOException {
        return ((Byte) peekCallback().readFromStream()).byteValue();
    }

    public int readUnsignedByte() throws IOException {
        int b = ((Byte) peekCallback().readFromStream()).byteValue();
        if (b < 0) {
            return b + Service.LOCUS_CON;
        }
        return b;
    }

    public int readInt() throws IOException {
        return ((Integer) peekCallback().readFromStream()).intValue();
    }

    public char readChar() throws IOException {
        return ((Character) peekCallback().readFromStream()).charValue();
    }

    public float readFloat() throws IOException {
        return ((Float) peekCallback().readFromStream()).floatValue();
    }

    public double readDouble() throws IOException {
        return ((Double) peekCallback().readFromStream()).doubleValue();
    }

    public long readLong() throws IOException {
        return ((Long) peekCallback().readFromStream()).longValue();
    }

    public short readShort() throws IOException {
        return ((Short) peekCallback().readFromStream()).shortValue();
    }

    public int readUnsignedShort() throws IOException {
        int b = ((Short) peekCallback().readFromStream()).shortValue();
        if (b < 0) {
            return b + 32767;
        }
        return b;
    }

    public String readUTF() throws IOException {
        return (String) peekCallback().readFromStream();
    }

    public void readFully(byte[] buf) throws IOException {
        readFully(buf, 0, buf.length);
    }

    public void readFully(byte[] buf, int off, int len) throws IOException {
        System.arraycopy((byte[]) peekCallback().readFromStream(), 0, buf, off, len);
    }

    public int read() throws IOException {
        return readUnsignedByte();
    }

    public int read(byte[] buf, int off, int len) throws IOException {
        byte[] b = (byte[]) peekCallback().readFromStream();
        if (b.length != len) {
            throw new StreamCorruptedException("Expected " + len + " bytes from stream, got " + b.length);
        }
        System.arraycopy(b, 0, buf, off, len);
        return len;
    }

    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    public GetField readFields() throws IOException {
        return new CustomGetField(peekCallback().readFieldsFromStream());
    }

    public void registerValidation(ObjectInputValidation validation, int priority) throws NotActiveException, InvalidObjectException {
        peekCallback().registerValidation(validation, priority);
    }

    public void close() throws IOException {
        peekCallback().close();
    }

    public int available() {
        throw new UnsupportedOperationException();
    }

    public String readLine() {
        throw new UnsupportedOperationException();
    }

    public int skipBytes(int len) {
        throw new UnsupportedOperationException();
    }

    public long skip(long n) {
        throw new UnsupportedOperationException();
    }

    public void mark(int readlimit) {
        throw new UnsupportedOperationException();
    }

    public void reset() {
        throw new UnsupportedOperationException();
    }

    public boolean markSupported() {
        return false;
    }
}
