package cn.com.startai.qxsdk.channel.mqtt.client;

import java.util.Arrays;

/**
 * Created by Robin on 2018/5/7.
 * qq: 419109715 彬影
 */

public class QXMqttWill {

    private String topic;
    private byte[] playload;
    private int qos;
    private boolean retains;

    @Override
    public String toString() {
        return "QXMqttWill{" +
                "topic='" + topic + '\'' +
                ", playload=" + Arrays.toString(playload) +
                ", qos=" + qos +
                ", retains=" + retains +
                '}';
    }

    public QXMqttWill() {
    }

    public QXMqttWill(String topic, byte[] playload, int qos, boolean retains) {
        this.topic = topic;
        this.playload = playload;
        this.qos = qos;
        this.retains = retains;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public byte[] getPlayload() {
        return playload;
    }

    public void setPlayload(byte[] playload) {
        this.playload = playload;
    }

    public int getQos() {
        return qos;
    }

    public void setQos(int qos) {
        this.qos = qos;
    }

    public boolean getRetains() {
        return retains;
    }

    public void setRetains(boolean retains) {
        this.retains = retains;
    }
}
