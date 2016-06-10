package com.thoughtworks.xstream.io.xml;

import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.thoughtworks.xstream.converters.ErrorWriter;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.io.naming.NameCoder;
import java.io.Reader;
import org.xbill.DNS.KEYRecord;
import org.xmlpull.v1.XmlPullParser;

public class XppReader extends AbstractPullReader {
    private final XmlPullParser parser;
    private final Reader reader;

    public XppReader(Reader reader, XmlPullParser parser) {
        this(reader, parser, new XmlFriendlyNameCoder());
    }

    public XppReader(Reader reader, XmlPullParser parser, NameCoder nameCoder) {
        super(nameCoder);
        this.parser = parser;
        this.reader = reader;
        try {
            parser.setInput(this.reader);
            moveDown();
        } catch (Throwable e) {
            throw new StreamException(e);
        }
    }

    public XppReader(Reader reader) {
        this(reader, new XmlFriendlyReplacer());
    }

    public XppReader(Reader reader, XmlFriendlyReplacer replacer) {
        super(replacer);
        try {
            this.parser = createParser();
            this.reader = reader;
            this.parser.setInput(this.reader);
            moveDown();
        } catch (Throwable e) {
            throw new StreamException(e);
        }
    }

    protected XmlPullParser createParser() {
        Exception exception;
        try {
            return (XmlPullParser) Class.forName("org.xmlpull.mxp1.MXParser", true, XmlPullParser.class.getClassLoader()).newInstance();
        } catch (Exception e) {
            exception = e;
            throw new StreamException("Cannot create Xpp3 parser instance.", exception);
        } catch (Exception e2) {
            exception = e2;
            throw new StreamException("Cannot create Xpp3 parser instance.", exception);
        } catch (Exception e22) {
            exception = e22;
            throw new StreamException("Cannot create Xpp3 parser instance.", exception);
        }
    }

    protected int pullNextEvent() {
        try {
            switch (this.parser.next()) {
                case KEYRecord.OWNER_USER /*0*/:
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    return 1;
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                    return 2;
                case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                    return 3;
                case FileOptions.OPTIMIZE_FOR_FIELD_NUMBER /*9*/:
                    return 4;
                default:
                    return 0;
            }
        } catch (Throwable e) {
            throw new StreamException(e);
        } catch (Throwable e2) {
            throw new StreamException(e2);
        }
    }

    protected String pullElementName() {
        return this.parser.getName();
    }

    protected String pullText() {
        return this.parser.getText();
    }

    public String getAttribute(String name) {
        return this.parser.getAttributeValue(null, encodeAttribute(name));
    }

    public String getAttribute(int index) {
        return this.parser.getAttributeValue(index);
    }

    public int getAttributeCount() {
        return this.parser.getAttributeCount();
    }

    public String getAttributeName(int index) {
        return decodeAttribute(this.parser.getAttributeName(index));
    }

    public void appendErrors(ErrorWriter errorWriter) {
        errorWriter.add("line number", String.valueOf(this.parser.getLineNumber()));
    }

    public void close() {
        try {
            this.reader.close();
        } catch (Throwable e) {
            throw new StreamException(e);
        }
    }
}
