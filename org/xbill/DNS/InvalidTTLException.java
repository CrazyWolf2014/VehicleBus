package org.xbill.DNS;

public class InvalidTTLException extends IllegalArgumentException {
    public InvalidTTLException(long j) {
        super("Invalid DNS TTL: " + j);
    }
}
