package cn.com.startai.qxsdk.channel.mqtt.entity;

import java.io.Serializable;

import cn.com.startai.qxsdk.channel.mqtt.BaseMessage;
import cn.com.startai.qxsdk.channel.mqtt.task.OnMqttTaskCallBack;
import cn.com.startai.qxsdk.global.QXError;
import cn.com.startai.qxsdk.utils.QXJsonUtils;
import cn.com.startai.qxsdk.utils.QXLog;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * 查询支付宝认证信息
 * Created by Robin on 2018/8/22.
 * qq: 419109715 彬影
 */

public class GetAlipayAuthInfo implements Serializable {

    public static String MSG_DESC = "查询支付宝密钥 ";

    public static final String AUTH_TYPE_AUTH = "AUTH";
    public static final String AUTH_TYPE_LOGIN = "LOGIN";


    public static class Req {
        private String authTargetId;

        public Req(String authTargetId) {
            this.authTargetId = authTargetId;
        }

        @Override
        public String toString() {
            return "ContentBean{" +
                    "authTargetId='" + authTargetId + '\'' +
                    '}';
        }

        public String getAuthTargetId() {
            return authTargetId;
        }

        public void setAuthTargetId(String authTargetId) {
            this.authTargetId = authTargetId;
        }


    }


    public static class Resp extends BaseMessage implements Serializable {

        private ContentBean content;

        @Override
        public String toString() {
            return "Resp{" +
                    "content=" + content +
                    ", msgcw='" + msgcw + '\'' +
                    ", msgtype='" + msgtype + '\'' +
                    ", fromid='" + fromid + '\'' +
                    ", toid='" + toid + '\'' +
                    ", domain='" + domain + '\'' +
                    ", appid='" + appid + '\'' +
                    ", ts=" + ts +
                    ", msgid='" + msgid + '\'' +
                    ", m_ver='" + m_ver + '\'' +
                    ", result=" + result +
                    '}';
        }

        public ContentBean getContent() {
            return content;
        }

        public void setContent(ContentBean content) {
            this.content = content;
        }

        public static class ContentBean extends QXError {


            private Req errcontent = null;

            private String aliPayAuthInfo;
            private String authTargetId;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", errcontent=" + errcontent +
                        ", aliPayAuthInfo='" + aliPayAuthInfo + '\'' +
                        ", authTargetId='" + authTargetId + '\'' +
                        '}';
            }

            public String getAuthTargetId() {
                return authTargetId;
            }

            public void setAuthTargetId(String authTargetId) {
                this.authTargetId = authTargetId;
            }

            public String getAliPayAuthInfo() {

                return aliPayAuthInfo;
            }

            public void setAliPayAuthInfo(String aliPayAuthInfo) {
                this.aliPayAuthInfo = aliPayAuthInfo;
            }

            public Req getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req errcontent) {
                this.errcontent = errcontent;
            }

        }

    }

    /**
     * 请求 查询支付宝密钥 返回结果
     *
     * @param miof
     */
    public static void handlerMsg(String miof, OnMqttTaskCallBack callBack) {


        Resp resp = QXJsonUtils.fromJson(miof, Resp.class);
        if (resp == null) {
            QXLog.e(TAG, MSG_DESC + " 返回格式错误");
            return;
        }
        if (resp.getResult() == 1) {
            QXLog.e(TAG, MSG_DESC + " 成功");

        } else {
            GetAlipayAuthInfo.Resp.ContentBean content = resp.getContent();
            GetAlipayAuthInfo.Req errcontent = content.getErrcontent();
            content.setAuthTargetId(errcontent.getAuthTargetId());
            QXLog.e(TAG, MSG_DESC + " 失败 " + resp.getContent().getErrmsg());
        }
        callBack.onGetAlipayAuthInfoResult(resp);
    }

}
