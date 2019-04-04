package cn.com.startai.qxsdk.connect.mqtt.client;


import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;

import cn.com.startai.qxsdk.QX;
import cn.com.startai.qxsdk.connect.mqtt.IQXMqtt;
import cn.com.startai.qxsdk.connect.mqtt.ServerConnectState;
import cn.com.startai.qxsdk.connect.mqtt.QXMqttData;
import cn.com.startai.qxsdk.connect.mqtt.event.IQxMqttListener;
import cn.com.startai.qxsdk.connect.mqtt.event.QXTimerPingListener;
import cn.com.startai.qxsdk.connect.mqtt.event.QXTimerPingSender;
import cn.com.startai.qxsdk.event.IQXCallListener;
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


    //将构造函数私有化
    private QXMqttImpl() {
    }

    public static QXMqttImpl getInstance() {
        return SingleTonHoulder.singleTonInstance;
    }

    //静态内部类
    private static class SingleTonHoulder {
        private static final QXMqttImpl singleTonInstance = new QXMqttImpl();
    }


    private IMqttAsyncClient client;

    private QXTimerPingSender.PingListener pingListener;
    private MqttConnectOptions option;
    private IQxMqttListener listener;

    private ServerConnectState connectState;

    private int retryCount;
    private String clientId;

    private Handler mHandler;
    private HandlerThread ht;

    @Override
    public void init() {

        if (mHandler == null) {
            ht = new HandlerThread(TAG + "QXMqttImpl");
            ht.start();
            mHandler = new Handler(ht.getLooper());
        }
        pingListener = new QXTimerPingListener();
        QXNetworkManager.getInstance().addNetworkListener(this);
        connect();
    }

    @Override
    public void release() {

        QXNetworkManager.getInstance().removeNetworkListener(this);

        disconnect(true);

        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        if (ht != null) {
            ht.quit();
            ht = null;
        }
    }


    private synchronized void connect() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    if (connectState == ServerConnectState.CONNECTED && QXSpController.getIsActivite()) {
                        //激活成功才回调连接成功
                        callbackConn();
                        return;
                    }
                    if (!QXNetworkManager.getInstance().isAvaliableNetwork(false)) {
                        QXLog.e(TAG, "network is unvaliable ");
                        connectState = ServerConnectState.DISCONNECTED;
                        callbackDisConn(QXError.ERROR_CONN_NET);
                        return;
                    }


                    if (connectState == ServerConnectState.CONNECTING) {
                        QXLog.e(TAG, "正在连接中，无需重复连接");
                        return;
                    }
                    connectState = ServerConnectState.CONNECTING;
                    option = QXMqttConfig.getConnectOptions(QX.getInstance().getApp());
                    if (option == null) {
                        callbackDisConn(QXError.ERROR_CONN_CER);
                        return;
                    }

                    String host = QXMqttConfig.getOptimalHost();


                    clientId = QXMqttConfig.getClientId();
                    QXLog.d(TAG, "connect host = " + host + " clientid = " + clientId);
                    QXTimerPingSender QXTimerPingSender = new QXTimerPingSender(pingListener);
                    client = new MqttAsyncClient(host, clientId, null, QXTimerPingSender);
                    client.setCallback(mqttCallback);

                } catch (MqttException e) {
                    e.printStackTrace();
                    return;
                }


                try {
                    client.connect(option).waitForCompletion();

                    QXLog.e(TAG, "connect success host = " + client.getServerURI() + " clientid = " + client.getClientId());
                    connectState = ServerConnectState.CONNECTED;
                    pingListener.onReset();
                    QXMqttConfig.setClientId(clientId);
                    QXMqttConfig.setHost(client.getServerURI());

                    callbackConn();


                } catch (MqttException mqttException) {

                    int reasonCode = mqttException.getReasonCode();
                    if (reasonCode == 32100 || reasonCode == 32110 || reasonCode == 32102 || reasonCode == 32111) {
                        return;
                    }
                    QXLog.e(TAG, "connect failure");
                    mqttException.printStackTrace();
                    connectState = ServerConnectState.CONNECTFAIL;

                    callbackDisConn(mqttException.getReasonCode() + "", mqttException.getMessage());

                    QXLog.e(TAG, "连接失败,准备重试 " + retryCount++);
                    //准备重连
                    callbackReConn();
                    reconnectWithDelay(2000);

                }
            }
        });
    }


    @Override
    public void disconnect(boolean isRelease) {
        connectState = ServerConnectState.DISCONNECTING;
        try {
            client.disconnect().waitForCompletion();
            QXLog.e(TAG, "client disconnected");
            if (isRelease) {
                client.close();
                QXLog.e(TAG, "client closed");
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
        connectState = ServerConnectState.DISCONNECTED;
    }

    @Override
    public synchronized void publish(final QXMqttData mqttData, final IQXCallListener onCallListener) {

        if (client != null && connectState == ServerConnectState.CONNECTED) {

            final MqttMessage mqttMessage = new MqttMessage(mqttData.getData());

            try {
                QXLog.d(TAG, "publish before");
                client.publish(mqttData.getTopic(), mqttMessage, null, new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        QXLog.d(TAG, "topic = " + mqttData.getTopic() + "\nqos = " + mqttMessage.getQos() + "\nretain = " + mqttMessage.isRetained() + "\npublish " + new String(mqttMessage.getPayload()));

                        //send success
                        QXCallbackManager.callbackCallResult(true, onCallListener, null);
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                    }
                });


            } catch (MqttException e) {
                e.printStackTrace();
                QXCallbackManager.callbackCallResult(true, onCallListener, e.getReasonCode() + "");
            }

        } else {
            QXCallbackManager.callbackCallResult(false, onCallListener, QXError.ERROR_SEND_CLIENT_DISCONNECT);
        }


    }

    private synchronized void toSubscribe(boolean isSync, List<String> topics, final IQXCallListener callListener) {
        if (connectState == ServerConnectState.CONNECTED) {
            try {
                if (topics == null) {
                    QXLog.e(TAG, "topics must not be null");
                    QXCallbackManager.callbackCallResult(false, callListener, QXError.ERROR_SUB_NULL_TOPIC);
                    return;
                }

                String[] topicArr = new String[topics.size()];

                topicArr = topics.toArray(topicArr);


                int[] qoss = new int[topics.size()];
                for (int i : qoss) {
                    qoss[i] = 1;
                }
                if (isSync) {

                    client.subscribe(topicArr, qoss).waitForCompletion();
                    QXLog.e(TAG, "sub topics = " + Arrays.toString(topicArr) + " success");
                    QXCallbackManager.callbackCallResult(true, callListener, null);
                } else {
                    final String[] finalTopicArr = topicArr;
                    client.subscribe(topicArr, qoss, null, new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            QXLog.e(TAG, "sub topics = " + Arrays.toString(finalTopicArr) + " success");
                            QXCallbackManager.callbackCallResult(true, callListener, null);
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                        }
                    });
                }


            } catch (MqttException e) {
                QXLog.e(TAG, "sub topics = " + topics + " failed");
                QXCallbackManager.callbackCallResult(false, callListener, e.getReasonCode() + "", e.getMessage());
            } catch (Exception e) {
                QXLog.e(TAG, "sub topics = " + topics + " failed");
                QXCallbackManager.callbackCallResult(false, callListener, QXError.UNKOWN + "", e.getMessage());
            }

        } else {
            QXLog.e(TAG, "sub failed " + QXError.ERROR_SUB_NO_CONN);
            QXCallbackManager.callbackCallResult(false, callListener, QXError.ERROR_SUB_NO_CONN);
        }
    }

    @Override
    public synchronized void subscribe(final List<String> topics, final IQXCallListener callListener) {
        toSubscribe(false, topics, callListener);
    }

    @Override
    public synchronized void subscribeSync(List<String> topics, IQXCallListener callListener) {
        toSubscribe(true, topics, callListener);
    }


    @Override
    public synchronized void unSubscribe(final List<String> topics, final IQXCallListener IQXCallListener) {

        if (connectState == ServerConnectState.CONNECTED) {
            try {
                if (topics == null) {
                    QXLog.e(TAG, "topics must not be null");
                    QXCallbackManager.callbackCallResult(false, IQXCallListener, QXError.ERROR_SUB_NULL_TOPIC);
                    return;
                }

                String[] topicArr = new String[topics.size()];

                topicArr = topics.toArray(topicArr);

                client.unsubscribe(topicArr).waitForCompletion();

                QXLog.d(TAG, "sub topics = " + Arrays.toString(topicArr) + " success");
                QXCallbackManager.callbackCallResult(true, IQXCallListener, null);
            } catch (MqttException e) {
                QXLog.e(TAG, "sub topics = " + topics + " failed");
                QXCallbackManager.callbackCallResult(false, IQXCallListener, e.getReasonCode() + "", e.getMessage());
            } catch (Exception e) {
                QXLog.e(TAG, "sub topics = " + topics + " failed");
                QXCallbackManager.callbackCallResult(false, IQXCallListener, QXError.UNKOWN + "", e.getMessage());
            }
        }

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
    public synchronized ServerConnectState getQXMqttConnectState() {
        return connectState;
    }

    @Override
    public synchronized void disconnectAndReconnect() {
        QXLog.d(TAG, "准备断开连接并重连");
        disconnect(false);
        connect();
    }

    private MqttCallback mqttCallback = new MqttCallback() {


        @Override
        public void connectionLost(Throwable cause) {
            QXLog.e(TAG, "connectionLost");
            cause.printStackTrace();
            connectState = ServerConnectState.DISCONNECTED;
            callbackDisConn(QXError.ERROR_CONN_NET);
            callbackReConn();

            reconnectWithDelay(1000);
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
//            QXLog.e(TAG, "deliveryComplete ");
        }
    };

    private synchronized void reconnectWithDelay(long timeDelay) {
        final String taskKey = "reconnectWithDelay";
        QXLog.d(TAG, "reconnecting timeDelay = " + timeDelay);
        QXTimerUtil.schedule(taskKey, new TimerTask() {
            @Override
            public void run() {
                if (connectState == ServerConnectState.CONNECTED || connectState == ServerConnectState.CONNECTING) {
                    QXLog.e(TAG, "正在连接中，或已经连接成功，无需重复进行连接");
                    return;
                }
                connect();
                QXTimerUtil.close(taskKey);
            }
        }, timeDelay);

    }

    @Override
    public void onWifiConnected() {
        reconnectWithDelay(100);
    }

    @Override
    public void onMobileConnected() {
        reconnectWithDelay(100);
    }

    @Override
    public void onEthernetConnected() {
        reconnectWithDelay(100);
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
