package com.cnlaunch.framework.common.parse;

import android.text.TextUtils;
import com.cnlaunch.framework.common.CacheManager;
import com.cnlaunch.framework.utils.NLog;
import java.util.HashSet;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;
import org.xmlpull.v1.XmlPullParser;

public class SoapManager {
    private static SoapManager instance;
    private final String tag;

    public SoapManager() {
        this.tag = SoapManager.class.getSimpleName();
    }

    public static SoapManager getInstance() {
        if (instance == null) {
            synchronized (SoapManager.class) {
                if (instance == null) {
                    instance = new SoapManager();
                }
            }
        }
        return instance;
    }

    public <T> String soapToJson(SoapObject soapObject, Class<T> clazz) {
        return soapToJson(soapObject, clazz, new String[0]);
    }

    public <T> String soapToJson(SoapObject soapObject, Class<T> clazz, String... listNodeNames) {
        long start = System.currentTimeMillis();
        JSONObject jsonResult = new JSONObject();
        HashSet<String> hashset = new HashSet();
        getSoapresult(soapObject, jsonResult, hashset);
        NLog.m917e(this.tag, "jsonResult: " + jsonResult);
        String json = getSoapJsonResult(jsonResult, hashset, listNodeNames);
        NLog.m917e(this.tag, "soapToJson: " + json.toString());
        long end = System.currentTimeMillis();
        NLog.m917e(this.tag, "soapToJson take time : " + (end - start));
        if (NLog.isDebug) {
            CacheManager.saveTestData(json, clazz.getSimpleName() + ".txt");
        }
        return json;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void getSoapresult(org.ksoap2.serialization.SoapObject r13, org.json.JSONObject r14, java.util.HashSet<java.lang.String> r15) {
        /*
        r12 = this;
        if (r13 == 0) goto L_0x000f;
    L_0x0002:
        r8 = 0;
        r3 = 0;
        r1 = 0;
        r5 = 0;
        r4 = r13.getPropertyCount();	 Catch:{ JSONException -> 0x006a }
        r7 = 0;
        r2 = r1;
        r9 = r8;
    L_0x000d:
        if (r7 < r4) goto L_0x0010;
    L_0x000f:
        return;
    L_0x0010:
        r8 = new org.ksoap2.serialization.PropertyInfo;	 Catch:{ JSONException -> 0x006f }
        r8.<init>();	 Catch:{ JSONException -> 0x006f }
        r13.getPropertyInfo(r7, r8);	 Catch:{ JSONException -> 0x0073 }
        r5 = r8.getName();	 Catch:{ JSONException -> 0x0073 }
        r10 = org.ksoap2.serialization.SoapPrimitive.class;
        r11 = r8.getType();	 Catch:{ JSONException -> 0x0073 }
        if (r10 != r11) goto L_0x0031;
    L_0x0024:
        r10 = r8.getValue();	 Catch:{ JSONException -> 0x0073 }
        r14.put(r5, r10);	 Catch:{ JSONException -> 0x0073 }
        r1 = r2;
    L_0x002c:
        r7 = r7 + 1;
        r2 = r1;
        r9 = r8;
        goto L_0x000d;
    L_0x0031:
        r10 = org.ksoap2.serialization.SoapObject.class;
        r11 = r8.getType();	 Catch:{ JSONException -> 0x0073 }
        if (r10 != r11) goto L_0x0076;
    L_0x0039:
        r1 = new org.json.JSONObject;	 Catch:{ JSONException -> 0x0073 }
        r1.<init>();	 Catch:{ JSONException -> 0x0073 }
        r10 = r13.getProperty(r7);	 Catch:{ JSONException -> 0x006a }
        r0 = r10;
        r0 = (org.ksoap2.serialization.SoapObject) r0;	 Catch:{ JSONException -> 0x006a }
        r3 = r0;
        r10 = r14.has(r5);	 Catch:{ JSONException -> 0x006a }
        if (r10 == 0) goto L_0x0063;
    L_0x004c:
        r15.add(r5);	 Catch:{ JSONException -> 0x006a }
        r10 = new java.lang.StringBuilder;	 Catch:{ JSONException -> 0x006a }
        r11 = java.lang.String.valueOf(r5);	 Catch:{ JSONException -> 0x006a }
        r10.<init>(r11);	 Catch:{ JSONException -> 0x006a }
        r10 = r10.append(r7);	 Catch:{ JSONException -> 0x006a }
        r5 = r10.toString();	 Catch:{ JSONException -> 0x006a }
        r15.add(r5);	 Catch:{ JSONException -> 0x006a }
    L_0x0063:
        r12.getSoapresult(r3, r1, r15);	 Catch:{ JSONException -> 0x006a }
        r14.put(r5, r1);	 Catch:{ JSONException -> 0x006a }
        goto L_0x002c;
    L_0x006a:
        r6 = move-exception;
    L_0x006b:
        r6.printStackTrace();
        goto L_0x000f;
    L_0x006f:
        r6 = move-exception;
        r1 = r2;
        r8 = r9;
        goto L_0x006b;
    L_0x0073:
        r6 = move-exception;
        r1 = r2;
        goto L_0x006b;
    L_0x0076:
        r1 = r2;
        goto L_0x002c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.cnlaunch.framework.common.parse.SoapManager.getSoapresult(org.ksoap2.serialization.SoapObject, org.json.JSONObject, java.util.HashSet):void");
    }

    private String getSoapJsonResult(JSONObject jsonResult, HashSet<String> hashset, String... listNodeNames) {
        try {
            String jsonkey = XmlPullParser.NO_NAMESPACE;
            Object jsonArray = new JSONArray();
            if (!(jsonResult == null || listNodeNames == null || listNodeNames.length <= 0)) {
                Iterator<?> it = new JSONObject(jsonResult.toString()).keys();
                String str = this.tag;
                Object[] objArr = new Object[1];
                objArr[0] = "it=" + it;
                NLog.m916d(str, objArr);
                for (String nodeName : listNodeNames) {
                    while (it.hasNext()) {
                        jsonkey = it.next().toString();
                        String index = jsonkey.replace(nodeName, XmlPullParser.NO_NAMESPACE);
                        str = this.tag;
                        String[] strArr = new Object[1];
                        strArr[0] = "nodeName=" + nodeName + ",jsonKey=" + jsonkey + ",index=" + index;
                        NLog.m916d(str, strArr);
                        if (nodeName.equals(jsonkey)) {
                            boolean flag = true;
                            Object itemObj = (JSONObject) jsonResult.remove(jsonkey);
                            str = this.tag;
                            strArr = new Object[1];
                            strArr[0] = "itemObj=" + itemObj;
                            NLog.m916d(str, strArr);
                            Iterator<?> itemIt = itemObj.keys();
                            str = this.tag;
                            strArr = new Object[1];
                            strArr[0] = "itemIt=" + itemIt;
                            NLog.m916d(str, strArr);
                            while (itemIt.hasNext()) {
                                String itemkey = itemIt.next().toString();
                                str = this.tag;
                                strArr = new Object[1];
                                strArr[0] = "itemkey=" + itemkey;
                                NLog.m916d(str, strArr);
                                if (!hashset.isEmpty() || !itemkey.equals("x431PadSoft")) {
                                    Iterator<String> setIt = hashset.iterator();
                                    while (setIt.hasNext()) {
                                        if (itemkey.equals(((String) setIt.next()).toString())) {
                                            flag = false;
                                            jsonArray.put((JSONObject) itemObj.get(itemkey));
                                            break;
                                        }
                                    }
                                }
                                flag = false;
                                jsonArray.put((JSONObject) itemObj.get(itemkey));
                                break;
                            }
                            if (flag) {
                                jsonArray.put(itemObj);
                            }
                        } else if (jsonkey.indexOf(nodeName) > -1 && !TextUtils.isEmpty(index) && TextUtils.isDigitsOnly(index) && Integer.parseInt(index) > 0) {
                            jsonArray.put((JSONObject) jsonResult.remove(jsonkey));
                        }
                    }
                    jsonResult.put(nodeName, jsonArray);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonResult.toString();
    }
}
