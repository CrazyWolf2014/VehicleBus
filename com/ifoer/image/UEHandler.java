package com.ifoer.image;

import android.content.Intent;
import android.os.Process;
import android.util.Log;
import com.ifoer.expeditionphone.WelcomeActivity;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;

public class UEHandler implements UncaughtExceptionHandler {
    private File fileErrorLog;
    private GDApplication softApp;

    public UEHandler(GDApplication app) {
        this.softApp = app;
        this.fileErrorLog = new File(GDApplication.PATH_ERROR_LOG);
    }

    public void uncaughtException(Thread thread, Throwable ex) {
        Exception e;
        Intent intent;
        Throwable th;
        String info = null;
        ByteArrayOutputStream baos = null;
        PrintStream printStream = null;
        try {
            ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
            try {
                PrintStream printStream2 = new PrintStream(baos2);
                try {
                    ex.printStackTrace(printStream2);
                    String info2 = new String(baos2.toByteArray());
                    if (printStream2 != null) {
                        try {
                            printStream2.close();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                    if (baos2 != null) {
                        baos2.close();
                        printStream = printStream2;
                        baos = baos2;
                        info = info2;
                        Log.d("ANDROID_LAB", "Thread.getName()=" + thread.getName() + " id=" + thread.getId() + " state=" + thread.getState());
                        Log.d("ANDROID_LAB", "Error[" + info + "]");
                        intent = new Intent(this.softApp, WelcomeActivity.class);
                        intent.setFlags(268435456);
                        this.softApp.startActivity(intent);
                        write2ErrorLog(this.fileErrorLog, info);
                        Process.killProcess(Process.myPid());
                    }
                    printStream = printStream2;
                    baos = baos2;
                    info = info2;
                } catch (Exception e3) {
                    e2 = e3;
                    printStream = printStream2;
                    baos = baos2;
                    try {
                        e2.printStackTrace();
                        if (printStream != null) {
                            try {
                                printStream.close();
                            } catch (Exception e22) {
                                e22.printStackTrace();
                            }
                        }
                        if (baos != null) {
                            baos.close();
                        }
                        Log.d("ANDROID_LAB", "Thread.getName()=" + thread.getName() + " id=" + thread.getId() + " state=" + thread.getState());
                        Log.d("ANDROID_LAB", "Error[" + info + "]");
                        intent = new Intent(this.softApp, WelcomeActivity.class);
                        intent.setFlags(268435456);
                        this.softApp.startActivity(intent);
                        write2ErrorLog(this.fileErrorLog, info);
                        Process.killProcess(Process.myPid());
                    } catch (Throwable th2) {
                        th = th2;
                        if (printStream != null) {
                            try {
                                printStream.close();
                            } catch (Exception e222) {
                                e222.printStackTrace();
                                throw th;
                            }
                        }
                        if (baos != null) {
                            baos.close();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    printStream = printStream2;
                    baos = baos2;
                    if (printStream != null) {
                        printStream.close();
                    }
                    if (baos != null) {
                        baos.close();
                    }
                    throw th;
                }
            } catch (Exception e4) {
                e222 = e4;
                baos = baos2;
                e222.printStackTrace();
                if (printStream != null) {
                    printStream.close();
                }
                if (baos != null) {
                    baos.close();
                }
                Log.d("ANDROID_LAB", "Thread.getName()=" + thread.getName() + " id=" + thread.getId() + " state=" + thread.getState());
                Log.d("ANDROID_LAB", "Error[" + info + "]");
                intent = new Intent(this.softApp, WelcomeActivity.class);
                intent.setFlags(268435456);
                this.softApp.startActivity(intent);
                write2ErrorLog(this.fileErrorLog, info);
                Process.killProcess(Process.myPid());
            } catch (Throwable th4) {
                th = th4;
                baos = baos2;
                if (printStream != null) {
                    printStream.close();
                }
                if (baos != null) {
                    baos.close();
                }
                throw th;
            }
        } catch (Exception e5) {
            e222 = e5;
            e222.printStackTrace();
            if (printStream != null) {
                printStream.close();
            }
            if (baos != null) {
                baos.close();
            }
            Log.d("ANDROID_LAB", "Thread.getName()=" + thread.getName() + " id=" + thread.getId() + " state=" + thread.getState());
            Log.d("ANDROID_LAB", "Error[" + info + "]");
            intent = new Intent(this.softApp, WelcomeActivity.class);
            intent.setFlags(268435456);
            this.softApp.startActivity(intent);
            write2ErrorLog(this.fileErrorLog, info);
            Process.killProcess(Process.myPid());
        }
        Log.d("ANDROID_LAB", "Thread.getName()=" + thread.getName() + " id=" + thread.getId() + " state=" + thread.getState());
        Log.d("ANDROID_LAB", "Error[" + info + "]");
        intent = new Intent(this.softApp, WelcomeActivity.class);
        intent.setFlags(268435456);
        this.softApp.startActivity(intent);
        write2ErrorLog(this.fileErrorLog, info);
        Process.killProcess(Process.myPid());
    }

    private void write2ErrorLog(File file, String content) {
        Exception e;
        Throwable th;
        FileOutputStream fos = null;
        try {
            if (file.exists()) {
                file.delete();
            } else {
                file.getParentFile().mkdirs();
            }
            file.createNewFile();
            FileOutputStream fos2 = new FileOutputStream(file);
            try {
                fos2.write(content.getBytes());
                if (fos2 != null) {
                    try {
                        fos2.close();
                        fos = fos2;
                        return;
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
                fos = fos2;
            } catch (Exception e3) {
                e2 = e3;
                fos = fos2;
                try {
                    e2.printStackTrace();
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (Exception e22) {
                            e22.printStackTrace();
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (Exception e222) {
                            e222.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                fos = fos2;
                if (fos != null) {
                    fos.close();
                }
                throw th;
            }
        } catch (Exception e4) {
            e222 = e4;
            e222.printStackTrace();
            if (fos != null) {
                fos.close();
            }
        }
    }
}
