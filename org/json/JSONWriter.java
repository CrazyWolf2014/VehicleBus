package org.json;

import java.io.Writer;

public class JSONWriter {
    private static final int maxdepth = 20;
    private boolean comma;
    protected char mode;
    private char[] stack;
    private int top;
    protected Writer writer;

    public JSONWriter(Writer w) {
        this.comma = false;
        this.mode = 'i';
        this.stack = new char[maxdepth];
        this.top = 0;
        this.writer = w;
    }

    private JSONWriter append(String s) throws JSONException {
        if (s == null) {
            throw new JSONException("Null pointer");
        } else if (this.mode == 'o' || this.mode == 'a') {
            try {
                if (this.comma && this.mode == 'a') {
                    this.writer.write(44);
                }
                this.writer.write(s);
                if (this.mode == 'o') {
                    this.mode = 'k';
                }
                this.comma = true;
                return this;
            } catch (Throwable e) {
                throw new JSONException(e);
            }
        } else {
            throw new JSONException("Value out of sequence.");
        }
    }

    public JSONWriter array() throws JSONException {
        if (this.mode == 'i' || this.mode == 'o' || this.mode == 'a') {
            push('a');
            append("[");
            this.comma = false;
            return this;
        }
        throw new JSONException("Misplaced array.");
    }

    private JSONWriter end(char m, char c) throws JSONException {
        if (this.mode != m) {
            String str;
            if (m == 'o') {
                str = "Misplaced endObject.";
            } else {
                str = "Misplaced endArray.";
            }
            throw new JSONException(str);
        }
        pop(m);
        try {
            this.writer.write(c);
            this.comma = true;
            return this;
        } catch (Throwable e) {
            throw new JSONException(e);
        }
    }

    public JSONWriter endArray() throws JSONException {
        return end('a', ']');
    }

    public JSONWriter endObject() throws JSONException {
        return end('k', '}');
    }

    public JSONWriter key(String s) throws JSONException {
        if (s == null) {
            throw new JSONException("Null key.");
        } else if (this.mode == 'k') {
            try {
                if (this.comma) {
                    this.writer.write(44);
                }
                this.writer.write(JSONObject.quote(s));
                this.writer.write(58);
                this.comma = false;
                this.mode = 'o';
                return this;
            } catch (Throwable e) {
                throw new JSONException(e);
            }
        } else {
            throw new JSONException("Misplaced key.");
        }
    }

    public JSONWriter object() throws JSONException {
        if (this.mode == 'i') {
            this.mode = 'o';
        }
        if (this.mode == 'o' || this.mode == 'a') {
            append("{");
            push('k');
            this.comma = false;
            return this;
        }
        throw new JSONException("Misplaced object.");
    }

    private void pop(char c) throws JSONException {
        if (this.top <= 0 || this.stack[this.top - 1] != c) {
            throw new JSONException("Nesting error.");
        }
        this.top--;
        this.mode = this.top == 0 ? 'd' : this.stack[this.top - 1];
    }

    private void push(char c) throws JSONException {
        if (this.top >= maxdepth) {
            throw new JSONException("Nesting too deep.");
        }
        this.stack[this.top] = c;
        this.mode = c;
        this.top++;
    }

    public JSONWriter value(boolean b) throws JSONException {
        return append(b ? "true" : "false");
    }

    public JSONWriter value(double d) throws JSONException {
        return value(new Double(d));
    }

    public JSONWriter value(long l) throws JSONException {
        return append(Long.toString(l));
    }

    public JSONWriter value(Object o) throws JSONException {
        return append(JSONObject.valueToString(o));
    }
}
