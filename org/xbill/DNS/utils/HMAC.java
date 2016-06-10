package org.xbill.DNS.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class HMAC {
    private static final byte IPAD = (byte) 54;
    private static final byte OPAD = (byte) 92;
    private int blockLength;
    private MessageDigest digest;
    private byte[] ipad;
    private byte[] opad;

    private void init(byte[] bArr) {
        if (bArr.length > this.blockLength) {
            bArr = this.digest.digest(bArr);
            this.digest.reset();
        }
        this.ipad = new byte[this.blockLength];
        this.opad = new byte[this.blockLength];
        int i = 0;
        while (i < bArr.length) {
            this.ipad[i] = (byte) (bArr[i] ^ 54);
            this.opad[i] = (byte) (bArr[i] ^ 92);
            i++;
        }
        while (i < this.blockLength) {
            this.ipad[i] = IPAD;
            this.opad[i] = OPAD;
            i++;
        }
        this.digest.update(this.ipad);
    }

    public HMAC(MessageDigest messageDigest, int i, byte[] bArr) {
        messageDigest.reset();
        this.digest = messageDigest;
        this.blockLength = i;
        init(bArr);
    }

    public HMAC(String str, int i, byte[] bArr) {
        try {
            this.digest = MessageDigest.getInstance(str);
            this.blockLength = i;
            init(bArr);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("unknown digest algorithm " + str);
        }
    }

    public HMAC(MessageDigest messageDigest, byte[] bArr) {
        this(messageDigest, 64, bArr);
    }

    public HMAC(String str, byte[] bArr) {
        this(str, 64, bArr);
    }

    public void update(byte[] bArr, int i, int i2) {
        this.digest.update(bArr, i, i2);
    }

    public void update(byte[] bArr) {
        this.digest.update(bArr);
    }

    public byte[] sign() {
        byte[] digest = this.digest.digest();
        this.digest.reset();
        this.digest.update(this.opad);
        return this.digest.digest(digest);
    }

    public boolean verify(byte[] bArr) {
        return verify(bArr, false);
    }

    public boolean verify(byte[] bArr, boolean z) {
        byte[] bArr2;
        Object sign = sign();
        if (!z || bArr.length >= sign.length) {
            Object obj = sign;
        } else {
            bArr2 = new byte[bArr.length];
            System.arraycopy(sign, 0, bArr2, 0, bArr2.length);
        }
        return Arrays.equals(bArr, bArr2);
    }

    public void clear() {
        this.digest.reset();
        this.digest.update(this.ipad);
    }

    public int digestLength() {
        return this.digest.getDigestLength();
    }
}
