package cn.com.startai.qxsdk.connect.mqtt.client;

import android.nfc.Tag;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.concurrent.Delayed;

import cn.com.startai.qxsdk.QX;
import cn.com.startai.qxsdk.connect.BaseData;
import cn.com.startai.qxsdk.connect.mqtt.IQXMqtt;
import cn.com.startai.qxsdk.connect.mqtt.QXMqttConnectState;
import cn.com.startai.qxsdk.connect.mqtt.event.IQxMqttListener;
import cn.com.startai.qxsdk.connect.mqtt.event.StartaiTimerPingListener;
import cn.com.startai.qxsdk.connect.mqtt.event.StartaiTimerPingSender;
import cn.com.startai.qxsdk.connect.udp.QXUDPImpl;
import cn.com.startai.qxsdk.event.IQXNetworkListener;
import cn.com.startai.qxsdk.global.QXError;
import cn.com.startai.qxsdk.global.QXErrorCode;
import cn.com.startai.qxsdk.global.QXSpController;
import cn.com.startai.qxsdk.utils.QXLog;

/**
 * Created by Robin on 2019/3/21.
 * 419109715@qq.com 彬影
 */
public class QXMqttImpl implements IQXMqtt, IQXNetworkListener {

    private static final String TAG = "QXMqttImpl";
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

    public StartaiTimerPingSender.PingListener getPingListener() {
        if (pingListener == null) {
            pingListener = new StartaiTimerPingListener();
        }
        return pingListener;
    }

    private static volatile QXMqttImpl instance;

    private QXMqttImpl() {
    }

    public static void registerInstance(String appid) {
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
                    QXMqttConfig.setAppid(appid);
                    instance.init();
                }
            }
        }
        QX.setQxMqtt(instance);
    }


    @Override
    public void init() {

        QX.getNetworkReceiver().addNetworkListener(this);

        connect();
    }

    @Override
    public void release() {

        disconnect(true);


        mSendHandler.removeCallbacksAndMessages(null);
        htSend.quit();
        QXLog.e(TAG, "htSend quit");

        mBusiHandler.removeCallbacksAndMessages(null);
        htBusi.quit();
        QXLog.e(TAG, "htBusi quit");

        instance = null;

    }

    @Override
    public void doSend(BaseData data) {

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

                    if (QX.getNetworkReceiver().isUnkownNetwork()) {
                        QXLog.e(TAG, "network is unvaliable ");
                        connectState = QXMqttConnectState.DISCONNECTED;
                        callbackDisConn(QXError.ERROR_CONN_NET);
                        return;
                    }


                    if (connectState == QXMqttConnectState.CONNECTING) {
                        QXLog.e(TAG, "正在连接中，无需重复连接");
                        return;
                    }

                    option = QXMqttConfig.getConnectOptions(QX.getApp());
                    if (option == null) {
                        callbackDisConn(QXError.ERROR_CONN_CER);
                        return;
                    }

                    String host = QXMqttConfig.getHost();


                    String clientId = QXMqttConfig.getClientId();
                    StartaiTimerPingSender startaiTimerPingSender = new StartaiTimerPingSender(getPingListener());
                    client = new MqttAsyncClient(host, clientId, null, startaiTimerPingSender);
                    client.setCallback(mqttCallback);

                } catch (MqttException e) {
                    e.printStackTrace();
                    return;
                }

                connectState = QXMqttConnectState.CONNECTING;
                try {
                    client.connect().waitForCompletion();

                    QXLog.d(TAG, "connect success");
                    connectState = QXMqttConnectState.CONNECTED;
                    callbackConn();


                } catch (MqttException mqttException) {

                    int reasonCode = mqttException.getReasonCode();
                    if (reasonCode == 32100 || reasonCode == 32110 || reasonCode == 32102 || reasonCode == 32111) {
                        return;
                    }
                    QXLog.d(TAG, "connect failure");
                    mqttException.printStackTrace();
                    connectState = QXMqttConnectState.CONNECTFAIL;

                    callbackDisConn(QXError.ERROR_CONN_NET);

                    QXLog.e(TAG, "连接失败,准备重试 " + returnCount++);
                    sleepTime(2000);
                    //准备重连
                    callbackReConn();
                    connect();

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
    public void publish() {

    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {

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

    private MqttCallback mqttCallback = new MqttCallbackExtended() {
        @Override
        public void connectComplete(boolean reconnect, String serverURI) {
            //reconnect  true : fisrt connect  , false : reconnect
            QXLog.e(TAG, "connectComplete reconnect = " + reconnect + " serverURI = " + serverURI);

        }

        @Override
        public void connectionLost(Throwable cause) {
            QXLog.e(TAG, "connectionLost");
            cause.printStackTrace();

        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            String msg = new String(message.getPayload());
            int qos = message.getQos();
            QXLog.e(TAG, "messageArrived : topic = " + topic + "\nqos =  " + qos + "\nmessage = " + msg);
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            QXLog.e(TAG, "deliveryComplete ");
        }
    };


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

    private void callbackReConn() {
        IQxMqttListener listener = getListener();
        if (listener != null) {
            listener.onMqttReconnecting();
        }
    }
}
