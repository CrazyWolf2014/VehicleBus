package com.ifoer.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.cnmobi.im.dto.MessageVo;
import com.cnmobi.im.dto.Msg;
import com.ifoer.entity.ChatInfo;
import com.ifoer.expeditionphone.MainActivity;
import com.ifoer.mine.Contact;
import com.launch.service.BundleBuilder;
import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.smackx.packet.MultipleAddresses;

public class CircleChatDBService {
    public static void AddChat(Context context, ChatInfo chatInfo) {
        SQLiteDatabase db = MainActivity.database;
        ContentValues values = new ContentValues();
        values.put(MultipleAddresses.CC, chatInfo.getCc());
        values.put("userName", chatInfo.getUserName());
        values.put("roomID", chatInfo.getRoomID());
        values.put("fromName", chatInfo.getFromName());
        values.put("fromCC", chatInfo.getFromCC());
        values.put(Msg.TIME_REDIO, chatInfo.getTime());
        values.put(BundleBuilder.AskFromMessage, chatInfo.getMessage());
        values.put("mySend", Boolean.valueOf(chatInfo.isMySend()));
        values.put(MessageVo.TYPE_IMAGE, Boolean.valueOf(chatInfo.isImage()));
        values.put("toName", chatInfo.getToName());
        values.put("toCC", chatInfo.getToCC());
        db.insert(DBHelper.CIRCLE_CHATINFO_TABLE, null, values);
        System.out.println("\u6dfb\u52a0\u804a\u5929\u8bb0\u5f55");
    }

    public static List<ChatInfo> getChatInfoByRoomId(Context context, String cc, String roomID, int page) {
        List<ChatInfo> chatList = new ArrayList();
        String sql = "select * from circle_chatinfo_table where cc=? and roomID=? order by time desc  limit " + (page * 20);
        Cursor cursor = MainActivity.database.rawQuery(sql, new String[]{cc, roomID});
        cursor.moveToLast();
        if (cursor.getCount() > 0) {
            do {
                ChatInfo chatInfo = new ChatInfo();
                chatInfo.setCc(cursor.getString(cursor.getColumnIndex(MultipleAddresses.CC)));
                chatInfo.setFromCC(cursor.getString(cursor.getColumnIndex("fromCC")));
                chatInfo.setFromName(cursor.getString(cursor.getColumnIndex("fromName")));
                chatInfo.setImage(cursor.getString(cursor.getColumnIndex(MessageVo.TYPE_IMAGE)).equals(Contact.RELATION_FRIEND));
                chatInfo.setMessage(cursor.getString(cursor.getColumnIndex(BundleBuilder.AskFromMessage)));
                chatInfo.setMySend(cursor.getString(cursor.getColumnIndex("mySend")).equals(Contact.RELATION_FRIEND));
                chatInfo.setRoomID(cursor.getString(cursor.getColumnIndex("roomID")));
                chatInfo.setTime(cursor.getString(cursor.getColumnIndex(Msg.TIME_REDIO)));
                chatInfo.setToCC(cursor.getString(cursor.getColumnIndex("toCC")));
                chatInfo.setToName(cursor.getString(cursor.getColumnIndex("toName")));
                chatInfo.setUserName(cursor.getString(cursor.getColumnIndex("userName")));
                chatList.add(chatInfo);
            } while (cursor.moveToPrevious());
        }
        cursor.close();
        return chatList;
    }
}
