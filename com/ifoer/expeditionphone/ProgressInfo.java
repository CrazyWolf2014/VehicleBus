package com.ifoer.expeditionphone;

import java.io.File;

public class ProgressInfo {
    int current;
    File file;
    int fileSum;
    int leftHours;
    int leftMinites;
    int leftSeconds;
    String msg;
    int percent;
    int sentBytes;
    int speedKB;
    int totalBytes;

    public int getFileSum() {
        return this.fileSum;
    }

    public ProgressInfo setFileSum(int fileSum) {
        this.fileSum = fileSum;
        return this;
    }

    public int getCurrent() {
        return this.current;
    }

    public ProgressInfo setCurrent(int current) {
        this.current = current;
        return this;
    }

    public ProgressInfo(File file, String msg, int totalBytes, int sentBytes, int speedKB, int leftSeconds, int leftMinites, int leftHours) {
        this.file = file;
        this.msg = msg;
        this.totalBytes = totalBytes;
        this.sentBytes = sentBytes;
        this.speedKB = speedKB;
        this.leftSeconds = leftSeconds;
        this.leftMinites = leftMinites;
        this.leftHours = leftHours;
    }

    public int getPercent() {
        return this.percent;
    }

    public ProgressInfo setPercent(int percent) {
        this.percent = percent;
        return this;
    }

    public File getFile() {
        return this.file;
    }

    public ProgressInfo setFile(File file) {
        this.file = file;
        return this;
    }

    public String getMsg() {
        return this.msg;
    }

    public ProgressInfo setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public int getTotalBytes() {
        return this.totalBytes;
    }

    public ProgressInfo setTotalBytes(int totalBytes) {
        this.totalBytes = totalBytes;
        return this;
    }

    public int getSentBytes() {
        return this.sentBytes;
    }

    public ProgressInfo setSentBytes(int sentBytes) {
        this.sentBytes = sentBytes;
        return this;
    }

    public int getSpeedKB() {
        return this.speedKB;
    }

    public ProgressInfo setSpeedKB(int speedKB) {
        this.speedKB = speedKB;
        return this;
    }

    public int getLeftSeconds() {
        return this.leftSeconds;
    }

    public ProgressInfo setLeftSeconds(int leftSeconds) {
        this.leftSeconds = leftSeconds;
        return this;
    }

    public int getLeftMinites() {
        return this.leftMinites;
    }

    public ProgressInfo setLeftMinites(int leftMinites) {
        this.leftMinites = leftMinites;
        return this;
    }

    public int getLeftHours() {
        return this.leftHours;
    }

    public ProgressInfo setLeftHours(int leftHours) {
        this.leftHours = leftHours;
        return this;
    }

    public String toString() {
        return "ProgressInfo [file=" + this.file + ", msg=" + this.msg + ", totalBytes=" + this.totalBytes + ", sentBytes=" + this.sentBytes + ", speedKB=" + this.speedKB + ", leftSeconds=" + this.leftSeconds + ", leftMinites=" + this.leftMinites + ", leftHours=" + this.leftHours + "]";
    }
}
