package cn.com.startai.qxsdk.busi.entity;


import cn.com.startai.qxsdk.QX;
import cn.com.startai.qxsdk.connect.mqtt.BaseMessage;
import cn.com.startai.qxsdk.connect.mqtt.client.QXMqttConfig;
import cn.com.startai.qxsdk.connect.mqtt.task.OnMqttTaskCallBack;
import cn.com.startai.qxsdk.global.QXError;
import cn.com.startai.qxsdk.global.QXSpController;
import cn.com.startai.qxsdk.utils.QXJsonUtils;
import cn.com.startai.qxsdk.utils.QXLog;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * Created by Robin on 2019/3/26.
 * 419109715@qq.com 彬影
 */
public class Activate {

    public static final int ACTIVATETYPE_SELF_1 = 1;
    public static final int ACTIVATETYPE_SELF_0 = 0;
    public static final int ACTIVATETYPE_DEVICE = 2;

    public static class Req {

        private String apptype;
        private String domain;
        private String sn;
        private String appid;
        private String clientid;
        private int activateType;
        private String m_ver;

        private FirmwareParamBean firmwareParam;


        public int getActivateType() {
            return activateType;
        }

        public void setActivateType(int activateType) {
            this.activateType = activateType;
        }


        public String getApptype() {
            return apptype;
        }

        public void setApptype(String apptype) {
            this.apptype = apptype;
        }

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getClientid() {
            return clientid;
        }

        public void setClientid(String clientid) {
            this.clientid = clientid;
        }

        public String getM_ver() {
            return m_ver;
        }

        public void setM_ver(String m_ver) {
            this.m_ver = m_ver;
        }


        public FirmwareParamBean getFirmwareParam() {
            return firmwareParam;
        }

        public void setFirmwareParam(FirmwareParamBean firmwareParam) {
            this.firmwareParam = firmwareParam;
        }

        public static class FirmwareParamBean {


            private String sysVersion;
            private String product;
            private String firmwareVersion;
            private String iNetMac;
            private String bluetoothMac;
            private String imei;
            private String wifiMac;
            private String screenSize;
            private String cpuSerial;
            private String androidId;
            private String modelNumber;

            @Override
            public String toString() {
                return "FirmwareParamBean{" +
                        "sysVersion='" + sysVersion + '\'' +
                        ", product='" + product + '\'' +
                        ", firmwareVersion='" + firmwareVersion + '\'' +
                        ", iNetMac='" + iNetMac + '\'' +
                        ", bluetoothMac='" + bluetoothMac + '\'' +
                        ", imei='" + imei + '\'' +
                        ", wifiMac='" + wifiMac + '\'' +
                        ", screenSize='" + screenSize + '\'' +
                        ", cpuSerial='" + cpuSerial + '\'' +
                        ", androidId='" + androidId + '\'' +
                        ", modelNumber='" + modelNumber + '\'' +
                        '}';
            }

            public String getModelNumber() {
                return modelNumber;
            }

            public void setModelNumber(String modelNumber) {
                this.modelNumber = modelNumber;
            }

            public String getBluetoothMac() {
                return bluetoothMac;
            }

            public void setBluetoothMac(String bluetoothMac) {
                this.bluetoothMac = bluetoothMac;
            }


            public String getAndroidId() {
                return androidId;
            }

            public void setAndroidId(String androidId) {
                this.androidId = androidId;
            }

            public String getCpuSerial() {
                return cpuSerial;
            }

            public void setCpuSerial(String cpuSerial) {
                this.cpuSerial = cpuSerial;
            }

            public String getSysVersion() {
                return sysVersion;
            }

            public void setSysVersion(String sysVersion) {
                this.sysVersion = sysVersion;
            }

            public String getProduct() {
                return product;
            }

            public void setProduct(String product) {
                this.product = product;
            }

            public String getFirmwareVersion() {
                return firmwareVersion;
            }

            public void setFirmwareVersion(String firmwareVersion) {
                this.firmwareVersion = firmwareVersion;
            }

            public String getINetMac() {
                return iNetMac;
            }

            public void setINetMac(String iNetMac) {
                this.iNetMac = iNetMac;
            }

            public String getImei() {
                return imei;
            }

            public void setImei(String imei) {
                this.imei = imei;
            }

            public String getWifiMac() {
                return wifiMac;
            }

            public void setWifiMac(String wifiMac) {
                this.wifiMac = wifiMac;
            }

            public String getScreenSize() {
                return screenSize;
            }

            public void setScreenSize(String screenSize) {
                this.screenSize = screenSize;
            }
        }

    }

    public static class Resp extends BaseMessage {


        private ContentBean content;

        @Override
        public String toString() {
            return "Resp{" +
                    "msgcw='" + msgcw + '\'' +
                    ", msgtype='" + msgtype + '\'' +
                    ", fromid='" + fromid + '\'' +
                    ", toid='" + toid + '\'' +
                    ", domain='" + domain + '\'' +
                    ", appid='" + appid + '\'' +
                    ", ts=" + ts +
                    ", msgid='" + msgid + '\'' +
                    ", m_ver='" + m_ver + '\'' +
                    ", result=" + result +
                    ", content=" + content +
                    '}';
        }

        public ContentBean getContent() {
            return content;
        }

        public void setContent(ContentBean content) {
            this.content = content;
        }

        public static class ContentBean extends QXError {

            /*
                      终端请求云端注册业务，clientid项为GUID，clientid,sn,apptype, m_ver,appid,domian不能为空
          apptype:设备类型
          广告屏为smartAd,智能音响为smartBox,智能插座 smartOl,网关为smartGw,智能手机为smartCrtl等
          firmwareParam:
          为固件参数
          activateType:激活类型 如果APP代智能设备激活 值为2 ,自己激活为1 或者不填写该字段
                       */
            private String apptype;
            private String domain;
            private String sn;
            private String appid;
            private String clientid;
            private String m_ver;
            private int activateType;
            private Req errcontent;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", apptype='" + apptype + '\'' +
                        ", domain='" + domain + '\'' +
                        ", sn='" + sn + '\'' +
                        ", appid='" + appid + '\'' +
                        ", clientid='" + clientid + '\'' +
                        ", m_ver='" + m_ver + '\'' +
                        ", activateType=" + activateType +
                        ", errcontent=" + errcontent +
                        ", firmwareParam=" + firmwareParam +
                        '}';
            }

            public Req getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req errcontent) {
                this.errcontent = errcontent;
            }

            public int getActivateType() {
                return activateType;
            }

            public void setActivateType(int activateType) {
                this.activateType = activateType;
            }

            private FirmwareParamBean firmwareParam;

            public String getApptype() {
                return apptype;
            }

            public void setApptype(String apptype) {
                this.apptype = apptype;
            }

            public String getDomain() {
                return domain;
            }

            public void setDomain(String domain) {
                this.domain = domain;
            }

            public String getSn() {
                return sn;
            }

            public void setSn(String sn) {
                this.sn = sn;
            }

            public String getAppid() {
                return appid;
            }

            public void setAppid(String appid) {
                this.appid = appid;
            }

            public String getClientid() {
                return clientid;
            }

            public void setClientid(String clientid) {
                this.clientid = clientid;
            }

            public String getM_ver() {
                return m_ver;
            }

            public void setM_ver(String m_ver) {
                this.m_ver = m_ver;
            }

            public FirmwareParamBean getFirmwareParam() {
                return firmwareParam;
            }

            public void setFirmwareParam(FirmwareParamBean firmwareParam) {
                this.firmwareParam = firmwareParam;
            }

            public static class FirmwareParamBean {
                /**
                 * sysVersion : 4.4.2
                 * product : astar_sc3812r
                 * firmwareVersion : astar_sc3812r-eng 4.4.2
                 * iNetMac : 以太网mac
                 * imei : wfewfwfwfwef
                 * wifiMac : cc:b8:a8:1d:4e:da
                 * screenSize : 1280*720
                 */

                private String sysVersion;
                private String product;
                private String firmwareVersion;
                private String iNetMac;
                private String bluetoothMac;
                private String imei;
                private String wifiMac;
                private String screenSize;
                private String cpuSerial;
                private String androidId;
                private String modelNumber;

                @Override
                public String toString() {
                    return "FirmwareParamBean{" +
                            "sysVersion='" + sysVersion + '\'' +
                            ", product='" + product + '\'' +
                            ", firmwareVersion='" + firmwareVersion + '\'' +
                            ", iNetMac='" + iNetMac + '\'' +
                            ", bluetoothMac='" + bluetoothMac + '\'' +
                            ", imei='" + imei + '\'' +
                            ", wifiMac='" + wifiMac + '\'' +
                            ", screenSize='" + screenSize + '\'' +
                            ", cpuSerial='" + cpuSerial + '\'' +
                            ", androidId='" + androidId + '\'' +
                            ", modelNumber='" + modelNumber + '\'' +
                            '}';
                }

                public String getModelNumber() {
                    return modelNumber;
                }

                public void setModelNumber(String modelNumber) {
                    this.modelNumber = modelNumber;
                }

                public String getBluetoothMac() {
                    return bluetoothMac;
                }

                public void setBluetoothMac(String bluetoothMac) {
                    this.bluetoothMac = bluetoothMac;
                }

                public String getiNetMac() {
                    return iNetMac;
                }

                public void setiNetMac(String iNetMac) {
                    this.iNetMac = iNetMac;
                }

                public String getAndroidId() {
                    return androidId;
                }

                public void setAndroidId(String androidId) {
                    this.androidId = androidId;
                }

                public String getCpuSerial() {
                    return cpuSerial;
                }

                public void setCpuSerial(String cpuSerial) {
                    this.cpuSerial = cpuSerial;
                }

                public String getSysVersion() {
                    return sysVersion;
                }

                public void setSysVersion(String sysVersion) {
                    this.sysVersion = sysVersion;
                }

                public String getProduct() {
                    return product;
                }

                public void setProduct(String product) {
                    this.product = product;
                }

                public String getFirmwareVersion() {
                    return firmwareVersion;
                }

                public void setFirmwareVersion(String firmwareVersion) {
                    this.firmwareVersion = firmwareVersion;
                }

                public String getINetMac() {
                    return iNetMac;
                }

                public void setINetMac(String iNetMac) {
                    this.iNetMac = iNetMac;
                }

                public String getImei() {
                    return imei;
                }

                public void setImei(String imei) {
                    this.imei = imei;
                }

                public String getWifiMac() {
                    return wifiMac;
                }

                public void setWifiMac(String wifiMac) {
                    this.wifiMac = wifiMac;
                }

                public String getScreenSize() {
                    return screenSize;
                }

                public void setScreenSize(String screenSize) {
                    this.screenSize = screenSize;
                }
            }
        }

    }


    public static void handlerMsg(String miof, OnMqttTaskCallBack callBack) {
        Resp resp = QXJsonUtils.fromJson(miof, Resp.class);
        if (resp == null) {
            QXLog.e(TAG, "返回数据格式错误");
            return;
        }

        if (resp.getResult() == 1) {

            //自己激活成功
            if (QXMqttConfig.getSn(QX.getInstance().getApp()).equals(resp.content.getSn())) {
                QXLog.e(TAG, "激活成功");
                callBack.onActivateResult(resp);
            } else {
                //代发的激活包 激活成功
                QXLog.e(TAG, "代激活成功");
                callBack.onHardwareActivateResult(resp);
            }
        } else {

            String errcode = resp.getContent().getErrcode();
            //自己激活失败
            if ("0x800101".equals(errcode)
                    || "0x800102".equals(errcode)
                    || "0x800103".equals(errcode)
                    || "0x800104".equals(errcode)
                    || "000000".equals(errcode)
                    || "0x800105".equals(errcode)
                    ) {
                QXLog.e(TAG, "激活失败");
                callBack.onActivateResult(resp);
            } else {
                QXLog.e(TAG, "代激活失败 " + resp.getContent().getErrmsg());
                callBack.onHardwareActivateResult(resp);
            }

        }
    }

}
