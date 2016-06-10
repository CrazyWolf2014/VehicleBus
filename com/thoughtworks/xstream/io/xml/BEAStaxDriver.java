package com.thoughtworks.xstream.io.xml;

import com.bea.xml.stream.MXParserFactory;
import com.bea.xml.stream.XMLOutputFactoryBase;
import com.thoughtworks.xstream.io.naming.NameCoder;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;

public class BEAStaxDriver extends StaxDriver {
    public BEAStaxDriver(QNameMap qnameMap, XmlFriendlyNameCoder nameCoder) {
        super(qnameMap, (NameCoder) nameCoder);
    }

    public BEAStaxDriver(QNameMap qnameMap) {
        super(qnameMap);
    }

    public BEAStaxDriver(XmlFriendlyNameCoder nameCoder) {
        super((NameCoder) nameCoder);
    }

    protected XMLInputFactory createInputFactory() {
        return new MXParserFactory();
    }

    protected XMLOutputFactory createOutputFactory() {
        return new XMLOutputFactoryBase();
    }
}
