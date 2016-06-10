package org.jivesoftware.smackx.packet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.codehaus.jackson.org.objectweb.asm.signature.SignatureVisitor;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.packet.XMPPError.Condition;
import org.jivesoftware.smack.util.StringUtils;

public class VCard extends IQ {
    private String avatar;
    private String emailHome;
    private String emailWork;
    private String firstName;
    private Map<String, String> homeAddr;
    private Map<String, String> homePhones;
    private String lastName;
    private String middleName;
    private String organization;
    private String organizationUnit;
    private Map<String, String> otherSimpleFields;
    private Map<String, String> otherUnescapableFields;
    private Map<String, String> workAddr;
    private Map<String, String> workPhones;

    private interface ContentBuilder {
        void addTagContent();
    }

    private class VCardWriter {
        private final StringBuilder sb;

        /* renamed from: org.jivesoftware.smackx.packet.VCard.VCardWriter.1 */
        class C12081 implements ContentBuilder {
            C12081() {
            }

            public void addTagContent() {
                VCardWriter.this.buildActualContent();
            }
        }

        /* renamed from: org.jivesoftware.smackx.packet.VCard.VCardWriter.2 */
        class C12092 implements ContentBuilder {
            final /* synthetic */ String val$email;
            final /* synthetic */ String val$type;

            C12092(String str, String str2) {
                this.val$type = str;
                this.val$email = str2;
            }

            public void addTagContent() {
                VCardWriter.this.appendEmptyTag(this.val$type);
                VCardWriter.this.appendEmptyTag("INTERNET");
                VCardWriter.this.appendEmptyTag("PREF");
                VCardWriter.this.appendTag("USERID", StringUtils.escapeForXML(this.val$email));
            }
        }

        /* renamed from: org.jivesoftware.smackx.packet.VCard.VCardWriter.3 */
        class C12103 implements ContentBuilder {
            final /* synthetic */ String val$code;
            final /* synthetic */ Entry val$entry;

            C12103(Entry entry, String str) {
                this.val$entry = entry;
                this.val$code = str;
            }

            public void addTagContent() {
                VCardWriter.this.appendEmptyTag(this.val$entry.getKey());
                VCardWriter.this.appendEmptyTag(this.val$code);
                VCardWriter.this.appendTag("NUMBER", StringUtils.escapeForXML((String) this.val$entry.getValue()));
            }
        }

        /* renamed from: org.jivesoftware.smackx.packet.VCard.VCardWriter.4 */
        class C12114 implements ContentBuilder {
            final /* synthetic */ Map val$addr;
            final /* synthetic */ String val$code;

            C12114(String str, Map map) {
                this.val$code = str;
                this.val$addr = map;
            }

            public void addTagContent() {
                VCardWriter.this.appendEmptyTag(this.val$code);
                for (Entry entry : this.val$addr.entrySet()) {
                    VCardWriter.this.appendTag((String) entry.getKey(), StringUtils.escapeForXML((String) entry.getValue()));
                }
            }
        }

        /* renamed from: org.jivesoftware.smackx.packet.VCard.VCardWriter.5 */
        class C12125 implements ContentBuilder {
            C12125() {
            }

            public void addTagContent() {
                VCardWriter.this.appendTag("ORGNAME", StringUtils.escapeForXML(VCard.this.organization));
                VCardWriter.this.appendTag("ORGUNIT", StringUtils.escapeForXML(VCard.this.organizationUnit));
            }
        }

        /* renamed from: org.jivesoftware.smackx.packet.VCard.VCardWriter.6 */
        class C12136 implements ContentBuilder {
            C12136() {
            }

            public void addTagContent() {
                VCardWriter.this.appendTag("FAMILY", StringUtils.escapeForXML(VCard.this.lastName));
                VCardWriter.this.appendTag("GIVEN", StringUtils.escapeForXML(VCard.this.firstName));
                VCardWriter.this.appendTag("MIDDLE", StringUtils.escapeForXML(VCard.this.middleName));
            }
        }

        /* renamed from: org.jivesoftware.smackx.packet.VCard.VCardWriter.7 */
        class C12147 implements ContentBuilder {
            final /* synthetic */ String val$tagText;

            C12147(String str) {
                this.val$tagText = str;
            }

            public void addTagContent() {
                VCardWriter.this.sb.append(this.val$tagText.trim());
            }
        }

        VCardWriter(StringBuilder stringBuilder) {
            this.sb = stringBuilder;
        }

        public void write() {
            appendTag("vCard", "xmlns", "vcard-temp", VCard.this.hasContent(), new C12081());
        }

        private void buildActualContent() {
            if (VCard.this.hasNameField()) {
                appendN();
            }
            appendOrganization();
            appendGenericFields();
            appendEmail(VCard.this.emailWork, "WORK");
            appendEmail(VCard.this.emailHome, "HOME");
            appendPhones(VCard.this.workPhones, "WORK");
            appendPhones(VCard.this.homePhones, "HOME");
            appendAddress(VCard.this.workAddr, "WORK");
            appendAddress(VCard.this.homeAddr, "HOME");
        }

        private void appendEmail(String str, String str2) {
            if (str != null) {
                appendTag("EMAIL", true, new C12092(str2, str));
            }
        }

        private void appendPhones(Map<String, String> map, String str) {
            for (Entry c12103 : map.entrySet()) {
                appendTag("TEL", true, new C12103(c12103, str));
            }
        }

        private void appendAddress(Map<String, String> map, String str) {
            if (map.size() > 0) {
                appendTag("ADR", true, new C12114(str, map));
            }
        }

        private void appendEmptyTag(Object obj) {
            this.sb.append('<').append(obj).append("/>");
        }

        private void appendGenericFields() {
            for (Entry entry : VCard.this.otherSimpleFields.entrySet()) {
                appendTag(((String) entry.getKey()).toString(), StringUtils.escapeForXML((String) entry.getValue()));
            }
            for (Entry entry2 : VCard.this.otherUnescapableFields.entrySet()) {
                appendTag(((String) entry2.getKey()).toString(), (String) entry2.getValue());
            }
        }

        private void appendOrganization() {
            if (VCard.this.hasOrganizationFields()) {
                appendTag("ORG", true, new C12125());
            }
        }

        private void appendN() {
            appendTag("N", true, new C12136());
        }

        private void appendTag(String str, String str2, String str3, boolean z, ContentBuilder contentBuilder) {
            this.sb.append('<').append(str);
            if (str2 != null) {
                this.sb.append(' ').append(str2).append(SignatureVisitor.INSTANCEOF).append('\'').append(str3).append('\'');
            }
            if (z) {
                this.sb.append('>');
                contentBuilder.addTagContent();
                this.sb.append("</").append(str).append(">\n");
                return;
            }
            this.sb.append("/>\n");
        }

        private void appendTag(String str, boolean z, ContentBuilder contentBuilder) {
            appendTag(str, null, null, z, contentBuilder);
        }

        private void appendTag(String str, String str2) {
            if (str2 != null) {
                appendTag(str, true, new C12147(str2));
            }
        }
    }

    public VCard() {
        this.homePhones = new HashMap();
        this.workPhones = new HashMap();
        this.homeAddr = new HashMap();
        this.workAddr = new HashMap();
        this.otherSimpleFields = new HashMap();
        this.otherUnescapableFields = new HashMap();
    }

    public String getField(String str) {
        return (String) this.otherSimpleFields.get(str);
    }

    public void setField(String str, String str2) {
        setField(str, str2, false);
    }

    public void setField(String str, String str2, boolean z) {
        if (z) {
            this.otherUnescapableFields.put(str, str2);
        } else {
            this.otherSimpleFields.put(str, str2);
        }
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String str) {
        this.firstName = str;
        updateFN();
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String str) {
        this.lastName = str;
        updateFN();
    }

    public String getMiddleName() {
        return this.middleName;
    }

    public void setMiddleName(String str) {
        this.middleName = str;
        updateFN();
    }

    public String getNickName() {
        return (String) this.otherSimpleFields.get("NICKNAME");
    }

    public void setNickName(String str) {
        this.otherSimpleFields.put("NICKNAME", str);
    }

    public String getEmailHome() {
        return this.emailHome;
    }

    public void setEmailHome(String str) {
        this.emailHome = str;
    }

    public String getEmailWork() {
        return this.emailWork;
    }

    public void setEmailWork(String str) {
        this.emailWork = str;
    }

    public String getJabberId() {
        return (String) this.otherSimpleFields.get("JABBERID");
    }

    public void setJabberId(String str) {
        this.otherSimpleFields.put("JABBERID", str);
    }

    public String getOrganization() {
        return this.organization;
    }

    public void setOrganization(String str) {
        this.organization = str;
    }

    public String getOrganizationUnit() {
        return this.organizationUnit;
    }

    public void setOrganizationUnit(String str) {
        this.organizationUnit = str;
    }

    public String getAddressFieldHome(String str) {
        return (String) this.homeAddr.get(str);
    }

    public void setAddressFieldHome(String str, String str2) {
        this.homeAddr.put(str, str2);
    }

    public String getAddressFieldWork(String str) {
        return (String) this.workAddr.get(str);
    }

    public void setAddressFieldWork(String str, String str2) {
        this.workAddr.put(str, str2);
    }

    public void setPhoneHome(String str, String str2) {
        this.homePhones.put(str, str2);
    }

    public String getPhoneHome(String str) {
        return (String) this.homePhones.get(str);
    }

    public void setPhoneWork(String str, String str2) {
        this.workPhones.put(str, str2);
    }

    public String getPhoneWork(String str) {
        return (String) this.workPhones.get(str);
    }

    public void setAvatar(URL url) {
        byte[] bArr = new byte[0];
        try {
            bArr = getBytes(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setAvatar(bArr);
    }

    public void setAvatar(byte[] bArr) {
        if (bArr == null) {
            this.otherUnescapableFields.remove("PHOTO");
            return;
        }
        String encodeBase64 = StringUtils.encodeBase64(bArr);
        this.avatar = encodeBase64;
        setField("PHOTO", "<TYPE>image/jpeg</TYPE><BINVAL>" + encodeBase64 + "</BINVAL>", true);
    }

    public void setAvatar(byte[] bArr, String str) {
        if (bArr == null) {
            this.otherUnescapableFields.remove("PHOTO");
            return;
        }
        String encodeBase64 = StringUtils.encodeBase64(bArr);
        this.avatar = encodeBase64;
        setField("PHOTO", "<TYPE>" + str + "</TYPE><BINVAL>" + encodeBase64 + "</BINVAL>", true);
    }

    public void setEncodedImage(String str) {
        this.avatar = str;
    }

    public byte[] getAvatar() {
        if (this.avatar == null) {
            return null;
        }
        return StringUtils.decodeBase64(this.avatar);
    }

    public static byte[] getBytes(URL url) throws IOException {
        File file = new File(url.getPath());
        if (file.exists()) {
            return getFileBytes(file);
        }
        return null;
    }

    private static byte[] getFileBytes(File file) throws IOException {
        Throwable th;
        BufferedInputStream bufferedInputStream;
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            try {
                byte[] bArr = new byte[((int) file.length())];
                if (bufferedInputStream.read(bArr) != bArr.length) {
                    throw new IOException("Entire file not read");
                }
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                return bArr;
            } catch (Throwable th2) {
                th = th2;
                if (bufferedInputStream != null) {
                    bufferedInputStream.close();
                }
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            bufferedInputStream = null;
            if (bufferedInputStream != null) {
                bufferedInputStream.close();
            }
            throw th;
        }
    }

    public String getAvatarHash() {
        String str = null;
        byte[] avatar = getAvatar();
        if (avatar == null) {
            return str;
        }
        try {
            MessageDigest instance = MessageDigest.getInstance("SHA-1");
            instance.update(avatar);
            return StringUtils.encodeHex(instance.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return str;
        }
    }

    private void updateFN() {
        StringBuilder stringBuilder = new StringBuilder();
        if (this.firstName != null) {
            stringBuilder.append(StringUtils.escapeForXML(this.firstName)).append(' ');
        }
        if (this.middleName != null) {
            stringBuilder.append(StringUtils.escapeForXML(this.middleName)).append(' ');
        }
        if (this.lastName != null) {
            stringBuilder.append(StringUtils.escapeForXML(this.lastName));
        }
        setField("FN", stringBuilder.toString());
    }

    public void save(Connection connection) throws XMPPException {
        checkAuthenticated(connection, true);
        setType(Type.SET);
        setFrom(connection.getUser());
        PacketCollector createPacketCollector = connection.createPacketCollector(new PacketIDFilter(getPacketID()));
        connection.sendPacket(this);
        Packet nextResult = createPacketCollector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
        createPacketCollector.cancel();
        if (nextResult == null) {
            throw new XMPPException("No response from server on status set.");
        } else if (nextResult.getError() != null) {
            throw new XMPPException(nextResult.getError());
        }
    }

    public void load(Connection connection) throws XMPPException {
        checkAuthenticated(connection, true);
        setFrom(connection.getUser());
        doLoad(connection, connection.getUser());
    }

    public void load(Connection connection, String str) throws XMPPException {
        checkAuthenticated(connection, false);
        setTo(str);
        doLoad(connection, str);
    }

    private void doLoad(Connection connection, String str) throws XMPPException {
        VCard vCard;
        setType(Type.GET);
        PacketCollector createPacketCollector = connection.createPacketCollector(new PacketIDFilter(getPacketID()));
        connection.sendPacket(this);
        try {
            vCard = (VCard) createPacketCollector.nextResult((long) SmackConfiguration.getPacketReplyTimeout());
            if (vCard == null) {
                try {
                    String str2 = "Timeout getting VCard information";
                    throw new XMPPException(str2, new XMPPError(Condition.request_timeout, str2));
                } catch (ClassCastException e) {
                    System.out.println("No VCard for " + str);
                    copyFieldsFrom(vCard);
                }
            }
            if (vCard.getError() != null) {
                throw new XMPPException(vCard.getError());
            }
            copyFieldsFrom(vCard);
        } catch (ClassCastException e2) {
            vCard = null;
            System.out.println("No VCard for " + str);
            copyFieldsFrom(vCard);
        }
    }

    public String getChildElementXML() {
        StringBuilder stringBuilder = new StringBuilder();
        new VCardWriter(stringBuilder).write();
        return stringBuilder.toString();
    }

    private void copyFieldsFrom(VCard vCard) {
        if (vCard == null) {
            vCard = new VCard();
        }
        for (Field field : VCard.class.getDeclaredFields()) {
            if (field.getDeclaringClass() == VCard.class && !Modifier.isFinal(field.getModifiers())) {
                try {
                    field.setAccessible(true);
                    field.set(this, field.get(vCard));
                } catch (Throwable e) {
                    throw new RuntimeException("This cannot happen:" + field, e);
                }
            }
        }
    }

    private void checkAuthenticated(Connection connection, boolean z) {
        if (connection == null) {
            throw new IllegalArgumentException("No connection was provided");
        } else if (!connection.isAuthenticated()) {
            throw new IllegalArgumentException("Connection is not authenticated");
        } else if (z && connection.isAnonymous()) {
            throw new IllegalArgumentException("Connection cannot be anonymous");
        }
    }

    private boolean hasContent() {
        return hasNameField() || hasOrganizationFields() || this.emailHome != null || this.emailWork != null || this.otherSimpleFields.size() > 0 || this.otherUnescapableFields.size() > 0 || this.homeAddr.size() > 0 || this.homePhones.size() > 0 || this.workAddr.size() > 0 || this.workPhones.size() > 0;
    }

    private boolean hasNameField() {
        return (this.firstName == null && this.lastName == null && this.middleName == null) ? false : true;
    }

    private boolean hasOrganizationFields() {
        return (this.organization == null && this.organizationUnit == null) ? false : true;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        VCard vCard = (VCard) obj;
        if (this.emailHome != null) {
            if (!this.emailHome.equals(vCard.emailHome)) {
                return false;
            }
        } else if (vCard.emailHome != null) {
            return false;
        }
        if (this.emailWork != null) {
            if (!this.emailWork.equals(vCard.emailWork)) {
                return false;
            }
        } else if (vCard.emailWork != null) {
            return false;
        }
        if (this.firstName != null) {
            if (!this.firstName.equals(vCard.firstName)) {
                return false;
            }
        } else if (vCard.firstName != null) {
            return false;
        }
        if (!this.homeAddr.equals(vCard.homeAddr) || !this.homePhones.equals(vCard.homePhones)) {
            return false;
        }
        if (this.lastName != null) {
            if (!this.lastName.equals(vCard.lastName)) {
                return false;
            }
        } else if (vCard.lastName != null) {
            return false;
        }
        if (this.middleName != null) {
            if (!this.middleName.equals(vCard.middleName)) {
                return false;
            }
        } else if (vCard.middleName != null) {
            return false;
        }
        if (this.organization != null) {
            if (!this.organization.equals(vCard.organization)) {
                return false;
            }
        } else if (vCard.organization != null) {
            return false;
        }
        if (this.organizationUnit != null) {
            if (!this.organizationUnit.equals(vCard.organizationUnit)) {
                return false;
            }
        } else if (vCard.organizationUnit != null) {
            return false;
        }
        if (this.otherSimpleFields.equals(vCard.otherSimpleFields) && this.workAddr.equals(vCard.workAddr)) {
            return this.workPhones.equals(vCard.workPhones);
        }
        return false;
    }

    public int hashCode() {
        int hashCode;
        int i = 0;
        int hashCode2 = ((this.firstName != null ? this.firstName.hashCode() : 0) + (((((((this.homePhones.hashCode() * 29) + this.workPhones.hashCode()) * 29) + this.homeAddr.hashCode()) * 29) + this.workAddr.hashCode()) * 29)) * 29;
        if (this.lastName != null) {
            hashCode = this.lastName.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode2 = (hashCode + hashCode2) * 29;
        if (this.middleName != null) {
            hashCode = this.middleName.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode2 = (hashCode + hashCode2) * 29;
        if (this.emailHome != null) {
            hashCode = this.emailHome.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode2 = (hashCode + hashCode2) * 29;
        if (this.emailWork != null) {
            hashCode = this.emailWork.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode2 = (hashCode + hashCode2) * 29;
        if (this.organization != null) {
            hashCode = this.organization.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode = (hashCode + hashCode2) * 29;
        if (this.organizationUnit != null) {
            i = this.organizationUnit.hashCode();
        }
        return ((hashCode + i) * 29) + this.otherSimpleFields.hashCode();
    }

    public String toString() {
        return getChildElementXML();
    }
}
