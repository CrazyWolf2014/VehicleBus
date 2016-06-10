package com.google.zxing.oned.rss.expanded.decoders;

import com.amap.mapapi.map.TrafficProtos.TrafficTile.TrafficIncident;
import com.cnmobi.im.view.RecordButton;
import com.google.protobuf.DescriptorProtos.UninterpretedOption;
import com.google.zxing.NotFoundException;
import com.google.zxing.common.BitArray;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.codehaus.jackson.smile.SmileConstants;
import org.xbill.DNS.WKSRecord.Protocol;
import org.xbill.DNS.WKSRecord.Service;

public abstract class AbstractExpandedDecoder {
    protected final GeneralAppIdDecoder generalDecoder;
    protected final BitArray information;

    AbstractExpandedDecoder(BitArray bitArray) {
        this.information = bitArray;
        this.generalDecoder = new GeneralAppIdDecoder(bitArray);
    }

    public static AbstractExpandedDecoder createDecoder(BitArray bitArray) {
        if (bitArray.get(1)) {
            return new AI01AndOtherAIs(bitArray);
        }
        if (!bitArray.get(2)) {
            return new AnyAIDecoder(bitArray);
        }
        switch (GeneralAppIdDecoder.extractNumericValueFromBitArray(bitArray, 1, 4)) {
            case UninterpretedOption.POSITIVE_INT_VALUE_FIELD_NUMBER /*4*/:
                return new AI013103decoder(bitArray);
            case UninterpretedOption.NEGATIVE_INT_VALUE_FIELD_NUMBER /*5*/:
                return new AI01320xDecoder(bitArray);
            default:
                switch (GeneralAppIdDecoder.extractNumericValueFromBitArray(bitArray, 1, 5)) {
                    case TrafficIncident.VERTEXOFFSET_FIELD_NUMBER /*12*/:
                        return new AI01392xDecoder(bitArray);
                    case TrafficIncident.INCIDENTVERTEX_FIELD_NUMBER /*13*/:
                        return new AI01393xDecoder(bitArray);
                    default:
                        switch (GeneralAppIdDecoder.extractNumericValueFromBitArray(bitArray, 1, 7)) {
                            case SmileConstants.MAX_SHORT_NAME_UNICODE_BYTES /*56*/:
                                return new AI013x0x1xDecoder(bitArray, "310", "11");
                            case Opcodes.DSTORE /*57*/:
                                return new AI013x0x1xDecoder(bitArray, "320", "11");
                            case Opcodes.ASTORE /*58*/:
                                return new AI013x0x1xDecoder(bitArray, "310", "13");
                            case 59:
                                return new AI013x0x1xDecoder(bitArray, "320", "13");
                            case RecordButton.MAX_TIME /*60*/:
                                return new AI013x0x1xDecoder(bitArray, "310", "15");
                            case Service.NI_MAIL /*61*/:
                                return new AI013x0x1xDecoder(bitArray, "320", "15");
                            case Protocol.CFTP /*62*/:
                                return new AI013x0x1xDecoder(bitArray, "310", "17");
                            case Service.VIA_FTP /*63*/:
                                return new AI013x0x1xDecoder(bitArray, "320", "17");
                            default:
                                throw new IllegalStateException(new StringBuffer().append("unknown decoder: ").append(bitArray).toString());
                        }
                }
        }
    }

    public abstract String parseInformation() throws NotFoundException;
}
