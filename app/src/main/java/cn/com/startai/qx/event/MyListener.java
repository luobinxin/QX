package cn.com.startai.qx.event;

import android.net.NetworkInfo;

import org.greenrobot.eventbus.EventBus;

import cn.com.startai.qx.utils.EventAction;
import cn.com.startai.qx.utils.EventBean;
import cn.com.startai.qxsdk.busi.common.BindDeviceResp;
import cn.com.startai.qxsdk.busi.common.ConnectDeviceResp;
import cn.com.startai.qxsdk.busi.common.GetBindListResp;
import cn.com.startai.qxsdk.busi.common.PassthroughResp;
import cn.com.startai.qxsdk.busi.common.UnBindDeviceResp;
import cn.com.startai.qxsdk.busi.socket.ISocketBusiListener;
import cn.com.startai.qxsdk.busi.socket.entity.RecoveryResp;
import cn.com.startai.qxsdk.busi.socket.entity.RenameDeviceResp;
import cn.com.startai.qxsdk.busi.socket.entity.UpdateVersionResp;
import cn.com.startai.qxsdk.channel.mqtt.entity.Activate;
import cn.com.startai.qxsdk.channel.mqtt.entity.BindEmail;
import cn.com.startai.qxsdk.channel.mqtt.entity.BindMobile;
import cn.com.startai.qxsdk.channel.mqtt.entity.BindThirdAccount;
import cn.com.startai.qxsdk.channel.mqtt.entity.CheckIdentifyCode;
import cn.com.startai.qxsdk.channel.mqtt.entity.GetAlipayAuthInfo;
import cn.com.startai.qxsdk.channel.mqtt.entity.GetIdentifyCode;
import cn.com.startai.qxsdk.channel.mqtt.entity.GetLatestAppVersion;
import cn.com.startai.qxsdk.channel.mqtt.entity.GetRealPayResult;
import cn.com.startai.qxsdk.channel.mqtt.entity.GetUserInfo;
import cn.com.startai.qxsdk.channel.mqtt.entity.GetWeatherInfo;
import cn.com.startai.qxsdk.channel.mqtt.entity.Login;
import cn.com.startai.qxsdk.channel.mqtt.entity.Register;
import cn.com.startai.qxsdk.channel.mqtt.entity.ResetLoginPwd;
import cn.com.startai.qxsdk.channel.mqtt.entity.SendEmail;
import cn.com.startai.qxsdk.channel.mqtt.entity.ThirdPaymentUnifiedOrder;
import cn.com.startai.qxsdk.channel.mqtt.entity.UnActivate;
import cn.com.startai.qxsdk.channel.mqtt.entity.UnBindThirdAccount;
import cn.com.startai.qxsdk.channel.mqtt.entity.UpdateDeviceInfo;
import cn.com.startai.qxsdk.channel.mqtt.entity.UpdateLoginPwd;
import cn.com.startai.qxsdk.channel.mqtt.entity.UpdateRemark;
import cn.com.startai.qxsdk.channel.mqtt.entity.UpdateUserInfo;
import cn.com.startai.qxsdk.channel.BaseData;
import cn.com.startai.qxsdk.db.bean.DeviceBean;
import cn.com.startai.qxsdk.global.QXError;

/**
 * Created by Robin on 2019/3/26.
 * 419109715@qq.com 彬影
 */
public class MyListener implements ISocketBusiListener {
    /**
     * 连接设备结果
     *
     * @param resp
     */
    @Override
    public void onConnectDeviceResult(ConnectDeviceResp resp) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_CONNECT_DEVICE_RESULT, resp));
    }

    /**
     * 消息透传结果
     *
     * @param resp
     */
    @Override
    public void onPassthroughResult(PassthroughResp resp) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_PASSTHROUGH_RESULT, resp));

    }

    @Override
    public void onMessageArrive(BaseData baseData) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_MSG_ARRIVE, baseData));
    }

    /**
     * 修改备注名结果
     *
     * @param resp 成功内容
     */
    @Override
    public void onUpdateRemarkResult(UpdateRemark.Resp resp) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_UPDATE_REMARK_RESULT, resp));
    }

    /**
     * 删除好友回调
     *
     * @param resp
     */
    @Override
    public void onUnBindResult(UnBindDeviceResp resp) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_UNBIND_RESULT, resp));

    }

    /**
     * 查询最新软件版本结果
     *
     * @param resp 最新软件版本信息
     */
    @Override
    public void onGetLatestVersionResult(GetLatestAppVersion.Resp resp) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_GET_LATEST_VERSION_RESULT, resp));
    }

    /**
     * 更新用户密码返回
     *
     * @param resp 用户密码信息
     */
    @Override
    public void onUpdateLoginPwdResult(UpdateLoginPwd.Resp resp) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_UPDATE_USER_PWD_RESULT, resp));
    }

    /**
     * 更新用户信息结果
     *
     * @param resp
     */
    @Override
    public void onUpdateUserInfoResult(UpdateUserInfo.Resp resp) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_UPDATE_USERINFO_RESULT, resp));
    }

    /**
     * 更新设备信息
     *
     * @param resp
     */
    @Override
    public void onUpdateDeviceInfoResult(UpdateDeviceInfo.Resp resp) {

        EventBus.getDefault().post(new EventBean(EventAction.ACTION_UPDATE_DEVICE_INFO_RESULT, resp));
    }

    /**
     * 查询用户信息结果
     *
     * @param resp
     */
    @Override
    public void onGetUserInfoResult(GetUserInfo.Resp resp) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_GET_USERINFO_RESULT, resp));
    }

    /**
     * 发送邮件结果返回
     *
     * @param resp 成功的信息
     */
    @Override
    public void onSendEmailResult(SendEmail.Resp resp) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_SEND_EMAIL_RESULT, resp));
    }

    /**
     * 登录过期 需要重新登录
     */
    @Override
    public void onLoginExpired() {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_LOGIN_EXPIRED));
    }

    /**
     * 登录结果
     *
     * @param resp
     */
    @Override
    public void onLoginResult(Login.Resp resp) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_LOGIN_RESULT, resp));
    }

    /**
     * 注册结果回调
     *
     * @param resp
     */
    @Override
    public void onRegisterResult(Register.Resp resp) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_REGISTER_RESULT, resp));
    }

    /**
     * 检验验证码结果
     *
     * @param resp
     */
    @Override
    public void onCheckIdetifyResult(CheckIdentifyCode.Resp resp) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_CHECK_IDENTIFY_CODE_RESULT, resp));
    }

    /**
     * 获取验证码结果
     *
     * @param resp
     */
    @Override
    public void onGetIdentifyCodeResult(GetIdentifyCode.Resp resp) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_GET_IDENTIFY_CODE_RESULT, resp));
    }

    /**
     * 与服务器连接成功
     */
    @Override
    public void onServerConnected() {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_SERVER_CONNECTED));

//        final String topic1 = "Q/client/868893046962824/#";
//        final String topic2 = "SERVICE/NMC/qx55e3cf4bf12fd3f7/#";
//        List<String> strs = new ArrayList();
//        strs.add(topic1);
//        strs.add(topic2);
//        QXMqttImpl.getInstance().subscribeSync(strs, new IQXCallListener() {
//            @Override
//            public void onSuccess() {
//                TAndL.L("sub success TTTTT");
//                QXMqttData mqttData = new QXMqttData("abce".getBytes(), "Q/client/868893046962824");
//                QXMqttImpl.getInstance().publish(mqttData, null);
//
//            }
//
//            @Override
//            public void onFailed(QXError error) {
//                TAndL.L("sub Feiled TTTTT");
//
//
//            }
//        });

    }

    /**
     * 与服务器断开连接|连接失败
     *
     * @param qxError
     */
    @Override
    public void onServerDisConnect(QXError qxError) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_SERVER_DISCONNECTED, qxError));
    }

    /**
     * 重连接中
     */
    @Override
    public void onServerReConnecting() {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_SERVER_RECONNECTING));
    }

    /**
     * 发现一台设备（局域网）
     *
     * @param deviceBean
     */
    @Override
    public void onDiscoveryResult(DeviceBean deviceBean) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_DISCOVERY_RESULT, deviceBean));
    }

    /**
     * 开始发现（局域网）
     */
    @Override
    public void onDiscoveryStart() {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_DISCOVERY_START));
    }

    /**
     * 停止发现（局域网）
     */
    @Override
    public void onDiscoveryStop() {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_DISCOVERY_STOP));
    }

    /**
     * 设备属性状态 变化  包括  ssid rssi bssid 局域网连接状态 ，广域网连接状态 ，绑定状态 等等
     *
     * @param deviceBean
     */
    @Override
    public void onDeviceBeanStateChange(DeviceBean deviceBean) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_DEVICEBEAN_STATUS_CHANE, deviceBean));
    }

    /**
     * 设备激活回调，如果激活成功只会回调一次
     *
     * @param resp
     */
    @Override
    public void onActiviteResult(Activate.Resp resp) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_ACTIVATE_RESULT, resp));
    }

    /**
     * 第三方硬件激活结果
     *
     * @param resp
     */
    @Override
    public void onHardwareActivateResult(Activate.Resp resp) {

        EventBus.getDefault().post(new EventBean(EventAction.ACTION_HARDWARE_ACTIVATE_RESULT, resp));
    }

    /**
     * 添加好友回调
     *
     * @param resp
     */
    @Override
    public void onBindResult(BindDeviceResp resp) {

        EventBus.getDefault().post(new EventBean(EventAction.ACTION_BIND_RESULT, resp));
    }

    /**
     * 注销激活
     *
     * @param resp
     */
    @Override
    public void onUnActiviteResult(UnActivate.Resp resp) {

        EventBus.getDefault().post(new EventBean(EventAction.ACTION_UNACTIVATE_RESULT, resp));
    }

    /**
     * 重置登录密码结果
     *
     * @param resp 成功内容
     */
    @Override
    public void onResetLoginPwdResult(ResetLoginPwd.Resp resp) {

        EventBus.getDefault().post(new EventBean(EventAction.ACTION_RESET_LOGIN_PWE_RESULT, resp));
    }

    /**
     * 第三方支付 统一下单结果
     *
     * @param resp
     */
    @Override
    public void onThirdPaymentUnifiedOrderResult(ThirdPaymentUnifiedOrder.Resp resp) {

    }

    /**
     * 查询真实订单支付结果
     *
     * @param resp
     */
    @Override
    public void onGetRealOrderPayStatus(GetRealPayResult.Resp resp) {

    }

    /**
     * 查询支付宝认证信息
     *
     * @param resp
     */
    @Override
    public void onGetAlipayAuthInfoResult(GetAlipayAuthInfo.Resp resp) {

    }

    /**
     * 绑定手机号返回
     *
     * @param resp
     */
    @Override
    public void onBindMobileNumResult(BindMobile.Resp resp) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_BIND_MOBILE_RESULT, resp));
    }

    /**
     * 查询天气信息返回
     *
     * @param resp
     */
    @Override
    public void onGetWeatherInfoResult(GetWeatherInfo.Resp resp) {

    }

    /**
     * 解绑第三方账号返回
     *
     * @param resp
     */
    @Override
    public void onUnBindThirdAccountResult(UnBindThirdAccount.Resp resp) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_UNBIND_THIRDACCOUNT_RESULT, resp));
    }

    /**
     * 绑定第三方账号返回
     *
     * @param resp
     */
    @Override
    public void onBindThirdAccountResult(BindThirdAccount.Resp resp) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_BIND_THIRDACCOUNT_RESULT, resp));
    }

    /**
     * 获取绑定列表 分页
     *
     * @param resp
     */
    @Override
    public void onGetBindListResult(GetBindListResp resp) {

        EventBus.getDefault().post(new EventBean(EventAction.ACTION_GET_BIND_LIST_RESULT, resp));
    }

    /**
     * 绑定邮箱返回
     *
     * @param resp
     */
    @Override
    public void onBindEmailResult(BindEmail.Resp resp) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_BIND_EMAIL_RESULT, resp));
    }

    /**
     * 登出
     *
     * @param result
     */
    @Override
    public void onLogoutResult(int result) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_LOGOUT_RESULT, result));
    }

    /**
     * 网络状态变化
     *
     * @param networkType
     * @param state
     */
    @Override
    public void onNetworkChange(String networkType, NetworkInfo.State state) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_NETWORK_CHANGE, networkType, state));
    }


    @Override
    public void onUpdateVersionResult(UpdateVersionResp resp) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_UPDATE_VERSION_RESULT, resp));
    }

    @Override
    public void onSettingRecoveryResult(RecoveryResp resp) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_RECOVERY_RESULT, resp));
    }

    @Override
    public void onDeviceRenameResult(RenameDeviceResp resp) {
        EventBus.getDefault().post(new EventBean(EventAction.ACTION_RENAME_RESULT, resp));

    }
}
