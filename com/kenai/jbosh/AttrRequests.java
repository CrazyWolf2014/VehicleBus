package com.kenai.jbosh;

final class AttrRequests extends AbstractIntegerAttr {
    private AttrRequests(String str) throws BOSHException {
        super(str);
        checkMinValue(1);
    }

    static AttrRequests createFromString(String str) throws BOSHException {
        if (str == null) {
            return null;
        }
        return new AttrRequests(str);
    }
}
