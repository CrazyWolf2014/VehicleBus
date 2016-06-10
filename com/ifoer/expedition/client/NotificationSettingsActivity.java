package com.ifoer.expedition.client;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;

public class NotificationSettingsActivity extends PreferenceActivity {
    private static final String LOGTAG;

    /* renamed from: com.ifoer.expedition.client.NotificationSettingsActivity.1 */
    class C04971 implements OnPreferenceChangeListener {
        C04971() {
        }

        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if (Boolean.valueOf(newValue.toString()).booleanValue()) {
                preference.setTitle("Notifications Enabled");
            } else {
                preference.setTitle("Notifications Disabled");
            }
            return true;
        }
    }

    static {
        LOGTAG = LogUtil.makeLogTag(NotificationSettingsActivity.class);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPreferenceScreen(createPreferenceHierarchy());
        setPreferenceDependencies();
        CheckBoxPreference notifyPref = (CheckBoxPreference) getPreferenceManager().findPreference(Constants.SETTINGS_NOTIFICATION_ENABLED);
        if (notifyPref.isChecked()) {
            notifyPref.setTitle("Notifications Enabled");
        } else {
            notifyPref.setTitle("Notifications Disabled");
        }
    }

    private PreferenceScreen createPreferenceHierarchy() {
        Log.d(LOGTAG, "createSettingsPreferenceScreen()...");
        PreferenceManager preferenceManager = getPreferenceManager();
        preferenceManager.setSharedPreferencesName(Constants.SHARED_PREFERENCE_NAME);
        preferenceManager.setSharedPreferencesMode(0);
        PreferenceScreen root = preferenceManager.createPreferenceScreen(this);
        CheckBoxPreference notifyPref = new CheckBoxPreference(this);
        notifyPref.setKey(Constants.SETTINGS_NOTIFICATION_ENABLED);
        notifyPref.setTitle("Notifications Enabled");
        notifyPref.setSummaryOn("Receive push messages");
        notifyPref.setSummaryOff("Do not receive push messages");
        notifyPref.setDefaultValue(Boolean.TRUE);
        notifyPref.setOnPreferenceChangeListener(new C04971());
        CheckBoxPreference soundPref = new CheckBoxPreference(this);
        soundPref.setKey(Constants.SETTINGS_SOUND_ENABLED);
        soundPref.setTitle("Sound");
        soundPref.setSummary("Play a sound for notifications");
        soundPref.setDefaultValue(Boolean.TRUE);
        CheckBoxPreference vibratePref = new CheckBoxPreference(this);
        vibratePref.setKey(Constants.SETTINGS_VIBRATE_ENABLED);
        vibratePref.setTitle("Vibrate");
        vibratePref.setSummary("Vibrate the phone for notifications");
        vibratePref.setDefaultValue(Boolean.TRUE);
        root.addPreference(notifyPref);
        root.addPreference(soundPref);
        root.addPreference(vibratePref);
        return root;
    }

    private void setPreferenceDependencies() {
        Preference soundPref = getPreferenceManager().findPreference(Constants.SETTINGS_SOUND_ENABLED);
        if (soundPref != null) {
            soundPref.setDependency(Constants.SETTINGS_NOTIFICATION_ENABLED);
        }
        Preference vibratePref = getPreferenceManager().findPreference(Constants.SETTINGS_VIBRATE_ENABLED);
        if (vibratePref != null) {
            vibratePref.setDependency(Constants.SETTINGS_NOTIFICATION_ENABLED);
        }
    }
}
