package org.jivesoftware.smackx.packet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.IQ.Type;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smackx.commands.AdHocCommand.Action;
import org.jivesoftware.smackx.commands.AdHocCommand.SpecificErrorCondition;
import org.jivesoftware.smackx.commands.AdHocCommand.Status;
import org.jivesoftware.smackx.commands.AdHocCommandNote;
import org.xmlpull.v1.XmlPullParser;

public class AdHocCommandData extends IQ {
    private Action action;
    private ArrayList<Action> actions;
    private Action executeAction;
    private DataForm form;
    private String id;
    private String lang;
    private String name;
    private String node;
    private List<AdHocCommandNote> notes;
    private String sessionID;
    private Status status;

    public static class SpecificError implements PacketExtension {
        public static final String namespace = "http://jabber.org/protocol/commands";
        public SpecificErrorCondition condition;

        public SpecificError(SpecificErrorCondition specificErrorCondition) {
            this.condition = specificErrorCondition;
        }

        public String getElementName() {
            return this.condition.toString();
        }

        public String getNamespace() {
            return namespace;
        }

        public SpecificErrorCondition getCondition() {
            return this.condition;
        }

        public String toXML() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<").append(getElementName());
            stringBuilder.append(" xmlns=\"").append(getNamespace()).append("\"/>");
            return stringBuilder.toString();
        }
    }

    public AdHocCommandData() {
        this.notes = new ArrayList();
        this.actions = new ArrayList();
    }

    public String getChildElementXML() {
        Iterator it;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<command xmlns=\"http://jabber.org/protocol/commands\"");
        stringBuilder.append(" node=\"").append(this.node).append("\"");
        if (!(this.sessionID == null || this.sessionID.equals(XmlPullParser.NO_NAMESPACE))) {
            stringBuilder.append(" sessionid=\"").append(this.sessionID).append("\"");
        }
        if (this.status != null) {
            stringBuilder.append(" status=\"").append(this.status).append("\"");
        }
        if (this.action != null) {
            stringBuilder.append(" action=\"").append(this.action).append("\"");
        }
        if (!(this.lang == null || this.lang.equals(XmlPullParser.NO_NAMESPACE))) {
            stringBuilder.append(" lang=\"").append(this.lang).append("\"");
        }
        stringBuilder.append(">");
        if (getType() == Type.RESULT) {
            stringBuilder.append("<actions");
            if (this.executeAction != null) {
                stringBuilder.append(" execute=\"").append(this.executeAction).append("\"");
            }
            if (this.actions.size() == 0) {
                stringBuilder.append("/>");
            } else {
                stringBuilder.append(">");
                it = this.actions.iterator();
                while (it.hasNext()) {
                    stringBuilder.append("<").append((Action) it.next()).append("/>");
                }
                stringBuilder.append("</actions>");
            }
        }
        if (this.form != null) {
            stringBuilder.append(this.form.toXML());
        }
        for (AdHocCommandNote adHocCommandNote : this.notes) {
            stringBuilder.append("<note type=\"").append(adHocCommandNote.getType().toString()).append("\">");
            stringBuilder.append(adHocCommandNote.getValue());
            stringBuilder.append("</note>");
        }
        stringBuilder.append("</command>");
        return stringBuilder.toString();
    }

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getNode() {
        return this.node;
    }

    public void setNode(String str) {
        this.node = str;
    }

    public List<AdHocCommandNote> getNotes() {
        return this.notes;
    }

    public void addNote(AdHocCommandNote adHocCommandNote) {
        this.notes.add(adHocCommandNote);
    }

    public void remveNote(AdHocCommandNote adHocCommandNote) {
        this.notes.remove(adHocCommandNote);
    }

    public DataForm getForm() {
        return this.form;
    }

    public void setForm(DataForm dataForm) {
        this.form = dataForm;
    }

    public Action getAction() {
        return this.action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Action> getActions() {
        return this.actions;
    }

    public void addAction(Action action) {
        this.actions.add(action);
    }

    public void setExecuteAction(Action action) {
        this.executeAction = action;
    }

    public Action getExecuteAction() {
        return this.executeAction;
    }

    public void setSessionID(String str) {
        this.sessionID = str;
    }

    public String getSessionID() {
        return this.sessionID;
    }
}
