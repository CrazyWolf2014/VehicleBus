package org.apache.qpid.management.common.sasl;

import org.apache.harmony.javax.security.auth.callback.Callback;
import org.apache.harmony.javax.security.auth.callback.CallbackHandler;
import org.apache.harmony.javax.security.auth.callback.NameCallback;
import org.apache.harmony.javax.security.auth.callback.PasswordCallback;
import org.apache.harmony.javax.security.sasl.Sasl;
import org.apache.harmony.javax.security.sasl.SaslClient;
import org.apache.harmony.javax.security.sasl.SaslException;

public class PlainSaslClient implements SaslClient {
    private static byte SEPARATOR;
    private String authenticationID;
    private String authorizationID;
    private CallbackHandler cbh;
    private boolean completed;
    private byte[] password;

    static {
        SEPARATOR = (byte) 0;
    }

    public PlainSaslClient(String str, CallbackHandler callbackHandler) throws SaslException {
        this.completed = false;
        this.cbh = callbackHandler;
        Object[] userInfo = getUserInfo();
        this.authorizationID = str;
        this.authenticationID = (String) userInfo[0];
        this.password = (byte[]) userInfo[1];
        if (this.authenticationID == null || this.password == null) {
            throw new SaslException("PLAIN: authenticationID and password must be specified");
        }
    }

    public byte[] evaluateChallenge(byte[] bArr) throws SaslException {
        int i = 0;
        if (this.completed) {
            throw new IllegalStateException("PLAIN: authentication already completed");
        }
        this.completed = true;
        try {
            int length;
            Object bytes = this.authorizationID == null ? null : this.authorizationID.getBytes("UTF8");
            Object bytes2 = this.authenticationID.getBytes("UTF8");
            int length2 = (this.password.length + bytes2.length) + 2;
            if (bytes != null) {
                length = bytes.length;
            } else {
                length = 0;
            }
            Object obj = new byte[(length + length2)];
            if (bytes != null) {
                System.arraycopy(bytes, 0, obj, 0, bytes.length);
                i = bytes.length;
            }
            int i2 = i + 1;
            obj[i] = SEPARATOR;
            System.arraycopy(bytes2, 0, obj, i2, bytes2.length);
            i = bytes2.length + i2;
            i2 = i + 1;
            obj[i] = SEPARATOR;
            System.arraycopy(this.password, 0, obj, i2, this.password.length);
            clearPassword();
            return obj;
        } catch (Throwable e) {
            throw new SaslException("PLAIN: Cannot get UTF-8 encoding of ids", e);
        }
    }

    public String getMechanismName() {
        return Constants.MECH_PLAIN;
    }

    public boolean hasInitialResponse() {
        return true;
    }

    public boolean isComplete() {
        return this.completed;
    }

    public byte[] unwrap(byte[] bArr, int i, int i2) throws SaslException {
        if (this.completed) {
            throw new IllegalStateException("PLAIN: this mechanism supports neither integrity nor privacy");
        }
        throw new IllegalStateException("PLAIN: authentication not completed");
    }

    public byte[] wrap(byte[] bArr, int i, int i2) throws SaslException {
        if (this.completed) {
            throw new IllegalStateException("PLAIN: this mechanism supports neither integrity nor privacy");
        }
        throw new IllegalStateException("PLAIN: authentication not completed");
    }

    public Object getNegotiatedProperty(String str) {
        if (!this.completed) {
            throw new IllegalStateException("PLAIN: authentication not completed");
        } else if (str.equals(Sasl.QOP)) {
            return "auth";
        } else {
            return null;
        }
    }

    private void clearPassword() {
        if (this.password != null) {
            for (int i = 0; i < this.password.length; i++) {
                this.password[i] = (byte) 0;
            }
            this.password = null;
        }
    }

    public void dispose() throws SaslException {
        clearPassword();
    }

    protected void finalize() {
        clearPassword();
    }

    private Object[] getUserInfo() throws SaslException {
        try {
            byte[] bytes;
            NameCallback nameCallback = new NameCallback("PLAIN authentication id: ");
            PasswordCallback passwordCallback = new PasswordCallback("PLAIN password: ", false);
            this.cbh.handle(new Callback[]{nameCallback, passwordCallback});
            String name = nameCallback.getName();
            char[] password = passwordCallback.getPassword();
            if (password != null) {
                bytes = new String(password).getBytes("UTF8");
                passwordCallback.clearPassword();
            } else {
                bytes = null;
            }
            return new Object[]{name, bytes};
        } catch (Throwable e) {
            throw new SaslException("Cannot get password", e);
        } catch (Throwable e2) {
            throw new SaslException("Cannot get userid/password", e2);
        }
    }
}
