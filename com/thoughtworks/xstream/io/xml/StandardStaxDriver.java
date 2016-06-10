package com.thoughtworks.xstream.io.xml;

import com.thoughtworks.xstream.core.JVM;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.io.naming.NameCoder;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;

public class StandardStaxDriver extends StaxDriver {
    public StandardStaxDriver(QNameMap qnameMap, XmlFriendlyNameCoder nameCoder) {
        super(qnameMap, (NameCoder) nameCoder);
    }

    public StandardStaxDriver(QNameMap qnameMap) {
        super(qnameMap);
    }

    public StandardStaxDriver(XmlFriendlyNameCoder nameCoder) {
        super((NameCoder) nameCoder);
    }

    protected XMLInputFactory createInputFactory() {
        Exception exception;
        try {
            Class staxInputFactory = JVM.getStaxInputFactory();
            if (staxInputFactory != null) {
                return (XMLInputFactory) staxInputFactory.newInstance();
            }
            throw new StreamException("Java runtime has no standard XMLInputFactory implementation.", null);
        } catch (Exception e) {
            exception = e;
            throw new StreamException("Cannot create standard XMLInputFactory instance of Java runtime.", exception);
        } catch (Exception e2) {
            exception = e2;
            throw new StreamException("Cannot create standard XMLInputFactory instance of Java runtime.", exception);
        } catch (Exception e22) {
            exception = e22;
            throw new StreamException("Cannot create standard XMLInputFactory instance of Java runtime.", exception);
        }
    }

    protected XMLOutputFactory createOutputFactory() {
        Exception exception;
        try {
            Class staxOutputFactory = JVM.getStaxOutputFactory();
            if (staxOutputFactory != null) {
                return (XMLOutputFactory) staxOutputFactory.newInstance();
            }
            throw new StreamException("Java runtime has no standard XMLOutputFactory implementation.", null);
        } catch (Exception e) {
            exception = e;
            throw new StreamException("Cannot create standard XMLOutputFactory instance of Java runtime.", exception);
        } catch (Exception e2) {
            exception = e2;
            throw new StreamException("Cannot create standard XMLOutputFactory instance of Java runtime.", exception);
        } catch (Exception e22) {
            exception = e22;
            throw new StreamException("Cannot create standard XMLOutputFactory instance of Java runtime.", exception);
        }
    }
}
