package com.ifoer.util;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.entity.Constant;
import com.ifoer.entity.PageInteractiveData;
import com.ifoer.expedition.BluetoothOrder.PageInteractiveDataAnalysis;

public class HistoryDialog {
    private static final boolean f1310D = false;
    private static boolean hasShow;
    private static Builder sptInputNumericDialog;
    public static Dialog unifyDialog;

    /* renamed from: com.ifoer.util.HistoryDialog.1 */
    class C07451 implements OnClickListener {
        private final /* synthetic */ PageInteractiveData val$data;

        C07451(PageInteractiveData pageInteractiveData) {
            this.val$data = pageInteractiveData;
        }

        public void onClick(DialogInterface dialog, int which) {
            Constant.isVin = true;
            Constant.feedback = PageInteractiveDataAnalysis.feedbackData(new byte[]{(byte) 1}, this.val$data);
            dialog.dismiss();
        }
    }

    /* renamed from: com.ifoer.util.HistoryDialog.2 */
    class C07462 implements OnClickListener {
        private final /* synthetic */ PageInteractiveData val$data;

        C07462(PageInteractiveData pageInteractiveData) {
            this.val$data = pageInteractiveData;
        }

        public void onClick(DialogInterface dialog, int which) {
            Constant.isVin = false;
            byte[] no = new byte[]{(byte) 0};
            Constant.feedback = null;
            Constant.feedback = PageInteractiveDataAnalysis.feedbackData(no, this.val$data);
            dialog.dismiss();
        }
    }

    static {
        hasShow = false;
    }

    public static void vinYesNo(Context context, PageInteractiveData data) {
        if (unifyDialog != null && unifyDialog.isShowing()) {
            unifyDialog.dismiss();
        }
        Builder builder = new Builder(context);
        builder.setTitle("tips");
        builder.setMessage("\u662f\u5426\u9009\u62e9vin\u7801\u8fdb\u5165");
        builder.setCancelable(false);
        builder.setPositiveButton(context.getResources().getText(C0136R.string.Yes), new C07451(data));
        builder.setNegativeButton(context.getResources().getText(C0136R.string.No), new C07462(data));
        try {
            unifyDialog = builder.show();
            unifyDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
