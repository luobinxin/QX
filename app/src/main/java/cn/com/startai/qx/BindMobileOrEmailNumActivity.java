package cn.com.startai.qx;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.com.startai.helper.TAndL;
import cn.com.startai.qx.utils.EventAction;
import cn.com.startai.qx.utils.EventBean;
import cn.com.startai.qxsdk.channel.mqtt.entity.BindEmail;
import cn.com.startai.qxsdk.channel.mqtt.entity.BindMobile;
import cn.com.startai.qxsdk.channel.mqtt.entity.CheckIdentifyCode;
import cn.com.startai.qxsdk.channel.mqtt.entity.GetIdentifyCode;
import cn.com.startai.qxsdk.channel.mqtt.entity.SendEmail;
import cn.com.startai.qxsdk.channel.mqtt.BaseMessage;

public class BindMobileOrEmailNumActivity extends BaseActivity {

    @BindView(R.id.include2)
    Toolbar toolbar;
    @BindView(R.id.et_new_account)
    EditText etNewMobile;
    @BindView(R.id.bt_get_code)
    Button btGetCode;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.bt_next)
    Button btNext;
    @BindView(R.id.clt_1)
    ConstraintLayout clt1;
    @BindView(R.id.tv_account)
    TextView tvMobileNum;
    @BindView(R.id.bt_Bind)
    Button btBind;
    @BindView(R.id.clt_2)
    ConstraintLayout clt2;

    private String account;
    private boolean isMobile;

    @BindView(R.id.tv_mobile_num_tag)
    TextView tvMobileNumTag;
    private int uiIndex;
    private Unbinder bind;


    @Override
    protected int getLayout() {
        return R.layout.activity_update_mobile_or_email_num;
    }

    @Override
    public void onEvent(EventBean eventBean) {
        super.onEvent(eventBean);
        switch (action) {
            case EventAction
                    .ACTION_BIND_MOBILE_RESULT:
                BindMobile.Resp resp1 = (BindMobile.Resp) eventBean.getObject1();
                onBindMobileResult(resp1);
                break;
            case EventAction.ACTION_BIND_EMAIL_RESULT:
                BindEmail.Resp resp2 = (BindEmail.Resp) eventBean.getObject1();
                onBindEmailResult(resp2);
                break;
            case EventAction.ACTION_GET_IDENTIFY_CODE_RESULT:

                GetIdentifyCode.Resp resp3 = (GetIdentifyCode.Resp) eventBean.getObject1();
                onGetIdentifyCodeResult(resp3);

                break;
            case EventAction.ACTION_CHECK_IDENTIFY_CODE_RESULT:
                CheckIdentifyCode.Resp resp4 = (CheckIdentifyCode.Resp) eventBean.getObject1();
                onCheckIdentifyCodeResult(resp4);
                break;
            case EventAction.ACTION_SEND_EMAIL_RESULT:

                SendEmail.Resp resp5 = (SendEmail.Resp) eventBean.getObject1();
                onSendEmailResult(resp5);

                break;
        }

    }

    private void onGetIdentifyCodeResult(GetIdentifyCode.Resp resp) {
        if (resp.getResult() == BaseMessage.RESULT_SUCCESS) {
            TAndL.TL(getApplicationContext(), "获取验证码成功");
        } else {
            TAndL.TL(getApplicationContext(), "获取验证码失败 " + resp.getContent().getErrmsg());
        }
    }

    private void onCheckIdentifyCodeResult(CheckIdentifyCode.Resp resp) {
        if (resp.getResult() == BaseMessage.RESULT_SUCCESS) {
            TAndL.TL(getApplicationContext(), "校验验证码成功");
            showUI_2();
        } else {
            TAndL.TL(getApplicationContext(), "校验验证码失败 " + resp.getContent().getErrmsg());
        }
    }

    private void onSendEmailResult(SendEmail.Resp resp) {
        if (resp.getResult() == BaseMessage.RESULT_SUCCESS) {
            TAndL.TL(getApplicationContext(), "发送邮件成功");
        } else {
            TAndL.TL(getApplicationContext(), "发送邮件失败 " + resp.getContent().getErrmsg());
        }
    }

    private void onBindEmailResult(BindEmail.Resp resp) {
        if (resp.getResult() == BaseMessage.RESULT_SUCCESS) {
            TAndL.TL(getApplicationContext(), "绑定邮箱成功");
            finish();
        } else {
            TAndL.TL(getApplicationContext(), "绑定邮箱失败 " + resp.getContent().getErrmsg());
        }
    }

    private void onBindMobileResult(BindMobile.Resp resp) {

        if (resp.getResult() == BaseMessage.RESULT_SUCCESS) {
            TAndL.TL(getApplicationContext(), "绑定手机号成功");
            finish();
        } else {
            TAndL.TL(getApplicationContext(), "绑定手机号失败 " + resp.getContent().getErrmsg());
        }
    }

    @Override
    public void init() {
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String tag = "绑定邮箱号";
        etNewMobile.setHint("新邮箱号");
        tvMobileNumTag.setText("邮箱号");
        if (intent != null) {
            isMobile = intent.getBooleanExtra("isMobile", false);
            if (isMobile) {
                tag = "绑定手机号";
                etNewMobile.setHint("新手机号");
                tvMobileNumTag.setText("手机号");
            }
        }

        toolbar.setTitle(tag);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_48dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        showUI_1();
    }

    @OnClick({R.id.bt_get_code, R.id.bt_next, R.id.bt_Bind})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_get_code:

                initTimeCount(btGetCode);
                timeCount.start();

                account = etNewMobile.getText().toString();
                if (isMobile) {

                    GetIdentifyCode.Req req = new GetIdentifyCode.Req(account, GetIdentifyCode.TYPE_BIND_MOBILENUM);
                    getQXBusi().getIdentifyCode(req, callListener);

                } else {

                    SendEmail.Req req = new SendEmail.Req(account, SendEmail.TYPE_CODE_TO_BIND_EMAIL);
                    getQXBusi().sendEmail(req, callListener);
                }

                break;
            case R.id.bt_next:

                String identifyCode = etCode.getText().toString();
                if (isMobile) {

                    CheckIdentifyCode.Req req = new CheckIdentifyCode.Req(account, identifyCode, CheckIdentifyCode.TYPE_MOBILE_BIND_MOBILENUM);

                    getQXBusi().checkIdentifyCode(req, callListener);
                } else {
                    CheckIdentifyCode.Req req = new CheckIdentifyCode.Req(account, identifyCode, CheckIdentifyCode.TYPE_EMAIL_BIND_EMAILNUM);
                    getQXBusi().checkIdentifyCode(req, callListener);

                }

                break;
            case R.id.bt_Bind:


                if (isMobile) {
                    BindMobile.Req req = new BindMobile.Req(account);
                    getQXBusi().bindMobileNum(req, callListener);
                } else {
                    BindEmail.Req req = new BindEmail.Req(account);
                    getQXBusi().bindEmail(req, callListener);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (uiIndex == 2) {
            showUI_1();
        } else {
            super.onBackPressed();
        }
    }

    public void showUI_1() {
        uiIndex = 1;
        clt1.setVisibility(View.VISIBLE);
        clt2.setVisibility(View.INVISIBLE);
    }

    public void showUI_2() {
        uiIndex = 2;
        clt1.setVisibility(View.INVISIBLE);
        clt2.setVisibility(View.VISIBLE);
        tvMobileNum.setText(account);
    }


}
