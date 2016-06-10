package org.jivesoftware.smackx.workgroup.agent;

import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.workgroup.packet.Transcript;
import org.jivesoftware.smackx.workgroup.packet.Transcripts;

public class TranscriptManager {
    private Connection connection;

    public TranscriptManager(Connection connection) {
        this.connection = connection;
    }

    public Transcript getTranscript(String str, String str2) throws XMPPException {
        Packet transcript = new Transcript(str2);
        transcript.setTo(str);
        PacketCollector createPacketCollector = this.connection.createPacketCollector(new PacketIDFilter(transcript.getPacketID()));
        this.connection.sendPacket(transcript);
        Transcript transcript2 = (Transcript) createPacketCollector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        createPacketCollector.cancel();
        if (transcript2 == null) {
            throw new XMPPException("No response from server on status set.");
        } else if (transcript2.getError() == null) {
            return transcript2;
        } else {
            throw new XMPPException(transcript2.getError());
        }
    }

    public Transcripts getTranscripts(String str, String str2) throws XMPPException {
        Packet transcripts = new Transcripts(str2);
        transcripts.setTo(str);
        PacketCollector createPacketCollector = this.connection.createPacketCollector(new PacketIDFilter(transcripts.getPacketID()));
        this.connection.sendPacket(transcripts);
        Transcripts transcripts2 = (Transcripts) createPacketCollector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        createPacketCollector.cancel();
        if (transcripts2 == null) {
            throw new XMPPException("No response from server on status set.");
        } else if (transcripts2.getError() == null) {
            return transcripts2;
        } else {
            throw new XMPPException(transcripts2.getError());
        }
    }
}
