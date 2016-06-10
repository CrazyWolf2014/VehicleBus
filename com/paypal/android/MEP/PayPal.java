package com.paypal.android.MEP;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.paypal.android.p022a.C0838g;
import com.paypal.android.p022a.C0839h;
import com.paypal.android.p022a.C0842m;
import com.paypal.android.p022a.C1107b;
import com.paypal.android.p025c.C0860a;
import java.io.Serializable;
import java.util.Locale;
import junit.framework.Assert;
import org.xmlpull.v1.XmlPullParser;

public final class PayPal {
    public static final int AUTH_SETTING_DISABLED = 0;
    public static final int AUTH_SETTING_ENABLED = 1;
    public static final int BUTTON_152x33 = 0;
    public static final int BUTTON_194x37 = 1;
    public static final int BUTTON_278x43 = 2;
    public static final int BUTTON_294x45 = 3;
    public static final int ENV_LIVE = 1;
    public static final int ENV_NONE = 2;
    public static final int ENV_SANDBOX = 0;
    public static final int FEEPAYER_EACHRECEIVER = 0;
    public static final int FEEPAYER_PRIMARYRECEIVER = 2;
    public static final int FEEPAYER_SECONDARYONLY = 3;
    public static final int FEEPAYER_SENDER = 1;
    public static final int LOGIN_VIA_DRT = 2;
    public static final int LOGIN_VIA_EMAIL = 0;
    public static final int LOGIN_VIA_EMAIL_EBAY_USER = 3;
    public static final int LOGIN_VIA_PHONE = 1;
    public static final int NUM_STYLES = 4;
    public static final int PAYMENT_SUBTYPE_AFFILIATE = 0;
    public static final int PAYMENT_SUBTYPE_B2B = 1;
    public static final int PAYMENT_SUBTYPE_CHILDCARE = 15;
    public static final int PAYMENT_SUBTYPE_CONTRACTORS = 17;
    public static final int PAYMENT_SUBTYPE_DONATIONS = 6;
    public static final int PAYMENT_SUBTYPE_ENTERTAINMENT = 18;
    public static final int PAYMENT_SUBTYPE_EVENTS = 16;
    public static final int PAYMENT_SUBTYPE_GOVERNMENT = 9;
    public static final int PAYMENT_SUBTYPE_INSURANCE = 10;
    public static final int PAYMENT_SUBTYPE_INVOICE = 20;
    public static final int PAYMENT_SUBTYPE_MEDICAL = 14;
    public static final int PAYMENT_SUBTYPE_MORTGAGE = 13;
    public static final int PAYMENT_SUBTYPE_NONE = 22;
    public static final int PAYMENT_SUBTYPE_PAYROLL = 2;
    public static final int PAYMENT_SUBTYPE_REBATES = 3;
    public static final int PAYMENT_SUBTYPE_REFUNDS = 4;
    public static final int PAYMENT_SUBTYPE_REIMBURSEMENTS = 5;
    public static final int PAYMENT_SUBTYPE_REIMBUSEMENTS = 5;
    public static final int PAYMENT_SUBTYPE_REMITTANCES = 11;
    public static final int PAYMENT_SUBTYPE_RENT = 12;
    public static final int PAYMENT_SUBTYPE_TOURISM = 19;
    public static final int PAYMENT_SUBTYPE_TRANSFER = 21;
    public static final int PAYMENT_SUBTYPE_TUITION = 8;
    public static final int PAYMENT_SUBTYPE_UTILITIES = 7;
    public static final int PAYMENT_TYPE_GOODS = 0;
    public static final int PAYMENT_TYPE_NONE = 3;
    public static final int PAYMENT_TYPE_PERSONAL = 2;
    public static final int PAYMENT_TYPE_SERVICE = 1;
    public static final int PAY_TYPE_CHAINED = 2;
    public static final int PAY_TYPE_PARALLEL = 1;
    public static final int PAY_TYPE_PREAPPROVAL = 3;
    public static final int PAY_TYPE_SIMPLE = 0;
    private static final String[] f1392a;
    private static final String[] f1393b;
    private static C0860a f1394f;
    private C0798a f1395c;
    private final C0799b f1396d;
    private Boolean f1397e;
    private boolean f1398g;
    private String f1399h;
    private String f1400i;

    /* renamed from: com.paypal.android.MEP.PayPal.a */
    private class C0798a {
        protected PayPalAdvancedPayment f1371a;
        protected PayPalPreapproval f1372b;
        protected CheckoutButton f1373c;
        protected Context f1374d;
        protected String f1375e;
        protected String f1376f;
        protected String f1377g;
        protected String f1378h;
        protected String f1379i;
        protected int f1380j;
        protected int f1381k;
        protected int f1382l;
        protected boolean f1383m;
        protected boolean f1384n;

        public C0798a(PayPal payPal) {
            this.f1378h = "https://www.paypal.com";
            this.f1379i = "https://www.paypal.com";
            this.f1371a = null;
            this.f1372b = null;
            this.f1373c = null;
        }
    }

    /* renamed from: com.paypal.android.MEP.PayPal.b */
    private class C0799b {
        protected String f1385a;
        protected String f1386b;
        protected String f1387c;
        protected int f1388d;
        protected int f1389e;
        protected boolean f1390f;
        final /* synthetic */ PayPal f1391g;

        public C0799b(PayPal payPal) {
            this.f1391g = payPal;
        }
    }

    static {
        String[] strArr = new String[PAYMENT_SUBTYPE_REFUNDS];
        strArr[PAYMENT_TYPE_GOODS] = "GOODS";
        strArr[PAY_TYPE_PARALLEL] = "SERVICE";
        strArr[PAY_TYPE_CHAINED] = "PERSONAL";
        strArr[PAY_TYPE_PREAPPROVAL] = "NONE";
        f1392a = strArr;
        f1393b = new String[]{"AFFILIATE_PAYMENTS", "B2B", "PAYROLL", "REBATES", "REFUNDS", "REIMBURSEMENTS", "DONATIONS", "UTILITIES", "TUITION", "GOVERNMENT", "INSURANCE", "REMITTANCES", "RENT", "MORTGAGE", "MEDICAL", "CHILD_CARE", "EVENT_PLANNING", "GENERAL_CONTRACTORS", "ENTERTAINMENT", "TOURISM", "INVOICE", "TRANSFER", "NONE"};
        f1394f = null;
    }

    private PayPal() {
        this.f1397e = Boolean.valueOf(false);
        this.f1398g = false;
        this.f1399h = null;
        this.f1400i = XmlPullParser.NO_NAMESPACE;
        this.f1395c = new C0798a(this);
        this.f1396d = new C0799b(this);
        resetAccount();
    }

    private static String m1481a(String str) {
        return str == null ? XmlPullParser.NO_NAMESPACE : str;
    }

    public static String getBuild() {
        String str = "1.5.5.45";
        return str.substring(str.lastIndexOf(46) + PAY_TYPE_PARALLEL);
    }

    public static PayPal getInstance() {
        return PayPalActivity._paypal;
    }

    public static String getPaySubtype(int i) {
        return f1393b[i];
    }

    public static String getPayType(int i) {
        return f1392a[i];
    }

    public static String getVersion() {
        return "1.5.5.45";
    }

    public static String getVersionWithoutBuild() {
        String str = "1.5.5.45";
        return str.substring(PAYMENT_TYPE_GOODS, str.lastIndexOf(46));
    }

    public static PayPal initWithAppID(Context context, String str, int i) throws IllegalStateException {
        if (PayPalActivity._paypal != null) {
            PayPalActivity._paypal.deinitialize();
        }
        PayPal payPal = new PayPal();
        PayPalActivity._paypal = payPal;
        payPal.f1395c.f1374d = context;
        PayPalActivity._paypal.f1395c.f1375e = str;
        PayPalActivity._paypal.f1395c.f1380j = i;
        PayPalActivity._paypal.f1395c.f1384n = false;
        PayPalActivity._paypal.f1395c.f1383m = true;
        C1107b.m2380c();
        payPal = PayPalActivity._paypal;
        C0838g.m1562a();
        Locale locale = Locale.getDefault();
        String str2 = locale.getLanguage() + '_' + locale.getCountry();
        payPal.setLanguage(str2);
        C0839h.m1573c(str2);
        if (!PayPalActivity._paypal.f1397e.booleanValue()) {
            synchronized (PayPalActivity._paypal) {
                try {
                    PayPalActivity._paypal.wait();
                } catch (InterruptedException e) {
                }
            }
        }
        return PayPalActivity._paypal;
    }

    public static int logd(String str, String str2) {
        return PAYMENT_TYPE_GOODS;
    }

    public static int loge(String str, String str2) {
        return Log.e(str, str2);
    }

    public final boolean canShowCart() {
        if (getPayType() == PAY_TYPE_PREAPPROVAL) {
            return false;
        }
        if (getPayment().getReceivers().size() == PAY_TYPE_PARALLEL) {
            PayPalReceiverDetails payPalReceiverDetails = (PayPalReceiverDetails) getPayment().getReceivers().get(PAYMENT_TYPE_GOODS);
            if (payPalReceiverDetails.getInvoiceData() == null) {
                return false;
            }
            PayPalInvoiceData invoiceData = payPalReceiverDetails.getInvoiceData();
            if (invoiceData.getTax() == null && invoiceData.getShipping() == null && invoiceData.getInvoiceItems().size() == 0) {
                return false;
            }
        }
        return true;
    }

    public final Intent checkout(PayPalAdvancedPayment payPalAdvancedPayment, Context context) {
        return checkout(payPalAdvancedPayment, context, null, null);
    }

    public final Intent checkout(PayPalAdvancedPayment payPalAdvancedPayment, Context context, PayPalResultDelegate payPalResultDelegate) {
        return checkout(payPalAdvancedPayment, context, null, payPalResultDelegate);
    }

    public final Intent checkout(PayPalAdvancedPayment payPalAdvancedPayment, Context context, PaymentAdjuster paymentAdjuster) {
        return checkout(payPalAdvancedPayment, context, paymentAdjuster, null);
    }

    public final Intent checkout(PayPalAdvancedPayment payPalAdvancedPayment, Context context, PaymentAdjuster paymentAdjuster, PayPalResultDelegate payPalResultDelegate) {
        this.f1395c.f1371a = payPalAdvancedPayment;
        this.f1395c.f1372b = null;
        Intent intent = new Intent(context, PayPalActivity.class);
        intent.putExtra(PayPalActivity.EXTRA_PAYMENT_INFO, payPalAdvancedPayment);
        if (paymentAdjuster != null) {
            intent.putExtra(PayPalActivity.EXTRA_PAYMENT_ADJUSTER, (Serializable) paymentAdjuster);
        }
        if (payPalResultDelegate != null) {
            intent.putExtra(PayPalActivity.EXTRA_RESULT_DELEGATE, (Serializable) payPalResultDelegate);
        }
        return intent;
    }

    public final Intent checkout(PayPalPayment payPalPayment, Context context) {
        return checkout(payPalPayment, context, null, null);
    }

    public final Intent checkout(PayPalPayment payPalPayment, Context context, PayPalResultDelegate payPalResultDelegate) {
        return checkout(payPalPayment, context, null, payPalResultDelegate);
    }

    public final Intent checkout(PayPalPayment payPalPayment, Context context, PaymentAdjuster paymentAdjuster) {
        return checkout(payPalPayment, context, paymentAdjuster, null);
    }

    public final Intent checkout(PayPalPayment payPalPayment, Context context, PaymentAdjuster paymentAdjuster, PayPalResultDelegate payPalResultDelegate) {
        PayPalAdvancedPayment payPalAdvancedPayment = new PayPalAdvancedPayment();
        payPalAdvancedPayment.setCurrencyType(payPalPayment.getCurrencyType());
        payPalAdvancedPayment.setIpnUrl(payPalPayment.getIpnUrl());
        payPalAdvancedPayment.setMemo(payPalPayment.getMemo());
        PayPalReceiverDetails payPalReceiverDetails = new PayPalReceiverDetails();
        payPalReceiverDetails.setRecipient(payPalPayment.getRecipient());
        payPalReceiverDetails.setSubtotal(payPalPayment.getSubtotal());
        payPalReceiverDetails.setInvoiceData(payPalPayment.getInvoiceData());
        payPalReceiverDetails.setPaymentType(payPalPayment.getPaymentType());
        payPalReceiverDetails.setPaymentSubtype(payPalPayment.getPaymentSubtype());
        payPalReceiverDetails.setDescription(payPalPayment.getMemo());
        payPalReceiverDetails.setCustomID(payPalPayment.getCustomID());
        payPalReceiverDetails.setMerchantName(payPalPayment.getMerchantName());
        payPalReceiverDetails.setIsPrimary(false);
        payPalAdvancedPayment.getReceivers().add(payPalReceiverDetails);
        return checkout(payPalAdvancedPayment, context, paymentAdjuster, payPalResultDelegate);
    }

    public final void deinitialize() {
        C1107b.m2381d();
        PayPalActivity._paypal = null;
        f1394f = null;
    }

    public final String getAccountCountryDialingCode() {
        return this.f1400i.length() > 0 ? this.f1400i : C1107b.m2400m();
    }

    public final String getAccountEmail() {
        return m1481a(this.f1396d.f1386b);
    }

    public final String getAccountName() {
        return m1481a(this.f1396d.f1385a);
    }

    public final String getAccountPhone() {
        return this.f1396d.f1387c;
    }

    public final String getAdjustPaymentError() {
        return this.f1399h == null ? C0839h.m1568a("ANDROID_calc_error") : this.f1399h;
    }

    public final String getAppID() {
        return this.f1395c.f1375e;
    }

    public final int getAuthMethod() {
        return this.f1396d.f1388d;
    }

    public final int getAuthSetting() {
        return this.f1396d.f1389e;
    }

    public final String getCancelUrl() {
        return this.f1395c.f1378h;
    }

    public final CheckoutButton getCheckoutButton(Context context, int i, int i2) {
        this.f1395c.f1373c = new CheckoutButton(context);
        this.f1395c.f1373c.m1480a(i, i2);
        this.f1395c.f1381k = i2;
        this.f1395c.f1373c.setActive(true);
        return this.f1395c.f1373c;
    }

    public final float getDensity() {
        return getParentContext().getResources().getDisplayMetrics().density;
    }

    public final String getDeviceReferenceToken() {
        return C0842m.m1593b();
    }

    public final boolean getDynamicAmountCalculationEnabled() {
        return this.f1395c.f1384n;
    }

    public final int getFeesPayer() {
        return this.f1395c.f1382l;
    }

    public final String getFlowContext() {
        return null;
    }

    public final boolean getIsRememberMe() {
        return this.f1398g;
    }

    public final String getLanguage() {
        return this.f1395c.f1377g;
    }

    public final Context getParentContext() {
        return this.f1395c.f1374d;
    }

    public final int getPayType() {
        return (this.f1395c.f1371a != null || this.f1395c.f1372b == null) ? this.f1395c.f1371a.getReceivers().size() == PAY_TYPE_PARALLEL ? PAYMENT_TYPE_GOODS : this.f1395c.f1371a.hasPrimaryReceiever() ? PAY_TYPE_CHAINED : PAY_TYPE_PARALLEL : PAY_TYPE_PREAPPROVAL;
    }

    public final PayPalAdvancedPayment getPayment() {
        return this.f1395c.f1371a;
    }

    public final PayPalPreapproval getPreapproval() {
        return this.f1395c.f1372b;
    }

    public final String getPreapprovalKey() {
        return this.f1395c.f1376f;
    }

    public final String getReturnUrl() {
        return this.f1395c.f1379i;
    }

    public final int getServer() {
        return this.f1395c.f1380j;
    }

    public final String getSessionToken() {
        return null;
    }

    public final boolean getShippingEnabled() {
        return this.f1395c.f1383m;
    }

    public final int getTextType() {
        return this.f1395c.f1381k;
    }

    public final boolean hasCreatedPIN() {
        return this.f1396d.f1390f;
    }

    public final boolean isHeavyCountry() {
        String iSO3Country = Locale.getDefault().getISO3Country();
        return iSO3Country.equals("USA") || iSO3Country.equals("GBR") || iSO3Country.equals("CAN") || iSO3Country.equals("AUS") || iSO3Country.equals("ESP") || iSO3Country.equals("ITA") || iSO3Country.equals("FRA") || iSO3Country.equals("SGP") || iSO3Country.equals("MYS");
    }

    public final boolean isLibraryInitialized() {
        return PayPalActivity._paypal == null ? false : this.f1397e.booleanValue();
    }

    public final boolean isLightCountry() {
        return !isHeavyCountry();
    }

    public final boolean isPersonalPayment() {
        if (this.f1395c.f1371a == null) {
            return false;
        }
        for (int i = PAYMENT_TYPE_GOODS; i < this.f1395c.f1371a.getReceivers().size(); i += PAY_TYPE_PARALLEL) {
            if (((PayPalReceiverDetails) this.f1395c.f1371a.getReceivers().get(i)).getPaymentType() != PAY_TYPE_CHAINED) {
                return false;
            }
        }
        return true;
    }

    public final void onInitializeCompletedError(int i, Object obj) {
    }

    public final void onInitializeCompletedOK(int i, Object obj) {
    }

    public final Intent preapprove(PayPalPreapproval payPalPreapproval, Context context) {
        return preapprove(payPalPreapproval, context, null);
    }

    public final Intent preapprove(PayPalPreapproval payPalPreapproval, Context context, PayPalResultDelegate payPalResultDelegate) {
        this.f1395c.f1372b = payPalPreapproval;
        this.f1395c.f1371a = null;
        Intent intent = new Intent(context, PayPalActivity.class);
        intent.putExtra(PayPalActivity.EXTRA_PREAPPROVAL_INFO, payPalPreapproval);
        if (payPalResultDelegate != null) {
            intent.putExtra(PayPalActivity.EXTRA_RESULT_DELEGATE, (Serializable) payPalResultDelegate);
        }
        return intent;
    }

    public final void resetAccount() {
        this.f1398g = false;
        C0799b c0799b = this.f1396d;
        c0799b.f1391g.setAccountName(null);
        c0799b.f1391g.setAccountEmail(null);
        c0799b.f1391g.setAccountPhone(null);
        c0799b.f1391g.setAuthMethod(PAYMENT_TYPE_GOODS);
        c0799b.f1391g.setAuthSetting(PAYMENT_TYPE_GOODS);
        c0799b.f1391g.setPINCreated(false);
    }

    public final void setAccountCountryDialingCode(String str) {
        Assert.assertNotNull(XmlPullParser.NO_NAMESPACE, str);
        this.f1400i = str;
    }

    public final void setAccountEmail(String str) {
        this.f1396d.f1386b = str;
    }

    public final void setAccountName(String str) {
        this.f1396d.f1385a = str;
    }

    public final void setAccountPhone(String str) {
        this.f1396d.f1387c = str;
    }

    public final void setAdjustPaymentError(String str) {
        this.f1399h = str;
    }

    public final void setAuthMethod(int i) {
        this.f1396d.f1388d = i;
    }

    public final void setAuthSetting(int i) {
        this.f1396d.f1389e = i;
    }

    public final void setCancelUrl(String str) {
        this.f1395c.f1378h = str;
    }

    public final void setDeviceReferenceToken(String str) {
    }

    public final void setDynamicAmountCalculationEnabled(boolean z) {
        this.f1395c.f1384n = z;
    }

    public final void setFeesPayer(int i) {
        this.f1395c.f1382l = i;
    }

    public final void setIsRememberMe(boolean z) {
        this.f1398g = z;
    }

    public final void setLanguage(String str) {
        if (!C0839h.m1572b(str)) {
            str = "en_US";
        }
        this.f1395c.f1377g = str;
        C0839h.m1573c(this.f1395c.f1377g);
    }

    public final void setLibraryInitialized(boolean z) {
        synchronized (PayPalActivity._paypal) {
            PayPalActivity._paypal.f1397e = Boolean.valueOf(z);
            PayPalActivity._paypal.notifyAll();
        }
    }

    public final void setPINCreated(boolean z) {
        this.f1396d.f1390f = z;
    }

    public final void setPreapprovalKey(String str) {
        this.f1395c.f1376f = str;
    }

    public final void setReturnUrl(String str) {
        this.f1395c.f1379i = str;
    }

    public final void setSessionToken(String str) {
    }

    public final void setShippingEnabled(boolean z) {
        this.f1395c.f1383m = z;
    }

    public final boolean shouldShowFees() {
        String iSO3Country = Locale.getDefault().getISO3Country();
        boolean z = (Locale.GERMANY.getISO3Country().compareTo(iSO3Country) == 0 || Locale.ITALY.getISO3Country().compareTo(iSO3Country) == 0) ? true : PAYMENT_TYPE_GOODS;
        return !z && isPersonalPayment();
    }

    public final void shutdown() {
        C0839h.m1571a();
        C0838g.m1566b();
        this.f1395c = null;
    }
}
