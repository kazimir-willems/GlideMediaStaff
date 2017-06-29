package staff.com.task;

import android.os.AsyncTask;

import org.greenrobot.eventbus.EventBus;

import staff.com.event.UploadModifiedStockEvent;
import staff.com.proxy.UploadModifiedStockProxy;
import staff.com.vo.UploadModifiedStockResponseVo;

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