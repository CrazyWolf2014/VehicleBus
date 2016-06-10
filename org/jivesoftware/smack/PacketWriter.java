package org.jivesoftware.smack;

import com.ifoer.util.MyHttpException;
import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.jivesoftware.smack.packet.Packet;

class PacketWriter {
    private XMPPConnection connection;
    private boolean done;
    private Thread keepAliveThread;
    private long lastActive;
    private final BlockingQueue<Packet> queue;
    private Writer writer;
    private Thread writerThread;

    /* renamed from: org.jivesoftware.smack.PacketWriter.1 */
    class C09581 extends Thread {
        C09581() {
        }

        public void run() {
            PacketWriter.this.writePackets(this);
        }
    }

    private class KeepAliveTask implements Runnable {
        private int delay;
        private Thread thread;

        public KeepAliveTask(int i) {
            this.delay = i;
        }

        protected void setThread(Thread thread) {
            this.thread = thread;
        }

        public void run() {
            try {
                Thread.sleep((long) (this.delay + 15000));
            } catch (InterruptedException e) {
            }
            while (!PacketWriter.this.done && PacketWriter.this.keepAliveThread == this.thread) {
                synchronized (PacketWriter.this.writer) {
                    if (System.currentTimeMillis() - PacketWriter.this.lastActive >= ((long) this.delay)) {
                        try {
                            PacketWriter.this.writer.write(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
                            PacketWriter.this.writer.flush();
                        } catch (Exception e2) {
                        }
                    }
                }
                try {
                    Thread.sleep((long) this.delay);
                } catch (InterruptedException e3) {
                }
            }
        }
    }

    protected PacketWriter(XMPPConnection xMPPConnection) {
        this.lastActive = System.currentTimeMillis();
        this.queue = new ArrayBlockingQueue(MyHttpException.ERROR_SERVER, true);
        this.connection = xMPPConnection;
        init();
    }

    protected void init() {
        this.writer = this.connection.writer;
        this.done = false;
        this.writerThread = new C09581();
        this.writerThread.setName("Smack Packet Writer (" + this.connection.connectionCounterValue + ")");
        this.writerThread.setDaemon(true);
    }

    public void sendPacket(Packet packet) {
        if (!this.done) {
            this.connection.firePacketInterceptors(packet);
            try {
                this.queue.put(packet);
                synchronized (this.queue) {
                    this.queue.notifyAll();
                }
                this.connection.firePacketSendingListeners(packet);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void startup() {
        this.writerThread.start();
    }

    void startKeepAliveProcess() {
        int keepAliveInterval = SmackConfiguration.getKeepAliveInterval();
        if (keepAliveInterval > 0) {
            Object keepAliveTask = new KeepAliveTask(keepAliveInterval);
            this.keepAliveThread = new Thread(keepAliveTask);
            keepAliveTask.setThread(this.keepAliveThread);
            this.keepAliveThread.setDaemon(true);
            this.keepAliveThread.setName("Smack Keep Alive (" + this.connection.connectionCounterValue + ")");
            this.keepAliveThread.start();
        }
    }

    void setWriter(Writer writer) {
        this.writer = writer;
    }

    public void shutdown() {
        this.done = true;
        synchronized (this.queue) {
            this.queue.notifyAll();
        }
        if (this.keepAliveThread != null) {
            this.keepAliveThread.interrupt();
        }
    }

    void cleanup() {
        this.connection.interceptors.clear();
        this.connection.sendListeners.clear();
    }

    private Packet nextPacket() {
        Packet packet = null;
        while (!this.done) {
            packet = (Packet) this.queue.poll();
            if (packet != null) {
                break;
            }
            try {
                synchronized (this.queue) {
                    this.queue.wait();
                }
            } catch (InterruptedException e) {
            }
        }
        return packet;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void writePackets(java.lang.Thread r5) {
        /*
        r4 = this;
        r4.openStream();	 Catch:{ IOException -> 0x002d }
    L_0x0003:
        r0 = r4.done;	 Catch:{ IOException -> 0x002d }
        if (r0 != 0) goto L_0x004b;
    L_0x0007:
        r0 = r4.writerThread;	 Catch:{ IOException -> 0x002d }
        if (r0 != r5) goto L_0x004b;
    L_0x000b:
        r0 = r4.nextPacket();	 Catch:{ IOException -> 0x002d }
        if (r0 == 0) goto L_0x0003;
    L_0x0011:
        r1 = r4.writer;	 Catch:{ IOException -> 0x002d }
        monitor-enter(r1);	 Catch:{ IOException -> 0x002d }
        r2 = r4.writer;	 Catch:{ all -> 0x002a }
        r0 = r0.toXML();	 Catch:{ all -> 0x002a }
        r2.write(r0);	 Catch:{ all -> 0x002a }
        r0 = r4.writer;	 Catch:{ all -> 0x002a }
        r0.flush();	 Catch:{ all -> 0x002a }
        r2 = java.lang.System.currentTimeMillis();	 Catch:{ all -> 0x002a }
        r4.lastActive = r2;	 Catch:{ all -> 0x002a }
        monitor-exit(r1);	 Catch:{ all -> 0x002a }
        goto L_0x0003;
    L_0x002a:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x002a }
        throw r0;	 Catch:{ IOException -> 0x002d }
    L_0x002d:
        r0 = move-exception;
        r1 = r4.done;
        if (r1 != 0) goto L_0x004a;
    L_0x0032:
        r1 = r4.connection;
        r1 = r1.isSocketClosed();
        if (r1 != 0) goto L_0x004a;
    L_0x003a:
        r1 = 1;
        r4.done = r1;
        r1 = r4.connection;
        r1 = r1.packetReader;
        if (r1 == 0) goto L_0x004a;
    L_0x0043:
        r1 = r4.connection;
        r1 = r1.packetReader;
        r1.notifyConnectionError(r0);
    L_0x004a:
        return;
    L_0x004b:
        r1 = r4.writer;	 Catch:{ Exception -> 0x006b }
        monitor-enter(r1);	 Catch:{ Exception -> 0x006b }
    L_0x004e:
        r0 = r4.queue;	 Catch:{ all -> 0x0068 }
        r0 = r0.isEmpty();	 Catch:{ all -> 0x0068 }
        if (r0 != 0) goto L_0x0088;
    L_0x0056:
        r0 = r4.queue;	 Catch:{ all -> 0x0068 }
        r0 = r0.remove();	 Catch:{ all -> 0x0068 }
        r0 = (org.jivesoftware.smack.packet.Packet) r0;	 Catch:{ all -> 0x0068 }
        r2 = r4.writer;	 Catch:{ all -> 0x0068 }
        r0 = r0.toXML();	 Catch:{ all -> 0x0068 }
        r2.write(r0);	 Catch:{ all -> 0x0068 }
        goto L_0x004e;
    L_0x0068:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0068 }
        throw r0;	 Catch:{ Exception -> 0x006b }
    L_0x006b:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ IOException -> 0x002d }
    L_0x006f:
        r0 = r4.queue;	 Catch:{ IOException -> 0x002d }
        r0.clear();	 Catch:{ IOException -> 0x002d }
        r0 = r4.writer;	 Catch:{ Exception -> 0x008f, all -> 0x0098 }
        r1 = "</stream:stream>";
        r0.write(r1);	 Catch:{ Exception -> 0x008f, all -> 0x0098 }
        r0 = r4.writer;	 Catch:{ Exception -> 0x008f, all -> 0x0098 }
        r0.flush();	 Catch:{ Exception -> 0x008f, all -> 0x0098 }
        r0 = r4.writer;	 Catch:{ Exception -> 0x0086 }
        r0.close();	 Catch:{ Exception -> 0x0086 }
        goto L_0x004a;
    L_0x0086:
        r0 = move-exception;
        goto L_0x004a;
    L_0x0088:
        r0 = r4.writer;	 Catch:{ all -> 0x0068 }
        r0.flush();	 Catch:{ all -> 0x0068 }
        monitor-exit(r1);	 Catch:{ all -> 0x0068 }
        goto L_0x006f;
    L_0x008f:
        r0 = move-exception;
        r0 = r4.writer;	 Catch:{ Exception -> 0x0096 }
        r0.close();	 Catch:{ Exception -> 0x0096 }
        goto L_0x004a;
    L_0x0096:
        r0 = move-exception;
        goto L_0x004a;
    L_0x0098:
        r0 = move-exception;
        r1 = r4.writer;	 Catch:{ Exception -> 0x009f }
        r1.close();	 Catch:{ Exception -> 0x009f }
    L_0x009e:
        throw r0;	 Catch:{ IOException -> 0x002d }
    L_0x009f:
        r1 = move-exception;
        goto L_0x009e;
        */
        throw new UnsupportedOperationException("Method not decompiled: org.jivesoftware.smack.PacketWriter.writePackets(java.lang.Thread):void");
    }

    void openStream() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<stream:stream");
        stringBuilder.append(" to=\"").append(this.connection.getServiceName()).append("\"");
        stringBuilder.append(" xmlns=\"jabber:client\"");
        stringBuilder.append(" xmlns:stream=\"http://etherx.jabber.org/streams\"");
        stringBuilder.append(" version=\"1.0\">");
        this.writer.write(stringBuilder.toString());
        this.writer.flush();
    }
}
