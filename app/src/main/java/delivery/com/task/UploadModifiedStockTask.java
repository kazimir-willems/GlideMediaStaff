package delivery.com.task;

import android.os.AsyncTask;

import org.greenrobot.eventbus.EventBus;

import delivery.com.event.UploadModifiedStockEvent;
import delivery.com.event.UploadStockInfoEvent;
import delivery.com.proxy.UploadModifiedStockProxy;
import delivery.com.proxy.UploadStockInfoProxy;
import delivery.com.vo.UploadModifiedStockResponseVo;
import delivery.com.vo.UploadStockInfoResponseVo;

public class UploadModifiedStockTask extends AsyncTask<String, Void, UploadModifiedStockResponseVo> {

    private String datas;

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected UploadModifiedStockResponseVo doInBackground(String... params) {
        UploadModifiedStockProxy simpleProxy = new UploadModifiedStockProxy();
        datas = params[0];
        try {
            final UploadModifiedStockResponseVo responseVo = simpleProxy.run(datas);

            return responseVo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(UploadModifiedStockResponseVo responseVo) {
        EventBus.getDefault().post(new UploadModifiedStockEvent(responseVo));
    }
}