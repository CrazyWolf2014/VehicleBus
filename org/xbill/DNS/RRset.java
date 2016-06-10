package org.xbill.DNS;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.codehaus.jackson.util.MinimalPrettyPrinter;

public class RRset implements Serializable {
    private static final long serialVersionUID = -3270249290171239695L;
    private short nsigs;
    private short position;
    private List rrs;

    public RRset() {
        this.rrs = new ArrayList(1);
        this.nsigs = (short) 0;
        this.position = (short) 0;
    }

    public RRset(Record record) {
        this();
        safeAddRR(record);
    }

    public RRset(RRset rRset) {
        synchronized (rRset) {
            this.rrs = (List) ((ArrayList) rRset.rrs).clone();
            this.nsigs = rRset.nsigs;
            this.position = rRset.position;
        }
    }

    private void safeAddRR(Record record) {
        if (record instanceof RRSIGRecord) {
            this.rrs.add(record);
            this.nsigs = (short) (this.nsigs + 1);
        } else if (this.nsigs == (short) 0) {
            this.rrs.add(record);
        } else {
            this.rrs.add(this.rrs.size() - this.nsigs, record);
        }
    }

    public synchronized void addRR(Record record) {
        if (this.rrs.size() == 0) {
            safeAddRR(record);
        } else {
            Record first = first();
            if (record.sameRRset(first)) {
                if (record.getTTL() != first.getTTL()) {
                    if (record.getTTL() > first.getTTL()) {
                        record = record.cloneRecord();
                        record.setTTL(first.getTTL());
                    } else {
                        for (int i = 0; i < this.rrs.size(); i++) {
                            first = ((Record) this.rrs.get(i)).cloneRecord();
                            first.setTTL(record.getTTL());
                            this.rrs.set(i, first);
                        }
                    }
                }
                if (!this.rrs.contains(record)) {
                    safeAddRR(record);
                }
            } else {
                throw new IllegalArgumentException("record does not match rrset");
            }
        }
    }

    public synchronized void deleteRR(Record record) {
        if (this.rrs.remove(record) && (record instanceof RRSIGRecord)) {
            this.nsigs = (short) (this.nsigs - 1);
        }
    }

    public synchronized void clear() {
        this.rrs.clear();
        this.position = (short) 0;
        this.nsigs = (short) 0;
    }

    private synchronized Iterator iterator(boolean z, boolean z2) {
        Iterator it;
        int i = 0;
        synchronized (this) {
            short s;
            int size = this.rrs.size();
            if (z) {
                s = size - this.nsigs;
            } else {
                s = this.nsigs;
            }
            if (s == (short) 0) {
                it = Collections.EMPTY_LIST.iterator();
            } else {
                if (!z) {
                    i = size - this.nsigs;
                } else if (z2) {
                    if (this.position >= s) {
                        this.position = (short) 0;
                    }
                    i = this.position;
                    this.position = (short) (i + 1);
                }
                List arrayList = new ArrayList(s);
                if (z) {
                    arrayList.addAll(this.rrs.subList(i, s));
                    if (i != 0) {
                        arrayList.addAll(this.rrs.subList(0, i));
                    }
                } else {
                    arrayList.addAll(this.rrs.subList(i, size));
                }
                it = arrayList.iterator();
            }
        }
        return it;
    }

    public synchronized Iterator rrs(boolean z) {
        return iterator(true, z);
    }

    public synchronized Iterator rrs() {
        return iterator(true, true);
    }

    public synchronized Iterator sigs() {
        return iterator(false, false);
    }

    public synchronized int size() {
        return this.rrs.size() - this.nsigs;
    }

    public Name getName() {
        return first().getName();
    }

    public int getType() {
        return first().getRRsetType();
    }

    public int getDClass() {
        return first().getDClass();
    }

    public synchronized long getTTL() {
        return first().getTTL();
    }

    public synchronized Record first() {
        if (this.rrs.size() == 0) {
            throw new IllegalStateException("rrset is empty");
        }
        return (Record) this.rrs.get(0);
    }

    private String iteratorToString(Iterator it) {
        StringBuffer stringBuffer = new StringBuffer();
        while (it.hasNext()) {
            Record record = (Record) it.next();
            stringBuffer.append("[");
            stringBuffer.append(record.rdataToString());
            stringBuffer.append("]");
            if (it.hasNext()) {
                stringBuffer.append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
            }
        }
        return stringBuffer.toString();
    }

    public String toString() {
        if (this.rrs == null) {
            return "{empty}";
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("{ ");
        stringBuffer.append(getName() + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuffer.append(getTTL() + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuffer.append(DClass.string(getDClass()) + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuffer.append(Type.string(getType()) + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        stringBuffer.append(iteratorToString(iterator(true, false)));
        if (this.nsigs > (short) 0) {
            stringBuffer.append(" sigs: ");
            stringBuffer.append(iteratorToString(iterator(false, false)));
        }
        stringBuffer.append(" }");
        return stringBuffer.toString();
    }
}
