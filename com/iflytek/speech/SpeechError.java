package com.iflytek.speech;

import com.iflytek.p007c.C0255a;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import org.xmlpull.v1.XmlPullParser;

public class SpeechError extends Exception {
    public static final int ERROR_AUDIO_RECORD = 9;
    public static final int ERROR_BROWSER_NOT_INSTALLED = 26;
    public static final int ERROR_CLIENT = 8;
    public static final int ERROR_EMPTY_UTTERANCE = 13;
    public static final int ERROR_FILE_ACCESS = 14;
    public static final int ERROR_INSUFFICIENT_PERMISSIONS = 4;
    public static final int ERROR_INVALID_DATA = 20;
    public static final int ERROR_INVALID_ENCODING = 12;
    public static final int ERROR_INVALID_GRAMMAR = 21;
    public static final int ERROR_INVALID_LOCAL_RESOURCE = 22;
    public static final int ERROR_INVALID_PARAM = 7;
    public static final int ERROR_INVALID_RESULT = 5;
    public static final int ERROR_IN_USE = 19;
    public static final int ERROR_LOGIN = 18;
    public static final int ERROR_LOGIN_INVALID_PWD = 24;
    public static final int ERROR_LOGIN_INVALID_USER = 23;
    public static final int ERROR_MEMORY_WRANING = 16;
    public static final int ERROR_NETWORK_TIMEOUT = 2;
    public static final int ERROR_NET_EXPECTION = 3;
    public static final int ERROR_NO_MATCH = 10;
    public static final int ERROR_NO_NETWORK = 1;
    public static final int ERROR_PERMISSION_DENIED = 25;
    public static final int ERROR_PLAY_MEDIA = 15;
    public static final int ERROR_SERVER_CONNECT = 6;
    public static final int ERROR_SPEECH_TIMEOUT = 11;
    public static final int ERROR_TEXT_OVERFLOW = 17;
    public static final int ERROR_TTS_INTERRUPT = 27;
    public static final int UNKNOWN = 30000;
    private static final long serialVersionUID = 4434424251478985596L;
    private int f1110a;
    private int f1111b;
    private String f1112c;

    /* renamed from: com.iflytek.speech.SpeechError.a */
    public enum C0291a {
        NETSET,
        RETRY,
        MORE,
        CANCEL
    }

    public SpeechError(int i, int i2) {
        this.f1110a = 0;
        this.f1111b = 0;
        this.f1112c = XmlPullParser.NO_NAMESPACE;
        this.f1110a = i;
        this.f1111b = i2;
        if (this.f1111b == UNKNOWN) {
            this.f1111b = i + BaseImageDownloader.DEFAULT_HTTP_READ_TIMEOUT;
        }
        if (this.f1110a == UNKNOWN) {
            this.f1110a = ERROR_NETWORK_TIMEOUT;
            if (this.f1111b == 10118) {
                this.f1110a = ERROR_SPEECH_TIMEOUT;
            } else if (10106 == this.f1111b || 10107 == this.f1111b || 10124 == this.f1111b) {
                this.f1110a = ERROR_INVALID_PARAM;
            } else if (this.f1111b >= 10200 && this.f1111b < 10300) {
                this.f1110a = ERROR_NET_EXPECTION;
            } else if (this.f1111b == 10117 || this.f1111b == 10101) {
                this.f1110a = ERROR_MEMORY_WRANING;
            } else if (this.f1111b == 10113) {
                this.f1110a = ERROR_TEXT_OVERFLOW;
            } else if (this.f1111b >= 10400 && this.f1111b <= 10407) {
                this.f1110a = ERROR_LOGIN;
            } else if (this.f1111b < SpeechConfig.Rate11K || this.f1111b >= 11100) {
                if (this.f1111b == 10129) {
                    this.f1110a = ERROR_IN_USE;
                } else if (this.f1111b == 10109) {
                    this.f1110a = ERROR_INVALID_DATA;
                } else if (this.f1111b == 10702) {
                    this.f1110a = ERROR_INVALID_GRAMMAR;
                } else if (this.f1111b >= 10500 && this.f1111b < 10600) {
                    this.f1110a = ERROR_INVALID_LOCAL_RESOURCE;
                } else if (this.f1111b >= 11200 && this.f1111b <= 11205) {
                    this.f1110a = ERROR_PERMISSION_DENIED;
                }
            } else if (this.f1111b == 11005) {
                this.f1110a = ERROR_LOGIN_INVALID_USER;
            } else if (this.f1111b == 11006) {
                this.f1110a = ERROR_LOGIN_INVALID_PWD;
            } else {
                this.f1110a = ERROR_LOGIN;
            }
        }
        this.f1112c = C0255a.m1150c(this.f1110a);
    }

    public SpeechError(Exception exception) {
        this.f1110a = 0;
        this.f1111b = 0;
        this.f1112c = XmlPullParser.NO_NAMESPACE;
        this.f1111b = UNKNOWN;
        this.f1110a = ERROR_CLIENT;
        this.f1112c = exception.toString();
    }

    public static String appendErrorCodeDes(String str, int i) {
        return ((str + "<br>") + "(" + C0255a.m1151d(0) + ":") + i + ")";
    }

    public int getErrorCode() {
        return this.f1111b;
    }

    public String getErrorDesc() {
        return this.f1112c;
    }

    public String getErrorDescription(boolean z) {
        String str = this.f1112c + ".";
        return z ? appendErrorCodeDes(str, this.f1111b) : str;
    }

    public int getErrorType() {
        return this.f1110a;
    }

    public C0291a getOperation() {
        C0291a c0291a = C0291a.RETRY;
        switch (this.f1110a) {
            case ERROR_NO_NETWORK /*1*/:
                return C0291a.NETSET;
            case ERROR_NETWORK_TIMEOUT /*2*/:
            case ERROR_NET_EXPECTION /*3*/:
            case ERROR_INVALID_RESULT /*5*/:
                return C0291a.MORE;
            case ERROR_EMPTY_UTTERANCE /*13*/:
                return C0291a.CANCEL;
            default:
                return c0291a;
        }
    }

    public String toString() {
        return getErrorDescription(true);
    }
}
