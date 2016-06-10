package com.thoughtworks.xstream.core.util;

import com.thoughtworks.xstream.io.StreamException;
import java.io.Writer;
import org.xbill.DNS.KEYRecord.Flags;

public class QuickWriter {
    private char[] buffer;
    private int pointer;
    private final Writer writer;

    public QuickWriter(Writer writer) {
        this(writer, Flags.FLAG5);
    }

    public QuickWriter(Writer writer, int bufferSize) {
        this.writer = writer;
        this.buffer = new char[bufferSize];
    }

    public void write(String str) {
        int len = str.length();
        if (this.pointer + len >= this.buffer.length) {
            flush();
            if (len > this.buffer.length) {
                raw(str.toCharArray());
                return;
            }
        }
        str.getChars(0, len, this.buffer, this.pointer);
        this.pointer += len;
    }

    public void write(char c) {
        if (this.pointer + 1 >= this.buffer.length) {
            flush();
            if (this.buffer.length == 0) {
                raw(c);
                return;
            }
        }
        char[] cArr = this.buffer;
        int i = this.pointer;
        this.pointer = i + 1;
        cArr[i] = c;
    }

    public void write(char[] c) {
        int len = c.length;
        if (this.pointer + len >= this.buffer.length) {
            flush();
            if (len > this.buffer.length) {
                raw(c);
                return;
            }
        }
        System.arraycopy(c, 0, this.buffer, this.pointer, len);
        this.pointer += len;
    }

    public void flush() {
        try {
            this.writer.write(this.buffer, 0, this.pointer);
            this.pointer = 0;
            this.writer.flush();
        } catch (Throwable e) {
            throw new StreamException(e);
        }
    }

    public void close() {
        try {
            this.writer.write(this.buffer, 0, this.pointer);
            this.pointer = 0;
            this.writer.close();
        } catch (Throwable e) {
            throw new StreamException(e);
        }
    }

    private void raw(char[] c) {
        try {
            this.writer.write(c);
            this.writer.flush();
        } catch (Throwable e) {
            throw new StreamException(e);
        }
    }

    private void raw(char c) {
        try {
            this.writer.write(c);
            this.writer.flush();
        } catch (Throwable e) {
            throw new StreamException(e);
        }
    }
}
