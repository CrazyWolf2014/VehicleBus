package org.codehaus.jackson.impl;

import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.tencent.mm.sdk.platformtools.FilePathGenerator;
import org.codehaus.jackson.JsonLocation;
import org.codehaus.jackson.JsonStreamContext;
import org.codehaus.jackson.util.CharTypes;
import org.xbill.DNS.KEYRecord;

public final class JsonReadContext extends JsonStreamContext {
    protected JsonReadContext _child;
    protected int _columnNr;
    protected String _currentName;
    protected int _lineNr;
    protected final JsonReadContext _parent;

    public JsonReadContext(JsonReadContext parent, int type, int lineNr, int colNr) {
        this._child = null;
        this._type = type;
        this._parent = parent;
        this._lineNr = lineNr;
        this._columnNr = colNr;
        this._index = -1;
    }

    protected final void reset(int type, int lineNr, int colNr) {
        this._type = type;
        this._index = -1;
        this._lineNr = lineNr;
        this._columnNr = colNr;
        this._currentName = null;
    }

    public static JsonReadContext createRootContext(int lineNr, int colNr) {
        return new JsonReadContext(null, 0, lineNr, colNr);
    }

    public final JsonReadContext createChildArrayContext(int lineNr, int colNr) {
        JsonReadContext ctxt = this._child;
        if (ctxt == null) {
            ctxt = new JsonReadContext(this, 1, lineNr, colNr);
            this._child = ctxt;
            return ctxt;
        }
        ctxt.reset(1, lineNr, colNr);
        return ctxt;
    }

    public final JsonReadContext createChildObjectContext(int lineNr, int colNr) {
        JsonReadContext ctxt = this._child;
        if (ctxt == null) {
            ctxt = new JsonReadContext(this, 2, lineNr, colNr);
            this._child = ctxt;
            return ctxt;
        }
        ctxt.reset(2, lineNr, colNr);
        return ctxt;
    }

    public final String getCurrentName() {
        return this._currentName;
    }

    public final JsonReadContext getParent() {
        return this._parent;
    }

    public final JsonLocation getStartLocation(Object srcRef) {
        return new JsonLocation(srcRef, -1, this._lineNr, this._columnNr);
    }

    public final boolean expectComma() {
        int ix = this._index + 1;
        this._index = ix;
        return this._type != 0 && ix > 0;
    }

    public void setCurrentName(String name) {
        this._currentName = name;
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder(64);
        switch (this._type) {
            case KEYRecord.OWNER_USER /*0*/:
                sb.append(FilePathGenerator.ANDROID_DIR_SEP);
                break;
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                sb.append('[');
                sb.append(getCurrentIndex());
                sb.append(']');
                break;
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                sb.append('{');
                if (this._currentName != null) {
                    sb.append('\"');
                    CharTypes.appendQuoted(sb, this._currentName);
                    sb.append('\"');
                } else {
                    sb.append('?');
                }
                sb.append(']');
                break;
        }
        return sb.toString();
    }
}
