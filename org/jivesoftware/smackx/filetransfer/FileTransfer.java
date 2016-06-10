package org.jivesoftware.smackx.filetransfer;

import java.io.InputStream;
import java.io.OutputStream;
import org.jivesoftware.smack.XMPPException;

public abstract class FileTransfer {
    private static final int BUFFER_SIZE = 8192;
    protected long amountWritten;
    private Error error;
    private Exception exception;
    private String fileName;
    private String filePath;
    private long fileSize;
    protected FileTransferNegotiator negotiator;
    private String peer;
    private Status status;
    private final Object statusMonitor;
    protected String streamID;

    public enum Error {
        none("No error"),
        not_acceptable("The peer did not find any of the provided stream mechanisms acceptable."),
        bad_file("The provided file to transfer does not exist or could not be read."),
        no_response("The remote user did not respond or the connection timed out."),
        connection("An error occured over the socket connected to send the file."),
        stream("An error occured while sending or recieving the file.");
        
        private final String msg;

        private Error(String str) {
            this.msg = str;
        }

        public String getMessage() {
            return this.msg;
        }

        public String toString() {
            return this.msg;
        }
    }

    public enum Status {
        error("Error"),
        initial("Initial"),
        negotiating_transfer("Negotiating Transfer"),
        refused("Refused"),
        negotiating_stream("Negotiating Stream"),
        negotiated("Negotiated"),
        in_progress("In Progress"),
        complete("Complete"),
        cancelled("Cancelled");
        
        private String status;

        private Status(String str) {
            this.status = str;
        }

        public String toString() {
            return this.status;
        }
    }

    public abstract void cancel();

    protected FileTransfer(String str, String str2, FileTransferNegotiator fileTransferNegotiator) {
        this.status = Status.initial;
        this.statusMonitor = new Object();
        this.amountWritten = -1;
        this.peer = str;
        this.streamID = str2;
        this.negotiator = fileTransferNegotiator;
    }

    protected void setFileInfo(String str, long j) {
        this.fileName = str;
        this.fileSize = j;
    }

    protected void setFileInfo(String str, String str2, long j) {
        this.filePath = str;
        this.fileName = str2;
        this.fileSize = j;
    }

    public long getFileSize() {
        return this.fileSize;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public String getPeer() {
        return this.peer;
    }

    public double getProgress() {
        if (this.amountWritten <= 0 || this.fileSize <= 0) {
            return 0.0d;
        }
        return ((double) this.amountWritten) / ((double) this.fileSize);
    }

    public boolean isDone() {
        return this.status == Status.cancelled || this.status == Status.error || this.status == Status.complete || this.status == Status.refused;
    }

    public Status getStatus() {
        return this.status;
    }

    protected void setError(Error error) {
        this.error = error;
    }

    public Error getError() {
        return this.error;
    }

    public Exception getException() {
        return this.exception;
    }

    public String getStreamID() {
        return this.streamID;
    }

    protected void setException(Exception exception) {
        this.exception = exception;
    }

    protected void setStatus(Status status) {
        synchronized (this.statusMonitor) {
            this.status = status;
        }
    }

    protected boolean updateStatus(Status status, Status status2) {
        boolean z;
        synchronized (this.statusMonitor) {
            if (status != this.status) {
                z = false;
            } else {
                this.status = status2;
                z = true;
            }
        }
        return z;
    }

    protected void writeToStream(InputStream inputStream, OutputStream outputStream) throws XMPPException {
        int i = 0;
        byte[] bArr = new byte[BUFFER_SIZE];
        this.amountWritten = 0;
        do {
            try {
                outputStream.write(bArr, 0, i);
                this.amountWritten += (long) i;
                try {
                    i = inputStream.read(bArr);
                    if (i == -1) {
                        break;
                    }
                } catch (Throwable e) {
                    throw new XMPPException("error reading from input stream", e);
                }
            } catch (Throwable e2) {
                throw new XMPPException("error writing to output stream", e2);
            }
        } while (!getStatus().equals(Status.cancelled));
        if (!getStatus().equals(Status.cancelled) && getError() == Error.none && this.amountWritten != this.fileSize) {
            setStatus(Status.error);
            this.error = Error.connection;
        }
    }

    public long getAmountWritten() {
        return this.amountWritten;
    }
}
