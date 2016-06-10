package com.ifoer.db;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import com.cnlaunch.x431pro.module.upgrade.model.X431PadDtoSoft;
import com.ifoer.util.Unzip;
import java.io.File;

public class SaveDownloadRunnable extends Thread {
    private static final boolean f1225D = true;
    private Context context;
    private X431PadDtoSoft dto;
    private Handler handler;
    private String unzipPath;
    private String zipPath;

    public SaveDownloadRunnable(Context context, String zipPath, String unzipPath, Handler handler, X431PadDtoSoft dto) {
        this.zipPath = zipPath;
        this.unzipPath = unzipPath;
        this.context = context;
        this.handler = handler;
        this.dto = dto;
    }

    public void run() {
        super.run();
        this.dto.setState(3);
        this.handler.obtainMessage(this.dto.getState(), this.dto).sendToTarget();
        String message = Unzip.unZip(this.zipPath, this.unzipPath, false);
        File file = new File(this.zipPath);
        this.context.sendBroadcast(new Intent("show"));
        if (message.equals("success")) {
            this.dto.setState(4);
            this.handler.obtainMessage(this.dto.getState(), this.dto).sendToTarget();
            Log.i("SaveDownLoadRunable", "\u89e3\u538b\u6210\u529f");
            if (file.exists()) {
                file.delete();
                return;
            }
            return;
        }
        this.dto.setState(5);
        this.handler.obtainMessage(this.dto.getState(), this.dto).sendToTarget();
        System.out.println("------\u89e3\u538b\u5931\u8d25");
    }
}
