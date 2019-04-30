package cn.com.startai.qxsdk.channel.mqtt.entity;


import java.io.Serializable;

import cn.com.startai.qxsdk.channel.mqtt.BaseMessage;
import cn.com.startai.qxsdk.channel.mqtt.task.OnMqttTaskCallBack;
import cn.com.startai.qxsdk.global.QXError;
import cn.com.startai.qxsdk.utils.QXJsonUtils;
import cn.com.startai.qxsdk.utils.QXLog;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * 修改密码
 * Created by Robin on 2018/8/3.
 * qq: 419109715 彬影
 */

public class UpdateLoginPwd implements Serializable {

    public static String MSG_DESC = "修改密码 ";


    public static class Req {

        public Req(String oldPwd, String newPwd) {
            this.oldPwd = oldPwd;
            this.newPwd = newPwd;
        }

        public Req(String newPwd) {
            this.newPwd = newPwd;
        }

        private String userid = null; //用户id
        private String oldPwd = null; //老密码
        private String newPwd = null; //新密码

        @Override
        public String toString() {
            return "ContentBean{" +
                    "userid='" + userid + '\'' +
                    ", oldPwd='" + oldPwd + '\'' +
                    ", newPwd='" + newPwd + '\'' +
                    '}';
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getOldPwd() {
            return oldPwd;
        }

        public void setOldPwd(String oldPwd) {
            this.oldPwd = oldPwd;
        }

        public String getNewPwd() {
            return newPwd;
        }

        public void setNewPwd(String newPwd) {
            this.newPwd = newPwd;
        }


    }


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


            private String userid = null; //用户id
            private String oldPwd = null; //老密码
            private String newPwd = null; //新密码
            private Req errcontent;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", userid='" + userid + '\'' +
                        ", oldPwd='" + oldPwd + '\'' +
                        ", newPwd='" + newPwd + '\'' +
                        ", errcontent=" + errcontent +
                        '}';
            }

            public Req getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req errcontent) {
                this.errcontent = errcontent;
            }

            public ContentBean(String userid, String oldPwd, String newPwd) {
                this.userid = userid;
                this.oldPwd = oldPwd;
                this.newPwd = newPwd;
            }

            public String getUserid() {
                return userid;
            }

            public void setUserid(String userid) {
                this.userid = userid;
            }

            public String getOldPwd() {
                return oldPwd;
            }

            public void setOldPwd(String oldPwd) {
                this.oldPwd = oldPwd;
            }

            public String getNewPwd() {
                return newPwd;
            }

            public void setNewPwd(String newPwd) {
                this.newPwd = newPwd;
            }
        }
    }

    /**
     * 请求修改密码返回结果
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
            QXLog.e(TAG, "修改密码成功");
        } else {

            Resp.ContentBean content = resp.getContent();
            Req errcontent = content.getErrcontent();
            content.setNewPwd(errcontent.getNewPwd());
            content.setOldPwd(errcontent.getOldPwd());
            content.setUserid(errcontent.getUserid());

            QXLog.e(TAG, MSG_DESC + " 失败 " + resp.getContent().getErrmsg());
        }
        callBack.onUpdateLoginPwdResult(resp);

    }
}
