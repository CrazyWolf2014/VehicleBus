package org.apache.harmony.javax.security.auth.callback;

import java.io.Serializable;

public class NameCallback implements Callback, Serializable {
    private static final long serialVersionUID = 3770938795909392253L;
    private String defaultName;
    private String inputName;
    private String prompt;

    private void setPrompt(String str) {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("auth.14");
        }
        this.prompt = str;
    }

    private void setDefaultName(String str) {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("auth.1E");
        }
        this.defaultName = str;
    }

    public NameCallback(String str) {
        setPrompt(str);
    }

    public NameCallback(String str, String str2) {
        setPrompt(str);
        setDefaultName(str2);
    }

    public String getPrompt() {
        return this.prompt;
    }

    public String getDefaultName() {
        return this.defaultName;
    }

    public void setName(String str) {
        this.inputName = str;
    }

    public String getName() {
        return this.inputName;
    }
}
