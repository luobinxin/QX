package cn.com.startai.qxsdk.busi.charger.entity;


import java.io.Serializable;

import cn.com.startai.qxsdk.busi.charger.IChargerBusiListener;
import cn.com.startai.qxsdk.channel.mqtt.BaseMessage;
import cn.com.startai.qxsdk.global.QXError;
import cn.com.startai.qxsdk.utils.QXJsonUtils;
import cn.com.startai.qxsdk.utils.QXLog;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * 查询用户是否存在借出单
 * Created by Robin on 2018/8/22.
 * qq: 419109715 彬影
 */

public class GetUserChargingStatus implements Serializable {

    public static final String MIOF_DESC = "查询用户是否存在借出单";

    public static final String TYPE_RETURN = "RETURN";
    public static final String TYPE_LENT = "LENT";


    /**
     * 查询用户是否存在借出单 返回结果
     *
     * @param miof
     */
    public static void handlerMsg(String miof, IChargerBusiListener listener) {


        Resp resp = QXJsonUtils.fromJson(miof, Resp.class);
        if (resp == null) {
            QXLog.e(TAG, "返回数据格式错误");
            return;
        }
        if (resp.getResult() == 1) {

            QXLog.d(TAG, MIOF_DESC + "成功");
        } else {

            Resp.ContentBean content = resp.getContent();
            Req errcontent = content.getErrcontent();
            content.setUserId(errcontent.getUserId());

            QXLog.d(TAG, MIOF_DESC + "失败");
        }

        listener.onGetUserChargingStatusResult(resp);

    }


    public static class Req {

        /*userId :
         */

        private String userId;

        @Override
        public String toString() {
            return "ContentBean{}";
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
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


            private Req errcontent;


            /**
             * userId :
             * type : LENT
             */

            private String userId;
            private String type;

            public boolean isCharging() {
                return TYPE_RETURN.equals(type);
            }

            public ContentBean() {
            }

            public ContentBean(String userId, String type) {
                this.userId = userId;
                this.type = type;
            }

            public ContentBean(Req errcontent, String userId, String type) {
                this.errcontent = errcontent;
                this.userId = userId;
                this.type = type;
            }

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", errcontent=" + errcontent +
                        ", userId='" + userId + '\'' +
                        ", type='" + type + '\'' +
                        '}';
            }

            public Req getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req errcontent) {
                this.errcontent = errcontent;
            }

            public String getUserId() {

                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }
}


