package com.tencent.mm.sdk.platformtools;

import java.util.HashSet;
import java.util.Set;

public final class MMEntryLock {
    private static Set<String> an;

    static {
        an = new HashSet();
    }

    private MMEntryLock() {
    }

    public static boolean isLocked(String str) {
        return an.contains(str);
    }

    public static boolean lock(String str) {
        if (isLocked(str)) {
            Log.m1655d("MicroMsg.MMEntryLock", "locked-" + str);
            return false;
        }
        Log.m1655d("MicroMsg.MMEntryLock", "lock-" + str);
        return an.add(str);
    }

    public static void unlock(String str) {
        an.remove(str);
        Log.m1655d("MicroMsg.MMEntryLock", "unlock-" + str);
    }
}
