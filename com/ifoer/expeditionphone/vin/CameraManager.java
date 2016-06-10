package com.ifoer.expeditionphone.vin;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.WindowManager;
import java.io.IOException;

final class CameraManager {
    private static final String TAG = "MLOG: CameraManager.java: ";
    private static CameraManager m_CameraManager;
    private static byte m_cImgDivisor;
    private AutoFocusCallback m_AutoFocusCallback;
    private Camera m_Camera;
    private final Context m_Context;
    private Rect m_FramingRect;
    private Handler m_ParentMessageHandler;
    private SurfaceHolder m_ParentSurfaceHolder;
    PictureCallback m_PictureCallbackJPG;
    private boolean m_bInitialized;
    private boolean m_bPreviewing;
    private Point m_ptScreenResolution;

    /* renamed from: com.ifoer.expeditionphone.vin.CameraManager.1 */
    class C06571 implements PictureCallback {
        C06571() {
        }

        public void onPictureTaken(byte[] data, Camera c) {
            Log.i(CameraManager.TAG, "pcjpg - started");
            if (data == null) {
                Log.i(CameraManager.TAG, "data is null");
                return;
            }
            CameraManager.this.m_ParentMessageHandler.obtainMessage(5, data).sendToTarget();
            CameraManager.this.m_ParentMessageHandler = null;
            CameraManager.this.m_Camera.startPreview();
        }
    }

    /* renamed from: com.ifoer.expeditionphone.vin.CameraManager.2 */
    class C06582 implements AutoFocusCallback {
        C06582() {
        }

        public void onAutoFocus(boolean success, Camera camera) {
            if (success) {
                Log.v(CameraManager.TAG, " Focus succeded.");
                CameraManager.this.m_ParentMessageHandler.sendEmptyMessage(6);
                return;
            }
            Log.v(CameraManager.TAG, " Focus failed.");
            CameraManager.this.m_ParentMessageHandler.sendEmptyMessage(7);
        }
    }

    static {
        m_cImgDivisor = (byte) 2;
    }

    public void RequestPicture(Handler handler) {
        if (this.m_Camera != null && this.m_bPreviewing) {
            this.m_ParentMessageHandler = handler;
        }
    }

    public void RequestCameraFocus(Handler handler) {
        if (this.m_Camera != null && this.m_bPreviewing) {
            this.m_ParentMessageHandler = handler;
        }
    }

    public static void Initialize(Context context) {
        if (m_CameraManager == null) {
            m_CameraManager = new CameraManager(context);
        }
    }

    public static void SetImgDivisor(int imgDivisor) {
        if (imgDivisor == 1 || imgDivisor == 2 || imgDivisor == 4) {
            m_cImgDivisor = (byte) imgDivisor;
        } else {
            m_cImgDivisor = (byte) 2;
        }
    }

    public static CameraManager get() {
        return m_CameraManager;
    }

    private CameraManager(Context context) {
        this.m_ParentSurfaceHolder = null;
        this.m_PictureCallbackJPG = new C06571();
        this.m_AutoFocusCallback = new C06582();
        this.m_Context = context;
        GetScreenResolution(context);
        this.m_Camera = null;
        this.m_bInitialized = false;
        this.m_bPreviewing = false;
    }

    public void SetSurfaceHolder(SurfaceHolder holder) {
        this.m_ParentSurfaceHolder = holder;
    }

    public boolean OpenDriver() {
        if (this.m_ParentSurfaceHolder == null) {
            return false;
        }
        if (this.m_Camera == null) {
            this.m_Camera = Camera.open();
            try {
                this.m_Camera.setPreviewDisplay(this.m_ParentSurfaceHolder);
                if (!this.m_bInitialized) {
                    this.m_bInitialized = true;
                    GetScreenResolution(this.m_Context);
                }
                SetCameraParameters();
            } catch (IOException e) {
                Log.v(TAG, e.toString());
                return false;
            }
        }
        return true;
    }

    public void OpenDriver(SurfaceHolder holder) {
        if (this.m_Camera == null) {
            this.m_Camera = Camera.open();
            try {
                this.m_Camera.setPreviewDisplay(holder);
            } catch (IOException e) {
                Log.v(TAG, e.toString());
            }
            if (!this.m_bInitialized) {
                this.m_bInitialized = true;
                GetScreenResolution(this.m_Context);
            }
            SetCameraParameters();
        }
    }

    public void CloseDriver() {
        if (this.m_Camera != null) {
            this.m_Camera.release();
            this.m_Camera = null;
            this.m_ParentSurfaceHolder = null;
        }
    }

    public void StartPreview() {
        if (this.m_Camera != null && !this.m_bPreviewing) {
            this.m_Camera.startPreview();
            this.m_bPreviewing = true;
        }
    }

    public void StopPreview() {
        if (this.m_Camera != null && this.m_bPreviewing) {
            this.m_Camera.setPreviewCallback(null);
            this.m_Camera.stopPreview();
            this.m_bPreviewing = false;
        }
    }

    public void RequestAutoFocus() {
        if (this.m_Camera != null && this.m_bPreviewing) {
            this.m_Camera.autoFocus(this.m_AutoFocusCallback);
        }
    }

    public Rect GetFramingRect(boolean linemode) {
        if (linemode) {
            int left = (this.m_ptScreenResolution.x / 2) - ((this.m_ptScreenResolution.x * 2) / 5);
            int top = (this.m_ptScreenResolution.y / 2) - 50;
            int right = (this.m_ptScreenResolution.x / 2) + ((this.m_ptScreenResolution.x * 2) / 5);
            int bottom = (this.m_ptScreenResolution.y / 2) + 50;
            VinCameraActivity.mBitmapWidth = right - left;
            this.m_FramingRect = new Rect(left, top, right, bottom);
        } else {
            this.m_FramingRect = new Rect(0, 0, this.m_ptScreenResolution.x - 0, (this.m_ptScreenResolution.y - 0) - 50);
        }
        return this.m_FramingRect;
    }

    public void GetPicture() {
        this.m_Camera.takePicture(null, null, this.m_PictureCallbackJPG);
    }

    public void SetCameraParameters() {
        if (this.m_ptScreenResolution != null) {
            Display display = ((WindowManager) this.m_Context.getSystemService("window")).getDefaultDisplay();
            Parameters parameters = this.m_Camera.getParameters();
            parameters.setPreviewSize(this.m_ptScreenResolution.x, this.m_ptScreenResolution.y);
            parameters.setPictureSize(display.getWidth(), display.getHeight());
            this.m_Camera.setParameters(parameters);
            Log.v(TAG, parameters.flatten());
        }
    }

    private Point GetScreenResolution(Context context) {
        if (this.m_ptScreenResolution == null) {
            Display display = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
            this.m_ptScreenResolution = new Point(display.getWidth(), display.getHeight());
        }
        return this.m_ptScreenResolution;
    }
}
