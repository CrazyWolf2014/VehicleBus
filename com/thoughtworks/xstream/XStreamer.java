package com.thoughtworks.xstream;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.xml.XppDriver;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

public class XStreamer {
    public String toXML(XStream xstream, Object obj) throws ObjectStreamException {
        Writer writer = new StringWriter();
        try {
            toXML(xstream, obj, writer);
            return writer.toString();
        } catch (ObjectStreamException e) {
            throw e;
        } catch (IOException e2) {
            throw new ConversionException("Unexpeced IO error from a StringWriter", e2);
        }
    }

    public void toXML(XStream xstream, Object obj, Writer out) throws IOException {
        ObjectOutputStream oos = new XStream().createObjectOutputStream(out);
        try {
            oos.writeObject(xstream);
            oos.flush();
            xstream.toXML(obj, out);
        } finally {
            oos.close();
        }
    }

    public Object fromXML(String xml) throws ClassNotFoundException, ObjectStreamException {
        try {
            return fromXML(new StringReader(xml));
        } catch (ObjectStreamException e) {
            throw e;
        } catch (IOException e2) {
            throw new ConversionException("Unexpeced IO error from a StringReader", e2);
        }
    }

    public Object fromXML(HierarchicalStreamDriver driver, String xml) throws ClassNotFoundException, ObjectStreamException {
        try {
            return fromXML(driver, new StringReader(xml));
        } catch (ObjectStreamException e) {
            throw e;
        } catch (IOException e2) {
            throw new ConversionException("Unexpeced IO error from a StringReader", e2);
        }
    }

    public Object fromXML(Reader xml) throws IOException, ClassNotFoundException {
        return fromXML(new XppDriver(), xml);
    }

    public Object fromXML(HierarchicalStreamDriver driver, Reader xml) throws IOException, ClassNotFoundException {
        ObjectInputStream in;
        XStream outer = new XStream(driver);
        HierarchicalStreamReader reader = driver.createReader(xml);
        ObjectInputStream configIn = outer.createObjectInputStream(reader);
        Object readObject;
        try {
            in = ((XStream) configIn.readObject()).createObjectInputStream(reader);
            readObject = in.readObject();
            configIn.close();
            return readObject;
        } catch (Throwable th) {
            readObject = th;
        } finally {
            in.close();
        }
    }
}
