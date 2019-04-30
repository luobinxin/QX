package cn.com.startai.qxsdk.busi.charger.entity;

import java.io.Serializable;

import cn.com.startai.qxsdk.busi.charger.IChargerBusiListener;
import cn.com.startai.qxsdk.channel.mqtt.BaseMessage;
import cn.com.startai.qxsdk.global.QXError;
import cn.com.startai.qxsdk.utils.QXJsonUtils;
import cn.com.startai.qxsdk.utils.QXLog;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * 借|还 充电宝
 * Created by Robin on 2018/8/22.
 * qq: 419109715 彬影
 */

public class BorrowOrGiveBack implements Serializable {

    public static final String MIOF_DESC = "借|还 充电宝";

    public static final String TYPE_LENT = "LENT";
    public static final String TYPE_RETURN = "RETURN";


    public static class Req {
        /**
         * userId : 030c7711705e7f3e1814283462703fcf
         * imei : 1000000089
         */

        private String userId;
        private String imei;

        public Req(String imei) {
            this.imei = imei;
        }

        @Override
        public String toString() {
            return "ContentBean{" +
                    "userId='" + userId + '\'' +
                    ", imei='" + imei + '\'' +
                    '}';
        }


        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
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
             * userId : 030c7711705e7f3e1814283462703fcf
             * imei : 1000000089
             * no : Lend849jfjs9e9gjrjskdoh
             * lentTime : 2018-01-01 09:00:00
             * lentMerchantName : 常德人家
             * returnTime:2018-01-01 11:00:00
             * returnMerchantName:常德人家
             * fee:1.05
             */

            private String userId;
            private String imei;
            private String type;
            private String no;
            private String lentTime;
            private String lentMerchantName;
            private String returnTime;
            private String returnMerchantName;
            private String fee;

            private Req errcontent;

            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", userId='" + userId + '\'' +
                        ", imei='" + imei + '\'' +
                        ", type='" + type + '\'' +
                        ", no='" + no + '\'' +
                        ", lentTime='" + lentTime + '\'' +
                        ", lentMerchantName='" + lentMerchantName + '\'' +
                        ", returnTime='" + returnTime + '\'' +
                        ", returnMerchantName='" + returnMerchantName + '\'' +
                        ", fee='" + fee + '\'' +
                        ", errcontent=" + errcontent +
                        '}';
            }

            public Req getErrcontent() {
                return errcontent;
            }

            public void setErrcontent(Req errcontent) {
                this.errcontent = errcontent;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getReturnTime() {
                return returnTime;
            }

            public void setReturnTime(String returnTime) {
                this.returnTime = returnTime;
            }

            public String getReturnMerchantName() {
                return returnMerchantName;
            }

            public void setReturnMerchantName(String returnMerchantName) {
                this.returnMerchantName = returnMerchantName;
            }

            public String getFee() {
                return fee;
            }

            public void setFee(String fee) {
                this.fee = fee;
            }


            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getImei() {
                return imei;
            }

            public void setImei(String imei) {
                this.imei = imei;
            }

            public String getNo() {
                return no;
            }

            public void setNo(String no) {
                this.no = no;
            }

            public String getLentTime() {
                return lentTime;
            }

            public void setLentTime(String lentTime) {
                this.lentTime = lentTime;
            }

            public String getLentMerchantName() {
                return lentMerchantName;
            }

            public void setLentMerchantName(String lentMerchantName) {
                this.lentMerchantName = lentMerchantName;
            }
        }
    }


    /**
     * 请求 借|还 充电宝 返回结果
     *
     * @param miof
     */
    public static void handlerMsg(String miof, IChargerBusiListener callback) {
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
            content.setImei(errcontent.getImei());
            content.setUserId(errcontent.getUserId());
            QXLog.d(TAG, MIOF_DESC + "失败");
        }
        if (callback != null) {
            callback.onBorrowOrGiveBackResult(resp);
        }

    }

}
