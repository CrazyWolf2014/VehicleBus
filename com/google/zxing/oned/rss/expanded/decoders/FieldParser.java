package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.NotFoundException;
import org.xmlpull.v1.XmlPullParser;

final class FieldParser {
    private static final Object[][] FOUR_DIGIT_DATA_LENGTH;
    private static final Object[][] THREE_DIGIT_DATA_LENGTH;
    private static final Object[][] THREE_DIGIT_PLUS_DIGIT_DATA_LENGTH;
    private static final Object[][] TWO_DIGIT_DATA_LENGTH;
    private static final Object VARIABLE_LENGTH;

    static {
        VARIABLE_LENGTH = new Object();
        r0 = new Object[24][];
        r0[0] = new Object[]{"00", new Integer(18)};
        r0[1] = new Object[]{"01", new Integer(14)};
        r0[2] = new Object[]{"02", new Integer(14)};
        r0[3] = new Object[]{"10", VARIABLE_LENGTH, new Integer(20)};
        r0[4] = new Object[]{"11", new Integer(6)};
        r0[5] = new Object[]{"12", new Integer(6)};
        r0[6] = new Object[]{"13", new Integer(6)};
        r0[7] = new Object[]{"15", new Integer(6)};
        r0[8] = new Object[]{"17", new Integer(6)};
        r0[9] = new Object[]{"20", new Integer(2)};
        r0[10] = new Object[]{"21", VARIABLE_LENGTH, new Integer(20)};
        r0[11] = new Object[]{"22", VARIABLE_LENGTH, new Integer(29)};
        r0[12] = new Object[]{"30", VARIABLE_LENGTH, new Integer(8)};
        r0[13] = new Object[]{"37", VARIABLE_LENGTH, new Integer(8)};
        r0[14] = new Object[]{"90", VARIABLE_LENGTH, new Integer(30)};
        r0[15] = new Object[]{"91", VARIABLE_LENGTH, new Integer(30)};
        r0[16] = new Object[]{"92", VARIABLE_LENGTH, new Integer(30)};
        r0[17] = new Object[]{"93", VARIABLE_LENGTH, new Integer(30)};
        r0[18] = new Object[]{"94", VARIABLE_LENGTH, new Integer(30)};
        r0[19] = new Object[]{"95", VARIABLE_LENGTH, new Integer(30)};
        r0[20] = new Object[]{"96", VARIABLE_LENGTH, new Integer(30)};
        r0[21] = new Object[]{"97", VARIABLE_LENGTH, new Integer(30)};
        r0[22] = new Object[]{"98", VARIABLE_LENGTH, new Integer(30)};
        r0[23] = new Object[]{"99", VARIABLE_LENGTH, new Integer(30)};
        TWO_DIGIT_DATA_LENGTH = r0;
        r0 = new Object[23][];
        r0[0] = new Object[]{"240", VARIABLE_LENGTH, new Integer(30)};
        r0[1] = new Object[]{"241", VARIABLE_LENGTH, new Integer(30)};
        r0[2] = new Object[]{"242", VARIABLE_LENGTH, new Integer(6)};
        r0[3] = new Object[]{"250", VARIABLE_LENGTH, new Integer(30)};
        r0[4] = new Object[]{"251", VARIABLE_LENGTH, new Integer(30)};
        r0[5] = new Object[]{"253", VARIABLE_LENGTH, new Integer(17)};
        r0[6] = new Object[]{"254", VARIABLE_LENGTH, new Integer(20)};
        r0[7] = new Object[]{"400", VARIABLE_LENGTH, new Integer(30)};
        r0[8] = new Object[]{"401", VARIABLE_LENGTH, new Integer(30)};
        r0[9] = new Object[]{"402", new Integer(17)};
        r0[10] = new Object[]{"403", VARIABLE_LENGTH, new Integer(30)};
        r0[11] = new Object[]{"410", new Integer(13)};
        r0[12] = new Object[]{"411", new Integer(13)};
        r0[13] = new Object[]{"412", new Integer(13)};
        r0[14] = new Object[]{"413", new Integer(13)};
        r0[15] = new Object[]{"414", new Integer(13)};
        r0[16] = new Object[]{"420", VARIABLE_LENGTH, new Integer(20)};
        r0[17] = new Object[]{"421", VARIABLE_LENGTH, new Integer(15)};
        r0[18] = new Object[]{"422", new Integer(3)};
        r0[19] = new Object[]{"423", VARIABLE_LENGTH, new Integer(15)};
        r0[20] = new Object[]{"424", new Integer(3)};
        r0[21] = new Object[]{"425", new Integer(3)};
        r0[22] = new Object[]{"426", new Integer(3)};
        THREE_DIGIT_DATA_LENGTH = r0;
        r0 = new Object[57][];
        r0[0] = new Object[]{"310", new Integer(6)};
        r0[1] = new Object[]{"311", new Integer(6)};
        r0[2] = new Object[]{"312", new Integer(6)};
        r0[3] = new Object[]{"313", new Integer(6)};
        r0[4] = new Object[]{"314", new Integer(6)};
        r0[5] = new Object[]{"315", new Integer(6)};
        r0[6] = new Object[]{"316", new Integer(6)};
        r0[7] = new Object[]{"320", new Integer(6)};
        r0[8] = new Object[]{"321", new Integer(6)};
        r0[9] = new Object[]{"322", new Integer(6)};
        r0[10] = new Object[]{"323", new Integer(6)};
        r0[11] = new Object[]{"324", new Integer(6)};
        r0[12] = new Object[]{"325", new Integer(6)};
        r0[13] = new Object[]{"326", new Integer(6)};
        r0[14] = new Object[]{"327", new Integer(6)};
        r0[15] = new Object[]{"328", new Integer(6)};
        r0[16] = new Object[]{"329", new Integer(6)};
        r0[17] = new Object[]{"330", new Integer(6)};
        r0[18] = new Object[]{"331", new Integer(6)};
        r0[19] = new Object[]{"332", new Integer(6)};
        r0[20] = new Object[]{"333", new Integer(6)};
        r0[21] = new Object[]{"334", new Integer(6)};
        r0[22] = new Object[]{"335", new Integer(6)};
        r0[23] = new Object[]{"336", new Integer(6)};
        r0[24] = new Object[]{"340", new Integer(6)};
        r0[25] = new Object[]{"341", new Integer(6)};
        r0[26] = new Object[]{"342", new Integer(6)};
        r0[27] = new Object[]{"343", new Integer(6)};
        r0[28] = new Object[]{"344", new Integer(6)};
        r0[29] = new Object[]{"345", new Integer(6)};
        r0[30] = new Object[]{"346", new Integer(6)};
        r0[31] = new Object[]{"347", new Integer(6)};
        r0[32] = new Object[]{"348", new Integer(6)};
        r0[33] = new Object[]{"349", new Integer(6)};
        r0[34] = new Object[]{"350", new Integer(6)};
        r0[35] = new Object[]{"351", new Integer(6)};
        r0[36] = new Object[]{"352", new Integer(6)};
        r0[37] = new Object[]{"353", new Integer(6)};
        r0[38] = new Object[]{"354", new Integer(6)};
        r0[39] = new Object[]{"355", new Integer(6)};
        r0[40] = new Object[]{"356", new Integer(6)};
        r0[41] = new Object[]{"357", new Integer(6)};
        r0[42] = new Object[]{"360", new Integer(6)};
        r0[43] = new Object[]{"361", new Integer(6)};
        r0[44] = new Object[]{"362", new Integer(6)};
        r0[45] = new Object[]{"363", new Integer(6)};
        r0[46] = new Object[]{"364", new Integer(6)};
        r0[47] = new Object[]{"365", new Integer(6)};
        r0[48] = new Object[]{"366", new Integer(6)};
        r0[49] = new Object[]{"367", new Integer(6)};
        r0[50] = new Object[]{"368", new Integer(6)};
        r0[51] = new Object[]{"369", new Integer(6)};
        r0[52] = new Object[]{"390", VARIABLE_LENGTH, new Integer(15)};
        r0[53] = new Object[]{"391", VARIABLE_LENGTH, new Integer(18)};
        r0[54] = new Object[]{"392", VARIABLE_LENGTH, new Integer(15)};
        r0[55] = new Object[]{"393", VARIABLE_LENGTH, new Integer(18)};
        r0[56] = new Object[]{"703", VARIABLE_LENGTH, new Integer(30)};
        THREE_DIGIT_PLUS_DIGIT_DATA_LENGTH = r0;
        r0 = new Object[17][];
        r0[0] = new Object[]{"7001", new Integer(13)};
        r0[1] = new Object[]{"7002", VARIABLE_LENGTH, new Integer(30)};
        r0[2] = new Object[]{"7003", new Integer(10)};
        r0[3] = new Object[]{"8001", new Integer(14)};
        r0[4] = new Object[]{"8002", VARIABLE_LENGTH, new Integer(20)};
        r0[5] = new Object[]{"8003", VARIABLE_LENGTH, new Integer(30)};
        r0[6] = new Object[]{"8004", VARIABLE_LENGTH, new Integer(30)};
        r0[7] = new Object[]{"8005", new Integer(6)};
        r0[8] = new Object[]{"8006", new Integer(18)};
        r0[9] = new Object[]{"8007", VARIABLE_LENGTH, new Integer(30)};
        r0[10] = new Object[]{"8008", VARIABLE_LENGTH, new Integer(12)};
        r0[11] = new Object[]{"8018", new Integer(18)};
        r0[12] = new Object[]{"8020", VARIABLE_LENGTH, new Integer(25)};
        r0[13] = new Object[]{"8100", new Integer(6)};
        r0[14] = new Object[]{"8101", new Integer(10)};
        r0[15] = new Object[]{"8102", new Integer(2)};
        r0[16] = new Object[]{"8110", VARIABLE_LENGTH, new Integer(30)};
        FOUR_DIGIT_DATA_LENGTH = r0;
    }

    private FieldParser() {
    }

    static String parseFieldsInGeneralPurpose(String str) throws NotFoundException {
        if (str.length() == 0) {
            return XmlPullParser.NO_NAMESPACE;
        }
        if (str.length() < 2) {
            throw NotFoundException.getNotFoundInstance();
        }
        String substring = str.substring(0, 2);
        int i = 0;
        while (i < TWO_DIGIT_DATA_LENGTH.length) {
            if (TWO_DIGIT_DATA_LENGTH[i][0].equals(substring)) {
                return TWO_DIGIT_DATA_LENGTH[i][1] == VARIABLE_LENGTH ? processVariableAI(2, ((Integer) TWO_DIGIT_DATA_LENGTH[i][2]).intValue(), str) : processFixedAI(2, ((Integer) TWO_DIGIT_DATA_LENGTH[i][1]).intValue(), str);
            } else {
                i++;
            }
        }
        if (str.length() < 3) {
            throw NotFoundException.getNotFoundInstance();
        }
        substring = str.substring(0, 3);
        i = 0;
        while (i < THREE_DIGIT_DATA_LENGTH.length) {
            if (THREE_DIGIT_DATA_LENGTH[i][0].equals(substring)) {
                return THREE_DIGIT_DATA_LENGTH[i][1] == VARIABLE_LENGTH ? processVariableAI(3, ((Integer) THREE_DIGIT_DATA_LENGTH[i][2]).intValue(), str) : processFixedAI(3, ((Integer) THREE_DIGIT_DATA_LENGTH[i][1]).intValue(), str);
            } else {
                i++;
            }
        }
        i = 0;
        while (i < THREE_DIGIT_PLUS_DIGIT_DATA_LENGTH.length) {
            if (THREE_DIGIT_PLUS_DIGIT_DATA_LENGTH[i][0].equals(substring)) {
                return THREE_DIGIT_PLUS_DIGIT_DATA_LENGTH[i][1] == VARIABLE_LENGTH ? processVariableAI(4, ((Integer) THREE_DIGIT_PLUS_DIGIT_DATA_LENGTH[i][2]).intValue(), str) : processFixedAI(4, ((Integer) THREE_DIGIT_PLUS_DIGIT_DATA_LENGTH[i][1]).intValue(), str);
            } else {
                i++;
            }
        }
        if (str.length() < 4) {
            throw NotFoundException.getNotFoundInstance();
        }
        substring = str.substring(0, 4);
        i = 0;
        while (i < FOUR_DIGIT_DATA_LENGTH.length) {
            if (FOUR_DIGIT_DATA_LENGTH[i][0].equals(substring)) {
                return FOUR_DIGIT_DATA_LENGTH[i][1] == VARIABLE_LENGTH ? processVariableAI(4, ((Integer) FOUR_DIGIT_DATA_LENGTH[i][2]).intValue(), str) : processFixedAI(4, ((Integer) FOUR_DIGIT_DATA_LENGTH[i][1]).intValue(), str);
            } else {
                i++;
            }
        }
        throw NotFoundException.getNotFoundInstance();
    }

    private static String processFixedAI(int i, int i2, String str) throws NotFoundException {
        if (str.length() < i) {
            throw NotFoundException.getNotFoundInstance();
        }
        String substring = str.substring(0, i);
        if (str.length() < i + i2) {
            throw NotFoundException.getNotFoundInstance();
        }
        return new StringBuffer().append('(').append(substring).append(')').append(str.substring(i, i + i2)).append(parseFieldsInGeneralPurpose(str.substring(i + i2))).toString();
    }

    private static String processVariableAI(int i, int i2, String str) throws NotFoundException {
        String substring = str.substring(0, i);
        int length = str.length() < i + i2 ? str.length() : i + i2;
        return new StringBuffer().append('(').append(substring).append(')').append(str.substring(i, length)).append(parseFieldsInGeneralPurpose(str.substring(length))).toString();
    }
}
