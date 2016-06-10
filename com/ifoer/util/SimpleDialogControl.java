package com.ifoer.util;

import android.content.Context;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.ifoer.entity.SptMessageBoxText;

public class SimpleDialogControl {
    public static void showDiaglog(Context contexts, int dialogType, SptMessageBoxText sptMessageBoxText) {
        switch (dialogType) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                SimpleDialog.okDialog(contexts, sptMessageBoxText.getDialogTitle(), sptMessageBoxText.getDialogContent());
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                SimpleDialog.okCancelDialog(contexts, sptMessageBoxText.getDialogTitle(), sptMessageBoxText.getDialogContent());
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                SimpleDialog.yesNoDialog(contexts, sptMessageBoxText.getDialogTitle(), sptMessageBoxText.getDialogContent());
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                SimpleDialog.retryCancelDialog(contexts, sptMessageBoxText.getDialogTitle(), sptMessageBoxText.getDialogContent());
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                SimpleDialog.noButtonDialog(contexts, sptMessageBoxText.getDialogTitle(), sptMessageBoxText.getDialogContent());
            case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                SimpleDialog.okPrintDialog(contexts, sptMessageBoxText.getDialogTitle(), sptMessageBoxText.getDialogContent());
            case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                SimpleDialog.okDialog(contexts, sptMessageBoxText.getDialogTitle(), sptMessageBoxText.getDialogContent());
            default:
        }
    }
}
