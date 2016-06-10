package com.cnlaunch.framework.utils.lang;

import android.text.TextUtils;
import com.cnlaunch.framework.utils.NLog;
import com.ifoer.mine.Contact;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LangManager {
    private static final String tag;

    static {
        tag = LangManager.class.getSimpleName();
    }

    public static List<LangInfo> getLangList() {
        List<LangInfo> list = new ArrayList();
        list.add(new LangInfo(Contact.RELATION_FRIEND, "\u5fb7\u8bed", Lang.DE));
        list.add(new LangInfo(Contact.RELATION_BACKNAME, "\u65e5\u6587", Lang.JP));
        list.add(new LangInfo(Contact.RELATION_NODONE, "\u4fc4\u7f57\u65af", Lang.RU));
        list.add(new LangInfo(Contact.RELATION_NOAGREE, "\u6cd5\u8bed", Lang.FR));
        list.add(new LangInfo(Contact.RELATION_AGREE, "\u897f\u73ed\u7259", Lang.ES));
        list.add(new LangInfo("6", "\u8461\u8404\u7259", Lang.PT, Lang.BR));
        list.add(new LangInfo("7", "\u6ce2\u5170", Lang.PL));
        list.add(new LangInfo("8", "\u571f\u8033\u5176", Lang.TR));
        list.add(new LangInfo("9", "\u8377\u5170\u8bed", Lang.NL));
        list.add(new LangInfo("10", "\u5e0c\u814a", Lang.GR, Lang.EL));
        list.add(new LangInfo("11", "\u5308\u7259\u5229\u8bed", Lang.HU));
        list.add(new LangInfo("12", "\u963f\u62c9\u4f2f\u8bed", Lang.AR, Lang.EG));
        list.add(new LangInfo("13", "\u4e39\u9ea6\u8bed", Lang.DA, Lang.DK));
        list.add(new LangInfo("14", "\u97e9\u8bed", Lang.KO, Lang.KR));
        list.add(new LangInfo("15", "\u6ce2\u65af\u8bed", Lang.FA, Lang.IR));
        list.add(new LangInfo("16", "\u7f57\u9a6c\u5c3c\u4e9a\u8bed", Lang.RO));
        list.add(new LangInfo("17", "\u585e\u5c14\u7ef4\u4e9a\u8bed", Lang.SR, Lang.RS));
        list.add(new LangInfo("18", "\u82ac\u5170\u8bed", Lang.FI));
        list.add(new LangInfo("19", "\u745e\u5178\u8bed", Lang.SV, Lang.SE));
        list.add(new LangInfo("20", "\u6377\u514b\u8bed", Lang.CS, Lang.CZ));
        list.add(new LangInfo("221", "\u9999\u6e2f", Lang.HK, Lang.TW));
        list.add(new LangInfo("1001", "\u82f1\u8bed", Lang.EN));
        list.add(new LangInfo("1002", "\u4e2d\u6587", Lang.CN));
        list.add(new LangInfo("1003", "\u610f\u5927\u5229", Lang.IT));
        list.add(new LangInfo("231", "\u514b\u7f57\u5730\u4e9a", Lang.HR));
        return list;
    }

    public static String getLangId(String langCode) {
        if (TextUtils.isEmpty(langCode)) {
            NLog.m917e(tag, "getLangId langCode is not null.");
            return "1001";
        }
        for (LangInfo bean : getLangList()) {
            if (!langCode.equalsIgnoreCase(bean.getCode())) {
                if (langCode.equalsIgnoreCase(bean.getCode1())) {
                }
            }
            return bean.getLangId();
        }
        return "1001";
    }

    public static String getLangCode(String langId) {
        if (TextUtils.isEmpty(langId)) {
            NLog.m917e(tag, "getLangCode langId is not null.");
            return Lang.EN;
        }
        for (LangInfo bean : getLangList()) {
            if (langId.equals(bean.getLangId())) {
                return bean.getCode();
            }
        }
        return Lang.EN;
    }

    public static String getLangCode1(String langId) {
        if (TextUtils.isEmpty(langId)) {
            NLog.m917e(tag, "getLangCode langId is not null.");
            return Lang.EN;
        }
        for (LangInfo bean : getLangList()) {
            if (langId.equals(bean.getLangId())) {
                if (TextUtils.isEmpty(bean.getCode1())) {
                    return bean.getCode();
                }
                return bean.getCode1();
            }
        }
        return Lang.EN;
    }

    public static String getLanguage() {
        return Locale.getDefault().getLanguage();
    }

    public static String getCountry() {
        return Locale.getDefault().getCountry();
    }
}
