package cn.sharesdk.incentive;

import android.text.TextUtils;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.Service;
import cn.sharesdk.framework.utils.C0055d;
import cn.sharesdk.framework.utils.C0058e;
import com.amap.mapapi.location.LocationManagerProxy;
import com.cnlaunch.framework.network.async.AsyncTaskManager;
import com.ifoer.ui.FastDiagnosisActivity;
import java.util.HashMap;

public class Incentive extends Service {
    static OnRewardListener f1783a;
    private C0075l f1784b;

    private void m1860b(String str) {
        if (!TextUtils.isEmpty(str)) {
            HashMap a = new C0055d().m212a(str);
            if (AsyncTaskManager.REQUEST_SUCCESS_CODE == ((Integer) a.get(LocationManagerProxy.KEY_STATUS_CHANGED)).intValue()) {
                int intValue = ((Integer) ((HashMap) a.get("res")).get("award")).intValue();
                if (intValue > 0 && f1783a != null) {
                    f1783a.onReward(intValue);
                }
            }
        }
    }

    public static void setOnRewardListener(OnRewardListener onRewardListener) {
        f1783a = onRewardListener;
    }

    String m1861a() {
        try {
            String a = this.f1784b.m245a();
            if (!TextUtils.isEmpty(a)) {
                HashMap a2 = new C0055d().m212a(a);
                if (AsyncTaskManager.REQUEST_SUCCESS_CODE == ((Integer) a2.get(LocationManagerProxy.KEY_STATUS_CHANGED)).intValue()) {
                    return String.valueOf(((HashMap) a2.get("res")).get("url"));
                }
            }
        } catch (Throwable th) {
            C0058e.m220b(th);
        }
        return null;
    }

    protected int getServiceVersionInt() {
        return FastDiagnosisActivity.PHOTO_ALBUM_REQUEST_CODE;
    }

    public String getServiceVersionName() {
        return "1.0.0";
    }

    public void onBind() {
        this.f1784b = new C0075l(this);
        new C0069c(this).start();
    }

    public void rewardSharing(int i, ShareParams shareParams, HashMap<String, Object> hashMap) {
        C1032m c1032m = new C1032m(this);
        c1032m.m1871a(i);
        c1032m.m1872a(shareParams, hashMap);
        new C0070d(this, c1032m).start();
    }

    public void showIncentivePage() {
        showIncentivePage(null);
    }

    public void showIncentivePage(IncentivePageAdapter incentivePageAdapter) {
        C1029e c1029e = new C1029e();
        c1029e.m1867a(incentivePageAdapter);
        c1029e.show(getContext(), null);
    }
}
