package cn.sharesdk.framework;

import android.content.Context;
import cn.sharesdk.framework.p001b.p003b.C1020f.C0043a;
import java.util.HashMap;

public abstract class Platform {
    public static final int ACTION_AUTHORIZING = 1;
    public static final int ACTION_FOLLOWING_USER = 6;
    public static final int ACTION_GETTING_FRIEND_LIST = 2;
    public static final int ACTION_SENDING_DIRECT_MESSAGE = 5;
    public static final int ACTION_SHARE = 9;
    public static final int ACTION_TIMELINE = 7;
    public static final int ACTION_USER_INFOR = 8;
    public static final int CUSTOMER_ACTION_MASK = 65535;
    public static final int SHARE_APPS = 7;
    public static final int SHARE_EMOJI = 9;
    public static final int SHARE_FILE = 8;
    public static final int SHARE_IMAGE = 2;
    public static final int SHARE_MUSIC = 5;
    public static final int SHARE_TEXT = 1;
    public static final int SHARE_VIDEO = 6;
    public static final int SHARE_WEBPAGE = 4;
    protected final Context f0a;
    protected final PlatformDb f1b;
    protected PlatformActionListener f2c;
    private C0045f f3d;

    public static class ShareParams {
        public String imagePath;
        public String text;
    }

    public Platform(Context context) {
        this.f0a = context;
        this.f3d = new C0045f(this, context);
        this.f1b = this.f3d.m134g();
        this.f2c = this.f3d.m136i();
    }

    public void SSOSetting(boolean z) {
        this.f3d.m123a(z);
    }

    protected abstract C0043a m0a(ShareParams shareParams, HashMap<String, Object> hashMap);

    protected String m1a(int i, String str, String str2) {
        return this.f3d.m114a(i, str, str2);
    }

    protected String m2a(String str, String str2) {
        return m1a(getPlatformId(), str, str2);
    }

    protected abstract void m3a();

    protected abstract void m4a(int i, int i2, String str);

    protected abstract void m5a(ShareParams shareParams);

    protected abstract void m6a(String str);

    protected abstract void m7a(String str, String str2, int i, HashMap<String, Object> hashMap, HashMap<String, String> hashMap2);

    protected abstract void m8a(String[] strArr);

    protected abstract boolean m9a(int i, Object obj);

    public void authorize() {
        authorize(null);
    }

    public void authorize(String[] strArr) {
        this.f3d.m124a(strArr);
    }

    void m10b() {
        this.f3d.m120a(getName());
    }

    protected abstract void m11b(int i, int i2, String str);

    protected void m12b(int i, Object obj) {
        this.f3d.m117a(i, obj);
    }

    protected abstract void m13b(String str);

    protected void m14c(int i, Object obj) {
        this.f3d.m129c(i, obj);
    }

    protected abstract void m15c(String str);

    boolean m16c() {
        return this.f3d.m133f();
    }

    public void customerProtocol(String str, String str2, short s, HashMap<String, Object> hashMap, HashMap<String, String> hashMap2) {
        this.f3d.m122a(str, str2, s, hashMap, hashMap2);
    }

    public void followFriend(String str) {
        this.f3d.m127b(str);
    }

    public Context getContext() {
        return this.f0a;
    }

    public PlatformDb getDb() {
        return this.f1b;
    }

    public String getDevinfo(String str) {
        return getDevinfo(getName(), str);
    }

    public String getDevinfo(String str, String str2) {
        return ShareSDK.m21a(str, str2);
    }

    public int getId() {
        return this.f3d.m113a();
    }

    public abstract String getName();

    public PlatformActionListener getPlatformActionListener() {
        return this.f3d.m128c();
    }

    protected abstract int getPlatformId();

    public String getShortLintk(String str, boolean z) {
        return this.f3d.m115a(str, z);
    }

    public int getSortId() {
        return this.f3d.m125b();
    }

    public void getTimeLine(String str, int i, int i2) {
        this.f3d.m121a(str, i, i2);
    }

    public abstract int getVersion();

    public boolean isSSODisable() {
        return this.f3d.m132e();
    }

    public boolean isValid() {
        return this.f3d.m131d();
    }

    public void listFriend(int i, int i2, String str) {
        this.f3d.m116a(i, i2, str);
    }

    public void removeAccount() {
        this.f3d.m135h();
    }

    public void setPlatformActionListener(PlatformActionListener platformActionListener) {
        this.f3d.m119a(platformActionListener);
    }

    public void share(ShareParams shareParams) {
        this.f3d.m118a(shareParams);
    }

    public void showUser(String str) {
        this.f3d.m130c(str);
    }
}
