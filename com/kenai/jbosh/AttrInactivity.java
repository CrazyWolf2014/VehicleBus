package com.kenai.jbosh;

final class AttrInactivity extends AbstractIntegerAttr {
    private AttrInactivity(String str) throws BOSHException {
        super(str);
        checkMinValue(0);
    }

    static AttrInactivity createFromString(String str) throws BOSHException {
        if (str == null) {
            return null;
        }
        return new AttrInactivity(str);
    }
}
