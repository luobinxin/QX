package cn.com.startai.qxsdk.channel.mqtt.task;

import cn.com.startai.qxsdk.channel.mqtt.entity.Activate;
import cn.com.startai.qxsdk.channel.mqtt.entity.Bind;
import cn.com.startai.qxsdk.channel.mqtt.entity.BindEmail;
import cn.com.startai.qxsdk.channel.mqtt.entity.BindMobile;
import cn.com.startai.qxsdk.channel.mqtt.entity.BindThirdAccount;
import cn.com.startai.qxsdk.channel.mqtt.entity.BrokerHost;
import cn.com.startai.qxsdk.channel.mqtt.entity.CheckIdentifyCode;
import cn.com.startai.qxsdk.channel.mqtt.entity.GetAlipayAuthInfo;
import cn.com.startai.qxsdk.channel.mqtt.entity.GetBindList;
import cn.com.startai.qxsdk.channel.mqtt.entity.GetIdentifyCode;
import cn.com.startai.qxsdk.channel.mqtt.entity.GetLatestAppVersion;
import cn.com.startai.qxsdk.channel.mqtt.entity.GetRealPayResult;
import cn.com.startai.qxsdk.channel.mqtt.entity.GetUserInfo;
import cn.com.startai.qxsdk.channel.mqtt.entity.GetWeatherInfo;
import cn.com.startai.qxsdk.channel.mqtt.entity.Login;
import cn.com.startai.qxsdk.channel.mqtt.entity.Passthrough;
import cn.com.startai.qxsdk.channel.mqtt.entity.Register;
import cn.com.startai.qxsdk.channel.mqtt.entity.ResetLoginPwd;
import cn.com.startai.qxsdk.channel.mqtt.entity.SendEmail;
import cn.com.startai.qxsdk.channel.mqtt.entity.ThirdPaymentUnifiedOrder;
import cn.com.startai.qxsdk.channel.mqtt.entity.UnActivate;
import cn.com.startai.qxsdk.channel.mqtt.entity.UnBindThirdAccount;
import cn.com.startai.qxsdk.channel.mqtt.entity.UnBind;
import cn.com.startai.qxsdk.channel.mqtt.entity.UpdateDeviceInfo;
import cn.com.startai.qxsdk.channel.mqtt.entity.UpdateLoginPwd;
import cn.com.startai.qxsdk.channel.mqtt.entity.UpdateRemark;
import cn.com.startai.qxsdk.channel.mqtt.entity.UpdateUserInfo;

/**
 * Created by Robin on 2019/3/25.
 * 419109715@qq.com 彬影
 */
public interface OnMqttTaskCallBack {

    void onLoginResult(Login.Resp resp);

    void onMessageArrived(String topic, byte[] message);

    void onActivateResult(Activate.Resp resp);

    void onHardwareActivateResult(Activate.Resp resp);

    void onUpdateDeviceInfoResult(UpdateDeviceInfo.Resp resp);

    void onGetBrokerHostesult(BrokerHost.Resp resp);

    void onGetIdentifyCodeResult(GetIdentifyCode.Resp resp);

    void onCheckIdentifyCodeResult(CheckIdentifyCode.Resp resp);

    void onRegisterResult(Register.Resp resp);

    void onSendEmail(SendEmail.Resp resp);

    void onUpdateUserInfoResult(UpdateUserInfo.Resp resp);

    void onGetUserInfoResult(GetUserInfo.Resp resp);

    void onGetLatestAppVersionResult(GetLatestAppVersion.Resp resp);

    void onResetLoginPwdResult(ResetLoginPwd.Resp resp);

    void onUpdateLoginPwdResult(UpdateLoginPwd.Resp resp);

    void onUpdateRemarkResult(UpdateRemark.Resp resp);

    void onUnActivateResult(UnActivate.Resp resp);

    void onUnBindResult(UnBind.Resp resp);

    void onBindResult(Bind.Resp resp);

    void onGetBindListResult(GetBindList.Resp resp);

    void onBindEmailResult(BindEmail.Resp resp);

    void onBindThirdAccountResult(BindThirdAccount.Resp resp);

    void onUnBindThirdAccountResult(UnBindThirdAccount.Resp resp);

    void onGetWeatherInfoResult(GetWeatherInfo.Resp resp);

    void onBindMobileResult(BindMobile.Resp resp);

    void onGetAlipayAuthInfoResult(GetAlipayAuthInfo.Resp resp);

    void onGetRealPayResult(GetRealPayResult.Resp resp);

    void onThirdPaymentUnifiedOrderResult(ThirdPaymentUnifiedOrder.Resp resp);

    void onDeviceOnlineOffLine(int onlineState , String sn);

    void onPassthrough(Passthrough.Resp resp);


}
