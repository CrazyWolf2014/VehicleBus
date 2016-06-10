package com.tencent.mm.sdk;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.Resolver;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MMSharedPreferences implements SharedPreferences {
    private final String[] columns;
    private final ContentResolver f1621i;
    private final HashMap<String, Object> f1622j;
    private REditor f1623k;

    private static class REditor implements Editor {
        private ContentResolver f1617i;
        private Map<String, Object> f1618l;
        private Set<String> f1619m;
        private boolean f1620n;

        public REditor(ContentResolver contentResolver) {
            this.f1618l = new HashMap();
            this.f1619m = new HashSet();
            this.f1620n = false;
            this.f1617i = contentResolver;
        }

        public void apply() {
        }

        public Editor clear() {
            this.f1620n = true;
            return this;
        }

        public boolean commit() {
            ContentValues contentValues = new ContentValues();
            if (this.f1620n) {
                this.f1617i.delete(SharedPref.CONTENT_URI, null, null);
                this.f1620n = false;
            }
            for (String str : this.f1619m) {
                this.f1617i.delete(SharedPref.CONTENT_URI, "key = ?", new String[]{str});
            }
            for (Entry value : this.f1618l.entrySet()) {
                if (Resolver.unresolveObj(contentValues, value.getValue())) {
                    this.f1617i.update(SharedPref.CONTENT_URI, contentValues, "key = ?", new String[]{(String) ((Entry) r2.next()).getKey()});
                }
            }
            return true;
        }

        public Editor putBoolean(String str, boolean z) {
            this.f1618l.put(str, Boolean.valueOf(z));
            this.f1619m.remove(str);
            return this;
        }

        public Editor putFloat(String str, float f) {
            this.f1618l.put(str, Float.valueOf(f));
            this.f1619m.remove(str);
            return this;
        }

        public Editor putInt(String str, int i) {
            this.f1618l.put(str, Integer.valueOf(i));
            this.f1619m.remove(str);
            return this;
        }

        public Editor putLong(String str, long j) {
            this.f1618l.put(str, Long.valueOf(j));
            this.f1619m.remove(str);
            return this;
        }

        public Editor putString(String str, String str2) {
            this.f1618l.put(str, str2);
            this.f1619m.remove(str);
            return this;
        }

        public Editor putStringSet(String str, Set<String> set) {
            return null;
        }

        public Editor remove(String str) {
            this.f1619m.add(str);
            return this;
        }
    }

    public MMSharedPreferences(Context context) {
        this.columns = new String[]{"_id", SharedPref.KEY, SharedPref.TYPE, SharedPref.VALUE};
        this.f1622j = new HashMap();
        this.f1623k = null;
        this.f1621i = context.getContentResolver();
    }

    private Object getValue(String str) {
        try {
            Cursor query = this.f1621i.query(SharedPref.CONTENT_URI, this.columns, "key = ?", new String[]{str}, null);
            if (query == null) {
                return null;
            }
            Object resolveObj = query.moveToFirst() ? Resolver.resolveObj(query.getInt(query.getColumnIndex(SharedPref.TYPE)), query.getString(query.getColumnIndex(SharedPref.VALUE))) : null;
            query.close();
            return resolveObj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean contains(String str) {
        return getValue(str) != null;
    }

    public Editor edit() {
        if (this.f1623k == null) {
            this.f1623k = new REditor(this.f1621i);
        }
        return this.f1623k;
    }

    public Map<String, ?> getAll() {
        try {
            Cursor query = this.f1621i.query(SharedPref.CONTENT_URI, this.columns, null, null, null);
            if (query == null) {
                return null;
            }
            int columnIndex = query.getColumnIndex(SharedPref.KEY);
            int columnIndex2 = query.getColumnIndex(SharedPref.TYPE);
            int columnIndex3 = query.getColumnIndex(SharedPref.VALUE);
            while (query.moveToNext()) {
                this.f1622j.put(query.getString(columnIndex), Resolver.resolveObj(query.getInt(columnIndex2), query.getString(columnIndex3)));
            }
            query.close();
            return this.f1622j;
        } catch (Exception e) {
            e.printStackTrace();
            return this.f1622j;
        }
    }

    public boolean getBoolean(String str, boolean z) {
        Object value = getValue(str);
        return (value == null || !(value instanceof Boolean)) ? z : ((Boolean) value).booleanValue();
    }

    public float getFloat(String str, float f) {
        Object value = getValue(str);
        return (value == null || !(value instanceof Float)) ? f : ((Float) value).floatValue();
    }

    public int getInt(String str, int i) {
        Object value = getValue(str);
        return (value == null || !(value instanceof Integer)) ? i : ((Integer) value).intValue();
    }

    public long getLong(String str, long j) {
        Object value = getValue(str);
        return (value == null || !(value instanceof Long)) ? j : ((Long) value).longValue();
    }

    public String getString(String str, String str2) {
        Object value = getValue(str);
        return (value == null || !(value instanceof String)) ? str2 : (String) value;
    }

    public Set<String> getStringSet(String str, Set<String> set) {
        return null;
    }

    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
    }

    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
    }
}
