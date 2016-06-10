package com.cnmobi.im.bo;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Log;
import com.cnmobi.im.ImMainActivity;
import com.cnmobi.im.dao.RiderDao;
import com.cnmobi.im.dto.RiderVo;
import com.cnmobi.im.util.FileConstant;
import com.cnmobi.im.util.UiUtils;
import com.cnmobi.im.util.XmppConnection;
import com.tencent.mm.sdk.platformtools.Util;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.PrivacyItem.PrivacyRule;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.smackx.provider.VCardProvider;

public class LogoManager {
    private static final String TAG = "LogoManager";
    private static LogoManager instance;
    private Map<String, Bitmap> bitmapMap;
    private ExecutorService executor;
    List<LogoBackListener> listeners;

    public class FetchLogoRunnable implements Runnable {
        String jid;

        FetchLogoRunnable(String jid) {
            this.jid = jid;
        }

        public void run() {
            LogoManager.getInstance().getSingleLogo(this.jid);
        }
    }

    public interface LogoBackListener {
        void logoback(String str, Bitmap bitmap);
    }

    static {
        instance = new LogoManager();
    }

    public void start2GetLogo(String jid) {
        this.executor.execute(new FetchLogoRunnable(jid));
    }

    public void add(LogoBackListener listener) {
        this.listeners.add(listener);
    }

    public void remove(LogoBackListener listener) {
        this.listeners.remove(listener);
    }

    private LogoManager() {
        this.bitmapMap = new HashMap();
        this.executor = Executors.newFixedThreadPool(3);
        this.listeners = new ArrayList();
    }

    public static LogoManager getInstance() {
        return instance;
    }

    public void getRiderLogos() {
        List<RiderVo> riders = null;
        try {
            riders = RiderDao.getInstance().getRiders(ImMainActivity.mOwerJid, PrivacyRule.SUBSCRIPTION_BOTH);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (riders != null && riders.size() != 0) {
            for (RiderVo rider : riders) {
                try {
                    notifyLogoListeners(rider.jId, getBitmapFromServer(rider.jId));
                } catch (Exception e2) {
                }
            }
        }
    }

    public void getSingleLogo(String jid) {
        try {
            notifyLogoListeners(jid, getBitmapByJid(jid));
        } catch (Exception e) {
        }
    }

    public void notifyLogoListeners(String jid, Bitmap bitmap) {
        for (LogoBackListener listener : this.listeners) {
            listener.logoback(jid, bitmap);
        }
    }

    public Bitmap getBitmapByJid(String jid) {
        Bitmap result = getBitmapFromCache(jid);
        if (result != null) {
            return result;
        }
        try {
            result = getBitmapFromServer(jid);
        } catch (Exception e) {
            Log.e(TAG, "Get Bitmao from Server Error:", e);
        }
        return result;
    }

    public Bitmap getBitmapFromCache(String jid) {
        Bitmap result = (Bitmap) this.bitmapMap.get(jid);
        if (result != null) {
            return result;
        }
        File file = new File(FileConstant.FILE_LOGOS + UiUtils.getCCNumber(jid) + Util.PHOTO_DEFAULT_EXT);
        if (file.exists()) {
            result = BitmapFactory.decodeFile(file.getPath());
            if (result != null) {
                this.bitmapMap.put(jid, result);
                return result;
            }
        }
        return result;
    }

    public Bitmap getBitmapFromServer(String jid) {
        VCard vcard = new VCard();
        ProviderManager.getInstance().addIQProvider("vCard", "vcard-temp", new VCardProvider());
        try {
            vcard.load(XmppConnection.getConnection(), jid);
            if (vcard == null || vcard.getAvatar() == null) {
                return null;
            }
            ByteArrayInputStream bais = new ByteArrayInputStream(vcard.getAvatar());
            Bitmap result = BitmapFactory.decodeStream(bais);
            try {
                cache(jid, result);
                bais.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        } catch (XMPPException e2) {
            return null;
        }
    }

    public void cache(String jid, Bitmap bitmap) throws Exception {
        Log.d(TAG, "SDFileFetcher cache");
        if (bitmap != null) {
            this.bitmapMap.put(jid, bitmap);
            File dirFile = new File(FileConstant.FILE_LOGOS);
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            File file = new File(FileConstant.FILE_LOGOS + UiUtils.getCCNumber(jid) + Util.PHOTO_DEFAULT_EXT);
            try {
                file.createNewFile();
                OutputStream outStream = new FileOutputStream(file);
                bitmap.compress(CompressFormat.JPEG, 50, outStream);
                outStream.flush();
                outStream.close();
                RiderDao.getInstance().updateLogoUri(jid, file.getPath());
            } catch (FileNotFoundException e) {
                Log.w("FileNotFoundException", e);
            } catch (IOException e2) {
                Log.w("IOException", e2);
            }
        }
    }

    public void removeFromMap(String jid) {
        this.bitmapMap.remove(jid);
    }
}
