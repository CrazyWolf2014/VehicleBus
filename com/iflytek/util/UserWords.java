package com.iflytek.util;

import android.text.TextUtils;
import com.iflytek.msc.p013f.C0276e;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class UserWords {
    private Hashtable<String, ArrayList<String>> f1197a;

    public UserWords() {
        this.f1197a = null;
        this.f1197a = new Hashtable();
    }

    public UserWords(String str) {
        this.f1197a = null;
        this.f1197a = new Hashtable();
        m1436a(str);
    }

    private String m1435a() {
        try {
            if (this.f1197a == null) {
                C0276e.m1220a("mwords is null...");
                return null;
            }
            JSONObject jSONObject = new JSONObject();
            Object jSONArray = new JSONArray();
            for (Entry entry : this.f1197a.entrySet()) {
                Object jSONObject2 = new JSONObject();
                Object jSONArray2 = new JSONArray();
                Iterator it = ((ArrayList) entry.getValue()).iterator();
                while (it.hasNext()) {
                    jSONArray2.put((String) it.next());
                }
                jSONObject2.put("name", entry.getKey());
                jSONObject2.put("words", jSONArray2);
                jSONArray.put(jSONObject2);
            }
            jSONObject.put("userword", jSONArray);
            return jSONObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void m1436a(String str) {
        try {
            if (TextUtils.isEmpty(str)) {
                C0276e.m1220a("userword is null...");
                return;
            }
            JSONArray jSONArray = new JSONObject(new JSONTokener(str)).getJSONArray("userword");
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObject = (JSONObject) jSONArray.get(i);
                JSONArray jSONArray2 = jSONObject.getJSONArray("words");
                ArrayList arrayList = new ArrayList();
                for (int i2 = 0; i2 < jSONArray2.length(); i2++) {
                    String obj = jSONArray2.get(i2).toString();
                    if (!(arrayList == null || arrayList.contains(obj))) {
                        arrayList.add(obj);
                    }
                }
                this.f1197a.put(jSONObject.get("name").toString(), arrayList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean m1437a(ArrayList<String> arrayList, String str) {
        if (arrayList == null || arrayList.contains(str)) {
            return false;
        }
        arrayList.add(str);
        return true;
    }

    private boolean m1438a(ArrayList<String> arrayList, ArrayList<String> arrayList2) {
        if (arrayList == null || arrayList2 == null) {
            return false;
        }
        Iterator it = arrayList2.iterator();
        while (it.hasNext()) {
            m1437a((ArrayList) arrayList, (String) it.next());
        }
        return true;
    }

    public ArrayList<String> getKeys() {
        if (this.f1197a == null || this.f1197a.isEmpty()) {
            return null;
        }
        ArrayList<String> arrayList = new ArrayList();
        for (Object add : this.f1197a.keySet()) {
            arrayList.add(add);
        }
        return arrayList;
    }

    public ArrayList<String> getWords() {
        return getWords("default");
    }

    public ArrayList<String> getWords(String str) {
        return (ArrayList) this.f1197a.get(str);
    }

    public boolean hasKey(String str) {
        return this.f1197a.containsKey(str);
    }

    public boolean putWord(String str) {
        return putWord("default", str);
    }

    public boolean putWord(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return false;
        }
        ArrayList words = getWords(str);
        if (words != null) {
            m1437a(words, str2);
        } else {
            words = new ArrayList();
            m1437a(words, str2);
            this.f1197a.put(str, words);
        }
        return true;
    }

    public boolean putWords(String str, ArrayList<String> arrayList) {
        if (TextUtils.isEmpty(str) || arrayList == null || arrayList.size() <= 0) {
            return false;
        }
        ArrayList words = getWords(str);
        if (words != null) {
            m1438a(words, (ArrayList) arrayList);
        } else {
            m1438a(new ArrayList(), (ArrayList) arrayList);
            this.f1197a.put(str, arrayList);
        }
        return true;
    }

    public boolean putWords(ArrayList<String> arrayList) {
        return putWords("default", arrayList);
    }

    public String toString() {
        return m1435a();
    }
}
