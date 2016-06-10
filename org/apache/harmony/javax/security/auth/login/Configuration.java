package org.apache.harmony.javax.security.auth.login;

import org.apache.harmony.javax.security.auth.AuthPermission;

public abstract class Configuration {
    private static final AuthPermission GET_LOGIN_CONFIGURATION;
    private static final String LOGIN_CONFIGURATION_PROVIDER = "login.configuration.provider";
    private static final AuthPermission SET_LOGIN_CONFIGURATION;
    private static Configuration configuration;

    /* renamed from: org.apache.harmony.javax.security.auth.login.Configuration.1 */
    static class C11481 extends Configuration {
        C11481() {
        }

        public void refresh() {
        }

        public AppConfigurationEntry[] getAppConfigurationEntry(String str) {
            return new AppConfigurationEntry[0];
        }
    }

    public abstract AppConfigurationEntry[] getAppConfigurationEntry(String str);

    public abstract void refresh();

    static {
        GET_LOGIN_CONFIGURATION = new AuthPermission("getLoginConfiguration");
        SET_LOGIN_CONFIGURATION = new AuthPermission("setLoginConfiguration");
    }

    protected Configuration() {
    }

    public static Configuration getConfiguration() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(GET_LOGIN_CONFIGURATION);
        }
        return getAccessibleConfiguration();
    }

    private static final Configuration getDefaultProvider() {
        return new C11481();
    }

    static Configuration getAccessibleConfiguration() {
        Configuration configuration = configuration;
        if (configuration == null) {
            synchronized (Configuration.class) {
                if (configuration == null) {
                    configuration = getDefaultProvider();
                }
                configuration = configuration;
            }
        }
        return configuration;
    }

    public static void setConfiguration(Configuration configuration) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(SET_LOGIN_CONFIGURATION);
        }
        configuration = configuration;
    }
}
