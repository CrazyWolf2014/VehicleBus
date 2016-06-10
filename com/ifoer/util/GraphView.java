package com.ifoer.util;

import android.content.Context;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.cnlaunch.x431frame.C0136R;
import com.cnlaunch.x431pro.common.RequestCode;
import com.google.protobuf.DescriptorProtos.FileOptions;
import com.googlecode.leptonica.android.Skew;
import com.ifoer.entity.Constant;
import com.ifoer.entity.ItemData;
import com.ifoer.entity.SptExDataStreamIdItem;
import com.ifoer.entity.SptVwDataStreamIdItem;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.thoughtworks.xstream.XStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xbill.DNS.Type;
import org.xmlpull.v1.XmlPullParser;

public class GraphView extends RelativeLayout {
    private boolean isVm;
    private LayoutParams lp;
    private GraphicalView mChart;
    private int[] mColors;
    private Context mContext;
    private int mDataStreamCount;
    private XYMultipleSeriesDataset mDataset;
    public int mFrameCount;
    private final Handler mHandler;
    private XYSeries mSeries;
    private String mTitle;
    String[] mTitles;
    private String mUnit;
    String[] mUnits;
    private XYSeries[] mXySeries;
    private int mYFrame;
    private XYMultipleSeriesRenderer renderer;
    public int sFrameCount;
    private int xCount;
    private int xMax;
    private int xMin;
    private int yCount;
    private int yMax;
    private int yMin;

    /* renamed from: com.ifoer.util.GraphView.1 */
    class C07441 extends Handler {
        C07441() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TrafficIncident.ENDTIME_FIELD_NUMBER /*15*/:
                    GraphView.this.updateChart(msg.getData().getDouble("times"), (ArrayList) msg.getData().getSerializable("itemDatas"), msg.getData().getInt("interVal"), msg.getData().getInt("yMin"), msg.getData().getInt("yMax"));
                case FileOptions.CC_GENERIC_SERVICES_FIELD_NUMBER /*16*/:
                    double times1 = msg.getData().getDouble("times");
                    List<ArrayList<ItemData>> listxy = (List) msg.getData().getSerializable("listxy");
                    int interVal1 = msg.getData().getInt("interVal");
                    int yMin1 = msg.getData().getInt("yMin");
                    int yMax1 = msg.getData().getInt("yMax");
                    GraphView.this.updateChartCombine(msg.getData().getInt("listCount"), times1, listxy, interVal1, yMin1, yMax1, 0, msg.getData().getIntArray("interValArr"), msg.getData().getIntArray("yMinArr"), msg.getData().getIntArray("yMaxArr"));
                default:
            }
        }
    }

    private class PushDataThread extends Thread {
        private int currentCheckedItem;
        private List<ArrayList<?>> lists;
        private List<ArrayList<?>> listxy;
        private double times;

        public PushDataThread(List<ArrayList<?>> lists, double times, int currentCheckedItem) {
            this.lists = lists;
            this.times = times;
            this.currentCheckedItem = currentCheckedItem;
        }

        public void run() {
            int i;
            List<Double> temp = new ArrayList();
            if (!(((ArrayList) this.lists.get(0)).get(0) instanceof SptVwDataStreamIdItem)) {
                i = 0;
                while (true) {
                    if (i >= this.lists.size()) {
                        break;
                    }
                    temp.add(Double.valueOf(((SptExDataStreamIdItem) ((ArrayList) this.lists.get(i)).get(this.currentCheckedItem)).getStreamStrDouble()));
                    i++;
                }
            } else {
                i = 0;
                while (true) {
                    if (i >= this.lists.size()) {
                        break;
                    }
                    temp.add(Double.valueOf(((SptVwDataStreamIdItem) ((ArrayList) this.lists.get(i)).get(this.currentCheckedItem)).getStreamStrDouble()));
                    i++;
                }
            }
            double max = ((Double) Collections.max(temp)).doubleValue();
            double min = ((Double) Collections.min(temp)).doubleValue();
            ArrayList<ItemData> itemDatas = new ArrayList();
            int interVal = 1;
            int yMin = 0;
            int yMax = 30;
            double flagMax = 0.0d;
            if (max <= 0.0d || min < 0.0d) {
                double maxNumeber = Math.abs(max) + Math.abs(min);
                if (maxNumeber % 10.0d == 0.0d) {
                    flagMax = maxNumeber - 1.0d;
                }
                if (flagMax % 1000000.0d < 1000000.0d && maxNumeber > 600000.0d) {
                    interVal = maxNumeber <= 1500000.0d ? 250000 : maxNumeber <= 3000000.0d ? 500000 : 1000000;
                } else if (flagMax % 100000.0d < 100000.0d && maxNumeber > 60000.0d) {
                    interVal = maxNumeber <= 150000.0d ? 25000 : maxNumeber <= 300000.0d ? RequestCode.REQ_CHECK_FIRST_RUN_WITH_CACHE : 100000;
                } else if (flagMax % 10000.0d < 10000.0d && maxNumeber > 6000.0d) {
                    interVal = maxNumeber <= 15000.0d ? 2500 : maxNumeber <= 30000.0d ? BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT : XStream.PRIORITY_VERY_HIGH;
                } else if (flagMax % 1000.0d < 1000.0d && maxNumeber > 600.0d) {
                    interVal = maxNumeber <= 1500.0d ? Type.TSIG : maxNumeber <= 3000.0d ? MyHttpException.ERROR_SERVER : 1000;
                } else if (flagMax % 100.0d < 100.0d && maxNumeber > 60.0d) {
                    interVal = maxNumeber <= 150.0d ? 25 : maxNumeber <= 300.0d ? 50 : 100;
                } else if (flagMax % 10.0d < 10.0d && maxNumeber > 6.0d) {
                    interVal = maxNumeber <= 30.0d ? 5 : maxNumeber <= 60.0d ? 10 : 20;
                }
                for (i = 0; i <= 6; i++) {
                    if (Math.abs(min) <= ((double) (interVal * i))) {
                        yMin = -(i * 5);
                        yMax = yMin + 30;
                        break;
                    }
                }
            } else {
                if (max % 10.0d == 0.0d) {
                    flagMax = max - 1.0d;
                }
                if (flagMax % 1000000.0d < 1000000.0d && max > 600000.0d) {
                    interVal = max <= 1500000.0d ? 250000 : max <= 3000000.0d ? 500000 : (max <= 5000000.0d || max >= 1.0E7d) ? max > 1.0E7d ? (int) Math.ceil(max / 5.0d) : 1000000 : 2000000;
                } else if (flagMax % 100000.0d < 100000.0d && max > 60000.0d) {
                    interVal = max <= 150000.0d ? 25000 : max <= 300000.0d ? RequestCode.REQ_CHECK_FIRST_RUN_WITH_CACHE : 100000;
                } else if (flagMax % 10000.0d < 10000.0d && max > 6000.0d) {
                    interVal = max <= 15000.0d ? 2500 : max <= 30000.0d ? BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT : XStream.PRIORITY_VERY_HIGH;
                } else if (flagMax % 1000.0d < 1000.0d && max > 600.0d) {
                    interVal = max <= 1500.0d ? Type.TSIG : max <= 3000.0d ? MyHttpException.ERROR_SERVER : 1000;
                } else if (flagMax % 100.0d < 100.0d && max > 60.0d) {
                    interVal = max <= 150.0d ? 25 : max <= 300.0d ? 50 : 100;
                } else if (flagMax % 10.0d < 10.0d && max > 6.0d) {
                    interVal = max <= 30.0d ? 5 : max <= 60.0d ? 10 : 20;
                }
            }
            double xStart = 0.0d;
            if (this.times >= ((double) GraphView.this.sFrameCount)) {
                xStart = this.times - ((double) GraphView.this.sFrameCount);
            }
            ItemData itemData;
            if (!(((ArrayList) this.lists.get(0)).get(0) instanceof SptExDataStreamIdItem)) {
                i = 0;
                while (true) {
                    if (i >= this.lists.size()) {
                        break;
                    }
                    ArrayList<SptVwDataStreamIdItem> itemList = (ArrayList) this.lists.get(i);
                    itemData = new ItemData();
                    itemData.setX(xStart);
                    itemData.setY(((SptVwDataStreamIdItem) itemList.get(this.currentCheckedItem)).getStreamStrDouble() * ((double) (Skew.SWEEP_RANGE / (6.0f * ((float) interVal)))));
                    itemDatas.add(itemData);
                    xStart += 1.0d;
                    i++;
                }
            } else {
                i = 0;
                while (true) {
                    if (i >= this.lists.size()) {
                        break;
                    }
                    ArrayList<SptExDataStreamIdItem> itemList2 = (ArrayList) this.lists.get(i);
                    itemData = new ItemData();
                    itemData.setX(xStart);
                    itemData.setY(((SptExDataStreamIdItem) itemList2.get(this.currentCheckedItem)).getStreamStrDouble() * ((double) (Skew.SWEEP_RANGE / (6.0f * ((float) interVal)))));
                    itemDatas.add(itemData);
                    xStart += 1.0d;
                    i++;
                }
            }
            Message msg = new Message();
            msg.what = 15;
            Bundle bundle = new Bundle();
            bundle.putDouble("times", this.times);
            bundle.putSerializable("itemDatas", itemDatas);
            bundle.putInt("interVal", interVal);
            bundle.putInt("yMin", yMin);
            bundle.putInt("yMax", yMax);
            msg.setData(bundle);
            GraphView.this.mHandler.sendMessage(msg);
        }
    }

    private class PushDataThreadCombine extends Thread {
        private double flagMax;
        private int interVal;
        private int listCount;
        private List<ArrayList<?>> lists;
        private List<ArrayList<ItemData>> listxy;
        private int[] mInterValArr;
        private List<Integer> mListCheck;
        private int mScale;
        private double times;
        private int yMax;
        int[] yMaxArr;
        private int yMin;
        int[] yMinArr;

        public PushDataThreadCombine(int iCount, List<ArrayList<?>> lists, double times, List<Integer> listCheck) {
            this.listxy = new ArrayList();
            this.interVal = 1;
            this.yMin = 0;
            this.yMax = 30;
            this.flagMax = 0.0d;
            this.mScale = 1;
            this.yMinArr = new int[4];
            this.yMaxArr = new int[]{30, 30, 30, 30};
            this.listCount = iCount;
            this.lists = lists;
            this.times = times;
            this.mListCheck = listCheck;
            this.mInterValArr = new int[this.mListCheck.size()];
        }

        public void run() {
            int m = 0;
            while (true) {
                if (m >= this.mListCheck.size()) {
                    Message msg = new Message();
                    msg.what = 16;
                    Bundle bundle = new Bundle();
                    String str = "times";
                    bundle.putDouble(r21, this.times);
                    bundle.putSerializable("listxy", (Serializable) this.listxy);
                    str = "interVal";
                    bundle.putInt(r21, this.interVal);
                    str = "yMin";
                    bundle.putInt(r21, this.yMin);
                    str = "yMax";
                    bundle.putInt(r21, this.yMax);
                    str = "listCount";
                    bundle.putInt(r21, this.listCount);
                    str = "interValArr";
                    bundle.putIntArray(r21, this.mInterValArr);
                    str = "yMinArr";
                    bundle.putIntArray(r21, this.yMinArr);
                    str = "yMaxArr";
                    bundle.putIntArray(r21, this.yMaxArr);
                    msg.setData(bundle);
                    GraphView.this.mHandler.sendMessage(msg);
                    return;
                }
                int i;
                ArrayList<SptExDataStreamIdItem> itemList;
                ArrayList<ItemData> itemDatas = new ArrayList();
                List<Double> temp = new ArrayList();
                if (!(((ArrayList) this.lists.get(0)).get(0) instanceof SptVwDataStreamIdItem)) {
                    GraphView.this.isVm = false;
                    i = 0;
                    while (true) {
                        if (i >= this.lists.size()) {
                            break;
                        }
                        itemList = (ArrayList) this.lists.get(i);
                        try {
                            temp.add(Double.valueOf(((SptExDataStreamIdItem) itemList.get(((Integer) this.mListCheck.get(m)).intValue())).getStreamStrDouble()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        i++;
                    }
                } else {
                    i = 0;
                    while (true) {
                        if (i >= this.lists.size()) {
                            break;
                        }
                        GraphView.this.isVm = true;
                        List list = this.lists;
                        temp.add(Double.valueOf(((SptVwDataStreamIdItem) ((ArrayList) list.get(i)).get(((Integer) this.mListCheck.get(m)).intValue())).getStreamStrDouble()));
                        i++;
                    }
                }
                double max = ((Double) Collections.max(temp)).doubleValue();
                double min = ((Double) Collections.min(temp)).doubleValue();
                if (max <= 0.0d || min < 0.0d) {
                    double maxNumeber = Math.abs(max) + Math.abs(min);
                    if (maxNumeber % 10.0d == 0.0d) {
                        this.flagMax = maxNumeber - 1.0d;
                    }
                    if (this.flagMax % 1000000.0d >= 1000000.0d || maxNumeber <= 600000.0d) {
                        if (this.flagMax % 100000.0d >= 100000.0d || maxNumeber <= 60000.0d) {
                            if (this.flagMax % 10000.0d >= 10000.0d || maxNumeber <= 6000.0d) {
                                if (this.flagMax % 1000.0d >= 1000.0d || maxNumeber <= 600.0d) {
                                    if (this.flagMax % 100.0d >= 100.0d || maxNumeber <= 60.0d) {
                                        if (this.flagMax % 10.0d < 10.0d && maxNumeber > 6.0d) {
                                            if (maxNumeber <= 30.0d) {
                                                this.interVal = 5;
                                            } else if (maxNumeber <= 60.0d) {
                                                this.interVal = 10;
                                            } else {
                                                this.interVal = 20;
                                            }
                                        }
                                    } else if (maxNumeber <= 150.0d) {
                                        this.interVal = 25;
                                    } else if (maxNumeber <= 300.0d) {
                                        this.interVal = 50;
                                    } else {
                                        this.interVal = 100;
                                    }
                                } else if (maxNumeber <= 1500.0d) {
                                    this.interVal = Type.TSIG;
                                } else if (maxNumeber <= 3000.0d) {
                                    this.interVal = MyHttpException.ERROR_SERVER;
                                } else {
                                    this.interVal = 1000;
                                }
                            } else if (maxNumeber <= 15000.0d) {
                                this.interVal = 2500;
                            } else if (maxNumeber <= 30000.0d) {
                                this.interVal = BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT;
                            } else {
                                this.interVal = XStream.PRIORITY_VERY_HIGH;
                            }
                        } else if (maxNumeber <= 150000.0d) {
                            this.interVal = 25000;
                        } else if (maxNumeber <= 300000.0d) {
                            this.interVal = RequestCode.REQ_CHECK_FIRST_RUN_WITH_CACHE;
                        } else {
                            this.interVal = 100000;
                        }
                    } else if (maxNumeber <= 1500000.0d) {
                        this.interVal = 250000;
                    } else if (maxNumeber <= 3000000.0d) {
                        this.interVal = 500000;
                    } else {
                        this.interVal = 1000000;
                    }
                    for (i = 0; i <= 12; i++) {
                        if (Math.abs(min) <= ((double) (this.interVal * i))) {
                            this.yMin = -(i * 5);
                            this.yMax = this.yMin + 30;
                            break;
                        }
                    }
                    this.yMinArr[m] = this.yMin;
                    this.yMaxArr[m] = (int) (((float) this.yMax) * 1.5f);
                } else {
                    if (max % 10.0d == 0.0d) {
                        this.flagMax = max - 1.0d;
                    }
                    if (this.flagMax % 1000000.0d >= 1000000.0d || max <= 600000.0d) {
                        if (this.flagMax % 100000.0d >= 100000.0d || max <= 60000.0d) {
                            if (this.flagMax % 10000.0d >= 10000.0d || max <= 6000.0d) {
                                if (this.flagMax % 1000.0d >= 1000.0d || max <= 600.0d) {
                                    if (this.flagMax % 100.0d >= 100.0d || max <= 60.0d) {
                                        if (this.flagMax % 10.0d < 10.0d && max > 6.0d) {
                                            if (max <= 30.0d) {
                                                this.interVal = 5;
                                            } else if (max <= 60.0d) {
                                                this.interVal = 10;
                                            } else {
                                                this.interVal = 20;
                                            }
                                        }
                                    } else if (max <= 150.0d) {
                                        this.interVal = 25;
                                    } else if (max <= 300.0d) {
                                        this.interVal = 50;
                                    } else {
                                        this.interVal = 100;
                                    }
                                } else if (max <= 1500.0d) {
                                    this.interVal = Type.TSIG;
                                } else if (max <= 3000.0d) {
                                    this.interVal = MyHttpException.ERROR_SERVER;
                                } else {
                                    this.interVal = 1000;
                                }
                            } else if (max <= 15000.0d) {
                                this.interVal = 2500;
                            } else if (max <= 30000.0d) {
                                this.interVal = BaseImageDownloader.DEFAULT_HTTP_CONNECT_TIMEOUT;
                            } else {
                                this.interVal = XStream.PRIORITY_VERY_HIGH;
                            }
                        } else if (max <= 150000.0d) {
                            this.interVal = 25000;
                        } else if (max <= 300000.0d) {
                            this.interVal = RequestCode.REQ_CHECK_FIRST_RUN_WITH_CACHE;
                        } else {
                            this.interVal = 100000;
                        }
                    } else if (max <= 1500000.0d) {
                        this.interVal = 250000;
                    } else if (max <= 3000000.0d) {
                        this.interVal = 500000;
                    } else if (max > 5000000.0d && max < 1.0E7d) {
                        this.interVal = 2000000;
                    } else if (max > 1.0E7d) {
                        this.interVal = (int) Math.ceil(max / 5.0d);
                    } else {
                        this.interVal = 1000000;
                    }
                }
                GraphView.this.isVm = true;
                if (GraphView.this.isVm) {
                    this.mInterValArr[m] = this.interVal * this.mScale;
                } else {
                    this.mInterValArr[m] = (int) (((double) (this.interVal * this.mScale)) / 1.5d);
                }
                double xStart = 0.0d;
                if (this.times >= ((double) GraphView.this.mFrameCount)) {
                    xStart = this.times - ((double) GraphView.this.mFrameCount);
                }
                ItemData itemData;
                int intValue;
                if (!(((ArrayList) this.lists.get(0)).get(0) instanceof SptExDataStreamIdItem)) {
                    i = 0;
                    while (true) {
                        if (i >= this.lists.size()) {
                            break;
                        }
                        ArrayList<SptVwDataStreamIdItem> itemList2 = (ArrayList) this.lists.get(i);
                        itemData = new ItemData();
                        itemData.setX(xStart);
                        intValue = ((Integer) this.mListCheck.get(m)).intValue();
                        itemData.setY(((SptVwDataStreamIdItem) itemList2.get(r21)).getStreamStrDouble() * ((double) (((float) GraphView.this.mYFrame) / (6.0f * ((float) this.interVal)))));
                        itemData.setValue(((SptVwDataStreamIdItem) itemList2.get(((Integer) this.mListCheck.get(m)).intValue())).getStreamStrDouble());
                        itemDatas.add(itemData);
                        xStart += 1.0d;
                        i++;
                    }
                } else {
                    i = 0;
                    while (true) {
                        if (i >= this.lists.size()) {
                            break;
                        }
                        itemList = (ArrayList) this.lists.get(i);
                        itemData = new ItemData();
                        itemData.setX(xStart);
                        try {
                            intValue = ((Integer) this.mListCheck.get(m)).intValue();
                            itemData.setY(((SptExDataStreamIdItem) itemList.get(r21)).getStreamStrDouble() * ((double) (((float) GraphView.this.mYFrame) / (6.0f * ((float) this.interVal)))));
                            itemData.setValue(((SptExDataStreamIdItem) itemList.get(((Integer) this.mListCheck.get(m)).intValue())).getStreamStrDouble());
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                        itemDatas.add(itemData);
                        xStart += 1.0d;
                        i++;
                    }
                }
                this.listxy.add(itemDatas);
                m++;
            }
        }
    }

    public GraphView(Context context, View otherView, int width, int height, String title, String unit) {
        super(context);
        this.mTitle = XmlPullParser.NO_NAMESPACE;
        this.mUnit = XmlPullParser.NO_NAMESPACE;
        this.xCount = 0;
        this.yCount = 0;
        this.xMin = 0;
        this.xMax = Constant.GRAPHIC_X;
        this.yMin = 0;
        this.yMax = 30;
        this.mDataStreamCount = 0;
        this.sFrameCount = Constant.GRAPHIC_X;
        this.mFrameCount = Constant.GRAPHIC_X;
        this.mYFrame = 30;
        this.isVm = false;
        this.mColors = new int[]{-8388864, -2456308, -15636980, -16424587};
        this.mHandler = new C07441();
        this.mContext = context;
        this.mTitle = title;
        this.mUnit = unit;
        this.lp = new LayoutParams(-1, -1);
        setLayoutParams(this.lp);
        setBackgroundResource(C0136R.drawable.transparent);
        this.mSeries = new XYSeries(title);
        this.mDataset = new XYMultipleSeriesDataset();
        this.mDataset.addSeries(this.mSeries);
        this.renderer = buildRenderer(-16711936, PointStyle.POINT, true);
        if (this.mChart == null) {
            this.mChart = ChartFactory.getLineChartView(context, this.mDataset, this.renderer);
            ViewGroup.LayoutParams layoutParams = new AbsListView.LayoutParams(-1, -1);
            this.mChart.setLayoutParams(layoutParams);
            addView(this.mChart, layoutParams);
        } else {
            this.mChart.repaint();
        }
        setChartSettings2(this.renderer, this.mContext.getResources().getString(C0136R.string.communicationsNumber), unit, (double) this.xMin, (double) this.xMax, (double) this.yMin, (double) this.yMax, -1, -1, 0, 5);
    }

    public void updateChart(double times, ArrayList<ItemData> itemDatas, int yInterval, int yMin, int yMax) {
        this.mDataset.removeSeries(this.mSeries);
        this.mSeries.clear();
        for (int k = 0; k < itemDatas.size(); k++) {
            this.mSeries.add(((ItemData) itemDatas.get(k)).getX(), ((ItemData) itemDatas.get(k)).getY());
        }
        double xstart = 0.0d;
        double xend = (double) this.sFrameCount;
        if (times >= ((double) this.sFrameCount)) {
            xstart = times - ((double) this.sFrameCount);
            xend = times;
        }
        setChartSettings2(this.renderer, this.mContext.getResources().getString(C0136R.string.communicationsNumber), this.mUnit, xstart, xend, (double) yMin, (double) yMax, -1, -1, 0, yInterval);
        this.mDataset.addSeries(this.mSeries);
        this.mChart.invalidate();
    }

    protected XYMultipleSeriesRenderer buildRenderer(int color, PointStyle style, boolean fill) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        XYSeriesRenderer r = new XYSeriesRenderer();
        r.setColor(color);
        r.setPointStyle(style);
        r.setFillPoints(fill);
        r.setLineWidth(3.0f);
        renderer.addSeriesRenderer(r);
        return renderer;
    }

    public GraphView(Context context, View otherView, int width, int height, String[] titles, String[] units, ArrayList<Integer> arrayList) {
        super(context);
        this.mTitle = XmlPullParser.NO_NAMESPACE;
        this.mUnit = XmlPullParser.NO_NAMESPACE;
        this.xCount = 0;
        this.yCount = 0;
        this.xMin = 0;
        this.xMax = Constant.GRAPHIC_X;
        this.yMin = 0;
        this.yMax = 30;
        this.mDataStreamCount = 0;
        this.sFrameCount = Constant.GRAPHIC_X;
        this.mFrameCount = Constant.GRAPHIC_X;
        this.mYFrame = 30;
        this.isVm = false;
        this.mColors = new int[]{-8388864, -2456308, -15636980, -16424587};
        this.mHandler = new C07441();
        this.mContext = context;
        this.mTitles = titles;
        this.mUnits = units;
        this.lp = new LayoutParams(-2, -2);
        setLayoutParams(this.lp);
        this.mDataset = new XYMultipleSeriesDataset();
        this.mDataStreamCount = titles.length;
        this.mXySeries = new XYSeries[this.mDataStreamCount];
        int[] colors = new int[this.mDataStreamCount];
        int[] interValArr = new int[this.mDataStreamCount];
        int[] yMinArr = new int[this.mDataStreamCount];
        int[] yMaxArr = new int[this.mDataStreamCount];
        for (int i = 0; i < this.mDataStreamCount; i++) {
            colors[i] = this.mColors[i];
            interValArr[i] = 0;
            yMinArr[i] = 0;
            yMaxArr[i] = 30;
            this.mSeries = new XYSeries(new StringBuilder(String.valueOf(titles[i].replaceAll(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, XmlPullParser.NO_NAMESPACE))).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(units[i]).toString(), i);
            this.mXySeries[i] = this.mSeries;
            this.mDataset.addSeries(this.mXySeries[i]);
        }
        this.renderer = buildRenderer(colors, new PointStyle[]{PointStyle.CIRCLE, PointStyle.DIAMOND, PointStyle.SQUARE, PointStyle.TRIANGLE}, true);
        if (this.mChart == null) {
            this.mChart = ChartFactory.getCubeLineChartView(context, this.mDataset, this.renderer, 0.2f);
            ViewGroup.LayoutParams layoutParams = new AbsListView.LayoutParams(-2, -2);
            this.mChart.setLayoutParams(layoutParams);
            addView(this.mChart, layoutParams);
        } else {
            this.mChart.repaint();
        }
        setChartSettings(this.renderer, units, (double) this.xMin, (double) this.xMax, (double) this.yMin, (double) this.yMax, colors, colors, 0, 5, interValArr, yMinArr, yMaxArr);
    }

    public void updateChartCombine(int listCount, double times, List<ArrayList<ItemData>> listitemDatas, int yInterval, int yMin, int yMax, int index, int[] interValArr, int[] yMinArr, int[] yMaxArr) {
        int[] colors = new int[this.mDataStreamCount];
        for (int i = 0; i < this.mDataStreamCount; i++) {
            colors[i] = this.mColors[i];
            this.mDataset.removeSeries(this.mXySeries[i]);
            ArrayList<ItemData> itemDatas = (ArrayList) listitemDatas.get(i);
            this.mXySeries[i].clear();
            XYSeries xYSeries = new XYSeries(new StringBuilder(String.valueOf(this.mTitles[i].replaceAll(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, XmlPullParser.NO_NAMESPACE))).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(((ItemData) itemDatas.get(itemDatas.size() - 1)).getValue()).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(this.mUnits[i]).toString(), i);
            for (int k = 0; k < itemDatas.size(); k++) {
                xYSeries.add(((ItemData) itemDatas.get(k)).getX(), ((ItemData) itemDatas.get(k)).getY());
            }
            this.mXySeries[i] = xYSeries;
            double xstart = 0.0d;
            double xend = (double) this.mFrameCount;
            if (times >= ((double) this.mFrameCount)) {
                xstart = times - ((double) this.mFrameCount);
                xend = times;
            }
            setChartSettings(this.renderer, this.mUnits, xstart, xend, (double) yMin, (double) yMax, colors, colors, 0, yInterval, interValArr, yMinArr, yMaxArr);
            this.mDataset.addSeries(this.mXySeries[i]);
            this.mChart.invalidate();
        }
    }

    protected XYMultipleSeriesRenderer buildRenderer(int[] colors, PointStyle[] styles, boolean fill) {
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer(this.mDataStreamCount);
        for (int color : colors) {
            XYSeriesRenderer r = new XYSeriesRenderer();
            r.setColor(color);
            r.setLineWidth(3.2f);
            renderer.addSeriesRenderer(r);
        }
        return renderer;
    }

    protected void setChartSettings2(XYMultipleSeriesRenderer renderer, String xTitle, String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor, int labelsColor, int yMinNo, int interval) {
        int i;
        renderer.setGraphType(0);
        renderer.setLabelsTextSize(15.0f);
        if (yMax > 1000.0d) {
            renderer.setAxisTitleTextSize(12.0f);
        } else {
            renderer.setAxisTitleTextSize(16.0f);
        }
        renderer.setChartTitleTextSize(16.0f);
        int[] iArr = new int[4];
        iArr[1] = 70;
        iArr[2] = 5;
        iArr[3] = 10;
        renderer.setMargins(iArr);
        renderer.setXLabelsAngle(0.0f);
        renderer.setYLabelsAngle(0.0f);
        renderer.setYTitle(XmlPullParser.NO_NAMESPACE);
        renderer.setXAxisMin(xMin);
        renderer.setXAxisMax(xMax);
        renderer.setYAxisMin(yMin);
        renderer.setYAxisMax(yMax);
        renderer.setAxesColor(axesColor);
        renderer.setLabelsColor(labelsColor);
        renderer.setShowGridX(false);
        renderer.setShowGridY(false);
        renderer.setPanEnabled(false, false);
        renderer.setYLabelsAlign(Align.RIGHT);
        renderer.setPointSize(2.0f);
        renderer.setShowLegend(false);
        renderer.setXLabels(0);
        renderer.setYLabels(0);
        this.xCount = (int) xMin;
        int d = this.sFrameCount / 5;
        for (i = 0; i <= this.sFrameCount; i++) {
            if (this.xCount % d == 0) {
                renderer.addXTextLabel((double) this.xCount, new StringBuilder(String.valueOf(this.xCount)).toString());
            } else {
                renderer.addXTextLabel((double) this.xCount, XmlPullParser.NO_NAMESPACE);
            }
            this.xCount++;
        }
        this.yCount = (int) yMin;
        for (i = 0; i <= 30; i++) {
            if (i % 5 == 0) {
                renderer.addYTextLabel((double) this.yCount, new StringBuilder(String.valueOf(((int) (((float) this.yCount) / Skew.SWEEP_DELTA)) * interval)).append("  ").toString());
            }
            this.yCount++;
        }
    }

    protected void setChartSettings(XYMultipleSeriesRenderer renderer, String[] yTitle, double xMin, double xMax, double yMin, double yMax, int[] axesColor, int[] labelsColor, int yMinNo, int interval, int[] interValArr, int[] yMinArr, int[] yMaxArr) {
        renderer.setGraphType(1);
        renderer.setLabelsTextSize(12.0f);
        renderer.setAxisTitleTextSize(15.0f);
        renderer.setChartTitleTextSize(Skew.SWEEP_DELTA);
        renderer.setLegendTextSize(14.0f);
        int[] iArr = new int[4];
        iArr[0] = 20;
        iArr[1] = 70;
        iArr[3] = 70;
        renderer.setMargins(iArr);
        renderer.setLegendHeight(90);
        renderer.setShowGridX(false);
        renderer.setShowGridY(false);
        renderer.setXLabelsAngle(Skew.SWEEP_RANGE);
        renderer.setYLabelsAngle(0.0f);
        renderer.setChartTitle(this.mTitle);
        renderer.setAxesColor(-1);
        renderer.setLabelsColor(-1);
        int i = 0;
        while (i < this.mDataStreamCount) {
            renderer.setXAxisMin(xMin, i);
            renderer.setXAxisMax(xMax, i);
            renderer.setYAxisMin((double) yMinArr[i], i);
            renderer.setYAxisMax((double) yMaxArr[i], i);
            renderer.setYLabelsColor(i, labelsColor[i]);
            if (i % 2 == 0) {
                renderer.setYLabelsAlign(Align.LEFT, i);
            } else {
                renderer.setYLabelsAlign(Align.RIGHT, i);
            }
            if (i == 1 || i == 2) {
                renderer.setYAxisAlign(Align.RIGHT, i);
            } else {
                renderer.setYAxisAlign(Align.LEFT, i);
            }
            i++;
        }
        renderer.setXLabels(30);
        renderer.setPanEnabled(false, false);
        renderer.setPointSize(0.0f);
        renderer.setZoomEnabled(false);
        renderer.setShowLegend(true);
        renderer.setXLabels(0);
        renderer.setYLabels(0);
        int d = this.mFrameCount / 5;
        this.xCount = (int) xMin;
        for (i = 0; i <= this.mFrameCount; i++) {
            if (this.xCount % d == 0) {
                renderer.addXTextLabel((double) this.xCount, new StringBuilder(String.valueOf(this.xCount)).toString());
            } else {
                renderer.addXTextLabel((double) this.xCount, XmlPullParser.NO_NAMESPACE);
            }
            this.xCount++;
        }
        this.yCount = (int) yMin;
        for (i = 0; i <= 50; i++) {
            int j;
            if (i % 5 == 0) {
                for (j = 0; j < this.mDataStreamCount; j++) {
                    if (this.isVm) {
                        renderer.addYTextLabel((double) this.yCount, new StringBuilder(String.valueOf((((float) this.yCount) / Skew.SWEEP_DELTA) * ((float) interValArr[j]))).toString(), j);
                    } else {
                        renderer.addYTextLabel((double) this.yCount, new StringBuilder(String.valueOf(((double) ((((float) this.yCount) / Skew.SWEEP_DELTA) * ((float) interValArr[j]))) * 1.5d)).toString(), j);
                    }
                }
            } else {
                for (j = 0; j < this.mDataStreamCount; j++) {
                    renderer.addYTextLabel((double) this.yCount, XmlPullParser.NO_NAMESPACE, j);
                }
            }
            this.yCount++;
        }
    }

    public void pushDataToChart(List<ArrayList<?>> lists, double times, int currentCheckedItem) {
        if (lists != null && lists.size() > 0) {
            new PushDataThread(lists, times, currentCheckedItem).start();
        }
    }

    public void pushDataToChartCombine(int size, List<ArrayList<?>> lists, double times, List<Integer> listCheck) {
        if (lists != null && lists.size() > 0) {
            PushDataThreadCombine pushDataThreadCombine = new PushDataThreadCombine(size, lists, times, listCheck);
            try {
                PushDataThreadCombine.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            pushDataThreadCombine.start();
        }
    }

    public static int dip2px(Context context, float dipValue) {
        return (int) ((dipValue * context.getResources().getDisplayMetrics().density) + 0.5f);
    }
}
