package cn.com.startai.qxsdk.busi;

import android.app.Application;
import android.os.Build;

import cn.com.startai.qxsdk.QX;
import cn.com.startai.qxsdk.QXUserManager;
import cn.com.startai.qxsdk.busi.entity.Activate;
import cn.com.startai.qxsdk.busi.entity.BrokerHost;
import cn.com.startai.qxsdk.busi.entity.Login;
import cn.com.startai.qxsdk.busi.entity.UpdateDeviceInfo;
import cn.com.startai.qxsdk.connect.BaseData;
import cn.com.startai.qxsdk.connect.mqtt.MsgCw;
import cn.com.startai.qxsdk.connect.mqtt.MsgType;
import cn.com.startai.qxsdk.connect.mqtt.QXMqttData;
import cn.com.startai.qxsdk.connect.mqtt.StartaiMessage;
import cn.com.startai.qxsdk.connect.mqtt.TopicConsts;
import cn.com.startai.qxsdk.connect.mqtt.client.QXMqttConfig;
import cn.com.startai.qxsdk.global.DeviceInfoManager;
import cn.com.startai.qxsdk.utils.QXJsonUtils;
import cn.com.startai.qxsdk.utils.QXStringUtils;

/**
 * Created by Robin on 2019/3/25.
 * 419109715@qq.com 彬影
 */
public class QXReqDataCreater {



    public static BaseData getBrokerHostData(BrokerHost.Req req) {
        return getBaseServerData(MsgType.TYPE_GET_BROKER_HOST, req);
    }

    public static BaseData getUpdateDeviceInfoData(UpdateDeviceInfo.Req req) {
        return getBaseServerData(MsgType.TYPE_UPDATE_DEVICE_INFO, req);
    }

    public static BaseData getLoginData(Login.Req loginReq) {
        String msgType = MsgType.TYPE_LOGIN;

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
