package com.cnlaunch.framework.utils.lang;

public class LangInfo {
    private String code;
    private String code1;
    private String langId;
    private String language;

    public LangInfo(String langId, String language, String code) {
        this.langId = langId;
        this.language = language;
        this.code = code;
    }

    public LangInfo(String langId, String language, String code, String code1) {
        this.langId = langId;
        this.language = language;
        this.code = code;
        this.code1 = code1;
    }

    public String getLangId() {
        return this.langId;
    }

    public void setLangId(String langId) {
        this.langId = langId;
    }

    public String getLanguage() {
        return this.language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode1() {
        return this.code1;
    }

    public void setCode1(String code1) {
        this.code1 = code1;
    }
}
