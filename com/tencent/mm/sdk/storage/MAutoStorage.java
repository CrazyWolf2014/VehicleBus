package com.tencent.mm.sdk.storage;

import android.content.ContentValues;
import android.database.Cursor;
import com.tencent.mm.sdk.platformtools.Log;
import com.tencent.mm.sdk.platformtools.Util;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;
import com.tencent.mm.sdk.storage.IAutoDBItem.MAutoDBInfo;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import junit.framework.Assert;
import org.codehaus.jackson.util.MinimalPrettyPrinter;

public abstract class MAutoStorage<T extends IAutoDBItem> extends MStorage {
    private final ISQLiteDatabase bS;
    private final MAutoDBInfo bT;
    private final String bU;

    public MAutoStorage(ISQLiteDatabase iSQLiteDatabase, MAutoDBInfo mAutoDBInfo, String str, String[] strArr) {
        int i = 0;
        this.bS = iSQLiteDatabase;
        this.bT = mAutoDBInfo;
        this.bT.primaryKey = Util.isNullOrNil(this.bT.primaryKey) ? IAutoDBItem.SYSTEM_ROWID_FIELD : this.bT.primaryKey;
        this.bU = str;
        List updateSQLs = getUpdateSQLs(this.bT, getTableName(), this.bS);
        for (int i2 = 0; i2 < updateSQLs.size(); i2++) {
            this.bS.execSQL(this.bU, (String) updateSQLs.get(i2));
        }
        if (strArr != null && strArr.length > 0) {
            while (i < strArr.length) {
                this.bS.execSQL(this.bU, strArr[i]);
                i++;
            }
        }
    }

    private static StringBuilder m2447a(ContentValues contentValues, String... strArr) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : strArr) {
            stringBuilder.append(str + " = ? AND ");
            if (contentValues.get(str) == null) {
                return null;
            }
        }
        stringBuilder.append(" 1=1");
        return stringBuilder;
    }

    private boolean m2448a(ContentValues contentValues) {
        Cursor query = this.bS.query(getTableName(), this.bT.columns, this.bT.primaryKey + " = ?", new String[]{Util.nullAsNil(contentValues.getAsString(this.bT.primaryKey))}, null, null, null);
        boolean checkIOEqual = IAutoDBItem.checkIOEqual(contentValues, query);
        query.close();
        return checkIOEqual;
    }

    private static String[] m2449a(String[] strArr, ContentValues contentValues) {
        String[] strArr2 = new String[strArr.length];
        for (int i = 0; i < strArr2.length; i++) {
            strArr2[i] = Util.nullAsNil(contentValues.getAsString(strArr[i]));
        }
        return strArr2;
    }

    private void m2450f(String str) {
        Log.m1655d("MicroMsg.SDK.MAutoStorage", getTableName() + ":" + str);
    }

    private void m2451g(String str) {
        Log.m1657e("MicroMsg.SDK.MAutoStorage", getTableName() + ":" + str);
    }

    public static String getCreateSQLs(MAutoDBInfo mAutoDBInfo, String str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE IF NOT EXISTS " + str + " ( ");
        stringBuilder.append(mAutoDBInfo.sql);
        stringBuilder.append(");");
        return stringBuilder.toString();
    }

    public static List<String> getUpdateSQLs(MAutoDBInfo mAutoDBInfo, String str, ISQLiteDatabase iSQLiteDatabase) {
        List<String> linkedList = new LinkedList();
        Map hashMap = new HashMap();
        Cursor rawQuery = iSQLiteDatabase.rawQuery("PRAGMA table_info( " + str + " )", new String[0]);
        if (rawQuery == null) {
            return linkedList;
        }
        while (rawQuery.moveToNext()) {
            hashMap.put(rawQuery.getString(rawQuery.getColumnIndex("name")), rawQuery.getString(rawQuery.getColumnIndex(SharedPref.TYPE)));
        }
        rawQuery.close();
        for (Entry entry : mAutoDBInfo.colsMap.entrySet()) {
            String str2 = (String) entry.getValue();
            String str3 = (String) entry.getKey();
            if (str2 != null && str2.length() > 0) {
                String str4 = (String) hashMap.get(str3);
                if (str4 == null) {
                    linkedList.add("ALTER TABLE " + str + " ADD COLUMN " + str3 + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + str2 + ";");
                    hashMap.remove(str3);
                } else if (!str4.equalsIgnoreCase(str2)) {
                    Log.m1657e("MicroMsg.SDK.MAutoStorage", "conflicting alter table on column: " + str3 + ", " + str4 + "<o-n>" + str2);
                    hashMap.remove(str3);
                }
            }
        }
        return linkedList;
    }

    public boolean delete(long j) {
        boolean z = true;
        if (this.bS.delete(getTableName(), "rowid = ?", new String[]{String.valueOf(j)}) <= 0) {
            z = false;
        }
        if (z) {
            notify();
        }
        return z;
    }

    public boolean delete(T t, String... strArr) {
        boolean z = false;
        ContentValues convertTo = t.convertTo();
        if (convertTo == null || convertTo.size() <= 0) {
            m2451g("delete failed, value.size <= 0");
            return false;
        } else if (strArr == null || strArr.length <= 0) {
            m2450f("delete with primary key");
            if (this.bS.delete(getTableName(), this.bT.primaryKey + " = ?", new String[]{Util.nullAsNil(convertTo.getAsString(this.bT.primaryKey))}) > 0) {
                z = true;
            }
            if (!z) {
                return z;
            }
            doNotify();
            return z;
        } else {
            StringBuilder a = m2447a(convertTo, strArr);
            if (a == null) {
                m2451g("delete failed, check keys failed");
                return false;
            } else if (this.bS.delete(getTableName(), a.toString(), m2449a(strArr, convertTo)) > 0) {
                doNotify(this.bT.primaryKey);
                return true;
            } else {
                m2451g("delete failed");
                return false;
            }
        }
    }

    public boolean get(long j, T t) {
        Cursor query = this.bS.query(getTableName(), this.bT.columns, "rowid = ?", new String[]{String.valueOf(j)}, null, null, null);
        if (query.moveToFirst()) {
            t.convertFrom(query);
            query.close();
            return true;
        }
        query.close();
        return false;
    }

    public boolean get(T t, String... strArr) {
        ContentValues convertTo = t.convertTo();
        if (convertTo == null || convertTo.size() <= 0) {
            m2451g("get failed, value.size <= 0");
            return false;
        } else if (strArr == null || strArr.length <= 0) {
            m2450f("get with primary key");
            r0 = this.bS.query(getTableName(), this.bT.columns, this.bT.primaryKey + " = ?", new String[]{Util.nullAsNil(convertTo.getAsString(this.bT.primaryKey))}, null, null, null);
            if (r0.moveToFirst()) {
                t.convertFrom(r0);
                r0.close();
                return true;
            }
            r0.close();
            return false;
        } else {
            StringBuilder a = m2447a(convertTo, strArr);
            if (a == null) {
                m2451g("get failed, check keys failed");
                return false;
            }
            r0 = this.bS.query(getTableName(), this.bT.columns, a.toString(), m2449a(strArr, convertTo), null, null, null);
            if (r0.moveToFirst()) {
                t.convertFrom(r0);
                r0.close();
                return true;
            }
            r0.close();
            m2450f("get failed, not found");
            return false;
        }
    }

    public Cursor getAll() {
        return this.bS.query(getTableName(), this.bT.columns, null, null, null, null, null);
    }

    public int getCount() {
        Cursor rawQuery = rawQuery("select count(*) from " + getTableName(), new String[0]);
        if (rawQuery == null) {
            return 0;
        }
        rawQuery.moveToFirst();
        int i = rawQuery.getInt(0);
        rawQuery.close();
        return i;
    }

    public String getPrimaryKey() {
        return this.bT.primaryKey;
    }

    public String getTableName() {
        return this.bU;
    }

    public boolean insert(T t) {
        ContentValues convertTo = t.convertTo();
        if (convertTo == null || convertTo.size() <= 0) {
            m2451g("insert failed, value.size <= 0");
            return false;
        }
        t.systemRowid = this.bS.insert(getTableName(), this.bT.primaryKey, convertTo);
        if (t.systemRowid <= 0) {
            m2451g("insert failed");
            return false;
        }
        convertTo.put(IAutoDBItem.SYSTEM_ROWID_FIELD, Long.valueOf(t.systemRowid));
        doNotify(convertTo.getAsString(this.bT.primaryKey));
        return true;
    }

    public Cursor rawQuery(String str, String... strArr) {
        return this.bS.rawQuery(str, strArr);
    }

    public boolean replace(T t) {
        Assert.assertTrue("replace primaryKey == null", !Util.isNullOrNil(this.bT.primaryKey));
        ContentValues convertTo = t.convertTo();
        if (convertTo != null) {
            if (convertTo.size() == (convertTo.containsKey(IAutoDBItem.SYSTEM_ROWID_FIELD) ? 1 : 0) + t.getDBInfo().fields.length) {
                if (m2448a(convertTo)) {
                    m2450f("no need replace , fields no change");
                    return true;
                } else if (this.bS.replace(getTableName(), this.bT.primaryKey, convertTo) > 0) {
                    doNotify(this.bT.primaryKey);
                    return true;
                } else {
                    m2451g("replace failed");
                    return false;
                }
            }
        }
        m2451g("replace failed, cv.size() != item.fields().length");
        return false;
    }

    public boolean update(long j, T t) {
        ContentValues convertTo = t.convertTo();
        if (convertTo == null || convertTo.size() <= 0) {
            m2451g("update failed, value.size <= 0");
            return false;
        }
        Cursor query = this.bS.query(getTableName(), this.bT.columns, "rowid = ?", new String[]{String.valueOf(j)}, null, null, null);
        if (IAutoDBItem.checkIOEqual(convertTo, query)) {
            query.close();
            m2450f("no need replace , fields no change");
            return true;
        }
        query.close();
        boolean z = this.bS.update(getTableName(), convertTo, "rowid = ?", new String[]{String.valueOf(j)}) > 0;
        if (!z) {
            return z;
        }
        doNotify();
        return z;
    }

    public boolean update(T t, String... strArr) {
        boolean z = false;
        ContentValues convertTo = t.convertTo();
        if (convertTo == null || convertTo.size() <= 0) {
            m2451g("update failed, value.size <= 0");
            return false;
        } else if (strArr == null || strArr.length <= 0) {
            m2450f("update with primary key");
            if (m2448a(convertTo)) {
                m2450f("no need replace , fields no change");
                return true;
            }
            if (this.bS.update(getTableName(), convertTo, this.bT.primaryKey + " = ?", new String[]{Util.nullAsNil(convertTo.getAsString(this.bT.primaryKey))}) > 0) {
                z = true;
            }
            if (!z) {
                return z;
            }
            doNotify();
            return z;
        } else {
            StringBuilder a = m2447a(convertTo, strArr);
            if (a == null) {
                m2451g("update failed, check keys failed");
                return false;
            } else if (this.bS.update(getTableName(), convertTo, a.toString(), m2449a(strArr, convertTo)) > 0) {
                doNotify(this.bT.primaryKey);
                return true;
            } else {
                m2451g("update failed");
                return false;
            }
        }
    }
}
