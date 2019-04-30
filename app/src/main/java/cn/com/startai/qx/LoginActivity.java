package cn.com.startai.qx;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.com.startai.helper.TAndL;
import cn.com.startai.qx.utils.EventAction;
import cn.com.startai.qx.utils.EventBean;
import cn.com.startai.qxsdk.QX;
import cn.com.startai.qxsdk.channel.mqtt.entity.Login;


/**
 * 账号加密码登录
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText etUname;
    private EditText etPwd;
    private Button btLogin;
    private TextView tvForget;
    private TextView tvForeteEmail;
    private TextView tvRegister;
    private String TAG = LoginActivity.class.getSimpleName();
    private TextView tvProtocol;
    private TextView tvRegister2;
    private long t;

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.include);
        toolbar.setTitle("用户登录");
        setSupportActionBar(toolbar);


        initView();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {

        if (System.currentTimeMillis() - t < 2 * 1000) {
            QX.getInstance().release();
            finishAllActivity();
        } else {
            t = System.currentTimeMillis();
            TAndL.TL(getApplicationContext(), "再次点击返回键退出应用");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.fast_login) {
            startActivity(LoginFastActivity.class);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(EventBean eventBean) {

        super.onEvent(eventBean);

        String action = eventBean.getAction();

        switch (action) {
            case EventAction
                    .ACTION_LOGIN_RESULT:

                Login.Resp loginResult = (Login.Resp) eventBean.getObject1();

                if (loginResult.getResult() == loginResult.RESULT_SUCCESS) {
                    //登录成功
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();

                } else {
                    //登录失败
                    TAndL.TL(getApplicationContext(), loginResult.getContent().getErrmsg());
                }


                break;
        }


    }


    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.bt_login2_login) {
            String uname = etUname.getText().toString();
            String pwd = etPwd.getText().toString();

            Login.Req req = new Login.Req(uname, pwd, "");

            getQXBusi().login(req, callListener);

        } else if (i == R.id.tv_main_new_register) {
            startActivity(new Intent(LoginActivity.this, RegisterMobileActivity.class));

        } else if (i == R.id.tv_main_forget_pwd) {
            startActivity(new Intent(LoginActivity.this, ForgetPwdMobileActivity.class));

        } else if (i == R.id.tv_login_forget_email) {
            startActivity(new Intent(LoginActivity.this, ForgetPwdEmailActivity.class));

        } else if (i == R.id.tv_main_new_register2) {
            startActivity(new Intent(LoginActivity.this, RegisterEmailActivity.class));

        }
        String reg = "";
    }


    private void initView() {

        etUname = (EditText) findViewById(R.id.et_login2_mobile);
        etPwd = (EditText) findViewById(R.id.et_login2_identify);
        btLogin = (Button) findViewById(R.id.bt_login2_login);
        tvForget = (TextView) findViewById(R.id.tv_main_forget_pwd);
        tvForget.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvForeteEmail = (TextView) findViewById(R.id.tv_login_forget_email);
        tvForeteEmail.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvRegister = (TextView) findViewById(R.id.tv_main_new_register);
        tvRegister.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvRegister2 = (TextView) findViewById(R.id.tv_main_new_register2);
        tvRegister2.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        tvProtocol = (TextView) findViewById(R.id.tv_main_protocl);
        tvProtocol.setText("登录则表您同意遵守亓行智能wifi插座的");

    }

    String text1 = "用户协议";
    String and = "和";
    String text2 = "隐私协议";

    private void initListener() {

        btLogin.setOnClickListener(this);
        tvForget.setOnClickListener(this);
        tvForeteEmail.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        tvRegister2.setOnClickListener(this);

        SpannableString spStr1 = new SpannableString(text1);

        spStr1.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#99cc33"));
                ds.setUnderlineText(true);
            }

            @Override
            public void onClick(View widget) {
                TAndL.TL(getApplicationContext(), text1);
            }
        }, 0, text1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvProtocol.append(spStr1);
        tvProtocol.append(and);
        SpannableString spStr2 = new SpannableString(text2);
        spStr2.setSpan(new ClickableSpan() {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#99cc33"));
                ds.setUnderlineText(true);
            }

            @Override
            public void onClick(View widget) {
                TAndL.TL(getApplicationContext(), text2);

            }
        }, 0, text2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvProtocol.append(spStr2);
        tvProtocol.setMovementMethod(LinkMovementMethod.getInstance());

    }


}
