package cn.com.startai.qxsdk.busi.charger.entity;

import java.io.Serializable;

import cn.com.startai.qxsdk.busi.charger.IChargerBusiListener;
import cn.com.startai.qxsdk.channel.mqtt.BaseMessage;
import cn.com.startai.qxsdk.global.QXError;
import cn.com.startai.qxsdk.utils.QXJsonUtils;
import cn.com.startai.qxsdk.utils.QXLog;

import static cn.com.startai.qxsdk.QX.TAG;

/**
 * 查询机柜费率
 * Created by Robin on 2018/8/22.
 * qq: 419109715 彬影
 */

public class GetChargerFee implements Serializable {

    public static final String MIOF_DESC = "查询机柜费率";


    public static final int CHARGINGTYPE_FREE = 0;
    public static final int CHARGINGTYPE_UNIT = 1;
    public static final int CHARGINGTYPE_FIXED = 2;


    /**
     * 查询机柜费率 返回结果
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
            content.setImei(errcontent.getImei());

            QXLog.d(TAG, MIOF_DESC + "失败");
        }

        listener.onGetChargerFeeResult(resp);

    }


    public static class Req {

        /**
         * imei :
         */

        private String imei;

        public Req(String imei) {
            this.imei = imei;
        }

        @Override
        public String toString() {
            return "ContentBean{" +
                    "imei='" + imei + '\'' +
                    '}';
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


            private Req errcontent;


            /**
             * imei :
             * policyName : 1元/小时
             * money : 100
             * chargingType : 1
             * moneyMax : 500
             * duration : 0
             * count : 1
             */

            private String imei;
            private String policyName;
            private int money;
            private int chargingType;
            private int moneyMax;
            private int duration;
            private int count;


            @Override
            public String toString() {
                return "ContentBean{" +
                        "errcode='" + errcode + '\'' +
                        ", errmsg='" + errmsg + '\'' +
                        ", errcontent=" + errcontent +
                        ", imei='" + imei + '\'' +
                        ", policyName='" + policyName + '\'' +
                        ", money=" + money +
                        ", chargingType=" + chargingType +
                        ", moneyMax=" + moneyMax +
                        ", duration=" + duration +
                        ", count=" + count +
                        '}';
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

            public String getPolicyName() {
                return policyName;
            }

            public void setPolicyName(String policyName) {
                this.policyName = policyName;
            }

            public int getMoney() {
                return money;
            }

            public void setMoney(int money) {
                this.money = money;
            }

            public int getChargingType() {
                return chargingType;
            }

            public void setChargingType(int chargingType) {
                this.chargingType = chargingType;
            }

            public int getMoneyMax() {
                return moneyMax;
            }

            public void setMoneyMax(int moneyMax) {
                this.moneyMax = moneyMax;
            }

            public int getDuration() {
                return duration;
            }

            public void setDuration(int duration) {
                this.duration = duration;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }
        }
    }
}


