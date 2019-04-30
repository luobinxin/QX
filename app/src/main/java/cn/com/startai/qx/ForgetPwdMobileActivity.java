package cn.com.startai.qx;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.com.startai.helper.TAndL;
import cn.com.startai.qx.utils.EventAction;
import cn.com.startai.qx.utils.EventBean;
import cn.com.startai.qxsdk.channel.mqtt.entity.CheckIdentifyCode;
import cn.com.startai.qxsdk.channel.mqtt.entity.GetIdentifyCode;
import cn.com.startai.qxsdk.channel.mqtt.entity.ResetLoginPwd;


public class ForgetPwdMobileActivity extends BaseActivity {

    private EditText etMobile;
    private EditText etCode;
    private EditText etNewPwd;
    private Button btGetCode;
    private Button btOk;
    private Button btCheckCode;


    @Override
    protected int getLayout() {
        return R.layout.activity_forget;
    }

    @Override
    public void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.include2);
        toolbar.setTitle("手机号重置密码");
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
            case EventAction.ACTION_RESET_LOGIN_PWE_RESULT:

                ResetLoginPwd.Resp resp1 = (ResetLoginPwd.Resp) eventBean.getObject1();

                onResetMobileLoginPwdResult(resp1);
                break;
            case EventAction.ACTION_CHECK_IDENTIFY_CODE_RESULT:

                CheckIdentifyCode.Resp resp2 = (CheckIdentifyCode.Resp) eventBean.getObject1();

                onCheckIdetifyResult(resp2);
                break;
            case EventAction.ACTION_GET_IDENTIFY_CODE_RESULT:
                GetIdentifyCode.Resp resp3 = (GetIdentifyCode.Resp) eventBean.getObject1();

                onGetIdentifyCodeResult(resp3);
                break;
        }
    }

    private void initListener() {

        btGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initTimeCount(btGetCode);
                startTimeCount();

                String mobile = etMobile.getText().toString();
                GetIdentifyCode.Req req = new GetIdentifyCode.Req(mobile, GetIdentifyCode.TYPE_RESET_PWD);


                getQXBusi().getIdentifyCode(req, callListener);

            }
        });

        btCheckCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobile = etMobile.getText().toString();
                String code = etCode.getText().toString();

                CheckIdentifyCode.Req req = new CheckIdentifyCode.Req(mobile, code, CheckIdentifyCode.TYPE_MOBILE_RESET_PWD);

                getQXBusi().checkIdentifyCode(req, callListener);


            }
        });

        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String mobile = etMobile.getText().toString();
                String pwd = etNewPwd.getText().toString();
                ResetLoginPwd.Req req = new ResetLoginPwd.Req(mobile, pwd);
                getQXBusi().resetLoginPwd(req, callListener);

            }
        });

    }

    private void initView() {

        etMobile = findViewById(R.id.et_forget_email_email);
        etCode = findViewById(R.id.et_forget_code);
        etNewPwd = findViewById(R.id.et_forget_new_pwd);

        btGetCode = findViewById(R.id.bt_forget_getcode);
        btOk = findViewById(R.id.bt_forget_ok);
        btCheckCode = findViewById(R.id.bt_forget_checkcode);
btOk.setEnabled(false);
    }

    public void onGetIdentifyCodeResult(GetIdentifyCode.Resp resp) {

        if (resp.getResult() == resp.RESULT_SUCCESS) {
            //获取验证码成功
            TAndL.TL(getApplicationContext(), "获取验证码结果 " + resp);
        } else {
            //获取验证失败
            TAndL.TL(getApplicationContext(), "获取验证码结果 " + resp.getContent().getErrmsg());

        }
    }


    public void onCheckIdetifyResult(CheckIdentifyCode.Resp resp) {
        if (resp.getResult() == resp.RESULT_SUCCESS) {
            TAndL.TL(getApplicationContext(), "校验验证码成功  = " + resp);
            btOk.setEnabled(true);
            //校验验证码成功
        } else {
            //校验验证码失败
            TAndL.TL(getApplicationContext(), "校验验证码失败  = " + resp.getContent().getErrmsg());

        }
    }


    public void onResetMobileLoginPwdResult(ResetLoginPwd.Resp resp) {

        if (resp.getResult() == 1) {
            TAndL.TL(getApplicationContext(), "重置密码成功 " + resp);
            finish();
        } else {
            TAndL.TL(getApplicationContext(), "重置密码失败 " + resp.getContent().getErrmsg());
            btOk.setEnabled(false);
        }
    }

}
