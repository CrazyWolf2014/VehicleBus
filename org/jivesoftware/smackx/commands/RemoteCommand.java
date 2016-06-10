package org.jivesoftware.smackx.commands;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.commands.AdHocCommand.Action;
import org.jivesoftware.smackx.packet.AdHocCommandData;

public class RemoteCommand extends AdHocCommand {
    private Connection connection;
    private String jid;
    private long packetReplyTimeout;
    private String sessionID;

    protected RemoteCommand(Connection connection, String str, String str2) {
        this.connection = connection;
        this.jid = str2;
        setNode(str);
        this.packetReplyTimeout = (long) SmackConfiguration.getPacketReplyTimeout();
    }

    public void cancel() throws XMPPException {
        executeAction(Action.cancel, this.packetReplyTimeout);
    }

    public void complete(Form form) throws XMPPException {
        executeAction(Action.complete, form, this.packetReplyTimeout);
    }

    public void execute() throws XMPPException {
        executeAction(Action.execute, this.packetReplyTimeout);
    }

    public void execute(Form form) throws XMPPException {
        executeAction(Action.execute, form, this.packetReplyTimeout);
    }

    public void next(Form form) throws XMPPException {
        executeAction(Action.next, form, this.packetReplyTimeout);
    }

    public void prev() throws XMPPException {
        executeAction(Action.prev, this.packetReplyTimeout);
    }

    private void executeAction(Action action, long j) throws XMPPException {
        executeAction(action, null, j);
    }

    private void executeAction(Action action, Form form, long j) throws XMPPException {
        Packet adHocCommandData = new AdHocCommandData();
        adHocCommandData.setType(Type.SET);
        adHocCommandData.setTo(getOwnerJID());
        adHocCommandData.setNode(getNode());
        adHocCommandData.setSessionID(this.sessionID);
        adHocCommandData.setAction(action);
        if (form != null) {
            adHocCommandData.setForm(form.getDataFormToSend());
        }
        PacketCollector createPacketCollector = this.connection.createPacketCollector(new PacketIDFilter(adHocCommandData.getPacketID()));
        this.connection.sendPacket(adHocCommandData);
        adHocCommandData = createPacketCollector.nextResult(j);
        createPacketCollector.cancel();
        if (adHocCommandData == null) {
            throw new XMPPException("No response from server on status set.");
        } else if (adHocCommandData.getError() != null) {
            throw new XMPPException(adHocCommandData.getError());
        } else {
            AdHocCommandData adHocCommandData2 = (AdHocCommandData) adHocCommandData;
            this.sessionID = adHocCommandData2.getSessionID();
            super.setData(adHocCommandData2);
        }
    }

    public String getOwnerJID() {
        return this.jid;
    }

    public long getPacketReplyTimeout() {
        return this.packetReplyTimeout;
    }

    public void setPacketReplyTimeout(long j) {
        this.packetReplyTimeout = j;
    }
}
