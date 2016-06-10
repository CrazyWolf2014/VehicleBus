package org.jivesoftware.smack.util;

import android.support.v4.view.MotionEventCompat;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.codehaus.jackson.smile.SmileConstants;
import org.xbill.DNS.Flags;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.TTL;
import org.xbill.DNS.WKSRecord.Service;
import org.xmlpull.v1.XmlPullParser;

public class Base64 {
    public static final int DECODE = 0;
    public static final int DONT_BREAK_LINES = 8;
    public static final int ENCODE = 1;
    private static final byte EQUALS_SIGN = (byte) 61;
    private static final byte EQUALS_SIGN_ENC = (byte) -1;
    public static final int GZIP = 2;
    private static final int MAX_LINE_LENGTH = 76;
    private static final byte NEW_LINE = (byte) 10;
    public static final int NO_OPTIONS = 0;
    public static final int ORDERED = 32;
    private static final String PREFERRED_ENCODING = "UTF-8";
    public static final int URL_SAFE = 16;
    private static final byte WHITE_SPACE_ENC = (byte) -5;
    private static final byte[] _ORDERED_ALPHABET;
    private static final byte[] _ORDERED_DECODABET;
    private static final byte[] _STANDARD_ALPHABET;
    private static final byte[] _STANDARD_DECODABET;
    private static final byte[] _URL_SAFE_ALPHABET;
    private static final byte[] _URL_SAFE_DECODABET;

    public static class InputStream extends FilterInputStream {
        private byte[] alphabet;
        private boolean breakLines;
        private byte[] buffer;
        private int bufferLength;
        private byte[] decodabet;
        private boolean encode;
        private int lineLength;
        private int numSigBytes;
        private int options;
        private int position;

        public InputStream(java.io.InputStream inputStream) {
            this(inputStream, Base64.NO_OPTIONS);
        }

        public InputStream(java.io.InputStream inputStream, int i) {
            boolean z = true;
            super(inputStream);
            this.breakLines = (i & Base64.DONT_BREAK_LINES) != Base64.DONT_BREAK_LINES;
            if ((i & Base64.ENCODE) != Base64.ENCODE) {
                z = false;
            }
            this.encode = z;
            this.bufferLength = this.encode ? 4 : 3;
            this.buffer = new byte[this.bufferLength];
            this.position = -1;
            this.lineLength = Base64.NO_OPTIONS;
            this.options = i;
            this.alphabet = Base64.getAlphabet(i);
            this.decodabet = Base64.getDecodabet(i);
        }

        public int read() throws IOException {
            byte[] bArr;
            if (this.position < 0) {
                int read;
                if (this.encode) {
                    bArr = new byte[3];
                    int i = Base64.NO_OPTIONS;
                    for (int i2 = Base64.NO_OPTIONS; i2 < 3; i2 += Base64.ENCODE) {
                        try {
                            read = this.in.read();
                            if (read >= 0) {
                                bArr[i2] = (byte) read;
                                i += Base64.ENCODE;
                            }
                        } catch (IOException e) {
                            if (i2 == 0) {
                                throw e;
                            }
                        }
                    }
                    if (i <= 0) {
                        return -1;
                    }
                    Base64.encode3to4(bArr, Base64.NO_OPTIONS, i, this.buffer, Base64.NO_OPTIONS, this.options);
                    this.position = Base64.NO_OPTIONS;
                    this.numSigBytes = 4;
                } else {
                    byte[] bArr2 = new byte[4];
                    int i3 = Base64.NO_OPTIONS;
                    while (i3 < 4) {
                        do {
                            read = this.in.read();
                            if (read < 0) {
                                break;
                            }
                        } while (this.decodabet[read & Service.LOCUS_CON] <= -5);
                        if (read < 0) {
                            break;
                        }
                        bArr2[i3] = (byte) read;
                        i3 += Base64.ENCODE;
                    }
                    if (i3 == 4) {
                        this.numSigBytes = Base64.decode4to3(bArr2, Base64.NO_OPTIONS, this.buffer, Base64.NO_OPTIONS, this.options);
                        this.position = Base64.NO_OPTIONS;
                    } else if (i3 == 0) {
                        return -1;
                    } else {
                        throw new IOException("Improperly padded Base64 input.");
                    }
                }
            }
            if (this.position < 0) {
                throw new IOException("Error in Base64 code reading stream.");
            } else if (this.position >= this.numSigBytes) {
                return -1;
            } else {
                if (this.encode && this.breakLines && this.lineLength >= Base64.MAX_LINE_LENGTH) {
                    this.lineLength = Base64.NO_OPTIONS;
                    return 10;
                }
                this.lineLength += Base64.ENCODE;
                bArr = this.buffer;
                int i4 = this.position;
                this.position = i4 + Base64.ENCODE;
                byte b = bArr[i4];
                if (this.position >= this.bufferLength) {
                    this.position = -1;
                }
                return b & KEYRecord.PROTOCOL_ANY;
            }
        }

        public int read(byte[] bArr, int i, int i2) throws IOException {
            int i3 = Base64.NO_OPTIONS;
            while (i3 < i2) {
                int read = read();
                if (read >= 0) {
                    bArr[i + i3] = (byte) read;
                    i3 += Base64.ENCODE;
                } else if (i3 == 0) {
                    return -1;
                } else {
                    return i3;
                }
            }
            return i3;
        }
    }

    public static class OutputStream extends FilterOutputStream {
        private byte[] alphabet;
        private byte[] b4;
        private boolean breakLines;
        private byte[] buffer;
        private int bufferLength;
        private byte[] decodabet;
        private boolean encode;
        private int lineLength;
        private int options;
        private int position;
        private boolean suspendEncoding;

        public OutputStream(java.io.OutputStream outputStream) {
            this(outputStream, Base64.ENCODE);
        }

        public OutputStream(java.io.OutputStream outputStream, int i) {
            int i2;
            boolean z = true;
            super(outputStream);
            this.breakLines = (i & Base64.DONT_BREAK_LINES) != Base64.DONT_BREAK_LINES;
            if ((i & Base64.ENCODE) != Base64.ENCODE) {
                z = false;
            }
            this.encode = z;
            if (this.encode) {
                i2 = 3;
            } else {
                i2 = 4;
            }
            this.bufferLength = i2;
            this.buffer = new byte[this.bufferLength];
            this.position = Base64.NO_OPTIONS;
            this.lineLength = Base64.NO_OPTIONS;
            this.suspendEncoding = false;
            this.b4 = new byte[4];
            this.options = i;
            this.alphabet = Base64.getAlphabet(i);
            this.decodabet = Base64.getDecodabet(i);
        }

        public void write(int i) throws IOException {
            if (this.suspendEncoding) {
                this.out.write(i);
            } else if (this.encode) {
                r0 = this.buffer;
                r1 = this.position;
                this.position = r1 + Base64.ENCODE;
                r0[r1] = (byte) i;
                if (this.position >= this.bufferLength) {
                    this.out.write(Base64.encode3to4(this.b4, this.buffer, this.bufferLength, this.options));
                    this.lineLength += 4;
                    if (this.breakLines && this.lineLength >= Base64.MAX_LINE_LENGTH) {
                        this.out.write(10);
                        this.lineLength = Base64.NO_OPTIONS;
                    }
                    this.position = Base64.NO_OPTIONS;
                }
            } else if (this.decodabet[i & Service.LOCUS_CON] > Base64.WHITE_SPACE_ENC) {
                r0 = this.buffer;
                r1 = this.position;
                this.position = r1 + Base64.ENCODE;
                r0[r1] = (byte) i;
                if (this.position >= this.bufferLength) {
                    this.out.write(this.b4, Base64.NO_OPTIONS, Base64.decode4to3(this.buffer, Base64.NO_OPTIONS, this.b4, Base64.NO_OPTIONS, this.options));
                    this.position = Base64.NO_OPTIONS;
                }
            } else if (this.decodabet[i & Service.LOCUS_CON] != Base64.WHITE_SPACE_ENC) {
                throw new IOException("Invalid character in Base64 data.");
            }
        }

        public void write(byte[] bArr, int i, int i2) throws IOException {
            if (this.suspendEncoding) {
                this.out.write(bArr, i, i2);
                return;
            }
            for (int i3 = Base64.NO_OPTIONS; i3 < i2; i3 += Base64.ENCODE) {
                write(bArr[i + i3]);
            }
        }

        public void flushBase64() throws IOException {
            if (this.position <= 0) {
                return;
            }
            if (this.encode) {
                this.out.write(Base64.encode3to4(this.b4, this.buffer, this.position, this.options));
                this.position = Base64.NO_OPTIONS;
                return;
            }
            throw new IOException("Base64 input not properly padded.");
        }

        public void close() throws IOException {
            flushBase64();
            super.close();
            this.buffer = null;
            this.out = null;
        }

        public void suspendEncoding() throws IOException {
            flushBase64();
            this.suspendEncoding = true;
        }

        public void resumeEncoding() {
            this.suspendEncoding = false;
        }
    }

    static {
        _STANDARD_ALPHABET = new byte[]{(byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90, (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103, (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, (byte) 110, (byte) 111, (byte) 112, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, (byte) 120, (byte) 121, (byte) 122, (byte) 48, (byte) 49, (byte) 50, (byte) 51, SmileConstants.TOKEN_KEY_LONG_STRING, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 43, (byte) 47};
        _STANDARD_DECODABET = new byte[]{(byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, WHITE_SPACE_ENC, WHITE_SPACE_ENC, (byte) -9, (byte) -9, WHITE_SPACE_ENC, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, WHITE_SPACE_ENC, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) 62, (byte) -9, (byte) -9, (byte) -9, (byte) 63, SmileConstants.TOKEN_KEY_LONG_STRING, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, SmileConstants.HEADER_BYTE_1, (byte) 59, (byte) 60, EQUALS_SIGN, (byte) -9, (byte) -9, (byte) -9, EQUALS_SIGN_ENC, (byte) -9, (byte) -9, (byte) -9, (byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9, NEW_LINE, Flags.CD, (byte) 12, (byte) 13, (byte) 14, (byte) 15, (byte) 16, (byte) 17, (byte) 18, (byte) 19, (byte) 20, (byte) 21, (byte) 22, (byte) 23, (byte) 24, (byte) 25, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) 26, (byte) 27, (byte) 28, (byte) 29, (byte) 30, (byte) 31, SmileConstants.TOKEN_LITERAL_EMPTY_STRING, SmileConstants.TOKEN_LITERAL_NULL, SmileConstants.TOKEN_LITERAL_FALSE, SmileConstants.TOKEN_LITERAL_TRUE, (byte) 36, (byte) 37, (byte) 38, (byte) 39, (byte) 40, SmileConstants.HEADER_BYTE_2, (byte) 42, (byte) 43, (byte) 44, (byte) 45, (byte) 46, (byte) 47, (byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) -9, (byte) -9, (byte) -9, (byte) -9};
        _URL_SAFE_ALPHABET = new byte[]{(byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90, (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103, (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, (byte) 110, (byte) 111, (byte) 112, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, (byte) 120, (byte) 121, (byte) 122, (byte) 48, (byte) 49, (byte) 50, (byte) 51, SmileConstants.TOKEN_KEY_LONG_STRING, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 45, (byte) 95};
        _URL_SAFE_DECODABET = new byte[]{(byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, WHITE_SPACE_ENC, WHITE_SPACE_ENC, (byte) -9, (byte) -9, WHITE_SPACE_ENC, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, WHITE_SPACE_ENC, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) 62, (byte) -9, (byte) -9, SmileConstants.TOKEN_KEY_LONG_STRING, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, SmileConstants.HEADER_BYTE_1, (byte) 59, (byte) 60, EQUALS_SIGN, (byte) -9, (byte) -9, (byte) -9, EQUALS_SIGN_ENC, (byte) -9, (byte) -9, (byte) -9, (byte) 0, (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9, NEW_LINE, Flags.CD, (byte) 12, (byte) 13, (byte) 14, (byte) 15, (byte) 16, (byte) 17, (byte) 18, (byte) 19, (byte) 20, (byte) 21, (byte) 22, (byte) 23, (byte) 24, (byte) 25, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) 63, (byte) -9, (byte) 26, (byte) 27, (byte) 28, (byte) 29, (byte) 30, (byte) 31, SmileConstants.TOKEN_LITERAL_EMPTY_STRING, SmileConstants.TOKEN_LITERAL_NULL, SmileConstants.TOKEN_LITERAL_FALSE, SmileConstants.TOKEN_LITERAL_TRUE, (byte) 36, (byte) 37, (byte) 38, (byte) 39, (byte) 40, SmileConstants.HEADER_BYTE_2, (byte) 42, (byte) 43, (byte) 44, (byte) 45, (byte) 46, (byte) 47, (byte) 48, (byte) 49, (byte) 50, (byte) 51, (byte) -9, (byte) -9, (byte) -9, (byte) -9};
        _ORDERED_ALPHABET = new byte[]{(byte) 45, (byte) 48, (byte) 49, (byte) 50, (byte) 51, SmileConstants.TOKEN_KEY_LONG_STRING, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, (byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, (byte) 80, (byte) 81, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90, (byte) 95, (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103, (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, (byte) 110, (byte) 111, (byte) 112, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, (byte) 120, (byte) 121, (byte) 122};
        _ORDERED_DECODABET = new byte[]{(byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, WHITE_SPACE_ENC, WHITE_SPACE_ENC, (byte) -9, (byte) -9, WHITE_SPACE_ENC, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, WHITE_SPACE_ENC, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) 0, (byte) -9, (byte) -9, (byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9, NEW_LINE, (byte) -9, (byte) -9, (byte) -9, EQUALS_SIGN_ENC, (byte) -9, (byte) -9, (byte) -9, Flags.CD, (byte) 12, (byte) 13, (byte) 14, (byte) 15, (byte) 16, (byte) 17, (byte) 18, (byte) 19, (byte) 20, (byte) 21, (byte) 22, (byte) 23, (byte) 24, (byte) 25, (byte) 26, (byte) 27, (byte) 28, (byte) 29, (byte) 30, (byte) 31, SmileConstants.TOKEN_LITERAL_EMPTY_STRING, SmileConstants.TOKEN_LITERAL_NULL, SmileConstants.TOKEN_LITERAL_FALSE, SmileConstants.TOKEN_LITERAL_TRUE, (byte) 36, (byte) -9, (byte) -9, (byte) -9, (byte) -9, (byte) 37, (byte) -9, (byte) 38, (byte) 39, (byte) 40, SmileConstants.HEADER_BYTE_2, (byte) 42, (byte) 43, (byte) 44, (byte) 45, (byte) 46, (byte) 47, (byte) 48, (byte) 49, (byte) 50, (byte) 51, SmileConstants.TOKEN_KEY_LONG_STRING, (byte) 53, (byte) 54, (byte) 55, (byte) 56, (byte) 57, SmileConstants.HEADER_BYTE_1, (byte) 59, (byte) 60, EQUALS_SIGN, (byte) 62, (byte) 63, (byte) -9, (byte) -9, (byte) -9, (byte) -9};
    }

    private static final byte[] getAlphabet(int i) {
        if ((i & URL_SAFE) == URL_SAFE) {
            return _URL_SAFE_ALPHABET;
        }
        if ((i & ORDERED) == ORDERED) {
            return _ORDERED_ALPHABET;
        }
        return _STANDARD_ALPHABET;
    }

    private static final byte[] getDecodabet(int i) {
        if ((i & URL_SAFE) == URL_SAFE) {
            return _URL_SAFE_DECODABET;
        }
        if ((i & ORDERED) == ORDERED) {
            return _ORDERED_DECODABET;
        }
        return _STANDARD_DECODABET;
    }

    private Base64() {
    }

    public static final void main(String[] strArr) {
        if (strArr.length < 3) {
            usage("Not enough arguments.");
            return;
        }
        String str = strArr[NO_OPTIONS];
        String str2 = strArr[ENCODE];
        String str3 = strArr[GZIP];
        if (str.equals("-e")) {
            encodeFileToFile(str2, str3);
        } else if (str.equals("-d")) {
            decodeFileToFile(str2, str3);
        } else {
            usage("Unknown flag: " + str);
        }
    }

    private static final void usage(String str) {
        System.err.println(str);
        System.err.println("Usage: java Base64 -e|-d inputfile outputfile");
    }

    private static byte[] encode3to4(byte[] bArr, byte[] bArr2, int i, int i2) {
        encode3to4(bArr2, NO_OPTIONS, i, bArr, NO_OPTIONS, i2);
        return bArr;
    }

    private static byte[] encode3to4(byte[] bArr, int i, int i2, byte[] bArr2, int i3, int i4) {
        int i5 = NO_OPTIONS;
        byte[] alphabet = getAlphabet(i4);
        int i6 = (i2 > ENCODE ? (bArr[i + ENCODE] << 24) >>> URL_SAFE : NO_OPTIONS) | (i2 > 0 ? (bArr[i] << 24) >>> DONT_BREAK_LINES : NO_OPTIONS);
        if (i2 > GZIP) {
            i5 = (bArr[i + GZIP] << 24) >>> 24;
        }
        i5 |= i6;
        switch (i2) {
            case ENCODE /*1*/:
                bArr2[i3] = alphabet[i5 >>> 18];
                bArr2[i3 + ENCODE] = alphabet[(i5 >>> 12) & 63];
                bArr2[i3 + GZIP] = EQUALS_SIGN;
                bArr2[i3 + 3] = EQUALS_SIGN;
                break;
            case GZIP /*2*/:
                bArr2[i3] = alphabet[i5 >>> 18];
                bArr2[i3 + ENCODE] = alphabet[(i5 >>> 12) & 63];
                bArr2[i3 + GZIP] = alphabet[(i5 >>> 6) & 63];
                bArr2[i3 + 3] = EQUALS_SIGN;
                break;
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                bArr2[i3] = alphabet[i5 >>> 18];
                bArr2[i3 + ENCODE] = alphabet[(i5 >>> 12) & 63];
                bArr2[i3 + GZIP] = alphabet[(i5 >>> 6) & 63];
                bArr2[i3 + 3] = alphabet[i5 & 63];
                break;
        }
        return bArr2;
    }

    public static String encodeObject(Serializable serializable) {
        return encodeObject(serializable, NO_OPTIONS);
    }

    public static String encodeObject(Serializable serializable, int i) {
        java.io.OutputStream outputStream;
        GZIPOutputStream gZIPOutputStream;
        ObjectOutputStream objectOutputStream;
        IOException e;
        Throwable th;
        int i2 = i & GZIP;
        int i3 = i & DONT_BREAK_LINES;
        ByteArrayOutputStream byteArrayOutputStream;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                outputStream = new OutputStream(byteArrayOutputStream, i | ENCODE);
                if (i2 == GZIP) {
                    try {
                        gZIPOutputStream = new GZIPOutputStream(outputStream);
                        try {
                            objectOutputStream = new ObjectOutputStream(gZIPOutputStream);
                        } catch (IOException e2) {
                            e = e2;
                            objectOutputStream = null;
                            try {
                                e.printStackTrace();
                                try {
                                    objectOutputStream.close();
                                } catch (Exception e3) {
                                }
                                try {
                                    gZIPOutputStream.close();
                                } catch (Exception e4) {
                                }
                                try {
                                    outputStream.close();
                                } catch (Exception e5) {
                                }
                                try {
                                    byteArrayOutputStream.close();
                                    return null;
                                } catch (Exception e6) {
                                    return null;
                                }
                            } catch (Throwable th2) {
                                th = th2;
                                try {
                                    objectOutputStream.close();
                                } catch (Exception e7) {
                                }
                                try {
                                    gZIPOutputStream.close();
                                } catch (Exception e8) {
                                }
                                try {
                                    outputStream.close();
                                } catch (Exception e9) {
                                }
                                try {
                                    byteArrayOutputStream.close();
                                } catch (Exception e10) {
                                }
                                throw th;
                            }
                        } catch (Throwable th3) {
                            objectOutputStream = null;
                            th = th3;
                            objectOutputStream.close();
                            gZIPOutputStream.close();
                            outputStream.close();
                            byteArrayOutputStream.close();
                            throw th;
                        }
                    } catch (IOException e11) {
                        e = e11;
                        gZIPOutputStream = null;
                        objectOutputStream = null;
                        e.printStackTrace();
                        objectOutputStream.close();
                        gZIPOutputStream.close();
                        outputStream.close();
                        byteArrayOutputStream.close();
                        return null;
                    } catch (Throwable th32) {
                        gZIPOutputStream = null;
                        objectOutputStream = null;
                        th = th32;
                        objectOutputStream.close();
                        gZIPOutputStream.close();
                        outputStream.close();
                        byteArrayOutputStream.close();
                        throw th;
                    }
                }
                objectOutputStream = new ObjectOutputStream(outputStream);
                gZIPOutputStream = null;
                try {
                    objectOutputStream.writeObject(serializable);
                    try {
                        objectOutputStream.close();
                    } catch (Exception e12) {
                    }
                    try {
                        gZIPOutputStream.close();
                    } catch (Exception e13) {
                    }
                    try {
                        outputStream.close();
                    } catch (Exception e14) {
                    }
                    try {
                        byteArrayOutputStream.close();
                    } catch (Exception e15) {
                    }
                    try {
                        return new String(byteArrayOutputStream.toByteArray(), PREFERRED_ENCODING);
                    } catch (UnsupportedEncodingException e16) {
                        return new String(byteArrayOutputStream.toByteArray());
                    }
                } catch (IOException e17) {
                    e = e17;
                    e.printStackTrace();
                    objectOutputStream.close();
                    gZIPOutputStream.close();
                    outputStream.close();
                    byteArrayOutputStream.close();
                    return null;
                }
            } catch (IOException e18) {
                e = e18;
                gZIPOutputStream = null;
                objectOutputStream = null;
                outputStream = null;
                e.printStackTrace();
                objectOutputStream.close();
                gZIPOutputStream.close();
                outputStream.close();
                byteArrayOutputStream.close();
                return null;
            } catch (Throwable th322) {
                gZIPOutputStream = null;
                objectOutputStream = null;
                outputStream = null;
                th = th322;
                objectOutputStream.close();
                gZIPOutputStream.close();
                outputStream.close();
                byteArrayOutputStream.close();
                throw th;
            }
        } catch (IOException e19) {
            e = e19;
            gZIPOutputStream = null;
            objectOutputStream = null;
            outputStream = null;
            byteArrayOutputStream = null;
            e.printStackTrace();
            objectOutputStream.close();
            gZIPOutputStream.close();
            outputStream.close();
            byteArrayOutputStream.close();
            return null;
        } catch (Throwable th3222) {
            gZIPOutputStream = null;
            objectOutputStream = null;
            outputStream = null;
            byteArrayOutputStream = null;
            th = th3222;
            objectOutputStream.close();
            gZIPOutputStream.close();
            outputStream.close();
            byteArrayOutputStream.close();
            throw th;
        }
    }

    public static String encodeBytes(byte[] bArr) {
        return encodeBytes(bArr, NO_OPTIONS, bArr.length, NO_OPTIONS);
    }

    public static String encodeBytes(byte[] bArr, int i) {
        return encodeBytes(bArr, NO_OPTIONS, bArr.length, i);
    }

    public static String encodeBytes(byte[] bArr, int i, int i2) {
        return encodeBytes(bArr, i, i2, NO_OPTIONS);
    }

    public static String encodeBytes(byte[] bArr, int i, int i2, int i3) {
        ByteArrayOutputStream byteArrayOutputStream;
        OutputStream outputStream;
        GZIPOutputStream gZIPOutputStream;
        IOException e;
        Throwable th;
        int i4 = i3 & DONT_BREAK_LINES;
        if ((i3 & GZIP) == GZIP) {
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
                try {
                    outputStream = new OutputStream(byteArrayOutputStream, i3 | ENCODE);
                    try {
                        gZIPOutputStream = new GZIPOutputStream(outputStream);
                        try {
                            gZIPOutputStream.write(bArr, i, i2);
                            gZIPOutputStream.close();
                            try {
                                gZIPOutputStream.close();
                            } catch (Exception e2) {
                            }
                            try {
                                outputStream.close();
                            } catch (Exception e3) {
                            }
                            try {
                                byteArrayOutputStream.close();
                            } catch (Exception e4) {
                            }
                            try {
                                return new String(byteArrayOutputStream.toByteArray(), PREFERRED_ENCODING);
                            } catch (UnsupportedEncodingException e5) {
                                return new String(byteArrayOutputStream.toByteArray());
                            }
                        } catch (IOException e6) {
                            e = e6;
                            try {
                                e.printStackTrace();
                                try {
                                    gZIPOutputStream.close();
                                } catch (Exception e7) {
                                }
                                try {
                                    outputStream.close();
                                } catch (Exception e8) {
                                }
                                try {
                                    byteArrayOutputStream.close();
                                    return null;
                                } catch (Exception e9) {
                                    return null;
                                }
                            } catch (Throwable th2) {
                                th = th2;
                                try {
                                    gZIPOutputStream.close();
                                } catch (Exception e10) {
                                }
                                try {
                                    outputStream.close();
                                } catch (Exception e11) {
                                }
                                try {
                                    byteArrayOutputStream.close();
                                } catch (Exception e12) {
                                }
                                throw th;
                            }
                        }
                    } catch (IOException e13) {
                        e = e13;
                        gZIPOutputStream = null;
                        e.printStackTrace();
                        gZIPOutputStream.close();
                        outputStream.close();
                        byteArrayOutputStream.close();
                        return null;
                    } catch (Throwable th3) {
                        gZIPOutputStream = null;
                        th = th3;
                        gZIPOutputStream.close();
                        outputStream.close();
                        byteArrayOutputStream.close();
                        throw th;
                    }
                } catch (IOException e14) {
                    e = e14;
                    outputStream = null;
                    gZIPOutputStream = null;
                    e.printStackTrace();
                    gZIPOutputStream.close();
                    outputStream.close();
                    byteArrayOutputStream.close();
                    return null;
                } catch (Throwable th32) {
                    outputStream = null;
                    gZIPOutputStream = null;
                    th = th32;
                    gZIPOutputStream.close();
                    outputStream.close();
                    byteArrayOutputStream.close();
                    throw th;
                }
            } catch (IOException e15) {
                e = e15;
                outputStream = null;
                gZIPOutputStream = null;
                byteArrayOutputStream = null;
                e.printStackTrace();
                gZIPOutputStream.close();
                outputStream.close();
                byteArrayOutputStream.close();
                return null;
            } catch (Throwable th322) {
                outputStream = null;
                gZIPOutputStream = null;
                byteArrayOutputStream = null;
                th = th322;
                gZIPOutputStream.close();
                outputStream.close();
                byteArrayOutputStream.close();
                throw th;
            }
        }
        int i5;
        int i6 = i4 == 0 ? ENCODE : NO_OPTIONS;
        i4 = (i2 * 4) / 3;
        if (i2 % 3 > 0) {
            i5 = 4;
        } else {
            i5 = NO_OPTIONS;
        }
        int i7 = i4 + i5;
        if (i6 != 0) {
            i5 = i4 / MAX_LINE_LENGTH;
        } else {
            i5 = NO_OPTIONS;
        }
        byte[] bArr2 = new byte[(i5 + i7)];
        int i8 = i2 - 2;
        int i9 = NO_OPTIONS;
        int i10 = NO_OPTIONS;
        int i11 = NO_OPTIONS;
        while (i11 < i8) {
            encode3to4(bArr, i11 + i, 3, bArr2, i10, i3);
            i5 = i9 + 4;
            if (i6 != 0 && i5 == MAX_LINE_LENGTH) {
                bArr2[i10 + 4] = NEW_LINE;
                i10 += ENCODE;
                i5 = NO_OPTIONS;
            }
            i10 += 4;
            i9 = i5;
            i11 += 3;
        }
        if (i11 < i2) {
            encode3to4(bArr, i11 + i, i2 - i11, bArr2, i10, i3);
            i10 += 4;
        }
        try {
            return new String(bArr2, NO_OPTIONS, i10, PREFERRED_ENCODING);
        } catch (UnsupportedEncodingException e16) {
            return new String(bArr2, NO_OPTIONS, i10);
        }
    }

    private static int decode4to3(byte[] bArr, int i, byte[] bArr2, int i2, int i3) {
        byte[] decodabet = getDecodabet(i3);
        if (bArr[i + GZIP] == EQUALS_SIGN) {
            bArr2[i2] = (byte) ((((decodabet[bArr[i + ENCODE]] & KEYRecord.PROTOCOL_ANY) << 12) | ((decodabet[bArr[i]] & KEYRecord.PROTOCOL_ANY) << 18)) >>> URL_SAFE);
            return ENCODE;
        } else if (bArr[i + 3] == EQUALS_SIGN) {
            int i4 = ((decodabet[bArr[i + GZIP]] & KEYRecord.PROTOCOL_ANY) << 6) | (((decodabet[bArr[i]] & KEYRecord.PROTOCOL_ANY) << 18) | ((decodabet[bArr[i + ENCODE]] & KEYRecord.PROTOCOL_ANY) << 12));
            bArr2[i2] = (byte) (i4 >>> URL_SAFE);
            bArr2[i2 + ENCODE] = (byte) (i4 >>> DONT_BREAK_LINES);
            return GZIP;
        } else {
            try {
                int i5 = ((((decodabet[bArr[i]] & KEYRecord.PROTOCOL_ANY) << 18) | ((decodabet[bArr[i + ENCODE]] & KEYRecord.PROTOCOL_ANY) << 12)) | ((decodabet[bArr[i + GZIP]] & KEYRecord.PROTOCOL_ANY) << 6)) | (decodabet[bArr[i + 3]] & KEYRecord.PROTOCOL_ANY);
                bArr2[i2] = (byte) (i5 >> URL_SAFE);
                bArr2[i2 + ENCODE] = (byte) (i5 >> DONT_BREAK_LINES);
                bArr2[i2 + GZIP] = (byte) i5;
                return 3;
            } catch (Exception e) {
                System.out.println(XmlPullParser.NO_NAMESPACE + bArr[i] + ": " + decodabet[bArr[i]]);
                System.out.println(XmlPullParser.NO_NAMESPACE + bArr[i + ENCODE] + ": " + decodabet[bArr[i + ENCODE]]);
                System.out.println(XmlPullParser.NO_NAMESPACE + bArr[i + GZIP] + ": " + decodabet[bArr[i + GZIP]]);
                System.out.println(XmlPullParser.NO_NAMESPACE + bArr[i + 3] + ": " + decodabet[bArr[i + 3]]);
                return -1;
            }
        }
    }

    public static byte[] decode(byte[] bArr, int i, int i2, int i3) {
        int i4;
        byte[] decodabet = getDecodabet(i3);
        Object obj = new byte[((i2 * 3) / 4)];
        byte[] bArr2 = new byte[4];
        int i5 = i;
        int i6 = NO_OPTIONS;
        int i7 = NO_OPTIONS;
        while (i5 < i + i2) {
            byte b = (byte) (bArr[i5] & Service.LOCUS_CON);
            byte b2 = decodabet[b];
            if (b2 >= -5) {
                if (b2 >= -1) {
                    i4 = i6 + ENCODE;
                    bArr2[i6] = b;
                    if (i4 > 3) {
                        i4 = decode4to3(bArr2, NO_OPTIONS, obj, i7, i3) + i7;
                        if (b == 61) {
                            break;
                        }
                        i6 = i4;
                        i4 = NO_OPTIONS;
                    } else {
                        i6 = i7;
                    }
                } else {
                    i4 = i6;
                    i6 = i7;
                }
                i5 += ENCODE;
                i7 = i6;
                i6 = i4;
            } else {
                System.err.println("Bad Base64 input character at " + i5 + ": " + bArr[i5] + "(decimal)");
                return null;
            }
        }
        i4 = i7;
        Object obj2 = new byte[i4];
        System.arraycopy(obj, NO_OPTIONS, obj2, NO_OPTIONS, i4);
        return obj2;
    }

    public static byte[] decode(String str) {
        return decode(str, NO_OPTIONS);
    }

    public static byte[] decode(String str, int i) {
        byte[] bytes;
        ByteArrayOutputStream byteArrayOutputStream;
        ByteArrayInputStream byteArrayInputStream;
        ByteArrayInputStream byteArrayInputStream2;
        Throwable th;
        GZIPInputStream gZIPInputStream = null;
        try {
            bytes = str.getBytes(PREFERRED_ENCODING);
        } catch (UnsupportedEncodingException e) {
            bytes = str.getBytes();
        }
        bytes = decode(bytes, NO_OPTIONS, bytes.length, i);
        if (bytes != null && bytes.length >= 4 && 35615 == ((bytes[NO_OPTIONS] & KEYRecord.PROTOCOL_ANY) | ((bytes[ENCODE] << DONT_BREAK_LINES) & MotionEventCompat.ACTION_POINTER_INDEX_MASK))) {
            byte[] bArr = new byte[KEYRecord.Flags.FLAG4];
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
                try {
                    byteArrayInputStream = new ByteArrayInputStream(bytes);
                    try {
                        GZIPInputStream gZIPInputStream2 = new GZIPInputStream(byteArrayInputStream);
                        while (true) {
                            try {
                                int read = gZIPInputStream2.read(bArr);
                                if (read < 0) {
                                    break;
                                }
                                byteArrayOutputStream.write(bArr, NO_OPTIONS, read);
                            } catch (IOException e2) {
                                gZIPInputStream = gZIPInputStream2;
                                byteArrayInputStream2 = byteArrayInputStream;
                            } catch (Throwable th2) {
                                th = th2;
                                gZIPInputStream = gZIPInputStream2;
                            }
                        }
                        bytes = byteArrayOutputStream.toByteArray();
                        try {
                            byteArrayOutputStream.close();
                        } catch (Exception e3) {
                        }
                        try {
                            gZIPInputStream2.close();
                        } catch (Exception e4) {
                        }
                        try {
                            byteArrayInputStream.close();
                        } catch (Exception e5) {
                        }
                    } catch (IOException e6) {
                        byteArrayInputStream2 = byteArrayInputStream;
                        try {
                            byteArrayOutputStream.close();
                        } catch (Exception e7) {
                        }
                        try {
                            gZIPInputStream.close();
                        } catch (Exception e8) {
                        }
                        try {
                            byteArrayInputStream2.close();
                        } catch (Exception e9) {
                        }
                        return bytes;
                    } catch (Throwable th3) {
                        th = th3;
                        try {
                            byteArrayOutputStream.close();
                        } catch (Exception e10) {
                        }
                        try {
                            gZIPInputStream.close();
                        } catch (Exception e11) {
                        }
                        try {
                            byteArrayInputStream.close();
                        } catch (Exception e12) {
                        }
                        throw th;
                    }
                } catch (IOException e13) {
                    byteArrayInputStream2 = null;
                    byteArrayOutputStream.close();
                    gZIPInputStream.close();
                    byteArrayInputStream2.close();
                    return bytes;
                } catch (Throwable th4) {
                    th = th4;
                    byteArrayInputStream = null;
                    byteArrayOutputStream.close();
                    gZIPInputStream.close();
                    byteArrayInputStream.close();
                    throw th;
                }
            } catch (IOException e14) {
                byteArrayOutputStream = null;
                byteArrayInputStream2 = null;
                byteArrayOutputStream.close();
                gZIPInputStream.close();
                byteArrayInputStream2.close();
                return bytes;
            } catch (Throwable th5) {
                th = th5;
                byteArrayOutputStream = null;
                byteArrayInputStream = null;
                byteArrayOutputStream.close();
                gZIPInputStream.close();
                byteArrayInputStream.close();
                throw th;
            }
        }
        return bytes;
    }

    public static Object decodeToObject(String str) {
        ByteArrayInputStream byteArrayInputStream;
        ObjectInputStream objectInputStream;
        Object readObject;
        IOException e;
        Throwable th;
        ClassNotFoundException e2;
        java.io.InputStream inputStream;
        java.io.InputStream inputStream2 = null;
        try {
            byteArrayInputStream = new ByteArrayInputStream(decode(str));
            try {
                objectInputStream = new ObjectInputStream(byteArrayInputStream);
                try {
                    readObject = objectInputStream.readObject();
                    try {
                        byteArrayInputStream.close();
                    } catch (Exception e3) {
                    }
                    try {
                        objectInputStream.close();
                    } catch (Exception e4) {
                    }
                } catch (IOException e5) {
                    e = e5;
                    try {
                        e.printStackTrace();
                        try {
                            byteArrayInputStream.close();
                        } catch (Exception e6) {
                        }
                        try {
                            objectInputStream.close();
                        } catch (Exception e7) {
                        }
                        return readObject;
                    } catch (Throwable th2) {
                        th = th2;
                        try {
                            byteArrayInputStream.close();
                        } catch (Exception e8) {
                        }
                        try {
                            objectInputStream.close();
                        } catch (Exception e9) {
                        }
                        throw th;
                    }
                } catch (ClassNotFoundException e10) {
                    e2 = e10;
                    e2.printStackTrace();
                    try {
                        byteArrayInputStream.close();
                    } catch (Exception e11) {
                    }
                    try {
                        objectInputStream.close();
                    } catch (Exception e12) {
                    }
                    return readObject;
                }
            } catch (IOException e13) {
                e = e13;
                inputStream = inputStream2;
                e.printStackTrace();
                byteArrayInputStream.close();
                objectInputStream.close();
                return readObject;
            } catch (ClassNotFoundException e14) {
                e2 = e14;
                inputStream = inputStream2;
                e2.printStackTrace();
                byteArrayInputStream.close();
                objectInputStream.close();
                return readObject;
            } catch (Throwable th3) {
                inputStream = inputStream2;
                th = th3;
                byteArrayInputStream.close();
                objectInputStream.close();
                throw th;
            }
        } catch (IOException e15) {
            e = e15;
            objectInputStream = inputStream2;
            byteArrayInputStream = inputStream2;
            e.printStackTrace();
            byteArrayInputStream.close();
            objectInputStream.close();
            return readObject;
        } catch (ClassNotFoundException e16) {
            e2 = e16;
            objectInputStream = inputStream2;
            byteArrayInputStream = inputStream2;
            e2.printStackTrace();
            byteArrayInputStream.close();
            objectInputStream.close();
            return readObject;
        } catch (Throwable th32) {
            objectInputStream = inputStream2;
            byteArrayInputStream = inputStream2;
            th = th32;
            byteArrayInputStream.close();
            objectInputStream.close();
            throw th;
        }
        return readObject;
    }

    public static boolean encodeToFile(byte[] bArr, String str) {
        OutputStream outputStream;
        Throwable th;
        boolean z = true;
        OutputStream outputStream2 = null;
        try {
            outputStream = new OutputStream(new FileOutputStream(str), ENCODE);
            try {
                outputStream.write(bArr);
                try {
                    outputStream.close();
                } catch (Exception e) {
                }
            } catch (IOException e2) {
                z = false;
                try {
                    outputStream.close();
                } catch (Exception e3) {
                }
                return z;
            } catch (Throwable th2) {
                th = th2;
                outputStream2 = outputStream;
                try {
                    outputStream2.close();
                } catch (Exception e4) {
                }
                throw th;
            }
        } catch (IOException e5) {
            outputStream = null;
            z = false;
            outputStream.close();
            return z;
        } catch (Throwable th3) {
            th = th3;
            outputStream2.close();
            throw th;
        }
        return z;
    }

    public static boolean decodeToFile(String str, String str2) {
        OutputStream outputStream;
        Throwable th;
        boolean z = false;
        OutputStream outputStream2 = null;
        try {
            outputStream = new OutputStream(new FileOutputStream(str2), NO_OPTIONS);
            try {
                outputStream.write(str.getBytes(PREFERRED_ENCODING));
                z = true;
                try {
                    outputStream.close();
                } catch (Exception e) {
                }
            } catch (IOException e2) {
                try {
                    outputStream.close();
                } catch (Exception e3) {
                }
                return z;
            } catch (Throwable th2) {
                th = th2;
                outputStream2 = outputStream;
                try {
                    outputStream2.close();
                } catch (Exception e4) {
                }
                throw th;
            }
        } catch (IOException e5) {
            outputStream = null;
            outputStream.close();
            return z;
        } catch (Throwable th3) {
            th = th3;
            outputStream2.close();
            throw th;
        }
        return z;
    }

    public static byte[] decodeFromFile(String str) {
        Throwable th;
        byte[] bArr = null;
        int i = NO_OPTIONS;
        InputStream inputStream = null;
        try {
            File file = new File(str);
            if (file.length() > TTL.MAX_VALUE) {
                System.err.println("File is too big for this convenience method (" + file.length() + " bytes).");
                try {
                    inputStream.close();
                } catch (Exception e) {
                }
            } else {
                Object obj = new byte[((int) file.length())];
                inputStream = new InputStream(new BufferedInputStream(new FileInputStream(file)), NO_OPTIONS);
                while (true) {
                    try {
                        int read = inputStream.read(obj, i, KEYRecord.Flags.EXTEND);
                        if (read < 0) {
                            break;
                        }
                        i += read;
                    } catch (IOException e2) {
                    }
                }
                bArr = new byte[i];
                System.arraycopy(obj, NO_OPTIONS, bArr, NO_OPTIONS, i);
                try {
                    inputStream.close();
                } catch (Exception e3) {
                }
            }
        } catch (IOException e4) {
            inputStream = null;
            try {
                System.err.println("Error decoding from file " + str);
                try {
                    inputStream.close();
                } catch (Exception e5) {
                }
                return bArr;
            } catch (Throwable th2) {
                th = th2;
                try {
                    inputStream.close();
                } catch (Exception e6) {
                }
                throw th;
            }
        } catch (Throwable th3) {
            Throwable th4 = th3;
            inputStream = null;
            th = th4;
            inputStream.close();
            throw th;
        }
        return bArr;
    }

    public static String encodeFromFile(String str) {
        InputStream inputStream;
        Throwable th;
        InputStream inputStream2 = null;
        try {
            File file = new File(str);
            byte[] bArr = new byte[Math.max((int) (((double) file.length()) * 1.4d), 40)];
            InputStream inputStream3 = new InputStream(new BufferedInputStream(new FileInputStream(file)), ENCODE);
            int i = NO_OPTIONS;
            while (true) {
                try {
                    int read = inputStream3.read(bArr, i, KEYRecord.Flags.EXTEND);
                    if (read >= 0) {
                        i = read + i;
                    } else {
                        String str2 = new String(bArr, NO_OPTIONS, i, PREFERRED_ENCODING);
                        try {
                            inputStream3.close();
                            return str2;
                        } catch (Exception e) {
                            return str2;
                        }
                    }
                } catch (IOException e2) {
                    inputStream = inputStream3;
                } catch (Throwable th2) {
                    th = th2;
                    inputStream2 = inputStream3;
                }
            }
        } catch (IOException e3) {
            inputStream = null;
            try {
                System.err.println("Error encoding from file " + str);
                try {
                    inputStream.close();
                    return null;
                } catch (Exception e4) {
                    return null;
                }
            } catch (Throwable th3) {
                Throwable th4 = th3;
                inputStream2 = inputStream;
                th = th4;
                try {
                    inputStream2.close();
                } catch (Exception e5) {
                }
                throw th;
            }
        } catch (Throwable th5) {
            th = th5;
            inputStream2.close();
            throw th;
        }
    }

    public static void encodeFileToFile(String str, String str2) {
        java.io.OutputStream bufferedOutputStream;
        IOException e;
        Throwable th;
        String encodeFromFile = encodeFromFile(str);
        try {
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(str2));
            try {
                bufferedOutputStream.write(encodeFromFile.getBytes(StringEncodings.US_ASCII));
                try {
                    bufferedOutputStream.close();
                } catch (Exception e2) {
                }
            } catch (IOException e3) {
                e = e3;
                try {
                    e.printStackTrace();
                    try {
                        bufferedOutputStream.close();
                    } catch (Exception e4) {
                    }
                } catch (Throwable th2) {
                    th = th2;
                    try {
                        bufferedOutputStream.close();
                    } catch (Exception e5) {
                    }
                    throw th;
                }
            }
        } catch (IOException e6) {
            e = e6;
            bufferedOutputStream = null;
            e.printStackTrace();
            bufferedOutputStream.close();
        } catch (Throwable th3) {
            th = th3;
            bufferedOutputStream = null;
            bufferedOutputStream.close();
            throw th;
        }
    }

    public static void decodeFileToFile(String str, String str2) {
        java.io.OutputStream bufferedOutputStream;
        IOException e;
        Throwable th;
        byte[] decodeFromFile = decodeFromFile(str);
        java.io.OutputStream outputStream = null;
        try {
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(str2));
            try {
                bufferedOutputStream.write(decodeFromFile);
                try {
                    bufferedOutputStream.close();
                } catch (Exception e2) {
                }
            } catch (IOException e3) {
                e = e3;
                try {
                    e.printStackTrace();
                    try {
                        bufferedOutputStream.close();
                    } catch (Exception e4) {
                    }
                } catch (Throwable th2) {
                    th = th2;
                    outputStream = bufferedOutputStream;
                    try {
                        outputStream.close();
                    } catch (Exception e5) {
                    }
                    throw th;
                }
            }
        } catch (IOException e6) {
            e = e6;
            bufferedOutputStream = null;
            e.printStackTrace();
            bufferedOutputStream.close();
        } catch (Throwable th3) {
            th = th3;
            outputStream.close();
            throw th;
        }
    }
}
