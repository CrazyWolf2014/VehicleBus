package com.paypal.android.MEP;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import com.paypal.android.MEP.p020a.C1099a;
import com.paypal.android.MEP.p020a.C1099a.C0803a;
import com.paypal.android.MEP.p020a.C1100b;
import com.paypal.android.MEP.p020a.C1101c;
import com.paypal.android.MEP.p020a.C1102d;
import com.paypal.android.MEP.p020a.C1102d.C08051;
import com.paypal.android.MEP.p020a.C1102d.C0806a;
import com.paypal.android.MEP.p020a.C1103e;
import com.paypal.android.MEP.p020a.C1104f;
import com.paypal.android.MEP.p020a.C1105g;
import com.paypal.android.MEP.p020a.C1105g.C0810a;
import com.paypal.android.MEP.p020a.C1106h;
import com.paypal.android.MEP.p021b.C0817f;
import com.paypal.android.p022a.C0839h;
import com.paypal.android.p022a.C1107b;
import com.paypal.android.p024b.C0858j;
import com.paypal.android.p024b.C0859l;
import java.util.Hashtable;
import java.util.Vector;

public final class PayPalActivity extends Activity implements AnimationListener {
    public static String AUTH_SUCCESS = null;
    public static String CREATE_PAYMENT_FAIL = null;
    public static String CREATE_PAYMENT_SUCCESS = null;
    public static final String EXTRA_CORRELATION_ID = "com.paypal.android.CORRELATION_ID";
    public static final String EXTRA_ERROR_ID = "com.paypal.android.ERROR_ID";
    public static final String EXTRA_ERROR_MESSAGE = "com.paypal.android.ERROR_MESSAGE";
    public static final String EXTRA_PAYMENT_ADJUSTER = "com.paypal.android.PAYMENT_ADJUSTER";
    public static final String EXTRA_PAYMENT_INFO = "com.paypal.android.PAYPAL_PAYMENT";
    public static final String EXTRA_PAYMENT_STATUS = "com.paypal.android.PAYMENT_STATUS";
    public static final String EXTRA_PAY_KEY = "com.paypal.android.PAY_KEY";
    public static final String EXTRA_PREAPPROVAL_INFO = "com.paypal.android.PAYPAL_PREAPPROVAL";
    public static final String EXTRA_RESULT_DELEGATE = "com.paypal.android.RESULT_DELEGATE";
    public static String FATAL_ERROR = null;
    public static String LOGIN_FAIL = null;
    public static String LOGIN_SUCCESS = null;
    public static final int RESULT_FAILURE = 2;
    public static final int VIEW_ABOUT = 2;
    public static final int VIEW_CREATE_PIN = 7;
    public static final int VIEW_FATAL_ERROR = 5;
    public static final int VIEW_INFO = 1;
    public static final int VIEW_LOGIN = 0;
    public static final int VIEW_NUM_VIEWS = 9;
    public static final int VIEW_PREAPPROVAL = 6;
    public static final int VIEW_REVIEW = 3;
    public static final int VIEW_SUCCESS = 4;
    public static final int VIEW_TEST = 8;
    public static C1107b _networkHandler;
    public static PayPal _paypal;
    public static String _popIntent;
    public static String _pushIntent;
    public static String _replaceIntent;
    public static String _updateIntent;
    private static PayPalActivity f1401c;
    private static String f1402h;
    private PaymentAdjuster f1403a;
    private PayPalResultDelegate f1404b;
    private Vector<C0858j> f1405d;
    private Animation f1406e;
    private Intent f1407f;
    private boolean f1408g;
    private BroadcastReceiver f1409i;
    private BroadcastReceiver f1410j;
    public boolean transactionSuccessful;

    static {
        f1401c = null;
        _networkHandler = null;
        f1402h = null;
    }

    public PayPalActivity() {
        this.f1407f = null;
        this.f1408g = false;
        this.transactionSuccessful = false;
        this.f1409i = new C0819c(this);
        this.f1410j = new C0818b(this);
    }

    private boolean m1485a(int i) {
        View c1102d;
        if (i == 0) {
            C1107b.m2383e().m2419a("mpl-login");
            c1102d = new C1102d(this);
        } else if (i == VIEW_INFO) {
            C1107b.m2383e().m2419a("mpl-help-binding");
            c1102d = new C1100b(this);
        } else if (i == VIEW_ABOUT) {
            C1107b.m2383e().m2419a("mpl-help");
            c1102d = new C1104f(this);
        } else if (i == VIEW_REVIEW) {
            c1102d = new C1099a(this);
        } else if (i == VIEW_SUCCESS) {
            c1102d = new C1106h(this);
        } else if (i == VIEW_FATAL_ERROR) {
            c1102d = new C1101c(this, this.f1407f);
        } else if (i == VIEW_PREAPPROVAL) {
            c1102d = new C1105g(this);
        } else if (i != VIEW_CREATE_PIN) {
            return false;
        } else {
            C1107b.m2383e().m2419a("mpl-create-PIN");
            c1102d = new C1103e(this);
        }
        C0858j c0858j = this.f1405d.size() > 0 ? (C0858j) this.f1405d.lastElement() : null;
        setContentView(c1102d);
        this.f1405d.add(c1102d);
        if (c0858j != null) {
            c0858j.m1636a();
        }
        if (i == 0) {
            Animation scaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, VIEW_ABOUT, 0.5f, VIEW_ABOUT, 0.5f);
            scaleAnimation.setDuration(500);
            scaleAnimation.setRepeatCount(VIEW_LOGIN);
            scaleAnimation.setAnimationListener(this);
            c1102d.setAnimation(scaleAnimation);
            if (f1402h != null) {
                ((C1102d) c1102d).m2322a(C0806a.STATE_LOGGING_OUT);
                ((C1102d) c1102d).m2327d(f1402h);
                f1402h = null;
            }
        }
        return true;
    }

    static /* synthetic */ boolean m1486a(PayPalActivity payPalActivity) {
        C0858j c0858j = (C0858j) f1401c.f1405d.lastElement();
        if (payPalActivity.f1405d.size() != VIEW_INFO) {
            payPalActivity.f1405d.remove(c0858j);
            C0858j c0858j2 = (C0858j) payPalActivity.f1405d.lastElement();
            if (c0858j2 != null) {
                payPalActivity.setContentView(c0858j2);
            }
        }
        c0858j.m1636a();
        return true;
    }

    private void m1488b() {
        String appID = PayPal.getInstance().getAppID();
        _pushIntent = appID + "PUSH_DIALOG_";
        _popIntent = appID + "POP_DIALOG";
        _replaceIntent = appID + "REPLACE_DIALOG_";
        _updateIntent = appID + "UPDATE_VIEW";
        LOGIN_SUCCESS = appID + "LOGIN_SUCCESS";
        LOGIN_FAIL = appID + "LOGIN_FAIL";
        AUTH_SUCCESS = appID + "AUTH_SUCCESS";
        CREATE_PAYMENT_SUCCESS = appID + "CREATE_PAYMENT_SUCCESS";
        CREATE_PAYMENT_FAIL = appID + "CREATE_PAYMENT_FAIL";
        FATAL_ERROR = appID + "FATAL_ERROR";
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(_popIntent);
        for (int i = VIEW_LOGIN; i < VIEW_NUM_VIEWS; i += VIEW_INFO) {
            intentFilter.addAction(_pushIntent + i);
            intentFilter.addAction(_replaceIntent + i);
        }
        intentFilter.addAction(_updateIntent);
        registerReceiver(this.f1410j, intentFilter);
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction(LOGIN_SUCCESS);
        intentFilter2.addAction(LOGIN_FAIL);
        intentFilter2.addAction(CREATE_PAYMENT_SUCCESS);
        intentFilter2.addAction(CREATE_PAYMENT_FAIL);
        intentFilter2.addAction(FATAL_ERROR);
        registerReceiver(this.f1409i, intentFilter2);
        C1107b.m2380c();
    }

    static /* synthetic */ boolean m1490b(PayPalActivity payPalActivity, int i) {
        C0858j c0858j = (C0858j) payPalActivity.f1405d.lastElement();
        if (!payPalActivity.m1485a(i)) {
            return false;
        }
        payPalActivity.f1405d.remove(c0858j);
        return true;
    }

    private void m1491c() {
        if (this.f1405d == null || this.f1405d.size() <= 0) {
            finish();
            f1401c = null;
            return;
        }
        C0858j c0858j = (C0858j) this.f1405d.lastElement();
        this.f1406e = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f, VIEW_ABOUT, 0.5f, VIEW_ABOUT, 0.5f);
        this.f1406e.setDuration(500);
        this.f1406e.setRepeatCount(VIEW_LOGIN);
        this.f1406e.setAnimationListener(this);
        c0858j.setAnimation(this.f1406e);
        runOnUiThread(new C0859l(c0858j, this.f1406e));
    }

    public static PayPalActivity getInstance() {
        return f1401c;
    }

    public final MEPAmounts AdjustAmounts(MEPAddress mEPAddress, String str, String str2, String str3, String str4) {
        return this.f1403a != null ? this.f1403a.adjustAmount(mEPAddress, str, str2, str3, str4) : null;
    }

    public final Vector<MEPReceiverAmounts> adjustAmountsAdvanced(MEPAddress mEPAddress, String str, Vector<MEPReceiverAmounts> vector) {
        return this.f1403a != null ? this.f1403a.adjustAmountsAdvanced(mEPAddress, str, vector) : null;
    }

    public final C0858j getDialog() {
        return (C0858j) this.f1405d.lastElement();
    }

    public final void onActivityResult(int i, int i2, Intent intent) {
        if (i == VIEW_REVIEW) {
            setResult(i2, intent);
            m1491c();
        }
    }

    public final void onAnimationEnd(Animation animation) {
        if (animation == this.f1406e) {
            f1401c.finish();
            f1401c = null;
        }
    }

    public final void onAnimationRepeat(Animation animation) {
    }

    public final void onAnimationStart(Animation animation) {
    }

    protected final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.f1408g = false;
        Hashtable hashtable = (Hashtable) getLastNonConfigurationInstance();
        if (hashtable != null) {
            _paypal = (PayPal) hashtable.get("PayPal");
            Vector vector = (Vector) hashtable.get("ViewStack");
            this.f1405d = new Vector(VIEW_LOGIN);
            _networkHandler = (C1107b) hashtable.get("NetworkHandler");
            if (hashtable.get("ReviewViewInfo") != null) {
                C1099a.f2141a = (Hashtable) hashtable.get("ReviewViewInfo");
            }
            m1488b();
            int i = VIEW_LOGIN;
            Object obj = null;
            while (i < vector.size()) {
                int intValue = ((Integer) vector.elementAt(i)).intValue();
                Object c1102d = intValue == 0 ? new C1102d(this) : intValue == VIEW_INFO ? new C1100b(this) : intValue == VIEW_ABOUT ? new C1104f(this) : intValue == VIEW_REVIEW ? new C1099a(this) : intValue == VIEW_SUCCESS ? new C1106h(this) : intValue == VIEW_FATAL_ERROR ? new C1101c(this) : intValue == VIEW_PREAPPROVAL ? new C1105g(this) : intValue == VIEW_CREATE_PIN ? new C1103e(this) : obj;
                this.f1405d.add(c1102d);
                i += VIEW_INFO;
                obj = c1102d;
            }
            Editable editable = (Editable) hashtable.get("UserString");
            EditText editText = (EditText) findViewById(5004);
            if (!(editText == null || editable == null || editable.length() <= 0)) {
                editText.setText(editable);
            }
            Editable editable2 = (Editable) hashtable.get("PasswordString");
            EditText editText2 = (EditText) findViewById(5005);
            if (!(editText2 == null || editable2 == null || editable2.length() <= 0)) {
                editText2.setText(editable2);
            }
            setContentView((View) this.f1405d.lastElement());
            this.f1405d.lastElement();
            f1401c = this;
            return;
        }
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_PAYMENT_INFO) || intent.hasExtra(EXTRA_PREAPPROVAL_INFO)) {
            if (intent.hasExtra(EXTRA_PAYMENT_ADJUSTER)) {
                this.f1403a = (PaymentAdjuster) intent.getSerializableExtra(EXTRA_PAYMENT_ADJUSTER);
            }
            if (intent.hasExtra(EXTRA_RESULT_DELEGATE)) {
                this.f1404b = (PayPalResultDelegate) intent.getSerializableExtra(EXTRA_RESULT_DELEGATE);
            }
            m1488b();
            PayPal instance = PayPal.getInstance();
            if (instance.getPayType() == VIEW_REVIEW) {
                PayPalPreapproval payPalPreapproval = (PayPalPreapproval) intent.getSerializableExtra(EXTRA_PREAPPROVAL_INFO);
                if (payPalPreapproval == null) {
                    paymentFailed((String) C1107b.m2383e().m2421c("CorrelationId"), (String) C1107b.m2383e().m2421c("PayKey"), "-1", C0839h.m1568a("ANDROID_no_payment"), true, true);
                    return;
                } else if (!payPalPreapproval.isValid() || instance.getPreapprovalKey() == null || instance.getPreapprovalKey().length() <= 0) {
                    paymentFailed((String) C1107b.m2383e().m2421c("CorrelationId"), (String) C1107b.m2383e().m2421c("PayKey"), "-1", C0839h.m1568a("ANDROID_invalid_payment"), true, true);
                    return;
                } else if (!C1107b.m2369a()) {
                    paymentFailed((String) C1107b.m2383e().m2421c("CorrelationId"), (String) C1107b.m2383e().m2421c("PayKey"), "-1", C0839h.m1568a("ANDROID_application_not_authorized"), true, true);
                    return;
                }
            }
            PayPalAdvancedPayment payPalAdvancedPayment = (PayPalAdvancedPayment) intent.getSerializableExtra(EXTRA_PAYMENT_INFO);
            if (payPalAdvancedPayment == null) {
                paymentFailed((String) C1107b.m2383e().m2421c("CorrelationId"), (String) C1107b.m2383e().m2421c("PayKey"), "-1", C0839h.m1568a("ANDROID_no_payment"), true, true);
                return;
            } else if (!payPalAdvancedPayment.isValid()) {
                paymentFailed((String) C1107b.m2383e().m2421c("CorrelationId"), (String) C1107b.m2383e().m2421c("PayKey"), "-1", C0839h.m1568a("ANDROID_invalid_payment"), true, true);
                return;
            } else if (!C1107b.m2369a()) {
                paymentFailed((String) C1107b.m2383e().m2421c("CorrelationId"), (String) C1107b.m2383e().m2421c("PayKey"), "-1", C0839h.m1568a("ANDROID_application_not_authorized"), true, true);
                return;
            }
            f1401c = this;
            if (this.f1405d != null) {
                this.f1405d.setSize(VIEW_LOGIN);
            } else {
                this.f1405d = new Vector(VIEW_LOGIN);
            }
            C08051.m1497a(VIEW_LOGIN);
            return;
        }
        throw new NullPointerException("PayPalPayment/Preapproval object does not exist in intent");
    }

    protected final void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(this.f1410j);
            unregisterReceiver(this.f1409i);
        } catch (Exception e) {
        }
    }

    public final boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i != VIEW_SUCCESS) {
            return super.onKeyDown(i, keyEvent);
        }
        if (this.f1405d.lastElement() instanceof C1102d) {
            if (((C1102d) this.f1405d.lastElement()).m2326c() != C0806a.STATE_LOGGING_IN) {
                new C0817f(this).show();
            }
            return true;
        } else if (this.f1405d.lastElement() instanceof C1099a) {
            if (!(((C1099a) this.f1405d.lastElement()).m2306c() == C0803a.STATE_SENDING_PAYMENT || ((C1099a) this.f1405d.lastElement()).m2306c() == C0803a.STATE_UPDATING)) {
                new C0817f(this).show();
            }
            return true;
        } else if (this.f1405d.lastElement() instanceof C1105g) {
            if (((C1105g) this.f1405d.lastElement()).m2353c() != C0810a.STATE_CONFIRM_PREAPPROVAL) {
                new C0817f(this).show();
            }
            return true;
        } else if ((this.f1405d.lastElement() instanceof C1104f) || (this.f1405d.lastElement() instanceof C1100b)) {
            C08051.m1496a();
            return true;
        } else if (!(this.f1405d.lastElement() instanceof C1106h) && !(this.f1405d.lastElement() instanceof C1103e)) {
            return super.onKeyDown(i, keyEvent);
        } else {
            if (!this.f1408g) {
                this.f1408g = true;
                paymentSucceeded((String) _networkHandler.m2421c("PayKey"), (String) _networkHandler.m2421c("PaymentExecStatus"), true);
            }
            return true;
        }
    }

    public final Object onRetainNonConfigurationInstance() {
        Editable text;
        Hashtable hashtable = new Hashtable();
        hashtable.put("PayPal", _paypal);
        Vector vector = new Vector();
        for (int i = VIEW_LOGIN; i < this.f1405d.size(); i += VIEW_INFO) {
            C0858j c0858j = (C0858j) this.f1405d.elementAt(i);
            int i2 = c0858j instanceof C1102d ? VIEW_LOGIN : c0858j instanceof C1100b ? VIEW_INFO : c0858j instanceof C1104f ? VIEW_ABOUT : c0858j instanceof C1099a ? VIEW_REVIEW : c0858j instanceof C1106h ? VIEW_SUCCESS : c0858j instanceof C1101c ? VIEW_FATAL_ERROR : c0858j instanceof C1105g ? VIEW_PREAPPROVAL : c0858j instanceof C1103e ? VIEW_CREATE_PIN : VIEW_LOGIN;
            vector.add(new Integer(i2));
        }
        hashtable.put("ViewStack", vector);
        hashtable.put("NetworkHandler", _networkHandler);
        if (C1099a.f2141a != null) {
            hashtable.put("ReviewViewInfo", C1099a.f2141a);
        }
        EditText editText = (EditText) findViewById(5004);
        if (editText != null) {
            text = editText.getText();
            if (text != null && text.length() > 0) {
                hashtable.put("UserString", text);
            }
        }
        editText = (EditText) findViewById(5005);
        if (editText != null) {
            text = editText.getText();
            if (text != null && text.length() > 0) {
                hashtable.put("PasswordString", text);
            }
        }
        return hashtable;
    }

    public final void paymentCanceled() {
        if (this.transactionSuccessful) {
            paymentSucceeded((String) C1107b.m2383e().m2421c("PayKey"), (String) C1107b.m2383e().m2421c("PaymentExecStatus"));
            return;
        }
        if (this.f1404b != null) {
            this.f1404b.onPaymentCanceled("OTHER");
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PAYMENT_STATUS, "OTHER");
        setResult(VIEW_LOGIN, intent);
        m1491c();
    }

    public final void paymentFailed(String str, String str2, String str3, String str4, boolean z, boolean z2) {
        if (this.transactionSuccessful) {
            paymentSucceeded((String) C1107b.m2383e().m2421c("PayKey"), (String) C1107b.m2383e().m2421c("PaymentExecStatus"));
            return;
        }
        if (this.f1404b != null && z2) {
            this.f1404b.onPaymentFailed("OTHER", str, str2, str3, str4);
        }
        if (z) {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_CORRELATION_ID, str);
            intent.putExtra(EXTRA_PAY_KEY, str2);
            intent.putExtra(EXTRA_ERROR_ID, str3);
            intent.putExtra(EXTRA_ERROR_MESSAGE, str4);
            intent.putExtra(EXTRA_PAYMENT_STATUS, "OTHER");
            setResult(VIEW_ABOUT, intent);
            m1491c();
        }
    }

    public final void paymentSucceeded(String str, String str2) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_PAY_KEY, str);
        intent.putExtra(EXTRA_PAYMENT_STATUS, str2);
        setResult(-1, intent);
        m1491c();
    }

    public final void paymentSucceeded(String str, String str2, boolean z) {
        if (this.f1404b != null) {
            this.f1404b.onPaymentSucceeded(str, str2);
        }
        if (z) {
            paymentSucceeded(str, str2);
        }
    }

    public final void setTransactionSuccessful(boolean z) {
        this.transactionSuccessful = z;
    }
}
