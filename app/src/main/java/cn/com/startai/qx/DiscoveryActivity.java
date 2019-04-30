package cn.com.startai.qx;

import android.content.DialogInterface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;

import cn.com.startai.helper.TAndL;
import cn.com.startai.qx.adapter.UpdateAdapter;
import cn.com.startai.qx.utils.EventAction;
import cn.com.startai.qx.utils.EventBean;
import cn.com.startai.qx.utils.SLog;
import cn.com.startai.qxsdk.busi.common.BindDeviceReq;
import cn.com.startai.qxsdk.busi.common.BindDeviceResp;
import cn.com.startai.qxsdk.db.bean.DeviceBean;


public class DiscoveryActivity extends BaseActivity {


    private RecyclerView rv;
    private ArrayList<DeviceBean> datas;
    private UpdateAdapter adapter;
    private SwipeRefreshLayout srl;
    private Toolbar toolbar;

    @Override
    protected int getLayout() {
        return R.layout.activity_lan_device_update;
    }

    @Override
    public void init() {
        toolbar = (Toolbar) findViewById(R.id.include);
        toolbar.setTitle("局域网设备");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_48dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initView();
        initAdapter();
        initListener();

    }

    private void updateTag(final boolean isScan) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                toolbar.setTitle(isScan ? "扫描中..." : "局域网设备");
            }
        });
    }

    @Override
    public void onEvent(EventBean eventBean) {
        super.onEvent(eventBean);
        String action = eventBean.getAction();
        switch (action) {
            case EventAction
                    .ACTION_DISCOVERY_START:
                updateTag(true);

                break;
            case EventAction
                    .ACTION_DISCOVERY_STOP:
                updateTag(false);

                break;
            case EventAction
                    .ACTION_DISCOVERY_RESULT:


                final DeviceBean deviceBean = (DeviceBean) eventBean.getObject1();

                runOnUiThread(new Runnable() {
                    @Override
                    public synchronized void run() {


                        if (datas.size() == 0) {
                            datas.add(deviceBean);
                            adapter.setNewData(datas);
                        } else {
                            boolean isContains = false;
                            for (DeviceBean data : datas) {

                                if (data.getMac().equals(deviceBean.getMac())) {
                                    isContains = true;
                                    break;
                                }

                            }
                            if (!isContains) {
                                datas.add(deviceBean);
                                adapter.setNewData(datas);
                            }
                        }



                    }
                });

                break;

            case EventAction
                    .ACTION_BIND_RESULT:

                BindDeviceResp bindDeviceResp = (BindDeviceResp) eventBean.getObject1();
                onLanBindDeviceResult(bindDeviceResp.getResult(), bindDeviceResp.getDeviceBean());

                break;

        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        startFindLanDevice();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_update, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_action_reset) {

            createResetDialog();


        } else if (id == R.id.menu_action_update) {
            createUpdateDialog();
        }
        return super.onOptionsItemSelected(item);
    }


    private void createResetDialog() {

        new AlertDialog.Builder(DiscoveryActivity.this)
                .setTitle("提示")
                .setMessage("是否初始化局域网内的所有设备")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reset();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();

    }

    private void reset() {


        new Thread() {
            @Override
            public void run() {
                super.run();
//                Lan.getInstance().recoveryLanDevice("");
                TAndL.TL(getApplicationContext(), "重置包已经发送");
            }
        }.start();
    }

    private void update() {
        new Thread() {
            @Override
            public void run() {
                super.run();

//                Lan.getInstance().updateForceLanDevice("");
                TAndL.TL(getApplicationContext(), "升级包已经发送");

            }
        }.start();

    }

    private void createUpdateDialog() {

        new AlertDialog.Builder(DiscoveryActivity.this)
                .setTitle("提示")
                .setMessage("是否强制升级局域网内所有设备")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        update();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();

    }


    void initAdapter() {

        datas = new ArrayList<>();
        //创建布局管理
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);

        //创建适配器
        adapter = new UpdateAdapter(R.layout.item_update, datas);
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);

        //给RecyclerView设置适配器
        rv.setAdapter(adapter);


    }

    private void initListener() {


        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                final DeviceBean device = (DeviceBean) adapter.getItem(position);

                new AlertDialog.Builder(DiscoveryActivity.this)
                        .setTitle("提示")
                        .setMessage(device + "")
                        .setPositiveButton("绑定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                String bindPwd = "";
                                if (device.isBindNeedPwd()) {
                                    bindPwd = "123456";
                                }

                                BindDeviceReq req = new BindDeviceReq(device.getMac(), bindPwd);
                                getQXBusi().bindDevice(req, callListener);

                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();

            }


        });


        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                //禁用 adapter 的上拉加载
//                adapter.setEnableLoadMore(false);

                SLog.d(TAG, "下拉刷新 onRefresh ");
                startFindLanDevice();

                srl.setRefreshing(false);

            }
        });
    }


    public void onLanBindDeviceResult(int result, DeviceBean deviceBean) {

        TAndL.TL(getApplicationContext(), "绑定" + ((result == 1) ? "成功" : "失败") + deviceBean);
        if (result == 1) {
            finish();
        }
    }

    private void stopFindLanDevice() {

        getQXBusi().stopDiscovery();

    }


    private void startFindLanDevice() {

        datas.clear();
        adapter.setNewData(datas);

        getQXBusi().discovery(30 * 1000);

    }


    public void onRecoveryLanDeviceResult(boolean result, String mac) {

        TAndL.TL(getApplicationContext(), "mac = " + mac + "\n已经恢复出厂设置");

    }

//    @Override
//    public void onLanDeviceUpdateResult(boolean result, UpdateVersionReq version) {
//        super.onLanDeviceUpdateResult(result, version);
//
//        TAndL.TL(getApplicationContext(), version.mac + " 升级成功 " + version.curVersionStr + "==>" + version.newVersionStr);
//
//    }


    private void initView() {

        rv = findViewById(R.id.rv_update);
        srl = findViewById(R.id.srl);
    }

    @Override
    protected void onDestroy() {
        stopFindLanDevice();
        super.onDestroy();
    }
}

