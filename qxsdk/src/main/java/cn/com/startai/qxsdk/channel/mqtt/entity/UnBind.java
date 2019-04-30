package cn.com.startai.qxsdk.channel.mqtt.entity;

import cn.com.startai.qxsdk.channel.mqtt.BaseMessage;
import cn.com.startai.qxsdk.channel.mqtt.task.OnMqttTaskCallBack;
import cn.com.startai.qxsdk.global.QXError;
import cn.com.startai.qxsdk.utils.QXJsonUtils;
import cn.com.startai.qxsdk.utils.QXLog;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * 删除好友或设备
 * Created by Robin on 2018/5/10.
 * qq: 419109715 彬影
 */

public class UnBind {

    public static String MSG_DESC = "删除好友或设备 ";


    public static class Req {

        private String unbindingid;
        private String beunbindingid;

        public Req(String unbindingid, String beunbindingid) {
            this.unbindingid = unbindingid;
            this.beunbindingid = beunbindingid;
        }

        @Override
        public String toString() {
            return "ContentBean{" +
                    "unbindingid='" + unbindingid + '\'' +
                    ", beunbindingid='" + beunbindingid + '\'' +
                    '}';
        }

        public String getUnbindingid() {
            return unbindingid;
        }

        public void setUnbindingid(String unbindingid) {
            this.unbindingid = unbindingid;
        }

        public String getBeunbindingid() {
            return beunbindingid;
        }

        public void setBeunbindingid(String beunbindingid) {
            this.beunbindingid = beunbindingid;
        }


    }


    public static class Resp extends BaseMessage {


        private ContentBean content;

        public ContentBean getContent() {
            return content;
        }

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

        public void setContent(ContentBean content) {
            this.content = content;
        }

        public static class ContentBean extends QXError {


            /**
             * unbindingid :
             * beunbindingid :
             */

            private String unbindingid;
            private String beunbindingid;
            private Req errcontent;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", unbindingid='" + unbindingid + '\'' +
                        ", beunbindingid='" + beunbindingid + '\'' +
                        ", errcontent=" + errcontent +
                        '}';
            }

            public Req getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req errcontent) {
                this.errcontent = errcontent;
            }

            public String getUnbindingid() {
                return unbindingid;
            }

            public void setUnbindingid(String unbindingid) {
                this.unbindingid = unbindingid;
            }

            public String getBeunbindingid() {
                return beunbindingid;
            }

            public void setBeunbindingid(String beunbindingid) {
                this.beunbindingid = beunbindingid;
            }
        }
    }

    /**
     * 解绑结果
     *
     * @param miof
     */
    public static void handlerMsg(String miof, OnMqttTaskCallBack callBack) {

        Resp resp = QXJsonUtils.fromJson(miof, Resp.class);
        if (resp == null) {
            QXLog.e(TAG, "返回数据格式错误");
            return;
        }
        int result = resp.getResult();

        if (result == 1) {
            QXLog.e(TAG, "解绑成功");

        } else {

            Resp.ContentBean content = resp.getContent();
            content.setUnbindingid(resp.getContent().getErrcontent().getUnbindingid());
            content.setBeunbindingid(resp.getContent().getErrcontent().getBeunbindingid());
            if ("0x800403".equals(resp.getContent().getErrcode())) {
                resp.setResult(1);
                QXLog.e(TAG, "解绑成功");
            } else {

                QXLog.e(TAG, "解绑失败 " + resp.getContent().getErrmsg());
            }

        }

        callBack.onUnBindResult(resp);


    }
}
