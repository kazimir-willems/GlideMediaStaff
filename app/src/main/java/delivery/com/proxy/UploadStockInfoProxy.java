package delivery.com.proxy;

import java.io.IOException;

import delivery.com.util.URLManager;
import delivery.com.vo.UploadStockInfoRequestVo;
import delivery.com.vo.UploadStockInfoResponseVo;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class UploadStockInfoProxy extends BaseProxy {

    public UploadStockInfoResponseVo run(String despatches) throws IOException {
        UploadStockInfoRequestVo requestVo = new UploadStockInfoRequestVo();
        requestVo.data = despatches;
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("data", requestVo.data);

        RequestBody formBody = formBuilder.build();

        String contentString = postPlain(URLManager.getUploadStockInfoURL(), formBody);

        UploadStockInfoResponseVo responseVo = new UploadStockInfoResponseVo();

        responseVo.success = 1;

        return responseVo;
    }
}
