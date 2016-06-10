package com.kenai.jbosh;

final class AttrAck extends AbstractAttr<String> {
    private AttrAck(String str) throws BOSHException {
        super(str);
    }

    static AttrAck createFromString(String str) throws BOSHException {
        if (str == null) {
            return null;
        }
        return new AttrAck(str);
    }
}
