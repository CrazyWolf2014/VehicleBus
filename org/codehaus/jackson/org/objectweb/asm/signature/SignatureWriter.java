package org.codehaus.jackson.org.objectweb.asm.signature;

public class SignatureWriter implements SignatureVisitor {
    private final StringBuffer f2340a;
    private boolean f2341b;
    private boolean f2342c;
    private int f2343d;

    public SignatureWriter() {
        this.f2340a = new StringBuffer();
    }

    private void m2492a() {
        if (this.f2341b) {
            this.f2341b = false;
            this.f2340a.append('>');
        }
    }

    private void m2493b() {
        if (this.f2343d % 2 != 0) {
            this.f2340a.append('>');
        }
        this.f2343d /= 2;
    }

    public String toString() {
        return this.f2340a.toString();
    }

    public SignatureVisitor visitArrayType() {
        this.f2340a.append('[');
        return this;
    }

    public void visitBaseType(char c) {
        this.f2340a.append(c);
    }

    public SignatureVisitor visitClassBound() {
        return this;
    }

    public void visitClassType(String str) {
        this.f2340a.append('L');
        this.f2340a.append(str);
        this.f2343d *= 2;
    }

    public void visitEnd() {
        m2493b();
        this.f2340a.append(';');
    }

    public SignatureVisitor visitExceptionType() {
        this.f2340a.append('^');
        return this;
    }

    public void visitFormalTypeParameter(String str) {
        if (!this.f2341b) {
            this.f2341b = true;
            this.f2340a.append('<');
        }
        this.f2340a.append(str);
        this.f2340a.append(':');
    }

    public void visitInnerClassType(String str) {
        m2493b();
        this.f2340a.append('.');
        this.f2340a.append(str);
        this.f2343d *= 2;
    }

    public SignatureVisitor visitInterface() {
        return this;
    }

    public SignatureVisitor visitInterfaceBound() {
        this.f2340a.append(':');
        return this;
    }

    public SignatureVisitor visitParameterType() {
        m2492a();
        if (!this.f2342c) {
            this.f2342c = true;
            this.f2340a.append('(');
        }
        return this;
    }

    public SignatureVisitor visitReturnType() {
        m2492a();
        if (!this.f2342c) {
            this.f2340a.append('(');
        }
        this.f2340a.append(')');
        return this;
    }

    public SignatureVisitor visitSuperclass() {
        m2492a();
        return this;
    }

    public SignatureVisitor visitTypeArgument(char c) {
        if (this.f2343d % 2 == 0) {
            this.f2343d++;
            this.f2340a.append('<');
        }
        if (c != SignatureVisitor.INSTANCEOF) {
            this.f2340a.append(c);
        }
        return this;
    }

    public void visitTypeArgument() {
        if (this.f2343d % 2 == 0) {
            this.f2343d++;
            this.f2340a.append('<');
        }
        this.f2340a.append('*');
    }

    public void visitTypeVariable(String str) {
        this.f2340a.append('T');
        this.f2340a.append(str);
        this.f2340a.append(';');
    }
}
