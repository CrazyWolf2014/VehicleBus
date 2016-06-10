package org.jivesoftware.smackx.filetransfer;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.FromMatchesFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.bytestreams.socks5.Socks5BytestreamManager;
import org.jivesoftware.smackx.bytestreams.socks5.Socks5BytestreamRequest;
import org.jivesoftware.smackx.bytestreams.socks5.packet.Bytestream;
import org.jivesoftware.smackx.packet.StreamInitiation;

public class Socks5TransferNegotiator extends StreamNegotiator {
    private Connection connection;
    private Socks5BytestreamManager manager;

    private static class ByteStreamRequest extends Socks5BytestreamRequest {
        private ByteStreamRequest(Socks5BytestreamManager socks5BytestreamManager, Bytestream bytestream) {
            super(socks5BytestreamManager, bytestream);
        }
    }

    private static class BytestreamSIDFilter extends PacketTypeFilter {
        private String sessionID;

        public BytestreamSIDFilter(String str) {
            super(Bytestream.class);
            if (str == null) {
                throw new IllegalArgumentException("StreamID cannot be null");
            }
            this.sessionID = str;
        }

        public boolean accept(Packet packet) {
            if (!super.accept(packet)) {
                return false;
            }
            Bytestream bytestream = (Bytestream) packet;
            if (this.sessionID.equals(bytestream.getSessionID()) && Type.SET.equals(bytestream.getType())) {
                return true;
            }
            return false;
        }
    }

    Socks5TransferNegotiator(Connection connection) {
        this.connection = connection;
        this.manager = Socks5BytestreamManager.getBytestreamManager(this.connection);
    }

    public OutputStream createOutgoingStream(String str, String str2, String str3) throws XMPPException {
        try {
            return this.manager.establishSession(str3, str).getOutputStream();
        } catch (Throwable e) {
            throw new XMPPException("error establishing SOCKS5 Bytestream", e);
        } catch (Throwable e2) {
            throw new XMPPException("error establishing SOCKS5 Bytestream", e2);
        }
    }

    public InputStream createIncomingStream(StreamInitiation streamInitiation) throws XMPPException, InterruptedException {
        this.manager.ignoreBytestreamRequestOnce(streamInitiation.getSessionID());
        return negotiateIncomingStream(initiateIncomingStream(this.connection, streamInitiation));
    }

    public PacketFilter getInitiationPacketFilter(String str, String str2) {
        this.manager.ignoreBytestreamRequestOnce(str2);
        return new AndFilter(new FromMatchesFilter(str), new BytestreamSIDFilter(str2));
    }

    public String[] getNamespaces() {
        return new String[]{Socks5BytestreamManager.NAMESPACE};
    }

    InputStream negotiateIncomingStream(Packet packet) throws XMPPException, InterruptedException {
        try {
            InputStream pushbackInputStream = new PushbackInputStream(new ByteStreamRequest((Bytestream) packet, null).accept().getInputStream());
            pushbackInputStream.unread(pushbackInputStream.read());
            return pushbackInputStream;
        } catch (Throwable e) {
            throw new XMPPException("Error establishing input stream", e);
        }
    }

    public void cleanup() {
    }
}
