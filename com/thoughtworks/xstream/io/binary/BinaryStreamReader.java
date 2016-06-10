package com.thoughtworks.xstream.io.binary;

import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.thoughtworks.xstream.converters.ErrorWriter;
import com.thoughtworks.xstream.io.ExtendedHierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.io.binary.Token.Formatter;
import java.io.DataInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BinaryStreamReader implements ExtendedHierarchicalStreamReader {
    private final ReaderDepthState depthState;
    private final IdRegistry idRegistry;
    private final DataInputStream in;
    private Token pushback;
    private final Formatter tokenFormatter;

    private static class IdRegistry {
        private Map map;

        private IdRegistry() {
            this.map = new HashMap();
        }

        public void put(long id, String value) {
            this.map.put(new Long(id), value);
        }

        public String get(long id) {
            String result = (String) this.map.get(new Long(id));
            if (result != null) {
                return result;
            }
            throw new StreamException("Unknown ID : " + id);
        }
    }

    public BinaryStreamReader(InputStream inputStream) {
        this.depthState = new ReaderDepthState();
        this.idRegistry = new IdRegistry();
        this.tokenFormatter = new Formatter();
        this.in = new DataInputStream(inputStream);
        moveDown();
    }

    public boolean hasMoreChildren() {
        return this.depthState.hasMoreChildren();
    }

    public String getNodeName() {
        return this.depthState.getName();
    }

    public String getValue() {
        return this.depthState.getValue();
    }

    public String getAttribute(String name) {
        return this.depthState.getAttribute(name);
    }

    public String getAttribute(int index) {
        return this.depthState.getAttribute(index);
    }

    public int getAttributeCount() {
        return this.depthState.getAttributeCount();
    }

    public String getAttributeName(int index) {
        return this.depthState.getAttributeName(index);
    }

    public Iterator getAttributeNames() {
        return this.depthState.getAttributeNames();
    }

    public void moveDown() {
        this.depthState.push();
        Token firstToken = readToken();
        switch (firstToken.getType()) {
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                this.depthState.setName(this.idRegistry.get(firstToken.getId()));
                while (true) {
                    Token nextToken = readToken();
                    switch (nextToken.getType()) {
                        case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                            this.depthState.setHasMoreChildren(true);
                            pushBack(nextToken);
                            return;
                        case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                            this.depthState.setHasMoreChildren(false);
                            pushBack(nextToken);
                            return;
                        case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                            this.depthState.addAttribute(this.idRegistry.get(nextToken.getId()), nextToken.getValue());
                            break;
                        case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                            this.depthState.setValue(nextToken.getValue());
                            break;
                        default:
                            throw new StreamException("Unexpected token " + nextToken);
                    }
                }
            default:
                throw new StreamException("Expected StartNode");
        }
    }

    public void moveUp() {
        this.depthState.pop();
        int depth = 0;
        while (true) {
            switch (readToken().getType()) {
                case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                    depth++;
                    break;
                case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                    if (depth != 0) {
                        depth--;
                        break;
                    }
                    Token nextToken = readToken();
                    switch (nextToken.getType()) {
                        case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                            this.depthState.setHasMoreChildren(true);
                            break;
                        case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                            this.depthState.setHasMoreChildren(false);
                            break;
                        default:
                            throw new StreamException("Unexpected token " + nextToken);
                    }
                    pushBack(nextToken);
                    return;
                default:
                    break;
            }
        }
    }

    private Token readToken() {
        if (this.pushback == null) {
            try {
                Token token = this.tokenFormatter.read(this.in);
                switch (token.getType()) {
                    case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                        this.idRegistry.put(token.getId(), token.getValue());
                        return readToken();
                    default:
                        return token;
                }
            } catch (Throwable e) {
                throw new StreamException(e);
            }
        }
        Token result = this.pushback;
        this.pushback = null;
        return result;
    }

    public void pushBack(Token token) {
        if (this.pushback == null) {
            this.pushback = token;
            return;
        }
        throw new Error("Cannot push more than one token back");
    }

    public void close() {
        try {
            this.in.close();
        } catch (Throwable e) {
            throw new StreamException(e);
        }
    }

    public String peekNextChild() {
        if (this.depthState.hasMoreChildren()) {
            return this.idRegistry.get(this.pushback.getId());
        }
        return null;
    }

    public HierarchicalStreamReader underlyingReader() {
        return this;
    }

    public void appendErrors(ErrorWriter errorWriter) {
    }
}
