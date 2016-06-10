package org.xbill.DNS;

import com.cnlaunch.mycar.jni.JniX431File;
import com.tencent.mm.sdk.platformtools.Util;
import java.util.Date;
import org.xbill.DNS.KEYRecord.Flags;
import org.xbill.DNS.utils.HMAC;
import org.xbill.DNS.utils.base64;

public class TSIG {
    public static final short FUDGE = (short) 300;
    public static final Name HMAC;
    public static final Name HMAC_MD5;
    private static final String HMAC_MD5_STR = "HMAC-MD5.SIG-ALG.REG.INT.";
    public static final Name HMAC_SHA1;
    private static final String HMAC_SHA1_STR = "hmac-sha1.";
    public static final Name HMAC_SHA224;
    private static final String HMAC_SHA224_STR = "hmac-sha224.";
    public static final Name HMAC_SHA256;
    private static final String HMAC_SHA256_STR = "hmac-sha256.";
    public static final Name HMAC_SHA384;
    private static final String HMAC_SHA384_STR = "hmac-sha384.";
    public static final Name HMAC_SHA512;
    private static final String HMAC_SHA512_STR = "hmac-sha512.";
    private Name alg;
    private String digest;
    private int digestBlockLength;
    private byte[] key;
    private Name name;

    public static class StreamVerifier {
        private TSIG key;
        private TSIGRecord lastTSIG;
        private int lastsigned;
        private int nresponses;
        private HMAC verifier;

        public StreamVerifier(TSIG tsig, TSIGRecord tSIGRecord) {
            this.key = tsig;
            this.verifier = new HMAC(this.key.digest, this.key.digestBlockLength, this.key.key);
            this.nresponses = 0;
            this.lastTSIG = tSIGRecord;
        }

        public int verify(Message message, byte[] bArr) {
            TSIGRecord tsig = message.getTSIG();
            this.nresponses++;
            int verify;
            if (this.nresponses == 1) {
                verify = this.key.verify(message, bArr, this.lastTSIG);
                if (verify == 0) {
                    byte[] signature = tsig.getSignature();
                    DNSOutput dNSOutput = new DNSOutput();
                    dNSOutput.writeU16(signature.length);
                    this.verifier.update(dNSOutput.toByteArray());
                    this.verifier.update(signature);
                }
                this.lastTSIG = tsig;
                return verify;
            }
            if (tsig != null) {
                message.getHeader().decCount(3);
            }
            byte[] toWire = message.getHeader().toWire();
            if (tsig != null) {
                message.getHeader().incCount(3);
            }
            this.verifier.update(toWire);
            if (tsig == null) {
                verify = bArr.length - toWire.length;
            } else {
                verify = message.tsigstart - toWire.length;
            }
            this.verifier.update(bArr, toWire.length, verify);
            if (tsig != null) {
                this.lastsigned = this.nresponses;
                this.lastTSIG = tsig;
                if (tsig.getName().equals(this.key.name) && tsig.getAlgorithm().equals(this.key.alg)) {
                    DNSOutput dNSOutput2 = new DNSOutput();
                    long time = tsig.getTimeSigned().getTime() / 1000;
                    time &= Util.MAX_32BIT_VALUE;
                    dNSOutput2.writeU16((int) (time >> 32));
                    dNSOutput2.writeU32(time);
                    dNSOutput2.writeU16(tsig.getFudge());
                    this.verifier.update(dNSOutput2.toByteArray());
                    if (this.verifier.verify(tsig.getSignature())) {
                        this.verifier.clear();
                        dNSOutput2 = new DNSOutput();
                        dNSOutput2.writeU16(tsig.getSignature().length);
                        this.verifier.update(dNSOutput2.toByteArray());
                        this.verifier.update(tsig.getSignature());
                        message.tsigState = 1;
                        return 0;
                    }
                    if (Options.check("verbose")) {
                        System.err.println("BADSIG failure");
                    }
                    message.tsigState = 4;
                    return 16;
                }
                if (Options.check("verbose")) {
                    System.err.println("BADKEY failure");
                }
                message.tsigState = 4;
                return 17;
            }
            if (this.nresponses - this.lastsigned >= 100) {
                verify = 1;
            } else {
                verify = 0;
            }
            if (verify != 0) {
                message.tsigState = 4;
                return 1;
            }
            message.tsigState = 2;
            return 0;
        }
    }

    static {
        HMAC_MD5 = Name.fromConstantString(HMAC_MD5_STR);
        HMAC = HMAC_MD5;
        HMAC_SHA1 = Name.fromConstantString(HMAC_SHA1_STR);
        HMAC_SHA224 = Name.fromConstantString(HMAC_SHA224_STR);
        HMAC_SHA256 = Name.fromConstantString(HMAC_SHA256_STR);
        HMAC_SHA384 = Name.fromConstantString(HMAC_SHA384_STR);
        HMAC_SHA512 = Name.fromConstantString(HMAC_SHA512_STR);
    }

    private void getDigest() {
        if (this.alg.equals(HMAC_MD5)) {
            this.digest = "md5";
            this.digestBlockLength = 64;
        } else if (this.alg.equals(HMAC_SHA1)) {
            this.digest = "sha-1";
            this.digestBlockLength = 64;
        } else if (this.alg.equals(HMAC_SHA224)) {
            this.digest = "sha-224";
            this.digestBlockLength = 64;
        } else if (this.alg.equals(HMAC_SHA256)) {
            this.digest = "sha-256";
            this.digestBlockLength = 64;
        } else if (this.alg.equals(HMAC_SHA512)) {
            this.digest = "sha-512";
            this.digestBlockLength = Flags.FLAG8;
        } else if (this.alg.equals(HMAC_SHA384)) {
            this.digest = "sha-384";
            this.digestBlockLength = Flags.FLAG8;
        } else {
            throw new IllegalArgumentException("Invalid algorithm");
        }
    }

    public TSIG(Name name, Name name2, byte[] bArr) {
        this.name = name2;
        this.alg = name;
        this.key = bArr;
        getDigest();
    }

    public TSIG(Name name, byte[] bArr) {
        this(HMAC_MD5, name, bArr);
    }

    public TSIG(Name name, String str, String str2) {
        this.key = base64.fromString(str2);
        if (this.key == null) {
            throw new IllegalArgumentException("Invalid TSIG key string");
        }
        try {
            this.name = Name.fromString(str, Name.root);
            this.alg = name;
            getDigest();
        } catch (TextParseException e) {
            throw new IllegalArgumentException("Invalid TSIG key name");
        }
    }

    public TSIG(String str, String str2, String str3) {
        this(HMAC_MD5, str2, str3);
        if (str.equalsIgnoreCase("hmac-md5")) {
            this.alg = HMAC_MD5;
        } else if (str.equalsIgnoreCase("hmac-sha1")) {
            this.alg = HMAC_SHA1;
        } else if (str.equalsIgnoreCase("hmac-sha224")) {
            this.alg = HMAC_SHA224;
        } else if (str.equalsIgnoreCase("hmac-sha256")) {
            this.alg = HMAC_SHA256;
        } else if (str.equalsIgnoreCase("hmac-sha384")) {
            this.alg = HMAC_SHA384;
        } else if (str.equalsIgnoreCase("hmac-sha512")) {
            this.alg = HMAC_SHA512;
        } else {
            throw new IllegalArgumentException("Invalid TSIG algorithm");
        }
        getDigest();
    }

    public TSIG(String str, String str2) {
        this(HMAC_MD5, str, str2);
    }

    public static TSIG fromString(String str) {
        String[] split = str.split("[:/]", 3);
        if (split.length < 2) {
            throw new IllegalArgumentException("Invalid TSIG key specification");
        }
        if (split.length == 3) {
            try {
                return new TSIG(split[0], split[1], split[2]);
            } catch (IllegalArgumentException e) {
                split = str.split("[:/]", 2);
            }
        }
        return new TSIG(HMAC_MD5, split[0], split[1]);
    }

    public TSIGRecord generate(Message message, byte[] bArr, int i, TSIGRecord tSIGRecord) {
        Date date;
        DNSOutput dNSOutput;
        byte[] sign;
        if (i != 18) {
            date = new Date();
        } else {
            date = tSIGRecord.getTimeSigned();
        }
        HMAC hmac = null;
        if (i == 0 || i == 18) {
            hmac = new HMAC(this.digest, this.digestBlockLength, this.key);
        }
        int intValue = Options.intValue("tsigfudge");
        if (intValue < 0 || intValue > 32767) {
            intValue = JniX431File.MAX_DS_COLNUMBER;
        }
        if (tSIGRecord != null) {
            dNSOutput = new DNSOutput();
            dNSOutput.writeU16(tSIGRecord.getSignature().length);
            if (hmac != null) {
                hmac.update(dNSOutput.toByteArray());
                hmac.update(tSIGRecord.getSignature());
            }
        }
        if (hmac != null) {
            hmac.update(bArr);
        }
        dNSOutput = new DNSOutput();
        this.name.toWireCanonical(dNSOutput);
        dNSOutput.writeU16(KEYRecord.PROTOCOL_ANY);
        dNSOutput.writeU32(0);
        this.alg.toWireCanonical(dNSOutput);
        long time = date.getTime() / 1000;
        time &= Util.MAX_32BIT_VALUE;
        dNSOutput.writeU16((int) (time >> 32));
        dNSOutput.writeU32(time);
        dNSOutput.writeU16(intValue);
        dNSOutput.writeU16(i);
        dNSOutput.writeU16(0);
        if (hmac != null) {
            hmac.update(dNSOutput.toByteArray());
        }
        if (hmac != null) {
            sign = hmac.sign();
        } else {
            sign = new byte[0];
        }
        byte[] bArr2 = null;
        if (i == 18) {
            DNSOutput dNSOutput2 = new DNSOutput();
            long time2 = new Date().getTime() / 1000;
            time2 &= Util.MAX_32BIT_VALUE;
            dNSOutput2.writeU16((int) (time2 >> 32));
            dNSOutput2.writeU32(time2);
            bArr2 = dNSOutput2.toByteArray();
        }
        return new TSIGRecord(this.name, KEYRecord.PROTOCOL_ANY, 0, this.alg, date, intValue, sign, message.getHeader().getID(), i, bArr2);
    }

    public void apply(Message message, int i, TSIGRecord tSIGRecord) {
        message.addRecord(generate(message, message.toWire(), i, tSIGRecord), 3);
        message.tsigState = 3;
    }

    public void apply(Message message, TSIGRecord tSIGRecord) {
        apply(message, 0, tSIGRecord);
    }

    public void applyStream(Message message, TSIGRecord tSIGRecord, boolean z) {
        if (z) {
            apply(message, tSIGRecord);
            return;
        }
        Date date = new Date();
        HMAC hmac = new HMAC(this.digest, this.digestBlockLength, this.key);
        int intValue = Options.intValue("tsigfudge");
        if (intValue < 0 || intValue > 32767) {
            intValue = JniX431File.MAX_DS_COLNUMBER;
        }
        DNSOutput dNSOutput = new DNSOutput();
        dNSOutput.writeU16(tSIGRecord.getSignature().length);
        hmac.update(dNSOutput.toByteArray());
        hmac.update(tSIGRecord.getSignature());
        hmac.update(message.toWire());
        dNSOutput = new DNSOutput();
        long time = date.getTime() / 1000;
        time &= Util.MAX_32BIT_VALUE;
        dNSOutput.writeU16((int) (time >> 32));
        dNSOutput.writeU32(time);
        dNSOutput.writeU16(intValue);
        hmac.update(dNSOutput.toByteArray());
        message.addRecord(new TSIGRecord(this.name, KEYRecord.PROTOCOL_ANY, 0, this.alg, date, intValue, hmac.sign(), message.getHeader().getID(), 0, null), 3);
        message.tsigState = 3;
    }

    public byte verify(Message message, byte[] bArr, int i, TSIGRecord tSIGRecord) {
        message.tsigState = 4;
        TSIGRecord tsig = message.getTSIG();
        HMAC hmac = new HMAC(this.digest, this.digestBlockLength, this.key);
        if (tsig == null) {
            return (byte) 1;
        }
        if (tsig.getName().equals(this.name) && tsig.getAlgorithm().equals(this.alg)) {
            if (Math.abs(System.currentTimeMillis() - tsig.getTimeSigned().getTime()) > 1000 * ((long) tsig.getFudge())) {
                if (Options.check("verbose")) {
                    System.err.println("BADTIME failure");
                }
                return (byte) 18;
            }
            DNSOutput dNSOutput;
            if (!(tSIGRecord == null || tsig.getError() == 17 || tsig.getError() == 16)) {
                dNSOutput = new DNSOutput();
                dNSOutput.writeU16(tSIGRecord.getSignature().length);
                hmac.update(dNSOutput.toByteArray());
                hmac.update(tSIGRecord.getSignature());
            }
            message.getHeader().decCount(3);
            byte[] toWire = message.getHeader().toWire();
            message.getHeader().incCount(3);
            hmac.update(toWire);
            hmac.update(bArr, toWire.length, message.tsigstart - toWire.length);
            dNSOutput = new DNSOutput();
            tsig.getName().toWireCanonical(dNSOutput);
            dNSOutput.writeU16(tsig.dclass);
            dNSOutput.writeU32(tsig.ttl);
            tsig.getAlgorithm().toWireCanonical(dNSOutput);
            long time = tsig.getTimeSigned().getTime() / 1000;
            time &= Util.MAX_32BIT_VALUE;
            dNSOutput.writeU16((int) (time >> 32));
            dNSOutput.writeU32(time);
            dNSOutput.writeU16(tsig.getFudge());
            dNSOutput.writeU16(tsig.getError());
            if (tsig.getOther() != null) {
                dNSOutput.writeU16(tsig.getOther().length);
                dNSOutput.writeByteArray(tsig.getOther());
            } else {
                dNSOutput.writeU16(0);
            }
            hmac.update(dNSOutput.toByteArray());
            toWire = tsig.getSignature();
            int digestLength = hmac.digestLength();
            int i2 = this.digest.equals("md5") ? 10 : digestLength / 2;
            if (toWire.length > digestLength) {
                if (Options.check("verbose")) {
                    System.err.println("BADSIG: signature too long");
                }
                return (byte) 16;
            } else if (toWire.length < i2) {
                if (Options.check("verbose")) {
                    System.err.println("BADSIG: signature too short");
                }
                return (byte) 16;
            } else if (hmac.verify(toWire, true)) {
                message.tsigState = 1;
                return (byte) 0;
            } else {
                if (Options.check("verbose")) {
                    System.err.println("BADSIG: signature verification");
                }
                return (byte) 16;
            }
        }
        if (Options.check("verbose")) {
            System.err.println("BADKEY failure");
        }
        return (byte) 17;
    }

    public int verify(Message message, byte[] bArr, TSIGRecord tSIGRecord) {
        return verify(message, bArr, bArr.length, tSIGRecord);
    }

    public int recordLength() {
        return (((((this.name.length() + 10) + this.alg.length()) + 8) + 18) + 4) + 8;
    }
}
