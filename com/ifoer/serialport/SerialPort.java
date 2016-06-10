package com.ifoer.serialport;

import CRP.utils.CRPTools;
import CRP.utils.LibraryLoader;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialPort {
    private static final String TAG = "SerialPort";
    public static SerialPort instance;
    private FileDescriptor mFd;
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;

    private static native FileDescriptor open(String str, int i, int i2);

    public native void close();

    static {
        instance = null;
        LibraryLoader.load(CRPTools.CRP_SERIALPORT_LIB);
    }

    public static SerialPort getInsance(String device, int baudrate, int flags) {
        if (instance == null) {
            try {
                instance = new SerialPort(device, baudrate, flags);
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        return instance;
    }

    public SerialPort(String device, int baudrate, int flags) throws SecurityException, IOException {
        this.mFd = open(device, baudrate, flags);
        if (this.mFd == null) {
            throw new IOException();
        }
        this.mFileInputStream = new FileInputStream(this.mFd);
        this.mFileOutputStream = new FileOutputStream(this.mFd);
    }

    public InputStream getInputStream() {
        return this.mFileInputStream;
    }

    public OutputStream getOutputStream() {
        return this.mFileOutputStream;
    }
}
