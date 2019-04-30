package cn.com.startai.qxsdk.channel.mqtt.event;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import cn.com.startai.qxsdk.global.QXError;

/**
 * Created by Robin on 2019/3/21.
 * 419109715@qq.com 彬影
 */
public interface IQxMqttListener {

    void onMqttConnected();

    void onMqttDisconnected(QXError qxError);

    void onMqttReconnecting();

    void onMessageArrived(String topic, MqttMessage message);

}
