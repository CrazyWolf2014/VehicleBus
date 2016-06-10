package CRP.utils;

import android.util.Log;

public class LibraryLoader {
    static final String TAG = "LibLoader";

    public static void load(String name) {
        Log.d(TAG, "Trying to load library " + name + " from LD_PATH: " + System.getProperty("java.library.path"));
        try {
            System.loadLibrary(name);
        } catch (UnsatisfiedLinkError e) {
            Log.e(TAG, e.toString());
        }
    }
}
