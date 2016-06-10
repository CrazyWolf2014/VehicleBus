package org.xbill.DNS;

import java.io.IOException;
import java.util.Iterator;

public class Update extends Message {
    private int dclass;
    private Name origin;

    public Update(Name name, int i) {
        if (name.isAbsolute()) {
            DClass.check(i);
            getHeader().setOpcode(5);
            addRecord(Record.newRecord(name, 6, 1), 0);
            this.origin = name;
            this.dclass = i;
            return;
        }
        throw new RelativeNameException(name);
    }

    public Update(Name name) {
        this(name, 1);
    }

    private void newPrereq(Record record) {
        addRecord(record, 1);
    }

    private void newUpdate(Record record) {
        addRecord(record, 2);
    }

    public void present(Name name) {
        newPrereq(Record.newRecord(name, KEYRecord.PROTOCOL_ANY, KEYRecord.PROTOCOL_ANY, 0));
    }

    public void present(Name name, int i) {
        newPrereq(Record.newRecord(name, i, KEYRecord.PROTOCOL_ANY, 0));
    }

    public void present(Name name, int i, String str) throws IOException {
        newPrereq(Record.fromString(name, i, this.dclass, 0, str, this.origin));
    }

    public void present(Name name, int i, Tokenizer tokenizer) throws IOException {
        newPrereq(Record.fromString(name, i, this.dclass, 0, tokenizer, this.origin));
    }

    public void present(Record record) {
        newPrereq(record);
    }

    public void absent(Name name) {
        newPrereq(Record.newRecord(name, KEYRecord.PROTOCOL_ANY, CERTRecord.OID, 0));
    }

    public void absent(Name name, int i) {
        newPrereq(Record.newRecord(name, i, CERTRecord.OID, 0));
    }

    public void add(Name name, int i, long j, String str) throws IOException {
        newUpdate(Record.fromString(name, i, this.dclass, j, str, this.origin));
    }

    public void add(Name name, int i, long j, Tokenizer tokenizer) throws IOException {
        newUpdate(Record.fromString(name, i, this.dclass, j, tokenizer, this.origin));
    }

    public void add(Record record) {
        newUpdate(record);
    }

    public void add(Record[] recordArr) {
        for (Record add : recordArr) {
            add(add);
        }
    }

    public void add(RRset rRset) {
        Iterator rrs = rRset.rrs();
        while (rrs.hasNext()) {
            add((Record) rrs.next());
        }
    }

    public void delete(Name name) {
        newUpdate(Record.newRecord(name, KEYRecord.PROTOCOL_ANY, KEYRecord.PROTOCOL_ANY, 0));
    }

    public void delete(Name name, int i) {
        newUpdate(Record.newRecord(name, i, KEYRecord.PROTOCOL_ANY, 0));
    }

    public void delete(Name name, int i, String str) throws IOException {
        newUpdate(Record.fromString(name, i, (int) CERTRecord.OID, 0, str, this.origin));
    }

    public void delete(Name name, int i, Tokenizer tokenizer) throws IOException {
        newUpdate(Record.fromString(name, i, (int) CERTRecord.OID, 0, tokenizer, this.origin));
    }

    public void delete(Record record) {
        newUpdate(record.withDClass(CERTRecord.OID, 0));
    }

    public void delete(Record[] recordArr) {
        for (Record delete : recordArr) {
            delete(delete);
        }
    }

    public void delete(RRset rRset) {
        Iterator rrs = rRset.rrs();
        while (rrs.hasNext()) {
            delete((Record) rrs.next());
        }
    }

    public void replace(Name name, int i, long j, String str) throws IOException {
        delete(name, i);
        add(name, i, j, str);
    }

    public void replace(Name name, int i, long j, Tokenizer tokenizer) throws IOException {
        delete(name, i);
        add(name, i, j, tokenizer);
    }

    public void replace(Record record) {
        delete(record.getName(), record.getType());
        add(record);
    }

    public void replace(Record[] recordArr) {
        for (Record replace : recordArr) {
            replace(replace);
        }
    }

    public void replace(RRset rRset) {
        delete(rRset.getName(), rRset.getType());
        Iterator rrs = rRset.rrs();
        while (rrs.hasNext()) {
            add((Record) rrs.next());
        }
    }
}
