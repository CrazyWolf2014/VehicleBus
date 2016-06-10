package cn.sharesdk.framework.p000a;

import java.io.InputStream;
import org.apache.http.entity.InputStreamEntity;

/* renamed from: cn.sharesdk.framework.a.c */
public abstract class C0022c {
    protected abstract InputStream m27a();

    protected abstract long m28b();

    public InputStreamEntity m29c() {
        return new InputStreamEntity(m27a(), m28b());
    }
}
