package com.cnmobi.im.dao;

import android.database.Cursor;
import com.cnmobi.im.ImMainActivity;
import com.cnmobi.im.dto.ChatRoom;
import java.util.ArrayList;
import java.util.List;

public class ChatroomDao {
    private static ChatroomDao instance;
    private MySQLiteOpenHelper mySqlHelper;

    static {
        instance = new ChatroomDao();
    }

    private ChatroomDao() {
        this.mySqlHelper = MySQLiteOpenHelper.getInstance(ImMainActivity.context);
    }

    public static ChatroomDao getInstance() {
        return instance;
    }

    public void insert(ChatRoom chatRoom) throws Exception {
        chatRoom.setOwnerJid(ImMainActivity.mOwerJid);
        Object[] bindArgs = new Object[]{chatRoom.getJid(), chatRoom.getName(), Integer.valueOf(chatRoom.getOccupants()), chatRoom.getDescription(), chatRoom.getSubject(), chatRoom.getCreaterJid(), chatRoom.getOwnerJid()};
        this.mySqlHelper.getWritableDatabase().execSQL("INSERT INTO chatroom(jId,name,occupants,description,subject,createrJid,ownerJid)VALUES(?,?,?,?,?,?,?)", bindArgs);
    }

    public void delete(String jid) throws Exception {
        Object[] bindArgs = new Object[]{jid, ImMainActivity.mOwerJid};
        this.mySqlHelper.getWritableDatabase().execSQL("delete from chatroom where jId = ? and ownerJid=?", bindArgs);
    }

    public List<ChatRoom> getChatRooms() throws Exception {
        String[] args = new String[]{ImMainActivity.mOwerJid};
        Cursor cursor = this.mySqlHelper.getReadableDatabase().rawQuery("SELECT jId,name,occupants,description,subject,createrJid FROM chatroom WHERE ownerJid=?", args);
        List<ChatRoom> chatrooms = new ArrayList();
        while (cursor.moveToNext()) {
            ChatRoom chatroom = new ChatRoom();
            chatroom.setOwnerJid(ImMainActivity.mOwerJid);
            chatroom.setJid(cursor.getString(0));
            chatroom.setName(cursor.getString(1));
            chatroom.setOccupants(cursor.getInt(2));
            chatroom.setDescription(cursor.getString(3));
            chatroom.setSubject(cursor.getString(4));
            chatroom.setCreaterJid(cursor.getString(5));
            chatrooms.add(chatroom);
        }
        cursor.close();
        return chatrooms;
    }
}
