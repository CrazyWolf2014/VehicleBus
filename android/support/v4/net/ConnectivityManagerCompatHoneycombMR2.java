package android.support.v4.net;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import org.xbill.DNS.KEYRecord;

class ConnectivityManagerCompatHoneycombMR2 {
    ConnectivityManagerCompatHoneycombMR2() {
    }

    public static boolean isActiveNetworkMetered(ConnectivityManager cm) {
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            return true;
        }
        switch (info.getType()) {
            case KEYRecord.OWNER_USER /*0*/:
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
            case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                return true;
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
            case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
            case FileOptions.OPTIMIZE_FOR_FIELD_NUMBER /*9*/:
                return false;
            default:
                return true;
        }
    }
}
