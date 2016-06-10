package com.thoughtworks.xstream.core.util;

import com.thoughtworks.xstream.converters.reflection.ObjectAccessException;
import com.thoughtworks.xstream.core.util.Pool.Factory;
import java.beans.PropertyEditor;

public class ThreadSafePropertyEditor {
    private final Class editorType;
    private final Pool pool;

    /* renamed from: com.thoughtworks.xstream.core.util.ThreadSafePropertyEditor.1 */
    class C11371 implements Factory {
        C11371() {
        }

        public Object newInstance() {
            try {
                return ThreadSafePropertyEditor.this.editorType.newInstance();
            } catch (InstantiationException e) {
                throw new ObjectAccessException("Could not call default constructor of " + ThreadSafePropertyEditor.this.editorType.getName(), e);
            } catch (IllegalAccessException e2) {
                throw new ObjectAccessException("Could not call default constructor of " + ThreadSafePropertyEditor.this.editorType.getName(), e2);
            }
        }
    }

    public ThreadSafePropertyEditor(Class type, int initialPoolSize, int maxPoolSize) {
        if (PropertyEditor.class.isAssignableFrom(type)) {
            this.editorType = type;
            this.pool = new Pool(initialPoolSize, maxPoolSize, new C11371());
            return;
        }
        throw new IllegalArgumentException(type.getName() + " is not a " + PropertyEditor.class.getName());
    }

    public String getAsText(Object object) {
        PropertyEditor editor = fetchFromPool();
        try {
            editor.setValue(object);
            String asText = editor.getAsText();
            return asText;
        } finally {
            this.pool.putInPool(editor);
        }
    }

    public Object setAsText(String str) {
        PropertyEditor editor = fetchFromPool();
        try {
            editor.setAsText(str);
            Object value = editor.getValue();
            return value;
        } finally {
            this.pool.putInPool(editor);
        }
    }

    private PropertyEditor fetchFromPool() {
        return (PropertyEditor) this.pool.fetchFromPool();
    }
}
