package org.jivesoftware.smackx.bytestreams.socks5;

import com.thoughtworks.xstream.XStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Collection;
import java.util.concurrent.TimeoutException;
import org.codehaus.jackson.util.BufferRecycler;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.packet.XMPPError.Condition;
import org.jivesoftware.smack.util.Cache;
import org.jivesoftware.smackx.bytestreams.BytestreamRequest;
import org.jivesoftware.smackx.bytestreams.socks5.packet.Bytestream;
import org.jivesoftware.smackx.bytestreams.socks5.packet.Bytestream.StreamHost;

public class Socks5BytestreamRequest implements BytestreamRequest {
    private static final Cache<String, Integer> ADDRESS_BLACKLIST;
    private static final long BLACKLIST_LIFETIME = 7200000;
    private static final int BLACKLIST_MAX_SIZE = 100;
    private static int CONNECTION_FAILURE_THRESHOLD;
    private Bytestream bytestreamRequest;
    private Socks5BytestreamManager manager;
    private int minimumConnectTimeout;
    private int totalConnectTimeout;

    static {
        ADDRESS_BLACKLIST = new Cache(BLACKLIST_MAX_SIZE, BLACKLIST_LIFETIME);
        CONNECTION_FAILURE_THRESHOLD = 2;
    }

    public static int getConnectFailureThreshold() {
        return CONNECTION_FAILURE_THRESHOLD;
    }

    public static void setConnectFailureThreshold(int i) {
        CONNECTION_FAILURE_THRESHOLD = i;
    }

    protected Socks5BytestreamRequest(Socks5BytestreamManager socks5BytestreamManager, Bytestream bytestream) {
        this.totalConnectTimeout = XStream.PRIORITY_VERY_HIGH;
        this.minimumConnectTimeout = BufferRecycler.DEFAULT_WRITE_CONCAT_BUFFER_LEN;
        this.manager = socks5BytestreamManager;
        this.bytestreamRequest = bytestream;
    }

    public int getTotalConnectTimeout() {
        if (this.totalConnectTimeout <= 0) {
            return XStream.PRIORITY_VERY_HIGH;
        }
        return this.totalConnectTimeout;
    }

    public void setTotalConnectTimeout(int i) {
        this.totalConnectTimeout = i;
    }

    public int getMinimumConnectTimeout() {
        if (this.minimumConnectTimeout <= 0) {
            return BufferRecycler.DEFAULT_WRITE_CONCAT_BUFFER_LEN;
        }
        return this.minimumConnectTimeout;
    }

    public void setMinimumConnectTimeout(int i) {
        this.minimumConnectTimeout = i;
    }

    public String getFrom() {
        return this.bytestreamRequest.getFrom();
    }

    public String getSessionID() {
        return this.bytestreamRequest.getSessionID();
    }

    public Socks5BytestreamSession accept() throws XMPPException, InterruptedException {
        Socket socket;
        StreamHost streamHost = null;
        Collection<StreamHost> streamHosts = this.bytestreamRequest.getStreamHosts();
        if (streamHosts.size() == 0) {
            cancelRequest();
        }
        String createDigest = Socks5Utils.createDigest(this.bytestreamRequest.getSessionID(), this.bytestreamRequest.getFrom(), this.manager.getConnection().getUser());
        int max = Math.max(getTotalConnectTimeout() / streamHosts.size(), getMinimumConnectTimeout());
        for (StreamHost streamHost2 : streamHosts) {
            String str = streamHost2.getAddress() + ":" + streamHost2.getPort();
            int connectionFailures = getConnectionFailures(str);
            if (CONNECTION_FAILURE_THRESHOLD <= 0 || connectionFailures < CONNECTION_FAILURE_THRESHOLD) {
                try {
                    streamHost = streamHost2;
                    socket = new Socks5Client(streamHost2, createDigest).getSocket(max);
                    break;
                } catch (TimeoutException e) {
                    incrementConnectionFailures(str);
                } catch (IOException e2) {
                    incrementConnectionFailures(str);
                } catch (XMPPException e3) {
                    incrementConnectionFailures(str);
                }
            }
        }
        Object obj = streamHost;
        if (streamHost == null || socket == null) {
            cancelRequest();
        }
        this.manager.getConnection().sendPacket(createUsedHostResponse(streamHost));
        return new Socks5BytestreamSession(socket, streamHost.getJID().equals(this.bytestreamRequest.getFrom()));
    }

    public void reject() {
        this.manager.replyRejectPacket(this.bytestreamRequest);
    }

    private void cancelRequest() throws XMPPException {
        String str = "Could not establish socket with any provided host";
        XMPPError xMPPError = new XMPPError(Condition.item_not_found, str);
        this.manager.getConnection().sendPacket(IQ.createErrorResponse(this.bytestreamRequest, xMPPError));
        throw new XMPPException(str, xMPPError);
    }

    private Bytestream createUsedHostResponse(StreamHost streamHost) {
        Bytestream bytestream = new Bytestream(this.bytestreamRequest.getSessionID());
        bytestream.setTo(this.bytestreamRequest.getFrom());
        bytestream.setType(Type.RESULT);
        bytestream.setPacketID(this.bytestreamRequest.getPacketID());
        bytestream.setUsedHost(streamHost.getJID());
        return bytestream;
    }

    private void incrementConnectionFailures(String str) {
        Integer num = (Integer) ADDRESS_BLACKLIST.get(str);
        ADDRESS_BLACKLIST.put(str, Integer.valueOf(num == null ? 1 : num.intValue() + 1));
    }

    private int getConnectionFailures(String str) {
        Integer num = (Integer) ADDRESS_BLACKLIST.get(str);
        return num != null ? num.intValue() : 0;
    }
}
