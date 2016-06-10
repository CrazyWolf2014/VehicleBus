package org.xbill.DNS;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

final class TCPClient extends Client {
    public TCPClient(long j) throws IOException {
        super(SocketChannel.open(), j);
    }

    void bind(SocketAddress socketAddress) throws IOException {
        ((SocketChannel) this.key.channel()).socket().bind(socketAddress);
    }

    void connect(SocketAddress socketAddress) throws IOException {
        SocketChannel socketChannel = (SocketChannel) this.key.channel();
        if (!socketChannel.connect(socketAddress)) {
            this.key.interestOps(8);
            while (!socketChannel.finishConnect()) {
                try {
                    if (!this.key.isConnectable()) {
                        Client.blockUntil(this.key, this.endTime);
                    }
                } catch (Throwable th) {
                    if (this.key.isValid()) {
                        this.key.interestOps(0);
                    }
                }
            }
            if (this.key.isValid()) {
                this.key.interestOps(0);
            }
        }
    }

    void send(byte[] bArr) throws IOException {
        SocketChannel socketChannel = (SocketChannel) this.key.channel();
        Client.verboseLog("TCP write", bArr);
        byte[] bArr2 = new byte[]{(byte) (bArr.length >>> 8), (byte) (bArr.length & KEYRecord.PROTOCOL_ANY)};
        ByteBuffer[] byteBufferArr = new ByteBuffer[]{ByteBuffer.wrap(bArr2), ByteBuffer.wrap(bArr)};
        this.key.interestOps(4);
        int i = 0;
        while (i < bArr.length + 2) {
            if (this.key.isWritable()) {
                long write = socketChannel.write(byteBufferArr);
                if (write < 0) {
                    throw new EOFException();
                }
                i += (int) write;
                try {
                    if (i < bArr.length + 2 && System.currentTimeMillis() > this.endTime) {
                        throw new SocketTimeoutException();
                    }
                } catch (Throwable th) {
                    if (this.key.isValid()) {
                        this.key.interestOps(0);
                    }
                }
            } else {
                Client.blockUntil(this.key, this.endTime);
            }
        }
        if (this.key.isValid()) {
            this.key.interestOps(0);
        }
    }

    private byte[] _recv(int i) throws IOException {
        SocketChannel socketChannel = (SocketChannel) this.key.channel();
        byte[] bArr = new byte[i];
        ByteBuffer wrap = ByteBuffer.wrap(bArr);
        this.key.interestOps(1);
        int i2 = 0;
        while (i2 < i) {
            if (this.key.isReadable()) {
                long read = (long) socketChannel.read(wrap);
                if (read < 0) {
                    throw new EOFException();
                }
                i2 += (int) read;
                if (i2 < i) {
                    try {
                        if (System.currentTimeMillis() > this.endTime) {
                            throw new SocketTimeoutException();
                        }
                    } catch (Throwable th) {
                        if (this.key.isValid()) {
                            this.key.interestOps(0);
                        }
                    }
                } else {
                    continue;
                }
            } else {
                Client.blockUntil(this.key, this.endTime);
            }
        }
        if (this.key.isValid()) {
            this.key.interestOps(0);
        }
        return bArr;
    }

    byte[] recv() throws IOException {
        byte[] _recv = _recv(2);
        _recv = _recv((_recv[1] & KEYRecord.PROTOCOL_ANY) + ((_recv[0] & KEYRecord.PROTOCOL_ANY) << 8));
        Client.verboseLog("TCP read", _recv);
        return _recv;
    }

    static byte[] sendrecv(SocketAddress socketAddress, SocketAddress socketAddress2, byte[] bArr, long j) throws IOException {
        TCPClient tCPClient = new TCPClient(j);
        if (socketAddress != null) {
            try {
                tCPClient.bind(socketAddress);
            } catch (Throwable th) {
                tCPClient.cleanup();
            }
        }
        tCPClient.connect(socketAddress2);
        tCPClient.send(bArr);
        byte[] recv = tCPClient.recv();
        tCPClient.cleanup();
        return recv;
    }

    static byte[] sendrecv(SocketAddress socketAddress, byte[] bArr, long j) throws IOException {
        return sendrecv(null, socketAddress, bArr, j);
    }
}
