package org.xbill.DNS;

public class RelativeNameException extends IllegalArgumentException {
    public RelativeNameException(Name name) {
        super("'" + name + "' is not an absolute name");
    }

    public RelativeNameException(String str) {
        super(str);
    }
}
