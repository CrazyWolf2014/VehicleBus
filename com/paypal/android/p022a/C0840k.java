package com.paypal.android.p022a;

import android.content.Intent;
import android.util.Log;
import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.cnmobi.im.bo.ImConstants;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.ifoer.mine.Contact;
import com.paypal.android.MEP.C0811a.C0801b;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;
import junit.framework.Assert;
import org.xbill.DNS.KEYRecord;

/* renamed from: com.paypal.android.a.k */
class C0840k extends Thread {
    private /* synthetic */ C1107b f1572a;

    C0840k(C1107b c1107b) {
        this.f1572a = c1107b;
    }

    public final void run() {
        Exception e;
        boolean d;
        while (!this.f1572a.f2230i) {
            C0801b c0801b = (C0801b) this.f1572a.f2229h.get("delegate");
            Object h;
            switch (this.f1572a.f2228g) {
                case KEYRecord.OWNER_USER /*0*/:
                    boolean a;
                    PayPal.logd("MPL", "start LOGIN");
                    C1107b.m2383e().m2419a("mpl-review");
                    this.f1572a.f2228g = -1;
                    try {
                        a = C1107b.m2371a(this.f1572a, (String) this.f1572a.f2229h.get("usernameOrPhone"), (String) this.f1572a.f2229h.get("passwordOrPin"));
                    } catch (Exception e2) {
                        PayPal.loge("Login", "Error during call to log in. " + e2.getMessage());
                        a = false;
                    }
                    if (!a) {
                        this.f1572a.f2226e = -1;
                    }
                    if (!a) {
                        C1107b.m2367a(this.f1572a, ImConstants.TYPE_LOGIN, c0801b);
                        break;
                    }
                    c0801b.m1492a(0, null);
                    PayPal.logd("MPL", "end LOGIN ok");
                    break;
                case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                    PayPal.logd("MPL", "start CREATE_PAYMENT");
                    this.f1572a.f2228g = -1;
                    C1107b.m2383e().m2420a("FundingPlanId", Contact.RELATION_ASK);
                    C1107b.m2383e().m2420a("FundingPlans", null);
                    C1107b.m2383e().m2420a("DefaultFundingPlan", null);
                    Object f = this.f1572a.m2415y();
                    if (f == null) {
                        this.f1572a.f2226e = -1;
                        C1107b.m2367a(this.f1572a, "CREATE_PAYMENT", c0801b);
                        break;
                    }
                    if (((String) f.get("ActionType")).equals("PAY")) {
                        c0801b.m1492a(4, (Object) "-1");
                    } else {
                        c0801b.m1492a(3, f);
                    }
                    PayPal.logd("MPL", "end CREATE_PAYMENT ok");
                    break;
                case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                    PayPal.logd("MPL", "start SEND_PAYMENT");
                    this.f1572a.f2228g = -1;
                    if (!this.f1572a.m2409s()) {
                        c0801b.m1494d(this.f1572a.m2423f());
                        PayPalActivity.getInstance().sendBroadcast(new Intent("CHANGE_STRING"));
                        break;
                    }
                    h = C1107b.m2390h(this.f1572a);
                    if (h == null || h.length() <= 0) {
                        this.f1572a.f2226e = -1;
                    } else {
                        this.f1572a.m2419a("mpl-success");
                    }
                    if (h == null || h.length() <= 0) {
                        C1107b.m2367a(this.f1572a, "SEND_PAYMENT", c0801b);
                    } else {
                        c0801b.m1492a(4, h);
                        PayPal.logd("MPL", "end SEND_PAYMENT ok");
                    }
                    PayPalActivity.getInstance().sendBroadcast(new Intent("CHANGE_STRING"));
                    break;
                    break;
                case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                    PayPal.logd("MPL", "start FUNDING");
                    this.f1572a.f2228g = -1;
                    h = this.f1572a.m2416z();
                    if (h == null) {
                        this.f1572a.f2226e = -1;
                        C1107b.m2367a(this.f1572a, "FUNDING", c0801b);
                        break;
                    }
                    c0801b.m1492a(5, h);
                    PayPal.logd("MPL", "end FUNDING ok");
                    break;
                case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                    Assert.assertTrue("UPDATE_PAYMENT is supposed to be dead code", false);
                    break;
                case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                    this.f1572a.f2228g = -1;
                    if (!this.f1572a.m2410t()) {
                        this.f1572a.f2226e = -1;
                        C1107b.m2367a(this.f1572a, "GET_SHIPPING_ADDRESSES", c0801b);
                        break;
                    }
                    c0801b.m1492a(7, this.f1572a.f2229h);
                    break;
                case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                    PayPal.logd("MPL", "start CHECK_AUTH");
                    this.f1572a.f2228g = -1;
                    if (this.f1572a.m2361B()) {
                        C1107b.f2215a.m1639a(8, this.f1572a.f2229h);
                        PayPal.logd("MPL", "end CHECK_AUTH ok");
                    } else {
                        C1107b.m2366a(this.f1572a, 8, C1107b.f2215a);
                    }
                    this.f1572a.f2226e = -1;
                    break;
                case FileOptions.OPTIMIZE_FOR_FIELD_NUMBER /*9*/:
                    Assert.assertTrue("QUICK_PAY is supposed to be dead code", false);
                    break;
                case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
                    this.f1572a.f2228g = -1;
                    PayPal.logd("MPL", "start QUICK_LOGIN");
                    try {
                        d = this.f1572a.m2361B();
                        try {
                            d = C1107b.m2385e(this.f1572a);
                        } catch (Exception e3) {
                            e2 = e3;
                            Log.e("Login", "Error during call to log in. " + e2.getMessage());
                            if (!d) {
                                this.f1572a.f2226e = -1;
                            }
                            if (d) {
                                c0801b.m1492a(10, null);
                                PayPal.logd("MPL", "end QUICK_LOGIN ok");
                            } else {
                                C1107b.m2367a(this.f1572a, "QUICK_LOGIN", c0801b);
                            }
                            Thread.sleep(100);
                        }
                    } catch (Exception e4) {
                        e2 = e4;
                        d = false;
                        Log.e("Login", "Error during call to log in. " + e2.getMessage());
                        if (d) {
                            this.f1572a.f2226e = -1;
                        }
                        if (d) {
                            C1107b.m2367a(this.f1572a, "QUICK_LOGIN", c0801b);
                        } else {
                            c0801b.m1492a(10, null);
                            PayPal.logd("MPL", "end QUICK_LOGIN ok");
                        }
                        Thread.sleep(100);
                    }
                    if (d) {
                        this.f1572a.f2226e = -1;
                    }
                    if (d) {
                        c0801b.m1492a(10, null);
                        PayPal.logd("MPL", "end QUICK_LOGIN ok");
                    } else {
                        C1107b.m2367a(this.f1572a, "QUICK_LOGIN", c0801b);
                    }
                case TrafficIncident.LOCATION_FIELD_NUMBER /*11*/:
                    PayPal.logd("MPL", "start CREATE_PIN");
                    this.f1572a.f2228g = -1;
                    if (!this.f1572a.m2362C()) {
                        this.f1572a.f2226e = -1;
                        C1107b.m2367a(this.f1572a, "CREATE_PIN", c0801b);
                        break;
                    }
                    c0801b.m1492a(11, this.f1572a.f2229h);
                    PayPal.logd("MPL", "end CREATE_PIN ok");
                    break;
                case TrafficIncident.VERTEXOFFSET_FIELD_NUMBER /*12*/:
                    PayPal.logd("MPL", "start REMOVE_AUTH");
                    this.f1572a.f2228g = -1;
                    if (this.f1572a.m2363D()) {
                        C1107b.f2216b.m1639a(12, this.f1572a.f2229h);
                        PayPal.logd("MPL", "end REMOVE_AUTH ok");
                    } else {
                        C1107b.m2366a(this.f1572a, 12, C1107b.f2216b);
                    }
                    this.f1572a.f2226e = -1;
                    break;
                case TrafficIncident.INCIDENTVERTEX_FIELD_NUMBER /*13*/:
                    this.f1572a.f2228g = -1;
                    if (!this.f1572a.m2412v()) {
                        this.f1572a.f2226e = -1;
                        C1107b.m2367a(this.f1572a, "PREAPPROVAL_DETAILS", c0801b);
                        break;
                    }
                    c0801b.m1492a(13, null);
                    break;
                case TrafficIncident.STARTTIME_FIELD_NUMBER /*14*/:
                    this.f1572a.f2228g = -1;
                    if (!this.f1572a.m2413w()) {
                        this.f1572a.f2226e = -1;
                        C1107b.m2367a(this.f1572a, "PREAPPROVAL_CONFIRM", c0801b);
                        break;
                    }
                    c0801b.m1492a(14, null);
                    break;
                case TrafficIncident.ENDTIME_FIELD_NUMBER /*15*/:
                    if (!this.f1572a.m2411u()) {
                        this.f1572a.f2226e = -1;
                        C1107b.m2367a(this.f1572a, "CREATE_PREAPPROVAL", c0801b);
                        break;
                    }
                    c0801b.m1492a(15, null);
                    break;
            }
            try {
                Thread.sleep(100);
            } catch (Exception e5) {
                e5.printStackTrace();
            }
        }
        PayPal.logd("NetworkHandler", "thread exiting");
    }
}
