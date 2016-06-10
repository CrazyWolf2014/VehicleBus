package org.jivesoftware.smackx.filetransfer;

import java.io.InputStream;
import java.io.OutputStream;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.packet.DataForm;
import org.jivesoftware.smackx.packet.StreamInitiation;

public abstract class StreamNegotiator {
    public abstract void cleanup();

    public abstract InputStream createIncomingStream(StreamInitiation streamInitiation) throws XMPPException, InterruptedException;

    public abstract OutputStream createOutgoingStream(String str, String str2, String str3) throws XMPPException;

    public abstract PacketFilter getInitiationPacketFilter(String str, String str2);

    public abstract String[] getNamespaces();

    abstract InputStream negotiateIncomingStream(Packet packet) throws XMPPException, InterruptedException;

    public StreamInitiation createInitiationAccept(StreamInitiation streamInitiation, String[] strArr) {
        StreamInitiation streamInitiation2 = new StreamInitiation();
        streamInitiation2.setTo(streamInitiation.getFrom());
        streamInitiation2.setFrom(streamInitiation.getTo());
        streamInitiation2.setType(Type.RESULT);
        streamInitiation2.setPacketID(streamInitiation.getPacketID());
        DataForm dataForm = new DataForm(Form.TYPE_SUBMIT);
        FormField formField = new FormField("stream-method");
        for (String addValue : strArr) {
            formField.addValue(addValue);
        }
        dataForm.addField(formField);
        streamInitiation2.setFeatureNegotiationForm(dataForm);
        return streamInitiation2;
    }

    public IQ createError(String str, String str2, String str3, XMPPError xMPPError) {
        IQ createIQ = FileTransferNegotiator.createIQ(str3, str2, str, Type.ERROR);
        createIQ.setError(xMPPError);
        return createIQ;
    }

    Packet initiateIncomingStream(Connection connection, StreamInitiation streamInitiation) throws XMPPException {
        Packet createInitiationAccept = createInitiationAccept(streamInitiation, getNamespaces());
        PacketCollector createPacketCollector = connection.createPacketCollector(getInitiationPacketFilter(streamInitiation.getFrom(), streamInitiation.getSessionID()));
        connection.sendPacket(createInitiationAccept);
        createInitiationAccept = createPacketCollector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        createPacketCollector.cancel();
        if (createInitiationAccept != null) {
            return createInitiationAccept;
        }
        throw new XMPPException("No response from file transfer initiator");
    }
}
