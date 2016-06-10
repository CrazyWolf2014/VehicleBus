package com.cnmobi.im.util;

import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.xbill.DNS.KEYRecord;

public class DateUtils {
    public static Date parse(String dateStr, String format) {
        Date result = null;
        try {
            result = new SimpleDateFormat(format).parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String format(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    public static String getWeekDay(Date date) {
        switch (date.getDay()) {
            case KEYRecord.OWNER_USER /*0*/:
                return "\u661f\u671f\u5929";
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                return "\u661f\u671f\u4e00";
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                return "\u661f\u671f\u4e8c";
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                return "\u661f\u671f\u4e09";
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                return "\u661f\u671f\u56db";
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                return "\u661f\u671f\u4e94";
            case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                return "\u661f\u671f\u516d";
            default:
                return null;
        }
    }
}
