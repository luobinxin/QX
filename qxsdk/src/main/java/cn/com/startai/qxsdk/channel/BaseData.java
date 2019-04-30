package cn.com.startai.qxsdk.channel;

import java.util.Arrays;

import cn.com.swain.support.protocolEngine.Repeat.RepeatMsgModel;

/**
 * Created by Robin on 2019/3/20.
 * 419109715@qq.com 彬影
 */
public class BaseData {


    private String mac;
    private String ip;
    private int port;
    private String topic;
    private byte[] data;


    @Override
    public String toString() {
        return "BaseData{" +
                "mac='" + mac + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", topic='" + topic + '\'' +
                ", data=" + Arrays.toString(data) +
                ", communicationMode=" + communicationMode +
                ", repeatMsgModel=" + repeatMsgModel +
                '}';
    }


    public BaseData(byte[] data) {
        this.data = data;
    }

    public BaseData(String topic, byte[] data) {
        this.topic = topic;
        this.data = data;
    }

    public BaseData(String ip, int port, byte[] data) {
        this.ip = ip;
        this.port = port;
        this.data = data;
    }


    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    protected CommunicationMode communicationMode;


    public void initMqttBleUdpMode() {
        communicationMode = CommunicationMode.MQTT_BLE_UDP;
    }

    public void initMqttUdpMode() {
        communicationMode = CommunicationMode.MQTT_BLE_UDP;
    }

    public void initMqttBleMode() {
        communicationMode = CommunicationMode.MQTT_BLE;
    }

    public void initBleUdpMode() {
        communicationMode = CommunicationMode.BLE_UDP;
    }


    public void initBleMode() {
        communicationMode = CommunicationMode.BLE;
    }

    public void initMqttMode() {
        communicationMode = CommunicationMode.MQTT;
    }

    public void initUdpMode() {
        communicationMode = CommunicationMode.UDP;
    }


    /**
     * 重发的数据结构
     */
    private final RepeatMsgModel repeatMsgModel = new RepeatMsgModel();

    public CommunicationMode getCommunicationMode() {
        return communicationMode;
    }

    public void setCommunicationMode(CommunicationMode communicationMode) {
        this.communicationMode = communicationMode;
    }

    public RepeatMsgModel getRepeatMsgModel() {


        return repeatMsgModel;
    }

}
