package com.ifoer.expeditionphone;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.cnlaunch.x431frame.C0136R;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import org.xbill.DNS.KEYRecord;

public class MySpaceManagermentLayout extends RelativeLayout {
    private RelativeLayout My_order;
    private TextView My_order_Text;
    private RelativeLayout baogao;
    private TextView bgtext;
    private Context context;
    private RelativeLayout jilu;
    private TextView operatetext;
    private TextView pay_head_title_Text;
    private RelativeLayout shoppingcar;
    private ImageView view0;
    private ImageView view1;
    private ImageView view2;
    private ImageView view4;

    /* renamed from: com.ifoer.expeditionphone.MySpaceManagermentLayout.1 */
    class C06121 implements OnClickListener {
        C06121() {
        }

        public void onClick(View v) {
            MainActivity.panel.removePanelContainer();
            MainActivity.panel.fillPanelContainer(new SpaceDiagnosticReportLayout(MySpaceManagermentLayout.this.context));
            MainActivity.panel.openthreePanelContainer();
        }
    }

    /* renamed from: com.ifoer.expeditionphone.MySpaceManagermentLayout.2 */
    class C06132 implements OnClickListener {
        C06132() {
        }

        public void onClick(View v) {
            MainActivity.panel.removePanelContainer();
            MainActivity.panel.fillPanelContainer(new SpaceOperationRecordsLayout(MySpaceManagermentLayout.this.context));
            MainActivity.panel.openthreePanelContainer();
        }
    }

    /* renamed from: com.ifoer.expeditionphone.MySpaceManagermentLayout.3 */
    class C06143 implements OnClickListener {
        C06143() {
        }

        public void onClick(View v) {
            MainActivity.panel.removePanelContainer();
            MainActivity.panel.fillPanelContainer(new SpaceShoppingCarActivity(MySpaceManagermentLayout.this.context));
            MainActivity.panel.openthreePanelContainer();
        }
    }

    public MySpaceManagermentLayout(Context context) {
        super(context);
        this.context = context;
    }

    public void initTopView(View baseView) {
        this.baogao = (RelativeLayout) baseView.findViewById(C0136R.id.bg);
        this.baogao.setOnClickListener(new C06121());
        this.jilu = (RelativeLayout) baseView.findViewById(C0136R.id.operate);
        this.jilu.setOnClickListener(new C06132());
        this.My_order = (RelativeLayout) baseView.findViewById(C0136R.id.My_order);
        this.shoppingcar = (RelativeLayout) baseView.findViewById(C0136R.id.pay_head_title);
        this.shoppingcar.setOnClickListener(new C06143());
        this.bgtext = (TextView) baseView.findViewById(C0136R.id.bgtext);
        this.operatetext = (TextView) baseView.findViewById(C0136R.id.operatetext);
        this.My_order_Text = (TextView) baseView.findViewById(C0136R.id.My_order_Text);
        this.pay_head_title_Text = (TextView) baseView.findViewById(C0136R.id.pay_head_title_Text);
        this.view0 = (ImageView) baseView.findViewById(C0136R.id.view0);
        this.view1 = (ImageView) baseView.findViewById(C0136R.id.view1);
        this.view2 = (ImageView) baseView.findViewById(C0136R.id.view2);
        this.view4 = (ImageView) baseView.findViewById(C0136R.id.view4);
    }

    public void setTopView(Context context, int i) {
        if (i == 0) {
            this.baogao.setClickable(false);
        } else if (i == 1) {
            this.jilu.setClickable(false);
        } else if (i == 2) {
            this.My_order.setClickable(false);
        } else if (i == 3) {
            this.shoppingcar.setClickable(false);
        }
        switch (i) {
            case KEYRecord.OWNER_USER /*0*/:
                this.baogao.setBackgroundResource(C0136R.drawable.black_bg);
                this.bgtext.setTextColor(-256);
                this.operatetext.setTextColor(-1);
                this.My_order_Text.setTextColor(-1);
                this.pay_head_title_Text.setTextColor(-1);
                this.view0.setBackgroundResource(C0136R.drawable.yello_up);
                this.view1.setBackgroundResource(C0136R.drawable.yello_line);
                this.view2.setBackgroundResource(C0136R.drawable.yello_line);
                this.view4.setBackgroundResource(C0136R.drawable.yello_line);
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                this.jilu.setBackgroundResource(C0136R.drawable.black_bg);
                this.bgtext.setTextColor(-1);
                this.operatetext.setTextColor(-256);
                this.My_order_Text.setTextColor(-1);
                this.pay_head_title_Text.setTextColor(-1);
                this.view0.setBackgroundResource(C0136R.drawable.yello_line);
                this.view1.setBackgroundResource(C0136R.drawable.yello_up);
                this.view2.setBackgroundResource(C0136R.drawable.yello_line);
                this.view4.setBackgroundResource(C0136R.drawable.yello_line);
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                this.My_order.setBackgroundResource(C0136R.drawable.black_bg);
                this.bgtext.setTextColor(-1);
                this.operatetext.setTextColor(-1);
                this.My_order_Text.setTextColor(-256);
                this.pay_head_title_Text.setTextColor(-1);
                this.view0.setBackgroundResource(C0136R.drawable.yello_line);
                this.view1.setBackgroundResource(C0136R.drawable.yello_line);
                this.view2.setBackgroundResource(C0136R.drawable.yello_up);
                this.view4.setBackgroundResource(C0136R.drawable.yello_line);
            case FieldOptions.DEPRECATED_FIELD_NUMBER /*3*/:
                this.shoppingcar.setBackgroundResource(C0136R.drawable.black_bg);
                this.bgtext.setTextColor(-1);
                this.operatetext.setTextColor(-1);
                this.My_order_Text.setTextColor(-1);
                this.pay_head_title_Text.setTextColor(-256);
                this.view0.setBackgroundResource(C0136R.drawable.yello_line);
                this.view1.setBackgroundResource(C0136R.drawable.yello_line);
                this.view2.setBackgroundResource(C0136R.drawable.yello_line);
                this.view4.setBackgroundResource(C0136R.drawable.yello_up);
            default:
        }
    }
}
