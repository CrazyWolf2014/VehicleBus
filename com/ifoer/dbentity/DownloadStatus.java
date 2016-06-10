package com.ifoer.dbentity;

public class DownloadStatus {
    private long downloadSize;
    private long fileSize;
    private String status;
    private boolean statusRedownload;

    public long getDownloadSize() {
        return this.downloadSize;
    }

    public void setDownloadSize(long downloadSize) {
        this.downloadSize = downloadSize;
    }

    public long getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isStatusRedownload() {
        return this.statusRedownload;
    }

    public void setStatusRedownload(boolean statusRedownload) {
        this.statusRedownload = statusRedownload;
    }
}
