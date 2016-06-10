package com.ifoer.entity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.ifoer.expeditionphone.BaseActivity;
import com.ifoer.service.GetConfigTool;
import org.achartengine.chart.TimeChart;
import org.xmlpull.v1.XmlPullParser;

public class InterfaceDao {
    static GetConfigTool getConfigTool;
    private static InterfaceDao instance;
    private static SharedPreferences sp;

    private InterfaceDao() {
        getConfigTool = new GetConfigTool(BaseActivity.mContexts);
        sp = PreferenceManager.getDefaultSharedPreferences(BaseActivity.mContexts);
    }

    public static InterfaceDao getInstance() {
        if (instance == null) {
            instance = new InterfaceDao();
        }
        return instance;
    }

    public static void getConfigFile(boolean flag) {
        if (System.currentTimeMillis() - sp.getLong("stamptime", 0) > TimeChart.DAY || flag) {
            getConfigTool.loadToLocation(GetConfigTool.IM_CONFIG_NAME, GetConfigTool.IM_APP_ID);
            getConfigTool.loadToLocation(GetConfigTool.CONFIG_NAME, GetConfigTool.APP_ID);
        }
        if (flag) {
            getConfigTool.refershConfiList();
            getConfigTool.sendIMConfig();
        }
    }

    public String getConfigArea(String config_name) {
        return getConfigTool.getConfigArea(config_name);
    }

    public static String getConfigFileContent(String fileName) {
        return getConfigTool.getConfigContent(fileName);
    }

    public static String search(String key) {
        if (getConfigTool == null) {
            getConfigTool = new GetConfigTool(BaseActivity.mContexts);
        }
        String url = getConfigTool.search(key);
        if (url == null || url.equals(XmlPullParser.NO_NAMESPACE)) {
            getConfigFile(true);
        }
        return url;
    }
}
