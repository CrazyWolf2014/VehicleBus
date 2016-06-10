package com.ifoer.expeditionphone.vin;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.cnlaunch.x431frame.C0136R;
import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechConfig.RATE;
import com.iflytek.speech.SpeechError;
import com.iflytek.ui.RecognizerDialog;
import com.iflytek.ui.RecognizerDialogListener;
import com.ifoer.ui.FastDiagnosisActivity;
import com.tencent.mm.sdk.platformtools.SpecilApiUtil;
import java.util.ArrayList;
import java.util.Iterator;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xmlpull.v1.XmlPullParser;

public class VinVoiceActivity extends Activity implements RecognizerDialogListener {
    private AnimationDrawable animationDrawable;
    private Button btnCancel;
    private Button btnOk;
    private OnClickListener clickListener;
    private EditText editText;
    private RecognizerDialog isrDialog;
    private ImageView ivVoice;
    private LinearLayout llVoiceWindow;

    /* renamed from: com.ifoer.expeditionphone.vin.VinVoiceActivity.1 */
    class C06691 implements OnClickListener {
        C06691() {
        }

        public void onClick(View v) {
            if (v.getId() == C0136R.id.btn_voice_ok) {
                Intent intent = new Intent();
                intent.putExtra("VOICE", VinVoiceActivity.this.editText.getText().toString());
                VinVoiceActivity.this.setResult(FastDiagnosisActivity.VOICE_RECOGNITION_REQUEST_CODE, intent);
                VinVoiceActivity.this.finish();
            } else if (v.getId() == C0136R.id.btn_voice_cancel) {
                if (VinVoiceActivity.this.getResources().getString(C0136R.string.cancel).equals(VinVoiceActivity.this.btnCancel.getText().toString())) {
                    VinVoiceActivity.this.finish();
                } else {
                    VinVoiceActivity.this.editText.setText(XmlPullParser.NO_NAMESPACE);
                }
            } else if (v.getId() == C0136R.id.iv_voice_voice) {
                VinVoiceActivity.this.showIsrDialog();
            }
        }
    }

    /* renamed from: com.ifoer.expeditionphone.vin.VinVoiceActivity.2 */
    class C06702 implements TextWatcher {
        C06702() {
        }

        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            if (XmlPullParser.NO_NAMESPACE.equals(VinVoiceActivity.this.editText.getText().toString())) {
                VinVoiceActivity.this.btnCancel.setText(C0136R.string.cancel);
            } else {
                VinVoiceActivity.this.btnCancel.setText(C0136R.string.clean);
            }
        }

        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }

        public void afterTextChanged(Editable arg0) {
        }
    }

    public VinVoiceActivity() {
        this.isrDialog = null;
        this.clickListener = new C06691();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0136R.layout.vin_voice);
        initView();
    }

    private void initView() {
        this.isrDialog = new RecognizerDialog(this, "appid=523e6750");
        this.isrDialog.setListener(this);
        this.editText = (EditText) findViewById(C0136R.id.et_text);
        this.btnOk = (Button) findViewById(C0136R.id.btn_voice_ok);
        this.btnCancel = (Button) findViewById(C0136R.id.btn_voice_cancel);
        this.ivVoice = (ImageView) findViewById(C0136R.id.iv_voice_voice);
        this.llVoiceWindow = (LinearLayout) findViewById(C0136R.id.ll_voice);
        this.ivVoice.setOnClickListener(this.clickListener);
        this.btnCancel.setOnClickListener(this.clickListener);
        this.btnOk.setOnClickListener(this.clickListener);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        this.llVoiceWindow.setLayoutParams(new LayoutParams(dm.widthPixels - 40, (int) (((double) dm.heightPixels) * 0.2857142857142857d)));
        this.llVoiceWindow.setBackgroundResource(C0136R.drawable.bg_radius);
        this.ivVoice.setImageResource(C0136R.drawable.voice_search_start_anim);
        this.animationDrawable = (AnimationDrawable) this.ivVoice.getDrawable();
        this.animationDrawable.start();
        this.editText.addTextChangedListener(new C06702());
    }

    public void onResults(ArrayList<RecognizerResult> results, boolean isLast) {
        StringBuilder builder = new StringBuilder();
        Iterator it = results.iterator();
        while (it.hasNext()) {
            RecognizerResult recognizerResult = (RecognizerResult) it.next();
            if ("\u3002".equals(recognizerResult.text)) {
                builder.append(SpecilApiUtil.LINE_SEP);
            } else {
                builder.append(str2NumbersOrLetters(recognizerResult.text).toUpperCase());
            }
        }
        this.editText.append(builder);
        this.editText.setSelection(this.editText.length());
    }

    public void onEnd(SpeechError arg0) {
    }

    public void showIsrDialog() {
        this.isrDialog.setSampleRate(RATE.rate16k);
        this.isrDialog.setEngine("sms", null, null);
        this.isrDialog.show();
    }

    public String str2NumbersOrLetters(String str) {
        char[] temC = str.toCharArray();
        for (int i = 0; i < temC.length; i++) {
            int mid = temC[i];
            if ((mid < 48 || mid > 57) && ((mid < 65 || mid > 90) && (mid < 97 || mid > Opcodes.ISHR))) {
                temC[i] = ' ';
            }
        }
        return new String(temC).replaceAll(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, XmlPullParser.NO_NAMESPACE);
    }
}
