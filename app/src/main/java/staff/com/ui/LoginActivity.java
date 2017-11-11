package staff.com.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import staff.com.R;
import staff.com.application.DeliveryApplication;
import staff.com.consts.StateConsts;
import staff.com.event.LoginEvent;
import staff.com.task.LoginTask;
import staff.com.util.StringUtil;
import staff.com.vo.LoginResponseVo;

public class LoginActivity extends AppCompatActivity {

    //defining views
    private ProgressDialog progressDialog;

    @BindView(R.id.edt_user_name)
    TextInputEditText edtUserName;
    @BindView(R.id.edt_password)
    TextInputEditText edtPassword;

    private Animation shake;

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        progressDialog = new ProgressDialog(this);
        shake = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.edittext_shake);

    }

    @Override
    public void onResume() {
        super.onResume();

        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onLoginEvent(LoginEvent event) {
        hideProgressDialog();
        LoginResponseVo responseVo = event.getResponse();
        if (responseVo != null) {
            if(responseVo.status.equals("success")) {
                String access = responseVo.access;
                if(access.equals("cheltenham")) {
                    DeliveryApplication.nAccess = StateConsts.USER_CHELTENHAM;
                    startHomeActivity();
                } else if(access.equals("wessex")) {
                    DeliveryApplication.nAccess = StateConsts.USER_WESSEX;
                    startHomeActivity();
                }
            } else {
                loginFailed();
            }
        } else {
            networkError();
        }
    }

    @OnClick(R.id.btn_signin)
    void onClickBtnSignIn() {
        username = edtUserName.getText().toString();
        password = edtPassword.getText().toString();

        if (!checkUserName()) return;
        if (!checkPassword()) return;

        startSignIn();
    }

    private boolean checkUserName() {
        if (StringUtil.isEmpty(username)) {
            showInfoNotice(edtUserName);
            return false;
        }

        return true;
    }

    private boolean checkPassword() {
        if (StringUtil.isEmpty(username)) {
            showInfoNotice(edtUserName);
            return false;
        }

        return true;
    }

    private void showInfoNotice(TextInputEditText target) {
        target.startAnimation(shake);
        if (target.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    //storing token to mysql server
    private void startSignIn() {

        if(username.equals("cheltenham") && password.equals("wordpass123")) {
            DeliveryApplication.nAccess = StateConsts.USER_CHELTENHAM;
            startHomeActivity();
        } else if (username.equals("wessex") && password.equals("wordpass123")) {
            DeliveryApplication.nAccess = StateConsts.USER_WESSEX;
            startHomeActivity();
        } else {
            progressDialog.setMessage(getResources().getString(R.string.signing_in));
            progressDialog.show();

            password = Base64.encodeToString(password.getBytes(), Base64.NO_WRAP);
            JSONObject json = new JSONObject();
            try {
                json.put("username", username);
                json.put("password", password);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }

            LoginTask task = new LoginTask();
            task.execute(json.toString());
        }
    }

    //start Home Activity
    private void startHomeActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void hideProgressDialog() {
        if(progressDialog.isShowing())
            progressDialog.dismiss();
    }

    private void networkError() {
        Toast.makeText(LoginActivity.this, getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
    }

    private void loginFailed() {
        Toast.makeText(LoginActivity.this, getResources().getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
    }
}