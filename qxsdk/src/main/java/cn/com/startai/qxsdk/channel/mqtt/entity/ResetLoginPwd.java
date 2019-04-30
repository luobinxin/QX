package cn.com.startai.qxsdk.channel.mqtt.entity;

import java.io.Serializable;

import cn.com.startai.qxsdk.channel.mqtt.BaseMessage;
import cn.com.startai.qxsdk.channel.mqtt.task.OnMqttTaskCallBack;
import cn.com.startai.qxsdk.global.QXError;
import cn.com.startai.qxsdk.utils.QXJsonUtils;
import cn.com.startai.qxsdk.utils.QXLog;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * 重置密码
 * <p>
 * Created by Robin on 2018/8/3.
 * qq: 419109715 彬影
 */

public class ResetLoginPwd implements Serializable {

    public static String MSG_DESC = "重置密码 ";


    public static class Req {


        private String account = null;
        private String pwd = null; //新密码

        public Req(String account, String pwd) {
            this.account = account;
            this.pwd = pwd;
        }

        @Override
        public String toString() {
            return "ContentBean{" +
                    ", pwd='" + pwd + '\'' +
                    ", account='" + account + '\'' +
                    '}';
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }


        public String getPwd() {
            return pwd;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
        }


    }


    /**
     * 修改密码
     */
    public static class Resp extends BaseMessage implements Serializable {

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


            private String pwd = null; //新密码

            private String account = null;


            private Req errcontent = null;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", pwd='" + pwd + '\'' +
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

            public ContentBean() {
            }


            public String getPwd() {
                return pwd;
            }

            public void setPwd(String pwd) {
                this.pwd = pwd;
            }
        }
    }

    /**
     * 重置登录密码 结果返回
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
            QXLog.e(TAG, "重置登录密码 成功");


        } else {
            Resp.ContentBean content = resp.getContent();
            Req errcontent = content.getErrcontent();
            content.setPwd(errcontent.getPwd());
            content.setAccount(errcontent.getAccount());

            QXLog.e(TAG, MSG_DESC + " 失败 " + resp.getContent().getErrmsg());
        }
        callBack.onResetLoginPwdResult(resp);

    }
}
