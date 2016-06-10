package com.tencent.mm.sdk.platformtools;

import android.content.Context;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.xbill.DNS.KEYRecord;

public final class FilesCopy {
    private FilesCopy() {
    }

    public static boolean copy(String str, String str2, boolean z) {
        int i = 0;
        File file = new File(str);
        if (!file.exists()) {
            return false;
        }
        File file2 = new File(str2);
        if (file.isFile()) {
            if (!file2.isFile() && file2.exists()) {
                return false;
            }
            copyFile(str, str2);
            if (z) {
                file.delete();
            }
        } else if (file.isDirectory()) {
            if (!file2.exists()) {
                file2.mkdir();
            }
            if (!file2.isDirectory()) {
                return false;
            }
            String[] list = file.list();
            while (i < list.length) {
                copy(str + FilePathGenerator.ANDROID_DIR_SEP + list[i], str2 + FilePathGenerator.ANDROID_DIR_SEP + list[i], z);
                i++;
            }
        }
        return true;
    }

    public static boolean copyAssets(Context context, String str, String str2) {
        Exception e;
        Throwable th;
        FileOutputStream fileOutputStream;
        try {
            InputStream open = context.getAssets().open(str);
            fileOutputStream = new FileOutputStream(str2);
            try {
                byte[] bArr = new byte[KEYRecord.FLAG_NOCONF];
                while (true) {
                    int read = open.read(bArr);
                    if (read == -1) {
                        break;
                    }
                    fileOutputStream.write(bArr, 0, read);
                }
                File file = new File(str2);
                open.close();
                boolean z = file.exists() && ((long) context.getAssets().open(str).available()) == file.length();
                try {
                    fileOutputStream.close();
                    return z;
                } catch (IOException e2) {
                    Log.m1658e("FilesCopy", null, e2);
                    return false;
                }
            } catch (Exception e3) {
                e = e3;
                try {
                    e.printStackTrace();
                    if (fileOutputStream != null) {
                        return false;
                    }
                    try {
                        fileOutputStream.close();
                        return false;
                    } catch (IOException e22) {
                        Log.m1658e("FilesCopy", null, e22);
                        return false;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.close();
                        } catch (IOException e4) {
                            Log.m1658e("FilesCopy", null, e4);
                        }
                    }
                    throw th;
                }
            }
        } catch (Exception e5) {
            e = e5;
            fileOutputStream = null;
            e.printStackTrace();
            if (fileOutputStream != null) {
                return false;
            }
            fileOutputStream.close();
            return false;
        } catch (Throwable th3) {
            th = th3;
            fileOutputStream = null;
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            throw th;
        }
    }

    public static boolean copyFile(String str, String str2) {
        FileOutputStream fileOutputStream;
        Exception e;
        Throwable th;
        FileInputStream fileInputStream = null;
        FileInputStream fileInputStream2;
        try {
            fileInputStream2 = new FileInputStream(str);
            try {
                fileOutputStream = new FileOutputStream(str2);
                try {
                    byte[] bArr = new byte[KEYRecord.FLAG_NOCONF];
                    while (true) {
                        int read = fileInputStream2.read(bArr);
                        if (read == -1) {
                            break;
                        }
                        fileOutputStream.write(bArr, 0, read);
                    }
                    boolean z = true;
                    try {
                        fileInputStream2.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                        z = false;
                    }
                    try {
                        fileOutputStream.close();
                        return z;
                    } catch (IOException e22) {
                        e22.printStackTrace();
                        return false;
                    }
                } catch (Exception e3) {
                    e = e3;
                    fileInputStream = fileInputStream2;
                    try {
                        e.printStackTrace();
                        if (fileInputStream != null) {
                            try {
                                fileInputStream.close();
                            } catch (IOException e222) {
                                e222.printStackTrace();
                            }
                        }
                        if (fileOutputStream != null) {
                            return false;
                        }
                        try {
                            fileOutputStream.close();
                            return false;
                        } catch (IOException e2222) {
                            e2222.printStackTrace();
                            return false;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        fileInputStream2 = fileInputStream;
                        if (fileInputStream2 != null) {
                            try {
                                fileInputStream2.close();
                            } catch (IOException e4) {
                                e4.printStackTrace();
                            }
                        }
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                            } catch (IOException e42) {
                                e42.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    if (fileInputStream2 != null) {
                        fileInputStream2.close();
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    throw th;
                }
            } catch (Exception e5) {
                e = e5;
                fileOutputStream = null;
                fileInputStream = fileInputStream2;
                e.printStackTrace();
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (fileOutputStream != null) {
                    return false;
                }
                fileOutputStream.close();
                return false;
            } catch (Throwable th4) {
                th = th4;
                fileOutputStream = null;
                if (fileInputStream2 != null) {
                    fileInputStream2.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                throw th;
            }
        } catch (Exception e6) {
            e = e6;
            fileOutputStream = null;
            e.printStackTrace();
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            if (fileOutputStream != null) {
                return false;
            }
            fileOutputStream.close();
            return false;
        } catch (Throwable th5) {
            th = th5;
            fileOutputStream = null;
            fileInputStream2 = null;
            if (fileInputStream2 != null) {
                fileInputStream2.close();
            }
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            throw th;
        }
    }
}
