package org.vudroid.core;

public class VuDroidLibraryLoader {
    private static boolean alreadyLoaded;

    static {
        alreadyLoaded = false;
    }

    public static void load() {
        if (!alreadyLoaded) {
            System.loadLibrary("vudroid");
            alreadyLoaded = true;
        }
    }
}
