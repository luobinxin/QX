package cn.com.startai.qxsdk.connect.mqtt.task;

import cn.com.startai.qxsdk.busi.entity.Activate;
import cn.com.startai.qxsdk.busi.entity.Login;
import cn.com.startai.qxsdk.busi.entity.UpdateDeviceInfo;

/**
 * Created by Robin on 2019/3/25.
 * 419109715@qq.com 彬影
 */
public interface OnMqttTaskCallBack {

    void onLoginResult(Login.Resp resp);

    void onMessageArrived(String topic, String message);

    void onActivateResult(Activate.Resp resp);

    void onHardwareActivateResult(Activate.Resp resp);

    void onUpdateDeviceInfoResult(UpdateDeviceInfo.Resp resp);
}
