package com.cnmobi.im.bo;

import com.cnmobi.im.dto.RiderVo;
import java.util.List;

public interface CnmobiImListener {
    void latelyRiderChange(List<RiderVo> list);

    void riderChange(List<RiderVo> list);
}
