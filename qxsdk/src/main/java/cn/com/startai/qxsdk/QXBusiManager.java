package cn.com.startai.qxsdk;

import android.app.Application;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.xutils.x;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;

import cn.com.startai.qxsdk.busi.BaseMiofBusiHandler;
import cn.com.startai.qxsdk.busi.BusiEntity.BindDeviceReq;
import cn.com.startai.qxsdk.busi.BusiEntity.PassthroughReq;
import cn.com.startai.qxsdk.busi.BusiEntity.UnBindDeviceReq;
import cn.com.startai.qxsdk.busi.QXReqDataCreater;
import cn.com.startai.qxsdk.busi.entity.Activate;
import cn.com.startai.qxsdk.busi.entity.Bind;
import cn.com.startai.qxsdk.busi.entity.BindEmail;
import cn.com.startai.qxsdk.busi.entity.BindMobile;
import cn.com.startai.qxsdk.busi.entity.BindThirdAccount;
import cn.com.startai.qxsdk.busi.entity.BrokerHost;
import cn.com.startai.qxsdk.busi.entity.CheckIdentifyCode;
import cn.com.startai.qxsdk.busi.entity.GetAlipayAuthInfo;
import cn.com.startai.qxsdk.busi.entity.GetBindList;
import cn.com.startai.qxsdk.busi.entity.GetIdentifyCode;
import cn.com.startai.qxsdk.busi.entity.GetLatestAppVersion;
import cn.com.startai.qxsdk.busi.entity.GetRealPayResult;
import cn.com.startai.qxsdk.busi.entity.GetUserInfo;
import cn.com.startai.qxsdk.busi.entity.GetWeatherInfo;
import cn.com.startai.qxsdk.busi.entity.Login;
import cn.com.startai.qxsdk.busi.entity.LoginWithThirdAccount;
import cn.com.startai.qxsdk.busi.entity.Register;
import cn.com.startai.qxsdk.busi.entity.ResetLoginPwd;
import cn.com.startai.qxsdk.busi.entity.SendEmail;
import cn.com.startai.qxsdk.busi.entity.ThirdPaymentUnifiedOrder;
import cn.com.startai.qxsdk.busi.entity.UnActivate;
import cn.com.startai.qxsdk.busi.entity.UnBind;
import cn.com.startai.qxsdk.busi.entity.UnBindThirdAccount;
import cn.com.startai.qxsdk.busi.entity.UpdateDeviceInfo;
import cn.com.startai.qxsdk.busi.entity.UpdateLoginPwd;
import cn.com.startai.qxsdk.busi.entity.UpdateRemark;
import cn.com.startai.qxsdk.busi.entity.UpdateUserInfo;
import cn.com.startai.qxsdk.connect.BaseData;
import cn.com.startai.qxsdk.connect.ble.BLEData;
import cn.com.startai.qxsdk.connect.ble.IQXBLE;
import cn.com.startai.qxsdk.connect.ble.QXBleImpl;
import cn.com.startai.qxsdk.connect.mqtt.BaseMessage;
import cn.com.startai.qxsdk.connect.mqtt.IQXMqtt;
import cn.com.startai.qxsdk.connect.mqtt.QXMqttConnectState;
import cn.com.startai.qxsdk.connect.mqtt.QXMqttData;
import cn.com.startai.qxsdk.connect.mqtt.TopicConsts;
import cn.com.startai.qxsdk.connect.mqtt.client.QXMqttConfig;
import cn.com.startai.qxsdk.connect.mqtt.client.QXMqttImpl;
import cn.com.startai.qxsdk.connect.mqtt.event.IQxMqttListener;
import cn.com.startai.qxsdk.connect.mqtt.task.OnMqttTaskCallBack;
import cn.com.startai.qxsdk.connect.udp.BroadcastDiscoveryUtil;
import cn.com.startai.qxsdk.connect.udp.IQXUDP;
import cn.com.startai.qxsdk.connect.udp.IQXUDPListener;
import cn.com.startai.qxsdk.connect.udp.UDPData;
import cn.com.startai.qxsdk.connect.udp.bean.LanBindingDevice;
import cn.com.startai.qxsdk.connect.udp.bean.LanDeviceInfo;
import cn.com.startai.qxsdk.connect.udp.client.QXUDPImpl;
import cn.com.startai.qxsdk.connect.udp.task.OnUdpTaskCallBack;
import cn.com.startai.qxsdk.connect.udp.task.ProtocolTaskImpl;
import cn.com.startai.qxsdk.db.QXDBManager;
import cn.com.startai.qxsdk.db.bean.DeviceBean;
import cn.com.startai.qxsdk.db.bean.TopicBean;
import cn.com.startai.qxsdk.db.bean.UserBean;
import cn.com.startai.qxsdk.event.IQXCallListener;
import cn.com.startai.qxsdk.event.IQXBusi;
import cn.com.startai.qxsdk.event.IQXBusiResultListener;
import cn.com.startai.qxsdk.event.QXEventManager;
import cn.com.startai.qxsdk.global.AreaNodesManager;
import cn.com.startai.qxsdk.global.LooperManager;
import cn.com.startai.qxsdk.global.QXCallbackManager;
import cn.com.startai.qxsdk.global.QXError;
import cn.com.startai.qxsdk.global.QXInitParam;
import cn.com.startai.qxsdk.global.QXParamManager;
import cn.com.startai.qxsdk.global.QXSpController;
import cn.com.startai.qxsdk.network.IQXNetworkListener;
import cn.com.startai.qxsdk.network.QXNetworkManager;
import cn.com.startai.qxsdk.utils.QXLog;
import cn.com.startai.qxsdk.utils.QXTimerUtil;
import cn.com.startai.qxsdk.utils.area.AreaLocation;
import cn.com.startai.qxsdk.utils.area.QXLocationManager;
import cn.com.swain.support.protocolEngine.ProtocolProcessorFactory;
import cn.com.swain.support.protocolEngine.resolve.AbsProtocolProcessor;
import cn.com.swain.support.protocolEngine.task.FailTaskResult;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * Created by Robin on 2019/3/20.
 * 419109715@qq.com 彬影
 */
public class QXBusiManager implements IQXBusi, IQxMqttListener, OnMqttTaskCallBack, IQXUDPListener, OnUdpTaskCallBack, IQXNetworkListener {


    private boolean isNeedCallBackUpdateDeviceInfo;

    private QXBusiManager() {
    }


    private static QXBusiManager instance;


    public static void setInstance(QXBusiManager instance) {
        QXBusiManager.instance = instance;
    }


    private IQXMqtt qxMqtt;
    private IQXBLE qxBle;
    private IQXUDP qxUdp;
    private Application app;

    private BroadcastDiscoveryUtil broadcastDiscoveryUtil;


    private QXEventManager eventManager;

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
        return this.app != null;
    }

    /**
     * 获取与服务器的连接状态
     *
     * @return
     */
    @Override
    public QXMqttConnectState getServerConnectState() {
        if (getQxMqtt() != null) {
            return getQxMqtt().getQXMqttConnectState();
        }
        return QXMqttConnectState.DISCONNECTED;
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

    public QXEventManager getEventManager() {
        if (eventManager == null) {
            eventManager = QXEventManager.getInstance();
        }
        return eventManager;
    }

    public void setEventManager(QXEventManager eventManager) {
        this.eventManager = eventManager;
    }

    private BaseMiofBusiHandler busiHandler;
    //协议解析器
    private AbsProtocolProcessor pm;

    public BaseMiofBusiHandler getBusiHandler() {
        return busiHandler;
    }

    public void setBusiHandler(BaseMiofBusiHandler busiHandler) {
        this.busiHandler = busiHandler;
    }

    public static QXBusiManager getInstance() {
        if (instance == null) {
            instance = new QXBusiManager();
        }
        return instance;
    }

    public void init(Application app, QXInitParam qxInitParam) {
        if (qxInitParam == null || TextUtils.isEmpty(qxInitParam.appid)) {
            Log.e(TAG, "QXSDK init failed 'qxInitParam' is empty or 'appid' is empty");
            return;
        }


        this.app = app;
        x.Ext.init(app);

        QXNetworkManager.getInstance().init(app);

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

        setQxMqtt(QXMqttImpl.getInstance());
        setQxUdp(QXUDPImpl.getInstance());
        setQxBle(QXBleImpl.getInstance());

        broadcastDiscoveryUtil = new BroadcastDiscoveryUtil(LooperManager.getInstance().getWorkLooper(), getQxUdp(), this);


        getQxMqtt().setListener(this);
        getQxUdp().setListener(this);
        pm = ProtocolProcessorFactory.newMultiChannelSingleTask(LooperManager.getInstance().getProtocolLooper(),
                new ProtocolTaskImpl(this, getApp()),
                QXParamManager.getInstance().getmProtocolVersion(), true);
        busiHandler = new BaseMiofBusiHandler(this);

        getQxMqtt().init();
        QXNetworkManager.getInstance().addNetworkListener(this);
    }

    /**
     * 添加监听
     *
     * @param listener
     */
    @Override
    public void addQXBusiListener(IQXBusiResultListener listener) {
        getEventManager().addQXBusiResultListener(listener);

    }

    /**
     * 注销监听
     *
     * @param listener
     */
    @Override
    public void removeQXBusiListener(IQXBusiResultListener listener) {
        getEventManager().removeBusiResultListener(listener);
    }

    public void release() {

        if (pm != null) {
            pm.release();
        }
        QXTimerUtil.closeAll();
        if (getApp() != null) {
            getQxBle().release();
            getQxMqtt().release();
            getQxUdp().release();
        }
        QXNetworkManager.getInstance().release();


    }


    public void doSend(BaseData baseData, IQXCallListener onCallListener) {
        if (baseData == null) {
            QXCallbackManager.callbackCallResult(false, onCallListener, QXError.ERROR_SEND_PARAM_INVALIBLE);
            return;
        }
        if (baseData instanceof QXMqttData) {
            QXMqttData mqttData = (QXMqttData) baseData;
            getQxMqtt().publish(mqttData, onCallListener);
        } else if (baseData instanceof UDPData) {
            UDPData udpData = (UDPData) baseData;
            getQxUdp().send(udpData, onCallListener);
        } else if (baseData instanceof BLEData) {
            BLEData ble = (BLEData) baseData;
            getQxBle().send(ble, onCallListener);
        }
    }

    public void addListener(IQXBusiResultListener listener) {
        getEventManager().addQXBusiResultListener(listener);
    }

    public void removeListener(IQXBusiResultListener listener) {
        if (getEventManager() != null) {
            getEventManager().removeBusiResultListener(listener);
        }
    }
    // --------------------------- IQxMqttListener start ------------------------------

    @Override
    public void onMqttConnected() {

        subUserOrSnTopic();

        if (QXSpController.getIsActivite()) {
            getEventManager().onServerConnected();
            checkAreNode();
            reportIp();
            checkUnCompleteMsg();
            subFriendReportTopic();
            checkIsAvaliToken();
        } else {
            checkActivate();
        }
    }

    private void reportIp() {
        final String taskKe = "reportIp";
        QXTimerUtil.schedule(taskKe, new TimerTask() {
            @Override
            public void run() {
                AreaLocation areaLocation = QXLocationManager.getInstance().getLocation();
                if (areaLocation != null && !TextUtils.isEmpty(areaLocation.getQuery())) {

                    UpdateDeviceInfo.Req req = new UpdateDeviceInfo.Req(QXMqttConfig.getSn(QX.getInstance().getApp()), new UpdateDeviceInfo.Req.StatusParam(areaLocation.getQuery()));

                    BaseData updateDeviceInfoData = QXReqDataCreater.getUpdateDeviceInfoData(req);
                    doSend(updateDeviceInfoData, null);

                } else {
                    Log.w(TAG, "outterIp = " + areaLocation);
                }

                QXTimerUtil.close(taskKe);
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
                getEventManager().onLoginExpired();
            } else {
                QXLog.d(TAG, "账号登录状态正常，可以正常使用");
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
                    doSend(QXReqDataCreater.getBrokerHostData(req), null);
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
                    BaseData activateData = QXReqDataCreater.getActivateData(null);
                    doSend(activateData, null);
                } else {
                    QXLog.e(TAG, "设备已经正常激活");
                    QXTimerUtil.close(timerKey);
                }
            }
        }, 0, 60 * 1000);

    }

    @Override
    public void onMqttDisconnected(QXError qxError) {
        getEventManager().onServerDisConnect(qxError);
    }

    @Override
    public void onMqttReconnecting() {
        getEventManager().onServerReConnecting();
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

    }

    /**
     * 心跳回复
     *
     * @param mac
     * @param result
     */
    @Override
    public void onHeartbeatResult(String mac, boolean result) {

    }

    /**
     * 设备发现
     *
     * @param id
     * @param result
     * @param mWiFiDevice
     */
    @Override
    public void onDeviceDiscoveryResult(String id, boolean result, LanDeviceInfo mWiFiDevice) {

    }

    /**
     * 设备开始发现
     */
    @Override
    public void onDiscoveryStart() {

    }

    /**
     * 设备发现结果
     */
    @Override
    public void onDiscoveryStop() {

    }

    /**
     * 局域网绑定
     *
     * @param result
     * @param mLanBindingDevice
     */
    @Override
    public void onLanBindResult(boolean result, LanBindingDevice mLanBindingDevice) {

    }

    /**
     * token 失效
     *
     * @param mac
     */
    @Override
    public void onTokenInvalid(String mac) {

    }

    /**
     * 局域网解绑
     *
     * @param result
     * @param mLanBindingDevice
     */
    @Override
    public void onLanUnBindResult(boolean result, LanBindingDevice mLanBindingDevice) {

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

    }

    /**
     * 请求连接
     *
     * @param result
     * @param id
     */
    @Override
    public void onConnectResult(boolean result, String id) {

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

    }

    // --------------------------- OnUdpTaskCallBack end ------------------------------


    /**
     * 订阅用户相关主题
     */
    private void subUserOrSnTopic() {


        final List<String> topics = new ArrayList<>();
        topics.add(TopicConsts.Q_CLIENT + "/" + QXMqttConfig.getSn(getApp()) + "/#");
        String userId = QXUserManager.getInstance().getUserId();
        if (!TextUtils.isEmpty(userId)) {
            topics.add(TopicConsts.Q_CLIENT + "/" + userId + "/#");
        }

        getQxMqtt().subscribeSync(topics, new IQXCallListener() {
            @Override
            public void onSuccess() {
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
    }


    /**
     * 订阅好友遗嘱 主题
     */
    private void subFriendReportTopic() {


        List<String> subTopic = null;

        String userId = QXUserManager.getInstance().getUserId();
        if (!TextUtils.isEmpty(userId)) {
            final ArrayList<TopicBean> allTopics = QXDBManager.getInstance().getAllTopic(userId);
            QXLog.d(TAG, "allSubTopics = " + allTopics);


            if (allTopics != null && allTopics.size() > 0) {
                subTopic = new ArrayList<>();
                for (TopicBean t : allTopics) {

                    if (t.getType().equals("set")) {
                        subTopic.add(t.getTopic());
                    }

                }

            }
            if (subTopic == null || subTopic.size() == 0) {
                return;
            }
            final List<String> finalSubTopic = subTopic;
            getQxMqtt().subscribeSync(subTopic, new IQXCallListener() {
                @Override
                public void onSuccess() {
                    for (TopicBean t : allTopics) {
                        for (String s : finalSubTopic) {
                            if (t.getTopic().equals(s)) {
                                t.setCurrType("set");
                                QXDBManager.getInstance().addOrUpdateTopic(t);
                            }
                        }
                    }
                    QXTimerUtil.close("subFriendReportTopic");
                }

                @Override
                public void onFailed(QXError error) {
                    //3秒后重试
                    QXTimerUtil.schedule("subFriendReportTopic", new TimerTask() {
                        @Override
                        public void run() {
                            subFriendReportTopic();
                        }
                    }, 3000);
                }
            });
        }


    }

    /**
     * 取消订阅好友遗嘱 主题
     */
    private void unSubFriendReportTopic() {


        List<String> unSubTopic = null;

        final String userId = QXUserManager.getInstance().getUserId();
        if (!TextUtils.isEmpty(userId)) {
            final ArrayList<TopicBean> allTopics = QXDBManager.getInstance().getAllTopic(userId);
            QXLog.d(TAG, "allUnSubTopics = " + allTopics);


            if (allTopics != null && allTopics.size() > 0) {
                unSubTopic = new ArrayList<>();
                for (TopicBean t : allTopics) {

                    if (t.getType().equals("remove")) {
                        unSubTopic.add(t.getTopic());
                    }

                }

            }

            if (unSubTopic == null || unSubTopic.size() == 0) {
                return;
            }
            final List<String> finalUnSubTopic = unSubTopic;
            getQxMqtt().unSubscribe(unSubTopic, new IQXCallListener() {
                @Override
                public void onSuccess() {
                    for (String s : finalUnSubTopic) {
                        QXDBManager.getInstance().deleteTopicByTopic(userId, s);
                    }

                    QXTimerUtil.close("unSubFriendReportTopic");
                }

                @Override
                public void onFailed(QXError error) {
                    //3秒后重试
                    QXTimerUtil.schedule("unSubFriendReportTopic", new TimerTask() {
                        @Override
                        public void run() {
                            unSubFriendReportTopic();
                        }
                    }, 3000);
                }
            });
        }
    }


    //------------------- OnMqttTaskCallBack start----------------------


    @Override
    public void onLoginResult(Login.Resp resp) {
        if (resp.getResult() == resp.RESULT_SUCCESS) {

            //登录登录状态
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

            //订阅对应主题
            subFriendReportTopic();

            //登录成功
            //初始化 mqUdp
            getQxUdp().setListener(this);
            getQxUdp().init();

        }

        QXEventManager.getInstance().onLoginResult(resp);

    }

    @Override
    public void onMessageArrived(String topic, String message) {

    }

    @Override
    public void onActivateResult(Activate.Resp resp) {
        QXSpController.setIsActivite(true);
        checkAreNode();

        getEventManager().onServerConnected();

    }

    @Override
    public void onHardwareActivateResult(Activate.Resp resp) {
        getEventManager().onHardwareActivateResult(resp);
    }

    @Override
    public void onUpdateDeviceInfoResult(UpdateDeviceInfo.Resp resp) {
        if (isNeedCallBackUpdateDeviceInfo) {
            getEventManager().onUpdateDeviceInfoResult(resp);
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
                    doSend(QXReqDataCreater.getBrokerHostData(req), null);

                    QXTimerUtil.close(taskKey);
                }
            }, cycle * 1000);
        }

    }

    @Override
    public void onGetIdentifyCodeResult(GetIdentifyCode.Resp resp) {

        getEventManager().onGetIdentifyCodeResult(resp);

    }

    @Override
    public void onCheckIdentifyCodeResult(CheckIdentifyCode.Resp resp) {
        getEventManager().onCheckIdetifyResult(resp);
    }

    @Override
    public void onLogoutResult(int result) {

    }

    @Override
    public void onLoginExpired() {

    }

    @Override
    public void onRegisterResult(Register.Resp resp) {

        getEventManager().onRegisterResult(resp);

    }

    @Override
    public void onSendEmail(SendEmail.Resp resp) {
        getEventManager().onSendEmailResult(resp);
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

        getEventManager().onUpdateUserInfoResult(resp);
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

        getEventManager().onGetUserInfoResult(resp);

    }

    @Override
    public void onGetLatestAppVersionResult(GetLatestAppVersion.Resp resp) {
        getEventManager().onGetLatestVersionResult(resp);
    }

    @Override
    public void onResetLoginPwdResult(ResetLoginPwd.Resp resp) {
        getEventManager().onResetLoginPwdResult(resp);
    }

    @Override
    public void onUpdateLoginPwdResult(UpdateLoginPwd.Resp resp) {
        getEventManager().onUpdateLoginPwdResult(resp);
    }

    @Override
    public void onUpdateRemarkResult(UpdateRemark.Resp resp) {

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

        getEventManager().onUnActiviteResult(resp);
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

        } else {

            QXLog.e(TAG, "解绑失败 " + resp.getContent().getErrmsg());

            if (id.equals(resp.getContent().getBeunbindingid())) {
                friendId = resp.getContent().getErrcontent().getUnbindingid();
            } else {
                friendId = resp.getContent().getErrcontent().getBeunbindingid();
            }

        }


        if (userBean != null) {
            TopicBean topicBeanWill = new TopicBean(TopicConsts.getSubFriendWillTopic(friendId), "remove", "", userBean.getUserId());
            QXDBManager.getInstance().addOrUpdateTopic(topicBeanWill);

            TopicBean topicBeanReport = new TopicBean(TopicConsts.getSubFriendReportTopic(friendId), "remove", "", userBean.getUserId());
            QXDBManager.getInstance().addOrUpdateTopic(topicBeanReport);

            unSubFriendReportTopic();

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


        if (resp.getResult() == BaseMessage.RESULT_SUCCESS) {

            Bind.Resp.ContentBean.BebindingBean bebinding = null;
            if (id.equals(resp.getContent().getBebinding().getId())) {

                Bind.Resp.ContentBean.BindingBean binding = resp.getContent().getBinding();
                bebinding = new Bind.Resp.ContentBean.BebindingBean(binding.getId(), binding.getApptype(), binding.getFeatureid(), binding.getTopic(), binding.getConnstatus(), binding.getMac());
            } else {
                bebinding = resp.getContent().getBebinding();
            }

//            StartAI.getInstance().getPersisitnet().getEventDispatcher().onBindResult(resp, id, bebinding);

            if (userBean != null) {

                TopicBean topicBeanWill = new TopicBean(TopicConsts.getSubFriendWillTopic(bebinding.getId()), "set", "", id);
                QXDBManager.getInstance().addOrUpdateTopic(topicBeanWill);

                TopicBean topicBeanReport = new TopicBean(TopicConsts.getSubFriendReportTopic(bebinding.getId()), "set", "", id);
                QXDBManager.getInstance().addOrUpdateTopic(topicBeanReport);

                subFriendReportTopic();

            }
        } else {
            Bind.Resp.ContentBean.BebindingBean bebindingBean = new Bind.Resp.ContentBean.BebindingBean();
            bebindingBean.setId(resp.getContent().getErrcontent().getBebindingid());
//            StartAI.getInstance().getPersisitnet().getEventDispatcher().onBindResult(resp, id, bebindingBean);

        }

    }

    @Override
    public void onGetBindListResult(GetBindList.Resp resp) {

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

        getEventManager().onBindEmailResult(resp);
    }

    @Override
    public void onBindThirdAccountResult(BindThirdAccount.Resp resp) {

    }

    @Override
    public void onUnBindThirdAccountResult(UnBindThirdAccount.Resp resp) {

    }

    @Override
    public void onGetWeatherInfoResult(GetWeatherInfo.Resp resp) {

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

        getEventManager().onBindMobileNumResult(resp);
    }

    @Override
    public void onGetAlipayAuthInfoResult(GetAlipayAuthInfo.Resp resp) {

    }

    @Override
    public void onGetRealPayResult(GetRealPayResult.Resp resp) {

    }

    @Override
    public void onThirdPaymentUnifiedOrderResult(ThirdPaymentUnifiedOrder.Resp resp) {

    }

    //------------------- OnMqttTaskCallBack end----------------------


    //------------------- IQXBusi start----------------------

    /**
     * 消息透传
     *
     * @param req
     * @param callListener
     */
    @Override
    public void passthrough(PassthroughReq req, IQXCallListener callListener) {

    }

    /**
     * 修备注名
     *
     * @param req      对方的sn 跟新的备注名
     * @param listener
     */
    @Override
    public void updateRemark(UpdateRemark.Req req, IQXCallListener listener) {

    }

    /**
     * 与设备进行连接设备
     *
     * @param deviceBean
     * @param callListener
     */
    @Override
    public void connectDevice(@NonNull DeviceBean deviceBean, IQXCallListener callListener) {

    }

    /**
     * 断开与设备的连接
     *
     * @param deviceBean
     * @param callListener
     */
    @Override
    public void disConnectDevice(@NonNull DeviceBean deviceBean, IQXCallListener callListener) {

    }

    /**
     * 局域网设备扫描
     *
     * @param timeMillims
     */
    @Override
    public void discovery(long timeMillims) {

        broadcastDiscoveryUtil.startDiscovery(QXUserManager.getInstance().getUserId(), timeMillims);

    }

    /**
     * 停止局域网设备扫描
     */
    @Override
    public void stopDiscovery() {
        broadcastDiscoveryUtil.stopDiscovery();
    }

    /**
     * 绑定设备
     *
     * @param req
     * @param callListener
     */
    @Override
    public void bindDevice(@NonNull BindDeviceReq req, IQXCallListener callListener) {

    }

    /**
     * 解绑设备
     *
     * @param req
     * @param callListener
     */
    @Override
    public void unBindDevice(@NonNull UnBindDeviceReq req, IQXCallListener callListener) {

    }

    /**
     * 查询绑定关系
     *
     * @param callListener
     * @return
     */
    @Override
    public void getBindDeviceList(IQXCallListener callListener) {

    }

    /**
     * 获取最新软件版本
     *
     * @param callListener
     */
    @Override
    public void getLatestVersion(IQXCallListener callListener) {
        doSend(QXReqDataCreater.getLatestAppVersionData(), callListener);
    }

    /**
     * 修改用户登录密码
     *
     * @param req
     * @param callListener
     */
    @Override
    public void updateLoginPwd(UpdateLoginPwd.Req req, IQXCallListener callListener) {
        doSend(QXReqDataCreater.getUpdateLoginPwd(req), callListener);
    }

    /**
     * 更新用户信息
     *
     * @param req
     * @param callListener
     */
    @Override
    public void updateUserInfo(UpdateUserInfo.Req req, IQXCallListener callListener) {
        doSend(QXReqDataCreater.getUpdateUserInfoData(req), callListener);
    }

    /**
     * 查询用户信息
     *
     * @param callListener
     */
    @Override
    public void getUserInfo(IQXCallListener callListener) {

        doSend(QXReqDataCreater.getUserInfoData(), callListener);
    }

    /**
     * 请求发送邮件
     *
     * @param req          邮箱
     * @param callListener
     */
    @Override
    public void sendEmail(SendEmail.Req req, IQXCallListener callListener) {
        doSend(QXReqDataCreater.getSendEmailData(req), callListener);
    }

    /**
     * 第三方账号登录
     *
     * @param req          第三方登录参数
     * @param callListener
     */
    @Override
    public void loginWithThirdAccount(LoginWithThirdAccount.Req req, IQXCallListener callListener) {

    }

    /**
     * 登出
     */
    @Override
    public void logout() {

        //清空上次连接的clientid
        QXSpController.setClientid("");


        //清空用户登录状态
        QXUserManager.getInstance().resetUser();

        //断开连接并重新以随机clientid连接
        getQxMqtt().disconnectAndReconnect();

        QXLog.e(TAG, "登出成功");
        getEventManager().onLogoutResult(1);

    }

    /**
     * 登录
     *
     * @param req
     * @param callListener
     */
    @Override
    public void login(@NonNull Login.Req req, IQXCallListener callListener) {
        BaseData loginData = QXReqDataCreater.getLoginData(req);
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

        doSend(QXReqDataCreater.getRegisterData(req), callListener);

    }

    /**
     * 获取手机验证码
     *
     * @param req
     * @param callListener
     */
    @Override
    public void getIdentifyCode(@NonNull GetIdentifyCode.Req req, IQXCallListener callListener) {
        doSend(QXReqDataCreater.getIdentifyCodeData(req), callListener);
    }

    /**
     * 检验手机验证码
     *
     * @param req
     * @param callListener
     */
    @Override
    public void checkIdentifyCode(@NonNull CheckIdentifyCode.Req req, IQXCallListener callListener) {

        doSend(QXReqDataCreater.getCheckIdentifyCodeData(req), callListener);
    }

    /**
     * 帮第三方设备激活
     *
     * @param req
     * @param callListener
     */
    @Override
    public void hardwareActivate(@NonNull Activate.Req req, IQXCallListener callListener) {
        BaseData activateData = QXReqDataCreater.getActivateData(req);
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

    }

    /**
     * 重置密码
     *
     * @param req          手机号 或邮箱号
     * @param callListener
     */
    @Override
    public void resetLoginPwd(ResetLoginPwd.Req req, IQXCallListener callListener) {
        doSend(QXReqDataCreater.getResetLoginPwdData(req), callListener);
    }

    /**
     * 第三方支付 统一下单
     *
     * @param contentBean
     * @param callListener
     */
    @Override
    public void thirdPaymentUnifiedOrder(ThirdPaymentUnifiedOrder.Req contentBean, IQXCallListener callListener) {

    }

    /**
     * 查询真实订单支付结果
     *
     * @param orderNum
     * @param callListener
     */
    @Override
    public void getRealOrderPayStatus(String orderNum, IQXCallListener callListener) {

    }

    /**
     * 查询支付宝认证信息
     *
     * @param authType
     * @param callListener
     */
    @Override
    public void getAlipayAuthInfo(String authType, IQXCallListener callListener) {

    }

    /**
     * 关联手机号
     *
     * @param req
     * @param callListener
     */
    @Override
    public void bindMobileNum(BindMobile.Req req, IQXCallListener callListener) {
        doSend(QXReqDataCreater.getBindMobileData(req), callListener);
    }

    /**
     * 解绑第三方账号
     *
     * @param req
     * @param callListener
     */
    @Override
    public void unBindThirdAccount(UnBindThirdAccount.Req req, IQXCallListener callListener) {

    }

    /**
     * 绑定第三方账号
     *
     * @param req
     * @param callListener
     */
    @Override
    public void bindThirdAccount(BindThirdAccount.Req req, IQXCallListener callListener) {

    }

    /**
     * 查询天气信息
     *
     * @param req
     * @param callListener
     */
    @Override
    public void getWeatherInfo(GetWeatherInfo.Req req, IQXCallListener callListener) {

    }

    /**
     * 绑定邮箱
     *
     * @param req
     * @param callListener
     */
    @Override
    public void bindEmail(BindEmail.Req req, IQXCallListener callListener) {

        doSend(QXReqDataCreater.getBindEmailData(req), callListener);
    }

    /**
     * 查询 绑定列表 分页
     *
     * @param req
     * @param callListener
     */
    @Override
    public void getBindListByPage(GetBindList.Req req, IQXCallListener callListener) {

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
    public void onNetworkStateChange() {
        QXLocationManager.getInstance().setLocation(null);
    }
    //------------------- IQXNetworkListener end ----------------------
}
