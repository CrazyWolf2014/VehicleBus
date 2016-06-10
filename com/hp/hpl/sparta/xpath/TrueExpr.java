package com.hp.hpl.sparta.xpath;

import org.xmlpull.v1.XmlPullParser;

public class TrueExpr extends BooleanExpr {
    static final TrueExpr INSTANCE;

    static {
        INSTANCE = new TrueExpr();
    }

    private TrueExpr() {
    }

    public void accept(BooleanExprVisitor booleanExprVisitor) {
        booleanExprVisitor.visit(this);
    }

    public String toString() {
        return XmlPullParser.NO_NAMESPACE;
    }
}
