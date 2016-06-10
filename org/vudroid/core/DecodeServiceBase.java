package org.vudroid.core;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.vudroid.core.DecodeService.DecodeCallback;
import org.vudroid.core.codec.CodecContext;
import org.vudroid.core.codec.CodecDocument;
import org.vudroid.core.codec.CodecPage;
import org.vudroid.core.utils.PathFromUri;

public class DecodeServiceBase implements DecodeService {
    public static final String DECODE_SERVICE = "ViewDroidDecodeService";
    private static final int PAGE_POOL_SIZE = 32;
    private final CodecContext codecContext;
    private View containerView;
    private ContentResolver contentResolver;
    private final Map<Object, Future<?>> decodingFutures;
    private CodecDocument document;
    private final ExecutorService executorService;
    private boolean isRecycled;
    private Queue<Integer> pageEvictionQueue;
    @SuppressLint({"UseSparseArrays"})
    private final HashMap<Integer, SoftReference<CodecPage>> pages;

    /* renamed from: org.vudroid.core.DecodeServiceBase.1 */
    class C09911 implements Runnable {
        private final /* synthetic */ DecodeTask val$decodeTask;

        C09911(DecodeTask decodeTask) {
            this.val$decodeTask = decodeTask;
        }

        public void run() {
            try {
                Thread.currentThread().setPriority(4);
                DecodeServiceBase.this.performDecode(this.val$decodeTask);
            } catch (IOException e) {
                Log.e(DecodeServiceBase.DECODE_SERVICE, "Decode fail", e);
            }
        }
    }

    /* renamed from: org.vudroid.core.DecodeServiceBase.2 */
    class C09922 implements Runnable {
        C09922() {
        }

        public void run() {
            for (SoftReference<CodecPage> codecPageSoftReference : DecodeServiceBase.this.pages.values()) {
                CodecPage page = (CodecPage) codecPageSoftReference.get();
                if (page != null) {
                    page.recycle();
                }
            }
            DecodeServiceBase.this.document.recycle();
            DecodeServiceBase.this.codecContext.recycle();
        }
    }

    private class DecodeTask {
        private final DecodeCallback decodeCallback;
        private final Object decodeKey;
        private final int pageNumber;
        private final RectF pageSliceBounds;
        private final float zoom;

        private DecodeTask(int pageNumber, DecodeCallback decodeCallback, float zoom, Object decodeKey, RectF pageSliceBounds) {
            this.pageNumber = pageNumber;
            this.decodeCallback = decodeCallback;
            this.zoom = zoom;
            this.decodeKey = decodeKey;
            this.pageSliceBounds = pageSliceBounds;
        }
    }

    public DecodeServiceBase(CodecContext codecContext) {
        this.executorService = Executors.newSingleThreadExecutor();
        this.decodingFutures = new ConcurrentHashMap();
        this.pages = new HashMap();
        this.pageEvictionQueue = new LinkedList();
        this.codecContext = codecContext;
    }

    public void setContentResolver(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
        this.codecContext.setContentResolver(contentResolver);
    }

    public void setContainerView(View containerView) {
        this.containerView = containerView;
    }

    public void open(Uri fileUri) {
        this.document = this.codecContext.openDocument(PathFromUri.retrieve(this.contentResolver, fileUri));
    }

    public void decodePage(Object decodeKey, int pageNum, DecodeCallback decodeCallback, float zoom, RectF pageSliceBounds) {
        DecodeTask decodeTask = new DecodeTask(pageNum, decodeCallback, zoom, decodeKey, pageSliceBounds, null);
        synchronized (this.decodingFutures) {
            if (this.isRecycled) {
                return;
            }
            Future<?> removed = (Future) this.decodingFutures.put(decodeKey, this.executorService.submit(new C09911(decodeTask)));
            if (removed != null) {
                removed.cancel(false);
            }
        }
    }

    public void stopDecoding(Object decodeKey) {
        Future<?> future = (Future) this.decodingFutures.remove(decodeKey);
        if (future != null) {
            future.cancel(false);
        }
    }

    private void performDecode(DecodeTask currentDecodeTask) throws IOException {
        if (isTaskDead(currentDecodeTask)) {
            Log.d(DECODE_SERVICE, "Skipping decode task for page " + currentDecodeTask.pageNumber);
            return;
        }
        Log.d(DECODE_SERVICE, "Starting decode of page: " + currentDecodeTask.pageNumber);
        CodecPage vuPage = getPage(currentDecodeTask.pageNumber);
        preloadNextPage(currentDecodeTask.pageNumber);
        if (!isTaskDead(currentDecodeTask)) {
            Log.d(DECODE_SERVICE, "Start converting map to bitmap");
            float scale = calculateScale(vuPage) * currentDecodeTask.zoom;
            Bitmap bitmap = vuPage.renderBitmap(getScaledWidth(currentDecodeTask, vuPage, scale), getScaledHeight(currentDecodeTask, vuPage, scale), currentDecodeTask.pageSliceBounds);
            Log.d(DECODE_SERVICE, "Converting map to bitmap finished");
            if (isTaskDead(currentDecodeTask)) {
                bitmap.recycle();
            } else {
                finishDecoding(currentDecodeTask, bitmap);
            }
        }
    }

    private int getScaledHeight(DecodeTask currentDecodeTask, CodecPage vuPage, float scale) {
        return Math.round(((float) getScaledHeight(vuPage, scale)) * currentDecodeTask.pageSliceBounds.height());
    }

    private int getScaledWidth(DecodeTask currentDecodeTask, CodecPage vuPage, float scale) {
        return Math.round(((float) getScaledWidth(vuPage, scale)) * currentDecodeTask.pageSliceBounds.width());
    }

    private int getScaledHeight(CodecPage vuPage, float scale) {
        return (int) (((float) vuPage.getHeight()) * scale);
    }

    private int getScaledWidth(CodecPage vuPage, float scale) {
        return (int) (((float) vuPage.getWidth()) * scale);
    }

    private float calculateScale(CodecPage codecPage) {
        return (1.0f * ((float) getTargetWidth())) / ((float) codecPage.getWidth());
    }

    private void finishDecoding(DecodeTask currentDecodeTask, Bitmap bitmap) {
        updateImage(currentDecodeTask, bitmap);
        stopDecoding(Integer.valueOf(currentDecodeTask.pageNumber));
    }

    private void preloadNextPage(int pageNumber) throws IOException {
        int nextPage = pageNumber + 1;
        if (nextPage < getPageCount()) {
            getPage(nextPage);
        }
    }

    private CodecPage getPage(int pageIndex) {
        if (!this.pages.containsKey(Integer.valueOf(pageIndex)) || ((SoftReference) this.pages.get(Integer.valueOf(pageIndex))).get() == null) {
            this.pages.put(Integer.valueOf(pageIndex), new SoftReference(this.document.getPage(pageIndex)));
            this.pageEvictionQueue.remove(Integer.valueOf(pageIndex));
            this.pageEvictionQueue.offer(Integer.valueOf(pageIndex));
            if (this.pageEvictionQueue.size() > PAGE_POOL_SIZE) {
                CodecPage evictedPage = (CodecPage) ((SoftReference) this.pages.remove((Integer) this.pageEvictionQueue.poll())).get();
                if (evictedPage != null) {
                    evictedPage.recycle();
                }
            }
        }
        return (CodecPage) ((SoftReference) this.pages.get(Integer.valueOf(pageIndex))).get();
    }

    private void waitForDecode(CodecPage vuPage) {
        vuPage.waitForDecode();
    }

    private int getTargetWidth() {
        return this.containerView.getWidth();
    }

    public int getEffectivePagesWidth() {
        CodecPage page = getPage(0);
        return getScaledWidth(page, calculateScale(page));
    }

    public int getEffectivePagesHeight() {
        CodecPage page = getPage(0);
        return getScaledHeight(page, calculateScale(page));
    }

    public int getPageWidth(int pageIndex) {
        return getPage(pageIndex).getWidth();
    }

    public int getPageHeight(int pageIndex) {
        return getPage(pageIndex).getHeight();
    }

    private void updateImage(DecodeTask currentDecodeTask, Bitmap bitmap) {
        currentDecodeTask.decodeCallback.decodeComplete(bitmap);
    }

    private boolean isTaskDead(DecodeTask currentDecodeTask) {
        boolean z;
        synchronized (this.decodingFutures) {
            z = !this.decodingFutures.containsKey(currentDecodeTask.decodeKey);
        }
        return z;
    }

    public int getPageCount() {
        return this.document.getPageCount();
    }

    public void recycle() {
        synchronized (this.decodingFutures) {
            this.isRecycled = true;
        }
        for (Object key : this.decodingFutures.keySet()) {
            stopDecoding(key);
        }
        this.executorService.submit(new C09922());
        this.executorService.shutdown();
    }
}
