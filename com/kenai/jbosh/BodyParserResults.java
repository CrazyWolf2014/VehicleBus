package com.kenai.jbosh;

import java.util.HashMap;
import java.util.Map;

final class BodyParserResults {
    private final Map<BodyQName, String> attrs;

    BodyParserResults() {
        this.attrs = new HashMap();
    }

    void addBodyAttributeValue(BodyQName bodyQName, String str) {
        this.attrs.put(bodyQName, str);
    }

    Map<BodyQName, String> getAttributes() {
        return this.attrs;
    }
}
