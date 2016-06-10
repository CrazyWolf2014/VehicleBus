package com.cnmobi.im.view;

import android.content.Context;
import android.preference.Preference;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import com.cnlaunch.x431frame.C0136R;

public class PreferenceHead extends Preference {
    private OnClickListener onBackButtonClickListener;

    /* renamed from: com.cnmobi.im.view.PreferenceHead.1 */
    class C02081 implements OnClickListener {
        C02081() {
        }

        public void onClick(View v) {
            if (PreferenceHead.this.onBackButtonClickListener != null) {
                PreferenceHead.this.onBackButtonClickListener.onClick(v);
            }
        }
    }

    public PreferenceHead(Context context) {
        super(context);
        setLayoutResource(C0136R.layout.pretop_title);
    }

    protected void onBindView(View view) {
        super.onBindView(view);
        ((ImageButton) view.findViewById(C0136R.id.backBtn)).setOnClickListener(new C02081());
    }

    public void setOnBackButtonClickListener(OnClickListener onClickListener) {
        this.onBackButtonClickListener = onClickListener;
    }
}
