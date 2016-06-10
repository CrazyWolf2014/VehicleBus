package com.thoughtworks.xstream.io.xml;

import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.io.naming.NameCoder;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;

public class SjsxpDriver extends StaxDriver {
    public SjsxpDriver(QNameMap qnameMap, XmlFriendlyNameCoder nameCoder) {
        super(qnameMap, (NameCoder) nameCoder);
    }

    public SjsxpDriver(QNameMap qnameMap) {
        super(qnameMap);
    }

    public SjsxpDriver(XmlFriendlyNameCoder nameCoder) {
        super((NameCoder) nameCoder);
    }

    protected XMLInputFactory createInputFactory() {
        Exception exception;
        try {
            return (XMLInputFactory) Class.forName("com.sun.xml.internal.stream.XMLInputFactoryImpl").newInstance();
        } catch (Exception e) {
            exception = e;
            throw new StreamException("Cannot create SJSXP (Sun JDK 6 StAX) XMLInputFaqctory instance.", exception);
        } catch (Exception e2) {
            exception = e2;
            throw new StreamException("Cannot create SJSXP (Sun JDK 6 StAX) XMLInputFaqctory instance.", exception);
        } catch (Exception e22) {
            exception = e22;
            throw new StreamException("Cannot create SJSXP (Sun JDK 6 StAX) XMLInputFaqctory instance.", exception);
        }
    }

    protected XMLOutputFactory createOutputFactory() {
        Exception exception;
        try {
            return (XMLOutputFactory) Class.forName("com.sun.xml.internal.stream.XMLOutputFactoryImpl").newInstance();
        } catch (Exception e) {
            exception = e;
            throw new StreamException("Cannot create SJSXP (Sun JDK 6 StAX) XMLOutputFaqctory instance.", exception);
        } catch (Exception e2) {
            exception = e2;
            throw new StreamException("Cannot create SJSXP (Sun JDK 6 StAX) XMLOutputFaqctory instance.", exception);
        } catch (Exception e22) {
            exception = e22;
            throw new StreamException("Cannot create SJSXP (Sun JDK 6 StAX) XMLOutputFaqctory instance.", exception);
        }
    }
}
