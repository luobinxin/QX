package cn.com.startai.qxsdk.connect.mqtt.client;

import android.os.Handler;
import android.os.HandlerThread;

import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;

import cn.com.startai.qxsdk.QX;
import cn.com.startai.qxsdk.connect.mqtt.IQXMqtt;
import cn.com.startai.qxsdk.connect.mqtt.QXMqttConnectState;
import cn.com.startai.qxsdk.connect.mqtt.QXMqttData;
import cn.com.startai.qxsdk.connect.mqtt.event.IQxMqttListener;
import cn.com.startai.qxsdk.connect.mqtt.event.StartaiTimerPingListener;
import cn.com.startai.qxsdk.connect.mqtt.event.StartaiTimerPingSender;
import cn.com.startai.qxsdk.connect.udp.client.QXUDPImpl;
import cn.com.startai.qxsdk.event.IOnCallListener;
import cn.com.startai.qxsdk.network.IQXNetworkListener;
import cn.com.startai.qxsdk.global.QXCallbackManager;
import cn.com.startai.qxsdk.global.QXError;
import cn.com.startai.qxsdk.global.QXSpController;
import cn.com.startai.qxsdk.network.QXNetworkManager;
import cn.com.startai.qxsdk.utils.QXLog;
import cn.com.startai.qxsdk.utils.QXTimerUtil;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * Created by Robin on 2019/3/21.
 * 419109715@qq.com 彬影
 */
public class QXMqttImpl implements IQXMqtt, IQXNetworkListener {

    private static final String TAG_BUSI = TAG + "BUSI";
    private static final String TAG_SEND = TAG + "SEND";
    private static final String TAG_CONN = TAG + "CONN";

    private IMqttAsyncClient client;

    private StartaiTimerPingSender.PingListener pingListener;
    private MqttConnectOptions option;
    private IQxMqttListener listener;

    private QXMqttConnectState connectState;


    private static HandlerThread htConn;
    private static HandlerThread htBusi;
    private static HandlerThread htSend;

    private static Handler mConnHandler;
    private static Handler mBusiHandler;
    private static Handler mSendHandler;
    private int returnCount;
    private String clientId;


    public StartaiTimerPingSender.PingListener getPingListener() {
        if (pingListener == null) {
            pingListener = new StartaiTimerPingListener();
        }
        return pingListener;
    }

    private static volatile QXMqttImpl instance;

    private QXMqttImpl() {
    }

    public static IQXMqtt getInstance() {
        if (instance == null) {
            synchronized (QXUDPImpl.class) {
                if (instance == null) {
                    instance = new QXMqttImpl();

                    htConn = new HandlerThread(TAG_CONN);
                    htConn.start();
                    mConnHandler = new Handler(htConn.getLooper());

                    htBusi = new HandlerThread(TAG_BUSI);
                    htBusi.start();
                    mBusiHandler = new Handler(htBusi.getLooper());

                    htSend = new HandlerThread(TAG_SEND);
                    htSend.start();
                    mSendHandler = new Handler(htSend.getLooper());

                }
            }
        }

        return instance;
    }


    @Override
    public void init() {


        QXNetworkManager.getInstance().addNetworkListener(this);

        connect();
    }

    @Override
    public void release() {

        QXNetworkManager.getInstance().removeNetworkListener(this);

        disconnect(true);

        mSendHandler.removeCallbacksAndMessages(null);
        htSend.quit();
        QXLog.e(TAG, "htSend quit");

        mBusiHandler.removeCallbacksAndMessages(null);
        htBusi.quit();
        QXLog.e(TAG, "htBusi quit");


        instance = null;
    }


    public void disAndReconnect() {
        QXLog.d(TAG, "准备断开连接并重连");
        disconnect(false);
        connect();
    }

    private void connect() {

        mConnHandler.post(new Runnable() {
            @Override
            public void run() {

                try {

                    if (connectState == QXMqttConnectState.DISCONNECTED && QXSpController.getIsActivite()) {
                        //激活成功才回调连接成功
                        callbackConn();
                        return;
                    }

                    if (!QXNetworkManager.getInstance().isAvaliableNetwork()) {
                        QXLog.e(TAG, "network is unvaliable ");
                        connectState = QXMqttConnectState.DISCONNECTED;
                        callbackDisConn(QXError.ERROR_CONN_NET);
                        return;
                    }


                    if (connectState == QXMqttConnectState.CONNECTING) {
                        QXLog.e(TAG, "正在连接中，无需重复连接");
                        return;
                    }
                    connectState = QXMqttConnectState.CONNECTING;
                    option = QXMqttConfig.getConnectOptions(QX.getInstance().getApp());
                    if (option == null) {
                        callbackDisConn(QXError.ERROR_CONN_CER);
                        return;
                    }

                    String host = QXMqttConfig.getHost();


                    clientId = QXMqttConfig.getClientId();
                    QXLog.d(TAG, "connect host = " + host + " clientid = " + clientId);
                    StartaiTimerPingSender startaiTimerPingSender = new StartaiTimerPingSender(getPingListener());
                    client = new MqttAsyncClient(host, clientId, null, startaiTimerPingSender);
                    client.setCallback(mqttCallback);

                } catch (MqttException e) {
                    e.printStackTrace();
                    return;
                }


                try {
                    client.connect(option).waitForCompletion();

                    QXLog.e(TAG, "connect success");
                    connectState = QXMqttConnectState.CONNECTED;
                    pingListener.onReset();
                    QXMqttConfig.setClientId(clientId);
                    callbackConn();


                } catch (MqttException mqttException) {

                    int reasonCode = mqttException.getReasonCode();
                    if (reasonCode == 32100 || reasonCode == 32110 || reasonCode == 32102 || reasonCode == 32111) {
                        return;
                    }
                    QXLog.e(TAG, "connect failure");
                    mqttException.printStackTrace();
                    connectState = QXMqttConnectState.CONNECTFAIL;

                    callbackDisConn(mqttException.getReasonCode() + "", mqttException.getMessage());

                    QXLog.e(TAG, "连接失败,准备重试 " + returnCount++);
                    //准备重连
                    callbackReConn();
                    reconnect(2000);

                }


            }
        });


    }


    private void sleepTime(long time) {

        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void disconnect(final boolean inReleaseCall) {

        mConnHandler.post(new Runnable() {
            @Override
            public void run() {

                connectState = QXMqttConnectState.DISCONNECTING;
                try {
                    client.disconnect().waitForCompletion();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                connectState = QXMqttConnectState.DISCONNECTED;
                if (inReleaseCall) {
                    mConnHandler.removeCallbacksAndMessages(null);
                    htConn.quit();
                    QXLog.e(TAG, "htConnect quit");
                }
            }
        });
    }

    @Override
    public void publish(final QXMqttData mqttData, final IOnCallListener onCallListener) {
        mSendHandler.post(new Runnable() {
            @Override
            public void run() {

                if (client != null && connectState == QXMqttConnectState.CONNECTED) {

                    MqttMessage mqttMessage = new MqttMessage(mqttData.getData());

                    try {
                        QXLog.d(TAG, "publish before");
                        client.publish(mqttData.getTopic(), mqttMessage).waitForCompletion();

                        QXLog.d(TAG, "topic = " + mqttData.getTopic() + "\nqos = " + mqttMessage.getQos() + "\nretain = " + mqttMessage.isRetained() + "\npublish " + new String(mqttMessage.getPayload()));

                        //send success
                        QXCallbackManager.callbackCallResult(true, onCallListener, null);

                    } catch (MqttException e) {
                        e.printStackTrace();
                        QXCallbackManager.callbackCallResult(true, onCallListener, e.getReasonCode() + "");
                    }

                } else {
                    QXCallbackManager.callbackCallResult(false, onCallListener, QXError.ERROR_SEND_CLIENT_DISCONNECT);
                }

            }
        });
    }


    @Override
    public void subscribe(final List<String> topics, final IOnCallListener iOnCallListener) {
        mConnHandler.post(new Runnable() {
            @Override
            public void run() {

                if (connectState == QXMqttConnectState.CONNECTED) {
                    try {
                        if (topics == null) {
                            QXLog.e(TAG, "topics must not be null");
                            QXCallbackManager.callbackCallResult(false, iOnCallListener, QXError.ERROR_SUB_NULL_TOPIC);
                            return;
                        }

                        String[] topicArr = new String[topics.size()];

                        topicArr = topics.toArray(topicArr);


                        int[] qoss = new int[topics.size()];
                        for (int i : qoss) {
                            qoss[i] = 1;
                        }

                        client.subscribe(topicArr, qoss).waitForCompletion();

                        QXLog.e(TAG, "sub topics = " + Arrays.toString(topicArr) + " success");
                        QXCallbackManager.callbackCallResult(true, iOnCallListener, null);
                    } catch (MqttException e) {
                        QXLog.e(TAG, "sub topics = " + topics + " failed");
                        QXCallbackManager.callbackCallResult(false, iOnCallListener, e.getReasonCode() + "", e.getMessage());
                    } catch (Exception e) {
                        QXLog.e(TAG, "sub topics = " + topics + " failed");
                        QXCallbackManager.callbackCallResult(false, iOnCallListener, QXError.UNKOWN + "", e.getMessage());
                    }

                }

            }
        });
    }


    @Override
    public void unSubscribe(final List<String> topics, final IOnCallListener iOnCallListener) {
        mConnHandler.post(new Runnable() {
            @Override
            public void run() {

                if (connectState == QXMqttConnectState.CONNECTED) {
                    try {
                        if (topics == null) {
                            QXLog.e(TAG, "topics must not be null");
                            QXCallbackManager.callbackCallResult(false, iOnCallListener, QXError.ERROR_SUB_NULL_TOPIC);
                            return;
                        }

                        String[] topicArr = new String[topics.size()];

                        topicArr = topics.toArray(topicArr);

                        client.unsubscribe(topicArr).waitForCompletion();

                        QXLog.d(TAG, "sub topics = " + Arrays.toString(topicArr) + " success");
                        QXCallbackManager.callbackCallResult(true, iOnCallListener, null);
                    } catch (MqttException e) {
                        QXLog.e(TAG, "sub topics = " + topics + " failed");
                        QXCallbackManager.callbackCallResult(false, iOnCallListener, e.getReasonCode() + "", e.getMessage());
                    } catch (Exception e) {
                        QXLog.e(TAG, "sub topics = " + topics + " failed");
                        QXCallbackManager.callbackCallResult(false, iOnCallListener, QXError.UNKOWN + "", e.getMessage());
                    }

                }

            }
        });
    }


    @Override
    public void setListener(IQxMqttListener listener) {
        this.listener = listener;
    }

    @Override
    public IQxMqttListener getListener() {
        return this.listener;
    }

    @Override
    public IMqttAsyncClient getClient() {
        return client;
    }

    @Override
    public QXMqttConnectState getQXMqttConnectState() {
        return connectState;
    }

    private MqttCallback mqttCallback = new MqttCallback() {


        @Override
        public void connectionLost(Throwable cause) {
            QXLog.e(TAG, "connectionLost");
            cause.printStackTrace();
            connectState = QXMqttConnectState.DISCONNECTED;
            callbackDisConn(QXError.ERROR_CONN_NET);
            callbackReConn();

            reconnect(1000);
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            String msg = new String(message.getPayload());
            int qos = message.getQos();
            QXLog.e(TAG, "onMessageArrived : topic = " + topic + "\nqos =  " + qos + "\nmessage = " + msg);
            callbackMsgArrive(topic, message);
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            QXLog.e(TAG, "deliveryComplete ");
        }
    };

    void reconnect(long timeDelay) {
        QXLog.d(TAG, "reconnecting timeDelay = " + timeDelay);
        QXTimerUtil.schedule("reconnect", new TimerTask() {
            @Override
            public void run() {
                if (connectState == QXMqttConnectState.CONNECTED || connectState == QXMqttConnectState.CONNECTING) {
                    QXLog.e(TAG, "正在连接中，或已经连接成功，无需重复进行连接");
                    return;
                }
                connect();
            }
        }, timeDelay);

    }

    @Override
    public void onWifiConnected() {
        reconnect(100);
    }

    @Override
    public void onMobileConnected() {
        reconnect(100);
    }

    @Override
    public void onEthernetConnected() {
        reconnect(100);
    }

    @Override
    public void onUnkownNetwork() {

    }

    @Override
    public void onNetworkStateChange() {

    }

    private void callbackMsgArrive(String topic, MqttMessage message) {
        IQxMqttListener listener = getListener();
        if (listener != null) {
            listener.onMessageArrived(topic, message);
        }
    }

    private void callbackConn() {
        IQxMqttListener listener = getListener();
        if (listener != null) {
            listener.onMqttConnected();
        }
    }

    private void callbackDisConn(String errCode) {
        QXError qxError = new QXError(errCode);
        IQxMqttListener listener = getListener();
        if (listener != null) {
            listener.onMqttDisconnected(qxError);
        }
    }

    private void callbackDisConn(String errCode, String errorMsg) {
        QXError qxError = new QXError(errCode, errorMsg);
        IQxMqttListener listener = getListener();
        if (listener != null) {
            listener.onMqttDisconnected(qxError);
        }
    }

    private void callbackReConn() {
        IQxMqttListener listener = getListener();
        if (listener != null) {
            listener.onMqttReconnecting();
        }
    }
}
