package com.cnmobi.im;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.view.View;
import android.view.View.OnClickListener;
import com.cnlaunch.x431frame.C0136R;
import com.cnmobi.im.view.PreferenceHead;
import com.ifoer.util.MySharedPreferences;

public class SettingActivity extends PreferenceActivity {
    public static SettingActivity settinginstance;

    /* renamed from: com.cnmobi.im.SettingActivity.1 */
    class C01901 implements OnClickListener {
        C01901() {
        }

        public void onClick(View v) {
            SettingActivity.this.finish();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(C0136R.xml.setting);
        if (MySharedPreferences.getIntValue(this, MySharedPreferences.DefScanPad) == 1) {
            getListView().setBackgroundColor(getResources().getColor(C0136R.color.white));
            PreferenceScreen preferenceScreen = getPreferenceScreen();
            PreferenceHead ph1 = new PreferenceHead(this);
            ph1.setOnBackButtonClickListener(new C01901());
            ph1.setOrder(0);
            preferenceScreen.addPreference(ph1);
        }
    }
}
