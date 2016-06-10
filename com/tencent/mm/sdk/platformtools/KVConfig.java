package com.tencent.mm.sdk.platformtools;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;

public class KVConfig {
    private static boolean f1637E;

    static {
        f1637E = false;
    }

    private static void m1648a(Map<String, String> map) {
        if (map == null || map.size() <= 0) {
            Log.m1663v("MicroMsg.SDK.KVConfig", "empty values");
            return;
        }
        for (Entry entry : map.entrySet()) {
            Log.m1663v("MicroMsg.SDK.KVConfig", "key=" + ((String) entry.getKey()) + " value=" + ((String) entry.getValue()));
        }
    }

    private static void m1649a(Map<String, String> map, String str, Node node, int i) {
        int i2 = 0;
        if (node.getNodeName().equals("#text")) {
            map.put(str, node.getNodeValue());
        } else if (node.getNodeName().equals("#cdata-section")) {
            map.put(str, node.getNodeValue());
        } else {
            int i3;
            String str2 = str + "." + node.getNodeName() + (i > 0 ? Integer.valueOf(i) : XmlPullParser.NO_NAMESPACE);
            map.put(str2, node.getNodeValue());
            NamedNodeMap attributes = node.getAttributes();
            if (attributes != null) {
                for (i3 = 0; i3 < attributes.getLength(); i3++) {
                    Node item = attributes.item(i3);
                    map.put(str2 + ".$" + item.getNodeName(), item.getNodeValue());
                }
            }
            HashMap hashMap = new HashMap();
            NodeList childNodes = node.getChildNodes();
            while (i2 < childNodes.getLength()) {
                Node item2 = childNodes.item(i2);
                i3 = Util.nullAsNil((Integer) hashMap.get(item2.getNodeName()));
                m1649a(map, str2, item2, i3);
                hashMap.put(item2.getNodeName(), Integer.valueOf(i3 + 1));
                i2++;
            }
        }
    }

    public static Map<String, String> parseIni(String str) {
        if (str == null || str.length() <= 0) {
            return null;
        }
        Map<String, String> hashMap = new HashMap();
        for (String str2 : str.split(SpecilApiUtil.LINE_SEP)) {
            if (str2 != null && str2.length() > 0) {
                String[] split = str2.trim().split("=", 2);
                if (split != null && split.length >= 2) {
                    String str3 = split[0];
                    Object obj = split[1];
                    if (str3 != null && str3.length() > 0 && str3.matches("^[a-zA-Z0-9_]*")) {
                        hashMap.put(str3, obj);
                    }
                }
            }
        }
        if (!f1637E) {
            return hashMap;
        }
        m1648a(hashMap);
        return hashMap;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Map<java.lang.String, java.lang.String> parseXml(java.lang.String r8, java.lang.String r9, java.lang.String r10) {
        /*
        r7 = 1;
        r6 = 0;
        r0 = 0;
        if (r8 != 0) goto L_0x0010;
    L_0x0005:
        r1 = -1;
    L_0x0006:
        if (r1 >= 0) goto L_0x0017;
    L_0x0008:
        r1 = "MicroMsg.SDK.KVConfig";
        r2 = "text not in xml format";
        com.tencent.mm.sdk.platformtools.Log.m1657e(r1, r2);
    L_0x000f:
        return r0;
    L_0x0010:
        r1 = 60;
        r1 = r8.indexOf(r1);
        goto L_0x0006;
    L_0x0017:
        if (r1 <= 0) goto L_0x002c;
    L_0x0019:
        r2 = "MicroMsg.SDK.KVConfig";
        r3 = "fix xml header from + %d";
        r4 = new java.lang.Object[r7];
        r5 = java.lang.Integer.valueOf(r1);
        r4[r6] = r5;
        com.tencent.mm.sdk.platformtools.Log.m1666w(r2, r3, r4);
        r8 = r8.substring(r1);
    L_0x002c:
        if (r8 == 0) goto L_0x000f;
    L_0x002e:
        r1 = r8.length();
        if (r1 <= 0) goto L_0x000f;
    L_0x0034:
        r1 = new java.util.HashMap;
        r1.<init>();
        r2 = javax.xml.parsers.DocumentBuilderFactory.newInstance();
        r2 = r2.newDocumentBuilder();	 Catch:{ ParserConfigurationException -> 0x004b }
        if (r2 != 0) goto L_0x0050;
    L_0x0043:
        r1 = "MicroMsg.SDK.KVConfig";
        r2 = "new Document Builder failed";
        com.tencent.mm.sdk.platformtools.Log.m1657e(r1, r2);
        goto L_0x000f;
    L_0x004b:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x000f;
    L_0x0050:
        r3 = new org.xml.sax.InputSource;	 Catch:{ DOMException -> 0x0074, SAXException -> 0x007a, IOException -> 0x007f, Exception -> 0x0084 }
        r4 = new java.io.ByteArrayInputStream;	 Catch:{ DOMException -> 0x0074, SAXException -> 0x007a, IOException -> 0x007f, Exception -> 0x0084 }
        r5 = r8.getBytes();	 Catch:{ DOMException -> 0x0074, SAXException -> 0x007a, IOException -> 0x007f, Exception -> 0x0084 }
        r4.<init>(r5);	 Catch:{ DOMException -> 0x0074, SAXException -> 0x007a, IOException -> 0x007f, Exception -> 0x0084 }
        r3.<init>(r4);	 Catch:{ DOMException -> 0x0074, SAXException -> 0x007a, IOException -> 0x007f, Exception -> 0x0084 }
        if (r10 == 0) goto L_0x0063;
    L_0x0060:
        r3.setEncoding(r10);	 Catch:{ DOMException -> 0x0074, SAXException -> 0x007a, IOException -> 0x007f, Exception -> 0x0084 }
    L_0x0063:
        r3 = r2.parse(r3);	 Catch:{ DOMException -> 0x0074, SAXException -> 0x007a, IOException -> 0x007f, Exception -> 0x0084 }
        r3.normalize();	 Catch:{ DOMException -> 0x00dd, SAXException -> 0x007a, IOException -> 0x007f, Exception -> 0x0084 }
    L_0x006a:
        if (r3 != 0) goto L_0x0089;
    L_0x006c:
        r1 = "MicroMsg.SDK.KVConfig";
        r2 = "new Document failed";
        com.tencent.mm.sdk.platformtools.Log.m1657e(r1, r2);
        goto L_0x000f;
    L_0x0074:
        r2 = move-exception;
        r3 = r0;
    L_0x0076:
        r2.printStackTrace();
        goto L_0x006a;
    L_0x007a:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x000f;
    L_0x007f:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x000f;
    L_0x0084:
        r1 = move-exception;
        r1.printStackTrace();
        goto L_0x000f;
    L_0x0089:
        r2 = r3.getDocumentElement();
        if (r2 != 0) goto L_0x0098;
    L_0x008f:
        r1 = "MicroMsg.SDK.KVConfig";
        r2 = "getDocumentElement failed";
        com.tencent.mm.sdk.platformtools.Log.m1657e(r1, r2);
        goto L_0x000f;
    L_0x0098:
        if (r9 == 0) goto L_0x00b3;
    L_0x009a:
        r3 = r2.getNodeName();
        r3 = r9.equals(r3);
        if (r3 == 0) goto L_0x00b3;
    L_0x00a4:
        r0 = "";
        m1649a(r1, r0, r2, r6);
    L_0x00a9:
        r0 = f1637E;
        if (r0 == 0) goto L_0x00b0;
    L_0x00ad:
        m1648a(r1);
    L_0x00b0:
        r0 = r1;
        goto L_0x000f;
    L_0x00b3:
        r2 = r2.getElementsByTagName(r9);
        r3 = r2.getLength();
        if (r3 > 0) goto L_0x00c6;
    L_0x00bd:
        r1 = "MicroMsg.SDK.KVConfig";
        r2 = "parse item null";
        com.tencent.mm.sdk.platformtools.Log.m1657e(r1, r2);
        goto L_0x000f;
    L_0x00c6:
        r0 = r2.getLength();
        if (r0 <= r7) goto L_0x00d3;
    L_0x00cc:
        r0 = "MicroMsg.SDK.KVConfig";
        r3 = "parse items more than one";
        com.tencent.mm.sdk.platformtools.Log.m1665w(r0, r3);
    L_0x00d3:
        r0 = "";
        r2 = r2.item(r6);
        m1649a(r1, r0, r2, r6);
        goto L_0x00a9;
    L_0x00dd:
        r2 = move-exception;
        goto L_0x0076;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tencent.mm.sdk.platformtools.KVConfig.parseXml(java.lang.String, java.lang.String, java.lang.String):java.util.Map<java.lang.String, java.lang.String>");
    }
}
