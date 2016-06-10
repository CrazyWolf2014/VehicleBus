package org.jivesoftware.smack.packet;

import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.util.StringUtils;
import org.xmlpull.v1.XmlPullParser;

public class Authentication extends IQ {
    private String digest;
    private String password;
    private String resource;
    private String username;

    public Authentication() {
        this.username = null;
        this.password = null;
        this.digest = null;
        this.resource = null;
        setType(Type.SET);
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String str) {
        this.username = str;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String str) {
        this.password = str;
    }

    public String getDigest() {
        return this.digest;
    }

    public void setDigest(String str, String str2) {
        this.digest = StringUtils.hash(str + str2);
    }

    public void setDigest(String str) {
        this.digest = str;
    }

    public String getResource() {
        return this.resource;
    }

    public void setResource(String str) {
        this.resource = str;
    }

    public String getChildElementXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<query xmlns=\"jabber:iq:auth\">");
        if (this.username != null) {
            if (this.username.equals(XmlPullParser.NO_NAMESPACE)) {
                stringBuilder.append("<username/>");
            } else {
                stringBuilder.append("<username>").append(this.username).append("</username>");
            }
        }
        if (this.digest != null) {
            if (this.digest.equals(XmlPullParser.NO_NAMESPACE)) {
                stringBuilder.append("<digest/>");
            } else {
                stringBuilder.append("<digest>").append(this.digest).append("</digest>");
            }
        }
        if (this.password != null && this.digest == null) {
            if (this.password.equals(XmlPullParser.NO_NAMESPACE)) {
                stringBuilder.append("<password/>");
            } else {
                stringBuilder.append("<password>").append(StringUtils.escapeForXML(this.password)).append("</password>");
            }
        }
        if (this.resource != null) {
            if (this.resource.equals(XmlPullParser.NO_NAMESPACE)) {
                stringBuilder.append("<resource/>");
            } else {
                stringBuilder.append("<resource>").append(this.resource).append("</resource>");
            }
        }
        stringBuilder.append("</query>");
        return stringBuilder.toString();
    }
}
