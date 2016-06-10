package com.cnmobi.im.bo;

import com.cnmobi.im.dao.ChatroomDao;
import com.cnmobi.im.dto.ChatRoom;
import com.cnmobi.im.util.XmppConnection;
import com.ifoer.util.XmppTool;
import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.RoomInfo;

public class ChatroomManager {
    private static final String TAG = "ChatroomManager";
    private static ChatroomManager instance;
    private List<ChatRoomListener> chatRoomListeners;

    public interface ChatRoomListener {
        void chatRoomChange();
    }

    static {
        instance = new ChatroomManager();
    }

    private ChatroomManager() {
        this.chatRoomListeners = new ArrayList();
    }

    public static ChatroomManager getInstance() {
        return instance;
    }

    public void addChatRoomListener(ChatRoomListener listener) {
        this.chatRoomListeners.add(listener);
    }

    public void removeChatRoomListener(ChatRoomListener listener) {
        this.chatRoomListeners.remove(listener);
    }

    public void refresh() throws Exception {
        List<ChatRoom> chatroomsFromServer = getChatroomsFromServer();
        List<ChatRoom> chatroomsFromDb = ChatroomDao.getInstance().getChatRooms();
        if (chatroomsFromDb != null && chatroomsFromDb.size() > 0) {
            for (ChatRoom chatroom : chatroomsFromDb) {
                if (!isChatRoomInList(chatroomsFromServer, chatroom.getJid())) {
                    ChatroomDao.getInstance().delete(chatroom.getJid());
                }
            }
        }
        if (chatroomsFromServer != null && chatroomsFromServer.size() > 0) {
            for (ChatRoom chatroom2 : chatroomsFromServer) {
                if (!isChatRoomInList(chatroomsFromDb, chatroom2.getJid())) {
                    ChatroomDao.getInstance().insert(chatroom2);
                }
            }
        }
        if (this.chatRoomListeners != null && this.chatRoomListeners.size() > 0) {
            for (ChatRoomListener listener : this.chatRoomListeners) {
                listener.chatRoomChange();
            }
        }
    }

    public boolean isChatRoomInList(List<ChatRoom> chatrooms, String chatroomJid) {
        if (chatrooms == null || chatrooms.size() == 0) {
            return false;
        }
        for (ChatRoom chatroom : chatrooms) {
            if (chatroomJid.equals(chatroom.getJid())) {
                return true;
            }
        }
        return false;
    }

    public List<ChatRoom> getChatroomsFromServer() throws Exception {
        List<ChatRoom> chatrooms = new ArrayList();
        XMPPConnection connection = XmppConnection.getConnection();
        ServiceDiscoveryManager serviceDiscoveryManager = new ServiceDiscoveryManager(connection);
        if (!MultiUserChat.getHostedRooms(connection, connection.getServiceName()).isEmpty()) {
            for (HostedRoom hostedRoom : MultiUserChat.getHostedRooms(connection, new StringBuilder(XmppTool.GetRoomesHeadFlag).append(XmppConnection.getConnection().getServiceName()).toString())) {
                RoomInfo roomInfo = MultiUserChat.getRoomInfo(connection, hostedRoom.getJid());
                if (hostedRoom.getJid().indexOf(XmppConnection.JID_SEPARATOR) > 0) {
                    ChatRoom chatRoom = new ChatRoom();
                    chatRoom.setName(hostedRoom.getName());
                    chatRoom.setJid(hostedRoom.getJid());
                    chatRoom.setOccupants(roomInfo.getOccupantsCount());
                    chatRoom.setDescription(roomInfo.getDescription());
                    chatRoom.setSubject(roomInfo.getSubject());
                    chatrooms.add(chatRoom);
                }
            }
        }
        return chatrooms;
    }
}
