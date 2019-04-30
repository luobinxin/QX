package cn.com.startai.qxsdk.channel.mqtt.entity;


import java.io.Serializable;

import cn.com.startai.qxsdk.channel.mqtt.BaseMessage;
import cn.com.startai.qxsdk.channel.mqtt.task.OnMqttTaskCallBack;
import cn.com.startai.qxsdk.global.QXError;
import cn.com.startai.qxsdk.utils.QXJsonUtils;
import cn.com.startai.qxsdk.utils.QXLog;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * 绑定邮箱
 * Created by Robin on 2019/2/13.
 * qq: 419109715 彬影
 */

public class BindEmail implements Serializable {

    public static String MSG_DESC = "绑定邮箱 ";


    public static class Req {

        /**
         * userid :
         * email :
         */

        private String userid;
        private String email;

        public Req() {
        }

        public Req(String email) {
            this.email = email;
        }

        public Req(String userid, String email) {
            this.userid = userid;
            this.email = email;
        }

        @Override
        public String toString() {
            return "ContentBean{" +
                    "userid='" + userid + '\'' +
                    ", email='" + email + '\'' +
                    '}';
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
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


            /**
             * userid :
             * email :
             */

            private String userid;
            private String email;

            public ContentBean() {
            }

            public ContentBean(Req errcontent, String userid, String email) {
                this.errcontent = errcontent;
                this.userid = userid;
                this.email = email;
            }

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", errcontent=" + errcontent +
                        ", userid='" + userid + '\'' +
                        ", email='" + email + '\'' +
                        '}';
            }

            public Req getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req errcontent) {
                this.errcontent = errcontent;
            }

            public String getUserid() {
                return userid;
            }

            public void setUserid(String userid) {
                this.userid = userid;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }
        }
    }

    /**
     * 请求 绑定邮箱 返回结果
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
            BindEmail.Resp.ContentBean content = resp.getContent();
            BindEmail.Req errcontent = content.getErrcontent();
            content.setEmail(errcontent.getEmail());
            content.setUserid(errcontent.getUserid());
            QXLog.e(TAG, MSG_DESC + " 失败 " + resp.getContent().getErrmsg());
        }

        callBack.onBindEmailResult(resp);

    }
}
