package com.ifoer.db;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import com.cnlaunch.x431pro.module.upgrade.model.X431PadDtoSoft;
import com.ifoer.dbentity.Car;
import com.ifoer.dbentity.SerialNumber;
import com.ifoer.expedition.ndk.MakeLicense;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.util.FindFiles;
import com.ifoer.util.Unzip;
import com.tencent.mm.sdk.platformtools.FilePathGenerator;
import java.io.File;

public class SaveDataRunnable extends Thread {
    private static final boolean f1224D = false;
    private Car car;
    private Context context;
    private X431PadDtoSoft dto;
    private Handler handler;
    private String serialNo;
    private String unzipPath;
    private String versionPath;
    private String zipPath;

    public SaveDataRunnable(Context context, String versionPath, String zipPath, String unzipPath, X431PadDtoSoft dto, Handler handler, String serialNo) {
        this.versionPath = versionPath;
        this.zipPath = zipPath;
        this.unzipPath = unzipPath;
        this.dto = dto;
        this.context = context;
        this.handler = handler;
        this.serialNo = serialNo;
    }

    public void run() {
        super.run();
        this.dto.setState(3);
        this.handler.obtainMessage(this.dto.getState(), this.dto).sendToTarget();
        String message = Unzip.unZip(this.zipPath, this.unzipPath, false);
        this.context.sendBroadcast(new Intent("show"));
        File file = new File(this.zipPath);
        if (message.equals("success")) {
            boolean succeedMakeLicense = new MakeLicense().autoMakeLicense(this.zipPath, this.versionPath + this.dto.getVersionNo() + FilePathGenerator.ANDROID_DIR_SEP + "LICENSE.DAT");
            this.dto.setState(4);
            this.handler.obtainMessage(this.dto.getState(), this.dto).sendToTarget();
            int areaId = 0;
            if (this.dto.getSoftApplicableArea() != null) {
                areaId = Integer.parseInt(this.dto.getSoftApplicableArea());
            }
            SerialNumber serialNum = FindFiles.getFilePathFromSD(this.versionPath, this.dto.getSoftPackageID(), areaId, this.serialNo);
            if (!(serialNum == null || serialNum.getCarList() == null || serialNum.getCarList().size() <= 0)) {
                DBDao.getInstance(this.context).addToCarVer(serialNum, MainActivity.database);
                this.context.sendBroadcast(new Intent("refreshUi"));
            }
        } else {
            this.dto.setState(5);
            this.handler.obtainMessage(this.dto.getState(), this.dto).sendToTarget();
        }
        if (file.exists() && !file.delete()) {
        }
    }
}
