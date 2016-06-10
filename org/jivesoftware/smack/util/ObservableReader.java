package org.jivesoftware.smack.util;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class ObservableReader extends Reader {
    List<ReaderListener> listeners;
    Reader wrappedReader;

    public ObservableReader(Reader reader) {
        this.wrappedReader = null;
        this.listeners = new ArrayList();
        this.wrappedReader = reader;
    }

    public int read(char[] cArr, int i, int i2) throws IOException {
        int read = this.wrappedReader.read(cArr, i, i2);
        if (read > 0) {
            ReaderListener[] readerListenerArr;
            String str = new String(cArr, i, read);
            synchronized (this.listeners) {
                readerListenerArr = new ReaderListener[this.listeners.size()];
                this.listeners.toArray(readerListenerArr);
            }
            for (ReaderListener read2 : readerListenerArr) {
                read2.read(str);
            }
        }
        return read;
    }

    public void close() throws IOException {
        this.wrappedReader.close();
    }

    public int read() throws IOException {
        return this.wrappedReader.read();
    }

    public int read(char[] cArr) throws IOException {
        return this.wrappedReader.read(cArr);
    }

    public long skip(long j) throws IOException {
        return this.wrappedReader.skip(j);
    }

    public boolean ready() throws IOException {
        return this.wrappedReader.ready();
    }

    public boolean markSupported() {
        return this.wrappedReader.markSupported();
    }

    public void mark(int i) throws IOException {
        this.wrappedReader.mark(i);
    }

    public void reset() throws IOException {
        this.wrappedReader.reset();
    }

    public void addReaderListener(ReaderListener readerListener) {
        if (readerListener != null) {
            synchronized (this.listeners) {
                if (!this.listeners.contains(readerListener)) {
                    this.listeners.add(readerListener);
                }
            }
        }
    }

    public void removeReaderListener(ReaderListener readerListener) {
        synchronized (this.listeners) {
            this.listeners.remove(readerListener);
        }
    }
}
