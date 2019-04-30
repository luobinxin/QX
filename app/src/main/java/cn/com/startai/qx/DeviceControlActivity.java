package cn.com.startai.qx;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.startai.helper.HStringUtils;
import cn.com.startai.helper.HTimerUtil;
import cn.com.startai.helper.TAndL;
import cn.com.startai.qx.utils.EventAction;
import cn.com.startai.qx.utils.EventBean;
import cn.com.startai.qxsdk.busi.socket.SocketBusiManager;
import cn.com.startai.qxsdk.busi.socket.entity.QueryVersionReq;
import cn.com.startai.qxsdk.busi.socket.entity.RecoveryReq;
import cn.com.startai.qxsdk.busi.socket.entity.RecoveryResp;
import cn.com.startai.qxsdk.busi.socket.entity.RenameDeviceResp;
import cn.com.startai.qxsdk.busi.socket.entity.UpdateVersionReq;
import cn.com.startai.qxsdk.busi.socket.entity.UpdateVersionResp;
import cn.com.startai.qxsdk.channel.BaseData;
import cn.com.startai.qxsdk.channel.mqtt.ServerConnectState;
import cn.com.startai.qxsdk.channel.mqtt.entity.Activate;
import cn.com.startai.qxsdk.QX;
import cn.com.startai.qxsdk.busi.common.PassthroughReq;
import cn.com.startai.qxsdk.busi.common.PassthroughResp;
import cn.com.startai.qxsdk.db.bean.DeviceBean;
import cn.com.startai.qxsdk.event.IQXCallListener;
import cn.com.startai.qxsdk.global.QXError;
import cn.com.startai.qxsdk.utils.QXLog;

public class DeviceControlActivity extends BaseActivity {

    @BindView(R.id.sw1)
    ToggleButton sw1;
    private EditText etHexStr;
    private Button btPass;
    private DeviceBean device;
    private EditText etPeroid;
    private TextView tvLog;
    private TextView tvRecvCount;
    private TextView tvSendCount;
    private int recvCount;
    private int recvCountLen;

    private int sendCount;
    private int sendCountLen;
    private Button btLanUpdate;
    private TextView tvCurrDevice;
    private Button btReset;
    private PassthroughReq passReq;
    private TextView tvConnect;


    @Override
    public void onEvent(EventBean eventBean) {
        super.onEvent(eventBean);

        switch (action) {
            case EventAction.ACTION_DEVICEBEAN_STATUS_CHANE:
                device = (DeviceBean) eventBean.getObject1();
                updateCurrDeviceTag();
                break;

            case EventAction.ACTION_PASSTHROUGH_RESULT:

                PassthroughResp resp = (PassthroughResp) eventBean.getObject1();
                onPassthroughResult(resp);

                break;

            case EventAction.ACTION_SERVER_CONNECTED:
                updateNetTag();
                break;

            case EventAction.ACTION_SERVER_DISCONNECTED:
                updateNetTag();
                break;

            case EventAction.ACTION_UNBIND_RESULT:
                finish();
                break;

            case EventAction.ACTION_MSG_ARRIVE:
                BaseData baseData = (BaseData) eventBean.getObject1();
                onMessageArrive(baseData);
                break;

            case EventAction.ACTION_RECOVERY_RESULT:
                RecoveryResp recoveryResp = (RecoveryResp) eventBean.getObject1();
                onSettingRecoveryResult(recoveryResp);
                break;

            case EventAction.ACTION_UPDATE_VERSION_RESULT:

                UpdateVersionResp respupdate = (UpdateVersionResp) eventBean.getObject1();
                onUpdateVersionResult(respupdate);

                break;


        }

    }

    private void onMessageArrive(BaseData baseData) {


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

    @Override
    protected int getLayout() {
        return R.layout.activity_device_control;
    }

    @Override
    public void init() {
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.include4);
        toolbar.setTitle("设备控制");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_48dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        device = (DeviceBean) getIntent().getSerializableExtra("device");

        initview();
        initListener();

        updateNetTag();
        updateCurrDeviceTag();

    }


    public void onPassthroughResult(PassthroughResp resp) {
        if (device != null) {

            String dataString = getStrFromRespData(resp);
            recvCountLen += dataString.length();
            recvCount++;
            String log = "共接收 " + recvCount + " 消息 总长度 " + recvCountLen;

            appendRecv(log);
            appendLog("recv = " + dataString + "  ---" + recvCount);
        }

    }

    private String getStrFromReqData(PassthroughReq req) {
        String dataString = "";
        boolean checked = sw1.isChecked();
        if (checked) {
            try {
                dataString = HStringUtils.byteArr2HexStr(req.getDataArr());
            } catch (Exception e) {
                QXLog.i(TAG, "data is not hex");

            }
        } else {
            dataString = new String(req.getDataStr());
        }

        return dataString;
    }

    private String getStrFromRespData(PassthroughResp req) {
        String dataString = "";
        boolean checked = sw1.isChecked();
        if (checked) {
            try {
                dataString = HStringUtils.byteArr2HexStr(req.getDataArr());
            } catch (Exception e) {
                QXLog.i(TAG, "data is not hex");

            }
        } else {
            dataString = new String(req.getDataStr());
        }

        return dataString;
    }

    private void appendRecv(final String log) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                tvRecvCount.setText(log);

            }
        });
    }


    private void appendLog(final String dataString) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                String currTimeStr = new SimpleDateFormat("HH:mm:ss--sss").format(new Date());

                String text = tvLog.getText().toString();

                if (text.length() > 50 * 1024 * 1024) {
                    tvLog.setText("");
                }

                tvLog.setText("\n" + dataString + " ----- " + currTimeStr + "\n" + text);

            }
        });
    }

    public void onHardwareActivateResult(Activate.Resp resp) {
        TAndL.TL(getApplicationContext(), "代激活结果 result = " + resp);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        HTimerUtil.close("etPeroid");
        getQXBusi().disConnectDevice(device, null);
    }

    private void initview() {
        etHexStr = (EditText) findViewById(R.id.et_device_control_hexstr);
        etPeroid = (EditText) findViewById(R.id.et_ps_peroid);

        btPass = (Button) findViewById(R.id.bt_devicecontrol_pass_lan);


        tvLog = (TextView) findViewById(R.id.id_ps_log);
        tvLog.setMovementMethod(ScrollingMovementMethod.getInstance());
        tvRecvCount = (TextView) findViewById(R.id.id_ps_recvcount);
        tvSendCount = (TextView) findViewById(R.id.id_ps_sendcount);

        tvRecvCount.setText("");
        tvSendCount.setText("");
        tvLog.setText("");

        btLanUpdate = (Button) findViewById(R.id.bt_devicecontrol_checkupdate);
//        btHardwareAct = (Button) findViewById(R.id.bt_hardware_act);

        btReset = findViewById(R.id.bt_reset);


        tvCurrDevice = findViewById(R.id.tv_devicecontrol_currdevice);
        tvConnect = findViewById(R.id.include5);

    }

    private void updateCurrDeviceTag() {

        if (!device.isCanCommunicate()) {
            finish();
        } else {
            Log.d(TAG, "device = " + device);
            tvCurrDevice.setText("sn:" + device.getSn() + "\nmac:" + device.getMac() + "\n" + (device.isLanState() ? "近场" : "远场"));
        }

    }

    private void initListener() {
//        btHardwareAct.setOnClickListener(this);
        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                StartAISDK.getInstance().getBusiManager().recoveryLanDevice(device.getMac(), callListener);

                RecoveryReq req = new RecoveryReq(device);
                SocketBusiManager.getInstance().recoveryDevice(req, callListener);

            }
        });

        tvLog.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                tvLog.setText("");

                return false;
            }
        });

        btLanUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                QueryVersionReq req = new QueryVersionReq(device);
                SocketBusiManager.getInstance().queryVersion(req, callListener);

            }
        });

//        btHardwareAct.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                C_0x8001.Req.ContentBean contentBean = new C_0x8001.Req.ContentBean();
//
//                contentBean.setAppid("ae6529f2fc52782a6d75db3259257084");
//                contentBean.setApptype("smartOlWifi");
//                contentBean.setClientid("SNSNSNSNSNSNSNSNSNSNSNSNSNSNSNSN");
//                contentBean.setDomain("startai");
//                contentBean.setSn("SNSNSNSNSNSNSNSNSNSNSNSNSNSNSNSN");
//                contentBean.setM_ver("Json_1.2.9_9.2.1");
//
//                C_0x8001.Req.ContentBean.FirmwareParamBean firmwareParamBean = new C_0x8001.Req.ContentBean.FirmwareParamBean();
//                firmwareParamBean.setBluetoothMac("AA:AA:AA:AA:AA:AA");
//                firmwareParamBean.setFirmwareVersion("abc");
//
//
//                contentBean.setFirmwareParam(firmwareParamBean);
//
//                //代智能硬件激活
//                StartAISDK.getInstance().getBusiManager().hardwareActivate(contentBean, callListener);
//
//
//            }
//        });

        btPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String dataStr = etHexStr.getText().toString();

                boolean checked = sw1.isChecked();
                if (checked) {
                    try {
                        byte[] dataArr = HStringUtils.hexStr2ByteArr(dataStr.replace(" ", ""));
                        passReq = new PassthroughReq(device, dataArr);
                    } catch (Exception e) {
                        TAndL.TL(getApplicationContext(), "data is not hex str");
                        return;
                    }

                } else {
                    passReq = new PassthroughReq(device, dataStr.getBytes());
                }

                final String s = etPeroid.getText().toString();
                long peroid = 0; //重复发送时间 间隔
                if (!TextUtils.isEmpty(s)) {
                    peroid = Long.parseLong(s);
                }
                if (peroid == 0) {
                    HTimerUtil.close("etPeroid");
                    passData(passReq);
                } else {
                    HTimerUtil.schedule("etPeroid", new TimerTask() {
                        @Override
                        public void run() {
                            passData(passReq);
                        }
                    }, 0, peroid);
                }
            }
        });


    }


    private void passData(final PassthroughReq req) {
        getQXBusi().passthrough(req, new IQXCallListener() {
            @Override
            public void onSuccess() {
                appendSendLog(getStrFromReqData(req));
                appendLog("Send = " + Arrays.toString(req.getDataArr()) + "  ---" + sendCount);
            }

            @Override
            public void onFailed(QXError callError) {
                TAndL.TL(getApplicationContext(), "消息发送失败 " + callError.getErrmsg());
            }
        });
    }


    private void appendSendLog(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                sendCountLen += s.length();

                sendCount++;
                tvSendCount.setText("共发送 " + sendCount + " 消息 总长度 = " + sendCountLen);
            }
        });
    }


    public void onUpdateVersionResult(final UpdateVersionResp resp) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {


                TAndL.TL(getApplicationContext(), "onUpdateVersionResult result = " + resp.getResult());
                if (resp.isQueryVersionAction()) {

                    if (resp.getResult() == 1) {

                        if (resp.curVersion >= resp.newVersion) {
                            TAndL.TL(getApplicationContext(), "已经是最新版本了 resp = " + resp.getDoubleCurVersion());
                        } else {
                            new AlertDialog.Builder(DeviceControlActivity.this)
                                    .setTitle("提示")
                                    .setMessage("检测到新版本：" + resp.getDoubleCurVersion() + " ==>" + resp.getDoubleNewVersion() + ",确定更新？")
                                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();


                                            UpdateVersionReq request = new UpdateVersionReq(device);
                                            SocketBusiManager.getInstance().updateDevice(request, callListener);


                                        }
                                    })
                                    .show();
                        }


                    }

                } else if (resp.isUpdateVersionAction()) {

                    new AlertDialog.Builder(DeviceControlActivity.this)
                            .setTitle("提示")
                            .setMessage("更新" + ((resp.getResult() == 1) ? "成功 " : "失败 ") + resp.getDoubleCurVersion() + " ==>" + resp.getDoubleNewVersion())
                            .setNegativeButton("我知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();

                }
            }
        });
    }

    public void onSettingRecoveryResult(RecoveryResp resp) {

        if (resp.getDeviceBean().getMac().equals(device.getMac()) && resp.getResult() == 1) {

            TAndL.TL(getApplicationContext(), "恢复出厂设备成功");
        }

    }

    public void onDeviceRenameResult(RenameDeviceResp resp) {

        TAndL.TL(getApplicationContext(), "重命名 " + ((resp.getResult() == 1) ? "成功" : "失败 -- ") + resp.getNewName() + " id = " + resp.getDeviceBean().getMac());

    }


}
