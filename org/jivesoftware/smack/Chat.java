package org.jivesoftware.smack;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;

public class Chat {
    private ChatManager chatManager;
    private final Set<MessageListener> listeners;
    private String participant;
    private String threadID;

    Chat(ChatManager chatManager, String str, String str2) {
        this.listeners = new CopyOnWriteArraySet();
        this.chatManager = chatManager;
        this.participant = str;
        this.threadID = str2;
    }

    public String getThreadID() {
        return this.threadID;
    }

    public String getParticipant() {
        return this.participant;
    }

    public void sendMessage(String str) throws XMPPException {
        Message message = new Message(this.participant, Type.chat);
        message.setThread(this.threadID);
        message.setBody(str);
        this.chatManager.sendMessage(this, message);
    }

    public void sendMessage(Message message) throws XMPPException {
        message.setTo(this.participant);
        message.setType(Type.chat);
        message.setThread(this.threadID);
        this.chatManager.sendMessage(this, message);
    }

    public void addMessageListener(MessageListener messageListener) {
        if (messageListener != null) {
            this.listeners.add(messageListener);
        }
    }

    public void removeMessageListener(MessageListener messageListener) {
        this.listeners.remove(messageListener);
    }

    public Collection<MessageListener> getListeners() {
        return Collections.unmodifiableCollection(this.listeners);
    }

    public PacketCollector createCollector() {
        return this.chatManager.createPacketCollector(this);
    }

    void deliver(Message message) {
        message.setThread(this.threadID);
        for (MessageListener processMessage : this.listeners) {
            processMessage.processMessage(this, message);
        }
    }

    public boolean equals(Object obj) {
        return (obj instanceof Chat) && this.threadID.equals(((Chat) obj).getThreadID()) && this.participant.equals(((Chat) obj).getParticipant());
    }
}
