package com.tencent.mm.sdk.platformtools;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import junit.framework.Assert;
import org.xbill.DNS.KEYRecord.Flags;

public class MAlarmHandler {
    public static final long NEXT_FIRE_INTERVAL = Long.MAX_VALUE;
    private static int ac;
    private static Map<Integer, MAlarmHandler> ah;
    private static IBumper aj;
    private static boolean ak;
    private final int ad;
    private final boolean ae;
    private long af;
    private long ag;
    private final CallBack ai;

    public interface CallBack {
        boolean onTimerExpired();
    }

    public interface IBumper {
        void cancel();

        void prepare();
    }

    static {
        ah = new HashMap();
        ak = false;
    }

    public MAlarmHandler(CallBack callBack, boolean z) {
        this.af = 0;
        this.ag = 0;
        Assert.assertTrue("bumper not initialized", ak);
        this.ai = callBack;
        this.ae = z;
        if (ac >= Flags.FLAG2) {
            ac = 0;
        }
        int i = ac + 1;
        ac = i;
        this.ad = i;
    }

    public static long fire() {
        List linkedList = new LinkedList();
        Set<Integer> hashSet = new HashSet();
        hashSet.addAll(ah.keySet());
        long j = NEXT_FIRE_INTERVAL;
        for (Integer num : hashSet) {
            long j2;
            MAlarmHandler mAlarmHandler = (MAlarmHandler) ah.get(num);
            if (mAlarmHandler != null) {
                long ticksToNow = Util.ticksToNow(mAlarmHandler.af);
                if (ticksToNow < 0) {
                    ticksToNow = 0;
                }
                if (ticksToNow > mAlarmHandler.ag) {
                    if (mAlarmHandler.ai.onTimerExpired() && mAlarmHandler.ae) {
                        j = mAlarmHandler.ag;
                    } else {
                        linkedList.add(num);
                    }
                    mAlarmHandler.af = Util.currentTicks();
                } else if (mAlarmHandler.ag - ticksToNow < j) {
                    j2 = mAlarmHandler.ag - ticksToNow;
                    j = j2;
                }
            }
            j2 = j;
            j = j2;
        }
        for (int i = 0; i < linkedList.size(); i++) {
            ah.remove(linkedList.get(i));
        }
        if (j == NEXT_FIRE_INTERVAL && aj != null) {
            aj.cancel();
            Log.m1663v("MicroMsg.MAlarmHandler", "cancel bumper for no more handler");
        }
        return j;
    }

    public static void initAlarmBumper(IBumper iBumper) {
        ak = true;
        aj = iBumper;
    }

    protected void finalize() {
        stopTimer();
        super.finalize();
    }

    public void startTimer(long j) {
        this.ag = j;
        this.af = Util.currentTicks();
        long j2 = this.ag;
        Log.m1655d("MicroMsg.MAlarmHandler", "check need prepare: check=" + j2);
        long j3 = NEXT_FIRE_INTERVAL;
        for (Entry value : ah.entrySet()) {
            long j4;
            MAlarmHandler mAlarmHandler = (MAlarmHandler) value.getValue();
            if (mAlarmHandler != null) {
                long ticksToNow = Util.ticksToNow(mAlarmHandler.af);
                if (ticksToNow < 0) {
                    ticksToNow = 0;
                }
                if (ticksToNow > mAlarmHandler.ag) {
                    j3 = mAlarmHandler.ag;
                } else if (mAlarmHandler.ag - ticksToNow < j3) {
                    j4 = mAlarmHandler.ag - ticksToNow;
                    j3 = j4;
                }
            }
            j4 = j3;
            j3 = j4;
        }
        Object obj = j3 > j2 ? 1 : null;
        stopTimer();
        ah.put(Integer.valueOf(this.ad), this);
        if (aj != null && obj != null) {
            Log.m1663v("MicroMsg.MAlarmHandler", "prepare bumper");
            aj.prepare();
        }
    }

    public void stopTimer() {
        ah.remove(Integer.valueOf(this.ad));
    }

    public boolean stopped() {
        return !ah.containsKey(Integer.valueOf(this.ad));
    }
}
