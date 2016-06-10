package org.codehaus.jackson.org.objectweb.asm;

import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.ifoer.expeditionphone.WelcomeActivity;
import com.ifoer.ui.MainMenuActivity;
import org.codehaus.jackson.smile.SmileConstants;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.transport.ServiceConnection;
import org.kxml2.wap.Wbxml;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;

final class Frame {
    static final int[] f1684a;
    Label f1685b;
    int[] f1686c;
    int[] f1687d;
    private int[] f1688e;
    private int[] f1689f;
    private int f1690g;
    private int f1691h;
    private int[] f1692i;

    static {
        int[] iArr = new int[202];
        String str = "EFFFFFFFFGGFFFGGFFFEEFGFGFEEEEEEEEEEEEEEEEEEEEDEDEDDDDDCDCDEEEEEEEEEEEEEEEEEEEEBABABBBBDCFFFGGGEDCDCDCDCDCDCDCDCDCDCEEEEDDDDDDDCDCDCEFEFDDEEFFDEDEEEBDDBBDDDDDDCCCCCCCCEFEDDDCDCDEEEEEEEEEEFEEEEEEDDEEDDEE";
        for (int i = 0; i < iArr.length; i++) {
            iArr[i] = str.charAt(i) - 69;
        }
        f1684a = iArr;
    }

    Frame() {
    }

    private int m1726a() {
        if (this.f1690g > 0) {
            int[] iArr = this.f1689f;
            int i = this.f1690g - 1;
            this.f1690g = i;
            return iArr[i];
        }
        Label label = this.f1685b;
        int i2 = label.f1713f - 1;
        label.f1713f = i2;
        return 50331648 | (-i2);
    }

    private int m1727a(int i) {
        if (this.f1688e == null || i >= this.f1688e.length) {
            return 33554432 | i;
        }
        int i2 = this.f1688e[i];
        if (i2 != 0) {
            return i2;
        }
        i2 = 33554432 | i;
        this.f1688e[i] = i2;
        return i2;
    }

    private int m1728a(ClassWriter classWriter, int i) {
        int c;
        if (i == 16777222) {
            c = classWriter.m2471c(classWriter.f2255F) | 24117248;
        } else if ((-1048576 & i) != 25165824) {
            return i;
        } else {
            c = classWriter.m2471c(classWriter.f2254E[1048575 & i].f1703g) | 24117248;
        }
        for (int i2 = 0; i2 < this.f1691h; i2++) {
            int i3 = this.f1692i[i2];
            int i4 = -268435456 & i3;
            int i5 = 251658240 & i3;
            if (i5 == 33554432) {
                i3 = this.f1686c[i3 & 8388607] + i4;
            } else if (i5 == 50331648) {
                i3 = this.f1687d[this.f1687d.length - (i3 & 8388607)] + i4;
            }
            if (i == i3) {
                return c;
            }
        }
        return i;
    }

    private void m1729a(int i, int i2) {
        if (this.f1688e == null) {
            this.f1688e = new int[10];
        }
        int length = this.f1688e.length;
        if (i >= length) {
            Object obj = new int[Math.max(i + 1, length * 2)];
            System.arraycopy(this.f1688e, 0, obj, 0, length);
            this.f1688e = obj;
        }
        this.f1688e[i] = i2;
    }

    private void m1730a(String str) {
        char charAt = str.charAt(0);
        if (charAt == '(') {
            m1735c((Type.getArgumentsAndReturnSizes(str) >> 2) - 1);
        } else if (charAt == 'J' || charAt == 'D') {
            m1735c(2);
        } else {
            m1735c(1);
        }
    }

    private void m1731a(ClassWriter classWriter, String str) {
        int b = m1733b(classWriter, str);
        if (b != 0) {
            m1734b(b);
            if (b == 16777220 || b == 16777219) {
                m1734b(16777216);
            }
        }
    }

    private static boolean m1732a(ClassWriter classWriter, int i, int[] iArr, int i2) {
        int i3 = iArr[i2];
        if (i3 == i) {
            return false;
        }
        int i4;
        if ((268435455 & i) != 16777221) {
            i4 = i;
        } else if (i3 == 16777221) {
            return false;
        } else {
            i4 = 16777221;
        }
        if (i3 == 0) {
            iArr[i2] = i4;
            return true;
        }
        if ((i3 & 267386880) == 24117248 || (i3 & -268435456) != 0) {
            if (i4 == 16777221) {
                return false;
            }
            i4 = (-1048576 & i4) == (-1048576 & i3) ? (i3 & 267386880) == 24117248 ? classWriter.m2460a(i4 & 1048575, 1048575 & i3) | ((i4 & -268435456) | 24117248) : classWriter.m2471c("java/lang/Object") | 24117248 : ((i4 & 267386880) == 24117248 || (i4 & -268435456) != 0) ? classWriter.m2471c("java/lang/Object") | 24117248 : 16777216;
        } else if (i3 != 16777221) {
            i4 = 16777216;
        } else if ((i4 & 267386880) != 24117248 && (i4 & -268435456) == 0) {
            i4 = 16777216;
        }
        if (i3 == i4) {
            return false;
        }
        iArr[i2] = i4;
        return true;
    }

    private static int m1733b(ClassWriter classWriter, String str) {
        int i = 16777217;
        int indexOf = str.charAt(0) == '(' ? str.indexOf(41) + 1 : 0;
        switch (str.charAt(indexOf)) {
            case Protocol.RVD /*66*/:
            case Service.BOOTPS /*67*/:
            case Service.NETRJS_3 /*73*/:
            case Opcodes.AASTORE /*83*/:
            case Opcodes.DUP_X1 /*90*/:
                return 16777217;
            case Service.BOOTPC /*68*/:
                return 16777219;
            case 'F':
                return 16777218;
            case Service.NETRJS_4 /*74*/:
                return 16777220;
            case Protocol.BR_SAT_MON /*76*/:
                return 24117248 | classWriter.m2471c(str.substring(indexOf + 1, str.length() - 1));
            case Opcodes.SASTORE /*86*/:
                return 0;
            default:
                int i2 = indexOf + 1;
                while (str.charAt(i2) == '[') {
                    i2++;
                }
                switch (str.charAt(i2)) {
                    case Protocol.RVD /*66*/:
                        i = 16777226;
                        break;
                    case Service.BOOTPS /*67*/:
                        i = 16777227;
                        break;
                    case Service.BOOTPC /*68*/:
                        i = 16777219;
                        break;
                    case 'F':
                        i = 16777218;
                        break;
                    case Service.NETRJS_3 /*73*/:
                        break;
                    case Service.NETRJS_4 /*74*/:
                        i = 16777220;
                        break;
                    case Opcodes.AASTORE /*83*/:
                        i = 16777228;
                        break;
                    case Opcodes.DUP_X1 /*90*/:
                        i = 16777225;
                        break;
                    default:
                        i = classWriter.m2471c(str.substring(i2 + 1, str.length() - 1)) | 24117248;
                        break;
                }
                return ((i2 - indexOf) << 28) | i;
        }
    }

    private void m1734b(int i) {
        if (this.f1689f == null) {
            this.f1689f = new int[10];
        }
        int length = this.f1689f.length;
        if (this.f1690g >= length) {
            Object obj = new int[Math.max(this.f1690g + 1, length * 2)];
            System.arraycopy(this.f1689f, 0, obj, 0, length);
            this.f1689f = obj;
        }
        int[] iArr = this.f1689f;
        int i2 = this.f1690g;
        this.f1690g = i2 + 1;
        iArr[i2] = i;
        length = this.f1685b.f1713f + this.f1690g;
        if (length > this.f1685b.f1714g) {
            this.f1685b.f1714g = length;
        }
    }

    private void m1735c(int i) {
        if (this.f1690g >= i) {
            this.f1690g -= i;
            return;
        }
        Label label = this.f1685b;
        label.f1713f -= i - this.f1690g;
        this.f1690g = 0;
    }

    private void m1736d(int i) {
        if (this.f1692i == null) {
            this.f1692i = new int[2];
        }
        int length = this.f1692i.length;
        if (this.f1691h >= length) {
            Object obj = new int[Math.max(this.f1691h + 1, length * 2)];
            System.arraycopy(this.f1692i, 0, obj, 0, length);
            this.f1692i = obj;
        }
        int[] iArr = this.f1692i;
        int i2 = this.f1691h;
        this.f1691h = i2 + 1;
        iArr[i2] = i;
    }

    void m1737a(int i, int i2, ClassWriter classWriter, Item item) {
        int a;
        int a2;
        int a3;
        String str;
        switch (i) {
            case KEYRecord.OWNER_USER /*0*/:
            case Opcodes.INEG /*116*/:
            case Service.UUCP_PATH /*117*/:
            case Opcodes.FNEG /*118*/:
            case Service.NNTP /*119*/:
            case Opcodes.I2B /*145*/:
            case Opcodes.I2C /*146*/:
            case Opcodes.I2S /*147*/:
            case Opcodes.GOTO /*167*/:
            case Opcodes.RETURN /*177*/:
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                m1734b(16777221);
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
            case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
            case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
            case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
            case FileOptions.CC_GENERIC_SERVICES_FIELD_NUMBER /*16*/:
            case FileOptions.JAVA_GENERIC_SERVICES_FIELD_NUMBER /*17*/:
            case WelcomeActivity.GPIO_IOCSDATAHIGH /*21*/:
                m1734b(16777217);
            case FileOptions.OPTIMIZE_FOR_FIELD_NUMBER /*9*/:
            case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
            case Protocol.XNS_IDP /*22*/:
                m1734b(16777220);
                m1734b(16777216);
            case TrafficIncident.LOCATION_FIELD_NUMBER /*11*/:
            case TrafficIncident.VERTEXOFFSET_FIELD_NUMBER /*12*/:
            case TrafficIncident.INCIDENTVERTEX_FIELD_NUMBER /*13*/:
            case Service.TELNET /*23*/:
                m1734b(16777218);
            case TrafficIncident.STARTTIME_FIELD_NUMBER /*14*/:
            case TrafficIncident.ENDTIME_FIELD_NUMBER /*15*/:
            case Protocol.TRUNK_2 /*24*/:
                m1734b(16777219);
                m1734b(16777216);
            case FileOptions.PY_GENERIC_SERVICES_FIELD_NUMBER /*18*/:
                switch (item.f1700b) {
                    case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                        m1734b(16777217);
                    case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                        m1734b(16777218);
                    case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                        m1734b(16777220);
                        m1734b(16777216);
                    case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                        m1734b(16777219);
                        m1734b(16777216);
                    case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                        m1734b(24117248 | classWriter.m2471c("java/lang/Class"));
                    default:
                        m1734b(24117248 | classWriter.m2471c("java/lang/String"));
                }
            case MainMenuActivity.REQUEST_USER_LOGIN_OUT /*25*/:
                m1734b(m1727a(i2));
            case Service.MPM_SND /*46*/:
            case Service.LA_MAINT /*51*/:
            case Type.TLSA /*52*/:
            case SimpleResolver.DEFAULT_PORT /*53*/:
                m1735c(2);
                m1734b(16777217);
            case Service.NI_FTP /*47*/:
            case Opcodes.D2L /*143*/:
                m1735c(2);
                m1734b(16777220);
                m1734b(16777216);
            case Type.DNSKEY /*48*/:
                m1735c(2);
                m1734b(16777218);
            case Service.LOGIN /*49*/:
            case Service.NETBIOS_DGM /*138*/:
                m1735c(2);
                m1734b(16777219);
                m1734b(16777216);
            case Type.NSEC3 /*50*/:
                m1735c(1);
                m1734b(m1726a() - 268435456);
            case Opcodes.ISTORE /*54*/:
            case SmileConstants.MAX_SHORT_NAME_UNICODE_BYTES /*56*/:
            case Opcodes.ASTORE /*58*/:
                m1729a(i2, m1726a());
                if (i2 > 0) {
                    a = m1727a(i2 - 1);
                    if (a == 16777220 || a == 16777219) {
                        m1729a(i2 - 1, 16777216);
                    } else if ((251658240 & a) != 16777216) {
                        m1729a(i2 - 1, a | 8388608);
                    }
                }
            case Service.ISI_GL /*55*/:
            case Opcodes.DSTORE /*57*/:
                m1735c(1);
                m1729a(i2, m1726a());
                m1729a(i2 + 1, 16777216);
                if (i2 > 0) {
                    a = m1727a(i2 - 1);
                    if (a == 16777220 || a == 16777219) {
                        m1729a(i2 - 1, 16777216);
                    } else if ((251658240 & a) != 16777216) {
                        m1729a(i2 - 1, a | 8388608);
                    }
                }
            case Service.FINGER /*79*/:
            case Service.HOSTS2_NS /*81*/:
            case Opcodes.AASTORE /*83*/:
            case Opcodes.BASTORE /*84*/:
            case Opcodes.CASTORE /*85*/:
            case Opcodes.SASTORE /*86*/:
                m1735c(3);
            case Opcodes.LASTORE /*80*/:
            case Opcodes.DASTORE /*82*/:
                m1735c(4);
            case Opcodes.POP /*87*/:
            case Opcodes.IFEQ /*153*/:
            case Opcodes.IFNE /*154*/:
            case Opcodes.IFLT /*155*/:
            case Opcodes.IFGE /*156*/:
            case Opcodes.IFGT /*157*/:
            case Opcodes.IFLE /*158*/:
            case Opcodes.TABLESWITCH /*170*/:
            case Opcodes.LOOKUPSWITCH /*171*/:
            case Opcodes.IRETURN /*172*/:
            case Opcodes.FRETURN /*174*/:
            case Opcodes.ARETURN /*176*/:
            case Opcodes.ATHROW /*191*/:
            case Wbxml.EXT_2 /*194*/:
            case Wbxml.OPAQUE /*195*/:
            case Opcodes.IFNULL /*198*/:
            case Opcodes.IFNONNULL /*199*/:
                m1735c(1);
            case Opcodes.POP2 /*88*/:
            case Opcodes.IF_ICMPEQ /*159*/:
            case SmileConstants.TOKEN_PREFIX_SHORT_UNICODE /*160*/:
            case Opcodes.IF_ICMPLT /*161*/:
            case Opcodes.IF_ICMPGE /*162*/:
            case Opcodes.IF_ICMPGT /*163*/:
            case Opcodes.IF_ICMPLE /*164*/:
            case Opcodes.IF_ACMPEQ /*165*/:
            case Opcodes.IF_ACMPNE /*166*/:
            case Opcodes.LRETURN /*173*/:
            case Opcodes.DRETURN /*175*/:
                m1735c(2);
            case Service.SU_MIT_TG /*89*/:
                a = m1726a();
                m1734b(a);
                m1734b(a);
            case Opcodes.DUP_X1 /*90*/:
                a = m1726a();
                a2 = m1726a();
                m1734b(a);
                m1734b(a2);
                m1734b(a);
            case Service.MIT_DOV /*91*/:
                a = m1726a();
                a2 = m1726a();
                a3 = m1726a();
                m1734b(a);
                m1734b(a3);
                m1734b(a2);
                m1734b(a);
            case Opcodes.DUP2 /*92*/:
                a = m1726a();
                a2 = m1726a();
                m1734b(a2);
                m1734b(a);
                m1734b(a2);
                m1734b(a);
            case Service.DCP /*93*/:
                a = m1726a();
                a2 = m1726a();
                a3 = m1726a();
                m1734b(a2);
                m1734b(a);
                m1734b(a3);
                m1734b(a2);
                m1734b(a);
            case Opcodes.DUP2_X2 /*94*/:
                a = m1726a();
                a2 = m1726a();
                a3 = m1726a();
                int a4 = m1726a();
                m1734b(a2);
                m1734b(a);
                m1734b(a4);
                m1734b(a3);
                m1734b(a2);
                m1734b(a);
            case Service.SUPDUP /*95*/:
                a = m1726a();
                a2 = m1726a();
                m1734b(a);
                m1734b(a2);
            case SmileConstants.TOKEN_PREFIX_SMALL_ASCII /*96*/:
            case ParseCharStream.HISTORY_LENGTH /*100*/:
            case Service.X400_SND /*104*/:
            case Opcodes.IDIV /*108*/:
            case Opcodes.IREM /*112*/:
            case SoapEnvelope.VER12 /*120*/:
            case Opcodes.ISHR /*122*/:
            case Opcodes.IUSHR /*124*/:
            case Opcodes.IAND /*126*/:
            case Flags.FLAG8 /*128*/:
            case Service.CISCO_FNA /*130*/:
            case Service.PROFILE /*136*/:
            case Service.BL_IDM /*142*/:
            case Opcodes.FCMPL /*149*/:
            case Opcodes.FCMPG /*150*/:
                m1735c(2);
                m1734b(16777217);
            case Service.SWIFT_RVF /*97*/:
            case Service.HOSTNAME /*101*/:
            case Service.CSNET_NS /*105*/:
            case Service.POP_2 /*109*/:
            case Service.AUTH /*113*/:
            case Service.LOCUS_CON /*127*/:
            case Service.PWDGEN /*129*/:
            case Service.CISCO_TNA /*131*/:
                m1735c(4);
                m1734b(16777220);
                m1734b(16777216);
            case Service.TACNEWS /*98*/:
            case Service.ISO_TSAP /*102*/:
            case Opcodes.FMUL /*106*/:
            case SoapEnvelope.VER11 /*110*/:
            case Opcodes.FREM /*114*/:
            case Service.NETBIOS_NS /*137*/:
            case Opcodes.D2F /*144*/:
                m1735c(2);
                m1734b(16777218);
            case Service.METAGRAM /*99*/:
            case Service.X400 /*103*/:
            case Service.RTELNET /*107*/:
            case Service.SUNRPC /*111*/:
            case Service.SFTP /*115*/:
                m1735c(4);
                m1734b(16777219);
                m1734b(16777216);
            case Service.ERPC /*121*/:
            case Service.NTP /*123*/:
            case Service.LOCUS_MAP /*125*/:
                m1735c(3);
                m1734b(16777220);
                m1734b(16777216);
            case Service.CISCO_SYS /*132*/:
                m1729a(i2, 16777217);
            case Service.STATSRV /*133*/:
            case Service.EMFIS_DATA /*140*/:
                m1735c(1);
                m1734b(16777220);
                m1734b(16777216);
            case Service.INGRES_NET /*134*/:
                m1735c(1);
                m1734b(16777218);
            case Service.LOC_SRV /*135*/:
            case Service.EMFIS_CNTL /*141*/:
                m1735c(1);
                m1734b(16777219);
                m1734b(16777216);
            case Service.NETBIOS_SSN /*139*/:
            case Opcodes.ARRAYLENGTH /*190*/:
            case Wbxml.EXT_1 /*193*/:
                m1735c(1);
                m1734b(16777217);
            case Opcodes.LCMP /*148*/:
            case Opcodes.DCMPL /*151*/:
            case Opcodes.DCMPG /*152*/:
                m1735c(4);
                m1734b(16777217);
            case Opcodes.JSR /*168*/:
            case Opcodes.RET /*169*/:
                throw new RuntimeException("JSR/RET are not supported with computeFrames option");
            case Opcodes.GETSTATIC /*178*/:
                m1731a(classWriter, item.f1705i);
            case Opcodes.PUTSTATIC /*179*/:
                m1730a(item.f1705i);
            case Opcodes.GETFIELD /*180*/:
                m1735c(1);
                m1731a(classWriter, item.f1705i);
            case Opcodes.PUTFIELD /*181*/:
                m1730a(item.f1705i);
                m1726a();
            case Opcodes.INVOKEVIRTUAL /*182*/:
            case Opcodes.INVOKESPECIAL /*183*/:
            case Opcodes.INVOKESTATIC /*184*/:
            case Opcodes.INVOKEINTERFACE /*185*/:
                m1730a(item.f1705i);
                if (i != Opcodes.INVOKESTATIC) {
                    a = m1726a();
                    if (i == Opcodes.INVOKESPECIAL && item.f1704h.charAt(0) == '<') {
                        m1736d(a);
                    }
                }
                m1731a(classWriter, item.f1705i);
            case Opcodes.INVOKEDYNAMIC /*186*/:
                m1730a(item.f1704h);
                m1731a(classWriter, item.f1704h);
            case Opcodes.NEW /*187*/:
                m1734b(25165824 | classWriter.m2461a(item.f1703g, i2));
            case Opcodes.NEWARRAY /*188*/:
                m1726a();
                switch (i2) {
                    case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                        m1734b(285212681);
                    case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                        m1734b(285212683);
                    case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                        m1734b(285212674);
                    case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                        m1734b(285212675);
                    case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                        m1734b(285212682);
                    case FileOptions.OPTIMIZE_FOR_FIELD_NUMBER /*9*/:
                        m1734b(285212684);
                    case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
                        m1734b(285212673);
                    default:
                        m1734b(285212676);
                }
            case Opcodes.ANEWARRAY /*189*/:
                str = item.f1703g;
                m1726a();
                if (str.charAt(0) == '[') {
                    m1731a(classWriter, new StringBuffer().append('[').append(str).toString());
                } else {
                    m1734b(classWriter.m2471c(str) | 292552704);
                }
            case Wbxml.EXT_0 /*192*/:
                str = item.f1703g;
                m1726a();
                if (str.charAt(0) == '[') {
                    m1731a(classWriter, str);
                } else {
                    m1734b(classWriter.m2471c(str) | 24117248);
                }
            default:
                m1735c(i2);
                m1731a(classWriter, item.f1703g);
        }
    }

    void m1738a(ClassWriter classWriter, int i, Type[] typeArr, int i2) {
        int i3 = 1;
        int i4 = 0;
        this.f1686c = new int[i2];
        this.f1687d = new int[0];
        if ((i & 8) != 0) {
            i3 = 0;
        } else if ((ServiceConnection.DEFAULT_BUFFER_SIZE & i) == 0) {
            this.f1686c[0] = 24117248 | classWriter.m2471c(classWriter.f2255F);
        } else {
            this.f1686c[0] = 16777222;
        }
        while (i4 < typeArr.length) {
            int b = m1733b(classWriter, typeArr[i4].getDescriptor());
            int i5 = i3 + 1;
            this.f1686c[i3] = b;
            if (b == 16777220 || b == 16777219) {
                i3 = i5 + 1;
                this.f1686c[i5] = 16777216;
            } else {
                i3 = i5;
            }
            i4++;
        }
        while (i3 < i2) {
            i4 = i3 + 1;
            this.f1686c[i3] = 16777216;
            i3 = i4;
        }
    }

    boolean m1739a(ClassWriter classWriter, Frame frame, int i) {
        int i2;
        int i3;
        boolean z = false;
        int length = this.f1686c.length;
        int length2 = this.f1687d.length;
        if (frame.f1686c == null) {
            frame.f1686c = new int[length];
            z = true;
        }
        int i4 = 0;
        boolean z2 = z;
        while (i4 < length) {
            int i5;
            if (this.f1688e == null || i4 >= this.f1688e.length) {
                i2 = this.f1686c[i4];
            } else {
                i2 = this.f1688e[i4];
                if (i2 == 0) {
                    i2 = this.f1686c[i4];
                } else {
                    i3 = -268435456 & i2;
                    i5 = 251658240 & i2;
                    if (i5 != 16777216) {
                        i3 = i5 == 33554432 ? i3 + this.f1686c[8388607 & i2] : i3 + this.f1687d[length2 - (8388607 & i2)];
                        i2 = ((i2 & 8388608) == 0 || !(i3 == 16777220 || i3 == 16777219)) ? i3 : 16777216;
                    }
                }
            }
            if (this.f1692i != null) {
                i2 = m1728a(classWriter, i2);
            }
            z2 |= m1732a(classWriter, i2, frame.f1686c, i4);
            i4++;
        }
        if (i > 0) {
            i3 = 0;
            i2 = z2;
            while (i3 < length) {
                int a = m1732a(classWriter, this.f1686c[i3], frame.f1686c, i3) | i2;
                i3++;
                i2 = a;
            }
            if (frame.f1687d == null) {
                frame.f1687d = new int[1];
                i2 = 1;
            }
            return m1732a(classWriter, i, frame.f1687d, 0) | i2;
        }
        length = this.f1687d.length + this.f1685b.f1713f;
        if (frame.f1687d == null) {
            frame.f1687d = new int[(this.f1690g + length)];
            z = true;
        } else {
            z = z2;
        }
        boolean z3 = z;
        for (a = 0; a < length; a++) {
            i2 = this.f1687d[a];
            if (this.f1692i != null) {
                i2 = m1728a(classWriter, i2);
            }
            z3 |= m1732a(classWriter, i2, frame.f1687d, a);
        }
        for (i2 = 0; i2 < this.f1690g; i2++) {
            a = this.f1689f[i2];
            i4 = -268435456 & a;
            i5 = 251658240 & a;
            if (i5 != 16777216) {
                i4 = i5 == 33554432 ? i4 + this.f1686c[8388607 & a] : i4 + this.f1687d[length2 - (8388607 & a)];
                a = ((a & 8388608) == 0 || !(i4 == 16777220 || i4 == 16777219)) ? i4 : 16777216;
            }
            if (this.f1692i != null) {
                a = m1728a(classWriter, a);
            }
            z3 |= m1732a(classWriter, a, frame.f1687d, length + i2);
        }
        return z3;
    }
}
