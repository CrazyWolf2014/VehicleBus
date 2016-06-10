package com.autonavi.gelocator.api;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ParserXml {
    public MyLocaitonBean ParserLocationXml(String str) {
        InputStream byteArrayInputStream = new ByteArrayInputStream(str.getBytes());
        SAXParserFactory newInstance = SAXParserFactory.newInstance();
        DefaultHandler c0105h = new C0105h(this);
        try {
            newInstance.newSAXParser().parse(byteArrayInputStream, c0105h);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        return c0105h.f814a;
    }

    public String ParserSapsXml(String str) {
        InputStream byteArrayInputStream = new ByteArrayInputStream(str.getBytes());
        SAXParserFactory newInstance = SAXParserFactory.newInstance();
        DefaultHandler c0106i = new C0106i(this);
        try {
            newInstance.newSAXParser().parse(byteArrayInputStream, c0106i);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        return c0106i.f816a;
    }
}
