package cn.com.startai.qx;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import cn.com.startai.helper.TAndL;
import cn.com.startai.qx.utils.EventAction;
import cn.com.startai.qx.utils.EventBean;
import cn.com.startai.qxsdk.busi.common.BaseResp;
import cn.com.startai.qxsdk.busi.socket.SocketBusiManager;
import cn.com.startai.qxsdk.busi.socket.entity.RenameDeviceReq;
import cn.com.startai.qxsdk.busi.socket.entity.RenameDeviceResp;
import cn.com.startai.qxsdk.channel.mqtt.entity.UpdateUserInfo;
import cn.com.startai.qxsdk.db.bean.DeviceBean;
import cn.com.startai.qxsdk.db.bean.UserBean;

public class UpdateLanDeviceNameActivity extends BaseActivity {

    private EditText etNewName;
    private DeviceBean currDevice;
    private UserBean userBean;

    @Override
    protected int getLayout() {
        return R.layout.activity_update_nick_name;
    }

    @Override
    public void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.include2);
        toolbar.setTitle("修改名称(局域网)");
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
            case EventAction.ACTION_RENAME_RESULT:
                RenameDeviceResp resp = (RenameDeviceResp) eventBean.getObject1();
                onLanDeviceRenameResult(resp);

                break;
        }

    }

    private void initData() {

        currDevice = (DeviceBean) getIntent().getSerializableExtra("currDevice");


        if (currDevice == null) {

            etNewName.setText("");
        } else {
            etNewName.setText(currDevice.getName());

        }

    }

    private void initview() {

        etNewName = (EditText) findViewById(R.id.et_update_nickname);

    }

    public void onUpdateUserInfoResult(final UpdateUserInfo.Resp resp) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (resp.getResult() == resp.RESULT_SUCCESS) {
                    TAndL.TL(getApplicationContext(), "昵称修改成功 ");
                    userBean.setNickName(resp.getContent().getNickName());
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
            if (currDevice != null) {
                String newName = etNewName.getText().toString();
                RenameDeviceReq req = new RenameDeviceReq(currDevice, newName);
                SocketBusiManager.getInstance().renameDevice(req, callListener);
            }


            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void onLanDeviceRenameResult(RenameDeviceResp resp) {

        if (resp.getResult() == BaseResp.RESULT_SUCCESS) {
            TAndL.TL(getApplicationContext(), "修改名称（局域网）成功");
            finish();
        } else {
            TAndL.TL(getApplicationContext(), "修改名称（局域网）失败");
        }

    }
}

