package cn.com.startai.qxsdk.busi.charger.entity;


import java.io.Serializable;

import cn.com.startai.qxsdk.busi.charger.IChargerBusiListener;
import cn.com.startai.qxsdk.channel.mqtt.BaseMessage;
import cn.com.startai.qxsdk.global.QXError;
import cn.com.startai.qxsdk.utils.QXJsonUtils;
import cn.com.startai.qxsdk.utils.QXLog;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * 查询机柜信息
 * Created by Robin on 2018/8/22.
 * qq: 419109715 彬影
 */

public class GetChargerInfo implements Serializable {

    public static final String MIOF_DESC = "查询机柜信息";


    /**
     * 请求 查询机柜信息 返回结果
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


            QXLog.d(TAG, MIOF_DESC + " 成功");
        } else {
            Resp.ContentBean content = resp.getContent();
            Req errcontent = content.getErrcontent();
            content.setImei(errcontent.getImei());

            QXLog.d(TAG, MIOF_DESC + " 失败");
        }
        listener.onGetChargerInfoResult(resp);

    }

    public static class Req {
        /**
         * imei : 1000000009
         */

        private String imei;

        public Req(String imei) {
            this.imei = imei;
        }

        public String getImei() {
            return imei;
        }

        public void setImei(String imei) {
            this.imei = imei;
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


            /**
             * imei : 1000000009
             * merchantId : 114
             * fullyCount : 1
             * noFullyCount : 0
             * errorCount : 0
             * emptyCount : 2
             * isOnline : false
             */

            private String imei;
            private String merchantId;
            private int fullyCount;
            private int noFullyCount;
            private int errorCount;
            private int emptyCount;
            private boolean isOnline;
            private Req errcontent;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", imei='" + imei + '\'' +
                        ", merchantId=" + merchantId +
                        ", fullyCount=" + fullyCount +
                        ", noFullyCount=" + noFullyCount +
                        ", errorCount=" + errorCount +
                        ", emptyCount=" + emptyCount +
                        ", isOnline=" + isOnline +
                        ", errcontent=" + errcontent +
                        '}';
            }

            public boolean isOnline() {

                return isOnline;
            }

            public void setOnline(boolean online) {
                isOnline = online;
            }

            public Req getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req errcontent) {
                this.errcontent = errcontent;
            }

            public String getImei() {
                return imei;
            }

            public void setImei(String imei) {
                this.imei = imei;
            }

            public String getMerchantId() {
                return merchantId;
            }

            public void setMerchantId(String merchantId) {
                this.merchantId = merchantId;
            }

            public int getFullyCount() {
                return fullyCount;
            }

            public void setFullyCount(int fullyCount) {
                this.fullyCount = fullyCount;
            }

            public int getNoFullyCount() {
                return noFullyCount;
            }

            public void setNoFullyCount(int noFullyCount) {
                this.noFullyCount = noFullyCount;
            }

            public int getErrorCount() {
                return errorCount;
            }

            public void setErrorCount(int errorCount) {
                this.errorCount = errorCount;
            }

            public int getEmptyCount() {
                return emptyCount;
            }

            public void setEmptyCount(int emptyCount) {
                this.emptyCount = emptyCount;
            }

            public boolean isIsOnline() {
                return isOnline;
            }

            public void setIsOnline(boolean isOnline) {
                this.isOnline = isOnline;
            }
        }
    }


}
