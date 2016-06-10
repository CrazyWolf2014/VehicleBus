package com.ifoer.util;

import com.google.zxing.client.result.ExpandedProductParsedResult;
import com.ifoer.entity.SptActiveTestStream;
import com.ifoer.entity.SptExDataStreamIdItem;
import com.ifoer.entity.SptVwDataStreamIdItem;
import com.thoughtworks.xstream.XStream;
import java.util.ArrayList;

public class MeasureConversion {
    public static final int[] KEEPSIZE;

    static {
        KEEPSIZE = new int[]{1, 10, 100, 1000, XStream.PRIORITY_VERY_HIGH};
    }

    public static void convertMetricToImperial(ArrayList<SptExDataStreamIdItem> dataStreamList, int pointSize) {
        ArrayList<SptExDataStreamIdItem> tempList = new ArrayList();
        if (dataStreamList != null && dataStreamList.size() != 0) {
            int i;
            for (i = 0; i < dataStreamList.size(); i++) {
                SptExDataStreamIdItem temp = (SptExDataStreamIdItem) dataStreamList.get(i);
                if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase(ExpandedProductParsedResult.KILOGRAM)) {
                    temp.setStreamState("lb");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 2.205d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u516c\u65a4")) {
                    temp.setStreamState("\u78c5");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 2.205d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u5343\u514b")) {
                    temp.setStreamState("\u78c5");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 2.205d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("G")) {
                    temp.setStreamState("oz");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.035274d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u514b")) {
                    temp.setStreamState("\u76ce\u53f8");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.035274d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u6beb\u514b")) {
                    temp.setStreamState("\u514b\u62c9");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.005d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("mg")) {
                    temp.setStreamState("ct");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.005d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("KM")) {
                    temp.setStreamState("mile");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.621d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u516c\u91cc")) {
                    temp.setStreamState("\u82f1\u91cc");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.621d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("M")) {
                    temp.setStreamState("yd");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.0936d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u7c73")) {
                    temp.setStreamState("\u7801");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.0936d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("DM")) {
                    temp.setStreamState("FT");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.328d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u5206\u7c73")) {
                    temp.setStreamState("\u82f1\u5c3a");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.328d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("CM")) {
                    temp.setStreamState("inch");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.3937008d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u5398\u7c73")) {
                    temp.setStreamState("\u540b");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.3937008d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("MM")) {
                    temp.setStreamState("inch");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.0393701d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u6beb\u7c73")) {
                    temp.setStreamState("\u540b");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.0393701d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("km^2")) {
                    temp.setStreamState("mi^2");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.3861022d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u5e73\u65b9\u5343\u7c73")) {
                    temp.setStreamState("\u5e73\u65b9\u82f1\u91cc");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.3861022d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("m^2")) {
                    temp.setStreamState("sq yd");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.19599d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u5e73\u65b9\u7c73")) {
                    temp.setStreamState("\u5e73\u65b9\u7801");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.19599d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("dm^2")) {
                    temp.setStreamState("sq ft");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.1076391d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u5e73\u65b9\u5206\u7c73")) {
                    temp.setStreamState("\u5e73\u65b9\u82f1\u5c3a");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.1076391d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("cm^2")) {
                    temp.setStreamState("sq inch");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.1550003d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u5e73\u65b9\u5398\u7c73")) {
                    temp.setStreamState("\u5e73\u65b9\u82f1\u5bf8");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.1550003d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("mm^2")) {
                    temp.setStreamState("sq inch");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.00155d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u5e73\u65b9\u6beb\u7c73")) {
                    temp.setStreamState("\u5e73\u65b9\u82f1\u5bf8");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.0016d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("km^3")) {
                    temp.setStreamState("mi^3");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.2399d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u7acb\u65b9\u5343\u7c73")) {
                    temp.setStreamState("\u7acb\u65b9\u82f1\u91cc");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.2399d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("m^3")) {
                    temp.setStreamState("yd^3");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.308d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u7acb\u65b9\u7c73")) {
                    temp.setStreamState("\u7acb\u65b9\u7801");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.308d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("dm^3")) {
                    temp.setStreamState("ft^3");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.0353d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u7acb\u65b9\u5206\u7c73")) {
                    temp.setStreamState("\u7acb\u65b9\u82f1\u5c3a");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.0353d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("cm^3")) {
                    temp.setStreamState("in^3");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.061d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u7acb\u65b9\u5398\u7c73")) {
                    temp.setStreamState("\u7acb\u65b9\u82f1\u5bf8");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.061d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("mm^3")) {
                    temp.setStreamState("in^3");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 6.0E-4d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u7acb\u65b9\u6beb\u7c73")) {
                    temp.setStreamState("\u7acb\u65b9\u82f1\u5bf8");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 6.0E-4d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("L")) {
                    temp.setStreamState("gal.");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.22d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u5347")) {
                    temp.setStreamState("\u52a0\u4ed1");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.22d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("mL")) {
                    temp.setStreamState("fl oz");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.0353d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u6beb\u5347")) {
                    temp.setStreamState("\u6db2\u76ce\u53f8");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.0353d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("KM/H")) {
                    temp.setStreamState("mph");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.6213712d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u516c\u91cc\u6bcf\u65f6")) {
                    temp.setStreamState("\u82f1\u91cc\u6bcf\u65f6");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.6213712d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u5343\u7c73\u6bcf\u5c0f\u65f6")) {
                    temp.setStreamState("\u82f1\u91cc\u6bcf\u65f6");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.6213712d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("km/s")) {
                    temp.setStreamState("mps");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.6214d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u5343\u7c73/\u79d2")) {
                    temp.setStreamState("\u82f1\u91cc\u6bcf\u79d2");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.6214d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u5343\u7c73\u6bcf\u79d2")) {
                    temp.setStreamState("\u82f1\u91cc\u6bcf\u79d2");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.6214d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("m/s")) {
                    temp.setStreamState("ydps");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.0936d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u7c73/\u79d2")) {
                    temp.setStreamState("\u7801\u6bcf\u79d2");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.0936d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u7c73\u6bcf\u79d2")) {
                    temp.setStreamState("\u7801\u6bcf\u79d2");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.0936d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("Nm")) {
                    temp.setStreamState("lb-ft");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.7382d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u725b\u7c73")) {
                    temp.setStreamState("\u78c5\u82f1\u5c3a");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.7382d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("kg.f")) {
                    temp.setStreamState("kip");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.0022d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u5343\u514b\u529b")) {
                    temp.setStreamState("\u5343\u78c5\u529b");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.0022d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("KPA")) {
                    temp.setStreamState("psi");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 20.8854351d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u5343\u5e15")) {
                    temp.setStreamState("\u78c5\u6bcf\u5e73\u65b9\u82f1\u5c3a");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 20.8854351d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u00b0C")) {
                    temp.setStreamState("\u00b0F");
                    temp.setStreamStr((((double) Math.round((((temp.getStreamStrDouble() * 9.0d) / 5.0d) + 32.0d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u2103")) {
                    temp.setStreamState("\u00b0F");
                    temp.setStreamStr((((double) Math.round((((temp.getStreamStrDouble() * 9.0d) / 5.0d) + 32.0d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u00baC")) {
                    temp.setStreamState("\u00b0F");
                    temp.setStreamStr((((double) Math.round((((temp.getStreamStrDouble() * 9.0d) / 5.0d) + 32.0d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().trim().equalsIgnoreCase("DEGREE C")) {
                    temp.setStreamState("Degree F");
                    temp.setStreamStr((((double) Math.round((((temp.getStreamStrDouble() * 9.0d) / 5.0d) + 32.0d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u6444\u6c0f\u5ea6")) {
                    temp.setStreamState("\u534e\u6c0f\u5ea6");
                    temp.setStreamStr((((double) Math.round((((temp.getStreamStrDouble() * 9.0d) / 5.0d) + 32.0d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u5ea6")) {
                    temp.setStreamState("\u534e\u6c0f\u5ea6");
                    temp.setStreamStr((((double) Math.round((((temp.getStreamStrDouble() * 9.0d) / 5.0d) + 32.0d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                }
                tempList.add(temp);
            }
            dataStreamList.clear();
            for (i = 0; i < tempList.size(); i++) {
                dataStreamList.add((SptExDataStreamIdItem) tempList.get(i));
            }
        }
    }

    public static void convertImperialToMetric(ArrayList<SptExDataStreamIdItem> dataStreamList, int pointSize) {
        ArrayList<SptExDataStreamIdItem> tempList = new ArrayList();
        if (dataStreamList != null && dataStreamList.size() != 0) {
            int i;
            for (i = 0; i < dataStreamList.size(); i++) {
                SptExDataStreamIdItem temp = (SptExDataStreamIdItem) dataStreamList.get(i);
                if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("lb")) {
                    temp.setStreamState(ExpandedProductParsedResult.KILOGRAM);
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.4535d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u78c5")) {
                    temp.setStreamState("\u516c\u65a4");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.4535d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u78c5")) {
                    temp.setStreamState("\u5343\u514b");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.4535d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("oz")) {
                    temp.setStreamState("G");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 28.3286d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u76ce\u53f8")) {
                    temp.setStreamState("\u514b");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 28.3286d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u514b\u62c9")) {
                    temp.setStreamState("\u6beb\u514b");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 200.0d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("ct")) {
                    temp.setStreamState("mg");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 200.0d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("mile")) {
                    temp.setStreamState("KM");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.6093d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u82f1\u91cc")) {
                    temp.setStreamState("\u516c\u91cc");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.6093d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("yd")) {
                    temp.setStreamState("M");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.9144d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u7801")) {
                    temp.setStreamState("\u7c73");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.9144d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("FT")) {
                    temp.setStreamState("DM");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 3.0488d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u82f1\u5c3a")) {
                    temp.setStreamState("\u5206\u7c73");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 3.0488d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("inch")) {
                    temp.setStreamState("CM");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 2.54d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u540b")) {
                    temp.setStreamState("\u5398\u7c73");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 2.54d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("inch")) {
                    temp.setStreamState("MM");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 25.3807d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u540b")) {
                    temp.setStreamState("\u6beb\u7c73");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 25.3807d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("mi^2")) {
                    temp.setStreamState("km^2");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 2.59d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u5e73\u65b9\u82f1\u91cc")) {
                    temp.setStreamState("\u5e73\u65b9\u5343\u7c73");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 2.59d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().trim().equalsIgnoreCase("sqyd")) {
                    temp.setStreamState("m^2");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.8361d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u5e73\u65b9\u7801")) {
                    temp.setStreamState("\u5e73\u65b9\u7c73");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.8361d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().trim().equalsIgnoreCase("sqft")) {
                    temp.setStreamState("dm^2");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 9.2937d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u5e73\u65b9\u82f1\u5c3a")) {
                    temp.setStreamState("\u5e73\u65b9\u5206\u7c73");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 9.2937d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().trim().equalsIgnoreCase("sqinch")) {
                    temp.setStreamState("cm^2");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 6.4516d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u5e73\u65b9\u82f1\u5bf8")) {
                    temp.setStreamState("\u5e73\u65b9\u5398\u7c73");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 6.4516d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("sq inch")) {
                    temp.setStreamState("mm^2");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 645.16d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u5e73\u65b9\u82f1\u5bf8")) {
                    temp.setStreamState("\u5e73\u65b9\u6beb\u7c73");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 645.16d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("mi^3")) {
                    temp.setStreamState("km^3");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 4.1684d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u7acb\u65b9\u82f1\u91cc")) {
                    temp.setStreamState("\u7acb\u65b9\u5343\u7c73");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 4.1684d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("yd^3")) {
                    temp.setStreamState("m^3");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.7645d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u7acb\u65b9\u7801")) {
                    temp.setStreamState("\u7acb\u65b9\u7c73");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.7645d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("ft^3")) {
                    temp.setStreamState("dm^3");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 28.3286d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u7acb\u65b9\u82f1\u5c3a")) {
                    temp.setStreamState("\u7acb\u65b9\u5206\u7c73");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 28.3286d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("in^3")) {
                    temp.setStreamState("cm^3");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 16.387d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u7acb\u65b9\u82f1\u5bf8")) {
                    temp.setStreamState("\u7acb\u65b9\u5398\u7c73");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 16.387d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("in^3")) {
                    temp.setStreamState("mm^3");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 16387.04d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u7acb\u65b9\u82f1\u5bf8")) {
                    temp.setStreamState("\u7acb\u65b9\u6beb\u7c73");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 16387.04d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("gal.")) {
                    temp.setStreamState("L");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 4.5461d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u52a0\u4ed1")) {
                    temp.setStreamState("\u5347");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 4.5461d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("fl oz")) {
                    temp.setStreamState("mL");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 28.3286d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u6db2\u76ce\u53f8")) {
                    temp.setStreamState("\u6beb\u5347");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 28.3286d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("mph")) {
                    temp.setStreamState("KM/H");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.6093d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u82f1\u91cc\u6bcf\u65f6")) {
                    temp.setStreamState("\u516c\u91cc\u6bcf\u65f6");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.6093d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u82f1\u91cc\u6bcf\u65f6")) {
                    temp.setStreamState("\u5343\u7c73\u6bcf\u5c0f\u65f6");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.6093d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("mps")) {
                    temp.setStreamState("km/s");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.6093d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u82f1\u91cc\u6bcf\u79d2")) {
                    temp.setStreamState("\u5343\u7c73/\u79d2");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.6093d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u82f1\u91cc\u6bcf\u79d2")) {
                    temp.setStreamState("\u5343\u7c73\u6bcf\u79d2");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.6093d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("ydps")) {
                    temp.setStreamState("m/s");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.9144d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u7801\u6bcf\u79d2")) {
                    temp.setStreamState("\u7c73/\u79d2");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.9144d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u7801\u6bcf\u79d2")) {
                    temp.setStreamState("\u7c73\u6bcf\u79d2");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.9144d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("lb-ft")) {
                    temp.setStreamState("Nm");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.3546d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u78c5\u82f1\u5c3a")) {
                    temp.setStreamState("\u725b\u7c73");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.3546d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("kip")) {
                    temp.setStreamState("kg.f");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 453.59d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u5343\u78c5\u529b")) {
                    temp.setStreamState("\u5343\u514b\u529b");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 453.59d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equalsIgnoreCase("psi")) {
                    temp.setStreamState("KPA");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.0479d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u78c5\u6bcf\u5e73\u65b9\u82f1\u5c3a")) {
                    temp.setStreamState("\u5343\u5e15");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.0479d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u00b0F")) {
                    temp.setStreamState("\u00b0C");
                    temp.setStreamStr((((double) Math.round((((temp.getStreamStrDouble() - 32.0d) * 5.0d) / 9.0d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u00b0F")) {
                    temp.setStreamState("\u2103");
                    temp.setStreamStr((((double) Math.round((((temp.getStreamStrDouble() - 32.0d) * 5.0d) / 9.0d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u00b0F")) {
                    temp.setStreamState("\u00baC");
                    temp.setStreamStr((((double) Math.round((((temp.getStreamStrDouble() - 32.0d) * 5.0d) / 9.0d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().trim().equalsIgnoreCase("Degree F")) {
                    temp.setStreamState("DEGREE C");
                    temp.setStreamStr((((double) Math.round((((temp.getStreamStrDouble() - 32.0d) * 5.0d) / 9.0d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u534e\u6c0f\u5ea6")) {
                    temp.setStreamState("\u6444\u6c0f\u5ea6");
                    temp.setStreamStr((((double) Math.round((((temp.getStreamStrDouble() - 32.0d) * 5.0d) / 9.0d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptExDataStreamIdItem) dataStreamList.get(i)).getStreamState().equals("\u534e\u6c0f\u5ea6")) {
                    temp.setStreamState("\u5ea6");
                    temp.setStreamStr((((double) Math.round((((temp.getStreamStrDouble() - 32.0d) * 5.0d) / 9.0d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                }
                tempList.add(temp);
            }
            dataStreamList.clear();
            for (i = 0; i < tempList.size(); i++) {
                dataStreamList.add((SptExDataStreamIdItem) tempList.get(i));
            }
        }
    }

    public static void convertMetricToImperialVM(ArrayList<SptVwDataStreamIdItem> dataStreamList, int pointSize) {
        ArrayList<SptVwDataStreamIdItem> tempList = new ArrayList();
        if (dataStreamList != null && dataStreamList.size() != 0) {
            int i;
            for (i = 0; i < dataStreamList.size(); i++) {
                SptVwDataStreamIdItem temp = (SptVwDataStreamIdItem) dataStreamList.get(i);
                if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase(ExpandedProductParsedResult.KILOGRAM)) {
                    temp.setStreamUnitIdContent("lb");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 2.205d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u516c\u65a4")) {
                    temp.setStreamUnitIdContent("\u78c5");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 2.205d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u5343\u514b")) {
                    temp.setStreamUnitIdContent("\u78c5");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 2.205d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("G")) {
                    temp.setStreamUnitIdContent("oz");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.035274d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u514b")) {
                    temp.setStreamUnitIdContent("\u76ce\u53f8");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.035274d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u6beb\u514b")) {
                    temp.setStreamUnitIdContent("\u514b\u62c9");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.005d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("mg")) {
                    temp.setStreamUnitIdContent("ct");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.005d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("KM")) {
                    temp.setStreamUnitIdContent("mile");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.621d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u516c\u91cc")) {
                    temp.setStreamUnitIdContent("\u82f1\u91cc");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.621d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("M")) {
                    temp.setStreamUnitIdContent("yd");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.0936d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u7c73")) {
                    temp.setStreamUnitIdContent("\u7801");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.0936d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("DM")) {
                    temp.setStreamUnitIdContent("FT");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.328d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u5206\u7c73")) {
                    temp.setStreamUnitIdContent("\u82f1\u5c3a");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.328d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("CM")) {
                    temp.setStreamUnitIdContent("inch");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.3937008d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u5398\u7c73")) {
                    temp.setStreamUnitIdContent("\u540b");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.3937008d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("MM")) {
                    temp.setStreamUnitIdContent("inch");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.0393701d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u6beb\u7c73")) {
                    temp.setStreamUnitIdContent("\u540b");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.0393701d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("km^2")) {
                    temp.setStreamUnitIdContent("mi^2");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.3861022d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u5e73\u65b9\u5343\u7c73")) {
                    temp.setStreamUnitIdContent("\u5e73\u65b9\u82f1\u91cc");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.3861022d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("m^2")) {
                    temp.setStreamUnitIdContent("sq yd");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.19599d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u5e73\u65b9\u7c73")) {
                    temp.setStreamUnitIdContent("\u5e73\u65b9\u7801");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.19599d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("dm^2")) {
                    temp.setStreamUnitIdContent("sq ft");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.1076391d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u5e73\u65b9\u5206\u7c73")) {
                    temp.setStreamUnitIdContent("\u5e73\u65b9\u82f1\u5c3a");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.1076391d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("cm^2")) {
                    temp.setStreamUnitIdContent("sq inch");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.1550003d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u5e73\u65b9\u5398\u7c73")) {
                    temp.setStreamUnitIdContent("\u5e73\u65b9\u82f1\u5bf8");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.1550003d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("mm^2")) {
                    temp.setStreamUnitIdContent("sq inch");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.00155d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u5e73\u65b9\u6beb\u7c73")) {
                    temp.setStreamUnitIdContent("\u5e73\u65b9\u82f1\u5bf8");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.0016d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("km^3")) {
                    temp.setStreamUnitIdContent("mi^3");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.2399d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u7acb\u65b9\u5343\u7c73")) {
                    temp.setStreamUnitIdContent("\u7acb\u65b9\u82f1\u91cc");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.2399d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("m^3")) {
                    temp.setStreamUnitIdContent("yd^3");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.308d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u7acb\u65b9\u7c73")) {
                    temp.setStreamUnitIdContent("\u7acb\u65b9\u7801");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.308d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("dm^3")) {
                    temp.setStreamUnitIdContent("ft^3");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.0353d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u7acb\u65b9\u5206\u7c73")) {
                    temp.setStreamUnitIdContent("\u7acb\u65b9\u82f1\u5c3a");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.0353d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("cm^3")) {
                    temp.setStreamUnitIdContent("in^3");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.061d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u7acb\u65b9\u5398\u7c73")) {
                    temp.setStreamUnitIdContent("\u7acb\u65b9\u82f1\u5bf8");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.061d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("mm^3")) {
                    temp.setStreamUnitIdContent("in^3");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 6.0E-4d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u7acb\u65b9\u6beb\u7c73")) {
                    temp.setStreamUnitIdContent("\u7acb\u65b9\u82f1\u5bf8");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 6.0E-4d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("L")) {
                    temp.setStreamUnitIdContent("gal.");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.22d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u5347")) {
                    temp.setStreamUnitIdContent("\u52a0\u4ed1");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.22d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("mL")) {
                    temp.setStreamUnitIdContent("fl oz");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.0353d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u6beb\u5347")) {
                    temp.setStreamUnitIdContent("\u6db2\u76ce\u53f8");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.0353d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("KM/H")) {
                    temp.setStreamUnitIdContent("mph");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.6213712d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u516c\u91cc\u6bcf\u65f6")) {
                    temp.setStreamUnitIdContent("\u82f1\u91cc\u6bcf\u65f6");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.6213712d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u5343\u7c73\u6bcf\u5c0f\u65f6")) {
                    temp.setStreamUnitIdContent("\u82f1\u91cc\u6bcf\u65f6");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.6213712d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("km/s")) {
                    temp.setStreamUnitIdContent("mps");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.6214d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u5343\u7c73/\u79d2")) {
                    temp.setStreamUnitIdContent("\u82f1\u91cc\u6bcf\u79d2");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.6214d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u5343\u7c73\u6bcf\u79d2")) {
                    temp.setStreamUnitIdContent("\u82f1\u91cc\u6bcf\u79d2");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.6214d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("m/s")) {
                    temp.setStreamUnitIdContent("ydps");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.0936d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u7c73/\u79d2")) {
                    temp.setStreamUnitIdContent("\u7801\u6bcf\u79d2");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.0936d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u7c73\u6bcf\u79d2")) {
                    temp.setStreamUnitIdContent("\u7801\u6bcf\u79d2");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.0936d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("Nm")) {
                    temp.setStreamUnitIdContent("lb-ft");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.7382d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u725b\u7c73")) {
                    temp.setStreamUnitIdContent("\u78c5\u82f1\u5c3a");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.7382d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("kg.f")) {
                    temp.setStreamUnitIdContent("kip");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.0022d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u5343\u514b\u529b")) {
                    temp.setStreamUnitIdContent("\u5343\u78c5\u529b");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.0022d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("KPA")) {
                    temp.setStreamUnitIdContent("psi");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 20.8854351d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u5343\u5e15")) {
                    temp.setStreamUnitIdContent("\u78c5\u6bcf\u5e73\u65b9\u82f1\u5c3a");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 20.8854351d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u00b0C")) {
                    temp.setStreamUnitIdContent("\u00b0F");
                    temp.setStreamStr((((double) Math.round((((temp.getStreamStrDouble() * 9.0d) / 5.0d) + 32.0d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u2103")) {
                    temp.setStreamUnitIdContent("\u00b0F");
                    temp.setStreamStr((((double) Math.round((((temp.getStreamStrDouble() * 9.0d) / 5.0d) + 32.0d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u00baC")) {
                    temp.setStreamUnitIdContent("\u00b0F");
                    temp.setStreamStr((((double) Math.round((((temp.getStreamStrDouble() * 9.0d) / 5.0d) + 32.0d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().trim().equalsIgnoreCase("DEGREE C")) {
                    temp.setStreamUnitIdContent("Degree F");
                    temp.setStreamStr((((double) Math.round((((temp.getStreamStrDouble() * 9.0d) / 5.0d) + 32.0d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u6444\u6c0f\u5ea6")) {
                    temp.setStreamUnitIdContent("\u534e\u6c0f\u5ea6");
                    temp.setStreamStr((((double) Math.round((((temp.getStreamStrDouble() * 9.0d) / 5.0d) + 32.0d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u5ea6")) {
                    temp.setStreamUnitIdContent("\u534e\u6c0f\u5ea6");
                    temp.setStreamStr((((double) Math.round((((temp.getStreamStrDouble() * 9.0d) / 5.0d) + 32.0d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                }
                tempList.add(temp);
            }
            dataStreamList.clear();
            for (i = 0; i < tempList.size(); i++) {
                dataStreamList.add((SptVwDataStreamIdItem) tempList.get(i));
            }
        }
    }

    public static void convertImperialToMetricVM(ArrayList<SptVwDataStreamIdItem> dataStreamList, int pointSize) {
        ArrayList<SptVwDataStreamIdItem> tempList = new ArrayList();
        if (dataStreamList != null && dataStreamList.size() != 0) {
            int i;
            for (i = 0; i < dataStreamList.size(); i++) {
                SptVwDataStreamIdItem temp = (SptVwDataStreamIdItem) dataStreamList.get(i);
                if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("lb")) {
                    temp.setStreamUnitIdContent(ExpandedProductParsedResult.KILOGRAM);
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.4535d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u78c5")) {
                    temp.setStreamUnitIdContent("\u516c\u65a4");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.4535d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u78c5")) {
                    temp.setStreamUnitIdContent("\u5343\u514b");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.4535d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("oz")) {
                    temp.setStreamUnitIdContent("G");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 28.3286d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u76ce\u53f8")) {
                    temp.setStreamUnitIdContent("\u514b");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 28.3286d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u514b\u62c9")) {
                    temp.setStreamUnitIdContent("\u6beb\u514b");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 200.0d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("ct")) {
                    temp.setStreamUnitIdContent("mg");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 200.0d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("mile")) {
                    temp.setStreamUnitIdContent("KM");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.6093d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u82f1\u91cc")) {
                    temp.setStreamUnitIdContent("\u516c\u91cc");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.6093d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("yd")) {
                    temp.setStreamUnitIdContent("M");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.9144d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u7801")) {
                    temp.setStreamUnitIdContent("\u7c73");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.9144d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("FT")) {
                    temp.setStreamUnitIdContent("DM");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 3.0488d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u82f1\u5c3a")) {
                    temp.setStreamUnitIdContent("\u5206\u7c73");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 3.0488d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("inch")) {
                    temp.setStreamUnitIdContent("CM");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 2.54d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u540b")) {
                    temp.setStreamUnitIdContent("\u5398\u7c73");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 2.54d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("inch")) {
                    temp.setStreamUnitIdContent("MM");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 25.3807d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u540b")) {
                    temp.setStreamUnitIdContent("\u6beb\u7c73");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 25.3807d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("mi^2")) {
                    temp.setStreamUnitIdContent("km^2");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 2.59d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u5e73\u65b9\u82f1\u91cc")) {
                    temp.setStreamUnitIdContent("\u5e73\u65b9\u5343\u7c73");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 2.59d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().trim().equalsIgnoreCase("sqyd")) {
                    temp.setStreamUnitIdContent("m^2");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.8361d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u5e73\u65b9\u7801")) {
                    temp.setStreamUnitIdContent("\u5e73\u65b9\u7c73");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.8361d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().trim().equalsIgnoreCase("sqft")) {
                    temp.setStreamUnitIdContent("dm^2");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 9.2937d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u5e73\u65b9\u82f1\u5c3a")) {
                    temp.setStreamUnitIdContent("\u5e73\u65b9\u5206\u7c73");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 9.2937d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().trim().equalsIgnoreCase("sqinch")) {
                    temp.setStreamUnitIdContent("cm^2");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 6.4516d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u5e73\u65b9\u82f1\u5bf8")) {
                    temp.setStreamUnitIdContent("\u5e73\u65b9\u5398\u7c73");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 6.4516d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("sq inch")) {
                    temp.setStreamUnitIdContent("mm^2");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 645.16d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u5e73\u65b9\u82f1\u5bf8")) {
                    temp.setStreamUnitIdContent("\u5e73\u65b9\u6beb\u7c73");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 645.16d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("mi^3")) {
                    temp.setStreamUnitIdContent("km^3");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 4.1684d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u7acb\u65b9\u82f1\u91cc")) {
                    temp.setStreamUnitIdContent("\u7acb\u65b9\u5343\u7c73");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 4.1684d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("yd^3")) {
                    temp.setStreamUnitIdContent("m^3");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.7645d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u7acb\u65b9\u7801")) {
                    temp.setStreamUnitIdContent("\u7acb\u65b9\u7c73");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.7645d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("ft^3")) {
                    temp.setStreamUnitIdContent("dm^3");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 28.3286d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u7acb\u65b9\u82f1\u5c3a")) {
                    temp.setStreamUnitIdContent("\u7acb\u65b9\u5206\u7c73");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 28.3286d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("in^3")) {
                    temp.setStreamUnitIdContent("cm^3");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 16.387d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u7acb\u65b9\u82f1\u5bf8")) {
                    temp.setStreamUnitIdContent("\u7acb\u65b9\u5398\u7c73");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 16.387d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("in^3")) {
                    temp.setStreamUnitIdContent("mm^3");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 16387.04d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u7acb\u65b9\u82f1\u5bf8")) {
                    temp.setStreamUnitIdContent("\u7acb\u65b9\u6beb\u7c73");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 16387.04d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("gal.")) {
                    temp.setStreamUnitIdContent("L");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 4.5461d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u52a0\u4ed1")) {
                    temp.setStreamUnitIdContent("\u5347");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 4.5461d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("fl oz")) {
                    temp.setStreamUnitIdContent("mL");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 28.3286d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u6db2\u76ce\u53f8")) {
                    temp.setStreamUnitIdContent("\u6beb\u5347");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 28.3286d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("mph")) {
                    temp.setStreamUnitIdContent("KM/H");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.6093d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u82f1\u91cc\u6bcf\u65f6")) {
                    temp.setStreamUnitIdContent("\u516c\u91cc\u6bcf\u65f6");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.6093d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u82f1\u91cc\u6bcf\u65f6")) {
                    temp.setStreamUnitIdContent("\u5343\u7c73\u6bcf\u5c0f\u65f6");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.6093d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("mps")) {
                    temp.setStreamUnitIdContent("km/s");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.6093d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u82f1\u91cc\u6bcf\u79d2")) {
                    temp.setStreamUnitIdContent("\u5343\u7c73/\u79d2");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.6093d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u82f1\u91cc\u6bcf\u79d2")) {
                    temp.setStreamUnitIdContent("\u5343\u7c73\u6bcf\u79d2");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.6093d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("ydps")) {
                    temp.setStreamUnitIdContent("m/s");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.9144d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u7801\u6bcf\u79d2")) {
                    temp.setStreamUnitIdContent("\u7c73/\u79d2");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.9144d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u7801\u6bcf\u79d2")) {
                    temp.setStreamUnitIdContent("\u7c73\u6bcf\u79d2");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.9144d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("lb-ft")) {
                    temp.setStreamUnitIdContent("Nm");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.3546d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u78c5\u82f1\u5c3a")) {
                    temp.setStreamUnitIdContent("\u725b\u7c73");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.3546d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("kip")) {
                    temp.setStreamUnitIdContent("kg.f");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 453.59d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u5343\u78c5\u529b")) {
                    temp.setStreamUnitIdContent("\u5343\u514b\u529b");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 453.59d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equalsIgnoreCase("psi")) {
                    temp.setStreamUnitIdContent("KPA");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.0479d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u78c5\u6bcf\u5e73\u65b9\u82f1\u5c3a")) {
                    temp.setStreamUnitIdContent("\u5343\u5e15");
                    temp.setStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.0479d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u00b0F")) {
                    temp.setStreamUnitIdContent("\u00b0C");
                    temp.setStreamStr((((double) Math.round((((temp.getStreamStrDouble() - 32.0d) * 5.0d) / 9.0d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u00b0F")) {
                    temp.setStreamUnitIdContent("\u2103");
                    temp.setStreamStr((((double) Math.round((((temp.getStreamStrDouble() - 32.0d) * 5.0d) / 9.0d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u00b0F")) {
                    temp.setStreamUnitIdContent("\u00baC");
                    temp.setStreamStr((((double) Math.round((((temp.getStreamStrDouble() - 32.0d) * 5.0d) / 9.0d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().trim().equalsIgnoreCase("Degree F")) {
                    temp.setStreamUnitIdContent("DEGREE C");
                    temp.setStreamStr((((double) Math.round((((temp.getStreamStrDouble() - 32.0d) * 5.0d) / 9.0d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u534e\u6c0f\u5ea6")) {
                    temp.setStreamUnitIdContent("\u6444\u6c0f\u5ea6");
                    temp.setStreamStr((((double) Math.round((((temp.getStreamStrDouble() - 32.0d) * 5.0d) / 9.0d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptVwDataStreamIdItem) dataStreamList.get(i)).getStreamUnitIdContent().equals("\u534e\u6c0f\u5ea6")) {
                    temp.setStreamUnitIdContent("\u5ea6");
                    temp.setStreamStr((((double) Math.round((((temp.getStreamStrDouble() - 32.0d) * 5.0d) / 9.0d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                }
                tempList.add(temp);
            }
            dataStreamList.clear();
            for (i = 0; i < tempList.size(); i++) {
                dataStreamList.add((SptVwDataStreamIdItem) tempList.get(i));
            }
        }
    }

    public static void convertMetricToImperialActiveTest(ArrayList<SptActiveTestStream> dataStreamList, int pointSize) {
        ArrayList<SptActiveTestStream> tempList = new ArrayList();
        if (dataStreamList != null && dataStreamList.size() != 0) {
            int i;
            for (i = 0; i < dataStreamList.size(); i++) {
                SptActiveTestStream temp = (SptActiveTestStream) dataStreamList.get(i);
                if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase(ExpandedProductParsedResult.KILOGRAM)) {
                    temp.setUnit("lb");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 2.205d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u516c\u65a4")) {
                    temp.setUnit("\u78c5");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 2.205d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u5343\u514b")) {
                    temp.setUnit("\u78c5");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 2.205d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("G")) {
                    temp.setUnit("oz");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.035274d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u514b")) {
                    temp.setUnit("\u76ce\u53f8");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.035274d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u6beb\u514b")) {
                    temp.setUnit("\u514b\u62c9");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.005d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("mg")) {
                    temp.setUnit("ct");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.005d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("KM")) {
                    temp.setUnit("mile");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.621d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u516c\u91cc")) {
                    temp.setUnit("\u82f1\u91cc");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.621d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("M")) {
                    temp.setUnit("yd");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.0936d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u7c73")) {
                    temp.setUnit("\u7801");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.0936d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("DM")) {
                    temp.setUnit("FT");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.328d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u5206\u7c73")) {
                    temp.setUnit("\u82f1\u5c3a");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.328d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("CM")) {
                    temp.setUnit("inch");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.3937008d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u5398\u7c73")) {
                    temp.setUnit("\u540b");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.3937008d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("MM")) {
                    temp.setUnit("inch");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.0393701d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u6beb\u7c73")) {
                    temp.setUnit("\u540b");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.0393701d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("km^2")) {
                    temp.setUnit("mi^2");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.3861022d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u5e73\u65b9\u5343\u7c73")) {
                    temp.setUnit("\u5e73\u65b9\u82f1\u91cc");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.3861022d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("m^2")) {
                    temp.setUnit("sq yd");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.19599d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u5e73\u65b9\u7c73")) {
                    temp.setUnit("\u5e73\u65b9\u7801");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.19599d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("dm^2")) {
                    temp.setUnit("sq ft");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.1076391d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u5e73\u65b9\u5206\u7c73")) {
                    temp.setUnit("\u5e73\u65b9\u82f1\u5c3a");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.1076391d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("cm^2")) {
                    temp.setUnit("sq inch");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.1550003d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u5e73\u65b9\u5398\u7c73")) {
                    temp.setUnit("\u5e73\u65b9\u82f1\u5bf8");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.1550003d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("mm^2")) {
                    temp.setUnit("sq inch");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.00155d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u5e73\u65b9\u6beb\u7c73")) {
                    temp.setUnit("\u5e73\u65b9\u82f1\u5bf8");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.0016d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("km^3")) {
                    temp.setUnit("mi^3");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.2399d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u7acb\u65b9\u5343\u7c73")) {
                    temp.setUnit("\u7acb\u65b9\u82f1\u91cc");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.2399d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("m^3")) {
                    temp.setUnit("yd^3");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.308d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u7acb\u65b9\u7c73")) {
                    temp.setUnit("\u7acb\u65b9\u7801");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.308d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("dm^3")) {
                    temp.setUnit("ft^3");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.0353d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u7acb\u65b9\u5206\u7c73")) {
                    temp.setUnit("\u7acb\u65b9\u82f1\u5c3a");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.0353d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("cm^3")) {
                    temp.setUnit("in^3");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.061d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u7acb\u65b9\u5398\u7c73")) {
                    temp.setUnit("\u7acb\u65b9\u82f1\u5bf8");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.061d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("mm^3")) {
                    temp.setUnit("in^3");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 6.0E-4d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u7acb\u65b9\u6beb\u7c73")) {
                    temp.setUnit("\u7acb\u65b9\u82f1\u5bf8");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 6.0E-4d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("L")) {
                    temp.setUnit("gal.");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.22d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u5347")) {
                    temp.setUnit("\u52a0\u4ed1");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.22d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("mL")) {
                    temp.setUnit("fl oz");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.0353d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u6beb\u5347")) {
                    temp.setUnit("\u6db2\u76ce\u53f8");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.0353d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("KM/H")) {
                    temp.setUnit("mph");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.6213712d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u516c\u91cc\u6bcf\u65f6")) {
                    temp.setUnit("\u82f1\u91cc\u6bcf\u65f6");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.6213712d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u5343\u7c73\u6bcf\u5c0f\u65f6")) {
                    temp.setUnit("\u82f1\u91cc\u6bcf\u65f6");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.6213712d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("km/s")) {
                    temp.setUnit("mps");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.6214d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u5343\u7c73/\u79d2")) {
                    temp.setUnit("\u82f1\u91cc\u6bcf\u79d2");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.6214d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u5343\u7c73\u6bcf\u79d2")) {
                    temp.setUnit("\u82f1\u91cc\u6bcf\u79d2");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.6214d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("m/s")) {
                    temp.setUnit("ydps");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.0936d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u7c73/\u79d2")) {
                    temp.setUnit("\u7801\u6bcf\u79d2");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.0936d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u7c73\u6bcf\u79d2")) {
                    temp.setUnit("\u7801\u6bcf\u79d2");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.0936d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("Nm")) {
                    temp.setUnit("lb-ft");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.7382d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u725b\u7c73")) {
                    temp.setUnit("\u78c5\u82f1\u5c3a");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.7382d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("kg.f")) {
                    temp.setUnit("kip");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.0022d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u5343\u514b\u529b")) {
                    temp.setUnit("\u5343\u78c5\u529b");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.0022d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("KPA")) {
                    temp.setUnit("psi");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 20.8854351d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u5343\u5e15")) {
                    temp.setUnit("\u78c5\u6bcf\u5e73\u65b9\u82f1\u5c3a");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 20.8854351d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u00b0C")) {
                    temp.setUnit("\u00b0F");
                    temp.setDataStreamStr((((double) Math.round((((temp.getStreamStrDouble() * 9.0d) / 5.0d) + 32.0d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u2103")) {
                    temp.setUnit("\u00b0F");
                    temp.setDataStreamStr((((double) Math.round((((temp.getStreamStrDouble() * 9.0d) / 5.0d) + 32.0d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u00baC")) {
                    temp.setUnit("\u00b0F");
                    temp.setDataStreamStr((((double) Math.round((((temp.getStreamStrDouble() * 9.0d) / 5.0d) + 32.0d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().trim().equalsIgnoreCase("DEGREE C")) {
                    temp.setUnit("Degree F");
                    temp.setDataStreamStr((((double) Math.round((((temp.getStreamStrDouble() * 9.0d) / 5.0d) + 32.0d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u6444\u6c0f\u5ea6")) {
                    temp.setUnit("\u534e\u6c0f\u5ea6");
                    temp.setDataStreamStr((((double) Math.round((((temp.getStreamStrDouble() * 9.0d) / 5.0d) + 32.0d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u5ea6")) {
                    temp.setUnit("\u534e\u6c0f\u5ea6");
                    temp.setDataStreamStr((((double) Math.round((((temp.getStreamStrDouble() * 9.0d) / 5.0d) + 32.0d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                }
                tempList.add(temp);
            }
            dataStreamList.clear();
            for (i = 0; i < tempList.size(); i++) {
                dataStreamList.add((SptActiveTestStream) tempList.get(i));
            }
        }
    }

    public static void convertImperialToMetricActiveTest(ArrayList<SptActiveTestStream> dataStreamList, int pointSize) {
        ArrayList<SptActiveTestStream> tempList = new ArrayList();
        if (dataStreamList != null && dataStreamList.size() != 0) {
            int i;
            for (i = 0; i < dataStreamList.size(); i++) {
                SptActiveTestStream temp = (SptActiveTestStream) dataStreamList.get(i);
                if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("lb")) {
                    temp.setUnit(ExpandedProductParsedResult.KILOGRAM);
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.4535d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u78c5")) {
                    temp.setUnit("\u516c\u65a4");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.4535d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u78c5")) {
                    temp.setUnit("\u5343\u514b");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.4535d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("oz")) {
                    temp.setUnit("G");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 28.3286d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u76ce\u53f8")) {
                    temp.setUnit("\u514b");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 28.3286d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u514b\u62c9")) {
                    temp.setUnit("\u6beb\u514b");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 200.0d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("ct")) {
                    temp.setUnit("mg");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 200.0d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("mile")) {
                    temp.setUnit("KM");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.6093d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u82f1\u91cc")) {
                    temp.setUnit("\u516c\u91cc");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.6093d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("yd")) {
                    temp.setUnit("M");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.9144d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u7801")) {
                    temp.setUnit("\u7c73");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.9144d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("FT")) {
                    temp.setUnit("DM");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 3.0488d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u82f1\u5c3a")) {
                    temp.setUnit("\u5206\u7c73");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 3.0488d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("inch")) {
                    temp.setUnit("CM");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 2.54d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u540b")) {
                    temp.setUnit("\u5398\u7c73");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 2.54d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("inch")) {
                    temp.setUnit("MM");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 25.3807d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u540b")) {
                    temp.setUnit("\u6beb\u7c73");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 25.3807d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("mi^2")) {
                    temp.setUnit("km^2");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 2.59d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u5e73\u65b9\u82f1\u91cc")) {
                    temp.setUnit("\u5e73\u65b9\u5343\u7c73");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 2.59d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().trim().equalsIgnoreCase("sqyd")) {
                    temp.setUnit("m^2");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.8361d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u5e73\u65b9\u7801")) {
                    temp.setUnit("\u5e73\u65b9\u7c73");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.8361d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().trim().equalsIgnoreCase("sqft")) {
                    temp.setUnit("dm^2");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 9.2937d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u5e73\u65b9\u82f1\u5c3a")) {
                    temp.setUnit("\u5e73\u65b9\u5206\u7c73");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 9.2937d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().trim().equalsIgnoreCase("sqinch")) {
                    temp.setUnit("cm^2");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 6.4516d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u5e73\u65b9\u82f1\u5bf8")) {
                    temp.setUnit("\u5e73\u65b9\u5398\u7c73");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 6.4516d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("sq inch")) {
                    temp.setUnit("mm^2");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 645.16d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u5e73\u65b9\u82f1\u5bf8")) {
                    temp.setUnit("\u5e73\u65b9\u6beb\u7c73");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 645.16d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("mi^3")) {
                    temp.setUnit("km^3");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 4.1684d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u7acb\u65b9\u82f1\u91cc")) {
                    temp.setUnit("\u7acb\u65b9\u5343\u7c73");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 4.1684d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("yd^3")) {
                    temp.setUnit("m^3");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.7645d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u7acb\u65b9\u7801")) {
                    temp.setUnit("\u7acb\u65b9\u7c73");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.7645d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("ft^3")) {
                    temp.setUnit("dm^3");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 28.3286d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u7acb\u65b9\u82f1\u5c3a")) {
                    temp.setUnit("\u7acb\u65b9\u5206\u7c73");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 28.3286d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("in^3")) {
                    temp.setUnit("cm^3");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 16.387d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u7acb\u65b9\u82f1\u5bf8")) {
                    temp.setUnit("\u7acb\u65b9\u5398\u7c73");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 16.387d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("in^3")) {
                    temp.setUnit("mm^3");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 16387.04d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u7acb\u65b9\u82f1\u5bf8")) {
                    temp.setUnit("\u7acb\u65b9\u6beb\u7c73");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 16387.04d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("gal.")) {
                    temp.setUnit("L");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 4.5461d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u52a0\u4ed1")) {
                    temp.setUnit("\u5347");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 4.5461d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("fl oz")) {
                    temp.setUnit("mL");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 28.3286d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u6db2\u76ce\u53f8")) {
                    temp.setUnit("\u6beb\u5347");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 28.3286d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("mph")) {
                    temp.setUnit("KM/H");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.6093d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u82f1\u91cc\u6bcf\u65f6")) {
                    temp.setUnit("\u516c\u91cc\u6bcf\u65f6");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.6093d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u82f1\u91cc\u6bcf\u65f6")) {
                    temp.setUnit("\u5343\u7c73\u6bcf\u5c0f\u65f6");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.6093d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("mps")) {
                    temp.setUnit("km/s");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.6093d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u82f1\u91cc\u6bcf\u79d2")) {
                    temp.setUnit("\u5343\u7c73/\u79d2");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.6093d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u82f1\u91cc\u6bcf\u79d2")) {
                    temp.setUnit("\u5343\u7c73\u6bcf\u79d2");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.6093d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("ydps")) {
                    temp.setUnit("m/s");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.9144d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u7801\u6bcf\u79d2")) {
                    temp.setUnit("\u7c73/\u79d2");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.9144d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u7801\u6bcf\u79d2")) {
                    temp.setUnit("\u7c73\u6bcf\u79d2");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.9144d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("lb-ft")) {
                    temp.setUnit("Nm");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.3546d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u78c5\u82f1\u5c3a")) {
                    temp.setUnit("\u725b\u7c73");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 1.3546d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("kip")) {
                    temp.setUnit("kg.f");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 453.59d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u5343\u78c5\u529b")) {
                    temp.setUnit("\u5343\u514b\u529b");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 453.59d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equalsIgnoreCase("psi")) {
                    temp.setUnit("KPA");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.0479d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u78c5\u6bcf\u5e73\u65b9\u82f1\u5c3a")) {
                    temp.setUnit("\u5343\u5e15");
                    temp.setDataStreamStr((((double) Math.round((temp.getStreamStrDouble() * 0.0479d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u00b0F")) {
                    temp.setUnit("\u00b0C");
                    temp.setDataStreamStr((((double) Math.round((((temp.getStreamStrDouble() - 32.0d) * 5.0d) / 9.0d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u00b0F")) {
                    temp.setUnit("\u2103");
                    temp.setDataStreamStr((((double) Math.round((((temp.getStreamStrDouble() - 32.0d) * 5.0d) / 9.0d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u00b0F")) {
                    temp.setUnit("\u00baC");
                    temp.setDataStreamStr((((double) Math.round((((temp.getStreamStrDouble() - 32.0d) * 5.0d) / 9.0d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().trim().equalsIgnoreCase("Degree F")) {
                    temp.setUnit("DEGREE C");
                    temp.setDataStreamStr((((double) Math.round((((temp.getStreamStrDouble() - 32.0d) * 5.0d) / 9.0d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u534e\u6c0f\u5ea6")) {
                    temp.setUnit("\u6444\u6c0f\u5ea6");
                    temp.setDataStreamStr((((double) Math.round((((temp.getStreamStrDouble() - 32.0d) * 5.0d) / 9.0d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                } else if (((SptActiveTestStream) dataStreamList.get(i)).getUnit().equals("\u534e\u6c0f\u5ea6")) {
                    temp.setUnit("\u5ea6");
                    temp.setDataStreamStr((((double) Math.round((((temp.getStreamStrDouble() - 32.0d) * 5.0d) / 9.0d) * ((double) KEEPSIZE[pointSize]))) / ((double) KEEPSIZE[pointSize])));
                }
                tempList.add(temp);
            }
            dataStreamList.clear();
            for (i = 0; i < tempList.size(); i++) {
                dataStreamList.add((SptActiveTestStream) tempList.get(i));
            }
        }
    }
}
