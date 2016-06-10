package com.launch.rcu.socket;

import com.ifoer.mine.Contact;

public class SocketCode {
    public static final byte[] Byte_BackSuperior;
    public static final byte[] Byte_CombinActivity;
    public static final byte[] Byte_MostActivity;
    public static final byte[] Byte_Value;
    public static final byte[] Byte_back_button;
    public static final byte[] Byte_cancel;
    public static final byte[] Byte_combination_back;
    public static final byte[] Byte_combination_clear;
    public static final byte[] Byte_combination_sure;
    public static final byte[] Byte_date;
    public static final byte[] Byte_downChannel;
    public static final byte[] Byte_graphView;
    public static final byte[] Byte_guanli;
    public static final byte[] Byte_image_button;
    public static final byte[] Byte_jietu;
    public static final byte[] Byte_jilu;
    public static final byte[] Byte_record_button;
    public static final byte[] Byte_start;
    public static final byte[] Byte_sure;
    public static final byte[] Byte_toSeeReportList;
    public static final byte[] Byte_toSeeReportOne;
    public static final byte[] Byte_upChannel;
    public static final byte[] Byte_wenzi;
    public static final String REMOTE_BACK = "back";
    public static final String REMOTE_CANCEL = "cancel";
    public static final String REMOTE_COMBINACTIVITY = "combinActivity";
    public static final String REMOTE_COMBINATION_BACK = "combination_back";
    public static final String REMOTE_COMBINATION_CLEAR = "combination_clear";
    public static final String REMOTE_COMBINATION_SURE = "combination_sure";
    public static final String REMOTE_DATA = "data";
    public static final String REMOTE_DOWNCHANNEL = "downChannel";
    public static final String REMOTE_GRAPHVIEW = "graphView";
    public static final String REMOTE_GUANLI = "guanli";
    public static final String REMOTE_JIETU = "jietu";
    public static final String REMOTE_JILU = "jilu";
    public static final String REMOTE_MOSTACTIVITY = "mostActivity";
    public static final String REMOTE_PAUSE = "start";
    public static final String REMOTE_RECORD_BUTTON = "record";
    public static final String REMOTE_SHARE = "share";
    public static final String REMOTE_SHUJU2 = "shuju2";
    public static final String REMOTE_START = "pause";
    public static final String REMOTE_SURE = "sure";
    public static final String REMOTE_UPCHANNEL = "upChannel";
    public static final String REMOTE_VALUE = "value";
    public static final String REMOTE_WENZI = "wenzi";

    static {
        Byte_wenzi = Contact.RELATION_FRIEND.getBytes();
        Byte_jietu = Contact.RELATION_BACKNAME.getBytes();
        Byte_guanli = Contact.RELATION_NODONE.getBytes();
        Byte_jilu = Contact.RELATION_NOAGREE.getBytes();
        Byte_start = Contact.RELATION_AGREE.getBytes();
        Byte_date = "6".getBytes();
        Byte_upChannel = "7".getBytes();
        Byte_downChannel = "8".getBytes();
        Byte_image_button = "9".getBytes();
        Byte_record_button = "10".getBytes();
        Byte_back_button = "11".getBytes();
        Byte_combination_sure = "12".getBytes();
        Byte_combination_clear = "13".getBytes();
        Byte_combination_back = "14".getBytes();
        Byte_toSeeReportOne = "15".getBytes();
        Byte_toSeeReportList = "16".getBytes();
        Byte_BackSuperior = "17".getBytes();
        Byte_sure = "18".getBytes();
        Byte_cancel = "19".getBytes();
        Byte_MostActivity = "20".getBytes();
        Byte_CombinActivity = "21".getBytes();
        Byte_graphView = "22".getBytes();
        Byte_Value = "23".getBytes();
    }

    public static boolean compareByte(byte[] A, byte[] B) {
        if (new String(A).equals(new String(B))) {
            return true;
        }
        return false;
    }
}
