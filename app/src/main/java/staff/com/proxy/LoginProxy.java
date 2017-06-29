package staff.com.proxy;

import com.google.gson.Gson;

import java.io.IOException;

import staff.com.util.URLManager;
import staff.com.vo.LoginRequestVo;
import staff.com.vo.LoginResponseVo;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class LoginProxy extends BaseProxy {

    public LoginResponseVo run(String data) throws IOException {
        LoginRequestVo requestVo = new LoginRequestVo();
        requestVo.data = data;
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add("data", requestVo.data);

        RequestBody formBody = formBuilder.build();

        String contentString = postPlain(URLManager.getLoginURL(), formBody);

        LoginResponseVo responseVo = new Gson().fromJson(contentString, LoginResponseVo.class);

        return responseVo;
    }
}
