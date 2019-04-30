package cn.com.startai.qxsdk.channel.mqtt.entity;

import cn.com.startai.qxsdk.channel.mqtt.BaseMessage;
import cn.com.startai.qxsdk.channel.mqtt.task.OnMqttTaskCallBack;
import cn.com.startai.qxsdk.global.QXError;
import cn.com.startai.qxsdk.utils.QXJsonUtils;
import cn.com.startai.qxsdk.utils.QXLog;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * 注册
 * Created by Robin on 2018/6/14.
 * qq: 419109715 彬影
 */

public class Register {

    public static String MSG_DESC = "注册 ";

    public static final int TYPE_EMAIL_ACTIVATE_EMAIL = 1; //发送激活邮件注册
    public static final int TYPE_MOBILE_AFTER_CHECK_CODE = 2; //检验手机验证码注册
    public static final int TYPE_MOBILE_CODE_FAST_LOGIN = 3; //暂时用不上
    public static final int TYPE_EMAIL_AFTER_CHECK_CODE = 4;//检验邮箱验证码注册





    public static class Req {

        private String uname;
        private String pwd;
        private int type;

        public Req(String uname, String pwd, int type) {
            this.uname = uname;
            this.pwd = pwd;
            this.type = type;
        }

        @Override
        public String toString() {
            return "Req{" +
                    "uname='" + uname + '\'' +
                    ", pwd='" + pwd + '\'' +
                    ", type=" + type +
                    '}';
        }

        public String getUname() {
            return uname;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }

        public String getPwd() {
            return pwd;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
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

        public Resp(ContentBean content) {
            this.content = content;
        }

        public ContentBean getContent() {
            return content;
        }

        public void setContent(ContentBean content) {
            this.content = content;
        }

        public static class ContentBean extends QXError {

            private String uname;
            private String pwd;
            private int type;
            private Req errcontent;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", uname='" + uname + '\'' +
                        ", pwd='" + pwd + '\'' +
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

            public ContentBean(String uname, String pwd, int type) {
                this.uname = uname;
                this.pwd = pwd;
                this.type = type;
            }

            public String getUname() {
                return uname;
            }

            public void setUname(String uname) {
                this.uname = uname;
            }

            public String getPwd() {
                return pwd;
            }

            public void setPwd(String pwd) {
                this.pwd = pwd;
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
     * 用户注册返回
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
            QXLog.e(TAG, "用户注册成功");
        } else {

            Resp.ContentBean content = resp.getContent();
            Req errcontent = content.getErrcontent();
            content.setType(errcontent.getType());
            content.setUname(errcontent.getUname());
            content.setPwd(errcontent.getPwd());

            QXLog.e(TAG, MSG_DESC + " 失败 " + resp.getContent().getErrmsg());
        }
        callBack.onRegisterResult(resp);

    }

}
