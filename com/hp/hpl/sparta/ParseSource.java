package com.hp.hpl.sparta;

public interface ParseSource {
    public static final ParseLog DEFAULT_LOG;
    public static final int MAXLOOKAHEAD;

    static {
        DEFAULT_LOG = new DefaultLog();
        MAXLOOKAHEAD = "<?xml version=\"1.0\" encoding=\"\"".length() + 40;
    }

    int getLineNumber();

    String getSystemId();

    String toString();
}
