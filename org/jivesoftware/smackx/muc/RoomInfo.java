package org.jivesoftware.smackx.muc;

import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.packet.DiscoverInfo;
import org.xmlpull.v1.XmlPullParser;

public class RoomInfo {
    private String description;
    private boolean membersOnly;
    private boolean moderated;
    private boolean nonanonymous;
    private int occupantsCount;
    private boolean passwordProtected;
    private boolean persistent;
    private String room;
    private String subject;

    RoomInfo(DiscoverInfo discoverInfo) {
        this.description = XmlPullParser.NO_NAMESPACE;
        this.subject = XmlPullParser.NO_NAMESPACE;
        this.occupantsCount = -1;
        this.room = discoverInfo.getFrom();
        this.membersOnly = discoverInfo.containsFeature("muc_membersonly");
        this.moderated = discoverInfo.containsFeature("muc_moderated");
        this.nonanonymous = discoverInfo.containsFeature("muc_nonanonymous");
        this.passwordProtected = discoverInfo.containsFeature("muc_passwordprotected");
        this.persistent = discoverInfo.containsFeature("muc_persistent");
        Form formFrom = Form.getFormFrom(discoverInfo);
        if (formFrom != null) {
            FormField field = formFrom.getField("muc#roominfo_description");
            String str = (field == null || !field.getValues().hasNext()) ? XmlPullParser.NO_NAMESPACE : (String) field.getValues().next();
            this.description = str;
            field = formFrom.getField("muc#roominfo_subject");
            str = (field == null || !field.getValues().hasNext()) ? XmlPullParser.NO_NAMESPACE : (String) field.getValues().next();
            this.subject = str;
            field = formFrom.getField("muc#roominfo_occupants");
            this.occupantsCount = field == null ? -1 : Integer.parseInt((String) field.getValues().next());
        }
    }

    public String getRoom() {
        return this.room;
    }

    public String getDescription() {
        return this.description;
    }

    public String getSubject() {
        return this.subject;
    }

    public int getOccupantsCount() {
        return this.occupantsCount;
    }

    public boolean isMembersOnly() {
        return this.membersOnly;
    }

    public boolean isModerated() {
        return this.moderated;
    }

    public boolean isNonanonymous() {
        return this.nonanonymous;
    }

    public boolean isPasswordProtected() {
        return this.passwordProtected;
    }

    public boolean isPersistent() {
        return this.persistent;
    }
}
