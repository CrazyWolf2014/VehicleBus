package org.xbill.DNS;

import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.tencent.mm.sdk.platformtools.FilePathGenerator;
import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.DSAPublicKeySpec;
import java.security.spec.ECFieldFp;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.EllipticCurve;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import org.codehaus.jackson.util.MinimalPrettyPrinter;

public class DNSSEC {
    private static final int ASN1_INT = 2;
    private static final int ASN1_SEQ = 48;
    private static final int DSA_LEN = 20;
    private static final ECKeyInfo ECDSA_P256;
    private static final ECKeyInfo ECDSA_P384;

    public static class Algorithm {
        public static final int DH = 2;
        public static final int DSA = 3;
        public static final int DSA_NSEC3_SHA1 = 6;
        public static final int ECDSAP256SHA256 = 13;
        public static final int ECDSAP384SHA384 = 14;
        public static final int INDIRECT = 252;
        public static final int PRIVATEDNS = 253;
        public static final int PRIVATEOID = 254;
        public static final int RSAMD5 = 1;
        public static final int RSASHA1 = 5;
        public static final int RSASHA256 = 8;
        public static final int RSASHA512 = 10;
        public static final int RSA_NSEC3_SHA1 = 7;
        private static Mnemonic algs;

        private Algorithm() {
        }

        static {
            algs = new Mnemonic("DNSSEC algorithm", DH);
            algs.setMaximum(KEYRecord.PROTOCOL_ANY);
            algs.setNumericAllowed(true);
            algs.add(RSAMD5, "RSAMD5");
            algs.add(DH, "DH");
            algs.add(DSA, "DSA");
            algs.add(RSASHA1, "RSASHA1");
            algs.add(DSA_NSEC3_SHA1, "DSA-NSEC3-SHA1");
            algs.add(RSA_NSEC3_SHA1, "RSA-NSEC3-SHA1");
            algs.add(RSASHA256, "RSASHA256");
            algs.add(RSASHA512, "RSASHA512");
            algs.add(ECDSAP256SHA256, "ECDSAP256SHA256");
            algs.add(ECDSAP384SHA384, "ECDSAP384SHA384");
            algs.add(INDIRECT, "INDIRECT");
            algs.add(PRIVATEDNS, "PRIVATEDNS");
            algs.add(PRIVATEOID, "PRIVATEOID");
        }

        public static String string(int i) {
            return algs.getText(i);
        }

        public static int value(String str) {
            return algs.getValue(str);
        }
    }

    public static class DNSSECException extends Exception {
        DNSSECException(String str) {
            super(str);
        }
    }

    private static class ECKeyInfo {
        public BigInteger f1725a;
        public BigInteger f1726b;
        EllipticCurve curve;
        public BigInteger gx;
        public BigInteger gy;
        int length;
        public BigInteger f1727n;
        public BigInteger f1728p;
        ECParameterSpec spec;

        ECKeyInfo(int i, String str, String str2, String str3, String str4, String str5, String str6) {
            this.length = i;
            this.f1728p = new BigInteger(str, 16);
            this.f1725a = new BigInteger(str2, 16);
            this.f1726b = new BigInteger(str3, 16);
            this.gx = new BigInteger(str4, 16);
            this.gy = new BigInteger(str5, 16);
            this.f1727n = new BigInteger(str6, 16);
            this.curve = new EllipticCurve(new ECFieldFp(this.f1728p), this.f1725a, this.f1726b);
            this.spec = new ECParameterSpec(this.curve, new ECPoint(this.gx, this.gy), this.f1727n, 1);
        }
    }

    public static class IncompatibleKeyException extends IllegalArgumentException {
        IncompatibleKeyException() {
            super("incompatible keys");
        }
    }

    public static class KeyMismatchException extends DNSSECException {
        private KEYBase key;
        private SIGBase sig;

        KeyMismatchException(KEYBase kEYBase, SIGBase sIGBase) {
            super("key " + kEYBase.getName() + FilePathGenerator.ANDROID_DIR_SEP + Algorithm.string(kEYBase.getAlgorithm()) + FilePathGenerator.ANDROID_DIR_SEP + kEYBase.getFootprint() + MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR + "does not match signature " + sIGBase.getSigner() + FilePathGenerator.ANDROID_DIR_SEP + Algorithm.string(sIGBase.getAlgorithm()) + FilePathGenerator.ANDROID_DIR_SEP + sIGBase.getFootprint());
        }
    }

    public static class MalformedKeyException extends DNSSECException {
        MalformedKeyException(KEYBase kEYBase) {
            super("Invalid key data: " + kEYBase.rdataToString());
        }
    }

    public static class SignatureExpiredException extends DNSSECException {
        private Date now;
        private Date when;

        SignatureExpiredException(Date date, Date date2) {
            super("signature expired");
            this.when = date;
            this.now = date2;
        }

        public Date getExpiration() {
            return this.when;
        }

        public Date getVerifyTime() {
            return this.now;
        }
    }

    public static class SignatureNotYetValidException extends DNSSECException {
        private Date now;
        private Date when;

        SignatureNotYetValidException(Date date, Date date2) {
            super("signature is not yet valid");
            this.when = date;
            this.now = date2;
        }

        public Date getExpiration() {
            return this.when;
        }

        public Date getVerifyTime() {
            return this.now;
        }
    }

    public static class SignatureVerificationException extends DNSSECException {
        SignatureVerificationException() {
            super("signature verification failed");
        }
    }

    public static class UnsupportedAlgorithmException extends DNSSECException {
        UnsupportedAlgorithmException(int i) {
            super("Unsupported algorithm: " + i);
        }
    }

    private DNSSEC() {
    }

    private static void digestSIG(DNSOutput dNSOutput, SIGBase sIGBase) {
        dNSOutput.writeU16(sIGBase.getTypeCovered());
        dNSOutput.writeU8(sIGBase.getAlgorithm());
        dNSOutput.writeU8(sIGBase.getLabels());
        dNSOutput.writeU32(sIGBase.getOrigTTL());
        dNSOutput.writeU32(sIGBase.getExpire().getTime() / 1000);
        dNSOutput.writeU32(sIGBase.getTimeSigned().getTime() / 1000);
        dNSOutput.writeU16(sIGBase.getFootprint());
        sIGBase.getSigner().toWireCanonical(dNSOutput);
    }

    public static byte[] digestRRset(RRSIGRecord rRSIGRecord, RRset rRset) {
        int i;
        Name wild;
        DNSOutput dNSOutput = new DNSOutput();
        digestSIG(dNSOutput, rRSIGRecord);
        int size = rRset.size();
        Record[] recordArr = new Record[size];
        Iterator rrs = rRset.rrs();
        Name name = rRset.getName();
        int labels = rRSIGRecord.getLabels() + 1;
        if (name.labels() > labels) {
            i = size;
            wild = name.wild(name.labels() - labels);
        } else {
            i = size;
            wild = null;
        }
        while (rrs.hasNext()) {
            labels = i - 1;
            recordArr[labels] = (Record) rrs.next();
            i = labels;
        }
        Arrays.sort(recordArr);
        DNSOutput dNSOutput2 = new DNSOutput();
        if (wild != null) {
            wild.toWireCanonical(dNSOutput2);
        } else {
            name.toWireCanonical(dNSOutput2);
        }
        dNSOutput2.writeU16(rRset.getType());
        dNSOutput2.writeU16(rRset.getDClass());
        dNSOutput2.writeU32(rRSIGRecord.getOrigTTL());
        for (Record rdataToWireCanonical : recordArr) {
            dNSOutput.writeByteArray(dNSOutput2.toByteArray());
            size = dNSOutput.current();
            dNSOutput.writeU16(0);
            dNSOutput.writeByteArray(rdataToWireCanonical.rdataToWireCanonical());
            int current = (dNSOutput.current() - size) - 2;
            dNSOutput.save();
            dNSOutput.jump(size);
            dNSOutput.writeU16(current);
            dNSOutput.restore();
        }
        return dNSOutput.toByteArray();
    }

    public static byte[] digestMessage(SIGRecord sIGRecord, Message message, byte[] bArr) {
        DNSOutput dNSOutput = new DNSOutput();
        digestSIG(dNSOutput, sIGRecord);
        if (bArr != null) {
            dNSOutput.writeByteArray(bArr);
        }
        message.toWire(dNSOutput);
        return dNSOutput.toByteArray();
    }

    private static int BigIntegerLength(BigInteger bigInteger) {
        return (bigInteger.bitLength() + 7) / 8;
    }

    private static BigInteger readBigInteger(DNSInput dNSInput, int i) throws IOException {
        return new BigInteger(1, dNSInput.readByteArray(i));
    }

    private static BigInteger readBigInteger(DNSInput dNSInput) {
        return new BigInteger(1, dNSInput.readByteArray());
    }

    private static void writeBigInteger(DNSOutput dNSOutput, BigInteger bigInteger) {
        byte[] toByteArray = bigInteger.toByteArray();
        if (toByteArray[0] == null) {
            dNSOutput.writeByteArray(toByteArray, 1, toByteArray.length - 1);
        } else {
            dNSOutput.writeByteArray(toByteArray);
        }
    }

    private static PublicKey toRSAPublicKey(KEYBase kEYBase) throws IOException, GeneralSecurityException {
        DNSInput dNSInput = new DNSInput(kEYBase.getKey());
        int readU8 = dNSInput.readU8();
        if (readU8 == 0) {
            readU8 = dNSInput.readU16();
        }
        BigInteger readBigInteger = readBigInteger(dNSInput, readU8);
        return KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(readBigInteger(dNSInput), readBigInteger));
    }

    private static PublicKey toDSAPublicKey(KEYBase kEYBase) throws IOException, GeneralSecurityException, MalformedKeyException {
        DNSInput dNSInput = new DNSInput(kEYBase.getKey());
        int readU8 = dNSInput.readU8();
        if (readU8 > 8) {
            throw new MalformedKeyException(kEYBase);
        }
        BigInteger readBigInteger = readBigInteger(dNSInput, DSA_LEN);
        BigInteger readBigInteger2 = readBigInteger(dNSInput, (readU8 * 8) + 64);
        BigInteger readBigInteger3 = readBigInteger(dNSInput, (readU8 * 8) + 64);
        return KeyFactory.getInstance("DSA").generatePublic(new DSAPublicKeySpec(readBigInteger(dNSInput, (readU8 * 8) + 64), readBigInteger2, readBigInteger, readBigInteger3));
    }

    static {
        ECDSA_P256 = new ECKeyInfo(32, "FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFF", "FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFC", "5AC635D8AA3A93E7B3EBBD55769886BC651D06B0CC53B0F63BCE3C3E27D2604B", "6B17D1F2E12C4247F8BCE6E563A440F277037D812DEB33A0F4A13945D898C296", "4FE342E2FE1A7F9B8EE7EB4A7C0F9E162BCE33576B315ECECBB6406837BF51F5", "FFFFFFFF00000000FFFFFFFFFFFFFFFFBCE6FAADA7179E84F3B9CAC2FC632551");
        ECDSA_P384 = new ECKeyInfo(ASN1_SEQ, "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFFFF0000000000000000FFFFFFFF", "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFFFF0000000000000000FFFFFFFC", "B3312FA7E23EE7E4988E056BE3F82D19181D9C6EFE8141120314088F5013875AC656398D8A2ED19D2A85C8EDD3EC2AEF", "AA87CA22BE8B05378EB1C71EF320AD746E1D3B628BA79B9859F741E082542A385502F25DBF55296C3A545E3872760AB7", "3617DE4A96262C6F5D9E98BF9292DC29F8F41DBD289A147CE9DA3113B5F0B8C00A60B1CE1D7E819D7A431D7C90EA0E5F", "FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFC7634D81F4372DDF581A0DB248B0A77AECEC196ACCC52973");
    }

    private static PublicKey toECDSAPublicKey(KEYBase kEYBase, ECKeyInfo eCKeyInfo) throws IOException, GeneralSecurityException, MalformedKeyException {
        DNSInput dNSInput = new DNSInput(kEYBase.getKey());
        return KeyFactory.getInstance("EC").generatePublic(new ECPublicKeySpec(new ECPoint(readBigInteger(dNSInput, eCKeyInfo.length), readBigInteger(dNSInput, eCKeyInfo.length)), eCKeyInfo.spec));
    }

    static PublicKey toPublicKey(KEYBase kEYBase) throws DNSSECException {
        int algorithm = kEYBase.getAlgorithm();
        switch (algorithm) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
            case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
            case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
            case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
                return toRSAPublicKey(kEYBase);
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
            case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                return toDSAPublicKey(kEYBase);
            case TrafficIncident.INCIDENTVERTEX_FIELD_NUMBER /*13*/:
                return toECDSAPublicKey(kEYBase, ECDSA_P256);
            case TrafficIncident.STARTTIME_FIELD_NUMBER /*14*/:
                return toECDSAPublicKey(kEYBase, ECDSA_P384);
            default:
                try {
                    throw new UnsupportedAlgorithmException(algorithm);
                } catch (IOException e) {
                    throw new MalformedKeyException(kEYBase);
                } catch (GeneralSecurityException e2) {
                    throw new DNSSECException(e2.toString());
                }
        }
    }

    private static byte[] fromRSAPublicKey(RSAPublicKey rSAPublicKey) {
        DNSOutput dNSOutput = new DNSOutput();
        BigInteger publicExponent = rSAPublicKey.getPublicExponent();
        BigInteger modulus = rSAPublicKey.getModulus();
        int BigIntegerLength = BigIntegerLength(publicExponent);
        if (BigIntegerLength < KEYRecord.OWNER_ZONE) {
            dNSOutput.writeU8(BigIntegerLength);
        } else {
            dNSOutput.writeU8(0);
            dNSOutput.writeU16(BigIntegerLength);
        }
        writeBigInteger(dNSOutput, publicExponent);
        writeBigInteger(dNSOutput, modulus);
        return dNSOutput.toByteArray();
    }

    private static byte[] fromDSAPublicKey(DSAPublicKey dSAPublicKey) {
        DNSOutput dNSOutput = new DNSOutput();
        BigInteger q = dSAPublicKey.getParams().getQ();
        BigInteger p = dSAPublicKey.getParams().getP();
        BigInteger g = dSAPublicKey.getParams().getG();
        BigInteger y = dSAPublicKey.getY();
        dNSOutput.writeU8((p.toByteArray().length - 64) / 8);
        writeBigInteger(dNSOutput, q);
        writeBigInteger(dNSOutput, p);
        writeBigInteger(dNSOutput, g);
        writeBigInteger(dNSOutput, y);
        return dNSOutput.toByteArray();
    }

    private static byte[] fromECDSAPublicKey(ECPublicKey eCPublicKey) {
        DNSOutput dNSOutput = new DNSOutput();
        BigInteger affineX = eCPublicKey.getW().getAffineX();
        BigInteger affineY = eCPublicKey.getW().getAffineY();
        writeBigInteger(dNSOutput, affineX);
        writeBigInteger(dNSOutput, affineY);
        return dNSOutput.toByteArray();
    }

    static byte[] fromPublicKey(PublicKey publicKey, int i) throws DNSSECException {
        switch (i) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
            case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
            case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
            case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
                if (publicKey instanceof RSAPublicKey) {
                    return fromRSAPublicKey((RSAPublicKey) publicKey);
                }
                throw new IncompatibleKeyException();
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
            case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                if (publicKey instanceof DSAPublicKey) {
                    return fromDSAPublicKey((DSAPublicKey) publicKey);
                }
                throw new IncompatibleKeyException();
            case TrafficIncident.INCIDENTVERTEX_FIELD_NUMBER /*13*/:
            case TrafficIncident.STARTTIME_FIELD_NUMBER /*14*/:
                if (publicKey instanceof ECPublicKey) {
                    return fromECDSAPublicKey((ECPublicKey) publicKey);
                }
                throw new IncompatibleKeyException();
            default:
                throw new UnsupportedAlgorithmException(i);
        }
    }

    public static String algString(int i) throws UnsupportedAlgorithmException {
        switch (i) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                return "MD5withRSA";
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
            case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                return "SHA1withDSA";
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
            case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
                return "SHA1withRSA";
            case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
                return "SHA256withRSA";
            case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
                return "SHA512withRSA";
            case TrafficIncident.INCIDENTVERTEX_FIELD_NUMBER /*13*/:
                return "SHA256withECDSA";
            case TrafficIncident.STARTTIME_FIELD_NUMBER /*14*/:
                return "SHA384withECDSA";
            default:
                throw new UnsupportedAlgorithmException(i);
        }
    }

    private static byte[] DSASignaturefromDNS(byte[] bArr) throws DNSSECException, IOException {
        int i = 21;
        if (bArr.length != 41) {
            throw new SignatureVerificationException();
        }
        int i2;
        DNSInput dNSInput = new DNSInput(bArr);
        DNSOutput dNSOutput = new DNSOutput();
        dNSInput.readU8();
        byte[] readByteArray = dNSInput.readByteArray(DSA_LEN);
        if (readByteArray[0] < null) {
            i2 = 21;
        } else {
            i2 = DSA_LEN;
        }
        byte[] readByteArray2 = dNSInput.readByteArray(DSA_LEN);
        if (readByteArray2[0] >= null) {
            i = DSA_LEN;
        }
        dNSOutput.writeU8(ASN1_SEQ);
        dNSOutput.writeU8((i2 + i) + 4);
        dNSOutput.writeU8(ASN1_INT);
        dNSOutput.writeU8(i2);
        if (i2 > DSA_LEN) {
            dNSOutput.writeU8(0);
        }
        dNSOutput.writeByteArray(readByteArray);
        dNSOutput.writeU8(ASN1_INT);
        dNSOutput.writeU8(i);
        if (i > DSA_LEN) {
            dNSOutput.writeU8(0);
        }
        dNSOutput.writeByteArray(readByteArray2);
        return dNSOutput.toByteArray();
    }

    private static byte[] DSASignaturetoDNS(byte[] bArr, int i) throws IOException {
        DNSInput dNSInput = new DNSInput(bArr);
        DNSOutput dNSOutput = new DNSOutput();
        dNSOutput.writeU8(i);
        if (dNSInput.readU8() != ASN1_SEQ) {
            throw new IOException();
        }
        dNSInput.readU8();
        if (dNSInput.readU8() != ASN1_INT) {
            throw new IOException();
        }
        int readU8 = dNSInput.readU8();
        if (readU8 == 21) {
            if (dNSInput.readU8() != 0) {
                throw new IOException();
            }
        } else if (readU8 != DSA_LEN) {
            throw new IOException();
        }
        dNSOutput.writeByteArray(dNSInput.readByteArray(DSA_LEN));
        if (dNSInput.readU8() != ASN1_INT) {
            throw new IOException();
        }
        readU8 = dNSInput.readU8();
        if (readU8 == 21) {
            if (dNSInput.readU8() != 0) {
                throw new IOException();
            }
        } else if (readU8 != DSA_LEN) {
            throw new IOException();
        }
        dNSOutput.writeByteArray(dNSInput.readByteArray(DSA_LEN));
        return dNSOutput.toByteArray();
    }

    private static byte[] ECDSASignaturefromDNS(byte[] bArr, ECKeyInfo eCKeyInfo) throws DNSSECException, IOException {
        if (bArr.length != eCKeyInfo.length * ASN1_INT) {
            throw new SignatureVerificationException();
        }
        DNSInput dNSInput = new DNSInput(bArr);
        DNSOutput dNSOutput = new DNSOutput();
        byte[] readByteArray = dNSInput.readByteArray(eCKeyInfo.length);
        int i = eCKeyInfo.length;
        if (readByteArray[0] < null) {
            i++;
        }
        byte[] readByteArray2 = dNSInput.readByteArray(eCKeyInfo.length);
        int i2 = eCKeyInfo.length;
        if (readByteArray2[0] < null) {
            i2++;
        }
        dNSOutput.writeU8(ASN1_SEQ);
        dNSOutput.writeU8((i + i2) + 4);
        dNSOutput.writeU8(ASN1_INT);
        dNSOutput.writeU8(i);
        if (i > eCKeyInfo.length) {
            dNSOutput.writeU8(0);
        }
        dNSOutput.writeByteArray(readByteArray);
        dNSOutput.writeU8(ASN1_INT);
        dNSOutput.writeU8(i2);
        if (i2 > eCKeyInfo.length) {
            dNSOutput.writeU8(0);
        }
        dNSOutput.writeByteArray(readByteArray2);
        return dNSOutput.toByteArray();
    }

    private static byte[] ECDSASignaturetoDNS(byte[] bArr, ECKeyInfo eCKeyInfo) throws IOException {
        DNSInput dNSInput = new DNSInput(bArr);
        DNSOutput dNSOutput = new DNSOutput();
        if (dNSInput.readU8() != ASN1_SEQ) {
            throw new IOException();
        }
        dNSInput.readU8();
        if (dNSInput.readU8() != ASN1_INT) {
            throw new IOException();
        }
        int readU8 = dNSInput.readU8();
        if (readU8 == eCKeyInfo.length + 1) {
            if (dNSInput.readU8() != 0) {
                throw new IOException();
            }
        } else if (readU8 != eCKeyInfo.length) {
            throw new IOException();
        }
        dNSOutput.writeByteArray(dNSInput.readByteArray(eCKeyInfo.length));
        if (dNSInput.readU8() != ASN1_INT) {
            throw new IOException();
        }
        readU8 = dNSInput.readU8();
        if (readU8 == eCKeyInfo.length + 1) {
            if (dNSInput.readU8() != 0) {
                throw new IOException();
            }
        } else if (readU8 != eCKeyInfo.length) {
            throw new IOException();
        }
        dNSOutput.writeByteArray(dNSInput.readByteArray(eCKeyInfo.length));
        return dNSOutput.toByteArray();
    }

    private static void verify(PublicKey publicKey, int i, byte[] bArr, byte[] bArr2) throws DNSSECException {
        byte[] DSASignaturefromDNS;
        if (publicKey instanceof DSAPublicKey) {
            try {
                DSASignaturefromDNS = DSASignaturefromDNS(bArr2);
            } catch (IOException e) {
                throw new IllegalStateException();
            }
        } else if (publicKey instanceof ECPublicKey) {
            switch (i) {
                case TrafficIncident.INCIDENTVERTEX_FIELD_NUMBER /*13*/:
                    DSASignaturefromDNS = ECDSASignaturefromDNS(bArr2, ECDSA_P256);
                    break;
                case TrafficIncident.STARTTIME_FIELD_NUMBER /*14*/:
                    DSASignaturefromDNS = ECDSASignaturefromDNS(bArr2, ECDSA_P384);
                    break;
                default:
                    try {
                        throw new UnsupportedAlgorithmException(i);
                    } catch (IOException e2) {
                        throw new IllegalStateException();
                    }
            }
        } else {
            DSASignaturefromDNS = bArr2;
        }
        try {
            Signature instance = Signature.getInstance(algString(i));
            instance.initVerify(publicKey);
            instance.update(bArr);
            if (!instance.verify(DSASignaturefromDNS)) {
                throw new SignatureVerificationException();
            }
        } catch (GeneralSecurityException e3) {
            throw new DNSSECException(e3.toString());
        }
    }

    private static boolean matches(SIGBase sIGBase, KEYBase kEYBase) {
        return kEYBase.getAlgorithm() == sIGBase.getAlgorithm() && kEYBase.getFootprint() == sIGBase.getFootprint() && kEYBase.getName().equals(sIGBase.getSigner());
    }

    public static void verify(RRset rRset, RRSIGRecord rRSIGRecord, DNSKEYRecord dNSKEYRecord) throws DNSSECException {
        if (matches(rRSIGRecord, dNSKEYRecord)) {
            Date date = new Date();
            if (date.compareTo(rRSIGRecord.getExpire()) > 0) {
                throw new SignatureExpiredException(rRSIGRecord.getExpire(), date);
            } else if (date.compareTo(rRSIGRecord.getTimeSigned()) < 0) {
                throw new SignatureNotYetValidException(rRSIGRecord.getTimeSigned(), date);
            } else {
                verify(dNSKEYRecord.getPublicKey(), rRSIGRecord.getAlgorithm(), digestRRset(rRSIGRecord, rRset), rRSIGRecord.getSignature());
                return;
            }
        }
        throw new KeyMismatchException(dNSKEYRecord, rRSIGRecord);
    }

    private static byte[] sign(PrivateKey privateKey, PublicKey publicKey, int i, byte[] bArr, String str) throws DNSSECException {
        Signature instance;
        if (str != null) {
            try {
                instance = Signature.getInstance(algString(i), str);
            } catch (GeneralSecurityException e) {
                throw new DNSSECException(e.toString());
            }
        }
        instance = Signature.getInstance(algString(i));
        instance.initSign(privateKey);
        instance.update(bArr);
        byte[] sign = instance.sign();
        if (publicKey instanceof DSAPublicKey) {
            try {
                return DSASignaturetoDNS(sign, (BigIntegerLength(((DSAPublicKey) publicKey).getParams().getP()) - 64) / 8);
            } catch (IOException e2) {
                throw new IllegalStateException();
            }
        } else if (!(publicKey instanceof ECPublicKey)) {
            return sign;
        } else {
            switch (i) {
                case TrafficIncident.INCIDENTVERTEX_FIELD_NUMBER /*13*/:
                    return ECDSASignaturetoDNS(sign, ECDSA_P256);
                case TrafficIncident.STARTTIME_FIELD_NUMBER /*14*/:
                    return ECDSASignaturetoDNS(sign, ECDSA_P384);
                default:
                    try {
                        throw new UnsupportedAlgorithmException(i);
                    } catch (IOException e3) {
                        throw new IllegalStateException();
                    }
            }
            throw new IllegalStateException();
        }
    }

    static void checkAlgorithm(PrivateKey privateKey, int i) throws UnsupportedAlgorithmException {
        switch (i) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
            case UninterpretedOption.STRING_VALUE_FIELD_NUMBER /*7*/:
            case FileOptions.JAVA_OUTER_CLASSNAME_FIELD_NUMBER /*8*/:
            case FileOptions.JAVA_MULTIPLE_FILES_FIELD_NUMBER /*10*/:
                if (!(privateKey instanceof RSAPrivateKey)) {
                    throw new IncompatibleKeyException();
                }
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
            case UninterpretedOption.DOUBLE_VALUE_FIELD_NUMBER /*6*/:
                if (!(privateKey instanceof DSAPrivateKey)) {
                    throw new IncompatibleKeyException();
                }
            case TrafficIncident.INCIDENTVERTEX_FIELD_NUMBER /*13*/:
            case TrafficIncident.STARTTIME_FIELD_NUMBER /*14*/:
                if (!(privateKey instanceof ECPrivateKey)) {
                    throw new IncompatibleKeyException();
                }
            default:
                throw new UnsupportedAlgorithmException(i);
        }
    }

    public static RRSIGRecord sign(RRset rRset, DNSKEYRecord dNSKEYRecord, PrivateKey privateKey, Date date, Date date2) throws DNSSECException {
        return sign(rRset, dNSKEYRecord, privateKey, date, date2, null);
    }

    public static RRSIGRecord sign(RRset rRset, DNSKEYRecord dNSKEYRecord, PrivateKey privateKey, Date date, Date date2, String str) throws DNSSECException {
        int algorithm = dNSKEYRecord.getAlgorithm();
        checkAlgorithm(privateKey, algorithm);
        RRSIGRecord rRSIGRecord = new RRSIGRecord(rRset.getName(), rRset.getDClass(), rRset.getTTL(), rRset.getType(), algorithm, rRset.getTTL(), date2, date, dNSKEYRecord.getFootprint(), dNSKEYRecord.getName(), null);
        rRSIGRecord.setSignature(sign(privateKey, dNSKEYRecord.getPublicKey(), algorithm, digestRRset(rRSIGRecord, rRset), str));
        return rRSIGRecord;
    }

    static SIGRecord signMessage(Message message, SIGRecord sIGRecord, KEYRecord kEYRecord, PrivateKey privateKey, Date date, Date date2) throws DNSSECException {
        int algorithm = kEYRecord.getAlgorithm();
        checkAlgorithm(privateKey, algorithm);
        SIGBase sIGRecord2 = new SIGRecord(Name.root, KEYRecord.PROTOCOL_ANY, 0, 0, algorithm, 0, date2, date, kEYRecord.getFootprint(), kEYRecord.getName(), null);
        DNSOutput dNSOutput = new DNSOutput();
        digestSIG(dNSOutput, sIGRecord2);
        if (sIGRecord != null) {
            dNSOutput.writeByteArray(sIGRecord.getSignature());
        }
        message.toWire(dNSOutput);
        sIGRecord2.setSignature(sign(privateKey, kEYRecord.getPublicKey(), algorithm, dNSOutput.toByteArray(), null));
        return sIGRecord2;
    }

    static void verifyMessage(Message message, byte[] bArr, SIGRecord sIGRecord, SIGRecord sIGRecord2, KEYRecord kEYRecord) throws DNSSECException {
        if (matches(sIGRecord, kEYRecord)) {
            Date date = new Date();
            if (date.compareTo(sIGRecord.getExpire()) > 0) {
                throw new SignatureExpiredException(sIGRecord.getExpire(), date);
            } else if (date.compareTo(sIGRecord.getTimeSigned()) < 0) {
                throw new SignatureNotYetValidException(sIGRecord.getTimeSigned(), date);
            } else {
                DNSOutput dNSOutput = new DNSOutput();
                digestSIG(dNSOutput, sIGRecord);
                if (sIGRecord2 != null) {
                    dNSOutput.writeByteArray(sIGRecord2.getSignature());
                }
                Header header = (Header) message.getHeader().clone();
                header.decCount(3);
                dNSOutput.writeByteArray(header.toWire());
                dNSOutput.writeByteArray(bArr, 12, message.sig0start - 12);
                verify(kEYRecord.getPublicKey(), sIGRecord.getAlgorithm(), dNSOutput.toByteArray(), sIGRecord.getSignature());
                return;
            }
        }
        throw new KeyMismatchException(kEYRecord, sIGRecord);
    }

    static byte[] generateDSDigest(DNSKEYRecord dNSKEYRecord, int i) {
        MessageDigest instance;
        switch (i) {
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                instance = MessageDigest.getInstance("sha-1");
                break;
            case ASN1_INT /*2*/:
                instance = MessageDigest.getInstance("sha-256");
                break;
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                instance = MessageDigest.getInstance("sha-384");
                break;
            default:
                try {
                    throw new IllegalArgumentException("unknown DS digest type " + i);
                } catch (NoSuchAlgorithmException e) {
                    throw new IllegalStateException("no message digest support");
                }
        }
        instance.update(dNSKEYRecord.getName().toWire());
        instance.update(dNSKEYRecord.rdataToWireCanonical());
        return instance.digest();
    }
}
