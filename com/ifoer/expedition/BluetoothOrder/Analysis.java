package com.ifoer.expedition.BluetoothOrder;

import com.ifoer.entity.AnalysisData;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xmlpull.v1.XmlPullParser;

public class Analysis {
    public static AnalysisData analysis(byte[] sendCommand, byte[] receive) {
        int flag;
        int i;
        AnalysisData analysisData = new AnalysisData();
        String requestWordStr = ByteHexHelper.bytesToHexString(new byte[]{sendCommand[7], sendCommand[8]});
        int requestCounter = ByteHexHelper.byteToInt(sendCommand[6]);
        byte[] pSendBuffer = null;
        if (sendCommand.length > 6) {
            int sendBagLength = ByteHexHelper.intPackLength(new byte[]{sendCommand[4], sendCommand[5]});
            if (sendBagLength > 0 && sendCommand.length >= sendBagLength + 7) {
                pSendBuffer = new byte[(sendBagLength - 3)];
                flag = 0;
                for (i = 9; i < (sendBagLength + 9) - 3; i++) {
                    pSendBuffer[flag] = sendCommand[i];
                    flag++;
                }
            }
        }
        if (receive == null || receive.length <= 0) {
            analysisData.setState(Boolean.valueOf(false));
        } else if (receive.length <= 1 || !ByteHexHelper.byteToHexString(receive[0]).equalsIgnoreCase("55")) {
            analysisData.setState(Boolean.valueOf(false));
        } else if (receive.length <= 2 || !ByteHexHelper.byteToHexString(receive[1]).equalsIgnoreCase("aa")) {
            analysisData.setState(Boolean.valueOf(false));
        } else if (receive.length > 6) {
            int feedbackBagLength = ByteHexHelper.intPackLength(new byte[]{receive[4], receive[5]});
            if (feedbackBagLength <= 0 || receive.length < feedbackBagLength + 7) {
                analysisData.setState(Boolean.valueOf(false));
            } else if (ByteHexHelper.byteToInt(receive[6]) == requestCounter) {
                String feedbackWordStr = ByteHexHelper.bytesToHexString(new byte[]{receive[7], receive[8]});
                if (feedbackWordStr.substring(0, 1).equalsIgnoreCase("6") && feedbackWordStr.substring(1).equalsIgnoreCase(requestWordStr.substring(1))) {
                    byte[] pReceiveBuffer = new byte[(feedbackBagLength - 3)];
                    flag = 0;
                    for (i = 9; i < (feedbackBagLength + 9) - 3; i++) {
                        pReceiveBuffer[flag] = receive[i];
                        flag++;
                    }
                    analysisData.setState(Boolean.valueOf(true));
                    analysisData.setpReceiveBuffer(pReceiveBuffer);
                    analysisData.setpRequestBuffer(pSendBuffer);
                    analysisData.setRequestWordStr(requestWordStr);
                } else {
                    analysisData.setState(Boolean.valueOf(false));
                }
            } else {
                analysisData.setState(Boolean.valueOf(false));
            }
        } else {
            analysisData.setState(Boolean.valueOf(false));
        }
        return analysisData;
    }

    public static boolean analysisData(byte[] sendCommand, byte[] receive) {
        AnalysisData analysisData = new AnalysisData();
        String requestWordStr = ByteHexHelper.bytesToHexString(new byte[]{sendCommand[7], sendCommand[8]});
        int requestCounter = ByteHexHelper.byteToInt(sendCommand[6]);
        if (sendCommand.length > 6) {
            int sendBagLength = ByteHexHelper.intPackLength(new byte[]{sendCommand[4], sendCommand[5]});
            if (sendBagLength > 0 && sendCommand.length >= sendBagLength + 7) {
                byte[] pSendBuffer = new byte[(sendBagLength - 3)];
                int flag = 0;
                for (int i = 9; i < (sendBagLength + 9) - 3; i++) {
                    pSendBuffer[flag] = sendCommand[i];
                    flag++;
                }
            }
        }
        if (receive == null || receive.length <= 0) {
            analysisData.setState(Boolean.valueOf(false));
        } else if (receive.length <= 1 || !ByteHexHelper.byteToHexString(receive[0]).equalsIgnoreCase("55")) {
            analysisData.setState(Boolean.valueOf(false));
        } else if (receive.length <= 2 || !ByteHexHelper.byteToHexString(receive[1]).equalsIgnoreCase("aa")) {
            analysisData.setState(Boolean.valueOf(false));
        } else if (receive.length > 6) {
            int feedbackBagLength = ByteHexHelper.intPackLength(new byte[]{receive[4], receive[5]});
            if (feedbackBagLength <= 0 || receive.length < feedbackBagLength + 7) {
                analysisData.setState(Boolean.valueOf(false));
            } else if (ByteHexHelper.byteToInt(receive[6]) == requestCounter) {
                String feedbackWordStr = ByteHexHelper.bytesToHexString(new byte[]{receive[7], receive[8]});
                if (feedbackWordStr.substring(0, 1).equalsIgnoreCase("6") && feedbackWordStr.substring(1).equalsIgnoreCase(requestWordStr.substring(1))) {
                    return true;
                }
                analysisData.setState(Boolean.valueOf(false));
            } else {
                analysisData.setState(Boolean.valueOf(false));
            }
        } else {
            analysisData.setState(Boolean.valueOf(false));
        }
        return false;
    }

    public static Boolean analysis2111(AnalysisData analysisData) {
        boolean isSuccess;
        if (!analysisData.getState().booleanValue()) {
            isSuccess = false;
        } else if (ByteHexHelper.bytesToHexString(analysisData.getpReceiveBuffer()).equalsIgnoreCase("00")) {
            isSuccess = true;
        } else {
            isSuccess = false;
        }
        return Boolean.valueOf(isSuccess);
    }

    public static Boolean analysis2505(AnalysisData analysisData) {
        boolean isSuccess;
        if (analysisData.getState().booleanValue()) {
            isSuccess = true;
        } else {
            isSuccess = false;
        }
        return Boolean.valueOf(isSuccess);
    }

    public static Boolean analysis2504(AnalysisData analysisData) {
        boolean isSuccess;
        if (analysisData.getState().booleanValue()) {
            isSuccess = true;
        } else {
            isSuccess = false;
        }
        return Boolean.valueOf(isSuccess);
    }

    public static Boolean analysis2110(AnalysisData analysisData) {
        boolean isSuccess;
        if (!analysisData.getState().booleanValue()) {
            isSuccess = false;
        } else if (ByteHexHelper.bytesToHexString(analysisData.getpReceiveBuffer()).equalsIgnoreCase("00")) {
            isSuccess = true;
        } else {
            isSuccess = false;
        }
        return Boolean.valueOf(isSuccess);
    }

    public static String analysis2502(AnalysisData analysisData) {
        boolean state = analysisData.getState().booleanValue();
        String result = XmlPullParser.NO_NAMESPACE;
        if (!state) {
            return result;
        }
        byte[] receiverBuffer = analysisData.getpReceiveBuffer();
        byte[] requestBuffer = analysisData.getpRequestBuffer();
        if (requestBuffer.length <= 0) {
            return result;
        }
        String reqParam = ByteHexHelper.byteToHexString(requestBuffer[0]);
        String resParam = ByteHexHelper.byteToHexString(receiverBuffer[0]);
        if (!reqParam.equals(resParam)) {
            return result;
        }
        byte[] src;
        int i;
        if (resParam.equalsIgnoreCase("01")) {
            src = new byte[3];
            for (i = 0; i < 3; i++) {
                src[i] = receiverBuffer[i];
            }
            return ByteHexHelper.bytesToHexString(src);
        } else if (!resParam.equalsIgnoreCase("02")) {
            return result;
        } else {
            src = new byte[5];
            for (i = 0; i < 5; i++) {
                src[i] = receiverBuffer[i];
            }
            return ByteHexHelper.bytesToHexString(src);
        }
    }

    public static Boolean analysis2503(AnalysisData analysisData) {
        boolean isSuccess = false;
        if (analysisData.getState().booleanValue()) {
            byte[] request = analysisData.getpRequestBuffer();
            byte[] receiverBuffer = analysisData.getpReceiveBuffer();
            if (request.length > 0 && receiverBuffer.length > 0 && ByteHexHelper.byteToHexString(request[0]).equalsIgnoreCase(ByteHexHelper.byteToHexString(receiverBuffer[0])) && receiverBuffer.length > 1) {
                String res = ByteHexHelper.byteToHexString(receiverBuffer[1]);
                if (res.equalsIgnoreCase("00")) {
                    isSuccess = true;
                } else if (res.equalsIgnoreCase("01")) {
                    isSuccess = false;
                } else if (res.equalsIgnoreCase("02")) {
                    isSuccess = false;
                }
            }
        } else {
            isSuccess = false;
        }
        return Boolean.valueOf(isSuccess);
    }

    public static String analysis2114(AnalysisData analysisData) {
        String runningmode = XmlPullParser.NO_NAMESPACE;
        return ByteHexHelper.byteToHexString(analysisData.getpReceiveBuffer()[0]);
    }

    public static String analysis2105(AnalysisData analysisData) {
        boolean state = analysisData.getState().booleanValue();
        String downLoadBinVer = XmlPullParser.NO_NAMESPACE;
        if (state) {
            byte[] params = DpuOrderUtils.filterOutCmdParameters(analysisData.getpReceiveBuffer());
            if (params != null && params.length >= 3) {
                return (String) ByteHexHelper.toStringArray(params).get(1);
            }
        }
        downLoadBinVer = XmlPullParser.NO_NAMESPACE;
        return downLoadBinVer;
    }

    public static Boolean analysis2407(AnalysisData analysisData) {
        boolean isSuccess;
        String status = XmlPullParser.NO_NAMESPACE;
        boolean state = analysisData.getState().booleanValue();
        byte[] receiverBuffer = analysisData.getpReceiveBuffer();
        if (!state) {
            isSuccess = false;
        } else if (ByteHexHelper.byteToHexString(receiverBuffer[0]).equalsIgnoreCase("00")) {
            isSuccess = true;
        } else {
            isSuccess = false;
        }
        return Boolean.valueOf(isSuccess);
    }

    public static Boolean analysis2402(AnalysisData analysisData) {
        boolean isSuccess;
        String status = XmlPullParser.NO_NAMESPACE;
        boolean state = analysisData.getState().booleanValue();
        byte[] receiverBuffer = analysisData.getpReceiveBuffer();
        if (!state) {
            isSuccess = false;
        } else if (ByteHexHelper.byteToHexString(receiverBuffer[0]).equalsIgnoreCase("00")) {
            isSuccess = true;
        } else {
            isSuccess = false;
        }
        return Boolean.valueOf(isSuccess);
    }

    public static Boolean analysis2404(AnalysisData analysisData) {
        boolean isSuccess;
        String status = XmlPullParser.NO_NAMESPACE;
        boolean state = analysisData.getState().booleanValue();
        byte[] receiverBuffer = analysisData.getpReceiveBuffer();
        if (!state) {
            isSuccess = false;
        } else if (ByteHexHelper.byteToHexString(receiverBuffer[0]).equalsIgnoreCase("00")) {
            isSuccess = true;
        } else {
            isSuccess = false;
        }
        return Boolean.valueOf(isSuccess);
    }

    public static Boolean analysis2405(AnalysisData analysisData) {
        boolean isSuccess;
        String status = XmlPullParser.NO_NAMESPACE;
        boolean state = analysisData.getState().booleanValue();
        byte[] receiverBuffer = analysisData.getpReceiveBuffer();
        if (!state) {
            isSuccess = false;
        } else if (ByteHexHelper.byteToHexString(receiverBuffer[0]).equalsIgnoreCase("00")) {
            isSuccess = true;
        } else {
            isSuccess = false;
        }
        return Boolean.valueOf(isSuccess);
    }

    public static Boolean analysis2403(AnalysisData analysisData) {
        boolean isSuccess;
        String status = XmlPullParser.NO_NAMESPACE;
        boolean state = analysisData.getState().booleanValue();
        byte[] receiverBuffer = analysisData.getpReceiveBuffer();
        if (!state) {
            isSuccess = false;
        } else if (ByteHexHelper.byteToHexString(receiverBuffer[0]).equalsIgnoreCase("00")) {
            isSuccess = true;
        } else {
            isSuccess = false;
        }
        return Boolean.valueOf(isSuccess);
    }

    public static String[] analysis2103(AnalysisData analysisData) {
        boolean state = analysisData.getState().booleanValue();
        StringBuffer buffer = new StringBuffer();
        String[] content = null;
        if (state) {
            byte[] receiverBuffer = analysisData.getpReceiveBuffer();
            int receiverLength = receiverBuffer.length;
            if (receiverLength > 0 && receiverLength > 2) {
                int chipFlagLength = ByteHexHelper.intPackLength(new byte[]{receiverBuffer[0], receiverBuffer[1]});
                if (receiverLength > chipFlagLength + 2) {
                    int i;
                    byte[] chipFlag = new byte[(chipFlagLength - 1)];
                    int flag = 0;
                    for (i = 2; i < (2 + chipFlagLength) - 1; i++) {
                        chipFlag[flag] = receiverBuffer[i];
                        flag++;
                    }
                    buffer.append(new StringBuilder(String.valueOf(new String(chipFlag))).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).toString());
                    int len = 2 + chipFlagLength;
                    if (receiverLength > len + 2) {
                        int proSeriNumLength = ByteHexHelper.intPackLength(new byte[]{receiverBuffer[len], receiverBuffer[len + 1]});
                        len += 2;
                        byte[] psm = new byte[(proSeriNumLength - 1)];
                        flag = 0;
                        for (i = len; i < (len + proSeriNumLength) - 1; i++) {
                            psm[flag] = receiverBuffer[i];
                            flag++;
                        }
                        buffer.append(new StringBuilder(String.valueOf(new String(psm))).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).toString());
                        len += proSeriNumLength;
                        if (receiverLength > len + 2) {
                            int pcbVerLength = ByteHexHelper.intPackLength(new byte[]{receiverBuffer[len], receiverBuffer[len + 1]});
                            len += 2;
                            byte[] pcb = new byte[(pcbVerLength - 1)];
                            flag = 0;
                            for (i = len; i < (len + pcbVerLength) - 1; i++) {
                                pcb[flag] = receiverBuffer[i];
                                flag++;
                            }
                            buffer.append(new StringBuilder(String.valueOf(new String(pcb))).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).toString());
                            len += pcbVerLength;
                            if (receiverLength > len + 2) {
                                int DateLength = ByteHexHelper.intPackLength(new byte[]{receiverBuffer[len], receiverBuffer[len + 1]});
                                len += 2;
                                byte[] date = new byte[(DateLength - 1)];
                                flag = 0;
                                for (i = len; i < (len + DateLength) - 1; i++) {
                                    date[flag] = receiverBuffer[i];
                                    flag++;
                                }
                                buffer.append(new StringBuilder(String.valueOf(new String(date))).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).toString());
                                len += DateLength;
                                if (receiverLength > len + 2) {
                                    int deviceTypeLen = ByteHexHelper.intPackLength(new byte[]{receiverBuffer[len], receiverBuffer[len + 1]});
                                    len += 2;
                                    byte[] device = new byte[(deviceTypeLen - 1)];
                                    flag = 0;
                                    for (i = len; i < (len + deviceTypeLen) - 1; i++) {
                                        device[flag] = receiverBuffer[i];
                                        flag++;
                                    }
                                    buffer.append(new StringBuilder(String.valueOf(new String(device))).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).toString());
                                }
                            }
                        }
                    }
                }
            }
        }
        if (buffer.length() > 0) {
            content = buffer.toString().split(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR);
        }
        for (String str : content) {
        }
        return content;
    }

    public static Boolean analysis2109(AnalysisData analysisData) {
        boolean isSuccess = false;
        if (analysisData.getState().booleanValue()) {
            byte[] receiverBuffer = analysisData.getpReceiveBuffer();
            byte[] requestBuffer = analysisData.getpRequestBuffer();
            if (receiverBuffer.length > 0 && requestBuffer.length > 0 && ByteHexHelper.byteToHexString(receiverBuffer[0]).equalsIgnoreCase(ByteHexHelper.byteToHexString(receiverBuffer[0])) && receiverBuffer.length > 1) {
                String response = ByteHexHelper.byteToHexString(receiverBuffer[1]);
                if (response.equalsIgnoreCase("00")) {
                    isSuccess = true;
                } else {
                    returnParam(response);
                }
            }
        }
        return Boolean.valueOf(isSuccess);
    }

    public static void returnParam(String returnValue) {
        switch (Integer.parseInt(returnValue, 16)) {
        }
    }
}
