package com.thoughtworks.xstream.io.json;

import com.thoughtworks.xstream.io.AbstractDriver;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.io.xml.QNameMap;
import com.thoughtworks.xstream.io.xml.StaxReader;
import com.thoughtworks.xstream.io.xml.StaxWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import javax.xml.stream.XMLStreamException;
import org.codehaus.jettison.mapped.Configuration;
import org.codehaus.jettison.mapped.MappedNamespaceConvention;
import org.codehaus.jettison.mapped.MappedXMLInputFactory;
import org.codehaus.jettison.mapped.MappedXMLOutputFactory;

public class JettisonMappedXmlDriver extends AbstractDriver {
    protected final MappedNamespaceConvention convention;
    protected final MappedXMLInputFactory mif;
    protected final MappedXMLOutputFactory mof;
    protected final boolean useSerializeAsArray;

    public JettisonMappedXmlDriver() {
        this(new Configuration());
    }

    public JettisonMappedXmlDriver(Configuration config) {
        this(config, true);
    }

    public JettisonMappedXmlDriver(Configuration config, boolean useSerializeAsArray) {
        this.mof = new MappedXMLOutputFactory(config);
        this.mif = new MappedXMLInputFactory(config);
        this.convention = new MappedNamespaceConvention(config);
        this.useSerializeAsArray = useSerializeAsArray;
    }

    public HierarchicalStreamReader createReader(Reader reader) {
        try {
            return new StaxReader(new QNameMap(), this.mif.createXMLStreamReader(reader), getNameCoder());
        } catch (Throwable e) {
            throw new StreamException(e);
        }
    }

    public HierarchicalStreamReader createReader(InputStream input) {
        try {
            return new StaxReader(new QNameMap(), this.mif.createXMLStreamReader(input), getNameCoder());
        } catch (Throwable e) {
            throw new StreamException(e);
        }
    }

    public HierarchicalStreamReader createReader(URL in) {
        InputStream inputStream = null;
        try {
            inputStream = in.openStream();
            HierarchicalStreamReader staxReader = new StaxReader(new QNameMap(), this.mif.createXMLStreamReader(in.toExternalForm(), inputStream), getNameCoder());
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
            return staxReader;
        } catch (Throwable e2) {
            throw new StreamException(e2);
        } catch (Throwable e22) {
            throw new StreamException(e22);
        } catch (Throwable th) {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e3) {
                }
            }
        }
    }

    public HierarchicalStreamReader createReader(File in) {
        Throwable e;
        Throwable th;
        InputStream inputStream = null;
        try {
            InputStream instream = new FileInputStream(in);
            try {
                HierarchicalStreamReader staxReader = new StaxReader(new QNameMap(), this.mif.createXMLStreamReader(in.toURI().toASCIIString(), instream), getNameCoder());
                if (instream != null) {
                    try {
                        instream.close();
                    } catch (IOException e2) {
                    }
                }
                return staxReader;
            } catch (XMLStreamException e3) {
                e = e3;
                inputStream = instream;
                try {
                    throw new StreamException(e);
                } catch (Throwable th2) {
                    th = th2;
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e4) {
                        }
                    }
                    throw th;
                }
            } catch (IOException e5) {
                e = e5;
                inputStream = instream;
                throw new StreamException(e);
            } catch (Throwable th3) {
                th = th3;
                inputStream = instream;
                if (inputStream != null) {
                    inputStream.close();
                }
                throw th;
            }
        } catch (XMLStreamException e6) {
            e = e6;
            throw new StreamException(e);
        } catch (IOException e7) {
            e = e7;
            throw new StreamException(e);
        }
    }

    public HierarchicalStreamWriter createWriter(Writer writer) {
        try {
            if (this.useSerializeAsArray) {
                return new JettisonStaxWriter(new QNameMap(), this.mof.createXMLStreamWriter(writer), getNameCoder(), this.convention);
            }
            return new StaxWriter(new QNameMap(), this.mof.createXMLStreamWriter(writer), getNameCoder());
        } catch (Throwable e) {
            throw new StreamException(e);
        }
    }

    public HierarchicalStreamWriter createWriter(OutputStream output) {
        try {
            if (this.useSerializeAsArray) {
                return new JettisonStaxWriter(new QNameMap(), this.mof.createXMLStreamWriter(output), getNameCoder(), this.convention);
            }
            return new StaxWriter(new QNameMap(), this.mof.createXMLStreamWriter(output), getNameCoder());
        } catch (Throwable e) {
            throw new StreamException(e);
        }
    }
}
