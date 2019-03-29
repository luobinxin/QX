package cn.com.startai.qxsdk.connect.mqtt;


import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;

import java.util.List;

import cn.com.startai.qxsdk.connect.IConnectBusi;
import cn.com.startai.qxsdk.connect.mqtt.event.IQxMqttListener;
import cn.com.startai.qxsdk.event.IOnCallListener;

/**
 * Created by Robin on 2019/3/20.
 * 419109715@qq.com 彬影
 */
public interface IQXMqtt extends IConnectBusi {


    void publish(QXMqttData data, IOnCallListener listener);

    void subscribe(List<String> topic, final IOnCallListener iOnCallListener);

    void unSubscribe(List<String> topic, final IOnCallListener iOnCallListener);

    void setListener(IQxMqttListener listener);

    IQxMqttListener getListener();

    IMqttAsyncClient getClient();

    QXMqttConnectState getQXMqttConnectState();

}
