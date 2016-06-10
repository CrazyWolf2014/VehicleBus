package com.ifoer.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import com.cnlaunch.x431frame.C0136R.drawable;
import java.io.IOException;
import java.io.InputStream;

public class MySharedPreferences {
    public static final String Answer1 = "AnswerKey1";
    public static final String Answer2 = "AnswerKey2";
    public static final String Answer3 = "AnswerKey3";
    public static final String BgIdKey = "BgIdKey";
    public static final String CCKey = "CCKey";
    public static final String DefScanPad = "DefScanPad";
    public static final String IdentityType = "IdentityType";
    public static final boolean IfLogin = false;
    public static final String IfLoginKey = "IfLoginKey";
    public static final String IfSaveNamePswKey = "IfSaveNamePswKey";
    public static final String IfShow = "ifshow";
    public static final String IfShowNoticKey = "IfShowNoticKey";
    public static final String IfShowResponsibilityKey = "IfShowResponsibilityKey";
    public static final String Question1 = "questKey1";
    public static final String Question2 = "questKey2";
    public static final String Question3 = "questKey3";
    public static final String QuestionCode1 = "questCodeKey1";
    public static final String QuestionCode2 = "questCodeKey2";
    public static final String QuestionCode3 = "questCodeKey3";
    public static final String RemoteDiag_SNKey = "RemoteDiag_SNKey";
    public static final String TokenKey = "TokenKey";
    public static final String USER_PUBLIC_ID = "USER_PUBLIC_ID";
    public static final String UserNameKey = "UserNameKwy";
    public static final String UserPswKey = "UserPswKey";
    public static String bgID = null;
    static Class<drawable> cls = null;
    public static final String companyAddress = "companyAddress";
    public static final String companyPhoneNumber = "companyPhoneNumber";
    public static final String conversion = "conversion";
    public static final String dataStreamTotal = "dataStreamTotal";
    public static final String datatime = "datatime";
    public static final String devboot = "devboot";
    public static final String diagnosticSoftwareVersionNo = "diagnosticSoftwareVersionNo";
    public static final String first = "first";
    public static final String generateOperatingRecord = "generateOperatingRecord";
    public static final String hasODB = "hasODB";
    public static final String iflogout = "iflogout";
    public static final String isDownLoadTaskOver = "isDownLoadTaskOver";
    public static final boolean isHaveODB = false;
    public static final String isHeqiang = "isHeqiang";
    public static final String isSanMu = "isSanMu";
    public static final String isSerialPortIDMod = "isSerialPortIDMod";
    public static final String isSerialPortIDReg = "isSerialPortNOReg";
    public static final String licensePlateNumberDiag = "licensePlateNumberDiag";
    public static final String loginfirst = "loginfirstTwo";
    public static Drawable mDrawable = null;
    public static final String model = "model";
    public static final String openshowuser = "openShowUser";
    private static final String pcbversion = "pcbversion";
    public static final String savecc = "savecc";
    public static final String savemail = "savemail";
    public static final String savepaykey = "savepaykey";
    public static final String savesoftPackageId = "savesoftPackageId";
    public static final String serialNo = "serialNo";
    public static final String serialNoKey = "serialNoKey";
    public static final String serialPortID = "serialPortID";
    public static final String serialPortNO = "serialPortNO";
    public static SharedPreferences share = null;
    public static final String softversion = "softversion";
    public static final String stringdownloadbin = "stringdownloadbin";
    public static final String versionName = "VersionName";
    public static final String vinAutoPaths = "vinAutoPaths";
    public static final String whoCountry = "Country";
    private final String SHAREDPRENAME;

    public MySharedPreferences() {
        this.SHAREDPRENAME = "expedition";
    }

    static {
        cls = drawable.class;
        bgID = null;
        mDrawable = null;
    }

    public static SharedPreferences getSharedPref(Context context) {
        if (context != null) {
            share = context.getSharedPreferences("expedition", 0);
        }
        return share;
    }

    public static String getStringValue(Context context, String key) {
        if (share == null && context != null) {
            getSharedPref(context);
        }
        return share.getString(key, null);
    }

    public static void setString(Context context, String key, String value) {
        if (share == null && context != null) {
            getSharedPref(context);
        }
        key.equals(CCKey);
        share.edit().putString(key, value).commit();
    }

    public static void setInt(Context context, String key, int value) {
        if (share == null && context != null) {
            getSharedPref(context);
        }
        share.edit().putInt(key, value).commit();
    }

    public static int getIntValue(Context context, String key) {
        if (share == null && context != null) {
            getSharedPref(context);
        }
        return share.getInt(key, 0);
    }

    public static int getIntValue(Context context, String key, int defoultValue) {
        if (share == null && context != null) {
            getSharedPref(context);
        }
        return share.getInt(key, defoultValue);
    }

    public static void setLong(Context context, String key, long value) {
        if (share == null && context != null) {
            getSharedPref(context);
        }
        share.edit().putLong(key, value).commit();
    }

    public static long getLong(Context context, String key, long defValue) {
        if (share == null && context != null) {
            getSharedPref(context);
        }
        if (!TextUtils.isEmpty(key)) {
            key = key.toLowerCase();
        }
        return share.getLong(key, defValue);
    }

    public static boolean getBooleanValue(Context context, String key, boolean defaultBool) {
        if (share == null && context != null) {
            getSharedPref(context);
        }
        return share.getBoolean(key, defaultBool);
    }

    public static void setBoolean(Context context, String key, boolean value) {
        if (share == null && context != null) {
            getSharedPref(context);
        }
        share.edit().putBoolean(key, value).commit();
    }

    public static Drawable getBgId(Context context) {
        if (share == null) {
            getSharedPref(context);
        }
        try {
            bgID = share.getString(BgIdKey, "skin_xing.jpg");
        } catch (Exception e) {
            bgID = "skin_xing.jpg";
        }
        try {
            InputStream is = context.getResources().getAssets().open(bgID);
            System.gc();
            mDrawable = new BitmapDrawable(BitmapFactory.decodeStream(is));
            is.close();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return mDrawable;
    }
}
