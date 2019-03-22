package cn.com.startai.qxsdk.connect;

import java.util.Arrays;

/**
 * Created by Robin on 2019/3/20.
 * 419109715@qq.com 彬影
 */
public class BaseData {

    private byte[] dataArr;

    private CommunicationMode communicationMode;

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


    @Override
    public String toString() {
        return "BaseData{" +
                "dataArr=" + Arrays.toString(dataArr) +
                '}';
    }

    public byte[] getDataArr() {
        return dataArr;
    }

    public void setDataArr(byte[] dataArr) {
        this.dataArr = dataArr;
    }

    public BaseData(byte[] dataArr) {
        this.dataArr = dataArr;
    }
}
