package cn.com.startai.qxsdk.connect.mqtt.task;

import cn.com.startai.qxsdk.busi.entity.Activate;
import cn.com.startai.qxsdk.busi.entity.Bind;
import cn.com.startai.qxsdk.busi.entity.BindEmail;
import cn.com.startai.qxsdk.busi.entity.BindMobile;
import cn.com.startai.qxsdk.busi.entity.BindThirdAccount;
import cn.com.startai.qxsdk.busi.entity.BrokerHost;
import cn.com.startai.qxsdk.busi.entity.CheckIdentifyCode;
import cn.com.startai.qxsdk.busi.entity.GetAlipayAuthInfo;
import cn.com.startai.qxsdk.busi.entity.GetBindList;
import cn.com.startai.qxsdk.busi.entity.GetIdentifyCode;
import cn.com.startai.qxsdk.busi.entity.GetLatestAppVersion;
import cn.com.startai.qxsdk.busi.entity.GetRealPayResult;
import cn.com.startai.qxsdk.busi.entity.GetUserInfo;
import cn.com.startai.qxsdk.busi.entity.GetWeatherInfo;
import cn.com.startai.qxsdk.busi.entity.Login;
import cn.com.startai.qxsdk.busi.entity.Register;
import cn.com.startai.qxsdk.busi.entity.ResetLoginPwd;
import cn.com.startai.qxsdk.busi.entity.SendEmail;
import cn.com.startai.qxsdk.busi.entity.ThirdPaymentUnifiedOrder;
import cn.com.startai.qxsdk.busi.entity.UnActivate;
import cn.com.startai.qxsdk.busi.entity.UnBindThirdAccount;
import cn.com.startai.qxsdk.busi.entity.UnBind;
import cn.com.startai.qxsdk.busi.entity.UpdateDeviceInfo;
import cn.com.startai.qxsdk.busi.entity.UpdateLoginPwd;
import cn.com.startai.qxsdk.busi.entity.UpdateRemark;
import cn.com.startai.qxsdk.busi.entity.UpdateUserInfo;

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

    void onGetBrokerHostesult(BrokerHost.Resp resp);

    void onGetIdentifyCodeResult(GetIdentifyCode.Resp resp);

    void onCheckIdentifyCodeResult(CheckIdentifyCode.Resp resp);

    void onLogoutResult(int result);

    void onLoginExpired();

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

}
