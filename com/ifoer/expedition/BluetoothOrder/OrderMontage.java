package com.ifoer.expedition.BluetoothOrder;

import java.io.File;
import java.nio.Buffer;
import java.text.DecimalFormat;
import org.xmlpull.v1.XmlPullParser;

public class OrderMontage {
    private static Boolean f1287D = null;
    private static String commandWord = null;
    private static String counter = null;
    private static String dataArea = null;
    private static String packLengths = null;
    private static String packVerify = null;
    private static final String source = "F8";
    private static final String startCode = "55aa";
    private static final String target = "F0";

    static {
        counter = XmlPullParser.NO_NAMESPACE;
        packLengths = XmlPullParser.NO_NAMESPACE;
        commandWord = XmlPullParser.NO_NAMESPACE;
        dataArea = XmlPullParser.NO_NAMESPACE;
        packVerify = XmlPullParser.NO_NAMESPACE;
        f1287D = Boolean.valueOf(false);
    }

    public static byte[] readClock2102() {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "2102";
        dataArea = XmlPullParser.NO_NAMESPACE;
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return ByteHexHelper.hexStringToBytes(order);
    }

    public static byte[] setClock2101() {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "2101";
        dataArea = ByteHexHelper.currentData();
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return ByteHexHelper.hexStringToBytes(order);
    }

    public static byte[] DPUVerInfo2103() {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "2103";
        dataArea = XmlPullParser.NO_NAMESPACE;
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return ByteHexHelper.hexStringToBytes(order);
    }

    public static byte[] DPUKuVer2104() {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "2104";
        dataArea = XmlPullParser.NO_NAMESPACE;
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return ByteHexHelper.hexStringToBytes(order);
    }

    public static byte[] send2407() {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "2407";
        dataArea = XmlPullParser.NO_NAMESPACE;
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        return ByteHexHelper.hexStringToBytes("55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify);
    }

    public static byte[] DPUVer2105() {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "2105";
        dataArea = XmlPullParser.NO_NAMESPACE;
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return ByteHexHelper.hexStringToBytes(order);
    }

    public static byte[] DPU2106() {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "2106";
        dataArea = XmlPullParser.NO_NAMESPACE;
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return ByteHexHelper.hexStringToBytes(order);
    }

    public static byte[] writeDPUSerialNum2107(String mode, String serialNum) {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "2107";
        dataArea = new StringBuilder(String.valueOf(mode)).append(serialNum).toString();
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return ByteHexHelper.hexStringToBytes(order);
    }

    public static byte[] setBluetoothName2108(String name) {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "2108";
        dataArea = name;
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return ByteHexHelper.hexStringToBytes(order);
    }

    public static byte[] transferDPUMode2109(int mode) {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "2109";
        dataArea = new DecimalFormat("00").format((long) mode);
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return ByteHexHelper.hexStringToBytes(order);
    }

    public static byte[] clearFlash210A(String code) {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "210A";
        dataArea = code;
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return ByteHexHelper.hexStringToBytes(order);
    }

    public static byte[] modifyPw210B(String oldPw, String newPw) {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "210B";
        dataArea = new StringBuilder(String.valueOf(oldPw)).append(newPw).toString();
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return ByteHexHelper.hexStringToBytes(order);
    }

    public static byte[] resumePw210f(String str) {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "210f";
        dataArea = str;
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return ByteHexHelper.hexStringToBytes(order);
    }

    public static byte[] resumePw2110(String oldPw) {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "2110";
        dataArea = ByteHexHelper.dpuString(oldPw);
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return ByteHexHelper.hexStringToBytes(order);
    }

    public static byte[] download2111() {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "2111";
        dataArea = XmlPullParser.NO_NAMESPACE;
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return ByteHexHelper.hexStringToBytes(order);
    }

    public static byte[] writeConFile2112(Short fileLength, Buffer content) {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "2112";
        dataArea = fileLength + content.toString();
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return ByteHexHelper.hexStringToBytes(order);
    }

    public static byte[] readConFile2113() {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "2113";
        dataArea = XmlPullParser.NO_NAMESPACE;
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return ByteHexHelper.hexStringToBytes(order);
    }

    public static byte[] currentStatus2114() {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "2114";
        dataArea = XmlPullParser.NO_NAMESPACE;
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return ByteHexHelper.hexStringToBytes(order);
    }

    public static byte[] DPULicence2115() {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "2115";
        dataArea = XmlPullParser.NO_NAMESPACE;
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return ByteHexHelper.hexStringToBytes(order);
    }

    public static byte[] DPUBluetoothAddress2116() {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "2116";
        dataArea = XmlPullParser.NO_NAMESPACE;
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return ByteHexHelper.hexStringToBytes(order);
    }

    public static byte[] checkDPU2117(String ucContext, String ucSignature) {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "2117";
        dataArea = new StringBuilder(String.valueOf(ucContext)).append(ucSignature).toString();
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return ByteHexHelper.hexStringToBytes(order);
    }

    public static byte[] writeConnector2118(String fileName, Short fileSize, Buffer fileContent) {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "2118";
        dataArea = new StringBuilder(String.valueOf(fileName)).append(fileSize).append(fileContent.toString()).toString();
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return ByteHexHelper.hexStringToBytes(order);
    }

    public static byte[] readConnector2119(String fileName) {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "2119";
        dataArea = fileName;
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return ByteHexHelper.hexStringToBytes(order);
    }

    public static byte[] exitSimpleDiagnostic211a() {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "211a";
        dataArea = XmlPullParser.NO_NAMESPACE;
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return ByteHexHelper.hexStringToBytes(order);
    }

    public static byte[] intoLowPowerMode211b() {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "211b";
        dataArea = XmlPullParser.NO_NAMESPACE;
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return ByteHexHelper.hexStringToBytes(order);
    }

    public static byte[] upgrade2401(String buffer) {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "2401";
        dataArea = buffer;
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return ByteHexHelper.hexStringToBytes(order);
    }

    public static byte[] upgradeFileName2402(String fileName, Long fileSize) {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "2402";
        dataArea = new StringBuilder(String.valueOf(fileName)).append(fileSize).toString();
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return ByteHexHelper.hexStringToBytes(order);
    }

    public static byte[] upgradeFileContent2403(Long writePosition, Short dataLength, Buffer fileContent) {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "2403";
        dataArea = new StringBuilder(String.valueOf(writePosition.longValue() + ((long) dataLength.shortValue()))).append(fileContent.toString()).toString();
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return ByteHexHelper.hexStringToBytes(order);
    }

    public static byte[] upgradeFileConVerify2404(String md5Str) {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "2404";
        dataArea = md5Str;
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return ByteHexHelper.hexStringToBytes(order);
    }

    public static byte[] upgradeComplete2405() {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "2405";
        dataArea = XmlPullParser.NO_NAMESPACE;
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return ByteHexHelper.hexStringToBytes(order);
    }

    public static byte[] breakpointResume2406() {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "2406";
        dataArea = XmlPullParser.NO_NAMESPACE;
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return ByteHexHelper.hexStringToBytes(order);
    }

    public static byte[] updateFirmware2407() {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "2407";
        dataArea = XmlPullParser.NO_NAMESPACE;
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return ByteHexHelper.hexStringToBytes(order);
    }

    public static byte[] readModelFileInfo2408(String buffer) {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "2408";
        dataArea = buffer;
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return ByteHexHelper.hexStringToBytes(order);
    }

    public static byte[] link2501() {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "2501";
        dataArea = XmlPullParser.NO_NAMESPACE;
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return ByteHexHelper.hexStringToBytes(order);
    }

    public static byte[] requestConnect2502() {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "2502";
        dataArea = "02";
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        if (f1287D.booleanValue()) {
            System.out.println("\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return ByteHexHelper.hexStringToBytes(order);
    }

    public static byte[] securityCheck2503(String verify) {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "2503";
        dataArea = verify;
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return ByteHexHelper.hexStringToBytes(order);
    }

    public static byte[] disconnected2504() {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "2504";
        dataArea = XmlPullParser.NO_NAMESPACE;
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return ByteHexHelper.hexStringToBytes(order);
    }

    public static byte[] resetConnector2505() {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "2505";
        dataArea = XmlPullParser.NO_NAMESPACE;
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return ByteHexHelper.hexStringToBytes(order);
    }

    public static byte[] requestConnect2114() {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "2114";
        dataArea = "02";
        packLengths = ByteHexHelper.packLength(counter + commandWord);
        packVerify = ByteHexHelper.XOR("F0F8" + packLengths + counter + commandWord);
        return ByteHexHelper.hexStringToBytes("55aaF0F8" + packLengths + counter + commandWord + packVerify);
    }

    public static byte[] diagnosticList2500() {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "2500";
        dataArea = XmlPullParser.NO_NAMESPACE;
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return ByteHexHelper.hexStringToBytes(order);
    }

    public static byte[] smartBox2701No(byte[] param) {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = XmlPullParser.NO_NAMESPACE;
        dataArea = ByteHexHelper.bytesToHexString(param);
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        byte[] o2701 = ByteHexHelper.hexStringToBytes(order);
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return o2701;
    }

    public static byte[] sendFileNameAndLength2402(File file) {
        byte[] param = DpuOrderUtils.fileNameAndLength(file.getName(), file);
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "2402";
        dataArea = ByteHexHelper.bytesToHexString(param);
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        byte[] o2402 = ByteHexHelper.hexStringToBytes(order);
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return o2402;
    }

    public static byte[] sendUpdateFileMd52404(String md5) {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "2404";
        dataArea = ByteHexHelper.bytesToHexString(md5.getBytes());
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        byte[] o2404 = ByteHexHelper.hexStringToBytes(order);
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return o2404;
    }

    public static byte[] ValidateUpdateFinished2405() {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "2405";
        dataArea = XmlPullParser.NO_NAMESPACE;
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        byte[] o2405 = ByteHexHelper.hexStringToBytes(order);
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return o2405;
    }

    public static byte[] ValidateAllFilesMd52408() {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "2403";
        dataArea = XmlPullParser.NO_NAMESPACE;
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        byte[] o2408 = ByteHexHelper.hexStringToBytes(order);
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return o2408;
    }

    public static byte[] sendUpdateFilesContent2403(byte[] param) {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "2403";
        dataArea = ByteHexHelper.bytesToHexString(param);
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        byte[] o2403 = ByteHexHelper.hexStringToBytes(order);
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return o2403;
    }

    public static byte[] smartBox2701(byte[] param) {
        String counters = ByteHexHelper.RandomMethod();
        while (counters.equalsIgnoreCase(counter)) {
            counters = ByteHexHelper.RandomMethod();
        }
        counter = counters;
        commandWord = "2701";
        dataArea = ByteHexHelper.bytesToHexString(param);
        packLengths = ByteHexHelper.packLength(counter + commandWord + dataArea);
        packVerify = ByteHexHelper.packVerify(target, source, packLengths, counter, commandWord, dataArea);
        String order = "55aaF0F8" + packLengths + counter + commandWord + dataArea + packVerify;
        byte[] o2701 = ByteHexHelper.hexStringToBytes(order);
        if (f1287D.booleanValue()) {
            System.out.println("\u84dd\u7259\u901a\u8baf\u7684\u5b8c\u6574\u6307\u4ee4\u4e3a\uff1a " + order);
        }
        return o2701;
    }
}
