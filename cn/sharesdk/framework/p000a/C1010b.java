package cn.sharesdk.framework.p000a;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/* renamed from: cn.sharesdk.framework.a.b */
public class C1010b extends C0022c {
    private File f1731a;

    protected InputStream m1761a() {
        return new FileInputStream(this.f1731a);
    }

    public void m1762a(File file) {
        this.f1731a = file;
    }

    public void m1763a(String str) {
        this.f1731a = new File(str);
    }

    protected long m1764b() {
        return this.f1731a.length();
    }

    public String toString() {
        return this.f1731a.toString();
    }
}
