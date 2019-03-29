package cn.com.startai.qxsdk.connect;

import java.util.Arrays;

/**
 * Created by Robin on 2019/3/20.
 * 419109715@qq.com 彬影
 */
public class BaseData {

    private CommunicationMode communicationMode;

    private byte[] data;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public BaseData(byte[] data) {
        this.data = data;
    }

    public void initUDPSendType() {
        communicationMode = CommunicationMode.UDP;
    }

    public boolean isUDPSendType() {
        return communicationMode == CommunicationMode.UDP;
    }

    public void initUDPMqtt() {
        communicationMode = CommunicationMode.MQTT;
    }

    public boolean isMqttSendType() {
        return communicationMode == CommunicationMode.MQTT;
    }


}
