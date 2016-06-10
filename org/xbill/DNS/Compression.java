package org.xbill.DNS;

public class Compression {
    private static final int MAX_POINTER = 16383;
    private static final int TABLE_SIZE = 17;
    private Entry[] table;
    private boolean verbose;

    private static class Entry {
        Name name;
        Entry next;
        int pos;

        private Entry() {
        }
    }

    public Compression() {
        this.verbose = Options.check("verbosecompression");
        this.table = new Entry[TABLE_SIZE];
    }

    public void add(int i, Name name) {
        if (i <= MAX_POINTER) {
            int hashCode = (name.hashCode() & Integer.MAX_VALUE) % TABLE_SIZE;
            Entry entry = new Entry();
            entry.name = name;
            entry.pos = i;
            entry.next = this.table[hashCode];
            this.table[hashCode] = entry;
            if (this.verbose) {
                System.err.println("Adding " + name + " at " + i);
            }
        }
    }

    public int get(Name name) {
        Entry entry = this.table[(name.hashCode() & Integer.MAX_VALUE) % TABLE_SIZE];
        int i = -1;
        for (Entry entry2 = entry; entry2 != null; entry2 = entry2.next) {
            if (entry2.name.equals(name)) {
                i = entry2.pos;
            }
        }
        if (this.verbose) {
            System.err.println("Looking for " + name + ", found " + i);
        }
        return i;
    }
}
