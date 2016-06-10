package com.hp.hpl.sparta.xpath;

import com.cnmobi.im.dto.MessageVo;
import com.tencent.mm.sdk.openapi.BaseResp.ErrCode;
import java.io.IOException;
import org.kxml2.wap.WbxmlParser;
import org.xbill.DNS.WKSRecord.Service;

public class Step {
    public static Step DOT;
    private final boolean multiLevel_;
    private final NodeTest nodeTest_;
    private final BooleanExpr predicate_;

    static {
        DOT = new Step(ThisNodeTest.INSTANCE, TrueExpr.INSTANCE);
    }

    Step(NodeTest nodeTest, BooleanExpr booleanExpr) {
        this.nodeTest_ = nodeTest;
        this.predicate_ = booleanExpr;
        this.multiLevel_ = false;
    }

    Step(XPath xPath, boolean z, SimpleStreamTokenizer simpleStreamTokenizer) throws XPathException, IOException {
        this.multiLevel_ = z;
        switch (simpleStreamTokenizer.ttype) {
            case ErrCode.ERR_SENT_FAILED /*-3*/:
                if (simpleStreamTokenizer.sval.equals(MessageVo.TYPE_TEXT)) {
                    if (simpleStreamTokenizer.nextToken() == 40 && simpleStreamTokenizer.nextToken() == 41) {
                        this.nodeTest_ = TextTest.INSTANCE;
                        break;
                    }
                    throw new XPathException(xPath, "after text", simpleStreamTokenizer, "()");
                }
                this.nodeTest_ = new ElementTest(simpleStreamTokenizer.sval);
                break;
                break;
            case Service.NAMESERVER /*42*/:
                this.nodeTest_ = AllElementTest.INSTANCE;
                break;
            case Service.MPM_SND /*46*/:
                if (simpleStreamTokenizer.nextToken() != 46) {
                    simpleStreamTokenizer.pushBack();
                    this.nodeTest_ = ThisNodeTest.INSTANCE;
                    break;
                }
                this.nodeTest_ = ParentNodeTest.INSTANCE;
                break;
            case WbxmlParser.WAP_EXTENSION /*64*/:
                if (simpleStreamTokenizer.nextToken() == -3) {
                    this.nodeTest_ = new AttrTest(simpleStreamTokenizer.sval);
                    break;
                }
                throw new XPathException(xPath, "after @ in node test", simpleStreamTokenizer, "name");
            default:
                throw new XPathException(xPath, "at begininning of step", simpleStreamTokenizer, "'.' or '*' or name");
        }
        if (simpleStreamTokenizer.nextToken() == 91) {
            simpleStreamTokenizer.nextToken();
            this.predicate_ = ExprFactory.createExpr(xPath, simpleStreamTokenizer);
            if (simpleStreamTokenizer.ttype != 93) {
                throw new XPathException(xPath, "after predicate expression", simpleStreamTokenizer, "]");
            }
            simpleStreamTokenizer.nextToken();
            return;
        }
        this.predicate_ = TrueExpr.INSTANCE;
    }

    public NodeTest getNodeTest() {
        return this.nodeTest_;
    }

    public BooleanExpr getPredicate() {
        return this.predicate_;
    }

    public boolean isMultiLevel() {
        return this.multiLevel_;
    }

    public boolean isStringValue() {
        return this.nodeTest_.isStringValue();
    }

    public String toString() {
        return new StringBuffer().append(this.nodeTest_.toString()).append(this.predicate_.toString()).toString();
    }
}
