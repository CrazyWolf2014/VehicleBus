package com.ifoer.pro.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.ifoer.expedition.crp229.C0501R;
import com.ifoer.expedition.crp229.C0501R.drawable;
import com.ifoer.expedition.crp229.C0501R.string;
import com.ifoer.expeditionphone.inteface.IBaseDiagAdapterInterface;
import com.ifoer.image.AsyncImageView;
import java.util.HashMap;
import java.util.List;
import org.achartengine.renderer.DefaultRenderer;

@SuppressLint({"ResourceAsColor"})
public class BaseDiagAdapter extends com.ifoer.adapter.BaseDiagAdapter implements IBaseDiagAdapterInterface {
    private Bitmap bm;
    private int clicktemp;
    private Context context;
    private LayoutInflater inflater;
    private String language;
    private List<HashMap<String, Object>> listImage;
    private Typeface mTypeface;
    private String preIconString;

    class BaseDiagItem {
        TextView carName;
        TextView carNameText;
        ImageView free;
        AsyncImageView itemImage;
        AsyncImageView sigl;

        BaseDiagItem() {
        }
    }

    public BaseDiagAdapter(Context context, List<HashMap<String, Object>> listImage, String language) {
        super(context, listImage, language);
        this.preIconString = "init";
        this.clicktemp = -1;
        this.context = context;
        this.listImage = listImage;
        this.language = language;
        this.inflater = LayoutInflater.from(this.context);
        this.mTypeface = Typeface.createFromAsset(context.getAssets(), "impact.ttf");
    }

    public int getCount() {
        if (this.listImage.size() > 0) {
            return this.listImage.size();
        }
        return 0;
    }

    public void refresh(List<HashMap<String, Object>> carList) {
        this.listImage = carList;
        this.clicktemp = -1;
        notifyDataSetChanged();
    }

    public Object getItem(int position) {
        return this.listImage.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        BaseDiagItem baseItem;
        if (convertView == null) {
            baseItem = new BaseDiagItem();
            convertView = this.inflater.inflate(C0501R.layout.base_item, null);
            baseItem.itemImage = (AsyncImageView) convertView.findViewById(C0501R.id.ItemImage);
            baseItem.carName = (TextView) convertView.findViewById(C0501R.id.car_name);
            baseItem.carNameText = (TextView) convertView.findViewById(C0501R.id.CarNameText);
            baseItem.sigl = (AsyncImageView) convertView.findViewById(C0501R.id.sigl);
            baseItem.free = (ImageView) convertView.findViewById(C0501R.id.free);
            convertView.setTag(baseItem);
        } else {
            baseItem = (BaseDiagItem) convertView.getTag();
            resetViewCache(baseItem);
        }
        baseItem.itemImage.setVisibility(8);
        int nameValue;
        if (this.language.equals("zh")) {
            if (this.listImage.size() > 0) {
                Class<string> cls = string.class;
                try {
                    nameValue = cls.getDeclaredField(((HashMap) this.listImage.get(position)).get("name_zh").toString()).getInt(null);
                    baseItem.carName.setText(this.context.getResources().getText(nameValue));
                    if (this.context.getResources().getText(nameValue).equals("EOBD")) {
                        baseItem.carNameText.setText(this.context.getResources().getText(nameValue).toString().toUpperCase());
                    } else {
                        baseItem.carNameText.setText(this.context.getResources().getText(cls.getDeclaredField(((HashMap) this.listImage.get(position)).get("name").toString()).getInt(null)).toString().toUpperCase());
                    }
                    baseItem.itemImage.setBackgroundResource(drawable.class.getDeclaredField(((HashMap) this.listImage.get(position)).get("icon").toString()).getInt(null));
                    baseItem.itemImage.setVisibility(0);
                    baseItem.carNameText.setVisibility(8);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (this.listImage.size() > 0) {
            try {
                nameValue = string.class.getDeclaredField(((HashMap) this.listImage.get(position)).get("name").toString()).getInt(null);
                baseItem.carName.setText(this.context.getResources().getText(nameValue).toString().toUpperCase());
                baseItem.carNameText.setText(this.context.getResources().getText(nameValue).toString().toUpperCase());
                baseItem.carNameText.setVisibility(0);
                baseItem.itemImage.setVisibility(8);
                baseItem.carNameText.setText(this.context.getResources().getText(nameValue).toString().toUpperCase());
                baseItem.carName.setVisibility(8);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        if (this.listImage.size() > 0) {
            if (Integer.parseInt(((HashMap) this.listImage.get(position)).get("flag").toString()) > 0) {
                baseItem.sigl.setVisibility(4);
            } else {
                baseItem.sigl.setVisibility(0);
            }
        }
        if (this.clicktemp != position) {
            baseItem.carNameText.setTextColor(DefaultRenderer.BACKGROUND_COLOR);
        } else if (baseItem.carNameText.getVisibility() == 0) {
            baseItem.carNameText.setTextColor(-65536);
        } else {
            baseItem.carNameText.setTextColor(DefaultRenderer.BACKGROUND_COLOR);
        }
        return convertView;
    }

    private void resetViewCache(BaseDiagItem baseItem) {
        baseItem.free.setImageDrawable(null);
    }

    public static float px2pxByHVGA(Context context, float pxValue) {
        return ((pxValue + 0.5f) * context.getResources().getDisplayMetrics().density) + 0.5f;
    }

    public void setSelectItem(int position) {
        this.clicktemp = position;
    }
}
