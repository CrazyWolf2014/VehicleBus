package com.cnmobi.im.dao;

import android.database.Cursor;
import com.cnmobi.im.ImMainActivity;
import com.cnmobi.im.dto.MessageVo;
import java.util.ArrayList;
import java.util.List;

public class MessageDao {
    private static MessageDao instance = null;
    private static final int pageSize = 50;
    private MySQLiteOpenHelper mySqlHelper;

    private MessageDao() {
        this.mySqlHelper = MySQLiteOpenHelper.getInstance(ImMainActivity.context);
    }

    public static MessageDao getInstance() {
        if (instance == null) {
            instance = new MessageDao();
        }
        return instance;
    }

    public void insert(MessageVo message) {
        message.ownerJid = ImMainActivity.mOwerJid;
        Object[] bindArgs = new Object[]{message.time, message.jId, message.content, message.direction, message.type, Integer.valueOf(message.duration), message.filePath, message.ownerJid};
        this.mySqlHelper.getWritableDatabase().execSQL("INSERT INTO message(time,jId,content,direction,type,duration,filePath,ownerJid)VALUES(?,?,?,?,?,?,?,?)", bindArgs);
    }

    public List<MessageVo> getRecentMessages(String ownerJid) {
        String[] args = new String[]{ownerJid};
        Cursor cursor = this.mySqlHelper.getReadableDatabase().rawQuery("SELECT jId,time,content,direction,type,duration,filePath FROM message WHERE ownerJid=? GROUP BY jId ORDER BY time DESC", args);
        List<MessageVo> messages = new ArrayList();
        while (cursor.moveToNext()) {
            MessageVo message = new MessageVo(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
            message.type = cursor.getString(4);
            message.duration = cursor.getInt(5);
            message.filePath = cursor.getString(6);
            messages.add(message);
        }
        if (cursor != null) {
            cursor.close();
        }
        return messages;
    }

    public List<MessageVo> getMessagesByJid(String jId, String ownerJid, int pageNo) throws Exception {
        int start = (pageNo - 1) * pageSize;
        String[] selectionArgs = new String[]{jId, ownerJid, String.valueOf(pageSize), String.valueOf(start)};
        Cursor cursor = this.mySqlHelper.getReadableDatabase().rawQuery("SELECT time,content,direction,type,duration,filePath FROM message WHERE jId = ? AND ownerJid=? ORDER BY id DESC LIMIT ? OFFSET ?", selectionArgs);
        List<MessageVo> messages = new ArrayList();
        while (cursor.moveToNext()) {
            MessageVo message = new MessageVo(jId, cursor.getString(0), cursor.getString(1), cursor.getString(2));
            message.type = cursor.getString(3);
            message.duration = cursor.getInt(4);
            message.filePath = cursor.getString(5);
            messages.add(message);
        }
        if (cursor != null) {
            cursor.close();
        }
        return messages;
    }
}
