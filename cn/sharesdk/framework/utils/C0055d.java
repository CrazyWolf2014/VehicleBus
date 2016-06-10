package cn.sharesdk.framework.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

/* renamed from: cn.sharesdk.framework.utils.d */
public class C0055d {
    private ArrayList<Object> m205a(JSONArray jSONArray) {
        ArrayList<Object> arrayList = new ArrayList();
        int length = jSONArray.length();
        for (int i = 0; i < length; i++) {
            Object opt = jSONArray.opt(i);
            if (opt instanceof JSONObject) {
                opt = m206a((JSONObject) opt);
            } else if (opt instanceof JSONArray) {
                opt = m205a((JSONArray) opt);
            }
            arrayList.add(opt);
        }
        return arrayList;
    }

    private HashMap<String, Object> m206a(JSONObject jSONObject) {
        HashMap<String, Object> hashMap = new HashMap();
        Iterator keys = jSONObject.keys();
        while (keys.hasNext()) {
            String str = (String) keys.next();
            Object opt = jSONObject.opt(str);
            if (JSONObject.NULL.equals(opt)) {
                opt = null;
            }
            if (opt != null) {
                if (opt instanceof JSONObject) {
                    opt = m206a((JSONObject) opt);
                } else if (opt instanceof JSONArray) {
                    opt = m205a((JSONArray) opt);
                }
                hashMap.put(str, opt);
            }
        }
        return hashMap;
    }

    private JSONArray m207a(ArrayList<Object> arrayList) {
        JSONArray jSONArray = new JSONArray();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            Object next = it.next();
            if (next instanceof HashMap) {
                next = m210b((HashMap) next);
            } else if (next instanceof ArrayList) {
                next = m207a((ArrayList) next);
            }
            jSONArray.put(next);
        }
        return jSONArray;
    }

    private boolean m208a(Object obj) {
        return (obj instanceof byte[]) || (obj instanceof short[]) || (obj instanceof int[]) || (obj instanceof long[]) || (obj instanceof float[]) || (obj instanceof double[]) || (obj instanceof char[]) || (obj instanceof boolean[]) || (obj instanceof String[]);
    }

    private ArrayList<?> m209b(Object obj) {
        int i = 0;
        ArrayList<?> arrayList;
        int length;
        if (obj instanceof byte[]) {
            arrayList = new ArrayList();
            byte[] bArr = (byte[]) obj;
            length = bArr.length;
            while (i < length) {
                arrayList.add(Byte.valueOf(bArr[i]));
                i++;
            }
            return arrayList;
        } else if (obj instanceof short[]) {
            arrayList = new ArrayList();
            short[] sArr = (short[]) obj;
            length = sArr.length;
            while (i < length) {
                arrayList.add(Short.valueOf(sArr[i]));
                i++;
            }
            return arrayList;
        } else if (obj instanceof int[]) {
            arrayList = new ArrayList();
            int[] iArr = (int[]) obj;
            length = iArr.length;
            while (i < length) {
                arrayList.add(Integer.valueOf(iArr[i]));
                i++;
            }
            return arrayList;
        } else if (obj instanceof long[]) {
            arrayList = new ArrayList();
            long[] jArr = (long[]) obj;
            length = jArr.length;
            while (i < length) {
                arrayList.add(Long.valueOf(jArr[i]));
                i++;
            }
            return arrayList;
        } else if (obj instanceof float[]) {
            arrayList = new ArrayList();
            float[] fArr = (float[]) obj;
            length = fArr.length;
            while (i < length) {
                arrayList.add(Float.valueOf(fArr[i]));
                i++;
            }
            return arrayList;
        } else if (obj instanceof double[]) {
            arrayList = new ArrayList();
            double[] dArr = (double[]) obj;
            length = dArr.length;
            while (i < length) {
                arrayList.add(Double.valueOf(dArr[i]));
                i++;
            }
            return arrayList;
        } else if (obj instanceof char[]) {
            arrayList = new ArrayList();
            char[] cArr = (char[]) obj;
            length = cArr.length;
            while (i < length) {
                arrayList.add(Character.valueOf(cArr[i]));
                i++;
            }
            return arrayList;
        } else if (obj instanceof boolean[]) {
            arrayList = new ArrayList();
            boolean[] zArr = (boolean[]) obj;
            length = zArr.length;
            while (i < length) {
                arrayList.add(Boolean.valueOf(zArr[i]));
                i++;
            }
            return arrayList;
        } else if (!(obj instanceof String[])) {
            return null;
        } else {
            arrayList = new ArrayList();
            String[] strArr = (String[]) obj;
            length = strArr.length;
            while (i < length) {
                arrayList.add(strArr[i]);
                i++;
            }
            return arrayList;
        }
    }

    private JSONObject m210b(HashMap<String, Object> hashMap) {
        JSONObject jSONObject = new JSONObject();
        for (Entry entry : hashMap.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof HashMap) {
                value = m210b((HashMap) value);
            } else if (value instanceof ArrayList) {
                value = m207a((ArrayList) value);
            } else if (m208a(value)) {
                value = m207a(m209b(value));
            }
            jSONObject.put((String) entry.getKey(), value);
        }
        return jSONObject;
    }

    public String m211a(HashMap<String, Object> hashMap) {
        try {
            return m210b((HashMap) hashMap).toString();
        } catch (Throwable th) {
            C0058e.m220b(th);
            return XmlPullParser.NO_NAMESPACE;
        }
    }

    public HashMap<String, Object> m212a(String str) {
        try {
            if (str.startsWith("[") && str.endsWith("]")) {
                str = "{\"fakelist\":" + str + "}";
            }
            return m206a(new JSONObject(str));
        } catch (Throwable th) {
            C0058e.m220b(th);
            return new HashMap();
        }
    }
}
