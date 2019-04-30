package cn.com.startai.qx;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.com.startai.helper.TAndL;
import cn.com.startai.qx.utils.EventAction;
import cn.com.startai.qx.utils.EventBean;
import cn.com.startai.qxsdk.channel.mqtt.entity.CheckIdentifyCode;
import cn.com.startai.qxsdk.channel.mqtt.entity.Register;
import cn.com.startai.qxsdk.channel.mqtt.entity.SendEmail;

public class RegisterEmailActivity extends BaseActivity {

    private EditText etEmail;
    private EditText etCode;
    private EditText etPwd;

    private Button btRegister;
    private Button btGetCode;
    private Button btCheck;

    @Override
    protected int getLayout() {
        return R.layout.activity_register_email;
    }

    @Override
    public void init() {


        Toolbar toolbar = (Toolbar) findViewById(R.id.include3);
        toolbar.setTitle("邮箱注册");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_48dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initview();
        initListener();


    }

    @Override
    public void onEvent(EventBean eventBean) {
        super.onEvent(eventBean);

        switch (action) {

            case EventAction.ACTION_SEND_EMAIL_RESULT:

                SendEmail.Resp resp1 = (SendEmail.Resp) eventBean.getObject1();

                onSendEmailResult(resp1);

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


    public void onRegisterResult(Register.Resp resp) {
        if (resp.getResult() == resp.RESULT_SUCCESS) {

            TAndL.TL(getApplicationContext(), "注册成功  ");
            finish();
        } else {
            TAndL.TL(getApplicationContext(), "注册失败  " + resp.getContent().getErrmsg());
            btRegister.setEnabled(false);
        }
    }


    public void onSendEmailResult(SendEmail.Resp resp) {

        if (resp.getResult() == 1) {
            TAndL.TL(getApplicationContext(), "邮件发送成功 " + resp);
        } else {

            TAndL.TL(getApplicationContext(), "邮件发送失败 " + resp.getContent().getErrmsg());
        }

    }


    public void onCheckIdetifyResult(CheckIdentifyCode.Resp resp) {
        if (resp.getResult() == 1) {
            TAndL.TL(getApplicationContext(), "验证码校验成功");
            btRegister.setEnabled(true);
        } else {
            TAndL.TL(getApplicationContext(), "验证码校验失败 " + resp.getContent().getErrmsg());
        }
    }

    private void initListener() {

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uname = etEmail.getText().toString();
                String pwd = etPwd.getText().toString();

                Register.Req req = new Register.Req(uname, pwd, Register.TYPE_EMAIL_AFTER_CHECK_CODE);

                getQXBusi().register(req, callListener);


            }
        });

        btGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initTimeCount(btGetCode);
                startTimeCount();

                String email = etEmail.getText().toString();

                SendEmail.Req req = new SendEmail.Req(email, SendEmail.TYPE_CODE_TO_REGISTER);

                getQXBusi().sendEmail(req, callListener);

            }
        });

        btCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String code = etCode.getText().toString();
                CheckIdentifyCode.Req req = new CheckIdentifyCode.Req(email, code, CheckIdentifyCode.TYPE_EMAIL_REGISTER);
                getQXBusi().checkIdentifyCode(req, callListener);
            }
        });
    }

    private void initview() {

        etEmail = (EditText) findViewById(R.id.et_register_email_email);
        etCode = (EditText) findViewById(R.id.et_register_email_code);
        etPwd = (EditText) findViewById(R.id.et_register_email_pwd);
        btRegister = (Button) findViewById(R.id.bt_register_email_register);
        btGetCode = (Button) findViewById(R.id.bt_register_email_getcode);
        btCheck = (Button) findViewById(R.id.bt_register_email_check);
        btRegister.setEnabled(false);
    }


}
