package cn.com.startai.qxsdk.connect.mqtt;


import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;

import cn.com.startai.qxsdk.connect.IConnectBusi;
import cn.com.startai.qxsdk.connect.mqtt.event.IQxMqttListener;

/**
 * Created by Robin on 2019/3/20.
 * 419109715@qq.com 彬影
 */
public interface IQXMqtt extends IConnectBusi {

    String TAG = "mqtt";

    void publish();

    void subscribe();

    void unSubscribe();

    void setListener(IQxMqttListener listener);

    IQxMqttListener getListener();

    IMqttAsyncClient getClient();

    QXMqttConnectState getQXMqttConnectState();

}
