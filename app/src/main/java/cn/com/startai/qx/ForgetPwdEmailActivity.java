package cn.com.startai.qx;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.com.startai.helper.TAndL;
import cn.com.startai.qx.utils.EventAction;
import cn.com.startai.qx.utils.EventBean;
import cn.com.startai.qxsdk.channel.mqtt.entity.CheckIdentifyCode;
import cn.com.startai.qxsdk.channel.mqtt.entity.ResetLoginPwd;
import cn.com.startai.qxsdk.channel.mqtt.entity.SendEmail;


public class ForgetPwdEmailActivity extends BaseActivity {

    private EditText etEmail;
    private Button btNext;
    private Button btGetCode;
    private EditText etCode;
    private EditText etNewPwd;
    private Button btOk;


    @Override
    protected int getLayout() {
        return R.layout.activity_forget_email;
    }

    @Override
    public void init() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.include2);
        toolbar.setTitle("邮箱号重置 密码");
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
                    .ACTION_SEND_EMAIL_RESULT:

                SendEmail.Resp resp1 = (SendEmail.Resp) eventBean.getObject1();

                onSendEmailResult(resp1);
                break;
            case EventAction.ACTION_CHECK_IDENTIFY_CODE_RESULT:

                CheckIdentifyCode.Resp resp2 = (CheckIdentifyCode.Resp) eventBean.getObject1();

                onCheckIdentifyCodeResult(resp2);
                break;
            case EventAction.ACTION_RESET_LOGIN_PWE_RESULT:

                ResetLoginPwd.Resp resp3 = (ResetLoginPwd.Resp) eventBean.getObject1();
                onResetLoginPwdResult(resp3);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_forget_pwd_email, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.link) {

            String str = etEmail.getText().toString();
            if (!TextUtils.isEmpty(str)) {

                SendEmail.Req req = new SendEmail.Req(str, SendEmail.TYPE_LINK_TO_RESET_PWD);
                getQXBusi().sendEmail(req, callListener);

            } else {
                TAndL.TL(getApplicationContext(), "邮箱不能为空");
            }


        }
        return super.onOptionsItemSelected(item);
    }

    private void onCheckIdentifyCodeResult(CheckIdentifyCode.Resp resp) {
        if (resp.getResult() == resp.RESULT_SUCCESS) {
            TAndL.TL(getApplicationContext(), "校验邮箱验证码成功" + resp);
            btOk.setEnabled(true);
        } else {
            TAndL.TL(getApplicationContext(), "校验邮箱验证码失败" + resp.getContent().getErrmsg());
        }
    }

    private void onResetLoginPwdResult(ResetLoginPwd.Resp resp) {
        if (resp.getResult() == resp.RESULT_SUCCESS) {
            TAndL.TL(getApplicationContext(), "重置密码成功 " + resp);
            finish();
        } else {
            TAndL.TL(getApplicationContext(), "重置密码失败" + resp.getContent().getErrmsg());
            btOk.setEnabled(false);
        }
    }

    public void onSendEmailResult(SendEmail.Resp resp) {

        if (resp.getResult() == resp.RESULT_SUCCESS) {
            TAndL.TL(getApplicationContext(), "邮件发送成功 " + resp);
        } else {

            TAndL.TL(getApplicationContext(), "邮件发送失败 " + resp.getContent().getErrmsg());
        }

    }


    private void initListener() {

        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = etEmail.getText().toString();
                String newPwd = etNewPwd.getText().toString();

                ResetLoginPwd.Req req = new ResetLoginPwd.Req(email, newPwd);
                getQXBusi().resetLoginPwd(req, callListener);

            }
        });

        btGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initTimeCount(btGetCode);
                startTimeCount();

                String s = etEmail.getText().toString();
                SendEmail.Req req = new SendEmail.Req(s, SendEmail.TYPE_CODE_TO_RESET_PWD);
                getQXBusi().sendEmail(req, callListener);

            }
        });

        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = etEmail.getText().toString();
                String code = etCode.getText().toString();
                CheckIdentifyCode.Req req = new CheckIdentifyCode.Req(email, code, CheckIdentifyCode.TYPE_EMAIL_RESET_PWD);

                getQXBusi().checkIdentifyCode(req, callListener);


            }
        });

    }

    private void initView() {

        etEmail = findViewById(R.id.et_forget_email_email);
        btNext = findViewById(R.id.bt_forget_email_next);
        btGetCode = findViewById(R.id.bt_forget_email_getcode);
        etCode = findViewById(R.id.et_forget_email_code);
        etNewPwd = findViewById(R.id.et_forget_email_newpwd);
        btOk = findViewById(R.id.bt_forget_email_ok);
        btOk.setEnabled(false);

    }
}
