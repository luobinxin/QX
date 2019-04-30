package cn.com.startai.qxsdk.channel.mqtt.entity;


import cn.com.startai.qxsdk.channel.mqtt.BaseMessage;
import cn.com.startai.qxsdk.channel.mqtt.task.OnMqttTaskCallBack;
import cn.com.startai.qxsdk.global.QXError;
import cn.com.startai.qxsdk.utils.QXJsonUtils;
import cn.com.startai.qxsdk.utils.QXLog;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * 检验验证码
 * Created by Robin on 2018/6/14.
 * qq: 419109715 彬影
 */

public class CheckIdentifyCode {

    public static String MSG_DESC = "检验验证码 ";


    public static final int TYPE_MOBILE_LOGIN = 1;
    public static final int TYPE_MOBILE_RESET_PWD = 2;
    public static final int TYPE_MOBILE_REGISTER = 3;
    public static final int TYPE_MOBILE_THIRD_MUBICBOX_LOGIN = 4;
    public static final int TYPE_MOBILE_BIND_MOBILENUM = 5;
    public static final int TYPE_EMAIL_REGISTER = 6;
    public static final int TYPE_EMAIL_RESET_PWD = 7;
    public static final int TYPE_EMAIL_BIND_EMAILNUM = 8;


    public static class Req {

        private String account;
        private String identifyCode;
        private int type;//1表示用户登录2表示修改登录密码3表示用户注册

        public Req(String account, String identifyCode, int type) {
            this.account = account;
            this.identifyCode = identifyCode;
            this.type = type;
        }

        @Override
        public String toString() {
            return "ContentBean{" +
                    ", identifyCode='" + identifyCode + '\'' +
                    ", type=" + type +
                    ", account='" + account + '\'' +
                    '}';
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }


        public String getIdentifyCode() {
            return identifyCode;
        }

        public void setIdentifyCode(String identifyCode) {
            this.identifyCode = identifyCode;
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

            /**
             * mobile : 13332965499
             * identifyCode : 123456
             * type : 1
             */


            private String mobile;
            private String identifyCode;
            private int type;
            private String account;

            private Req errcontent;

            public ContentBean(String mobile, String identifyCode, int type, String account) {
                this.mobile = mobile;
                this.identifyCode = identifyCode;
                this.type = type;
                this.account = account;
            }

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", mobile='" + mobile + '\'' +
                        ", identifyCode='" + identifyCode + '\'' +
                        ", type=" + type +
                        ", account='" + account + '\'' +
                        ", errcontent=" + errcontent +
                        '}';
            }

            public String getAccount() {
                return account;
            }

            public void setAccount(String account) {
                this.account = account;
            }

            public Req getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req errcontent) {
                this.errcontent = errcontent;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getIdentifyCode() {
                return identifyCode;
            }

            public void setIdentifyCode(String identifyCode) {
                this.identifyCode = identifyCode;
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
     * 检验验证码
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


            QXLog.e(TAG, "检验验证码成功");


            String mobile = resp.getContent().getMobile();

        } else {
            Resp.ContentBean content = resp.getContent();
            Req req = content.getErrcontent();
            content.setType(req.getType());
            content.setIdentifyCode(req.getIdentifyCode());
            content.setAccount(req.getAccount());

            QXLog.e(TAG, MSG_DESC + " 失败 " + resp.getContent().getErrmsg());
        }

        callBack.onCheckIdentifyCodeResult(resp);

    }

}
