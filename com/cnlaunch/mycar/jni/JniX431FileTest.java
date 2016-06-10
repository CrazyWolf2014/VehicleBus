package com.cnlaunch.mycar.jni;

import android.util.Log;
import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.cnlaunch.x431pro.common.Constants;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.ifoer.entity.Constant;
import com.ifoer.entity.SptExDataStreamIdItem;
import com.ifoer.entity.SptVwDataStreamIdItem;
import com.tencent.mm.sdk.platformtools.LocaleUtil;
import com.tencent.mm.sdk.platformtools.Util;
import java.io.File;
import java.util.ArrayList;
import junit.framework.TestCase;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.WKSRecord.Protocol;

public class JniX431FileTest extends TestCase {
    private static final int DS_TYPE_1 = 256;
    private static final int DS_TYPE_2 = 257;
    private static final int DS_TYPE_3 = 512;
    private static final int DS_TYPE_4 = 515;
    private static final int DS_TYPE_5 = 517;
    private static final int DS_TYPE_6 = 768;
    private static final int DS_TYPE_7 = 769;
    private static final int DS_TYPE_8 = 787;
    private static final int DS_TYPE_9 = 771;
    private static final String TAG = "JniX431FileTest";
    private static JniX431File mJxf;

    static {
        mJxf = new JniX431File();
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    boolean Write_Userinfo(int file) {
        LSX_USERINFO userinfo = new LSX_USERINFO();
        userinfo.name = "customer A";
        userinfo.phone = "77777777";
        userinfo.license = "123456";
        return mJxf.lsx_write_userinfo(file, userinfo) == 0;
    }

    boolean Write_Baseinfo(int file) {
        LSX_BASEINFO baseinfo = new LSX_BASEINFO();
        baseinfo.productid = (short) 21;
        baseinfo.codepage = 936;
        baseinfo.langname = "Chinese Simplified";
        baseinfo.langcode = "cn";
        baseinfo.langcode_en = Util.ENGLISH;
        baseinfo.diagversion = "Audi V13.00";
        baseinfo.serialno = "980241111111";
        return mJxf.lsx_write_baseinfo(file, baseinfo) == 0;
    }

    boolean Write_Spinfo(int file) {
        LSX_SPINFO spinfo = new LSX_SPINFO();
        spinfo.name = "\u7ecf\u9500\u5546A";
        spinfo.phone = "88888888";
        return mJxf.lsx_write_spinfo(file, spinfo) == 0;
    }

    boolean Write_Autoinfo(int file) {
        LSX_AUTOINFO autoinfo = new LSX_AUTOINFO();
        autoinfo.model = "\u5965\u8fea";
        autoinfo.make = "Volkswagen";
        autoinfo.year = "2006";
        autoinfo.madein = "China";
        autoinfo.chassis = "chassis";
        autoinfo.enginemodel = "engine model";
        autoinfo.displacement = "2.0L";
        autoinfo.vin = "12345678901234567";
        return mJxf.lsx_write_autoinfo(file, autoinfo) == 0;
    }

    boolean Write_Baseinfo_langexttest(int file, String langcode, String langname) {
        LSX_BASEINFO baseinfo = new LSX_BASEINFO();
        baseinfo.productid = (short) 21;
        baseinfo.codepage = -13604;
        baseinfo.langname = langname;
        baseinfo.langcode = langcode;
        baseinfo.langcode_en = Util.ENGLISH;
        baseinfo.diagversion = "Audi V13.00";
        baseinfo.serialno = "980241111111";
        return mJxf.lsx_write_baseinfo(file, baseinfo) == 0;
    }

    boolean Write_Baseinfo_itemtest(int file) {
        LSX_BASEINFO baseinfo = new LSX_BASEINFO();
        baseinfo.productid = (short) 21;
        baseinfo.codepage = 936;
        baseinfo.langname = "Chinese Simplified";
        baseinfo.langcode = "cn";
        baseinfo.langcode_en = Util.ENGLISH;
        baseinfo.diagversion = "Audi V13.00";
        baseinfo.serialno = "980241111111";
        return mJxf.lsx_write_baseinfo(file, baseinfo) == 0;
    }

    boolean Write_Rdn(int grp) {
        LSX_STRING[] namestrs = new LSX_STRING[10];
        LSX_STRING[] textstrs = new LSX_STRING[10];
        for (int i = 0; i < 10; i++) {
            namestrs[i] = new LSX_STRING();
            textstrs[i] = new LSX_STRING();
        }
        namestrs[0].str = "RDN \u540d\u79f01";
        namestrs[0].str_en = "rdn name1";
        namestrs[1].str = "RDN \u540d\u79f02";
        namestrs[1].str_en = "rdn name2";
        namestrs[2].str = "RDN \u540d\u79f03";
        namestrs[2].str_en = "rdn name3";
        namestrs[3].str = "RDN \u540d\u79f04";
        namestrs[3].str_en = "rdn name4";
        namestrs[4].str = "RDN \u540d\u79f05";
        namestrs[4].str_en = "rdn name5";
        namestrs[5].str = "RDN \u540d\u79f06";
        namestrs[5].str_en = "rdn name6";
        namestrs[6].str = "RDN \u540d\u79f07";
        namestrs[6].str_en = "rdn name7";
        textstrs[0].str = "RDN \u6570\u503c1";
        textstrs[0].str_en = "rdn item1";
        textstrs[1].str = "RDN \u6570\u503c2";
        textstrs[1].str_en = "rdn item2";
        textstrs[2].str = "RDN \u6570\u503c3";
        textstrs[2].str_en = "rdn item3";
        textstrs[3].str = "RDN \u6570\u503c4";
        textstrs[3].str_en = "rdn item4";
        textstrs[4].str = "RDN \u6570\u503c5";
        textstrs[4].str_en = "rdn item5";
        textstrs[5].str = "RDN \u6570\u503c6";
        textstrs[5].str_en = "rdn item6";
        textstrs[6].str = "RDN \u6570\u503c7";
        textstrs[6].str_en = "rdn item7";
        if (mJxf.lsx_rec_writereadiness(grp, namestrs, textstrs, 7) == 0) {
            return true;
        }
        return false;
    }

    boolean Write_Dtcs(int grp, String dtc) {
        LSX_STRING state = new LSX_STRING();
        LSX_STRING desc = new LSX_STRING();
        String dtctime = new String();
        state.str = new StringBuilder(String.valueOf(dtc)).append(" \u72b6\u6001").toString();
        state.str_en = new StringBuilder(String.valueOf(dtc)).append(" state").toString();
        desc.str = new StringBuilder(String.valueOf(dtc)).append(" \u63cf\u8ff0").toString();
        desc.str_en = new StringBuilder(String.valueOf(dtc)).append(" desc").toString();
        return mJxf.lsx_rec_writedtc(grp, dtc, state, desc, new StringBuilder(String.valueOf(dtc)).append(" time").toString()) == 0;
    }

    boolean Write_Vi(int grp) {
        LSX_STRING vi = new LSX_STRING();
        vi.str = "\u7248\u672c\u4fe1\u606f1\n\u7248\u672c\u4fe1\u606f2";
        vi.str_en = "vi info1\nvi info2";
        return mJxf.lsx_rec_writevi(grp, vi) == 0;
    }

    boolean Write_DsBasics(int grp) {
        LSX_STRING[] namestrs = new LSX_STRING[10];
        LSX_STRING[] unitstrs = new LSX_STRING[10];
        for (int i = 0; i < 10; i++) {
            namestrs[i] = new LSX_STRING();
            unitstrs[i] = new LSX_STRING();
        }
        int[] type = new int[10];
        namestrs[0].str = "\u6570\u636e\u6d41 \u540d\u79f01";
        namestrs[0].str_en = "ds name1";
        namestrs[1].str = "\u6570\u636e\u6d41 \u540d\u79f02";
        namestrs[1].str_en = "ds name2";
        namestrs[2].str = "\u6570\u636e\u6d41 \u540d\u79f03";
        namestrs[2].str_en = "ds name3";
        namestrs[3].str = "\u6570\u636e\u6d41 \u540d\u79f04";
        namestrs[3].str_en = "ds name4";
        namestrs[4].str = "\u6570\u636e\u6d41 \u540d\u79f05";
        namestrs[4].str_en = "ds name5";
        namestrs[5].str = "\u6570\u636e\u6d41 \u540d\u79f06";
        namestrs[5].str_en = "ds name6";
        namestrs[6].str = "\u6570\u636e\u6d41 \u540d\u79f07";
        namestrs[6].str_en = "ds name7";
        namestrs[7].str = "\u6570\u636e\u6d41 \u540d\u79f08";
        namestrs[7].str_en = "ds name8";
        namestrs[8].str = "\u6570\u636e\u6d41 \u540d\u79f09";
        namestrs[8].str_en = "ds name9";
        unitstrs[0].str = "\u6570\u636e\u6d41 \u5355\u4f4d1";
        unitstrs[0].str_en = "ds unit1";
        unitstrs[1].str = "\u6570\u636e\u6d41 \u5355\u4f4d2";
        unitstrs[1].str_en = "ds unit2";
        unitstrs[2].str = "\u6570\u636e\u6d41 \u5355\u4f4d3";
        unitstrs[2].str_en = "ds unit3";
        unitstrs[3].str = "\u6570\u636e\u6d41 \u5355\u4f4d4";
        unitstrs[3].str_en = "ds unit4";
        unitstrs[4].str = "\u6570\u636e\u6d41 \u5355\u4f4d5";
        unitstrs[4].str_en = "ds unit5";
        unitstrs[5].str = "\u6570\u636e\u6d41 \u5355\u4f4d6";
        unitstrs[5].str_en = "ds unit6";
        unitstrs[6].str = "\u6570\u636e\u6d41 \u5355\u4f4d7";
        unitstrs[6].str_en = "ds unit7";
        unitstrs[7].str = "\u6570\u636e\u6d41 \u5355\u4f4d8";
        unitstrs[7].str_en = "ds unit8";
        unitstrs[8].str = "\u6570\u636e\u6d41 \u5355\u4f4d9";
        unitstrs[8].str_en = "ds unit9";
        type[0] = DS_TYPE_1;
        type[2] = DS_TYPE_3;
        type[4] = DS_TYPE_5;
        type[5] = DS_TYPE_6;
        type[7] = DS_TYPE_8;
        if (mJxf.lsx_rec_writedsbasics(grp, namestrs, unitstrs, type, 9) == 0) {
            return true;
        }
        return false;
    }

    boolean Write_Ds(int grp, int startlineno, int linecount) {
        int i;
        LSX_STRING[] itemstrs = new LSX_STRING[10];
        for (i = 0; i < 10; i++) {
            itemstrs[i] = new LSX_STRING();
        }
        int count = 0;
        do {
            count++;
            if (count > linecount) {
                break;
            }
            for (i = 0; i < 18; i += 2) {
                itemstrs[i / 2].str = "\u6570\u636e\u6d41 \u6570\u503c" + startlineno + "," + ((i / 2) + 1);
                itemstrs[i / 2].str_en = "ds item" + startlineno + "," + ((i / 2) + 1);
            }
            startlineno++;
        } while (mJxf.lsx_rec_writeds(grp, itemstrs, 9) == 0);
        if (count == linecount + 1) {
            return true;
        }
        return false;
    }

    boolean Write_FF(int grp, String dtc) {
        LSX_STRING[] namestrs = new LSX_STRING[5];
        LSX_STRING[] unitstrs = new LSX_STRING[5];
        LSX_STRING[] textstrs = new LSX_STRING[5];
        int[] type = new int[5];
        for (int i = 0; i < 5; i++) {
            namestrs[i] = new LSX_STRING();
            unitstrs[i] = new LSX_STRING();
            textstrs[i] = new LSX_STRING();
        }
        namestrs[0].str = "FF \u540d\u79f01";
        namestrs[0].str_en = "ff name1";
        namestrs[1].str = "FF \u540d\u79f02";
        namestrs[1].str_en = "ff name2";
        namestrs[2].str = "FF \u540d\u79f03";
        namestrs[2].str_en = "ff name3";
        namestrs[3].str = "FF \u540d\u79f04";
        namestrs[3].str_en = "ff name4";
        namestrs[4].str = "FF \u540d\u79f05";
        namestrs[4].str_en = "ff name5";
        unitstrs[0].str = "FF \u5355\u4f4d1";
        unitstrs[0].str_en = "ff unit1";
        unitstrs[1].str = "FF \u5355\u4f4d2";
        unitstrs[1].str_en = "ff unit2";
        unitstrs[2].str = "FF \u5355\u4f4d3";
        unitstrs[2].str_en = "ff unit3";
        unitstrs[3].str = "FF \u5355\u4f4d4";
        unitstrs[3].str_en = "ff unit4";
        unitstrs[4].str = "FF \u5355\u4f4d5";
        unitstrs[4].str_en = "ff unit5";
        type[0] = DS_TYPE_1;
        type[2] = DS_TYPE_3;
        type[4] = DS_TYPE_5;
        textstrs[0].str = "FF \u6570\u503c1";
        textstrs[0].str_en = "ff item1";
        textstrs[1].str = "FF \u6570\u503c2";
        textstrs[1].str_en = "ff item2";
        textstrs[2].str = "FF \u6570\u503c3";
        textstrs[2].str_en = "ff item3";
        textstrs[3].str = "FF \u6570\u503c4";
        textstrs[3].str_en = "ff item4";
        textstrs[4].str = "FF \u6570\u503c5";
        textstrs[4].str_en = "ff item5";
        if (mJxf.lsx_rec_writefreezeframe(grp, dtc, namestrs, unitstrs, type, textstrs, 5) == 0) {
            return true;
        }
        return false;
    }

    public void testlsx_open_lsx_close() throws Throwable {
        boolean z = true;
        int hlsx = mJxf.lsx_init();
        assertNotNull(Integer.valueOf(hlsx));
        String strFileName = FileUtils.sdCardGetDirectoryPath() + File.separator + "v2.x431";
        assertTrue(FileUtils.isFileExist(strFileName));
        X431String filename = new X431String(strFileName);
        X431Integer error = new X431Integer(10);
        int lsx_file = mJxf.lsx_open(hlsx, filename, 2, error);
        assertTrue(lsx_file == 0);
        if (error.mValue != -1) {
            z = false;
        }
        assertTrue(z);
        mJxf.lsx_close(lsx_file);
        mJxf.lsx_deinit(hlsx);
    }

    public void testlsx_checkfile() throws Throwable {
        boolean z;
        boolean z2 = true;
        String strFileName = FileUtils.sdCardGetDirectoryPath() + File.separator + "v2.x431";
        assertTrue(FileUtils.isFileExist(strFileName));
        int iRet = mJxf.lsx_checkfile(new X431String(strFileName));
        int itmp2 = iRet & Flags.EXTEND;
        int itmp3 = iRet & 2;
        if ((iRet & 1) != 0) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        if (itmp2 != 0) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        if (itmp3 != 0) {
            z2 = false;
        }
        assertTrue(z2);
    }

    public void testlsx_read_baseinfo() throws Throwable {
        boolean z;
        boolean z2 = true;
        int hlsx = mJxf.lsx_init();
        assertNotNull(Integer.valueOf(hlsx));
        String strFileName = FileUtils.sdCardGetDirectoryPath() + File.separator + "v2.x431";
        assertTrue(FileUtils.isFileExist(strFileName));
        int lsx_file = mJxf.lsx_open(hlsx, new X431String(strFileName), 1, new X431Integer(10));
        if (lsx_file != 0) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        LSX_BASEINFO baseinfo = new LSX_BASEINFO();
        assertEquals(0, mJxf.lsx_read_baseinfo(lsx_file, baseinfo));
        assertEquals("980241111111", baseinfo.serialno);
        assertEquals(1, baseinfo.productid);
        assertEquals(1252, baseinfo.codepage);
        assertNull(baseinfo.langname);
        assertEquals(Util.ENGLISH, baseinfo.langcode);
        assertNull(baseinfo.langcode_en);
        assertEquals("Audi V13.00", baseinfo.diagversion);
        if (baseinfo.creationtime.length() < 24) {
            z2 = false;
        }
        assertTrue(z2);
        mJxf.lsx_close(lsx_file);
        mJxf.lsx_deinit(hlsx);
    }

    public void testlsx_read_autoinfo() throws Throwable {
        boolean z = true;
        int hlsx = mJxf.lsx_init();
        assertNotNull(Integer.valueOf(hlsx));
        String strFileName = FileUtils.sdCardGetDirectoryPath() + File.separator + "v2.x431";
        assertTrue(FileUtils.isFileExist(strFileName));
        int lsx_file = mJxf.lsx_open(hlsx, new X431String(strFileName), 1, new X431Integer(10));
        if (lsx_file == 0) {
            z = false;
        }
        assertTrue(z);
        LSX_AUTOINFO autoinfo = new LSX_AUTOINFO();
        assertEquals(0, mJxf.lsx_read_autoinfo(lsx_file, autoinfo));
        assertEquals("12345678901234567", autoinfo.vin);
        assertEquals("Volkswagen", autoinfo.make);
        assertEquals("Audi", autoinfo.model);
        assertEquals("2006", autoinfo.year);
        assertEquals("China", autoinfo.madein);
        assertEquals("chassis", autoinfo.chassis);
        assertEquals("engine model", autoinfo.enginemodel);
        assertEquals("2.0L", autoinfo.displacement);
        mJxf.lsx_close(lsx_file);
        mJxf.lsx_deinit(hlsx);
    }

    public void testlsx_read_spinfo() throws Throwable {
        boolean z = true;
        int hlsx = mJxf.lsx_init();
        assertNotNull(Integer.valueOf(hlsx));
        String strFileName = FileUtils.sdCardGetDirectoryPath() + File.separator + "v2.x431";
        assertTrue(FileUtils.isFileExist(strFileName));
        int lsx_file = mJxf.lsx_open(hlsx, new X431String(strFileName), 1, new X431Integer(10));
        if (lsx_file == 0) {
            z = false;
        }
        assertTrue(z);
        LSX_SPINFO spinfo = new LSX_SPINFO();
        assertEquals(0, mJxf.lsx_read_spinfo(lsx_file, spinfo));
        assertEquals("\u7ecf\u9500\u5546A", spinfo.name);
        assertEquals("88888888", spinfo.phone);
        mJxf.lsx_close(lsx_file);
        mJxf.lsx_deinit(hlsx);
    }

    public void testlsx_read_userinfo() throws Throwable {
        boolean z = true;
        int hlsx = mJxf.lsx_init();
        assertNotNull(Integer.valueOf(hlsx));
        String strFileName = FileUtils.sdCardGetDirectoryPath() + File.separator + "v2.x431";
        assertTrue(FileUtils.isFileExist(strFileName));
        int lsx_file = mJxf.lsx_open(hlsx, new X431String(strFileName), 1, new X431Integer(10));
        if (lsx_file == 0) {
            z = false;
        }
        assertTrue(z);
        LSX_USERINFO userinfo = new LSX_USERINFO();
        assertEquals(0, mJxf.lsx_read_userinfo(lsx_file, userinfo));
        assertEquals("customer B", userinfo.name);
        assertEquals("999999999", userinfo.phone);
        assertEquals("654321", userinfo.license);
        mJxf.lsx_close(lsx_file);
        mJxf.lsx_deinit(hlsx);
    }

    public void testlsx_rec_readgroupcount() throws Throwable {
        boolean z = true;
        int hlsx = mJxf.lsx_init();
        assertNotNull(Integer.valueOf(hlsx));
        String strFileName = FileUtils.sdCardGetDirectoryPath() + File.separator + "v2.x431";
        assertTrue(FileUtils.isFileExist(strFileName));
        int lsx_file = mJxf.lsx_open(hlsx, new X431String(strFileName), 1, new X431Integer(10));
        if (lsx_file == 0) {
            z = false;
        }
        assertTrue(z);
        assertEquals(2, mJxf.lsx_rec_readgroupcount(lsx_file));
        mJxf.lsx_close(lsx_file);
        mJxf.lsx_deinit(hlsx);
    }

    public void testlsx_read_fileversion() throws Throwable {
        boolean z;
        boolean z2 = true;
        int hlsx = mJxf.lsx_init();
        assertNotNull(Integer.valueOf(hlsx));
        String strFileName = FileUtils.sdCardGetDirectoryPath() + File.separator + "v2.x431";
        assertTrue(FileUtils.isFileExist(strFileName));
        int lsx_file = mJxf.lsx_open(hlsx, new X431String(strFileName), 1, new X431Integer(10));
        if (lsx_file != 0) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        if ((mJxf.lsx_read_fileversion(lsx_file) & DS_TYPE_3) == 0) {
            z2 = false;
        }
        assertTrue(z2);
        mJxf.lsx_close(lsx_file);
        mJxf.lsx_deinit(hlsx);
    }

    public void testlsx_read_langcode() throws Throwable {
        int hlsx = mJxf.lsx_init();
        assertNotNull(Integer.valueOf(hlsx));
        String strFileName = FileUtils.sdCardGetDirectoryPath() + File.separator + "v2.x431";
        assertTrue(FileUtils.isFileExist(strFileName));
        int lsx_file = mJxf.lsx_open(hlsx, new X431String(strFileName), 1, new X431Integer(10));
        assertTrue(lsx_file != 0);
        X431String code = new X431String();
        X431String code_en = new X431String();
        assertEquals(1, mJxf.lsx_read_langcode(lsx_file, code, code_en));
        assertEquals(Util.ENGLISH, code.mValue);
        assertNull(code_en.mValue);
        mJxf.lsx_close(lsx_file);
        mJxf.lsx_deinit(hlsx);
    }

    public void testV2FileReadDtcDataTest() throws Throwable {
        int hlsx = mJxf.lsx_init();
        assertNotNull(Integer.valueOf(hlsx));
        String strFileName = FileUtils.sdCardGetDirectoryPath() + File.separator + "v2.x431";
        assertTrue(FileUtils.isFileExist(strFileName));
        int lsx_file = mJxf.lsx_open(hlsx, new X431String(strFileName), 1, new X431Integer(10));
        assertTrue(lsx_file != 0);
        int grpcount = mJxf.lsx_rec_readgroupcount(lsx_file);
        assertEquals(2, grpcount);
        X431String dtc = new X431String();
        X431String state = new X431String();
        X431String desc = new X431String();
        X431String time = new X431String();
        for (int i = 1; i < grpcount; i++) {
            int grp = mJxf.lsx_rec_readgroupid(lsx_file, i);
            assertTrue(grp != 0);
            assertEquals(5, mJxf.lsx_rec_readdtccount(grp));
            int item = mJxf.lsx_rec_readfirstdtcitem(grp);
            assertTrue(item != 0);
            int count = 0;
            while (item != 0) {
                assertEquals(0, mJxf.lsx_rec_readdtc(item, dtc, state, desc, time));
                count++;
                if (count == 1) {
                    assertEquals("P1200", dtc.mValue);
                    assertEquals("current", state.mValue);
                    assertEquals("fault 1", desc.mValue);
                    assertEquals("16:05:00", time.mValue);
                } else if (count == 4) {
                    assertEquals("P1300", dtc.mValue);
                    assertEquals("current", state.mValue);
                    assertEquals("fault 3", desc.mValue);
                    assertEquals("16:05:00", time.mValue);
                } else if (count == 5) {
                    assertEquals("P1301", dtc.mValue);
                    assertEquals("current", state.mValue);
                    assertEquals("fault 4", desc.mValue);
                    assertEquals("16:05:00", time.mValue);
                }
                item = mJxf.lsx_rec_readnextdtcitem(item);
            }
            assertEquals(5, count);
            assertEquals(0, mJxf.lsx_rec_readdtcinfo(grp, "P1201", state, desc, time));
            assertEquals("current", state.mValue);
            assertEquals("fault 2", desc.mValue);
            assertEquals("16:05:00", time.mValue);
            assertEquals(0, mJxf.lsx_rec_readdtcinfo(grp, "P1200", state, desc, time));
            assertEquals("current", state.mValue);
            assertEquals("fault 1", desc.mValue);
            assertEquals("16:05:00", time.mValue);
            assertEquals(0, mJxf.lsx_rec_readdtcinfo(grp, "P1301", state, desc, time));
            assertEquals("current", state.mValue);
            assertEquals("fault 4", desc.mValue);
            assertEquals("16:05:00", time.mValue);
        }
        mJxf.lsx_close(lsx_file);
        mJxf.lsx_deinit(hlsx);
    }

    public void testV2FileReadDSDataTest() throws Throwable {
        int hlsx = mJxf.lsx_init();
        assertNotNull(Integer.valueOf(hlsx));
        String strFileName = FileUtils.sdCardGetDirectoryPath() + File.separator + "v2.x431";
        assertTrue(FileUtils.isFileExist(strFileName));
        int lsx_file = mJxf.lsx_open(hlsx, new X431String(strFileName), 1, new X431Integer(10));
        assertTrue(lsx_file != 0);
        int grpcount = mJxf.lsx_rec_readgroupcount(lsx_file);
        assertEquals(2, grpcount);
        String buf = new String();
        String[] namestrs = new String[10];
        String[] unitstrs = new String[10];
        String[] textstrs = new String[10];
        short[] type = new short[10];
        for (int i = 1; i <= grpcount; i++) {
            int k;
            int grp = mJxf.lsx_rec_readgroupid(lsx_file, i);
            assertTrue(grp != 0);
            int itemcount = mJxf.lsx_rec_readdsitemcount(grp);
            if (i == 1) {
                assertEquals(6, itemcount);
            } else {
                assertEquals(9, itemcount);
            }
            int cols = mJxf.lsx_rec_readdscolcount(grp);
            assertEquals(7, cols);
            assertEquals(0, mJxf.lsx_rec_readdsname(grp, namestrs, cols));
            assertEquals(0, mJxf.lsx_rec_readdsunit(grp, unitstrs, cols));
            assertTrue(mJxf.lsx_rec_readdstype(grp, type, cols) < 0);
            for (k = 0; k < cols; k++) {
                assertEquals("name " + (k + 1), namestrs[k]);
                assertEquals("unit " + (k + 1), unitstrs[k]);
            }
            int item = mJxf.lsx_rec_readfirstdsitem(grp);
            assertTrue(item != 0);
            int count = 0;
            while (item != 0) {
                assertEquals(0, mJxf.lsx_rec_readds(item, textstrs, cols));
                count++;
                for (k = 0; k < cols; k++) {
                    assertEquals(new StringBuilder(String.valueOf("item " + count)).append(k + 1).toString(), textstrs[k]);
                }
                item = mJxf.lsx_rec_readrelndsitem(item, 1);
            }
            assertEquals(count, itemcount);
        }
        mJxf.lsx_close(lsx_file);
        mJxf.lsx_deinit(hlsx);
    }

    public void testsimpletest() throws Throwable {
        TEST_NewFileNoDataWriteTest();
        TEST_NewFileNoDataReadTest();
        TEST_NewFileBasicDataWriteTest();
        TEST_NewFileBasicDataReadTest();
    }

    private void TEST_NewFileNoDataWriteTest() {
        boolean z;
        boolean z2 = true;
        String dstfile = FileUtils.sdCardGetDirectoryPath() + File.separator + "newfilenodata.x431";
        FileUtils.DeleFile(dstfile);
        assertFalse(FileUtils.isFileExist(dstfile));
        int hlsx = mJxf.lsx_init();
        assertNotNull(Integer.valueOf(hlsx));
        X431String filename = new X431String(dstfile);
        int lsx_file = mJxf.lsx_open(hlsx, filename, 2, new X431Integer(10));
        if (lsx_file != 0) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        mJxf.lsx_close(lsx_file);
        mJxf.lsx_deinit(hlsx);
        int iRet1 = mJxf.lsx_checkfile(filename) & 2;
        int iRet2 = mJxf.lsx_checkfile(filename) & Flags.FLAG2;
        if ((mJxf.lsx_checkfile(filename) & 1) != 0) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        if (iRet1 != 0) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        if (iRet2 == 0) {
            z2 = false;
        }
        assertTrue(z2);
        assertEquals(312, FileUtils.GetFileSize(dstfile));
    }

    private void TEST_NewFileNoDataReadTest() {
        String dstfile = FileUtils.sdCardGetDirectoryPath() + File.separator + "newfilenodata.x431";
        assertTrue(FileUtils.isFileExist(dstfile));
        int hlsx = mJxf.lsx_init();
        assertNotNull(Integer.valueOf(hlsx));
        int file = mJxf.lsx_open(hlsx, new X431String(dstfile), 1, new X431Integer(10));
        assertTrue(file != 0);
        LSX_BASEINFO bi = new LSX_BASEINFO();
        assertTrue(mJxf.lsx_read_baseinfo(file, bi) == 0);
        assertNull(bi.serialno);
        assertNull(bi.serialno);
        assertEquals(0, bi.productid);
        assertEquals(1252, bi.codepage);
        assertEquals(Constants.VEHICLE_INI_KEY_ENGLISH, bi.langname);
        assertEquals(Util.ENGLISH, bi.langcode);
        assertNull(bi.langcode_en);
        assertEquals(null, bi.diagversion);
        assertTrue(bi.creationtime.length() >= 24);
        LSX_AUTOINFO ai = new LSX_AUTOINFO();
        assertTrue(mJxf.lsx_read_autoinfo(file, ai) == 0);
        assertNull(ai.vin);
        assertNull(ai.make);
        assertNull(ai.model);
        assertNull(ai.year);
        assertNull(ai.madein);
        assertNull(ai.chassis);
        assertNull(ai.enginemodel);
        assertNull(ai.displacement);
        LSX_SPINFO spi = new LSX_SPINFO();
        assertTrue(mJxf.lsx_read_spinfo(file, spi) == 0);
        assertNull(spi.name);
        assertNull(spi.phone);
        LSX_USERINFO ui = new LSX_USERINFO();
        assertTrue(mJxf.lsx_read_userinfo(file, ui) == 0);
        assertNull(ui.name);
        assertNull(ui.phone);
        assertNull(ui.license);
        assertTrue(mJxf.lsx_rec_readgroupcount(file) == 0);
        assertTrue((mJxf.lsx_read_fileversion(file) & DS_TYPE_6) != 0);
        X431String code = new X431String();
        X431String code_en = new X431String();
        assertEquals(1, mJxf.lsx_read_langcode(file, code, code_en));
        assertEquals(Util.ENGLISH, code.mValue);
        assertNull(code_en.mValue);
        mJxf.lsx_close(file);
        mJxf.lsx_deinit(hlsx);
    }

    private void TEST_NewFileBasicDataWriteTest() {
        boolean z;
        boolean z2 = true;
        String dstfile = FileUtils.sdCardGetDirectoryPath() + File.separator + "newfilebasicdata.x431";
        FileUtils.DeleFile(dstfile);
        assertFalse(FileUtils.isFileExist(dstfile));
        int hlsx = mJxf.lsx_init();
        assertNotNull(Integer.valueOf(hlsx));
        X431String filename = new X431String(dstfile);
        int file = mJxf.lsx_open(hlsx, filename, 2, new X431Integer(10));
        if (file != 0) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        assertTrue(Write_Userinfo(file));
        assertTrue(Write_Baseinfo(file));
        assertTrue(Write_Spinfo(file));
        assertTrue(Write_Autoinfo(file));
        mJxf.lsx_close(file);
        mJxf.lsx_deinit(hlsx);
        int iRet1 = mJxf.lsx_checkfile(filename) & 1;
        int iRet2 = mJxf.lsx_checkfile(filename) & 2;
        int iRet3 = mJxf.lsx_checkfile(filename) & Flags.FLAG2;
        long lRet = FileUtils.GetFileSize(dstfile);
        if (iRet1 != 0) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        if (iRet2 != 0) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        if (iRet3 == 0) {
            z2 = false;
        }
        assertTrue(z2);
        assertEquals(471, lRet);
    }

    private void TEST_NewFileBasicDataReadTest() {
        String dstfile = FileUtils.sdCardGetDirectoryPath() + File.separator + "newfilebasicdata.x431";
        assertTrue(FileUtils.isFileExist(dstfile));
        int hlsx = mJxf.lsx_init();
        assertNotNull(Integer.valueOf(hlsx));
        int file = mJxf.lsx_open(hlsx, new X431String(dstfile), 1, new X431Integer(10));
        assertTrue(file != 0);
        LSX_BASEINFO bi = new LSX_BASEINFO();
        assertEquals(0, mJxf.lsx_read_baseinfo(file, bi));
        assertEquals("980241111111", bi.serialno);
        assertEquals(21, bi.productid);
        assertEquals(936, bi.codepage);
        assertEquals("cn", bi.langcode);
        assertEquals(Util.ENGLISH, bi.langcode_en);
        assertEquals("Chinese Simplified", bi.langname);
        assertEquals("Audi V13.00", bi.diagversion);
        assertTrue(bi.creationtime.length() >= 24);
        LSX_AUTOINFO ai = new LSX_AUTOINFO();
        assertEquals(0, mJxf.lsx_read_autoinfo(file, ai));
        assertEquals("12345678901234567", ai.vin);
        assertEquals("Volkswagen", ai.make);
        assertEquals("\u5965\u8fea", ai.model);
        assertEquals("2006", ai.year);
        assertEquals("China", ai.madein);
        assertEquals("chassis", ai.chassis);
        assertEquals("engine model", ai.enginemodel);
        assertEquals("2.0L", ai.displacement);
        LSX_SPINFO spi = new LSX_SPINFO();
        assertEquals(0, mJxf.lsx_read_spinfo(file, spi));
        assertEquals("\u7ecf\u9500\u5546A", spi.name);
        assertEquals("88888888", spi.phone);
        LSX_USERINFO ui = new LSX_USERINFO();
        assertEquals(0, mJxf.lsx_read_userinfo(file, ui));
        assertEquals("customer A", ui.name);
        assertEquals("77777777", ui.phone);
        assertEquals("123456", ui.license);
        assertEquals(0, mJxf.lsx_rec_readgroupcount(file));
        assertTrue((mJxf.lsx_read_fileversion(file) & DS_TYPE_6) != 0);
        X431String code = new X431String();
        X431String code_en = new X431String();
        assertEquals(2, mJxf.lsx_read_langcode(file, code, code_en));
        assertEquals("cn", code.mValue);
        assertEquals(Util.ENGLISH, code_en.mValue);
        mJxf.lsx_close(file);
        mJxf.lsx_deinit(hlsx);
    }

    public void testlangexttest() throws Throwable {
        TEST_NewFileJaExtWriteTest();
        TEST_NewFileJaExtReadTest();
        TEST_NewFileChsExtWriteTest();
        TEST_NewFileChsExtReadTest();
    }

    private void TEST_NewFileJaExtWriteTest() {
        boolean z;
        boolean z2 = true;
        String dstfile = FileUtils.sdCardGetDirectoryPath() + File.separator + "newfilejaext.x431";
        FileUtils.DeleFile(dstfile);
        assertFalse(FileUtils.isFileExist(dstfile));
        int hlsx = mJxf.lsx_init();
        assertNotNull(Integer.valueOf(hlsx));
        X431String filename = new X431String(dstfile);
        int file = mJxf.lsx_open(hlsx, filename, 2, new X431Integer(10));
        assertTrue(file != 0);
        assertTrue(Write_Baseinfo_langexttest(file, LocaleUtil.JAPANESE, "Japanese"));
        mJxf.lsx_close(file);
        mJxf.lsx_deinit(hlsx);
        if ((mJxf.lsx_checkfile(filename) & 1) != 0) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        if ((mJxf.lsx_checkfile(filename) & 2) != 0) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        if ((mJxf.lsx_checkfile(filename) & Flags.FLAG2) == 0) {
            z2 = false;
        }
        assertTrue(z2);
        assertEquals(340, FileUtils.GetFileSize(dstfile));
    }

    private void TEST_NewFileJaExtReadTest() {
        boolean z;
        boolean z2 = true;
        String dstfile = FileUtils.sdCardGetDirectoryPath() + File.separator + "newfilejaext.x431";
        assertTrue(FileUtils.isFileExist(dstfile));
        int hlsx = mJxf.lsx_init();
        assertNotNull(Integer.valueOf(hlsx));
        int file = mJxf.lsx_open(hlsx, new X431String(dstfile), 1, new X431Integer(10));
        if (file != 0) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        LSX_BASEINFO bi = new LSX_BASEINFO();
        assertEquals(0, mJxf.lsx_read_baseinfo(file, bi));
        assertEquals("980241111111", bi.serialno);
        assertEquals(21, bi.productid);
        assertEquals(51932, bi.codepage);
        assertEquals("jp", bi.langcode);
        assertEquals(Util.ENGLISH, bi.langcode_en);
        assertEquals("Japanese", bi.langname);
        assertEquals("Audi V13.00", bi.diagversion);
        if (bi.creationtime.length() >= 24) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        assertEquals(0, mJxf.lsx_rec_readgroupcount(file));
        if ((mJxf.lsx_read_fileversion(file) & DS_TYPE_6) == 0) {
            z2 = false;
        }
        assertTrue(z2);
        X431String code = new X431String();
        X431String code_en = new X431String();
        assertEquals(2, mJxf.lsx_read_langcode(file, code, code_en));
        assertEquals("jp", code.mValue);
        assertEquals(Util.ENGLISH, code_en.mValue);
        mJxf.lsx_close(file);
        mJxf.lsx_deinit(hlsx);
    }

    private void TEST_NewFileChsExtWriteTest() {
        boolean z;
        boolean z2 = true;
        String dstfile = FileUtils.sdCardGetDirectoryPath() + File.separator + "newfilechsext.x431";
        FileUtils.DeleFile(dstfile);
        assertFalse(FileUtils.isFileExist(dstfile));
        int hlsx = mJxf.lsx_init();
        assertNotNull(Integer.valueOf(hlsx));
        X431String filename = new X431String(dstfile);
        int file = mJxf.lsx_open(hlsx, filename, 2, new X431Integer(10));
        assertTrue(file != 0);
        assertTrue(Write_Baseinfo_langexttest(file, "chs", "Chinese Simplified"));
        mJxf.lsx_close(file);
        mJxf.lsx_deinit(hlsx);
        if ((mJxf.lsx_checkfile(filename) & 1) != 0) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        if ((mJxf.lsx_checkfile(filename) & 2) != 0) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        if ((mJxf.lsx_checkfile(filename) & Flags.FLAG2) == 0) {
            z2 = false;
        }
        assertTrue(z2);
        assertEquals(351, FileUtils.GetFileSize(dstfile));
    }

    private void TEST_NewFileChsExtReadTest() {
        boolean z;
        boolean z2 = true;
        String dstfile = FileUtils.sdCardGetDirectoryPath() + File.separator + "newfilechsext.x431";
        assertTrue(FileUtils.isFileExist(dstfile));
        int hlsx = mJxf.lsx_init();
        assertNotNull(Integer.valueOf(hlsx));
        int file = mJxf.lsx_open(hlsx, new X431String(dstfile), 1, new X431Integer(10));
        if (file != 0) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        LSX_BASEINFO bi = new LSX_BASEINFO();
        assertEquals(0, mJxf.lsx_read_baseinfo(file, bi));
        assertEquals("980241111111", bi.serialno);
        assertEquals(21, bi.productid);
        assertEquals(51932, bi.codepage);
        assertEquals("cn", bi.langcode);
        assertEquals(Util.ENGLISH, bi.langcode_en);
        assertEquals("Chinese Simplified", bi.langname);
        assertEquals("Audi V13.00", bi.diagversion);
        if (bi.creationtime.length() >= 24) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        assertEquals(0, mJxf.lsx_rec_readgroupcount(file));
        if ((mJxf.lsx_read_fileversion(file) & DS_TYPE_6) == 0) {
            z2 = false;
        }
        assertTrue(z2);
        X431String code = new X431String();
        X431String code_en = new X431String();
        assertEquals(2, mJxf.lsx_read_langcode(file, code, code_en));
        assertEquals("cn", code.mValue);
        assertEquals(Util.ENGLISH, code_en.mValue);
        mJxf.lsx_close(file);
        mJxf.lsx_deinit(hlsx);
    }

    public void testitemtest() throws Throwable {
        TEST_GroupItemWriteTest();
        TEST_GroupItemReadInOrderTest();
        TEST_GroupItemReadReverseOrderTest();
        TEST_GroupItemReadDtcInOrderTest();
        TEST_GroupItemReadDtcReverseOrderTest();
        TEST_GroupItemReadFFInOrderTest();
        TEST_GroupItemReadFFReverseOrderTest();
        TEST_GroupItemReadDSInOrderTest();
        TEST_GroupItemReadDSReverseOrderTest();
    }

    private void TEST_GroupItemWriteTest() {
        String dstfile = FileUtils.sdCardGetDirectoryPath() + File.separator + "itemtest.x431";
        FileUtils.DeleFile(dstfile);
        assertFalse(FileUtils.isFileExist(dstfile));
        int hlsx = mJxf.lsx_init();
        assertNotNull(Integer.valueOf(hlsx));
        int file = mJxf.lsx_open(hlsx, new X431String(dstfile), 2, new X431Integer(10));
        assertTrue(file != 0);
        assertTrue(Write_Baseinfo_itemtest(file));
        int grp = mJxf.lsx_rec_writenewgroup(file, "Audi", "Canbus", "12345678976543210", "2008/09/26 21:30:31", 2);
        assertTrue(grp != 0);
        assertTrue(Write_Rdn(grp));
        assertTrue(Write_Dtcs(grp, "P1200"));
        assertTrue(Write_Dtcs(grp, "P1201"));
        assertTrue(Write_Dtcs(grp, "P1202"));
        assertTrue(Write_Vi(grp));
        assertTrue(Write_DsBasics(grp));
        assertTrue(Write_Ds(grp, 1, 13));
        int lineno = 1 + 13;
        assertTrue(Write_FF(grp, "P1300"));
        assertTrue(Write_FF(grp, "P1301"));
        assertTrue(Write_Ds(grp, lineno, 7));
        lineno += 7;
        assertTrue(Write_Dtcs(grp, "P1500"));
        assertTrue(Write_Dtcs(grp, "P1501"));
        assertTrue(Write_Rdn(grp));
        assertTrue(Write_Ds(grp, lineno, 10));
        assertTrue(Write_FF(grp, "P1600"));
        assertTrue(Write_FF(grp, "P1601"));
        assertTrue(Write_Dtcs(grp, "P1700"));
        assertTrue(Write_Dtcs(grp, "P1701"));
        assertTrue(Write_Dtcs(grp, "P1702"));
        assertTrue(Write_Dtcs(grp, "P1703"));
        assertTrue(Write_Dtcs(grp, "P1704"));
        assertTrue(Write_Dtcs(grp, "P1705"));
        assertTrue(Write_Dtcs(grp, "P1700"));
        assertTrue(mJxf.lsx_rec_finishnewgroup(grp, "2008/09/26/23:01:03") == 0);
        mJxf.lsx_close(file);
        mJxf.lsx_deinit(hlsx);
        assertEquals(13082, FileUtils.GetFileSize(dstfile));
    }

    private void TEST_GroupItemReadInOrderTest() {
        String dstfile = FileUtils.sdCardGetDirectoryPath() + File.separator + "itemtest.x431";
        assertTrue(FileUtils.isFileExist(dstfile));
        int hlsx = mJxf.lsx_init();
        assertNotNull(Integer.valueOf(hlsx));
        int file = mJxf.lsx_open(hlsx, new X431String(dstfile), 1, new X431Integer(10));
        assertTrue(file != 0);
        assertTrue(mJxf.lsx_rec_readgroupcount(file) == 1);
        int grp = mJxf.lsx_rec_readgroupid(file, 1);
        assertTrue(grp != 0);
        X431String name = new X431String();
        X431String protocol = new X431String();
        X431String vin = new X431String();
        X431String starttime = new X431String();
        X431String endtime = new X431String();
        X431Integer dsinterval = new X431Integer();
        assertTrue(mJxf.lsx_rec_readgroupinfo(grp, name, protocol, vin, starttime, endtime, dsinterval) == 0);
        assertEquals("Audi", name.mValue);
        assertEquals("Canbus", protocol.mValue);
        assertEquals("12345678976543210", vin.mValue);
        assertTrue(starttime.mValue.length() >= 19);
        assertTrue(endtime.mValue.length() >= 19);
        assertEquals(2, dsinterval.mValue);
        int itemtype = mJxf.lsx_rec_readalltype(grp);
        assertTrue((itemtype & 1) != 0);
        assertTrue((itemtype & 2) != 0);
        assertTrue((itemtype & 8) != 0);
        assertTrue((itemtype & 16) != 0);
        assertTrue((itemtype & 4) != 0);
        int count = 0;
        for (int item = mJxf.lsx_rec_readfirstitem(grp); item != 0; item = mJxf.lsx_rec_readnextitem(item)) {
            count++;
            if (count == 1) {
                boolean z;
                if ((mJxf.lsx_rec_readitemtype(item) & 16) != 0) {
                    z = true;
                } else {
                    z = false;
                }
                assertTrue(z);
            } else if (count >= 2 && count <= 4) {
                assertTrue((mJxf.lsx_rec_readitemtype(item) & 1) != 0);
            } else if (count == 5) {
                assertTrue((mJxf.lsx_rec_readitemtype(item) & 4) != 0);
            } else if (count == 6) {
                assertTrue((mJxf.lsx_rec_readitemtype(item) & 32) != 0);
            } else if (count >= 7 && count <= 19) {
                assertTrue((mJxf.lsx_rec_readitemtype(item) & 2) != 0);
            } else if (count >= 20 && count <= 21) {
                assertTrue((mJxf.lsx_rec_readitemtype(item) & 8) != 0);
            } else if (count >= 22 && count <= 28) {
                assertTrue((mJxf.lsx_rec_readitemtype(item) & 2) != 0);
            } else if (count >= 29 && count <= 30) {
                assertTrue((mJxf.lsx_rec_readitemtype(item) & 1) != 0);
            } else if (count == 31) {
                assertTrue((mJxf.lsx_rec_readitemtype(item) & 16) != 0);
            } else if (count >= 32 && count <= 41) {
                assertTrue((mJxf.lsx_rec_readitemtype(item) & 2) != 0);
            } else if (count >= 42 && count <= 43) {
                assertTrue((mJxf.lsx_rec_readitemtype(item) & 8) != 0);
            } else if (count >= 44 && count <= 50) {
                assertTrue((mJxf.lsx_rec_readitemtype(item) & 1) != 0);
            }
        }
        mJxf.lsx_close(file);
        mJxf.lsx_deinit(hlsx);
    }

    private void TEST_GroupItemReadReverseOrderTest() {
        boolean z;
        String dstfile = FileUtils.sdCardGetDirectoryPath() + File.separator + "itemtest.x431";
        assertTrue(FileUtils.isFileExist(dstfile));
        int hlsx = mJxf.lsx_init();
        assertNotNull(Integer.valueOf(hlsx));
        int file = mJxf.lsx_open(hlsx, new X431String(dstfile), 1, new X431Integer(10));
        if (file != 0) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        if (mJxf.lsx_rec_readgroupcount(file) == 1) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        int grp = mJxf.lsx_rec_readgroupid(file, 1);
        if (grp != 0) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        int itemtype = mJxf.lsx_rec_readalltype(grp);
        if ((itemtype & 1) != 0) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        if ((itemtype & 2) != 0) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        if ((itemtype & 8) != 0) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        if ((itemtype & 16) != 0) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        if ((itemtype & 4) != 0) {
            z = true;
        } else {
            z = false;
        }
        assertTrue(z);
        int count = 50;
        for (int item = mJxf.lsx_rec_readlastitem(grp); item != 0; item = mJxf.lsx_rec_readprevitem(item)) {
            if (count == 1) {
                if ((mJxf.lsx_rec_readitemtype(item) & 16) != 0) {
                    z = true;
                } else {
                    z = false;
                }
                assertTrue(z);
            } else if (count >= 2 && count <= 4) {
                assertTrue((mJxf.lsx_rec_readitemtype(item) & 1) != 0);
            } else if (count == 5) {
                assertTrue((mJxf.lsx_rec_readitemtype(item) & 4) != 0);
            } else if (count == 6) {
                assertTrue((mJxf.lsx_rec_readitemtype(item) & 32) != 0);
            } else if (count >= 7 && count <= 19) {
                assertTrue((mJxf.lsx_rec_readitemtype(item) & 2) != 0);
            } else if (count >= 20 && count <= 21) {
                assertTrue((mJxf.lsx_rec_readitemtype(item) & 8) != 0);
            } else if (count >= 22 && count <= 28) {
                assertTrue((mJxf.lsx_rec_readitemtype(item) & 2) != 0);
            } else if (count >= 29 && count <= 30) {
                assertTrue((mJxf.lsx_rec_readitemtype(item) & 1) != 0);
            } else if (count == 31) {
                assertTrue((mJxf.lsx_rec_readitemtype(item) & 16) != 0);
            } else if (count >= 32 && count <= 41) {
                assertTrue((mJxf.lsx_rec_readitemtype(item) & 2) != 0);
            } else if (count >= 42 && count <= 43) {
                assertTrue((mJxf.lsx_rec_readitemtype(item) & 8) != 0);
            } else if (count >= 44 && count <= 50) {
                assertTrue((mJxf.lsx_rec_readitemtype(item) & 1) != 0);
            }
            count--;
        }
        mJxf.lsx_close(file);
        mJxf.lsx_deinit(hlsx);
    }

    private void TEST_GroupItemReadDtcInOrderTest() {
        String dstfile = FileUtils.sdCardGetDirectoryPath() + File.separator + "itemtest.x431";
        assertTrue(FileUtils.isFileExist(dstfile));
        int hlsx = mJxf.lsx_init();
        assertNotNull(Integer.valueOf(hlsx));
        int file = mJxf.lsx_open(hlsx, new X431String(dstfile), 1, new X431Integer(10));
        assertTrue(file != 0);
        assertTrue(mJxf.lsx_rec_readgroupcount(file) == 1);
        int grp = mJxf.lsx_rec_readgroupid(file, 1);
        assertTrue(grp != 0);
        assertTrue(mJxf.lsx_rec_readdtccount(grp) == 12);
        X431String dtc = new X431String();
        X431String state = new X431String();
        X431String desc = new X431String();
        X431String dtctime = new X431String();
        int count = 0;
        for (int item = mJxf.lsx_rec_readfirstdtcitem(grp); item != 0; item = mJxf.lsx_rec_readnextdtcitem(item)) {
            assertTrue(mJxf.lsx_rec_readdtc(item, dtc, state, desc, dtctime) == 0);
            count++;
            switch (count) {
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    assertEquals("P1200", dtc.mValue);
                    break;
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    assertEquals("P1201", dtc.mValue);
                    break;
                case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                    assertEquals("P1202", dtc.mValue);
                    break;
                case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                    assertEquals("P1500", dtc.mValue);
                    break;
                case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                    assertEquals("P1501", dtc.mValue);
                    break;
                case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                    assertEquals("P1700", dtc.mValue);
                    break;
                case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                    assertEquals("P1701", dtc.mValue);
                    break;
                case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                    assertEquals("P1702", dtc.mValue);
                    break;
                case FileOptions.OPTIMIZE_FOR_FIELD_NUMBER /*9*/:
                    assertEquals("P1703", dtc.mValue);
                    break;
                case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
                    assertEquals("P1704", dtc.mValue);
                    break;
                case TrafficIncident.LOCATION_FIELD_NUMBER /*11*/:
                    assertEquals("P1705", dtc.mValue);
                    break;
                case TrafficIncident.VERTEXOFFSET_FIELD_NUMBER /*12*/:
                    assertEquals("P1700", dtc.mValue);
                    break;
                default:
                    break;
            }
        }
        mJxf.lsx_close(file);
        mJxf.lsx_deinit(hlsx);
    }

    private void TEST_GroupItemReadDtcReverseOrderTest() {
        String dstfile = FileUtils.sdCardGetDirectoryPath() + File.separator + "itemtest.x431";
        assertTrue(FileUtils.isFileExist(dstfile));
        int hlsx = mJxf.lsx_init();
        assertNotNull(Integer.valueOf(hlsx));
        int file = mJxf.lsx_open(hlsx, new X431String(dstfile), 1, new X431Integer(10));
        assertTrue(file != 0);
        assertTrue(mJxf.lsx_rec_readgroupcount(file) == 1);
        int grp = mJxf.lsx_rec_readgroupid(file, 1);
        assertTrue(grp != 0);
        assertTrue(mJxf.lsx_rec_readdtccount(grp) == 12);
        X431String dtc = new X431String();
        X431String state = new X431String();
        X431String desc = new X431String();
        X431String dtctime = new X431String();
        int count = 12;
        for (int item = mJxf.lsx_rec_readlastdtcitem(grp); item != 0; item = mJxf.lsx_rec_readprevdtcitem(item)) {
            assertTrue(mJxf.lsx_rec_readdtc(item, dtc, state, desc, dtctime) == 0);
            switch (count) {
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    assertEquals("P1200", dtc.mValue);
                    break;
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    assertEquals("P1201", dtc.mValue);
                    break;
                case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                    assertEquals("P1202", dtc.mValue);
                    break;
                case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                    assertEquals("P1500", dtc.mValue);
                    break;
                case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                    assertEquals("P1501", dtc.mValue);
                    break;
                case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                    assertEquals("P1700", dtc.mValue);
                    break;
                case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                    assertEquals("P1701", dtc.mValue);
                    break;
                case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                    assertEquals("P1702", dtc.mValue);
                    break;
                case FileOptions.OPTIMIZE_FOR_FIELD_NUMBER /*9*/:
                    assertEquals("P1703", dtc.mValue);
                    break;
                case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
                    assertEquals("P1704", dtc.mValue);
                    break;
                case TrafficIncident.LOCATION_FIELD_NUMBER /*11*/:
                    assertEquals("P1705", dtc.mValue);
                    break;
                case TrafficIncident.VERTEXOFFSET_FIELD_NUMBER /*12*/:
                    assertEquals("P1700", dtc.mValue);
                    break;
                default:
                    break;
            }
            count--;
        }
        mJxf.lsx_close(file);
        mJxf.lsx_deinit(hlsx);
    }

    private void TEST_GroupItemReadFFInOrderTest() {
        String dstfile = FileUtils.sdCardGetDirectoryPath() + File.separator + "itemtest.x431";
        assertTrue(FileUtils.isFileExist(dstfile));
        int hlsx = mJxf.lsx_init();
        assertNotNull(Integer.valueOf(hlsx));
        int file = mJxf.lsx_open(hlsx, new X431String(dstfile), 1, new X431Integer(10));
        assertTrue(file != 0);
        assertTrue(mJxf.lsx_rec_readgroupcount(file) == 1);
        int grp = mJxf.lsx_rec_readgroupid(file, 1);
        assertTrue(grp != 0);
        assertTrue(mJxf.lsx_rec_readffitemcount(grp) == 4);
        X431String dtc = new X431String();
        String[] textstrs = new String[10];
        int count = 0;
        for (int item = mJxf.lsx_rec_readfirstffitem(grp); item != 0; item = mJxf.lsx_rec_readnextffitem(item)) {
            assertTrue(mJxf.lsx_rec_readffcolcount(item) == 5);
            assertTrue(mJxf.lsx_rec_readfreezeframe(item, dtc, textstrs, 5) == 0);
            count++;
            switch (count) {
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    assertEquals("P1300", dtc.mValue);
                    break;
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    assertEquals("P1301", dtc.mValue);
                    break;
                case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                    assertEquals("P1600", dtc.mValue);
                    break;
                case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                    assertEquals("P1601", dtc.mValue);
                    break;
                default:
                    break;
            }
        }
        mJxf.lsx_close(file);
        mJxf.lsx_deinit(hlsx);
    }

    private void TEST_GroupItemReadFFReverseOrderTest() {
        String dstfile = FileUtils.sdCardGetDirectoryPath() + File.separator + "itemtest.x431";
        assertTrue(FileUtils.isFileExist(dstfile));
        int hlsx = mJxf.lsx_init();
        assertNotNull(Integer.valueOf(hlsx));
        int file = mJxf.lsx_open(hlsx, new X431String(dstfile), 1, new X431Integer(10));
        assertTrue(file != 0);
        assertTrue(mJxf.lsx_rec_readgroupcount(file) == 1);
        int grp = mJxf.lsx_rec_readgroupid(file, 1);
        assertTrue(grp != 0);
        assertTrue(mJxf.lsx_rec_readffitemcount(grp) == 4);
        X431String dtc = new X431String();
        String[] textstrs = new String[10];
        int count = 4;
        for (int item = mJxf.lsx_rec_readlastffitem(grp); item != 0; item = mJxf.lsx_rec_readprevffitem(item)) {
            assertTrue(mJxf.lsx_rec_readffcolcount(item) == 5);
            assertTrue(mJxf.lsx_rec_readfreezeframe(item, dtc, textstrs, 5) == 0);
            switch (count) {
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    assertEquals("P1300", dtc.mValue);
                    break;
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    assertEquals("P1301", dtc.mValue);
                    break;
                case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                    assertEquals("P1600", dtc.mValue);
                    break;
                case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                    assertEquals("P1601", dtc.mValue);
                    break;
                default:
                    break;
            }
            count--;
        }
        mJxf.lsx_close(file);
        mJxf.lsx_deinit(hlsx);
    }

    private void TEST_GroupItemReadDSInOrderTest() {
        String dstfile = FileUtils.sdCardGetDirectoryPath() + File.separator + "itemtest.x431";
        assertTrue(FileUtils.isFileExist(dstfile));
        int hlsx = mJxf.lsx_init();
        assertNotNull(Integer.valueOf(hlsx));
        int file = mJxf.lsx_open(hlsx, new X431String(dstfile), 1, new X431Integer(10));
        assertTrue(file != 0);
        X431String code = new X431String();
        X431String code_en = new X431String();
        assertTrue(mJxf.lsx_read_langcode(file, code, code_en) == 2);
        assertEquals("cn", code.mValue);
        assertEquals(Util.ENGLISH, code_en.mValue);
        assertTrue(mJxf.lsx_rec_readgroupcount(file) == 1);
        int grp = mJxf.lsx_rec_readgroupid(file, 1);
        assertTrue(grp != 0);
        assertTrue(mJxf.lsx_rec_readdscolcount(grp) == 9);
        String[] textstrs = new String[10];
        int count = 0;
        for (int item = mJxf.lsx_rec_readfirstdsitem(grp); item != 0; item = mJxf.lsx_rec_readrelndsitem(item, 1)) {
            count++;
            assertTrue(mJxf.lsx_selectreadtextlang(file, code.mValue) == 0);
            assertTrue(mJxf.lsx_rec_readds(item, textstrs, 9) == 0);
            switch (count) {
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    assertEquals("\u6570\u636e\u6d41 \u6570\u503c1,1", textstrs[0]);
                    assertEquals("\u6570\u636e\u6d41 \u6570\u503c1,3", textstrs[2]);
                    assertEquals("\u6570\u636e\u6d41 \u6570\u503c1,9", textstrs[8]);
                    break;
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    assertEquals("\u6570\u636e\u6d41 \u6570\u503c2,1", textstrs[0]);
                    assertEquals("\u6570\u636e\u6d41 \u6570\u503c2,4", textstrs[3]);
                    assertEquals("\u6570\u636e\u6d41 \u6570\u503c2,9", textstrs[8]);
                    break;
                case TrafficIncident.ENDTIME_FIELD_NUMBER /*15*/:
                    assertEquals("\u6570\u636e\u6d41 \u6570\u503c15,1", textstrs[0]);
                    assertEquals("\u6570\u636e\u6d41 \u6570\u503c15,7", textstrs[6]);
                    assertEquals("\u6570\u636e\u6d41 \u6570\u503c15,9", textstrs[8]);
                    break;
                case Protocol.NETBLT /*30*/:
                    assertEquals("\u6570\u636e\u6d41 \u6570\u503c30,1", textstrs[0]);
                    assertEquals("\u6570\u636e\u6d41 \u6570\u503c30,5", textstrs[4]);
                    assertEquals("\u6570\u636e\u6d41 \u6570\u503c30,9", textstrs[8]);
                    break;
            }
            assertTrue(mJxf.lsx_selectreadtextlang(file, code_en.mValue) == 0);
            assertTrue(mJxf.lsx_rec_readds(item, textstrs, 9) == 0);
            switch (count) {
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    assertEquals("ds item1,1", textstrs[0]);
                    assertEquals("ds item1,3", textstrs[2]);
                    assertEquals("ds item1,9", textstrs[8]);
                    break;
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    assertEquals("ds item2,1", textstrs[0]);
                    assertEquals("ds item2,4", textstrs[3]);
                    assertEquals("ds item2,9", textstrs[8]);
                    break;
                case TrafficIncident.ENDTIME_FIELD_NUMBER /*15*/:
                    assertEquals("ds item15,1", textstrs[0]);
                    assertEquals("ds item15,7", textstrs[6]);
                    assertEquals("ds item15,9", textstrs[8]);
                    break;
                case Protocol.NETBLT /*30*/:
                    assertEquals("ds item30,1", textstrs[0]);
                    assertEquals("ds item30,5", textstrs[4]);
                    assertEquals("ds item30,9", textstrs[8]);
                    break;
                default:
                    break;
            }
        }
        mJxf.lsx_close(file);
        mJxf.lsx_deinit(hlsx);
    }

    private void TEST_GroupItemReadDSReverseOrderTest() {
        String dstfile = FileUtils.sdCardGetDirectoryPath() + File.separator + "itemtest.x431";
        assertTrue(FileUtils.isFileExist(dstfile));
        int hlsx = mJxf.lsx_init();
        assertNotNull(Integer.valueOf(hlsx));
        int file = mJxf.lsx_open(hlsx, new X431String(dstfile), 1, new X431Integer(10));
        assertTrue(file != 0);
        X431String code = new X431String();
        X431String code_en = new X431String();
        assertTrue(mJxf.lsx_read_langcode(file, code, code_en) == 2);
        assertEquals("cn", code.mValue);
        assertEquals(Util.ENGLISH, code_en.mValue);
        assertTrue(mJxf.lsx_rec_readgroupcount(file) == 1);
        int grp = mJxf.lsx_rec_readgroupid(file, 1);
        assertTrue(grp != 0);
        assertTrue(mJxf.lsx_rec_readdscolcount(grp) == 9);
        String[] textstrs = new String[10];
        int count = 30;
        for (int item = mJxf.lsx_rec_readlastdsitem(grp); item != 0; item = mJxf.lsx_rec_readrelndsitem(item, 1)) {
            count++;
            assertTrue(mJxf.lsx_selectreadtextlang(file, code.mValue) == 0);
            assertTrue(mJxf.lsx_rec_readds(item, textstrs, 9) == 0);
            switch (count) {
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    assertEquals("\u6570\u636e\u6d41 \u6570\u503c1,1", textstrs[0]);
                    assertEquals("\u6570\u636e\u6d41 \u6570\u503c1,3", textstrs[2]);
                    assertEquals("\u6570\u636e\u6d41 \u6570\u503c1,9", textstrs[8]);
                    break;
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    assertEquals("\u6570\u636e\u6d41 \u6570\u503c2,1", textstrs[0]);
                    assertEquals("\u6570\u636e\u6d41 \u6570\u503c2,4", textstrs[3]);
                    assertEquals("\u6570\u636e\u6d41 \u6570\u503c2,9", textstrs[8]);
                    break;
                case TrafficIncident.ENDTIME_FIELD_NUMBER /*15*/:
                    assertEquals("\u6570\u636e\u6d41 \u6570\u503c15,1", textstrs[0]);
                    assertEquals("\u6570\u636e\u6d41 \u6570\u503c15,7", textstrs[6]);
                    assertEquals("\u6570\u636e\u6d41 \u6570\u503c15,9", textstrs[8]);
                    break;
                case Protocol.NETBLT /*30*/:
                    assertEquals("\u6570\u636e\u6d41 \u6570\u503c30,1", textstrs[0]);
                    assertEquals("\u6570\u636e\u6d41 \u6570\u503c30,5", textstrs[4]);
                    assertEquals("\u6570\u636e\u6d41 \u6570\u503c30,9", textstrs[8]);
                    break;
            }
            assertTrue(mJxf.lsx_selectreadtextlang(file, code_en.mValue) == 0);
            assertTrue(mJxf.lsx_rec_readds(item, textstrs, 9) == 0);
            switch (count) {
                case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                    assertEquals("ds item1,1", textstrs[0]);
                    assertEquals("ds item1,3", textstrs[2]);
                    assertEquals("ds item1,9", textstrs[8]);
                    break;
                case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                    assertEquals("ds item2,1", textstrs[0]);
                    assertEquals("ds item2,4", textstrs[3]);
                    assertEquals("ds item2,9", textstrs[8]);
                    break;
                case TrafficIncident.ENDTIME_FIELD_NUMBER /*15*/:
                    assertEquals("ds item15,1", textstrs[0]);
                    assertEquals("ds item15,7", textstrs[6]);
                    assertEquals("ds item15,9", textstrs[8]);
                    break;
                case Protocol.NETBLT /*30*/:
                    assertEquals("ds item30,1", textstrs[0]);
                    assertEquals("ds item30,5", textstrs[4]);
                    assertEquals("ds item30,9", textstrs[8]);
                    break;
                default:
                    break;
            }
            count--;
        }
        mJxf.lsx_close(file);
        mJxf.lsx_deinit(hlsx);
    }

    public int init() {
        return mJxf.lsx_init();
    }

    public int creatFile(String x431fileName, String langcode, String diagversion, String serialno, int hlsx) {
        String dstfile = Constant.DST_FILE + x431fileName;
        File fileFloder = new File(Constant.DST_FILE);
        if (!fileFloder.exists()) {
            fileFloder.mkdirs();
        }
        int file = -1;
        if (!FileUtils.isFileExist(dstfile)) {
            X431String filename = new X431String(dstfile);
            X431Integer error = new X431Integer(10);
            if (filename != null) {
                file = mJxf.lsx_open(hlsx, filename, 2, error);
                if (file != 0) {
                    System.out.println("writeBaseinfoSucceed = " + Boolean.valueOf(Write_File_Baseinfo(file, langcode, diagversion, serialno)));
                }
            }
        }
        return file;
    }

    private boolean Write_File_Baseinfo(int file, String langcode, String diagversion, String serialno) {
        LSX_BASEINFO baseinfo = new LSX_BASEINFO();
        baseinfo.productid = (short) 21;
        baseinfo.codepage = 936;
        baseinfo.langname = "Chinese Simplified";
        baseinfo.langcode = langcode;
        baseinfo.langcode_en = Util.ENGLISH;
        baseinfo.diagversion = diagversion;
        baseinfo.serialno = serialno;
        return mJxf.lsx_write_baseinfo(file, baseinfo) == 0;
    }

    public int writeNewGroup(int file, String carTypeName, String starttime) {
        return mJxf.lsx_rec_writenewgroup(file, carTypeName, "Canbus", "12345678976543210", starttime, 2);
    }

    public boolean writeDsBasics(int grp, ArrayList<SptExDataStreamIdItem> exDataStreamIdlist) {
        boolean success = false;
        Log.i("DataStreamIteam", "\u5199\u5165");
        if (grp != 0) {
            int leng = exDataStreamIdlist.size();
            LSX_STRING[] namestrs = new LSX_STRING[(leng + 1)];
            LSX_STRING[] unitstrs = new LSX_STRING[(leng + 1)];
            for (int i = 0; i < leng; i++) {
                namestrs[i] = new LSX_STRING();
                unitstrs[i] = new LSX_STRING();
                namestrs[i].str = ((SptExDataStreamIdItem) exDataStreamIdlist.get(i)).getStreamTextIdContent();
                namestrs[i].str_en = ((SptExDataStreamIdItem) exDataStreamIdlist.get(i)).getStreamTextIdContent();
                unitstrs[i].str = ((SptExDataStreamIdItem) exDataStreamIdlist.get(i)).getStreamState();
                unitstrs[i].str_en = ((SptExDataStreamIdItem) exDataStreamIdlist.get(i)).getStreamState();
            }
            if (mJxf.lsx_rec_writedsbasics(grp, namestrs, unitstrs, new int[(leng + 1)], leng) == 0) {
                success = true;
            }
        }
        return success;
    }

    public void writeDSDate(int grp, ArrayList<SptExDataStreamIdItem> exDataStreamIdlist) {
        int leng = exDataStreamIdlist.size();
        LSX_STRING[] itemstrs = new LSX_STRING[leng];
        for (int i = 0; i < leng; i++) {
            itemstrs[i] = new LSX_STRING();
            itemstrs[i].str = ((SptExDataStreamIdItem) exDataStreamIdlist.get(i)).getStreamStr();
        }
        int succeed = mJxf.lsx_rec_writeds(grp, itemstrs, leng);
    }

    public boolean writeVWDsBasics(int grp, ArrayList<SptVwDataStreamIdItem> vwDataStreamIdlist) {
        boolean success = false;
        if (grp != 0) {
            int leng = vwDataStreamIdlist.size();
            LSX_STRING[] namestrs = new LSX_STRING[(leng + 1)];
            LSX_STRING[] unitstrs = new LSX_STRING[(leng + 1)];
            for (int i = 0; i < leng; i++) {
                namestrs[i] = new LSX_STRING();
                unitstrs[i] = new LSX_STRING();
                namestrs[i].str = ((SptVwDataStreamIdItem) vwDataStreamIdlist.get(i)).getStreamTextIdContent();
                namestrs[i].str_en = ((SptVwDataStreamIdItem) vwDataStreamIdlist.get(i)).getStreamTextIdContent();
                unitstrs[i].str = ((SptVwDataStreamIdItem) vwDataStreamIdlist.get(i)).getStreamUnitIdContent();
                unitstrs[i].str_en = ((SptVwDataStreamIdItem) vwDataStreamIdlist.get(i)).getStreamUnitIdContent();
            }
            if (mJxf.lsx_rec_writedsbasics(grp, namestrs, unitstrs, new int[(leng + 1)], leng) == 0) {
                success = true;
            }
        }
        return success;
    }

    public void writeVWDSDate(int grp, ArrayList<SptVwDataStreamIdItem> vwDataStreamIdlist) {
        int leng = vwDataStreamIdlist.size();
        LSX_STRING[] itemstrs = new LSX_STRING[leng];
        for (int i = 0; i < leng; i++) {
            itemstrs[i] = new LSX_STRING();
            itemstrs[i].str = ((SptVwDataStreamIdItem) vwDataStreamIdlist.get(i)).getStreamStr();
        }
        int succeed = mJxf.lsx_rec_writeds(grp, itemstrs, leng);
    }

    public void writeEndCloseFile(int grpId, String endtime, int fileId, int hlsx, String x431fileName) {
        Log.i("\u95ea\u9000", "\u5f00\u59cb\u5199\u6587\u4ef6writeEndCloseFile");
        if (mJxf.lsx_rec_finishnewgroup(grpId, endtime) == 0) {
            mJxf.lsx_close(fileId);
            mJxf.lsx_deinit(hlsx);
        }
    }

    public int openFile(String x431fileName, int hlsx) {
        int lsx_file = -1;
        if (FileUtils.isFileExist(x431fileName)) {
            lsx_file = mJxf.lsx_open(hlsx, new X431String(x431fileName), 1, new X431Integer(10));
            if (lsx_file != 0) {
                return lsx_file;
            }
        }
        return lsx_file;
    }

    public int readGroupId(int fileId) {
        return mJxf.lsx_rec_readgroupid(fileId, 1);
    }

    public int readGroupItemCount(int grp) {
        return mJxf.lsx_rec_readdsitemcount(grp);
    }

    public int readGroupItemColCount(int grp) {
        return mJxf.lsx_rec_readdscolcount(grp);
    }

    public String[] readDsNames(int grp, int cols) {
        if (grp == 0) {
            return new String[0];
        }
        String[] namestrs = new String[cols];
        if (mJxf.lsx_rec_readdsname(grp, namestrs, cols) == 0) {
            return namestrs;
        }
        return new String[0];
    }

    public String[] readDsunitstrs(int grp, int cols) {
        if (grp == 0) {
            return new String[0];
        }
        String[] unitstrs = new String[cols];
        if (mJxf.lsx_rec_readdsunit(grp, unitstrs, cols) == 0) {
            return unitstrs;
        }
        return new String[0];
    }

    public int readDsDataFirstItemCount(int grp) {
        return mJxf.lsx_rec_readfirstdsitem(grp);
    }

    public String[] readDsDataFirstItemData(int grp, int cols, int itemCount) {
        String[] textstrs = new String[cols];
        if (itemCount <= 0 || mJxf.lsx_rec_readds(itemCount, textstrs, cols) != 0) {
            return new String[0];
        }
        return textstrs;
    }

    public String[] readDsDataNextItemData(int grp, int cols, int itemCount) {
        String[] textstrs = new String[cols];
        int item = mJxf.lsx_rec_readrelndsitem(itemCount, 1);
        if (itemCount <= 0 || mJxf.lsx_rec_readds(item, textstrs, cols) != 0) {
            return new String[0];
        }
        return textstrs;
    }

    public void readEndCloseFile(int fileId, int hlsx) {
        mJxf.lsx_close(fileId);
        mJxf.lsx_deinit(hlsx);
    }
}
