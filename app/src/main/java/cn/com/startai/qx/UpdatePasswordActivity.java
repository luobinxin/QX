package cn.com.startai.qx;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import cn.com.startai.helper.TAndL;
import cn.com.startai.qx.utils.EventAction;
import cn.com.startai.qx.utils.EventBean;
import cn.com.startai.qxsdk.channel.mqtt.entity.UpdateLoginPwd;


/**
 * 修改密码
 */
public class UpdatePasswordActivity extends BaseActivity {

    private EditText etOld;
    private EditText etNew1;
    private EditText etNew;
    private int isHavePwd;
    private Toolbar toolbar;


    @Override
    protected int getLayout() {
        return R.layout.activity_update_password;
    }

    @Override
    public void init() {


        toolbar = (Toolbar) findViewById(R.id.include4);
        toolbar.setTitle("修改密码");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_48dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        etOld = findViewById(R.id.et_upldate_pwd_old);
        etNew1 = findViewById(R.id.et_update_pwd_new1);
        etNew = findViewById(R.id.et_update_pwd_new);
    }

    @Override
    protected void onResume() {
        super.onResume();

        isHavePwd = getIntent().getIntExtra("isHavePwd", 0);
        if (isHavePwd == 0) {
            etNew1.setVisibility(View.GONE);
            etNew.setVisibility(View.GONE);
            etOld.setHint("输入密码");
            toolbar.setTitle("设置登录密码");
        }

    }

    @Override
    public void onEvent(EventBean eventBean) {
        super.onEvent(eventBean);

        switch (action) {

            case EventAction.ACTION_UPDATE_USER_PWD_RESULT:

                UpdateLoginPwd.Resp resp = (UpdateLoginPwd.Resp) eventBean.getObject1();

                onUpdateUserPwdResult(resp);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.getItem(0).setTitle("保存");
        return true;
    }


    public void onUpdateUserPwdResult(UpdateLoginPwd.Resp resp) {

        if (resp.getResult() == 1) {
            TAndL.TL(getApplicationContext(), "密码修改成功 ");
            finish();
        } else {
            TAndL.TL(getApplicationContext(), "密码修改失败" + resp.getContent().getErrmsg());

        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            String pwdOld = etOld.getText().toString();
            String pwdNew1 = etNew.getText().toString();
            String pwdNew2 = etNew1.getText().toString();

            if (isHavePwd == 1) {
                if (!TextUtils.isEmpty(pwdNew1) && !TextUtils.isEmpty(pwdNew2) && !pwdNew1.equals(pwdNew2)) {
                    TAndL.TL(getApplicationContext(), "两次输入的新密码不一致");
                    return true;
                }
                //更新密码
                UpdateLoginPwd.Req req = new UpdateLoginPwd.Req(pwdOld, pwdNew1);
                getQXBusi().updateLoginPwd(req, callListener);

            } else {
                //设置登录密码
                UpdateLoginPwd.Req req = new UpdateLoginPwd.Req(pwdOld);
                getQXBusi().updateLoginPwd(req, callListener);

            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
