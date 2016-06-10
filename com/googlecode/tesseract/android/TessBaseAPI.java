package com.googlecode.tesseract.android;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;
import com.googlecode.leptonica.android.Pix;
import com.googlecode.leptonica.android.Pixa;
import com.googlecode.leptonica.android.ReadFile;
import java.io.File;

public class TessBaseAPI {
    public static final int OEM_CUBE_ONLY = 1;
    public static final int OEM_DEFAULT = 3;
    public static final int OEM_TESSERACT_CUBE_COMBINED = 2;
    public static final int OEM_TESSERACT_ONLY = 0;
    public static final String VAR_CHAR_BLACKLIST = "tessedit_char_blacklist";
    public static final String VAR_CHAR_WHITELIST = "tessedit_char_whitelist";
    private int mNativeData;

    public static final class PageIteratorLevel {
        public static final int RIL_BLOCK = 0;
        public static final int RIL_PARA = 1;
        public static final int RIL_SYMBOL = 4;
        public static final int RIL_TEXTLINE = 2;
        public static final int RIL_WORD = 3;
    }

    public static final class PageSegMode {
        public static final int PSM_AUTO = 3;
        public static final int PSM_AUTO_ONLY = 2;
        public static final int PSM_AUTO_OSD = 1;
        public static final int PSM_CIRCLE_WORD = 9;
        public static final int PSM_COUNT = 13;
        public static final int PSM_OSD_ONLY = 0;
        public static final int PSM_SINGLE_BLOCK = 6;
        public static final int PSM_SINGLE_BLOCK_VERT_TEXT = 5;
        public static final int PSM_SINGLE_CHAR = 10;
        public static final int PSM_SINGLE_COLUMN = 4;
        public static final int PSM_SINGLE_LINE = 7;
        public static final int PSM_SINGLE_WORD = 8;
        public static final int PSM_SPARSE_TEXT = 11;
        public static final int PSM_SPARSE_TEXT_OSD = 12;
    }

    private static native void nativeClassInit();

    private native void nativeClear();

    private native void nativeConstruct();

    private native void nativeEnd();

    private native void nativeFinalize();

    private native int nativeGetCharacters();

    private native String nativeGetInitLanguagesAsString();

    private native int nativeGetRegions();

    private native int nativeGetResultIterator();

    private native int nativeGetStrips();

    private native int nativeGetTextlines();

    private native String nativeGetUTF8Text();

    private native int nativeGetWords();

    private native boolean nativeInit(String str, String str2);

    private native boolean nativeInitOem(String str, String str2, int i);

    private native int nativeMeanConfidence();

    private native void nativeSetDebug(boolean z);

    private native void nativeSetImageBytes(byte[] bArr, int i, int i2, int i3, int i4);

    private native void nativeSetImagePix(int i);

    private native void nativeSetPageSegMode(int i);

    private native void nativeSetRectangle(int i, int i2, int i3, int i4);

    private native boolean nativeSetVariable(String str, String str2);

    private native int[] nativeWordConfidences();

    static {
        System.loadLibrary("lept");
        System.loadLibrary("tess");
        nativeClassInit();
    }

    public TessBaseAPI() {
        nativeConstruct();
    }

    protected void finalize() throws Throwable {
        try {
            Log.d("TessBaseAPI.java", "finalize(): NOT calling nativeFinalize() due to premature garbage collection");
        } finally {
            Log.d("TessBaseAPI.java", "finalize(): calling super.finalize()");
            super.finalize();
        }
    }

    public boolean init(String datapath, String language) {
        if (datapath == null) {
            throw new IllegalArgumentException("Data path must not be null!");
        }
        if (!datapath.endsWith(File.separator)) {
            datapath = new StringBuilder(String.valueOf(datapath)).append(File.separator).toString();
        }
        File tessdata = new File(new StringBuilder(String.valueOf(datapath)).append("tessdata").toString());
        if (tessdata.exists() && tessdata.isDirectory()) {
            return nativeInit(datapath, language);
        }
        throw new IllegalArgumentException("Data path must contain subfolder tessdata!");
    }

    public boolean init(String datapath, String language, int ocrEngineMode) {
        if (datapath == null) {
            throw new IllegalArgumentException("Data path must not be null!");
        }
        if (!datapath.endsWith(File.separator)) {
            datapath = new StringBuilder(String.valueOf(datapath)).append(File.separator).toString();
        }
        File tessdata = new File(new StringBuilder(String.valueOf(datapath)).append("tessdata").toString());
        if (tessdata.exists() && tessdata.isDirectory()) {
            return nativeInitOem(datapath, language, ocrEngineMode);
        }
        throw new IllegalArgumentException("Data path must contain subfolder tessdata!");
    }

    public String getInitLanguagesAsString() {
        return nativeGetInitLanguagesAsString();
    }

    public void clear() {
        nativeClear();
    }

    public void end() {
        nativeEnd();
    }

    public boolean setVariable(String var, String value) {
        return nativeSetVariable(var, value);
    }

    public void setPageSegMode(int mode) {
        nativeSetPageSegMode(mode);
    }

    public void setDebug(boolean enabled) {
        nativeSetDebug(enabled);
    }

    public void setRectangle(Rect rect) {
        setRectangle(rect.left, rect.top, rect.width(), rect.height());
    }

    public void setRectangle(int left, int top, int width, int height) {
        nativeSetRectangle(left, top, width, height);
    }

    public void setImage(File file) {
        Pix image = ReadFile.readFile(file);
        if (image == null) {
            throw new RuntimeException("Failed to read image file");
        }
        nativeSetImagePix(image.getNativePix());
    }

    public void setImage(Bitmap bmp) {
        Pix image = ReadFile.readBitmap(bmp);
        if (image == null) {
            throw new RuntimeException("Failed to read bitmap");
        }
        nativeSetImagePix(image.getNativePix());
    }

    public void setImage(Pix image) {
        nativeSetImagePix(image.getNativePix());
    }

    public void setImage(byte[] imagedata, int width, int height, int bpp, int bpl) {
        nativeSetImageBytes(imagedata, width, height, bpp, bpl);
    }

    public String getUTF8Text() {
        return nativeGetUTF8Text().trim();
    }

    public int meanConfidence() {
        return nativeMeanConfidence();
    }

    public int[] wordConfidences() {
        int[] conf = nativeWordConfidences();
        if (conf == null) {
            return new int[OEM_TESSERACT_ONLY];
        }
        return conf;
    }

    public Pixa getRegions() {
        return new Pixa(nativeGetRegions(), OEM_TESSERACT_ONLY, OEM_TESSERACT_ONLY);
    }

    public Pixa getTextlines() {
        return new Pixa(nativeGetTextlines(), OEM_TESSERACT_ONLY, OEM_TESSERACT_ONLY);
    }

    public Pixa getStrips() {
        return new Pixa(nativeGetStrips(), OEM_TESSERACT_ONLY, OEM_TESSERACT_ONLY);
    }

    public Pixa getWords() {
        return new Pixa(nativeGetWords(), OEM_TESSERACT_ONLY, OEM_TESSERACT_ONLY);
    }

    @Deprecated
    public Pixa getCharacters() {
        return new Pixa(nativeGetCharacters(), OEM_TESSERACT_ONLY, OEM_TESSERACT_ONLY);
    }

    public ResultIterator getResultIterator() {
        int nativeResultIterator = nativeGetResultIterator();
        if (nativeResultIterator == 0) {
            return null;
        }
        return new ResultIterator(nativeResultIterator);
    }
}
