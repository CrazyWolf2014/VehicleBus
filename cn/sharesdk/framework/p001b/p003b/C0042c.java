package cn.sharesdk.framework.p001b.p003b;

import android.content.Context;

/* renamed from: cn.sharesdk.framework.b.b.c */
public abstract class C0042c {
    public long f50e;
    public String f51f;
    public String f52g;
    public String f53h;
    public int f54i;
    public String f55j;
    public int f56k;
    public String f57l;
    public String f58m;

    protected abstract String m90a();

    protected abstract void m91a(long j);

    public boolean m92a(Context context) {
        int b = m93b();
        int c = m95c();
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - m97e() < ((long) b)) {
            return m96d() < ((long) c);
        } else {
            m91a(currentTimeMillis);
            return true;
        }
    }

    protected abstract int m93b();

    public void m94b(Context context) {
        m98f();
    }

    protected abstract int m95c();

    protected abstract long m96d();

    protected abstract long m97e();

    protected abstract void m98f();

    public String m99g() {
        return this.f52g;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(m90a()).append(':');
        stringBuilder.append(this.f50e).append('|');
        stringBuilder.append(this.f51f).append('|');
        stringBuilder.append(this.f52g).append('|');
        stringBuilder.append(this.f53h).append('|');
        stringBuilder.append(this.f54i).append('|');
        stringBuilder.append(this.f55j).append('|');
        stringBuilder.append(this.f56k).append('|');
        stringBuilder.append(this.f57l);
        return stringBuilder.toString();
    }
}
