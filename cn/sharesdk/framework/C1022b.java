package cn.sharesdk.framework;

import android.text.TextUtils;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.p001b.C0044b;
import cn.sharesdk.framework.p001b.p003b.C0042c;
import cn.sharesdk.framework.p001b.p003b.C1020f;
import cn.sharesdk.framework.p001b.p003b.C1020f.C0043a;
import cn.sharesdk.framework.utils.C0052a;
import cn.sharesdk.framework.utils.C0058e;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.tencent.mm.sdk.plugin.BaseProfile;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.OAuth;
import java.util.HashMap;

/* renamed from: cn.sharesdk.framework.b */
public class C1022b implements PlatformActionListener {
    private PlatformActionListener f1770a;

    C1022b() {
    }

    private String m1840a(Platform platform) {
        try {
            return m1841a(platform.getDb(), new String[]{BaseProfile.COL_NICKNAME, "icon", "gender", "snsUserUrl", "resume", "secretType", OAuth.SECRET, "birthday", "followerCount", "favouriteCount", "shareCount", "snsregat", "snsUserLevel", "educationJSONArrayStr", "workJSONArrayStr"});
        } catch (Throwable th) {
            C0058e.m220b(th);
            return null;
        }
    }

    private String m1841a(PlatformDb platformDb, String[] strArr) {
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder stringBuilder2 = new StringBuilder();
        int i = 0;
        for (String str : strArr) {
            if (i > 0) {
                stringBuilder2.append('|');
                stringBuilder.append('|');
            }
            i++;
            String str2 = platformDb.get(str2);
            if (!TextUtils.isEmpty(str2)) {
                stringBuilder.append(str2);
                stringBuilder2.append(C0052a.m172c(str2, "utf-8"));
            }
        }
        C0058e.m221c("======UserData: " + stringBuilder.toString(), new Object[0]);
        return stringBuilder2.toString();
    }

    private void m1843a(Platform platform, int i, HashMap<String, Object> hashMap) {
        this.f1770a = new C1023c(this, this.f1770a, i, hashMap);
        platform.showUser(null);
    }

    private void m1844a(Platform platform, ShareParams shareParams, HashMap<String, Object> hashMap) {
    }

    private String m1845b(Platform platform) {
        try {
            return m1841a(platform.getDb(), new String[]{"gender", "birthday", "secretType", "educationJSONArrayStr", "workJSONArrayStr"});
        } catch (Throwable th) {
            C0058e.m220b(th);
            return null;
        }
    }

    private void m1846b(Platform platform, int i, HashMap<String, Object> hashMap) {
        ShareParams shareParams = hashMap != null ? (ShareParams) hashMap.remove("ShareParams") : null;
        try {
            HashMap hashMap2 = (HashMap) hashMap.clone();
        } catch (Throwable th) {
            C0058e.m220b(th);
            HashMap<String, Object> hashMap3 = hashMap;
        }
        m1844a(platform, shareParams, (HashMap) hashMap);
        if (this.f1770a != null) {
            this.f1770a.onComplete(platform, i, hashMap);
        }
        if (shareParams != null) {
            C0042c c1020f = new C1020f();
            c1020f.f1763b = "TencentWeibo".equals(platform.getName()) ? platform.getDb().get("name") : platform.getDb().getUserId();
            c1020f.f1762a = platform.getPlatformId();
            C0043a a = platform.m0a(shareParams, hashMap2);
            if (a != null) {
                c1020f.f1764c = a.f59a;
                c1020f.f1766n = a;
            }
            c1020f.f1767o = m1845b(platform);
            C0044b.m100a(platform.getContext()).m106a(c1020f);
        }
    }

    PlatformActionListener m1847a() {
        return this.f1770a;
    }

    void m1848a(Platform platform, int i, Object obj) {
        this.f1770a = new C1024d(this, this.f1770a, i, obj);
        platform.m8a(null);
    }

    void m1849a(PlatformActionListener platformActionListener) {
        this.f1770a = platformActionListener;
    }

    public void onCancel(Platform platform, int i) {
        if (this.f1770a != null) {
            this.f1770a.onCancel(platform, i);
        }
    }

    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        switch (i) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                m1843a(platform, i, (HashMap) hashMap);
            case FileOptions.OPTIMIZE_FOR_FIELD_NUMBER /*9*/:
                m1846b(platform, i, hashMap);
            default:
                if (this.f1770a != null) {
                    this.f1770a.onComplete(platform, i, hashMap);
                }
        }
    }

    public void onError(Platform platform, int i, Throwable th) {
        if (this.f1770a != null) {
            this.f1770a.onError(platform, i, th);
        }
    }
}
