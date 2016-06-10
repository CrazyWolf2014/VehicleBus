package com.ifoer.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import com.cnlaunch.x431frame.C0136R;
import com.googlecode.leptonica.android.Skew;

public class Panel extends LinearLayout {
    private static int HANDLE_WIDTH;
    private static int MOVE_WIDTH;
    public static int opendFlag;
    private LinearLayout downLayout;
    private ImageView handleBackground;
    private int heights;
    private LinearLayout leftLayout;
    private LinearLayout line;
    private LayoutParams lp;
    private Context mContext;
    private int mbottomMargin;
    private PanelClosedEvent panelClosedEvent;
    private LinearLayout panelContainer;
    private PanelOpenedEvent panelOpenedEvent;
    private LinearLayout rightLayout;
    private RelativeLayout settingLayout;
    private LinearLayout upLayout;

    /* renamed from: com.ifoer.view.Panel.1 */
    class C07711 implements OnClickListener {
        C07711() {
        }

        public void onClick(View arg0) {
            if (Panel.opendFlag == 1) {
                Panel.opendFlag = 2;
                Panel.this.lp.height = Panel.this.heights;
                Panel.this.lp.bottomMargin = 0;
                Panel.this.mbottomMargin = Math.abs(Panel.this.lp.bottomMargin);
                Panel.this.setLayoutParams(Panel.this.lp);
                Panel.this.setOrientation(1);
                Panel.this.upLayout.setVisibility(8);
                Panel.this.downLayout.setVisibility(0);
                Panel.this.line.setVisibility(8);
            } else if (Panel.opendFlag == 2) {
                Panel.opendFlag = 3;
                Panel.this.lp.height = Panel.this.heights;
                Panel.this.lp.bottomMargin = (-Panel.this.heights) + Panel.HANDLE_WIDTH;
                Panel.this.mbottomMargin = Math.abs(Panel.this.lp.bottomMargin);
                Panel.this.setLayoutParams(Panel.this.lp);
                Panel.this.setOrientation(1);
                Panel.this.upLayout.setVisibility(0);
                Panel.this.downLayout.setVisibility(8);
                Panel.this.line.setVisibility(8);
            } else if (Panel.opendFlag == 3) {
                Panel.opendFlag = 1;
                Panel.this.lp.height = Panel.this.heights / 2;
                Panel.this.lp.bottomMargin = 0;
                Panel.this.mbottomMargin = Math.abs(Panel.this.lp.bottomMargin);
                Panel.this.setLayoutParams(Panel.this.lp);
                Panel.this.setOrientation(1);
                Panel.this.upLayout.setVisibility(0);
                Panel.this.downLayout.setVisibility(0);
                Panel.this.line.setVisibility(0);
            }
        }
    }

    /* renamed from: com.ifoer.view.Panel.2 */
    class C07722 implements OnClickListener {
        C07722() {
        }

        public void onClick(View arg0) {
            if (Panel.opendFlag == 1) {
                Panel.opendFlag = 3;
                Panel.this.lp.height = Panel.this.heights;
                Panel.this.lp.bottomMargin = (-Panel.this.heights) + Panel.HANDLE_WIDTH;
                Panel.this.mbottomMargin = Math.abs(Panel.this.lp.bottomMargin);
                Panel.this.setLayoutParams(Panel.this.lp);
                Panel.this.setOrientation(1);
                Panel.this.upLayout.setVisibility(0);
                Panel.this.downLayout.setVisibility(8);
                Panel.this.line.setVisibility(8);
            } else if (Panel.opendFlag == 2) {
                Panel.opendFlag = 3;
                Panel.this.lp.height = Panel.this.heights;
                Panel.this.lp.bottomMargin = (-Panel.this.heights) + Panel.HANDLE_WIDTH;
                Panel.this.mbottomMargin = Math.abs(Panel.this.lp.bottomMargin);
                Panel.this.setLayoutParams(Panel.this.lp);
                Panel.this.setOrientation(1);
                Panel.this.upLayout.setVisibility(0);
                Panel.this.downLayout.setVisibility(8);
                Panel.this.line.setVisibility(8);
            } else if (Panel.opendFlag == 3) {
                Panel.opendFlag = 1;
                Panel.this.lp.height = Panel.this.heights / 2;
                Panel.this.lp.bottomMargin = 0;
                Panel.this.mbottomMargin = Math.abs(Panel.this.lp.bottomMargin);
                Panel.this.setLayoutParams(Panel.this.lp);
                Panel.this.setOrientation(1);
                Panel.this.upLayout.setVisibility(0);
                Panel.this.downLayout.setVisibility(0);
                Panel.this.line.setVisibility(0);
            }
        }
    }

    /* renamed from: com.ifoer.view.Panel.3 */
    class C07733 implements OnClickListener {
        C07733() {
        }

        public void onClick(View arg0) {
            if (Panel.opendFlag == 1) {
                Panel.opendFlag = 2;
                Panel.this.lp.height = Panel.this.heights;
                Panel.this.lp.bottomMargin = 0;
                Panel.this.mbottomMargin = Math.abs(Panel.this.lp.bottomMargin);
                Panel.this.setLayoutParams(Panel.this.lp);
                Panel.this.setOrientation(1);
                Panel.this.upLayout.setVisibility(8);
                Panel.this.downLayout.setVisibility(0);
                Panel.this.line.setVisibility(8);
            } else if (Panel.opendFlag == 3) {
                Panel.opendFlag = 1;
                Panel.this.lp.height = Panel.this.heights / 2;
                Panel.this.lp.bottomMargin = 0;
                Panel.this.mbottomMargin = Math.abs(Panel.this.lp.bottomMargin);
                Panel.this.setLayoutParams(Panel.this.lp);
                Panel.this.setOrientation(1);
                Panel.this.upLayout.setVisibility(0);
                Panel.this.downLayout.setVisibility(0);
                Panel.this.line.setVisibility(0);
            }
        }
    }

    /* renamed from: com.ifoer.view.Panel.4 */
    class C07744 implements OnClickListener {
        C07744() {
        }

        public void onClick(View arg0) {
            if (Panel.opendFlag == 1) {
                Panel.opendFlag = 3;
                Panel.this.lp.height = Panel.this.heights;
                Panel.this.lp.bottomMargin = (-Panel.this.heights) + Panel.HANDLE_WIDTH;
                Panel.this.mbottomMargin = Math.abs(Panel.this.lp.bottomMargin);
                Panel.this.setLayoutParams(Panel.this.lp);
                Panel.this.setOrientation(1);
                Panel.this.upLayout.setVisibility(0);
                Panel.this.downLayout.setVisibility(8);
                Panel.this.line.setVisibility(8);
            } else if (Panel.opendFlag == 2) {
                Panel.opendFlag = 3;
                Panel.this.lp.height = Panel.this.heights;
                Panel.this.lp.bottomMargin = (-Panel.this.heights) + Panel.HANDLE_WIDTH;
                Panel.this.mbottomMargin = Math.abs(Panel.this.lp.bottomMargin);
                Panel.this.setLayoutParams(Panel.this.lp);
                Panel.this.setOrientation(1);
                Panel.this.upLayout.setVisibility(0);
                Panel.this.downLayout.setVisibility(8);
                Panel.this.line.setVisibility(8);
            }
        }
    }

    @SuppressLint({"NewApi"})
    class AsynMove extends AsyncTask<Integer, Integer, Void> {
        AsynMove() {
        }

        protected Void doInBackground(Integer... params) {
            int times;
            if (Panel.this.mbottomMargin % Math.abs(params[0].intValue()) == 0) {
                times = Panel.this.mbottomMargin / Math.abs(params[0].intValue());
            } else {
                times = (Panel.this.mbottomMargin / Math.abs(params[0].intValue())) + 1;
            }
            for (int i = 0; i < times; i++) {
                publishProgress(params);
                try {
                    Thread.sleep((long) Math.abs(params[0].intValue()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        protected void onProgressUpdate(Integer... params) {
            LayoutParams lp = (LayoutParams) Panel.this.getLayoutParams();
            if (params[0].intValue() < 0) {
                lp.bottomMargin = Math.max(lp.bottomMargin + params[0].intValue(), -Panel.this.mbottomMargin);
            } else {
                lp.bottomMargin = Math.min(lp.bottomMargin + params[0].intValue(), 0);
            }
            if (lp.bottomMargin == 0 && Panel.this.panelOpenedEvent != null) {
                Panel.this.panelOpenedEvent.onPanelOpened(Panel.this);
            } else if (lp.bottomMargin == (-Panel.this.mbottomMargin) && Panel.this.panelClosedEvent != null) {
                Panel.this.panelClosedEvent.onPanelClosed(Panel.this);
            }
            Panel.this.setLayoutParams(lp);
        }
    }

    public interface PanelClosedEvent {
        void onPanelClosed(View view);
    }

    public interface PanelOpenedEvent {
        void onPanelOpened(View view);
    }

    static {
        HANDLE_WIDTH = 30;
        MOVE_WIDTH = 20;
        opendFlag = 1;
    }

    public Panel(Context context, View otherView, int width, int height) {
        super(context);
        this.mbottomMargin = 0;
        this.panelClosedEvent = null;
        this.panelOpenedEvent = null;
        this.heights = 0;
        this.mContext = context;
        HANDLE_WIDTH = dip2px(this.mContext, Skew.SWEEP_RANGE);
        MOVE_WIDTH = dip2px(this.mContext, 20.0f);
        this.heights = height;
        LayoutParams otherLP = (LayoutParams) otherView.getLayoutParams();
        otherLP.weight = 1.0f;
        otherView.setLayoutParams(otherLP);
        this.lp = new LayoutParams(width, height);
        this.lp.height = height / 2;
        this.lp.bottomMargin = 0;
        this.mbottomMargin = Math.abs(this.lp.bottomMargin);
        setLayoutParams(this.lp);
        setBackgroundResource(C0136R.drawable.transparent);
        setOrientation(1);
        this.settingLayout = (RelativeLayout) LayoutInflater.from(context).inflate(C0136R.layout.handlerlayout, null);
        this.handleBackground = (ImageView) this.settingLayout.findViewById(C0136R.id.handleBackground);
        this.upLayout = (LinearLayout) this.settingLayout.findViewById(C0136R.id.upLayout);
        this.downLayout = (LinearLayout) this.settingLayout.findViewById(C0136R.id.downLayout);
        this.leftLayout = (LinearLayout) this.settingLayout.findViewById(C0136R.id.leftLayout);
        this.rightLayout = (LinearLayout) this.settingLayout.findViewById(C0136R.id.rightLayout);
        this.line = (LinearLayout) this.settingLayout.findViewById(C0136R.id.line);
        this.settingLayout.setLayoutParams(new LayoutParams(width, HANDLE_WIDTH));
        this.leftLayout.setOnClickListener(new C07711());
        this.rightLayout.setOnClickListener(new C07722());
        this.upLayout.setOnClickListener(new C07733());
        this.downLayout.setOnClickListener(new C07744());
        addView(this.settingLayout);
        this.panelContainer = new LinearLayout(context);
        this.panelContainer.setLayoutParams(new LayoutParams(-1, -1));
        addView(this.panelContainer);
    }

    public void setPanelClosedEvent(PanelClosedEvent event) {
        this.panelClosedEvent = event;
    }

    public void setPanelOpenedEvent(PanelOpenedEvent event) {
        this.panelOpenedEvent = event;
    }

    public void fillPanelContainer(View v) {
        this.panelContainer.addView(v);
    }

    public void removePanelContainer() {
        this.panelContainer.removeAllViews();
    }

    public void closePanelContainer() {
        opendFlag = 3;
        this.lp.height = this.heights;
        this.lp.bottomMargin = (-this.heights) + HANDLE_WIDTH;
        this.mbottomMargin = Math.abs(this.lp.bottomMargin);
        setLayoutParams(this.lp);
        setOrientation(1);
        this.upLayout.setVisibility(0);
        this.downLayout.setVisibility(8);
        this.line.setVisibility(8);
    }

    public void openthreePanelContainer() {
        if (opendFlag == 3) {
            opendFlag = 1;
            this.lp.height = this.heights / 2;
            this.lp.bottomMargin = 0;
            this.mbottomMargin = Math.abs(this.lp.bottomMargin);
            setLayoutParams(this.lp);
            setOrientation(1);
            this.upLayout.setVisibility(0);
            this.downLayout.setVisibility(0);
            this.line.setVisibility(0);
        }
    }

    public static int dip2px(Context context, float dipValue) {
        return (int) ((dipValue * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public void openAllPanel() {
        opendFlag = 2;
        this.lp.height = this.heights;
        this.lp.bottomMargin = 0;
        this.mbottomMargin = Math.abs(this.lp.bottomMargin);
        setLayoutParams(this.lp);
        setOrientation(1);
        this.upLayout.setVisibility(8);
        this.downLayout.setVisibility(0);
        this.line.setVisibility(8);
    }

    public void setLayoutParams(int width, int height) {
        this.heights = height;
        if (opendFlag == 1) {
            height /= 2;
        } else if (opendFlag != 2) {
            height = HANDLE_WIDTH;
        }
        this.lp = new LayoutParams(width, height);
        setLayoutParams(this.lp);
        this.settingLayout.setLayoutParams(new LayoutParams(width, HANDLE_WIDTH));
    }
}
