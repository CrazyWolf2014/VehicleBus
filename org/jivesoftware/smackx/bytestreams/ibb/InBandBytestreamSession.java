package org.jivesoftware.smackx.bytestreams.ibb;

import com.google.protobuf.DescriptorProtos.MessageOptions;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.packet.XMPPError.Condition;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smack.util.SyncPacketSend;
import org.jivesoftware.smackx.bytestreams.BytestreamSession;
import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager.StanzaType;
import org.jivesoftware.smackx.bytestreams.ibb.packet.Close;
import org.jivesoftware.smackx.bytestreams.ibb.packet.Data;
import org.jivesoftware.smackx.bytestreams.ibb.packet.DataPacketExtension;
import org.jivesoftware.smackx.bytestreams.ibb.packet.Open;
import org.xbill.DNS.KEYRecord;

public class InBandBytestreamSession implements BytestreamSession {
    private final Open byteStreamRequest;
    private boolean closeBothStreamsEnabled;
    private final Connection connection;
    private IBBInputStream inputStream;
    private boolean isClosed;
    private IBBOutputStream outputStream;
    private String remoteJID;

    /* renamed from: org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamSession.1 */
    static /* synthetic */ class C09701 {
        static final /* synthetic */ int[] f1724xda8e09c8;

        static {
            f1724xda8e09c8 = new int[StanzaType.values().length];
            try {
                f1724xda8e09c8[StanzaType.IQ.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f1724xda8e09c8[StanzaType.MESSAGE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    private abstract class IBBInputStream extends InputStream {
        private byte[] buffer;
        private int bufferPointer;
        private boolean closeInvoked;
        private final PacketListener dataPacketListener;
        protected final BlockingQueue<DataPacketExtension> dataQueue;
        private boolean isClosed;
        private int readTimeout;
        private long seq;

        protected abstract PacketFilter getDataPacketFilter();

        protected abstract PacketListener getDataPacketListener();

        public IBBInputStream() {
            this.dataQueue = new LinkedBlockingQueue();
            this.bufferPointer = -1;
            this.seq = -1;
            this.isClosed = false;
            this.closeInvoked = false;
            this.readTimeout = 0;
            this.dataPacketListener = getDataPacketListener();
            InBandBytestreamSession.this.connection.addPacketListener(this.dataPacketListener, getDataPacketFilter());
        }

        public synchronized int read() throws IOException {
            int i = -1;
            synchronized (this) {
                checkClosed();
                if ((this.bufferPointer != -1 && this.bufferPointer < this.buffer.length) || loadBuffer()) {
                    byte[] bArr = this.buffer;
                    int i2 = this.bufferPointer;
                    this.bufferPointer = i2 + 1;
                    i = bArr[i2] & KEYRecord.PROTOCOL_ANY;
                }
            }
            return i;
        }

        public synchronized int read(byte[] bArr, int i, int i2) throws IOException {
            int i3 = -1;
            synchronized (this) {
                if (bArr == null) {
                    throw new NullPointerException();
                }
                if (i >= 0) {
                    if (i <= bArr.length && i2 >= 0 && i + i2 <= bArr.length && i + i2 >= 0) {
                        if (i2 == 0) {
                            i3 = 0;
                        } else {
                            checkClosed();
                            if ((this.bufferPointer != -1 && this.bufferPointer < this.buffer.length) || loadBuffer()) {
                                i3 = this.buffer.length - this.bufferPointer;
                                if (i2 <= i3) {
                                    i3 = i2;
                                }
                                System.arraycopy(this.buffer, this.bufferPointer, bArr, i, i3);
                                this.bufferPointer += i3;
                            }
                        }
                    }
                }
                throw new IndexOutOfBoundsException();
            }
            return i3;
        }

        public synchronized int read(byte[] bArr) throws IOException {
            return read(bArr, 0, bArr.length);
        }

        private synchronized boolean loadBuffer() throws IOException {
            boolean z;
            DataPacketExtension dataPacketExtension = null;
            try {
                if (this.readTimeout == 0) {
                    while (dataPacketExtension == null) {
                        if (this.isClosed && this.dataQueue.isEmpty()) {
                            z = false;
                            break;
                        }
                        dataPacketExtension = (DataPacketExtension) this.dataQueue.poll(1000, TimeUnit.MILLISECONDS);
                    }
                } else {
                    dataPacketExtension = (DataPacketExtension) this.dataQueue.poll((long) this.readTimeout, TimeUnit.MILLISECONDS);
                    if (dataPacketExtension == null) {
                        throw new SocketTimeoutException();
                    }
                }
                if (this.seq == 65535) {
                    this.seq = -1;
                }
                long seq = dataPacketExtension.getSeq();
                if (seq - 1 != this.seq) {
                    InBandBytestreamSession.this.close();
                    throw new IOException("Packets out of sequence");
                }
                this.seq = seq;
                this.buffer = dataPacketExtension.getDecodedData();
                this.bufferPointer = 0;
                z = true;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                z = false;
            }
            return z;
        }

        private void checkClosed() throws IOException {
            if ((this.isClosed && this.dataQueue.isEmpty()) || this.closeInvoked) {
                this.dataQueue.clear();
                throw new IOException("Stream is closed");
            }
        }

        public boolean markSupported() {
            return false;
        }

        public void close() throws IOException {
            if (!this.isClosed) {
                this.closeInvoked = true;
                InBandBytestreamSession.this.closeByLocal(true);
            }
        }

        private void closeInternal() {
            if (!this.isClosed) {
                this.isClosed = true;
            }
        }

        private void cleanup() {
            InBandBytestreamSession.this.connection.removePacketListener(this.dataPacketListener);
        }
    }

    private abstract class IBBOutputStream extends OutputStream {
        protected final byte[] buffer;
        protected int bufferPointer;
        protected boolean isClosed;
        protected long seq;

        protected abstract void writeToXML(DataPacketExtension dataPacketExtension) throws IOException;

        public IBBOutputStream() {
            this.bufferPointer = 0;
            this.seq = 0;
            this.isClosed = false;
            this.buffer = new byte[((InBandBytestreamSession.this.byteStreamRequest.getBlockSize() / 4) * 3)];
        }

        public synchronized void write(int i) throws IOException {
            if (this.isClosed) {
                throw new IOException("Stream is closed");
            }
            if (this.bufferPointer >= this.buffer.length) {
                flushBuffer();
            }
            byte[] bArr = this.buffer;
            int i2 = this.bufferPointer;
            this.bufferPointer = i2 + 1;
            bArr[i2] = (byte) i;
        }

        public synchronized void write(byte[] bArr, int i, int i2) throws IOException {
            if (bArr == null) {
                throw new NullPointerException();
            }
            if (i >= 0) {
                if (i <= bArr.length && i2 >= 0 && i + i2 <= bArr.length && i + i2 >= 0) {
                    if (i2 != 0) {
                        if (this.isClosed) {
                            throw new IOException("Stream is closed");
                        } else if (i2 >= this.buffer.length) {
                            writeOut(bArr, i, this.buffer.length);
                            write(bArr, this.buffer.length + i, i2 - this.buffer.length);
                        } else {
                            writeOut(bArr, i, i2);
                        }
                    }
                }
            }
            throw new IndexOutOfBoundsException();
        }

        public synchronized void write(byte[] bArr) throws IOException {
            write(bArr, 0, bArr.length);
        }

        private synchronized void writeOut(byte[] bArr, int i, int i2) throws IOException {
            if (this.isClosed) {
                throw new IOException("Stream is closed");
            }
            int i3 = 0;
            if (i2 > this.buffer.length - this.bufferPointer) {
                i3 = this.buffer.length - this.bufferPointer;
                System.arraycopy(bArr, i, this.buffer, this.bufferPointer, i3);
                this.bufferPointer += i3;
                flushBuffer();
            }
            System.arraycopy(bArr, i + i3, this.buffer, this.bufferPointer, i2 - i3);
            this.bufferPointer = (i2 - i3) + this.bufferPointer;
        }

        public synchronized void flush() throws IOException {
            if (this.isClosed) {
                throw new IOException("Stream is closed");
            }
            flushBuffer();
        }

        private synchronized void flushBuffer() throws IOException {
            if (this.bufferPointer != 0) {
                long j;
                writeToXML(new DataPacketExtension(InBandBytestreamSession.this.byteStreamRequest.getSessionID(), this.seq, StringUtils.encodeBase64(this.buffer, 0, this.bufferPointer, false)));
                this.bufferPointer = 0;
                if (this.seq + 1 == 65535) {
                    j = 0;
                } else {
                    j = this.seq + 1;
                }
                this.seq = j;
            }
        }

        public void close() throws IOException {
            if (!this.isClosed) {
                InBandBytestreamSession.this.closeByLocal(false);
            }
        }

        protected void closeInternal(boolean z) {
            if (!this.isClosed) {
                this.isClosed = true;
                if (z) {
                    try {
                        flushBuffer();
                    } catch (IOException e) {
                    }
                }
            }
        }
    }

    private class IBBDataPacketFilter implements PacketFilter {
        private IBBDataPacketFilter() {
        }

        public boolean accept(Packet packet) {
            if (!packet.getFrom().equalsIgnoreCase(InBandBytestreamSession.this.remoteJID)) {
                return false;
            }
            PacketExtension extension = packet.getExtension(DataPacketExtension.ELEMENT_NAME, InBandBytestreamManager.NAMESPACE);
            if (extension == null || !(extension instanceof DataPacketExtension)) {
                return false;
            }
            if (((DataPacketExtension) extension).getSessionID().equals(InBandBytestreamSession.this.byteStreamRequest.getSessionID())) {
                return true;
            }
            return false;
        }
    }

    private class IQIBBInputStream extends IBBInputStream {

        /* renamed from: org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamSession.IQIBBInputStream.1 */
        class C11791 implements PacketListener {
            private long lastSequence;

            C11791() {
                this.lastSequence = -1;
            }

            public void processPacket(Packet packet) {
                DataPacketExtension dataPacketExtension = (DataPacketExtension) packet.getExtension(DataPacketExtension.ELEMENT_NAME, InBandBytestreamManager.NAMESPACE);
                if (dataPacketExtension.getSeq() <= this.lastSequence) {
                    InBandBytestreamSession.this.connection.sendPacket(IQ.createErrorResponse((IQ) packet, new XMPPError(Condition.unexpected_request)));
                } else if (dataPacketExtension.getDecodedData() == null) {
                    InBandBytestreamSession.this.connection.sendPacket(IQ.createErrorResponse((IQ) packet, new XMPPError(Condition.bad_request)));
                } else {
                    IQIBBInputStream.this.dataQueue.offer(dataPacketExtension);
                    InBandBytestreamSession.this.connection.sendPacket(IQ.createResultIQ((IQ) packet));
                    this.lastSequence = dataPacketExtension.getSeq();
                    if (this.lastSequence == 65535) {
                        this.lastSequence = -1;
                    }
                }
            }
        }

        private IQIBBInputStream() {
            super();
        }

        protected PacketListener getDataPacketListener() {
            return new C11791();
        }

        protected PacketFilter getDataPacketFilter() {
            return new AndFilter(new PacketTypeFilter(Data.class), new IBBDataPacketFilter(null));
        }
    }

    private class IQIBBOutputStream extends IBBOutputStream {
        private IQIBBOutputStream() {
            super();
        }

        protected synchronized void writeToXML(DataPacketExtension dataPacketExtension) throws IOException {
            Packet data = new Data(dataPacketExtension);
            data.setTo(InBandBytestreamSession.this.remoteJID);
            try {
                SyncPacketSend.getReply(InBandBytestreamSession.this.connection, data);
            } catch (XMPPException e) {
                if (!this.isClosed) {
                    InBandBytestreamSession.this.close();
                    throw new IOException("Error while sending Data: " + e.getMessage());
                }
            }
        }
    }

    private class MessageIBBInputStream extends IBBInputStream {

        /* renamed from: org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamSession.MessageIBBInputStream.1 */
        class C11801 implements PacketListener {
            C11801() {
            }

            public void processPacket(Packet packet) {
                DataPacketExtension dataPacketExtension = (DataPacketExtension) packet.getExtension(DataPacketExtension.ELEMENT_NAME, InBandBytestreamManager.NAMESPACE);
                if (dataPacketExtension.getDecodedData() != null) {
                    MessageIBBInputStream.this.dataQueue.offer(dataPacketExtension);
                }
            }
        }

        private MessageIBBInputStream() {
            super();
        }

        protected PacketListener getDataPacketListener() {
            return new C11801();
        }

        protected PacketFilter getDataPacketFilter() {
            return new AndFilter(new PacketTypeFilter(Message.class), new IBBDataPacketFilter(null));
        }
    }

    private class MessageIBBOutputStream extends IBBOutputStream {
        private MessageIBBOutputStream() {
            super();
        }

        protected synchronized void writeToXML(DataPacketExtension dataPacketExtension) {
            Packet message = new Message(InBandBytestreamSession.this.remoteJID);
            message.addExtension(dataPacketExtension);
            InBandBytestreamSession.this.connection.sendPacket(message);
        }
    }

    protected InBandBytestreamSession(Connection connection, Open open, String str) {
        this.closeBothStreamsEnabled = false;
        this.isClosed = false;
        this.connection = connection;
        this.byteStreamRequest = open;
        this.remoteJID = str;
        switch (C09701.f1724xda8e09c8[open.getStanza().ordinal()]) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                this.inputStream = new IQIBBInputStream();
                this.outputStream = new IQIBBOutputStream();
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                this.inputStream = new MessageIBBInputStream();
                this.outputStream = new MessageIBBOutputStream();
            default:
        }
    }

    public InputStream getInputStream() {
        return this.inputStream;
    }

    public OutputStream getOutputStream() {
        return this.outputStream;
    }

    public int getReadTimeout() {
        return this.inputStream.readTimeout;
    }

    public void setReadTimeout(int i) {
        if (i < 0) {
            throw new IllegalArgumentException("Timeout must be >= 0");
        }
        this.inputStream.readTimeout = i;
    }

    public boolean isCloseBothStreamsEnabled() {
        return this.closeBothStreamsEnabled;
    }

    public void setCloseBothStreamsEnabled(boolean z) {
        this.closeBothStreamsEnabled = z;
    }

    public void close() throws IOException {
        closeByLocal(true);
        closeByLocal(false);
    }

    protected void closeByPeer(Close close) {
        this.inputStream.closeInternal();
        this.inputStream.cleanup();
        this.outputStream.closeInternal(false);
        this.connection.sendPacket(IQ.createResultIQ(close));
    }

    protected synchronized void closeByLocal(boolean z) throws IOException {
        if (!this.isClosed) {
            if (this.closeBothStreamsEnabled) {
                this.inputStream.closeInternal();
                this.outputStream.closeInternal(true);
            } else if (z) {
                this.inputStream.closeInternal();
            } else {
                this.outputStream.closeInternal(true);
            }
            if (this.inputStream.isClosed && this.outputStream.isClosed) {
                this.isClosed = true;
                Packet close = new Close(this.byteStreamRequest.getSessionID());
                close.setTo(this.remoteJID);
                try {
                    SyncPacketSend.getReply(this.connection, close);
                    this.inputStream.cleanup();
                    InBandBytestreamManager.getByteStreamManager(this.connection).getSessions().remove(this);
                } catch (XMPPException e) {
                    throw new IOException("Error while closing stream: " + e.getMessage());
                }
            }
        }
    }
}
