package cn.com.startai.qxsdk.channel.mqtt.entity;

import cn.com.startai.qxsdk.channel.mqtt.BaseMessage;
import cn.com.startai.qxsdk.channel.mqtt.task.OnMqttTaskCallBack;
import cn.com.startai.qxsdk.global.QXError;
import cn.com.startai.qxsdk.utils.QXJsonUtils;
import cn.com.startai.qxsdk.utils.QXLog;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * 更新设备信息
 * Created by Robin on 2019/3/29.
 * 419109715@qq.com 彬影
 */
public class UpdateDeviceInfo {


    public static class Req {
        private String sn;
        private StatusParam statusParam;
        private BusinessParam businessParam;

        public Req(String sn, StatusParam statusParam) {
            this.sn = sn;
            this.statusParam = statusParam;
        }

        @Override
        public String toString() {
            return "Req{" +
                    "sn='" + sn + '\'' +
                    ", statusParam=" + statusParam +
                    ", businessParam=" + businessParam +
                    '}';
        }

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public StatusParam getStatusParam() {
            return statusParam;
        }

        public void setStatusParam(StatusParam statusParam) {
            this.statusParam = statusParam;
        }

        public BusinessParam getBusinessParam() {
            return businessParam;
        }

        public void setBusinessParam(BusinessParam businessParam) {
            this.businessParam = businessParam;
        }

        public static class StatusParam {
            private String ip;

            public StatusParam(String ip) {
                this.ip = ip;
            }

            @Override
            public String toString() {
                return "StatusParam{" +
                        "ip='" + ip + '\'' +
                        '}';
            }

            public String getIp() {
                return ip;
            }

            public void setIp(String ip) {
                this.ip = ip;
            }
        }

        public static class BusinessParam {

        }


    }


    /**
     * 返回
     */
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

            private String sn;
            private StatusParam statusParam;
            private BusinessParam businessParam;


            private Req errcontent;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "sn='" + sn + '\'' +
                        ", statusParam=" + statusParam +
                        ", businessParam=" + businessParam +
                        ", errcontent=" + errcontent +
                        ", errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        '}';
            }

            public Req getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req errcontent) {
                this.errcontent = errcontent;
            }

            private class StatusParam {
            }

            private class BusinessParam {
                private String ip;

                @Override
                public String toString() {
                    return "BusinessParam{" +
                            "ip='" + ip + '\'' +
                            '}';
                }

                public String getIp() {
                    return ip;
                }

                public void setIp(String ip) {
                    this.ip = ip;
                }
            }

            public String getSn() {
                return sn;
            }

            public void setSn(String sn) {
                this.sn = sn;
            }

            public StatusParam getStatusParam() {
                return statusParam;
            }

            public void setStatusParam(StatusParam statusParam) {
                this.statusParam = statusParam;
            }

            public BusinessParam getBusinessParam() {
                return businessParam;
            }

            public void setBusinessParam(BusinessParam businessParam) {
                this.businessParam = businessParam;
            }
        }
    }


    /**
     * 更新设备信息 返回
     *
     * @param miof
     */
    public static void handlerMsg(String miof, OnMqttTaskCallBack callBack) {

        Resp resp = QXJsonUtils.fromJson(miof, Resp.class);
        if (resp == null) {
            QXLog.e(TAG, "返回数据格式错误");
            return;
        }
        if (resp.getResult() == 1) {

            QXLog.e(TAG, "更新设备信息 成功");
        } else {

            QXLog.e(TAG, "更新设备信息 失败 " + resp.getContent().getErrmsg());
        }

        callBack.onUpdateDeviceInfoResult(resp);
    }

}
