package com.kenai.jbosh;

final class AttrSessionID extends AbstractAttr<String> {
    private AttrSessionID(String str) {
        super(str);
    }

    static AttrSessionID createFromString(String str) {
        return new AttrSessionID(str);
    }
}
