package org.jivesoftware.smackx.filetransfer;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.OrFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.packet.StreamInitiation;

public class FaultTolerantNegotiator extends StreamNegotiator {
    private Connection connection;
    private PacketFilter primaryFilter;
    private StreamNegotiator primaryNegotiator;
    private PacketFilter secondaryFilter;
    private StreamNegotiator secondaryNegotiator;

    private class NegotiatorService implements Callable<InputStream> {
        private PacketCollector collector;

        NegotiatorService(PacketCollector packetCollector) {
            this.collector = packetCollector;
        }

        public InputStream call() throws Exception {
            Packet nextResult = this.collector.nextResult((long) (SmackConfiguration.getPacketReplyTimeout() * 2));
            if (nextResult != null) {
                return FaultTolerantNegotiator.this.determineNegotiator(nextResult).negotiateIncomingStream(nextResult);
            }
            throw new XMPPException("No response from remote client");
        }
    }

    public FaultTolerantNegotiator(Connection connection, StreamNegotiator streamNegotiator, StreamNegotiator streamNegotiator2) {
        this.primaryNegotiator = streamNegotiator;
        this.secondaryNegotiator = streamNegotiator2;
        this.connection = connection;
    }

    public PacketFilter getInitiationPacketFilter(String str, String str2) {
        if (this.primaryFilter == null || this.secondaryFilter == null) {
            this.primaryFilter = this.primaryNegotiator.getInitiationPacketFilter(str, str2);
            this.secondaryFilter = this.secondaryNegotiator.getInitiationPacketFilter(str, str2);
        }
        return new OrFilter(this.primaryFilter, this.secondaryFilter);
    }

    InputStream negotiateIncomingStream(Packet packet) throws XMPPException {
        throw new UnsupportedOperationException("Negotiation only handled by create incoming stream method.");
    }

    final Packet initiateIncomingStream(Connection connection, StreamInitiation streamInitiation) {
        throw new UnsupportedOperationException("Initiation handled by createIncomingStream method");
    }

    public InputStream createIncomingStream(StreamInitiation streamInitiation) throws XMPPException {
        InputStream inputStream;
        XMPPException xMPPException = null;
        PacketCollector createPacketCollector = this.connection.createPacketCollector(getInitiationPacketFilter(streamInitiation.getFrom(), streamInitiation.getSessionID()));
        this.connection.sendPacket(super.createInitiationAccept(streamInitiation, getNamespaces()));
        ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(2);
        CompletionService executorCompletionService = new ExecutorCompletionService(newFixedThreadPool);
        List<Future> arrayList = new ArrayList();
        Future poll;
        InputStream inputStream2;
        XMPPException xMPPException2;
        try {
            arrayList.add(executorCompletionService.submit(new NegotiatorService(createPacketCollector)));
            arrayList.add(executorCompletionService.submit(new NegotiatorService(createPacketCollector)));
            int i = 0;
            inputStream = null;
            while (inputStream == null && i < arrayList.size()) {
                int i2 = i + 1;
                try {
                    poll = executorCompletionService.poll(10, TimeUnit.SECONDS);
                    if (poll == null) {
                        i = i2;
                    } else {
                        XMPPException xMPPException3 = xMPPException;
                        inputStream2 = (InputStream) poll.get();
                        xMPPException2 = xMPPException3;
                        inputStream = inputStream2;
                        xMPPException = xMPPException2;
                        i = i2;
                    }
                } catch (InterruptedException e) {
                    i = i2;
                }
            }
            for (Future poll2 : arrayList) {
                poll2.cancel(true);
            }
            createPacketCollector.cancel();
            newFixedThreadPool.shutdownNow();
            if (inputStream != null) {
                return inputStream;
            }
            if (xMPPException != null) {
                throw xMPPException;
            }
            throw new XMPPException("File transfer negotiation failed.");
        } catch (InterruptedException e2) {
            xMPPException2 = xMPPException;
            inputStream2 = inputStream;
        } catch (ExecutionException e3) {
            xMPPException2 = new XMPPException(e3.getCause());
            inputStream2 = inputStream;
        } catch (Throwable th) {
            Throwable th2 = th;
            for (Future poll22 : arrayList) {
                poll22.cancel(true);
            }
            createPacketCollector.cancel();
            newFixedThreadPool.shutdownNow();
        }
    }

    private StreamNegotiator determineNegotiator(Packet packet) {
        return this.primaryFilter.accept(packet) ? this.primaryNegotiator : this.secondaryNegotiator;
    }

    public OutputStream createOutgoingStream(String str, String str2, String str3) throws XMPPException {
        try {
            return this.primaryNegotiator.createOutgoingStream(str, str2, str3);
        } catch (XMPPException e) {
            return this.secondaryNegotiator.createOutgoingStream(str, str2, str3);
        }
    }

    public String[] getNamespaces() {
        Object namespaces = this.primaryNegotiator.getNamespaces();
        Object namespaces2 = this.secondaryNegotiator.getNamespaces();
        Object obj = new String[(namespaces.length + namespaces2.length)];
        System.arraycopy(namespaces, 0, obj, 0, namespaces.length);
        System.arraycopy(namespaces2, 0, obj, namespaces.length, namespaces2.length);
        return obj;
    }

    public void cleanup() {
    }
}
