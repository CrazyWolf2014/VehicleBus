package org.jivesoftware.smack.sasl;

import com.ifoer.entity.Constant;
import de.measite.smack.Sasl;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import org.apache.harmony.javax.security.auth.callback.CallbackHandler;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.Base64;
import org.xmlpull.v1.XmlPullParser;

public class SASLFacebookConnect extends SASLMechanism {
    private String apiKey;
    private String sessionKey;
    private String sessionSecret;

    /* renamed from: org.jivesoftware.smack.sasl.SASLFacebookConnect.1 */
    class C11631 extends Packet {
        final /* synthetic */ StringBuilder val$stanza;

        C11631(StringBuilder stringBuilder) {
            this.val$stanza = stringBuilder;
        }

        public String toXML() {
            return this.val$stanza.toString();
        }
    }

    /* renamed from: org.jivesoftware.smack.sasl.SASLFacebookConnect.2 */
    class C11642 extends Packet {
        final /* synthetic */ StringBuilder val$stanza;

        C11642(StringBuilder stringBuilder) {
            this.val$stanza = stringBuilder;
        }

        public String toXML() {
            return this.val$stanza.toString();
        }
    }

    static {
        SASLAuthentication.registerSASLMechanism("X-FACEBOOK-PLATFORM", SASLFacebookConnect.class);
        SASLAuthentication.supportSASLMechanism("X-FACEBOOK-PLATFORM", 0);
    }

    public SASLFacebookConnect(SASLAuthentication sASLAuthentication) {
        super(sASLAuthentication);
        this.sessionKey = XmlPullParser.NO_NAMESPACE;
        this.sessionSecret = XmlPullParser.NO_NAMESPACE;
        this.apiKey = XmlPullParser.NO_NAMESPACE;
    }

    protected void authenticate() throws IOException, XMPPException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<auth mechanism=\"").append(getName());
        stringBuilder.append("\" xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\">");
        stringBuilder.append("</auth>");
        getSASLAuthentication().send(new C11631(stringBuilder));
    }

    public void authenticate(String str, String str2, String str3) throws IOException, XMPPException {
        if (str == null || str3 == null) {
            throw new IllegalStateException("Invalid parameters!");
        }
        String[] split = str.split("\\|");
        if (split == null || split.length != 2) {
            throw new IllegalStateException("Api key or session key is not present!");
        }
        this.apiKey = split[0];
        this.sessionKey = split[1];
        this.sessionSecret = str3;
        this.authenticationId = this.sessionKey;
        this.password = str3;
        this.hostname = str2;
        this.sc = Sasl.createSaslClient(new String[]{"DIGEST-MD5"}, null, "xmpp", str2, new HashMap(), this);
        authenticate();
    }

    public void authenticate(String str, String str2, CallbackHandler callbackHandler) throws IOException, XMPPException {
        this.sc = Sasl.createSaslClient(new String[]{"DIGEST-MD5"}, null, "xmpp", str2, new HashMap(), callbackHandler);
        authenticate();
    }

    protected String getName() {
        return "X-FACEBOOK-PLATFORM";
    }

    public void challengeReceived(String str) throws IOException {
        String str2;
        String str3;
        StringBuilder stringBuilder = new StringBuilder();
        byte[] bArr = null;
        if (str != null) {
            Map queryMap = getQueryMap(new String(Base64.decode(str)));
            String str4 = Constant.APP_VERSION;
            str2 = (String) queryMap.get("nonce");
            str3 = (String) queryMap.get("method");
            Long valueOf = Long.valueOf(new GregorianCalendar().getTimeInMillis() / 1000);
            try {
                bArr = ("api_key=" + this.apiKey + AlixDefine.split + "call_id=" + valueOf + AlixDefine.split + "method=" + str3 + AlixDefine.split + "nonce=" + str2 + AlixDefine.split + "session_key=" + this.sessionKey + AlixDefine.split + "v=" + str4 + AlixDefine.split + "sig=" + MD5("api_key=" + this.apiKey + "call_id=" + valueOf + "method=" + str3 + "nonce=" + str2 + "session_key=" + this.sessionKey + "v=" + str4 + this.sessionSecret)).getBytes();
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }
        str3 = XmlPullParser.NO_NAMESPACE;
        if (bArr != null) {
            str2 = Base64.encodeBytes(bArr, 8);
        } else {
            str2 = str3;
        }
        stringBuilder.append("<response xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\">");
        stringBuilder.append(str2);
        stringBuilder.append("</response>");
        getSASLAuthentication().send(new C11642(stringBuilder));
    }

    private Map<String, String> getQueryMap(String str) {
        String[] split = str.split(AlixDefine.split);
        Map<String, String> hashMap = new HashMap();
        for (String str2 : split) {
            hashMap.put(str2.split("=")[0], str2.split("=")[1]);
        }
        return hashMap;
    }

    private String convertToHex(byte[] bArr) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < bArr.length; i++) {
            int i2 = (bArr[i] >>> 4) & 15;
            int i3 = 0;
            while (true) {
                if (i2 < 0 || i2 > 9) {
                    stringBuffer.append((char) ((i2 - 10) + 97));
                } else {
                    stringBuffer.append((char) (i2 + 48));
                }
                int i4 = bArr[i] & 15;
                i2 = i3 + 1;
                if (i3 >= 1) {
                    break;
                }
                i3 = i2;
                i2 = i4;
            }
        }
        return stringBuffer.toString();
    }

    public String MD5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest instance = MessageDigest.getInstance("MD5");
        byte[] bArr = new byte[32];
        instance.update(str.getBytes("iso-8859-1"), 0, str.length());
        return convertToHex(instance.digest());
    }
}
