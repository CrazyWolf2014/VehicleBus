package org.xbill.DNS;

import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import org.codehaus.jackson.util.MinimalPrettyPrinter;

public class Cache {
    private static final int defaultMaxEntries = 50000;
    private CacheMap data;
    private int dclass;
    private int maxcache;
    private int maxncache;

    private static class CacheMap extends LinkedHashMap {
        private int maxsize;

        CacheMap(int i) {
            super(16, 0.75f, true);
            this.maxsize = -1;
            this.maxsize = i;
        }

        int getMaxSize() {
            return this.maxsize;
        }

        void setMaxSize(int i) {
            this.maxsize = i;
        }

        protected boolean removeEldestEntry(Entry entry) {
            return this.maxsize >= 0 && size() > this.maxsize;
        }
    }

    private interface Element {
        int compareCredibility(int i);

        boolean expired();

        int getType();
    }

    private static class CacheRRset extends RRset implements Element {
        private static final long serialVersionUID = 5971755205903597024L;
        int credibility;
        int expire;

        public CacheRRset(Record record, int i, long j) {
            this.credibility = i;
            this.expire = Cache.limitExpire(record.getTTL(), j);
            addRR(record);
        }

        public CacheRRset(RRset rRset, int i, long j) {
            super(rRset);
            this.credibility = i;
            this.expire = Cache.limitExpire(rRset.getTTL(), j);
        }

        public final boolean expired() {
            return ((int) (System.currentTimeMillis() / 1000)) >= this.expire;
        }

        public final int compareCredibility(int i) {
            return this.credibility - i;
        }

        public String toString() {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(super.toString());
            stringBuffer.append(" cl = ");
            stringBuffer.append(this.credibility);
            return stringBuffer.toString();
        }
    }

    private static class NegativeElement implements Element {
        int credibility;
        int expire;
        Name name;
        int type;

        public NegativeElement(Name name, int i, SOARecord sOARecord, int i2, long j) {
            this.name = name;
            this.type = i;
            long j2 = 0;
            if (sOARecord != null) {
                j2 = sOARecord.getMinimum();
            }
            this.credibility = i2;
            this.expire = Cache.limitExpire(j2, j);
        }

        public int getType() {
            return this.type;
        }

        public final boolean expired() {
            return ((int) (System.currentTimeMillis() / 1000)) >= this.expire;
        }

        public final int compareCredibility(int i) {
            return this.credibility - i;
        }

        public String toString() {
            StringBuffer stringBuffer = new StringBuffer();
            if (this.type == 0) {
                stringBuffer.append("NXDOMAIN " + this.name);
            } else {
                stringBuffer.append("NXRRSET " + this.name + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + Type.string(this.type));
            }
            stringBuffer.append(" cl = ");
            stringBuffer.append(this.credibility);
            return stringBuffer.toString();
        }
    }

    private static int limitExpire(long j, long j2) {
        if (j2 >= 0 && j2 < j) {
            j = j2;
        }
        long currentTimeMillis = (System.currentTimeMillis() / 1000) + j;
        if (currentTimeMillis < 0 || currentTimeMillis > TTL.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return (int) currentTimeMillis;
    }

    public Cache(int i) {
        this.maxncache = -1;
        this.maxcache = -1;
        this.dclass = i;
        this.data = new CacheMap(defaultMaxEntries);
    }

    public Cache() {
        this(1);
    }

    public Cache(String str) throws IOException {
        this.maxncache = -1;
        this.maxcache = -1;
        this.data = new CacheMap(defaultMaxEntries);
        Master master = new Master(str);
        while (true) {
            Record nextRecord = master.nextRecord();
            if (nextRecord != null) {
                addRecord(nextRecord, 0, master);
            } else {
                return;
            }
        }
    }

    private synchronized Object exactName(Name name) {
        return this.data.get(name);
    }

    private synchronized void removeName(Name name) {
        this.data.remove(name);
    }

    private synchronized Element[] allElements(Object obj) {
        Element[] elementArr;
        if (obj instanceof List) {
            List list = (List) obj;
            elementArr = (Element[]) list.toArray(new Element[list.size()]);
        } else {
            elementArr = new Element[]{(Element) obj};
        }
        return elementArr;
    }

    private synchronized Element oneElement(Name name, Object obj, int i, int i2) {
        Element element = null;
        synchronized (this) {
            if (i == KEYRecord.PROTOCOL_ANY) {
                throw new IllegalArgumentException("oneElement(ANY)");
            }
            Element element2;
            if (obj instanceof List) {
                List list = (List) obj;
                for (int i3 = 0; i3 < list.size(); i3++) {
                    element2 = (Element) list.get(i3);
                    if (element2.getType() == i) {
                        break;
                    }
                }
                element2 = null;
            } else {
                Element element3 = (Element) obj;
                if (element3.getType() == i) {
                    element2 = element3;
                }
                element2 = null;
            }
            if (element2 != null) {
                if (element2.expired()) {
                    removeElement(name, i);
                } else if (element2.compareCredibility(i2) >= 0) {
                    element = element2;
                }
            }
        }
        return element;
    }

    private synchronized Element findElement(Name name, int i, int i2) {
        Element element;
        Object exactName = exactName(name);
        if (exactName == null) {
            element = null;
        } else {
            element = oneElement(name, exactName, i, i2);
        }
        return element;
    }

    private synchronized void addElement(Name name, Element element) {
        Object obj = this.data.get(name);
        if (obj == null) {
            this.data.put(name, element);
        } else {
            int type = element.getType();
            if (obj instanceof List) {
                List list = (List) obj;
                for (int i = 0; i < list.size(); i++) {
                    if (((Element) list.get(i)).getType() == type) {
                        list.set(i, element);
                        break;
                    }
                }
                list.add(element);
            } else {
                Element element2 = (Element) obj;
                if (element2.getType() == type) {
                    this.data.put(name, element);
                } else {
                    LinkedList linkedList = new LinkedList();
                    linkedList.add(element2);
                    linkedList.add(element);
                    this.data.put(name, linkedList);
                }
            }
        }
    }

    private synchronized void removeElement(Name name, int i) {
        Object obj = this.data.get(name);
        if (obj != null) {
            if (obj instanceof List) {
                List list = (List) obj;
                int i2 = 0;
                while (i2 < list.size()) {
                    if (((Element) list.get(i2)).getType() == i) {
                        list.remove(i2);
                        if (list.size() == 0) {
                            this.data.remove(name);
                        }
                    } else {
                        i2++;
                    }
                }
            } else if (((Element) obj).getType() == i) {
                this.data.remove(name);
            }
        }
    }

    public synchronized void clearCache() {
        this.data.clear();
    }

    public synchronized void addRecord(Record record, int i, Object obj) {
        Name name = record.getName();
        int rRsetType = record.getRRsetType();
        if (Type.isRR(rRsetType)) {
            Element findElement = findElement(name, rRsetType, i);
            if (findElement == null) {
                addRRset(new CacheRRset(record, i, (long) this.maxcache), i);
            } else if (findElement.compareCredibility(i) == 0 && (findElement instanceof CacheRRset)) {
                ((CacheRRset) findElement).addRR(record);
            }
        }
    }

    public synchronized void addRRset(RRset rRset, int i) {
        long ttl = rRset.getTTL();
        Name name = rRset.getName();
        int type = rRset.getType();
        Element findElement = findElement(name, type, 0);
        if (ttl != 0) {
            if (findElement != null) {
                if (findElement.compareCredibility(i) <= 0) {
                    findElement = null;
                }
            }
            if (findElement == null) {
                if (rRset instanceof CacheRRset) {
                    rRset = (CacheRRset) rRset;
                } else {
                    rRset = new CacheRRset(rRset, i, (long) this.maxcache);
                }
                addElement(name, rRset);
            }
        } else if (findElement != null && findElement.compareCredibility(i) <= 0) {
            removeElement(name, type);
        }
    }

    public synchronized void addNegative(Name name, int i, SOARecord sOARecord, int i2) {
        long ttl;
        if (sOARecord != null) {
            ttl = sOARecord.getTTL();
        } else {
            ttl = 0;
        }
        Element findElement = findElement(name, i, 0);
        if (ttl != 0) {
            if (findElement != null) {
                if (findElement.compareCredibility(i2) <= 0) {
                    findElement = null;
                }
            }
            if (findElement == null) {
                addElement(name, new NegativeElement(name, i, sOARecord, i2, (long) this.maxncache));
            }
        } else if (findElement != null && findElement.compareCredibility(i2) <= 0) {
            removeElement(name, i);
        }
    }

    protected synchronized SetResponse lookup(Name name, int i, int i2) {
        SetResponse setResponse;
        int labels = name.labels();
        int i3 = labels;
        while (i3 >= 1) {
            Name name2;
            Object obj = i3 == 1 ? 1 : null;
            Object obj2 = i3 == labels ? 1 : null;
            if (obj != null) {
                name2 = Name.root;
            } else if (obj2 != null) {
                name2 = name;
            } else {
                name2 = new Name(name, labels - i3);
            }
            Object obj3 = this.data.get(name2);
            if (obj3 != null) {
                Element oneElement;
                if (obj2 == null || i != KEYRecord.PROTOCOL_ANY) {
                    if (obj2 == null) {
                        oneElement = oneElement(name2, obj3, 39, i2);
                        if (oneElement != null && (oneElement instanceof CacheRRset)) {
                            setResponse = new SetResponse(5, (CacheRRset) oneElement);
                            break;
                        }
                    }
                    oneElement = oneElement(name2, obj3, i, i2);
                    if (oneElement == null || !(oneElement instanceof CacheRRset)) {
                        if (oneElement == null) {
                            oneElement = oneElement(name2, obj3, 5, i2);
                            if (oneElement != null && (oneElement instanceof CacheRRset)) {
                                setResponse = new SetResponse(4, (CacheRRset) oneElement);
                                break;
                            }
                        }
                        setResponse = new SetResponse(2);
                        break;
                    }
                    SetResponse setResponse2 = new SetResponse(6);
                    setResponse2.addRRset((CacheRRset) oneElement);
                    setResponse = setResponse2;
                    break;
                }
                SetResponse setResponse3 = new SetResponse(6);
                Element[] allElements = allElements(obj3);
                int i4 = 0;
                int i5 = 0;
                while (i5 < allElements.length) {
                    int i6;
                    oneElement = allElements[i5];
                    if (oneElement.expired()) {
                        removeElement(name2, oneElement.getType());
                        i6 = i4;
                    } else if (!(oneElement instanceof CacheRRset)) {
                        i6 = i4;
                    } else if (oneElement.compareCredibility(i2) < 0) {
                        i6 = i4;
                    } else {
                        setResponse3.addRRset((CacheRRset) oneElement);
                        i6 = i4 + 1;
                    }
                    i5++;
                    i4 = i6;
                }
                if (i4 > 0) {
                    setResponse = setResponse3;
                    break;
                }
                oneElement = oneElement(name2, obj3, 2, i2);
                if (oneElement == null || !(oneElement instanceof CacheRRset)) {
                    if (!(obj2 == null || oneElement(name2, obj3, 0, i2) == null)) {
                        setResponse = SetResponse.ofType(1);
                        break;
                    }
                }
                setResponse = new SetResponse(3, (CacheRRset) oneElement);
                break;
            }
            i3--;
        }
        setResponse = SetResponse.ofType(0);
        return setResponse;
    }

    public SetResponse lookupRecords(Name name, int i, int i2) {
        return lookup(name, i, i2);
    }

    private RRset[] findRecords(Name name, int i, int i2) {
        SetResponse lookupRecords = lookupRecords(name, i, i2);
        if (lookupRecords.isSuccessful()) {
            return lookupRecords.answers();
        }
        return null;
    }

    public RRset[] findRecords(Name name, int i) {
        return findRecords(name, i, 3);
    }

    public RRset[] findAnyRecords(Name name, int i) {
        return findRecords(name, i, 2);
    }

    private final int getCred(int i, boolean z) {
        if (i == 1) {
            if (z) {
                return 4;
            }
            return 3;
        } else if (i == 2) {
            if (z) {
                return 4;
            }
            return 3;
        } else if (i == 3) {
            return 1;
        } else {
            throw new IllegalArgumentException("getCred: invalid section");
        }
    }

    private static void markAdditional(RRset rRset, Set set) {
        if (rRset.first().getAdditionalName() != null) {
            Iterator rrs = rRset.rrs();
            while (rrs.hasNext()) {
                Name additionalName = ((Record) rrs.next()).getAdditionalName();
                if (additionalName != null) {
                    set.add(additionalName);
                }
            }
        }
    }

    public SetResponse addMessage(Message message) {
        boolean flag = message.getHeader().getFlag(5);
        Record question = message.getQuestion();
        int rcode = message.getHeader().getRcode();
        Object obj = null;
        boolean check = Options.check("verbosecache");
        if ((rcode != 0 && rcode != 3) || question == null) {
            return null;
        }
        SetResponse setResponse;
        int i;
        Name name = question.getName();
        int type = question.getType();
        int dClass = question.getDClass();
        Set hashSet = new HashSet();
        RRset[] sectionRRsets = message.getSectionRRsets(1);
        int i2 = 0;
        Name name2 = name;
        SetResponse setResponse2 = null;
        while (i2 < sectionRRsets.length) {
            Object obj2;
            Name name3;
            if (sectionRRsets[i2].getDClass() != dClass) {
                obj2 = obj;
                name3 = name2;
            } else {
                int type2 = sectionRRsets[i2].getType();
                Name name4 = sectionRRsets[i2].getName();
                int cred = getCred(1, flag);
                if ((type2 == type || type == 255) && name4.equals(name2)) {
                    addRRset(sectionRRsets[i2], cred);
                    obj2 = 1;
                    if (name2 == name) {
                        if (setResponse2 == null) {
                            setResponse2 = new SetResponse(6);
                        }
                        setResponse2.addRRset(sectionRRsets[i2]);
                    }
                    markAdditional(sectionRRsets[i2], hashSet);
                    name3 = name2;
                } else if (type2 == 5 && name4.equals(name2)) {
                    addRRset(sectionRRsets[i2], cred);
                    if (name2 == name) {
                        setResponse = new SetResponse(4, sectionRRsets[i2]);
                    } else {
                        setResponse = setResponse2;
                    }
                    r17 = setResponse;
                    obj2 = obj;
                    name3 = ((CNAMERecord) sectionRRsets[i2].first()).getTarget();
                    setResponse2 = r17;
                } else if (type2 == 39 && name2.subdomain(name4)) {
                    addRRset(sectionRRsets[i2], cred);
                    if (name2 == name) {
                        setResponse = new SetResponse(5, sectionRRsets[i2]);
                    } else {
                        setResponse = setResponse2;
                    }
                    try {
                        r17 = setResponse;
                        obj2 = obj;
                        name3 = name2.fromDNAME((DNAMERecord) sectionRRsets[i2].first());
                        setResponse2 = r17;
                    } catch (NameTooLongException e) {
                    }
                } else {
                    obj2 = obj;
                    name3 = name2;
                }
            }
            i2++;
            name2 = name3;
            obj = obj2;
        }
        setResponse = setResponse2;
        RRset[] sectionRRsets2 = message.getSectionRRsets(2);
        RRset rRset = null;
        RRset rRset2 = null;
        int i3 = 0;
        while (i3 < sectionRRsets2.length) {
            if (sectionRRsets2[i3].getType() == 6 && name2.subdomain(sectionRRsets2[i3].getName())) {
                rRset2 = sectionRRsets2[i3];
            } else if (sectionRRsets2[i3].getType() == 2 && name2.subdomain(sectionRRsets2[i3].getName())) {
                rRset = sectionRRsets2[i3];
            }
            i3++;
        }
        if (obj == null) {
            int i4 = rcode == 3 ? 0 : type;
            if (rcode == 3 || rRset2 != null || rRset == null) {
                i3 = getCred(2, flag);
                SOARecord sOARecord = null;
                if (rRset2 != null) {
                    sOARecord = (SOARecord) rRset2.first();
                }
                addNegative(name2, i4, sOARecord, i3);
                if (setResponse == null) {
                    if (rcode == 3) {
                        i = 1;
                    } else {
                        i = 2;
                    }
                    setResponse = SetResponse.ofType(i);
                }
            } else {
                addRRset(rRset, getCred(2, flag));
                markAdditional(rRset, hashSet);
                if (setResponse == null) {
                    setResponse = new SetResponse(3, rRset);
                }
            }
        } else if (rcode == 0 && rRset != null) {
            addRRset(rRset, getCred(2, flag));
            markAdditional(rRset, hashSet);
        }
        RRset[] sectionRRsets3 = message.getSectionRRsets(3);
        i = 0;
        while (i < sectionRRsets3.length) {
            int type3 = sectionRRsets3[i].getType();
            if ((type3 == 1 || type3 == 28 || type3 == 38) && hashSet.contains(sectionRRsets3[i].getName())) {
                addRRset(sectionRRsets3[i], getCred(3, flag));
            }
            i++;
        }
        if (!check) {
            return setResponse;
        }
        System.out.println("addMessage: " + setResponse);
        return setResponse;
    }

    public void flushSet(Name name, int i) {
        removeElement(name, i);
    }

    public void flushName(Name name) {
        removeName(name);
    }

    public void setMaxNCache(int i) {
        this.maxncache = i;
    }

    public int getMaxNCache() {
        return this.maxncache;
    }

    public void setMaxCache(int i) {
        this.maxcache = i;
    }

    public int getMaxCache() {
        return this.maxcache;
    }

    public int getSize() {
        return this.data.size();
    }

    public int getMaxEntries() {
        return this.data.getMaxSize();
    }

    public void setMaxEntries(int i) {
        this.data.setMaxSize(i);
    }

    public int getDClass() {
        return this.dclass;
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        synchronized (this) {
            for (Object allElements : this.data.values()) {
                Element[] allElements2 = allElements(allElements);
                for (Object append : allElements2) {
                    stringBuffer.append(append);
                    stringBuffer.append(SpecilApiUtil.LINE_SEP);
                }
            }
        }
        return stringBuffer.toString();
    }
}
