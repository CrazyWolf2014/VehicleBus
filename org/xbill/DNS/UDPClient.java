package org.xbill.DNS;

import java.io.EOFException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.security.SecureRandom;

final class UDPClient extends Client {
    private static final int EPHEMERAL_RANGE = 64511;
    private static final int EPHEMERAL_START = 1024;
    private static final int EPHEMERAL_STOP = 65535;
    private static SecureRandom prng;
    private static volatile boolean prng_initializing;
    private boolean bound;

    /* renamed from: org.xbill.DNS.UDPClient.1 */
    static class C10001 implements Runnable {
        C10001() {
        }

        public void run() {
            UDPClient.prng.nextInt();
            UDPClient.prng_initializing = false;
        }
    }

    static {
        prng = new SecureRandom();
        prng_initializing = true;
        new Thread(new C10001()).start();
    }

    public UDPClient(long j) throws IOException {
        super(DatagramChannel.open(), j);
        this.bound = false;
    }

    private void bind_random(InetSocketAddress inetSocketAddress) throws IOException {
        if (prng_initializing) {
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
            }
            if (prng_initializing) {
                return;
            }
        }
        DatagramChannel datagramChannel = (DatagramChannel) this.key.channel();
        int i = 0;
        while (i < EPHEMERAL_START) {
            try {
                SocketAddress inetSocketAddress2;
                int nextInt = prng.nextInt(EPHEMERAL_RANGE) + EPHEMERAL_START;
                if (inetSocketAddress != null) {
                    inetSocketAddress2 = new InetSocketAddress(inetSocketAddress.getAddress(), nextInt);
                } else {
                    inetSocketAddress2 = new InetSocketAddress(nextInt);
                }
                datagramChannel.socket().bind(inetSocketAddress2);
                this.bound = true;
                return;
            } catch (SocketException e2) {
                i++;
            }
        }
    }

    void bind(SocketAddress socketAddress) throws IOException {
        if (socketAddress == null || ((socketAddress instanceof InetSocketAddress) && ((InetSocketAddress) socketAddress).getPort() == 0)) {
            bind_random((InetSocketAddress) socketAddress);
            if (this.bound) {
                return;
            }
        }
        if (socketAddress != null) {
            ((DatagramChannel) this.key.channel()).socket().bind(socketAddress);
            this.bound = true;
        }
    }

    void connect(SocketAddress socketAddress) throws IOException {
        if (!this.bound) {
            bind(null);
        }
        ((DatagramChannel) this.key.channel()).connect(socketAddress);
    }

    void send(byte[] bArr) throws IOException {
        DatagramChannel datagramChannel = (DatagramChannel) this.key.channel();
        Client.verboseLog("UDP write", bArr);
        datagramChannel.write(ByteBuffer.wrap(bArr));
    }

    byte[] recv(int i) throws IOException {
        DatagramChannel datagramChannel = (DatagramChannel) this.key.channel();
        Object obj = new byte[i];
        this.key.interestOps(1);
        while (!this.key.isReadable()) {
            try {
                Client.blockUntil(this.key, this.endTime);
            } finally {
                obj = this.key.isValid();
                if (obj != null) {
                    obj = this.key;
                    obj.interestOps(0);
                }
            }
        }
        long read = (long) datagramChannel.read(ByteBuffer.wrap(obj));
        if (read <= 0) {
            throw new EOFException();
        }
        int i2 = (int) read;
        Object obj2 = new byte[i2];
        System.arraycopy(obj, 0, obj2, 0, i2);
        Client.verboseLog("UDP read", obj2);
        return obj2;
    }

    static byte[] sendrecv(SocketAddress socketAddress, SocketAddress socketAddress2, byte[] bArr, int i, long j) throws IOException {
        UDPClient uDPClient = new UDPClient(j);
        try {
            uDPClient.bind(socketAddress);
            uDPClient.connect(socketAddress2);
            uDPClient.send(bArr);
            byte[] recv = uDPClient.recv(i);
            return recv;
        } finally {
            uDPClient.cleanup();
        }
    }

    static byte[] sendrecv(SocketAddress socketAddress, byte[] bArr, int i, long j) throws IOException {
        return sendrecv(null, socketAddress, bArr, i, j);
    }
}
