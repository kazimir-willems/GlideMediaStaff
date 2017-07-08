package staff.com.proxy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import staff.com.application.DeliveryApplication;
import staff.com.consts.StateConsts;
import staff.com.util.URLManager;
import staff.com.vo.DownloadStockInfoResponseVo;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class DownloadStockInfoProxy extends BaseProxy {

    public DownloadStockInfoResponseVo run() throws IOException {
        FormBody.Builder formBuilder = new FormBody.Builder();

        RequestBody formBody = formBuilder.build();

        String postURL = "";
        if(DeliveryApplication.nAccess == StateConsts.USER_CHELTENHAM)
            postURL = URLManager.getCheltenhamDownURL();
        else if(DeliveryApplication.nAccess == StateConsts.USER_WESSEX)
            postURL = URLManager.getWessexDownURL();
        String contentString = postPlain(postURL, formBody);

        DownloadStockInfoResponseVo responseVo = new DownloadStockInfoResponseVo();

        try {
            JSONObject json = new JSONObject(contentString);
            responseVo.staff = json.getString("staff");
            responseVo.issue = json.getString("issue");
            responseVo.warehouseList = json.getString("warehouseList");
            responseVo.bayList = json.getString("bayList");
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return responseVo;
    }
}
