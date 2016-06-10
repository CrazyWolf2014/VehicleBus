package com.cnmobi.im.cropImage;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import com.cnlaunch.x431frame.C0136R;

public class LoadingDialog extends Dialog {
    private Context context;
    private ImageView dialog_image;

    public LoadingDialog(Context context) {
        super(context);
        this.context = context;
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0136R.layout.layout_loading_dialog);
        this.dialog_image = (ImageView) findViewById(C0136R.id.dialog_image);
        ((AnimationDrawable) this.dialog_image.getDrawable()).start();
    }
}
