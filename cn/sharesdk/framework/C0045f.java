package cn.sharesdk.framework;

import android.content.Context;
import android.text.TextUtils;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.p001b.C0044b;
import cn.sharesdk.framework.utils.C0052a;
import cn.sharesdk.framework.utils.C0055d;
import cn.sharesdk.framework.utils.C0058e;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import java.util.HashMap;

/* renamed from: cn.sharesdk.framework.f */
public class C0045f {
    private Platform f74a;
    private Context f75b;
    private PlatformDb f76c;
    private C1022b f77d;
    private int f78e;
    private int f79f;
    private boolean f80g;
    private boolean f81h;
    private boolean f82i;

    public C0045f(Platform platform, Context context) {
        this.f81h = true;
        this.f74a = platform;
        this.f75b = context;
        String name = platform.getName();
        this.f76c = new PlatformDb(context, name, platform.getVersion());
        m120a(name);
        this.f77d = new C1022b();
    }

    private boolean m111j() {
        if (!ShareSDK.m22a()) {
            return true;
        }
        if (ShareSDK.m24b()) {
            this.f74a.m3a();
            return true;
        }
        try {
            HashMap hashMap = new HashMap();
            if (!C0044b.m100a(this.f75b).m108a(hashMap)) {
                return false;
            }
            if (ShareSDK.m23a(hashMap)) {
                this.f81h = "true".equals(m114a(this.f74a.getPlatformId(), "covert_url", null).trim());
                this.f74a.m3a();
                return true;
            }
            System.err.println("Failed to parse network dev-info: " + new C0055d().m211a(hashMap));
            return false;
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
    }

    private String m112k() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            if ("TencentWeibo".equals(this.f74a.getName())) {
                C0058e.m221c("user id %s ==>>", m134g().getUserName());
                stringBuilder.append(C0052a.m172c(m134g().getUserName(), "utf-8"));
            } else {
                stringBuilder.append(C0052a.m172c(m134g().getUserId(), "utf-8"));
            }
            stringBuilder.append("|").append(C0052a.m172c(m134g().get("secretType"), "utf-8"));
            stringBuilder.append("|").append(C0052a.m172c(m134g().get("gender"), "utf-8"));
            stringBuilder.append("|").append(C0052a.m172c(m134g().get("birthday"), "utf-8"));
            stringBuilder.append("|").append(C0052a.m172c(m134g().get("educationJSONArrayStr"), "utf-8"));
            stringBuilder.append("|").append(C0052a.m172c(m134g().get("workJSONArrayStr"), "utf-8"));
        } catch (Throwable th) {
            C0058e.m220b(th);
        }
        return stringBuilder.toString();
    }

    public int m113a() {
        return this.f78e;
    }

    public String m114a(int i, String str, String str2) {
        Object a = ShareSDK.m20a(i, str);
        return (TextUtils.isEmpty(a) || "null".equals(a)) ? this.f74a.getDevinfo(this.f74a.getName(), str2) : a;
    }

    public String m115a(String str, boolean z) {
        return (this.f81h && !TextUtils.isEmpty(str)) ? C0044b.m100a(this.f75b).m104a(str, z, this.f74a.getPlatformId(), m112k()) : str;
    }

    public void m116a(int i, int i2, String str) {
        m126b(2, new Object[]{Integer.valueOf(i), Integer.valueOf(i2), str});
    }

    public void m117a(int i, Object obj) {
        this.f77d.m1848a(this.f74a, i, obj);
    }

    public void m118a(ShareParams shareParams) {
        if (shareParams != null) {
            m126b(9, shareParams);
        } else if (this.f77d != null) {
            this.f77d.onError(this.f74a, 9, new NullPointerException());
        }
    }

    public void m119a(PlatformActionListener platformActionListener) {
        this.f77d.m1849a(platformActionListener);
    }

    public void m120a(String str) {
        try {
            this.f78e = Integer.parseInt(ShareSDK.m21a(str, "Id").trim());
        } catch (Throwable th) {
            System.err.println(this.f74a.getName() + " failed to parse Id, this will cause method getId() always returens 0");
        }
        try {
            this.f79f = Integer.parseInt(ShareSDK.m21a(str, "SortId").trim());
        } catch (Throwable th2) {
            System.err.println(this.f74a.getName() + " failed to parse SortId, this won't cause any problem, don't worry");
        }
        String a = ShareSDK.m21a(str, "Enable");
        if (a == null) {
            this.f82i = true;
            System.err.println(this.f74a.getName() + " failed to parse Enable, this will cause platform always be enable");
        } else {
            this.f82i = "true".equals(a.trim());
        }
        this.f74a.m6a(str);
    }

    public void m121a(String str, int i, int i2) {
        m126b(7, new Object[]{Integer.valueOf(i), Integer.valueOf(i2), str});
    }

    public void m122a(String str, String str2, short s, HashMap<String, Object> hashMap, HashMap<String, String> hashMap2) {
        m126b(655360 | s, new Object[]{str, str2, hashMap, hashMap2});
    }

    public void m123a(boolean z) {
        this.f80g = z;
    }

    public void m124a(String[] strArr) {
        new C0046g(this, strArr).start();
    }

    public int m125b() {
        return this.f79f;
    }

    protected void m126b(int i, Object obj) {
        new C0047h(this, i, obj).start();
    }

    public void m127b(String str) {
        m126b(6, str);
    }

    public PlatformActionListener m128c() {
        return this.f77d.m1847a();
    }

    protected void m129c(int i, Object obj) {
        Object[] objArr;
        switch (i) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                if (this.f77d != null) {
                    this.f77d.onComplete(this.f74a, 1, null);
                }
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                objArr = (Object[]) obj;
                this.f74a.m11b(((Integer) objArr[0]).intValue(), ((Integer) objArr[1]).intValue(), (String) objArr[2]);
            case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                this.f74a.m13b((String) obj);
            case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                objArr = (Object[]) obj;
                this.f74a.m4a(((Integer) objArr[0]).intValue(), ((Integer) objArr[1]).intValue(), (String) objArr[2]);
            case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                this.f74a.m15c(obj == null ? null : (String) obj);
            case FileOptions.OPTIMIZE_FOR_FIELD_NUMBER /*9*/:
                this.f74a.m5a((ShareParams) obj);
            default:
                objArr = (Object[]) obj;
                this.f74a.m7a(String.valueOf(objArr[0]), String.valueOf(objArr[1]), i, (HashMap) objArr[2], (HashMap) objArr[3]);
        }
    }

    public void m130c(String str) {
        m126b(8, str);
    }

    public boolean m131d() {
        return this.f76c.isValid();
    }

    public boolean m132e() {
        return this.f80g;
    }

    public boolean m133f() {
        return this.f82i;
    }

    public PlatformDb m134g() {
        return this.f76c;
    }

    public void m135h() {
        this.f76c.removeAccount();
    }

    protected PlatformActionListener m136i() {
        return this.f77d;
    }
}
