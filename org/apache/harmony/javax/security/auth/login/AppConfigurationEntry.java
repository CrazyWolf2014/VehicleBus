package org.apache.harmony.javax.security.auth.login;

import java.util.Collections;
import java.util.Map;

public class AppConfigurationEntry {
    private final LoginModuleControlFlag controlFlag;
    private final String loginModuleName;
    private final Map<String, ?> options;

    public static class LoginModuleControlFlag {
        public static final LoginModuleControlFlag OPTIONAL;
        public static final LoginModuleControlFlag REQUIRED;
        public static final LoginModuleControlFlag REQUISITE;
        public static final LoginModuleControlFlag SUFFICIENT;
        private final String flag;

        static {
            REQUIRED = new LoginModuleControlFlag("LoginModuleControlFlag: required");
            REQUISITE = new LoginModuleControlFlag("LoginModuleControlFlag: requisite");
            OPTIONAL = new LoginModuleControlFlag("LoginModuleControlFlag: optional");
            SUFFICIENT = new LoginModuleControlFlag("LoginModuleControlFlag: sufficient");
        }

        private LoginModuleControlFlag(String str) {
            this.flag = str;
        }

        public String toString() {
            return this.flag;
        }
    }

    public AppConfigurationEntry(String str, LoginModuleControlFlag loginModuleControlFlag, Map<String, ?> map) {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("auth.26");
        } else if (loginModuleControlFlag == null) {
            throw new IllegalArgumentException("auth.27");
        } else if (map == null) {
            throw new IllegalArgumentException("auth.1A");
        } else {
            this.loginModuleName = str;
            this.controlFlag = loginModuleControlFlag;
            this.options = Collections.unmodifiableMap(map);
        }
    }

    public String getLoginModuleName() {
        return this.loginModuleName;
    }

    public LoginModuleControlFlag getControlFlag() {
        return this.controlFlag;
    }

    public Map<String, ?> getOptions() {
        return this.options;
    }
}
