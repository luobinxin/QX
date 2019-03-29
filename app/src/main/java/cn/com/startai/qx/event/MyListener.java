package cn.com.startai.qx.event;

import cn.com.startai.helper.TAndL;
import cn.com.startai.qxsdk.busi.entity.Activate;
import cn.com.startai.qxsdk.busi.entity.Login;
import cn.com.startai.qxsdk.busi.entity.UpdateDeviceInfo;
import cn.com.startai.qxsdk.db.bean.DeviceBean;
import cn.com.startai.qxsdk.event.IQXBusiResultListener;
import cn.com.startai.qxsdk.global.QXError;

/**
 * Created by Robin on 2019/3/26.
 * 419109715@qq.com 彬影
 */
public class MyListener implements IQXBusiResultListener {
    /**
     * 更新设备信息
     *
     * @param resp
     */
    @Override
    public void onUpdateDeviceInfoResult(UpdateDeviceInfo.Resp resp) {

        TAndL.L("MyListener onUpdateDeviceInfoResult resp = " + resp);
    }

    /**
     * 登录结果
     *
     * @param resp
     */
    @Override
    public void onLoginResult(Login.Resp resp) {
        TAndL.L("MyListener onLoginResult resp = " + resp);

    }

    /**
     * 与服务器连接成功
     */
    @Override
    public void onServerConnected() {
        TAndL.L("MyListener onServerConnected");
    }

    /**
     * 与服务器断开连接|连接失败
     *
     * @param qxError
     */
    @Override
    public void onServerDisConnect(QXError qxError) {
        TAndL.L("MyListener onServerDisConnect qxError = " + qxError);
    }

    /**
     * 重连接中
     */
    @Override
    public void onServerReConnecting() {
        TAndL.L("MyListener onServerReConnecting");
    }

    /**
     * 发现一台设备（局域网）
     *
     * @param deviceBean
     */
    @Override
    public void onDiscoveryResult(DeviceBean deviceBean) {
        TAndL.L("MyListener onDiscoveryResult");
    }

    /**
     * 开始发现（局域网）
     */
    @Override
    public void onDiscoveryStart() {
        TAndL.L("MyListener onDiscoveryStart");
    }

    /**
     * 停止发现（局域网）
     */
    @Override
    public void onDiscoveryStop() {
        TAndL.L("MyListener onDiscoveryStop");
    }

    /**
     * 第三方硬件激活结果
     *
     * @param resp
     */
    @Override
    public void onHardwareActivateResult(Activate.Resp resp) {

        TAndL.L("MyListener onHardwareActivateResult");
    }
}
