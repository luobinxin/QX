package cn.com.startai.qxsdk.channel.mqtt.busi;


import org.json.JSONException;
import org.json.JSONObject;

import cn.com.startai.qxsdk.channel.mqtt.entity.Activate;
import cn.com.startai.qxsdk.channel.mqtt.entity.Bind;
import cn.com.startai.qxsdk.channel.mqtt.entity.BindEmail;
import cn.com.startai.qxsdk.channel.mqtt.entity.BindMobile;
import cn.com.startai.qxsdk.channel.mqtt.entity.BindThirdAccount;
import cn.com.startai.qxsdk.channel.mqtt.entity.BrokerHost;
import cn.com.startai.qxsdk.channel.mqtt.entity.CheckIdentifyCode;
import cn.com.startai.qxsdk.channel.mqtt.entity.DeviceOffline;
import cn.com.startai.qxsdk.channel.mqtt.entity.DeviceOnline;
import cn.com.startai.qxsdk.channel.mqtt.entity.GetAlipayAuthInfo;
import cn.com.startai.qxsdk.channel.mqtt.entity.GetBindList;
import cn.com.startai.qxsdk.channel.mqtt.entity.GetIdentifyCode;
import cn.com.startai.qxsdk.channel.mqtt.entity.GetLatestAppVersion;
import cn.com.startai.qxsdk.channel.mqtt.entity.GetRealPayResult;
import cn.com.startai.qxsdk.channel.mqtt.entity.GetUserInfo;
import cn.com.startai.qxsdk.channel.mqtt.entity.GetWeatherInfo;
import cn.com.startai.qxsdk.channel.mqtt.entity.Login;
import cn.com.startai.qxsdk.channel.mqtt.entity.LoginWithThirdAccount;
import cn.com.startai.qxsdk.channel.mqtt.entity.Passthrough;
import cn.com.startai.qxsdk.channel.mqtt.entity.Register;
import cn.com.startai.qxsdk.channel.mqtt.entity.ResetLoginPwd;
import cn.com.startai.qxsdk.channel.mqtt.entity.SendEmail;
import cn.com.startai.qxsdk.channel.mqtt.entity.ThirdPaymentUnifiedOrder;
import cn.com.startai.qxsdk.channel.mqtt.entity.UnActivate;
import cn.com.startai.qxsdk.channel.mqtt.entity.UnBind;
import cn.com.startai.qxsdk.channel.mqtt.entity.UnBindThirdAccount;
import cn.com.startai.qxsdk.channel.mqtt.entity.UpdateDeviceInfo;
import cn.com.startai.qxsdk.channel.mqtt.entity.UpdateLoginPwd;
import cn.com.startai.qxsdk.channel.mqtt.entity.UpdateRemark;
import cn.com.startai.qxsdk.channel.mqtt.entity.UpdateUserInfo;
import cn.com.startai.qxsdk.channel.mqtt.task.OnMqttTaskCallBack;
import cn.com.startai.qxsdk.channel.mqtt.MsgType;
import cn.com.startai.qxsdk.utils.QXLog;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * Created by Robin on 2018/6/14.
 * qq: 419109715 彬影
 */

public class BaseMiofBusiHandler {

    protected OnMqttTaskCallBack callback;

    public BaseMiofBusiHandler(OnMqttTaskCallBack callback) {
        this.callback = callback;
    }

    public void handMessage(String topic, String msg) {

        String msgtype = "";


        try {

            JSONObject jsonObject = new JSONObject(msg);
            if (msg.contains("\"" + MiofTag.TAG_MSGTYPE + "\"")) {
                msgtype = jsonObject.getString(MiofTag.TAG_MSGTYPE);
            }
        } catch (JSONException e) {
            QXLog.e(TAG, "JSON format error");
            e.printStackTrace();
            return;
        }


        switch (msgtype) {
            case MsgType.TYPE_DEVICE_ONLINE://设备 上线

                DeviceOnline.handlerMsg(msg, callback);
                break;

            case MsgType.TYPE_DEVICE_OFFLINE: //终端下线

                DeviceOffline.handlerMsg(msg, callback);
                break;

            case MsgType.TYPE_GET_BROKER_HOST://获取区域节点信息
                BrokerHost.handlerMsg(msg, callback);
                return;
            case MsgType.TYPE_ACTIVATE://设备激活
                Activate.handlerMsg(msg, callback);
                break;

            case MsgType.TYPE_BIND://添加好友
                Bind.handlerMsg(msg, callback);
                break;

            case MsgType.TYPE_UNACTIVATE://注销激活
                UnActivate.handlerMsg(msg, callback);
                break;

            case MsgType.TYPE_UNBIND://删除好友
                UnBind.handlerMsg(msg, callback);
                break;

            case MsgType.TYPE_GET_BIND_LIST_BY_PAGE://查询好友关系
                GetBindList.handlerMsg(msg, callback);
                break;

            case MsgType.TYPE_UPDATE_REMARK://修改备注名
                UpdateRemark.handlerMsg(msg, callback);
                break;

            case MsgType.TYPE_GET_LATEST_APP_VERSION://查询最新版本
                GetLatestAppVersion.handlerMsg(msg, callback);
                break;

            case MsgType.TYPE_REGISTER://用户注册
                Register.handlerMsg(msg, callback);
                break;

            case MsgType.TYPE_LOGIN://登录
                Login.handlerMsg(msg, callback);
                break;

            case MsgType.TYPE_UPDATE_DEVICE_INFO://更新设备信息
                UpdateDeviceInfo.handlerMsg(msg, callback);
                break;

            case MsgType.TYPE_UPDATE_USER_INFO://更新用户信息
                UpdateUserInfo.handlerMsg(msg, callback);
                break;

            case MsgType.TYPE_GET_IDENTIFY_CODE://获取验证码
                GetIdentifyCode.handlerMsg(msg, callback);
                break;
            case MsgType.TYPE_CHECK_IDENTIFY_CODE://检验验证码
                CheckIdentifyCode.handlerMsg(msg, callback);
                break;

            case MsgType.TYPE_SEND_EMAIL://请求发送邮件
                SendEmail.handlerMsg(msg, callback);
                break;

            case MsgType.TYPE_GET_USER_INFO://查询用户信息
                GetUserInfo.handlerMsg(msg, callback);
                break;

            case MsgType.TYPE_UPDATE_LOGIN_PWD://修改密码
                UpdateLoginPwd.handlerMsg(msg, callback);
                break;

            case MsgType.TYPE_RESET_LOGIN_PWD://重置登录密码

                ResetLoginPwd.handlerMsg(msg, callback);
                break;
            case MsgType.TYPE_LOGIN_WITH_THIRD_ACCOUNT://第三方登录

                LoginWithThirdAccount.handlerMsg(msg, callback);
                break;

            case MsgType.TYPE_THIRD_PAYMENT_UNIFIED_ORDER: //第三方支付 统一下单
                ThirdPaymentUnifiedOrder.handlerMsg(msg, callback);
                break;
            case MsgType.TYPE_PASSTHROUGH://消息透传
                Passthrough.handlerMsg(topic, msg, callback);
                break;

            case MsgType.TYPE_GET_REAL_ORDER_PAY_STATUS: //查询订单支付状态
                GetRealPayResult.handlerMsg(msg, callback);
                break;


            case MsgType.TYPE_GET_ALIPAY_AUTH_INFO: //查询 支付宝密钥
                GetAlipayAuthInfo.handlerMsg(msg, callback);

                break;

            case MsgType.TYPE_BIND_MOBILE_NUM: //绑定手机号
                BindMobile.handlerMsg(msg, callback);

                break;
            case MsgType.TYPE_GET_WEATHER_INFO://查询天气
                GetWeatherInfo.handlerMsg(msg, callback);
                break;
            case MsgType.TYPE_UNBIND_THIRD_ACCOUNT: //解绑 第三方账号
                UnBindThirdAccount.handlerMsg(msg, callback);

                break;

            case MsgType.TYPE_BINDTHIRDACCOUNT: //绑定 第三方账号
                BindThirdAccount.handlerMsg(msg, callback);

                break;
            case MsgType.TYPE_BIND_EMAIL: //绑定邮箱
                BindEmail.handlerMsg(msg, callback);

                break;

            default:
                //sdk未处理的数据 将直接回调到应用层
                callback.onMessageArrived(topic, msg.getBytes());
        }


    }

}
