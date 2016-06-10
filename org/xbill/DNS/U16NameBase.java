package org.xbill.DNS;

import java.io.IOException;
import org.codehaus.jackson.util.MinimalPrettyPrinter;

abstract class U16NameBase extends Record {
    private static final long serialVersionUID = -8315884183112502995L;
    protected Name nameField;
    protected int u16Field;

    protected U16NameBase() {
    }

    protected U16NameBase(Name name, int i, int i2, long j) {
        super(name, i, i2, j);
    }

    protected U16NameBase(Name name, int i, int i2, long j, int i3, String str, Name name2, String str2) {
        super(name, i, i2, j);
        this.u16Field = Record.checkU16(str, i3);
        this.nameField = Record.checkName(str2, name2);
    }

    void rrFromWire(DNSInput dNSInput) throws IOException {
        this.u16Field = dNSInput.readU16();
        this.nameField = new Name(dNSInput);
    }

    void rdataFromString(Tokenizer tokenizer, Name name) throws IOException {
        this.u16Field = tokenizer.getUInt16();
        this.nameField = tokenizer.getName(name);
    }

    String rrToString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.u16Field);
        stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuffer.append(this.nameField);
        return stringBuffer.toString();
    }

    protected int getU16Field() {
        return this.u16Field;
    }

    protected Name getNameField() {
        return this.nameField;
    }

    void rrToWire(DNSOutput dNSOutput, Compression compression, boolean z) {
        dNSOutput.writeU16(this.u16Field);
        this.nameField.toWire(dNSOutput, null, z);
    }
}
