package com.thoughtworks.xstream.io.xml;

import com.ctc.wstx.stax.WstxInputFactory;
import com.ctc.wstx.stax.WstxOutputFactory;
import com.thoughtworks.xstream.io.naming.NameCoder;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;

public class WstxDriver extends StaxDriver {
    public WstxDriver(QNameMap qnameMap, XmlFriendlyNameCoder nameCoder) {
        super(qnameMap, (NameCoder) nameCoder);
    }

    public WstxDriver(QNameMap qnameMap) {
        super(qnameMap);
    }

    public WstxDriver(XmlFriendlyNameCoder nameCoder) {
        super((NameCoder) nameCoder);
    }

    protected XMLInputFactory createInputFactory() {
        return new WstxInputFactory();
    }

    protected XMLOutputFactory createOutputFactory() {
        return new WstxOutputFactory();
    }
}
