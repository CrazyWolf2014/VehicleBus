package org.codehaus.jackson.org.objectweb.asm.signature;

import com.cnmobi.im.view.RecordButton;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;

public class SignatureReader {
    private final String f1723a;

    public SignatureReader(String str) {
        this.f1723a = str;
    }

    private static int m1757a(String str, int i, SignatureVisitor signatureVisitor) {
        int i2 = i + 1;
        char charAt = str.charAt(i);
        switch (charAt) {
            case Protocol.RVD /*66*/:
            case Service.BOOTPS /*67*/:
            case Service.BOOTPC /*68*/:
            case 'F':
            case Service.NETRJS_3 /*73*/:
            case Service.NETRJS_4 /*74*/:
            case Opcodes.AASTORE /*83*/:
            case Opcodes.SASTORE /*86*/:
            case Opcodes.DUP_X1 /*90*/:
                signatureVisitor.visitBaseType(charAt);
                return i2;
            case Opcodes.BASTORE /*84*/:
                int indexOf = str.indexOf(59, i2);
                signatureVisitor.visitTypeVariable(str.substring(i2, indexOf));
                return indexOf + 1;
            case Service.MIT_DOV /*91*/:
                return m1757a(str, i2, signatureVisitor.visitArrayType());
            default:
                Object obj = null;
                int i3 = i2;
                int i4 = i2;
                Object obj2 = null;
                while (true) {
                    int i5 = i4 + 1;
                    char charAt2 = str.charAt(i4);
                    String substring;
                    switch (charAt2) {
                        case Service.MPM_SND /*46*/:
                        case ';':
                            if (obj == null) {
                                substring = str.substring(i3, i5 - 1);
                                if (obj2 != null) {
                                    signatureVisitor.visitInnerClassType(substring);
                                } else {
                                    signatureVisitor.visitClassType(substring);
                                }
                            }
                            if (charAt2 != ';') {
                                obj2 = 1;
                                obj = null;
                                i3 = i5;
                                i4 = i5;
                                break;
                            }
                            signatureVisitor.visitEnd();
                            return i5;
                        case RecordButton.MAX_TIME /*60*/:
                            substring = str.substring(i3, i5 - 1);
                            if (obj2 != null) {
                                signatureVisitor.visitInnerClassType(substring);
                            } else {
                                signatureVisitor.visitClassType(substring);
                            }
                            int i6 = i5;
                            while (true) {
                                charAt2 = str.charAt(i6);
                                switch (charAt2) {
                                    case Service.NAMESERVER /*42*/:
                                        i6++;
                                        signatureVisitor.visitTypeArgument();
                                        break;
                                    case Service.NICNAME /*43*/:
                                    case Service.MPM /*45*/:
                                        i6 = m1757a(str, i6 + 1, signatureVisitor.visitTypeArgument(charAt2));
                                        break;
                                    case Protocol.CFTP /*62*/:
                                        i4 = i6;
                                        obj = 1;
                                        continue;
                                    default:
                                        i6 = m1757a(str, i6, signatureVisitor.visitTypeArgument(SignatureVisitor.INSTANCEOF));
                                        break;
                                }
                            }
                        default:
                            i4 = i5;
                            break;
                    }
                }
        }
    }

    public void accept(SignatureVisitor signatureVisitor) {
        int i = 0;
        String str = this.f1723a;
        int length = str.length();
        if (str.charAt(0) == '<') {
            i = 2;
            char charAt;
            do {
                int indexOf = str.indexOf(58, i);
                signatureVisitor.visitFormalTypeParameter(str.substring(i - 1, indexOf));
                i = indexOf + 1;
                charAt = str.charAt(i);
                indexOf = (charAt == 'L' || charAt == '[' || charAt == 'T') ? m1757a(str, i, signatureVisitor.visitClassBound()) : i;
                while (true) {
                    i = indexOf + 1;
                    charAt = str.charAt(indexOf);
                    if (charAt != ':') {
                        break;
                    }
                    indexOf = m1757a(str, i, signatureVisitor.visitInterfaceBound());
                }
            } while (charAt != '>');
        }
        if (str.charAt(i) == '(') {
            i++;
            while (str.charAt(i) != ')') {
                i = m1757a(str, i, signatureVisitor.visitParameterType());
            }
            i = m1757a(str, i + 1, signatureVisitor.visitReturnType());
            while (i < length) {
                i = m1757a(str, i + 1, signatureVisitor.visitExceptionType());
            }
            return;
        }
        i = m1757a(str, i, signatureVisitor.visitSuperclass());
        while (i < length) {
            i = m1757a(str, i, signatureVisitor.visitInterface());
        }
    }

    public void acceptType(SignatureVisitor signatureVisitor) {
        m1757a(this.f1723a, 0, signatureVisitor);
    }
}
