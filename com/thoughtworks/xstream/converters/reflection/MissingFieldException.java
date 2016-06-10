package com.thoughtworks.xstream.converters.reflection;

public class MissingFieldException extends ObjectAccessException {
    private final String className;
    private final String fieldName;

    public MissingFieldException(String className, String fieldName) {
        super("No field '" + fieldName + "' found in class '" + className + "'");
        this.className = className;
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    protected String getClassName() {
        return this.className;
    }
}
