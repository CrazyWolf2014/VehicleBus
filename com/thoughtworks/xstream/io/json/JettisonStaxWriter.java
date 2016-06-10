package com.thoughtworks.xstream.io.json;

import com.thoughtworks.xstream.io.naming.NameCoder;
import com.thoughtworks.xstream.io.xml.QNameMap;
import com.thoughtworks.xstream.io.xml.StaxWriter;
import com.thoughtworks.xstream.io.xml.XmlFriendlyReplacer;
import java.util.Collection;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.codehaus.jettison.AbstractXMLStreamWriter;
import org.codehaus.jettison.mapped.MappedNamespaceConvention;

public class JettisonStaxWriter extends StaxWriter {
    private final MappedNamespaceConvention convention;

    public JettisonStaxWriter(QNameMap qnameMap, XMLStreamWriter out, boolean writeEnclosingDocument, boolean namespaceRepairingMode, NameCoder nameCoder, MappedNamespaceConvention convention) throws XMLStreamException {
        super(qnameMap, out, writeEnclosingDocument, namespaceRepairingMode, nameCoder);
        this.convention = convention;
    }

    public JettisonStaxWriter(QNameMap qnameMap, XMLStreamWriter out, boolean writeEnclosingDocument, boolean namespaceRepairingMode, XmlFriendlyReplacer replacer, MappedNamespaceConvention convention) throws XMLStreamException {
        this(qnameMap, out, writeEnclosingDocument, namespaceRepairingMode, (NameCoder) replacer, convention);
    }

    public JettisonStaxWriter(QNameMap qnameMap, XMLStreamWriter out, boolean writeEnclosingDocument, boolean namespaceRepairingMode, MappedNamespaceConvention convention) throws XMLStreamException {
        super(qnameMap, out, writeEnclosingDocument, namespaceRepairingMode);
        this.convention = convention;
    }

    public JettisonStaxWriter(QNameMap qnameMap, XMLStreamWriter out, MappedNamespaceConvention convention) throws XMLStreamException {
        super(qnameMap, out);
        this.convention = convention;
    }

    public JettisonStaxWriter(QNameMap qnameMap, XMLStreamWriter out, NameCoder nameCoder, MappedNamespaceConvention convention) throws XMLStreamException {
        super(qnameMap, out, nameCoder);
        this.convention = convention;
    }

    public void startNode(String name, Class clazz) {
        XMLStreamWriter out = getXMLStreamWriter();
        if (clazz != null && (out instanceof AbstractXMLStreamWriter) && (Collection.class.isAssignableFrom(clazz) || Map.class.isAssignableFrom(clazz) || clazz.isArray())) {
            QName qname = getQNameMap().getQName(encodeNode(name));
            String key = this.convention.createKey(qname.getPrefix(), qname.getNamespaceURI(), qname.getLocalPart());
            if (!((AbstractXMLStreamWriter) out).getSerializedAsArrays().contains(key)) {
                ((AbstractXMLStreamWriter) out).seriliazeAsArray(key);
            }
        }
        startNode(name);
    }
}
