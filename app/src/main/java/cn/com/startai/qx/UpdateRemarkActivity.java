package cn.com.startai.qx;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import cn.com.startai.helper.TAndL;
import cn.com.startai.qx.utils.EventAction;
import cn.com.startai.qx.utils.EventBean;
import cn.com.startai.qxsdk.channel.mqtt.entity.UpdateRemark;
import cn.com.startai.qxsdk.db.bean.DeviceBean;


public class UpdateRemarkActivity extends BaseActivity {

    private EditText etRemark;
    private DeviceBean device;


    @Override
    protected int getLayout() {
        return R.layout.activity_update_remark;
    }

    @Override
    public void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.include2);
        toolbar.setTitle("修改备注名");
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

            case EventAction.ACTION_UPDATE_REMARK_RESULT:

                UpdateRemark.Resp resp = (UpdateRemark.Resp) eventBean.getObject1();

                onUpdateRemarkResult(resp);

                break;
        }
    }

    private void initData() {


        device = (DeviceBean) getIntent().getSerializableExtra("currDevice");


        if (device != null) {
            etRemark.setText(device.getRemark());
        }


    }

    private void initview() {

        etRemark = (EditText) findViewById(R.id.et_update_remark);

    }

    public void onUpdateRemarkResult(final UpdateRemark.Resp resp) {
        if (resp.getResult() == 1) {
            if (resp.getContent().getFid().equals(device.getSn())) {
                TAndL.TL(getApplicationContext(), "备注修改成功 ");

                etRemark.setText(resp.getContent().getRemark());
                finish();
            }
        } else {
            TAndL.TL(getApplicationContext(), "备注修改失败 " + resp.getContent().getErrmsg());

        }
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
            String remark = etRemark.getText().toString();

            UpdateRemark.Req req = new UpdateRemark.Req(device.getSn(), remark);

            getQXBusi().updateRemark(req, callListener);


            return true;

        }
        return super.onOptionsItemSelected(item);
    }


}

