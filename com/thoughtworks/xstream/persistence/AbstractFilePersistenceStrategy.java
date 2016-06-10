package com.thoughtworks.xstream.persistence;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.io.StreamException;
import com.thoughtworks.xstream.mapper.Mapper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Iterator;
import java.util.Map.Entry;

public abstract class AbstractFilePersistenceStrategy implements PersistenceStrategy {
    private final File baseDirectory;
    private final String encoding;
    private final FilenameFilter filter;
    private final transient XStream xstream;

    protected class ValidFilenameFilter implements FilenameFilter {
        protected ValidFilenameFilter() {
        }

        public boolean accept(File dir, String name) {
            return new File(dir, name).isFile() && AbstractFilePersistenceStrategy.this.isValid(dir, name);
        }
    }

    protected class XmlMapEntriesIterator implements Iterator {
        private File current;
        private final File[] files;
        private int position;

        /* renamed from: com.thoughtworks.xstream.persistence.AbstractFilePersistenceStrategy.XmlMapEntriesIterator.1 */
        class C09061 implements Entry {
            private final File file;
            private final Object key;

            C09061() {
                this.file = XmlMapEntriesIterator.this.current = XmlMapEntriesIterator.this.files[XmlMapEntriesIterator.access$404(XmlMapEntriesIterator.this)];
                this.key = AbstractFilePersistenceStrategy.this.extractKey(this.file.getName());
            }

            public Object getKey() {
                return this.key;
            }

            public Object getValue() {
                return AbstractFilePersistenceStrategy.this.readFile(this.file);
            }

            public Object setValue(Object value) {
                return AbstractFilePersistenceStrategy.this.put(this.key, value);
            }

            public boolean equals(Object obj) {
                if (!(obj instanceof Entry)) {
                    return false;
                }
                Object value = getValue();
                Entry e2 = (Entry) obj;
                Object key2 = e2.getKey();
                Object value2 = e2.getValue();
                if (this.key == null) {
                    if (key2 != null) {
                        return false;
                    }
                } else if (!this.key.equals(key2)) {
                    return false;
                }
                if (value == null) {
                    if (value2 != null) {
                        return false;
                    }
                } else if (!getValue().equals(e2.getValue())) {
                    return false;
                }
                return true;
            }
        }

        protected XmlMapEntriesIterator() {
            this.files = AbstractFilePersistenceStrategy.this.baseDirectory.listFiles(AbstractFilePersistenceStrategy.this.filter);
            this.position = -1;
            this.current = null;
        }

        static /* synthetic */ int access$404(XmlMapEntriesIterator x0) {
            int i = x0.position + 1;
            x0.position = i;
            return i;
        }

        public boolean hasNext() {
            return this.position + 1 < this.files.length;
        }

        public void remove() {
            if (this.current == null) {
                throw new IllegalStateException();
            }
            this.current.delete();
        }

        public Object next() {
            return new C09061();
        }
    }

    protected abstract Object extractKey(String str);

    protected abstract String getName(Object obj);

    public AbstractFilePersistenceStrategy(File baseDirectory, XStream xstream, String encoding) {
        this.baseDirectory = baseDirectory;
        this.xstream = xstream;
        this.encoding = encoding;
        this.filter = new ValidFilenameFilter();
    }

    protected ConverterLookup getConverterLookup() {
        return this.xstream.getConverterLookup();
    }

    protected Mapper getMapper() {
        return this.xstream.getMapper();
    }

    protected boolean isValid(File dir, String name) {
        return name.endsWith(".xml");
    }

    private void writeFile(File file, Object value) {
        Writer writer;
        try {
            FileOutputStream out = new FileOutputStream(file);
            writer = this.encoding != null ? new OutputStreamWriter(out, this.encoding) : new OutputStreamWriter(out);
            this.xstream.toXML(value, writer);
            writer.close();
        } catch (Throwable e) {
            throw new StreamException(e);
        } catch (Throwable th) {
            writer.close();
        }
    }

    private File getFile(String filename) {
        return new File(this.baseDirectory, filename);
    }

    private Object readFile(File file) {
        Reader reader;
        try {
            FileInputStream in = new FileInputStream(file);
            reader = this.encoding != null ? new InputStreamReader(in, this.encoding) : new InputStreamReader(in);
            Object fromXML = this.xstream.fromXML(reader);
            reader.close();
            return fromXML;
        } catch (FileNotFoundException e) {
            return null;
        } catch (Throwable e2) {
            throw new StreamException(e2);
        } catch (Throwable th) {
            reader.close();
        }
    }

    public Object put(Object key, Object value) {
        Object oldValue = get(key);
        writeFile(new File(this.baseDirectory, getName(key)), value);
        return oldValue;
    }

    public Iterator iterator() {
        return new XmlMapEntriesIterator();
    }

    public int size() {
        return this.baseDirectory.list(this.filter).length;
    }

    public boolean containsKey(Object key) {
        return getFile(getName(key)).isFile();
    }

    public Object get(Object key) {
        return readFile(getFile(getName(key)));
    }

    public Object remove(Object key) {
        File file = getFile(getName(key));
        if (!file.isFile()) {
            return null;
        }
        Object value = readFile(file);
        file.delete();
        return value;
    }
}
