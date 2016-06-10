package com.thoughtworks.xstream.io.json;

import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import java.io.Writer;

public class JsonHierarchicalStreamWriter extends JsonWriter {
    public JsonHierarchicalStreamWriter(Writer writer, char[] lineIndenter, String newLine) {
        super(writer, lineIndenter, newLine);
    }

    public JsonHierarchicalStreamWriter(Writer writer, char[] lineIndenter) {
        this(writer, lineIndenter, SpecilApiUtil.LINE_SEP);
    }

    public JsonHierarchicalStreamWriter(Writer writer, String lineIndenter, String newLine) {
        this(writer, lineIndenter.toCharArray(), newLine);
    }

    public JsonHierarchicalStreamWriter(Writer writer, String lineIndenter) {
        this(writer, lineIndenter.toCharArray());
    }

    public JsonHierarchicalStreamWriter(Writer writer) {
        this(writer, new char[]{' ', ' '});
    }
}
