package cn.sharesdk.framework;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import cn.sharesdk.framework.utils.C0058e;
import java.util.HashMap;

public class ShareSDKUIShell extends Activity {
    private static HashMap<String, FakeActivity> f7a;
    private FakeActivity f8b;

    static {
        f7a = new HashMap();
    }

    public static String m26a(FakeActivity fakeActivity) {
        String valueOf = String.valueOf(System.currentTimeMillis());
        f7a.put(valueOf, fakeActivity);
        return valueOf;
    }

    public void finish() {
        if (this.f8b == null || !this.f8b.onFinish()) {
            super.finish();
        }
    }

    protected void onActivityResult(int i, int i2, Intent intent) {
        if (this.f8b != null) {
            this.f8b.onActivityResult(i, i2, intent);
        }
        super.onActivityResult(i, i2, intent);
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (this.f8b != null) {
            this.f8b.onConfigurationChanged(configuration);
        }
    }

    protected void onCreate(Bundle bundle) {
        String stringExtra = getIntent().getStringExtra("launch_time");
        this.f8b = (FakeActivity) f7a.remove(stringExtra);
        if (this.f8b == null) {
            C0058e.m220b(new RuntimeException("Executor lost! launchTime = " + stringExtra));
            super.onCreate(bundle);
            finish();
            return;
        }
        this.f8b.setActivity(this);
        super.onCreate(bundle);
        this.f8b.onCreate();
    }

    protected void onDestroy() {
        if (this.f8b != null) {
            this.f8b.sendResult();
            this.f8b.onDestroy();
        }
        super.onDestroy();
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        boolean z = false;
        if (this.f8b != null) {
            z = this.f8b.onKeyEvent(i, keyEvent);
        }
        return z ? true : super.onKeyDown(i, keyEvent);
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        boolean z = false;
        if (this.f8b != null) {
            z = this.f8b.onKeyEvent(i, keyEvent);
        }
        return z ? true : super.onKeyUp(i, keyEvent);
    }

    protected void onNewIntent(Intent intent) {
        if (this.f8b == null) {
            super.onNewIntent(intent);
        } else {
            this.f8b.onNewIntent(intent);
        }
    }

    protected void onPause() {
        if (this.f8b != null) {
            this.f8b.onPause();
        }
        super.onPause();
    }

    protected void onRestart() {
        if (this.f8b != null) {
            this.f8b.onRestart();
        }
        super.onRestart();
    }

    protected void onResume() {
        if (this.f8b != null) {
            this.f8b.onResume();
        }
        super.onResume();
    }

    protected void onStart() {
        if (this.f8b != null) {
            this.f8b.onStart();
        }
        super.onStart();
    }

    protected void onStop() {
        if (this.f8b != null) {
            this.f8b.onStop();
        }
        super.onStop();
    }

    public void setContentView(int i) {
        setContentView(LayoutInflater.from(this).inflate(i, null));
    }

    public void setContentView(View view) {
        super.setContentView(view);
        if (this.f8b != null) {
            this.f8b.setContentView(view);
        }
    }

    public void setContentView(View view, LayoutParams layoutParams) {
        super.setContentView(view, layoutParams);
        if (this.f8b != null) {
            this.f8b.setContentView(view);
        }
    }
}
