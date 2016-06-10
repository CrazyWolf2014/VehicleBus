package org.jivesoftware.smackx.workgroup.ext.macros;

import com.ifoer.util.MySharedPreferences;
import com.tencent.mm.sdk.plugin.MMPluginProviderConstants.SharedPref;
import java.io.StringReader;
import org.achartengine.ChartFactory;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.jivesoftware.smack.util.StringUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class Macros extends IQ {
    public static final String ELEMENT_NAME = "macros";
    public static final String NAMESPACE = "http://jivesoftware.com/protocol/workgroup";
    private boolean personal;
    private MacroGroup personalMacroGroup;
    private MacroGroup rootGroup;

    public static class InternalProvider implements IQProvider {
        public IQ parseIQ(XmlPullParser xmlPullParser) throws Exception {
            IQ macros = new Macros();
            Object obj = null;
            while (obj == null) {
                int next = xmlPullParser.next();
                if (next == 2) {
                    if (xmlPullParser.getName().equals(MySharedPreferences.model)) {
                        macros.setRootGroup(parseMacroGroups(xmlPullParser.nextText()));
                    }
                } else if (next == 3 && xmlPullParser.getName().equals(Macros.ELEMENT_NAME)) {
                    obj = 1;
                }
            }
            return macros;
        }

        public Macro parseMacro(XmlPullParser xmlPullParser) throws Exception {
            Macro macro = new Macro();
            Object obj = null;
            while (obj == null) {
                int next = xmlPullParser.next();
                if (next == 2) {
                    if (xmlPullParser.getName().equals(ChartFactory.TITLE)) {
                        xmlPullParser.next();
                        macro.setTitle(xmlPullParser.getText());
                    } else if (xmlPullParser.getName().equals("description")) {
                        macro.setDescription(xmlPullParser.nextText());
                    } else if (xmlPullParser.getName().equals("response")) {
                        macro.setResponse(xmlPullParser.nextText());
                    } else if (xmlPullParser.getName().equals(SharedPref.TYPE)) {
                        macro.setType(Integer.valueOf(xmlPullParser.nextText()).intValue());
                    }
                } else if (next == 3 && xmlPullParser.getName().equals("macro")) {
                    obj = 1;
                }
            }
            return macro;
        }

        public MacroGroup parseMacroGroup(XmlPullParser xmlPullParser) throws Exception {
            MacroGroup macroGroup = new MacroGroup();
            Object obj = null;
            while (obj == null) {
                int next = xmlPullParser.next();
                if (next == 2) {
                    if (xmlPullParser.getName().equals("macrogroup")) {
                        macroGroup.addMacroGroup(parseMacroGroup(xmlPullParser));
                    }
                    if (xmlPullParser.getName().equals(ChartFactory.TITLE)) {
                        macroGroup.setTitle(xmlPullParser.nextText());
                    }
                    if (xmlPullParser.getName().equals("macro")) {
                        macroGroup.addMacro(parseMacro(xmlPullParser));
                    }
                } else if (next == 3 && xmlPullParser.getName().equals("macrogroup")) {
                    obj = 1;
                }
            }
            return macroGroup;
        }

        public MacroGroup parseMacroGroups(String str) throws Exception {
            MacroGroup macroGroup = null;
            XmlPullParser newPullParser = XmlPullParserFactory.newInstance().newPullParser();
            newPullParser.setInput(new StringReader(str));
            int eventType = newPullParser.getEventType();
            while (eventType != 1) {
                eventType = newPullParser.next();
                if (eventType == 2 && newPullParser.getName().equals("macrogroup")) {
                    macroGroup = parseMacroGroup(newPullParser);
                }
            }
            return macroGroup;
        }
    }

    public MacroGroup getRootGroup() {
        return this.rootGroup;
    }

    public void setRootGroup(MacroGroup macroGroup) {
        this.rootGroup = macroGroup;
    }

    public boolean isPersonal() {
        return this.personal;
    }

    public void setPersonal(boolean z) {
        this.personal = z;
    }

    public MacroGroup getPersonalMacroGroup() {
        return this.personalMacroGroup;
    }

    public void setPersonalMacroGroup(MacroGroup macroGroup) {
        this.personalMacroGroup = macroGroup;
    }

    public String getChildElementXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<").append(ELEMENT_NAME).append(" xmlns=\"").append(NAMESPACE).append("\">");
        if (isPersonal()) {
            stringBuilder.append("<personal>true</personal>");
        }
        if (getPersonalMacroGroup() != null) {
            stringBuilder.append("<personalMacro>");
            stringBuilder.append(StringUtils.escapeForXML(getPersonalMacroGroup().toXML()));
            stringBuilder.append("</personalMacro>");
        }
        stringBuilder.append("</").append(ELEMENT_NAME).append("> ");
        return stringBuilder.toString();
    }
}
