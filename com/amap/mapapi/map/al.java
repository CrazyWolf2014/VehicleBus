package com.amap.mapapi.map;

import android.graphics.Point;
import com.mapabc.minimap.map.vmap.NativeMap;
import com.mapabc.minimap.map.vmap.NativeMapEngine;
import com.mapabc.minimap.map.vmap.VMapProjection;
import java.nio.ByteBuffer;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.xbill.DNS.KEYRecord;
import org.xbill.DNS.KEYRecord.Flags;

/* compiled from: RenderedMapTile */
class al {
    public String f622a;
    boolean f623b;

    al() {
        this.f623b = false;
    }

    public boolean m778a(NativeMapEngine nativeMapEngine) {
        try {
            ByteBuffer allocate = ByteBuffer.allocate(Opcodes.ACC_DEPRECATED);
            if (nativeMapEngine.hasBitMapData(this.f622a)) {
                nativeMapEngine.fillBitmapBufferData(this.f622a, allocate.array());
            } else {
                NativeMap nativeMap = new NativeMap();
                nativeMap.initMap(allocate.array(), KEYRecord.OWNER_ZONE, KEYRecord.OWNER_ZONE);
                Point QuadKeyToTile = VMapProjection.QuadKeyToTile(this.f622a);
                int length = this.f622a.length();
                nativeMap.setMapParameter(((QuadKeyToTile.x * KEYRecord.OWNER_ZONE) + Flags.FLAG8) << (20 - length), ((QuadKeyToTile.y * KEYRecord.OWNER_ZONE) + Flags.FLAG8) << (20 - length), length, 0);
                nativeMap.paintMap(nativeMapEngine, 1);
                nativeMapEngine.putBitmapData(this.f622a, allocate.array(), KEYRecord.OWNER_ZONE, KEYRecord.OWNER_ZONE);
            }
            this.f623b = true;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
