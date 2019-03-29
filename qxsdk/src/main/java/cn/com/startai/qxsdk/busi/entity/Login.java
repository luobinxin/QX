package cn.com.startai.qxsdk.busi.entity;

import android.os.HandlerThread;

import cn.com.startai.qxsdk.connect.mqtt.BaseMessage;
import cn.com.startai.qxsdk.connect.mqtt.task.OnMqttTaskCallBack;
import cn.com.startai.qxsdk.global.QXError;
import cn.com.startai.qxsdk.utils.QXJsonUtils;
import cn.com.startai.qxsdk.utils.QXLog;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * Created by Robin on 2019/3/26.
 * 419109715@qq.com 彬影
 */
public class Login {

    public static final int TYPE_EMAIL_PWD = 1;
    public static final int TYPE_MOBILE_PWD = 2;
    public static final int TYPE_MOBILE_CODE = 3;
    public static final int TYPE_UNAME_PWD = 4;
    public static final int TYPE_MOBILE_CODE_PWD = 5;

    public static final int THIRD_WECHAT = 10;
    public static final int THIRD_ALIPAY = 11;
    public static final int THIRD_QQ = 12;
    public static final int THIRD_GOOGLE = 13;
    public static final int THIRD_TWITTER = 14;
    public static final int THIRD_AMAZON = 15;
    public static final int THIRD_FACEBOOK = 16;
    public static final int THIRD_MI = 17;
    public static final int THIRD_SMALLROUTINE = 18;


    public static class Req {

        private String uname;
        private String pwd;
        private String identifyCode;
        private int type;


        @Override
        public String toString() {
            return "LoginReq{" +
                    "uname='" + uname + '\'' +
                    ", pwd='" + pwd + '\'' +
                    ", identifyCode='" + identifyCode + '\'' +
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


    public static class Resp extends BaseMessage {


        private ContentBean content;

        @Override
        public String toString() {
            return "LoginResp{" +
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

        public class ContentBean extends QXError {

            private String userid;//用户id
            private String token; //用户token
            private long expire_in; //token时效
            private String uname;  // 用户名
            private int type;// 登录类型
            private Req errcontent;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "userid='" + userid + '\'' +
                        ", token='" + token + '\'' +
                        ", expire_in=" + expire_in +
                        ", uname='" + uname + '\'' +
                        ", type=" + type +
                        ", errcontent=" + errcontent +
                        '}';
            }

            public String getUserid() {
                return userid;
            }

            public void setUserid(String userid) {
                this.userid = userid;
            }

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }

            public long getExpire_in() {
                return expire_in;
            }

            public void setExpire_in(long expire_in) {
                this.expire_in = expire_in;
            }

            public String getUname() {
                return uname;
            }

            public void setUname(String uname) {
                this.uname = uname;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public Req getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req errcontent) {
                this.errcontent = errcontent;
            }
        }
    }


    public static void handlerMsg(String miof, OnMqttTaskCallBack callBack) {

        Login.Resp resp = QXJsonUtils.fromJson(miof, Login.Resp.class);
        QXLog.d(TAG, "resp = " + resp);
        if (resp == null) {
            QXLog.e(TAG, "返回数据格式错误");
            return;
        }

        Login.Resp.ContentBean content = resp.getContent();
        if (resp.getResult() == 1) {
            QXLog.e(TAG, "login success");


        } else {
            //登录失败
            Login.Req errcontent = content.getErrcontent();
            content.setType(errcontent.getType());
            content.setUname(errcontent.getUname());

            QXLog.e(TAG, "login failed " + content.getErrmsg());
        }

        callBack.onLoginResult(resp);
    }
}
