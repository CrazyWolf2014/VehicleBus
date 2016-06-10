package com.cnlaunch.x431pro.utils.file;

public interface UnZipListener {
    void error(int i, Throwable th);

    void finished();

    void progress(int i, int i2);

    void start();
}
