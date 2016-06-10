package com.ifoer.download;

import android.content.Context;
import android.os.Handler;
import com.cnlaunch.framework.common.Constants;
import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import com.cnlaunch.x431pro.module.upgrade.model.X431PadDtoSoft;
import com.ifoer.db.SaveDownloadRunnable;
import com.ifoer.entity.Constant;
import com.ifoer.md5.MD5;
import com.ifoer.util.MySharedPreferences;
import com.tencent.mm.sdk.platformtools.FilePathGenerator;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import org.jivesoftware.smackx.packet.MultipleAddresses;
import org.xbill.DNS.KEYRecord.Flags;

public class DownloadBinNewVersion implements Runnable {
    private boolean f1227D;
    private Context context;
    private String downloadUrl;
    private X431PadDtoSoft dto;
    private Handler handler;
    String serialNo;
    private String softId;
    private String sotrePath;
    private String unzipPath;
    private String versionDetailId;
    private String versionNo;

    public DownloadBinNewVersion(Context context, Handler handler, X431PadDtoSoft dto, String serialNo) {
        this.downloadUrl = "http://mycardl.x431.com/mobile/softCenter/downloadPhoneSoftWs.action";
        this.sotrePath = Constant.STORE_PATH;
        this.unzipPath = Constant.UNZIP_PATH;
        this.f1227D = false;
        this.context = context;
        this.softId = dto.getSoftId();
        this.handler = handler;
        this.versionDetailId = dto.getVersionDetailId();
        this.versionNo = dto.getVersionNo();
        this.dto = dto;
        this.serialNo = MySharedPreferences.getStringValue(context, MySharedPreferences.serialNoKey);
    }

    public void run() {
        Throwable th;
        String postData = "versionDetailId=" + this.versionDetailId;
        String cc = MySharedPreferences.getStringValue(this.context, MySharedPreferences.CCKey);
        String sign = MD5.getMD5Str(this.serialNo + this.versionDetailId + MySharedPreferences.getStringValue(this.context, MySharedPreferences.TokenKey));
        if (this.f1227D) {
            System.out.println("postData==" + postData);
        }
        OutputStream fos = null;
        InputStream is = null;
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(this.downloadUrl).openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", AsyncHttpResponseHandler.DEFAULT_CHARSET);
            con.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            con.setRequestProperty(MultipleAddresses.CC, cc);
            con.setRequestProperty(Constants.SIGN, sign);
            con.setDoInput(true);
            con.setDoOutput(true);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(con.getOutputStream());
            outputStreamWriter.write(postData);
            outputStreamWriter.flush();
            outputStreamWriter.close();
            is = con.getInputStream();
            int fileSize = con.getContentLength();
            int downloadSize = 0;
            if (fileSize <= 0) {
                this.handler.obtainMessage(0, this.softId).sendToTarget();
            } else {
                byte[] bs = new byte[Flags.FLAG5];
                File file = new File(this.sotrePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                String zipFileName = "Download_CRP229_V" + this.versionNo.replace(".", "_") + ".zip";
                OutputStream fos2 = new FileOutputStream(this.sotrePath + zipFileName);
                while (true) {
                    try {
                        int len = is.read(bs);
                        if (len == -1) {
                            break;
                        }
                        if (this.f1227D) {
                            System.out.println("\u4e0b\u8f7d..");
                        }
                        fos2.write(bs, 0, len);
                        downloadSize += len;
                        if (this.handler.hasMessages(1)) {
                            this.handler.removeMessages(1);
                        }
                        this.handler.obtainMessage(1, downloadSize, fileSize, this.softId).sendToTarget();
                        if (downloadSize == fileSize) {
                            this.handler.obtainMessage(2, downloadSize, fileSize, this.softId).sendToTarget();
                            new SaveDownloadRunnable(this.context, this.sotrePath + zipFileName, this.unzipPath + FilePathGenerator.ANDROID_DIR_SEP, this.handler, this.dto).start();
                        }
                    } catch (Exception e) {
                        fos = fos2;
                    } catch (Throwable th2) {
                        th = th2;
                        fos = fos2;
                    }
                }
                fos = fos2;
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                    return;
                }
            }
            if (is != null) {
                is.close();
            }
        } catch (Exception e3) {
            try {
                this.handler.obtainMessage(0, this.softId).sendToTarget();
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e22) {
                        e22.printStackTrace();
                        return;
                    }
                }
                if (is != null) {
                    is.close();
                }
            } catch (Throwable th3) {
                th = th3;
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e222) {
                        e222.printStackTrace();
                        throw th;
                    }
                }
                if (is != null) {
                    is.close();
                }
                throw th;
            }
        }
    }

    public String getFileId() {
        return this.softId;
    }
}
