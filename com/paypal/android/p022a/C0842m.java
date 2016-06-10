package com.paypal.android.p022a;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;
import com.cnlaunch.framework.network.async.AsyncTaskManager;
import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import com.ifoer.entity.Constant;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;
import com.paypal.android.MEP.PayPalInvoiceItem;
import com.paypal.android.MEP.PayPalPreapproval;
import com.paypal.android.MEP.PayPalReceiverDetails;
import com.paypal.android.p022a.p023a.C0822b;
import com.paypal.android.p022a.p023a.C0823c;
import com.paypal.android.p022a.p023a.C0824d;
import com.paypal.android.p022a.p023a.C0825e;
import com.paypal.android.p022a.p023a.C0828h;
import com.paypal.android.p022a.p023a.C0829i;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import javax.xml.parsers.DocumentBuilderFactory;
import junit.framework.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.paypal.android.a.m */
public final class C0842m {
    private static String[] f1573a;

    static {
        f1573a = new String[]{"+1", "+27", "+30", "+31", "+32", "+33", "+34", "+36", "+39", "+41", "+43", "+44", "+45", "+46", "+47", "+48", "+49", "+52", "+54", "+55", "+56", "+58", "+60", "+61", "+64", "+65", "+66", "+81", "+82", "+86", "+90", "+91", "+351", "+352", "+353", "+354", "+356", "+357", "+358", "+370", "+371", "+372", "+377", "+386", "+420", "+421", "+506", "+593", "+598", "+852", "+886", "+972"};
    }

    public static String m1580a() {
        String deviceId;
        Exception exception;
        String str;
        StringBuilder stringBuilder;
        String str2 = XmlPullParser.NO_NAMESPACE;
        String str3 = XmlPullParser.NO_NAMESPACE;
        String str4 = "false";
        String str5 = XmlPullParser.NO_NAMESPACE;
        String str6 = XmlPullParser.NO_NAMESPACE;
        String str7 = XmlPullParser.NO_NAMESPACE;
        String str8 = XmlPullParser.NO_NAMESPACE;
        String str9 = XmlPullParser.NO_NAMESPACE;
        String str10 = XmlPullParser.NO_NAMESPACE;
        String str11 = XmlPullParser.NO_NAMESPACE;
        String str12 = XmlPullParser.NO_NAMESPACE;
        String str13 = XmlPullParser.NO_NAMESPACE;
        String str14 = XmlPullParser.NO_NAMESPACE;
        String str15 = XmlPullParser.NO_NAMESPACE;
        try {
            str2 = C1107b.m2377b(PayPal.getInstance().getAppID());
            TelephonyManager telephonyManager = (TelephonyManager) PayPal.getInstance().getParentContext().getSystemService("phone");
            str11 = "MEID";
            str15 = "AndroidCDMA";
            if (telephonyManager.getPhoneType() == 1) {
                str11 = "IMEI";
                str15 = "AndroidGSM";
            }
            deviceId = telephonyManager.getDeviceId();
            str10 = "Phone";
            if (deviceId == null) {
                deviceId = ((WifiManager) PayPal.getInstance().getParentContext().getSystemService("wifi")).getConnectionInfo().getMacAddress();
                str11 = "MAC";
                str15 = "AndroidGSM";
                str10 = "Tablet";
            }
            str5 = C1107b.m2377b(deviceId);
            str7 = C1107b.m2377b(Build.DEVICE);
            str6 = C1107b.m2377b(Build.MODEL);
            str8 = "Android";
            str9 = C1107b.m2377b(VERSION.SDK);
            str3 = C1107b.m2377b(PayPal.getVersionWithoutBuild());
            str14 = PayPal.getInstance().getParentContext().getPackageName();
            try {
                Context parentContext = PayPal.getInstance().getParentContext();
                PackageManager packageManager = parentContext.getPackageManager();
                str13 = packageManager.getPackageInfo(parentContext.getPackageName(), 0).applicationInfo.loadLabel(packageManager).toString();
                if (str13 == null || str13.length() <= 0) {
                    str13 = str14;
                }
                if (str13 == null) {
                    str13 = str14;
                }
                try {
                    deviceId = str5.equals("000000000000000") ? "true" : str4;
                    str4 = str13;
                    str13 = str11;
                    str11 = str10;
                    str10 = str9;
                    str9 = str8;
                    str8 = str7;
                    str7 = str6;
                    str6 = str5;
                    str5 = deviceId;
                    deviceId = str15;
                    str15 = str14;
                } catch (Exception e) {
                    exception = e;
                    deviceId = str15;
                    str15 = str13;
                    str13 = str14;
                    exception.printStackTrace();
                    str = str15;
                    str15 = str13;
                    str13 = str11;
                    str11 = str10;
                    str10 = str9;
                    str9 = str8;
                    str8 = str7;
                    str7 = str6;
                    str6 = str5;
                    str5 = str4;
                    str4 = str;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append("<soap:Envelope ").append("xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" ").append("xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" ").append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ").append("xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" ").append("xmlns:GMAdapter=\"http://svcs.paypal.com/mobile/adapter/types/pt\" ").append("xmlns:tns1=\"http://svcs.paypal.com/types/common\" ").append("xmlns:jaxb=\"http://java.sun.com/xml/ns/jaxb\" ").append("xmlns:annox=\"http://annox.dev.java.net\" ").append("xmlns:ov=\"http://annox.dev.java.net/net.sf.oval.constraint\" ").append("xsl:version=\"1.0\">").append("<soap:Body>").append("<DeviceInterrogationRequest>");
                    C0842m.m1584a(stringBuilder, AlixDefine.VERSION, "2.0");
                    C0842m.m1584a(stringBuilder, "paypalAppId", str2);
                    C0842m.m1584a(stringBuilder, "mplVersion", str3);
                    C0842m.m1584a(stringBuilder, "deviceReferenceToken", C0842m.m1605d("DeviceReferenceToken"));
                    stringBuilder.append("<deviceDetails>").append("<deviceId>");
                    C0842m.m1584a(stringBuilder, "deviceIdType", str13);
                    C0842m.m1584a(stringBuilder, "deviceIdentifier", str6);
                    stringBuilder.append("</deviceId>");
                    C0842m.m1584a(stringBuilder, "deviceName", str8);
                    C0842m.m1584a(stringBuilder, "deviceModel", str7);
                    C0842m.m1584a(stringBuilder, "systemName", str9);
                    C0842m.m1584a(stringBuilder, "systemVersion", str10);
                    C0842m.m1584a(stringBuilder, "deviceCategory", str11);
                    C0842m.m1584a(stringBuilder, "deviceSimulator", str5);
                    stringBuilder.append("</deviceDetails>");
                    stringBuilder.append("<embeddingApplicationDetails>");
                    C0842m.m1584a(stringBuilder, "deviceAppId", str14);
                    C0842m.m1584a(stringBuilder, "deviceAppName", str15);
                    C0842m.m1584a(stringBuilder, "deviceAppDisplayName", str4);
                    C0842m.m1584a(stringBuilder, "clientPlatform", deviceId);
                    C0842m.m1584a(stringBuilder, "deviceAppVersion", str3);
                    stringBuilder.append("</embeddingApplicationDetails>");
                    stringBuilder.append("<securityDetails>");
                    C0842m.m1584a(stringBuilder, "applicationNonce", C0842m.m1608e());
                    C0842m.m1584a(stringBuilder, "deviceNonce", C0842m.m1604d());
                    stringBuilder.append("</securityDetails>");
                    stringBuilder.append("</DeviceInterrogationRequest>");
                    stringBuilder.append("</soap:Body>");
                    stringBuilder.append("</soap:Envelope>");
                    return stringBuilder.toString();
                }
            } catch (Exception e2) {
                exception = e2;
                str13 = str14;
                deviceId = str15;
                str15 = str14;
                exception.printStackTrace();
                str = str15;
                str15 = str13;
                str13 = str11;
                str11 = str10;
                str10 = str9;
                str9 = str8;
                str8 = str7;
                str7 = str6;
                str6 = str5;
                str5 = str4;
                str4 = str;
                stringBuilder = new StringBuilder();
                stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append("<soap:Envelope ").append("xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" ").append("xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" ").append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ").append("xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" ").append("xmlns:GMAdapter=\"http://svcs.paypal.com/mobile/adapter/types/pt\" ").append("xmlns:tns1=\"http://svcs.paypal.com/types/common\" ").append("xmlns:jaxb=\"http://java.sun.com/xml/ns/jaxb\" ").append("xmlns:annox=\"http://annox.dev.java.net\" ").append("xmlns:ov=\"http://annox.dev.java.net/net.sf.oval.constraint\" ").append("xsl:version=\"1.0\">").append("<soap:Body>").append("<DeviceInterrogationRequest>");
                C0842m.m1584a(stringBuilder, AlixDefine.VERSION, "2.0");
                C0842m.m1584a(stringBuilder, "paypalAppId", str2);
                C0842m.m1584a(stringBuilder, "mplVersion", str3);
                C0842m.m1584a(stringBuilder, "deviceReferenceToken", C0842m.m1605d("DeviceReferenceToken"));
                stringBuilder.append("<deviceDetails>").append("<deviceId>");
                C0842m.m1584a(stringBuilder, "deviceIdType", str13);
                C0842m.m1584a(stringBuilder, "deviceIdentifier", str6);
                stringBuilder.append("</deviceId>");
                C0842m.m1584a(stringBuilder, "deviceName", str8);
                C0842m.m1584a(stringBuilder, "deviceModel", str7);
                C0842m.m1584a(stringBuilder, "systemName", str9);
                C0842m.m1584a(stringBuilder, "systemVersion", str10);
                C0842m.m1584a(stringBuilder, "deviceCategory", str11);
                C0842m.m1584a(stringBuilder, "deviceSimulator", str5);
                stringBuilder.append("</deviceDetails>");
                stringBuilder.append("<embeddingApplicationDetails>");
                C0842m.m1584a(stringBuilder, "deviceAppId", str14);
                C0842m.m1584a(stringBuilder, "deviceAppName", str15);
                C0842m.m1584a(stringBuilder, "deviceAppDisplayName", str4);
                C0842m.m1584a(stringBuilder, "clientPlatform", deviceId);
                C0842m.m1584a(stringBuilder, "deviceAppVersion", str3);
                stringBuilder.append("</embeddingApplicationDetails>");
                stringBuilder.append("<securityDetails>");
                C0842m.m1584a(stringBuilder, "applicationNonce", C0842m.m1608e());
                C0842m.m1584a(stringBuilder, "deviceNonce", C0842m.m1604d());
                stringBuilder.append("</securityDetails>");
                stringBuilder.append("</DeviceInterrogationRequest>");
                stringBuilder.append("</soap:Body>");
                stringBuilder.append("</soap:Envelope>");
                return stringBuilder.toString();
            }
        } catch (Exception e22) {
            Exception exception2 = e22;
            deviceId = str15;
            str15 = str14;
            str14 = str12;
            exception = exception2;
            exception.printStackTrace();
            str = str15;
            str15 = str13;
            str13 = str11;
            str11 = str10;
            str10 = str9;
            str9 = str8;
            str8 = str7;
            str7 = str6;
            str6 = str5;
            str5 = str4;
            str4 = str;
            stringBuilder = new StringBuilder();
            stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append("<soap:Envelope ").append("xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" ").append("xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" ").append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ").append("xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" ").append("xmlns:GMAdapter=\"http://svcs.paypal.com/mobile/adapter/types/pt\" ").append("xmlns:tns1=\"http://svcs.paypal.com/types/common\" ").append("xmlns:jaxb=\"http://java.sun.com/xml/ns/jaxb\" ").append("xmlns:annox=\"http://annox.dev.java.net\" ").append("xmlns:ov=\"http://annox.dev.java.net/net.sf.oval.constraint\" ").append("xsl:version=\"1.0\">").append("<soap:Body>").append("<DeviceInterrogationRequest>");
            C0842m.m1584a(stringBuilder, AlixDefine.VERSION, "2.0");
            C0842m.m1584a(stringBuilder, "paypalAppId", str2);
            C0842m.m1584a(stringBuilder, "mplVersion", str3);
            C0842m.m1584a(stringBuilder, "deviceReferenceToken", C0842m.m1605d("DeviceReferenceToken"));
            stringBuilder.append("<deviceDetails>").append("<deviceId>");
            C0842m.m1584a(stringBuilder, "deviceIdType", str13);
            C0842m.m1584a(stringBuilder, "deviceIdentifier", str6);
            stringBuilder.append("</deviceId>");
            C0842m.m1584a(stringBuilder, "deviceName", str8);
            C0842m.m1584a(stringBuilder, "deviceModel", str7);
            C0842m.m1584a(stringBuilder, "systemName", str9);
            C0842m.m1584a(stringBuilder, "systemVersion", str10);
            C0842m.m1584a(stringBuilder, "deviceCategory", str11);
            C0842m.m1584a(stringBuilder, "deviceSimulator", str5);
            stringBuilder.append("</deviceDetails>");
            stringBuilder.append("<embeddingApplicationDetails>");
            C0842m.m1584a(stringBuilder, "deviceAppId", str14);
            C0842m.m1584a(stringBuilder, "deviceAppName", str15);
            C0842m.m1584a(stringBuilder, "deviceAppDisplayName", str4);
            C0842m.m1584a(stringBuilder, "clientPlatform", deviceId);
            C0842m.m1584a(stringBuilder, "deviceAppVersion", str3);
            stringBuilder.append("</embeddingApplicationDetails>");
            stringBuilder.append("<securityDetails>");
            C0842m.m1584a(stringBuilder, "applicationNonce", C0842m.m1608e());
            C0842m.m1584a(stringBuilder, "deviceNonce", C0842m.m1604d());
            stringBuilder.append("</securityDetails>");
            stringBuilder.append("</DeviceInterrogationRequest>");
            stringBuilder.append("</soap:Body>");
            stringBuilder.append("</soap:Envelope>");
            return stringBuilder.toString();
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append("<soap:Envelope ").append("xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" ").append("xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" ").append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ").append("xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" ").append("xmlns:GMAdapter=\"http://svcs.paypal.com/mobile/adapter/types/pt\" ").append("xmlns:tns1=\"http://svcs.paypal.com/types/common\" ").append("xmlns:jaxb=\"http://java.sun.com/xml/ns/jaxb\" ").append("xmlns:annox=\"http://annox.dev.java.net\" ").append("xmlns:ov=\"http://annox.dev.java.net/net.sf.oval.constraint\" ").append("xsl:version=\"1.0\">").append("<soap:Body>").append("<DeviceInterrogationRequest>");
        C0842m.m1584a(stringBuilder, AlixDefine.VERSION, "2.0");
        C0842m.m1584a(stringBuilder, "paypalAppId", str2);
        C0842m.m1584a(stringBuilder, "mplVersion", str3);
        C0842m.m1584a(stringBuilder, "deviceReferenceToken", C0842m.m1605d("DeviceReferenceToken"));
        stringBuilder.append("<deviceDetails>").append("<deviceId>");
        C0842m.m1584a(stringBuilder, "deviceIdType", str13);
        C0842m.m1584a(stringBuilder, "deviceIdentifier", str6);
        stringBuilder.append("</deviceId>");
        C0842m.m1584a(stringBuilder, "deviceName", str8);
        C0842m.m1584a(stringBuilder, "deviceModel", str7);
        C0842m.m1584a(stringBuilder, "systemName", str9);
        C0842m.m1584a(stringBuilder, "systemVersion", str10);
        C0842m.m1584a(stringBuilder, "deviceCategory", str11);
        C0842m.m1584a(stringBuilder, "deviceSimulator", str5);
        stringBuilder.append("</deviceDetails>");
        stringBuilder.append("<embeddingApplicationDetails>");
        C0842m.m1584a(stringBuilder, "deviceAppId", str14);
        C0842m.m1584a(stringBuilder, "deviceAppName", str15);
        C0842m.m1584a(stringBuilder, "deviceAppDisplayName", str4);
        C0842m.m1584a(stringBuilder, "clientPlatform", deviceId);
        C0842m.m1584a(stringBuilder, "deviceAppVersion", str3);
        stringBuilder.append("</embeddingApplicationDetails>");
        stringBuilder.append("<securityDetails>");
        C0842m.m1584a(stringBuilder, "applicationNonce", C0842m.m1608e());
        C0842m.m1584a(stringBuilder, "deviceNonce", C0842m.m1604d());
        stringBuilder.append("</securityDetails>");
        stringBuilder.append("</DeviceInterrogationRequest>");
        stringBuilder.append("</soap:Body>");
        stringBuilder.append("</soap:Envelope>");
        return stringBuilder.toString();
    }

    public static String m1581a(Hashtable<String, Object> hashtable) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">\n").append("<soapenv:Body id=\"_0\">\n").append("<ns2:ExecutePaymentRequest xmlns:ns2=\"http://svcs.paypal.com/types/ap\">\n");
        C0842m.m1584a(stringBuilder, "payKey", C1107b.m2377b((String) hashtable.get("PayKey")));
        stringBuilder.append("<requestEnvelope>\n");
        C0842m.m1584a(stringBuilder, "errorLanguage", PayPalActivity._paypal.getLanguage());
        stringBuilder.append("</requestEnvelope>\n");
        C0842m.m1596b(stringBuilder, "actionType", (String) hashtable.get("ActionType"));
        C0842m.m1596b(stringBuilder, "fundingPlanId", (String) hashtable.get("FundingPlanId"));
        stringBuilder.append("</ns2:ExecutePaymentRequest>").append("</soapenv:Body>\n").append("</soapenv:Envelope>");
        return stringBuilder.toString();
    }

    public static String m1582a(Hashtable<String, Object> hashtable, String str) throws C0843n {
        String str2 = (String) hashtable.get("NewPhone");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"").append(" xmlns:pt=\"http://svcs.paypal.com/mobile/adapter/types/pt\">").append("<soapenv:Header/>").append("<soapenv:Body>").append("<pt:DeviceCreatePinRequest>");
        C0842m.m1584a(stringBuilder, "sessionToken", C1107b.m2402n());
        C0842m.m1584a(stringBuilder, AlixDefine.VERSION, Constant.APP_VERSION);
        stringBuilder.append("<phone>");
        C0842m.m1584a(stringBuilder, "countryCode", C1107b.m2386f(str2));
        C0842m.m1584a(stringBuilder, "phoneNumber", C1107b.m2384e(str2));
        stringBuilder.append("</phone>");
        C0842m.m1584a(stringBuilder, "pin", str);
        stringBuilder.append("<securityDetails>");
        C0842m.m1584a(stringBuilder, "applicationNonce", C0842m.m1608e());
        C0842m.m1584a(stringBuilder, "deviceNonce", C0842m.m1604d());
        stringBuilder.append("</securityDetails>");
        stringBuilder.append("</pt:DeviceCreatePinRequest>").append("</soapenv:Body>").append("</soapenv:Envelope>");
        return stringBuilder.toString();
    }

    public static String m1583a(NodeList nodeList) {
        String str = XmlPullParser.NO_NAMESPACE;
        for (int i = 0; i < nodeList.getLength(); i++) {
            str = str + nodeList.item(i).getNodeValue();
        }
        return str;
    }

    protected static StringBuilder m1584a(StringBuilder stringBuilder, String str, String str2) {
        stringBuilder.append("<").append(str).append(">").append(str2).append("</").append(str).append(">");
        return stringBuilder;
    }

    protected static StringBuilder m1585a(StringBuilder stringBuilder, String str, boolean z) {
        stringBuilder.append("<").append(str).append(">").append(z ? "true" : "false").append("</").append(str).append(">");
        return stringBuilder;
    }

    private static void m1586a(String str, String str2) {
        try {
            FileOutputStream openFileOutput = PayPal.getInstance().getParentContext().openFileOutput(str, 2);
            openFileOutput.write(str2.getBytes());
            openFileOutput.flush();
            openFileOutput.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
        }
    }

    private static void m1587a(Hashtable<String, Object> hashtable, Node node, String str) {
        String nodeValue = node.getChildNodes().item(0).getNodeValue();
        hashtable.put(str, nodeValue);
        C0842m.m1586a("DeviceReferenceToken", nodeValue);
    }

    public static void m1588a(Document document) throws C0837f {
        NodeList elementsByTagName = document.getElementsByTagName("securityDetails");
        if (elementsByTagName.getLength() != 1) {
            throw new C0837f("Not exactly one securityDetails tag");
        }
        C0842m.m1589a(elementsByTagName.item(0));
    }

    private static void m1589a(Node node) {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            String nodeName = item.getNodeName();
            if (item.getChildNodes().getLength() > 0) {
                String nodeValue = item.getChildNodes().item(0).getNodeValue();
                if (nodeName.compareTo("applicationNonce") == 0) {
                    C0842m.m1586a("AppNonce", nodeValue);
                }
                if (nodeName.compareTo("deviceNonce") == 0) {
                    C0842m.m1586a("DeviceNonce", nodeValue);
                }
            }
        }
    }

    public static boolean m1590a(String str, Hashtable<String, Object> hashtable) {
        try {
            Document parse = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(str.getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET)));
            NodeList elementsByTagName = parse.getElementsByTagName("responseEnvelope");
            if (elementsByTagName.getLength() == 0) {
                return false;
            }
            Element element = (Element) elementsByTagName.item(0);
            NodeList elementsByTagName2 = element.getElementsByTagName("timestamp");
            if (elementsByTagName2.getLength() == 0) {
                return false;
            }
            hashtable.put("TimeStamp", C0842m.m1583a(((Element) elementsByTagName2.item(0)).getChildNodes()));
            elementsByTagName2 = element.getElementsByTagName("ack");
            if (elementsByTagName2.getLength() == 0) {
                return false;
            }
            hashtable.put("Ack", C0842m.m1583a(((Element) elementsByTagName2.item(0)).getChildNodes()));
            elementsByTagName2 = element.getElementsByTagName("correlationId");
            if (elementsByTagName2.getLength() == 0) {
                return false;
            }
            hashtable.put("CorrelationId", C0842m.m1583a(((Element) elementsByTagName2.item(0)).getChildNodes()));
            elementsByTagName = element.getElementsByTagName("build");
            if (elementsByTagName.getLength() == 0) {
                return false;
            }
            hashtable.put("Build", C0842m.m1583a(((Element) elementsByTagName.item(0)).getChildNodes()));
            elementsByTagName = parse.getElementsByTagName("payKey");
            if (elementsByTagName.getLength() == 0) {
                return false;
            }
            hashtable.put("PayKey", C0842m.m1583a(((Element) elementsByTagName.item(0)).getChildNodes()));
            elementsByTagName = parse.getElementsByTagName("paymentExecStatus");
            if (elementsByTagName.getLength() == 0) {
                return false;
            }
            hashtable.put("PaymentExecStatus", C0842m.m1583a(((Element) elementsByTagName.item(0)).getChildNodes()));
            elementsByTagName = parse.getElementsByTagName("defaultFundingPlan");
            if (elementsByTagName.getLength() != 0) {
                hashtable.put("DefaultFundingPlan", C0823c.m1517a((Element) elementsByTagName.item(0)));
            }
            elementsByTagName = parse.getElementsByTagName("payErrorList");
            return elementsByTagName.getLength() <= 0 || ((Element) elementsByTagName.item(0)).getElementsByTagName("payError").getLength() != 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean m1591a(Document document, Hashtable<String, Object> hashtable) {
        PayPal instance = PayPal.getInstance();
        try {
            NodeList elementsByTagName = document.getElementsByTagName("ns2:DeviceInterrogationResponse");
            for (int i = 0; i < elementsByTagName.getLength(); i++) {
                NodeList childNodes = elementsByTagName.item(i).getChildNodes();
                for (int i2 = 0; i2 < childNodes.getLength(); i2++) {
                    Node item = childNodes.item(i2);
                    String nodeName = item.getNodeName();
                    if (nodeName.equals("payButtonEnable")) {
                        hashtable.put(nodeName, item.getChildNodes().item(0).getNodeValue());
                    } else if (nodeName.equals("deviceReferenceToken")) {
                        C0842m.m1587a((Hashtable) hashtable, item, nodeName);
                    } else if (nodeName.equals("deviceAuthDetails")) {
                        NodeList childNodes2 = item.getChildNodes();
                        for (int i3 = 0; i3 < childNodes2.getLength(); i3++) {
                            Node item2 = childNodes2.item(i3);
                            String nodeName2 = item2.getNodeName();
                            if (item2.getChildNodes().getLength() > 0) {
                                String nodeValue = item2.getChildNodes().item(0).getNodeValue();
                                if (nodeValue == null) {
                                    nodeValue = XmlPullParser.NO_NAMESPACE;
                                }
                                if (nodeName2.equals("userName")) {
                                    instance.setAccountName(nodeValue);
                                } else if (nodeName2.equals("email")) {
                                    instance.setAccountEmail(nodeValue);
                                } else if (nodeName2.equals("phone")) {
                                    NodeList childNodes3 = item2.getChildNodes();
                                    Assert.assertTrue("Bad XML, <phone> doesn't have 2 children", childNodes3.getLength() == 2);
                                    nodeValue = XmlPullParser.NO_NAMESPACE;
                                    NodeList childNodes4 = childNodes3.item(0).getChildNodes();
                                    if (childNodes4.getLength() > 0) {
                                        nodeValue = childNodes4.item(0).getNodeValue();
                                    }
                                    instance.setAccountCountryDialingCode(nodeValue);
                                    nodeValue = XmlPullParser.NO_NAMESPACE;
                                    childNodes3 = childNodes3.item(1).getChildNodes();
                                    if (childNodes3.getLength() > 0) {
                                        nodeValue = childNodes3.item(0).getNodeValue();
                                    }
                                    instance.setAccountPhone("+" + C1107b.m2400m() + nodeValue);
                                } else if (nodeName2.equals("authMethod")) {
                                    int parseInt = Integer.parseInt(nodeValue);
                                    instance.setAuthMethod(parseInt);
                                    instance.setIsRememberMe(parseInt == 2);
                                } else if (nodeName2.equals("authSetting")) {
                                    instance.setAuthSetting(Integer.parseInt(nodeValue));
                                }
                            }
                        }
                    } else if (nodeName.equals("securityDetails")) {
                        C0842m.m1589a(item);
                    }
                }
            }
            return true;
        } catch (Exception e) {
            PayPal.loge("XMLBuilder", "parseAuthenticationRequest caught exception " + e.getMessage());
            return false;
        }
    }

    public static String[] m1592a(String str) {
        Vector vector = new Vector();
        try {
            NodeList elementsByTagName = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(str.getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET))).getElementsByTagName("parameter");
            if (elementsByTagName == null || elementsByTagName.getLength() == 0) {
                return null;
            }
            int i;
            for (i = 0; i < elementsByTagName.getLength(); i++) {
                vector.add(elementsByTagName.item(i).getChildNodes().item(0).getNodeValue());
            }
            String[] strArr = new String[vector.size()];
            for (i = 0; i < vector.size(); i++) {
                strArr[i] = (String) vector.get(i);
            }
            return strArr;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String m1593b() {
        return C0842m.m1605d("DeviceReferenceToken");
    }

    public static String m1594b(String str) {
        String str2 = null;
        try {
            NodeList elementsByTagName = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(str.getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET))).getElementsByTagName("TransactionID");
            if (!(elementsByTagName == null || elementsByTagName.getLength() == 0)) {
                str2 = elementsByTagName.item(0).getChildNodes().item(0).getNodeValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str2;
    }

    public static String m1595b(Hashtable<String, Object> hashtable) {
        String language = PayPalActivity._paypal.getLanguage();
        C0825e c0825e = (C0825e) hashtable.get("ClientDetails");
        String str = (String) hashtable.get("CurrencyCode");
        String str2 = (String) hashtable.get("DateOfMonth");
        String str3 = (String) hashtable.get("DayOfWeek");
        String str4 = (String) hashtable.get("EndingDate");
        String b = C1107b.m2377b((String) hashtable.get("IpnNotificationUrl"));
        String str5 = (String) hashtable.get("MaxAmountPerPayment");
        String str6 = (String) hashtable.get("MaxNumberOfPayments");
        String str7 = (String) hashtable.get("MaxNumberOfPaymentsPerPeriod");
        String str8 = (String) hashtable.get("MaxTotalAmountOfAllPayments");
        String b2 = C1107b.m2377b((String) hashtable.get("Memo"));
        String str9 = (String) hashtable.get("PaymentPeriod");
        String str10 = (String) hashtable.get("PinType");
        String b3 = C1107b.m2377b((String) hashtable.get("SenderEmail"));
        String str11 = "</requestEnvelope>\n";
        str11 = "<cancelUrl>";
        str11 = "https://www.paypal.com";
        str = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">\n<soapenv:Body id=\"_0\">\n<ns2:PreapprovalRequest xmlns:ns2=\"http://svcs.paypal.com/types/ap\">\n<requestEnvelope>\n<errorLanguage>" + language + "</errorLanguage>\n" + r19 + r19 + "https://www.paypal.com" + "</cancelUrl>\n" + "<currencyCode>" + str + "</currencyCode>\n" + "<endingDate>" + str4 + "</endingDate>\n" + "<maxTotalAmountOfAllPayments>" + str8 + "</maxTotalAmountOfAllPayments>\n" + "<returnUrl>" + r17 + "</returnUrl>\n" + "<startingDate>" + ((String) hashtable.get("StartingDate")) + "</startingDate>\n";
        String str12;
        if (c0825e != null) {
            str12 = (str2 != null || str2.length() <= 0) ? str : str + "<dateOfMonth>" + str2 + "</dateOfMonth>\n";
            if (str3 != null && str3.length() > 0) {
                str12 = str12 + "<dayOfWeek>" + str3 + "</dayOfWeek>\n";
            }
            if (b != null && b.length() > 0) {
                str12 = str12 + "<ipnNotificationUrl>" + C1107b.m2377b(b) + "</ipnNotificationUrl>\n";
            }
            if (str5 != null && str5.length() > 0) {
                str12 = str12 + "<maxAmountPerPayment>" + str5 + "</maxAmountPerPayment>\n";
            }
            if (str6 != null && str6.length() > 0) {
                str12 = str12 + "<maxNumberOfPayments>" + str6 + "</maxNumberOfPayments>\n";
            }
            if (str7 != null && str7.length() > 0) {
                str12 = str12 + "<maxNumberOfPaymentsPerPeriod>" + str7 + "</maxNumberOfPaymentsPerPeriod>\n";
            }
            if (b2 != null && b2.length() > 0) {
                str12 = str12 + "<memo>" + C1107b.m2377b(b2) + "</memo>\n";
            }
            if (str9 != null && str9.length() > 0) {
                str12 = str12 + "<paymentPeriod>" + str9 + "</paymentPeriod>\n";
            }
            if (str10 != null && str10.length() > 0) {
                str12 = str12 + "<pinType>" + str10 + "</pinType>\n";
            }
            if (b3 != null && b3.length() > 0) {
                str12 = str12 + "<senderEmail>" + C1107b.m2377b(b3) + "</senderEmail>\n";
            }
            return str12 + "</ns2:PreapprovalRequest></soapenv:Body>\n</soapenv:Envelope>";
        }
        if (str2 != null) {
        }
        str12 = str12 + "<dayOfWeek>" + str3 + "</dayOfWeek>\n";
        str12 = str12 + "<ipnNotificationUrl>" + C1107b.m2377b(b) + "</ipnNotificationUrl>\n";
        str12 = str12 + "<maxAmountPerPayment>" + str5 + "</maxAmountPerPayment>\n";
        str12 = str12 + "<maxNumberOfPayments>" + str6 + "</maxNumberOfPayments>\n";
        str12 = str12 + "<maxNumberOfPaymentsPerPeriod>" + str7 + "</maxNumberOfPaymentsPerPeriod>\n";
        str12 = str12 + "<memo>" + C1107b.m2377b(b2) + "</memo>\n";
        str12 = str12 + "<paymentPeriod>" + str9 + "</paymentPeriod>\n";
        str12 = str12 + "<pinType>" + str10 + "</pinType>\n";
        str12 = str12 + "<senderEmail>" + C1107b.m2377b(b3) + "</senderEmail>\n";
        return str12 + "</ns2:PreapprovalRequest></soapenv:Body>\n</soapenv:Envelope>";
    }

    private static void m1596b(StringBuilder stringBuilder, String str, String str2) {
        if (str2 != null) {
            C0842m.m1584a(stringBuilder, str, str2);
        }
    }

    public static boolean m1597b(String str, Hashtable<String, Object> hashtable) {
        try {
            Document parse = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(str.getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET)));
            NodeList elementsByTagName = parse.getElementsByTagName("responseEnvelope");
            if (elementsByTagName.getLength() == 0) {
                return false;
            }
            Element element = (Element) elementsByTagName.item(0);
            NodeList elementsByTagName2 = element.getElementsByTagName("timestamp");
            if (elementsByTagName2.getLength() == 0) {
                return false;
            }
            hashtable.put("TimeStamp", C0842m.m1583a(((Element) elementsByTagName2.item(0)).getChildNodes()));
            elementsByTagName2 = element.getElementsByTagName("ack");
            if (elementsByTagName2.getLength() == 0) {
                return false;
            }
            hashtable.put("Ack", C0842m.m1583a(((Element) elementsByTagName2.item(0)).getChildNodes()));
            elementsByTagName2 = element.getElementsByTagName("correlationId");
            if (elementsByTagName2.getLength() == 0) {
                return false;
            }
            hashtable.put("CorrelationId", C0842m.m1583a(((Element) elementsByTagName2.item(0)).getChildNodes()));
            elementsByTagName = element.getElementsByTagName("build");
            if (elementsByTagName.getLength() == 0) {
                return false;
            }
            hashtable.put("Build", C0842m.m1583a(((Element) elementsByTagName.item(0)).getChildNodes()));
            elementsByTagName = parse.getElementsByTagName("preapprovalKey");
            if (elementsByTagName.getLength() == 0) {
                return false;
            }
            hashtable.put("PreapprovalKey", C0842m.m1583a(((Element) elementsByTagName.item(0)).getChildNodes()));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean m1598b(Document document) throws C0837f {
        NodeList elementsByTagName = document.getElementsByTagName("securityDetails");
        if (elementsByTagName.getLength() != 1) {
            throw new C0837f(XmlPullParser.NO_NAMESPACE);
        }
        C0842m.m1589a(elementsByTagName.item(0));
        return true;
    }

    public static int m1599c(Document document) {
        try {
            NodeList elementsByTagName = document.getElementsByTagName("ErrorCode");
            if (elementsByTagName == null || elementsByTagName.getLength() == 0) {
                elementsByTagName = document.getElementsByTagName("errorId");
                if (elementsByTagName == null || elementsByTagName.getLength() == 0) {
                    return AsyncTaskManager.REQUEST_SUCCESS_CODE;
                }
            }
            return Integer.parseInt(elementsByTagName.item(0).getChildNodes().item(0).getNodeValue());
        } catch (Exception e) {
            e.printStackTrace();
            return 10004;
        }
    }

    public static String m1600c() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"").append(" xmlns:pt=\"http://svcs.paypal.com/mobile/adapter/types/pt\">").append("<soapenv:Header/>").append("<soapenv:Body>").append("<pt:RemoveDeviceAuthorizationRequest>");
        C0842m.m1584a(stringBuilder, "deviceReferenceToken", C0842m.m1605d("DeviceReferenceToken"));
        stringBuilder.append("<securityDetails>");
        C0842m.m1584a(stringBuilder, "applicationNonce", C0842m.m1608e());
        C0842m.m1584a(stringBuilder, "deviceNonce", C0842m.m1604d());
        stringBuilder.append("</securityDetails>").append("</pt:RemoveDeviceAuthorizationRequest>").append("</soapenv:Body>").append("</soapenv:Envelope>");
        return stringBuilder.toString();
    }

    public static String m1601c(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append("<soap:Envelope ").append("xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" ").append("xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" ").append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ").append("xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" ").append("xmlns:GMAdapter=\"http://svcs.paypal.com/mobile/adapter/types/pt\" ").append("xmlns:tns1=\"http://svcs.paypal.com/types/common\" ").append("xmlns:jaxb=\"http://java.sun.com/xml/ns/jaxb\" ").append("xmlns:annox=\"http://annox.dev.java.net\" ").append("xmlns:ov=\"http://annox.dev.java.net/net.sf.oval.constraint\" ").append("xsl:version=\"1.0\">").append("<soap:Body>").append("<GMAdapter:DeviceAuthenticationRequest>");
        C0842m.m1584a(stringBuilder, AlixDefine.VERSION, Constant.APP_VERSION);
        C0842m.m1584a(stringBuilder, "paypalAppId", C1107b.m2377b(PayPal.getInstance().getAppID()));
        C0842m.m1584a(stringBuilder, "mplVersion", PayPal.getVersionWithoutBuild());
        if (str.length() > 0) {
            stringBuilder.append(str);
        }
        C0842m.m1584a(stringBuilder, "deviceReferenceToken", C0842m.m1605d("DeviceReferenceToken"));
        stringBuilder.append("<securityDetails>");
        C0842m.m1584a(stringBuilder, "applicationNonce", C0842m.m1608e());
        C0842m.m1584a(stringBuilder, "deviceNonce", C0842m.m1604d());
        stringBuilder.append("</securityDetails>");
        stringBuilder.append("</GMAdapter:DeviceAuthenticationRequest>");
        stringBuilder.append("</soap:Body>");
        stringBuilder.append("</soap:Envelope>");
        return stringBuilder.toString();
    }

    public static String m1602c(Hashtable<String, Object> hashtable) {
        String str = (String) hashtable.get("Pin");
        String str2 = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">\n<soapenv:Body id=\"_0\">\n<ns2:ConfirmPreapprovalRequest xmlns:ns2=\"http://svcs.paypal.com/types/ap\">\n<requestEnvelope>\n<errorLanguage>" + PayPalActivity._paypal.getLanguage() + "</errorLanguage>\n" + "</requestEnvelope>\n" + "<preapprovalKey>" + C1107b.m2377b((String) hashtable.get("PreapprovalKey")) + "</preapprovalKey>\n";
        return (str != null ? str2 + "<pin>" + str + "</pin>\n" : str2) + "</ns2:ConfirmPreapprovalRequest></soapenv:Body>\n</soapenv:Envelope>";
    }

    public static boolean m1603c(String str, Hashtable<String, Object> hashtable) {
        try {
            Document parse = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(str.getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET)));
            PayPal instance = PayPal.getInstance();
            PayPalPreapproval preapproval = instance.getPreapproval();
            NodeList elementsByTagName = parse.getElementsByTagName("responseEnvelope");
            if (elementsByTagName.getLength() == 0) {
                return false;
            }
            Element element = (Element) elementsByTagName.item(0);
            NodeList elementsByTagName2 = element.getElementsByTagName("timestamp");
            if (elementsByTagName2.getLength() == 0) {
                return false;
            }
            hashtable.put("TimeStamp", C0842m.m1583a(((Element) elementsByTagName2.item(0)).getChildNodes()));
            elementsByTagName2 = element.getElementsByTagName("ack");
            if (elementsByTagName2.getLength() == 0) {
                return false;
            }
            hashtable.put("Ack", C0842m.m1583a(((Element) elementsByTagName2.item(0)).getChildNodes()));
            elementsByTagName2 = element.getElementsByTagName("correlationId");
            if (elementsByTagName2.getLength() == 0) {
                return false;
            }
            hashtable.put("CorrelationId", C0842m.m1583a(((Element) elementsByTagName2.item(0)).getChildNodes()));
            elementsByTagName = element.getElementsByTagName("build");
            if (elementsByTagName.getLength() == 0) {
                return false;
            }
            hashtable.put("Build", C0842m.m1583a(((Element) elementsByTagName.item(0)).getChildNodes()));
            elementsByTagName = parse.getElementsByTagName("approved");
            if (elementsByTagName.getLength() == 0) {
                return false;
            }
            String a = C0842m.m1583a(((Element) elementsByTagName.item(0)).getChildNodes());
            preapproval.setIsApproved(a.equals("true"));
            hashtable.put("Approved", a);
            elementsByTagName = parse.getElementsByTagName("cancelUrl");
            if (elementsByTagName.getLength() == 0) {
                return false;
            }
            a = C0842m.m1583a(((Element) elementsByTagName.item(0)).getChildNodes());
            instance.setCancelUrl(a);
            hashtable.put("CancelUrl", a);
            elementsByTagName = parse.getElementsByTagName("currencyCode");
            if (elementsByTagName.getLength() == 0) {
                return false;
            }
            a = C0842m.m1583a(((Element) elementsByTagName.item(0)).getChildNodes());
            preapproval.setCurrencyType(a);
            hashtable.put("CurrencyCode", a);
            elementsByTagName = parse.getElementsByTagName("dateOfMonth");
            if (elementsByTagName.getLength() > 0) {
                a = C0842m.m1583a(((Element) elementsByTagName.item(0)).getChildNodes());
                preapproval.setDayOfMonth(Integer.parseInt(a));
                hashtable.put("DateOfMonth", a);
            }
            elementsByTagName = parse.getElementsByTagName("dayOfWeek");
            if (elementsByTagName.getLength() > 0) {
                a = C0842m.m1583a(((Element) elementsByTagName.item(0)).getChildNodes());
                preapproval.setDayOfWeek(preapproval.getDayOfWeekInt(a));
                hashtable.put("DayOfWeek", a);
            }
            elementsByTagName = parse.getElementsByTagName("endingDate");
            if (elementsByTagName.getLength() > 0) {
                a = C0842m.m1583a(((Element) elementsByTagName.item(0)).getChildNodes());
                preapproval.setEndDate(a);
                hashtable.put("EndingDate", a);
            }
            elementsByTagName = parse.getElementsByTagName("ipnNotificationUrl");
            if (elementsByTagName.getLength() > 0) {
                a = C0842m.m1583a(((Element) elementsByTagName.item(0)).getChildNodes());
                preapproval.setIpnUrl(a);
                hashtable.put("IpnNotificationUrl", a);
            }
            elementsByTagName = parse.getElementsByTagName("maxAmountPerPayment");
            if (elementsByTagName.getLength() > 0) {
                a = C0842m.m1583a(((Element) elementsByTagName.item(0)).getChildNodes());
                preapproval.setMaxAmountPerPayment(new BigDecimal(a));
                hashtable.put("MaxAmountPerPayment", a);
            }
            elementsByTagName = parse.getElementsByTagName("maxNumberOfPayments");
            if (elementsByTagName.getLength() > 0) {
                a = C0842m.m1583a(((Element) elementsByTagName.item(0)).getChildNodes());
                preapproval.setMaxNumberOfPayments(Integer.parseInt(a));
                hashtable.put("MaxNumberOfPayments", a);
            }
            elementsByTagName = parse.getElementsByTagName("maxNumberOfPaymentsPerPeriod");
            if (elementsByTagName.getLength() > 0) {
                a = C0842m.m1583a(((Element) elementsByTagName.item(0)).getChildNodes());
                preapproval.setMaxNumberOfPaymentsPerPeriod(Integer.parseInt(a));
                hashtable.put("MaxNumberOfPaymentsPerPeriod", a);
            }
            elementsByTagName = parse.getElementsByTagName("maxTotalAmountOfAllPayments");
            if (elementsByTagName.getLength() == 0) {
                return false;
            }
            a = C0842m.m1583a(((Element) elementsByTagName.item(0)).getChildNodes());
            preapproval.setMaxTotalAmountOfAllPayments(new BigDecimal(a));
            hashtable.put("MaxTotalAmountOfAllPayments", a);
            elementsByTagName = parse.getElementsByTagName("memo");
            if (elementsByTagName.getLength() > 0) {
                a = C0842m.m1583a(((Element) elementsByTagName.item(0)).getChildNodes());
                preapproval.setMemo(a);
                hashtable.put("Memo", a);
            }
            elementsByTagName = parse.getElementsByTagName("paymentPeriod");
            if (elementsByTagName.getLength() > 0) {
                a = C0842m.m1583a(((Element) elementsByTagName.item(0)).getChildNodes());
                preapproval.setPaymentPeriod(preapproval.getPaymentPeriodInt(a));
                hashtable.put("PaymentPeriod", a);
            }
            elementsByTagName = parse.getElementsByTagName("pinType");
            if (elementsByTagName.getLength() > 0) {
                a = C0842m.m1583a(((Element) elementsByTagName.item(0)).getChildNodes());
                preapproval.setPinRequired(a.equals("REQUIRED"));
                hashtable.put("PinType", a);
            }
            elementsByTagName = parse.getElementsByTagName("returnUrl");
            if (elementsByTagName.getLength() == 0) {
                return false;
            }
            a = C0842m.m1583a(((Element) elementsByTagName.item(0)).getChildNodes());
            instance.setReturnUrl(a);
            hashtable.put("ReturnUrl", a);
            elementsByTagName = parse.getElementsByTagName("senderEmail");
            if (elementsByTagName.getLength() > 0) {
                hashtable.put("SenderEmail", C0842m.m1583a(((Element) elementsByTagName.item(0)).getChildNodes()));
            }
            elementsByTagName = parse.getElementsByTagName("startingDate");
            if (elementsByTagName.getLength() == 0) {
                return false;
            }
            a = C0842m.m1583a(((Element) elementsByTagName.item(0)).getChildNodes());
            preapproval.setStartDate(a);
            hashtable.put("StartingDate", a);
            hashtable.put("PreapprovalDetails", preapproval);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String m1604d() {
        String d = C0842m.m1605d("DeviceNonce");
        return d.length() > 0 ? d : "None";
    }

    private static String m1605d(String str) {
        String str2;
        try {
            FileInputStream openFileInput = PayPal.getInstance().getParentContext().openFileInput(str);
            byte[] bArr = new byte[openFileInput.available()];
            openFileInput.read(bArr);
            openFileInput.close();
            str2 = new String(bArr);
        } catch (Exception e) {
            str2 = null;
        }
        return str2 == null ? XmlPullParser.NO_NAMESPACE : str2;
    }

    public static String m1606d(Hashtable<String, Object> hashtable) {
        String language = PayPalActivity._paypal.getLanguage();
        String b = C1107b.m2377b((String) hashtable.get("PreapprovalKey"));
        String b2 = C1107b.m2377b((String) hashtable.get("GetBillingAddress"));
        String str = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">\n<soapenv:Body id=\"_0\">\n<ns2:PreapprovalDetailsRequest xmlns:ns2=\"http://svcs.paypal.com/types/ap\">\n<requestEnvelope>\n<errorLanguage>" + language + "</errorLanguage>\n" + "</requestEnvelope>\n" + "<preapprovalKey>" + b + "</preapprovalKey>\n";
        if (b2 != null) {
            str = str + "<getBillingAddress>" + b2 + "</getBillingAddress>\n";
        }
        return str + "</ns2:PreapprovalDetailsRequest></soapenv:Body>\n</soapenv:Envelope>";
    }

    public static boolean m1607d(String str, Hashtable<String, Object> hashtable) {
        try {
            NodeList elementsByTagName = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(str.getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET))).getElementsByTagName("responseEnvelope");
            if (elementsByTagName.getLength() == 0) {
                return false;
            }
            Element element = (Element) elementsByTagName.item(0);
            NodeList elementsByTagName2 = element.getElementsByTagName("timestamp");
            if (elementsByTagName2.getLength() == 0) {
                return false;
            }
            hashtable.put("TimeStamp", C0842m.m1583a(((Element) elementsByTagName2.item(0)).getChildNodes()));
            elementsByTagName2 = element.getElementsByTagName("ack");
            if (elementsByTagName2.getLength() == 0) {
                return false;
            }
            hashtable.put("Ack", C0842m.m1583a(((Element) elementsByTagName2.item(0)).getChildNodes()));
            elementsByTagName2 = element.getElementsByTagName("correlationId");
            if (elementsByTagName2.getLength() == 0) {
                return false;
            }
            hashtable.put("CorrelationId", C0842m.m1583a(((Element) elementsByTagName2.item(0)).getChildNodes()));
            elementsByTagName = element.getElementsByTagName("build");
            if (elementsByTagName.getLength() == 0) {
                return false;
            }
            hashtable.put("Build", C0842m.m1583a(((Element) elementsByTagName.item(0)).getChildNodes()));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String m1608e() {
        String d = C0842m.m1605d("AppNonce");
        return d.length() > 0 ? d : "None";
    }

    private static String m1609e(String str) throws C0843n {
        int i = 0;
        Assert.assertTrue("phone number must have +, country dialing code, phone number", str.indexOf("+") == 0);
        while (i < f1573a.length) {
            if (str.indexOf(f1573a[i]) >= 0) {
                return str.replace(f1573a[i], XmlPullParser.NO_NAMESPACE);
            }
            i++;
        }
        throw new C0843n("unusable phone number " + str);
    }

    public static String m1610e(Hashtable hashtable) {
        String b = C1107b.m2377b((String) hashtable.get("PayKey"));
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">\n<soapenv:Body id=\"_0\">\n<ns2:GetFundingPlansRequest xmlns:ns2=\"http://svcs.paypal.com/types/ap\">\n<payKey>" + b + "</payKey>\n" + "<requestEnvelope>\n" + "<errorLanguage>" + PayPalActivity._paypal.getLanguage() + "</errorLanguage>\n" + "</requestEnvelope>\n" + "</ns2:GetFundingPlansRequest>" + "</soapenv:Body>\n" + "</soapenv:Envelope>";
    }

    public static boolean m1611e(String str, Hashtable<String, Object> hashtable) {
        try {
            Document parse = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(str.getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET)));
            NodeList elementsByTagName = parse.getElementsByTagName("responseEnvelope");
            if (elementsByTagName.getLength() == 0) {
                return false;
            }
            Element element = (Element) elementsByTagName.item(0);
            NodeList elementsByTagName2 = element.getElementsByTagName("timestamp");
            if (elementsByTagName2.getLength() == 0) {
                return false;
            }
            hashtable.put("TimeStamp", C0842m.m1583a(((Element) elementsByTagName2.item(0)).getChildNodes()));
            elementsByTagName2 = element.getElementsByTagName("ack");
            if (elementsByTagName2.getLength() == 0) {
                return false;
            }
            hashtable.put("Ack", C0842m.m1583a(((Element) elementsByTagName2.item(0)).getChildNodes()));
            elementsByTagName2 = element.getElementsByTagName("correlationId");
            if (elementsByTagName2.getLength() == 0) {
                return false;
            }
            hashtable.put("CorrelationId", C0842m.m1583a(((Element) elementsByTagName2.item(0)).getChildNodes()));
            elementsByTagName = element.getElementsByTagName("build");
            if (elementsByTagName.getLength() == 0) {
                return false;
            }
            hashtable.put("Build", C0842m.m1583a(((Element) elementsByTagName.item(0)).getChildNodes()));
            Vector vector = new Vector();
            NodeList elementsByTagName3 = parse.getElementsByTagName("fundingPlan");
            for (int i = 0; i < elementsByTagName3.getLength(); i++) {
                vector.add(C0823c.m1517a((Element) elementsByTagName3.item(i)));
            }
            hashtable.put("FundingPlans", vector);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String m1612f(String str) throws C0843n {
        int i = 0;
        Assert.assertTrue("phone number must have +, country dialing code, phone number", str.indexOf("+") == 0);
        while (i < f1573a.length) {
            if (str.indexOf(f1573a[i]) >= 0) {
                return f1573a[i].substring(1);
            }
            i++;
        }
        throw new C0843n("unusable phone number " + str);
    }

    public static String m1613f(Hashtable<String, Object> hashtable) throws C0843n {
        String str = (String) hashtable.get("PaymentCurrencyID");
        String language = PayPalActivity._paypal.getLanguage();
        String str2 = (String) hashtable.get("TrackingId");
        String str3 = (String) hashtable.get("ReverseAllParallelPaymentsOnError");
        String str4 = str3 == null ? "true" : str3;
        str3 = (String) hashtable.get("Pin");
        String str5 = "https://www.paypal.com";
        String str6 = "https://www.paypal.com";
        String b = C1107b.m2377b((String) hashtable.get("IpnNotificationUrl"));
        String str7 = (String) hashtable.get("FeesPayer");
        String str8 = (String) hashtable.get("FundingType");
        String b2 = C1107b.m2377b((String) hashtable.get("Memo"));
        C0825e c0825e = (C0825e) hashtable.get("ClientDetails");
        ArrayList arrayList = (ArrayList) hashtable.get("Receivers");
        if (arrayList == null) {
            return null;
        }
        language = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Body>\n<ns2:Pay xmlns:ns2=\"http://svcs.paypal.com/services\">\n<requestEnvelope>\n<errorLanguage>" + language + "</errorLanguage>\n" + "</requestEnvelope>\n" + "<sender>\n" + "<useCredentials>true</useCredentials>\n" + "</sender>\n" + "<actionType>" + ((String) hashtable.get("ActionType")) + "</actionType>\n" + "<cancelUrl>" + str5 + "</cancelUrl>\n" + "<returnUrl>" + str6 + "</returnUrl>\n" + "<currencyCode>" + str + "</currencyCode>\n" + "<receiverList>\n";
        for (int i = 0; i < arrayList.size(); i++) {
            BigDecimal shipping;
            BigDecimal subtotal = ((PayPalReceiverDetails) arrayList.get(i)).getSubtotal();
            if (((PayPalReceiverDetails) arrayList.get(i)).getInvoiceData() != null) {
                shipping = ((PayPalReceiverDetails) arrayList.get(i)).getInvoiceData().getShipping();
                BigDecimal tax = ((PayPalReceiverDetails) arrayList.get(i)).getInvoiceData().getTax();
                BigDecimal add = shipping != null ? subtotal.add(shipping) : subtotal;
                shipping = tax != null ? add.add(tax) : add;
            } else {
                shipping = subtotal;
            }
            shipping.setScale(2, 4);
            subtotal.setScale(2, 4);
            int paymentType = ((PayPalReceiverDetails) arrayList.get(i)).getPaymentType();
            int paymentSubtype = ((PayPalReceiverDetails) arrayList.get(i)).getPaymentSubtype();
            boolean isPrimary = ((PayPalReceiverDetails) arrayList.get(i)).getIsPrimary();
            language = (language + "<receiver>\n") + "<amount>" + shipping.toString() + "</amount>\n";
            str5 = ((PayPalReceiverDetails) arrayList.get(i)).getRecipient();
            if (str5 == null || str5.length() <= 0) {
                return null;
            }
            if (((PayPalReceiverDetails) arrayList.get(i)).isEmailRecipient()) {
                str = language + "<email>" + C1107b.m2377b(str5) + "</email>\n";
            } else if (!((PayPalReceiverDetails) arrayList.get(i)).isPhoneRecipient()) {
                return null;
            } else {
                language = language + "<phone>\n";
                str = C0842m.m1612f(str5);
                if (str == null || str.length() == 0) {
                    str = C1107b.m2400m();
                }
                str = ((language + "<countryCode>" + str + "</countryCode>") + "<phoneNumber>" + C0842m.m1609e(str5) + "</phoneNumber>") + "</phone>\n";
            }
            if (paymentType != 3) {
                str = str + "<paymentType>" + PayPal.getPayType(paymentType) + "</paymentType>\n";
            }
            if (paymentSubtype != 22) {
                str = str + "<paymentSubtype>" + PayPal.getPaySubtype(paymentSubtype) + "</paymentSubtype>\n";
            }
            if (isPrimary) {
                str = str + "<primary>" + isPrimary + "</primary>\n";
            }
            language = str + "</receiver>\n";
        }
        str = language + "</receiverList>\n";
        if (str2 != null && str2.length() > 0) {
            str = str + "<trackingId>" + C1107b.m2377b(str2) + "</trackingId>\n";
        }
        if (str4 != null && str4.length() > 0) {
            str = str + "<reverseAllParallelPaymentsOnError>" + str4 + "</reverseAllParallelPaymentsOnError>\n";
        }
        if (str3 != null && str3.length() > 0) {
            str = str + "<pin>" + str3 + "</pin>\n";
        }
        if (b != null && b.length() > 0) {
            str = str + "<ipnNotificationUrl>" + C1107b.m2377b(b) + "</ipnNotificationUrl>\n";
        }
        if (str7 != null && str7.length() > 0) {
            str = str + "<feesPayer>" + str7 + "</feesPayer>\n";
        }
        if (str8 != null && str8.length() > 0) {
            str = str + "<fundingConstraint>\n<allowedFundingType>\n<fundingTypeInfo>\n<fundingType>" + str8 + "</fundingType>\n" + "</fundingTypeInfo>\n" + "</allowedFundingType>\n" + "</fundingConstraint>\n";
        }
        if (c0825e != null) {
            if (b2 != null && b2.length() > 0) {
                str = str + "<memo>" + C1107b.m2377b(b2) + "</memo>\n";
            }
            str = str + "</ns2:Pay></soapenv:Body>\n</soapenv:Envelope>";
            try {
                str.getBytes(Charset.forName(AsyncHttpResponseHandler.DEFAULT_CHARSET).name());
                return str;
            } catch (IllegalCharsetNameException e) {
                PayPal.loge("XMLBuilder", "Exception " + e.getMessage());
                return str;
            } catch (UnsupportedCharsetException e2) {
                PayPal.loge("XMLBuilder", "Exception " + e2.getMessage());
                return str;
            } catch (UnsupportedEncodingException e3) {
                PayPal.loge("XMLBuilder", "Exception " + e3.getMessage());
                return str;
            }
        }
        str = str + "<memo>" + C1107b.m2377b(b2) + "</memo>\n";
        str = str + "</ns2:Pay></soapenv:Body>\n</soapenv:Envelope>";
        str.getBytes(Charset.forName(AsyncHttpResponseHandler.DEFAULT_CHARSET).name());
        return str;
    }

    public static boolean m1614f(String str, Hashtable<String, Object> hashtable) {
        try {
            NodeList elementsByTagName = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(str.getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET))).getElementsByTagName("MEPRemoveDeviceAuthorizationResponseType");
            for (int i = 0; i < elementsByTagName.getLength(); i++) {
                NodeList childNodes = elementsByTagName.item(i).getChildNodes();
                for (int i2 = 0; i2 < childNodes.getLength(); i2++) {
                    Node item = childNodes.item(i2);
                    String nodeName = item.getNodeName();
                    if (nodeName.compareTo("DeviceReferenceToken") == 0) {
                        C0842m.m1587a((Hashtable) hashtable, item, nodeName);
                    }
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String m1615g(Hashtable<String, Object> hashtable) throws C0843n {
        String str;
        String str2 = (String) hashtable.get("PayKey");
        C0822b c0822b = (C0822b) hashtable.get("DisplayOptions");
        C0824d c0824d = (C0824d) hashtable.get("InstitutionCustomer");
        String str3 = (String) hashtable.get("ShippingAddressId");
        C0829i c0829i = (C0829i) hashtable.get("SenderOptions");
        ArrayList arrayList = (ArrayList) hashtable.get("Receivers");
        str2 = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">\n<soapenv:Body id=\"_0\">\n<ns2:SetPaymentOptionsRequest xmlns:ns2=\"http://svcs.paypal.com/types/ap\">\n<payKey>" + C1107b.m2377b(str2) + "</payKey>\n" + "<requestEnvelope>\n" + "<errorLanguage>" + PayPalActivity._paypal.getLanguage() + "</errorLanguage>\n" + "</requestEnvelope>\n";
        if (c0822b != null) {
            if (c0824d == null) {
                if (str3 != null) {
                    str2 = str2 + "<shippingAddressId>" + C1107b.m2377b(str3) + "</shippingAddressId>\n";
                }
            }
        } else if (c0824d == null) {
            if (str3 != null) {
                str2 = str2 + "<shippingAddressId>" + C1107b.m2377b(str3) + "</shippingAddressId>\n";
            }
        }
        if (str3 != null) {
            str2 = str2 + "<shippingAddressId>" + C1107b.m2377b(str3) + "</shippingAddressId>\n";
        }
        if (c0829i != null) {
            str2 = str2 + "<senderOptions>\n<sharePhoneNumber>" + false + "</sharePhoneNumber>\n" + "<shareAddress>" + false + "</shareAddress>\n" + "<requireShippingAddressSelection>" + false + "</requireShippingAddressSelection>\n" + "</senderOptions>\n";
        }
        if (arrayList != null) {
            int i = 0;
            str = str2;
            while (i < arrayList.size()) {
                PayPalReceiverDetails payPalReceiverDetails = (PayPalReceiverDetails) arrayList.get(i);
                str2 = str + "<receiverOptions>\n";
                if (payPalReceiverDetails.getDescription() != null && payPalReceiverDetails.getDescription().length() > 0) {
                    str2 = str2 + "<description>" + C1107b.m2377b(payPalReceiverDetails.getDescription()) + "</description>\n";
                }
                if (payPalReceiverDetails.getCustomID() != null && payPalReceiverDetails.getCustomID().length() > 0) {
                    str2 = str2 + "<customId>" + C1107b.m2377b(payPalReceiverDetails.getCustomID()) + "</customId>\n";
                }
                if (payPalReceiverDetails.getInvoiceData() != null) {
                    str = str2 + "<invoiceData>\n";
                    for (int i2 = 0; i2 < payPalReceiverDetails.getInvoiceData().getInvoiceItems().size(); i2++) {
                        PayPalInvoiceItem payPalInvoiceItem = (PayPalInvoiceItem) payPalReceiverDetails.getInvoiceData().getInvoiceItems().get(i2);
                        str = str + "<item>\n";
                        if (payPalInvoiceItem.getName() != null && payPalInvoiceItem.getName().length() > 0) {
                            str = str + "<name>" + C1107b.m2377b(payPalInvoiceItem.getName()) + "</name>\n";
                        }
                        if (payPalInvoiceItem.getID() != null && payPalInvoiceItem.getID().length() > 0) {
                            str = str + "<identifier>" + C1107b.m2377b(payPalInvoiceItem.getID()) + "</identifier>\n";
                        }
                        if (payPalInvoiceItem.getTotalPrice() != null && payPalInvoiceItem.getTotalPrice().toString().length() > 0) {
                            str = str + "<price>" + C1107b.m2377b(payPalInvoiceItem.getTotalPrice().toString()) + "</price>\n";
                        }
                        if (payPalInvoiceItem.getUnitPrice() != null && payPalInvoiceItem.getUnitPrice().toString().length() > 0) {
                            str = str + "<itemPrice>" + C1107b.m2377b(payPalInvoiceItem.getUnitPrice().toString()) + "</itemPrice>\n";
                        }
                        if (payPalInvoiceItem.getQuantity() > 0) {
                            str = str + "<itemCount>" + C1107b.m2377b(XmlPullParser.NO_NAMESPACE + payPalInvoiceItem.getQuantity()) + "</itemCount>\n";
                        }
                        str = str + "</item>\n";
                    }
                    if (payPalReceiverDetails.getInvoiceData().getTax() != null && payPalReceiverDetails.getInvoiceData().getTax().compareTo(BigDecimal.ZERO) > 0) {
                        str = str + "<totalTax>" + C1107b.m2377b(payPalReceiverDetails.getInvoiceData().getTax().toString()) + "</totalTax>\n";
                    }
                    if (payPalReceiverDetails.getInvoiceData().getShipping() != null && payPalReceiverDetails.getInvoiceData().getShipping().compareTo(BigDecimal.ZERO) > 0) {
                        str = str + "<totalShipping>" + C1107b.m2377b(payPalReceiverDetails.getInvoiceData().getShipping().toString()) + "</totalShipping>\n";
                    }
                    str2 = str + "</invoiceData>\n";
                }
                str = str2 + "<receiver>\n";
                if (payPalReceiverDetails.isEmailRecipient()) {
                    str2 = str + "<email>" + C1107b.m2377b(payPalReceiverDetails.getRecipient()) + "</email>\n";
                } else if (((PayPalReceiverDetails) arrayList.get(i)).isPhoneRecipient()) {
                    str = str + "<phone>\n";
                    str2 = C0842m.m1612f(payPalReceiverDetails.getRecipient());
                    if (str2 == null || str2.length() == 0) {
                        str2 = C1107b.m2400m();
                    }
                    str2 = ((str + "<countryCode>" + str2 + "</countryCode>") + "<phoneNumber>" + C0842m.m1609e(payPalReceiverDetails.getRecipient()) + "</phoneNumber>") + "</phone>\n";
                } else {
                    str2 = str;
                }
                i++;
                str = str2 + "</receiver>\n</receiverOptions>\n";
            }
        } else {
            str = str2;
        }
        return str + "</ns2:SetPaymentOptionsRequest></soapenv:Body>\n</soapenv:Envelope>";
    }

    public static boolean m1616g(String str, Hashtable<String, Object> hashtable) {
        try {
            Document parse = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(str.getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET)));
            NodeList elementsByTagName = parse.getElementsByTagName("responseEnvelope");
            if (elementsByTagName.getLength() == 0) {
                return false;
            }
            Element element = (Element) elementsByTagName.item(0);
            NodeList elementsByTagName2 = element.getElementsByTagName("timestamp");
            if (elementsByTagName2.getLength() == 0) {
                return false;
            }
            hashtable.put("TimeStamp", C0842m.m1583a(((Element) elementsByTagName2.item(0)).getChildNodes()));
            elementsByTagName2 = element.getElementsByTagName("ack");
            if (elementsByTagName2.getLength() == 0) {
                return false;
            }
            hashtable.put("Ack", C0842m.m1583a(((Element) elementsByTagName2.item(0)).getChildNodes()));
            elementsByTagName2 = element.getElementsByTagName("correlationId");
            if (elementsByTagName2.getLength() == 0) {
                return false;
            }
            hashtable.put("CorrelationId", C0842m.m1583a(((Element) elementsByTagName2.item(0)).getChildNodes()));
            elementsByTagName = element.getElementsByTagName("build");
            if (elementsByTagName.getLength() == 0) {
                return false;
            }
            hashtable.put("Build", C0842m.m1583a(((Element) elementsByTagName.item(0)).getChildNodes()));
            NodeList elementsByTagName3 = parse.getElementsByTagName("availableAddress");
            if (elementsByTagName3.getLength() == 0) {
                return false;
            }
            Vector vector = new Vector();
            for (int i = 0; i < elementsByTagName3.getLength(); i++) {
                C0828h c0828h = new C0828h();
                if (c0828h.m1530a((Element) elementsByTagName3.item(i))) {
                    vector.add(c0828h);
                }
            }
            hashtable.put("AvailableAddresses", vector);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String m1617h(Hashtable<String, Object> hashtable) {
        String b = C1107b.m2377b((String) hashtable.get("PayKey"));
        return "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">\n<soapenv:Body id=\"_0\">\n<ns2:GetAvailableShippingAddressesRequest xmlns:ns2=\"http://svcs.paypal.com/types/ap\">\n<key>" + b + "</key>\n" + "<requestEnvelope>\n" + "<errorLanguage>" + PayPalActivity._paypal.getLanguage() + "</errorLanguage>\n" + "</requestEnvelope>\n" + "</ns2:GetAvailableShippingAddressesRequest>" + "</soapenv:Body>\n" + "</soapenv:Envelope>";
    }
}
