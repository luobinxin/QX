package cn.com.startai.qxsdk.connect.udp.task;

import cn.com.startai.qxsdk.connect.udp.bean.LanBindingDevice;
import cn.com.startai.qxsdk.connect.udp.bean.LanDeviceInfo;
import cn.com.swain.support.protocolEngine.task.FailTaskResult;

/**
 * author: Guoqiang_Sun
 * date : 2018/4/10 0010
 * desc :
 */

public interface OnUdpTaskCallBack {


    void onFail(FailTaskResult mFailTask);

    void onSuccess(String mac, byte tye, byte cmd, int seq);

    /**
     * 心跳回复
     *
     * @param mac
     * @param result
     */
    void onHeartbeatResult(String mac, boolean result);

    /**
     * 设备发现
     *
     * @param id
     * @param result
     * @param mWiFiDevice
     */
    void onDeviceDiscoveryResult(String id, boolean result, LanDeviceInfo mWiFiDevice);

    /**
     * 设备开始发现
     */
    void onDiscoveryStart();

    /**
     * 设备发现结果
     */
    void onDiscoveryStop();

    /**
     * 局域网绑定
     *
     * @param result
     * @param mLanBindingDevice
     */
    void onLanBindResult(boolean result, LanBindingDevice mLanBindingDevice);

    /**
     * token 失效
     *
     * @param mac
     */
    void onTokenInvalid(String mac);

    /**
     * 局域网解绑
     *
     * @param result
     * @param mLanBindingDevice
     */
    void onLanUnBindResult(boolean result, LanBindingDevice mLanBindingDevice);

    /**
     * 请求token
     *
     * @param result
     * @param mac
     * @param random
     * @param token
     */
    void onRequestTokenResult(boolean result, String mac, int random, int token);

    /**
     * 请求连接
     *
     * @param result
     * @param id
     */
    void onConnectResult(boolean result, String id);

    /**
     * 休眠
     *
     * @param result
     * @param id
     */
    void onSleepResult(boolean result, String id);

//    /**
//     * 断开连接
//     *
//     * @param result
//     * @param id
//     */
//    void callbackLanConnectStatusChanged(boolean result, String id);

    void onRemoveCmd(String id, byte paramType, byte paramCmd, int seq);
}
