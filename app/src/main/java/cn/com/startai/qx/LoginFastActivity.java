package cn.com.startai.qx;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.com.startai.helper.TAndL;
import cn.com.startai.helper.TimeCount;
import cn.com.startai.qx.utils.EventAction;
import cn.com.startai.qx.utils.EventBean;
import cn.com.startai.qxsdk.channel.mqtt.entity.GetIdentifyCode;
import cn.com.startai.qxsdk.channel.mqtt.entity.Login;


/**
 * 手机短信验证码快速登录
 */
public class LoginFastActivity extends BaseActivity implements View.OnClickListener {
    private EditText etMobile;
    private EditText etIdentify;
    private Button btLogin;
    private Button btGetIdentify;
    private TimeCount timeCount;


    @Override
    protected int getLayout() {
        return R.layout.activity_login2;
    }

    @Override
    public void init() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.include2);
        toolbar.setTitle("快捷登录");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_48dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initView();
        initListener();
    }

    @Override
    public void onEvent(EventBean eventBean) {
        super.onEvent(eventBean);

        switch (action) {
            case EventAction
                    .ACTION_LOGIN_RESULT:

                Login.Resp resp2 = (Login.Resp) eventBean.getObject1();
                onLoginResult(resp2);

                break;

            case EventAction.ACTION_GET_IDENTIFY_CODE_RESULT:

                GetIdentifyCode.Resp resp1 = (GetIdentifyCode.Resp) eventBean.getObject1();
                onGetIdentifyCodeResult(resp1);
                break;
        }

    }

    @Override
    public void onClick(View v) {

        String mobile = etMobile.getText().toString();
        int i = v.getId();
        if (i == R.id.bt_login2_login) {
            String identify = etIdentify.getText().toString();

            Login.Req req = new Login.Req(mobile, "", identify);

            getQXBusi().login(req, callListener);

        } else if (i == R.id.bt_login2_getIdentify) {
            timeCount.start();

            GetIdentifyCode.Req req = new GetIdentifyCode.Req(mobile, GetIdentifyCode.TYPE_FAST_LOGIN);
            getQXBusi().getIdentifyCode(req, callListener);
        }
    }

    private void initView() {

        etMobile = (EditText) findViewById(R.id.et_login2_mobile);
        etIdentify = (EditText) findViewById(R.id.et_login2_identify);
        btLogin = (Button) findViewById(R.id.bt_login2_login);
        btGetIdentify = (Button) findViewById(R.id.bt_login2_getIdentify);
        timeCount = new TimeCount(60 * 1000, 1000, btGetIdentify);
    }

    private void initListener() {

        btLogin.setOnClickListener(this);
        btGetIdentify.setOnClickListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.getItem(0).setTitle("密码登录");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void onGetIdentifyCodeResult(GetIdentifyCode.Resp resp) {

        if (resp.getResult() == resp.RESULT_SUCCESS) {
            TAndL.TL(getApplicationContext(), "获取验证码 成功 " + resp);
        } else {
            TAndL.TL(getApplicationContext(), "获取验证码 成功 " + resp.getContent().getErrmsg());
        }

    }


    public void onLoginResult(Login.Resp resp) {

        if (resp.getResult() == resp.RESULT_SUCCESS) {
            TAndL.TL(getApplicationContext(), "登录成功 " + resp);

            startActivity(new Intent(LoginFastActivity.this, HomeActivity.class));
            finish();
        } else {
            TAndL.TL(getApplicationContext(), "登录失败 " + resp.getContent().getErrmsg());
        }

    }


}

