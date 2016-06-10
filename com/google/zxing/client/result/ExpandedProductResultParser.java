package com.google.zxing.client.result;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import java.util.Hashtable;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.smile.SmileConstants;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.Type;
import org.xbill.DNS.WKSRecord.Service;

final class ExpandedProductResultParser extends ResultParser {
    private ExpandedProductResultParser() {
    }

    private static String findAIvalue(int i, String str) {
        StringBuffer stringBuffer = new StringBuffer();
        if (str.charAt(i) != '(') {
            return "ERROR";
        }
        String substring = str.substring(i + 1);
        int i2 = 0;
        while (i2 < substring.length()) {
            char charAt = substring.charAt(i2);
            switch (charAt) {
                case Service.GRAPHICS /*41*/:
                    return stringBuffer.toString();
                case Type.DNSKEY /*48*/:
                case Service.LOGIN /*49*/:
                case Type.NSEC3 /*50*/:
                case Service.LA_MAINT /*51*/:
                case Type.TLSA /*52*/:
                case SimpleResolver.DEFAULT_PORT /*53*/:
                case Opcodes.ISTORE /*54*/:
                case Service.ISI_GL /*55*/:
                case SmileConstants.MAX_SHORT_NAME_UNICODE_BYTES /*56*/:
                case Opcodes.DSTORE /*57*/:
                    stringBuffer.append(charAt);
                    i2++;
                default:
                    return "ERROR";
            }
        }
        return stringBuffer.toString();
    }

    private static String findValue(int i, String str) {
        StringBuffer stringBuffer = new StringBuffer();
        String substring = str.substring(i);
        for (int i2 = 0; i2 < substring.length(); i2++) {
            char charAt = substring.charAt(i2);
            if (charAt == '(') {
                if (!"ERROR".equals(findAIvalue(i2, substring))) {
                    break;
                }
                stringBuffer.append('(');
            } else {
                stringBuffer.append(charAt);
            }
        }
        return stringBuffer.toString();
    }

    public static ExpandedProductParsedResult parse(Result result) {
        if (!BarcodeFormat.RSS_EXPANDED.equals(result.getBarcodeFormat())) {
            return null;
        }
        String text = result.getText();
        if (text == null) {
            return null;
        }
        String str = "-";
        String str2 = "-";
        String str3 = "-";
        String str4 = "-";
        String str5 = "-";
        String str6 = "-";
        String str7 = "-";
        String str8 = "-";
        String str9 = "-";
        String str10 = "-";
        String str11 = "-";
        String str12 = "-";
        String str13 = "-";
        Hashtable hashtable = new Hashtable();
        int i = 0;
        while (i < text.length()) {
            String findAIvalue = findAIvalue(i, text);
            if ("ERROR".equals(findAIvalue)) {
                return null;
            }
            int length = (findAIvalue.length() + 2) + i;
            String findValue = findValue(length, text);
            length += findValue.length();
            if ("00".equals(findAIvalue)) {
                str2 = findValue;
            } else {
                if ("01".equals(findAIvalue)) {
                    str = findValue;
                } else {
                    if ("10".equals(findAIvalue)) {
                        str3 = findValue;
                    } else {
                        if ("11".equals(findAIvalue)) {
                            str4 = findValue;
                        } else {
                            if ("13".equals(findAIvalue)) {
                                str5 = findValue;
                            } else {
                                if ("15".equals(findAIvalue)) {
                                    str6 = findValue;
                                } else {
                                    if ("17".equals(findAIvalue)) {
                                        str7 = findValue;
                                    } else {
                                        if (!"3100".equals(findAIvalue)) {
                                            if (!"3101".equals(findAIvalue)) {
                                                if (!"3102".equals(findAIvalue)) {
                                                    if (!"3103".equals(findAIvalue)) {
                                                        if (!"3104".equals(findAIvalue)) {
                                                            if (!"3105".equals(findAIvalue)) {
                                                                if (!"3106".equals(findAIvalue)) {
                                                                    if (!"3107".equals(findAIvalue)) {
                                                                        if (!"3108".equals(findAIvalue)) {
                                                                            if (!"3109".equals(findAIvalue)) {
                                                                                if (!"3200".equals(findAIvalue)) {
                                                                                    if (!"3201".equals(findAIvalue)) {
                                                                                        if (!"3202".equals(findAIvalue)) {
                                                                                            if (!"3203".equals(findAIvalue)) {
                                                                                                if (!"3204".equals(findAIvalue)) {
                                                                                                    if (!"3205".equals(findAIvalue)) {
                                                                                                        if (!"3206".equals(findAIvalue)) {
                                                                                                            if (!"3207".equals(findAIvalue)) {
                                                                                                                if (!"3208".equals(findAIvalue)) {
                                                                                                                    if (!"3209".equals(findAIvalue)) {
                                                                                                                        if (!"3920".equals(findAIvalue)) {
                                                                                                                            if (!"3921".equals(findAIvalue)) {
                                                                                                                                if (!"3922".equals(findAIvalue)) {
                                                                                                                                    if (!"3923".equals(findAIvalue)) {
                                                                                                                                        if (!"3930".equals(findAIvalue)) {
                                                                                                                                            if (!"3931".equals(findAIvalue)) {
                                                                                                                                                if (!"3932".equals(findAIvalue)) {
                                                                                                                                                    if (!"3933".equals(findAIvalue)) {
                                                                                                                                                        hashtable.put(findAIvalue, findValue);
                                                                                                                                                    }
                                                                                                                                                }
                                                                                                                                            }
                                                                                                                                        }
                                                                                                                                        if (findValue.length() < 4) {
                                                                                                                                            return null;
                                                                                                                                        }
                                                                                                                                        str11 = findValue.substring(3);
                                                                                                                                        str13 = findValue.substring(0, 3);
                                                                                                                                        str12 = findAIvalue.substring(3);
                                                                                                                                    }
                                                                                                                                }
                                                                                                                            }
                                                                                                                        }
                                                                                                                        str12 = findAIvalue.substring(3);
                                                                                                                        str11 = findValue;
                                                                                                                    }
                                                                                                                }
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                                str9 = ExpandedProductParsedResult.POUND;
                                                                                str10 = findAIvalue.substring(3);
                                                                                str8 = findValue;
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                        str9 = ExpandedProductParsedResult.KILOGRAM;
                                        str10 = findAIvalue.substring(3);
                                        str8 = findValue;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            i = length;
        }
        return new ExpandedProductParsedResult(str, str2, str3, str4, str5, str6, str7, str8, str9, str10, str11, str12, str13, hashtable);
    }
}
