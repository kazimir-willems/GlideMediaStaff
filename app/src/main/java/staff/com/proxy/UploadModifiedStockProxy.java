package staff.com.proxy;

import java.io.IOException;

import staff.com.util.URLManager;
import staff.com.vo.UploadModifiedStockRequestVo;
import staff.com.vo.UploadModifiedStockResponseVo;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class UploadModifiedStockProxy extends BaseProxy {

    public UploadModifiedStockResponseVo run(String despatches) throws IOException {
        UploadModifiedStockRequestVo requestVo = new UploadModifiedStockRequestVo();
        requestVo.data = despatches;
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("data", requestVo.data);

        RequestBody formBody = formBuilder.build();

        String contentString = postPlain(URLManager.getUploadModifiedStockURL(), formBody);

        UploadModifiedStockResponseVo responseVo = new UploadModifiedStockResponseVo();

        responseVo.success = 1;

        return responseVo;
    }
}
