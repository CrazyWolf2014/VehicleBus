package com.cnmobi.im.bo;

import android.util.Log;
import com.cnmobi.im.dao.MessageDao;
import com.cnmobi.im.dao.RiderDao;
import com.cnmobi.im.dto.MessageVo;
import com.cnmobi.im.dto.RiderVo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.jivesoftware.smack.packet.PrivacyItem.PrivacyRule;

public class CnmobiImManager {
    private static final String TAG = "CnmobiImManager";
    private static CnmobiImManager instance;
    private Map<String, CnmobiImListener> imListeners;

    static {
        instance = new CnmobiImManager();
    }

    private CnmobiImManager() {
        this.imListeners = new HashMap();
    }

    public static CnmobiImManager getInstance() {
        return instance;
    }

    public void addImListener(String key, CnmobiImListener imListener) {
        this.imListeners.put(key, imListener);
    }

    public List<RiderVo> getRiders(String ownerJid) throws Exception {
        return RiderDao.getInstance().getRiders(ownerJid, PrivacyRule.SUBSCRIPTION_BOTH);
    }

    public List<RiderVo> getRecentRiders(String ownerJid) throws Exception {
        List<MessageVo> messages = MessageDao.getInstance().getRecentMessages(ownerJid);
        if (messages == null || messages.size() == 0) {
            return null;
        }
        List<RiderVo> riders = new ArrayList();
        for (MessageVo message : messages) {
            RiderVo rider = RiderDao.getInstance().getRiderByJid(message.jId, ownerJid);
            if (rider != null) {
                rider.signature = message.content;
                if (PrivacyRule.SUBSCRIPTION_BOTH.equals(rider.type)) {
                    riders.add(rider);
                }
            }
        }
        return riders;
    }

    public void removeImListener(String key) {
        this.imListeners.remove(key);
    }

    public void riderChange(List<RiderVo> riders) {
        try {
            RiderDao.getInstance().clearTable();
        } catch (Exception e) {
            Log.e(TAG, "Clean Rider table error:", e);
        }
        for (RiderVo rider : riders) {
            try {
                RiderDao.getInstance().insert(rider);
            } catch (Exception e2) {
                Log.e(TAG, "Insert Rider table error:", e2);
            }
        }
        for (Entry<String, CnmobiImListener> entry : this.imListeners.entrySet()) {
            ((CnmobiImListener) entry.getValue()).riderChange(riders);
        }
    }
}
