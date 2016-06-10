package cn.sharesdk.framework;

/* renamed from: cn.sharesdk.framework.h */
class C0047h extends Thread {
    final /* synthetic */ int f85a;
    final /* synthetic */ Object f86b;
    final /* synthetic */ C0045f f87c;

    C0047h(C0045f c0045f, int i, Object obj) {
        this.f87c = c0045f;
        this.f85a = i;
        this.f86b = obj;
    }

    public void run() {
        this.f87c.m111j();
        if (this.f87c.f74a.m9a(this.f85a, this.f86b)) {
            this.f87c.m129c(this.f85a, this.f86b);
        }
    }
}
