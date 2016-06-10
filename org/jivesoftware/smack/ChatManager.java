package org.jivesoftware.smack;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.FromContainsFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.ThreadFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smack.util.collections.ReferenceMap;

public class ChatManager {
    private static long id;
    private static String prefix;
    private Map<String, Chat> baseJidChats;
    private Set<ChatManagerListener> chatManagerListeners;
    private Connection connection;
    private Map<PacketInterceptor, PacketFilter> interceptors;
    private Map<String, Chat> jidChats;
    private Map<String, Chat> threadChats;

    /* renamed from: org.jivesoftware.smack.ChatManager.1 */
    class C11521 implements PacketFilter {
        C11521() {
        }

        public boolean accept(Packet packet) {
            if (!(packet instanceof Message)) {
                return false;
            }
            Type type = ((Message) packet).getType();
            if (type == Type.groupchat || type == Type.headline) {
                return false;
            }
            return true;
        }
    }

    /* renamed from: org.jivesoftware.smack.ChatManager.2 */
    class C11532 implements PacketListener {
        C11532() {
        }

        public void processPacket(Packet packet) {
            Chat access$000;
            Message message = (Message) packet;
            if (message.getThread() == null) {
                access$000 = ChatManager.this.getUserChat(message.getFrom());
            } else {
                access$000 = ChatManager.this.getThreadChat(message.getThread());
                if (access$000 == null) {
                    access$000 = ChatManager.this.getUserChat(message.getFrom());
                }
            }
            if (access$000 == null) {
                access$000 = ChatManager.this.createChat(message);
            }
            ChatManager.this.deliverMessage(access$000, message);
        }
    }

    private static synchronized String nextID() {
        String stringBuilder;
        synchronized (ChatManager.class) {
            StringBuilder append = new StringBuilder().append(prefix);
            long j = id;
            id = 1 + j;
            stringBuilder = append.append(Long.toString(j)).toString();
        }
        return stringBuilder;
    }

    static {
        prefix = StringUtils.randomString(5);
        id = 0;
    }

    ChatManager(Connection connection) {
        this.threadChats = Collections.synchronizedMap(new ReferenceMap(0, 2));
        this.jidChats = Collections.synchronizedMap(new ReferenceMap(0, 2));
        this.baseJidChats = Collections.synchronizedMap(new ReferenceMap(0, 2));
        this.chatManagerListeners = new CopyOnWriteArraySet();
        this.interceptors = new WeakHashMap();
        this.connection = connection;
        connection.addPacketListener(new C11532(), new C11521());
    }

    public Chat createChat(String str, MessageListener messageListener) {
        String nextID;
        do {
            nextID = nextID();
        } while (this.threadChats.get(nextID) != null);
        return createChat(str, nextID, messageListener);
    }

    public Chat createChat(String str, String str2, MessageListener messageListener) {
        if (str2 == null) {
            str2 = nextID();
        }
        if (((Chat) this.threadChats.get(str2)) != null) {
            throw new IllegalArgumentException("ThreadID is already used");
        }
        Chat createChat = createChat(str, str2, true);
        createChat.addMessageListener(messageListener);
        return createChat;
    }

    private Chat createChat(String str, String str2, boolean z) {
        Chat chat = new Chat(this, str, str2);
        this.threadChats.put(str2, chat);
        this.jidChats.put(str, chat);
        this.baseJidChats.put(StringUtils.parseBareAddress(str), chat);
        for (ChatManagerListener chatCreated : this.chatManagerListeners) {
            chatCreated.chatCreated(chat, z);
        }
        return chat;
    }

    private Chat createChat(Message message) {
        String thread = message.getThread();
        if (thread == null) {
            thread = nextID();
        }
        return createChat(message.getFrom(), thread, false);
    }

    private Chat getUserChat(String str) {
        Chat chat = (Chat) this.jidChats.get(str);
        if (chat == null) {
            return (Chat) this.baseJidChats.get(StringUtils.parseBareAddress(str));
        }
        return chat;
    }

    public Chat getThreadChat(String str) {
        return (Chat) this.threadChats.get(str);
    }

    public void addChatListener(ChatManagerListener chatManagerListener) {
        this.chatManagerListeners.add(chatManagerListener);
    }

    public void removeChatListener(ChatManagerListener chatManagerListener) {
        this.chatManagerListeners.remove(chatManagerListener);
    }

    public Collection<ChatManagerListener> getChatListeners() {
        return Collections.unmodifiableCollection(this.chatManagerListeners);
    }

    private void deliverMessage(Chat chat, Message message) {
        chat.deliver(message);
    }

    void sendMessage(Chat chat, Message message) {
        for (Entry entry : this.interceptors.entrySet()) {
            PacketFilter packetFilter = (PacketFilter) entry.getValue();
            if (packetFilter != null && packetFilter.accept(message)) {
                ((PacketInterceptor) entry.getKey()).interceptPacket(message);
            }
        }
        if (message.getFrom() == null) {
            message.setFrom(this.connection.getUser());
        }
        this.connection.sendPacket(message);
    }

    PacketCollector createPacketCollector(Chat chat) {
        return this.connection.createPacketCollector(new AndFilter(new ThreadFilter(chat.getThreadID()), new FromContainsFilter(chat.getParticipant())));
    }

    public void addOutgoingMessageInterceptor(PacketInterceptor packetInterceptor) {
        addOutgoingMessageInterceptor(packetInterceptor, null);
    }

    public void addOutgoingMessageInterceptor(PacketInterceptor packetInterceptor, PacketFilter packetFilter) {
        if (packetInterceptor != null) {
            this.interceptors.put(packetInterceptor, packetFilter);
        }
    }
}
