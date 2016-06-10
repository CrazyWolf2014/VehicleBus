package com.thoughtworks.xstream.io.json;

import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.AbstractJsonWriter.Type;
import com.thoughtworks.xstream.io.naming.NameCoder;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import java.io.Writer;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.xbill.DNS.KEYRecord.Flags;

public class JsonWriter extends AbstractJsonWriter {
    private int depth;
    protected final Format format;
    private boolean newLineProposed;
    protected final QuickWriter writer;

    public static class Format {
        public static int COMPACT_EMPTY_ELEMENT;
        public static int SPACE_AFTER_LABEL;
        private char[] lineIndenter;
        private final int mode;
        private final NameCoder nameCoder;
        private char[] newLine;

        static {
            SPACE_AFTER_LABEL = 1;
            COMPACT_EMPTY_ELEMENT = 2;
        }

        public Format() {
            this(new char[]{' ', ' '}, new char[]{'\n'}, SPACE_AFTER_LABEL | COMPACT_EMPTY_ELEMENT);
        }

        public Format(char[] lineIndenter, char[] newLine, int mode) {
            this(lineIndenter, newLine, mode, new NoNameCoder());
        }

        public Format(char[] lineIndenter, char[] newLine, int mode, NameCoder nameCoder) {
            this.lineIndenter = lineIndenter;
            this.newLine = newLine;
            this.mode = mode;
            this.nameCoder = nameCoder;
        }

        public char[] getLineIndenter() {
            return this.lineIndenter;
        }

        public char[] getNewLine() {
            return this.newLine;
        }

        public int mode() {
            return this.mode;
        }

        public NameCoder getNameCoder() {
            return this.nameCoder;
        }
    }

    public JsonWriter(Writer writer, char[] lineIndenter, String newLine) {
        this(writer, 0, new Format(lineIndenter, newLine.toCharArray(), Format.SPACE_AFTER_LABEL | Format.COMPACT_EMPTY_ELEMENT));
    }

    public JsonWriter(Writer writer, char[] lineIndenter) {
        this(writer, 0, new Format(lineIndenter, new char[]{'\n'}, Format.SPACE_AFTER_LABEL | Format.COMPACT_EMPTY_ELEMENT));
    }

    public JsonWriter(Writer writer, String lineIndenter, String newLine) {
        this(writer, 0, new Format(lineIndenter.toCharArray(), newLine.toCharArray(), Format.SPACE_AFTER_LABEL | Format.COMPACT_EMPTY_ELEMENT));
    }

    public JsonWriter(Writer writer, String lineIndenter) {
        this(writer, 0, new Format(lineIndenter.toCharArray(), new char[]{'\n'}, Format.SPACE_AFTER_LABEL | Format.COMPACT_EMPTY_ELEMENT));
    }

    public JsonWriter(Writer writer) {
        this(writer, 0, new Format(new char[]{' ', ' '}, new char[]{'\n'}, Format.SPACE_AFTER_LABEL | Format.COMPACT_EMPTY_ELEMENT));
    }

    public JsonWriter(Writer writer, char[] lineIndenter, String newLine, int mode) {
        this(writer, mode, new Format(lineIndenter, newLine.toCharArray(), Format.SPACE_AFTER_LABEL | Format.COMPACT_EMPTY_ELEMENT));
    }

    public JsonWriter(Writer writer, int mode) {
        this(writer, mode, new Format());
    }

    public JsonWriter(Writer writer, Format format) {
        this(writer, 0, format);
    }

    public JsonWriter(Writer writer, int mode, Format format) {
        this(writer, mode, format, (int) Flags.FLAG5);
    }

    public JsonWriter(Writer writer, int mode, Format format, int bufferSize) {
        super(mode, format.getNameCoder());
        this.writer = new QuickWriter(writer, bufferSize);
        this.format = format;
        this.depth = (mode & 1) == 0 ? -1 : 0;
    }

    public void flush() {
        this.writer.flush();
    }

    public void close() {
        this.writer.close();
    }

    public HierarchicalStreamWriter underlyingWriter() {
        return this;
    }

    protected void startObject() {
        if (this.newLineProposed) {
            writeNewLine();
        }
        this.writer.write('{');
        startNewLine();
    }

    protected void addLabel(String name) {
        if (this.newLineProposed) {
            writeNewLine();
        }
        this.writer.write('\"');
        writeText(name);
        this.writer.write("\":");
        if ((this.format.mode() & Format.SPACE_AFTER_LABEL) != 0) {
            this.writer.write(' ');
        }
    }

    protected void addValue(String value, Type type) {
        if (this.newLineProposed) {
            writeNewLine();
        }
        if (type == Type.STRING) {
            this.writer.write('\"');
        }
        writeText(value);
        if (type == Type.STRING) {
            this.writer.write('\"');
        }
    }

    protected void startArray() {
        if (this.newLineProposed) {
            writeNewLine();
        }
        this.writer.write("[");
        startNewLine();
    }

    protected void nextElement() {
        this.writer.write(",");
        writeNewLine();
    }

    protected void endArray() {
        endNewLine();
        this.writer.write("]");
    }

    protected void endObject() {
        endNewLine();
        this.writer.write("}");
    }

    private void startNewLine() {
        int i = this.depth + 1;
        this.depth = i;
        if (i > 0) {
            this.newLineProposed = true;
        }
    }

    private void endNewLine() {
        int i = this.depth;
        this.depth = i - 1;
        if (i <= 0) {
            return;
        }
        if ((this.format.mode() & Format.COMPACT_EMPTY_ELEMENT) == 0 || !this.newLineProposed) {
            writeNewLine();
        } else {
            this.newLineProposed = false;
        }
    }

    private void writeNewLine() {
        int depth = this.depth;
        this.writer.write(this.format.getNewLine());
        int depth2 = depth;
        while (true) {
            depth = depth2 - 1;
            if (depth2 > 0) {
                this.writer.write(this.format.getLineIndenter());
                depth2 = depth;
            } else {
                this.newLineProposed = false;
                return;
            }
        }
    }

    private void writeText(String text) {
        int length = text.length();
        for (int i = 0; i < length; i++) {
            char c = text.charAt(i);
            switch (c) {
                case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                    this.writer.write("\\b");
                    break;
                case FileOptions.OPTIMIZE_FOR_FIELD_NUMBER /*9*/:
                    this.writer.write("\\t");
                    break;
                case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
                    this.writer.write("\\n");
                    break;
                case TrafficIncident.VERTEXOFFSET_FIELD_NUMBER /*12*/:
                    this.writer.write("\\f");
                    break;
                case TrafficIncident.INCIDENTVERTEX_FIELD_NUMBER /*13*/:
                    this.writer.write("\\r");
                    break;
                case org.xbill.DNS.Type.ATMA /*34*/:
                    this.writer.write("\\\"");
                    break;
                case Opcodes.DUP2 /*92*/:
                    this.writer.write("\\\\");
                    break;
                default:
                    if (c <= '\u001f') {
                        this.writer.write("\\u");
                        String hex = "000" + Integer.toHexString(c);
                        this.writer.write(hex.substring(hex.length() - 4));
                        break;
                    }
                    this.writer.write(c);
                    break;
            }
        }
    }
}
