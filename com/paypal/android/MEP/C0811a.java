package com.paypal.android.MEP;

/* renamed from: com.paypal.android.MEP.a */
public final class C0811a {
    private static C0811a f1482a;

    /* renamed from: com.paypal.android.MEP.a.a */
    private class C0800a extends Thread {
        private C0801b f1459a;

        public C0800a(C0811a c0811a, C0801b c0801b) {
            this.f1459a = c0801b;
        }

        public final void run() {
            try {
                Thread.yield();
                Thread.sleep(3000);
            } catch (Throwable th) {
            }
            this.f1459a.m1495l();
        }
    }

    /* renamed from: com.paypal.android.MEP.a.b */
    public interface C0801b {
        void m1492a(int i, Object obj);

        void m1493a(String str, Object obj);

        void m1494d(String str);

        void m1495l();
    }

    static {
        f1482a = null;
    }

    private C0811a() {
    }

    public static C0811a m1500a() {
        if (f1482a == null) {
            if (f1482a != null) {
                throw new IllegalStateException("Attempted to initialize PPMobileAPIInterface more than once.");
            }
            f1482a = new C0811a();
        }
        return f1482a;
    }

    public final void m1501a(C0801b c0801b) {
        new C0800a(this, c0801b).start();
    }

    public final void m1502a(C0801b c0801b, String str, String str2) {
        C0800a c0800a = new C0800a(this, c0801b);
        c0801b.m1493a("usernameOrPhone", (Object) str);
        c0801b.m1493a("passwordOrPin", (Object) str2);
        c0800a.start();
    }

    public final void m1503b(C0801b c0801b, String str, String str2) {
        C0800a c0800a = new C0800a(this, c0801b);
        c0801b.m1493a("mobileNumber", (Object) str);
        c0801b.m1493a("newPIN", (Object) str2);
        c0800a.start();
    }
}
