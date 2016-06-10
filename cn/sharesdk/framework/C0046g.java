package cn.sharesdk.framework;

/* renamed from: cn.sharesdk.framework.g */
class C0046g extends Thread {
    final /* synthetic */ String[] f83a;
    final /* synthetic */ C0045f f84b;

    C0046g(C0045f c0045f, String[] strArr) {
        this.f84b = c0045f;
        this.f83a = strArr;
    }

    public void run() {
        this.f84b.m111j();
        this.f84b.f74a.m8a(this.f83a);
    }
}
