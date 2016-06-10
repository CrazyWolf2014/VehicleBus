package com.cnmobi.im.dao;

import android.database.Cursor;
import com.cnmobi.im.ImMainActivity;
import com.cnmobi.im.dto.RiderVo;
import java.util.ArrayList;
import java.util.List;

public class RiderDao {
    private static RiderDao instance;
    private MySQLiteOpenHelper mySqlHelper;

    private RiderDao() {
        this.mySqlHelper = MySQLiteOpenHelper.getInstance(ImMainActivity.context);
    }

    public static RiderDao getInstance() {
        if (instance == null) {
            instance = new RiderDao();
        }
        return instance;
    }

    public void clearTable() throws Exception {
        this.mySqlHelper.getWritableDatabase().execSQL("delete from rider");
    }

    public boolean isRiderExist(String jId, String ownerJid) throws Exception {
        boolean result = false;
        String[] selectionArgs = new String[]{jId, ownerJid};
        Cursor cursor = this.mySqlHelper.getReadableDatabase().rawQuery("SELECT jId FROM rider WHERE jId=? AND ownerJid=?", selectionArgs);
        String jidFromDataBase = null;
        if (cursor.moveToFirst()) {
            jidFromDataBase = cursor.getString(0);
        }
        if (jidFromDataBase != null) {
            result = true;
        }
        cursor.close();
        return result;
    }

    public void insert(RiderVo rider) throws Exception {
        rider.ownerJid = ImMainActivity.mOwerJid;
        Object[] bindArgs = new Object[]{rider.jId, rider.name, rider.logoUri, Integer.valueOf(rider.online), rider.signature, rider.type, rider.ownerJid};
        this.mySqlHelper.getWritableDatabase().execSQL("INSERT INTO rider(jId,name,logoUri,online,signature,type,ownerJid)VALUES(?,?,?,?,?,?,?)", bindArgs);
    }

    public RiderVo getRiderByJid(String jid, String ownerJid) throws Exception {
        String[] bindArgs = new String[]{jid, ownerJid};
        Cursor cursor = this.mySqlHelper.getReadableDatabase().rawQuery("SELECT jId,name,logoUri,online,signature,type FROM rider WHERE jId=? AND ownerJid=?", bindArgs);
        RiderVo rider = null;
        if (cursor.moveToNext()) {
            rider = new RiderVo();
            rider.jId = cursor.getString(0);
            rider.name = cursor.getString(1);
            rider.logoUri = cursor.getString(2);
            rider.online = cursor.getInt(3);
            rider.signature = cursor.getString(4);
            rider.type = cursor.getString(5);
        }
        cursor.close();
        return rider;
    }

    public void update(RiderVo rider) throws Exception {
        Object[] bindArgs = new Object[]{rider.name, rider.logoUri, Integer.valueOf(rider.online), rider.signature, rider.jId, rider.ownerJid};
        this.mySqlHelper.getWritableDatabase().execSQL("UPDATE rider SET name=?,logoUri=?,online=?,signature=? WHERE jId = ? AND ownerJid=?", bindArgs);
    }

    public void update(String jId, int online, String signature) throws Exception {
        Object[] bindArgs = new Object[]{Integer.valueOf(online), signature, jId};
        this.mySqlHelper.getWritableDatabase().execSQL("UPDATE rider SET online=?,signature=? WHERE jId = ?", bindArgs);
    }

    public void updateType(String jId, String ownerJid, String type) throws Exception {
        Object[] bindArgs = new Object[]{type, jId, ownerJid};
        this.mySqlHelper.getWritableDatabase().execSQL("UPDATE rider SET type=? WHERE jId = ? AND ownerJid=?", bindArgs);
    }

    public void updateLogoUri(String jid, String logoUri) throws Exception {
        Object[] bindArgs = new Object[]{logoUri, jid};
        this.mySqlHelper.getWritableDatabase().execSQL("UPDATE rider SET logoUri=? WHERE jId = ?", bindArgs);
    }

    public void update(String jId, String type, String nickname) throws Exception {
        Object[] bindArgs = new Object[]{type, nickname, jId};
        this.mySqlHelper.getWritableDatabase().execSQL("UPDATE rider SET type=?,name=? WHERE jId = ?", bindArgs);
    }

    public void delete(String jId, String ownerJid) throws Exception {
        Object[] bindArgs = new Object[]{jId, ownerJid};
        this.mySqlHelper.getWritableDatabase().execSQL("delete from rider where jId = ? and ownerJid=?", bindArgs);
    }

    public List<RiderVo> getAllRiders(String ownerJid) throws Exception {
        String[] args = new String[]{ownerJid};
        Cursor cursor = this.mySqlHelper.getReadableDatabase().rawQuery("SELECT jId,name,logoUri,online,signature FROM rider WHERE ownerJid=? ORDER BY online DESC", args);
        List<RiderVo> riders = new ArrayList();
        while (cursor.moveToNext()) {
            RiderVo rider = new RiderVo();
            rider.jId = cursor.getString(0);
            rider.name = cursor.getString(1);
            rider.logoUri = cursor.getString(2);
            rider.online = cursor.getInt(3);
            rider.signature = cursor.getString(4);
            riders.add(rider);
        }
        cursor.close();
        return riders;
    }

    public List<RiderVo> getRiders(String ownerJid, String type) throws Exception {
        String[] args = new String[]{ownerJid, type};
        Cursor cursor = this.mySqlHelper.getReadableDatabase().rawQuery("SELECT jId,name,logoUri,online,signature FROM rider WHERE ownerJid=? AND type =? ORDER BY online DESC", args);
        List<RiderVo> riders = new ArrayList();
        while (cursor.moveToNext()) {
            RiderVo rider = new RiderVo();
            rider.jId = cursor.getString(0);
            rider.name = cursor.getString(1);
            rider.logoUri = cursor.getString(2);
            rider.online = cursor.getInt(3);
            rider.signature = cursor.getString(4);
            riders.add(rider);
        }
        cursor.close();
        return riders;
    }
}
