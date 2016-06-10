package com.thoughtworks.xstream.io.json;

import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import com.thoughtworks.xstream.io.AbstractDriver;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.io.naming.NameCoder;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;

public class JsonHierarchicalStreamDriver extends AbstractDriver {
    public JsonHierarchicalStreamDriver(NameCoder nameCoder) {
        super(nameCoder);
    }

    public HierarchicalStreamReader createReader(Reader in) {
        throw new UnsupportedOperationException("The JsonHierarchicalStreamDriver can only write JSON");
    }

    public HierarchicalStreamReader createReader(InputStream in) {
        throw new UnsupportedOperationException("The JsonHierarchicalStreamDriver can only write JSON");
    }

    public HierarchicalStreamReader createReader(URL in) {
        throw new UnsupportedOperationException("The JsonHierarchicalStreamDriver can only write JSON");
    }

    public HierarchicalStreamReader createReader(File in) {
        throw new UnsupportedOperationException("The JsonHierarchicalStreamDriver can only write JSON");
    }

    public HierarchicalStreamWriter createWriter(Writer out) {
        return new JsonWriter(out);
    }

    public HierarchicalStreamWriter createWriter(OutputStream out) {
        try {
            return createWriter(new OutputStreamWriter(out, AsyncHttpResponseHandler.DEFAULT_CHARSET));
        } catch (Throwable e) {
            throw new StreamException(e);
        }
    }
}
