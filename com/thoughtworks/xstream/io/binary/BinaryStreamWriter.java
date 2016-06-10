package com.thoughtworks.xstream.io.binary;

import com.thoughtworks.xstream.io.ExtendedHierarchicalStreamWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.io.binary.Token.Attribute;
import com.thoughtworks.xstream.io.binary.Token.EndNode;
import com.thoughtworks.xstream.io.binary.Token.Formatter;
import com.thoughtworks.xstream.io.binary.Token.MapIdToValue;
import com.thoughtworks.xstream.io.binary.Token.StartNode;
import com.thoughtworks.xstream.io.binary.Token.Value;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class BinaryStreamWriter implements ExtendedHierarchicalStreamWriter {
    private final IdRegistry idRegistry;
    private final DataOutputStream out;
    private final Formatter tokenFormatter;

    private class IdRegistry {
        private Map ids;
        private long nextId;

        private IdRegistry() {
            this.nextId = 0;
            this.ids = new HashMap();
        }

        public long getId(String value) {
            Long id = (Long) this.ids.get(value);
            if (id == null) {
                long j = this.nextId + 1;
                this.nextId = j;
                id = new Long(j);
                this.ids.put(value, id);
                BinaryStreamWriter.this.write(new MapIdToValue(id.longValue(), value));
            }
            return id.longValue();
        }
    }

    public BinaryStreamWriter(OutputStream outputStream) {
        this.idRegistry = new IdRegistry();
        this.tokenFormatter = new Formatter();
        this.out = new DataOutputStream(outputStream);
    }

    public void startNode(String name) {
        write(new StartNode(this.idRegistry.getId(name)));
    }

    public void startNode(String name, Class clazz) {
        startNode(name);
    }

    public void addAttribute(String name, String value) {
        write(new Attribute(this.idRegistry.getId(name), value));
    }

    public void setValue(String text) {
        write(new Value(text));
    }

    public void endNode() {
        write(new EndNode());
    }

    public void flush() {
        try {
            this.out.flush();
        } catch (Throwable e) {
            throw new StreamException(e);
        }
    }

    public void close() {
        try {
            this.out.close();
        } catch (Throwable e) {
            throw new StreamException(e);
        }
    }

    public HierarchicalStreamWriter underlyingWriter() {
        return this;
    }

    private void write(Token token) {
        try {
            this.tokenFormatter.write(this.out, token);
        } catch (Throwable e) {
            throw new StreamException(e);
        }
    }
}
