package cn.sharesdk.framework.p001b.p002a;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cn.sharesdk.framework.utils.C0058e;

/* renamed from: cn.sharesdk.framework.b.a.b */
public class C0037b {
    private static C0037b f30c;
    private Context f31a;
    private C0036a f32b;

    static {
        f30c = null;
    }

    private C0037b(Context context) {
        this.f31a = context.getApplicationContext();
        this.f32b = new C0036a(this.f31a);
    }

    public static synchronized C0037b m46a(Context context) {
        C0037b c0037b;
        synchronized (C0037b.class) {
            if (f30c == null) {
                f30c = new C0037b(context);
            }
            c0037b = f30c;
        }
        return c0037b;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int m47a(java.lang.String r6) {
        /*
        r5 = this;
        r2 = 0;
        r0 = 0;
        r1 = r5.f32b;
        r1 = r1.getWritableDatabase();
        r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x002f }
        r3.<init>();	 Catch:{ Exception -> 0x002f }
        r4 = "select count(*) from ";
        r3 = r3.append(r4);	 Catch:{ Exception -> 0x002f }
        r3 = r3.append(r6);	 Catch:{ Exception -> 0x002f }
        r3 = r3.toString();	 Catch:{ Exception -> 0x002f }
        r4 = 0;
        r2 = r1.rawQuery(r3, r4);	 Catch:{ Exception -> 0x002f }
        r1 = r2.moveToNext();	 Catch:{ Exception -> 0x002f }
        if (r1 == 0) goto L_0x002b;
    L_0x0026:
        r1 = 0;
        r0 = r2.getInt(r1);	 Catch:{ Exception -> 0x002f }
    L_0x002b:
        r2.close();
    L_0x002e:
        return r0;
    L_0x002f:
        r1 = move-exception;
        cn.sharesdk.framework.utils.C0058e.m220b(r1);	 Catch:{ all -> 0x0037 }
        r2.close();
        goto L_0x002e;
    L_0x0037:
        r0 = move-exception;
        r2.close();
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.sharesdk.framework.b.a.b.a(java.lang.String):int");
    }

    public int m48a(String str, String str2, String[] strArr) {
        int delete;
        Throwable e;
        try {
            delete = this.f32b.getWritableDatabase().delete(str, str2, strArr);
            try {
                C0058e.m216a("Deleted %d rows from table: %s", Integer.valueOf(delete), str);
            } catch (Exception e2) {
                e = e2;
                C0058e.m218a(e, "when delete database occur error table:%s,", str);
                return delete;
            }
        } catch (Throwable e3) {
            e = e3;
            delete = 0;
            C0058e.m218a(e, "when delete database occur error table:%s,", str);
            return delete;
        }
        return delete;
    }

    public long m49a(String str, ContentValues contentValues) {
        long j = -1;
        try {
            j = this.f32b.getWritableDatabase().replace(str, null, contentValues);
        } catch (Throwable e) {
            C0058e.m218a(e, "when insert database occur error table:%s,", str);
        }
        return j;
    }

    public Cursor m50a(String str, String[] strArr, String str2, String[] strArr2, String str3) {
        SQLiteDatabase writableDatabase = this.f32b.getWritableDatabase();
        C0058e.m216a("Query table: %s", str);
        try {
            return writableDatabase.query(str, strArr, str2, strArr2, null, null, str3);
        } catch (Throwable e) {
            C0058e.m218a(e, "when query database occur error table:%s,", str);
            return null;
        }
    }
}
