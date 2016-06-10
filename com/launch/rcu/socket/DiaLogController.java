package com.launch.rcu.socket;

import android.content.Context;
import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.ifoer.expedition.cto.CToJava;
import com.ifoer.mine.Contact;
import com.ifoer.util.MySharedPreferences;
import com.ifoer.util.SimpleDialog;
import com.launch.service.BundleBuilder;

public class DiaLogController {
    public static void diaLogControllerRemote(Context contexts, int dialogType, String dialogTitle, String dialogContent) {
        CToJava.haveData = Boolean.valueOf(false);
        switch (dialogType) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                CToJava.activeFlag = Boolean.valueOf(false);
                CToJava.inputBox = Boolean.valueOf(false);
                SimpleDialog.okDialog(contexts, dialogTitle, dialogContent);
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                SimpleDialog.okCancelDialog(contexts, dialogTitle, dialogContent);
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                SimpleDialog.yesNoDialog(contexts, dialogTitle, dialogContent);
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                SimpleDialog.retryCancelDialog(contexts, dialogTitle, dialogContent);
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                SimpleDialog.noButtonDialog(contexts, dialogTitle, dialogContent);
            case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                SimpleDialog.okPrintDialog(contexts, dialogTitle, dialogContent);
            case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                SimpleDialog.sptInputBoxTextDiagnose(contexts, dialogTitle, dialogContent);
            case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                SimpleDialog.sptInputStringDiagnose(contexts, dialogTitle, dialogContent);
            default:
        }
    }

    public static void diaLogControllerRemote2(Context contexts, int dialogType, String dialogTitle, String dialogContent, String inputHint, int digit) {
        if (!MySharedPreferences.getStringValue(contexts, BundleBuilder.IdentityType).equals(Contact.RELATION_ASK)) {
            CToJava.haveData = Boolean.valueOf(false);
            switch (dialogType) {
                case TrafficIncident.ENDTIME_FIELD_NUMBER /*15*/:
                    SimpleDialog.sptInputNumericDiagnose(contexts, dialogTitle, dialogContent, inputHint, digit);
                case FileOptions.CC_GENERIC_SERVICES_FIELD_NUMBER /*16*/:
                    SimpleDialog.sptInputStringExDiagnose(contexts, dialogTitle, dialogContent, inputHint);
                default:
            }
        }
    }
}
