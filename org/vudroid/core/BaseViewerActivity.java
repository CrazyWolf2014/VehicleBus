package org.vudroid.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.Toast;
import com.cnlaunch.mycar.jni.JniX431File;
import com.tencent.mm.sdk.platformtools.FilePathGenerator;
import org.vudroid.core.events.CurrentPageListener;
import org.vudroid.core.events.DecodingProgressListener;
import org.vudroid.core.models.CurrentPageModel;
import org.vudroid.core.models.DecodingProgressModel;
import org.vudroid.core.models.ZoomModel;
import org.xbill.DNS.KEYRecord.Flags;

public abstract class BaseViewerActivity extends Activity implements DecodingProgressListener, CurrentPageListener {
    private static final String DOCUMENT_VIEW_STATE_PREFERENCES = "DjvuDocumentViewState";
    private CurrentPageModel currentPageModel;
    private DecodeService decodeService;
    private DocumentView documentView;
    private Toast pageNumberToast;
    private PDFPreferences viewerPreferences;

    protected abstract DecodeService createDecodeService();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDecodeService();
        ZoomModel zoomModel = new ZoomModel();
        DecodingProgressModel progressModel = new DecodingProgressModel();
        progressModel.addEventListener(this);
        this.currentPageModel = new CurrentPageModel();
        this.currentPageModel.addEventListener(this);
        this.documentView = new DocumentView(this, zoomModel, progressModel, this.currentPageModel);
        zoomModel.addEventListener(this.documentView);
        this.documentView.setLayoutParams(new LayoutParams(-1, -1));
        this.decodeService.setContentResolver(getContentResolver());
        this.decodeService.setContainerView(this.documentView);
        this.documentView.setDecodeService(this.decodeService);
        this.decodeService.open(getIntent().getData());
        this.viewerPreferences = new PDFPreferences(this);
        FrameLayout frameLayout = createMainContainer();
        frameLayout.addView(this.documentView);
        setFullScreen();
        setContentView(frameLayout);
        this.documentView.goToPage(getSharedPreferences(DOCUMENT_VIEW_STATE_PREFERENCES, 0).getInt(getIntent().getData().toString(), 0));
        this.documentView.showDocument();
        this.viewerPreferences.addRecent(getIntent().getData());
    }

    public void decodingProgressChanged(int currentlyDecoding) {
    }

    @SuppressLint({"ShowToast"})
    public void currentPageChanged(int pageIndex) {
        String pageText = (pageIndex + 1) + FilePathGenerator.ANDROID_DIR_SEP + this.decodeService.getPageCount();
        if (this.pageNumberToast != null) {
            this.pageNumberToast.setText(pageText);
        } else {
            this.pageNumberToast = Toast.makeText(this, pageText, JniX431File.MAX_DS_COLNUMBER);
        }
        this.pageNumberToast.setGravity(51, 0, 0);
        this.pageNumberToast.show();
        saveCurrentPage();
    }

    private void setWindowTitle() {
        getWindow().setTitle(getIntent().getData().getLastPathSegment());
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setWindowTitle();
    }

    private void setFullScreen() {
        if (this.viewerPreferences.isFullScreen()) {
            getWindow().requestFeature(1);
            getWindow().setFlags(Flags.FLAG5, Flags.FLAG5);
        }
    }

    private FrameLayout createMainContainer() {
        return new FrameLayout(this);
    }

    private void initDecodeService() {
        if (this.decodeService == null) {
            this.decodeService = createDecodeService();
        }
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onDestroy() {
        this.decodeService.recycle();
        this.decodeService = null;
        super.onDestroy();
    }

    private void saveCurrentPage() {
        Editor editor = getSharedPreferences(DOCUMENT_VIEW_STATE_PREFERENCES, 0).edit();
        editor.putInt(getIntent().getData().toString(), this.documentView.getCurrentPage());
        editor.commit();
    }
}
