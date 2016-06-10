package com.iflytek.util;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.HandlerThread;
import com.iflytek.msc.p013f.C0276e;
import com.iflytek.util.p017a.C0324a;
import com.iflytek.util.p017a.C0325b;
import com.iflytek.util.p017a.C0327c;
import com.iflytek.util.p017a.p019c.C0326a;
import com.iflytek.util.p017a.p026b.C1093a;
import com.iflytek.util.p017a.p026b.C1094b;

public class ContactManager {
    private static ContactManager f1185a;
    private static Context f1186b;
    private static int f1187c;
    private static C0326a f1188d;
    private static C0324a f1189e;
    private static C0321b f1190f;
    private static C0320a f1191g;
    private static ContactListener f1192i;
    private HandlerThread f1193h;
    private Handler f1194j;
    private long f1195k;
    private long f1196l;

    public interface ContactListener {
        void onContactQueryFinish(String str, boolean z);
    }

    /* renamed from: com.iflytek.util.ContactManager.a */
    private class C0320a extends ContentObserver {
        final /* synthetic */ ContactManager f1183a;

        public C0320a(ContactManager contactManager, Handler handler) {
            this.f1183a = contactManager;
            super(handler);
        }

        public void onChange(boolean z) {
            C0276e.m1221a("iFly_ContactManager", "CallLogObserver | onChange");
            if (System.currentTimeMillis() - this.f1183a.f1196l < 5000) {
                C0276e.m1221a("iFly_ContactManager", "onChange too much");
                return;
            }
            this.f1183a.f1196l = System.currentTimeMillis();
            this.f1183a.m1431b();
        }
    }

    /* renamed from: com.iflytek.util.ContactManager.b */
    private class C0321b extends ContentObserver {
        final /* synthetic */ ContactManager f1184a;

        public C0321b(ContactManager contactManager, Handler handler) {
            this.f1184a = contactManager;
            super(handler);
        }

        public void onChange(boolean z) {
            C0276e.m1221a("iFly_ContactManager", "ContactObserver_Contact| onChange");
            if (System.currentTimeMillis() - this.f1184a.f1195k < 5000) {
                C0276e.m1221a("iFly_ContactManager", "onChange too much");
                return;
            }
            this.f1184a.f1195k = System.currentTimeMillis();
            this.f1184a.m1428a();
            this.f1184a.m1431b();
        }
    }

    static {
        f1185a = null;
        f1186b = null;
        f1187c = 4;
        f1188d = null;
        f1189e = null;
        f1192i = null;
    }

    private ContactManager() {
        this.f1193h = null;
        this.f1195k = 0;
        this.f1196l = 0;
        if (VERSION.SDK_INT > f1187c) {
            f1188d = new C1094b(f1186b);
        } else {
            f1188d = new C1093a(f1186b);
        }
        f1189e = new C0324a(f1186b, f1188d);
        this.f1193h = new HandlerThread("ContactManager_worker");
        this.f1193h.start();
        this.f1194j = new Handler(this.f1193h.getLooper());
        this.f1193h.setPriority(1);
        f1190f = new C0321b(this, this.f1194j);
        f1191g = new C0320a(this, this.f1194j);
    }

    private void m1428a() {
        if (f1192i != null && f1189e != null) {
            String a = C0327c.m1465a(f1189e.m1453a(), '\n');
            String str = f1186b.getFilesDir().getParent() + '/' + "name.txt";
            String a2 = C0325b.m1455a(str);
            if (a == null || a2 == null || !a.equals(a2)) {
                C0325b.m1454a(str, a, true);
                f1192i.onContactQueryFinish(a, true);
                return;
            }
            C0276e.m1221a("iFly_ContactManager", "contact name is not change.");
            f1192i.onContactQueryFinish(a, false);
        }
    }

    private void m1431b() {
        if (f1189e != null) {
            f1189e.m1452a(10);
        }
    }

    public static ContactManager createManager(Context context, ContactListener contactListener) {
        f1192i = contactListener;
        f1186b = context;
        if (f1185a == null) {
            f1185a = new ContactManager();
            f1186b.getContentResolver().registerContentObserver(f1188d.m1456a(), true, f1190f);
            f1186b.getContentResolver().registerContentObserver(f1188d.m1463f(), true, f1191g);
        }
        return f1185a;
    }

    public static ContactManager getManager() {
        return f1185a;
    }

    public void asyncQueryAllContactsName() {
        this.f1194j.post(new C0328a(this));
    }

    public String queryAllContactsName() {
        if (f1189e == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String str : f1189e.m1453a()) {
            stringBuilder.append(str + '\n');
        }
        return stringBuilder.toString();
    }
}
