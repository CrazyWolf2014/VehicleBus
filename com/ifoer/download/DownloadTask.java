package com.ifoer.download;

import android.content.Context;
import android.os.Handler;
import com.cnlaunch.x431pro.common.Constants;
import com.cnlaunch.x431pro.module.upgrade.model.X431PadDtoSoft;
import com.ifoer.db.DBDao;
import com.ifoer.dbentity.CarVersionInfo;
import com.ifoer.entity.Constant;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.util.AndroidToLan;
import com.ifoer.util.MySharedPreferences;
import com.tencent.mm.sdk.platformtools.FilePathGenerator;
import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

public class DownloadTask implements Runnable {
    private static final String apkStrorePath;
    public static String country;
    String carId;
    private String carPath;
    private Context context;
    public ArrayList<CarVersionInfo> data;
    private String diagSoftUrl;
    private String diagSotrePath;
    private String diagUnzipPath;
    private String downLoadurl;
    private X431PadDtoSoft dto;
    private String fileStorePath;
    private String fileUnzipPath;
    private String firmSotrePath;
    private String firmUnzipPath;
    private Handler handler;
    private String lanId;
    String lanName;
    public String language;
    Locale locale;
    private String phoneSoftUrl;
    String serialNo;
    private String softId;
    private String softPackageID;
    private int type;
    private String versionDetailId;
    private String versionNo;

    static {
        apkStrorePath = Constant.APK_PATH;
    }

    public DownloadTask(Context context, Handler handler, X431PadDtoSoft dto, String serialNo) {
        this.diagSoftUrl = "http://mycar.x431.com/mobile/softCenter/downloadDiagSoftWs.action";
        this.phoneSoftUrl = "http://mycar.x431.com/mobile/softCenter/downloadPhoneSoftWs.action";
        this.diagSotrePath = Constant.UPLOAD_ZIP_PATH;
        this.diagUnzipPath = Constant.LOCAL_SERIALNO_PATH;
        this.carPath = Constant.CAR_PATH;
        this.firmSotrePath = Constant.STORE_PATH;
        this.firmUnzipPath = Constant.UNZIP_PATH;
        this.context = context;
        this.softId = dto.getSoftId();
        this.softPackageID = dto.getSoftPackageID();
        this.handler = handler;
        this.versionDetailId = dto.getVersionDetailId();
        this.versionNo = dto.getVersionNo();
        this.lanId = dto.getLanId();
        this.dto = dto;
        this.type = dto.getType();
        this.serialNo = MySharedPreferences.getStringValue(context, MySharedPreferences.serialNoKey);
        this.locale = Locale.getDefault();
        country = this.locale.getCountry();
        this.language = this.locale.getLanguage();
        this.lanName = AndroidToLan.toLan(country);
        Constant.language = this.locale.toString();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
        r50 = this;
        r35 = "";
        r0 = r50;
        r4 = r0.dto;
        r4 = r4.getUrl();
        r0 = r50;
        r0.downLoadurl = r4;
        r0 = r50;
        r4 = r0.type;
        r8 = 1;
        if (r4 != r8) goto L_0x015d;
    L_0x0015:
        r4 = apkStrorePath;
        r0 = r50;
        r0.fileStorePath = r4;
    L_0x001b:
        r4 = new java.lang.StringBuilder;
        r8 = "serialNo=";
        r4.<init>(r8);
        r0 = r50;
        r8 = r0.serialNo;
        r4 = r4.append(r8);
        r8 = "&versionDetailId=";
        r4 = r4.append(r8);
        r0 = r50;
        r8 = r0.versionDetailId;
        r4 = r4.append(r8);
        r35 = r4.toString();
        r4 = new java.lang.StringBuilder;
        r0 = r50;
        r8 = r0.downLoadurl;
        r8 = java.lang.String.valueOf(r8);
        r4.<init>(r8);
        r8 = "?";
        r4 = r4.append(r8);
        r0 = r35;
        r4 = r4.append(r0);
        r44 = r4.toString();
        r0 = r50;
        r4 = r0.context;
        r8 = "CCKey";
        r18 = com.ifoer.util.MySharedPreferences.getStringValue(r4, r8);
        r0 = r50;
        r4 = r0.context;
        r8 = "TokenKey";
        r42 = com.ifoer.util.MySharedPreferences.getStringValue(r4, r8);
        r4 = new java.lang.StringBuilder;
        r0 = r50;
        r8 = r0.serialNo;
        r8 = java.lang.String.valueOf(r8);
        r4.<init>(r8);
        r0 = r50;
        r8 = r0.versionDetailId;
        r4 = r4.append(r8);
        r0 = r42;
        r4 = r4.append(r0);
        r4 = r4.toString();
        r40 = com.ifoer.md5.MD5.getMD5Str(r4);
        r25 = 0;
        r29 = 0;
        r43 = new java.net.URL;	 Catch:{ Exception -> 0x0635 }
        r43.<init>(r44);	 Catch:{ Exception -> 0x0635 }
        r19 = r43.openConnection();	 Catch:{ Exception -> 0x0635 }
        r19 = (java.net.HttpURLConnection) r19;	 Catch:{ Exception -> 0x0635 }
        r4 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
        r0 = r19;
        r0.setConnectTimeout(r4);	 Catch:{ Exception -> 0x0635 }
        r4 = "GET";
        r0 = r19;
        r0.setRequestMethod(r4);	 Catch:{ Exception -> 0x0635 }
        r4 = "Connection";
        r8 = "Keep-Alive";
        r0 = r19;
        r0.setRequestProperty(r4, r8);	 Catch:{ Exception -> 0x0635 }
        r4 = "Charset";
        r8 = "UTF-8";
        r0 = r19;
        r0.setRequestProperty(r4, r8);	 Catch:{ Exception -> 0x0635 }
        r4 = "content-type";
        r8 = "application/x-www-form-urlencoded";
        r0 = r19;
        r0.setRequestProperty(r4, r8);	 Catch:{ Exception -> 0x0635 }
        r0 = r50;
        r4 = r0.type;	 Catch:{ Exception -> 0x0635 }
        r8 = 1;
        if (r4 == r8) goto L_0x00e1;
    L_0x00cf:
        r4 = "cc";
        r0 = r19;
        r1 = r18;
        r0.setRequestProperty(r4, r1);	 Catch:{ Exception -> 0x0635 }
        r4 = "sign";
        r0 = r19;
        r1 = r40;
        r0.setRequestProperty(r4, r1);	 Catch:{ Exception -> 0x0635 }
    L_0x00e1:
        r4 = 1;
        r0 = r19;
        r0.setDoInput(r4);	 Catch:{ Exception -> 0x0635 }
        r4 = 1;
        r0 = r19;
        r0.setDoOutput(r4);	 Catch:{ Exception -> 0x0635 }
        r24 = r19.getContentLength();	 Catch:{ Exception -> 0x0635 }
        r0 = r50;
        r4 = r0.dto;	 Catch:{ Exception -> 0x0635 }
        r0 = r24;
        r8 = (long) r0;	 Catch:{ Exception -> 0x0635 }
        r4.setFileSize(r8);	 Catch:{ Exception -> 0x0635 }
        r29 = r19.getInputStream();	 Catch:{ Exception -> 0x0635 }
        r20 = 0;
        r36 = CRP.utils.CRPTools.getUsableSDCardSize();	 Catch:{ Exception -> 0x0635 }
        if (r24 > 0) goto L_0x0129;
    L_0x0107:
        r0 = r50;
        r4 = r0.handler;	 Catch:{ Exception -> 0x0635 }
        r8 = 0;
        r0 = r50;
        r9 = r0.softId;	 Catch:{ Exception -> 0x0635 }
        r4 = r4.obtainMessage(r8, r9);	 Catch:{ Exception -> 0x0635 }
        r4.sendToTarget();	 Catch:{ Exception -> 0x0635 }
        r28 = new android.content.Intent;	 Catch:{ Exception -> 0x0635 }
        r4 = "show";
        r0 = r28;
        r0.<init>(r4);	 Catch:{ Exception -> 0x0635 }
        r0 = r50;
        r4 = r0.context;	 Catch:{ Exception -> 0x0635 }
        r0 = r28;
        r4.sendBroadcast(r0);	 Catch:{ Exception -> 0x0635 }
    L_0x0129:
        r0 = r24;
        r8 = (long) r0;	 Catch:{ Exception -> 0x0635 }
        r4 = (r8 > r36 ? 1 : (r8 == r36 ? 0 : -1));
        if (r4 <= 0) goto L_0x0188;
    L_0x0130:
        r0 = r50;
        r4 = r0.handler;	 Catch:{ Exception -> 0x0635 }
        r8 = 6;
        r0 = r50;
        r9 = r0.softId;	 Catch:{ Exception -> 0x0635 }
        r4 = r4.obtainMessage(r8, r9);	 Catch:{ Exception -> 0x0635 }
        r4.sendToTarget();	 Catch:{ Exception -> 0x0635 }
        r28 = new android.content.Intent;	 Catch:{ Exception -> 0x0635 }
        r4 = "show";
        r0 = r28;
        r0.<init>(r4);	 Catch:{ Exception -> 0x0635 }
        r0 = r50;
        r4 = r0.context;	 Catch:{ Exception -> 0x0635 }
        r0 = r28;
        r4.sendBroadcast(r0);	 Catch:{ Exception -> 0x0635 }
    L_0x0152:
        if (r25 == 0) goto L_0x0157;
    L_0x0154:
        r25.close();	 Catch:{ IOException -> 0x062c }
    L_0x0157:
        if (r29 == 0) goto L_0x015c;
    L_0x0159:
        r29.close();	 Catch:{ IOException -> 0x062c }
    L_0x015c:
        return;
    L_0x015d:
        r0 = r50;
        r4 = r0.type;
        r8 = 2;
        if (r4 != r8) goto L_0x0176;
    L_0x0164:
        r0 = r50;
        r4 = r0.firmSotrePath;
        r0 = r50;
        r0.fileStorePath = r4;
        r0 = r50;
        r4 = r0.firmUnzipPath;
        r0 = r50;
        r0.fileUnzipPath = r4;
        goto L_0x001b;
    L_0x0176:
        r0 = r50;
        r4 = r0.diagSotrePath;
        r0 = r50;
        r0.fileStorePath = r4;
        r0 = r50;
        r4 = r0.diagUnzipPath;
        r0 = r50;
        r0.fileUnzipPath = r4;
        goto L_0x001b;
    L_0x0188:
        r0 = r50;
        r4 = r0.handler;	 Catch:{ Exception -> 0x0635 }
        r8 = 7;
        r9 = 0;
        r0 = r50;
        r11 = r0.dto;	 Catch:{ Exception -> 0x0635 }
        r11 = r11.getSoftName();	 Catch:{ Exception -> 0x0635 }
        r0 = r24;
        r4 = r4.obtainMessage(r8, r0, r9, r11);	 Catch:{ Exception -> 0x0635 }
        r4.sendToTarget();	 Catch:{ Exception -> 0x0635 }
        r4 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r0 = new byte[r4];	 Catch:{ Exception -> 0x0635 }
        r17 = r0;
        r22 = new java.io.File;	 Catch:{ Exception -> 0x0635 }
        r0 = r50;
        r4 = r0.fileStorePath;	 Catch:{ Exception -> 0x0635 }
        r0 = r22;
        r0.<init>(r4);	 Catch:{ Exception -> 0x0635 }
        r4 = r22.exists();	 Catch:{ Exception -> 0x0635 }
        if (r4 != 0) goto L_0x01b9;
    L_0x01b6:
        r22.mkdirs();	 Catch:{ Exception -> 0x0635 }
    L_0x01b9:
        r31 = 0;
        r0 = r50;
        r4 = r0.type;	 Catch:{ Exception -> 0x0635 }
        r8 = 1;
        if (r4 == r8) goto L_0x01d7;
    L_0x01c2:
        r0 = r50;
        r4 = r0.type;	 Catch:{ Exception -> 0x0635 }
        r8 = 2;
        if (r4 == r8) goto L_0x01d7;
    L_0x01c9:
        r0 = r50;
        r4 = r0.lanId;	 Catch:{ Exception -> 0x0635 }
        r8 = "1002";
        r4 = r4.equals(r8);	 Catch:{ Exception -> 0x0635 }
        if (r4 == 0) goto L_0x0260;
    L_0x01d5:
        r31 = "CN";
    L_0x01d7:
        r49 = "";
        r0 = r50;
        r4 = r0.type;	 Catch:{ Exception -> 0x0635 }
        r8 = 2;
        if (r4 != r8) goto L_0x0201;
    L_0x01e0:
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0635 }
        r8 = "Download_CRP229_V";
        r4.<init>(r8);	 Catch:{ Exception -> 0x0635 }
        r0 = r50;
        r8 = r0.versionNo;	 Catch:{ Exception -> 0x0635 }
        r9 = ".";
        r11 = "_";
        r8 = r8.replace(r9, r11);	 Catch:{ Exception -> 0x0635 }
        r4 = r4.append(r8);	 Catch:{ Exception -> 0x0635 }
        r8 = ".zip";
        r4 = r4.append(r8);	 Catch:{ Exception -> 0x0635 }
        r49 = r4.toString();	 Catch:{ Exception -> 0x0635 }
    L_0x0201:
        r0 = r50;
        r4 = r0.type;	 Catch:{ Exception -> 0x0635 }
        r8 = 1;
        if (r4 != r8) goto L_0x0270;
    L_0x0208:
        r49 = "CRP229.apk";
    L_0x020a:
        r23 = new java.io.File;	 Catch:{ Exception -> 0x0635 }
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0635 }
        r0 = r50;
        r8 = r0.fileStorePath;	 Catch:{ Exception -> 0x0635 }
        r8 = java.lang.String.valueOf(r8);	 Catch:{ Exception -> 0x0635 }
        r4.<init>(r8);	 Catch:{ Exception -> 0x0635 }
        r0 = r49;
        r4 = r4.append(r0);	 Catch:{ Exception -> 0x0635 }
        r4 = r4.toString();	 Catch:{ Exception -> 0x0635 }
        r0 = r23;
        r0.<init>(r4);	 Catch:{ Exception -> 0x0635 }
        r4 = r23.exists();	 Catch:{ Exception -> 0x0635 }
        if (r4 == 0) goto L_0x0231;
    L_0x022e:
        r23.delete();	 Catch:{ Exception -> 0x0635 }
    L_0x0231:
        r26 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x0635 }
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0635 }
        r0 = r50;
        r8 = r0.fileStorePath;	 Catch:{ Exception -> 0x0635 }
        r8 = java.lang.String.valueOf(r8);	 Catch:{ Exception -> 0x0635 }
        r4.<init>(r8);	 Catch:{ Exception -> 0x0635 }
        r0 = r49;
        r4 = r4.append(r0);	 Catch:{ Exception -> 0x0635 }
        r4 = r4.toString();	 Catch:{ Exception -> 0x0635 }
        r0 = r26;
        r0.<init>(r4);	 Catch:{ Exception -> 0x0635 }
    L_0x024f:
        r0 = r29;
        r1 = r17;
        r32 = r0.read(r1);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4 = -1;
        r0 = r32;
        if (r0 != r4) goto L_0x02ab;
    L_0x025c:
        r25 = r26;
        goto L_0x0152;
    L_0x0260:
        r0 = r50;
        r4 = r0.lanId;	 Catch:{ Exception -> 0x0635 }
        r8 = "1001";
        r4 = r4.equals(r8);	 Catch:{ Exception -> 0x0635 }
        if (r4 == 0) goto L_0x01d7;
    L_0x026c:
        r31 = "EN";
        goto L_0x01d7;
    L_0x0270:
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0635 }
        r0 = r50;
        r8 = r0.softPackageID;	 Catch:{ Exception -> 0x0635 }
        r8 = java.lang.String.valueOf(r8);	 Catch:{ Exception -> 0x0635 }
        r4.<init>(r8);	 Catch:{ Exception -> 0x0635 }
        r8 = "_";
        r4 = r4.append(r8);	 Catch:{ Exception -> 0x0635 }
        r0 = r50;
        r8 = r0.versionNo;	 Catch:{ Exception -> 0x0635 }
        r9 = ".";
        r11 = "_";
        r8 = r8.replace(r9, r11);	 Catch:{ Exception -> 0x0635 }
        r4 = r4.append(r8);	 Catch:{ Exception -> 0x0635 }
        r8 = "_";
        r4 = r4.append(r8);	 Catch:{ Exception -> 0x0635 }
        r0 = r31;
        r4 = r4.append(r0);	 Catch:{ Exception -> 0x0635 }
        r8 = ".zip";
        r4 = r4.append(r8);	 Catch:{ Exception -> 0x0635 }
        r49 = r4.toString();	 Catch:{ Exception -> 0x0635 }
        goto L_0x020a;
    L_0x02ab:
        r4 = com.ifoer.entity.Constant.needExistDownLoad;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        if (r4 != 0) goto L_0x05e6;
    L_0x02af:
        r4 = 0;
        r0 = r26;
        r1 = r17;
        r2 = r32;
        r0.write(r1, r4, r2);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r20 = r20 + r32;
        r0 = r50;
        r4 = r0.handler;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r8 = 1;
        r4 = r4.hasMessages(r8);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        if (r4 == 0) goto L_0x02ce;
    L_0x02c6:
        r0 = r50;
        r4 = r0.handler;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r8 = 1;
        r4.removeMessages(r8);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
    L_0x02ce:
        r0 = r50;
        r4 = r0.handler;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r8 = 1;
        r0 = r50;
        r9 = r0.softId;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r20;
        r1 = r24;
        r4 = r4.obtainMessage(r8, r0, r1, r9);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4.sendToTarget();	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r20;
        r1 = r24;
        if (r0 != r1) goto L_0x05b7;
    L_0x02e8:
        r23 = new java.io.File;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r50;
        r8 = r0.fileStorePath;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r8 = java.lang.String.valueOf(r8);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4.<init>(r8);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r49;
        r4 = r4.append(r0);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4 = r4.toString();	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r23;
        r0.<init>(r4);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4 = r23.exists();	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        if (r4 == 0) goto L_0x059e;
    L_0x030c:
        r8 = r23.length();	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r11 = 0;
        r4 = (r8 > r11 ? 1 : (r8 == r11 ? 0 : -1));
        if (r4 <= 0) goto L_0x059e;
    L_0x0316:
        r0 = r50;
        r4 = r0.type;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r8 = 1;
        if (r4 == r8) goto L_0x0519;
    L_0x031d:
        r0 = r50;
        r4 = r0.type;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r8 = 2;
        if (r4 == r8) goto L_0x0519;
    L_0x0324:
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r50;
        r8 = r0.fileUnzipPath;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r8 = java.lang.String.valueOf(r8);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4.<init>(r8);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r8 = "car";
        r4 = r4.append(r8);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r8 = "/DIAGNOSTIC/VEHICLES/";
        r4 = r4.append(r8);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r50;
        r8 = r0.softPackageID;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4 = r4.append(r8);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r8 = "/";
        r4 = r4.append(r8);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r5 = r4.toString();	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r50;
        r4 = r0.softPackageID;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r8 = "AutoSearch";
        r4 = r4.equalsIgnoreCase(r8);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        if (r4 == 0) goto L_0x037d;
    L_0x035b:
        r0 = r50;
        r4 = r0.context;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r8 = "vinAutoPaths";
        r9 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r11 = java.lang.String.valueOf(r5);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r9.<init>(r11);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r11 = "versionNo";
        r9 = r9.append(r11);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r11 = "/";
        r9 = r9.append(r11);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r9 = r9.toString();	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        com.ifoer.util.MySharedPreferences.setString(r4, r8, r9);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
    L_0x037d:
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r50;
        r8 = r0.fileStorePath;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r8 = java.lang.String.valueOf(r8);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4.<init>(r8);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r49;
        r4 = r4.append(r0);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r6 = r4.toString();	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r50;
        r8 = r0.fileUnzipPath;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r8 = java.lang.String.valueOf(r8);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4.<init>(r8);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r8 = "car";
        r4 = r4.append(r8);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r8 = "/";
        r4 = r4.append(r8);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r7 = r4.toString();	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4 = r50.deleteOldVersion();	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r50;
        r0.data = r4;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r50;
        r4 = r0.context;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r8 = "serialNoKey";
        r10 = com.ifoer.util.MySharedPreferences.getStringValue(r4, r8);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r50;
        r4 = r0.data;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4 = r4.size();	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        if (r4 <= 0) goto L_0x03e0;
    L_0x03cd:
        r0 = r50;
        r4 = r0.data;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r8 = 0;
        r4 = r4.get(r8);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4 = (com.ifoer.dbentity.CarVersionInfo) r4;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4 = r4.getCarId();	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r50;
        r0.carId = r4;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
    L_0x03e0:
        r48 = new java.util.ArrayList;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r48.<init>();	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r33 = 0;
        r0 = r50;
        r4 = r0.data;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4 = r4.size();	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        if (r4 <= 0) goto L_0x0428;
    L_0x03f1:
        r27 = 0;
    L_0x03f3:
        r0 = r50;
        r4 = r0.data;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4 = r4.size();	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r27;
        if (r0 < r4) goto L_0x0475;
    L_0x03ff:
        java.util.Collections.sort(r48);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4 = 0;
        r0 = r48;
        r4 = r0.get(r4);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4 = (java.lang.Double) r4;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r33 = r4.doubleValue();	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r50;
        r4 = r0.data;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4 = r4.size();	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r8 = 3;
        if (r4 != r8) goto L_0x0428;
    L_0x041a:
        r27 = 0;
    L_0x041c:
        r0 = r50;
        r4 = r0.data;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4 = r4.size();	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r27;
        if (r0 < r4) goto L_0x04a1;
    L_0x0428:
        r3 = new com.ifoer.db.SaveDataRunnable;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r50;
        r4 = r0.context;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r50;
        r8 = r0.dto;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r50;
        r9 = r0.handler;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r3.<init>(r4, r5, r6, r7, r8, r9, r10);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r3.start();	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        goto L_0x024f;
    L_0x043e:
        r21 = move-exception;
        r25 = r26;
    L_0x0441:
        r0 = r50;
        r4 = r0.handler;	 Catch:{ all -> 0x0632 }
        r8 = 0;
        r0 = r50;
        r9 = r0.softId;	 Catch:{ all -> 0x0632 }
        r4 = r4.obtainMessage(r8, r9);	 Catch:{ all -> 0x0632 }
        r4.sendToTarget();	 Catch:{ all -> 0x0632 }
        r28 = new android.content.Intent;	 Catch:{ all -> 0x0632 }
        r4 = "show";
        r0 = r28;
        r0.<init>(r4);	 Catch:{ all -> 0x0632 }
        r0 = r50;
        r4 = r0.context;	 Catch:{ all -> 0x0632 }
        r0 = r28;
        r4.sendBroadcast(r0);	 Catch:{ all -> 0x0632 }
        if (r25 == 0) goto L_0x0468;
    L_0x0465:
        r25.close();	 Catch:{ IOException -> 0x046f }
    L_0x0468:
        if (r29 == 0) goto L_0x015c;
    L_0x046a:
        r29.close();	 Catch:{ IOException -> 0x046f }
        goto L_0x015c;
    L_0x046f:
        r21 = move-exception;
        r21.printStackTrace();
        goto L_0x015c;
    L_0x0475:
        r0 = r50;
        r4 = r0.data;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r27;
        r4 = r4.get(r0);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4 = (com.ifoer.dbentity.CarVersionInfo) r4;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r47 = r4.getVersionNo();	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4 = 1;
        r8 = r47.length();	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r47;
        r4 = r0.substring(r4, r8);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r45 = java.lang.Double.parseDouble(r4);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4 = java.lang.Double.valueOf(r45);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r48;
        r0.add(r4);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r27 = r27 + 1;
        goto L_0x03f3;
    L_0x04a1:
        r0 = r50;
        r4 = r0.data;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r27;
        r4 = r4.get(r0);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4 = (com.ifoer.dbentity.CarVersionInfo) r4;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r8 = r4.getVersionNo();	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r9 = 1;
        r0 = r50;
        r4 = r0.data;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r27;
        r4 = r4.get(r0);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4 = (com.ifoer.dbentity.CarVersionInfo) r4;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4 = r4.getVersionNo();	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4 = r4.length();	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4 = r8.substring(r9, r4);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r8 = java.lang.Double.parseDouble(r4);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4 = (r8 > r33 ? 1 : (r8 == r33 ? 0 : -1));
        if (r4 != 0) goto L_0x0515;
    L_0x04d2:
        r0 = r50;
        r4 = r0.data;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r27;
        r4 = r4.get(r0);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4 = (com.ifoer.dbentity.CarVersionInfo) r4;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4 = r4.getVersionDir();	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r50;
        r30 = r0.deleteDynamicFile(r4);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r50;
        r4 = r0.context;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r8 = com.ifoer.db.DBDao.getInstance(r4);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r50;
        r4 = r0.data;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r27;
        r4 = r4.get(r0);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4 = (com.ifoer.dbentity.CarVersionInfo) r4;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r9 = r4.getCarId();	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r50;
        r4 = r0.data;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r27;
        r4 = r4.get(r0);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4 = (com.ifoer.dbentity.CarVersionInfo) r4;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4 = r4.getVersionNo();	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r11 = com.ifoer.expeditionphone.MainActivity.database;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r8.deleteDynamicLibrary(r10, r9, r4, r11);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
    L_0x0515:
        r27 = r27 + 1;
        goto L_0x041c;
    L_0x0519:
        r0 = r50;
        r4 = r0.type;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r8 = 1;
        if (r4 != r8) goto L_0x0544;
    L_0x0520:
        r0 = r50;
        r4 = r0.handler;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r8 = 4;
        r0 = r50;
        r9 = r0.softId;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r24;
        r1 = r24;
        r4 = r4.obtainMessage(r8, r0, r1, r9);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4.sendToTarget();	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        goto L_0x024f;
    L_0x0536:
        r4 = move-exception;
        r25 = r26;
    L_0x0539:
        if (r25 == 0) goto L_0x053e;
    L_0x053b:
        r25.close();	 Catch:{ IOException -> 0x0626 }
    L_0x053e:
        if (r29 == 0) goto L_0x0543;
    L_0x0540:
        r29.close();	 Catch:{ IOException -> 0x0626 }
    L_0x0543:
        throw r4;
    L_0x0544:
        r0 = r50;
        r4 = r0.type;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r8 = 2;
        if (r4 != r8) goto L_0x024f;
    L_0x054b:
        r0 = r50;
        r4 = r0.dto;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r20;
        r8 = (long) r0;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4.setDownloadSize(r8);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r50;
        r8 = r0.fileStorePath;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r8 = java.lang.String.valueOf(r8);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4.<init>(r8);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r49;
        r4 = r4.append(r0);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r6 = r4.toString();	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r50;
        r8 = r0.fileUnzipPath;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r8 = java.lang.String.valueOf(r8);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4.<init>(r8);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r8 = "/";
        r4 = r4.append(r8);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r7 = r4.toString();	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r3 = new com.ifoer.db.SaveDownloadRunnable;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r50;
        r12 = r0.context;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r50;
        r15 = r0.handler;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r50;
        r0 = r0.dto;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r16 = r0;
        r11 = r3;
        r13 = r6;
        r14 = r7;
        r11.<init>(r12, r13, r14, r15, r16);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r3.start();	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        goto L_0x024f;
    L_0x059e:
        r4 = "DownloadTask";
        r8 = "\u4e0b\u8f7d\u5931\u8d25";
        android.util.Log.i(r4, r8);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r50;
        r4 = r0.handler;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r8 = 0;
        r0 = r50;
        r9 = r0.softId;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4 = r4.obtainMessage(r8, r9);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4.sendToTarget();	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        goto L_0x024f;
    L_0x05b7:
        r38 = CRP.utils.CRPTools.getUsableSDCardSize();	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r24;
        r8 = (long) r0;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4 = (r38 > r8 ? 1 : (r38 == r8 ? 0 : -1));
        if (r4 >= 0) goto L_0x024f;
    L_0x05c2:
        r0 = r50;
        r4 = r0.handler;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r8 = 6;
        r0 = r50;
        r9 = r0.softId;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4 = r4.obtainMessage(r8, r9);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4.sendToTarget();	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r28 = new android.content.Intent;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4 = "show";
        r0 = r28;
        r0.<init>(r4);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r50;
        r4 = r0.context;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r28;
        r4.sendBroadcast(r0);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        goto L_0x024f;
    L_0x05e6:
        r41 = new java.io.File;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r50;
        r8 = r0.fileStorePath;	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r8 = java.lang.String.valueOf(r8);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4.<init>(r8);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r49;
        r4 = r4.append(r0);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4 = r4.toString();	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r0 = r41;
        r0.<init>(r4);	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        r4 = r41.exists();	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
        if (r4 == 0) goto L_0x0613;
    L_0x060a:
        r0 = r20;
        r1 = r24;
        if (r0 >= r1) goto L_0x0613;
    L_0x0610:
        r41.delete();	 Catch:{ Exception -> 0x043e, all -> 0x0536 }
    L_0x0613:
        if (r26 == 0) goto L_0x0618;
    L_0x0615:
        r26.close();	 Catch:{ IOException -> 0x0621 }
    L_0x0618:
        if (r29 == 0) goto L_0x061d;
    L_0x061a:
        r29.close();	 Catch:{ IOException -> 0x0621 }
    L_0x061d:
        r25 = r26;
        goto L_0x015c;
    L_0x0621:
        r21 = move-exception;
        r21.printStackTrace();
        goto L_0x061d;
    L_0x0626:
        r21 = move-exception;
        r21.printStackTrace();
        goto L_0x0543;
    L_0x062c:
        r21 = move-exception;
        r21.printStackTrace();
        goto L_0x015c;
    L_0x0632:
        r4 = move-exception;
        goto L_0x0539;
    L_0x0635:
        r21 = move-exception;
        goto L_0x0441;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ifoer.download.DownloadTask.run():void");
    }

    private boolean deleteDynamicFile(String filePath) {
        deleteAllFile(filePath);
        File file = new File(filePath);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    private void deleteAllFile(String path) {
        File file = new File(new StringBuilder(String.valueOf(path)).append(FilePathGenerator.ANDROID_DIR_SEP).toString());
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            if (files.length > 0) {
                for (File eachFile : files) {
                    if (eachFile.exists()) {
                        eachFile.delete();
                    }
                }
            }
        }
    }

    private ArrayList<CarVersionInfo> deleteOldVersion() {
        String serialNo1 = MySharedPreferences.getStringValue(this.context, MySharedPreferences.serialNoKey);
        ArrayList<CarVersionInfo> list = DBDao.getInstance(this.context).queryCarVersion(this.softPackageID, Locale.getDefault().getCountry().toUpperCase(), serialNo1, MainActivity.database);
        if (list.size() > 0) {
            return list;
        }
        country = Constants.DEFAULT_LANGUAGE;
        return DBDao.getInstance(this.context).queryCarVersion(this.softPackageID, Constants.DEFAULT_LANGUAGE, serialNo1, MainActivity.database);
    }

    public String getFileId() {
        return this.softId;
    }
}
