package com.cnlaunch.mycar.jni;

public class JniX431File {
    public static final String DSUNIT_DTCS = "dtcs";
    public static final String DSUNIT_TIME = "dstime";
    public static final int DS_TYPE_UNKNOWN = 0;
    public static final int LSX_ERR_ALLOC_MEMORY = -6;
    public static final int LSX_ERR_FILE_NOTFOUND = -3;
    public static final int LSX_ERR_HIGH_FILEVERSION = -2;
    public static final int LSX_ERR_INCORRECT_FORMAT = -4;
    public static final int LSX_ERR_INVALID_PARAMETER = -5;
    public static final int LSX_ERR_LOW_FILEVERSION = -1;
    public static final int LSX_ERR_OK = 0;
    public static final int LSX_FILE_READABLE = 1;
    public static final int LSX_FILE_V2 = 4096;
    public static final int LSX_FILE_V3 = 8192;
    public static final int LSX_FILE_WRITABLE = 2;
    public static final int MAX_DS_COLNUMBER = 300;
    public static final int MODE_READ = 1;
    public static final int MODE_WRITE = 2;
    public static final int PRODUCT_ADM = 19;
    public static final int PRODUCT_CRECORDER = 21;
    public static final int PRODUCT_PCCENTER = 18;
    public static final int PRODUCT_PCLINK = 17;
    public static final int PRODUCT_RECORDER = 20;
    public static final int PRODUCT_UNKNOWN = 0;
    public static final int PRODUCT_X431 = 1;
    public static final int PRODUCT_X431INFINITE = 2;
    public static final int PRODUCT_X431PC = 5;
    public static final int PRODUCT_X431TOOL = 4;
    public static final int PRODUCT_X431TOP = 3;
    public static final int RECORD_DATASTREAM = 2;
    public static final int RECORD_DSBASICS = 32;
    public static final int RECORD_DTC = 1;
    public static final int RECORD_FREEZEFRAME = 8;
    public static final int RECORD_READINESS = 16;
    public static final int RECORD_VERSIONINFO = 4;

    public native int lsx_checkfile(X431String x431String);

    public native int lsx_close(int i);

    public native int lsx_deinit(int i);

    public native int lsx_init();

    public native int lsx_open(int i, X431String x431String, int i2, X431Integer x431Integer);

    public native int lsx_read_autoinfo(int i, LSX_AUTOINFO lsx_autoinfo);

    public native int lsx_read_baseinfo(int i, LSX_BASEINFO lsx_baseinfo);

    public native short lsx_read_fileversion(int i);

    public native int lsx_read_langcode(int i, X431String x431String, X431String x431String2);

    public native int lsx_read_spinfo(int i, LSX_SPINFO lsx_spinfo);

    public native int lsx_read_userinfo(int i, LSX_USERINFO lsx_userinfo);

    public native int lsx_rec_finishnewgroup(int i, String str);

    public native int lsx_rec_readalltype(int i);

    public native int lsx_rec_readds(int i, String[] strArr, int i2);

    public native int lsx_rec_readdscolcount(int i);

    public native int lsx_rec_readdsitemcount(int i);

    public native int lsx_rec_readdsname(int i, String[] strArr, int i2);

    public native int lsx_rec_readdstype(int i, short[] sArr, int i2);

    public native int lsx_rec_readdsunit(int i, String[] strArr, int i2);

    public native int lsx_rec_readdtc(int i, X431String x431String, X431String x431String2, X431String x431String3, X431String x431String4);

    public native int lsx_rec_readdtccount(int i);

    public native int lsx_rec_readdtcinfo(int i, String str, X431String x431String, X431String x431String2, X431String x431String3);

    public native int lsx_rec_readffcolcount(int i);

    public native int lsx_rec_readffitemcount(int i);

    public native int lsx_rec_readfirstdsitem(int i);

    public native int lsx_rec_readfirstdtcitem(int i);

    public native int lsx_rec_readfirstffitem(int i);

    public native int lsx_rec_readfirstitem(int i);

    public native int lsx_rec_readfreezeframe(int i, X431String x431String, String[] strArr, int i2);

    public native int lsx_rec_readgroupcount(int i);

    public native int lsx_rec_readgroupid(int i, int i2);

    public native int lsx_rec_readgroupinfo(int i, X431String x431String, X431String x431String2, X431String x431String3, X431String x431String4, X431String x431String5, X431Integer x431Integer);

    public native int lsx_rec_readitemtype(int i);

    public native int lsx_rec_readlastdsitem(int i);

    public native int lsx_rec_readlastdtcitem(int i);

    public native int lsx_rec_readlastffitem(int i);

    public native int lsx_rec_readlastitem(int i);

    public native int lsx_rec_readnextdtcitem(int i);

    public native int lsx_rec_readnextffitem(int i);

    public native int lsx_rec_readnextitem(int i);

    public native int lsx_rec_readprevdtcitem(int i);

    public native int lsx_rec_readprevffitem(int i);

    public native int lsx_rec_readprevitem(int i);

    public native int lsx_rec_readrelndsitem(int i, int i2);

    public native int lsx_rec_writeds(int i, LSX_STRING[] lsx_stringArr, int i2);

    public native int lsx_rec_writedsbasics(int i, LSX_STRING[] lsx_stringArr, LSX_STRING[] lsx_stringArr2, int[] iArr, int i2);

    public native int lsx_rec_writedtc(int i, String str, LSX_STRING lsx_string, LSX_STRING lsx_string2, String str2);

    public native int lsx_rec_writefreezeframe(int i, String str, LSX_STRING[] lsx_stringArr, LSX_STRING[] lsx_stringArr2, int[] iArr, LSX_STRING[] lsx_stringArr3, int i2);

    public native int lsx_rec_writenewgroup(int i, String str, String str2, String str3, String str4, int i2);

    public native int lsx_rec_writereadiness(int i, LSX_STRING[] lsx_stringArr, LSX_STRING[] lsx_stringArr2, int i2);

    public native int lsx_rec_writevi(int i, LSX_STRING lsx_string);

    public native int lsx_selectreadtextlang(int i, String str);

    public native int lsx_write_autoinfo(int i, LSX_AUTOINFO lsx_autoinfo);

    public native int lsx_write_baseinfo(int i, LSX_BASEINFO lsx_baseinfo);

    public native int lsx_write_spinfo(int i, LSX_SPINFO lsx_spinfo);

    public native int lsx_write_userinfo(int i, LSX_USERINFO lsx_userinfo);

    static {
        System.loadLibrary("x431file");
    }
}
