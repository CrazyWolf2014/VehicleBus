package com.ifoer.pro.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.ifoer.db.DBHelper;

public class DBDao extends com.ifoer.db.DBDao {
    private static final String CAR_ICON = "car_icon";
    private static final String CAR_VERSION = "car_version";
    private static final boolean f2140D = false;
    public static final String OPERATING_RECORD_TABLE = "operating_record_table";
    private static final String UPGRADE = "upgrade";
    private static DBDao dao;
    private Context context;

    static {
        dao = null;
    }

    public DBDao(Context context) {
        this.context = context;
    }

    public static DBDao getInstance(Context context) {
        if (dao == null) {
            dao = new DBDao(context);
        }
        return dao;
    }

    public SQLiteDatabase getConnection() {
        return new DBHelper(this.context).getWritableDatabase();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.util.List<java.util.HashMap<java.lang.String, java.lang.Object>> query(java.lang.String r17, java.lang.String r18, java.lang.String r19, android.database.sqlite.SQLiteDatabase r20) {
        /*
        r16 = this;
        monitor-enter(r16);
        r8 = new java.util.ArrayList;	 Catch:{ all -> 0x015c }
        r8.<init>();	 Catch:{ all -> 0x015c }
        r9 = new java.util.ArrayList;	 Catch:{ all -> 0x015c }
        r9.<init>();	 Catch:{ all -> 0x015c }
        r1 = 0;
        r3 = 0;
        r4 = 0;
        r14 = 2;
        r5 = new android.database.Cursor[r14];	 Catch:{ all -> 0x015c }
        r12 = "";
        r14 = java.util.Locale.getDefault();	 Catch:{ Exception -> 0x0151 }
        r14 = r14.getLanguage();	 Catch:{ Exception -> 0x0151 }
        r14 = r14.toUpperCase();	 Catch:{ Exception -> 0x0151 }
        r15 = "ZH";
        r14 = r14.equals(r15);	 Catch:{ Exception -> 0x0151 }
        if (r14 != 0) goto L_0x003b;
    L_0x0027:
        r14 = java.util.Locale.getDefault();	 Catch:{ Exception -> 0x0151 }
        r14 = r14.getLanguage();	 Catch:{ Exception -> 0x0151 }
        r14 = r14.toUpperCase();	 Catch:{ Exception -> 0x0151 }
        r15 = "CN";
        r14 = r14.equals(r15);	 Catch:{ Exception -> 0x0151 }
        if (r14 == 0) goto L_0x009c;
    L_0x003b:
        r14 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0151 }
        r15 = "select car_icon.*,(select count(*) from car_version where car_version.carId=car_icon.softPackageId COLLATE NOCASE and serialNo=";
        r14.<init>(r15);	 Catch:{ Exception -> 0x0151 }
        r0 = r18;
        r14 = r14.append(r0);	 Catch:{ Exception -> 0x0151 }
        r15 = " and lanName='";
        r14 = r14.append(r15);	 Catch:{ Exception -> 0x0151 }
        r0 = r19;
        r14 = r14.append(r0);	 Catch:{ Exception -> 0x0151 }
        r15 = "') flag from ";
        r14 = r14.append(r15);	 Catch:{ Exception -> 0x0151 }
        r15 = "car_icon";
        r14 = r14.append(r15);	 Catch:{ Exception -> 0x0151 }
        r15 = " where areaId=? order by car_icon.szhName asc";
        r14 = r14.append(r15);	 Catch:{ Exception -> 0x0151 }
        r12 = r14.toString();	 Catch:{ Exception -> 0x0151 }
        r14 = 1;
        r14 = new java.lang.String[r14];	 Catch:{ Exception -> 0x0151 }
        r15 = 0;
        r14[r15] = r17;	 Catch:{ Exception -> 0x0151 }
        r0 = r20;
        r3 = r0.rawQuery(r12, r14);	 Catch:{ Exception -> 0x0151 }
        r14 = 0;
        r5[r14] = r3;	 Catch:{ Exception -> 0x0151 }
    L_0x0079:
        r2 = new android.database.MergeCursor;	 Catch:{ Exception -> 0x0151 }
        r2.<init>(r5);	 Catch:{ Exception -> 0x0151 }
        r2.moveToFirst();	 Catch:{ Exception -> 0x022f, all -> 0x0237 }
    L_0x0081:
        r14 = r2.isAfterLast();	 Catch:{ Exception -> 0x022f, all -> 0x0237 }
        if (r14 == 0) goto L_0x0192;
    L_0x0087:
        r14 = r9.size();	 Catch:{ Exception -> 0x022f, all -> 0x0237 }
        if (r14 <= 0) goto L_0x0094;
    L_0x008d:
        r7 = 0;
    L_0x008e:
        r14 = r9.size();	 Catch:{ Exception -> 0x022f, all -> 0x0237 }
        if (r7 < r14) goto L_0x023b;
    L_0x0094:
        if (r2 == 0) goto L_0x0099;
    L_0x0096:
        r2.close();	 Catch:{ all -> 0x015c }
    L_0x0099:
        r1 = r2;
    L_0x009a:
        monitor-exit(r16);
        return r8;
    L_0x009c:
        r13 = "";
        r14 = "EN";
        r0 = r19;
        r14 = r0.equals(r14);	 Catch:{ Exception -> 0x0151 }
        if (r14 != 0) goto L_0x00aa;
    L_0x00a8:
        r13 = " and flag>0";
    L_0x00aa:
        r14 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0151 }
        r15 = "select car_icon.*,(select count(*) from car_version where car_version.carId=car_icon.softPackageId COLLATE NOCASE and serialNo=";
        r14.<init>(r15);	 Catch:{ Exception -> 0x0151 }
        r0 = r18;
        r14 = r14.append(r0);	 Catch:{ Exception -> 0x0151 }
        r15 = " and lanName='";
        r14 = r14.append(r15);	 Catch:{ Exception -> 0x0151 }
        r0 = r19;
        r14 = r14.append(r0);	 Catch:{ Exception -> 0x0151 }
        r15 = "') flag from ";
        r14 = r14.append(r15);	 Catch:{ Exception -> 0x0151 }
        r15 = "car_icon";
        r14 = r14.append(r15);	 Catch:{ Exception -> 0x0151 }
        r15 = " where areaId=?";
        r14 = r14.append(r15);	 Catch:{ Exception -> 0x0151 }
        r14 = r14.append(r13);	 Catch:{ Exception -> 0x0151 }
        r15 = " order by car_icon.senName asc";
        r14 = r14.append(r15);	 Catch:{ Exception -> 0x0151 }
        r12 = r14.toString();	 Catch:{ Exception -> 0x0151 }
        r14 = 1;
        r14 = new java.lang.String[r14];	 Catch:{ Exception -> 0x0151 }
        r15 = 0;
        r14[r15] = r17;	 Catch:{ Exception -> 0x0151 }
        r0 = r20;
        r3 = r0.rawQuery(r12, r14);	 Catch:{ Exception -> 0x0151 }
        r14 = 0;
        r5[r14] = r3;	 Catch:{ Exception -> 0x0151 }
        r14 = "EN";
        r0 = r19;
        r14 = r0.equals(r14);	 Catch:{ Exception -> 0x0151 }
        if (r14 != 0) goto L_0x0079;
    L_0x00fc:
        r3.moveToFirst();	 Catch:{ Exception -> 0x0151 }
        r11 = "";
    L_0x0101:
        r14 = r3.isAfterLast();	 Catch:{ Exception -> 0x0151 }
        if (r14 == 0) goto L_0x015f;
    L_0x0107:
        r3.moveToFirst();	 Catch:{ Exception -> 0x0151 }
        r14 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0151 }
        r15 = "select car_icon.*,(select count(*) from car_version where car_version.carId=car_icon.softPackageId COLLATE NOCASE and serialNo=";
        r14.<init>(r15);	 Catch:{ Exception -> 0x0151 }
        r0 = r18;
        r14 = r14.append(r0);	 Catch:{ Exception -> 0x0151 }
        r15 = " and lanName='EN'";
        r14 = r14.append(r15);	 Catch:{ Exception -> 0x0151 }
        r15 = ") flag from ";
        r14 = r14.append(r15);	 Catch:{ Exception -> 0x0151 }
        r15 = "car_icon";
        r14 = r14.append(r15);	 Catch:{ Exception -> 0x0151 }
        r15 = " where areaId=";
        r14 = r14.append(r15);	 Catch:{ Exception -> 0x0151 }
        r0 = r17;
        r14 = r14.append(r0);	 Catch:{ Exception -> 0x0151 }
        r14 = r14.append(r11);	 Catch:{ Exception -> 0x0151 }
        r15 = " order by car_icon.senName asc";
        r14 = r14.append(r15);	 Catch:{ Exception -> 0x0151 }
        r12 = r14.toString();	 Catch:{ Exception -> 0x0151 }
        r14 = 0;
        r14 = new java.lang.String[r14];	 Catch:{ Exception -> 0x0151 }
        r0 = r20;
        r4 = r0.rawQuery(r12, r14);	 Catch:{ Exception -> 0x0151 }
        r14 = 1;
        r5[r14] = r4;	 Catch:{ Exception -> 0x0151 }
        goto L_0x0079;
    L_0x0151:
        r6 = move-exception;
    L_0x0152:
        r6.printStackTrace();	 Catch:{ all -> 0x018b }
        if (r1 == 0) goto L_0x009a;
    L_0x0157:
        r1.close();	 Catch:{ all -> 0x015c }
        goto L_0x009a;
    L_0x015c:
        r14 = move-exception;
        monitor-exit(r16);
        throw r14;
    L_0x015f:
        r14 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0151 }
        r15 = java.lang.String.valueOf(r11);	 Catch:{ Exception -> 0x0151 }
        r14.<init>(r15);	 Catch:{ Exception -> 0x0151 }
        r15 = " and car_icon.softPackageId <> '";
        r14 = r14.append(r15);	 Catch:{ Exception -> 0x0151 }
        r15 = "softPackageId";
        r15 = r3.getColumnIndex(r15);	 Catch:{ Exception -> 0x0151 }
        r15 = r3.getString(r15);	 Catch:{ Exception -> 0x0151 }
        r14 = r14.append(r15);	 Catch:{ Exception -> 0x0151 }
        r15 = "'";
        r14 = r14.append(r15);	 Catch:{ Exception -> 0x0151 }
        r11 = r14.toString();	 Catch:{ Exception -> 0x0151 }
        r3.moveToNext();	 Catch:{ Exception -> 0x0151 }
        goto L_0x0101;
    L_0x018b:
        r14 = move-exception;
    L_0x018c:
        if (r1 == 0) goto L_0x0191;
    L_0x018e:
        r1.close();	 Catch:{ all -> 0x015c }
    L_0x0191:
        throw r14;	 Catch:{ all -> 0x015c }
    L_0x0192:
        r10 = new java.util.HashMap;	 Catch:{ Exception -> 0x022f, all -> 0x0237 }
        r10.<init>();	 Catch:{ Exception -> 0x022f, all -> 0x0237 }
        r14 = "softPackageId";
        r15 = "softPackageId";
        r15 = r2.getColumnIndex(r15);	 Catch:{ Exception -> 0x022f, all -> 0x0237 }
        r15 = r2.getString(r15);	 Catch:{ Exception -> 0x022f, all -> 0x0237 }
        r10.put(r14, r15);	 Catch:{ Exception -> 0x022f, all -> 0x0237 }
        r14 = "softId";
        r15 = "softId";
        r15 = r2.getColumnIndex(r15);	 Catch:{ Exception -> 0x022f, all -> 0x0237 }
        r15 = r2.getInt(r15);	 Catch:{ Exception -> 0x022f, all -> 0x0237 }
        r15 = java.lang.Integer.valueOf(r15);	 Catch:{ Exception -> 0x022f, all -> 0x0237 }
        r10.put(r14, r15);	 Catch:{ Exception -> 0x022f, all -> 0x0237 }
        r14 = "name_zh";
        r15 = "zhName";
        r15 = r2.getColumnIndex(r15);	 Catch:{ Exception -> 0x022f, all -> 0x0237 }
        r15 = r2.getString(r15);	 Catch:{ Exception -> 0x022f, all -> 0x0237 }
        r10.put(r14, r15);	 Catch:{ Exception -> 0x022f, all -> 0x0237 }
        r14 = "name";
        r15 = "enName";
        r15 = r2.getColumnIndex(r15);	 Catch:{ Exception -> 0x022f, all -> 0x0237 }
        r15 = r2.getString(r15);	 Catch:{ Exception -> 0x022f, all -> 0x0237 }
        r10.put(r14, r15);	 Catch:{ Exception -> 0x022f, all -> 0x0237 }
        r14 = "icon";
        r15 = "icon";
        r15 = r2.getColumnIndex(r15);	 Catch:{ Exception -> 0x022f, all -> 0x0237 }
        r15 = r2.getString(r15);	 Catch:{ Exception -> 0x022f, all -> 0x0237 }
        r10.put(r14, r15);	 Catch:{ Exception -> 0x022f, all -> 0x0237 }
        r14 = "iconPath";
        r15 = "iconPath";
        r15 = r2.getColumnIndex(r15);	 Catch:{ Exception -> 0x022f, all -> 0x0237 }
        r15 = r2.getString(r15);	 Catch:{ Exception -> 0x022f, all -> 0x0237 }
        r10.put(r14, r15);	 Catch:{ Exception -> 0x022f, all -> 0x0237 }
        r14 = "areaId";
        r15 = "areaId";
        r15 = r2.getColumnIndex(r15);	 Catch:{ Exception -> 0x022f, all -> 0x0237 }
        r15 = r2.getInt(r15);	 Catch:{ Exception -> 0x022f, all -> 0x0237 }
        r15 = java.lang.Integer.valueOf(r15);	 Catch:{ Exception -> 0x022f, all -> 0x0237 }
        r10.put(r14, r15);	 Catch:{ Exception -> 0x022f, all -> 0x0237 }
        r14 = "flag";
        r15 = "flag";
        r15 = r2.getColumnIndex(r15);	 Catch:{ Exception -> 0x022f, all -> 0x0237 }
        r15 = r2.getInt(r15);	 Catch:{ Exception -> 0x022f, all -> 0x0237 }
        r15 = java.lang.Integer.valueOf(r15);	 Catch:{ Exception -> 0x022f, all -> 0x0237 }
        r10.put(r14, r15);	 Catch:{ Exception -> 0x022f, all -> 0x0237 }
        r14 = "flag";
        r14 = r2.getColumnIndex(r14);	 Catch:{ Exception -> 0x022f, all -> 0x0237 }
        r14 = r2.getInt(r14);	 Catch:{ Exception -> 0x022f, all -> 0x0237 }
        if (r14 <= 0) goto L_0x0233;
    L_0x0227:
        r9.add(r10);	 Catch:{ Exception -> 0x022f, all -> 0x0237 }
    L_0x022a:
        r2.moveToNext();	 Catch:{ Exception -> 0x022f, all -> 0x0237 }
        goto L_0x0081;
    L_0x022f:
        r6 = move-exception;
        r1 = r2;
        goto L_0x0152;
    L_0x0233:
        r8.add(r10);	 Catch:{ Exception -> 0x022f, all -> 0x0237 }
        goto L_0x022a;
    L_0x0237:
        r14 = move-exception;
        r1 = r2;
        goto L_0x018c;
    L_0x023b:
        r14 = r9.get(r7);	 Catch:{ Exception -> 0x022f, all -> 0x0237 }
        r14 = (java.util.HashMap) r14;	 Catch:{ Exception -> 0x022f, all -> 0x0237 }
        r8.add(r7, r14);	 Catch:{ Exception -> 0x022f, all -> 0x0237 }
        r7 = r7 + 1;
        goto L_0x008e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ifoer.pro.db.DBDao.query(java.lang.String, java.lang.String, java.lang.String, android.database.sqlite.SQLiteDatabase):java.util.List<java.util.HashMap<java.lang.String, java.lang.Object>>");
    }

    public String queryMaxVersion(String serialNo, String softId, String lanName, SQLiteDatabase db) {
        Cursor cursor = null;
        String maxOldVersion = null;
        try {
            cursor = db.rawQuery("select maxVersion from upgrade where serialNo=? and softId=? and lanName=?", new String[]{serialNo, softId, lanName});
            if (cursor.moveToFirst()) {
                maxOldVersion = cursor.getString(cursor.getColumnIndex("maxVersion"));
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        return maxOldVersion;
    }
}
