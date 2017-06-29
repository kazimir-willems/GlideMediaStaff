package staff.com.event;

import staff.com.vo.UploadModifiedStockResponseVo;

public class UploadModifiedStockEvent {
    private UploadModifiedStockResponseVo responseVo;

    public UploadModifiedStockEvent(UploadModifiedStockResponseVo responseVo) {
        this.responseVo = responseVo;
    }

    public UploadModifiedStockResponseVo getResponse() {
        return responseVo;
    }
}
