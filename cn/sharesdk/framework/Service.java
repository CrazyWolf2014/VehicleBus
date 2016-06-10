package cn.sharesdk.framework;

import android.content.Context;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.p001b.p003b.C1020f.C0043a;
import cn.sharesdk.framework.utils.C0053b;
import cn.sharesdk.framework.utils.C0055d;
import cn.sharesdk.framework.utils.C0058e;
import java.util.HashMap;

public abstract class Service {
    private Context f4a;
    private String f5b;

    public static abstract class ServiceEvent {
        private final int PLATFORM;
        protected Service service;

        public ServiceEvent(Service service) {
            this.PLATFORM = 1;
            this.service = service;
        }

        protected HashMap<String, Object> filterShareContent(int i, ShareParams shareParams, HashMap<String, Object> hashMap) {
            C0043a a = ShareSDK.getPlatform(this.service.getContext(), ShareSDK.platformIdToName(i)).m0a(shareParams, (HashMap) hashMap);
            HashMap<String, Object> hashMap2 = new HashMap();
            hashMap2.put("shareID", a.f59a);
            hashMap2.put("shareContent", new C0055d().m212a(a.toString()));
            C0058e.m221c("filterShareContent ==>>%s", hashMap2);
            return hashMap2;
        }

        protected HashMap<String, Object> toMap() {
            HashMap<String, Object> hashMap = new HashMap();
            C0053b a = C0053b.m175a(this.service.f4a);
            hashMap.put("deviceid", a.m195o());
            hashMap.put("appkey", this.service.getAppKey());
            hashMap.put("apppkg", a.m196p());
            hashMap.put("appver", Integer.valueOf(a.m198r()));
            hashMap.put("sdkver", Integer.valueOf(this.service.getServiceVersionInt()));
            hashMap.put("plat", Integer.valueOf(1));
            hashMap.put("networktype", a.m192l());
            hashMap.put("deviceData", a.m186f());
            return hashMap;
        }

        public final String toString() {
            return new C0055d().m211a(toMap());
        }
    }

    void m18a(Context context) {
        this.f4a = context;
    }

    void m19a(String str) {
        this.f5b = str;
    }

    public String getAppKey() {
        return this.f5b;
    }

    public Context getContext() {
        return this.f4a;
    }

    public String getDeviceKey() {
        return C0053b.m175a(this.f4a).m195o();
    }

    protected abstract int getServiceVersionInt();

    public abstract String getServiceVersionName();

    public void onBind() {
    }

    public void onUnbind() {
    }
}
