package com.cnlaunch.framework.network.download;

import android.content.Context;
import com.cnlaunch.framework.network.http.RequestParams;

public class DownloadParam {
    private Context context;
    private String downPath;
    private boolean enable_breakpoint;
    private String fileName;
    private long fileSize;
    private RequestParams params;
    private String url;
    private String versionNo;

    public DownloadParam() {
        this.enable_breakpoint = false;
    }

    public Context getContext() {
        return this.context;
    }

    public void setContext(Context mContext) {
        this.context = mContext;
    }

    public RequestParams getParams() {
        return this.params;
    }

    public void setParams(RequestParams params) {
        this.params = params;
    }

    public String getVersionNo() {
        return this.versionNo;
    }

    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDownPath() {
        return this.downPath;
    }

    public void setDownPath(String downPath) {
        this.downPath = downPath;
    }

    public long getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public void setEnableBreakpoint(boolean enable) {
        this.enable_breakpoint = enable;
    }

    public boolean getEnableBreakpoint() {
        return this.enable_breakpoint;
    }
}
