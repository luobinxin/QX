package cn.com.startai.qxsdk.channel.mqtt.entity;


import android.text.TextUtils;

import cn.com.startai.qxsdk.channel.mqtt.BaseMessage;
import cn.com.startai.qxsdk.channel.mqtt.task.OnMqttTaskCallBack;
import cn.com.startai.qxsdk.QXUserManager;
import cn.com.startai.qxsdk.utils.QXJsonUtils;
import cn.com.startai.qxsdk.utils.QXLog;

import static cn.com.startai.qxsdk.QX.TAG;

public class Passthrough {
    public static final String MSGTYPE = "0x8200";

    public Passthrough() {
    }


    public static void handlerMsg(String topic, String miof, OnMqttTaskCallBack callBack) {

        Passthrough.Resp resp = QXJsonUtils.fromJson(miof, Passthrough.Resp.class);
        if (resp == null) {
            QXLog.e(TAG, "返回数据格式错误");
        } else {
            if (TextUtils.isEmpty(resp.getFromid()) && topic.contains("-A")) {
                String[] aar = topic.split("/");
                String sn = aar[aar.length - 1].replace("-A", "");
                resp.setFromid(sn);
                String userId = QXUserManager.getInstance().getUserId();
                if (!TextUtils.isEmpty(userId)) {
                    resp.setToid(userId);
                }
            }

            QXLog.e(TAG, "透传消息");


            callBack.onPassthrough(resp);
        }
    }

    public static class Resp extends BaseMessage {
        private String content;

        public String toString() {
            return "Resp{msgcw='" + this.msgcw + '\'' + ", msgtype='" + this.msgtype + '\'' + ", fromid='" + this.fromid + '\'' + ", toid='" + this.toid + '\'' + ", domain='" + this.domain + '\'' + ", appid='" + this.appid + '\'' + ", ts=" + this.ts + ", msgid='" + this.msgid + '\'' + ", m_ver='" + this.m_ver + '\'' + ", result=" + this.result + ", content='" + this.content + '\'' + '}';
        }

        public Resp(String content) {
            this.content = content;
        }

        public String getContent() {
            return this.content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    public static class Req {
        private String content;

        public Req(String content) {
            this.content = content;
        }

        public String getContent() {
            return this.content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
