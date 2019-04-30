package cn.com.startai.qxsdk.channel.mqtt;


import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;

import java.util.List;

import cn.com.startai.qxsdk.channel.BaseData;
import cn.com.startai.qxsdk.channel.IConnectBusi;
import cn.com.startai.qxsdk.channel.mqtt.event.IQxMqttListener;
import cn.com.startai.qxsdk.event.IQXCallListener;

/**
 * Created by Robin on 2019/3/20.
 * 419109715@qq.com 彬影
 */
public interface IQXMqtt extends IConnectBusi {


    void publish(BaseData data, IQXCallListener listener);

    void subscribe(List<String> topic, final IQXCallListener callListener);

    void subscribeSync(List<String> topic, final IQXCallListener callListener);

    void unSubscribe(List<String> topic, final IQXCallListener callListener);

    void setListener(IQxMqttListener listener);

    IQxMqttListener getListener();

    IMqttAsyncClient getClient();

    ServerConnectState getQXMqttConnectState();

    void disconnectAndReconnect();

    void disconnect(boolean isRelease);

}
