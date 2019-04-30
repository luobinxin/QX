package cn.com.startai.qxsdk.event;

import android.net.NetworkInfo;

import cn.com.startai.qxsdk.busi.common.BindDeviceResp;
import cn.com.startai.qxsdk.busi.common.ConnectDeviceResp;
import cn.com.startai.qxsdk.busi.common.GetBindListResp;
import cn.com.startai.qxsdk.busi.common.PassthroughResp;
import cn.com.startai.qxsdk.busi.common.UnBindDeviceResp;
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
 * Created by Robin on 2019/3/25.
 * 419109715@qq.com 彬影
 */
public interface IQXBusiResultListener {

    /**
     * 连接设备结果
     *
     * @param resp
     */
    void onConnectDeviceResult(ConnectDeviceResp resp);

    /**
     * 消息透传结果
     */
    void onPassthroughResult(PassthroughResp resp);


    void onMessageArrive(BaseData baseData);


    /**
     * 修改备注名结果
     *
     * @param resp 成功内容
     */
    void onUpdateRemarkResult(UpdateRemark.Resp resp);

    /**
     * 删除好友回调
     */
    void onUnBindResult(UnBindDeviceResp resp);


    /**
     * 查询最新软件版本结果
     *
     * @param resp 最新软件版本信息
     */
    void onGetLatestVersionResult(GetLatestAppVersion.Resp resp);


    /**
     * 更新用户密码返回
     *
     * @param resp 用户密码信息
     */
    void onUpdateLoginPwdResult(UpdateLoginPwd.Resp resp);

    /**
     * 更新用户信息结果
     *
     * @param resp
     */
    void onUpdateUserInfoResult(UpdateUserInfo.Resp resp);

    /**
     * 更新设备信息
     *
     * @param resp
     */
    void onUpdateDeviceInfoResult(UpdateDeviceInfo.Resp resp);


    /**
     * 查询用户信息结果
     *
     * @param resp
     */
    void onGetUserInfoResult(GetUserInfo.Resp resp);

    /**
     * 发送邮件结果返回
     *
     * @param resp 成功的信息
     */
    void onSendEmailResult(SendEmail.Resp resp);

    /**
     * 登录过期 需要重新登录
     */
    void onLoginExpired();

    /**
     * 登录结果
     *
     * @param resp
     */
    void onLoginResult(Login.Resp resp);

    /**
     * 注册结果回调
     */
    void onRegisterResult(Register.Resp resp);

    /**
     * 检验验证码结果
     */
    void onCheckIdetifyResult(CheckIdentifyCode.Resp resp);

    /**
     * 获取验证码结果
     */
    void onGetIdentifyCodeResult(GetIdentifyCode.Resp resp);


    /**
     * 与服务器连接成功
     */
    void onServerConnected();

    /**
     * 与服务器断开连接|连接失败
     *
     * @param qxError
     */
    void onServerDisConnect(QXError qxError);

    /**
     * 重连接中
     */
    void onServerReConnecting();



    /**
     * 发现一台设备（局域网）
     *
     * @param deviceBean
     */
    void onDiscoveryResult(DeviceBean deviceBean);

    /**
     * 开始发现（局域网）
     */
    void onDiscoveryStart();

    /**
     * 停止发现（局域网）
     */
    void onDiscoveryStop();

    /**
     * 设备属性状态 变化  包括  ssid rssi bssid 局域网连接状态 ，广域网连接状态 ，绑定状态 等等
     *
     * @param deviceBean
     */
    void onDeviceBeanStateChange(DeviceBean deviceBean);

    /**
     * 设备激活回调，如果激活成功只会回调一次
     */
    void onActiviteResult(Activate.Resp resp);


    /**
     * 第三方硬件激活结果
     */
    void onHardwareActivateResult(Activate.Resp resp);

    /**
     * 添加好友回调
     *
     * @param resp
     */
    void onBindResult(BindDeviceResp resp);

    /**
     * 注销激活
     */
    void onUnActiviteResult(UnActivate.Resp resp);


    /**
     * 重置登录密码结果
     *
     * @param resp 成功内容
     */
    void onResetLoginPwdResult(ResetLoginPwd.Resp resp);

    /**
     * 第三方支付 统一下单结果
     *
     * @param resp
     */
    void onThirdPaymentUnifiedOrderResult(ThirdPaymentUnifiedOrder.Resp resp);

    /**
     * 查询真实订单支付结果
     *
     * @param resp
     */
    void onGetRealOrderPayStatus(GetRealPayResult.Resp resp);

    /**
     * 查询支付宝认证信息
     *
     * @param resp
     */
    void onGetAlipayAuthInfoResult(GetAlipayAuthInfo.Resp resp);


    /**
     * 绑定手机号返回
     *
     * @param resp
     */
    void onBindMobileNumResult(BindMobile.Resp resp);

    /**
     * 查询天气信息返回
     *
     * @param resp
     */
    void onGetWeatherInfoResult(GetWeatherInfo.Resp resp);


    /**
     * 解绑第三方账号返回
     *
     * @param resp
     */
    void onUnBindThirdAccountResult(UnBindThirdAccount.Resp resp);

    /**
     * 绑定第三方账号返回
     *
     * @param resp
     */
    void onBindThirdAccountResult(BindThirdAccount.Resp resp);


    /**
     * 获取绑定列表 分页
     *
     * @param resp
     */
    void onGetBindListResult(GetBindListResp resp);


    /**
     * 绑定邮箱返回
     *
     * @param resp
     */
    void onBindEmailResult(BindEmail.Resp resp);


    /**
     * 登出
     *
     * @param result
     */
    void onLogoutResult(int result);

    /**
     * 网络状态变化
     *
     * @param networkType
     * @param state
     */
    void onNetworkChange(String networkType, NetworkInfo.State state);


}
