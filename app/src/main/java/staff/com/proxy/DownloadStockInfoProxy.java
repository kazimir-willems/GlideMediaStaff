package staff.com.proxy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import staff.com.util.URLManager;
import staff.com.vo.DownloadStockInfoResponseVo;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class DownloadStockInfoProxy extends BaseProxy {

    public DownloadStockInfoResponseVo run() throws IOException {
        FormBody.Builder formBuilder = new FormBody.Builder();

        RequestBody formBody = formBuilder.build();

        String contentString = postPlain(URLManager.getDownloadStockInfoURL(), formBody);

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
