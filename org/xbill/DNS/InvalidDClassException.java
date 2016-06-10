package org.xbill.DNS;

public class InvalidDClassException extends IllegalArgumentException {
    public InvalidDClassException(int i) {
        super("Invalid DNS class: " + i);
    }
}
