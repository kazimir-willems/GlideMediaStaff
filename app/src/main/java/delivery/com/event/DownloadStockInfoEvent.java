package delivery.com.event;

import delivery.com.vo.DownloadStockInfoResponseVo;

public class DownloadStockInfoEvent {
    private DownloadStockInfoResponseVo responseVo;

    public DownloadStockInfoEvent(DownloadStockInfoResponseVo responseVo) {
        this.responseVo = responseVo;
    }

    public DownloadStockInfoResponseVo getResponse() {
        return responseVo;
    }
}
