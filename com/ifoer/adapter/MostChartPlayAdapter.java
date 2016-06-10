package com.ifoer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.cnlaunch.mycar.jni.JniX431File;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.entity.IntData;
import com.ifoer.entity.SptExDataStreamIdItem;
import com.ifoer.entity.SptVwDataStreamIdItem;
import com.ifoer.image.MostChartPlayGridView;
import com.ifoer.util.GraphView;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

public class MostChartPlayAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<IntData> listStr;
    private List<ArrayList<?>> lists;
    private GridView mGrid;
    private WindowManager manager;
    private int screenHeight;
    private int screenWidth;
    private double times;
    String title;
    String unit;

    class MostChartPlayItem {
        public LinearLayout graphLayout;
        public TextView graphText;
        public GraphView graphView;
        public TextView unit_txt;

        MostChartPlayItem() {
            this.graphLayout = null;
            this.graphView = null;
            this.graphText = null;
        }
    }

    public MostChartPlayAdapter(Context context, List<ArrayList<?>> lists, double times, ArrayList<IntData> listStr, WindowManager manager) {
        this.listStr = null;
        this.lists = new ArrayList();
        this.times = 0.0d;
        this.title = XmlPullParser.NO_NAMESPACE;
        this.unit = XmlPullParser.NO_NAMESPACE;
        this.context = context;
        this.listStr = listStr;
        this.lists = lists;
        this.times = times;
        this.inflater = LayoutInflater.from(this.context);
        this.manager = manager;
        this.screenHeight = manager.getDefaultDisplay().getHeight();
        this.screenWidth = manager.getDefaultDisplay().getWidth();
    }

    public int getCount() {
        if (this.listStr.size() > 0) {
            return this.listStr.size();
        }
        return 0;
    }

    public void refresh(List<ArrayList<?>> lists, double times) {
        this.lists = lists;
        this.times = times;
        notifyDataSetChanged();
    }

    public Object getItem(int position) {
        return this.listStr.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        MostChartPlayItem mostChartPlayItem;
        if (convertView == null) {
            mostChartPlayItem = new MostChartPlayItem();
            convertView = this.inflater.inflate(C0136R.layout.most_chart_play_item, null);
            this.mGrid = (GridView) ((GridView) parent).findViewById(C0136R.id.gridView);
            convertView.setLayoutParams(new LayoutParams(-1, -1));
            mostChartPlayItem.graphLayout = (LinearLayout) convertView.findViewById(C0136R.id.graphLayout);
            mostChartPlayItem.graphText = (TextView) convertView.findViewById(C0136R.id.title);
            mostChartPlayItem.unit_txt = (TextView) convertView.findViewById(C0136R.id.unit_txt);
            mostChartPlayItem.graphText.setTextSize(8.0f);
            mostChartPlayItem.graphText.setOverScrollMode(2);
            if (this.lists != null && this.lists.size() > 0) {
                if (((ArrayList) this.lists.get(0)).get(0) instanceof SptVwDataStreamIdItem) {
                    ArrayList<SptVwDataStreamIdItem> itemList = (ArrayList) this.lists.get(0);
                    this.title = ((SptVwDataStreamIdItem) itemList.get(((IntData) this.listStr.get(position)).getItem())).getStreamTextIdContent();
                    this.unit = ((SptVwDataStreamIdItem) itemList.get(((IntData) this.listStr.get(position)).getItem())).getStreamUnitIdContent();
                } else {
                    ArrayList<SptExDataStreamIdItem> itemList2 = (ArrayList) this.lists.get(0);
                    this.title = ((SptExDataStreamIdItem) itemList2.get(((IntData) this.listStr.get(position)).getItem())).getStreamTextIdContent();
                    this.unit = ((SptExDataStreamIdItem) itemList2.get(((IntData) this.listStr.get(position)).getItem())).getStreamState();
                }
            }
            mostChartPlayItem.unit_txt.setText(this.unit);
            MostChartPlayGridView mostChartPlayGridView = (MostChartPlayGridView) convertView.findViewById(C0136R.id.most_chart_play_grid_view);
            int showGraphNum = this.listStr.size();
            if (showGraphNum <= 2) {
                setMostCharValue(position, mostChartPlayItem, mostChartPlayGridView, 1);
            } else if (showGraphNum > 2 && showGraphNum <= 10) {
                setMostCharValue(position, mostChartPlayItem, mostChartPlayGridView, 2);
            }
            mostChartPlayItem.graphText.setText(this.title);
            mostChartPlayItem.graphLayout.addView(mostChartPlayItem.graphView, new ViewGroup.LayoutParams(-1, -1));
            mostChartPlayItem.graphLayout.setFocusable(false);
            mostChartPlayItem.graphLayout.setFocusableInTouchMode(false);
            convertView.setTag(mostChartPlayItem);
        } else {
            mostChartPlayItem = (MostChartPlayItem) convertView.getTag();
        }
        mostChartPlayItem.graphView.pushDataToChart(this.lists, this.times, ((IntData) this.listStr.get(position)).getItem());
        if (this.lists != null && this.lists.size() > 0) {
            if (((ArrayList) this.lists.get(0)).get(0) instanceof SptVwDataStreamIdItem) {
                itemList = (ArrayList) this.lists.get(0);
                if (mostChartPlayItem.graphText.getText().toString().length() == 0) {
                    mostChartPlayItem.graphText.setText(((SptVwDataStreamIdItem) itemList.get(((IntData) this.listStr.get(position)).getItem())).getStreamTextIdContent());
                }
                if (mostChartPlayItem.graphText.getText().toString().length() == 0) {
                    mostChartPlayItem.graphText.setText(((SptVwDataStreamIdItem) itemList.get(((IntData) this.listStr.get(position)).getItem())).getStreamTextIdContent());
                }
            } else {
                itemList2 = (ArrayList) this.lists.get(0);
                if (mostChartPlayItem.graphText.getText().toString().length() == 0) {
                    mostChartPlayItem.graphText.setText(((SptExDataStreamIdItem) itemList2.get(((IntData) this.listStr.get(position)).getItem())).getStreamTextIdContent());
                }
                if (mostChartPlayItem.graphText.getText().toString().length() == 0) {
                    mostChartPlayItem.graphText.setText(((SptExDataStreamIdItem) itemList2.get(((IntData) this.listStr.get(position)).getItem())).getStreamTextIdContent());
                }
            }
        }
        return convertView;
    }

    private void setMostCharValue(int position, MostChartPlayItem mostChartPlayItem, MostChartPlayGridView mostChartPlayGridView, int rows) {
        mostChartPlayGridView.SetRowNum(rows);
        mostChartPlayItem.graphView = new GraphView(this.context, mostChartPlayItem.graphLayout, JniX431File.MAX_DS_COLNUMBER, JniX431File.MAX_DS_COLNUMBER, this.title, this.unit);
    }
}
