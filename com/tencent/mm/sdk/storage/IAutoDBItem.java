package com.tencent.mm.sdk.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import com.tencent.mm.sdk.platformtools.Log;
import com.tencent.mm.sdk.platformtools.Util;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import junit.framework.Assert;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xmlpull.v1.XmlPullParser;

public abstract class IAutoDBItem implements MDBItem {
    public static final String COL_ROWID = "rowid";
    public static final String FIELD_PREFIX = "field_";
    public static final String SYSTEM_ROWID_FIELD = "rowid";
    public long systemRowid;

    public static class MAutoDBInfo {
        public Map<String, String> colsMap;
        public String[] columns;
        public Field[] fields;
        public String primaryKey;
        public String sql;

        public MAutoDBInfo() {
            this.colsMap = new HashMap();
        }
    }

    public IAutoDBItem() {
        this.systemRowid = -1;
    }

    private static String[] m2444a(Field[] fieldArr) {
        String[] strArr = new String[(fieldArr.length + 1)];
        for (int i = 0; i < fieldArr.length; i++) {
            strArr[i] = getColName(fieldArr[i]);
            Assert.assertTrue("getFullColumns failed:" + fieldArr[i].getName(), !Util.isNullOrNil(strArr[i]));
        }
        strArr[fieldArr.length] = SYSTEM_ROWID_FIELD;
        return strArr;
    }

    private static Map<String, String> m2445b(Field[] fieldArr) {
        Map<String, String> hashMap = new HashMap();
        for (Field field : fieldArr) {
            String type = CursorFieldHelper.type(field.getType());
            if (type == null) {
                Log.m1657e("MicroMsg.SDK.IAutoDBItem", "failed identify on column: " + field.getName() + ", skipped");
            } else {
                String colName = getColName(field);
                if (!Util.isNullOrNil(colName)) {
                    hashMap.put(colName, type);
                }
            }
        }
        return hashMap;
    }

    private static String m2446c(Field[] fieldArr) {
        StringBuilder stringBuilder = new StringBuilder();
        int i = 0;
        while (i < fieldArr.length) {
            Field field = fieldArr[i];
            String type = CursorFieldHelper.type(field.getType());
            if (type == null) {
                Log.m1657e("MicroMsg.SDK.IAutoDBItem", "failed identify on column: " + field.getName() + ", skipped");
            } else {
                String colName = getColName(field);
                if (!Util.isNullOrNil(colName)) {
                    String str;
                    int primaryKey;
                    String str2 = XmlPullParser.NO_NAMESPACE;
                    if (field.isAnnotationPresent(MAutoDBFieldAnnotation.class)) {
                        str = " default '" + ((MAutoDBFieldAnnotation) field.getAnnotation(MAutoDBFieldAnnotation.class)).defValue() + "' ";
                        primaryKey = ((MAutoDBFieldAnnotation) field.getAnnotation(MAutoDBFieldAnnotation.class)).primaryKey();
                    } else {
                        str = str2;
                        primaryKey = 0;
                    }
                    stringBuilder.append(colName + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + type + str + (primaryKey == 1 ? " PRIMARY KEY " : XmlPullParser.NO_NAMESPACE));
                    stringBuilder.append(i == fieldArr.length + -1 ? XmlPullParser.NO_NAMESPACE : ", ");
                }
            }
            i++;
        }
        return stringBuilder.toString();
    }

    public static boolean checkIOEqual(ContentValues contentValues, Cursor cursor) {
        if (contentValues == null) {
            return cursor == null;
        } else {
            if (cursor == null || cursor.getCount() != 1) {
                return false;
            }
            cursor.moveToFirst();
            int columnCount = cursor.getColumnCount();
            int size = contentValues.size();
            if (contentValues.containsKey(SYSTEM_ROWID_FIELD)) {
                size--;
            }
            if (cursor.getColumnIndex(SYSTEM_ROWID_FIELD) != -1) {
                columnCount--;
            }
            if (size != columnCount) {
                return false;
            }
            try {
                for (Entry key : contentValues.valueSet()) {
                    String str = (String) key.getKey();
                    if (!str.equals(SYSTEM_ROWID_FIELD)) {
                        columnCount = cursor.getColumnIndex(str);
                        if (columnCount == -1) {
                            return false;
                        }
                        if (contentValues.get(str) instanceof byte[]) {
                            Object obj;
                            byte[] bArr = (byte[]) contentValues.get(str);
                            byte[] blob = cursor.getBlob(columnCount);
                            if (!(bArr == null && blob == null)) {
                                if (bArr == null && blob != null) {
                                    obj = null;
                                    if (obj != null) {
                                        return false;
                                    }
                                } else if (bArr == null || blob != null) {
                                    if (bArr.length != blob.length) {
                                        obj = null;
                                    } else {
                                        for (columnCount = 0; columnCount < bArr.length; columnCount++) {
                                            if (bArr[columnCount] != blob[columnCount]) {
                                                obj = null;
                                                break;
                                            }
                                        }
                                    }
                                    if (obj != null) {
                                        return false;
                                    }
                                } else {
                                    obj = null;
                                    if (obj != null) {
                                        return false;
                                    }
                                }
                            }
                            size = 1;
                            if (obj != null) {
                                return false;
                            }
                        } else if (cursor.getString(columnCount) == null && contentValues.get(str) != null) {
                            return false;
                        } else {
                            if (contentValues.get(str) == null) {
                                return false;
                            }
                            if (!contentValues.get(str).toString().equals(cursor.getString(columnCount))) {
                                return false;
                            }
                        }
                    }
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public static String getColName(Field field) {
        if (field == null) {
            return null;
        }
        String name = field.getName();
        return (name == null || name.length() <= 0) ? null : name.startsWith(FIELD_PREFIX) ? name.substring(6) : name;
    }

    public static Cursor getCursorForProjection(ContentValues contentValues, String[] strArr) {
        Object[] objArr = new Object[strArr.length];
        for (int i = 0; i < objArr.length; i++) {
            objArr[i] = contentValues.get(strArr[i]);
        }
        Cursor matrixCursor = new MatrixCursor(strArr);
        matrixCursor.addRow(objArr);
        return matrixCursor;
    }

    public static Field[] getValidFields(Class<?> cls) {
        return initAutoDBInfo(cls).fields;
    }

    public static MAutoDBInfo initAutoDBInfo(Class<?> cls) {
        MAutoDBInfo mAutoDBInfo = new MAutoDBInfo();
        List linkedList = new LinkedList();
        for (Field field : cls.getDeclaredFields()) {
            int modifiers = field.getModifiers();
            String name = field.getName();
            if (!(name == null || !Modifier.isPublic(modifiers) || Modifier.isFinal(modifiers))) {
                String substring = name.startsWith(FIELD_PREFIX) ? name.substring(6) : name;
                if (field.isAnnotationPresent(MAutoDBFieldAnnotation.class)) {
                    if (((MAutoDBFieldAnnotation) field.getAnnotation(MAutoDBFieldAnnotation.class)).primaryKey() == 1) {
                        mAutoDBInfo.primaryKey = substring;
                    }
                } else if (name.startsWith(FIELD_PREFIX)) {
                }
                if (!Util.isNullOrNil(substring)) {
                    if (substring.equals(SYSTEM_ROWID_FIELD)) {
                        Assert.assertTrue("field_rowid reserved by MAutoDBItem, change now!", false);
                    }
                    linkedList.add(field);
                }
            }
        }
        mAutoDBInfo.fields = (Field[]) linkedList.toArray(new Field[0]);
        mAutoDBInfo.columns = m2444a(mAutoDBInfo.fields);
        mAutoDBInfo.colsMap = m2445b(mAutoDBInfo.fields);
        mAutoDBInfo.sql = m2446c(mAutoDBInfo.fields);
        return mAutoDBInfo;
    }

    public abstract void convertFrom(Cursor cursor);

    public abstract ContentValues convertTo();

    protected abstract MAutoDBInfo getDBInfo();
}
