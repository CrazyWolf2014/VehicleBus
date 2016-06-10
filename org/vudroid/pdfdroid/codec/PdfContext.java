package org.vudroid.pdfdroid.codec;

import android.content.ContentResolver;
import org.vudroid.core.VuDroidLibraryLoader;
import org.vudroid.core.codec.CodecContext;
import org.vudroid.core.codec.CodecDocument;
import org.xmlpull.v1.XmlPullParser;

public class PdfContext implements CodecContext {
    static {
        VuDroidLibraryLoader.load();
    }

    public CodecDocument openDocument(String fileName) {
        return PdfDocument.openDocument(fileName, XmlPullParser.NO_NAMESPACE);
    }

    public void setContentResolver(ContentResolver contentResolver) {
    }

    public void recycle() {
    }
}
