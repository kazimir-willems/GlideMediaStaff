package staff.com.event;

import staff.com.vo.DownloadStockInfoResponseVo;

public class DownloadStockInfoEvent {
    private DownloadStockInfoResponseVo responseVo;

    public DownloadStockInfoEvent(DownloadStockInfoResponseVo responseVo) {
        this.responseVo = responseVo;
    }

    public DownloadStockInfoResponseVo getResponse() {
        return responseVo;
    }
}
