package com.iflytek.util.p017a.p019c;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog.Calls;
import com.cnmobi.im.dto.Msg;
import com.iflytek.msc.p013f.C0276e;
import com.iflytek.msc.p013f.C0277f;
import com.iflytek.util.p017a.C0327c;
import com.iflytek.util.p017a.p018a.C0322a;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* renamed from: com.iflytek.util.a.c.a */
public abstract class C0326a {
    protected static final String[] f1214a;
    protected static String[] f1215c;
    protected Context f1216b;

    static {
        f1214a = new String[]{"number", "name", Msg.DATE};
    }

    public C0326a(Context context) {
        this.f1216b = null;
        this.f1216b = context;
    }

    public abstract Uri m1456a();

    public HashMap<String, String> m1457a(int i) {
        Exception e;
        Throwable th;
        String str = "date DESC limit " + i;
        HashMap<String, String> hashMap = new HashMap();
        Cursor query;
        try {
            query = this.f1216b.getContentResolver().query(Calls.CONTENT_URI, f1214a, "0==0) GROUP BY (number", null, str);
            if (query == null) {
                try {
                    C0276e.m1221a("iFly_ContactManager", "queryCallLog ----------------cursor is null");
                } catch (Exception e2) {
                    e = e2;
                }
            } else if (query.getCount() == 0) {
                C0276e.m1221a("iFly_ContactManager", "queryCallLog ----------------cursor getCount == 0");
            } else {
                while (query.moveToNext()) {
                    String string = query.getString(query.getColumnIndex("number"));
                    Object string2 = query.getString(query.getColumnIndex("name"));
                    String string3 = query.getString(query.getColumnIndex(Msg.DATE));
                    if (string2 == null) {
                        string2 = C0327c.m1464a(string);
                    }
                    hashMap.put(string3, string2);
                }
                C0276e.m1221a("iFly_ContactManager", "queryCallLog ----------------cursor getCount ==" + query.getCount());
            }
            if (query != null) {
                query.close();
            }
        } catch (Exception e3) {
            e = e3;
            query = null;
            try {
                e.printStackTrace();
                if (query != null) {
                    query.close();
                }
                return hashMap;
            } catch (Throwable th2) {
                th = th2;
                if (query != null) {
                    query.close();
                }
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            query = null;
            if (query != null) {
                query.close();
            }
            throw th;
        }
        return hashMap;
    }

    protected void m1458a(Context context) {
        f1215c = new String[100];
        f1215c[0] = "\u5176\u4ed6";
        f1215c[1] = "\u4f4f\u5b85";
        f1215c[2] = "\u624b\u673a";
        f1215c[3] = "\u5de5\u4f5c";
        f1215c[4] = "\u5de5\u4f5c\u4f20\u771f";
        f1215c[5] = "\u4f4f\u5b85\u4f20\u771f";
        f1215c[6] = "\u5bfb\u547c\u673a";
        f1215c[7] = "\u5176\u4ed6";
        f1215c[9] = "SIM\u5361";
        for (int i = 10; i < f1215c.length; i++) {
            f1215c[i] = "\u81ea\u5b9a\u4e49\u7535\u8bdd";
        }
    }

    protected abstract String[] m1459b();

    protected abstract String m1460c();

    public HashMap<String, String> m1461d() {
        Exception e;
        Throwable th;
        String[] b = m1459b();
        HashMap<String, String> hashMap = new HashMap();
        Cursor query;
        try {
            query = this.f1216b.getContentResolver().query(m1456a(), b, null, null, m1460c());
            if (query == null) {
                try {
                    C0276e.m1221a("iFly_ContactManager", "queryContacts------cursor is null");
                } catch (Exception e2) {
                    e = e2;
                }
            } else if (query.getCount() == 0) {
                C0276e.m1221a("iFly_ContactManager", "queryContacts------cursor getCount == 0");
            } else {
                while (query.moveToNext()) {
                    String string = query.getString(query.getColumnIndex(b[0]));
                    String string2 = query.getString(query.getColumnIndex(b[1]));
                    if (string != null) {
                        hashMap.put(string2, string);
                    }
                }
                C0276e.m1221a("iFly_ContactManager", "queryContacts_20------count = " + query.getCount());
            }
            if (query != null) {
                query.close();
            }
        } catch (Exception e3) {
            e = e3;
            query = null;
            try {
                e.printStackTrace();
                if (query != null) {
                    query.close();
                }
                return hashMap;
            } catch (Throwable th2) {
                th = th2;
                if (query != null) {
                    query.close();
                }
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            query = null;
            if (query != null) {
                query.close();
            }
            throw th;
        }
        return hashMap;
    }

    public List<C0322a> m1462e() {
        Exception e;
        Cursor cursor;
        Throwable th;
        List<C0322a> arrayList = new ArrayList();
        Cursor query;
        try {
            query = this.f1216b.getContentResolver().query(Uri.parse("content://icc/adn"), null, null, null, null);
            if (query != null) {
                try {
                    if (query.getCount() > 0) {
                        while (query.moveToNext()) {
                            String string = query.getString(query.getColumnIndex("name"));
                            String string2 = query.getString(query.getColumnIndex("_id"));
                            String a = C0277f.m1225a(C0327c.m1464a(query.getString(query.getColumnIndex("number"))));
                            if (string != null) {
                                arrayList.add(new C0322a(string2, string, null, a, null, f1215c[9]));
                            }
                        }
                        C0276e.m1221a("iFly_ContactManager", "querySIM-------count = " + query.getCount());
                        if (query != null) {
                            query.close();
                        }
                        return arrayList;
                    }
                } catch (Exception e2) {
                    e = e2;
                    cursor = query;
                    try {
                        e.printStackTrace();
                        if (cursor != null) {
                            cursor.close();
                        }
                        return arrayList;
                    } catch (Throwable th2) {
                        th = th2;
                        query = cursor;
                        if (query != null) {
                            query.close();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    if (query != null) {
                        query.close();
                    }
                    throw th;
                }
            }
            C0276e.m1221a("iFly_ContactManager", "querySIM-------cursor getCount = 0 or cursor is null");
            if (query != null) {
                query.close();
            }
        } catch (Exception e3) {
            e = e3;
            cursor = null;
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
            return arrayList;
        } catch (Throwable th4) {
            th = th4;
            query = null;
            if (query != null) {
                query.close();
            }
            throw th;
        }
        return arrayList;
    }

    public Uri m1463f() {
        return Calls.CONTENT_URI;
    }
}
