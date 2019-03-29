package cn.com.startai.qxsdk.busi;


import org.json.JSONException;
import org.json.JSONObject;

import cn.com.startai.qxsdk.busi.entity.Activate;
import cn.com.startai.qxsdk.busi.entity.BrokerHost;
import cn.com.startai.qxsdk.busi.entity.Login;
import cn.com.startai.qxsdk.busi.entity.UpdateDeviceInfo;
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
//            case C_0x8002.MSGTYPE://添加好友
//                C_0x8002.m_resp(msg);
//                break;
//
//            case C_0x8003.MSGTYPE://注销激活
//                C_0x8003.m_resp(msg);
//                break;
//
//            case C_0x8004.MSGTYPE://删除好友
//                C_0x8004.m_resp(msg);
//                break;
//
//            case C_0x8005.MSGTYPE://查询好友关系
//                C_0x8005.m_resp(msg);
//                break;
//
//            case C_0x8015.MSGTYPE://修改备注名
//                C_0x8015.m_resp(msg);
//                break;
//
//            case C_0x8016.MSGTYPE://查询最新版本
//                C_0x8016.m_resp(msg);
//                break;
//
//            case C_0x8017.MSGTYPE://用户注册
//                C_0x8017.m_resp(msg);
//                break;

            case MsgType.TYPE_LOGIN://登录
                Login.handlerMsg(msg, callback);
                break;

            case MsgType.TYPE_UPDATE_DEVICE_INFO://更新设备信息
                UpdateDeviceInfo.handlerMsg(msg, callback);
                break;
//
//            case C_0x8020.MSGTYPE://更新用户信息
//                C_0x8020.m_resp(msg);
//                break;
//
//            case C_0x8021.MSGTYPE://获取验证码
//                C_0x8021.m_resp(msg);
//                break;
//
//            case C_0x8022.MSGTYPE://检验验证码
//                C_0x8022.m_resp(msg);
//                break;
//
//            case C_0x8023.MSGTYPE://请求发送邮件
//                C_0x8023.m_resp(msg);
//                break;
//
//            case C_0x8024.MSGTYPE://查询用户信息
//                C_0x8024.m_resp(msg);
//                break;
//
//            case C_0x8025.MSGTYPE://修改密码
//                C_0x8025.m_resp(msg);
//                break;
//
//            case C_0x8026.MSGTYPE://手机重置密码
//
//                C_0x8026.m_resp(msg);
//                break;
//            case C_0x8027.MSGTYPE://第三方登录
//
//                C_0x8027.m_resp(msg);
//                break;
//
//            case C_0x8028.MSGTYPE: //第三方支付 统一下单
//                C_0x8028.m_resp(msg);
//                break;
//            case C_0x8200.MSGTYPE://消息透传
//                C_0x8200.m_resp(topic, msg);
//                break;
//
//            case C_0x8031.MSGTYPE: //查询订单支付状态
//                C_0x8031.m_resp(msg);
//                break;
//
//
//            case C_0x8033.MSGTYPE: //查询 支付宝密钥
//                C_0x8033.m_resp(msg);
//
//                break;
//
//            case C_0x8034.MSGTYPE: //查询 支付宝密钥
//                C_0x8034.m_resp(msg);
//
//                break;
//            case C_0x8035.MSGTYPE://查询天气
//                C_0x8035.m_resp(msg);
//                break;
//            case C_0x8036.MSGTYPE: //解绑 第三方账号
//                C_0x8036.m_resp(msg);
//
//                break;
//
//            case C_0x8037.MSGTYPE: //绑定 第三方账号
//                C_0x8037.m_resp(msg);
//
//                break;
//            case C_0x8038.MSGTYPE: //分页获取好友列表
//                C_0x8038.m_resp(msg);
//
//                break;
//            case C_0x8039.MSGTYPE: //绑定邮箱
//                C_0x8039.m_resp(msg);
//
//            break;

            default:
                //sdk未处理的数据 将直接回调到应用层
                callback.onMessageArrived(topic, msg);
        }


    }

}
