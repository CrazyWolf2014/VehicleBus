package com.amap.mapapi.map;

import android.view.View;
import android.view.View.OnClickListener;
import com.amap.mapapi.map.MapView.C0094e;

/* compiled from: MapView */
class ae implements OnClickListener {
    final /* synthetic */ C0094e f562a;

    ae(C0094e c0094e) {
        this.f562a = c0094e;
    }

    public void onClick(View view) {
        int i = 0;
        while (i < 4) {
            if (this.f562a.f455c[i].equals(view)) {
                if (i > 1) {
                    this.f562a.f453a = true;
                } else {
                    this.f562a.f453a = false;
                }
                if (this.f562a.f457e != null) {
                    this.f562a.f457e.m595a(i);
                }
            }
            i++;
        }
        i = -1;
        if (this.f562a.f457e != null) {
            this.f562a.f457e.m595a(i);
        }
    }
}
