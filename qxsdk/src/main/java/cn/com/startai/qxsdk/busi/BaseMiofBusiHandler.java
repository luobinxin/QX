package cn.com.startai.qxsdk.busi;


import org.json.JSONException;
import org.json.JSONObject;

import cn.com.startai.qxsdk.busi.entity.Activate;
import cn.com.startai.qxsdk.busi.entity.BrokerHost;
import cn.com.startai.qxsdk.busi.entity.CheckIdentifyCode;
import cn.com.startai.qxsdk.busi.entity.GetIdentifyCode;
import cn.com.startai.qxsdk.busi.entity.GetLatestAppVersion;
import cn.com.startai.qxsdk.busi.entity.GetUserInfo;
import cn.com.startai.qxsdk.busi.entity.Login;
import cn.com.startai.qxsdk.busi.entity.Register;
import cn.com.startai.qxsdk.busi.entity.ResetLoginPwd;
import cn.com.startai.qxsdk.busi.entity.SendEmail;
import cn.com.startai.qxsdk.busi.entity.UpdateDeviceInfo;
import cn.com.startai.qxsdk.busi.entity.UpdateLoginPwd;
import cn.com.startai.qxsdk.busi.entity.UpdateUserInfo;
import cn.com.startai.qxsdk.connect.mqtt.task.OnMqttTaskCallBack;
import cn.com.startai.qxsdk.connect.mqtt.task.system.LoginTask;
import cn.com.startai.qxsdk.connect.mqtt.MsgType;
import cn.com.startai.qxsdk.utils.QXLog;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * Created by Robin on 2018/6/14.
 * qq: 419109715 彬影
 */

public class BaseMiofBusiHandler {

    private OnMqttTaskCallBack callback;

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
            e.printStackTrace();
            QXLog.e(TAG, "JSON format is not correct");
            return;
        }


        switch (msgtype) {
//            case MsgType.TYPE_DEVICEONLINE://终端上线
//
//                C_0x9998.m_resp(msg);
//                break;
//
//            case C_0x9999.MSGTYPE://终端下线
//
//                C_0x9999.m_resp(msg);
//                break;
//
            case MsgType.TYPE_GET_BROKER_HOST://获取区域节点信息
                BrokerHost.handerMsg(msg, callback);
                return;
            case MsgType.TYPE_ACTIVATE://设备激活
                Activate.handlerMsg(msg, callback);
                break;
//
//            case Bind.MSGTYPE://添加好友
//                Bind.m_resp(msg);
//                break;
//
//            case UnActivate.MSGTYPE://注销激活
//                UnActivate.m_resp(msg);
//                break;
//
//            case UnBind.MSGTYPE://删除好友
//                UnBind.m_resp(msg);
//                break;
//
//            case GetBindList.MSGTYPE://查询好友关系
//                GetBindList.m_resp(msg);
//                break;
//
//            case C_0x8015.MSGTYPE://修改备注名
//                C_0x8015.m_resp(msg);
//                break;

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
//                break;
//            case LoginWithThirdAccount.MSGTYPE://第三方登录
//
//                LoginWithThirdAccount.m_resp(msg);
//                break;
//
//            case ThirdPaymentUnifiedOrder.MSGTYPE: //第三方支付 统一下单
//                ThirdPaymentUnifiedOrder.m_resp(msg);
//                break;
//            case C_0x8200.MSGTYPE://消息透传
//                C_0x8200.m_resp(topic, msg);
//                break;
//
//            case GetRealPayResult.MSGTYPE: //查询订单支付状态
//                GetRealPayResult.m_resp(msg);
//                break;
//
//
//            case GetAlipayAuthInfo.MSGTYPE: //查询 支付宝密钥
//                GetAlipayAuthInfo.m_resp(msg);
//
//                break;
//
//            case BindMobile.MSGTYPE: //查询 支付宝密钥
//                BindMobile.m_resp(msg);
//
//                break;
//            case GetWeatherInfo.MSGTYPE://查询天气
//                GetWeatherInfo.m_resp(msg);
//                break;
//            case UnBindThirdAccount.MSGTYPE: //解绑 第三方账号
//                UnBindThirdAccount.m_resp(msg);
//
//                break;
//
//            case BindThirdAccount.MSGTYPE: //绑定 第三方账号
//                BindThirdAccount.m_resp(msg);
//
//                break;
//            case GetBindList.MSGTYPE: //分页获取好友列表
//                GetBindList.m_resp(msg);
//
//                break;
//            case BindEmail.MSGTYPE: //绑定邮箱
//                BindEmail.m_resp(msg);
//
//            break;

            default:
                //sdk未处理的数据 将直接回调到应用层
                callback.onMessageArrived(topic, msg);
        }


    }

}
