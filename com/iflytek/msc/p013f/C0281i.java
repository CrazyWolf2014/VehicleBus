package com.iflytek.msc.p013f;

import android.text.TextUtils;
import com.cnlaunch.framework.network.http.SyncHttpTransportSE;
import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechError;
import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.json.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: com.iflytek.msc.f.i */
public class C0281i {
    public static boolean f1049a;

    static {
        f1049a = false;
    }

    private static RecognizerResult m1253a(JSONObject jSONObject) throws JSONException, SpeechError {
        RecognizerResult recognizerResult = new RecognizerResult();
        recognizerResult.confidence = jSONObject.getInt("sc");
        recognizerResult.text = jSONObject.getString("w");
        if (recognizerResult.text.contains("nomatch")) {
            throw new SpeechError(10, SyncHttpTransportSE.DEFAULTTIMEOUT);
        }
        if (jSONObject.has("mn")) {
            JSONArray jSONArray = jSONObject.getJSONArray("mn");
            if (jSONArray != null) {
                recognizerResult.semanteme = C0281i.m1254a(jSONArray);
            }
        }
        return recognizerResult;
    }

    private static ArrayList<HashMap<String, String>> m1254a(JSONArray jSONArray) throws JSONException {
        ArrayList<HashMap<String, String>> arrayList = new ArrayList();
        for (int i = 0; i < jSONArray.length(); i++) {
            arrayList.add(C0281i.m1259b(jSONArray.getJSONObject(i)));
        }
        return arrayList;
    }

    public static ArrayList<RecognizerResult> m1255a(byte[] bArr, String str) throws SpeechError, UnsupportedEncodingException {
        ArrayList arrayList = new ArrayList();
        if (bArr == null) {
            throw new SpeechError(10, SyncHttpTransportSE.DEFAULTTIMEOUT);
        }
        String str2 = new String(bArr, str);
        if (TextUtils.isEmpty(str2)) {
            throw new SpeechError(10, SyncHttpTransportSE.DEFAULTTIMEOUT);
        }
        C0276e.m1220a("json result:" + str2);
        try {
            JSONArray jSONArray = new JSONObject(new JSONTokener(str2)).getJSONArray("ws");
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONArray jSONArray2 = jSONArray.getJSONObject(i).getJSONArray("cw");
                for (int i2 = 0; i2 < jSONArray2.length(); i2++) {
                    C0281i.m1257a(arrayList, C0281i.m1253a(jSONArray2.getJSONObject(i2)));
                }
            }
            if (arrayList.size() > 0) {
                return arrayList;
            }
            throw new SpeechError(10, SyncHttpTransportSE.DEFAULTTIMEOUT);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SpeechError(10, SyncHttpTransportSE.DEFAULTTIMEOUT);
        }
    }

    public static void m1256a(RecognizerResult recognizerResult, byte[] bArr, String str) throws SpeechError, UnsupportedEncodingException {
        if (bArr != null) {
            Object str2 = new String(bArr, str);
            if (!TextUtils.isEmpty(str2)) {
                try {
                    JSONObject jSONObject = new JSONObject(new JSONTokener(str2));
                    if (jSONObject.has("sr")) {
                        JSONArray jSONArray = jSONObject.getJSONArray("sr");
                        if (jSONArray != null) {
                            recognizerResult.semanteme = C0281i.m1254a(jSONArray);
                        }
                    }
                } catch (Exception e) {
                    C0276e.m1222b(e.toString());
                }
            }
        }
    }

    private static void m1257a(ArrayList<RecognizerResult> arrayList, RecognizerResult recognizerResult) {
        if (arrayList.size() == 0) {
            arrayList.add(recognizerResult);
            return;
        }
        for (int i = 0; i < arrayList.size(); i++) {
            if (recognizerResult.confidence > ((RecognizerResult) arrayList.get(i)).confidence) {
                arrayList.add(i, recognizerResult);
                return;
            }
        }
        arrayList.add(recognizerResult);
    }

    public static ArrayList<RecognizerResult> m1258b(byte[] bArr, String str) throws SpeechError, UnsupportedEncodingException {
        ArrayList<RecognizerResult> arrayList = new ArrayList();
        if (bArr == null) {
            throw new SpeechError(10, SyncHttpTransportSE.DEFAULTTIMEOUT);
        }
        String str2 = new String(bArr, str);
        if (TextUtils.isEmpty(str2)) {
            throw new SpeechError(10, SyncHttpTransportSE.DEFAULTTIMEOUT);
        } else if (str2.replace(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, XmlPullParser.NO_NAMESPACE).replace(SpecilApiUtil.LINE_SEP, XmlPullParser.NO_NAMESPACE).toLowerCase().equals("nomatch")) {
            throw new SpeechError(10, SyncHttpTransportSE.DEFAULTTIMEOUT);
        } else {
            String str3 = "confidence";
            String str4 = "contact=";
            String str5 = "input=";
            try {
                String[] split = str2.split(HTTP.CRLF);
                for (String str22 : split) {
                    String[] split2 = str22.split("\t");
                    if (split2.length != 0) {
                        RecognizerResult recognizerResult = new RecognizerResult();
                        for (int i = 0; i < split2.length; i++) {
                            if (-1 != split2[i].indexOf(str3)) {
                                recognizerResult.confidence = Integer.parseInt(split2[i].substring(str3.length() + 1));
                            } else if (-1 != split2[i].indexOf(str4)) {
                                recognizerResult.text = split2[i].substring(str4.length());
                            } else if (-1 != split2[i].indexOf(str5)) {
                                String substring = split2[i].substring(str5.length());
                                HashMap hashMap = new HashMap();
                                hashMap.put(str5, substring);
                                recognizerResult.semanteme.add(hashMap);
                            }
                        }
                        arrayList.add(recognizerResult);
                    }
                }
                if (arrayList.size() > 0) {
                    return arrayList;
                }
                throw new SpeechError(10, SyncHttpTransportSE.DEFAULTTIMEOUT);
            } catch (Exception e) {
                e.printStackTrace();
                throw new SpeechError(10, SyncHttpTransportSE.DEFAULTTIMEOUT);
            }
        }
    }

    private static HashMap<String, String> m1259b(JSONObject jSONObject) throws JSONException {
        Iterator keys = jSONObject.keys();
        HashMap<String, String> hashMap = new HashMap();
        while (keys.hasNext()) {
            String str = (String) keys.next();
            hashMap.put(str, jSONObject.getString(str));
        }
        return hashMap;
    }

    private static String m1260c(JSONObject jSONObject) throws JSONException {
        String str = XmlPullParser.NO_NAMESPACE;
        f1049a = jSONObject.getBoolean("ls");
        if (!jSONObject.has("ws")) {
            return XmlPullParser.NO_NAMESPACE;
        }
        JSONArray jSONArray = jSONObject.getJSONArray("ws");
        for (int i = 0; i < jSONArray.length(); i++) {
            JSONArray jSONArray2 = jSONArray.getJSONObject(i).getJSONArray("cw");
            if (jSONArray2.length() > 0) {
                str = str + jSONArray2.getJSONObject(0).getString("w");
            }
        }
        return str;
    }

    public static String m1261c(byte[] bArr, String str) throws SpeechError, UnsupportedEncodingException {
        int i = 0;
        if (bArr == null) {
            return XmlPullParser.NO_NAMESPACE;
        }
        Object str2 = new String(bArr, str);
        f1049a = false;
        C0276e.m1220a("json result:" + str2);
        if (TextUtils.isEmpty(str2)) {
            return XmlPullParser.NO_NAMESPACE;
        }
        String str3 = XmlPullParser.NO_NAMESPACE;
        try {
            JSONObject jSONObject = new JSONObject(new JSONTokener(str2));
            str3 = str3 + C0281i.m1260c(jSONObject);
            if (!jSONObject.has("fs")) {
                return str3;
            }
            JSONArray jSONArray = jSONObject.getJSONArray("fs");
            while (i < jSONArray.length()) {
                String str4 = str3 + C0281i.m1260c(jSONArray.getJSONObject(i));
                i++;
                str3 = str4;
            }
            return str3;
        } catch (Exception e) {
            C0276e.m1222b("can't parse json result");
            return str3;
        }
    }
}
