package com.ifoer.expedition.BluetoothOrder;

public class StatisticHelper {
    int bytesPerSecond;
    int deltaBytes;
    long deltaTimeInMillis;
    int restTimeInSeconds;

    public void calcResults(long restBytes, int deltaBytes, long startTime, long endTime) {
        this.deltaTimeInMillis = endTime - startTime;
        if (this.deltaTimeInMillis != 0) {
            this.bytesPerSecond = (int) (((long) (deltaBytes * 1000)) / this.deltaTimeInMillis);
            this.restTimeInSeconds = (int) (restBytes / ((long) this.bytesPerSecond));
            return;
        }
        this.bytesPerSecond = 60;
        this.restTimeInSeconds = 60;
    }

    public int getRestSeconds() {
        return this.restTimeInSeconds % 60;
    }

    public int getRestMinutes() {
        return this.restTimeInSeconds / 60;
    }

    public int getRestHours() {
        return this.restTimeInSeconds / 3600;
    }

    public int getTransmissionSpeed() {
        return this.bytesPerSecond;
    }
}
