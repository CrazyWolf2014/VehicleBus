package com.kenai.jbosh;

final class AttrWait extends AbstractIntegerAttr {
    private AttrWait(String str) throws BOSHException {
        super(str);
        checkMinValue(1);
    }

    static AttrWait createFromString(String str) throws BOSHException {
        if (str == null) {
            return null;
        }
        return new AttrWait(str);
    }
}
