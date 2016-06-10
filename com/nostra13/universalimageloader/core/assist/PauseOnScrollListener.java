package com.nostra13.universalimageloader.core.assist;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import com.google.protobuf.DescriptorProtos.MessageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import org.xbill.DNS.KEYRecord;

public class PauseOnScrollListener implements OnScrollListener {
    private final OnScrollListener externalListener;
    private ImageLoader imageLoader;
    private final boolean pauseOnFling;
    private final boolean pauseOnScroll;

    public PauseOnScrollListener(ImageLoader imageLoader, boolean pauseOnScroll, boolean pauseOnFling) {
        this(imageLoader, pauseOnScroll, pauseOnFling, null);
    }

    public PauseOnScrollListener(ImageLoader imageLoader, boolean pauseOnScroll, boolean pauseOnFling, OnScrollListener customListener) {
        this.imageLoader = imageLoader;
        this.pauseOnScroll = pauseOnScroll;
        this.pauseOnFling = pauseOnFling;
        this.externalListener = customListener;
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case KEYRecord.OWNER_USER /*0*/:
                this.imageLoader.resume();
                break;
            case MessageOptions.MESSAGE_SET_WIRE_FORMAT_FIELD_NUMBER /*1*/:
                if (this.pauseOnScroll) {
                    this.imageLoader.pause();
                    break;
                }
                break;
            case MessageOptions.NO_STANDARD_DESCRIPTOR_ACCESSOR_FIELD_NUMBER /*2*/:
                if (this.pauseOnFling) {
                    this.imageLoader.pause();
                    break;
                }
                break;
        }
        if (this.externalListener != null) {
            this.externalListener.onScrollStateChanged(view, scrollState);
        }
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (this.externalListener != null) {
            this.externalListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }
}
