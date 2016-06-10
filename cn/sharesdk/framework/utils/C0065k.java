package cn.sharesdk.framework.utils;

/* renamed from: cn.sharesdk.framework.utils.k */
class C0065k implements Appendable {
    int f120a;
    char[] f121b;
    final /* synthetic */ Appendable f122c;
    final /* synthetic */ C1026j f123d;

    C0065k(C1026j c1026j, Appendable appendable) {
        this.f123d = c1026j;
        this.f122c = appendable;
        this.f120a = -1;
        this.f121b = new char[2];
    }

    private void m237a(char[] cArr, int i) {
        for (int i2 = 0; i2 < i; i2++) {
            this.f122c.append(cArr[i2]);
        }
    }

    public Appendable append(char c) {
        char[] a;
        if (this.f120a != -1) {
            if (Character.isLowSurrogate(c)) {
                a = this.f123d.m1857a(Character.toCodePoint((char) this.f120a, c));
                if (a != null) {
                    m237a(a, a.length);
                } else {
                    this.f122c.append((char) this.f120a);
                    this.f122c.append(c);
                }
                this.f120a = -1;
            } else {
                throw new IllegalArgumentException("Expected low surrogate character but got '" + c + "' with value " + c);
            }
        } else if (Character.isHighSurrogate(c)) {
            this.f120a = c;
        } else if (Character.isLowSurrogate(c)) {
            throw new IllegalArgumentException("Unexpected low surrogate character '" + c + "' with value " + c);
        } else {
            a = this.f123d.m1857a(c);
            if (a != null) {
                m237a(a, a.length);
            } else {
                this.f122c.append(c);
            }
        }
        return this;
    }

    public Appendable append(CharSequence charSequence) {
        return append(charSequence, 0, charSequence.length());
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Appendable append(java.lang.CharSequence r6, int r7, int r8) {
        /*
        r5 = this;
        r4 = -1;
        if (r7 >= r8) goto L_0x0052;
    L_0x0003:
        r0 = r5.f120a;
        if (r0 == r4) goto L_0x008c;
    L_0x0007:
        r0 = r7 + 1;
        r1 = r6.charAt(r7);
        r2 = java.lang.Character.isLowSurrogate(r1);
        if (r2 != 0) goto L_0x002c;
    L_0x0013:
        r0 = new java.lang.IllegalArgumentException;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Expected low surrogate character but got ";
        r2 = r2.append(r3);
        r1 = r2.append(r1);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
    L_0x002c:
        r2 = r5.f123d;
        r3 = r5.f120a;
        r3 = (char) r3;
        r1 = java.lang.Character.toCodePoint(r3, r1);
        r1 = r2.m1857a(r1);
        if (r1 == 0) goto L_0x0053;
    L_0x003b:
        r2 = r1.length;
        r5.m237a(r1, r2);
        r7 = r7 + 1;
    L_0x0041:
        r5.f120a = r4;
    L_0x0043:
        r1 = r5.f123d;
        r1 = r1.m1855a(r6, r0, r8);
        if (r1 <= r7) goto L_0x0050;
    L_0x004b:
        r0 = r5.f122c;
        r0.append(r6, r7, r1);
    L_0x0050:
        if (r1 != r8) goto L_0x005c;
    L_0x0052:
        return r5;
    L_0x0053:
        r1 = r5.f122c;
        r2 = r5.f120a;
        r2 = (char) r2;
        r1.append(r2);
        goto L_0x0041;
    L_0x005c:
        r0 = cn.sharesdk.framework.utils.C1026j.m1854b(r6, r1, r8);
        if (r0 >= 0) goto L_0x0066;
    L_0x0062:
        r0 = -r0;
        r5.f120a = r0;
        goto L_0x0052;
    L_0x0066:
        r2 = r5.f123d;
        r2 = r2.m1857a(r0);
        if (r2 == 0) goto L_0x007d;
    L_0x006e:
        r3 = r2.length;
        r5.m237a(r2, r3);
    L_0x0072:
        r0 = java.lang.Character.isSupplementaryCodePoint(r0);
        if (r0 == 0) goto L_0x008a;
    L_0x0078:
        r0 = 2;
    L_0x0079:
        r7 = r1 + r0;
        r0 = r7;
        goto L_0x0043;
    L_0x007d:
        r2 = r5.f121b;
        r3 = 0;
        r2 = java.lang.Character.toChars(r0, r2, r3);
        r3 = r5.f121b;
        r5.m237a(r3, r2);
        goto L_0x0072;
    L_0x008a:
        r0 = 1;
        goto L_0x0079;
    L_0x008c:
        r0 = r7;
        goto L_0x0043;
        */
        throw new UnsupportedOperationException("Method not decompiled: cn.sharesdk.framework.utils.k.append(java.lang.CharSequence, int, int):java.lang.Appendable");
    }
}
