package org.xbill.DNS;

import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.TreeMap;

public class Zone implements Serializable {
    public static final int PRIMARY = 1;
    public static final int SECONDARY = 2;
    private static final long serialVersionUID = -9220510891189510942L;
    private RRset NS;
    private SOARecord SOA;
    private Map data;
    private int dclass;
    private boolean hasWild;
    private Name origin;
    private Object originNode;

    class ZoneIterator implements Iterator {
        private int count;
        private RRset[] current;
        private boolean wantLastSOA;
        private Iterator zentries;

        ZoneIterator(boolean z) {
            synchronized (Zone.this) {
                this.zentries = Zone.this.data.entrySet().iterator();
            }
            this.wantLastSOA = z;
            RRset[] access$200 = Zone.this.allRRsets(Zone.this.originNode);
            this.current = new RRset[access$200.length];
            int i = Zone.SECONDARY;
            for (int i2 = 0; i2 < access$200.length; i2 += Zone.PRIMARY) {
                int type = access$200[i2].getType();
                if (type == 6) {
                    this.current[0] = access$200[i2];
                } else if (type == Zone.SECONDARY) {
                    this.current[Zone.PRIMARY] = access$200[i2];
                } else {
                    type = i + Zone.PRIMARY;
                    this.current[i] = access$200[i2];
                    i = type;
                }
            }
        }

        public boolean hasNext() {
            return this.current != null || this.wantLastSOA;
        }

        public Object next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            } else if (this.current == null) {
                this.wantLastSOA = false;
                return Zone.this.oneRRset(Zone.this.originNode, 6);
            } else {
                RRset[] rRsetArr = this.current;
                int i = this.count;
                this.count = i + Zone.PRIMARY;
                RRset rRset = rRsetArr[i];
                if (this.count == this.current.length) {
                    this.current = null;
                    while (this.zentries.hasNext()) {
                        Entry entry = (Entry) this.zentries.next();
                        if (!entry.getKey().equals(Zone.this.origin)) {
                            rRsetArr = Zone.this.allRRsets(entry.getValue());
                            if (rRsetArr.length != 0) {
                                this.current = rRsetArr;
                                this.count = 0;
                                break;
                            }
                        }
                    }
                }
                return rRset;
            }
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private void validate() throws IOException {
        this.originNode = exactName(this.origin);
        if (this.originNode == null) {
            throw new IOException(this.origin + ": no data specified");
        }
        RRset oneRRset = oneRRset(this.originNode, 6);
        if (oneRRset == null || oneRRset.size() != PRIMARY) {
            throw new IOException(this.origin + ": exactly 1 SOA must be specified");
        }
        this.SOA = (SOARecord) oneRRset.rrs().next();
        this.NS = oneRRset(this.originNode, SECONDARY);
        if (this.NS == null) {
            throw new IOException(this.origin + ": no NS set specified");
        }
    }

    private final void maybeAddRecord(Record record) throws IOException {
        int type = record.getType();
        Name name = record.getName();
        if (type == 6 && !name.equals(this.origin)) {
            throw new IOException("SOA owner " + name + " does not match zone origin " + this.origin);
        } else if (name.subdomain(this.origin)) {
            addRecord(record);
        }
    }

    public Zone(Name name, String str) throws IOException {
        this.dclass = PRIMARY;
        this.data = new TreeMap();
        if (name == null) {
            throw new IllegalArgumentException("no zone name specified");
        }
        Master master = new Master(str, name);
        this.origin = name;
        while (true) {
            Record nextRecord = master.nextRecord();
            if (nextRecord != null) {
                maybeAddRecord(nextRecord);
            } else {
                validate();
                return;
            }
        }
    }

    public Zone(Name name, Record[] recordArr) throws IOException {
        this.dclass = PRIMARY;
        this.data = new TreeMap();
        if (name == null) {
            throw new IllegalArgumentException("no zone name specified");
        }
        this.origin = name;
        for (int i = 0; i < recordArr.length; i += PRIMARY) {
            maybeAddRecord(recordArr[i]);
        }
        validate();
    }

    private void fromXFR(ZoneTransferIn zoneTransferIn) throws IOException, ZoneTransferException {
        this.data = new TreeMap();
        this.origin = zoneTransferIn.getName();
        for (Record maybeAddRecord : zoneTransferIn.run()) {
            maybeAddRecord(maybeAddRecord);
        }
        if (zoneTransferIn.isAXFR()) {
            validate();
            return;
        }
        throw new IllegalArgumentException("zones can only be created from AXFRs");
    }

    public Zone(ZoneTransferIn zoneTransferIn) throws IOException, ZoneTransferException {
        this.dclass = PRIMARY;
        fromXFR(zoneTransferIn);
    }

    public Zone(Name name, int i, String str) throws IOException, ZoneTransferException {
        this.dclass = PRIMARY;
        ZoneTransferIn newAXFR = ZoneTransferIn.newAXFR(name, str, null);
        newAXFR.setDClass(i);
        fromXFR(newAXFR);
    }

    public Name getOrigin() {
        return this.origin;
    }

    public RRset getNS() {
        return this.NS;
    }

    public SOARecord getSOA() {
        return this.SOA;
    }

    public int getDClass() {
        return this.dclass;
    }

    private synchronized Object exactName(Name name) {
        return this.data.get(name);
    }

    private synchronized RRset[] allRRsets(Object obj) {
        RRset[] rRsetArr;
        if (obj instanceof List) {
            List list = (List) obj;
            rRsetArr = (RRset[]) list.toArray(new RRset[list.size()]);
        } else {
            rRsetArr = new RRset[PRIMARY];
            rRsetArr[0] = (RRset) obj;
        }
        return rRsetArr;
    }

    private synchronized RRset oneRRset(Object obj, int i) {
        RRset rRset;
        if (i == KEYRecord.PROTOCOL_ANY) {
            throw new IllegalArgumentException("oneRRset(ANY)");
        } else if (obj instanceof List) {
            List list = (List) obj;
            for (int i2 = 0; i2 < list.size(); i2 += PRIMARY) {
                rRset = (RRset) list.get(i2);
                if (rRset.getType() == i) {
                    break;
                }
            }
            rRset = null;
        } else {
            RRset rRset2 = (RRset) obj;
            if (rRset2.getType() == i) {
                rRset = rRset2;
            }
            rRset = null;
        }
        return rRset;
    }

    private synchronized RRset findRRset(Name name, int i) {
        RRset rRset;
        Object exactName = exactName(name);
        if (exactName == null) {
            rRset = null;
        } else {
            rRset = oneRRset(exactName, i);
        }
        return rRset;
    }

    private synchronized void addRRset(Name name, RRset rRset) {
        if (!this.hasWild && name.isWild()) {
            this.hasWild = true;
        }
        Object obj = this.data.get(name);
        if (obj == null) {
            this.data.put(name, rRset);
        } else {
            int type = rRset.getType();
            if (obj instanceof List) {
                List list = (List) obj;
                for (int i = 0; i < list.size(); i += PRIMARY) {
                    if (((RRset) list.get(i)).getType() == type) {
                        list.set(i, rRset);
                        break;
                    }
                }
                list.add(rRset);
            } else {
                RRset rRset2 = (RRset) obj;
                if (rRset2.getType() == type) {
                    this.data.put(name, rRset);
                } else {
                    LinkedList linkedList = new LinkedList();
                    linkedList.add(rRset2);
                    linkedList.add(rRset);
                    this.data.put(name, linkedList);
                }
            }
        }
    }

    private synchronized void removeRRset(Name name, int i) {
        Object obj = this.data.get(name);
        if (obj != null) {
            if (obj instanceof List) {
                List list = (List) obj;
                int i2 = 0;
                while (i2 < list.size()) {
                    if (((RRset) list.get(i2)).getType() == i) {
                        list.remove(i2);
                        if (list.size() == 0) {
                            this.data.remove(name);
                        }
                    } else {
                        i2 += PRIMARY;
                    }
                }
            } else if (((RRset) obj).getType() == i) {
                this.data.remove(name);
            }
        }
    }

    private synchronized SetResponse lookup(Name name, int i) {
        SetResponse setResponse;
        int i2 = 0;
        synchronized (this) {
            if (name.subdomain(this.origin)) {
                int labels = name.labels();
                int labels2 = this.origin.labels();
                for (int i3 = labels2; i3 <= labels; i3 += PRIMARY) {
                    int i4;
                    int i5;
                    Name name2;
                    if (i3 == labels2) {
                        i4 = PRIMARY;
                    } else {
                        i4 = 0;
                    }
                    if (i3 == labels) {
                        i5 = PRIMARY;
                    } else {
                        i5 = 0;
                    }
                    if (i4 != 0) {
                        name2 = this.origin;
                    } else if (i5 != 0) {
                        name2 = name;
                    } else {
                        name2 = new Name(name, labels - i3);
                    }
                    Object exactName = exactName(name2);
                    if (exactName != null) {
                        RRset oneRRset;
                        if (i4 == 0) {
                            oneRRset = oneRRset(exactName, SECONDARY);
                            if (oneRRset != null) {
                                setResponse = new SetResponse(3, oneRRset);
                                break;
                            }
                        }
                        if (i5 == 0 || i != KEYRecord.PROTOCOL_ANY) {
                            if (i5 == 0) {
                                oneRRset = oneRRset(exactName, 39);
                                if (oneRRset != null) {
                                    setResponse = new SetResponse(5, oneRRset);
                                    break;
                                }
                            }
                            oneRRset = oneRRset(exactName, i);
                            if (oneRRset == null) {
                                oneRRset = oneRRset(exactName, 5);
                                if (oneRRset != null) {
                                    setResponse = new SetResponse(4, oneRRset);
                                    break;
                                }
                            }
                            setResponse = new SetResponse(6);
                            setResponse.addRRset(oneRRset);
                            break;
                            if (i5 != 0) {
                                setResponse = SetResponse.ofType(SECONDARY);
                                break;
                            }
                        } else {
                            setResponse = new SetResponse(6);
                            RRset[] allRRsets = allRRsets(exactName);
                            while (i2 < allRRsets.length) {
                                setResponse.addRRset(allRRsets[i2]);
                                i2 += PRIMARY;
                            }
                        }
                    }
                }
                if (this.hasWild) {
                    for (int i6 = 0; i6 < labels - labels2; i6 += PRIMARY) {
                        Object exactName2 = exactName(name.wild(i6 + PRIMARY));
                        if (exactName2 != null) {
                            RRset oneRRset2 = oneRRset(exactName2, i);
                            if (oneRRset2 != null) {
                                setResponse = new SetResponse(6);
                                setResponse.addRRset(oneRRset2);
                                break;
                            }
                        }
                    }
                }
                setResponse = SetResponse.ofType(PRIMARY);
            } else {
                setResponse = SetResponse.ofType(PRIMARY);
            }
        }
        return setResponse;
    }

    public SetResponse findRecords(Name name, int i) {
        return lookup(name, i);
    }

    public RRset findExactMatch(Name name, int i) {
        Object exactName = exactName(name);
        if (exactName == null) {
            return null;
        }
        return oneRRset(exactName, i);
    }

    public void addRRset(RRset rRset) {
        addRRset(rRset.getName(), rRset);
    }

    public void addRecord(Record record) {
        Name name = record.getName();
        int rRsetType = record.getRRsetType();
        synchronized (this) {
            RRset findRRset = findRRset(name, rRsetType);
            if (findRRset == null) {
                addRRset(name, new RRset(record));
            } else {
                findRRset.addRR(record);
            }
        }
    }

    public void removeRecord(Record record) {
        Name name = record.getName();
        int rRsetType = record.getRRsetType();
        synchronized (this) {
            RRset findRRset = findRRset(name, rRsetType);
            if (findRRset == null) {
                return;
            }
            if (findRRset.size() == PRIMARY && findRRset.first().equals(record)) {
                removeRRset(name, rRsetType);
            } else {
                findRRset.deleteRR(record);
            }
        }
    }

    public Iterator iterator() {
        return new ZoneIterator(false);
    }

    public Iterator AXFR() {
        return new ZoneIterator(true);
    }

    private void nodeToString(StringBuffer stringBuffer, Object obj) {
        RRset[] allRRsets = allRRsets(obj);
        for (int i = 0; i < allRRsets.length; i += PRIMARY) {
            RRset rRset = allRRsets[i];
            Iterator rrs = rRset.rrs();
            while (rrs.hasNext()) {
                stringBuffer.append(rrs.next() + SpecilApiUtil.LINE_SEP);
            }
            Iterator sigs = rRset.sigs();
            while (sigs.hasNext()) {
                stringBuffer.append(sigs.next() + SpecilApiUtil.LINE_SEP);
            }
        }
    }

    public synchronized String toMasterFile() {
        StringBuffer stringBuffer;
        stringBuffer = new StringBuffer();
        nodeToString(stringBuffer, this.originNode);
        for (Entry entry : this.data.entrySet()) {
            if (!this.origin.equals(entry.getKey())) {
                nodeToString(stringBuffer, entry.getValue());
            }
        }
        return stringBuffer.toString();
    }

    public String toString() {
        return toMasterFile();
    }
}
