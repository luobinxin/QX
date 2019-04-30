package cn.com.startai.qx;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import cn.com.startai.helper.TAndL;
import cn.com.startai.qx.adapter.DeviceAdapter;
import cn.com.startai.qx.utils.DemoSPUtils;
import cn.com.startai.qx.utils.EventAction;
import cn.com.startai.qx.utils.EventBean;
import cn.com.startai.qxsdk.QX;
import cn.com.startai.qxsdk.QXDeviceManager;
import cn.com.startai.qxsdk.busi.common.BindDeviceReq;
import cn.com.startai.qxsdk.busi.common.ConnectDeviceResp;
import cn.com.startai.qxsdk.busi.common.GetBindListReq;
import cn.com.startai.qxsdk.busi.common.GetBindListResp;
import cn.com.startai.qxsdk.busi.common.UnBindDeviceReq;
import cn.com.startai.qxsdk.busi.common.UnBindDeviceResp;
import cn.com.startai.qxsdk.channel.mqtt.BaseMessage;
import cn.com.startai.qxsdk.channel.mqtt.ServerConnectState;
import cn.com.startai.qxsdk.db.bean.DeviceBean;
import cn.com.startai.qxsdk.global.ParamConsts;
import cn.com.startai.qxsdk.global.QXInitParam;
import cn.com.startai.scansdk.ScanActivity;
import cn.com.startai.scansdk.permission.DialogHelper;

public class HomeActivity extends BaseActivity {
    public static final String APPID_OKEYLIGHT_RP8 = "7446b4eaf72aafe4fabc5dac3374fcb8";
    public static final String APPID_OKAYLIGHT_TRIGGLEHOME = "f818c2704026de3c35c5aee06120ff98";
    public static final String APPID_RUIOO_INCHARGER = "f7cc35b33c8f579fae3df9f3e5941608";
    public static final String APPID_STARTAI_QXIOT_001 = "6e3788eedb60442c88b647bfaa1d285b";
    public static final String APPID_OKEYLIGHT_SMARTPLUG = "8040ab3093804dc1a8aeba3e24d3b97c";
    private Button btLogout;
    private RecyclerView mRecyclerView;

    List<DeviceBean> list = new ArrayList<>();

    private DeviceAdapter mAdapter;
    private long t;
    private TextView tvConnect;

    private String TAG = "AudioTrackTest";
    private QXInitParam param;


    @Override
    protected int getLayout() {
        return R.layout.activity_home;
    }


    @Override
    public void init() {

        final Toolbar toolbar = (Toolbar) findViewById(R.id.include2);
        toolbar.setTitle("设备");
        setSupportActionBar(toolbar);

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String appid = DemoSPUtils.getInstance().getLastInitParam().appid;


                String userId = QX.getInstance().getQxUserManager().getUserId();
                TAndL.TL(getApplicationContext(), "appid = \n" + appid + "\nuserId = \n" + userId);


//                final String topic = "SERVICE/NMC/qxb546488afc25b190/0x07";
//
//                List<String> topics = new ArrayList<>();
//                topics.add(topic);
//                QX.getInstance().getQxBusiManager().getQxMqtt().subscribe(topics, new IQXCallListener() {
//                    @Override
//                    public void onSuccess() {
//                        TAndL.L("订阅成功 topic = " + topic);
//
//                    }
//
//                    @Override
//                    public void onFailed(QXError qxError) {
//                        TAndL.L("订阅失败 err = " + qxError.getErrmsg());
//                    }
//                });


            }
        });

        initview();
        initAdapter();

        initListener();

        initQXSDK();


    }


    public static final String topicTestSUB = "Q/client/432EA4016B40B0BDFEB1313B062A3EAE/#";
    public static final String topicTest = "Q/client/432EA4016B40B0BDFEB1313B062A3EAE";


    @Override
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(EventBean eventBean) {
        super.onEvent(eventBean);
        String action = eventBean.getAction();

        switch (action) {
            case EventAction.ACTION_SERVER_DISCONNECTED:
                updateNetTag();
                break;
            case EventAction.ACTION_SERVER_CONNECTED:
                if (isLogin()) {
                    updateNetTag();
                } else {
                    toLogin();

                }
                break;
            case EventAction.ACTION_SERVER_RECONNECTING:
                updateNetTag();
                break;
            case EventAction
                    .ACTION_DEVICEBEAN_STATUS_CHANE:

                DeviceBean changeBean = (DeviceBean) eventBean.getObject1();


                for (int i = 0; i < list.size(); i++) {
                    DeviceBean deviceBean = list.get(i);
                    if (deviceBean.getSn().equals(changeBean.getSn())) {
                        list.remove(i);
                        list.add(i, changeBean);
                        break;
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setNewData(list);
                        mAdapter.notifyDataSetChanged();
                    }
                });
                break;

            case EventAction.ACTION_GET_BIND_LIST_RESULT:


                GetBindListResp resp5 = (GetBindListResp) eventBean.getObject1();

                onGetBindListResult(resp5);

                break;

            case EventAction.ACTION_UNBIND_RESULT:


                UnBindDeviceResp resp1 = (UnBindDeviceResp) eventBean.getObject1();
                onUnBindResult(resp1);
                break;

            case EventAction.ACTION_CONNECT_DEVICE_RESULT:

                ConnectDeviceResp resp3 = (ConnectDeviceResp) eventBean.getObject1();
                onConnectDeviceResult(resp3);

                break;
        }
    }

    private void onConnectDeviceResult(ConnectDeviceResp resp) {
        if (resp.getResult() == resp.RESULT_SUCCESS) {
            DeviceBean item = resp.getDeviceBean();
            //连接设备成功 跳转到设备控制界面
            if (item.isCanCommunicate()) {
                Intent intent = new Intent(HomeActivity.this, DeviceControlActivity.class);
                intent.putExtra("device", item);
                startActivity(intent);
            } else {
                TAndL.TL(getApplicationContext(), "当前设备不可通信");
            }

        } else {
            TAndL.TL(getApplicationContext(), "连接设备失败，当前设备不可通信");
        }
    }

    private void getbindlist(boolean isFromLocal) {
        getQXBusi().getBindList(new GetBindListReq(isFromLocal), callListener);
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


    private void initAdapter() {


        //设置RecyclerView管理器
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //初始化适配器
        mAdapter = new DeviceAdapter(R.layout.item_device, list);
        //设置添加或删除item时的动画，这里使用默认动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //设置适配器
        mRecyclerView.setAdapter(mAdapter);
    }


    private void initListener() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                DeviceBean item = mAdapter.getItem(position);

                getQXBusi().connectDevice(item, callListener);

            }
        });


        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {

                final DeviceBean item = mAdapter.getItem(position);

                new AlertDialog.Builder(HomeActivity.this)
                        .setTitle("操作")
                        .setSingleChoiceItems(new String[]{"修改名称", "修改备注名", "连接", "断开连接", "解绑设备 ", "删除设备（本地）"}, -1, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                switch (which) {
                                    case 0: //修改名称

                                        Intent i = new Intent(HomeActivity.this, UpdateLanDeviceNameActivity.class);
                                        i.putExtra("currDevice", item);
                                        startActivity(i);

                                        break;
                                    case 1://修改备注名


                                        Intent intent = new Intent(HomeActivity.this, UpdateRemarkActivity.class);
                                        intent.putExtra("currDevice", item);
                                        startActivity(intent);
                                        break;


                                    case 2://连接

                                        getQXBusi().connectDevice(item, callListener);

                                        break;
                                    case 3://断开连接

                                        getQXBusi().disConnectDevice(item, callListener);

                                        break;
                                    case 4://解绑设备

                                        new AlertDialog.Builder(HomeActivity.this)
                                                .setTitle("提示")
                                                .setMessage("是否解绑")
                                                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                        UnBindDeviceReq req = new UnBindDeviceReq(item.getSn());
                                                        getQXBusi().unBindDevice(req, callListener);

                                                    }
                                                })
                                                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        dialogInterface.dismiss();
                                                    }
                                                })
                                                .create().show();


                                        break;


                                    case 5:// 本地删除

                                        QXDeviceManager.getInstance().deleteLocalDevice(item);
                                        getbindlist(true);
                                        break;
                                }
                            }
                        })
                        .show();


                return false;
            }
        });


    }

    public void onLanDisconnectDeviceResult(boolean result, DeviceBean deviceBean) {

        TAndL.TL(getApplicationContext(), "局域网连接断开" + (result ? "成功" : "失败") + " mac = " + deviceBean.getMac());

    }


    public void onGetBindListResult(GetBindListResp resp) {

        dismissDialog();

        //查询结果
        if (resp.getResult() == BaseMessage.RESULT_SUCCESS) {
            TAndL.TL(getApplicationContext(), "获取绑定列表成功 ");
            //设备列表已经自动缓存至本地 可直接加载本地数据
            updateList(resp.getDeviceBeanList());
        }

    }


    public void onUnBindResult(UnBindDeviceResp resp) {


        if (resp.getResult() == 1) {
            TAndL.TL(getApplicationContext(), "解绑成功 ");

            for (DeviceBean deviceBean : list) {

                if (deviceBean.getSn().equals(resp.getSn())) {
                    list.remove(deviceBean);

                    break;
                }
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.setNewData(list);
                    mAdapter.notifyDataSetChanged();
                }
            });

        } else {
            TAndL.TL(getApplicationContext(), "解绑失败 " + resp.getErrmsg());
        }

    }


    private void initview() {

        btLogout = findViewById(R.id.bt_home_logout);
        //通过findViewById拿到RecyclerView实例
        mRecyclerView = findViewById(R.id.rv_devicelist);
        btLogout.setVisibility(View.GONE);
        tvConnect = findViewById(R.id.include5);
    }

    private void toScanBarCode() {

        if (Build.VERSION_CODES.M <= Build.VERSION.SDK_INT) {
            PermissionUtils.permission(PermissionConstants.CAMERA)
                    .rationale(new PermissionUtils.OnRationaleListener() {
                        @Override
                        public void rationale(final ShouldRequest shouldRequest) {
                            DialogHelper.showRationaleDialog(shouldRequest);
                        }
                    })
                    .callback(new PermissionUtils.FullCallback() {
                        @Override
                        public void onGranted(List<String> permissionsGranted) {

                            toScanActivity();

                        }

                        @Override
                        public void onDenied(List<String> permissionsDeniedForever,
                                             List<String> permissionsDenied) {
                            if (!permissionsDeniedForever.isEmpty()) {
                                DialogHelper.showOpenAppSettingDialog();
                            }
                            LogUtils.d(permissionsDeniedForever, permissionsDenied);
                        }
                    })
                    .request();

        } else {
            toScanActivity();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case REQUEST_CODE_BIND_DEVICE:

                    String scanResult = data.getStringExtra("result");
                    Log.i(TAG, "scan result = " + scanResult);

                    String sn = getSnFromQRCodrResult(scanResult);

                    if (TextUtils.isEmpty(sn)) {
                        TAndL.TL(getApplicationContext(), "未识别出设备sn");
                    } else {
                        BindDeviceReq req = new BindDeviceReq(sn);
                        getQXBusi().bindDevice(req, callListener);

                    }

                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    String QR_CODE_INDEX = "http://www.startai.com.cn/qr/?";
//

    /**
     * 检查 是否是sn的二码维码
     */
    private String getSnFromQRCodrResult(String result) {

        if (result.startsWith(QR_CODE_INDEX)) {
            String[] datas = result.split("\\?");
            if (datas.length >= 2 && !TextUtils.isEmpty(datas[1])) {
                String sn = datas[1];
                return sn;
            }
        }
        return "";
    }


    /**
     * 跳转到绑定设备页面的 请求码
     */
    public static final int REQUEST_CODE_BIND_DEVICE = 1;

    private void toScanActivity() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                ScanActivity.showActivityForResult(HomeActivity.this, 1);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.menu_scan) {
            toScanBarCode();
        } else if (id == R.id.menu_reset) {
            startActivity(new Intent(HomeActivity.this, AccountActivity.class));
        } else if (id == R.id.menu_airkiss) {
            new AlertDialog.Builder(HomeActivity.this)
                    .setTitle("配网方式")
                    .setSingleChoiceItems(new String[]{"Airkiss", "Esptouch"}, -1, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            switch (which) {
                                case 0:
                                    startActivity(new Intent(HomeActivity.this, AirkissActivity.class));
                                    break;
                                case 1:
                                    startActivity(new Intent(HomeActivity.this, EsptouchActivity.class));
                                    break;
                            }
                        }
                    })
                    .show();
        } else if (id == R.id.menu_refresh) {

            getbindlist(false);

        } else if (id == R.id.menu_update) {
            startActivity(new Intent(HomeActivity.this, DiscoveryActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        TAndL.L("onResume()");

        getbindlist(true);

        updateNetTag();

    }


    private void updateNetTag() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (QX.getInstance().getServerConnectState() == ServerConnectState.CONNECTED) {
                    tvConnect.setVisibility(View.GONE);
                } else {
                    tvConnect.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    private void updateList(List<DeviceBean> deviceBeans) {
        list = deviceBeans;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {


                if (list == null || list.isEmpty()) {
                    TAndL.TL(getApplicationContext(), "Device list is empty");
                }
                mAdapter.setNewData(list);
            }
        });

    }


    private void initQXSDK() {


        param = getLastInitParam();
        if (param != null) {


            QX.getInstance().init(getApplication(), param);


        } else {

            new AlertDialog.Builder(HomeActivity.this)
                    .setTitle("选择产品")
                    .setSingleChoiceItems(new String[]{"qxiot-001(startai)", "RP8(okaylight)", "TriggleHome(okaylight)", "INCHARGER(ruioo)", "SmartPlug(okaylight)"}, -1, new DialogInterface.OnClickListener() {

                        QXInitParam param = null;

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            String appid = "";
                            byte customer = 0;
                            byte product = 0;
                            switch (which) {
                                case 0:
                                    appid = APPID_STARTAI_QXIOT_001;
                                    customer = ParamConsts.CUSTOM_STARTAI;
                                    product = ParamConsts.PRODUCT_MUSIK;
                                    break;
                                case 1:
                                    appid = APPID_OKEYLIGHT_RP8;

                                    break;
                                case 2:
                                    appid = APPID_OKAYLIGHT_TRIGGLEHOME;
                                    customer = ParamConsts.CUSTOM_WAN;
                                    product = ParamConsts.PRODUCT_TRIGGER_WIFI;

                                    break;
                                case 3:
                                    appid = APPID_RUIOO_INCHARGER;

                                    break;
                                case 4:
                                    appid = APPID_OKEYLIGHT_SMARTPLUG;
                                    customer = ParamConsts.CUSTOM_WAN;
                                    product = ParamConsts.PRODUCT_GROWROOMATE;
                                    break;

                            }

                            param = new QXInitParam(appid, customer, product, false);

                            QX.getInstance().init(getApplication(), param);
                            DemoSPUtils.getInstance().putLastInitParam(param);
                        }
                    })
                    .show();

        }


    }


    public QXInitParam getLastInitParam() {

        return DemoSPUtils.getInstance().getLastInitParam();
    }

}
