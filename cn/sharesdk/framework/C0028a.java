package cn.sharesdk.framework;

import android.content.Context;
import android.content.Intent;
import android.os.Handler.Callback;
import android.os.Message;

/* renamed from: cn.sharesdk.framework.a */
class C0028a implements Callback {
    final /* synthetic */ FakeActivity f17a;

    C0028a(FakeActivity fakeActivity) {
        this.f17a = fakeActivity;
    }

    public boolean handleMessage(Message message) {
        Object[] objArr = (Object[]) message.obj;
        Context context = (Context) objArr[0];
        Intent intent = (Intent) objArr[1];
        intent.setFlags(268435456);
        context.startActivity(intent);
        return false;
    }
}
