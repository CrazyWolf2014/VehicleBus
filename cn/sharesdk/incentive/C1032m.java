package cn.sharesdk.incentive;

import android.text.TextUtils;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.Service;
import cn.sharesdk.framework.Service.ServiceEvent;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.C0058e;
import com.tencent.mm.sdk.plugin.BaseProfile;
import java.util.HashMap;

/* renamed from: cn.sharesdk.incentive.m */
public class C1032m extends ServiceEvent {
    private int f1789a;
    private ShareParams f1790b;
    private HashMap<String, Object> f1791c;

    public C1032m(Service service) {
        super(service);
    }

    public void m1871a(int i) {
        this.f1789a = i;
    }

    public void m1872a(ShareParams shareParams, HashMap<String, Object> hashMap) {
        this.f1790b = shareParams;
        this.f1791c = hashMap;
    }

    protected HashMap<String, Object> toMap() {
        int i = 2;
        HashMap<String, Object> toMap = super.toMap();
        toMap.put("snsplat", Integer.valueOf(this.f1789a));
        HashMap filterShareContent = filterShareContent(this.f1789a, this.f1790b, this.f1791c);
        Object obj = filterShareContent.get("shareID");
        Object obj2 = filterShareContent.get("shareContent");
        if (obj != null) {
            toMap.put("shareid", obj);
        }
        if (obj2 != null) {
            toMap.put("sharecontent", obj2);
        }
        Platform platform = ShareSDK.getPlatform(this.service.getContext(), ShareSDK.platformIdToName(this.f1789a));
        if (platform != null) {
            int parseInt;
            CharSequence userId = platform.getDb().getUserId();
            if (!TextUtils.isEmpty(userId)) {
                toMap.put("snsuid", userId);
            }
            userId = platform.getDb().getUserName();
            if (!TextUtils.isEmpty(userId)) {
                toMap.put(BaseProfile.COL_NICKNAME, userId);
            }
            userId = platform.getDb().getUserIcon();
            if (!TextUtils.isEmpty(userId)) {
                toMap.put("icon", userId);
            }
            try {
                parseInt = Integer.parseInt(platform.getDb().get("gender"));
            } catch (Throwable th) {
                C0058e.m220b(th);
                parseInt = i;
            }
            toMap.put("gender", Integer.valueOf(parseInt));
            try {
                i = Integer.parseInt(platform.getDb().get("secretType"));
            } catch (Throwable th2) {
                C0058e.m220b(th2);
            }
            toMap.put("verifytype", Integer.valueOf(i));
        }
        return toMap;
    }
}
