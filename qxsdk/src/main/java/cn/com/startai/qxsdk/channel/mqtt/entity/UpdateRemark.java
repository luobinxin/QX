package cn.com.startai.qxsdk.channel.mqtt.entity;


import cn.com.startai.qxsdk.channel.mqtt.BaseMessage;
import cn.com.startai.qxsdk.channel.mqtt.task.OnMqttTaskCallBack;
import cn.com.startai.qxsdk.global.QXError;
import cn.com.startai.qxsdk.utils.QXJsonUtils;
import cn.com.startai.qxsdk.utils.QXLog;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * 修改备注名
 * Created by Robin on 2018/7/19.
 * qq: 419109715 彬影
 */

public class UpdateRemark {


    public static class Req {


        /**
         * id :
         * fid :
         * remark :
         */

        private String id;
        private String fid;
        private String remark;

        public Req(String fid, String remark) {
            this.fid = fid;
            this.remark = remark;
        }

        @Override
        public String toString() {
            return "ContentBean{" +
                    "id='" + id + '\'' +
                    ", fid='" + fid + '\'' +
                    ", remark='" + remark + '\'' +
                    '}';
        }


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFid() {
            return fid;
        }

        public void setFid(String fid) {
            this.fid = fid;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }


    }

    public static class Resp extends BaseMessage {


        private ContentBean content;

        public ContentBean getContent() {
            return content;
        }

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

        public void setContent(ContentBean content) {
            this.content = content;
        }


        public static class ContentBean extends QXError {


            /**
             * id :
             * fid :
             * remark :
             */

            private String id;
            private String fid;
            private String remark;
            private Req errcontent;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", id='" + id + '\'' +
                        ", fid='" + fid + '\'' +
                        ", remark='" + remark + '\'' +
                        ", errcontent=" + errcontent +
                        '}';
            }

            public Req getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req errcontent) {
                this.errcontent = errcontent;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getFid() {
                return fid;
            }

            public void setFid(String fid) {
                this.fid = fid;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }
        }


    }

    /**
     * 修改备注名
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

            QXLog.e(TAG, "修改备注名成功");
        } else {

            Resp.ContentBean content = resp.getContent();
            Req errcontent = content.getErrcontent();
            content.setId(errcontent.getId());
            content.setFid(errcontent.getFid());
            content.setRemark(errcontent.getRemark());

            QXLog.e(TAG, "修改备注名失败");
        }

        callBack.onUpdateRemarkResult(resp);

    }

}
