package cn.com.startai.qxsdk.busi;

import android.app.Application;
import android.os.Build;
import android.text.TextUtils;

import cn.com.startai.qxsdk.QX;
import cn.com.startai.qxsdk.QXUserManager;
import cn.com.startai.qxsdk.busi.entity.Activate;
import cn.com.startai.qxsdk.busi.entity.BindEmail;
import cn.com.startai.qxsdk.busi.entity.BindMobile;
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
import cn.com.startai.qxsdk.connect.BaseData;
import cn.com.startai.qxsdk.connect.mqtt.MsgCw;
import cn.com.startai.qxsdk.connect.mqtt.MsgType;
import cn.com.startai.qxsdk.connect.mqtt.QXMqttData;
import cn.com.startai.qxsdk.connect.mqtt.StartaiMessage;
import cn.com.startai.qxsdk.connect.mqtt.TopicConsts;
import cn.com.startai.qxsdk.connect.mqtt.client.QXMqttConfig;
import cn.com.startai.qxsdk.connect.udp.SocketSecureKey;
import cn.com.startai.qxsdk.db.bean.UserBean;
import cn.com.startai.qxsdk.global.DeviceInfoManager;
import cn.com.startai.qxsdk.utils.QXJsonUtils;
import cn.com.startai.qxsdk.utils.QXLog;
import cn.com.startai.qxsdk.utils.QXRegexUtil;
import cn.com.startai.qxsdk.utils.QXStringUtils;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * Created by Robin on 2019/3/25.
 * 419109715@qq.com 彬影
 */
public class QXReqDataCreater {

    private static void logParamEmpty(String param) {
        QXLog.e(TAG, "send failed : ' " + param + " ' is emtpy");
    }

    public static BaseData getBindEmailData(BindEmail.Req req) {
        if (TextUtils.isEmpty(req.getEmail())) {
            logParamEmpty("email");
            return null;
        }
        if (TextUtils.isEmpty(req.getUserid())) {
            req.setUserid(QXUserManager.getInstance().getUserId());
        }
        return getBaseServerData(MsgType.TYPE_BIND_EMAIL, req);
    }


    public static BaseData getBindMobileData(BindMobile.Req req) {
        if (TextUtils.isEmpty(req.getMobile())) {
            logParamEmpty("mobile");
            return null;
        }
        if (TextUtils.isEmpty(req.getUserid())) {
            req.setUserid(QXUserManager.getInstance().getUserId());
        }
        return getBaseServerData(MsgType.TYPE_BIND_MOBILE_NUM, req);
    }

    public static BaseData getLatestAppVersionData() {
        GetLatestAppVersion.Req req = new GetLatestAppVersion.Req();
        return getBaseServerData(MsgType.TYPE_GET_LATEST_APP_VERSION, req);
    }

    public static BaseData getUpdateUserInfoData(UpdateUserInfo.Req req) {
        if (TextUtils.isEmpty(req.getUserid())) {
            req.setUserid(QXUserManager.getInstance().getUserId());
        }
        return getBaseServerData(MsgType.TYPE_UPDATE_USER_INFO, req);
    }

    public static BaseData getUpdateLoginPwd(UpdateLoginPwd.Req req) {
        if (TextUtils.isEmpty(req.getNewPwd())) {
            logParamEmpty("newPwd");
            return null;
        }
        if (TextUtils.isEmpty(req.getUserid())) {
            req.setUserid(QXUserManager.getInstance().getUserId());
        }
        return getBaseServerData(MsgType.TYPE_UPDATE_LOGIN_PWD, req);
    }

    public static BaseData getResetLoginPwdData(ResetLoginPwd.Req req) {
        if (TextUtils.isEmpty(req.getAccount()) || TextUtils.isEmpty(req.getPwd())) {
            logParamEmpty("account or pwd");
            return null;
        }
        return getBaseServerData(MsgType.TYPE_RESET_LOGIN_PWD, req);
    }

    public static BaseData getUserInfoData() {
        UserBean user = QXUserManager.getInstance().getUser();
        if (user != null) {
            GetUserInfo.Req req = new GetUserInfo.Req(user.getUserId(), (int) user.getType());
            return getBaseServerData(MsgType.TYPE_GET_USER_INFO, req);
        } else {
            return null;
        }
    }

    public static BaseData getSendEmailData(SendEmail.Req req) {
        if (TextUtils.isEmpty(req.getEmail())) {
            logParamEmpty("email");
            return null;
        }

        return getBaseServerData(MsgType.TYPE_SEND_EMAIL, req);
    }

    public static BaseData getCheckIdentifyCodeData(CheckIdentifyCode.Req req) {

        if (TextUtils.isEmpty(req.getAccount()) || TextUtils.isEmpty(req.getIdentifyCode())) {
            logParamEmpty("account or identify");
            return null;
        }
        return getBaseServerData(MsgType.TYPE_CHECK_IDENTIFY_CODE, req);
    }

    public static BaseData getIdentifyCodeData(GetIdentifyCode.Req req) {

        if (TextUtils.isEmpty(req.getMobile())) {
            logParamEmpty("mobile");
            return null;

        }

        return getBaseServerData(MsgType.TYPE_GET_IDENTIFY_CODE, req);
    }

    public static BaseData getRegisterData(Register.Req req) {
        return getBaseServerData(MsgType.TYPE_REGISTER, req);
    }

    public static BaseData getBrokerHostData(BrokerHost.Req req) {
        return getBaseServerData(MsgType.TYPE_GET_BROKER_HOST, req);
    }

    public static BaseData getUpdateDeviceInfoData(UpdateDeviceInfo.Req req) {
        return getBaseServerData(MsgType.TYPE_UPDATE_DEVICE_INFO, req);
    }

    public static BaseData getLoginData(Login.Req loginReq) {
        String uName = loginReq.getUname();
        String pwd = loginReq.getPwd();
        String identifyCode = loginReq.getIdentifyCode();
        int type = loginReq.getType();
        if (TextUtils.isEmpty(uName)) {

            logParamEmpty("uname");
            return null;
        }

        if (TextUtils.isEmpty(pwd) && TextUtils.isEmpty(identifyCode)) {

            logParamEmpty("pwd and identyfiCode");
            return null;
        }

        if (type == 0) {

            if (QXRegexUtil.isEmail(uName)) {
                type = Login.TYPE_EMAIL_PWD;
            } else if (QXRegexUtil.isMobileSimple(uName)) {

                if (TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(identifyCode)) {
                    type = Login.TYPE_MOBILE_CODE;
                } else if (!TextUtils.isEmpty(pwd) && TextUtils.isEmpty(identifyCode)) {
                    type = Login.TYPE_MOBILE_PWD;
                } else if (!TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(identifyCode)) {
                    type = Login.TYPE_MOBILE_CODE_PWD;
                } else {
                    QXLog.e(TAG, "param error, type can not catch uname ");

                    return null;
                }

            } else if (QXRegexUtil.isUsername(uName)) {
                type = 4;
            } else {
                QXLog.e(TAG, "param error, uname error");

                return null;
            }

            if (type == 1 || type == 2 || type == 4 || type == 5) {
                if (TextUtils.isEmpty(pwd)) {
                    logParamEmpty("pwd");
                    return null;
                }
            } else {
                if (TextUtils.isEmpty(identifyCode)) {
                    logParamEmpty("identifyCode");
                    return null;
                }
            }
        }

        String msgType = MsgType.TYPE_LOGIN;
        loginReq.setType(type);
        return getBaseServerData(msgType, loginReq);
    }

    public static BaseData getActivateData(Activate.Req req) {
        if (req == null) {
            req = new Activate.Req();
            req.setAppid(QXMqttConfig.getAppid());
            req.setClientid(QXMqttConfig.getClientId());
            Application app = QX.getInstance().getApp();
            req.setSn(QXMqttConfig.getSn(app));

            DeviceInfoManager instance = DeviceInfoManager.getInstance();
            req.setM_ver(QXMqttConfig.m_ver);
            Activate.Req.FirmwareParamBean firmwareParamBean = new Activate.Req.FirmwareParamBean();
            firmwareParamBean.setImei(instance.getImei(app));
            firmwareParamBean.setFirmwareVersion(instance.getFirmwareVersion());
            firmwareParamBean.setINetMac(instance.getInetMac());
            firmwareParamBean.setWifiMac(instance.getWifiMac(app));
            firmwareParamBean.setINetMac(instance.getInetMac());
            firmwareParamBean.setScreenSize(instance.getScreenSize(app));
            firmwareParamBean.setSysVersion(instance.getSysVersion());
            firmwareParamBean.setProduct(Build.PRODUCT);
            firmwareParamBean.setCpuSerial(instance.getCpuSerial());
            firmwareParamBean.setAndroidId(instance.getAndroidId(app));
            firmwareParamBean.setBluetoothMac(instance.getBluetoothMac());
            firmwareParamBean.setModelNumber(instance.getModel());
            req.setFirmwareParam(firmwareParamBean);
        } else {
            if (req.getActivateType() == 0) {
                req.setActivateType(2);
            }
        }
        return getBaseServerData(MsgType.TYPE_ACTIVATE, req);

    }

    public static BaseData getPassthroughData(String sn, byte[] dataArr) {

        return getBaseDeviceData(sn, dataArr);

    }

    public static BaseData getBaseServerData(String msgType, Object baseReq) {
        String msgCw = MsgCw.CW_0x07;
        String topic = TopicConsts.getServiceTopic() + "/" + msgCw;

        String fromid = QXMqttConfig.getSn(QX.getInstance().getApp());

        if (DistributeParam.isDistribute(msgType)) {
            fromid = QXUserManager.getInstance().getUserId();
        }
        if (TextUtils.isEmpty(fromid)) {
            fromid = QXMqttConfig.getSn(QX.getInstance().getApp());
        }
        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype(msgType)
                .setMsgcw(msgCw)
                .setAppid(QXMqttConfig.getAppid())
                .setFromid(fromid)
                .setToid(TopicConsts.CLOUD_TOID)
                .setContent(baseReq)
                .create();

        String s = QXJsonUtils.toJson(message);

        BaseData data = new QXMqttData(s.getBytes(), topic);
        return data;
    }

    public static BaseData getBaseDeviceData(String sn, byte[] aar) {


        String topic = TopicConsts.getClientTopic(sn) + "/" + MsgCw.CW_0x01;

        String fromid = QXMqttConfig.getSn(QX.getInstance().getApp());

        if (DistributeParam.isDistribute(MsgType.TYPE_PASSTHROUGH)) {
            fromid = QXUserManager.getInstance().getUserId();
        }

        StartaiMessage message = new StartaiMessage.Builder()
                .setMsgtype(MsgType.TYPE_PASSTHROUGH)
                .setMsgcw(MsgCw.CW_0x01)
                .setAppid(QXMqttConfig.getAppid())
                .setFromid(fromid)
                .setToid(sn)
                .setContent(QXStringUtils.byteArr2HexStr(aar))
                .create();
        String s = QXJsonUtils.toJson(message);
        BaseData data = new QXMqttData(s.getBytes(), topic);
        return data;
    }
}
