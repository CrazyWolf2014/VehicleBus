package com.nostra13.universalimageloader.core.download;

import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import java.io.IOException;
import java.io.InputStream;

public class NetworkDeniedImageDownloader implements ImageDownloader {
    private final ImageDownloader wrappedDownloader;

    /* renamed from: com.nostra13.universalimageloader.core.download.NetworkDeniedImageDownloader.1 */
    static /* synthetic */ class C07941 {
        static final /* synthetic */ int[] f1342x4730d1a3;

        static {
            f1342x4730d1a3 = new int[Scheme.values().length];
            try {
                f1342x4730d1a3[Scheme.HTTP.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f1342x4730d1a3[Scheme.HTTPS.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
        }
    }

    public NetworkDeniedImageDownloader(ImageDownloader wrappedDownloader) {
        this.wrappedDownloader = wrappedDownloader;
    }

    public InputStream getStream(String imageUri, Object extra) throws IOException {
        switch (C07941.f1342x4730d1a3[Scheme.ofUri(imageUri).ordinal()]) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                throw new IllegalStateException();
            default:
                return this.wrappedDownloader.getStream(imageUri, extra);
        }
    }
}
