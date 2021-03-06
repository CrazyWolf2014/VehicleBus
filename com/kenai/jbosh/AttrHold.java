package com.kenai.jbosh;

final class AttrHold extends AbstractIntegerAttr {
    private AttrHold(String str) throws BOSHException {
        super(str);
        checkMinValue(0);
    }

    static AttrHold createFromString(String str) throws BOSHException {
        if (str == null) {
            return null;
        }
        return new AttrHold(str);
    }
}
