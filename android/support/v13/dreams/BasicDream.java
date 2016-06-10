package android.support.v13.dreams;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import org.xbill.DNS.KEYRecord.Flags;

public class BasicDream extends Activity {
    private static final boolean DEBUG = true;
    private static final String TAG = "BasicDream";
    private boolean mPlugged;
    private final BroadcastReceiver mPowerIntentReceiver;
    private View mView;

    /* renamed from: android.support.v13.dreams.BasicDream.1 */
    class C00001 extends BroadcastReceiver {
        C00001() {
        }

        public void onReceive(Context context, Intent intent) {
            boolean plugged = BasicDream.DEBUG;
            if ("android.intent.action.BATTERY_CHANGED".equals(intent.getAction())) {
                if (1 != intent.getIntExtra("plugged", 0)) {
                    plugged = false;
                }
                if (plugged != BasicDream.this.mPlugged) {
                    Log.d(BasicDream.TAG, "now " + (plugged ? "plugged in" : "unplugged"));
                    BasicDream.this.mPlugged = plugged;
                    if (BasicDream.this.mPlugged) {
                        BasicDream.this.getWindow().addFlags(Flags.FLAG8);
                    } else {
                        BasicDream.this.getWindow().clearFlags(Flags.FLAG8);
                    }
                }
            }
        }
    }

    class BasicDreamView extends View {
        public BasicDreamView(Context c) {
            super(c);
        }

        public BasicDreamView(Context c, AttributeSet at) {
            super(c, at);
        }

        public void onAttachedToWindow() {
            setSystemUiVisibility(1);
        }

        public void onDraw(Canvas c) {
            BasicDream.this.onDraw(c);
        }
    }

    public BasicDream() {
        this.mPlugged = false;
        this.mPowerIntentReceiver = new C00001();
    }

    public void onStart() {
        super.onStart();
        setContentView(new BasicDreamView(this));
        getWindow().addFlags(524289);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.BATTERY_CHANGED");
        registerReceiver(this.mPowerIntentReceiver, filter);
    }

    public void onPause() {
        super.onPause();
        Log.d(TAG, "exiting onPause");
        finish();
    }

    public void onStop() {
        super.onStop();
        unregisterReceiver(this.mPowerIntentReceiver);
    }

    protected View getContentView() {
        return this.mView;
    }

    public void setContentView(View v) {
        super.setContentView(v);
        this.mView = v;
    }

    protected void invalidate() {
        getContentView().invalidate();
    }

    public void onDraw(Canvas c) {
    }

    public void onUserInteraction() {
        Log.d(TAG, "exiting onUserInteraction");
        finish();
    }
}
