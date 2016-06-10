package com.ifoer.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.storage.StorageManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SuppressLint({"ServiceCast"})
public class StorageList {
    private Context mContext;
    private Method mMethodGetPaths;
    private StorageManager mStorageManager;

    public StorageList(Context contex) {
        this.mContext = contex;
        if (this.mContext != null) {
            this.mStorageManager = (StorageManager) this.mContext.getSystemService("storage");
            try {
                this.mMethodGetPaths = this.mStorageManager.getClass().getMethod("getVolumePaths", new Class[0]);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    public String[] getVolumePaths() {
        String[] paths = null;
        try {
            return (String[]) this.mMethodGetPaths.invoke(this.mStorageManager, new Object[0]);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return paths;
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
            return paths;
        } catch (InvocationTargetException e3) {
            e3.printStackTrace();
            return paths;
        }
    }
}
