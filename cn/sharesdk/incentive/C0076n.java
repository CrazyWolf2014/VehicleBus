package cn.sharesdk.incentive;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/* renamed from: cn.sharesdk.incentive.n */
public class C0076n {
    private static C0076n f138a;
    private Context f139b;
    private SharedPreferences f140c;

    private C0076n(Context context) {
        this.f139b = context.getApplicationContext();
        this.f140c = this.f139b.getSharedPreferences("share_sdk_incentive_0", 0);
    }

    public static C0076n m249a(Context context) {
        if (f138a == null) {
            f138a = new C0076n(context.getApplicationContext());
        }
        return f138a;
    }

    public long m250a(String str) {
        return this.f140c.getLong(str, 0);
    }

    public void m251a(String str, int i) {
        Editor edit = this.f140c.edit();
        edit.putInt(str, i);
        edit.commit();
    }

    public void m252a(String str, Long l) {
        Editor edit = this.f140c.edit();
        edit.putLong(str, l.longValue());
        edit.commit();
    }

    public int m253b(String str) {
        return this.f140c.getInt(str, 0);
    }
}
