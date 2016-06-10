package com.cnmobi.im.bo;

import com.cnmobi.im.dao.RiderDao;
import com.cnmobi.im.dto.RiderVo;
import com.cnmobi.im.util.XmppConnection;
import com.cnmobi.im.util.XmppService;
import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.smack.RosterEntry;

public class RiderManager {
    private static RiderManager instance;

    private RiderManager() {
    }

    public static RiderManager getInstance() {
        if (instance == null) {
            instance = new RiderManager();
        }
        return instance;
    }

    public void refresh(String ownerJid) throws Exception {
        List<RosterEntry> entries = XmppService.getAllEntries(XmppConnection.getConnection().getRoster());
        List<RiderVo> ridersFromServer = new ArrayList();
        for (RosterEntry entry : entries) {
            RiderVo rider = new RiderVo();
            String type = entry.getType().name();
            String showName = entry.getName();
            if (showName == null) {
                showName = entry.getUser().split(XmppConnection.JID_SEPARATOR)[0];
            }
            rider.name = showName;
            rider.signature = "\u79bb\u7ebf";
            rider.jId = entry.getUser();
            rider.online = 0;
            rider.type = type;
            ridersFromServer.add(rider);
        }
        List<RiderVo> ridersFromDb = RiderDao.getInstance().getAllRiders(ownerJid);
        if (ridersFromDb != null && ridersFromDb.size() > 0) {
            for (RiderVo rider2 : ridersFromDb) {
                if (!isRiderInServer(ridersFromServer, rider2.jId)) {
                    RiderDao.getInstance().delete(rider2.jId, ownerJid);
                }
            }
        }
        for (RiderVo rider22 : ridersFromServer) {
            if (RiderDao.getInstance().isRiderExist(rider22.jId, ownerJid)) {
                RiderDao.getInstance().update(rider22);
            } else {
                RiderDao.getInstance().insert(rider22);
            }
        }
    }

    public boolean isRiderInServer(List<RiderVo> ridersFromServer, String jid) {
        if (ridersFromServer == null || ridersFromServer.size() == 0) {
            return false;
        }
        for (RiderVo rider : ridersFromServer) {
            if (rider.jId.equals(jid)) {
                return true;
            }
        }
        return false;
    }

    public void updatePresence(String jId, int online, String signature) throws Exception {
        RiderDao.getInstance().update(jId, online, signature);
    }
}
