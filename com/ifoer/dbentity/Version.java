package com.ifoer.dbentity;

import java.util.ArrayList;
import java.util.List;

public class Version {
    private List<Language> language;
    private String versionName;
    private String versionPath;

    public Version() {
        this.language = new ArrayList();
    }

    public String getVersionName() {
        return this.versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionPath() {
        return this.versionPath;
    }

    public void setVersionPath(String versionPath) {
        this.versionPath = versionPath;
    }

    public List<Language> getLanguage() {
        return this.language;
    }

    public void setLanguage(List<Language> language) {
        this.language = language;
    }
}
