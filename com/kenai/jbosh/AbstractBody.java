package com.kenai.jbosh;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public abstract class AbstractBody {
    public abstract Map<BodyQName, String> getAttributes();

    public abstract String toXML();

    AbstractBody() {
    }

    public final Set<BodyQName> getAttributeNames() {
        return Collections.unmodifiableSet(getAttributes().keySet());
    }

    public final String getAttribute(BodyQName bodyQName) {
        return (String) getAttributes().get(bodyQName);
    }

    static BodyQName getBodyQName() {
        return BodyQName.createBOSH("body");
    }
}
