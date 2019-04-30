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
import cn.com.startai.qxsdk.channel.mqtt.entity.Register;


public class RegisterMobileActivity extends BaseActivity implements View.OnClickListener {

    private EditText etMobile;
    private EditText etIdentify;
    private EditText etPwd;
    private Button btCheckIdentify;
    private Button btGetIdentify;
    private Button btRegister;
    private String TAG = RegisterMobileActivity.class.getSimpleName();


    @Override
    protected int getLayout() {
        return R.layout.activity_regist;
    }

    @Override
    public void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.include2);
        toolbar.setTitle("手机号注册");
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

    private void initView() {

        etMobile = (EditText) findViewById(R.id.et_register_mobile);
        etIdentify = (EditText) findViewById(R.id.et_register_identify);
        etPwd = (EditText) findViewById(R.id.et_register_pwd);
        btGetIdentify = (Button) findViewById(R.id.bt_register_getidentify);
        btCheckIdentify = (Button) findViewById(R.id.bt_register_check_identify);
        btRegister = (Button) findViewById(R.id.bt_register_register);
        btRegister.setEnabled(false);
    }


    @Override
    public void onEvent(EventBean eventBean) {
        super.onEvent(eventBean);

        switch (action) {

            case EventAction.ACTION_GET_IDENTIFY_CODE_RESULT:

                GetIdentifyCode.Resp resp1 = (GetIdentifyCode.Resp) eventBean.getObject1();

                onGetIdentifyCodeResult(resp1);

                break;

            case EventAction.ACTION_CHECK_IDENTIFY_CODE_RESULT:


                CheckIdentifyCode.Resp resp2 = (CheckIdentifyCode.Resp) eventBean.getObject1();
                onCheckIdetifyResult(resp2);

                break;

            case EventAction.ACTION_REGISTER_RESULT:
                Register.Resp resp3 = (Register.Resp) eventBean.getObject1();
                onRegisterResult(resp3);
                break;


        }


    }

    private void initListener() {


        btCheckIdentify.setOnClickListener(this);
        btGetIdentify.setOnClickListener(this);
        btRegister.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        String mobile = etMobile.getText().toString();

        int i = v.getId();
        if (i == R.id.bt_register_getidentify) {
            initTimeCount(btGetIdentify);
            startTimeCount();

            GetIdentifyCode.Req req = new GetIdentifyCode.Req(mobile, GetIdentifyCode.TYPE_REGISTER);


            getQXBusi().getIdentifyCode(req, callListener);

        } else if (i == R.id.bt_register_register) {
            String pwd = etPwd.getText().toString();

            Register.Req req = new Register.Req(mobile, pwd, Register.TYPE_MOBILE_AFTER_CHECK_CODE);

            getQXBusi().register(req, callListener);


        } else if (i == R.id.bt_register_check_identify) {
            String identify = etIdentify.getText().toString();

            CheckIdentifyCode.Req req = new CheckIdentifyCode.Req(mobile, identify, CheckIdentifyCode.TYPE_MOBILE_REGISTER);

            getQXBusi().checkIdentifyCode(req, callListener);


        }


    }


    public void onRegisterResult(Register.Resp resp) {
        if (resp.getResult() == resp.RESULT_SUCCESS) {
            TAndL.TL(getApplicationContext(), "注册成功  " + resp);
            //注册成功
            finish();
        } else {
            //注册失败
            TAndL.TL(getApplicationContext(), "注册失败  " + resp.getContent().getErrmsg());
            btRegister.setEnabled(false);

        }
    }


    public void onGetIdentifyCodeResult(GetIdentifyCode.Resp resp) {
        if (resp.getResult() == resp.RESULT_SUCCESS) {
            TAndL.TL(getApplicationContext(), "获取验证码成功  " + resp);
            //获取验证码成功
        } else {
            //获取验证码失败
            TAndL.TL(getApplicationContext(), "获取验证码失败  " + resp.getContent().getErrmsg());
        }
    }


    public void onCheckIdetifyResult(CheckIdentifyCode.Resp resp) {
        if (resp.getResult() == resp.RESULT_SUCCESS) {

            TAndL.TL(getApplicationContext(), "校验验证码成功   " + resp);
            btRegister.setEnabled(true);
        } else {

            TAndL.TL(getApplicationContext(), "校验验证码失败 = " + resp.getContent().getErrmsg());
        }
    }


}
