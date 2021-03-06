package staff.com.task;

import android.os.AsyncTask;

import org.greenrobot.eventbus.EventBus;

import staff.com.event.DownloadStockInfoEvent;
import staff.com.proxy.DownloadStockInfoProxy;
import staff.com.vo.DownloadStockInfoResponseVo;

public class DownloadStockInfoTask extends AsyncTask<String, Void, DownloadStockInfoResponseVo> {

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected DownloadStockInfoResponseVo doInBackground(String... params) {
        DownloadStockInfoProxy simpleProxy = new DownloadStockInfoProxy();
        try {
            final DownloadStockInfoResponseVo responseVo = simpleProxy.run();

            return responseVo;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(DownloadStockInfoResponseVo responseVo) {
        EventBus.getDefault().post(new DownloadStockInfoEvent(responseVo));
    }
}