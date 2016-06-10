package org.jivesoftware.smackx.filetransfer;

import java.util.ArrayList;
import java.util.List;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.IQTypeFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.packet.XMPPError.Condition;
import org.jivesoftware.smackx.packet.StreamInitiation;

public class FileTransferManager {
    private Connection connection;
    private final FileTransferNegotiator fileTransferNegotiator;
    private List<FileTransferListener> listeners;

    /* renamed from: org.jivesoftware.smackx.filetransfer.FileTransferManager.1 */
    class C11971 implements PacketListener {
        C11971() {
        }

        public void processPacket(Packet packet) {
            FileTransferManager.this.fireNewRequest((StreamInitiation) packet);
        }
    }

    public FileTransferManager(Connection connection) {
        this.connection = connection;
        this.fileTransferNegotiator = FileTransferNegotiator.getInstanceFor(connection);
    }

    public void addFileTransferListener(FileTransferListener fileTransferListener) {
        if (this.listeners == null) {
            initListeners();
        }
        synchronized (this.listeners) {
            this.listeners.add(fileTransferListener);
        }
    }

    private void initListeners() {
        this.listeners = new ArrayList();
        this.connection.addPacketListener(new C11971(), new AndFilter(new PacketTypeFilter(StreamInitiation.class), new IQTypeFilter(Type.SET)));
    }

    protected void fireNewRequest(StreamInitiation streamInitiation) {
        synchronized (this.listeners) {
            FileTransferListener[] fileTransferListenerArr = new FileTransferListener[this.listeners.size()];
            this.listeners.toArray(fileTransferListenerArr);
        }
        FileTransferRequest fileTransferRequest = new FileTransferRequest(this, streamInitiation);
        for (FileTransferListener fileTransferRequest2 : fileTransferListenerArr) {
            fileTransferRequest2.fileTransferRequest(fileTransferRequest);
        }
    }

    public void removeFileTransferListener(FileTransferListener fileTransferListener) {
        if (this.listeners != null) {
            synchronized (this.listeners) {
                this.listeners.remove(fileTransferListener);
            }
        }
    }

    public OutgoingFileTransfer createOutgoingFileTransfer(String str) {
        return new OutgoingFileTransfer(this.connection.getUser(), str, this.fileTransferNegotiator.getNextStreamID(), this.fileTransferNegotiator);
    }

    protected IncomingFileTransfer createIncomingFileTransfer(FileTransferRequest fileTransferRequest) {
        if (fileTransferRequest == null) {
            throw new NullPointerException("RecieveRequest cannot be null");
        }
        IncomingFileTransfer incomingFileTransfer = new IncomingFileTransfer(fileTransferRequest, this.fileTransferNegotiator);
        incomingFileTransfer.setFileInfo(fileTransferRequest.getFileName(), fileTransferRequest.getFileSize());
        return incomingFileTransfer;
    }

    protected void rejectIncomingFileTransfer(FileTransferRequest fileTransferRequest) {
        StreamInitiation streamInitiation = fileTransferRequest.getStreamInitiation();
        Packet createIQ = FileTransferNegotiator.createIQ(streamInitiation.getPacketID(), streamInitiation.getFrom(), streamInitiation.getTo(), Type.ERROR);
        createIQ.setError(new XMPPError(Condition.no_acceptable));
        this.connection.sendPacket(createIQ);
    }
}
