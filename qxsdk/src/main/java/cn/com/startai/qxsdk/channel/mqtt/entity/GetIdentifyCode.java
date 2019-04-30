package cn.com.startai.qxsdk.channel.mqtt.entity;


import cn.com.startai.qxsdk.channel.mqtt.BaseMessage;
import cn.com.startai.qxsdk.channel.mqtt.task.OnMqttTaskCallBack;
import cn.com.startai.qxsdk.global.QXError;
import cn.com.startai.qxsdk.utils.QXJsonUtils;
import cn.com.startai.qxsdk.utils.QXLog;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * 获取验证码
 * Created by Robin on 2018/6/14.
 * qq: 419109715 彬影
 */

public class GetIdentifyCode {

    public static String MSG_DESC = "获取验证码 ";

    /*
    1表示用户登录(快捷登录)
2表示忘记密码(重置密码)
3表示用户注册
4第三方音响快捷登录
5绑定/更改手机号
     */
    public static final int TYPE_FAST_LOGIN = 1;
    public static final int TYPE_RESET_PWD = 2;
    public static final int TYPE_REGISTER = 3;
    public static final int TYPE_THIRD_MUSICBOX_LOGIN = 4;
    public static final int TYPE_BIND_MOBILENUM = 5;


    public static class Req {

        private String mobile;
        /**
         * 1表示用户登录
         * 2表示修改登录密码
         * 3表示用户注册
         */
        private int type;

        public Req(String mobile, int type) {
            this.mobile = mobile;
            this.type = type;
        }

        @Override
        public String toString() {
            return "ContentBean{" +
                    "mobile='" + mobile + '\'' +
                    ", type=" + type +
                    '}';
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }


    }


    /**
     * 获取验证码 返回
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

            private String mobile;
            /**
             * 1表示用户登录
             * 2表示修改登录密码
             * 3表示用户注册
             */
            private int type;
            private Req errcontent;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", mobile='" + mobile + '\'' +
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

            public ContentBean(String mobile, int type) {
                this.mobile = mobile;
                this.type = type;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }
    }

    /**
     * 获取验证码
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

            QXLog.e(TAG, "获取验证码成功");
        } else {

            Resp.ContentBean content = resp.getContent();
            Req errcontent = content.getErrcontent();
            content.setType(errcontent.getType());
            content.setMobile(errcontent.getMobile());

            QXLog.e(TAG, MSG_DESC + " 失败 " + resp.getContent().getErrmsg());
        }

        callBack.onGetIdentifyCodeResult(resp);

    }
}
