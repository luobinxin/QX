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
import cn.com.startai.qxsdk.busi.QXReqDataCreater;
import cn.com.startai.qxsdk.busi.entity.Activate;
import cn.com.startai.qxsdk.busi.entity.BrokerHost;
import cn.com.startai.qxsdk.busi.entity.Login;
import cn.com.startai.qxsdk.busi.entity.UpdateDeviceInfo;
import cn.com.startai.qxsdk.connect.BaseData;
import cn.com.startai.qxsdk.connect.ble.BLEData;
import cn.com.startai.qxsdk.connect.ble.IQXBLE;
import cn.com.startai.qxsdk.connect.ble.QXBleImpl;
import cn.com.startai.qxsdk.connect.mqtt.IQXMqtt;
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
import cn.com.startai.qxsdk.db.bean.TopicBean;
import cn.com.startai.qxsdk.db.bean.UserBean;
import cn.com.startai.qxsdk.event.IOnCallListener;
import cn.com.startai.qxsdk.event.IQXBusi;
import cn.com.startai.qxsdk.event.IQXBusiResultListener;
import cn.com.startai.qxsdk.event.QXEventManager;
import cn.com.startai.qxsdk.global.AreaNodesManager;
import cn.com.startai.qxsdk.global.LooperManager;
import cn.com.startai.qxsdk.global.QXError;
import cn.com.startai.qxsdk.global.QXInitParam;
import cn.com.startai.qxsdk.global.QXParamManager;
import cn.com.startai.qxsdk.global.QXSpController;
import cn.com.startai.qxsdk.network.QXNetworkManager;
import cn.com.startai.qxsdk.utils.QXLog;
import cn.com.startai.qxsdk.utils.QXTimerUtil;
import cn.com.startai.qxsdk.utils.area.AreaConfig;
import cn.com.startai.qxsdk.utils.area.AreaLocation;
import cn.com.swain.support.protocolEngine.ProtocolProcessorFactory;
import cn.com.swain.support.protocolEngine.resolve.AbsProtocolProcessor;
import cn.com.swain.support.protocolEngine.task.FailTaskResult;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * Created by Robin on 2019/3/20.
 * 419109715@qq.com 彬影
 */
public class QXBusiManager implements IQXBusi, IQxMqttListener, OnMqttTaskCallBack, IQXUDPListener, OnUdpTaskCallBack {


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

    private IQXBusi qxbusi;

    public IQXBusi getQxbusi() {
        if (qxbusi == null) {
            qxbusi = getInstance();
        }
        return qxbusi;
    }

    public void setQxbusi(IQXBusi qxbusi) {
        this.qxbusi = qxbusi;
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


    public void doSend(BaseData baseData, IOnCallListener onCallListener) {

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
        } else {
            checkActivate();
        }
    }

    private void reportIp() {
        QXTimerUtil.schedule("reportIp", new TimerTask() {
            @Override
            public void run() {
                AreaLocation areaLocation = AreaConfig.getArea();
                if (areaLocation != null && !TextUtils.isEmpty(areaLocation.getQuery())) {

                    UpdateDeviceInfo.Req req = new UpdateDeviceInfo.Req(QXMqttConfig.getSn(QX.getInstance().getApp()), new UpdateDeviceInfo.Req.StatusParam(areaLocation.getQuery()));

                    BaseData updateDeviceInfoData = QXReqDataCreater.getUpdateDeviceInfoData(req);
                    doSend(updateDeviceInfoData, null);

                } else {
                    Log.w(TAG, "outterIp = " + areaLocation);
                }

                QXTimerUtil.close("reportIp");
            }
        }, 100);

    }

    private void checkUnCompleteMsg() {

    }

    private void checkAreNode() {
        String checkGetAreaNode = "checkGetAreaNode";

        boolean isNeedToGetBrokerHost = false;

        if (QXMqttConfig.changeHostTimeDelay == 0) {
            QXLog.d(TAG, "没有设置自动切换节点，不需要去获取区域节点信息");
            return;
        }

        BrokerHost.Resp.ContentBean spAreaNodes = AreaNodesManager.getInstance().getSpAreaNodes();
        if (spAreaNodes == null) {
            QXLog.d(TAG, "没有获取过节点信息");
            isNeedToGetBrokerHost = true;
        } else {
            //获取上次同步 区域节点的时间， 如果 大于 设定值就去同步一下
            long lastTime = QXSpController.getLastGetBrokerHostrespTime(); //上次同步时间
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
                QXTimerUtil.schedule(checkGetAreaNode, new TimerTask() {
                    @Override
                    public void run() {
                        checkAreNode();
                    }
                }, delay + 1000);

            }
        }

        QXTimerUtil.close(checkGetAreaNode);
        if (isNeedToGetBrokerHost) {
            AreaLocation areaLocation = AreaConfig.getArea();
            BrokerHost.Req req = new BrokerHost.Req(areaLocation.getQuery());
            doSend(QXReqDataCreater.getBrokerHostData(req), null);
        }

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

        getQxMqtt().subscribe(topics, new IOnCallListener() {
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
            QXLog.d(TAG, "allTopics = " + allTopics);


            if (allTopics != null && allTopics.size() > 0) {
                subTopic = new ArrayList<>();
                for (TopicBean t : allTopics) {

                    if (t.getType().equals("set")) {
                        subTopic.add(t.getTopic());
                    }

                }

            }
            final List<String> finalSubTopic = subTopic;
            getQxMqtt().subscribe(subTopic, new IOnCallListener() {
                @Override
                public void onSuccess() {
                    for (TopicBean t : allTopics) {
                        for (String s : finalSubTopic) {
                            t.setCurrType("set");
                            QXDBManager.getInstance().addOrUpdateTopic(t);
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
            QXLog.d(TAG, "allTopics = " + allTopics);


            if (allTopics != null && allTopics.size() > 0) {
                unSubTopic = new ArrayList<>();
                for (TopicBean t : allTopics) {

                    if (t.getType().equals("remove")) {
                        unSubTopic.add(t.getTopic());
                    }

                }

            }


            final List<String> finalUnSubTopic = unSubTopic;
            getQxMqtt().unSubscribe(unSubTopic, new IOnCallListener() {
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
            UserBean userBean = new UserBean().fromLoginResp(resp);
            QXDBManager.getInstance().addOrUpdateUser(userBean);


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

//        getEventManager().onUpdateDeviceInfoResult(resp);

    }

    //------------------- OnMqttTaskCallBack end----------------------


    //------------------- IQXBusi start----------------------

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
     * 登录
     *
     * @param req
     * @param callListener
     */
    @Override
    public void login(@NonNull Login.Req req, IOnCallListener callListener) {
        BaseData loginData = QXReqDataCreater.getLoginData(req);
        doSend(loginData, callListener);
    }

    /**
     * 帮第三方设备激活
     *
     * @param req
     * @param callListener
     */
    @Override
    public void hardwareActivate(Activate.Req req, IOnCallListener callListener) {
        BaseData activateData = QXReqDataCreater.getActivateData(req);
        doSend(activateData, callListener);
    }


    //------------------- IQXBusi end----------------------
}
