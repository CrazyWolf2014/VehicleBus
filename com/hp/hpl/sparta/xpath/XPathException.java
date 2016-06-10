package com.hp.hpl.sparta.xpath;

import com.tencent.mm.sdk.openapi.BaseResp.ErrCode;
import java.io.IOException;
import org.apache.harmony.javax.security.auth.callback.ConfirmationCallback;
import org.codehaus.jackson.Base64Variant;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xmlpull.v1.XmlPullParser;

public class XPathException extends Exception {
    private Throwable cause_;

    XPathException(XPath xPath, Exception exception) {
        super(new StringBuffer().append(xPath).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(exception).toString());
        this.cause_ = null;
        this.cause_ = exception;
    }

    public XPathException(XPath xPath, String str) {
        super(new StringBuffer().append(xPath).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(str).toString());
        this.cause_ = null;
    }

    XPathException(XPath xPath, String str, SimpleStreamTokenizer simpleStreamTokenizer, String str2) {
        this(xPath, new StringBuffer().append(str).append(" got \"").append(toString(simpleStreamTokenizer)).append("\" instead of expected ").append(str2).toString());
    }

    private static String toString(SimpleStreamTokenizer simpleStreamTokenizer) {
        try {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(tokenToString(simpleStreamTokenizer));
            if (simpleStreamTokenizer.ttype != -1) {
                simpleStreamTokenizer.nextToken();
                stringBuffer.append(tokenToString(simpleStreamTokenizer));
                simpleStreamTokenizer.pushBack();
            }
            return stringBuffer.toString();
        } catch (IOException e) {
            return new StringBuffer().append("(cannot get  info: ").append(e).append(")").toString();
        }
    }

    private static String tokenToString(SimpleStreamTokenizer simpleStreamTokenizer) {
        switch (simpleStreamTokenizer.ttype) {
            case ErrCode.ERR_SENT_FAILED /*-3*/:
                return simpleStreamTokenizer.sval;
            case Base64Variant.BASE64_VALUE_PADDING /*-2*/:
                return new StringBuffer().append(simpleStreamTokenizer.nval).append(XmlPullParser.NO_NAMESPACE).toString();
            case ConfirmationCallback.UNSPECIFIED_OPTION /*-1*/:
                return "<end of expression>";
            default:
                return new StringBuffer().append((char) simpleStreamTokenizer.ttype).append(XmlPullParser.NO_NAMESPACE).toString();
        }
    }

    public Throwable getCause() {
        return this.cause_;
    }
}
