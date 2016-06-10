package com.ifoer.pro.expeditionphone;

import android.content.Intent;
import android.os.Bundle;
import com.ifoer.image.GDApplication;

public class MainMenuActivity extends com.ifoer.ui.MainMenuActivity {
    private GDApplication app;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createView();
    }

    public void createView() {
        super.createView();
    }

    public static Intent getmKeyToUpgradeIntent() {
        return mKeyToUpgradeIntent;
    }

    public void createMainActivity() {
        mMainActivityIntent = new Intent(this, MainActivity.class);
    }

    public void setmKeyToUpgradeIntent(Intent mKeyToUpgradeIntent) {
        mKeyToUpgradeIntent = mKeyToUpgradeIntent;
    }

    public void createKeyUpGrade() {
        setmKeyToUpgradeIntent(new Intent(this, KeyToUpgradeActivity.class));
    }

    public void onStart() {
        super.onStart();
    }

    public void onDestroy() {
        super.onDestroy();
    }
}
