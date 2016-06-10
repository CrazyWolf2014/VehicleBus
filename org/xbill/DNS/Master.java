package org.xbill.DNS;

import com.tencent.mm.sdk.platformtools.FilePathGenerator;
import com.tencent.mm.sdk.platformtools.Util;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Master {
    private int currentDClass;
    private long currentTTL;
    private int currentType;
    private long defaultTTL;
    private File file;
    private Generator generator;
    private List generators;
    private Master included;
    private Record last;
    private boolean needSOATTL;
    private boolean noExpandGenerate;
    private Name origin;
    private Tokenizer st;

    Master(File file, Name name, long j) throws IOException {
        this.last = null;
        this.included = null;
        if (name == null || name.isAbsolute()) {
            this.file = file;
            this.st = new Tokenizer(file);
            this.origin = name;
            this.defaultTTL = j;
            return;
        }
        throw new RelativeNameException(name);
    }

    public Master(String str, Name name, long j) throws IOException {
        this(new File(str), name, j);
    }

    public Master(String str, Name name) throws IOException {
        this(new File(str), name, -1);
    }

    public Master(String str) throws IOException {
        this(new File(str), null, -1);
    }

    public Master(InputStream inputStream, Name name, long j) {
        this.last = null;
        this.included = null;
        if (name == null || name.isAbsolute()) {
            this.st = new Tokenizer(inputStream);
            this.origin = name;
            this.defaultTTL = j;
            return;
        }
        throw new RelativeNameException(name);
    }

    public Master(InputStream inputStream, Name name) {
        this(inputStream, name, -1);
    }

    public Master(InputStream inputStream) {
        this(inputStream, null, -1);
    }

    private Name parseName(String str, Name name) throws TextParseException {
        try {
            return Name.fromString(str, name);
        } catch (TextParseException e) {
            throw this.st.exception(e.getMessage());
        }
    }

    private void parseTTLClassAndType() throws IOException {
        String string;
        int value;
        boolean z = false;
        String string2 = this.st.getString();
        int value2 = DClass.value(string2);
        this.currentDClass = value2;
        if (value2 >= 0) {
            string2 = this.st.getString();
            z = true;
        }
        this.currentTTL = -1;
        try {
            this.currentTTL = TTL.parseTTL(string2);
            string2 = this.st.getString();
        } catch (NumberFormatException e) {
            if (this.defaultTTL >= 0) {
                this.currentTTL = this.defaultTTL;
            } else if (this.last != null) {
                this.currentTTL = this.last.getTTL();
            }
        }
        if (!z) {
            int value3 = DClass.value(string2);
            this.currentDClass = value3;
            if (value3 >= 0) {
                string = this.st.getString();
                value = Type.value(string);
                this.currentType = value;
                if (value < 0) {
                    throw this.st.exception("Invalid type '" + string + "'");
                } else if (this.currentTTL >= 0) {
                } else {
                    if (this.currentType == 6) {
                        throw this.st.exception("missing TTL");
                    }
                    this.needSOATTL = true;
                    this.currentTTL = 0;
                    return;
                }
            }
            this.currentDClass = 1;
        }
        string = string2;
        value = Type.value(string);
        this.currentType = value;
        if (value < 0) {
            throw this.st.exception("Invalid type '" + string + "'");
        } else if (this.currentTTL >= 0) {
            if (this.currentType == 6) {
                this.needSOATTL = true;
                this.currentTTL = 0;
                return;
            }
            throw this.st.exception("missing TTL");
        }
    }

    private long parseUInt32(String str) {
        if (!Character.isDigit(str.charAt(0))) {
            return -1;
        }
        try {
            long parseLong = Long.parseLong(str);
            if (parseLong < 0 || parseLong > Util.MAX_32BIT_VALUE) {
                return -1;
            }
            return parseLong;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void startGenerate() throws IOException {
        String identifier = this.st.getIdentifier();
        int indexOf = identifier.indexOf("-");
        if (indexOf < 0) {
            throw this.st.exception("Invalid $GENERATE range specifier: " + identifier);
        }
        String substring;
        long parseUInt32;
        String substring2 = identifier.substring(0, indexOf);
        String substring3 = identifier.substring(indexOf + 1);
        String str = null;
        int indexOf2 = substring3.indexOf(FilePathGenerator.ANDROID_DIR_SEP);
        if (indexOf2 >= 0) {
            str = substring3.substring(indexOf2 + 1);
            substring = substring3.substring(0, indexOf2);
        } else {
            substring = substring3;
        }
        long parseUInt322 = parseUInt32(substring2);
        long parseUInt323 = parseUInt32(substring);
        if (str != null) {
            parseUInt32 = parseUInt32(str);
        } else {
            parseUInt32 = 1;
        }
        if (parseUInt322 < 0 || parseUInt323 < 0 || parseUInt322 > parseUInt323 || parseUInt32 <= 0) {
            throw this.st.exception("Invalid $GENERATE range specifier: " + identifier);
        }
        identifier = this.st.getIdentifier();
        parseTTLClassAndType();
        if (Generator.supportedType(this.currentType)) {
            String identifier2 = this.st.getIdentifier();
            this.st.getEOL();
            this.st.unget();
            this.generator = new Generator(parseUInt322, parseUInt323, parseUInt32, identifier, this.currentType, this.currentDClass, this.currentTTL, identifier2, this.origin);
            if (this.generators == null) {
                this.generators = new ArrayList(1);
            }
            this.generators.add(this.generator);
            return;
        }
        throw this.st.exception("$GENERATE does not support " + Type.string(this.currentType) + " records");
    }

    private void endGenerate() throws IOException {
        this.st.getEOL();
        this.generator = null;
    }

    private Record nextGenerated() throws IOException {
        try {
            return this.generator.nextRecord();
        } catch (TokenizerException e) {
            throw this.st.exception("Parsing $GENERATE: " + e.getBaseMessage());
        } catch (TextParseException e2) {
            throw this.st.exception("Parsing $GENERATE: " + e2.getMessage());
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public org.xbill.DNS.Record _nextRecord() throws java.io.IOException {
        /*
        r8 = this;
        r1 = 0;
        r4 = 1;
        r7 = 0;
        r0 = r8.included;
        if (r0 == 0) goto L_0x0012;
    L_0x0007:
        r0 = r8.included;
        r0 = r0.nextRecord();
        if (r0 == 0) goto L_0x0010;
    L_0x000f:
        return r0;
    L_0x0010:
        r8.included = r1;
    L_0x0012:
        r0 = r8.generator;
        if (r0 == 0) goto L_0x001f;
    L_0x0016:
        r0 = r8.nextGenerated();
        if (r0 != 0) goto L_0x000f;
    L_0x001c:
        r8.endGenerate();
    L_0x001f:
        r0 = r8.st;
        r0 = r0.get(r4, r7);
        r2 = r0.type;
        r3 = 2;
        if (r2 != r3) goto L_0x007d;
    L_0x002a:
        r0 = r8.st;
        r0 = r0.get();
        r2 = r0.type;
        if (r2 == r4) goto L_0x001f;
    L_0x0034:
        r0 = r0.type;
        if (r0 != 0) goto L_0x003a;
    L_0x0038:
        r0 = r1;
        goto L_0x000f;
    L_0x003a:
        r0 = r8.st;
        r0.unget();
        r0 = r8.last;
        if (r0 != 0) goto L_0x004c;
    L_0x0043:
        r0 = r8.st;
        r1 = "no owner";
        r0 = r0.exception(r1);
        throw r0;
    L_0x004c:
        r0 = r8.last;
        r0 = r0.getName();
    L_0x0052:
        r8.parseTTLClassAndType();
        r1 = r8.currentType;
        r2 = r8.currentDClass;
        r3 = r8.currentTTL;
        r5 = r8.st;
        r6 = r8.origin;
        r0 = org.xbill.DNS.Record.fromString(r0, r1, r2, r3, r5, r6);
        r8.last = r0;
        r0 = r8.needSOATTL;
        if (r0 == 0) goto L_0x007a;
    L_0x0069:
        r0 = r8.last;
        r0 = (org.xbill.DNS.SOARecord) r0;
        r0 = r0.getMinimum();
        r2 = r8.last;
        r2.setTTL(r0);
        r8.defaultTTL = r0;
        r8.needSOATTL = r7;
    L_0x007a:
        r0 = r8.last;
        goto L_0x000f;
    L_0x007d:
        r2 = r0.type;
        if (r2 == r4) goto L_0x001f;
    L_0x0081:
        r2 = r0.type;
        if (r2 != 0) goto L_0x0087;
    L_0x0085:
        r0 = r1;
        goto L_0x000f;
    L_0x0087:
        r2 = r0.value;
        r2 = r2.charAt(r7);
        r3 = 36;
        if (r2 != r3) goto L_0x0150;
    L_0x0091:
        r0 = r0.value;
        r2 = "$ORIGIN";
        r2 = r0.equalsIgnoreCase(r2);
        if (r2 == 0) goto L_0x00ac;
    L_0x009b:
        r0 = r8.st;
        r2 = org.xbill.DNS.Name.root;
        r0 = r0.getName(r2);
        r8.origin = r0;
        r0 = r8.st;
        r0.getEOL();
        goto L_0x001f;
    L_0x00ac:
        r2 = "$TTL";
        r2 = r0.equalsIgnoreCase(r2);
        if (r2 == 0) goto L_0x00c3;
    L_0x00b4:
        r0 = r8.st;
        r2 = r0.getTTL();
        r8.defaultTTL = r2;
        r0 = r8.st;
        r0.getEOL();
        goto L_0x001f;
    L_0x00c3:
        r2 = "$INCLUDE";
        r2 = r0.equalsIgnoreCase(r2);
        if (r2 == 0) goto L_0x0110;
    L_0x00cb:
        r0 = r8.st;
        r1 = r0.getString();
        r0 = r8.file;
        if (r0 == 0) goto L_0x010a;
    L_0x00d5:
        r0 = r8.file;
        r2 = r0.getParent();
        r0 = new java.io.File;
        r0.<init>(r2, r1);
    L_0x00e0:
        r1 = r8.origin;
        r2 = r8.st;
        r2 = r2.get();
        r3 = r2.isString();
        if (r3 == 0) goto L_0x00fb;
    L_0x00ee:
        r1 = r2.value;
        r2 = org.xbill.DNS.Name.root;
        r1 = r8.parseName(r1, r2);
        r2 = r8.st;
        r2.getEOL();
    L_0x00fb:
        r2 = new org.xbill.DNS.Master;
        r3 = r8.defaultTTL;
        r2.<init>(r0, r1, r3);
        r8.included = r2;
        r0 = r8.nextRecord();
        goto L_0x000f;
    L_0x010a:
        r0 = new java.io.File;
        r0.<init>(r1);
        goto L_0x00e0;
    L_0x0110:
        r2 = "$GENERATE";
        r2 = r0.equalsIgnoreCase(r2);
        if (r2 == 0) goto L_0x0136;
    L_0x0118:
        r0 = r8.generator;
        if (r0 == 0) goto L_0x0124;
    L_0x011c:
        r0 = new java.lang.IllegalStateException;
        r1 = "cannot nest $GENERATE";
        r0.<init>(r1);
        throw r0;
    L_0x0124:
        r8.startGenerate();
        r0 = r8.noExpandGenerate;
        if (r0 == 0) goto L_0x0130;
    L_0x012b:
        r8.endGenerate();
        goto L_0x001f;
    L_0x0130:
        r0 = r8.nextGenerated();
        goto L_0x000f;
    L_0x0136:
        r1 = r8.st;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Invalid directive: ";
        r2 = r2.append(r3);
        r0 = r2.append(r0);
        r0 = r0.toString();
        r0 = r1.exception(r0);
        throw r0;
    L_0x0150:
        r0 = r0.value;
        r1 = r8.origin;
        r0 = r8.parseName(r0, r1);
        r1 = r8.last;
        if (r1 == 0) goto L_0x0052;
    L_0x015c:
        r1 = r8.last;
        r1 = r1.getName();
        r1 = r0.equals(r1);
        if (r1 == 0) goto L_0x0052;
    L_0x0168:
        r0 = r8.last;
        r0 = r0.getName();
        goto L_0x0052;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.xbill.DNS.Master._nextRecord():org.xbill.DNS.Record");
    }

    public Record nextRecord() throws IOException {
        Record record = null;
        try {
            record = _nextRecord();
            return record;
        } finally {
            if (record == null) {
                record = this.st;
                record.close();
            }
        }
    }

    public void expandGenerate(boolean z) {
        this.noExpandGenerate = !z;
    }

    public Iterator generators() {
        if (this.generators != null) {
            return Collections.unmodifiableList(this.generators).iterator();
        }
        return Collections.EMPTY_LIST.iterator();
    }

    protected void finalize() {
        this.st.close();
    }
}
