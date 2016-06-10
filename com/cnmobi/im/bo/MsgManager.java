package com.cnmobi.im.bo;

import android.util.Log;
import com.cnmobi.im.dao.MessageDao;
import com.cnmobi.im.dto.MessageVo;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.jivesoftware.smack.packet.Presence;

public class MsgManager {
    private static MsgManager instance;
    private MsgCallback chatWindowsCallbak;
    private List<MsgCallback> msgCallbacks;
    private Map<String, List<MessageVo>> newMessages;
    private List<PresenceCallback> presenceCallbacks;
    private String talk2Jid;

    private MsgManager() {
        this.newMessages = new HashMap();
        this.msgCallbacks = new ArrayList();
        this.presenceCallbacks = new ArrayList();
    }

    public static MsgManager getInstance() {
        if (instance == null) {
            instance = new MsgManager();
        }
        return instance;
    }

    public void resetTalk2Jid(String talk2Jid) {
        this.talk2Jid = talk2Jid;
    }

    public void resetChatWindowsCallBack(MsgCallback chatWindowsCallbak) {
        this.chatWindowsCallbak = chatWindowsCallbak;
    }

    public void addCallBack(MsgCallback callback) {
        this.msgCallbacks.add(callback);
    }

    public void removeCallBack(MsgCallback callback) {
        this.msgCallbacks.remove(callback);
    }

    public void addPresenceCallBack(PresenceCallback callback) {
        this.presenceCallbacks.add(callback);
    }

    public void removePresenceCallBack(PresenceCallback callback) {
        this.presenceCallbacks.remove(callback);
    }

    public void addNewMsg(MessageVo message) {
        List<MessageVo> messages = (List) this.newMessages.get(message.jId);
        if (messages == null) {
            messages = new ArrayList();
            this.newMessages.put(message.jId, messages);
        }
        MessageDao.getInstance().insert(message);
        if (!message.jId.equals(this.talk2Jid)) {
            messages.add(message);
            newMsgChange(message);
        } else if (this.chatWindowsCallbak != null) {
            this.chatWindowsCallbak.msgChange(message);
        }
    }

    public int getUnReadCount() {
        int result = 0;
        for (Entry<String, List<MessageVo>> entry : this.newMessages.entrySet()) {
            result += ((List) entry.getValue()).size();
        }
        return result;
    }

    public int getUnReadCountByJid(String jid) {
        List<MessageVo> messages = (List) this.newMessages.get(jid);
        if (messages != null) {
            return messages.size();
        }
        return 0;
    }

    public void read(String jId) {
        this.newMessages.remove(jId);
        newMsgChange(null);
    }

    public void newMsgChange(MessageVo message) {
        Log.i("newMsgChange", "\u901a\u77e5\u4e2a\u6570 :" + this.msgCallbacks.size());
        for (MsgCallback callback : this.msgCallbacks) {
            callback.msgChange(message);
        }
    }

    public void presenceChange(Presence presence) {
        Log.i("presenceChange", "\u901a\u77e5\u4e2a\u6570 :" + this.msgCallbacks.size());
        for (PresenceCallback callback : this.presenceCallbacks) {
            callback.presenceChange(presence);
        }
    }

    public void entriesChange(Collection<String> c, int type) {
        for (PresenceCallback callback : this.presenceCallbacks) {
            callback.entriesChange(c, type);
        }
    }
}
