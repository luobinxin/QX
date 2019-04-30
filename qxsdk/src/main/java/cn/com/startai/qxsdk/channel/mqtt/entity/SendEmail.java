package cn.com.startai.qxsdk.channel.mqtt.entity;


import cn.com.startai.qxsdk.channel.mqtt.BaseMessage;
import cn.com.startai.qxsdk.channel.mqtt.task.OnMqttTaskCallBack;
import cn.com.startai.qxsdk.global.QXError;
import cn.com.startai.qxsdk.utils.QXJsonUtils;
import cn.com.startai.qxsdk.utils.QXLog;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * 请求发送邮件
 * Created by Robin on 2018/8/4.
 * qq: 419109715 彬影
 */

public class SendEmail {

    public static String MSG_DESC = "请求发送邮件 ";


    /*
    1 为重新发送激活邮件
2 为发送忘记密码邮件
6 为发送注册验证码邮件
7 为发送重置密码验证码邮件
8 为发送更换/添加绑定邮箱验证码邮件

     */

    public static final int TYPE_LINK_TO_RE_ACTIVATE = 1;
    public static final int TYPE_LINK_TO_RESET_PWD = 2;
    public static final int TYPE_CODE_TO_REGISTER = 6;
    public static final int TYPE_CODE_TO_RESET_PWD = 7;
    public static final int TYPE_CODE_TO_BIND_EMAIL = 8;


    /**
     * 请求发送邮件结果
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


            QXLog.e(TAG, "请求发送邮件成功");
        } else {
            Resp.ContentBean content = resp.getContent();
            Req errcontent = content.getErrcontent();
            content.setType(errcontent.getType());
            content.setEmail(errcontent.getEmail());

            QXLog.e(TAG, MSG_DESC + " 失败 " + resp.getContent().getErrmsg());
        }
        callBack.onSendEmail(resp);

    }

    /**
     * 请求
     */
    public static class Req {

        private String email;
        private int type;//1 为重新发送激活邮件 2 为发送忘记密码邮件

        public Req(String email, int type) {
            this.email = email;
            this.type = type;
        }

        @Override
        public String toString() {
            return "ContentBean{" +
                    "email='" + email + '\'' +
                    ", type=" + type +
                    '}';
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
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

            private String email;
            private int type;//1 为重新发送激活邮件 2 为发送忘记密码邮件
            private Req errcontent;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", email='" + email + '\'' +
                        ", type=" + type +
                        ", errcontent=" + errcontent +
                        '}';
            }

            public Req getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req errcontent) {
                this.errcontent = errcontent;
            }

            public ContentBean() {
            }

            public ContentBean(String email, int type) {
                this.email = email;
                this.type = type;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }
    }


}
