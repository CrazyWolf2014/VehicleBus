package com.hp.hpl.sparta.xpath;

import com.cnmobi.im.util.XmppConnection;

public class AttrTest extends NodeTest {
    private final String attrName_;

    AttrTest(String str) {
        this.attrName_ = str;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public String getAttrName() {
        return this.attrName_;
    }

    public boolean isStringValue() {
        return true;
    }

    public String toString() {
        return new StringBuffer().append(XmppConnection.JID_SEPARATOR).append(this.attrName_).toString();
    }
}
