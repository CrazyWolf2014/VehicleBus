package com.cnlaunch.framework.utils;

import com.thoughtworks.xstream.XStream;
import org.xbill.DNS.KEYRecord.Flags;

public class ErrorCode {
    public static int AUTH_FAILED;
    public static int CONFIG_ITEM_INVALID;
    public static int CONFIG_TYPE_INVALID;
    public static int CONST_UNDEFINED;
    public static int DB_CONNECT_FAILED;
    public static int DB_FAILED;
    public static int DEFINED_INVALID;
    public static int FILE_FAILED;
    public static int FILE_NOTFOUND;
    public static int IMAGE_SAVE_FAILED;
    public static int IMAGE_TYPE_INVALID;
    public static int INDEX_UNDEFINED;
    public static int INPUT_PARAM_INVALID;
    public static int INPUT_PARAM_MISSED;
    public static int INPUT_PARAM_NONUNIQUE;
    public static int INPUT_VERIFY_CODE_MISSED;
    public static int ITEM_NOT_FOUND;
    public static int MKDIR_FAILED;
    public static int NETWORK_FAILED;
    public static int PACK_FAILED;
    public static int PARAM_INVALID;
    public static int PARAM_MISSED;
    public static int SERIALIZE_FAILED;
    public static int SESSION_ID_INVALID;
    public static int SESSION_INVALID;
    public static int SESSION_TIMEOUT;
    public static int SIGN_FAILED;
    public static int TOKEN_VERIFY_FAILED;
    public static int UNKNOW;
    public static int UNPACK_FAILED;
    public static int UNSERIALIZE_FAILED;
    public static int VAR_UNDEFINED;

    static {
        UNKNOW = 1000;
        VAR_UNDEFINED = XStream.NO_REFERENCES;
        INDEX_UNDEFINED = XStream.ID_REFERENCES;
        CONST_UNDEFINED = XStream.XPATH_RELATIVE_REFERENCES;
        PARAM_INVALID = 1015;
        PARAM_MISSED = 1016;
        DEFINED_INVALID = 1017;
        INPUT_PARAM_INVALID = 1022;
        INPUT_PARAM_MISSED = 1023;
        INPUT_PARAM_NONUNIQUE = Flags.FLAG5;
        INPUT_VERIFY_CODE_MISSED = 1025;
        AUTH_FAILED = 1201;
        SESSION_INVALID = 1211;
        SESSION_TIMEOUT = 1212;
        SESSION_ID_INVALID = 1213;
        SIGN_FAILED = 1214;
        FILE_NOTFOUND = 1301;
        DB_FAILED = 1401;
        DB_CONNECT_FAILED = 1402;
        FILE_FAILED = 1500;
        MKDIR_FAILED = 1510;
        NETWORK_FAILED = 1600;
        CONFIG_TYPE_INVALID = 1701;
        CONFIG_ITEM_INVALID = 1702;
        SERIALIZE_FAILED = 1731;
        UNSERIALIZE_FAILED = 1732;
        PACK_FAILED = 1733;
        UNPACK_FAILED = 1734;
        ITEM_NOT_FOUND = 1741;
        IMAGE_TYPE_INVALID = 1751;
        IMAGE_SAVE_FAILED = 1752;
        TOKEN_VERIFY_FAILED = 1761;
    }
}
