package com.hp.hpl.sparta.xpath;

import com.cnmobi.im.dto.MessageVo;
import com.cnmobi.im.view.RecordButton;
import com.tencent.mm.sdk.openapi.BaseResp.ErrCode;
import java.io.IOException;
import org.codehaus.jackson.Base64Variant;
import org.kxml2.wap.WbxmlParser;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;

public class ExprFactory {
    static BooleanExpr createExpr(XPath xPath, SimpleStreamTokenizer simpleStreamTokenizer) throws XPathException, IOException {
        String str;
        switch (simpleStreamTokenizer.ttype) {
            case ErrCode.ERR_SENT_FAILED /*-3*/:
                if (!simpleStreamTokenizer.sval.equals(MessageVo.TYPE_TEXT)) {
                    throw new XPathException(xPath, "at beginning of expression", simpleStreamTokenizer, "text()");
                } else if (simpleStreamTokenizer.nextToken() != 40) {
                    throw new XPathException(xPath, "after text", simpleStreamTokenizer, "(");
                } else if (simpleStreamTokenizer.nextToken() != 41) {
                    throw new XPathException(xPath, "after text(", simpleStreamTokenizer, ")");
                } else {
                    switch (simpleStreamTokenizer.nextToken()) {
                        case Service.DSP /*33*/:
                            simpleStreamTokenizer.nextToken();
                            if (simpleStreamTokenizer.ttype != 61) {
                                throw new XPathException(xPath, "after !", simpleStreamTokenizer, "=");
                            }
                            simpleStreamTokenizer.nextToken();
                            if (simpleStreamTokenizer.ttype == 34 || simpleStreamTokenizer.ttype == 39) {
                                str = simpleStreamTokenizer.sval;
                                simpleStreamTokenizer.nextToken();
                                return new TextNotEqualsExpr(str);
                            }
                            throw new XPathException(xPath, "right hand side of !=", simpleStreamTokenizer, "quoted string");
                        case Service.NI_MAIL /*61*/:
                            simpleStreamTokenizer.nextToken();
                            if (simpleStreamTokenizer.ttype == 34 || simpleStreamTokenizer.ttype == 39) {
                                str = simpleStreamTokenizer.sval;
                                simpleStreamTokenizer.nextToken();
                                return new TextEqualsExpr(str);
                            }
                            throw new XPathException(xPath, "right hand side of equals", simpleStreamTokenizer, "quoted string");
                        default:
                            return TextExistsExpr.INSTANCE;
                    }
                }
            case Base64Variant.BASE64_VALUE_PADDING /*-2*/:
                int i = simpleStreamTokenizer.nval;
                simpleStreamTokenizer.nextToken();
                return new PositionEqualsExpr(i);
            case WbxmlParser.WAP_EXTENSION /*64*/:
                if (simpleStreamTokenizer.nextToken() != -3) {
                    throw new XPathException(xPath, "after @", simpleStreamTokenizer, "name");
                }
                String str2 = simpleStreamTokenizer.sval;
                int parseInt;
                switch (simpleStreamTokenizer.nextToken()) {
                    case Service.DSP /*33*/:
                        simpleStreamTokenizer.nextToken();
                        if (simpleStreamTokenizer.ttype != 61) {
                            throw new XPathException(xPath, "after !", simpleStreamTokenizer, "=");
                        }
                        simpleStreamTokenizer.nextToken();
                        if (simpleStreamTokenizer.ttype == 34 || simpleStreamTokenizer.ttype == 39) {
                            str = simpleStreamTokenizer.sval;
                            simpleStreamTokenizer.nextToken();
                            return new AttrNotEqualsExpr(str2, str);
                        }
                        throw new XPathException(xPath, "right hand side of !=", simpleStreamTokenizer, "quoted string");
                    case RecordButton.MAX_TIME /*60*/:
                        simpleStreamTokenizer.nextToken();
                        if (simpleStreamTokenizer.ttype == 34 || simpleStreamTokenizer.ttype == 39) {
                            parseInt = Integer.parseInt(simpleStreamTokenizer.sval);
                        } else if (simpleStreamTokenizer.ttype == -2) {
                            parseInt = simpleStreamTokenizer.nval;
                        } else {
                            throw new XPathException(xPath, "right hand side of less-than", simpleStreamTokenizer, "quoted string or number");
                        }
                        simpleStreamTokenizer.nextToken();
                        return new AttrLessExpr(str2, parseInt);
                    case Service.NI_MAIL /*61*/:
                        simpleStreamTokenizer.nextToken();
                        if (simpleStreamTokenizer.ttype == 34 || simpleStreamTokenizer.ttype == 39) {
                            str = simpleStreamTokenizer.sval;
                            simpleStreamTokenizer.nextToken();
                            return new AttrEqualsExpr(str2, str);
                        }
                        throw new XPathException(xPath, "right hand side of equals", simpleStreamTokenizer, "quoted string");
                    case Protocol.CFTP /*62*/:
                        simpleStreamTokenizer.nextToken();
                        if (simpleStreamTokenizer.ttype == 34 || simpleStreamTokenizer.ttype == 39) {
                            parseInt = Integer.parseInt(simpleStreamTokenizer.sval);
                        } else if (simpleStreamTokenizer.ttype == -2) {
                            parseInt = simpleStreamTokenizer.nval;
                        } else {
                            throw new XPathException(xPath, "right hand side of greater-than", simpleStreamTokenizer, "quoted string or number");
                        }
                        simpleStreamTokenizer.nextToken();
                        return new AttrGreaterExpr(str2, parseInt);
                    default:
                        return new AttrExistsExpr(str2);
                }
            default:
                throw new XPathException(xPath, "at beginning of expression", simpleStreamTokenizer, "@, number, or text()");
        }
    }
}
