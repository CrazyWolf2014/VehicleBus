package com.kenai.jbosh;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

final class BodyParserSAX implements BodyParser {
    private static final Logger LOG;
    private static final ThreadLocal<SoftReference<SAXParser>> PARSER;
    private static final SAXParserFactory SAX_FACTORY;

    /* renamed from: com.kenai.jbosh.BodyParserSAX.1 */
    static class C07801 extends ThreadLocal<SoftReference<SAXParser>> {
        C07801() {
        }

        protected SoftReference<SAXParser> initialValue() {
            return new SoftReference(null);
        }
    }

    private static final class Handler extends DefaultHandler {
        private String defaultNS;
        private final SAXParser parser;
        private final BodyParserResults result;

        private Handler(SAXParser sAXParser, BodyParserResults bodyParserResults) {
            this.defaultNS = null;
            this.parser = sAXParser;
            this.result = bodyParserResults;
        }

        public void startElement(String str, String str2, String str3, Attributes attributes) {
            if (BodyParserSAX.LOG.isLoggable(Level.FINEST)) {
                BodyParserSAX.LOG.finest("Start element: " + str3);
                BodyParserSAX.LOG.finest("    URI: " + str);
                BodyParserSAX.LOG.finest("    local: " + str2);
            }
            BodyQName bodyQName = AbstractBody.getBodyQName();
            if (bodyQName.getNamespaceURI().equals(str) && bodyQName.getLocalPart().equals(str2)) {
                for (int i = 0; i < attributes.getLength(); i++) {
                    String uri = attributes.getURI(i);
                    if (uri.length() == 0) {
                        uri = this.defaultNS;
                    }
                    String localName = attributes.getLocalName(i);
                    String value = attributes.getValue(i);
                    if (BodyParserSAX.LOG.isLoggable(Level.FINEST)) {
                        BodyParserSAX.LOG.finest("    Attribute: {" + uri + "}" + localName + " = '" + value + "'");
                    }
                    this.result.addBodyAttributeValue(BodyQName.create(uri, localName), value);
                }
                this.parser.reset();
                return;
            }
            throw new IllegalStateException("Root element was not '" + bodyQName.getLocalPart() + "' in the '" + bodyQName.getNamespaceURI() + "' namespace.  (Was '" + str2 + "' in '" + str + "')");
        }

        public void startPrefixMapping(String str, String str2) {
            if (str.length() == 0) {
                if (BodyParserSAX.LOG.isLoggable(Level.FINEST)) {
                    BodyParserSAX.LOG.finest("Prefix mapping: <DEFAULT> => " + str2);
                }
                this.defaultNS = str2;
            } else if (BodyParserSAX.LOG.isLoggable(Level.FINEST)) {
                BodyParserSAX.LOG.info("Prefix mapping: " + str + " => " + str2);
            }
        }
    }

    BodyParserSAX() {
    }

    static {
        LOG = Logger.getLogger(BodyParserSAX.class.getName());
        SAX_FACTORY = SAXParserFactory.newInstance();
        SAX_FACTORY.setNamespaceAware(true);
        SAX_FACTORY.setValidating(false);
        PARSER = new C07801();
    }

    public BodyParserResults parse(String str) throws BOSHException {
        Throwable e;
        BodyParserResults bodyParserResults = new BodyParserResults();
        try {
            InputStream byteArrayInputStream = new ByteArrayInputStream(str.getBytes());
            SAXParser sAXParser = getSAXParser();
            sAXParser.parse(byteArrayInputStream, new Handler(bodyParserResults, null));
            return bodyParserResults;
        } catch (SAXException e2) {
            e = e2;
            throw new BOSHException("Could not parse body:\n" + str, e);
        } catch (IOException e3) {
            e = e3;
            throw new BOSHException("Could not parse body:\n" + str, e);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static javax.xml.parsers.SAXParser getSAXParser() {
        /*
        r0 = PARSER;
        r0 = r0.get();
        r0 = (java.lang.ref.SoftReference) r0;
        r0 = r0.get();
        r0 = (javax.xml.parsers.SAXParser) r0;
        if (r0 != 0) goto L_0x002a;
    L_0x0010:
        r0 = SAX_FACTORY;	 Catch:{ ParserConfigurationException -> 0x002e, SAXException -> 0x0021 }
        r0 = r0.newSAXParser();	 Catch:{ ParserConfigurationException -> 0x002e, SAXException -> 0x0021 }
        r1 = new java.lang.ref.SoftReference;	 Catch:{ ParserConfigurationException -> 0x002e, SAXException -> 0x0021 }
        r1.<init>(r0);	 Catch:{ ParserConfigurationException -> 0x002e, SAXException -> 0x0021 }
        r2 = PARSER;	 Catch:{ ParserConfigurationException -> 0x002e, SAXException -> 0x0021 }
        r2.set(r1);	 Catch:{ ParserConfigurationException -> 0x002e, SAXException -> 0x0021 }
    L_0x0020:
        return r0;
    L_0x0021:
        r0 = move-exception;
    L_0x0022:
        r1 = new java.lang.IllegalStateException;
        r2 = "Could not create SAX parser";
        r1.<init>(r2, r0);
        throw r1;
    L_0x002a:
        r0.reset();
        goto L_0x0020;
    L_0x002e:
        r0 = move-exception;
        goto L_0x0022;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.kenai.jbosh.BodyParserSAX.getSAXParser():javax.xml.parsers.SAXParser");
    }
}
