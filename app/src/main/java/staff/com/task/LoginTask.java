package staff.com.task;

import android.os.AsyncTask;

import org.greenrobot.eventbus.EventBus;

import staff.com.event.LoginEvent;
import staff.com.proxy.LoginProxy;
import staff.com.vo.LoginResponseVo;

public class LoginTask extends AsyncTask<String, Void, LoginResponseVo> {

    private String datas;

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected LoginResponseVo doInBackground(String... params) {
        LoginProxy simpleProxy = new LoginProxy();
        datas = params[0];
        try {
            final LoginResponseVo responseVo = simpleProxy.run(datas);

            return responseVo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(LoginResponseVo responseVo) {
        EventBus.getDefault().post(new LoginEvent(responseVo));
    }
}