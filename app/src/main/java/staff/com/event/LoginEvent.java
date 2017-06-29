package staff.com.event;

import staff.com.vo.LoginResponseVo;

public class LoginEvent {
    private LoginResponseVo responseVo;

    public LoginEvent(LoginResponseVo responseVo) {
        this.responseVo = responseVo;
    }

    public LoginResponseVo getResponse() {
        return responseVo;
    }
}
