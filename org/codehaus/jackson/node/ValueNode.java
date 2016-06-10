package org.codehaus.jackson.node;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonToken;

public abstract class ValueNode extends BaseJsonNode {
    public abstract JsonToken asToken();

    protected ValueNode() {
    }

    public boolean isValueNode() {
        return true;
    }

    public JsonNode path(String fieldName) {
        return MissingNode.getInstance();
    }

    public JsonNode path(int index) {
        return MissingNode.getInstance();
    }

    public String toString() {
        return getValueAsText();
    }
}
