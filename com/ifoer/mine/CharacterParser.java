package com.ifoer.mine;

import android.support.v4.view.accessibility.AccessibilityEventCompat;
import com.cnlaunch.framework.network.async.AsyncTaskManager;
import com.cnlaunch.mycar.jni.JniX431File;
import com.ifoer.util.MyHttpException;
import com.ifoer.util.NetPOSPrinter;
import com.tencent.mm.sdk.platformtools.LocaleUtil;
import com.tencent.mm.sdk.platformtools.Util;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.smile.SmileConstants;
import org.jivesoftware.smackx.ping.PingManager;
import org.ksoap2.SoapEnvelope;
import org.kxml2.wap.Wbxml;
import org.xbill.DNS.CERTRecord;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Service;

public class CharacterParser {
    private static CharacterParser characterParser;
    public static String[] pystr;
    private static int[] pyvalue;
    private StringBuilder buffer;
    private String resource;

    static {
        pyvalue = new int[]{-20319, -20317, -20304, -20295, -20292, -20283, -20265, -20257, -20242, -20230, -20051, -20036, -20032, -20026, -20002, -19990, -19986, -19982, -19976, -19805, -19784, -19775, -19774, -19763, -19756, -19751, -19746, -19741, -19739, -19728, -19725, -19715, -19540, -19531, -19525, -19515, -19500, -19484, -19479, -19467, -19289, -19288, -19281, -19275, -19270, -19263, -19261, -19249, -19243, -19242, -19238, -19235, -19227, -19224, -19218, -19212, -19038, -19023, -19018, -19006, -19003, -18996, -18977, -18961, -18952, -18783, -18774, -18773, -18763, -18756, -18741, -18735, -18731, -18722, -18710, -18697, -18696, -18526, -18518, -18501, -18490, -18478, -18463, -18448, -18447, -18446, -18239, -18237, -18231, -18220, -18211, -18201, -18184, -18183, -18181, -18012, -17997, -17988, -17970, -17964, -17961, -17950, -17947, -17931, -17928, -17922, -17759, -17752, -17733, -17730, -17721, -17703, -17701, -17697, -17692, -17683, -17676, -17496, -17487, -17482, -17468, -17454, -17433, -17427, -17417, -17202, -17185, -16983, -16970, -16942, -16915, -16733, -16708, -16706, -16689, -16664, -16657, -16647, -16474, -16470, -16465, -16459, -16452, -16448, -16433, -16429, -16427, -16423, -16419, -16412, -16407, -16403, -16401, -16393, -16220, -16216, -16212, -16205, -16202, -16187, -16180, -16171, -16169, -16158, -16155, -15959, -15958, -15944, -15933, -15920, -15915, -15903, -15889, -15878, -15707, -15701, -15681, -15667, -15661, -15659, -15652, -15640, -15631, -15625, -15454, -15448, -15436, -15435, -15419, -15416, -15408, -15394, -15385, -15377, -15375, -15369, -15363, -15362, -15183, -15180, -15165, -15158, -15153, -15150, -15149, -15144, -15143, -15141, -15140, -15139, -15128, -15121, -15119, -15117, -15110, -15109, -14941, -14937, -14933, -14930, -14929, -14928, -14926, -14922, -14921, -14914, -14908, -14902, -14894, -14889, -14882, -14873, -14871, -14857, -14678, -14674, -14670, -14668, -14663, -14654, -14645, -14630, -14594, -14429, -14407, -14399, -14384, -14379, -14368, -14355, -14353, -14345, -14170, -14159, -14151, -14149, -14145, -14140, -14137, -14135, -14125, -14123, -14122, -14112, -14109, -14099, -14097, -14094, -14092, -14090, -14087, -14083, -13917, -13914, -13910, -13907, -13906, -13905, -13896, -13894, -13878, -13870, -13859, -13847, -13831, -13658, -13611, -13601, -13406, -13404, -13400, -13398, -13395, -13391, -13387, -13383, -13367, -13359, -13356, -13343, -13340, -13329, -13326, -13318, -13147, -13138, -13120, -13107, -13096, -13095, -13091, -13076, -13068, -13063, -13060, -12888, -12875, -12871, -12860, -12858, -12852, -12849, -12838, -12831, -12829, -12812, -12802, -12607, -12597, -12594, -12585, -12556, -12359, -12346, -12320, -12300, -12120, -12099, -12089, -12074, -12067, -12058, -12039, -11867, -11861, -11847, -11831, -11798, -11781, -11604, -11589, -11536, -11358, -11340, -11339, -11324, -11303, -11097, -11077, -11067, -11055, -11052, -11045, -11041, -11038, -11024, -11020, -11019, -11018, -11014, -10838, -10832, -10815, -10800, -10790, -10780, -10764, -10587, -10544, -10533, -10519, -10331, -10329, -10328, -10322, -10315, -10309, -10307, -10296, -10281, -10274, -10270, -10262, -10260, -10256, -10254};
        String[] strArr = new String[MyHttpException.ERROR_USERNAME_REPEAT];
        strArr[0] = "a";
        strArr[1] = "ai";
        strArr[2] = "an";
        strArr[3] = "ang";
        strArr[4] = "ao";
        strArr[5] = "ba";
        strArr[6] = "bai";
        strArr[7] = "ban";
        strArr[8] = "bang";
        strArr[9] = "bao";
        strArr[10] = "bei";
        strArr[11] = "ben";
        strArr[12] = "beng";
        strArr[13] = "bi";
        strArr[14] = "bian";
        strArr[15] = "biao";
        strArr[16] = "bie";
        strArr[17] = "bin";
        strArr[18] = "bing";
        strArr[19] = "bo";
        strArr[20] = "bu";
        strArr[21] = "ca";
        strArr[22] = "cai";
        strArr[23] = "can";
        strArr[24] = "cang";
        strArr[25] = "cao";
        strArr[26] = "ce";
        strArr[27] = "ceng";
        strArr[28] = "cha";
        strArr[29] = "chai";
        strArr[30] = "chan";
        strArr[31] = "chang";
        strArr[32] = "chao";
        strArr[33] = "che";
        strArr[34] = "chen";
        strArr[35] = "cheng";
        strArr[36] = "chi";
        strArr[37] = "chong";
        strArr[38] = "chou";
        strArr[39] = "chu";
        strArr[40] = "chuai";
        strArr[41] = "chuan";
        strArr[42] = "chuang";
        strArr[43] = "chui";
        strArr[44] = "chun";
        strArr[45] = "chuo";
        strArr[46] = "ci";
        strArr[47] = "cong";
        strArr[48] = "cou";
        strArr[49] = "cu";
        strArr[50] = "cuan";
        strArr[51] = "cui";
        strArr[52] = "cun";
        strArr[53] = "cuo";
        strArr[54] = "da";
        strArr[55] = "dai";
        strArr[56] = "dan";
        strArr[57] = "dang";
        strArr[58] = "dao";
        strArr[59] = "de";
        strArr[60] = "deng";
        strArr[61] = "di";
        strArr[62] = "dian";
        strArr[63] = "diao";
        strArr[64] = "die";
        strArr[65] = "ding";
        strArr[66] = "diu";
        strArr[67] = "dong";
        strArr[68] = "dou";
        strArr[69] = "du";
        strArr[70] = "duan";
        strArr[71] = "dui";
        strArr[72] = "dun";
        strArr[73] = "duo";
        strArr[74] = "e";
        strArr[75] = Util.ENGLISH;
        strArr[76] = "er";
        strArr[77] = "fa";
        strArr[78] = "fan";
        strArr[79] = "fang";
        strArr[80] = "fei";
        strArr[81] = "fen";
        strArr[82] = "feng";
        strArr[83] = "fo";
        strArr[84] = "fou";
        strArr[85] = "fu";
        strArr[86] = "ga";
        strArr[87] = "gai";
        strArr[88] = "gan";
        strArr[89] = "gang";
        strArr[90] = "gao";
        strArr[91] = "ge";
        strArr[92] = "gei";
        strArr[93] = "gen";
        strArr[94] = "geng";
        strArr[95] = "gong";
        strArr[96] = "gou";
        strArr[97] = "gu";
        strArr[98] = "gua";
        strArr[99] = "guai";
        strArr[100] = "guan";
        strArr[Service.HOSTNAME] = "guang";
        strArr[Service.ISO_TSAP] = "gui";
        strArr[Service.X400] = "gun";
        strArr[Service.X400_SND] = "guo";
        strArr[Service.CSNET_NS] = "ha";
        strArr[Opcodes.FMUL] = "hai";
        strArr[Service.RTELNET] = "han";
        strArr[Opcodes.IDIV] = "hang";
        strArr[Service.POP_2] = "hao";
        strArr[SoapEnvelope.VER11] = "he";
        strArr[Service.SUNRPC] = "hei";
        strArr[Opcodes.IREM] = "hen";
        strArr[Service.AUTH] = "heng";
        strArr[Opcodes.FREM] = "hong";
        strArr[Service.SFTP] = "hou";
        strArr[Opcodes.INEG] = "hu";
        strArr[Service.UUCP_PATH] = "hua";
        strArr[Opcodes.FNEG] = "huai";
        strArr[Service.NNTP] = "huan";
        strArr[SoapEnvelope.VER12] = "huang";
        strArr[Service.ERPC] = "hui";
        strArr[Opcodes.ISHR] = "hun";
        strArr[Service.NTP] = "huo";
        strArr[Opcodes.IUSHR] = "ji";
        strArr[Service.LOCUS_MAP] = "jia";
        strArr[Opcodes.IAND] = "jian";
        strArr[Service.LOCUS_CON] = "jiang";
        strArr[Flags.FLAG8] = "jiao";
        strArr[Service.PWDGEN] = "jie";
        strArr[Service.CISCO_FNA] = "jin";
        strArr[Service.CISCO_TNA] = "jing";
        strArr[Service.CISCO_SYS] = "jiong";
        strArr[Service.STATSRV] = "jiu";
        strArr[Service.INGRES_NET] = "ju";
        strArr[Service.LOC_SRV] = "juan";
        strArr[Service.PROFILE] = "jue";
        strArr[Service.NETBIOS_NS] = "jun";
        strArr[Service.NETBIOS_DGM] = "ka";
        strArr[Service.NETBIOS_SSN] = "kai";
        strArr[Service.EMFIS_DATA] = "kan";
        strArr[Service.EMFIS_CNTL] = "kang";
        strArr[Service.BL_IDM] = "kao";
        strArr[Opcodes.D2L] = "ke";
        strArr[Opcodes.D2F] = "ken";
        strArr[Opcodes.I2B] = "keng";
        strArr[Opcodes.I2C] = "kong";
        strArr[Opcodes.I2S] = "kou";
        strArr[Opcodes.LCMP] = "ku";
        strArr[Opcodes.FCMPL] = "kua";
        strArr[Opcodes.FCMPG] = "kuai";
        strArr[Opcodes.DCMPL] = "kuan";
        strArr[Opcodes.DCMPG] = "kuang";
        strArr[Opcodes.IFEQ] = "kui";
        strArr[Opcodes.IFNE] = "kun";
        strArr[Opcodes.IFLT] = "kuo";
        strArr[Opcodes.IFGE] = "la";
        strArr[Opcodes.IFGT] = "lai";
        strArr[Opcodes.IFLE] = "lan";
        strArr[Opcodes.IF_ICMPEQ] = "lang";
        strArr[SmileConstants.TOKEN_PREFIX_SHORT_UNICODE] = "lao";
        strArr[Opcodes.IF_ICMPLT] = "le";
        strArr[Opcodes.IF_ICMPGE] = "lei";
        strArr[Opcodes.IF_ICMPGT] = "leng";
        strArr[Opcodes.IF_ICMPLE] = "li";
        strArr[Opcodes.IF_ACMPEQ] = "lia";
        strArr[Opcodes.IF_ACMPNE] = "lian";
        strArr[Opcodes.GOTO] = "liang";
        strArr[Opcodes.JSR] = "liao";
        strArr[Opcodes.RET] = "lie";
        strArr[Opcodes.TABLESWITCH] = "lin";
        strArr[Opcodes.LOOKUPSWITCH] = "ling";
        strArr[Opcodes.IRETURN] = "liu";
        strArr[Opcodes.LRETURN] = "long";
        strArr[Opcodes.FRETURN] = "lou";
        strArr[Opcodes.DRETURN] = "lu";
        strArr[Opcodes.ARETURN] = "lv";
        strArr[Opcodes.RETURN] = "luan";
        strArr[Opcodes.GETSTATIC] = "lue";
        strArr[Opcodes.PUTSTATIC] = "lun";
        strArr[Opcodes.GETFIELD] = "luo";
        strArr[Opcodes.PUTFIELD] = "ma";
        strArr[Opcodes.INVOKEVIRTUAL] = "mai";
        strArr[Opcodes.INVOKESPECIAL] = "man";
        strArr[Opcodes.INVOKESTATIC] = "mang";
        strArr[Opcodes.INVOKEINTERFACE] = "mao";
        strArr[Opcodes.INVOKEDYNAMIC] = "me";
        strArr[Opcodes.NEW] = "mei";
        strArr[Opcodes.NEWARRAY] = "men";
        strArr[Opcodes.ANEWARRAY] = "meng";
        strArr[Opcodes.ARRAYLENGTH] = "mi";
        strArr[Opcodes.ATHROW] = "mian";
        strArr[Wbxml.EXT_0] = "miao";
        strArr[Wbxml.EXT_1] = "mie";
        strArr[Wbxml.EXT_2] = "min";
        strArr[Wbxml.OPAQUE] = "ming";
        strArr[Wbxml.LITERAL_AC] = "miu";
        strArr[Opcodes.MULTIANEWARRAY] = "mo";
        strArr[Opcodes.IFNULL] = "mou";
        strArr[Opcodes.IFNONNULL] = "mu";
        strArr[AsyncTaskManager.REQUEST_SUCCESS_CODE] = "na";
        strArr[201] = "nai";
        strArr[202] = "nan";
        strArr[203] = "nang";
        strArr[204] = "nao";
        strArr[205] = "ne";
        strArr[206] = "nei";
        strArr[207] = "nen";
        strArr[208] = "neng";
        strArr[209] = "ni";
        strArr[210] = "nian";
        strArr[211] = "niang";
        strArr[212] = "niao";
        strArr[213] = "nie";
        strArr[214] = "nin";
        strArr[215] = "ning";
        strArr[216] = "niu";
        strArr[217] = "nong";
        strArr[218] = "nu";
        strArr[219] = "nv";
        strArr[220] = "nuan";
        strArr[221] = "nue";
        strArr[222] = "nuo";
        strArr[223] = "o";
        strArr[SmileConstants.TOKEN_PREFIX_MISC_OTHER] = "ou";
        strArr[225] = "pa";
        strArr[226] = "pai";
        strArr[227] = "pan";
        strArr[SmileConstants.TOKEN_MISC_LONG_TEXT_UNICODE] = "pang";
        strArr[229] = "pao";
        strArr[230] = "pei";
        strArr[231] = "pen";
        strArr[SmileConstants.TOKEN_MISC_BINARY_7BIT] = "peng";
        strArr[233] = "pi";
        strArr[234] = "pian";
        strArr[235] = "piao";
        strArr[SmileConstants.TOKEN_MISC_SHARED_STRING_LONG] = "pie";
        strArr[237] = "pin";
        strArr[238] = PingManager.ELEMENT;
        strArr[239] = "po";
        strArr[240] = "pu";
        strArr[241] = "qi";
        strArr[242] = "qia";
        strArr[Service.SUR_MEAS] = "qian";
        strArr[244] = "qiang";
        strArr[Service.LINK] = "qiao";
        strArr[246] = "qie";
        strArr[247] = "qin";
        strArr[248] = "qing";
        strArr[Type.TKEY] = "qiong";
        strArr[Type.TSIG] = "qiu";
        strArr[Type.IXFR] = "qu";
        strArr[Type.AXFR] = "quan";
        strArr[CERTRecord.URI] = "que";
        strArr[CERTRecord.OID] = "qun";
        strArr[KEYRecord.PROTOCOL_ANY] = "ran";
        strArr[KEYRecord.OWNER_ZONE] = "rang";
        strArr[257] = "rao";
        strArr[258] = "re";
        strArr[259] = "ren";
        strArr[260] = "reng";
        strArr[261] = "ri";
        strArr[262] = "rong";
        strArr[263] = "rou";
        strArr[264] = LocaleUtil.RUSSIAN;
        strArr[265] = "ruan";
        strArr[266] = "rui";
        strArr[267] = "run";
        strArr[268] = "ruo";
        strArr[269] = "sa";
        strArr[270] = "sai";
        strArr[271] = "san";
        strArr[272] = "sang";
        strArr[273] = "sao";
        strArr[274] = "se";
        strArr[275] = "sen";
        strArr[276] = "seng";
        strArr[277] = "sha";
        strArr[278] = "shai";
        strArr[279] = "shan";
        strArr[280] = "shang";
        strArr[281] = "shao";
        strArr[282] = "she";
        strArr[283] = "shen";
        strArr[284] = "sheng";
        strArr[285] = "shi";
        strArr[286] = "shou";
        strArr[287] = "shu";
        strArr[288] = "shua";
        strArr[289] = "shuai";
        strArr[290] = "shuan";
        strArr[291] = "shuang";
        strArr[292] = "shui";
        strArr[293] = "shun";
        strArr[294] = "shuo";
        strArr[295] = "si";
        strArr[296] = "song";
        strArr[297] = "sou";
        strArr[298] = "su";
        strArr[299] = "suan";
        strArr[JniX431File.MAX_DS_COLNUMBER] = "sui";
        strArr[301] = "sun";
        strArr[302] = "suo";
        strArr[303] = "ta";
        strArr[304] = "tai";
        strArr[305] = "tan";
        strArr[306] = "tang";
        strArr[307] = "tao";
        strArr[308] = "te";
        strArr[309] = "teng";
        strArr[310] = "ti";
        strArr[311] = "tian";
        strArr[312] = "tiao";
        strArr[313] = "tie";
        strArr[314] = "ting";
        strArr[315] = "tong";
        strArr[316] = "tou";
        strArr[317] = "tu";
        strArr[318] = "tuan";
        strArr[319] = "tui";
        strArr[320] = "tun";
        strArr[321] = "tuo";
        strArr[322] = "wa";
        strArr[323] = "wai";
        strArr[324] = "wan";
        strArr[325] = "wang";
        strArr[326] = "wei";
        strArr[327] = "wen";
        strArr[328] = "weng";
        strArr[329] = "wo";
        strArr[330] = "wu";
        strArr[331] = "xi";
        strArr[332] = "xia";
        strArr[333] = "xian";
        strArr[334] = "xiang";
        strArr[335] = "xiao";
        strArr[336] = "xie";
        strArr[337] = "xin";
        strArr[338] = "xing";
        strArr[339] = "xiong";
        strArr[340] = "xiu";
        strArr[341] = "xu";
        strArr[342] = "xuan";
        strArr[343] = "xue";
        strArr[344] = "xun";
        strArr[345] = "ya";
        strArr[346] = "yan";
        strArr[347] = "yang";
        strArr[348] = "yao";
        strArr[349] = "ye";
        strArr[350] = "yi";
        strArr[351] = "yin";
        strArr[352] = "ying";
        strArr[353] = "yo";
        strArr[354] = "yong";
        strArr[355] = "you";
        strArr[356] = "yu";
        strArr[357] = "yuan";
        strArr[358] = "yue";
        strArr[359] = "yun";
        strArr[360] = "za";
        strArr[361] = "zai";
        strArr[362] = "zan";
        strArr[363] = "zang";
        strArr[364] = "zao";
        strArr[365] = "ze";
        strArr[366] = "zei";
        strArr[367] = "zen";
        strArr[368] = "zeng";
        strArr[369] = "zha";
        strArr[370] = "zhai";
        strArr[371] = "zhan";
        strArr[372] = "zhang";
        strArr[373] = "zhao";
        strArr[374] = "zhe";
        strArr[375] = "zhen";
        strArr[376] = "zheng";
        strArr[377] = "zhi";
        strArr[MyHttpException.ERROR_INVALID_AUTH_CODE] = "zhong";
        strArr[MyHttpException.ERROR_PASTDUE_AUTH_CODE] = "zhou";
        strArr[MyHttpException.ERROR_EXCEED_TEST_TIME] = "zhu";
        strArr[MyHttpException.ERROR_NOT_SET_PHONE] = "zhua";
        strArr[MyHttpException.ERROR_EMALI_OTHERSUAED] = "zhuai";
        strArr[MyHttpException.ERROR_PASW] = "zhuan";
        strArr[NetPOSPrinter.PRINT_WIDTH] = "zhuang";
        strArr[MyHttpException.ERROR_FAIL_PASW_PROTECTION] = "zhui";
        strArr[386] = "zhun";
        strArr[387] = "zhuo";
        strArr[388] = "zi";
        strArr[389] = "zong";
        strArr[390] = "zou";
        strArr[391] = "zu";
        strArr[MyHttpException.ERROR_USERPASW_FORM] = "zuan";
        strArr[MyHttpException.ERROR_USERNAME_FORM] = "zui";
        strArr[MyHttpException.ERROR_USERPHONE_FORM] = "zun";
        strArr[MyHttpException.ERROR_USER_EMAIL] = "zuo";
        pystr = strArr;
        characterParser = new CharacterParser();
    }

    public static CharacterParser getInstance() {
        return characterParser;
    }

    public String getResource() {
        return this.resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    private int getChsAscii(String chs) {
        int asc = 0;
        try {
            byte[] bytes = chs.getBytes("gb2312");
            if (bytes == null || bytes.length > 2 || bytes.length <= 0) {
                throw new RuntimeException("illegal resource string");
            }
            if (bytes.length == 1) {
                asc = bytes[0];
            }
            if (bytes.length != 2) {
                return asc;
            }
            return (((bytes[0] + KEYRecord.OWNER_ZONE) * KEYRecord.OWNER_ZONE) + (bytes[1] + KEYRecord.OWNER_ZONE)) - AccessibilityEventCompat.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED;
        } catch (Exception e) {
            System.out.println("ERROR:ChineseSpelling.class-getChsAscii(String chs)" + e);
            return 0;
        }
    }

    public String convert(String str) {
        int ascii = getChsAscii(str);
        if (ascii > 0 && ascii < SmileConstants.TOKEN_PREFIX_SHORT_UNICODE) {
            return String.valueOf((char) ascii);
        }
        for (int i = pyvalue.length - 1; i >= 0; i--) {
            if (pyvalue[i] <= ascii) {
                return pystr[i];
            }
        }
        return null;
    }

    public String getSelling(String chs) {
        this.buffer = new StringBuilder();
        for (int i = 0; i < chs.length(); i++) {
            String value;
            String key = chs.substring(i, i + 1);
            if (key.getBytes().length >= 2) {
                value = convert(key);
                if (value == null) {
                    value = "unknown";
                }
            } else {
                value = key;
            }
            this.buffer.append(value);
        }
        return this.buffer.toString();
    }

    public String getSpelling() {
        return getSelling(getResource());
    }
}
