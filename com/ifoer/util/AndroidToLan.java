package com.ifoer.util;

import com.cnlaunch.x431pro.common.Constants;
import com.thoughtworks.xstream.XStream;

public class AndroidToLan {
    public static String toLan(String lan) {
        String result = Constants.DEFAULT_LANGUAGE;
        if (lan.equalsIgnoreCase("CN")) {
            return "CN";
        }
        if (lan.equalsIgnoreCase("JP")) {
            return "JP";
        }
        if (lan.equalsIgnoreCase("DE")) {
            return "DE";
        }
        if (lan.equalsIgnoreCase("HK") || lan.equalsIgnoreCase("TW")) {
            return "HK";
        }
        if (lan.equalsIgnoreCase("FR")) {
            return "FR";
        }
        if (lan.equalsIgnoreCase("PT") || lan.equalsIgnoreCase("BR")) {
            return "PT";
        }
        if (lan.equalsIgnoreCase("RU")) {
            return "RU";
        }
        if (lan.equalsIgnoreCase("IT")) {
            return "IT";
        }
        if (lan.equalsIgnoreCase("ES")) {
            return "ES";
        }
        if (lan.equalsIgnoreCase("PL")) {
            return "PL";
        }
        if (lan.equalsIgnoreCase("TR")) {
            return "TR";
        }
        if (lan.equalsIgnoreCase("NL")) {
            return "NL";
        }
        if (lan.equalsIgnoreCase("GR")) {
            return "GR";
        }
        if (lan.equalsIgnoreCase("HU")) {
            return "HU";
        }
        if (lan.equalsIgnoreCase("AR") || lan.equalsIgnoreCase("EG")) {
            return "AR";
        }
        if (lan.equalsIgnoreCase("DA")) {
            return "DA";
        }
        if (lan.equalsIgnoreCase("FA")) {
            return "FA";
        }
        if (lan.equalsIgnoreCase("KO") || lan.equalsIgnoreCase("KR")) {
            return "KO";
        }
        if (lan.equalsIgnoreCase("FI")) {
            return "FI";
        }
        if (lan.equalsIgnoreCase("SV")) {
            return "SV";
        }
        if (lan.equalsIgnoreCase("CS")) {
            return "CS";
        }
        return Constants.DEFAULT_LANGUAGE;
    }

    public static int getLanId(String lanName) {
        if (lanName.equalsIgnoreCase("CN")) {
            return XStream.ID_REFERENCES;
        }
        if (lanName.equalsIgnoreCase("JP")) {
            return 2;
        }
        if (lanName.equalsIgnoreCase("DE")) {
            return 1;
        }
        if (lanName.equalsIgnoreCase("HK") || lanName.equalsIgnoreCase("TW")) {
            return 221;
        }
        if (lanName.equalsIgnoreCase("FR")) {
            return 4;
        }
        if (lanName.equalsIgnoreCase("PT") || lanName.equalsIgnoreCase("BR")) {
            return 6;
        }
        if (lanName.equalsIgnoreCase("RU")) {
            return 3;
        }
        if (lanName.equalsIgnoreCase("IT")) {
            return XStream.XPATH_RELATIVE_REFERENCES;
        }
        if (lanName.equalsIgnoreCase("ES")) {
            return 5;
        }
        if (lanName.equalsIgnoreCase("PL")) {
            return 7;
        }
        if (lanName.equalsIgnoreCase("TR")) {
            return 8;
        }
        if (lanName.equalsIgnoreCase("NL")) {
            return 9;
        }
        if (lanName.equalsIgnoreCase("GR")) {
            return 10;
        }
        if (lanName.equalsIgnoreCase("HU")) {
            return 11;
        }
        if (lanName.equalsIgnoreCase("AR") || lanName.equalsIgnoreCase("EG")) {
            return 12;
        }
        if (lanName.equalsIgnoreCase("DA")) {
            return 13;
        }
        if (lanName.equalsIgnoreCase("FA")) {
            return 15;
        }
        if (lanName.equalsIgnoreCase("KO") || lanName.equalsIgnoreCase("KR")) {
            return 14;
        }
        if (lanName.equalsIgnoreCase("FI")) {
            return 18;
        }
        if (lanName.equalsIgnoreCase("SV")) {
            return 19;
        }
        if (lanName.equalsIgnoreCase("CS")) {
            return 20;
        }
        if (lanName.equalsIgnoreCase("RO")) {
            return 16;
        }
        if (lanName.equalsIgnoreCase("SR")) {
            return 17;
        }
        return XStream.NO_REFERENCES;
    }

    public static int languages(String language) {
        if (language.equalsIgnoreCase("CN")) {
            return 1;
        }
        if (language.equalsIgnoreCase("JP")) {
            return 2;
        }
        if (language.equalsIgnoreCase("DE")) {
            return 3;
        }
        if (language.equalsIgnoreCase("HK") || language.equalsIgnoreCase("TW")) {
            return 4;
        }
        if (language.equalsIgnoreCase("FR")) {
            return 5;
        }
        if (language.equalsIgnoreCase("PT") || language.equalsIgnoreCase("BR")) {
            return 6;
        }
        if (language.equalsIgnoreCase("RU")) {
            return 7;
        }
        if (language.equalsIgnoreCase("IT")) {
            return 8;
        }
        if (language.equalsIgnoreCase("ES")) {
            return 9;
        }
        if (language.equalsIgnoreCase("PL")) {
            return 10;
        }
        if (language.equalsIgnoreCase("TR")) {
            return 11;
        }
        if (language.equalsIgnoreCase("NL")) {
            return 12;
        }
        if (language.equalsIgnoreCase("GR")) {
            return 13;
        }
        if (language.equalsIgnoreCase("HU")) {
            return 14;
        }
        if (language.equalsIgnoreCase("DA")) {
            return 18;
        }
        if (language.equalsIgnoreCase("FA")) {
            return 19;
        }
        if (language.equalsIgnoreCase("KO") || language.equalsIgnoreCase("KR") || language.equalsIgnoreCase("KOREAN")) {
            return 20;
        }
        if (language.equalsIgnoreCase("FI")) {
            return 21;
        }
        if (language.equalsIgnoreCase("SV")) {
            return 22;
        }
        if (language.equalsIgnoreCase("CS")) {
            return 23;
        }
        return 0;
    }

    public static String getIdToLanName(int lanId) {
        String lanName = Constants.DEFAULT_LANGUAGE;
        if (lanId == XStream.ID_REFERENCES) {
            return "CN";
        }
        if (lanId == 2) {
            return "JP";
        }
        if (lanId == 1) {
            return "DE";
        }
        if (lanId == 221) {
            return "TW";
        }
        if (lanId == 4) {
            return "FR";
        }
        if (lanId == 6) {
            return "PT";
        }
        if (lanId == 3) {
            return "RU";
        }
        if (lanId == XStream.XPATH_RELATIVE_REFERENCES) {
            return "IT";
        }
        if (lanId == 5) {
            return "ES";
        }
        if (lanId == 7) {
            return "PL";
        }
        if (lanId == 8) {
            return "TR";
        }
        if (lanId == 9) {
            return "NL";
        }
        if (lanId == 10) {
            return "GR";
        }
        if (lanId == 11) {
            return "HU";
        }
        if (lanId == 12) {
            return "AR";
        }
        if (lanId == 13) {
            return "DA";
        }
        if (lanId == 15) {
            return "FA";
        }
        if (lanId == 14) {
            return "KO";
        }
        if (lanId == 18) {
            return "FI";
        }
        if (lanId == 19) {
            return "SV";
        }
        if (lanId == 20) {
            return "CS";
        }
        if (lanId == 16) {
            return "RO";
        }
        if (lanId == 17) {
            return "SR";
        }
        return lanName;
    }
}
