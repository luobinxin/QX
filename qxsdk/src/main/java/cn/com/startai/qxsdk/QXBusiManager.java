package cn.com.startai.qxsdk;

import android.app.Application;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.xutils.x;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import cn.com.startai.qxsdk.channel.mqtt.busi.BaseMiofBusiHandler;
import cn.com.startai.qxsdk.channel.mqtt.entity.Passthrough;
import cn.com.startai.qxsdk.busi.common.BindDeviceReq;
import cn.com.startai.qxsdk.busi.common.BindDeviceResp;
import cn.com.startai.qxsdk.busi.common.ConnectDeviceResp;
import cn.com.startai.qxsdk.busi.common.GetBindListReq;
import cn.com.startai.qxsdk.busi.common.GetBindListResp;
import cn.com.startai.qxsdk.busi.common.PassthroughReq;
import cn.com.startai.qxsdk.busi.common.PassthroughResp;
import cn.com.startai.qxsdk.busi.common.UnBindDeviceReq;
import cn.com.startai.qxsdk.busi.common.UnBindDeviceResp;
import cn.com.startai.qxsdk.channel.mqtt.busi.QXMqttDataCreater;
import cn.com.startai.qxsdk.channel.mqtt.entity.Activate;
import cn.com.startai.qxsdk.channel.mqtt.entity.Bind;
import cn.com.startai.qxsdk.channel.mqtt.entity.BindEmail;
import cn.com.startai.qxsdk.channel.mqtt.entity.BindMobile;
import cn.com.startai.qxsdk.channel.mqtt.entity.BindThirdAccount;
import cn.com.startai.qxsdk.channel.mqtt.entity.BrokerHost;
import cn.com.startai.qxsdk.channel.mqtt.entity.CheckIdentifyCode;
import cn.com.startai.qxsdk.channel.mqtt.entity.GetAlipayAuthInfo;
import cn.com.startai.qxsdk.channel.mqtt.entity.GetBindList;
import cn.com.startai.qxsdk.channel.mqtt.entity.GetIdentifyCode;
import cn.com.startai.qxsdk.channel.mqtt.entity.GetLatestAppVersion;
import cn.com.startai.qxsdk.channel.mqtt.entity.GetRealPayResult;
import cn.com.startai.qxsdk.channel.mqtt.entity.GetUserInfo;
import cn.com.startai.qxsdk.channel.mqtt.entity.GetWeatherInfo;
import cn.com.startai.qxsdk.channel.mqtt.entity.Login;
import cn.com.startai.qxsdk.channel.mqtt.entity.LoginWithThirdAccount;
import cn.com.startai.qxsdk.channel.mqtt.entity.Register;
import cn.com.startai.qxsdk.channel.mqtt.entity.ResetLoginPwd;
import cn.com.startai.qxsdk.channel.mqtt.entity.SendEmail;
import cn.com.startai.qxsdk.channel.mqtt.entity.ThirdPaymentUnifiedOrder;
import cn.com.startai.qxsdk.channel.mqtt.entity.UnActivate;
import cn.com.startai.qxsdk.channel.mqtt.entity.UnBind;
import cn.com.startai.qxsdk.channel.mqtt.entity.UnBindThirdAccount;
import cn.com.startai.qxsdk.channel.mqtt.entity.UpdateDeviceInfo;
import cn.com.startai.qxsdk.channel.mqtt.entity.UpdateLoginPwd;
import cn.com.startai.qxsdk.channel.mqtt.entity.UpdateRemark;
import cn.com.startai.qxsdk.channel.mqtt.entity.UpdateUserInfo;
import cn.com.startai.qxsdk.channel.BaseData;
import cn.com.startai.qxsdk.channel.CommunicationMode;
import cn.com.startai.qxsdk.channel.ble.IQXBLE;
import cn.com.startai.qxsdk.channel.ble.QXBleImpl;
import cn.com.startai.qxsdk.channel.mqtt.BaseMessage;
import cn.com.startai.qxsdk.channel.mqtt.IQXMqtt;
import cn.com.startai.qxsdk.channel.mqtt.ServerConnectState;
import cn.com.startai.qxsdk.channel.mqtt.TopicConsts;
import cn.com.startai.qxsdk.channel.mqtt.client.QXMqttConfig;
import cn.com.startai.qxsdk.channel.mqtt.client.QXMqttImpl;
import cn.com.startai.qxsdk.channel.mqtt.event.IQxMqttListener;
import cn.com.startai.qxsdk.channel.mqtt.task.OnMqttTaskCallBack;
import cn.com.startai.qxsdk.channel.udp.BroadcastDiscoveryUtil;
import cn.com.startai.qxsdk.channel.udp.IQXUDP;
import cn.com.startai.qxsdk.channel.udp.QXUdpDataCreater;
import cn.com.startai.qxsdk.channel.udp.bean.ControlDevice;
import cn.com.startai.qxsdk.channel.udp.bean.ControlDeviceUtil;
import cn.com.startai.qxsdk.channel.udp.bean.LanDeviceLst;
import cn.com.startai.qxsdk.channel.udp.bean.ScmDevice;
import cn.com.startai.qxsdk.channel.udp.bean.ScmDeviceUtils;
import cn.com.startai.qxsdk.channel.udp.client.QXUDPImpl;
import cn.com.startai.qxsdk.channel.udp.event.IControlWiFi;
import cn.com.startai.qxsdk.channel.udp.event.IQXUDPListener;
import cn.com.startai.qxsdk.channel.udp.task.OnUdpTaskCallBack;
import cn.com.startai.qxsdk.channel.udp.task.ProtocolTaskImpl;
import cn.com.startai.qxsdk.db.QXDBManager;
import cn.com.startai.qxsdk.db.bean.DeviceBean;
import cn.com.startai.qxsdk.db.bean.UserBean;
import cn.com.startai.qxsdk.event.IOutput;
import cn.com.startai.qxsdk.event.IQXBusi;
import cn.com.startai.qxsdk.event.IQXBusiResultListener;
import cn.com.startai.qxsdk.event.IQXCallListener;
import cn.com.startai.qxsdk.global.AreaNodesManager;
import cn.com.startai.qxsdk.global.LooperManager;
import cn.com.startai.qxsdk.global.QXCallbackManager;
import cn.com.startai.qxsdk.global.QXError;
import cn.com.startai.qxsdk.global.QXErrorCode;
import cn.com.startai.qxsdk.global.QXInitParam;
import cn.com.startai.qxsdk.global.QXParamManager;
import cn.com.startai.qxsdk.global.QXSpController;
import cn.com.startai.qxsdk.utils.network.IQXNetworkListener;
import cn.com.startai.qxsdk.utils.network.QXNetworkManager;
import cn.com.startai.qxsdk.utils.QXLog;
import cn.com.startai.qxsdk.utils.QXNetUtils;
import cn.com.startai.qxsdk.utils.QXShareUtils;
import cn.com.startai.qxsdk.utils.QXStringUtils;
import cn.com.startai.qxsdk.utils.QXTimerUtil;
import cn.com.startai.qxsdk.utils.area.AreaLocation;
import cn.com.startai.qxsdk.utils.area.QXLocationManager;
import cn.com.swain.baselib.log.Tlog;
import cn.com.swain.baselib.util.Bit;
import cn.com.swain.baselib.util.IpUtil;
import cn.com.swain.support.protocolEngine.ProtocolProcessorFactory;
import cn.com.swain.support.protocolEngine.pack.ReceivesData;
import cn.com.swain.support.protocolEngine.resolve.AbsProtocolProcessor;
import cn.com.swain.support.protocolEngine.task.FailTaskResult;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * Created by Robin on 2019/3/20.
 * 419109715@qq.com 彬影
 */
public class QXBusiManager implements IOutput, IQXBusi, IQxMqttListener, OnMqttTaskCallBack, IQXUDPListener, OnUdpTaskCallBack, IQXNetworkListener, IControlWiFi.IWiFiResultCallBack {


    private QXNetUtils.ConnectWIFIInfo connectedWiFiInfo;
    private long lastDiscoveryTime;
    private ControlDeviceUtil mControlDeviceUtil;

    protected QXBusiManager() {
    }

//    public static QXBusiManager getInstance() {
//        return SingleTonHoulder.singleTonInstance;
//    }
//
//    private static class SingleTonHoulder {
//        private static final QXBusiManager singleTonInstance = new QXBusiManager();
//    }


    private boolean needCallBackUpdateDeviceInfo;
    private boolean needCallBackDeviceBeanStateChange;
    private boolean needCallBackGetUserInfo;
    private boolean needCallbackDiscovery;
    private boolean needCallbacBindResult;
    private boolean needCallbackControlDevice;


    private Application app;

    private IQXMqtt qxMqtt;
    private IQXBLE qxBle;
    private IQXUDP qxUdp;

    private final LanDeviceLst mDiscoveryDeviceLst = new LanDeviceLst();

    LanDeviceLst getmDiscoveryDeviceLst() {
        return mDiscoveryDeviceLst;
    }

    public IQXUDP getQxUdp() {
        return qxUdp;
    }

    public void setQxUdp(IQXUDP qxUdp) {
        this.qxUdp = qxUdp;
    }

    public IQXMqtt getQxMqtt() {
        return qxMqtt;
    }

    public void setQxMqtt(IQXMqtt qxMqtt) {
        this.qxMqtt = qxMqtt;
    }

    public IQXBLE getQxBle() {
        return qxBle;
    }

    public void setQxBle(IQXBLE qxBle) {
        this.qxBle = qxBle;
    }

    public Application getApp() {
        return app;
    }

    public boolean isInit() {
        return app != null;
    }

    private IQXBusiResultListener busiResultListener;

    private BroadcastDiscoveryUtil broadcastDiscoveryUtil;

    private ScmDeviceUtils mScmDeviceUtils;
    private final Map<String, Integer> tokenMap = Collections.synchronizedMap(new HashMap<String, Integer>());

    private ProtocolTaskImpl protocolTask;

    public ProtocolTaskImpl getProtocolTask() {
        if (protocolTask == null) {
            protocolTask = new ProtocolTaskImpl(this, getApp());
        }
        return protocolTask;
    }

    public void setProtocolTask(ProtocolTaskImpl protocolTask) {
        this.protocolTask = protocolTask;
    }

    /**
     * 获取与服务器的连接状态
     *
     * @return
     */
    @Override
    public ServerConnectState getServerConnectState() {
        if (getQxMqtt() != null) {
            return getQxMqtt().getQXMqttConnectState();
        }
        return ServerConnectState.DISCONNECTED;
    }

    /**
     * 注销激活
     *
     * @param callListener
     */
    @Override
    public void unActivite(IQXCallListener callListener) {

    }

    public void setApp(Application app) {
        this.app = app;
    }


    private BaseMiofBusiHandler busiHandler;
    //协议解析器
    private AbsProtocolProcessor pm;

    public BaseMiofBusiHandler getBusiHandler() {
        if (busiHandler == null) {
            busiHandler = new BaseMiofBusiHandler(this);
        }
        return busiHandler;
    }

    public void setBusiHandler(BaseMiofBusiHandler busiHandler) {
        this.busiHandler = busiHandler;
    }

    public void init(Application app, QXInitParam qxInitParam) {

        if (isInit()) {
            Log.e(TAG, "no need repeat init");
            return;
        }

        long t = System.currentTimeMillis();
        if (qxInitParam == null || TextUtils.isEmpty(qxInitParam.appid)) {
            Log.e(TAG, "QXSDK init failed 'qxInitParam' is empty or 'appid' is empty");
            return;
        }


        this.app = app;
        x.Ext.init(app);

        QXShareUtils.getInstance().init(getApp());
        QXNetworkManager.getInstance().init(getApp());
        QXNetworkManager.getInstance().addNetworkListener(this);


        QXParamManager paramManager = QXParamManager.getInstance();
        paramManager.setAppid(qxInitParam.appid);
        QXMqttConfig.setAppid(qxInitParam.appid);

        if (qxInitParam.needCustomerFilter != null) {
            paramManager.setNeedCustomerFilter(qxInitParam.needCustomerFilter);
        }
        if (qxInitParam.mCustom != null) {
            paramManager.setmCustom(qxInitParam.mCustom);
        }
        if (qxInitParam.mProduct != null) {
            paramManager.setmProduct(qxInitParam.mProduct);
        }
        if (qxInitParam.mProtocolVersion != null) {
            paramManager.setmProtocolVersion(qxInitParam.mProtocolVersion);
        }

        initMqtt();

        String userId = QXUserManager.getInstance().getUserId();
        if (!TextUtils.isEmpty(userId) && paramManager.isUdpParamInit()) {
            //如果已经登录，则初始化 udp
            initUdp();
        }

        QXLog.d(TAG, "init use time = " + (System.currentTimeMillis() - t));
    }

    private void initBle() {
        QXLog.e(TAG, "initBle");
        qxBle = QXBleImpl.getInstance();

        qxBle.init();
    }

    private void releaseBle() {
        QXLog.e(TAG, "releaseBle");
        if (qxBle != null) {
            qxBle.release();
            qxBle.release();
        }
    }

    private void initMqtt() {
        QXLog.e(TAG, "initMqtt");
        qxMqtt = QXMqttImpl.getInstance();
        qxMqtt.setListener(this);
        qxMqtt.init();
    }

    private void releaseMqtt() {
        QXLog.e(TAG, "releaseMqtt");
        if (qxMqtt != null) {
            qxMqtt.release();
            qxMqtt = null;
        }
    }

    private void initUdp() {


        QXParamManager qxParamManager = QXParamManager.getInstance();
        QXUdpDataCreater.getInstance().onSCreate();

        QXLog.e(TAG, "initUdp");
        pm = ProtocolProcessorFactory.newMultiChannelSingleTask(LooperManager.getInstance().getProtocolLooper(),
                getProtocolTask(),
                qxParamManager.getmProtocolVersion(), true);

        mControlDeviceUtil = new ControlDeviceUtil();

        mScmDeviceUtils = new ScmDeviceUtils(this, new ScmDevice.OnScmCallBack() {
            @Override
            public void onStartSendHeartbeat(ScmDevice mScmDevice) {

            }

            @Override
            public void onHeartbeatLose(String mac, int diff) {
                Tlog.e(TAG, " heartbeatLose startDiscovery mac :" + mac + " loseTimes:" + diff);
                ControlDevice controlDevice = mControlDeviceUtil.get(mac);
                if (controlDevice != null) {
                    if (controlDevice.canLanCom()) {
                        toDiscovery(5 * 3000);
                        onLanConnectStatusChange(false, mac);
                    } else {
                        toDiscovery(1000);
                    }
                    controlDevice.heartbeatLose(diff);
                }

            }

            @Override
            public void onDelaySend(int what, Object obj) {
                if (what == 0) {
                } else if (what == 1) {
                } else if (what == 2) {
                    BaseData data = (BaseData) obj;
                    doSend(data, null);
                }

            }
        });


        qxUdp = QXUDPImpl.getInstance();
        qxUdp.setListener(this);
        qxUdp.init();


    }

    private void onLanConnectStatusChange(boolean result, String mac) {

    }

    private void releaseUdp() {
        QXLog.e(TAG, "releaseUdp");
        if (broadcastDiscoveryUtil != null) {
            broadcastDiscoveryUtil.stopDiscovery();
            broadcastDiscoveryUtil = null;

        }
        if (pm != null) {
            pm.release();
        }

        QXUdpDataCreater.getInstance().onSDestroy();

        if (mScmDeviceUtils != null) {
            mScmDeviceUtils.cleanMap();
        }

        if (qxUdp != null) {
            qxUdp.release();
            qxUdp = null;
        }
    }

    /**
     * 添加监听
     *
     * @param listener
     */
    @Override
    public void setQXBusiListener(IQXBusiResultListener listener) {
        this.busiResultListener = listener;

    }

    public void release() {
        QXLog.e(TAG, "release");

        releaseBle();

        releaseMqtt();

        releaseUdp();

        QXTimerUtil.closeAll();

        QXNetworkManager.getInstance().removeNetworkListener(this);
        QXNetworkManager.getInstance().release();

        //清空缓存的设备列表
        QXDeviceManager.getInstance().release();

        app = null;
    }


    @Override
    public void doSend(BaseData baseData, IQXCallListener listener) {

        QXLog.d(TAG, "doSend baseData  = " + baseData);

        if (baseData == null) {
            QXCallbackManager.callbackCallResult(false, listener, QXError.ERROR_SEND_PARAM_INVALIBLE);
            return;
        }

        if (baseData.getRepeatMsgModel().isNeedRepeatSend()) {
            mScmDeviceUtils.getScmDevice(baseData.getMac()).recordSendMsg(baseData, 1000 * 3L);
        }


        if (baseData.getCommunicationMode() == CommunicationMode.MQTT) {
            doSendMqtt(baseData, listener);
            return;
        }

        if (baseData.getCommunicationMode() == CommunicationMode.UDP) {
            if (doSendUdp(baseData)) {
                QXCallbackManager.callbackCallResult(true, listener, "");
            } else {
                QXCallbackManager.callbackCallResult(false, listener, QXError.ERROR_SEND_SEND_LAN_FAILED);
            }
        }

    }

    @Override
    public void doSendMqtt(BaseData mqttData, IQXCallListener listener) {

        if (qxMqtt == null) {
            QXLog.e(TAG, "no qxmqtt");
            QXCallbackManager.callbackCallResult(false, listener, QXError.ERROR_SEND_CLIENT_DISCONNECT);
            return;
        }
        if (qxMqtt.getQXMqttConnectState() != ServerConnectState.CONNECTED) {
            QXLog.e(TAG, QXError.ERROR_SEND_CLIENT_DISCONNECT);
            QXCallbackManager.callbackCallResult(false, listener, QXError.ERROR_SEND_CLIENT_DISCONNECT);
            return;
        }

        qxMqtt.publish(mqttData, listener);

    }

    @Override
    public boolean doSendUdp(BaseData data) {

        if (!QXNetworkManager.getInstance().isWifiConnected()) {
            QXLog.e(TAG, "networkType is not 'WIFI'");
            return false;
        }

        if (qxUdp == null) {
            QXLog.e(TAG, "qxUdp is empty");
            return false;
        }

        String sendIp;
        int sendPort;

        DeviceBean lanDeviceByMac = mDiscoveryDeviceLst.getDeviceBeanByMac(data.getMac());
        if (lanDeviceByMac == null) {
            sendIp = data.getIp();
            sendPort = data.getPort();
        } else {
            sendIp = lanDeviceByMac.getIp();
            sendPort = lanDeviceByMac.getPort();
        }

        if (TextUtils.isEmpty(sendIp) || sendPort <= 0) {
            QXLog.e(TAG, "send ip or port error");
            return false;
        }

        try {
            InetAddress.getByName(lanDeviceByMac.getIp());
        } catch (UnknownHostException e) {
            e.printStackTrace();
            QXLog.e(TAG, "ip format error");
            return false;
        }

        data.setIp(sendIp);
        data.setPort(sendPort);

        qxUdp.send(data);

        return true;
    }

    @Override
    public void doSendBle(BaseData qxbleData) {

    }


    // --------------------------- IQxMqttListener start ------------------------------

    @Override
    public void onMqttConnected() {

        subUserOrSnTopic();

        if (QXSpController.getIsActivite()) {
            if (busiResultListener != null) {
                busiResultListener.onServerConnected();
            }
            checkAreNode();
            reportIp();
            checkUnCompleteMsg();
            subFriendReportTopic();
            checkIsAvaliToken();
        } else {
            checkActivate();
        }
    }

    private synchronized void reportIp() {
        final String taskKey = "reportIp";
        QXTimerUtil.schedule(taskKey, new TimerTask() {
            @Override
            public void run() {
                AreaLocation areaLocation = QXLocationManager.getInstance().getLocation();
                if (areaLocation != null && !TextUtils.isEmpty(areaLocation.getQuery())) {

                    UpdateDeviceInfo.Req req = new UpdateDeviceInfo.Req(QXMqttConfig.getSn(QX.getInstance().getApp()), new UpdateDeviceInfo.Req.StatusParam(areaLocation.getQuery()));

                    BaseData updateDeviceInfoData = QXMqttDataCreater.getUpdateDeviceInfoData(req);
                    doSend(updateDeviceInfoData, null);

                } else {
                    Log.w(TAG, "outterIp = " + areaLocation);
                }

                QXTimerUtil.close(taskKey);
            }
        }, 100);

    }

    private void checkUnCompleteMsg() {

    }

    /**
     * 检查toke是否过期
     */
    private void checkIsAvaliToken() {


        UserBean user = QXUserManager.getInstance().getUser();
        if (user != null) {
            //已经登录

            //获取上一次判断token的时间

            long l = QXSpController.getsetLastCheckExpireTime();
            long l1 = System.currentTimeMillis() - l;
            QXLog.d(TAG, "ll = " + l1);
            if (l1 < 12 * 60 * 60 * 1000) {
                QXLog.d(TAG, "未到检查 token 有效性 的时间");
                return;
            }

            QXLog.d(TAG, "定期检查 token 有效性");
            QXSpController.setLastCheckExpireTime(System.currentTimeMillis());

            long expire_in = user.getExpire_in();
            long time = user.getAddTime();

            long peroid = (System.currentTimeMillis() - time) / 1000;
            long diff = peroid - expire_in;
            QXLog.d(TAG, "expire_in = " + expire_in + " peroid " + peroid);
            if (expire_in > 0 && diff > 0) {
                QXLog.d(TAG, "账号登录状态已过期，需要重新登录");
                QXUserManager.getInstance().resetUser();
                if (busiResultListener != null) {
                    busiResultListener.onLoginExpired();
                } else {
                    QXLog.d(TAG, "账号登录状态正常，可以正常使用");
                }
            }

        }

    }

    public void checkAreNode() {

        if (QXMqttConfig.changeHostTimeDelay <= 0) {
            QXLog.d(TAG, "没有设置自动切换节点，不需要去获取区域节点信息");
            return;
        }
        final String taskKey = "checkAreNode";
        QXTimerUtil.schedule(taskKey, new TimerTask() {
            @Override
            public void run() {

                boolean isNeedToGetBrokerHost = false;

                //获取上次同步 区域节点的时间， 如果 大于 设定值就去同步一下
                long lastTime = QXSpController.getLastGetBrokerHostrespTime(); //上次同步时间
                BrokerHost.Resp.ContentBean spAreaNodes = AreaNodesManager.getInstance().getSpAreaNodes();//上次同步数据

                if (lastTime == 0 || spAreaNodes == null) {
                    isNeedToGetBrokerHost = true;
                } else {
                    int syncCycle = spAreaNodes.getCycle() * 1000; //同步周期
                    long peroid = System.currentTimeMillis() - lastTime;
                    QXLog.d(TAG, "验证上次同步区域节点时间 lastGet = " + lastTime + " syncCycle = " + syncCycle + " peroid = " + peroid);
                    if (peroid >= syncCycle) {
                        QXLog.d(TAG, "获取过节点信息，但离上次获取已经超过了限定值,重新同步区域节点信息");
                        isNeedToGetBrokerHost = true;
                    } else {
                        QXLog.d(TAG, "未到同步区域节点时间，无需同步");

                        long delay = syncCycle - peroid;
                        QXLog.d(TAG, delay + "ms后将会再次同步");
                        QXTimerUtil.schedule(taskKey, new TimerTask() {
                            @Override
                            public void run() {
                                checkAreNode();
                            }
                        }, delay + 1000);

                    }
                }

                if (isNeedToGetBrokerHost) {
                    AreaLocation areaLocation = QXLocationManager.getInstance().getLocation();
                    BrokerHost.Req req = new BrokerHost.Req(areaLocation.getQuery());
                    doSend(QXMqttDataCreater.getBrokerHostData(req), null);
                }
                QXTimerUtil.close(taskKey);

            }
        }, 100);


    }

    private void checkActivate() {
        final String timerKey = "checkActivate";
        QXTimerUtil.schedule(timerKey, new TimerTask() {
            @Override
            public void run() {

                boolean isActivite = QXSpController.getIsActivite();
                if (!isActivite) {
                    QXLog.e(TAG, "设备未激活，正在准备激活");
                    BaseData activateData = QXMqttDataCreater.getActivateData(null);
                    doSend(activateData, null);
                } else {
                    QXLog.d(TAG, "设备已经正常激活");
                    QXTimerUtil.close(timerKey);
                }
            }
        }, 0, 60 * 1000);

    }

    @Override
    public void onMqttDisconnected(QXError qxError) {
        if (busiResultListener != null) {
            busiResultListener.onServerDisConnect(qxError);
        }
    }

    @Override
    public void onMqttReconnecting() {
        if (busiResultListener != null) {
            busiResultListener.onServerReConnecting();
        }
    }

    @Override
    public void onMessageArrived(String topic, MqttMessage message) {


        String msg = new String(message.getPayload(), Charset.forName("utf-8"));
        getBusiHandler().handMessage(topic, msg);

    }


    // --------------------------- IQxMqttListener end ------------------------------

    // --------------------------- IQXUDPListener start ------------------------------

    /**
     * UDP init
     *
     * @param result init result
     * @param ip     init ip
     * @param port   init port
     */
    @Override
    public void onUDPInitResult(boolean result, String ip, int port) {
        QXLog.e(TAG, "onUDPInitResult result = " + result + " ip = " + ip + " port +" + port);
        if (result) {
            broadcastDiscoveryUtil = new BroadcastDiscoveryUtil(LooperManager.getInstance().getWorkLooper(), qxUdp, this, getApp());

        }
    }

    /**
     * udp receive data
     *
     * @param ip   receive data ip
     * @param port receive data port
     * @param data receive data
     */
    @Override
    public void onUDPReceiveData(String ip, int port, byte[] data) {
        QXLog.e(TAG, "onUDPReceiveData ip = " + ip + " port = " + port + " data = " + Arrays.toString(data));

        DeviceBean deviceBean = mDiscoveryDeviceLst.getLanDeviceByIP(ip);
        if (pm != null) {
            String mac = "";
            if (deviceBean != null) {
                mac = deviceBean.getMac();
            }

            if (TextUtils.isEmpty(mac)) {
                mac = "00:00:00:00:00:00";
                long d = System.currentTimeMillis();
                if (Math.abs(d - lastDiscoveryTime) > 7000L) {
                    toDiscovery(3000L);
                }
                lastDiscoveryTime = d;
            }

            ReceivesData mReceiveData = new ReceivesData(mac, data);
            mReceiveData.getReceiveModel().setModelIsLan();
            mReceiveData.obj = ip;
            mReceiveData.arg = port;
            pm.onInputProtocolData(mReceiveData);
        } else {
            Tlog.e(TAG, " onUDPReceiveData  input==null ");
        }

        deviceBean = mDiscoveryDeviceLst.getLanDeviceByIP(ip);
        if (deviceBean != null) {
            DeviceBean deviceBeanByMac = QXDeviceManager.getInstance().getDeviceBeanByMac(deviceBean.getMac());
            if (deviceBeanByMac != null) {
                callbackPassthrough(new PassthroughResp(1, deviceBeanByMac, ip, port, data, null));
            }
        }
    }

    /**
     * udp release
     *
     * @param result release result
     */
    @Override
    public void onUDPReleaseResult(boolean result) {
        QXLog.e(TAG, "onUDPReleaseResult result = " + result);

    }
// --------------------------- IQXUDPListener end ------------------------------

    // --------------------------- OnUdpTaskCallBack start ------------------------------

    @Override
    public void onFail(FailTaskResult mFailTask) {

    }

    @Override
    public void onSuccess(String mac, byte tye, byte cmd, int seq) {
        int what = (tye & 255) << 8 | cmd - 1 & 255;
        if (tye != 3 && tye != 0) {
            if (tye == 1 && cmd == 4) {
                Tlog.e(TAG, " receive lanDiscovery Data mac:" + mac + " what:" + Integer.toHexString(what) + " seq:" + seq);
            } else {
                Tlog.e(TAG, " receiveData mac:" + mac + " what:" + Integer.toHexString(what) + " seq:" + seq);
                this.mScmDeviceUtils.getScmDevice(mac).receiveOnePkg(what, seq);
            }
        } else {
            Tlog.e(TAG, " receive report Data mac:" + mac + " what:" + Integer.toHexString(what) + " seq:" + seq);
        }
    }

    /**
     * 心跳回复
     *
     * @param mac
     * @param result
     */
    @Override
    public void onHeartbeatResult(String mac, boolean result) {

        this.mScmDeviceUtils.getScmDevice(mac).onReceiveHeartbeat(result);
        ControlDevice controlDevice = this.mControlDeviceUtil.get(mac);
        if (controlDevice != null) {
            controlDevice.receiveHeartbeat(result);
            if (result && !controlDevice.canLanCom()) {
                String userId = QXUserManager.getInstance().getUserId();
                int token = this.getToken(userId, mac);
                controlDevice.lanDeviceDiscovery(token, userId);
            }
        }

    }


    private void saveToekn(String userid, String mac, int token) {
        tokenMap.put(userid + mac, token);
        DeviceBean deviceBeanByMac = QXDeviceManager.getInstance().getDeviceBeanByMac(mac);
        deviceBeanByMac.setToken(token);
        QXDeviceManager.getInstance().onUpdate(deviceBeanByMac, true);

    }

    private int getToken(String userid, String mac) {
        Integer token = tokenMap.get(userid + mac);
        if (token != null) {
            return token;
        }

        DeviceBean deviceBeanByMac = QXDeviceManager.getInstance().getDeviceBeanByMac(mac);
        if (deviceBeanByMac != null) {
            token = deviceBeanByMac.getToken();
        } else {
            token = 0;
        }
        tokenMap.put(userid + mac, token);
        return token;
    }


    /**
     * 设备发现
     *
     * @param id
     * @param result
     * @param devicebean
     */
    @Override
    public void onDeviceDiscoveryResult(String id, boolean result, DeviceBean devicebean) {

        QXLog.d(TAG, "find Device id = " + id + " result = " + result + " deviceBean = " + devicebean);
        String userId = QXUserManager.getInstance().getUserId();

        String mac = devicebean.getMac();

        ScmDevice scmDevice = mScmDeviceUtils.getScmDevice(mac);
        scmDevice.putIp(devicebean.getIp());


        mDiscoveryDeviceLst.deviceDiscoveryUpdateDevice(devicebean);

        //局域网发现了设备 尝试自动连接
        ControlDevice controlDevice = mControlDeviceUtil.get(devicebean.getMac());

        if (controlDevice != null) {

            int token = getToken(userId, mac);
            controlDevice.lanDeviceDiscovery(token, userId);
        }


        DeviceBean deviceBeanByMac = QXDeviceManager.getInstance().getDeviceBeanByMac(devicebean.getMac());

        if (deviceBeanByMac != null) {


            boolean isNeedToWanBind = false;
            boolean isNeedToLanBInd = false;
            if (devicebean.isWanBind() && deviceBeanByMac.isWanBind() && !deviceBeanByMac.isLanBind()) {

                isNeedToLanBInd = true;
            } else if (devicebean.isLanBind() && deviceBeanByMac.isLanBind() && !deviceBeanByMac.isWanBind()) {

                isNeedToWanBind = true;
            }

            //更新已绑定的设备的rssi 及 name
            int mLastRSSI = deviceBeanByMac.getRssi();
            int mLastPort = deviceBeanByMac.getPort();
            int mLastMainVersion = deviceBeanByMac.getMainVersion();
            int mLastSubVersion = deviceBeanByMac.getSubVersion();
            boolean mLastLanBind = deviceBeanByMac.isLanBind();
            boolean mIsRemoteState = deviceBeanByMac.isWanState();
            int mLastProduct = deviceBeanByMac.getProduct();
            int mLastCustomer = deviceBeanByMac.getCustomer();
            boolean mLastIsAdmin = deviceBeanByMac.isAdmin();
            boolean mLastIsHasAdmin = deviceBeanByMac.isHasAdmin();
            boolean mLastIsHasActive = deviceBeanByMac.isActivateState();
            int mLastModel = deviceBeanByMac.getModel();
            String mLastName = deviceBeanByMac.getName() == null ? "" : deviceBeanByMac.getName();
            String mLastSSID = deviceBeanByMac.getSsid() == null ? "" : deviceBeanByMac.getSsid();
            String mLastBssid = deviceBeanByMac.getBssid() == null ? "" : deviceBeanByMac.getBssid();
            String mLastIp = deviceBeanByMac.getIp() == null ? "" : deviceBeanByMac.getIp();


            boolean isChange = false;
            //如果是wifiRssi变化不大，则不更新数据，如果从 -57 到 -58 类似这样的波动
            if (Math.abs(mLastRSSI - devicebean.getRssi()) > 5) {
                Log.w(TAG, "rssi change lastRssiLevel = " + mLastRSSI + " lanRssiLevel = " + devicebean.getRssi());
                deviceBeanByMac.setRssi(devicebean.getRssi());
                isChange = true;
            }


            if (!mLastSSID.equals(devicebean.getSsid() == null ? "" : devicebean.getSsid())) {
                Log.w(TAG, "ssid change lastSSID = " + mLastSSID + " lanSsid = " + devicebean.getSsid());
                deviceBeanByMac.setSsid(devicebean.getSsid());
                isChange = true;
            }

            if (!mLastBssid.equals(connectedWiFiInfo.bssid == null ? "" : connectedWiFiInfo.bssid)) {
                Log.w(TAG, "bssid change lastBssid = " + mLastBssid + " lanBssid = " + connectedWiFiInfo.bssid);
                deviceBeanByMac.setBssid(connectedWiFiInfo.bssid);
                isChange = true;
            }

            if (!mLastName.equals(devicebean.getName() == null ? "" : devicebean.getName())) {
                Log.w(TAG, "name change lastName = " + mLastName + " lanName = " + devicebean.getName());
                deviceBeanByMac.setName(devicebean.getName());
                isChange = true;
            }

            if (!mLastIp.equals(devicebean.getIp() == null ? "" : devicebean.getIp())) {
                Log.w(TAG, "ip change lastIP = " + mLastIp + " lanName = " + devicebean.getName());
                deviceBeanByMac.setIp(devicebean.getIp());
                isChange = true;
            }

            if (mLastPort != devicebean.getPort()) {
                Log.w(TAG, "port change lastPort = " + mLastPort + " lanPort = " + devicebean.getPort());
                deviceBeanByMac.setPort(devicebean.getPort());
                isChange = true;
            }

            if (mLastLanBind != devicebean.isLanBind()) {
                Log.w(TAG, "bind change lastLanBind = " + mLastLanBind + " lanBin = " + devicebean.isLanBind());
                deviceBeanByMac.setLanBind(devicebean.isLanBind());
                deviceBeanByMac.setLanBindingtime(System.currentTimeMillis());
                isChange = true;
            }

            if (mLastMainVersion != devicebean.getMainVersion()
                    || mLastSubVersion != devicebean.getSubVersion()) {
                Log.w(TAG, "version change last version = " + (mLastMainVersion + "." + mLastSubVersion) + " lanVersion = " + (devicebean.getMainVersion() + "." + devicebean.getSubVersion()));
                deviceBeanByMac.setSubVersion(devicebean.getSubVersion());
                deviceBeanByMac.setMainVersion(devicebean.getMainVersion());
                isChange = true;
            }

            if (mIsRemoteState != devicebean.isWanState()) {
                Log.w(TAG, "server connect change  last state = " + mIsRemoteState + " lan remote state = " + devicebean.isWanState());
                deviceBeanByMac.setWanState(devicebean.isWanState());
                isChange = true;
            }

            if (mLastProduct != devicebean.getProduct()) {
                Log.w(TAG, "produce change lastProduct = " + mLastProduct + " lanProduct = " + devicebean.getProduct());
                deviceBeanByMac.setProduct(devicebean.getProduct());
                isChange = true;
            }
            if (mLastCustomer != devicebean.getCustomer()) {
                Log.w(TAG, "customer change lastCustomer = " + mLastCustomer + " lanCustomer = " + devicebean.getCustomer());
                deviceBeanByMac.setCustomer(devicebean.getCustomer());
                isChange = true;
            }

            if (mLastIsHasActive != devicebean.isActivateState()) {
                Log.w(TAG, "hasActive change mLastIsHasActive = " + mLastIsHasActive + " lanHasActive = " + devicebean.isActivateState());
                deviceBeanByMac.setActivateState(devicebean.isActivateState());
                isChange = true;
            }

            if (mLastIsAdmin != devicebean.isAdmin()) {
                Log.w(TAG, "isAdmin change mLastIsAdmin = " + mLastIsAdmin + " lanIsAdimin = " + devicebean.isAdmin());
                deviceBeanByMac.setAdmin(devicebean.isAdmin());
                isChange = true;
            }


            if (mLastIsHasAdmin != devicebean.isHasAdmin()) {
                Log.w(TAG, "hasAdmin change mLastIsHasAdmin= " + mLastIsHasAdmin + " lanHasAdmin = " + devicebean.isHasAdmin());
                deviceBeanByMac.setHasAdmin(devicebean.isHasAdmin());
                isChange = true;

            }

            if (mLastModel != devicebean.getModel()) {
                Log.w(TAG, "model change lastModel = " + mLastModel + " lanModel = " + devicebean.getModel());
                deviceBeanByMac.setModel(devicebean.getModel());
                isChange = true;
            }


            if (isChange) {
                QXDeviceManager.getInstance().onUpdate(deviceBeanByMac, true);
                if (busiResultListener != null) {
                    busiResultListener.onDeviceBeanStateChange(deviceBeanByMac);
                }
            }

            // 如果发现的设备已经绑定，并且已经连接状态为false ，就且正在控制 就 自动连接
            if (!deviceBeanByMac.isLanState() && mControlDeviceUtil.containsKey(deviceBeanByMac.getMac())) {
                Log.e(TAG, "自动连接 mac = " + deviceBeanByMac.getMac() + "  sn = " + deviceBeanByMac.getSn());
                toConnectDevice(deviceBeanByMac, null);
            }


            if (isNeedToLanBInd) {
                //广域网已经绑定，局域网未绑定，自动进行局域网绑定
                Log.d(TAG, "auto to lanbind");
                lanBind(new BindDeviceReq(devicebean.getMac(), ""));

            }
            if (isNeedToWanBind) {
                //局域网已经绑定，广域网未绑定，自动进行广域网绑定
                Log.d(TAG, "auto to wanbind");
                wanBind(new BindDeviceReq(deviceBeanByMac.getSn()), null);
            }

        } else {
            devicebean.setLanBind(false);
            devicebean.setWanBind(false);
            deviceBeanByMac = devicebean;

        }

        if (busiResultListener != null && needCallbackDiscovery) {
            busiResultListener.onDiscoveryResult(deviceBeanByMac);
        }


    }


    /**
     * 设备开始发现
     */
    @Override
    public void onDiscoveryStart() {
        if (busiResultListener != null && needCallbackDiscovery) {
            busiResultListener.onDiscoveryStart();
        }
    }

    /**
     * 设备发现结果
     */
    @Override
    public void onDiscoveryStop() {
        if (needCallbackDiscovery && busiResultListener != null) {
            needCallbackDiscovery = false;
            busiResultListener.onDiscoveryStop();
        }
    }

    /**
     * 局域网绑定
     *
     * @param result
     * @param mLanBindingDevice
     */
    @Override
    public void onLanBindResult(boolean result, DeviceBean mLanBindingDevice) {


        DeviceBean lanDeviceInfo = this.mDiscoveryDeviceLst.getDeviceBeanByMac(mLanBindingDevice.getMac());
        if (lanDeviceInfo != null && result) {
            lanDeviceInfo.setSn(mLanBindingDevice.getSn());
            lanDeviceInfo.setMac(mLanBindingDevice.getMac());
            if (lanDeviceInfo.getName() == null) {
                lanDeviceInfo.setName(mLanBindingDevice.getMac());
            }

            lanDeviceInfo.setAdmin(mLanBindingDevice.isAdmin());
            lanDeviceInfo.setCpuInfo(mLanBindingDevice.getCpuInfo());
            lanDeviceInfo.setLanBind(true);
            lanDeviceInfo.setUserId(mLanBindingDevice.getUserId());

        }


        boolean mlastBindState = false;
        Log.d(TAG, "onLanBindResult result = " + result + " landeviceinfo = " + lanDeviceInfo);

        if (!result) {
            Log.e(TAG, "绑定失败");
            return;
        }

        //局域网绑定成功 保存状态
        DeviceBean deviceBeanByMac = QXDeviceManager.getInstance().getDeviceBeanByMac(lanDeviceInfo.getMac());

        if (deviceBeanByMac == null) {
            //没有绑定过
            deviceBeanByMac = lanDeviceInfo;
        } else {
            mlastBindState = deviceBeanByMac.isLanBind();
        }
        deviceBeanByMac.setLanBind(true);
        deviceBeanByMac.setLanBindingtime(System.currentTimeMillis());

        QXDeviceManager.getInstance().onLanBindReulst(deviceBeanByMac);

        onResultNeedRequestToken(lanDeviceInfo.getMac(), QXUserManager.getInstance().getUserId());

        //回调绑定成功
        if (busiResultListener != null) {
            if (needCallbacBindResult) {
                BindDeviceResp resp = new BindDeviceResp(1, deviceBeanByMac);
                busiResultListener.onBindResult(resp);
                needCallbacBindResult = false;
            }
            if (!mlastBindState) {
                busiResultListener.onDeviceBeanStateChange(deviceBeanByMac);
            }
        }
        //判断是否已经广域网绑定，如果没有将自动进行广域网绑定
        if (!deviceBeanByMac.isWanBind()) {

            BindDeviceReq req = new BindDeviceReq(deviceBeanByMac.getSn());
            wanBind(req, null);
        }

    }

    /**
     * token 失效
     *
     * @param mac
     */
    @Override
    public void onTokenInvalid(String mac) {
        Tlog.e(TAG, " onTokenInvalid " + mac);
        ControlDevice controlDevice = this.mControlDeviceUtil.get(mac);
        if (controlDevice != null) {
            this.getToken(QXUserManager.getInstance().getUserId(), mac);
            controlDevice.onTokenInvalid(-1, QXUserManager.getInstance().getUserId());

            this.callbackDeviceLanStateChange(false, mac);
        }

    }

    private void callbackDeviceLanStateChange(boolean state, String mac) {

        DeviceBean deviceBeanByMac = QXDeviceManager.getInstance().getDeviceBeanByMac(mac);

        Log.d(TAG, "callbackDeviceBeanStatusChange  deviceBeanByMac = " + deviceBeanByMac);
        Log.d(TAG, "callbackDeviceBeanStatusChange  state = " + state + "mac = " + mac);
        if (deviceBeanByMac != null) {
            deviceBeanByMac.setLanState(state);
            QXDeviceManager.getInstance().onUpdate(deviceBeanByMac, true);
            if (busiResultListener != null) {
                busiResultListener.onDeviceBeanStateChange(deviceBeanByMac);
            }
        }

        if (needCallbackControlDevice && busiResultListener != null) {
            if (state) {
                busiResultListener.onConnectDeviceResult(new ConnectDeviceResp(1, deviceBeanByMac));
            } else {
                if (deviceBeanByMac.isCanCommunicate()) {
                    busiResultListener.onConnectDeviceResult(new ConnectDeviceResp(1, deviceBeanByMac));
                } else {
                    busiResultListener.onConnectDeviceResult(new ConnectDeviceResp(0, deviceBeanByMac, QXError.ERROR_CONN_DEVICE_CAN_NOT_COMMUNICATE));
                }
            }
            needCallbackControlDevice = false;
        }

    }

    /**
     * 局域网解绑
     *
     * @param result
     * @param mLanBindingDevice
     */
    @Override
    public void onLanUnBindResult(boolean result, DeviceBean mLanBindingDevice) {

        QXLog.d(TAG, "onLanUnBindResult result = " + result + " mLanBindingDevice = " + mLanBindingDevice);

        DeviceBean deviceBeanByMac = QXDeviceManager.getInstance().getDeviceBeanByMac(mLanBindingDevice.getMac());
        if (deviceBeanByMac != null) {
            QXDeviceManager.getInstance().deleteLocalDevice(deviceBeanByMac);
        }


    }

    /**
     * 请求token
     *
     * @param result
     * @param mac
     * @param random
     * @param token
     */
    @Override
    public void onRequestTokenResult(boolean result, String mac, int random, int token) {

        if (result) {
            ScmDevice scmDevice = this.mScmDeviceUtils.getScmDevice(mac);
            int requestRandom = scmDevice.getRequestRandom();
            Tlog.e(TAG, " requestRandom:" + requestRandom + " " + random);
            if (requestRandom == random - 1) {
                scmDevice.setToken(token);
                QXUdpDataCreater.getInstance().putToken(mac, token);
                this.saveToekn(QXUserManager.getInstance().getUserId(), mac, token);
                if (this.mControlDeviceUtil.containsKey(mac)) {
                    ControlDevice controlDevice = this.mControlDeviceUtil.get(mac);
                    if (controlDevice != null) {
                        controlDevice.onResponseToken(QXUserManager.getInstance().getUserId(), token);
                    }
                }
            }
        }

    }

    /**
     * 请求连接
     *
     * @param result
     */
    @Override
    public void onConnectResult(boolean result, String mac) {
        ScmDevice scmDevice = this.mScmDeviceUtils.getScmDevice(mac);
        scmDevice.setConResult(result);
        ControlDevice mControlDevice = this.mControlDeviceUtil.get(mac);
        if (mControlDevice != null) {
            if (result) {
                this.mScmDeviceUtils.getScmDevice(mac).connected();
                this.mScmDeviceUtils.showConnectDevice();
                DeviceBean deviceBeanByMac = this.mDiscoveryDeviceLst.getDeviceBeanByMac(mac);
                mControlDevice.responseConnected(deviceBeanByMac != null && IpUtil.ipMatches(deviceBeanByMac.getIp()));
            } else {
                mControlDevice.responseConnectedFail(QXUserManager.getInstance().getUserId());
            }
        }
    }

    /**
     * 休眠
     *
     * @param result
     * @param id
     */
    @Override
    public void onSleepResult(boolean result, String id) {

    }

    @Override
    public void onRemoveCmd(String id, byte paramType, byte paramCmd, int seq) {
        Tlog.e(TAG, " onRemoveCmd Data mac:" + id + " what:" + Integer.toHexString(paramType) + "-" + Integer.toHexString(paramCmd) + " seq:" + seq);
        int what = (paramType & 255) << 8 | paramCmd - 1 & 255;
        this.mScmDeviceUtils.getScmDevice(id).receiveOnePkg(what, seq);
    }

    // --------------------------- OnUdpTaskCallBack end ------------------------------


    /**
     * 订阅用户相关主题
     */
    private void subUserOrSnTopic() {


        final List<String> topics = new ArrayList<>();

        String snTopic = TopicConsts.getSnTopic(getApp());
        if (getQxMqtt() != null && getQxMqtt().getQXMqttConnectState() == ServerConnectState.CONNECTED) {


            if (!isSub(snTopic)) {
                topics.add(snTopic);
            }

            String userId = QXUserManager.getInstance().getUserId();
            if (!TextUtils.isEmpty(userId)) {
                String userTopic = TopicConsts.getUserTopic(userId);
                if (!isSub(userTopic)) {
                    topics.add(userTopic);
                }
            }
            if (topics.size() > 0) {
                getQxMqtt().subscribeSync(topics, new IQXCallListener() {
                    @Override
                    public void onSuccess() {
                        for (String topic : topics) {
                            QXSpController.setIsSub(getQxMqtt().getClient().getClientId(), topic, true);
                        }
                        QXTimerUtil.close("subUserOrSnTopic");
                    }

                    @Override
                    public void onFailed(QXError error) {
                        //3秒后重试
                        QXTimerUtil.schedule("subUserOrSnTopic", new TimerTask() {
                            @Override
                            public void run() {
                                subUserOrSnTopic();
                            }
                        }, 3000);
                    }
                });
            } else {
                Log.e(TAG, "topics has aready sub");
            }
        }
    }


    /**
     * 订阅好友遗嘱 主题
     */
    private void subFriendReportTopic() {
        final String taskKey = "subFriendReportTopic";
        if (getQxMqtt() != null && getQxMqtt().getQXMqttConnectState() == ServerConnectState.CONNECTED) {

            List<String> subTopic = null;
            List<DeviceBean> subDevice = null;

            String userId = QXUserManager.getInstance().getUserId();
            if (!TextUtils.isEmpty(userId)) {
                List<DeviceBean> deviceBeanList = QXDeviceManager.getInstance().getDeviceBeanList();

                if (deviceBeanList != null && deviceBeanList.size() > 0) {
                    subTopic = new ArrayList<>();
                    subDevice = new ArrayList<>();
                    for (DeviceBean deviceBean : deviceBeanList) {

                        if (!deviceBean.isSub()) {
                            subTopic.add(TopicConsts.getSubFriendWillTopic(deviceBean.getSn()));
                            subTopic.add(TopicConsts.getSubFriendReportTopic(deviceBean.getSn()));
                            subDevice.add(deviceBean);
                        }

                    }
                }

                if (subTopic == null || subTopic.size() == 0) {
                    Log.e(TAG, "FriendReport has aready sub or no friend");
                    return;
                }
                final List<DeviceBean> finalsubDevice = subDevice;
                getQxMqtt().subscribeSync(subTopic, new IQXCallListener() {

                    @Override
                    public void onSuccess() {
                        for (DeviceBean deviceBean : finalsubDevice) {
                            deviceBean.setSub(true);
                            QXDeviceManager.getInstance().onUpdate(deviceBean, true);
                        }
                        QXTimerUtil.close(taskKey);
                    }

                    @Override
                    public void onFailed(QXError error) {
                        //3秒后重试
                        QXTimerUtil.schedule(taskKey, new TimerTask() {
                            @Override
                            public void run() {
                                subFriendReportTopic();
                            }
                        }, 3000);
                    }
                });
            }
        }


    }

    private boolean isSub(String topic) {
        return QXSpController.isSub(getQxMqtt().getClient().getClientId(), topic);
    }

    /**
     * 取消订阅好友遗嘱 主题
     */
    private void unSubFriendReportTopic(final String sn) {

        final String taskKay = "unSubFriendReportTopic";
        if (getQxMqtt() != null && getQxMqtt().getQXMqttConnectState() == ServerConnectState.CONNECTED) {


            List<String> unSubTopic = null;

            final String userId = QXUserManager.getInstance().getUserId();
            if (!TextUtils.isEmpty(userId)) {

                final DeviceBean deviceBeanBySn = QXDeviceManager.getInstance().getDeviceBeanBySn(sn);
                unSubTopic = new ArrayList<>();
                unSubTopic.add(TopicConsts.getSubFriendReportTopic(sn));
                unSubTopic.add(TopicConsts.getSubFriendWillTopic(sn));

                QXLog.d(TAG, "allUnSubFriendTopics = " + unSubTopic);

                getQxMqtt().unSubscribe(unSubTopic, new IQXCallListener() {


                    @Override
                    public void onSuccess() {
                        if (deviceBeanBySn != null) {
                            deviceBeanBySn.setSub(false);
                            QXDeviceManager.getInstance().onUpdate(deviceBeanBySn, true);
                        }

                        QXTimerUtil.close(taskKay);
                    }

                    @Override
                    public void onFailed(QXError error) {
                        //3秒后重试
                        QXTimerUtil.schedule(taskKay, new TimerTask() {
                            @Override
                            public void run() {
                                unSubFriendReportTopic(sn);
                            }
                        }, 3000);
                    }
                });
            }
        }
    }


    //------------------- OnMqttTaskCallBack start----------------------


    @Override
    public void onLoginResult(Login.Resp resp) {
        if (resp.getResult() == resp.RESULT_SUCCESS) {
            //清空缓存的设备列表
            QXDeviceManager.getInstance().release();

            //重置登录状态
            QXUserManager.getInstance().resetUser();

            //保存本地登录状态
            UserBean userByUserid = QXDBManager.getInstance().getUserByUserid(resp.getContent().getUserid());
            if (userByUserid == null) {
                userByUserid = new UserBean();
            }
            userByUserid = userByUserid.fromLoginResp(resp);

            QXUserManager.getInstance().addOrUpdateUser(userByUserid);


            //订阅 userid相关主题
            subUserOrSnTopic();

            //订阅对应设备主题
            subFriendReportTopic();

            //获取一次用户信息

            BaseData userInfoData = QXMqttDataCreater.getUserInfoData();
            doSend(userInfoData, null);

            if (QXParamManager.getInstance().isUdpParamInit()) {

                initUdp();

            }


        }

        if (busiResultListener != null) {
            busiResultListener.onLoginResult(resp);

        }
    }

    @Override
    public void onMessageArrived(String topic, byte[] message) {

        if (busiResultListener != null) {
            busiResultListener.onMessageArrive(new BaseData(topic, message));
        }

    }

    @Override
    public void onActivateResult(Activate.Resp resp) {
        QXSpController.setIsActivite(true);
        checkAreNode();
        reportIp();
        if (busiResultListener != null) {
            busiResultListener.onActiviteResult(resp);
            busiResultListener.onServerConnected();
        }
    }

    @Override
    public void onHardwareActivateResult(Activate.Resp resp) {
        if (busiResultListener != null) {
            busiResultListener.onHardwareActivateResult(resp);
        }
    }

    @Override
    public void onUpdateDeviceInfoResult(UpdateDeviceInfo.Resp resp) {
        if (needCallBackUpdateDeviceInfo) {
            if (busiResultListener != null) {
                busiResultListener.onUpdateDeviceInfoResult(resp);
                needCallBackUpdateDeviceInfo = false;
            }
        }

    }

    @Override
    public void onGetBrokerHostesult(BrokerHost.Resp resp) {
        if (resp.getResult() == BaseMessage.RESULT_SUCCESS) {


            BrokerHost.Resp.ContentBean areaNodes = resp.getContent();
            int size = areaNodes.getNode().size();
            if (size == 0) {
                QXLog.d(TAG, "没有获取到区域节点信息，60秒后再次获取");
                checkAreNode();
                return;
            }

            //保存 节点信息
            AreaNodesManager.getInstance().setSpAreaNodes(areaNodes);
            //保存此次获取节点信息的时候， sdk会在每次重启后去判断这个时间是否大于更新周期，如果大于周期则重新获取
            QXSpController.setLastGetBrokerHostrespTime(System.currentTimeMillis());

            int cycle = areaNodes.getCycle();
            List<BrokerHost.Resp.ContentBean.NodeBean> nodes = areaNodes.getNode();

            boolean isNeedChangHost = false;

            String currentConnUrl = getQxMqtt().getClient().getServerURI();

            for (BrokerHost.Resp.ContentBean.NodeBean nodeBean : nodes) {
                //当前连接的节点权值已经为0，不可用 需要立即切换节点
                if (nodeBean.getWeight() <= 0 && nodeBean.getServer_domain().equals(currentConnUrl)) {
                    isNeedChangHost = true;
                }
            }

            BrokerHost.Resp.ContentBean cacheAreaNodes = AreaNodesManager.getInstance().getCacheAreaNodes();
            if (cacheAreaNodes == null) {
                AreaNodesManager.getInstance().setCacheAreaNodes(areaNodes);
            }


            if (isNeedChangHost) {
                for (BrokerHost.Resp.ContentBean.NodeBean nb : cacheAreaNodes.getNode()) {
                    if (nb.getServer_domain().equals(currentConnUrl)) {
                        nb.setWeight(0);
                        QXLog.d(TAG, "发现新的权值列表中，当前连接的节点权值是0，为不可用节点，立即切换节点");
                        getQxMqtt().disconnectAndReconnect();
                        break;
                    }
                }
            }


            final String taskKey = "checkGetAreaNode";
            QXTimerUtil.schedule(taskKey, new TimerTask() {
                @Override
                public void run() {
                    AreaLocation areaLocation = QXLocationManager.getInstance().getLocation();
                    BrokerHost.Req req = new BrokerHost.Req(areaLocation.getQuery());
                    doSend(QXMqttDataCreater.getBrokerHostData(req), null);

                    QXTimerUtil.close(taskKey);
                }
            }, cycle * 1000);
        }

    }

    @Override
    public void onGetIdentifyCodeResult(GetIdentifyCode.Resp resp) {

        if (busiResultListener != null) {
            busiResultListener.onGetIdentifyCodeResult(resp);

        }
    }

    @Override
    public void onCheckIdentifyCodeResult(CheckIdentifyCode.Resp resp) {
        if (busiResultListener != null) {
            busiResultListener.onCheckIdetifyResult(resp);
        }
    }


    @Override
    public void onRegisterResult(Register.Resp resp) {

        if (busiResultListener != null) {
            busiResultListener.onRegisterResult(resp);

        }
    }

    @Override
    public void onSendEmail(SendEmail.Resp resp) {
        if (busiResultListener != null) {
            busiResultListener.onSendEmailResult(resp);
        }
    }

    @Override
    public void onUpdateUserInfoResult(UpdateUserInfo.Resp resp) {
        if (resp.getResult() == BaseMessage.RESULT_SUCCESS) {

            //保存本地用户信息
            UserBean userByUserid = QXDBManager.getInstance().getUserByUserid(resp.getContent().getUserid());
            if (userByUserid != null) {
                userByUserid = userByUserid.fromUpdateUserInfoResp(resp);
                resp.setUserBean(userByUserid);
                QXUserManager.getInstance().addOrUpdateUser(userByUserid);
            }

        }

        if (busiResultListener != null) {
            busiResultListener.onUpdateUserInfoResult(resp);
        }
    }

    @Override
    public void onGetUserInfoResult(GetUserInfo.Resp resp) {
        if (resp.getResult() == BaseMessage.RESULT_SUCCESS) {

            //保存本地用户信息
            UserBean userByUserid = QXDBManager.getInstance().getUserByUserid(resp.getContent().getUserid());
            if (userByUserid != null) {
                userByUserid = userByUserid.fromGetUserInfoResp(resp);
                resp.setUserBean(userByUserid);
                QXUserManager.getInstance().addOrUpdateUser(userByUserid);

            }


        }

        if (busiResultListener != null && needCallBackGetUserInfo) {
            busiResultListener.onGetUserInfoResult(resp);
            needCallBackGetUserInfo = false;
        }
    }

    @Override
    public void onGetLatestAppVersionResult(GetLatestAppVersion.Resp resp) {
        if (busiResultListener != null) {
            busiResultListener.onGetLatestVersionResult(resp);
        }
    }

    @Override
    public void onResetLoginPwdResult(ResetLoginPwd.Resp resp) {
        if (busiResultListener != null) {
            busiResultListener.onResetLoginPwdResult(resp);
        }
    }

    @Override
    public void onUpdateLoginPwdResult(UpdateLoginPwd.Resp resp) {
        if (busiResultListener != null) {
            busiResultListener.onUpdateLoginPwdResult(resp);
        }
    }

    @Override
    public void onUpdateRemarkResult(UpdateRemark.Resp resp) {

        if (resp.getResult() == BaseMessage.RESULT_SUCCESS) {
            DeviceBean deviceBeanBySn = QXDeviceManager.getInstance().getDeviceBeanBySn(resp.getContent().getFid());
            if (deviceBeanBySn != null) {
                deviceBeanBySn.setRemark(resp.getContent().getRemark());
                QXDeviceManager.getInstance().onUpdate(deviceBeanBySn, true);
            }
        }

        if (busiResultListener != null) {
            busiResultListener.onUpdateRemarkResult(resp);
        }
    }

    @Override
    public void onUnActivateResult(UnActivate.Resp resp) {
        if (resp.getResult() == BaseMessage.RESULT_SUCCESS) {

            //是自己的注销激活消息
            if (QXMqttConfig.getSn(QX.getInstance().getApp()).equalsIgnoreCase(resp.getContent().getId())) {
                QXSpController.clearAllSp();
                QXDBManager.getInstance().deleteAllTable();
                getQxMqtt().release();

            }
        }

        if (busiResultListener != null) {
            busiResultListener.onUnActiviteResult(resp);
        }
    }

    @Override
    public void onUnBindResult(UnBind.Resp resp) {

        String friendId = "";

        UserBean userBean = QXUserManager.getInstance().getUser();
        String id = "";
        if (userBean != null) {
            id = userBean.getUserId();
        } else {
            id = QXMqttConfig.getSn(QX.getInstance().getApp());
        }

        if (resp.getResult() == BaseMessage.RESULT_SUCCESS) {
            if (id.equals(resp.getContent().getBeunbindingid())) {
                friendId = resp.getContent().getUnbindingid();
            } else {
                friendId = resp.getContent().getBeunbindingid();
            }

            if (userBean != null) {
                unSubFriendReportTopic(friendId);
            }


            QXDeviceManager.getInstance().onUnBindResult(new DeviceBean(friendId));

            if (busiResultListener != null) {
                busiResultListener.onUnBindResult(new UnBindDeviceResp(1, friendId));
            }

        } else {


            QXLog.e(TAG, "解绑失败 " + resp.getContent().getErrmsg());

            if (id.equals(resp.getContent().getBeunbindingid())) {
                friendId = resp.getContent().getErrcontent().getUnbindingid();
            } else {
                friendId = resp.getContent().getErrcontent().getBeunbindingid();
            }

            if (busiResultListener != null) {
                busiResultListener.onUnBindResult(new UnBindDeviceResp(0, resp.getContent().getErrcode(), resp.getContent().getErrmsg(), friendId));
            }
        }


    }

    @Override
    public void onBindResult(Bind.Resp resp) {

        UserBean userBean = QXUserManager.getInstance().getUser();
        String id = "";
        if (userBean == null) {
            id = QXMqttConfig.getSn(QX.getInstance().getApp());
        } else {
            id = userBean.getUserId();
        }

        Bind.Resp.ContentBean.BebindingBean bebinding = null;
        if (resp.getResult() == BaseMessage.RESULT_SUCCESS) {

            if (id.equals(resp.getContent().getBebinding().getId())) {

                Bind.Resp.ContentBean.BindingBean binding = resp.getContent().getBinding();
                bebinding = new Bind.Resp.ContentBean.BebindingBean(binding.getId(), binding.getApptype(), binding.getFeatureid(), binding.getTopic(), binding.getConnstatus(), binding.getMac());
            } else {
                bebinding = resp.getContent().getBebinding();
            }
            QXDeviceManager.getInstance().onBindResult(bebinding);
            if (userBean != null) {
                subFriendReportTopic();
            }


            boolean mlastBindState;
            DeviceBean deviceBeanBySn = QXDeviceManager.getInstance().getDeviceBeanBySn(bebinding.getId());
            if (deviceBeanBySn == null) {
                //没有绑定过
                deviceBeanBySn = new DeviceBean();
                deviceBeanBySn.fromWanDeviceInfo_8002(bebinding);
            }
            mlastBindState = deviceBeanBySn.isWanBind();

            deviceBeanBySn.setWanBind(true);
            deviceBeanBySn.setWanBindtime(System.currentTimeMillis());

            QXDeviceManager.getInstance().onUpdate(deviceBeanBySn, true);


            //回调绑定成功
            if (busiResultListener != null) {
                if (needCallbacBindResult) {
                    BindDeviceResp bindDeviceResp = new BindDeviceResp(1, deviceBeanBySn);
                    busiResultListener.onBindResult(bindDeviceResp);
                    needCallbacBindResult = false;
                }
                if (!mlastBindState) {
                    busiResultListener.onDeviceBeanStateChange(deviceBeanBySn);
                }
            }

            //判断是否已经局域网绑定，如果没有将自动进行局域网绑定
            if (!deviceBeanBySn.isLanBind()) {
                toDiscovery(5 * 3000);
            }


        } else {
            bebinding = new Bind.Resp.ContentBean.BebindingBean();
            bebinding.setId(resp.getContent().getErrcontent().getBebindingid());

            if (busiResultListener != null) {
                busiResultListener.onBindResult(new BindDeviceResp(0, resp.getContent().getErrcode(), resp.getContent().getErrmsg(), new DeviceBean(bebinding.getId())));
            }

        }
    }

    @Override
    public void onGetBindListResult(GetBindList.Resp resp) {


        if (resp.getResult() == BaseMessage.RESULT_SUCCESS) {
            UserBean userBean = QXUserManager.getInstance().getUser();


            //将数据保存或更新至本地
            List<GetBindList.Resp.ContentBean.FriendBean> friends = resp.getContent().getFriends();

            if (friends != null && friends.size() > 0 && userBean != null) {

                //更新本地设备列表
                QXDeviceManager.getInstance().onGetBindDeviceResult(resp.getContent().getFriends());

                toDiscovery(5 * 3000);

                //订阅对应主题
                subFriendReportTopic();

            }

            GetBindListResp re = new GetBindListResp(resp.getResult(), QXDeviceManager.getInstance().getDeviceBeanList());
            if (busiResultListener != null) {
                busiResultListener.onGetBindListResult(re);
            }

        } else {

            GetBindListResp re = new GetBindListResp(resp.getResult(), resp.getContent().getErrcode(), resp.getContent().getErrmsg());
            if (busiResultListener != null) {
                busiResultListener.onGetBindListResult(re);
            }

        }


    }


    @Override
    public void onBindEmailResult(BindEmail.Resp resp) {

        if (resp.getResult() == BaseMessage.RESULT_SUCCESS) {

            UserBean userByUserid = QXDBManager.getInstance().getUserByUserid(resp.getContent().getUserid());
            if (userByUserid != null) {
                userByUserid.setEmail(resp.getContent().getEmail());
                QXUserManager.getInstance().addOrUpdateUser(userByUserid);
            }
        }

        if (busiResultListener != null) {
            busiResultListener.onBindEmailResult(resp);
        }
    }

    @Override
    public void onBindThirdAccountResult(BindThirdAccount.Resp resp) {
        if (busiResultListener != null) {
            busiResultListener.onBindThirdAccountResult(resp);
        }
    }

    @Override
    public void onUnBindThirdAccountResult(UnBindThirdAccount.Resp resp) {

        if (busiResultListener != null) {
            busiResultListener.onUnBindThirdAccountResult(resp);
        }

    }

    @Override
    public void onGetWeatherInfoResult(GetWeatherInfo.Resp resp) {
        if (busiResultListener != null) {
            busiResultListener.onGetWeatherInfoResult(resp);
        }
    }

    @Override
    public void onBindMobileResult(BindMobile.Resp resp) {
        if (resp.getResult() == BaseMessage.RESULT_SUCCESS) {

            UserBean userByUserid = QXDBManager.getInstance().getUserByUserid(resp.getContent().getUserid());
            if (userByUserid != null) {
                userByUserid.setMobile(resp.getContent().getMobile());
                QXUserManager.getInstance().addOrUpdateUser(userByUserid);
            }
        }

        if (busiResultListener != null) {
            busiResultListener.onBindMobileNumResult(resp);
        }
    }

    @Override
    public void onGetAlipayAuthInfoResult(GetAlipayAuthInfo.Resp resp) {
        if (busiResultListener != null) {
            busiResultListener.onGetAlipayAuthInfoResult(resp);
        }
    }

    @Override
    public void onGetRealPayResult(GetRealPayResult.Resp resp) {
        if (busiResultListener != null) {
            busiResultListener.onGetRealOrderPayStatus(resp);
        }
    }

    @Override
    public void onThirdPaymentUnifiedOrderResult(ThirdPaymentUnifiedOrder.Resp resp) {
        if (busiResultListener != null) {
            busiResultListener.onThirdPaymentUnifiedOrderResult(resp);
        }
    }

    @Override
    public void onDeviceOnlineOffLine(int onlineState, String sn) {

        if (busiResultListener != null) {
            DeviceBean deviceBeanBySn = QXDeviceManager.getInstance().getDeviceBeanBySn(sn);
            if (deviceBeanBySn.isWanState() != (onlineState == 1)) {
                deviceBeanBySn.setWanState(onlineState == 1);
                QXDeviceManager.getInstance().onUpdate(deviceBeanBySn, true);

                busiResultListener.onDeviceBeanStateChange(deviceBeanBySn);
            }
        }

    }

    @Override
    public void onPassthrough(Passthrough.Resp resp) {

        if (busiResultListener != null) {
            DeviceBean deviceBeanBySn = QXDeviceManager.getInstance().getDeviceBeanBySn(resp.getFromid());

            String dataStr = resp.getContent();
            try {
                byte[] dataArr = QXStringUtils.hexStr2ByteArr(dataStr.replace(" ", ""));

                callbackPassthrough(new PassthroughResp(1, deviceBeanBySn, dataArr, null));
                return;
            } catch (NumberFormatException e) {
//                e.printStackTrace();
            }
            callbackPassthrough(new PassthroughResp(1, deviceBeanBySn, null, dataStr));

        }


    }

    //------------------- OnMqttTaskCallBack end----------------------


    // ------------------------ IControlWiFi.IWiFiResultCallBack start ------------------

    /**
     * 回调 设备需要重新请求token
     *
     * @param mac
     * @param userID
     */
    @Override
    public void onResultNeedRequestToken(String mac, String userID) {

        requestToken(mac, userID);

    }

    public void appSleep(String mac, String userID, int token) {
        Tlog.v(TAG, " appSleep userID:" + userID + " token:" + token);
        if (userID != null) {
            BaseData mResponseData = QXUdpDataCreater.getInstance().getAppSleep(mac, userID.getBytes(), token);
            Tlog.v(TAG, " appSleep data: " + String.valueOf(mResponseData));
            doSend(mResponseData, null);
        } else {
            Tlog.e(TAG, " appSleep userID=null ");
        }

    }

    public void requestToken(String mac, String userID) {
        int random = (int) ((Math.random() * 9.0D + 1.0D) * 100000.0D);
        Tlog.v(TAG, " requestToken  " + userID + " random:" + random);
        ScmDevice scmDevice = this.mScmDeviceUtils.getScmDevice(mac);
        scmDevice.putRequestTokenRandom(random);
        byte[] bytes = userID != null ? userID.getBytes() : null;
        BaseData mResponseData = QXUdpDataCreater.getInstance().getRequestToken(mac, bytes, random);
        doSend(mResponseData, null);
    }

    /**
     * 回调 设备可以进行连接通信
     *
     * @param mac         设备mac
     * @param loginUserID userid
     * @param token       toktn
     */
    @Override
    public void onResultCanControlDevice(String mac, String loginUserID, int token) {
        String userID = QXUserManager.getInstance().getUserId();
        Tlog.d(TAG, " controlDevice  " + userID + " token:" + Integer.toHexString(token));
        this.mScmDeviceUtils.getScmDevice(mac).setToken(token);
        QXUdpDataCreater.getInstance().putToken(mac, token);
        this.saveToekn(userID, mac, token);
        byte[] bytes = userID != null ? userID.getBytes() : null;
        BaseData mResponseData = QXUdpDataCreater.getInstance().getControlDevice(mac, bytes, token);
        Tlog.d(TAG, " controlDevice data: " + String.valueOf(mResponseData));
        doSend(mResponseData, null);
    }

    /**
     * 回调连接设备结果
     *
     * @param result
     * @param deviceBean
     */
    @Override
    public void onResultConnectDevice(int result, DeviceBean deviceBean) {


        callbackDeviceLanStateChange(result == 1, deviceBean.getMac());


    }

    // ------------------------ IControlWiFi.IWiFiResultCallBack end ------------------


    //------------------- IQXBusi start----------------------

    /**
     * 消息透传
     *
     * @param req
     * @param callListener
     */
    @Override
    public void passthrough(@NonNull final PassthroughReq req, final IQXCallListener callListener) {

        DeviceBean deviceBeanBySn = QXDeviceManager.getInstance().getDeviceBeanBySn(req.getDeviceBean().getSn());
        if (deviceBeanBySn != null) {
            //判断消息是否非法
            if (req.getDataArr() == null) {
                QXCallbackManager.callbackCallResult(false, callListener, QXError.ERROR_SEND_PARAM_INVALIBLE);
                return;
            }

            //判断设备是否可通信
            if (!deviceBeanBySn.isCanCommunicate()) {
                QXCallbackManager.callbackCallResult(false, callListener, QXError.ERROR_SEND_DEVICE_CAN_NOT_COMMUNICATE);
                return;
            }
            //如果局域网连接，尝试局域网发送
            BaseData data = new BaseData(deviceBeanBySn.getIp(), deviceBeanBySn.getPort(), req.getDataArr());
            data.setMac(deviceBeanBySn.getMac());
            data.initUdpMode();
            doSend(data, new IQXCallListener() {
                @Override
                public void onSuccess() {
                    QXCallbackManager.callbackCallResult(true, callListener, null);
                }

                @Override
                public void onFailed(QXError error) {
                    //如果局域网未能发送成功，尝试广域网发送
                    if (!TextUtils.isEmpty(req.getDataStr())) {
                        BaseData passthroughData = QXMqttDataCreater.getPassthroughData(req.getDeviceBean().getSn(), req.getDataStr());
                        doSend(passthroughData, callListener);
                    } else if (req.getDataArr() != null) {
                        BaseData passthroughData = QXMqttDataCreater.getPassthroughData(req.getDeviceBean().getSn(), req.getDataArr());
                        doSend(passthroughData, callListener);
                    }
                }
            });


        } else {
            QXCallbackManager.callbackCallResult(false, callListener, QXError.ERROR_SEND_NO_FID);
        }

    }

    private boolean checkIsInLan(DeviceBean deviceBean) {

        DeviceBean lanDeviceByMac = mDiscoveryDeviceLst.getDeviceBeanByMac(deviceBean.getMac());
        if (lanDeviceByMac == null) {

            DeviceBean lanDeviceByIP = mDiscoveryDeviceLst.getLanDeviceByIP(deviceBean.getIp());
            if (lanDeviceByIP == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 修备注名
     *
     * @param req      对方的sn 跟新的备注名
     * @param listener
     */
    @Override
    public void updateRemark(UpdateRemark.Req req, IQXCallListener listener) {
        doSend(QXMqttDataCreater.getUpdateRemarkData(req), listener);
    }

    /**
     * 与设备进行连接设备
     *
     * @param deviceBean
     * @param callListener
     */
    @Override
    public void connectDevice(@NonNull DeviceBean deviceBean, IQXCallListener callListener) {

        needCallbackControlDevice = true;

        toConnectDevice(deviceBean, callListener);


    }

    private void toConnectDevice(@NonNull DeviceBean deviceBean, IQXCallListener callListener) {

        ControlDevice mControlDevice = mControlDeviceUtil.get(deviceBean.getMac());
        DeviceBean deviceBeanByMac = mDiscoveryDeviceLst.getDeviceBeanByMac(deviceBean.getMac());
        String loginUserID = QXUserManager.getInstance().getUserId();
        int token = this.getToken(loginUserID, deviceBean.getMac());
        if (mControlDevice == null) {
            mControlDevice = new ControlDevice(deviceBean, this);
            this.mControlDeviceUtil.put(deviceBean.getMac(), mControlDevice);
            mControlDevice.controlWiFiDevice(token, loginUserID, deviceBeanByMac != null);
        } else {
            mControlDevice.recontrolWiFiDevice(token, loginUserID, deviceBeanByMac != null);
        }

        QXCallbackManager.callbackCallResult(true, callListener, "");
    }

    /**
     * 断开与设备的连接
     *
     * @param deviceBean
     * @param callListener
     */
    @Override
    public void disConnectDevice(@NonNull DeviceBean deviceBean, IQXCallListener callListener) {


        String mac = deviceBean.getMac();
        String userId = QXUserManager.getInstance().getUserId();
        Tlog.e(TAG, " disControlDevice " + mac);
        ControlDevice mControlDevice = this.mControlDeviceUtil.get(mac);
        if (mControlDevice != null) {
            mControlDevice.disControl();
            this.mControlDeviceUtil.remove(mac);
            int token = this.getToken(userId, mac);
            Tlog.e(TAG, " appSleep " + mac + " token:" + Integer.toHexString(token));
            appSleep(mac, userId, token);
            ScmDevice scmDevice = this.mScmDeviceUtils.getScmDevice(mac);
            scmDevice.disconnected();
        } else {
            this.callbackDeviceLanStateChange(false, mac);
        }


    }

    /**
     * 局域网设备扫描
     *
     * @param timeMillims
     */
    @Override
    public void discovery(long timeMillims) {
        needCallbackDiscovery = true;
        toDiscovery(timeMillims);
    }

    private void toDiscovery(long timeMillims) {
        if (broadcastDiscoveryUtil != null) {
            broadcastDiscoveryUtil.startDiscovery(QXUserManager.getInstance().getUserId(), timeMillims);
        }
    }

    /**
     * 停止局域网设备扫描
     */
    @Override
    public void stopDiscovery() {
        if (broadcastDiscoveryUtil != null) {
            broadcastDiscoveryUtil.stopDiscovery();
        }
    }

    /**
     * 绑定设备
     *
     * @param req
     * @param callListener
     */
    @Override
    public void bindDevice(@NonNull BindDeviceReq req, IQXCallListener callListener) {
        needCallbacBindResult = true;
        if (!TextUtils.isEmpty(req.getSn())) {
            wanBind(req, callListener);
        } else if (!TextUtils.isEmpty(req.getMac())) {
            lanBind(req);
        }

    }

    void lanBind(BindDeviceReq req) {
        String userId = QXUserManager.getInstance().getUserId();
        QXLog.d(TAG, " lanBindDevice  " + req.getMac() + " userID:" + userId);
        byte[] bytes = userId != null ? userId.getBytes() : null;
        int info = (new Bit()).add(0).reserve(1, bytes != null && bytes.length > 0).getDevice();
        byte[] pwdBuf = req.getPwd() != null ? req.getPwd().getBytes() : null;
        BaseData mResponseData = QXUdpDataCreater.getInstance().getBindDevice(req.getMac(), bytes, (byte) info, pwdBuf);
        mResponseData.getRepeatMsgModel().setMaxRepeatTimes(3);
        QXLog.d(TAG, " bindDevice data: " + String.valueOf(mResponseData));
        doSend(mResponseData, null);
    }

    void wanBind(BindDeviceReq req, IQXCallListener callListener) {
        String sn = req.getSn();
        doSend(QXMqttDataCreater.getBindDeviceData(sn), callListener);
    }

    /**
     * 解绑设备
     *
     * @param req
     * @param callListener
     */
    @Override
    public void unBindDevice(@NonNull UnBindDeviceReq req, IQXCallListener callListener) {

        if (!TextUtils.isEmpty(req.getSn())) {
            DeviceBean deviceBeanBySn = QXDeviceManager.getInstance().getDeviceBeanBySn(req.getSn());
            if (deviceBeanBySn != null) {
                wanUnBind(req, callListener);
                if (!TextUtils.isEmpty(deviceBeanBySn.getMac())) {
                    lanUnBind(deviceBeanBySn.getMac(), callListener);
                }
            } else {
                QXCallbackManager.callbackCallResult(false, callListener, QXErrorCode.ERROR_SEND_NO_FID);
            }

        }

    }

    private void lanUnBind(String mac, IQXCallListener callListener) {
        String userId = QXUserManager.getInstance().getUserId();
        Tlog.v(TAG, " unbindDevice  " + mac + " userID:" + userId);
        byte[] bytes = userId != null ? userId.getBytes() : null;
        int info = (new Bit()).remove(0).reserve(1, bytes != null && bytes.length > 0).getDevice();
        BaseData mResponseData = QXUdpDataCreater.getInstance().getBindDevice(mac, bytes, (byte) info, (byte[]) null);
        Tlog.v(TAG, " unbindDevice data: " + String.valueOf(mResponseData));
        doSend(mResponseData, null);
    }

    private void wanUnBind(UnBindDeviceReq req, IQXCallListener callListener) {
        doSend(QXMqttDataCreater.getUnBindDeviceData(req.getSn()), callListener);
    }


    /**
     * 获取最新软件版本
     *
     * @param callListener
     */
    @Override
    public void getLatestVersion(IQXCallListener callListener) {
        doSend(QXMqttDataCreater.getLatestAppVersionData(), callListener);
    }

    /**
     * 修改用户登录密码
     *
     * @param req
     * @param callListener
     */
    @Override
    public void updateLoginPwd(UpdateLoginPwd.Req req, IQXCallListener callListener) {
        doSend(QXMqttDataCreater.getUpdateLoginPwd(req), callListener);
    }

    /**
     * 更新用户信息
     *
     * @param req
     * @param callListener
     */
    @Override
    public void updateUserInfo(UpdateUserInfo.Req req, IQXCallListener callListener) {
        doSend(QXMqttDataCreater.getUpdateUserInfoData(req), callListener);
    }

    /**
     * 查询用户信息
     *
     * @param callListener
     */
    @Override
    public void getUserInfo(IQXCallListener callListener) {
        needCallBackGetUserInfo = true;
        doSend(QXMqttDataCreater.getUserInfoData(), callListener);
    }

    /**
     * 请求发送邮件
     *
     * @param req          邮箱
     * @param callListener
     */
    @Override
    public void sendEmail(SendEmail.Req req, IQXCallListener callListener) {
        doSend(QXMqttDataCreater.getSendEmailData(req), callListener);
    }

    /**
     * 第三方账号登录
     *
     * @param req          第三方登录参数
     * @param callListener
     */
    @Override
    public void loginWithThirdAccount(LoginWithThirdAccount.Req req, IQXCallListener callListener) {
        doSend(QXMqttDataCreater.getLoginWithThirdAccountData(req), callListener);
    }

    /**
     * 登出
     */
    @Override
    public void logout() {

        String snTopic = TopicConsts.getSnTopic(getApp());
        String userTopic = TopicConsts.getUserTopic(QXUserManager.getInstance().getUserId());

        //清除本地的订阅状态
        QXSpController.removeIsSub(QXMqttConfig.getClientId(), snTopic);
        QXSpController.removeIsSub(QXMqttConfig.getClientId(), userTopic);
        //清空上次连接的clientid
        QXMqttConfig.setClientId("");


        //清空用户登录状态
        QXUserManager.getInstance().resetUser();

        //清空缓存的设备列表
        QXDeviceManager.getInstance().release();

        //断开连接并重新以随机clientid连接
        getQxMqtt().disconnectAndReconnect();

        //释放udp资源
        releaseUdp();

        QXLog.e(TAG, "登出成功");
        if (busiResultListener != null) {
            busiResultListener.onLogoutResult(1);
        }
    }

    /**
     * 登录
     *
     * @param req
     * @param callListener
     */
    @Override
    public void login(@NonNull Login.Req req, IQXCallListener callListener) {
        BaseData loginData = QXMqttDataCreater.getLoginData(req);
        doSend(loginData, callListener);
    }

    /**
     * 注册
     *
     * @param req
     * @param callListener
     */
    @Override
    public void register(@NonNull Register.Req req, IQXCallListener callListener) {

        doSend(QXMqttDataCreater.getRegisterData(req), callListener);

    }

    /**
     * 获取手机验证码
     *
     * @param req
     * @param callListener
     */
    @Override
    public void getIdentifyCode(@NonNull GetIdentifyCode.Req req, IQXCallListener callListener) {
        doSend(QXMqttDataCreater.getIdentifyCodeData(req), callListener);
    }

    /**
     * 检验手机验证码
     *
     * @param req
     * @param callListener
     */
    @Override
    public void checkIdentifyCode(@NonNull CheckIdentifyCode.Req req, IQXCallListener callListener) {
        doSend(QXMqttDataCreater.getCheckIdentifyCodeData(req), callListener);
    }

    /**
     * 帮第三方设备激活
     *
     * @param req
     * @param callListener
     */
    @Override
    public void hardwareActivate(@NonNull Activate.Req req, IQXCallListener callListener) {
        BaseData activateData = QXMqttDataCreater.getActivateData(req);
        doSend(activateData, callListener);
    }

    /**
     * 更新设备信息
     *
     * @param req
     * @param callListener
     */
    @Override
    public void updateDeviceInfo(UpdateDeviceInfo.Req req, IQXCallListener callListener) {
        needCallBackUpdateDeviceInfo = true;
        BaseData updateDeviceInfoData = QXMqttDataCreater.getUpdateDeviceInfoData(req);
        doSend(updateDeviceInfoData, callListener);
    }

    /**
     * 重置密码
     *
     * @param req          手机号 或邮箱号
     * @param callListener
     */
    @Override
    public void resetLoginPwd(ResetLoginPwd.Req req, IQXCallListener callListener) {
        doSend(QXMqttDataCreater.getResetLoginPwdData(req), callListener);
    }

    /**
     * 第三方支付 统一下单
     *
     * @param req
     * @param callListener
     */
    @Override
    public void thirdPaymentUnifiedOrder(ThirdPaymentUnifiedOrder.Req req, IQXCallListener callListener) {
        doSend(QXMqttDataCreater.getThirdPaymentUnifiedOrderData(req), callListener);
    }

    /**
     * 查询真实订单支付结果
     *
     * @param req
     * @param callListener
     */
    @Override
    public void getRealOrderPayStatus(GetRealPayResult.Req req, IQXCallListener callListener) {
        doSend(QXMqttDataCreater.getRealOrderPayStatusData(req), callListener);
    }

    /**
     * 查询支付宝认证信息
     *
     * @param authType
     * @param callListener
     */
    @Override
    public void getAlipayAuthInfo(String authType, IQXCallListener callListener) {
        doSend(QXMqttDataCreater.getAlipayAuthInfoData(authType), callListener);
    }

    /**
     * 关联手机号
     *
     * @param req
     * @param callListener
     */
    @Override
    public void bindMobileNum(BindMobile.Req req, IQXCallListener callListener) {
        doSend(QXMqttDataCreater.getBindMobileData(req), callListener);
    }

    /**
     * 解绑第三方账号
     *
     * @param req
     * @param callListener
     */
    @Override
    public void unBindThirdAccount(UnBindThirdAccount.Req req, IQXCallListener callListener) {
        doSend(QXMqttDataCreater.getUnBindThirdAccountData(req), callListener);
    }

    /**
     * 绑定第三方账号
     *
     * @param req
     * @param callListener
     */
    @Override
    public void bindThirdAccount(BindThirdAccount.Req req, IQXCallListener callListener) {
        doSend(QXMqttDataCreater.getBindThirdAccountData(req), callListener);
    }

    /**
     * 查询天气信息
     *
     * @param req
     * @param callListener
     */
    @Override
    public void getWeatherInfo(GetWeatherInfo.Req req, IQXCallListener callListener) {
        doSend(QXMqttDataCreater.getWeatherInfoData(req), callListener);
    }

    /**
     * 绑定邮箱
     *
     * @param req
     * @param callListener
     */
    @Override
    public void bindEmail(BindEmail.Req req, IQXCallListener callListener) {
        doSend(QXMqttDataCreater.getBindEmailData(req), callListener);
    }

    /**
     * 查询 绑定列表 分页
     *
     * @param callListener
     */
    @Override
    public void getBindList(GetBindListReq req, IQXCallListener callListener) {
        if (req.isFromLocal()) {
            callListener.onSuccess();
            if (busiResultListener != null) {
                busiResultListener.onGetBindListResult(new GetBindListResp(1, QXDeviceManager.getInstance().getDeviceBeanList()));
                toDiscovery(5 * 3000);
            }
        } else {
            BaseData bindDeviceListData = QXMqttDataCreater.getBindDeviceListData(new GetBindList.Req(1, 999));
            doSend(bindDeviceListData, callListener);
        }
    }

    //------------------- IQXBusi end----------------------


    //------------------- IQXNetworkListener start----------------------
    @Override
    public void onWifiConnected() {
    }

    @Override
    public void onMobileConnected() {
    }

    @Override
    public void onEthernetConnected() {
    }

    @Override
    public void onUnkownNetwork() {
    }

    @Override
    public void onNetworkStateChange(String networkType, NetworkInfo.State state) {
//        QXLog.d(TAG, "onNetworkStateChange networkType = " + networkType + " state = " + state);
        QXLocationManager.getInstance().setLocation(null);

        if (QXNetworkManager.getInstance().isWifiConnected()) {
            connectedWiFiInfo = QXNetUtils.getConnectedWiFiInfo(getApp());
        }

        if (mControlDeviceUtil != null) {
            mControlDeviceUtil.onNetworkStateChange();
        }
        if (mScmDeviceUtils != null) {
            mScmDeviceUtils.onNetworkChange();
        }
        if (mDiscoveryDeviceLst != null) {
            mDiscoveryDeviceLst.clear();
        }

        QXDeviceManager.getInstance().resetLanState();

        if (networkType.equals("WIFI") && state == NetworkInfo.State.CONNECTED && qxUdp != null && broadcastDiscoveryUtil != null) {
            toDiscovery(5 * 3000);
        }

        if (busiResultListener != null) {
            busiResultListener.onNetworkChange(networkType, state);
        }

    }
    //------------------- IQXNetworkListener end ----------------------

    protected void callbackPassthrough(PassthroughResp resp) {
        if (busiResultListener != null) {
            busiResultListener.onPassthroughResult(resp);
        }
    }
}
