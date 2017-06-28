package delivery.com.task;

import android.os.AsyncTask;

import org.greenrobot.eventbus.EventBus;

import delivery.com.event.UploadStockInfoEvent;
import delivery.com.proxy.UploadStockInfoProxy;
import delivery.com.vo.UploadStockInfoResponseVo;

public class UploadStockInfoTask extends AsyncTask<String, Void, UploadStockInfoResponseVo> {

    private String datas;

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected UploadStockInfoResponseVo doInBackground(String... params) {
        UploadStockInfoProxy simpleProxy = new UploadStockInfoProxy();
        datas = params[0];
        try {
            final UploadStockInfoResponseVo responseVo = simpleProxy.run(datas);

            return responseVo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(UploadStockInfoResponseVo responseVo) {
        EventBus.getDefault().post(new UploadStockInfoEvent(responseVo));
    }
}