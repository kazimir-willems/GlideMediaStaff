package delivery.com.event;

import delivery.com.vo.UploadModifiedStockResponseVo;
import delivery.com.vo.UploadStockInfoResponseVo;

public class UploadModifiedStockEvent {
    private UploadModifiedStockResponseVo responseVo;

    public UploadModifiedStockEvent(UploadModifiedStockResponseVo responseVo) {
        this.responseVo = responseVo;
    }

    public UploadModifiedStockResponseVo getResponse() {
        return responseVo;
    }
}
