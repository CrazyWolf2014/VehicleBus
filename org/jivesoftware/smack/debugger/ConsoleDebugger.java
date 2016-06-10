package org.jivesoftware.smack.debugger;

import com.cnmobi.im.util.XmppConnection;
import com.tencent.mm.sdk.platformtools.FilePathGenerator;
import java.io.Reader;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.ObservableReader;
import org.jivesoftware.smack.util.ObservableWriter;
import org.jivesoftware.smack.util.ReaderListener;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smack.util.WriterListener;
import org.xmlpull.v1.XmlPullParser;

public class ConsoleDebugger implements SmackDebugger {
    public static boolean printInterpreted;
    private ConnectionListener connListener;
    private Connection connection;
    private SimpleDateFormat dateFormatter;
    private PacketListener listener;
    private Reader reader;
    private ReaderListener readerListener;
    private Writer writer;
    private WriterListener writerListener;

    /* renamed from: org.jivesoftware.smack.debugger.ConsoleDebugger.1 */
    class C11591 implements ReaderListener {
        C11591() {
        }

        public void read(String str) {
            System.out.println(ConsoleDebugger.this.dateFormatter.format(new Date()) + " RCV  (" + ConsoleDebugger.this.connection.hashCode() + "): " + str);
        }
    }

    /* renamed from: org.jivesoftware.smack.debugger.ConsoleDebugger.2 */
    class C11602 implements WriterListener {
        C11602() {
        }

        public void write(String str) {
            System.out.println(ConsoleDebugger.this.dateFormatter.format(new Date()) + " SENT (" + ConsoleDebugger.this.connection.hashCode() + "): " + str);
        }
    }

    /* renamed from: org.jivesoftware.smack.debugger.ConsoleDebugger.3 */
    class C11613 implements PacketListener {
        C11613() {
        }

        public void processPacket(Packet packet) {
            if (ConsoleDebugger.printInterpreted) {
                System.out.println(ConsoleDebugger.this.dateFormatter.format(new Date()) + " RCV PKT (" + ConsoleDebugger.this.connection.hashCode() + "): " + packet.toXML());
            }
        }
    }

    /* renamed from: org.jivesoftware.smack.debugger.ConsoleDebugger.4 */
    class C11624 implements ConnectionListener {
        C11624() {
        }

        public void connectionClosed() {
            System.out.println(ConsoleDebugger.this.dateFormatter.format(new Date()) + " Connection closed (" + ConsoleDebugger.this.connection.hashCode() + ")");
        }

        public void connectionClosedOnError(Exception exception) {
            System.out.println(ConsoleDebugger.this.dateFormatter.format(new Date()) + " Connection closed due to an exception (" + ConsoleDebugger.this.connection.hashCode() + ")");
            exception.printStackTrace();
        }

        public void reconnectionFailed(Exception exception) {
            System.out.println(ConsoleDebugger.this.dateFormatter.format(new Date()) + " Reconnection failed due to an exception (" + ConsoleDebugger.this.connection.hashCode() + ")");
            exception.printStackTrace();
        }

        public void reconnectionSuccessful() {
            System.out.println(ConsoleDebugger.this.dateFormatter.format(new Date()) + " Connection reconnected (" + ConsoleDebugger.this.connection.hashCode() + ")");
        }

        public void reconnectingIn(int i) {
            System.out.println(ConsoleDebugger.this.dateFormatter.format(new Date()) + " Connection (" + ConsoleDebugger.this.connection.hashCode() + ") will reconnect in " + i);
        }
    }

    static {
        printInterpreted = false;
    }

    public ConsoleDebugger(Connection connection, Writer writer, Reader reader) {
        this.dateFormatter = new SimpleDateFormat("hh:mm:ss aaa");
        this.connection = null;
        this.listener = null;
        this.connListener = null;
        this.connection = connection;
        this.writer = writer;
        this.reader = reader;
        createDebug();
    }

    private void createDebug() {
        Reader observableReader = new ObservableReader(this.reader);
        this.readerListener = new C11591();
        observableReader.addReaderListener(this.readerListener);
        Writer observableWriter = new ObservableWriter(this.writer);
        this.writerListener = new C11602();
        observableWriter.addWriterListener(this.writerListener);
        this.reader = observableReader;
        this.writer = observableWriter;
        this.listener = new C11613();
        this.connListener = new C11624();
    }

    public Reader newConnectionReader(Reader reader) {
        ((ObservableReader) this.reader).removeReaderListener(this.readerListener);
        Reader observableReader = new ObservableReader(reader);
        observableReader.addReaderListener(this.readerListener);
        this.reader = observableReader;
        return this.reader;
    }

    public Writer newConnectionWriter(Writer writer) {
        ((ObservableWriter) this.writer).removeWriterListener(this.writerListener);
        Writer observableWriter = new ObservableWriter(writer);
        observableWriter.addWriterListener(this.writerListener);
        this.writer = observableWriter;
        return this.writer;
    }

    public void userHasLogged(String str) {
        System.out.println(("User logged (" + this.connection.hashCode() + "): " + (XmlPullParser.NO_NAMESPACE.equals(StringUtils.parseName(str)) ? XmlPullParser.NO_NAMESPACE : StringUtils.parseBareAddress(str)) + XmppConnection.JID_SEPARATOR + this.connection.getServiceName() + ":" + this.connection.getPort()) + FilePathGenerator.ANDROID_DIR_SEP + StringUtils.parseResource(str));
        this.connection.addConnectionListener(this.connListener);
    }

    public Reader getReader() {
        return this.reader;
    }

    public Writer getWriter() {
        return this.writer;
    }

    public PacketListener getReaderListener() {
        return this.listener;
    }

    public PacketListener getWriterListener() {
        return null;
    }
}
