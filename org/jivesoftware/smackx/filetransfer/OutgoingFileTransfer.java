package org.jivesoftware.smackx.filetransfer;

import com.ifoer.util.MyHttpException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smackx.filetransfer.FileTransfer.Error;
import org.jivesoftware.smackx.filetransfer.FileTransfer.Status;

public class OutgoingFileTransfer extends FileTransfer {
    private static int RESPONSE_TIMEOUT;
    private NegotiationProgress callback;
    private String initiator;
    private OutputStream outputStream;
    private Thread transferThread;

    /* renamed from: org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.1 */
    class C09801 implements Runnable {
        final /* synthetic */ String val$description;
        final /* synthetic */ String val$fileName;
        final /* synthetic */ long val$fileSize;
        final /* synthetic */ NegotiationProgress val$progress;

        C09801(String str, long j, String str2, NegotiationProgress negotiationProgress) {
            this.val$fileName = str;
            this.val$fileSize = j;
            this.val$description = str2;
            this.val$progress = negotiationProgress;
        }

        public void run() {
            try {
                OutgoingFileTransfer.this.outputStream = OutgoingFileTransfer.this.negotiateStream(this.val$fileName, this.val$fileSize, this.val$description);
                this.val$progress.outputStreamEstablished(OutgoingFileTransfer.this.outputStream);
            } catch (XMPPException e) {
                OutgoingFileTransfer.this.handleXMPPException(e);
            }
        }
    }

    /* renamed from: org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.2 */
    class C09812 implements Runnable {
        final /* synthetic */ String val$description;
        final /* synthetic */ File val$file;

        C09812(File file, String str) {
            this.val$file = file;
            this.val$description = str;
        }

        public void run() {
            InputStream fileInputStream;
            Exception e;
            Throwable th;
            try {
                OutgoingFileTransfer.this.outputStream = OutgoingFileTransfer.this.negotiateStream(this.val$file.getName(), this.val$file.length(), this.val$description);
                if (OutgoingFileTransfer.this.outputStream != null && OutgoingFileTransfer.this.updateStatus(Status.negotiated, Status.in_progress)) {
                    try {
                        fileInputStream = new FileInputStream(this.val$file);
                        try {
                            OutgoingFileTransfer.this.writeToStream(fileInputStream, OutgoingFileTransfer.this.outputStream);
                            if (fileInputStream != null) {
                                try {
                                    fileInputStream.close();
                                } catch (IOException e2) {
                                }
                            }
                            OutgoingFileTransfer.this.outputStream.flush();
                            OutgoingFileTransfer.this.outputStream.close();
                        } catch (FileNotFoundException e3) {
                            e = e3;
                            try {
                                OutgoingFileTransfer.this.setStatus(Status.error);
                                OutgoingFileTransfer.this.setError(Error.bad_file);
                                OutgoingFileTransfer.this.setException(e);
                                if (fileInputStream != null) {
                                    try {
                                        fileInputStream.close();
                                    } catch (IOException e4) {
                                    }
                                }
                                OutgoingFileTransfer.this.outputStream.flush();
                                OutgoingFileTransfer.this.outputStream.close();
                                OutgoingFileTransfer.this.updateStatus(Status.in_progress, Status.complete);
                            } catch (Throwable th2) {
                                th = th2;
                                if (fileInputStream != null) {
                                    try {
                                        fileInputStream.close();
                                    } catch (IOException e5) {
                                        throw th;
                                    }
                                }
                                OutgoingFileTransfer.this.outputStream.flush();
                                OutgoingFileTransfer.this.outputStream.close();
                                throw th;
                            }
                        } catch (XMPPException e6) {
                            e = e6;
                            OutgoingFileTransfer.this.setStatus(Status.error);
                            OutgoingFileTransfer.this.setException(e);
                            if (fileInputStream != null) {
                                try {
                                    fileInputStream.close();
                                } catch (IOException e7) {
                                }
                            }
                            OutgoingFileTransfer.this.outputStream.flush();
                            OutgoingFileTransfer.this.outputStream.close();
                            OutgoingFileTransfer.this.updateStatus(Status.in_progress, Status.complete);
                        }
                    } catch (FileNotFoundException e8) {
                        e = e8;
                        fileInputStream = null;
                        OutgoingFileTransfer.this.setStatus(Status.error);
                        OutgoingFileTransfer.this.setError(Error.bad_file);
                        OutgoingFileTransfer.this.setException(e);
                        if (fileInputStream != null) {
                            fileInputStream.close();
                        }
                        OutgoingFileTransfer.this.outputStream.flush();
                        OutgoingFileTransfer.this.outputStream.close();
                        OutgoingFileTransfer.this.updateStatus(Status.in_progress, Status.complete);
                    } catch (XMPPException e9) {
                        e = e9;
                        fileInputStream = null;
                        OutgoingFileTransfer.this.setStatus(Status.error);
                        OutgoingFileTransfer.this.setException(e);
                        if (fileInputStream != null) {
                            fileInputStream.close();
                        }
                        OutgoingFileTransfer.this.outputStream.flush();
                        OutgoingFileTransfer.this.outputStream.close();
                        OutgoingFileTransfer.this.updateStatus(Status.in_progress, Status.complete);
                    } catch (Throwable th3) {
                        th = th3;
                        fileInputStream = null;
                        if (fileInputStream != null) {
                            fileInputStream.close();
                        }
                        OutgoingFileTransfer.this.outputStream.flush();
                        OutgoingFileTransfer.this.outputStream.close();
                        throw th;
                    }
                    OutgoingFileTransfer.this.updateStatus(Status.in_progress, Status.complete);
                }
            } catch (XMPPException e10) {
                OutgoingFileTransfer.this.handleXMPPException(e10);
            }
        }
    }

    /* renamed from: org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.3 */
    class C09823 implements Runnable {
        final /* synthetic */ String val$description;
        final /* synthetic */ String val$fileName;
        final /* synthetic */ long val$fileSize;
        final /* synthetic */ InputStream val$in;

        public void run() {
            /* JADX: method processing error */
/*
            Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x008c in list []
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:42)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:58)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
            /*
            r6 = this;
            r0 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;
            r1 = r6.val$fileName;
            r2 = r6.val$fileSize;
            r0.setFileInfo(r1, r2);
            r0 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;	 Catch:{ XMPPException -> 0x0023, IllegalStateException -> 0x002a }
            r1 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;	 Catch:{ XMPPException -> 0x0023, IllegalStateException -> 0x002a }
            r2 = r6.val$fileName;	 Catch:{ XMPPException -> 0x0023, IllegalStateException -> 0x002a }
            r3 = r6.val$fileSize;	 Catch:{ XMPPException -> 0x0023, IllegalStateException -> 0x002a }
            r5 = r6.val$description;	 Catch:{ XMPPException -> 0x0023, IllegalStateException -> 0x002a }
            r1 = r1.negotiateStream(r2, r3, r5);	 Catch:{ XMPPException -> 0x0023, IllegalStateException -> 0x002a }
            r0.outputStream = r1;	 Catch:{ XMPPException -> 0x0023, IllegalStateException -> 0x002a }
        L_0x001a:
            r0 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;
            r0 = r0.outputStream;
            if (r0 != 0) goto L_0x0038;
        L_0x0022:
            return;
        L_0x0023:
            r0 = move-exception;
            r1 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;
            r1.handleXMPPException(r0);
            goto L_0x0022;
        L_0x002a:
            r0 = move-exception;
            r1 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;
            r2 = org.jivesoftware.smackx.filetransfer.FileTransfer.Status.error;
            r1.setStatus(r2);
            r1 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;
            r1.setException(r0);
            goto L_0x001a;
        L_0x0038:
            r0 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;
            r1 = org.jivesoftware.smackx.filetransfer.FileTransfer.Status.negotiated;
            r2 = org.jivesoftware.smackx.filetransfer.FileTransfer.Status.in_progress;
            r0 = r0.updateStatus(r1, r2);
            if (r0 == 0) goto L_0x0022;
        L_0x0044:
            r0 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;	 Catch:{ XMPPException -> 0x0076, IllegalStateException -> 0x00a1, all -> 0x00cc }
            r1 = r6.val$in;	 Catch:{ XMPPException -> 0x0076, IllegalStateException -> 0x00a1, all -> 0x00cc }
            r2 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;	 Catch:{ XMPPException -> 0x0076, IllegalStateException -> 0x00a1, all -> 0x00cc }
            r2 = r2.outputStream;	 Catch:{ XMPPException -> 0x0076, IllegalStateException -> 0x00a1, all -> 0x00cc }
            r0.writeToStream(r1, r2);	 Catch:{ XMPPException -> 0x0076, IllegalStateException -> 0x00a1, all -> 0x00cc }
            r0 = r6.val$in;	 Catch:{ IOException -> 0x00eb }
            if (r0 == 0) goto L_0x005a;	 Catch:{ IOException -> 0x00eb }
        L_0x0055:
            r0 = r6.val$in;	 Catch:{ IOException -> 0x00eb }
            r0.close();	 Catch:{ IOException -> 0x00eb }
        L_0x005a:
            r0 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;	 Catch:{ IOException -> 0x00eb }
            r0 = r0.outputStream;	 Catch:{ IOException -> 0x00eb }
            r0.flush();	 Catch:{ IOException -> 0x00eb }
            r0 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;	 Catch:{ IOException -> 0x00eb }
            r0 = r0.outputStream;	 Catch:{ IOException -> 0x00eb }
            r0.close();	 Catch:{ IOException -> 0x00eb }
        L_0x006c:
            r0 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;
            r1 = org.jivesoftware.smackx.filetransfer.FileTransfer.Status.in_progress;
            r2 = org.jivesoftware.smackx.filetransfer.FileTransfer.Status.complete;
            r0.updateStatus(r1, r2);
            goto L_0x0022;
        L_0x0076:
            r0 = move-exception;
            r1 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;	 Catch:{ XMPPException -> 0x0076, IllegalStateException -> 0x00a1, all -> 0x00cc }
            r2 = org.jivesoftware.smackx.filetransfer.FileTransfer.Status.error;	 Catch:{ XMPPException -> 0x0076, IllegalStateException -> 0x00a1, all -> 0x00cc }
            r1.setStatus(r2);	 Catch:{ XMPPException -> 0x0076, IllegalStateException -> 0x00a1, all -> 0x00cc }
            r1 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;	 Catch:{ XMPPException -> 0x0076, IllegalStateException -> 0x00a1, all -> 0x00cc }
            r1.setException(r0);	 Catch:{ XMPPException -> 0x0076, IllegalStateException -> 0x00a1, all -> 0x00cc }
            r0 = r6.val$in;
            if (r0 == 0) goto L_0x008c;
        L_0x0087:
            r0 = r6.val$in;
            r0.close();
        L_0x008c:
            r0 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;
            r0 = r0.outputStream;
            r0.flush();
            r0 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;
            r0 = r0.outputStream;
            r0.close();
            goto L_0x006c;
        L_0x009f:
            r0 = move-exception;
            goto L_0x006c;
        L_0x00a1:
            r0 = move-exception;
            r1 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;	 Catch:{ XMPPException -> 0x0076, IllegalStateException -> 0x00a1, all -> 0x00cc }
            r2 = org.jivesoftware.smackx.filetransfer.FileTransfer.Status.error;	 Catch:{ XMPPException -> 0x0076, IllegalStateException -> 0x00a1, all -> 0x00cc }
            r1.setStatus(r2);	 Catch:{ XMPPException -> 0x0076, IllegalStateException -> 0x00a1, all -> 0x00cc }
            r1 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;	 Catch:{ XMPPException -> 0x0076, IllegalStateException -> 0x00a1, all -> 0x00cc }
            r1.setException(r0);	 Catch:{ XMPPException -> 0x0076, IllegalStateException -> 0x00a1, all -> 0x00cc }
            r0 = r6.val$in;	 Catch:{ IOException -> 0x00ca }
            if (r0 == 0) goto L_0x00b7;	 Catch:{ IOException -> 0x00ca }
        L_0x00b2:
            r0 = r6.val$in;	 Catch:{ IOException -> 0x00ca }
            r0.close();	 Catch:{ IOException -> 0x00ca }
        L_0x00b7:
            r0 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;	 Catch:{ IOException -> 0x00ca }
            r0 = r0.outputStream;	 Catch:{ IOException -> 0x00ca }
            r0.flush();	 Catch:{ IOException -> 0x00ca }
            r0 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;	 Catch:{ IOException -> 0x00ca }
            r0 = r0.outputStream;	 Catch:{ IOException -> 0x00ca }
            r0.close();	 Catch:{ IOException -> 0x00ca }
            goto L_0x006c;
        L_0x00ca:
            r0 = move-exception;
            goto L_0x006c;
        L_0x00cc:
            r0 = move-exception;
            r1 = r6.val$in;	 Catch:{ IOException -> 0x00e9 }
            if (r1 == 0) goto L_0x00d6;	 Catch:{ IOException -> 0x00e9 }
        L_0x00d1:
            r1 = r6.val$in;	 Catch:{ IOException -> 0x00e9 }
            r1.close();	 Catch:{ IOException -> 0x00e9 }
        L_0x00d6:
            r1 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;	 Catch:{ IOException -> 0x00e9 }
            r1 = r1.outputStream;	 Catch:{ IOException -> 0x00e9 }
            r1.flush();	 Catch:{ IOException -> 0x00e9 }
            r1 = org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.this;	 Catch:{ IOException -> 0x00e9 }
            r1 = r1.outputStream;	 Catch:{ IOException -> 0x00e9 }
            r1.close();	 Catch:{ IOException -> 0x00e9 }
        L_0x00e8:
            throw r0;
        L_0x00e9:
            r1 = move-exception;
            goto L_0x00e8;
        L_0x00eb:
            r0 = move-exception;
            goto L_0x006c;
            */
            throw new UnsupportedOperationException("Method not decompiled: org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer.3.run():void");
        }

        C09823(String str, long j, String str2, InputStream inputStream) {
            this.val$fileName = str;
            this.val$fileSize = j;
            this.val$description = str2;
            this.val$in = inputStream;
        }
    }

    public interface NegotiationProgress {
        void errorEstablishingStream(Exception exception);

        void outputStreamEstablished(OutputStream outputStream);

        void statusUpdated(Status status, Status status2);
    }

    static {
        RESPONSE_TIMEOUT = 60000;
    }

    public static int getResponseTimeout() {
        return RESPONSE_TIMEOUT;
    }

    public static void setResponseTimeout(int i) {
        RESPONSE_TIMEOUT = i;
    }

    protected OutgoingFileTransfer(String str, String str2, String str3, FileTransferNegotiator fileTransferNegotiator) {
        super(str2, str3, fileTransferNegotiator);
        this.initiator = str;
    }

    protected void setOutputStream(OutputStream outputStream) {
        if (this.outputStream == null) {
            this.outputStream = outputStream;
        }
    }

    protected OutputStream getOutputStream() {
        if (getStatus().equals(Status.negotiated)) {
            return this.outputStream;
        }
        return null;
    }

    public synchronized OutputStream sendFile(String str, long j, String str2) throws XMPPException {
        if (isDone() || this.outputStream != null) {
            throw new IllegalStateException("The negotation process has already been attempted on this file transfer");
        }
        try {
            setFileInfo(str, j);
            this.outputStream = negotiateStream(str, j, str2);
        } catch (XMPPException e) {
            handleXMPPException(e);
            throw e;
        }
        return this.outputStream;
    }

    public synchronized void sendFile(String str, long j, String str2, NegotiationProgress negotiationProgress) {
        if (negotiationProgress == null) {
            throw new IllegalArgumentException("Callback progress cannot be null.");
        }
        checkTransferThread();
        if (isDone() || this.outputStream != null) {
            throw new IllegalStateException("The negotation process has already been attempted for this file transfer");
        }
        setFileInfo(str, j);
        this.callback = negotiationProgress;
        this.transferThread = new Thread(new C09801(str, j, str2, negotiationProgress), "File Transfer Negotiation " + this.streamID);
        this.transferThread.start();
    }

    private void checkTransferThread() {
        if ((this.transferThread != null && this.transferThread.isAlive()) || isDone()) {
            throw new IllegalStateException("File transfer in progress or has already completed.");
        }
    }

    public synchronized void sendFile(File file, String str) throws XMPPException {
        checkTransferThread();
        if (file != null && file.exists() && file.canRead()) {
            setFileInfo(file.getAbsolutePath(), file.getName(), file.length());
            this.transferThread = new Thread(new C09812(file, str), "File Transfer " + this.streamID);
            this.transferThread.start();
        } else {
            throw new IllegalArgumentException("Could not read file");
        }
    }

    public synchronized void sendStream(InputStream inputStream, String str, long j, String str2) {
        checkTransferThread();
        setFileInfo(str, j);
        this.transferThread = new Thread(new C09823(str, j, str2, inputStream), "File Transfer " + this.streamID);
        this.transferThread.start();
    }

    private void handleXMPPException(XMPPException xMPPException) {
        XMPPError xMPPError = xMPPException.getXMPPError();
        if (xMPPError != null) {
            int code = xMPPError.getCode();
            if (code == 403) {
                setStatus(Status.refused);
                return;
            } else if (code == MyHttpException.ERROR_PARAMETER) {
                setStatus(Status.error);
                setError(Error.not_acceptable);
            } else {
                setStatus(Status.error);
            }
        }
        setException(xMPPException);
    }

    public long getBytesSent() {
        return this.amountWritten;
    }

    private OutputStream negotiateStream(String str, long j, String str2) throws XMPPException {
        if (updateStatus(Status.initial, Status.negotiating_transfer)) {
            StreamNegotiator negotiateOutgoingTransfer = this.negotiator.negotiateOutgoingTransfer(getPeer(), this.streamID, str, j, str2, RESPONSE_TIMEOUT);
            if (negotiateOutgoingTransfer == null) {
                setStatus(Status.error);
                setError(Error.no_response);
                return null;
            } else if (updateStatus(Status.negotiating_transfer, Status.negotiating_stream)) {
                this.outputStream = negotiateOutgoingTransfer.createOutgoingStream(this.streamID, this.initiator, getPeer());
                if (updateStatus(Status.negotiating_stream, Status.negotiated)) {
                    return this.outputStream;
                }
                throw new XMPPException("Illegal state change");
            } else {
                throw new XMPPException("Illegal state change");
            }
        }
        throw new XMPPException("Illegal state change");
    }

    public void cancel() {
        setStatus(Status.cancelled);
    }

    protected boolean updateStatus(Status status, Status status2) {
        boolean updateStatus = super.updateStatus(status, status2);
        if (this.callback != null && updateStatus) {
            this.callback.statusUpdated(status, status2);
        }
        return updateStatus;
    }

    protected void setStatus(Status status) {
        Status status2 = getStatus();
        super.setStatus(status);
        if (this.callback != null) {
            this.callback.statusUpdated(status2, status);
        }
    }

    protected void setException(Exception exception) {
        super.setException(exception);
        if (this.callback != null) {
            this.callback.errorEstablishingStream(exception);
        }
    }
}
