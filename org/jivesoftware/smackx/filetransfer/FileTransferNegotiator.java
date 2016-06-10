package org.jivesoftware.smackx.filetransfer;

import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.packet.XMPPError.Condition;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.FormField.Option;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.bytestreams.ibb.InBandBytestreamManager;
import org.jivesoftware.smackx.bytestreams.socks5.Socks5BytestreamManager;
import org.jivesoftware.smackx.packet.DataForm;
import org.jivesoftware.smackx.packet.StreamInitiation;
import org.jivesoftware.smackx.packet.StreamInitiation.File;

public class FileTransferNegotiator {
    public static boolean IBB_ONLY = false;
    private static final String[] NAMESPACE;
    protected static final String STREAM_DATA_FIELD_NAME = "stream-method";
    private static final String STREAM_INIT_PREFIX = "jsi_";
    private static final Random randomGenerator;
    private static final Map<Connection, FileTransferNegotiator> transferObject;
    private final StreamNegotiator byteStreamTransferManager;
    private final Connection connection;
    private final StreamNegotiator inbandTransferManager;

    /* renamed from: org.jivesoftware.smackx.filetransfer.FileTransferNegotiator.2 */
    class C11982 implements ConnectionListener {
        final /* synthetic */ Connection val$connection;

        C11982(Connection connection) {
            this.val$connection = connection;
        }

        public void connectionClosed() {
            FileTransferNegotiator.this.cleanup(this.val$connection);
        }

        public void connectionClosedOnError(Exception exception) {
            FileTransferNegotiator.this.cleanup(this.val$connection);
        }

        public void reconnectionFailed(Exception exception) {
        }

        public void reconnectionSuccessful() {
        }

        public void reconnectingIn(int i) {
        }
    }

    /* renamed from: org.jivesoftware.smackx.filetransfer.FileTransferNegotiator.1 */
    static class C12931 extends IQ {
        C12931() {
        }

        public String getChildElementXML() {
            return null;
        }
    }

    static {
        boolean z = true;
        NAMESPACE = new String[]{"http://jabber.org/protocol/si/profile/file-transfer", "http://jabber.org/protocol/si"};
        transferObject = new ConcurrentHashMap();
        randomGenerator = new Random();
        if (System.getProperty("ibb") == null) {
            z = false;
        }
        IBB_ONLY = z;
    }

    public static FileTransferNegotiator getInstanceFor(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection cannot be null");
        } else if (!connection.isConnected()) {
            return null;
        } else {
            if (transferObject.containsKey(connection)) {
                return (FileTransferNegotiator) transferObject.get(connection);
            }
            FileTransferNegotiator fileTransferNegotiator = new FileTransferNegotiator(connection);
            setServiceEnabled(connection, true);
            transferObject.put(connection, fileTransferNegotiator);
            return fileTransferNegotiator;
        }
    }

    public static void setServiceEnabled(Connection connection, boolean z) {
        ServiceDiscoveryManager instanceFor = ServiceDiscoveryManager.getInstanceFor(connection);
        List<String> arrayList = new ArrayList();
        arrayList.addAll(Arrays.asList(NAMESPACE));
        arrayList.add(InBandBytestreamManager.NAMESPACE);
        if (!IBB_ONLY) {
            arrayList.add(Socks5BytestreamManager.NAMESPACE);
        }
        for (String str : arrayList) {
            if (!z) {
                instanceFor.removeFeature(str);
            } else if (!instanceFor.includesFeature(str)) {
                instanceFor.addFeature(str);
            }
        }
    }

    public static boolean isServiceEnabled(Connection connection) {
        ServiceDiscoveryManager instanceFor = ServiceDiscoveryManager.getInstanceFor(connection);
        List<String> arrayList = new ArrayList();
        arrayList.addAll(Arrays.asList(NAMESPACE));
        arrayList.add(InBandBytestreamManager.NAMESPACE);
        if (!IBB_ONLY) {
            arrayList.add(Socks5BytestreamManager.NAMESPACE);
        }
        for (String includesFeature : arrayList) {
            if (!instanceFor.includesFeature(includesFeature)) {
                return false;
            }
        }
        return true;
    }

    public static IQ createIQ(String str, String str2, String str3, Type type) {
        IQ c12931 = new C12931();
        c12931.setPacketID(str);
        c12931.setTo(str2);
        c12931.setFrom(str3);
        c12931.setType(type);
        return c12931;
    }

    public static Collection<String> getSupportedProtocols() {
        List arrayList = new ArrayList();
        arrayList.add(InBandBytestreamManager.NAMESPACE);
        if (!IBB_ONLY) {
            arrayList.add(Socks5BytestreamManager.NAMESPACE);
        }
        return Collections.unmodifiableList(arrayList);
    }

    private FileTransferNegotiator(Connection connection) {
        configureConnection(connection);
        this.connection = connection;
        this.byteStreamTransferManager = new Socks5TransferNegotiator(connection);
        this.inbandTransferManager = new IBBTransferNegotiator(connection);
    }

    private void configureConnection(Connection connection) {
        connection.addConnectionListener(new C11982(connection));
    }

    private void cleanup(Connection connection) {
        if (transferObject.remove(connection) != null) {
            this.inbandTransferManager.cleanup();
        }
    }

    public StreamNegotiator selectStreamNegotiator(FileTransferRequest fileTransferRequest) throws XMPPException {
        StreamInitiation streamInitiation = fileTransferRequest.getStreamInitiation();
        FormField streamMethodField = getStreamMethodField(streamInitiation.getFeatureNegotiationForm());
        if (streamMethodField == null) {
            String str = "No stream methods contained in packet.";
            XMPPError xMPPError = new XMPPError(Condition.bad_request, str);
            Packet createIQ = createIQ(streamInitiation.getPacketID(), streamInitiation.getFrom(), streamInitiation.getTo(), Type.ERROR);
            createIQ.setError(xMPPError);
            this.connection.sendPacket(createIQ);
            throw new XMPPException(str, xMPPError);
        }
        try {
            return getNegotiator(streamMethodField);
        } catch (XMPPException e) {
            createIQ = createIQ(streamInitiation.getPacketID(), streamInitiation.getFrom(), streamInitiation.getTo(), Type.ERROR);
            createIQ.setError(e.getXMPPError());
            this.connection.sendPacket(createIQ);
            throw e;
        }
    }

    private FormField getStreamMethodField(DataForm dataForm) {
        Iterator fields = dataForm.getFields();
        while (fields.hasNext()) {
            FormField formField = (FormField) fields.next();
            if (formField.getVariable().equals(STREAM_DATA_FIELD_NAME)) {
                return formField;
            }
        }
        return null;
    }

    private StreamNegotiator getNegotiator(FormField formField) throws XMPPException {
        Iterator options = formField.getOptions();
        Object obj = null;
        Object obj2 = null;
        while (options.hasNext()) {
            String value = ((Option) options.next()).getValue();
            if (value.equals(Socks5BytestreamManager.NAMESPACE) && !IBB_ONLY) {
                obj2 = 1;
            } else if (value.equals(InBandBytestreamManager.NAMESPACE)) {
                obj = 1;
            }
        }
        if (obj2 == null && r1 == null) {
            XMPPError xMPPError = new XMPPError(Condition.bad_request, "No acceptable transfer mechanism");
            throw new XMPPException(xMPPError.getMessage(), xMPPError);
        } else if (obj2 != null && r1 != null) {
            return new FaultTolerantNegotiator(this.connection, this.byteStreamTransferManager, this.inbandTransferManager);
        } else {
            if (obj2 != null) {
                return this.byteStreamTransferManager;
            }
            return this.inbandTransferManager;
        }
    }

    public void rejectStream(StreamInitiation streamInitiation) {
        XMPPError xMPPError = new XMPPError(Condition.forbidden, "Offer Declined");
        Packet createIQ = createIQ(streamInitiation.getPacketID(), streamInitiation.getFrom(), streamInitiation.getTo(), Type.ERROR);
        createIQ.setError(xMPPError);
        this.connection.sendPacket(createIQ);
    }

    public String getNextStreamID() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(STREAM_INIT_PREFIX);
        stringBuilder.append(Math.abs(randomGenerator.nextLong()));
        return stringBuilder.toString();
    }

    public StreamNegotiator negotiateOutgoingTransfer(String str, String str2, String str3, long j, String str4, int i) throws XMPPException {
        Packet streamInitiation = new StreamInitiation();
        streamInitiation.setSesssionID(str2);
        streamInitiation.setMimeType(URLConnection.guessContentTypeFromName(str3));
        File file = new File(str3, j);
        file.setDesc(str4);
        streamInitiation.setFile(file);
        streamInitiation.setFeatureNegotiationForm(createDefaultInitiationForm());
        streamInitiation.setFrom(this.connection.getUser());
        streamInitiation.setTo(str);
        streamInitiation.setType(Type.SET);
        PacketCollector createPacketCollector = this.connection.createPacketCollector(new PacketIDFilter(streamInitiation.getPacketID()));
        this.connection.sendPacket(streamInitiation);
        Packet nextResult = createPacketCollector.nextResult((long) i);
        createPacketCollector.cancel();
        if (!(nextResult instanceof IQ)) {
            return null;
        }
        IQ iq = (IQ) nextResult;
        if (iq.getType().equals(Type.RESULT)) {
            return getOutgoingNegotiator(getStreamMethodField(((StreamInitiation) nextResult).getFeatureNegotiationForm()));
        }
        if (iq.getType().equals(Type.ERROR)) {
            throw new XMPPException(iq.getError());
        }
        throw new XMPPException("File transfer response unreadable");
    }

    private StreamNegotiator getOutgoingNegotiator(FormField formField) throws XMPPException {
        Iterator values = formField.getValues();
        Object obj = null;
        Object obj2 = null;
        while (values.hasNext()) {
            String str = (String) values.next();
            if (str.equals(Socks5BytestreamManager.NAMESPACE) && !IBB_ONLY) {
                obj2 = 1;
            } else if (str.equals(InBandBytestreamManager.NAMESPACE)) {
                obj = 1;
            }
        }
        if (obj2 == null && r1 == null) {
            XMPPError xMPPError = new XMPPError(Condition.bad_request, "No acceptable transfer mechanism");
            throw new XMPPException(xMPPError.getMessage(), xMPPError);
        } else if (obj2 != null && r1 != null) {
            return new FaultTolerantNegotiator(this.connection, this.byteStreamTransferManager, this.inbandTransferManager);
        } else {
            if (obj2 != null) {
                return this.byteStreamTransferManager;
            }
            return this.inbandTransferManager;
        }
    }

    private DataForm createDefaultInitiationForm() {
        DataForm dataForm = new DataForm(Form.TYPE_FORM);
        FormField formField = new FormField(STREAM_DATA_FIELD_NAME);
        formField.setType(FormField.TYPE_LIST_SINGLE);
        if (!IBB_ONLY) {
            formField.addOption(new Option(Socks5BytestreamManager.NAMESPACE));
        }
        formField.addOption(new Option(InBandBytestreamManager.NAMESPACE));
        dataForm.addField(formField);
        return dataForm;
    }
}
