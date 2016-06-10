package org.xbill.DNS;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import org.xbill.DNS.utils.hexdump;

class Client {
    protected long endTime;
    protected SelectionKey key;

    protected Client(SelectableChannel selectableChannel, long j) throws IOException {
        Selector selector = null;
        this.endTime = j;
        try {
            selector = Selector.open();
            selectableChannel.configureBlocking(false);
            this.key = selectableChannel.register(selector, 1);
        } catch (Throwable th) {
            if (selector != null) {
                selector.close();
            }
            selectableChannel.close();
        }
    }

    protected static void blockUntil(SelectionKey selectionKey, long j) throws IOException {
        long currentTimeMillis = j - System.currentTimeMillis();
        int i = 0;
        if (currentTimeMillis > 0) {
            i = selectionKey.selector().select(currentTimeMillis);
        } else if (currentTimeMillis == 0) {
            i = selectionKey.selector().selectNow();
        }
        if (i == 0) {
            throw new SocketTimeoutException();
        }
    }

    protected static void verboseLog(String str, byte[] bArr) {
        if (Options.check("verbosemsg")) {
            System.err.println(hexdump.dump(str, bArr));
        }
    }

    void cleanup() throws IOException {
        this.key.selector().close();
        this.key.channel().close();
    }
}
