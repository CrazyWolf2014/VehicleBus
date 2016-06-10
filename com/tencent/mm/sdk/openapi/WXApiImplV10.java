package com.tencent.mm.sdk.openapi;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.tencent.mm.algorithm.MD5;
import com.tencent.mm.sdk.Build;
import com.tencent.mm.sdk.MMSharedPreferences;
import com.tencent.mm.sdk.channel.ConstantsMMessage;
import com.tencent.mm.sdk.channel.MMessage;
import com.tencent.mm.sdk.channel.MMessageAct;
import com.tencent.mm.sdk.openapi.GetMessageFromWX.Req;
import com.tencent.mm.sdk.openapi.SendAuth.Resp;
import com.tencent.mm.sdk.platformtools.Log;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.PluginIntent;

final class WXApiImplV10 implements IWXAPI {
    private Context f2236q;
    private String f2237r;
    private boolean f2238s;

    protected WXApiImplV10(Context context, String str) {
        this(context, str, false);
    }

    protected WXApiImplV10(Context context, String str, boolean z) {
        this.f2238s = false;
        this.f2236q = context;
        this.f2237r = str;
        this.f2238s = z;
    }

    private boolean m2439a(String str) {
        if (this.f2238s) {
            try {
                return m2441a(this.f2236q.getPackageManager().getPackageInfo(str, 64).signatures);
            } catch (NameNotFoundException e) {
                return false;
            }
        }
        Log.m1655d("MicroMsg.SDK.WXApiImplV10", "ignore wechat app signature validation");
        return true;
    }

    private static boolean m2440a(byte[] bArr, byte[] bArr2) {
        if (bArr == null || bArr.length == 0 || bArr2 == null || bArr2.length == 0) {
            Log.m1657e("MicroMsg.SDK.WXApiImplV10", "checkSumConsistent fail, invalid arguments");
            return false;
        } else if (bArr.length != bArr2.length) {
            Log.m1657e("MicroMsg.SDK.WXApiImplV10", "checkSumConsistent fail, length is different");
            return false;
        } else {
            for (int i = 0; i < bArr.length; i++) {
                if (bArr[i] != bArr2[i]) {
                    return false;
                }
            }
            return true;
        }
    }

    private boolean m2441a(Signature[] signatureArr) {
        if (this.f2238s) {
            for (Signature toCharsString : signatureArr) {
                String toCharsString2 = toCharsString.toCharsString();
                Log.m1655d("MicroMsg.SDK.WXApiImplV10", "check signature:" + toCharsString2);
                if (toCharsString2.equals("308202eb30820254a00302010202044d36f7a4300d06092a864886f70d01010505003081b9310b300906035504061302383631123010060355040813094775616e67646f6e673111300f060355040713085368656e7a68656e31353033060355040a132c54656e63656e7420546563686e6f6c6f6779285368656e7a68656e2920436f6d70616e79204c696d69746564313a3038060355040b133154656e63656e74204775616e677a686f7520526573656172636820616e6420446576656c6f706d656e742043656e7465723110300e0603550403130754656e63656e74301e170d3131303131393134333933325a170d3431303131313134333933325a3081b9310b300906035504061302383631123010060355040813094775616e67646f6e673111300f060355040713085368656e7a68656e31353033060355040a132c54656e63656e7420546563686e6f6c6f6779285368656e7a68656e2920436f6d70616e79204c696d69746564313a3038060355040b133154656e63656e74204775616e677a686f7520526573656172636820616e6420446576656c6f706d656e742043656e7465723110300e0603550403130754656e63656e7430819f300d06092a864886f70d010101050003818d0030818902818100c05f34b231b083fb1323670bfbe7bdab40c0c0a6efc87ef2072a1ff0d60cc67c8edb0d0847f210bea6cbfaa241be70c86daf56be08b723c859e52428a064555d80db448cdcacc1aea2501eba06f8bad12a4fa49d85cacd7abeb68945a5cb5e061629b52e3254c373550ee4e40cb7c8ae6f7a8151ccd8df582d446f39ae0c5e930203010001300d06092a864886f70d0101050500038181009c8d9d7f2f908c42081b4c764c377109a8b2c70582422125ce545842d5f520aea69550b6bd8bfd94e987b75a3077eb04ad341f481aac266e89d3864456e69fba13df018acdc168b9a19dfd7ad9d9cc6f6ace57c746515f71234df3a053e33ba93ece5cd0fc15f3e389a3f365588a9fcb439e069d3629cd7732a13fff7b891499")) {
                    Log.m1655d("MicroMsg.SDK.WXApiImplV10", "pass");
                    return true;
                }
            }
            return false;
        }
        Log.m1655d("MicroMsg.SDK.WXApiImplV10", "ignore wechat app signature validation");
        return true;
    }

    public final int getWXAppSupportAPI() {
        if (isWXAppInstalled()) {
            return new MMSharedPreferences(this.f2236q).getInt("_build_info_sdk_int_", 0);
        }
        Log.m1657e("MicroMsg.SDK.WXApiImplV10", "open wx app failed, not installed or signature check failed");
        return 0;
    }

    public final boolean handleIntent(Intent intent, IWXAPIEventHandler iWXAPIEventHandler) {
        boolean z;
        if (intent == null) {
            z = false;
        } else {
            String stringExtra = intent.getStringExtra(ConstantsAPI.WX_TOKEN_KEY);
            z = stringExtra != null && stringExtra.equals(ConstantsAPI.WX_TOKEN_VALUE);
        }
        if (!z) {
            return false;
        }
        stringExtra = intent.getStringExtra(ConstantsMMessage.CONTENT);
        int intExtra = intent.getIntExtra(ConstantsMMessage.SDK_VERSION, 0);
        String stringExtra2 = intent.getStringExtra(ConstantsMMessage.APP_PACKAGE);
        if (stringExtra2 == null || stringExtra2.length() == 0) {
            Log.m1657e("MicroMsg.SDK.WXApiImplV10", "invalid argument");
            return false;
        }
        byte[] byteArrayExtra = intent.getByteArrayExtra(ConstantsMMessage.CHECK_SUM);
        StringBuffer stringBuffer = new StringBuffer();
        if (stringExtra != null) {
            stringBuffer.append(stringExtra);
        }
        stringBuffer.append(intExtra);
        stringBuffer.append(stringExtra2);
        stringBuffer.append("mMcShCsTr");
        if (m2440a(byteArrayExtra, MD5.getMessageDigest(stringBuffer.toString().substring(1, 9).getBytes()).getBytes())) {
            switch (intent.getIntExtra("_wxapi_command_type", 0)) {
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    iWXAPIEventHandler.onResp(new Resp(intent.getExtras()));
                    return true;
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    iWXAPIEventHandler.onResp(new SendMessageToWX.Resp(intent.getExtras()));
                    return true;
                case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                    iWXAPIEventHandler.onReq(new Req(intent.getExtras()));
                    return true;
                case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                    iWXAPIEventHandler.onReq(new ShowMessageFromWX.Req(intent.getExtras()));
                    return true;
                default:
                    return false;
            }
        }
        Log.m1657e("MicroMsg.SDK.WXApiImplV10", "checksum fail");
        return false;
    }

    public final boolean isWXAppInstalled() {
        boolean z = false;
        try {
            PackageInfo packageInfo = this.f2236q.getPackageManager().getPackageInfo(PluginIntent.APP_PACKAGE_PATTERN, 64);
            if (packageInfo != null) {
                z = m2441a(packageInfo.signatures);
            }
        } catch (NameNotFoundException e) {
        }
        return z;
    }

    public final boolean isWXAppSupportAPI() {
        return getWXAppSupportAPI() >= Build.SDK_INT;
    }

    public final boolean openWXApp() {
        if (isWXAppInstalled()) {
            try {
                this.f2236q.startActivity(this.f2236q.getPackageManager().getLaunchIntentForPackage(PluginIntent.APP_PACKAGE_PATTERN));
                return true;
            } catch (Exception e) {
                Log.m1657e("MicroMsg.SDK.WXApiImplV10", "startActivity fail, exception = " + e.getMessage());
                return false;
            }
        }
        Log.m1657e("MicroMsg.SDK.WXApiImplV10", "open wx app failed, not installed or signature check failed");
        return false;
    }

    public final boolean registerApp(String str) {
        if (m2439a(PluginIntent.APP_PACKAGE_PATTERN)) {
            if (str != null) {
                this.f2237r = str;
            }
            Log.m1655d("MicroMsg.SDK.WXApiImplV10", "register app " + this.f2236q.getPackageName());
            MMessage.send(this.f2236q, PluginIntent.APP_PACKAGE_PATTERN, ConstantsAPI.ACTION_HANDLE_APP_REGISTER, "weixin://registerapp?appid=" + this.f2237r);
            return true;
        }
        Log.m1657e("MicroMsg.SDK.WXApiImplV10", "register app failed for wechat app signature check failed");
        return false;
    }

    public final boolean sendReq(BaseReq baseReq) {
        if (!m2439a(PluginIntent.APP_PACKAGE_PATTERN)) {
            Log.m1657e("MicroMsg.SDK.WXApiImplV10", "sendReq failed for wechat app signature check failed");
            return false;
        } else if (baseReq.checkArgs()) {
            Bundle bundle = new Bundle();
            baseReq.toBundle(bundle);
            return MMessageAct.sendToWx(this.f2236q, "weixin://sendreq?appid=" + this.f2237r, bundle);
        } else {
            Log.m1657e("MicroMsg.SDK.WXApiImplV10", "sendReq checkArgs fail");
            return false;
        }
    }

    public final boolean sendResp(BaseResp baseResp) {
        if (!m2439a(PluginIntent.APP_PACKAGE_PATTERN)) {
            Log.m1657e("MicroMsg.SDK.WXApiImplV10", "sendResp failed for wechat app signature check failed");
            return false;
        } else if (baseResp.checkArgs()) {
            Bundle bundle = new Bundle();
            baseResp.toBundle(bundle);
            return MMessageAct.sendToWx(this.f2236q, "weixin://sendresp?appid=" + this.f2237r, bundle);
        } else {
            Log.m1657e("MicroMsg.SDK.WXApiImplV10", "sendResp checkArgs fail");
            return false;
        }
    }

    public final void unregisterApp() {
        if (!m2439a(PluginIntent.APP_PACKAGE_PATTERN)) {
            Log.m1657e("MicroMsg.SDK.WXApiImplV10", "unregister app failed for wechat app signature check failed");
        } else if (this.f2237r == null || this.f2237r.length() == 0) {
            Log.m1657e("MicroMsg.SDK.WXApiImplV10", "unregisterApp fail, appId is empty");
        } else {
            Log.m1655d("MicroMsg.SDK.WXApiImplV10", "unregister app " + this.f2236q.getPackageName());
            MMessage.send(this.f2236q, PluginIntent.APP_PACKAGE_PATTERN, ConstantsAPI.ACTION_HANDLE_APP_UNREGISTER, "weixin://unregisterapp?appid=" + this.f2237r);
        }
    }
}
