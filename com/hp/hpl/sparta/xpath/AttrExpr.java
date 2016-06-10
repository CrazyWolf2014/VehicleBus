package com.hp.hpl.sparta.xpath;

import com.cnmobi.im.util.XmppConnection;

public abstract class AttrExpr extends BooleanExpr {
    private final String attrName_;

    AttrExpr(String str) {
        this.attrName_ = str;
    }

    public String getAttrName() {
        return this.attrName_;
    }

    public String toString() {
        return new StringBuffer().append(XmppConnection.JID_SEPARATOR).append(this.attrName_).toString();
    }
}
