package cn.com.startai.qx;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import cn.com.startai.helper.TAndL;
import cn.com.startai.qx.utils.EventAction;
import cn.com.startai.qx.utils.EventBean;
import cn.com.startai.qxsdk.QX;
import cn.com.startai.qxsdk.channel.mqtt.entity.UpdateUserInfo;
import cn.com.startai.qxsdk.db.bean.UserBean;


/**
 * 修改昵称
 */
public class UpdateNickNameActivity extends BaseActivity {

    private EditText etNickName;


    @Override
    protected int getLayout() {
        return R.layout.activity_update_nick_name;
    }

    @Override
    public void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.include2);
        toolbar.setTitle("修改昵称");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_48dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initview();
        initData();
    }

    @Override
    public void onEvent(EventBean eventBean) {
        super.onEvent(eventBean);
        switch (action) {
            case EventAction.ACTION_UPDATE_USERINFO_RESULT:

                UpdateUserInfo.Resp resp = (UpdateUserInfo.Resp) eventBean.getObject1();

                onUpdateUserInfoResult(resp);
                break;
        }
    }

    private void initData() {
        UserBean userBean = QX.getInstance().getQxUserManager().getUser();
        if (userBean == null) {
            etNickName.setText("");
        } else {
            etNickName.setText(userBean.getNickName());

        }

    }

    private void initview() {

        etNickName = (EditText) findViewById(R.id.et_update_nickname);

    }

    public void onUpdateUserInfoResult(final UpdateUserInfo.Resp resp) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (resp.getResult() == resp.RESULT_SUCCESS) {
                    TAndL.TL(getApplicationContext(), "昵称修改成功 ");
                    finish();
                } else {
                    TAndL.TL(getApplicationContext(), "昵称修改失败 " + resp.getContent().getErrmsg());

                }
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.getItem(0).setTitle("保存");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            String nickName = etNickName.getText().toString();

            UpdateUserInfo.Req req = new UpdateUserInfo.Req();
            req.setNickName(nickName); //需要修改哪个字段就填写哪个属性
            getQXBusi().updateUserInfo(req, callListener);

            return true;

        }
        return super.onOptionsItemSelected(item);
    }


}

