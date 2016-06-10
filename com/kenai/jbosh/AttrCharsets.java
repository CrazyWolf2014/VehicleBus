package com.kenai.jbosh;

final class AttrCharsets extends AbstractAttr<String> {
    private final String[] charsets;

    private AttrCharsets(String str) {
        super(str);
        this.charsets = str.split("\\ +");
    }

    static AttrCharsets createFromString(String str) {
        if (str == null) {
            return null;
        }
        return new AttrCharsets(str);
    }

    boolean isAccepted(String str) {
        for (String equalsIgnoreCase : this.charsets) {
            if (equalsIgnoreCase.equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }
}
