package com.thoughtworks.xstream.io;

import com.thoughtworks.xstream.io.naming.NameCoder;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;

public abstract class AbstractDriver implements HierarchicalStreamDriver {
    private NameCoder replacer;

    public AbstractDriver() {
        this(new NoNameCoder());
    }

    public AbstractDriver(NameCoder nameCoder) {
        this.replacer = nameCoder;
    }

    protected NameCoder getNameCoder() {
        return this.replacer;
    }

    public HierarchicalStreamReader createReader(URL in) {
        try {
            return createReader(in.openStream());
        } catch (Throwable e) {
            throw new StreamException(e);
        }
    }

    public HierarchicalStreamReader createReader(File in) {
        try {
            return createReader(new FileInputStream(in));
        } catch (Throwable e) {
            throw new StreamException(e);
        }
    }
}
