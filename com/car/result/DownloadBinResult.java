package com.car.result;

import com.ifoer.entity.SoftMaxVersion;

public class DownloadBinResult extends WSResult {
    private static final long serialVersionUID = -1416365604265437005L;
    private int code;
    private SoftMaxVersion versioninfo;

    public DownloadBinResult() {
        this.versioninfo = new SoftMaxVersion();
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public SoftMaxVersion getVersioninfo() {
        return this.versioninfo;
    }

    public void setVersioninfo(SoftMaxVersion versioninfo) {
        this.versioninfo = versioninfo;
    }
}
