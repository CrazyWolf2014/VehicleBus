package com.ifoer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.ifoer.entity.Bluetooth;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

public class BluetoothListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Bluetooth> list;

    class Item {
        TextView bluetoothLianjie;
        TextView bluetoothName;
        TextView bluetoothPeiDui;
        TextView bluetoothXuLieHao;
        ImageView image;

        Item() {
        }
    }

    public BluetoothListAdapter(List<Bluetooth> list, Context context) {
        this.list = new ArrayList();
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        if (this.list != null) {
            return this.list.size();
        }
        return 0;
    }

    public Object getItem(int arg0) {
        return this.list.get(arg0);
    }

    public long getItemId(int arg0) {
        return (long) arg0;
    }

    public View getView(int arg0, View convertView, ViewGroup arg2) {
        Item item;
        if (convertView == null) {
            item = new Item();
            convertView = this.inflater.inflate(C0136R.layout.bluetoth_item, null);
            item.bluetoothName = (TextView) convertView.findViewById(C0136R.id.bluetooth_name);
            item.bluetoothLianjie = (TextView) convertView.findViewById(C0136R.id.bluetooth_zhuangtai);
            item.bluetoothXuLieHao = (TextView) convertView.findViewById(C0136R.id.bluetooth_xuliehao);
            item.bluetoothPeiDui = (TextView) convertView.findViewById(C0136R.id.bluetooth_peidui);
            item.image = (ImageView) convertView.findViewById(C0136R.id.item_image);
            convertView.setTag(item);
        } else {
            item = (Item) convertView.getTag();
        }
        if (((Bluetooth) this.list.get(arg0)).getBluetoothpeidui() != null) {
            if (((Bluetooth) this.list.get(arg0)).getBluetoothpeidui().toString().equals(this.context.getResources().getString(C0136R.string.ispair))) {
                item.bluetoothName.setTextColor(this.context.getResources().getColor(C0136R.color.red));
                item.image.setImageDrawable(this.context.getResources().getDrawable(C0136R.drawable.bluetooth_device_enable));
            } else if (((Bluetooth) this.list.get(arg0)).getBluetoothpeidui().toString().equals(this.context.getResources().getString(C0136R.string.notpair))) {
                item.bluetoothName.setTextColor(this.context.getResources().getColor(C0136R.color.hui));
                item.image.setImageDrawable(this.context.getResources().getDrawable(C0136R.drawable.bluetooth_device_default));
            }
        }
        if (!(XmlPullParser.NO_NAMESPACE.equals(((Bluetooth) this.list.get(arg0)).getBluetoothname()) || ((Bluetooth) this.list.get(arg0)).getBluetoothname() == null)) {
            item.bluetoothName.setText(((Bluetooth) this.list.get(arg0)).getBluetoothname().toString());
        }
        if (((Bluetooth) this.list.get(arg0)).getBluetootzhuantai() != null) {
            item.bluetoothLianjie.setText(((Bluetooth) this.list.get(arg0)).getBluetootzhuantai().toString());
        }
        if (((Bluetooth) this.list.get(arg0)).getBluetoothaddress() != null) {
            item.bluetoothXuLieHao.setText(((Bluetooth) this.list.get(arg0)).getBluetoothaddress().toString());
        }
        if (((Bluetooth) this.list.get(arg0)).getBluetoothpeidui() != null) {
            item.bluetoothPeiDui.setText(((Bluetooth) this.list.get(arg0)).getBluetoothpeidui().toString());
        }
        return convertView;
    }
}
