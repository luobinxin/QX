package cn.com.startai.qxsdk.event;

import cn.com.startai.qxsdk.channel.BaseData;

/**
 * Created by Robin on 2019/4/10.
 * 419109715@qq.com 彬影
 */
public interface IOutput {

    void doSend(BaseData baseData, IQXCallListener listener);

    void doSendMqtt(BaseData qxMqttData, IQXCallListener listener);

    boolean doSendUdp(BaseData qxudpData);

    void doSendBle(BaseData qxbleData);


}
