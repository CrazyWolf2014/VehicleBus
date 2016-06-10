package cn.sharesdk.framework.utils;

import org.xbill.DNS.KEYRecord.Flags;

/* renamed from: cn.sharesdk.framework.utils.l */
final class C0066l extends ThreadLocal<char[]> {
    C0066l() {
    }

    protected char[] m238a() {
        return new char[Flags.FLAG5];
    }

    protected /* synthetic */ Object initialValue() {
        return m238a();
    }
}
