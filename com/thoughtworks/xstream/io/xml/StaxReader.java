package com.thoughtworks.xstream.io.xml;

import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.thoughtworks.xstream.converters.ErrorWriter;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.io.naming.NameCoder;
import javax.xml.stream.XMLStreamReader;

public class StaxReader extends AbstractPullReader {
    private final XMLStreamReader in;
    private final QNameMap qnameMap;

    public StaxReader(QNameMap qnameMap, XMLStreamReader in) {
        this(qnameMap, in, new XmlFriendlyNameCoder());
    }

    public StaxReader(QNameMap qnameMap, XMLStreamReader in, NameCoder replacer) {
        super(replacer);
        this.qnameMap = qnameMap;
        this.in = in;
        moveDown();
    }

    public StaxReader(QNameMap qnameMap, XMLStreamReader in, XmlFriendlyReplacer replacer) {
        this(qnameMap, in, (NameCoder) replacer);
    }

    protected int pullNextEvent() {
        try {
            switch (this.in.next()) {
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                    return 1;
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                    return 2;
                case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                    return 3;
                case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                    return 4;
                default:
                    return 0;
            }
        } catch (Throwable e) {
            throw new StreamException(e);
        }
    }

    protected String pullElementName() {
        return this.qnameMap.getJavaClassName(this.in.getName());
    }

    protected String pullText() {
        return this.in.getText();
    }

    public String getAttribute(String name) {
        return this.in.getAttributeValue(null, encodeAttribute(name));
    }

    public String getAttribute(int index) {
        return this.in.getAttributeValue(index);
    }

    public int getAttributeCount() {
        return this.in.getAttributeCount();
    }

    public String getAttributeName(int index) {
        return decodeAttribute(this.in.getAttributeLocalName(index));
    }

    public void appendErrors(ErrorWriter errorWriter) {
        errorWriter.add("line number", String.valueOf(this.in.getLocation().getLineNumber()));
    }

    public void close() {
        try {
            this.in.close();
        } catch (Throwable e) {
            throw new StreamException(e);
        }
    }
}
