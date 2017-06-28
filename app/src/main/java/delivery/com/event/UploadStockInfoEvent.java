package delivery.com.event;

import delivery.com.vo.UploadStockInfoResponseVo;

public class UploadStockInfoEvent {
    private UploadStockInfoResponseVo responseVo;

    public UploadStockInfoEvent(UploadStockInfoResponseVo responseVo) {
        this.responseVo = responseVo;
    }

    public UploadStockInfoResponseVo getResponse() {
        return responseVo;
    }
}
