package org.jivesoftware.smackx.workgroup.ext.macros;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MacroGroup {
    private List<MacroGroup> macroGroups;
    private List<Macro> macros;
    private String title;

    public MacroGroup() {
        this.macros = new ArrayList();
        this.macroGroups = new ArrayList();
    }

    public void addMacro(Macro macro) {
        this.macros.add(macro);
    }

    public void removeMacro(Macro macro) {
        this.macros.remove(macro);
    }

    public Macro getMacroByTitle(String str) {
        for (Macro macro : Collections.unmodifiableList(this.macros)) {
            if (macro.getTitle().equalsIgnoreCase(str)) {
                return macro;
            }
        }
        return null;
    }

    public void addMacroGroup(MacroGroup macroGroup) {
        this.macroGroups.add(macroGroup);
    }

    public void removeMacroGroup(MacroGroup macroGroup) {
        this.macroGroups.remove(macroGroup);
    }

    public Macro getMacro(int i) {
        return (Macro) this.macros.get(i);
    }

    public MacroGroup getMacroGroupByTitle(String str) {
        for (MacroGroup macroGroup : Collections.unmodifiableList(this.macroGroups)) {
            if (macroGroup.getTitle().equalsIgnoreCase(str)) {
                return macroGroup;
            }
        }
        return null;
    }

    public MacroGroup getMacroGroup(int i) {
        return (MacroGroup) this.macroGroups.get(i);
    }

    public List<Macro> getMacros() {
        return this.macros;
    }

    public void setMacros(List<Macro> list) {
        this.macros = list;
    }

    public List<MacroGroup> getMacroGroups() {
        return this.macroGroups;
    }

    public void setMacroGroups(List<MacroGroup> list) {
        this.macroGroups = list;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String toXML() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<macrogroup>");
        stringBuilder.append("<title>" + getTitle() + "</title>");
        stringBuilder.append("<macros>");
        for (Macro macro : getMacros()) {
            stringBuilder.append("<macro>");
            stringBuilder.append("<title>" + macro.getTitle() + "</title>");
            stringBuilder.append("<type>" + macro.getType() + "</type>");
            stringBuilder.append("<description>" + macro.getDescription() + "</description>");
            stringBuilder.append("<response>" + macro.getResponse() + "</response>");
            stringBuilder.append("</macro>");
        }
        stringBuilder.append("</macros>");
        if (getMacroGroups().size() > 0) {
            stringBuilder.append("<macroGroups>");
            for (MacroGroup toXML : getMacroGroups()) {
                stringBuilder.append(toXML.toXML());
            }
            stringBuilder.append("</macroGroups>");
        }
        stringBuilder.append("</macrogroup>");
        return stringBuilder.toString();
    }
}
