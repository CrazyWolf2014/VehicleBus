package cn.sharesdk.framework.p001b.p002a;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/* renamed from: cn.sharesdk.framework.b.a.c */
public class C0038c {
    private static C0038c f33c;
    private Context f34a;
    private SharedPreferences f35b;

    private C0038c(Context context) {
        this.f34a = context.getApplicationContext();
        this.f35b = this.f34a.getSharedPreferences("share_sdk_0", 0);
    }

    public static C0038c m51a(Context context) {
        if (f33c == null) {
            f33c = new C0038c(context.getApplicationContext());
        }
        return f33c;
    }

    public long m52a() {
        return this.f35b.getLong("service_time", 0);
    }

    public long m53a(String str) {
        return this.f35b.getLong(str, 0);
    }

    public void m54a(String str, int i) {
        Editor edit = this.f35b.edit();
        edit.putInt(str, i);
        edit.commit();
    }

    public void m55a(String str, Long l) {
        Editor edit = this.f35b.edit();
        edit.putLong(str, l.longValue());
        edit.commit();
    }

    public void m56a(String str, String str2) {
        Editor edit = this.f35b.edit();
        edit.putString(str, str2);
        edit.commit();
    }

    public int m57b(String str) {
        return this.f35b.getInt(str, 0);
    }

    public boolean m58b() {
        return Boolean.parseBoolean(this.f35b.getString("upload_device_info", "true"));
    }

    public void m59c(String str) {
        m56a("upload_device_info", str);
    }

    public boolean m60c() {
        return Boolean.parseBoolean(this.f35b.getString("upload_user_info", "true"));
    }

    public void m61d(String str) {
        m56a("upload_user_info", str);
    }

    public boolean m62d() {
        return Boolean.parseBoolean(this.f35b.getString("upload_share_content", "true"));
    }

    public void m63e(String str) {
        m56a("upload_share_content", str);
    }
}
