package com.google.zxing.oned.rss.expanded.decoders;

import com.google.zxing.common.BitArray;
import com.thoughtworks.xstream.XStream;

final class AI01320xDecoder extends AI013x0xDecoder {
    AI01320xDecoder(BitArray bitArray) {
        super(bitArray);
    }

    protected void addWeightCode(StringBuffer stringBuffer, int i) {
        if (i < XStream.PRIORITY_VERY_HIGH) {
            stringBuffer.append("(3202)");
        } else {
            stringBuffer.append("(3203)");
        }
    }

    protected int checkWeight(int i) {
        return i < XStream.PRIORITY_VERY_HIGH ? i : i - 10000;
    }
}
