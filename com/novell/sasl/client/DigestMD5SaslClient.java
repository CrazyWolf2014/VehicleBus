package com.novell.sasl.client;

import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.cnlaunch.framework.network.http.AsyncHttpResponseHandler;
import com.cnlaunch.x431pro.common.Constants;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.tencent.mm.sdk.platformtools.FilePathGenerator;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Map;
import org.apache.harmony.javax.security.auth.callback.Callback;
import org.apache.harmony.javax.security.auth.callback.CallbackHandler;
import org.apache.harmony.javax.security.auth.callback.NameCallback;
import org.apache.harmony.javax.security.auth.callback.PasswordCallback;
import org.apache.harmony.javax.security.sasl.RealmCallback;
import org.apache.harmony.javax.security.sasl.RealmChoiceCallback;
import org.apache.harmony.javax.security.sasl.Sasl;
import org.apache.harmony.javax.security.sasl.SaslClient;
import org.apache.harmony.javax.security.sasl.SaslException;
import org.xbill.DNS.KEYRecord;
import org.xmlpull.v1.XmlPullParser;

public class DigestMD5SaslClient implements SaslClient {
    private static final String DIGEST_METHOD = "AUTHENTICATE";
    private static final int NONCE_BYTE_COUNT = 32;
    private static final int NONCE_HEX_COUNT = 64;
    private static final int STATE_DIGEST_RESPONSE_SENT = 1;
    private static final int STATE_DISPOSED = 4;
    private static final int STATE_INITIAL = 0;
    private static final int STATE_INVALID_SERVER_RESPONSE = 3;
    private static final int STATE_VALID_SERVER_RESPONSE = 2;
    private char[] m_HA1;
    private String m_authorizationId;
    private CallbackHandler m_cbh;
    private String m_clientNonce;
    private DigestChallenge m_dc;
    private String m_digestURI;
    private String m_name;
    private Map m_props;
    private String m_protocol;
    private String m_qopValue;
    private String m_realm;
    private String m_serverName;
    private int m_state;

    public static SaslClient getClient(String str, String str2, String str3, Map map, CallbackHandler callbackHandler) {
        String str4 = (String) map.get(Sasl.QOP);
        String str5 = (String) map.get(Sasl.STRENGTH);
        str5 = (String) map.get(Sasl.SERVER_AUTH);
        if (str4 != null && !"auth".equals(str4)) {
            return null;
        }
        if (str5 != null && !"false".equals(str5)) {
            return null;
        }
        if (callbackHandler == null) {
            return null;
        }
        return new DigestMD5SaslClient(str, str2, str3, map, callbackHandler);
    }

    private DigestMD5SaslClient(String str, String str2, String str3, Map map, CallbackHandler callbackHandler) {
        this.m_authorizationId = XmlPullParser.NO_NAMESPACE;
        this.m_protocol = XmlPullParser.NO_NAMESPACE;
        this.m_serverName = XmlPullParser.NO_NAMESPACE;
        this.m_qopValue = XmlPullParser.NO_NAMESPACE;
        this.m_HA1 = null;
        this.m_clientNonce = XmlPullParser.NO_NAMESPACE;
        this.m_realm = XmlPullParser.NO_NAMESPACE;
        this.m_name = XmlPullParser.NO_NAMESPACE;
        this.m_authorizationId = str;
        this.m_protocol = str2;
        this.m_serverName = str3;
        this.m_props = map;
        this.m_cbh = callbackHandler;
        this.m_state = STATE_INITIAL;
    }

    public boolean hasInitialResponse() {
        return false;
    }

    public boolean isComplete() {
        if (this.m_state == STATE_VALID_SERVER_RESPONSE || this.m_state == STATE_INVALID_SERVER_RESPONSE || this.m_state == STATE_DISPOSED) {
            return true;
        }
        return false;
    }

    public byte[] unwrap(byte[] bArr, int i, int i2) throws SaslException {
        throw new IllegalStateException("unwrap: QOP has neither integrity nor privacy>");
    }

    public byte[] wrap(byte[] bArr, int i, int i2) throws SaslException {
        throw new IllegalStateException("wrap: QOP has neither integrity nor privacy>");
    }

    public Object getNegotiatedProperty(String str) {
        if (this.m_state != STATE_VALID_SERVER_RESPONSE) {
            throw new IllegalStateException("getNegotiatedProperty: authentication exchange not complete.");
        } else if (Sasl.QOP.equals(str)) {
            return "auth";
        } else {
            return null;
        }
    }

    public void dispose() throws SaslException {
        if (this.m_state != STATE_DISPOSED) {
            this.m_state = STATE_DISPOSED;
        }
    }

    public byte[] evaluateChallenge(byte[] bArr) throws SaslException {
        switch (this.m_state) {
            case STATE_INITIAL /*0*/:
                if (bArr.length == 0) {
                    throw new SaslException("response = byte[0]");
                }
                try {
                    byte[] bytes = createDigestResponse(bArr).getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET);
                    this.m_state = STATE_DIGEST_RESPONSE_SENT;
                    return bytes;
                } catch (Throwable e) {
                    throw new SaslException("UTF-8 encoding not suppported by platform", e);
                }
            case STATE_DIGEST_RESPONSE_SENT /*1*/:
                if (checkServerResponseAuth(bArr)) {
                    this.m_state = STATE_VALID_SERVER_RESPONSE;
                    return null;
                }
                this.m_state = STATE_INVALID_SERVER_RESPONSE;
                throw new SaslException("Could not validate response-auth value from server");
            case STATE_VALID_SERVER_RESPONSE /*2*/:
            case STATE_INVALID_SERVER_RESPONSE /*3*/:
                throw new SaslException("Authentication sequence is complete");
            case STATE_DISPOSED /*4*/:
                throw new SaslException("Client has been disposed");
            default:
                throw new SaslException("Unknown client state.");
        }
    }

    char[] convertToHex(byte[] bArr) {
        char[] cArr = new char[NONCE_BYTE_COUNT];
        for (int i = STATE_INITIAL; i < 16; i += STATE_DIGEST_RESPONSE_SENT) {
            cArr[i * STATE_VALID_SERVER_RESPONSE] = getHexChar((byte) ((bArr[i] & 240) >> STATE_DISPOSED));
            cArr[(i * STATE_VALID_SERVER_RESPONSE) + STATE_DIGEST_RESPONSE_SENT] = getHexChar((byte) (bArr[i] & 15));
        }
        return cArr;
    }

    char[] DigestCalcHA1(String str, String str2, String str3, String str4, String str5, String str6) throws SaslException {
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(str2.getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET));
            instance.update(":".getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET));
            instance.update(str3.getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET));
            instance.update(":".getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET));
            instance.update(str4.getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET));
            byte[] digest = instance.digest();
            if ("md5-sess".equals(str)) {
                instance.update(digest);
                instance.update(":".getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET));
                instance.update(str5.getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET));
                instance.update(":".getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET));
                instance.update(str6.getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET));
                digest = instance.digest();
            }
            return convertToHex(digest);
        } catch (Throwable e) {
            throw new SaslException("No provider found for MD5 hash", e);
        } catch (Throwable e2) {
            throw new SaslException("UTF-8 encoding not supported by platform.", e2);
        }
    }

    char[] DigestCalcResponse(char[] cArr, String str, String str2, String str3, String str4, String str5, String str6, boolean z) throws SaslException {
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            if (z) {
                instance.update(str5.getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET));
            }
            instance.update(":".getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET));
            instance.update(str6.getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET));
            if ("auth-int".equals(str4)) {
                instance.update(":".getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET));
                instance.update("00000000000000000000000000000000".getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET));
            }
            char[] convertToHex = convertToHex(instance.digest());
            instance.update(new String(cArr).getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET));
            instance.update(":".getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET));
            instance.update(str.getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET));
            instance.update(":".getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET));
            if (str4.length() > 0) {
                instance.update(str2.getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET));
                instance.update(":".getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET));
                instance.update(str3.getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET));
                instance.update(":".getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET));
                instance.update(str4.getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET));
                instance.update(":".getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET));
            }
            instance.update(new String(convertToHex).getBytes(AsyncHttpResponseHandler.DEFAULT_CHARSET));
            return convertToHex(instance.digest());
        } catch (Throwable e) {
            throw new SaslException("No provider found for MD5 hash", e);
        } catch (Throwable e2) {
            throw new SaslException("UTF-8 encoding not supported by platform.", e2);
        }
    }

    private String createDigestResponse(byte[] bArr) throws SaslException {
        StringBuffer stringBuffer = new StringBuffer(KEYRecord.OWNER_HOST);
        this.m_dc = new DigestChallenge(bArr);
        this.m_digestURI = this.m_protocol + FilePathGenerator.ANDROID_DIR_SEP + this.m_serverName;
        if ((this.m_dc.getQop() & STATE_DIGEST_RESPONSE_SENT) == STATE_DIGEST_RESPONSE_SENT) {
            this.m_qopValue = "auth";
            Callback[] callbackArr = new Callback[STATE_INVALID_SERVER_RESPONSE];
            ArrayList realms = this.m_dc.getRealms();
            int size = realms.size();
            if (size == 0) {
                callbackArr[STATE_INITIAL] = new RealmCallback("Realm");
            } else if (size == STATE_DIGEST_RESPONSE_SENT) {
                callbackArr[STATE_INITIAL] = new RealmCallback("Realm", (String) realms.get(STATE_INITIAL));
            } else {
                callbackArr[STATE_INITIAL] = new RealmChoiceCallback("Realm", (String[]) realms.toArray(new String[size]), STATE_INITIAL, false);
            }
            callbackArr[STATE_DIGEST_RESPONSE_SENT] = new PasswordCallback("Password", false);
            if (this.m_authorizationId == null || this.m_authorizationId.length() == 0) {
                callbackArr[STATE_VALID_SERVER_RESPONSE] = new NameCallback(Constants.VEHICLE_INI_SECTION_NAME);
            } else {
                callbackArr[STATE_VALID_SERVER_RESPONSE] = new NameCallback(Constants.VEHICLE_INI_SECTION_NAME, this.m_authorizationId);
            }
            try {
                this.m_cbh.handle(callbackArr);
                if (size > STATE_DIGEST_RESPONSE_SENT) {
                    int[] selectedIndexes = ((RealmChoiceCallback) callbackArr[STATE_INITIAL]).getSelectedIndexes();
                    if (selectedIndexes.length > 0) {
                        this.m_realm = ((RealmChoiceCallback) callbackArr[STATE_INITIAL]).getChoices()[selectedIndexes[STATE_INITIAL]];
                    } else {
                        this.m_realm = ((RealmChoiceCallback) callbackArr[STATE_INITIAL]).getChoices()[STATE_INITIAL];
                    }
                } else {
                    this.m_realm = ((RealmCallback) callbackArr[STATE_INITIAL]).getText();
                }
                this.m_clientNonce = getClientNonce();
                this.m_name = ((NameCallback) callbackArr[STATE_VALID_SERVER_RESPONSE]).getName();
                if (this.m_name == null) {
                    this.m_name = ((NameCallback) callbackArr[STATE_VALID_SERVER_RESPONSE]).getDefaultName();
                }
                if (this.m_name == null) {
                    throw new SaslException("No user name was specified.");
                }
                this.m_HA1 = DigestCalcHA1(this.m_dc.getAlgorithm(), this.m_name, this.m_realm, new String(((PasswordCallback) callbackArr[STATE_DIGEST_RESPONSE_SENT]).getPassword()), this.m_dc.getNonce(), this.m_clientNonce);
                char[] DigestCalcResponse = DigestCalcResponse(this.m_HA1, this.m_dc.getNonce(), "00000001", this.m_clientNonce, this.m_qopValue, DIGEST_METHOD, this.m_digestURI, true);
                stringBuffer.append("username=\"");
                stringBuffer.append(this.m_authorizationId);
                if (this.m_realm.length() != 0) {
                    stringBuffer.append("\",realm=\"");
                    stringBuffer.append(this.m_realm);
                }
                stringBuffer.append("\",cnonce=\"");
                stringBuffer.append(this.m_clientNonce);
                stringBuffer.append("\",nc=");
                stringBuffer.append("00000001");
                stringBuffer.append(",qop=");
                stringBuffer.append(this.m_qopValue);
                stringBuffer.append(",digest-uri=\"");
                stringBuffer.append(this.m_digestURI);
                stringBuffer.append("\",response=");
                stringBuffer.append(DigestCalcResponse);
                stringBuffer.append(",charset=utf-8,nonce=\"");
                stringBuffer.append(this.m_dc.getNonce());
                stringBuffer.append("\"");
                return stringBuffer.toString();
            } catch (Throwable e) {
                throw new SaslException("Handler does not support necessary callbacks", e);
            } catch (Throwable e2) {
                throw new SaslException("IO exception in CallbackHandler.", e2);
            }
        }
        throw new SaslException("Client only supports qop of 'auth'");
    }

    boolean checkServerResponseAuth(byte[] bArr) throws SaslException {
        return new String(DigestCalcResponse(this.m_HA1, this.m_dc.getNonce(), "00000001", this.m_clientNonce, this.m_qopValue, DIGEST_METHOD, this.m_digestURI, false)).equals(new ResponseAuth(bArr).getResponseValue());
    }

    private static char getHexChar(byte b) {
        switch (b) {
            case STATE_INITIAL /*0*/:
                return '0';
            case STATE_DIGEST_RESPONSE_SENT /*1*/:
                return '1';
            case STATE_VALID_SERVER_RESPONSE /*2*/:
                return '2';
            case STATE_INVALID_SERVER_RESPONSE /*3*/:
                return '3';
            case STATE_DISPOSED /*4*/:
                return '4';
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                return '5';
            case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                return '6';
            case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                return '7';
            case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                return '8';
            case FileOptions.OPTIMIZE_FOR_FIELD_NUMBER /*9*/:
                return '9';
            case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
                return 'a';
            case TrafficIncident.LOCATION_FIELD_NUMBER /*11*/:
                return 'b';
            case TrafficIncident.VERTEXOFFSET_FIELD_NUMBER /*12*/:
                return 'c';
            case TrafficIncident.INCIDENTVERTEX_FIELD_NUMBER /*13*/:
                return 'd';
            case TrafficIncident.STARTTIME_FIELD_NUMBER /*14*/:
                return 'e';
            case TrafficIncident.ENDTIME_FIELD_NUMBER /*15*/:
                return 'f';
            default:
                return 'Z';
        }
    }

    String getClientNonce() throws SaslException {
        byte[] bArr = new byte[NONCE_BYTE_COUNT];
        char[] cArr = new char[NONCE_HEX_COUNT];
        try {
            SecureRandom.getInstance("SHA1PRNG").nextBytes(bArr);
            for (int i = STATE_INITIAL; i < NONCE_BYTE_COUNT; i += STATE_DIGEST_RESPONSE_SENT) {
                cArr[i * STATE_VALID_SERVER_RESPONSE] = getHexChar((byte) (bArr[i] & 15));
                cArr[(i * STATE_VALID_SERVER_RESPONSE) + STATE_DIGEST_RESPONSE_SENT] = getHexChar((byte) ((bArr[i] & 240) >> STATE_DISPOSED));
            }
            return new String(cArr);
        } catch (Throwable e) {
            throw new SaslException("No random number generator available", e);
        }
    }

    public String getMechanismName() {
        return "DIGEST-MD5";
    }
}
