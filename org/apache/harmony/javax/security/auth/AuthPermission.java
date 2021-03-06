package org.apache.harmony.javax.security.auth;

import java.security.BasicPermission;

public final class AuthPermission extends BasicPermission {
    private static final String CREATE_LOGIN_CONTEXT = "createLoginContext";
    private static final String CREATE_LOGIN_CONTEXT_ANY = "createLoginContext.*";
    private static final long serialVersionUID = 5806031445061587174L;

    private static String init(String str) {
        if (str == null) {
            throw new NullPointerException("auth.13");
        } else if (CREATE_LOGIN_CONTEXT.equals(str)) {
            return CREATE_LOGIN_CONTEXT_ANY;
        } else {
            return str;
        }
    }

    public AuthPermission(String str) {
        super(init(str));
    }

    public AuthPermission(String str, String str2) {
        super(init(str), str2);
    }
}
