package org.jivesoftware.smackx.filetransfer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.filetransfer.FileTransfer.Error;
import org.jivesoftware.smackx.filetransfer.FileTransfer.Status;

public class IncomingFileTransfer extends FileTransfer {
    private InputStream inputStream;
    private FileTransferRequest recieveRequest;

    /* renamed from: org.jivesoftware.smackx.filetransfer.IncomingFileTransfer.1 */
    class C09781 implements Runnable {
        final /* synthetic */ File val$file;

        C09781(File file) {
            this.val$file = file;
        }

        public void run() {
            OutputStream fileOutputStream;
            Exception e;
            Exception exception;
            try {
                IncomingFileTransfer.this.inputStream = IncomingFileTransfer.this.negotiateStream();
                try {
                    fileOutputStream = new FileOutputStream(this.val$file);
                    try {
                        IncomingFileTransfer.this.setStatus(Status.in_progress);
                        IncomingFileTransfer.this.writeToStream(IncomingFileTransfer.this.inputStream, fileOutputStream);
                    } catch (XMPPException e2) {
                        e = e2;
                        IncomingFileTransfer.this.setStatus(Status.error);
                        IncomingFileTransfer.this.setError(Error.stream);
                        IncomingFileTransfer.this.setException(e);
                        if (IncomingFileTransfer.this.getStatus().equals(Status.in_progress)) {
                            IncomingFileTransfer.this.setStatus(Status.complete);
                        }
                        if (IncomingFileTransfer.this.inputStream != null) {
                            try {
                                IncomingFileTransfer.this.inputStream.close();
                            } catch (Throwable th) {
                            }
                        }
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                            } catch (Throwable th2) {
                                return;
                            }
                        }
                    } catch (FileNotFoundException e3) {
                        e = e3;
                        IncomingFileTransfer.this.setStatus(Status.error);
                        IncomingFileTransfer.this.setError(Error.bad_file);
                        IncomingFileTransfer.this.setException(e);
                        if (IncomingFileTransfer.this.getStatus().equals(Status.in_progress)) {
                            IncomingFileTransfer.this.setStatus(Status.complete);
                        }
                        if (IncomingFileTransfer.this.inputStream != null) {
                            IncomingFileTransfer.this.inputStream.close();
                        }
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                        }
                    }
                } catch (Exception e4) {
                    exception = e4;
                    fileOutputStream = null;
                    e = exception;
                    IncomingFileTransfer.this.setStatus(Status.error);
                    IncomingFileTransfer.this.setError(Error.stream);
                    IncomingFileTransfer.this.setException(e);
                    if (IncomingFileTransfer.this.getStatus().equals(Status.in_progress)) {
                        IncomingFileTransfer.this.setStatus(Status.complete);
                    }
                    if (IncomingFileTransfer.this.inputStream != null) {
                        IncomingFileTransfer.this.inputStream.close();
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                } catch (Exception e42) {
                    exception = e42;
                    fileOutputStream = null;
                    e = exception;
                    IncomingFileTransfer.this.setStatus(Status.error);
                    IncomingFileTransfer.this.setError(Error.bad_file);
                    IncomingFileTransfer.this.setException(e);
                    if (IncomingFileTransfer.this.getStatus().equals(Status.in_progress)) {
                        IncomingFileTransfer.this.setStatus(Status.complete);
                    }
                    if (IncomingFileTransfer.this.inputStream != null) {
                        IncomingFileTransfer.this.inputStream.close();
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                }
                if (IncomingFileTransfer.this.getStatus().equals(Status.in_progress)) {
                    IncomingFileTransfer.this.setStatus(Status.complete);
                }
                if (IncomingFileTransfer.this.inputStream != null) {
                    IncomingFileTransfer.this.inputStream.close();
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (XMPPException e5) {
                IncomingFileTransfer.this.handleXMPPException(e5);
            }
        }
    }

    /* renamed from: org.jivesoftware.smackx.filetransfer.IncomingFileTransfer.2 */
    class C09792 implements Callable<InputStream> {
        final /* synthetic */ StreamNegotiator val$streamNegotiator;

        C09792(StreamNegotiator streamNegotiator) {
            this.val$streamNegotiator = streamNegotiator;
        }

        public InputStream call() throws Exception {
            return this.val$streamNegotiator.createIncomingStream(IncomingFileTransfer.this.recieveRequest.getStreamInitiation());
        }
    }

    protected IncomingFileTransfer(FileTransferRequest fileTransferRequest, FileTransferNegotiator fileTransferNegotiator) {
        super(fileTransferRequest.getRequestor(), fileTransferRequest.getStreamID(), fileTransferNegotiator);
        this.recieveRequest = fileTransferRequest;
    }

    public InputStream recieveFile() throws XMPPException {
        if (this.inputStream != null) {
            throw new IllegalStateException("Transfer already negotiated!");
        }
        try {
            this.inputStream = negotiateStream();
            return this.inputStream;
        } catch (Exception e) {
            setException(e);
            throw e;
        }
    }

    public void recieveFile(File file) throws XMPPException {
        if (file != null) {
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (Throwable e) {
                    throw new XMPPException("Could not create file to write too", e);
                }
            }
            if (file.canWrite()) {
                new Thread(new C09781(file), "File Transfer " + this.streamID).start();
                return;
            }
            throw new IllegalArgumentException("Cannot write to provided file");
        }
        throw new IllegalArgumentException("File cannot be null");
    }

    private void handleXMPPException(XMPPException xMPPException) {
        setStatus(Status.error);
        setException(xMPPException);
    }

    private InputStream negotiateStream() throws XMPPException {
        setStatus(Status.negotiating_transfer);
        StreamNegotiator selectStreamNegotiator = this.negotiator.selectStreamNegotiator(this.recieveRequest);
        setStatus(Status.negotiating_stream);
        FutureTask futureTask = new FutureTask(new C09792(selectStreamNegotiator));
        futureTask.run();
        try {
            InputStream inputStream = (InputStream) futureTask.get(15, TimeUnit.SECONDS);
            futureTask.cancel(true);
            setStatus(Status.negotiated);
            return inputStream;
        } catch (Throwable e) {
            throw new XMPPException("Interruption while executing", e);
        } catch (Throwable e2) {
            throw new XMPPException("Error in execution", e2);
        } catch (Throwable e22) {
            throw new XMPPException("Request timed out", e22);
        } catch (Throwable th) {
            futureTask.cancel(true);
        }
    }

    public void cancel() {
        setStatus(Status.cancelled);
    }
}
