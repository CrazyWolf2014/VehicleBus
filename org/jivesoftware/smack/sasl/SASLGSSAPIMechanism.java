package org.jivesoftware.smack.sasl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.harmony.javax.security.auth.callback.CallbackHandler;
import org.apache.harmony.javax.security.sasl.Sasl;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPException;

public class SASLGSSAPIMechanism extends SASLMechanism {
    public SASLGSSAPIMechanism(SASLAuthentication sASLAuthentication) {
        super(sASLAuthentication);
        System.setProperty("javax.security.auth.useSubjectCredsOnly", "false");
        System.setProperty("java.security.auth.login.config", "gss.conf");
    }

    protected String getName() {
        return "GSSAPI";
    }

    public void authenticate(String str, String str2, CallbackHandler callbackHandler) throws IOException, XMPPException {
        String[] strArr = new String[]{getName()};
        Map hashMap = new HashMap();
        hashMap.put(Sasl.SERVER_AUTH, "TRUE");
        this.sc = de.measite.smack.Sasl.createSaslClient(strArr, str, "xmpp", str2, hashMap, callbackHandler);
        authenticate();
    }

    public void authenticate(String str, String str2, String str3) throws IOException, XMPPException {
        String[] strArr = new String[]{getName()};
        Map hashMap = new HashMap();
        hashMap.put(Sasl.SERVER_AUTH, "TRUE");
        this.sc = de.measite.smack.Sasl.createSaslClient(strArr, str, "xmpp", str2, hashMap, this);
        authenticate();
    }
}
